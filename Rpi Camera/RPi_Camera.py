from datetime import datetime
import firebase_admin
from firebase_admin import credentials, firestore, storage
import os
import time

os.system("CLS")
cred = credentials.Certificate("google-services.json")
firebase_admin.initialize_app(cred, {
    'storageBucket': 'gs://thesis-hydroponics-v5.appspot.com/RPi_Image'
})
db = firestore.client()
collection_ref = db.collection('Camera_Infos')

doc_ref_IS = db.collection('Camera_Infos').document('is_avail')
doc_ref_REQ = db.collection('Camera_Infos').document('request_avail')
doc_ref_REQ1 = db.collection('Camera_Infos').document('request_cam_vid')
doc_ref_RDY = db.collection('Camera_Infos').document('ready_detection')

# Initial checking of availability
try:
    os.system('libcamera-vid -o /home/pi/Desktop/RPi_8/test.h264 --height 1232 --width 1640 --framerate 41 -t 5')
    doc_ref_IS.update({u'is_avail': 1})
except Exception as e:
    doc_ref_IS.update({u'is_avail': 0})

def on_snapshot(doc_snapshot, changes, read_time):
    for change in changes:
        if change.type.name == 'MODIFIED':
            doc_IS = doc_ref_IS.get()
            doc_IS = f"{doc_IS.to_dict()}"
            doc_IS = doc_IS.split(": ")[1][:-1]
            print(f"[{doc_IS}]")
            
            modified_data = change.document.to_dict()
            modified_data = str(modified_data)
            field_name = modified_data.split("': ")[0][2:]
            field_val = modified_data.split("': ")[1][:-1]
            print(f"[{field_name}] [{field_val}]")
            
            # Request checking of availability
            if field_name == "request_avail" and field_val == "1":
                try:
                    os.system('libcamera-vid -o /home/pi/Desktop/RPi_8/test.h264 --height 1232 --width 1640 --framerate 41 -t 5')
                    doc_ref_IS.update({u'is_avail': 1})
                except Exception as e:
                    doc_ref_IS.update({u'is_avail': 0})
                doc_ref_REQ.update({u'request_avail': 0})

            # Request camera vid from RPi
            if (field_name == "request_cam_vid" and field_val == "1"):
                try:
                    # Make it unavailable at first due to current process
                    doc_ref_IS.update({u'is_avail': 0})
                    # Take a video recording
                    os.system('libcamera-vid -o /home/pi/Desktop/RPi_8/rpi_vid.h264 --height 1232 --width 1640 --framerate 41 -t 50000')
                    # Upload to firebase storage
                    localpath = '/home/pi/Desktop/RPi_8/rpi_vid.h264'
                    firebasepath = 'RPi_Image/rpi_vid.h264'
                    bucket = storage.bucket('thesis-hydroponics-v5.appspot.com')
                    blob = bucket.blob(firebasepath)
                    blob.upload_from_filename(localpath)
                    blob.make_public()
                    doc_ref_RDY.update({u'ready_detection': 1})
                except Exception as e:
                    doc_ref_IS.update({u'is_avail': 0})

                # Check it's availability again to return to previous/current state
                try:
                    os.system('libcamera-vid -o /home/pi/Desktop/RPi_8/test.h264 --height 1232 --width 1640 --framerate 41 -t 5')
                    doc_ref_IS.update({u'is_avail': 1})
                except Exception as e:
                    doc_ref_IS.update({u'is_avail': 0})
                doc_ref_REQ1.update({u'request_cam_vid': 0})
            time.sleep(0.1)

doc_watch = collection_ref.on_snapshot(on_snapshot)

while True:
    pass