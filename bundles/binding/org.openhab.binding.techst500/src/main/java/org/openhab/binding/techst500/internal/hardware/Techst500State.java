/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.techst500.internal.hardware;

import org.openhab.binding.techst500.internal.hardware.Techst500Proxy.Day;

/**
 * state
 * 
 * @author Eric Thill
 * @author Stephan Noerenberg
 * @since 1.6.0
 */
public class Techst500State {
	
	private final float chTemperature;
	private final float chTemperatureShould;
	private final float fanSpeed;
	private final boolean fanOperates;
	private final boolean feederOperates;
	private final boolean chPumpOperates;
	private final boolean wwPumpOperates;
	private final float htwTemperature;
	private final String currentTime;
	private final Day currentDay;
	private final float wwTemperature;
	private final float outsideTemperature;
	private final float feederTemperature;
	
	public Techst500State(float chTemperature, float chTemperatureShould, float fanSpeed, boolean fanOperates, boolean feederOperates, boolean chPumpOperates, boolean wwPumpOperates, float htwTemperature, String currentTime, Day currentDay, float wwTemperature, float outsideTemperature, float feederTemperature) {
		this.chTemperature = chTemperature;
		this.chTemperatureShould = chTemperatureShould;
		this.fanSpeed = fanSpeed;
		this.fanOperates = fanOperates;
		this.feederOperates = feederOperates;
		this.chPumpOperates = chPumpOperates;
		this.wwPumpOperates = wwPumpOperates;
		this.htwTemperature = htwTemperature;
		this.currentTime = currentTime;
		this.currentDay = currentDay;
		this.wwTemperature = wwTemperature;
		this.outsideTemperature = outsideTemperature;
		this.feederTemperature = feederTemperature;
	}
	
	public float getChTemperature() {
		return chTemperature;
	}
	
	public float getChTemperatureShould() {
		return chTemperatureShould;
	}
	
	public float getFanSpeed() {
		return fanSpeed;
	}
	
	public boolean isFanOperating() {
		return fanOperates;
	}

	public boolean isFeederOperating() {
		return feederOperates;
	}

	public boolean isChPumpOperating() {
		return chPumpOperates;
	}

	public boolean isWwPumpOperating() {
		return wwPumpOperates;
	}

	public float getHtwTemperature() {
		return htwTemperature;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public Day getCurrentDay() {
		return currentDay;
	}

	public float getWwTemperature() {
		return wwTemperature;
	}

	public float getOutsideTemperature() {
		return outsideTemperature;
	}

	public float getFeederTemperature() {
		return feederTemperature;
	}
}
