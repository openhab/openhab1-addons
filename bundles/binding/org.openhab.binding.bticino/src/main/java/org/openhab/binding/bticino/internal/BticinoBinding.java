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
package org.openhab.binding.bticino.internal;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class implements a binding of serial devices to openHAB. The binding
 * configurations are provided by the {@link GenericItemProvider}.
 * </p>
 * 
 * <p>
 * The format of the binding configuration is simple and looks like this:
 * </p>
 * serial="&lt;port&gt;" where &lt;port&gt; is the identification of the serial
 * port on the host system, e.g. "COM1" on Windows, "/dev/ttyS0" on Linux or
 * "/dev/tty.PL2303-0000103D" on Mac
 * <p>
 * Switch items with this binding will receive an ON-OFF update on the bus,
 * whenever data becomes available on the serial interface<br/>
 * String items will receive the submitted data in form of a string value as a
 * status update, while openHAB commands to a Switch item is sent out as data
 * through the serial interface.
 * </p>
 * 
 * @author Kai Kreuzer
 * 
 */
public class BticinoBinding extends AbstractEventSubscriber implements
		BindingConfigReader, ManagedService
{

	private static final Logger logger = LoggerFactory
			.getLogger(BticinoBinding.class);

	/**
	 * RegEx to validate a bticino gateway config
	 * <code>'^(.*?)\\.(host|port)$'</code>
	 */
	private static final Pattern EXTRACT_BTICINO_GATEWAY_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(host|port)$");

	private Map<String, BticinoDevice> bticinoDevices = new HashMap<String, BticinoDevice>();

	/**
	 * stores information about the which items are associated to which port.
	 * The map has this content structure: itemname -> port
	 */
	private Map<String, String> itemMap = new HashMap<String, String>();

	// indicates that the updated has been run once
	boolean m_binding_initialized = false;
	
	/**
	 * stores information about the context of items. The map has this content
	 * structure: context -> Set of itemNames
	 */
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	private EventPublisher eventPublisher = null;

	private Map<String, BticinoConfig> m_bticino_config_cache = new HashMap<String, BticinoConfig>();

	static class BticinoConfig
	{
		String id;
		String host;
		int port;

		@Override
		public String toString()
		{
			return "Bticino [id=" + id + ", host=" + host + ", port=" + port
					+ "]";
		}
	}

	public void setEventPublisher(EventPublisher eventPublisher)
	{
		this.eventPublisher = eventPublisher;

		for (BticinoDevice serialDevice : bticinoDevices.values())
		{
			serialDevice.setEventPublisher(eventPublisher);
		}
	}

	public void unsetEventPublisher(EventPublisher eventPublisher)
	{
		this.eventPublisher = null;

		for (BticinoDevice serialDevice : bticinoDevices.values())
		{
			serialDevice.setEventPublisher(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command)
	{
		if (itemMap.keySet().contains(itemName))
		{
			BticinoDevice l_bticino_device = bticinoDevices.get(itemMap
					.get(itemName));
			l_bticino_device.receiveCommand(itemName, command);

		} else
		{
			// just to know that something is wrong
			logger.error("Item ["
					+ itemName
					+ "] is not handled by any openweb gateway, possible programming bug! Handled items : " + Arrays.toString(itemMap.keySet().toArray()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveUpdate(String itemName, State newStatus)
	{
		// ignore any updates
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType()
	{
		return "bticino";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException
	{
		if (!(item instanceof SwitchItem || item instanceof RollershutterItem))
		{
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', this Items is not allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException
	{
		// bticino="if=0;who=1;what=1;where=23"
		// if => support for multiple MH200 devices (= several houses throug
		// VPN)

		HashMap<String, String> l_bticino_binding_config = bticinoBindingConfigDecompose(bindingConfig);

		// the gateway name is defined with the "if" property
		// when this is not defined, we revert to default (this
		// must be present in openhab*.cfg
		String l_gw_if_id = "default";

		if (l_bticino_binding_config.containsKey("if"))
			l_gw_if_id = l_bticino_binding_config.get("if");

		BticinoDevice l_bticino_device = bticinoDevices.get(l_gw_if_id);

		if (l_bticino_device != null)
		{

			// remember which if will handle which item
			itemMap.put(item.getName(), l_gw_if_id);

			l_bticino_device.addItem(item, l_bticino_binding_config);

			Set<String> itemNames = contextMap.get(context);
			if (itemNames == null)
			{
				itemNames = new HashSet<String>();
				contextMap.put(context, itemNames);
			}
			itemNames.add(item.getName());
		} else
		{
			throw new BindingConfigParseException(
					"Could not get BTicino interface with ID [" + l_gw_if_id
							+ "] that is needed for Item [" + item.getName()
							+ "]");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConfigurations(String context)
	{
		Set<String> itemNames = contextMap.get(context);
		if (itemNames != null)
		{
			for (String itemName : itemNames)
			{
				// we remove all information in the serial devices
				BticinoDevice l_bticino_device = bticinoDevices.get(itemMap
						.get(itemName));
				itemMap.remove(itemName);
				if (l_bticino_device == null)
				{
					continue;
				}
				l_bticino_device.removeItem(itemName);
				// if there is no binding left, dispose this device
				// l_bticino_device.close();
			}
			contextMap.remove(context);
		}
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException
	{
		if (!m_binding_initialized)
		{
			m_binding_initialized = true;
			// We will read every configuration key, and encounter
			// hostname, port for the configured bticino gateways
			Enumeration keys = config.keys();
			while (keys.hasMoreElements())
			{
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...

				if ("service.pid".equals(key))
				{
					continue;
				}

				Matcher matcher = EXTRACT_BTICINO_GATEWAY_CONFIG_PATTERN
						.matcher(key);

				if (!matcher.matches())
				{
					logger.debug("given bticino gateway-config-key '"
							+ key
							+ "' does not follow the expected pattern '<gateway_name>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String l_gw_if_id = matcher.group(1);

				BticinoConfig l_bticino_config = m_bticino_config_cache
						.get(l_gw_if_id);
				if (l_bticino_config == null)
				{
					l_bticino_config = new BticinoConfig();
					l_bticino_config.id = l_gw_if_id;
					m_bticino_config_cache.put(l_gw_if_id, l_bticino_config);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey))
				{
					l_bticino_config.host = value;
				} else if ("port".equals(configKey))
				{
					l_bticino_config.port = Integer.valueOf(value);
				} else
				{
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey
									+ "' is unknown");
				}
			}

			// Now for all the bticino gateways configured in the configuration,
			// connect to the physical gateways
			connectAllBticinoDevices();

			// Now start all the bticino gateways
			startAllBticinoDevices();
		}
	}

	private void connectAllBticinoDevices() throws ConfigurationException
	{
		for (String l_gw_if_id : m_bticino_config_cache.keySet())
		{
			BticinoConfig l_current_device_config = m_bticino_config_cache
					.get(l_gw_if_id);

			BticinoDevice l_bticino_device = bticinoDevices.get(l_gw_if_id);

			if (l_bticino_device == null)
			{
				// Create a gw service object
				l_bticino_device = new BticinoDevice(l_current_device_config.id);
				l_bticino_device.setEventPublisher(eventPublisher);
				l_bticino_device.setHost(l_current_device_config.host);
				l_bticino_device.setPort(l_current_device_config.port);
				try
				{
					l_bticino_device.initialize();
				} catch (InitializationException e)
				{
					throw new ConfigurationException(l_gw_if_id,
							"Could not open create BTicino interface with ID ["
									+ l_gw_if_id + "], Exception : "
									+ e.getMessage());
				} catch (Throwable e)
				{
					throw new ConfigurationException(l_gw_if_id,
							"Could not open create BTicino interface with ID ["
									+ l_gw_if_id + "], Exception : "
									+ e.getMessage());
				}
				bticinoDevices.put(l_gw_if_id, l_bticino_device);
			}
		}
	}

	private void startAllBticinoDevices() throws ConfigurationException
	{
		for (String l_gw_if_id : m_bticino_config_cache.keySet())
		{
			BticinoConfig l_current_device_config = m_bticino_config_cache
					.get(l_gw_if_id);

			BticinoDevice l_bticino_device = bticinoDevices.get(l_gw_if_id);

			if ((l_bticino_device != null)
					&& (!l_bticino_device.isDeviceStarted()))
			{
				l_bticino_device.startDevice();
			}
		}
	}

	static private HashMap<String, String> bticinoBindingConfigDecompose(
			String p_binding_config)
	{
		HashMap<String, String> l_configuration_hm = new HashMap<String, String>();
		// who=1;what=1;where=23
		String[] l_key_value_pairs = p_binding_config.split(";");
		for (int l_idx = 0; l_idx < l_key_value_pairs.length; l_idx++)
		{
			String[] l_key_value = l_key_value_pairs[l_idx].split("=");
			l_configuration_hm.put(l_key_value[0], l_key_value[1]);
		}
		return l_configuration_hm;
	}

}