/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiGpioInterruptExample2.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterruptCallback;

public class WiringPiGpioInterruptExample2 {
    
    public static void main(String args[]) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO interrupt test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        Gpio.pinMode (0, Gpio.INPUT) ;
        Gpio.pinMode (1, Gpio.INPUT) ;
        Gpio.pinMode (2, Gpio.INPUT) ;
        Gpio.pinMode (3, Gpio.INPUT) ;
        Gpio.pinMode (4, Gpio.INPUT) ;
        Gpio.pinMode (5, Gpio.INPUT) ;
        Gpio.pinMode (6, Gpio.INPUT) ;
        Gpio.pinMode (7, Gpio.INPUT) ;

        Gpio.pullUpDnControl(0, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(1, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(2, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(3, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(4, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(5, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(6, Gpio.PUD_DOWN);
        Gpio.pullUpDnControl(7, Gpio.PUD_DOWN);

        // setup a pin interrupt callback for pin 7
        Gpio.wiringPiISR(0, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(1, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(2, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(3, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(4, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(5, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(6, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });
        Gpio.wiringPiISR(7, Gpio.INT_EDGE_FALLING, new GpioInterruptCallback() {
            @Override
            public void callback(int pin) {
                System.out.println(" ==>> GPIO PIN " + pin + " - INTERRUPT DETECTED");
            }
        });

        // wait for user to exit program
        System.console().readLine("Press <ENTER> to exit program.\r\n");
    }
}

