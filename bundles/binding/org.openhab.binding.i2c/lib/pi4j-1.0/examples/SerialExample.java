// START SNIPPET: serial-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SerialExample.java  
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


import java.util.Date;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

/**
 * This example code demonstrates how to perform serial communications using the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SerialExample {
    
    public static void main(String args[]) throws InterruptedException {
        
        // !! ATTENTION !!
        // By default, the serial port is configured as a console port 
        // for interacting with the Linux OS shell.  If you want to use 
        // the serial port in a software program, you must disable the 
        // OS from using this port.  Please see this blog article by  
        // Clayton Smith for step-by-step instructions on how to disable 
        // the OS console for this port:
        // http://www.irrational.net/2012/04/19/using-the-raspberry-pis-serial-port/
                
        System.out.println("<--Pi4J--> Serial Communication Example ... started.");
        System.out.println(" ... connect using settings: 38400, N, 8, 1.");
        System.out.println(" ... data received on serial port should be displayed below.");
        
        // create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                // print out the data received to the console
                System.out.print(event.getData());
            }            
        });
                
        try {
            // open the default serial port provided on the GPIO header
            serial.open(Serial.DEFAULT_COM_PORT, 38400);
            
            // continuous loop to keep the program running until the user terminates the program
            while(true) {
                try {
                    // write a formatted string to the serial transmit buffer
                    serial.write("CURRENT TIME: %s", new Date().toString());
    
                    // write a individual bytes to the serial transmit buffer
                    serial.write((byte) 13);
                    serial.write((byte) 10);
    
                    // write a simple string to the serial transmit buffer
                    serial.write("Second Line");
    
                    // write a individual characters to the serial transmit buffer
                    serial.write('\r');
                    serial.write('\n');
    
                    // write a string terminating with CR+LF to the serial transmit buffer
                    serial.writeln("Third Line");
                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();                    
                }
                
                // wait 1 second before continuing
                Thread.sleep(1000);
            }
            
        }
        catch(SerialPortException ex) {
            System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
    }
}

// END SNIPPET: serial-snippet
