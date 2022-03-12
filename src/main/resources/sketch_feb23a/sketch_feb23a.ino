#define USE_ARDUINO_INTERRUPTS true

#include <SoftwareSerial.h>
#include <PulseSensorPlayground.h>

SoftwareSerial BTserial(0,1);

long baud = 57600;
int PulseSensorPin = 0;// Pulse Sensor PURPLE WIRE connected to ANALOG PIN 0
int Signal;// holds the incoming raw data. Signal value can range from 0-1024
int threshold = 650;
int BPM = 0;
PulseSensorPlayground pulseSensor;

void setup() {
  pinMode(13, OUTPUT);
  digitalWrite(13,LOW); //Turn off LED.
  pulseSensor.analogInput(PulseSensorPin);
  pulseSensor.setThreshold(threshold);
  pulseSensor.begin();
  BTserial.begin(baud);
}

void loop() {
  BPM = pulseSensor.getBeatsPerMinute();
  
  Signal = analogRead(PulseSensorPin);
  String out = "[";
  out.concat(BPM);
  out.concat("]{");
  out.concat(Signal);
  out.concat("}");
  BTserial.println(out);
  delay(3);
}
