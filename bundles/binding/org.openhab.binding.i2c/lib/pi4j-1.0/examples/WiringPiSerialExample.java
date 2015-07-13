
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiSerialExample.java  
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
import com.pi4j.wiringpi.Serial;

public class WiringPiSerialExample {
    
    public static void main(String args[]) throws InterruptedException {
        
        System.out.println("<--Pi4J--> SERIAL test program");

        // open serial port for communication
        int fd = Serial.serialOpen(Serial.DEFAULT_COM_PORT, 38400);
        if (fd == -1) {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }

        // infinite loop
        while(true) {
            
            // send test ASCII message
            Serial.serialPuts(fd, "TEST\r\n");

            // display data received to console
            int dataavail = Serial.serialDataAvail(fd);            
            while(dataavail > 0) {
                int data = Serial.serialGetchar(fd);
                System.out.print((char)data);                
                dataavail = Serial.serialDataAvail(fd);
            }
            
            // wash, rinse, repeat
            Thread.sleep(1000);
        }
    }
}
