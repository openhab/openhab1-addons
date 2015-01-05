/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of a openHAB item that is bound to a Hue
 * Tap switch. It contains the following information:
 * 
 * <ul>
 * <li>The device number the bulb has on the Hue bridge. The bulbs should have
 * numbers from 1 up to the number of connected bulbs.</li>
 * </ul>
 * 
 * @author Gernot Eger
 * @since 1.7.0
 */
public class HueTapBindingConfig extends AbstractHueBindingConfig implements BindingConfig {

	static final Logger logger = LoggerFactory.getLogger(HueTapBindingConfig.class);

	/**
	 * The binding type of the hue item.
	 * <ul>
	 * <li>switch1 - . </li>
	 * <li>switch2 - : </li>
	 * <li>switch3 - :. </li>
	 * <li>switch4 - :: </li>
	 * </ul>
	 */
	public enum SwitchId {
		switch1, switch2, switch3, switch4
		
		
	}
	
	/**
	 * The switch number 1-4 of the tap device
	 */
	protected final SwitchId switchId;
	
	/**
	 * Constructor of the HueTapBindingConfig.
	 * 
	 * @param deviceNumber
	 *            The number under which the tap device is filed in the Hue bridge.
	 *            
	 * @throws BindingConfigParseException
	 */
	public HueTapBindingConfig(String deviceNumber,String switchNumber)
			throws BindingConfigParseException {
		
		super(deviceNumber);
		switchId=parseSwitchIdConfigString(switchNumber);
		
	}
	
	
	/**
	 * Parses switch string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The switch id [1..4] as a string.
	 * @return The step size as an integer value.
	 * @throws BindingConfigParseException
	 */
	private SwitchId parseSwitchIdConfigString(String configString)
			throws BindingConfigParseException {
		
		switch(configString){
			case "1":	return SwitchId.switch1;
			case "2":	return SwitchId.switch2;
			case "3":	return SwitchId.switch3;
			case "4":	return SwitchId.switch4;
			default: throw new BindingConfigParseException("Error parsing switch id '"+configString+"'");
		}		
	}
	
}
