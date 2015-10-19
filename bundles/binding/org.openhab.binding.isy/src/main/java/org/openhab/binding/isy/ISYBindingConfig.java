/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * @author Tim Diekmann
 *
 */
public class ISYBindingConfig implements BindingConfig {
	/**
	 * Type of ISY node
	 */
	public enum Type {
		SWITCH, GROUP, CONTACT, THERMOSTAT, NUMBER
	}

	public Item item;
	public Type type;
	/**
	 * Address used to send commands to, e.g. node or scene
	 */
	public String controller;
	/**
	 * Address reporting back the state for a node or a scene. For a scene this
	 * is the controller
	 */
	public String address;

	/**
	 * ISY command associated with the node. ISY SDK calls them UDControl
	 */
	public ISYControl cmd = ISYControl.ST;
}
