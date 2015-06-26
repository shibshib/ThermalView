# ThermalView

A Java openCV and Arduino based project. By using a Grid-EYE sensor attached to an Arduino Microcontroller, it becomes possible
to create a "heat map" or a "thermal view" of whatever object the Grid-EYE sensor is aimed at. The Panasonic Grid-EYE sensor 
is found here: http://na.industrial.panasonic.com/products/sensors/sensors-automotive-industrial-applications/grid-eye-infrared-array-sensor 
and can be purchased from Digi-Key. It is an 8x8 passive IR sensor array. 



In this project, we feed the data coming in from the sensor from an Arduino through the USB serial bus to a computer, which in turn is connected
to a webcam. Running the java program will invoke a webcam view with an overlay of 8x8 squares, each representing a sensor in the 
Grid-EYE. The squares are color coded to show different heat levels detected by each sensor. This project is simply a prototype 
to demonstrate the abilities of the Grid-EYE and it has been further used in Academic Research in thermal tracking. The code for which is proprietary.

For more information about this project, please contact the repository owner. 
