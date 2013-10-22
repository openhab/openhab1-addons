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
package org.openhab.binding.tcpsimple.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.tcpsimple.TCPSimpleBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public class TCPSimpleBinding extends
		AbstractActiveBinding<TCPSimpleBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(TCPSimpleBinding.class);

	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");

	private static final Pattern EXTRACT_PACKET_PATTERN = Pattern
			.compile(".*?<(.*?):(.*?),(.*?):(.*?)>");

	private MessageListener eventListener = new MessageListener();

	private List<TCPSimpleConnectorConfig> connectorList = new ArrayList<TCPSimpleConnectorConfig>();

	/**
	 * the refresh interval which is used to poll values from the TCPSimple server
	 * (optional, defaults to 5000ms)
	 */
	private long refreshInterval = 5000;

	public TCPSimpleBinding() {
	}

	public void activate() {
		logger.debug("TCPSimple binding activated");
		super.activate();
	}

	public void deactivate() {
		logger.debug("TCPSimple binding deactivated");
		// deallocate Resources here that are no longer needed and
		// should be reset when activating this binding again
		stopListening();
	}

	private void listen() {
		stopListening();

		for (TCPSimpleConnectorConfig config : connectorList) {
			// Sanity check keep alive
			if(config.keepalivestring == null || config.keepalivestring.isEmpty()) {
				config.keepaliveperiod = 0;
			}

			TCPSimpleConnector connector = new TCPSimpleConnector();
			if (connector != null) {
				// Initialise the IP connection
				connector.addEventListener(eventListener);
				try {
					logger.debug("TCPSimple: Connecting to {}",
							config.name);
					connector.connect(config.address, config.port);
					config.connector = connector;
				} catch (IOException e) {
					logger.debug("Error connecting TCPSimple: {} : {}",
							config.name, e);
				}
			}
		}
	}

	private void stopListening() {
		for (TCPSimpleConnectorConfig config : connectorList) {
			if (config.connector != null) {
				config.connector.disconnect();
				config.connector.removeEventListener(eventListener);
				config.connector = null;
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "TCPSimple Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		long lDateTime = System.currentTimeMillis();
		// Check that the connector hasn't timed out
		for (TCPSimpleConnectorConfig config : connectorList) {
			if (config.connector != null) {
				logger.debug("TCPSimple: Checktimeout("+ config.name + ") - now:"+lDateTime+" then:"+config.connector.getLastReceive()+" dif:"+(config.connector.getLastReceive()-lDateTime));
				// Timeout
				if (lDateTime > config.connector.getLastReceive()
						+ config.restartperiod) {
					logger.debug("TCPSimple: Connection Timeout!");
					config.connector.disconnect();
					try {
						config.connector.connect(config.address, config.port);
					} catch (IOException e) {
						logger.debug("Error reconnecting TCPSimple: {} : {}",
								config.name, e);
					}
				}

				// Keep-alive...
				if(config.keepaliveperiod != 0 && lDateTime > config.lastKeepAlive + config.keepaliveperiod) {
					logger.debug("TCPSimple: sending keepalive to "+config.name);
					config.lastKeepAlive = lDateTime;
					String keepAlive = new String(config.keepalivestring);
					keepAlive += "\r\n";
					config.connector.sendMessage(keepAlive.getBytes());
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		
		TCPSimpleBindingProvider providerCmd = null;

		for (TCPSimpleBindingProvider provider : this.providers) {
			String variable = provider.getBindingVariable(itemName, command);
			if (variable != null) {
				providerCmd = provider;
				break;
			}
		}

		if (providerCmd == null) {
			logger.warn("No match for binding provider [itemName={}, command={}]", itemName, command);
			return;
		}

		logger.debug("TCPSimple command for {} to {}", itemName, providerCmd.toString());
		String connectorName = providerCmd.getConnector(itemName);

		for (TCPSimpleConnectorConfig config : connectorList) {
			if (config.connector != null && config.name.equals(connectorName)) {
				String addrname = providerCmd.getAddressName(itemName);
				String addrval = providerCmd.getAddress(itemName);
				String variable = providerCmd.getBindingVariable(itemName, command);
				String value = providerCmd.getBindingValue(itemName, command);
				String commandString = new String("<" + addrname + ":" + addrval + "," + variable + ":" + value + ">\r\n");

				logger.debug("Sending command to {}: {}", connectorName, commandString);

				config.connector.sendMessage(commandString.getBytes());
			}
		}

	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			Enumeration<String> keys = config.keys();

			if (connectorList == null) {
				connectorList = new ArrayList<TCPSimpleConnectorConfig>();
			}

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				TCPSimpleConnectorConfig deviceConfig = null;
				for (TCPSimpleConnectorConfig connector : connectorList) {
					if (connector.name.equals(deviceId)) {
						deviceConfig = connector;
					}
				}

				if (deviceConfig == null) {
					deviceConfig = new TCPSimpleConnectorConfig();
					deviceConfig.name = deviceId;
					connectorList.add(deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("address".equals(configKey)) {
					deviceConfig.address = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.parseInt(value);
				} else if ("restartperiod".equals(configKey)) {
					deviceConfig.restartperiod = Integer.parseInt(value);
				} else if ("keepaliveperiod".equals(configKey)) {
					deviceConfig.keepaliveperiod = Integer.parseInt(value);
				} else if ("keepalivestring".equals(configKey)) {
						deviceConfig.keepalivestring = value;
				} else {
					throw new ConfigurationException(configKey,
							"The given TCPSimple configKey '" + configKey
									+ "' is unknown");
				}
			}

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			listen();
			setProperlyConfigured(true);
		}
	}

	private class MessageListener implements TCPSimpleEventListener {

		@Override
		public void packetReceived(EventObject event, String packet) {
			TCPSimpleConnector source = (TCPSimpleConnector) event.getSource();
			TCPSimpleConnectorConfig inConnector = null;
			for (TCPSimpleConnectorConfig config : connectorList) {
				if (config.connector == source) {
					inConnector = config;
					break;
				}
			}

			if (inConnector == null) {
				logger.debug("TCPSimple: packet from unknown connector {}",
						packet);
				return;
			}

			logger.debug("TCPSimple: Rx from {}: {}", inConnector.name, packet);

			Matcher matcher = EXTRACT_PACKET_PATTERN.matcher(packet);

			if (!matcher.matches()) {
				logger.debug("TCPSimple: Packet not decoded -: " + packet);
				return;
			}

			matcher.reset();
			matcher.find();

			String addressName = matcher.group(1);
			String addressVal = matcher.group(2);
			String valueName = matcher.group(3);
			String valueVal = matcher.group(4);

			for (TCPSimpleBindingProvider provider : providers) {
				for (String itemName : provider
						.getInBindingItemNamesForConnector(inConnector.name)) {
					String strAddress = provider.getAddressName(itemName);
					if (strAddress == null)
						continue;

					// Check the address
					if (addressName.equals(provider.getAddressName(itemName)) == false)
						continue;

					if (addressVal.equals(provider.getAddress(itemName)) == false)
						continue;

					if (valueName.equals(provider
							.getInBindingVariable(itemName)) == false)
						continue;

					Class<? extends Item> itemType = provider
							.getItemType(itemName);
					State state = createState(itemType, valueVal);
					eventPublisher.postUpdate(itemName, state);
				}
			}

		}
	}

	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 * 
	 * @param itemType
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 *         or a {@link StringType} if <code>item</code> is <code>null</code>
	 */
	private State createState(Class<? extends Item> itemType,
			String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				return OpenClosedType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return OnOffType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'",
					itemType, transformedResponse);
			return StringType.valueOf(transformedResponse);
		}
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the binding provider.
	 */
	static class TCPSimpleConnectorConfig {
		public String name;
		public String address;
		public int port;
		public long restartperiod = 600000;
		public long keepaliveperiod = 0;
		public String keepalivestring;
		public TCPSimpleConnector connector = null;
		public long lastKeepAlive = 0;
	}
}
