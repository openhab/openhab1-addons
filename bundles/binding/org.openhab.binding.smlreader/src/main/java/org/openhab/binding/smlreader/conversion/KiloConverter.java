/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.conversion;

/**
* @author Mathias Gilhuber
* @since 1.7.0
* UnitConverter - divides the given value through 1000
*/
final public class KiloConverter implements IUnitConverter<Double> {

	/**
	* Constructor
	*/
	public KiloConverter(){
	}

	/**
	* Divide through 1000
	*/
	public Double convert(String value) throws NullPointerException, NumberFormatException{
		return Double.parseDouble(value) / 1000;
	}
	
	/**
	*
	*/
	public TargetConversion getPattern(){
		return TargetConversion.kilo;
	}
}