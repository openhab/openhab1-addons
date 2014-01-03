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

import it.cicolella.openwebnet.IBticinoEventListener;
import it.cicolella.openwebnet.OpenWebNet;
import it.cicolella.openwebnet.ProtocolRead;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a serial device that is linked to exactly one String
 * item and/or Switch item.
 * 
 * @author Kai Kreuzer
 * 
 */
public class BticinoDevice implements IBticinoEventListener
{

	private String m_gateway_identifier = "";
	private String m_host = "";
	private int m_port = 0;
	private boolean m_device_is_started = false;
	private Object m_lock = new Object();

	private OpenWebNet m_open_web_net;

	private static final Logger logger = LoggerFactory
			.getLogger(BticinoDevice.class);

	// itemname, item object
	private Map<String, Item> m_item_map = new HashMap<String, Item>();
	// itemname, item bticino config (key-values
	// if=default;who=1;what=1;where=23)
	private Map<String, HashMap<String, String>> m_item_config_map = new HashMap<String, HashMap<String, String>>();
	// bticino point (who (light, heating, ...) ";" where (adress of the light)
	// , itemname
	// this is used to find a match between te events on the bus and the device
	// only one unique is allowed (could give problem when eg a timed event for
	// light and dimmer and onoff)
	private Map<String, List<String>> m_btchino_device_to_item_map = new HashMap<String, List<String>>();

	private EventPublisher eventPublisher;

	public BticinoDevice(String p_gateway_id)
	{
		m_gateway_identifier = p_gateway_id;
	}

	public void setHost(String p_host)
	{
		m_host = p_host;
	}

	public void setPort(int p_port)
	{
		m_port = p_port;
	}

	public void setEventPublisher(EventPublisher eventPublisher)
	{
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher)
	{
		this.eventPublisher = null;
	}

	public void addItem(Item p_item,
			HashMap<String, String> p_bticino_binding_config)
	{
		m_item_map.put(p_item.getName(), p_item);
		m_item_config_map.put(p_item.getName(), p_bticino_binding_config);

		String l_hash_key = p_bticino_binding_config.get("who") + ";"
				+ p_bticino_binding_config.get("where");

		List<String> l_list;

		if (!m_btchino_device_to_item_map.containsKey(l_hash_key))
		{
			l_list = new LinkedList<String>();
			m_btchino_device_to_item_map.put(l_hash_key, l_list);
		} else
		{
			l_list = m_btchino_device_to_item_map.get(l_hash_key);
		}
		l_list.add(p_item.getName());

	}

	public Item removeItem(String p_item_name)
	{
		m_item_config_map.remove(p_item_name);
		return (m_item_map.remove(p_item_name));
	}

	public boolean hasItems()
	{
		return (m_item_map.isEmpty());
	}

	/**
	 * Initialize this device and open the serial port
	 * 
	 * @throws InitializationException
	 *             if port can not be opened
	 */
	public void initialize() throws InitializationException
	{
		// throw new InitializationException("Serial port '" + port +
		// "' could not be found. Available ports are:\n" + sb.toString());

		logger.debug("initialize OK");
	}

	public void startDevice()
	{
		if (m_open_web_net == null)
		{
			m_open_web_net = new OpenWebNet(m_host, m_port);
			m_open_web_net.addEventListener(this);
			m_open_web_net.onStart();
		}

		m_device_is_started = true;
	}

	public void stopDevice()
	{
		if (m_open_web_net != null)
		{
			m_open_web_net.onStop();
			m_open_web_net = null;
		}
		m_device_is_started = false;
	}

	public boolean isDeviceStarted()
	{
		return m_device_is_started;
	}

