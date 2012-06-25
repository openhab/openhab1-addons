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
package org.openhab.io.dropbox.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.io.dropbox.DropboxBindingProvider;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Dropbox binding information from it. It registers as a 
 * {@link DropboxBindingProvider} service as well.</p>
 * 
 * <p>The only valid binding configuration string is:
 * <ul>
 * 	<li><code>{ dropbox="" }</code> - xxx</li>
 * </ul>
 * 
 * These binding configurations can be used on either SwitchItems or StringItems.
 * With SwitchItems one might deactivate synchronization at all, whereas with 
 * StringItems one might configure the synchronization mode.
 * </p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxGenericBindingProvider extends AbstractGenericBindingProvider implements DropboxBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(DropboxGenericBindingProvider.class);
	
	public DropboxGenericBindingProvider() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "dropbox";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			DropboxBindingConfig config = new DropboxBindingConfig();
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
	
	static class DropboxBindingConfig implements BindingConfig {
	}

}
