from datetime import datetime
import firebase_admin
from firebase_admin import credentials, firestore, storage
import os
import time
import subprocess
# Initialize Firebase Admin SDK
cred = credentials.Certificate("google-services.json")
firebase_admin.initialize_app(cred, {
    'storageBucket': 'gs://thesis-hydroponics-v5.appspot.com'
})
db = firestore.client()

# Reference to the 'Camera_Infos' collection
collection_ref = db.collection('Camera_Infos')
# References to specific documents within 'Camera_Infos' collection
doc_ref_AC = db.collection('Camera_Infos').document('Android_cam')
doc_ref_RD = db.collection('Camera_Infos').document('ready_detection')
doc_ref_RC = db.collection('Camera_Infos').document('RPi_Camera')


def find_newest_image_in_directory(directory_path):
    # Get a list of all files in the directory with specific extensions
    image_files = [os.path.join(directory_path, f) for f in os.listdir(directory_path)
                   if f.endswith(('.jpg', '.jpeg', '.png'))]
    # Check if there are any image files in the directory
    if not image_files:
        return None  # No images found
    # Find and return the path of the newest image file
    return max(image_files, key=os.path.getctime)

def check_ready_detection_status():
    # Fetch the current state of both documents
    doc_AC = doc_ref_AC.get().to_dict()
    doc_RD = doc_ref_RD.get().to_dict()
    # Check if 'ready_detection' is 1 in both documents
    if doc_AC.get('ready_detection') in ['1', 1] and doc_RD.get('ready_detection') in ['1', 1]:
        return True
    else:
        return False

# Function to be called upon detecting changes in the Firestore collection
def on_snapshot(doc_snapshot, changes, read_time):
    if check_ready_detection_status():
        print("Both ready_detection fields are set to 1.")  # Debug print
        try:
            # Specify the Firebase Storage path for the image
            localpath = 'C:/THESIS/Yolov7/yolov7-custom/rgb2hsv_input/128_36cm.jpg'
            firebasepath = 'public/128_36cm.jpg'
            bucket = storage.bucket('thesis-hydroponics-v5.appspot.com')
            blob = bucket.blob(firebasepath)
            # Download the image from Firebase Storage
            blob.download_to_filename(localpath)
            print(f"Image downloaded to {localpath}")
            # Execute image processing and detection scripts
            subprocess.run(['python', 'convert.py'], check=True)
            
            thresholded_image_path = find_newest_image_in_directory('C:/THESIS/Yolov7/yolov7-custom/rgb2hsv_output')
            subprocess.run(['python', 'detect.py', '--weights', 'best.pt', '--conf', '0.5', '--img-size', '720', '--source', thresholded_image_path, '--no-trace', '--save-txt'], check=True)
            subprocess.run(['python', 'regression.py'], check=True)
            subprocess.run(['python', 'dpest.py'], check=True)

            with open('predicted_height.txt', 'r') as f:
                predicted_height = float(f.read())

            doc_ref_AC.update({'lettuce_height': predicted_height})

            with open('average_predictions.txt', 'r') as f:
                average_predictions = float(f.read())

            doc_ref_RC.update({'pest_amount': average_predictions})

            # Update both documents to reset 'ready_detection' and update 'lettuce_height'
            doc_ref_AC.update({'ready_detection': 0})
            doc_ref_RD.update({'ready_detection': 0})
            
        except Exception as e:
            print(f"Error during operations: {e}")

# Start listening to changes in the 'Camera_Infos' collection
doc_watch = collection_ref.on_snapshot(on_snapshot)

# Keep the script running to listen for changes
while True:
    time.sleep(1)  # Prevents the script from using too much CPU
