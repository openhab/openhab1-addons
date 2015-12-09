/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.weather.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.binding.weather.internal.utils.UnitUtils;


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
	 * @return Humidex index value
	 */
	@ActionDoc(text="Compute the Humidex index given temperature and hygrometry",
					returns="Humidex index value")
	public static double getHumidex(
			@ParamDoc(name="Temperature") double temperature,
			@ParamDoc(name="Relative hygro level") int hygro) {
		return UnitUtils.getHumidex(temperature, hygro);
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
		Double kmh = UnitUtils.mpsToKmh(speed);
		return UnitUtils.kmhToBeaufort(kmh).intValue();
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
		
		return UnitUtils.getSeaLevelPressure(pressure, temp, altitude);
	}
	
	/**
	 * Transform an orientation angle to its
	 * cardinal string equivalent
	 * @param orientation in °
	 * @return String representing the direction
	 */
	@ActionDoc(text="Transform a direction angle to its cardinal string equivalent",
				returns="String representing the direction")
	public static String getWindDirection(
			@ParamDoc(name="Orientation angle") int degree) { 
	
		return UnitUtils.getWindDirection(degree);
	}	

}

