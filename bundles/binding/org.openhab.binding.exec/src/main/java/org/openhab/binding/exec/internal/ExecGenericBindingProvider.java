/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.exec.internal;

import java.util.HashMap;

import org.openhab.binding.exec.ExecBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Exec binding information from it. It registers as a 
 * {@link ExecBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ exec="ON:ssh user@openhab.org touch ~/test.txt" }</code> - connect to openhab.org via ssh and issue the command 'touch ~/test.txt'</li>
 * 	<li><code>{ exec="OFF:ssh teichsta@openhab.org shutdown -p now" }</code></li>
 *  <li><code>{ exec="OFF:ssh teichsta@wlan-router ifconfig wlan0 down" }</code></li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 0.6.0
 */
public class ExecGenericBindingProvider extends AbstractGenericBindingProvider implements ExecBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "exec";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (item instanceof SwitchItem) {
			
			// split the given commands
			String[] configParts = bindingConfig.trim().split(",");
			
			ExecBindingConfig config = new ExecBindingConfig();
			
			for (String configPart : configParts) {
				
				// split command and commandLine
				String[] configPartDetails = configPart.trim().split(":");
				if (configPartDetails.length != 2) {
					throw new BindingConfigParseException("each Exec configuration must contain two parts");
				}
				
				String command = configPartDetails[0];
				String commandLine = configPartDetails[1];
				
				config.put(command, commandLine);
			}
			
			addBindingConfig(item, config);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandLine(String itemName, String command) {
		ExecBindingConfig config = (ExecBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command) : null;
	}
		
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Exec
	 * binding provider.
	 */
	private class ExecBindingConfig extends HashMap<String, String> implements BindingConfig {
		
        /** generated serialVersion UID */
		private static final long serialVersionUID = 6164971643530954095L;
		
	}


}
