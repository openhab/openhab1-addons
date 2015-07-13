

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP23S17GpioExample.java  
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


import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23S17Pin;
import com.pi4j.io.gpio.*;
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
 * This example implements the MCP23S17 GPIO expansion board.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 * 
 * <p>
 * The MCP23S17 is connected via SPI connection to the Raspberry Pi and provides
 * 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class MCP23S17GpioExample {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> MCP23S17 GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
        final MCP23S17GpioProvider gpioProvider = new MCP23S17GpioProvider(MCP23S17GpioProvider.DEFAULT_ADDRESS, SpiChannel.CS0);
        
        // provision gpio input pins from MCP23S17
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B0, "MyInput-B0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B1, "MyInput-B1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B2, "MyInput-B2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B3, "MyInput-B3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B4, "MyInput-B4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B5, "MyInput-B5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B6, "MyInput-B6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23S17Pin.GPIO_B7, "MyInput-B7", PinPullResistance.PULL_UP),
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
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A0, "MyOutput-A0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A1, "MyOutput-A1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A2, "MyOutput-A2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A3, "MyOutput-A3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A4, "MyOutput-A4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A5, "MyOutput-A5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A6, "MyOutput-A6", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23S17Pin.GPIO_A7, "MyOutput-A7", PinState.LOW)
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

