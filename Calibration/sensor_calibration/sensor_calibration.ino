void setup() {
  Serial.begin(115200);
  // Solve the equation
  
  // I. Water Level
  double type1 = 1;
  double tolerance = 0.1;
  Serial.println("Water Level");
  // Trial 1
  double initialGuess = 390;
  double root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  initialGuess = 349;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  initialGuess = 332;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  initialGuess = 322;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  initialGuess = 319;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  initialGuess = 275;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  initialGuess = 269;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  initialGuess = 220;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  initialGuess = 5;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  
  // II. Luminosity
  type1 = 2;
  tolerance = 0.1;
  Serial.println("\nLuminosity");
  // Trial 1
  initialGuess = 46;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  initialGuess = 3600;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  initialGuess = 2600;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  initialGuess = 920;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  initialGuess = 400;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  initialGuess = 825;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  initialGuess = 1340;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  initialGuess = 486;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  initialGuess = 476;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 10
  initialGuess = 633;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);

  // III. Temperature
  double type2 = 1;
  Serial.println("\nTemperature");
  // Trial 1
  double sensorVal = 59.4;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  sensorVal = 59;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  sensorVal = 59.7;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  sensorVal = 64.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  sensorVal = 63.2;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  sensorVal = 60;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  sensorVal = 56;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  sensorVal = 58.1;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  sensorVal = 60.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 10
  sensorVal = 60.1;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);

  // IV. Humidity
  type2 = 2;
  Serial.println("\nHumidity");
  // Trial 1
  sensorVal = 156.7;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  sensorVal = 152;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  sensorVal = 140.2;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  sensorVal = 160.2;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  sensorVal = 120.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  sensorVal = 155.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  sensorVal = 170.8;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  sensorVal = 149.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  sensorVal = 150.3;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 10
  sensorVal = 158;
  root = twopointCalibration(type2, sensorVal);
  Serial.print(root);
  
  // V. pH Level
  type1 = 3;
  tolerance = 0.1;
  Serial.println("\npH Level");
  // Trial 1
  initialGuess = 8.03;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  initialGuess = 8.43;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  initialGuess = 9.30;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  initialGuess = 9.89;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  initialGuess = 10.53;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  initialGuess = 2.73;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  initialGuess = 5.48;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  initialGuess = 3.58;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  initialGuess = 4.43;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 10
  initialGuess = 5.86;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  
  // VI. TDS Level
  type1 = 4;
  tolerance = 0.05;
  Serial.println("\nTDS Level");
  // Trial 1
  initialGuess = 101.73;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 2
  initialGuess = 243.50;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 3
  initialGuess = 420.25;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 4
  initialGuess = 581.50;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 5
  initialGuess = 588.64;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 6
  initialGuess = 586.90;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 7
  initialGuess = 269.07;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 8
  initialGuess = 384.10;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 9
  initialGuess = 518.50;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
  Serial.print(" | ");
  // Trial 10
  initialGuess = 359.68;
  root = newtonRaphson(type1, initialGuess, tolerance, initialGuess);
  Serial.print(root);
}

void loop() {
  // Empty loop
}

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