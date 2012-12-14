/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.plcbus.internal;

import org.openhab.binding.plcbus.PLCBusBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Implementation of a PLCBusBindingProvider
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <p>
 * <code>
 * 	plcbus="&lt;userCode&gt; &lt;unit&gt;"
 * </code>
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>plcbus="B2 A1"</code></li>
 * </ul>
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusGenericBindingProvider extends
		AbstractGenericBindingProvider implements PLCBusBindingProvider,
		AutoUpdateBindingProvider {

	@Override
	public String getBindingType() {
		return "plcbus";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// all types of items are valid ...
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		PLCBusBindingConfig config = new PLCBusBindingConfig(bindingConfig);
		addBindingConfig(item, config);
	}

	@Override
	public Boolean autoUpdate(String itemName) {
		return true;
	}

	@Override
	public PLCBusBindingConfig getConfigFor(String itemName) {
		return (PLCBusBindingConfig) bindingConfigs.get(itemName);
	}

}
