/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.weather.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;


/**
 * This class contains the methods that are made available in scripts and rules for Weather.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class Weather {

	/**
	 * Compute the Humidex index"
	 * http://en.wikipedia.org/wiki/Humidex
	 * @param temperature in (°C)
	 * @param hygro relative level (%)
	 * @return distance between the two points in meters
	 */
	@ActionDoc(text="Compute the Humidex index given temperature and hygrometry",
					returns="Humidex index value")
	public static double getHumidex(
			@ParamDoc(name="Temperature") double temperature,
			@ParamDoc(name="Relative hygro level") double hygro) {
		
		double result = 6.112 * Math.pow(10, 7.5 * temperature/(237.7 + temperature)) * hygro/100;
		result = temperature + 0.555555556 * (result - 10);
		return result;
	}
	
	/**
	 * Compute the Beaufort scale for a given wind speed
	 * http://en.wikipedia.org/wiki/Beaufort_scale
	 * @param wind speed in m/s
	 * @return Beaufort Index between 0 and 12
	 */
	@ActionDoc(text="Compute the Beaufort scale for a given wind speed",
				returns="Beaufort Index between 0 and 12")
	public static int getBeaufortIndex(
			@ParamDoc(name="WindSpeed") double speed) {

		int result;
		if (speed < 0.3) result = 0;
		else if (speed < 1.6) result = 1;
		else if (speed < 3.4) result = 2;
		else if (speed < 5.5) result = 3;
		else if (speed < 8) result = 4;
		else if (speed < 10.8) result = 5;
		else if (speed < 13.9) result = 6;
		else if (speed < 17.2) result = 7;
		else if (speed < 20.8) result = 8;
		else if (speed < 24.5) result = 9;
		else if (speed < 28.5) result = 10;
		else if (speed < 32.7) result = 11;
		else result = 12;
		
		return result;
	}
	
	/**
	 * Compute the Sea Level Pressure
	 * @param absolute pressure hPa
	 * @param temperature (°C)
	 * @param altitude in meter
	 * @return Equivalent Seal Level pressure
	 *
	 * http://keisan.casio.com/exec/system/1224575267
	 *
	 */
	@ActionDoc(text="Compute the Sea Level Pressure",
				returns="Equivalent Seal Level pressure")
	public static double getSeaLevelPressure(
				@ParamDoc(name="absolute pressure hPa") double pressure,
				@ParamDoc(name="temperature (°C)") 		double temp,
				@ParamDoc(name="Altitude in meter") 	double altitude) {

		double x = 0.0065 * altitude;
		x = (1 - x/(temp + x + 273.15));
		double result = pressure * Math.pow(x,-5.257);
		
		return result;
	}

}
