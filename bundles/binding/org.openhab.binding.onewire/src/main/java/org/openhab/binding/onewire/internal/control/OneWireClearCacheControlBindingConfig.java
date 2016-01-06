/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.control;

import org.openhab.binding.onewire.internal.OneWireBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This Class is a control for onewire binding. This control can clear the ItemStateCache
 * 
 * If you bind this to a Switch item, when the Switch received command ON, then the whole cache gets cleared.
 * If this class is bind to a String item, you have to send the name of an item as command, so the cached value of
 * this item will be removed.
 * 
 * Examples:
 * <ul>
 * <li><code>Switch OneWireClearCache "OneWireClearCache" {onewire="control=CLEAR_CACHE"}</code></li>
 * <li><code>String OneWireClearCacheOneItem "OneWireClearCacheOneItem" {onewire="control=CLEAR_CACHE"}</code></li>
 * </ul>
 * 
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireClearCacheControlBindingConfig extends AbstractOneWireControlBindingConfig {

	public OneWireClearCacheControlBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super(pvBindingConfig);
	}
	
	/**
	 * Checks, if this special binding-type matches to the given pvBindingConfig
	 * 
	 * @param pvItem
	 * @param pvBindingConfig
	 * @return boolean
	 */
	public static boolean isBindingConfigToCreate(Item pvItem, String pvBindingConfig) {
		return ((pvItem instanceof StringItem || pvItem instanceof SwitchItem) && (pvBindingConfig.contains("control=CLEAR_CACHE")));
	}
	
	@Override
	public void executeControl(OneWireBinding pvOneWireBinding, Command pvCommand) {
		if (pvCommand.equals(OnOffType.ON)) {
			pvOneWireBinding.clearCacheItemState();
		} else if (pvCommand instanceof StringType) {
			String lvItemName = pvCommand.toString();
			if (lvItemName!=null && !lvItemName.trim().equals("")) {
				pvOneWireBinding.clearCacheItemState(lvItemName);
			}
		}
	}

	@Override
	public String toString() {
		return "OneWireClearCacheControlBindingConfig []";
	}

}
