#include <Wire.h>
#include <EEPROM.h>
#include "GravityTDS.h"
#include "DHT.h"                          
#include <Wire.h>
#include "TSL2561.h"
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <SimpleTimer.h>
 
// float calibration_value = 21.34 - 0.1;
float calibration_value = 22.5 - 0.1; //change this value to calibrate
int phval = 0; 
unsigned long int avgval; 
int buffer_arr[10],temp;
 
float ph_act;
// Define the pH sensor input pin
const int phSensorPin = A0;
// Define the TDS sensor input pin
#define TdsSensorPin A1

// Sensor pins
#define sensorPower 7 // Water level sensor
#define sensorPin A3 // Water level sensor

GravityTDS gravityTds;
float tdsValue = 0;
#define DHTPIN 8
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
TSL2561 tsl(TSL2561_ADDR_FLOAT); // Luminosity sensor
#define DHT11_PIN 7 // Temp & humidity sensor
int val = 0; // Water Level sensor

void setup() {
   Wire.begin();
  // Begin serial communication at 115200 baud
  Serial.begin(115200);

  // Initialize the TDS sensor
  gravityTds.setPin(TdsSensorPin);
  gravityTds.setAref(5.0);
  gravityTds.setAdcRange(1024);
  gravityTds.begin();

  // Initialize the light sensor
  if (tsl.begin()) {
    // Serial.println("Found sensor");
  } else {
    // Serial.println("No sensor?");
    // while (1);
  }
  tsl.setGain(TSL2561_GAIN_16X);      // set 16x gain (for dim situations)
  tsl.setTiming(TSL2561_INTEGRATIONTIME_13MS);  // shortest integration time (bright light)

  //Water Sensor
  pinMode(sensorPower, OUTPUT);
  digitalWrite(sensorPower, LOW);

  // Set to LOW so no power flows through the sensor
  digitalWrite(sensorPower, LOW);
}

void loop() {
 for(int i=0;i<10;i++) 
 { 
 buffer_arr[i]=analogRead(A0);
 delay(30);
 }
 for(int i=0;i<9;i++)
 {
 for(int j=i+1;j<10;j++)
  {
 if(buffer_arr[i]>buffer_arr[j])
    {
 temp=buffer_arr[i];
 buffer_arr[i]=buffer_arr[j];
 buffer_arr[j]=temp;
    }
  }
 }
 avgval=0;
 for(int i=2;i<8;i++)
 avgval+=buffer_arr[i];
 float volt=(float)avgval*5.0/1024/6; 
  ph_act = -5.70 * volt + calibration_value;

  // Measure TDS
  float temperature = dht.readTemperature();
  gravityTds.setTemperature(temperature);
  gravityTds.update();
  tdsValue = gravityTds.getTdsValue();

  uint16_t x = tsl.getLuminosity(TSL2561_VISIBLE);
  uint32_t lum = tsl.getFullLuminosity();
  uint16_t ir, full;
  ir = lum >> 16;
  full = lum & 0xFFFF;
  int level = readSensor();
  float humidity = dht.readHumidity();

  // Humidity Calibration
  if (humidity == humidity){
    float x1 = twopointCalibration(2, humidity);
    Serial.print(x1, 2);
  } else {
    Serial.print(41.10);
  }
  Serial.print("\t\t");
  // Temperature Calibration
  if (temperature == temperature){
    float x2 = twopointCalibration(1, temperature);
    Serial.print(x2, 2);
  } else {
    Serial.print(31.10);  
  }
  Serial.print("\t\t");
  // pH Level Calibration
  double tolerance = 0.1;
  float x3 = newtonRaphson(3, float(ph_act), tolerance, float(ph_act));
  Serial.print(x3, 2);
  Serial.print("\t\t");
  // TDS Level Calibration
  if (tdsValue == tdsValue){
    float x4 = newtonRaphson(4, tdsValue, 0.05, tdsValue);
    Serial.print(x4, 2);
  } else {
    Serial.print(5.00);
  }
  Serial.print("\t\t");
  // Water Level Calibration
  float x5 = newtonRaphson(1, level, 0.1, level);
  Serial.print(x5, 2);
  Serial.print("\t\t"); 
  // Luminosity Calibration
  float x6 = newtonRaphson(2, double(tsl.calculateLux(full, ir)), 0.1, double(tsl.calculateLux(full, ir)));
  Serial.print(x6, 2);
  Serial.print("\n"); 
  delay(100);
}

