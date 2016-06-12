

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  OlimexGpioExample.java  
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


import com.pi4j.gpio.extension.olimex.OlimexAVRIOGpioProvider;
import com.pi4j.gpio.extension.olimex.OlimexAVRIOPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.serial.Serial;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO pin state control and monitoring.
 * </p>  
 * 
 * <p>
 * This example implements the Olimex AVR-IO-M-16 expansion board.
 * More information about the board can be found here: *
 * https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * </p>
 * 
 * <p>
 * The Olimex AVR-IO board is connected via RS232 serial connection to the Raspberry Pi and provides
 * 4 electromechanical RELAYs and 4 opto-isolated INPUT pins.
 * </p>
 * 
 * @see https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * @author Robert Savage
 */
public class OlimexGpioExample {
    
    public static void main(String args[]) throws InterruptedException {

        System.out.println("<--Pi4J--> GPIO Listen Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        // create custom Olimex GPIO provider
        final OlimexAVRIOGpioProvider olimexProvider = new OlimexAVRIOGpioProvider(Serial.DEFAULT_COM_PORT);
        
        // provision gpio input pin #01 from Olimex
        final GpioPinDigitalInput myInput = gpio.provisionDigitalInputPin(olimexProvider, OlimexAVRIOPin.IN_01);
        
        // create gpio pin listener
        GpioPinListenerDigital listener = new GpioPinListenerDigital() {            
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        };
        
        // register gpio pin listener for each input pin 
        myButton.addListener(listener);
        myInput.addListener(listener);
        
        // setup gpio pins #04, #05, #06 as an output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput myRelays[] = { 
            gpio.provisionDigitalOutputPin(olimexProvider, OlimexAVRIOPin.RELAY_01, "RELAY #1", PinState.LOW),
            gpio.provisionDigitalOutputPin(olimexProvider, OlimexAVRIOPin.RELAY_02, "RELAY #2", PinState.LOW),
            gpio.provisionDigitalOutputPin(olimexProvider, OlimexAVRIOPin.RELAY_03, "RELAY #3", PinState.LOW),
            gpio.provisionDigitalOutputPin(olimexProvider, OlimexAVRIOPin.RELAY_04, "RELAY #4", PinState.LOW)
          };
        
        // create a gpio control trigger on the input pin ; when the input goes HIGH, also set gpio pin #04 to HIGH
        myButton.addTrigger(new GpioSetStateTrigger(PinState.HIGH, myRelays[0], PinState.HIGH));

        // create a gpio control trigger on the input pin ; when the input goes LOW, also set gpio pin #04 to LOW
        myButton.addTrigger(new GpioSetStateTrigger(PinState.LOW, myRelays[0], PinState.LOW));

        // create a gpio synchronization trigger on the input pin; when the input changes, also set gpio pin #05 to same state
        myButton.addTrigger(new GpioSyncStateTrigger(myRelays[1]));

        // create a gpio synchronization trigger on the input pin; when the input changes, also set gpio pin #05 to same state
        myButton.addTrigger(new GpioSyncStateTrigger(myRelays[2]));
        
        // create a gpio pulse trigger on the input pin; when the input goes HIGH, also pulse gpio pin #06 to the HIGH state for 1 second
        myButton.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, myRelays[3], 1000));

        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");
        
        // keep program running until user aborts (CTRL-C)
        // or we reach 60 seconds
        for (int seconds = 0; seconds < 60; seconds++) {
            Thread.sleep(1000);
        }
        
        System.out.println(" ... exiting program.");
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();                 
    }
}
