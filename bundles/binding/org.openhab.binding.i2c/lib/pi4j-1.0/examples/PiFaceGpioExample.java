

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PiFaceGpioExample.java  
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


import com.pi4j.gpio.extension.piface.PiFaceGpioProvider;
import com.pi4j.gpio.extension.piface.PiFacePin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO pin state control and monitoring.
 * </p>  
 * 
 * <p>
 * This example implements the MCP23017 GPIO expansion board.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 * 
 * <p>
 * The MCP23017 is connected via I2C connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class PiFaceGpioExample {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> PiFace (MCP23017) GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom PiFace GPIO provider
        final PiFaceGpioProvider gpioProvider = new PiFaceGpioProvider(PiFaceGpioProvider.DEFAULT_ADDRESS, SpiChannel.CS0);
        
        // provision gpio input pins from PiFaceGpioProvider
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_00),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_01),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_02),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_03),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_04),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_05),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_06),
                gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_07)
            };
        
        // create and register gpio pin listener
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        }, myInputs);
        
        // provision gpio output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput myOutputs[] = { 
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_00),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_01),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_02),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_03),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_04),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_05),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_06),
            gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_07),
          };
        
        // keep program running for 20 seconds
        for (int count = 0; count < 10; count++) {
            gpio.setState(true, myOutputs);
            Thread.sleep(1000);
            gpio.setState(false, myOutputs);
            Thread.sleep(1000);
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}

