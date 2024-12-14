import cv2
import numpy as np
import os

def play_video(video_path):
    # Open the video file
    cap = cv2.VideoCapture(video_path)

    # Check if the video file is opened successfully
    if not cap.isOpened():
        print("Error opening video file")
        return

    # Get the frames per second (fps) of the video
    fps = cap.get(cv2.CAP_PROP_FPS)

    # Create a window to display the video
    cv2.namedWindow("Video", cv2.WINDOW_NORMAL)

    # Read the first frameq
    #ret, prev_frame = cap.read()

    while True:
        # Read a frame from the video
        ret, frame = cap.read()

        # If the frame is not read successfully, break the loop
        if not ret:
            break

        # BGR to Grayscale
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # Brightness and Contrast Modification: https://stackoverflow.com/questions/39308030/how-do-i-increase-the-contrast-of-an-image-in-python-opencv
        alpha = 1.5 # Contrast control (1.0-3.0)
        beta = -35 # Brightness control (0-100)
        frame = cv2.convertScaleAbs(frame, alpha=alpha, beta=beta)
        # Gaussian Filter
        sigma = 1  # Adjust the sigma value as needed
        frame = cv2.GaussianBlur(frame, (0, 0), sigma)
        # Erosion
        kernel = np.ones((10,10),np.uint8)
        frame = cv2.erode(frame, kernel, iterations = 1)
        # Adaptive Thresholding
        frame = cv2.adaptiveThreshold(frame, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 11, 6)
        frame = cv2.merge([frame, frame, frame])

        # Display the frame
        cv2.imshow("Video", frame)

        # Update the previous frame for the next iteration
        #prev_frame = frame

        # Wait for the specified time (calculated based on fps)
        # and check if the user pressed the 'q' key to exit
        if cv2.waitKey(int(1000 / fps)) & 0xFF == ord('q'):
            break

    # Release the video capture object and close the window
    cap.release()
    cv2.destroyAllWindows()

os.system("CLS")
# Replace 'your_video.mp4' with the path to your video file
video_path = '12.mp4'
play_video(video_path)