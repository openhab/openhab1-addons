
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiLcdExample.java  
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;

public class WiringPiLcdExample {

    public final static int LCD_ROWS = 2;
    public final static int LCD_COLUMNS = 16;
    public final static int LCD_BITS = 4;
    
    public static void main(String args[]) throws InterruptedException, UnsupportedEncodingException {
        System.out.println("<--Pi4J--> Wiring Pi LCD test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // initialize LCD
        int lcdHandle= Lcd.lcdInit(LCD_ROWS,     // number of row supported by LCD
                                   LCD_COLUMNS,  // number of columns supported by LCD
                                   LCD_BITS,     // number of bits used to communicate to LCD 
                                   11,           // LCD RS pin
                                   10,           // LCD strobe pin
                                   0,            // LCD data bit 1
                                   1,            // LCD data bit 2
                                   2,            // LCD data bit 3
                                   3,            // LCD data bit 4
                                   0,            // LCD data bit 5 (set to 0 if using 4 bit communication)
                                   0,            // LCD data bit 6 (set to 0 if using 4 bit communication)
                                   0,            // LCD data bit 7 (set to 0 if using 4 bit communication)
                                   0);           // LCD data bit 8 (set to 0 if using 4 bit communication)


        // verify initialization
        if (lcdHandle == -1) {
            System.out.println(" ==>> LCD INIT FAILED");
            return;
        }

        // clear LCD
        Lcd.lcdClear(lcdHandle);
        Thread.sleep(1000);
        
        // write line 1 to LCD
        Lcd.lcdHome(lcdHandle);
        //Lcd.lcdPosition (lcdHandle, 0, 0) ; 
        Lcd.lcdPuts (lcdHandle, "The Pi4J Project") ;

        // write line 2 to LCD        
        Lcd.lcdPosition (lcdHandle, 0, 1) ; 
        Lcd.lcdPuts (lcdHandle, "----------------") ;

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // update time every one second
        while(true){
            // write time to line 2 on LCD            
            Lcd.lcdPosition (lcdHandle, 0, 1) ; 
            Lcd.lcdPuts (lcdHandle, "--- " + formatter.format(new Date()) + " ---");
            Thread.sleep(1000);
        }
    }
}

