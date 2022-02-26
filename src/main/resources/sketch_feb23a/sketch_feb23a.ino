#include <SoftwareSerial.h>

SoftwareSerial BTserial(0,1);

int baud = 9600;
int PulseSensorPurplePin = 0;// Pulse Sensor PURPLE WIRE connected to ANALOG PIN 0
int LED13 = 13; //The on-board Arduio LED
int Signal;// holds the incoming raw data. Signal value can range from 0-1024
int Threshold = 550;// Determine which Signal to "count as a beat", and which to ingore.


void setup() {
  pinMode(LED13,OUTPUT); // pin that will blink to heartbeat!
  Serial.begin(baud);
  BTserial.begin(baud);
}

void loop() {

  Signal = analogRead(PulseSensorPurplePin);// Read the PulseSensor's value.

  //Serial.println(Signal);// Send the Signal value to Serial Plotter.
  BTserial.println(Signal);//Send the Signal over bluetooth


  if(Signal > Threshold){ //Turn on Arduino's on-Board LED.
    digitalWrite(LED13,HIGH);
  } else {
    digitalWrite(LED13,LOW); //Turn off LED.
  }
  
  delay(10);
}
