/**
 * openHAB, the open Home Automation Bus. Copyright (C) 2010-2013, openHAB.org
 * <admin@openhab.org>
 * 
 * See the contributors.txt file in the distribution for a full listing of
 * individual contributors.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining it
 * with Eclipse (or a modified version of that library), containing parts
 * covered by the terms of the Eclipse Public License (EPL), the licensors of
 * this Program grant you additional permission to convey the resulting work.
 */
package org.openhab.binding.insteonhub.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Insteon Hub item binding configuration
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBindingConfig implements BindingConfig {

	public static final String KEY_BINDING_TYPE = "bindingType";
	public static final String KEY_HUB_ID = "hubid";
	public static final String KEY_DEVICE = "device";
	
	public enum BindingType {
		digital, analog;
	}

	private final String hubId;
	private final String device;
	private final BindingType bindingType;
	private final String itemName;

	public InsteonHubBindingConfig(String hubId, String device, String bindingType,
			String itemName) throws BindingConfigParseException {
		this.hubId = hubId != null ? hubId
				: InsteonHubBinding.DEFAULT_HUB_ID;
		if(device == null) {
			throw new IllegalArgumentException("device cannot be null");
		}
		this.device = parseDevice(device);
		this.bindingType = parseBindingType(bindingType);
		this.itemName = itemName;
	}
	
	private static String parseDevice(String device) {
		device = device.trim();
		return device.replace(".", "");
	}

	private static BindingType parseBindingType(String str)
			throws BindingConfigParseException {
		if (str == null) {
			throw new BindingConfigParseException(KEY_BINDING_TYPE);
		}
		try {
			return BindingType.valueOf(str);
		} catch (Exception e) {
			throw new BindingConfigParseException("error parsing "
					+ KEY_BINDING_TYPE);
		}
	}

	public String getHubId() {
		return hubId;
	}
	
	public String getDevice() {
		return device;
	}

	public BindingType getBindingType() {
		return bindingType;
	}

	public String getItemName() {
		return itemName;
	}
}
