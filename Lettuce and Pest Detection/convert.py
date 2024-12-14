import os
import cv2
import numpy as np
import glob

def get_newest_image_path(input_folder):
    # List of all the image files in the specified directory with extensions .jpg, .jpeg, and .png
    image_files = glob.glob(input_folder + '/*.[jJ][pP][gG]') + glob.glob(input_folder + '/*.[jJ][pP][eE][gG]') + glob.glob(input_folder + '/*.[pP][nN][gG]')
    
    # Find the newest file in the list
    if not image_files:
        return None  # No image files found in the directory
    newest_image_path = max(image_files, key=os.path.getmtime)
    return newest_image_path

def rgb_to_hsv(rgb_image):
    hsv_image = cv2.cvtColor(rgb_image, cv2.COLOR_BGR2HSV)
    return hsv_image

def color_thresholding(hsv_image, lower_threshold, upper_threshold):
    mask = cv2.inRange(hsv_image, lower_threshold, upper_threshold)
    result = cv2.bitwise_and(rgb_image, rgb_image, mask=mask)
    return result

# Set your color threshold (in HSV format)
lower_threshold = np.array([15, 15, 15]) 
upper_threshold = np.array([100, 255, 255]) 

# Input and output directories
input_folder = "rgb2hsv_input"
output_folder = "rgb2hsv_output"

# Create output folder if it doesn't exist
os.makedirs(output_folder, exist_ok=True)

# Process each image in the input folder
newest_image_path = get_newest_image_path(input_folder)
if newest_image_path:
    filename = os.path.basename(newest_image_path)
    rgb_image = cv2.imread(newest_image_path)

    # Convert RGB to HSV
    hsv_image = rgb_to_hsv(rgb_image)

    # Perform color thresholding
    thresholded_image = color_thresholding(hsv_image, lower_threshold, upper_threshold)

    # Save the thresholded image in the output folder
    output_path = os.path.join(output_folder, filename)
    cv2.imwrite(output_path, thresholded_image)
    print(f"Processed and saved the newest image: {filename}")
else:
    print(f"No images found in {input_folder}")

print("Processing complete. If an image was found, it has been saved in:", output_folder)
