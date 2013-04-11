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
package org.openhab.binding.homematic.internal.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class can parse information from the generic binding format and provides
 * Homematic binding information from it. It registers as a
 * {@link HomematicBindingProvider} service as well.
 * 
 * The syntax of the binding configuration strings accepted is the following: <br>
 * <br>
 * <b>Device items:</b><br>
 * <code>
 * 	homematic="&lt;address&gt;#&lt;parameterId (e.g. TEMPERATURE, HUMIDITY)&gt;"
 * </code> <br>
 * Examples:
 * <ul>
 * <li><code>homematic="IEQ0022806:1#TEMPERATURE"</code></li>
 * <li><code>homematic="IEQ0022806:1#HUMIDITY"</code></li>
 * </ul>
 * <br>
 * <br>
 * <b>Admin items:</b><br>
 * <code>
 *  homematic="ADMIN:&lt;command&gt;"
 * </code><br>
 * Examples:
 * <ul>
 * <li><code>homematic="ADMIN:DUMP_UNCONFIGURED_DEVICES"</code></li>
 * <li><code>homematic="ADMIN:DUMP_UNSUPPORTED_DEVICES"</code></li>
 * </ul>
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicGenericBindingProvider extends AbstractGenericBindingProvider implements HomematicBindingProvider {

    private Map<String, Item> items = new HashMap<String, Item>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "homematic";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    }

    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        HomematicBindingConfig config = new HomematicBindingConfig();
        if (bindingConfig.startsWith(AdminItem.PREFIX)) {
            config.adminItem = AdminItem.fromBindingConfig(bindingConfig);
        } else {
            config.parameterAddress = ParameterAddress.fromBindingConfig(bindingConfig);
        }
        addBindingConfig(item, config);
    }

    @Override
    protected void addBindingConfig(Item item, BindingConfig config) {
        super.addBindingConfig(item, config);
        items.put(item.getName(), item);
    }

    @Override
    public void removeConfigurations(String context) {
        Set<Item> items = contextMap.get(context);
        if (items != null) {
            for (Item item : items) {
                // TODO: Can there be two items with the same name in two different contexts?
                items.remove(item.getName());
            }
        }
        super.removeConfigurations(context);
    }

    @Override
    public ParameterAddress getParameterAddress(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.parameterAddress : null;
    }

    @Override
    public AdminItem getAdminItem(String itemName) {
        HomematicBindingConfig config = (HomematicBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.adminItem : null;
    }

    @Override
    public boolean isAdminItem(String itemName) {
        return getAdminItem(itemName) != null;
    }

    @Override
    public Item getItem(String itemName) {
        // PRESS_LONG and PRESS_LONG_RELEASE should go to the same Item!
        if(itemName.equals(ParameterKey.PRESS_LONG_RELEASE.name())) {
            return items.get(ParameterKey.PRESS_LONG.name());
        }
        return items.get(itemName);
    }

    /**
     * This is an internal data structure to store information from the binding
     * config strings and use it to answer the requests to the Homematic binding
     * provider.
     * 
     * @author Thomas Letsch (contact@thomas-letsch.de)
     */
    private class HomematicBindingConfig implements BindingConfig {
        private ParameterAddress parameterAddress;
        private AdminItem adminItem;
    }

}
