#include <SoftwareSerial.h>

SoftwareSerial BTserial(0,1);

long baud = 57600;
int PulseSensorPin = 0;// Pulse Sensor PURPLE WIRE connected to ANALOG PIN 0
int Signal;// holds the incoming raw data. Signal value can range from 0-1024


void setup() {
  pinMode(13, OUTPUT);
  digitalWrite(13,LOW); //Turn off LED.
  BTserial.begin(baud);
}

void loop() {

  Signal = analogRead(PulseSensorPin);
  BTserial.println(Signal);
  delay(3);
}