	public void receiveCommand(String itemName, Command command)
	{
		try
		{
			synchronized (m_lock)
			{
				// An command is received from the openHab system
				// analyse it and execute it
				logger.debug("Command '{}' received for item {}", new String[] {
						command.toString(), itemName });

				// TODO : instead of working with this string to map, use an
				// object
				// representation
				HashMap<String, String> l_item_config_map = m_item_config_map
						.get(itemName);
				// TODO : check for existance

				ProtocolRead l_pr = new ProtocolRead(
						l_item_config_map.get("who") + "*"
								+ l_item_config_map.get("where"));
				l_pr.addProperty("who", l_item_config_map.get("who"));
				l_pr.addProperty("address", l_item_config_map.get("where"));

				int l_who = Integer.parseInt(l_item_config_map.get("who"));
				switch (l_who)
				{
				// Lights
				case 1:
				{
					if (OnOffType.ON.equals(command))
						l_pr.addProperty("what", "1");
					else
						l_pr.addProperty("what", "0");
					break;
				}
				// Shutter
				case 2:
				{
					if (UpDownType.UP.equals(command))
						l_pr.addProperty("what", "1");
					else if (UpDownType.DOWN.equals(command))
						l_pr.addProperty("what", "2");
					else if (StopMoveType.STOP.equals(command))
						l_pr.addProperty("what", "0");
					break;
				}
				}

				m_open_web_net.onCommand(l_pr);
			}
		} catch (Exception e)
		{
			logger.error("Error processing receiveCommand '{}'",
					new String[] { e.getMessage() });
		}

	}

	public void handleEvent(ProtocolRead p_protocol_read) throws Exception
	{
		// the events on the bus are now received
		// map them to events on the openhab bus

		// who + adress => komt dit voor in de lijst met Items?
		// dan voor alle Items het correcte type zetten adv het ontvangen event
		logger.debug("Bticino WHO [" + p_protocol_read.getProperty("who")
				+ "], WHAT [" + p_protocol_read.getProperty("what")
				+ "], WHERE [" + p_protocol_read.getProperty("where") + "]");

		String l_hash_key = p_protocol_read.getProperty("who") + ";"
				+ p_protocol_read.getProperty("where");

		if (m_btchino_device_to_item_map.containsKey(l_hash_key))
		{
			logger.debug("RECEIVED EVENT FOR Item, TRANSLATE TO OPENHAB BUS EVENT");

			// Get every Item object linked to this bticino item
			List<String> l_item_names = m_btchino_device_to_item_map
					.get(l_hash_key);

			// depending on every item, generate the correct event
			for (String l_item_name : l_item_names)
			{
				Item l_item = m_item_map.get(l_item_name);

				if (l_item instanceof SwitchItem)
				{
					if (p_protocol_read.getProperty("messageType")
							.equalsIgnoreCase("lighting"))
					{
						if (p_protocol_read.getProperty("messageDescription")
								.equalsIgnoreCase("Light ON"))
						{
							eventPublisher
									.postUpdate(l_item_name, OnOffType.ON);
						} else if (p_protocol_read.getProperty(
								"messageDescription").equalsIgnoreCase(
								"Light OFF"))
						{
							eventPublisher.postUpdate(l_item_name,
									OnOffType.OFF);
						}
					}
				} else if (l_item instanceof RollershutterItem)
				{
					if (p_protocol_read.getProperty("messageType")
							.equalsIgnoreCase("automation"))
					{

						if (p_protocol_read.getProperty("messageDescription")
								.equalsIgnoreCase("Automation STOP"))
						{
							// eventPublisher
							// .postUpdate(l_item_name, StopMoveType.STOP);
						} else if (p_protocol_read.getProperty(
								"messageDescription").equalsIgnoreCase(
								"Automation UP"))
						{
							eventPublisher.postUpdate(l_item_name,
									UpDownType.UP);
						} else if (p_protocol_read.getProperty(
								"messageDescription").equalsIgnoreCase(
								"Automation DOWN"))
						{
							eventPublisher.postUpdate(l_item_name,
									UpDownType.DOWN);
						}
					}
				}
			}

			// // send data to the bus
			// logger.debug("Received message '{}' on serial port {}", new
			// String[]
			// { result, port });
			// if (eventPublisher != null && stringItemName != null) {
			// eventPublisher.postUpdate(stringItemName, new
			// StringType(result));
			// }
			// // if we receive empty values, we treat this to be a switch
			// operation
			// if (eventPublisher != null && switchItemName != null &&
			// result.trim().isEmpty()) {
			// eventPublisher.postUpdate(switchItemName, OnOffType.ON);
			// eventPublisher.postUpdate(switchItemName, OnOffType.OFF);
			// }

		}
	}
}
