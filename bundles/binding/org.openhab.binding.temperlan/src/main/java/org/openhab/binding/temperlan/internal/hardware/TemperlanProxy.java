/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.temperlan.internal.hardware;

import java.io.IOException;
import java.util.Arrays;

import org.openhab.binding.temperlan.internal.TemperlanActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Temperlan Proxy
 * 
 * @author Eric Thill
 * @author Ben Jones
 * @author Stephan Noerenberg
 * @since 1.6.0
 */
public class TemperlanProxy {
	
	private static Logger logger = LoggerFactory.getLogger(TemperlanActivator.class);
	private final String host;

	public TemperlanProxy(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public TemperlanState getState() throws IOException {

		float temperature1 = -200;
		float temperature2 = -200;
		float temperature3 = -200;
		float temperature4 = -200;
		float temperature5 = -200;
		float temperature6 = -200;
		float temperature7 = -200;
		float temperature8 = -200;
		float temperature9 = -200;
		float temperature10 = -200;
		float temperature11 = -200;
		float temperature12 = -200;

		Plug plug = new Plug(host, 5200);
		byte[] responseArray = plug.send();
		
        int currentIndex = 3; // index 4 describes the count of sensors
        int countSensors = responseArray[currentIndex];
        currentIndex++;

        logger.debug("response array: " + Arrays.toString(responseArray));
        logger.debug("response hex:" + bytesToHex(responseArray));
        
        //List temperatures = new ArrayList();
        
        for (int i = 0; i < countSensors; i++) {
        	
        	logger.debug("sensor: " + i);
        	logger.debug("currentIndex: " + currentIndex);
        	logger.debug("first byte: " + responseArray[currentIndex] + ", second byte: " + responseArray[currentIndex + 1]);
        	
        	int[] array = convertToIntArray(responseArray);

        	logger.debug("first int: " + array[currentIndex] + ", second int: " + array[currentIndex + 1]);
        	
            int temp = ((array[currentIndex]) * 256 + (array[currentIndex + 1]));
            if (temp >= 32768) {
                temp -= 65536;
            }
            
            logger.debug("temp: " + temp);
         
            float temperature = (float) (temp * 0.0625);
            
            //temperatures.add(i, temperature);
            currentIndex = currentIndex + 2;
            
            switch (i) {
			case 0:
				temperature1 = temperature;
				break;
			case 1:
				temperature2 = temperature;
				break;
			case 2:
				temperature3 = temperature;
				break;
			case 3:
				temperature4 = temperature;
				break;
			case 4:
				temperature5 = temperature;
				break;
			case 5:
				temperature6 = temperature;
				break;
			case 6:
				temperature7 = temperature;
				break;
			case 7:
				temperature8 = temperature;
				break;
			case 8:
				temperature9 = temperature;
				break;
			case 9:
				temperature10 = temperature;
				break;
			case 10:
				temperature11 = temperature;
				break;
			case 11:
				temperature12 = temperature;
				break;
			default:
				break;
			}
        }
		
        plug.close();
        
		return new TemperlanState(temperature1, temperature2, temperature3,
								  temperature4, temperature5, temperature6, 
								  temperature7, temperature8, temperature9, 
								  temperature10, temperature11, temperature12);
	}  
	
	public static int[] convertToIntArray(byte[] input)
	{
	    int[] ret = new int[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
	    }
	    return ret;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}


