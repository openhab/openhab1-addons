/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.fritzaha.internal.hardware.devices;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaQueryscriptUpdateSwitchCallback;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaReauthCallback;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaSwitchedOutlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Switch in outlet addressed via query script
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaQueryscriptSwitch implements FritzahaSwitchedOutlet {
	/**
	 * Host ID
	 */
	String host;
	/**
	 * Device ID
	 */
	String id;

	/**
	 * {@inheritDoc}
	 */
	public String getHost() {
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	static final Logger logger = LoggerFactory.getLogger(FritzahaQueryscriptSwitch.class);

	/**
	 * {@inheritDoc}
	 */
	public void setSwitchState(boolean onOff, String itemName, FritzahaWebInterface webIface) {
		logger.debug("Setting Switch with Device ID " + id + " to value " + (onOff ? "on" : "off"));
		String path = "net/home_auto_query.lua";
		String args = "xhr=1&command=SwitchOnOff&value_to_set=" + (onOff ? 1 : 0) + "&id=" + id;
		webIface.asyncPost(path, args, new FritzahaQueryscriptUpdateSwitchCallback(path, args, webIface,
				FritzahaReauthCallback.Method.GET, 1, itemName));
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateSwitchState(String itemName, FritzahaWebInterface webIface) {
		logger.debug("Getting Switch value for Device ID " + id);
		String path = "net/home_auto_query.lua";
		String args = "xhr=1&command=OutletStates&id=" + id;
		webIface.asyncGet(path, args, new FritzahaQueryscriptUpdateSwitchCallback(path, args, webIface,
				FritzahaReauthCallback.Method.GET, 1, itemName));
	}

	public FritzahaQueryscriptSwitch(String host, String id) {
		this.host = host;
		this.id = id;
	}
}
