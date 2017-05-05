/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.temperlan.internal.hardware;

/**
 * Receiver state
 * 
 * @author Stephan Noerenberg
 * @since 1.6.0
 */
public class TemperlanState {
	
	private final float  temperature1;
	private final float  temperature2;
	private final float  temperature3;
	private final float  temperature4;
	private final float  temperature5;
	private final float  temperature6;
	private final float  temperature7;
	private final float  temperature8;
	private final float  temperature9;
	private final float  temperature10;
	private final float  temperature11;
	private final float  temperature12;

	
	public TemperlanState(float temperature1,float temperature2,float temperature3,float temperature4,float temperature5,float temperature6,float temperature7,float temperature8,float temperature9,float temperature10,float temperature11,float temperature12) {
		this.temperature1 = temperature1;
		this.temperature2 = temperature2;
		this.temperature3 = temperature3;
		this.temperature4 = temperature4;
		this.temperature5 = temperature5;
		this.temperature6 = temperature6;
		this.temperature7 = temperature7;
		this.temperature8 = temperature8;
		this.temperature9 = temperature9;
		this.temperature10 = temperature10;
		this.temperature11 = temperature11;
		this.temperature12 = temperature12;
	}
	
	public float getTemperature1() { return temperature1;}
	public float getTemperature2() { return temperature2;}
	public float getTemperature3() { return temperature3;}
	public float getTemperature4() { return temperature4;}
	public float getTemperature5() { return temperature5;}
	public float getTemperature6() { return temperature6;}
	public float getTemperature7() { return temperature7;}
	public float getTemperature8() { return temperature8;}
	public float getTemperature9() { return temperature9;}
	public float getTemperature10() { return temperature10;}
	public float getTemperature11() { return temperature11;}
	public float getTemperature12() { return temperature12;}

}
