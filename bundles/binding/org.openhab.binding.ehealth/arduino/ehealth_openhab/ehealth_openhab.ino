/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * @version 1.6.0
 * @author Thomas Eichstaedt-Engelen
 */
#include <PinChangeInt.h>
#include <eHealth.h>

char recv[256];
uint8_t cont = 0;
char delimiter = '@';
int dataRate = 1000;

void setup() { 
  Serial.begin(9600);
  eHealth.initPulsioximeter();
  eHealth.initPositionSensor(); 
} 

void loop() { 
  check();

  //attach interrupts to use the pulsioximeter.     
  PCintPort::attachInterrupt(6, readPulsioximeter, RISING);
  delay(1000);

  while(1) {
    // extract data from eHealth lib
    int airFlow = eHealth.getAirFlow();     
    float temperature = eHealth.getTemperature();     
    float skinConductance = eHealth.getSkinConductance();   
    float skinResistance = eHealth.getSkinResistance();
    float skinConductanceVoltage = eHealth.getSkinConductanceVoltage();
    int bpm = eHealth.getBPM();     
    int oxygenSaturation = eHealth.getOxygenSaturation();     
    uint8_t bodyPosition = eHealth.getBodyPosition();      
    float ecg = eHealth.getECG();
    int emg = eHealth.getEMG();
    emg = map(emg, 200, 300, 0, 100);  // Use this function to adapt your muscle level to 0-100%
    uint8_t glucose = eHealth.glucoseDataVector[0].glucose;

    // send data to the serial interface
    Serial.print(int(airFlow));            Serial.print(delimiter);
    Serial.print(temperature);             Serial.print(delimiter);
    Serial.print(skinConductance);         Serial.print(delimiter);
    Serial.print(skinResistance);          Serial.print(delimiter);
    Serial.print(skinConductanceVoltage);  Serial.print(delimiter);
    Serial.print(int(bpm));                Serial.print(delimiter);
    Serial.print(int(oxygenSaturation));   Serial.print(delimiter);
    Serial.print(int(bodyPosition));       Serial.print(delimiter);
    Serial.print(int(ecg));                Serial.print(delimiter);
    Serial.print(int(emg));                Serial.print(delimiter);
    Serial.print(int(glucose));            Serial.print("\n");
    
    // adjust data rate ...
    delay(dataRate);  
  }

}

void check() {
  cont=0; 
  delay(500);
  
  while (Serial.available()>0) {
    recv[cont]=Serial.read(); 
    delay(10);
    cont++;
  }
  
  recv[cont]='\0';
  Serial.println(recv);
  Serial.flush(); 
  delay(100);
}


void readPulsioximeter() {  
  cont ++;
  if (cont == 50) { //Get only one of  50 measures to reduce the latency
    eHealth.readPulsioximeter();  
    cont = 0;
  }
}

