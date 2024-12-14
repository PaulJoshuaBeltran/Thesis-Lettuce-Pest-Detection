import torch
import torch.nn as nn
import torch.optim as optim
import numpy as np
import os
import re
import glob
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from torch.utils.data import DataLoader, TensorDataset
from datetime import datetime

IMG_WIDTH = 3096
IMG_HEIGHT = 4128

def extract_height_from_filename(filename):
    match = re.search(r'(\d+(?:pt\d+)?)cm\.txt$', filename)
    if match:
        height_str = match.group(1).replace('pt', '.')
        return float(height_str)
    return None

def extract_and_predict_height(model, scaler, label_file_path):
    filename = os.path.basename(label_file_path)
    actual_height = extract_height_from_filename(filename)
    
    with open(label_file_path, 'r') as file:
        line = file.readline()
        _, _, y_center, _, height = map(float, line.strip().split())
        top_y = yolo_to_top_y(y_center, height)
    
    predicted_height = predict_height(model, scaler, top_y)
    
    return predicted_height, actual_height, top_y

def yolo_to_top_y(y_center, height):
    return (y_center - height / 2) * IMG_HEIGHT

def prepare_dataset(label_dir):
    top_ys = []
    heights = []
    for filename in os.listdir(label_dir):
        actual_height = extract_height_from_filename(filename)
        if actual_height is not None:
            file_path = os.path.join(label_dir, filename)
            with open(file_path, 'r') as f:
                for line in f:
                    parts = line.strip().split()
                    if len(parts) == 5:
                        _, _, y_center, _, height = map(float, parts)
                        top_y = yolo_to_top_y(y_center, height)
                        top_ys.append(top_y)
                        heights.append(actual_height)
    return np.array(top_ys, dtype=np.float32).reshape(-1, 1), np.array(heights, dtype=np.float32).reshape(-1, 1)

class HeightPredictor(nn.Module):
    def __init__(self):
        super(HeightPredictor, self).__init__()
        self.fc = nn.Sequential(
            nn.Linear(1, 256),  
            nn.ReLU(),
            nn.Dropout(0.1),  
            nn.Linear(256, 128),  
            nn.ReLU(),
            nn.Dropout(0.1), 
            nn.Linear(128, 64), 
            nn.ReLU(),
            nn.Linear(64, 32),  
            nn.ReLU(),
            nn.Linear(32, 1)  
        )
    def forward(self, x):
        return self.fc(x)

def train_model(X, y, patience=10):
    X_train, X_temp, y_train, y_temp = train_test_split(X, y, test_size=0.4, random_state=42)
    X_val, X_test, y_val, y_test = train_test_split(X_temp, y_temp, test_size=0.5, random_state=42)
    
    scaler = StandardScaler().fit(X_train)
    X_train_scaled = scaler.transform(X_train)
    X_val_scaled = scaler.transform(X_val)
    X_test_scaled = scaler.transform(X_test)

    train_dataset = TensorDataset(torch.from_numpy(X_train_scaled), torch.from_numpy(y_train))
    val_dataset = TensorDataset(torch.from_numpy(X_val_scaled), torch.from_numpy(y_val))
    
    train_loader = DataLoader(train_dataset, batch_size=16, shuffle=True)
    val_loader = DataLoader(val_dataset, batch_size=16, shuffle=False)
    
    model = HeightPredictor()
    criterion = nn.MSELoss()
    optimizer = optim.Adam(model.parameters(), lr=0.001)
    
    best_val_loss = float('inf')
    patience_counter = 0
    
    epochs = 2500
    for epoch in range(epochs):
        model.train()
        for inputs, targets in train_loader:
            optimizer.zero_grad()
            outputs = model(inputs)
            loss = criterion(outputs, targets)
            loss.backward()
            optimizer.step()
        
        model.eval()
        val_loss = 0
        with torch.no_grad():
            for inputs, targets in val_loader:
                outputs = model(inputs)
                val_loss += criterion(outputs, targets).item()
        val_loss /= len(val_loader)
        
        #print(f'Epoch {epoch}/{epochs}, Loss: {loss.item()}, Val Loss: {val_loss}')
        
        if val_loss < best_val_loss:
            best_val_loss = val_loss
            patience_counter = 0

            torch.save(model.state_dict(), 'best_model.pth')
        else:
            patience_counter += 1
            if patience_counter >= patience:
                print("Early stopping triggered.")
                break
    
    model.load_state_dict(torch.load('best_model.pth'))
    
    return model, scaler

def predict_height(model, scaler, top_y):
    top_y_scaled = scaler.transform([[top_y]])
    with torch.no_grad():
        predicted_height = model(torch.from_numpy(top_y_scaled).float()).item()
    return predicted_height

def find_newest_exp_label_dir(base_path='C:/THESIS/Yolov7/yolov7-custom/runs/detect'):
    exp_dirs = glob.glob(os.path.join(base_path, 'exp*'))
    newest_exp_dir = max(exp_dirs, key=os.path.getctime)
    label_files_dir = os.path.join(newest_exp_dir, 'labels')
    return label_files_dir

label_dir = 'C:/THESIS/Yolov7/yolov7-custom/labels'
top_ys, heights = prepare_dataset(label_dir)
model, scaler = train_model(top_ys, heights)

label_file_path = 'C:/THESIS/Yolov7/yolov7-custom/labels'

newest_label_dir = find_newest_exp_label_dir()

label_files = glob.glob(os.path.join(newest_label_dir, '*.txt'))

for label_file_path in label_files:
    predicted_height, actual_height, top_y = extract_and_predict_height(model, scaler, label_file_path)

    with open('predicted_height.txt', 'w') as file:
        file.write(str(predicted_height))

    print("\n")
    print(f"Label File: {os.path.basename(label_file_path)}")
    print(f"Topmost Y-Value: {top_y:.2f} pixels")
    print(f"Actual Height: {actual_height} cm")
    print(f"Predicted Height: {predicted_height:.2f} cm")