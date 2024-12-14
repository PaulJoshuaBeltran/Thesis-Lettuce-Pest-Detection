import cv2
from roboflow import Roboflow
import os
import glob

# Initialize Roboflow model
rf = Roboflow(api_key="wgWaPQc6fw2R6JFJKupD")
project = rf.workspace().project("pest_v5")
model = project.version(1).model

def video_to_frames(video_path, frames_dir="frames", start_time=0, end_time=None):
    """
    Extracts frames from a specified segment of a video file and saves them as individual images.
    
    :param video_path: Path to the video file.
    :param frames_dir: Directory where frames will be saved.
    :param start_time: Start time in seconds for extracting frames.
    :param end_time: End time in seconds for extracting frames. If None, goes till the end of the video.
    """
    if not os.path.exists(frames_dir):
        os.makedirs(frames_dir)
        
    cap = cv2.VideoCapture(video_path)
    video_fps = cap.get(cv2.CAP_PROP_FPS)
    
    start_frame = int(start_time * video_fps)
    end_frame = int(end_time * video_fps) if end_time else int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    
    cap.set(cv2.CAP_PROP_POS_FRAMES, start_frame)
    
    count = 0
    frame_id = start_frame
    
    while frame_id < end_frame:
        ret, frame = cap.read()
        
        if not ret:
            break
        
        frame_path = os.path.join(frames_dir, f"frame_{count}.jpg")
        cv2.imwrite(frame_path, frame)
        count += 1
        frame_id += 1
    
    cap.release()
    print(f"Extracted {count} frames, saved to directory: {frames_dir}")

def predict_on_frames(frames_dir="frames", predictions_dir="predictions"):
    """
    Runs predictions on each frame saved in the specified directory, counts predictions, and calculates the average.
    
    :param frames_dir: Directory where frames are stored.
    :param predictions_dir: Directory where prediction images will be saved.
    """
    if not os.path.exists(predictions_dir):
        os.makedirs(predictions_dir)
        
    total_predictions = 0
    frames_count = 0
    
    frames = [os.path.join(frames_dir, f) for f in os.listdir(frames_dir) if f.endswith('.jpg')]
    
    for frame_path in frames:
        # Retrieve the prediction result as a JSON object
        prediction_result_json = model.predict(frame_path, confidence=40, overlap=30).json()
        prediction = model.predict(frame_path, confidence=40, overlap=30)
        predictions = prediction_result_json['predictions']
        num_predictions = len(predictions)
        
        # Process for saving prediction outputs remains unchanged
        # This example does not explicitly cover the visualization and saving of prediction results
        prediction_output_path = os.path.join(predictions_dir, os.path.basename(frame_path).replace('.jpg', '_prediction.jpg'))
        # You would save your frame with visualizations here if needed
        
        total_predictions += num_predictions
        frames_count += 1
        
        print(f"Frame: {os.path.basename(frame_path)}, Predictions: {num_predictions}")
        prediction.save(prediction_output_path)
    
    if frames_count > 0:
        average_predictions = total_predictions / frames_count
        print(f"Total number of predictions across all frames: {total_predictions}")
        print(f"Average number of predictions across all frames: {average_predictions}")
        with open('average_predictions.txt', 'w') as file:
        	file.write(str(average_predictions))
    else:
        print("No frames were processed.")

def create_video_from_images(images_dir, output_video_path, video_path):
    """
    Creates a video from a sequence of images stored in a directory, using the FPS from the original video.
    
    :param images_dir: Directory containing images.
    :param output_video_path: Path where the output video will be saved.
    :param video_path: Path to the original video for FPS reference.
    """
    cap = cv2.VideoCapture(video_path)
    fps = cap.get(cv2.CAP_PROP_FPS)
    
    images = sorted([os.path.join(images_dir, img) for img in os.listdir(images_dir) if img.endswith('_prediction.jpg')])
    
    frame = cv2.imread(images[0])
    height, width, layers = frame.shape
    size = (width, height)
    
    out = cv2.VideoWriter(output_video_path, cv2.VideoWriter_fourcc(*'mp4v'), fps, size)
    
    for image_path in images:
        frame = cv2.imread(image_path)
        out.write(frame)
    
    out.release()
    cap.release()
    print(f"Video created successfully: {output_video_path}")

def find_newest_video(directory):
    """
    Finds the newest video file in the specified directory.
    """
    list_of_files = glob.glob(os.path.join(directory, '*.mp4'))  # Can add other video formats if needed
    if not list_of_files:  # No video files found
        return None
    newest_file = max(list_of_files, key=os.path.getmtime)
    return newest_file

def generate_output_video_path(input_video_path, output_directory):
    """
    Generates the output video path based on the input video filename and the specified output directory.
    """
    base_name = os.path.basename(input_video_path)
    output_filename = f"Prediction_{base_name}"
    return os.path.join(output_directory, output_filename)

# Paths and video segment
video_directory = "C:/THESIS/Yolov7/yolov7-custom/pest vid/Original"
output_video_directory = "C:/THESIS/Yolov7/yolov7-custom/pest vid/Prediction Video"
start_time = 9  # Adjust as needed
end_time = 10   # Adjust as needed

# Find the newest video in the directory
video_path = find_newest_video(video_directory)
if not video_path:
    print("No video files found in the directory.")
else:
    # Generate the output video path
    output_video_path = generate_output_video_path(video_path, output_video_directory)
    
    # Process video
    video_to_frames(video_path, start_time=start_time, end_time=end_time)
    predict_on_frames()
    
    # Create video from predictions
    create_video_from_images("predictions", output_video_path, video_path)
