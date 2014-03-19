/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.owserver.internal;

import java.io.IOException;
import java.io.StringReader;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.owserver.OWServerBindingProvider;
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
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An active binding which requests the EDS OWServer data.
 *
 * This class parses the information from the EDS OW-Server XML file.
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public class OWServerBinding extends
		AbstractActiveBinding<OWServerBindingProvider> implements
		ManagedService {

	static final Logger logger = LoggerFactory.getLogger(OWServerBinding.class);

	/**
	 * the timeout to use for connecting to a given host (defaults to 5000
	 * milliseconds)
	 */
	private int timeout = 5000;

	/**
	 * the interval to find new refresh candidates (defaults to 1000
	 * milliseconds)
	 */
	private int granularity = 1000;

	/**
	 * the maximum duration of data in the cache (defaults to 1500 milliseconds)
	 */
	private int cacheDuration = 1500;

	/**
	 *  RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> 
	 */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host)$");

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	private Map<String, OWServerConfig> serverList = new HashMap<String, OWServerConfig>();

	public OWServerBinding() {
	}

	@Override
	public void activate() {
		logger.debug("OWServer: Activate");

		super.activate();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	@Override
	protected String getName() {
		return "OWServer Refresh Service";
	}

	String getVariable(String response, String romId, String name) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Get the DOM Builder
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("Error parsing OWServer XML response "+e.getMessage());
		}

		// Load and Parse the XML document
		// document contains the complete XML as a Tree.
		Document document = null;
		try {
			InputSource is = new InputSource(new StringReader(response));
	        document = builder.parse(is);
		} catch (SAXException e) {
			logger.error("Error reading OWServer XML response "+e.getMessage());
		} catch (IOException e) {
			logger.error("Error reading OWServer XML response "+e.getMessage());
		}

		// Iterating through the nodes and extracting the data.
		NodeList nodeList = document.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeName().startsWith("owd_")) {
				boolean romMatch = false;
				NodeList childNodes = node.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node cNode = childNodes.item(j);

					// Identifying the child tag of employee encountered.
					if (cNode instanceof Element) {
						String content = cNode.getLastChild().getTextContent()
								.trim();
						if(cNode.getNodeName().equals("ROMId") & content.equals(romId)) {
							romMatch = true;
						}

						String nname = cNode.getNodeName();
						if(nname.equals(name) & romMatch == true) {
							return content;
						}
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		for (OWServerBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				int refreshInterval = provider.getRefreshInterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {
					logger.debug("Item '{}' is about to be refreshed now", itemName);

					// Get the unit serverId from the binding, and relate that to the config
					String unit = provider.getServerId(itemName);
					OWServerConfig server = serverList.get(unit);
					needsUpdate = false;
					if (server == null) {
						needsUpdate = false;
						logger.error("Unknown OW server referenced: "+unit);
						
						continue;
					}
					else {
						age = System.currentTimeMillis() - server.lastUpdate;
						if (age >= cacheDuration)
							needsUpdate = true;
					}

					String response = null;
					if(needsUpdate == true) {
						String address = "http://"+server.host+"/details.xml";
						logger.debug("Getting OWSERVER data from "+address);
						response = HttpUtil.executeUrl("GET", address, timeout);
						server.cache = response;

						if (response == null) {
							server.lastUpdate = (long) 0;
							logger.error("No response received from '{}'", address);
						} else {
							server.lastUpdate = System.currentTimeMillis();
						}
						serverList.put(address, server);
					}
					else {
						logger.debug("Using OWSERVER cache");
						response = server.cache;
					}

					if(response != null) {
						String value = getVariable(response, provider.getRomId(itemName), provider.getName(itemName));
						if (value != null) {
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State state = createState(itemType, value);
							eventPublisher.postUpdate(itemName, state);
						}
					}

					lastUpdateMap.put(itemName, System.currentTimeMillis());
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
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			Enumeration<String> keys = config.keys();

			if ( serverList == null ) {
				serverList = new HashMap<String, OWServerConfig>();
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

				String serverId = matcher.group(1);

				OWServerConfig deviceConfig = serverList.get(serverId);

				if (deviceConfig == null) {
					deviceConfig = new OWServerConfig();
					serverList.put(serverId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else {
					throw new ConfigurationException(configKey, "The given OWServer configKey '" + configKey + "' is unknown");
				}
			}

			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}

			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}

			String cacheString = (String) config.get("cache");
			if (StringUtils.isNotBlank(cacheString)) {
				cacheDuration = Integer.parseInt(cacheString);
			}
			
			setProperlyConfigured(true);
		}
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the HTTP binding
	 * provider.
	 */
	static class OWServerConfig {
		public String host;
		public Long lastUpdate;
		public String cache;

		OWServerConfig() {
			lastUpdate = (long) 0;	
		}
		
		@Override
		public String toString() {
			return "OWServerCache [host="+host+" last=" + lastUpdate + ", cache=" + cache + "]";
		}
	}
	
	
}