int readSensor() {
  digitalWrite(sensorPower, HIGH);  // Turn the sensor ON
  delay(10);              // wait 10 milliseconds
  val = analogRead(sensorPin);    // Read the analog value form sensor
  digitalWrite(sensorPower, LOW);   // Turn the sensor OFF
  return val;             // send current reading
}

// Modules for Calibration
// Function to be solved
double f(int type, double x, double y_val) {
  double x5, x4, x3, x2, x1, x0;
  switch (type){
  	case 1:
    	x5 = 8.1641025640*x*x*x*x*x;
      	x4 = 90.5687645684*x*x*x*x;
      	x3 = 377.8834498815*x*x*x;
      	x2 = 737.3578088543*x*x;
      	x1 = 706.0328671309*x;
      	x0 = 6.0069930071;
    	return x5-x4+x3-x2+x1+x0-y_val;
    case 2:
      	x3 = 0.0000001294*x*x*x;
      	x2 = 0.0004607598*x*x;
      	x1 = 1.2143647107*x;
      	x0 = 36.0503883641;
    	return x3-x2+x1-x0-y_val;
    case 3:
    	x5 = 0.0018052047*x*x*x*x*x;
      	x4 = 0.0511401944*x*x*x*x;
      	x3 = 0.5242113175*x*x*x;
      	x2 = 2.3155885095*x*x;
      	x1 = 4.8469816869*x;
      	x0 = 0.5408937392;
    	return x5-x4+x3-x2+x1-x0-y_val;
    case 4:
    	x4 = 0.0000000002*x*x*x*x;
      	x3 = 0.0000006371*x*x*x;
      	x2 = 0.0002914698*x*x;
      	x1 = 0.6932858267*x;
      	x0 = 39.4928169526;
    	return x4-x3+x2+x1+x0-y_val;
  }
}
// Derivative of the function
double fprime(int type, double x) {
  double x5, x4, x3, x2, x1, x0;
  switch (type){
  	case 1:
    	x4 = 8.1641025640*5*x*x*x*x;
  	 	x3 = 90.5687645684*4*x*x*x;
  	 	x2 = 377.8834498815*3*x*x;
  	 	x1 = 737.3578088543*2*x;
  	 	x0 = 706.0328671309;
    	return x4+x3-x2+x1+x0;
    case 2:
    	x2 = 0.0000001294*3*x*x;
      	x1 = 0.0004607598*2*x;
      	x0 = 1.2143647107;
    	return x2-x1+x0;
    case 3:
    	x4 = 0.0018052047*5*x*x*x*x;
      	x3 = 0.0511401944*4*x*x*x;
      	x2 = 0.5242113175*3*x*x;
      	x1 = 2.3155885095*2*x;
      	x0 = 4.8469816869;
    	return x4-x3+x2-x1+x0;
    case 4:
    	x3 = 0.0000000002*4*x*x*x;
      	x2 = 0.0000006371*3*x*x;
      	x1 = 0.0002914698*2*x;
      	x0 = 0.6932858267*x;
    	return x3-x2+x1+x0;
  }
}
// Newton-Raphson method
double newtonRaphson(int type, double initialGuess, double tolerance, double y_val) {
  double x = initialGuess;
  while (abs(f(type, x, y_val)) > tolerance) {
    x -= f(type, x, y_val) / fprime(type, x);
    // Serial.println(x);
  }
  return x;
}
// Two-Point calibration method
double twopointCalibration(int type, double sensorVal){
  double rawLow, rawHigh, refLow, refHigh;
  switch (type){
  	case 1:
    	rawLow = 56;
    	rawHigh = 64.3;
    	refLow = 29.1;
    	refHigh = 34.5;
    	break;
    case 2:
    	rawLow = 120.3;
    	rawHigh = 170.8;
    	refLow = 39.3;
    	refHigh = 54;
    	break;
  }
  double rawRange = abs(rawHigh - rawLow);
  double refRange = abs(refHigh - refLow);
  return (((sensorVal-rawLow)*refRange)/rawRange)+refLow;
}