/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Properties of a neo stat thermostate that can be bound to an openhab item.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public enum NeoStatProperty {
	CurrentTemperature("CurrentTemperature"), CurrentSetTemperature(
			"CurrentSetTemperature"), CurrentFloorTemperature(
			"CurrentFloorTemperature"), DeviceName("DeviceName"), Heating(
			"Heating"), Away("Away"), Standby("Standby");

	/**
	 * A string constant used in the configuration file to identify the
	 * property. Note: Not using enum name here, so that name can be refactored
	 * without impacting existing configurations.
	 */
	public final String binding;

	private NeoStatProperty(final String b) {
		binding = b;
	}

	/**
	 * Converts given binding string to the matching property.
	 * 
	 * @param b
	 *            binding string
	 * @return matching property or <code>null</code>
	 */
	public static NeoStatProperty fromBinding(final String b) {
		for (NeoStatProperty p : NeoStatProperty.values()) {
			if (p.binding.equals(b)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets binding strings.
	 * 
	 * @return list of all possible binding strings.
	 */
	public static List<String> getBindings() {
		return Lists.transform(Arrays.asList(NeoStatProperty.values()),
				new Function<NeoStatProperty, String>() {

					@Override
					public String apply(NeoStatProperty property) {
						return property.binding;
					}
				});
	}
}
