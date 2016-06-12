// START SNIPPET: listen-gpio-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  DebounceGpioExample.java  
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.Date;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class DebounceGpioExample {
    
    public static void main(String args[]) throws InterruptedException {
        System.out.println("<--Pi4J--> GPIO Debounce Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        //
        // ----- DEBOUNCE -----
        //
        // Bouncing is the tendency of any two metal contacts in an electronic device to generate multiple signals as
        // the contacts close or open; debouncing is any kind of hardware device or software that ensures that only a
        // single signal will be acted upon for a single opening or closing of a contact.
        //
        // Pi4J supports a debounce feature to suppress state change event notifications on GPIO input pins.
        // This feature allows the consumer to set a debounce delay time in milliseconds.  When a pin state change
        // occurs, the initial event will be raised and the debounce delay timer will be started.  Any subsequent
        // pin state changes will be suppressed until after the debounce delay timer has expired.  When the debounce
        // delay timer expires and if the pin state is different than the start when the debounce timer started, a
        // pin event will be raised to notify the consumer of the pin state change.   (You can optionally set a
        // different debounce delay for each pin state or use the example below to set the same delay time for all
        // pin states)
        //
        // Please note that if you make a call to 'getState()', 'isHigh()' or 'isLow()' the actual current state
        // will be returned, the debounce feature only suppresses event notifications, is does not attempt to
        // mask the actual pin state.
        myButton.setDebounce(1000);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println("[" + new Date() +
                        "] --> GPIO PIN STATE CHANGE: " +
                        event.getPin() + " = " + event.getState());
            }
            
        });

        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");
        
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller        
    }
}

// END SNIPPET: listen-gpio-snippet
