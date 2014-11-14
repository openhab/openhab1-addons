/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder;

import java.util.ArrayList;
import org.openhab.binding.alarmdecoder.internal.ADMsgType;
import org.openhab.binding.alarmdecoder.internal.AlarmDecoderBindingConfig;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
/**
 * This interface is implemented by classes that provide per-item binding configuration
 * 
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public interface AlarmDecoderBindingProvider extends AutoUpdateBindingProvider {

	/**
	 * Get the binding configuration for a given itemName. This method
	 * is useful when sending a command, where the item name is known,
	 * and the corresponding device must be determined
	 * @param itemName
	 * @return the binding configuration for the item or null if not found
	 */
	public AlarmDecoderBindingConfig getBindingConfig(String itemName);

	/**
	 * Find all the configurations that reference a given message type and address.
	 * If a null address is given, all configurations with the given message type
	 * are returned. This method is useful to find the items that need to be
	 * updated when a message comes in.
	 * 
	 * @param mt the message type to look for (or all types if null)
	 * @param addr the address to narrow it down to (or all addresses if null)
	 * @param feature the feature to narrow it down to (or all features if null)
	 * @return a (potentially empty) list of binding configs that reference (mt, addr, feature)
	 */
	
	public ArrayList<AlarmDecoderBindingConfig> getConfigurations(ADMsgType mt, String addr,
			String feature);

}
