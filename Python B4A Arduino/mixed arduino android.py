from datetime import datetime
import firebase_admin
from firebase_admin import credentials, firestore, storage, messaging
import os
import random
import serial
import time

os.system("CLS")
cred = credentials.Certificate("google-services.json")
# firebase_admin.initialize_app(cred)
firebase_admin.initialize_app(cred, {
    'storageBucket': 'gs://thesis-hydroponics-v5.appspot.com'
})
db = firestore.client()
collection_ref = db.collection('Sensors_Infos')

doc_ref_REQ = db.collection('Sensors_Infos').document('request_avail')
doc_ref_REQ1 = db.collection('Sensors_Infos').document('request_sensor_val')

doc_ref_IS = db.collection('Sensors_Infos').document('is_avail_sensor')
doc_ref_HUM = db.collection('Sensors_Infos').document('humidity_sensor')
doc_ref_TMP = db.collection('Sensors_Infos').document('temperature_sensor')
doc_ref_PH = db.collection('Sensors_Infos').document('ph_sensor')
doc_ref_TDS = db.collection('Sensors_Infos').document('TDS_sensor')
doc_ref_WL1 = db.collection('Sensors_Infos').document('water_level_sensor1')
doc_ref_WL2 = db.collection('Sensors_Infos').document('water_level_sensor2')
doc_ref_LUX = db.collection('Sensors_Infos').document('lux_sensor')

collection_ref1 = db.collection('Camera_Infos')
doc_ref_AC = db.collection('Camera_Infos').document('Android_cam')

def on_snapshot(doc_snapshot, changes, read_time):
    for change in changes:
        if change.type.name == 'MODIFIED':
            doc_IS = doc_ref_IS.get()
            doc_IS = f"{doc_IS.to_dict()}"
            doc_IS = doc_IS.split(": ")[1][:-1]
            print(doc_IS)
            
            modified_data = change.document.to_dict()
            modified_data = str(modified_data)
            field_name = modified_data.split("': ")[0][2:]
            field_val = modified_data.split("': ")[1][:-1]
            print(f"{field_name} {field_val}")

            modified_data = change.document.to_dict()
            modified_data = str(modified_data)
            field_name = modified_data.split("': ")[0][2:]
            field_val = modified_data.split("': ")[1][:-1]
            print(f"{field_name} {field_val}")
            
            if field_name == "request_avail" and field_val == "1":
                try:
                    ser = serial.Serial('COM6', 115200)
                    doc_ref_IS.update({u'is_avail': 1})
                    print("AVAILABLE")
                except Exception as e:
                    doc_ref_IS.update({u'is_avail': 0})
                    print("UNAVAILABLE")
                doc_ref_REQ.update({u'request_avail': 0})

            if (field_name == "request_val" and field_val == "1"):
                if doc_IS == "1":
                    doc_ref_IS.update({u'is_avail': 0})
                    try:
                        # Configure the serial port
                        ser = serial.Serial('COM6', 115200)
                        # Read a line of data from the serial port
                        line = ser.readline()
                        # print(line)
                        print("AVAILABLE")
                        
                        # Decode the line and split it into a list of values
                        decoded_line = line.decode('utf-8').rstrip()
                        values = decoded_line.split('\t\t')
                        
                        print(f"Humidity: {values[0]}")
                        print(f"Temperature: {values[1]}")
                        print(f"pH: {values[2]}")
                        print(f"TDS/Water Conductivity: {values[3]}")
                        print(f"Water Level: {values[4]}")
                        print(f"Lux: {values[5]}")
                        print("-----------------------------")

                        # datetime object containing current date and time
                        now = datetime.now()
                        print("now =", now)
                        dt_string = now.strftime("%d/%m/%Y %H:%M:%S")

                        doc_ref_HUM.update({dt_string: float(values[0])})
                        doc_ref_TMP.update({dt_string: float(values[1])})
                        doc_ref_PH.update({dt_string: float(values[2])})
                        doc_ref_TDS.update({dt_string: float(values[3])})
                        doc_ref_WL1.update({dt_string: float(values[4])})
                        doc_ref_WL2.update({dt_string: float(values[4])})
                        doc_ref_LUX.update({dt_string: float(values[5])})
                        doc_ref_IS.update({u'is_avail': 1})
                    except Exception as e:
                        print("UNAVAILABLE")
                        doc_ref_IS.update({u'is_avail': 0})
                doc_ref_REQ1.update({u'request_val': 0})
            time.sleep(0.1)

def on_snapshot1(doc_snapshot, changes, read_time):
    for change in changes:
        if change.type.name == 'MODIFIED':
            modified_data = change.document.to_dict()
            modified_data = str(modified_data)
            field_val = modified_data.split("'request_img")[1][3]
            print(f"request_img: {field_val}")

            if (field_val == "1"):
                print("DETECTED")
                # Create a message to send
                message = messaging.Message(
                    data={
                        'title': 'Request Lettuce Pic',
                        'body': 'TAKE ANDROID MOBILE PHONE CAM'
                    },
                    token='fXZa0kvsR1eIf6L7mX6sJU:APA91bGjnh2vZbewirz_PoP1-vzR8vX4TxC2QHJVrWALGuur864-9AQWxB8NTJCIPsAlRR5Avfgk1CySdMtYzaBn6nL450sgrJxWX8PN_ONf55XjxMseJ10-8QX-IeybL33fuI9GYjih'
                )

                # Send the message
                response = messaging.send(message)
                print('Successfully sent in-app message:', response)

                doc_ref_AC.update({u'request_img': 0})

                time.sleep(1)
                bucket = storage.bucket("thesis-hydroponics-v5.appspot.com")
                # Specify the file to be downloaded
                blob = bucket.blob("public/android.txt")
                # Download the file to the local path
                blob.download_to_filename("android.txt")
                print(f"File downloaded successfully")

                time.sleep(1)
                with open("android.txt", 'r') as file:
                    # Read the entire contents of the file
                    file_content = file.read()
                    if file_content == "IS_AVAIL":
                        doc_ref_AC.update({u'is_avail': 1})
                    else:
                        doc_ref_AC.update({u'is_avail': 0})
            time.sleep(0.1)

doc_watch = collection_ref.on_snapshot(on_snapshot)
doc_watch1 = collection_ref1.on_snapshot(on_snapshot1)

while True:
    pass