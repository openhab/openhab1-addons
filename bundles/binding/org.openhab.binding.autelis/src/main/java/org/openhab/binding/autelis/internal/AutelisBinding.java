/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.autelis.internal;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openhab.binding.autelis.AutelisBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
	

/**
 * Autelis Pool Control Binding
 * 
 * Autelis controllers allow remote access to many common pool systems. This
 * binding allows openHAB to both monitor and control a pool system through these 
 * controllers.
 * 
 * @see <a href="http://Autelis.com">http://autelis.com</a>
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class AutelisBinding extends AbstractActiveBinding<AutelisBindingProvider> {

	private static final Logger logger = 
		LoggerFactory.getLogger(AutelisBinding.class);

	/**
	 * Default port to use for connections to a Autelis controller
	 */
	static final int DEFAULT_PORT=80;
	
	/**
	 * Default timeout for http connections to a Autelis controller
	 */
	static final int TIMEOUT=5000;
	
	
	/**
	 * Dim command for lights
	 */
	static final String AUTELIS_CMD_DIM = "dim";
	
	/**
	 * UP command 
	 */
	static final String AUTELIS_CMD_UP = "up";
	
	/**
	 * Down Command
	 */
	static final String AUTELIS_CMD_DOWN = "down";
	
	/**
	 * Value command
	 */
	static final String AUTELIS_CMD_VALUE = "value";
	
	/**
	 * Value command
	 */
	static final String AUTELIS_CMD_HEAT = "hval";
	
	/**
	 * Equipment status type
	 */
	static final String AUTELIS_TYPES_EQUIP = "equipment";
	
	/**
	 * General status type
	 */
	static final String AUTELIS_TYPES_STATUS = "status";
	
	/**
	 * Temperature status type
	 */
	static final String AUTELIS_TYPES_TEMP = "temp";
	
	/**
	 * Lights  type
	 */
	static final String AUTELIS_TYPES_LIGHTS = "lightscmd";
	
	/**
	 * Chemistry status type
	 */
	static final String AUTELIS_TYPES_CHEMISTRY = "chem";
	
	/**
	 * Pumps status type.
	 */
	static final String AUTELIS_TYPES_PUMPS = "pumps";
	
	/**
	 * Setpoint
	 */
	static final String AUTELIS_SETPOINT = "sp";
	
	/**
	 * Heat point ?
	 */
	static final String AUTELIS_HEATPOINT = "hp";
	
	/**
	 * Heat type?
	 */
	static final String AUTELIS_HEATTYPE = "ht";
	
	/**
	 * Constructed URL consisting of host, port, username and password use to connect to a Autelis controller
	 */
	private String baseURL;
	
	/**
	 * Regex expression to match XML responses from the Autelis, this is used to combine similar XML docs
	 * into a single document, {@link XPath} is still used for XML querying
	 */
	private Pattern responsePattern = Pattern.compile("<response>(.+?)</response>", Pattern.DOTALL);
	
	/**
	 * Commands can only be sent on equipment and temp configurations
	 */
	private Pattern commandPattern = Pattern.compile("^(equipment|temp)\\.(.*)");
	
	/**
	 * Autelis controllers will not update their XML immediately after we change a value. To compensate
	 * we cache previous values for a {@link Item} using the item name as a key.  After a polling
	 * run has been executed we only update an item if the value is different then what's in the
	 * cache.  This cache is cleared after a fixed time period when commands are sent.
	 */
	private Map<String, State> stateMap = Collections.synchronizedMap(new HashMap<String, State>());
	
	/**
	 * Clear our state every hour
	 */
	private static int NORMAL_CLEARTIME = 60 * 60; //one hour
	
	/**
	 * Clear state after an command is sent
	 */
	private static int UPDATE_CLEARTIME = 60 * 2; //two minutes
	
	/**
	 * Holds the next clear time in millis
	 */
	private long clearTime;

	/** 
	 * the refresh interval which is used to poll values from the Autelis
	 * device (optional, defaults to 5000ms)
	 */
	private long refreshInterval = 5000;
	
	public AutelisBinding() {
		logger.debug("Autelius binding started");
	}
		
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		configureBinding(configuration);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		configureBinding(configuration);
	}
	
	/**
	 * Configures binding for both bundle activation and modification.
	 * @param configuration
	 */
	private void configureBinding(final Map<String, Object> configuration){
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}
		
		int port = DEFAULT_PORT;
		String host = (String) configuration.get("host");
		String username = (String) configuration.get("username");
		String password = (String) configuration.get("password");
		
		if(StringUtils.isBlank(host)){
			logger.error("Host config parameter is missing");
			setProperlyConfigured(false);
			return;
		}
		
		String portString = (String) configuration.get("port"); 
		if(StringUtils.isNotBlank(portString))
			port = Integer.parseInt(portString);

		String prefix = "";
		if(username != null) {
			prefix = username + ":" + password + "@";
		}
		
		baseURL = "http://" + prefix + host + ":" + port;

		logger.debug("Autelius binding configured for host {}", host);
		
		setProperlyConfigured(true);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "autelis";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.trace("Connecting to {}" + baseURL);	

		clearState();
		
		
		String xmlDoc = fetchStateFromController();
		
		if(xmlDoc == null)
			return;
		
		for (AutelisBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				Item item = provider.getItem(itemName);
				String config = provider.getAutelisBindingConfigString(itemName);
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				try {
					InputSource is = new InputSource(new StringReader(xmlDoc));
					String value = xpath.evaluate("response/" + config.replace('.', '/'), is);
					State state = toState(item.getClass(), value);
					State oldState = stateMap.put(itemName, state);
					if(!state.equals(oldState)){
						logger.debug("updating item {} with state {}", itemName, state);
						eventPublisher.postUpdate(itemName, state);
					}
				} catch (XPathExpressionException e) {
					logger.warn("could not parse xml",e);
				}

			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.trace("internalReceiveCommand({},{}) is called!", itemName, command);
		for (AutelisBindingProvider provider : providers) {
			Item item = provider.getItem(itemName);
			String config = provider.getAutelisBindingConfigString(itemName);
			Matcher m = commandPattern.matcher(config);
			if (m.find() && m.groupCount() > 1) {
			    String type = m.group(1);
			    String name = m.group(2);
				if(type.equals(AUTELIS_TYPES_EQUIP)){				
					String cmd = AUTELIS_CMD_VALUE;
					int value;
					if(command == OnOffType.OFF){
						value = 0;
					} else if(command == OnOffType.ON){
						value = 1;
					} else if(command instanceof DecimalType){
						value = ((DecimalType)item.getStateAs(DecimalType.class)).intValue();
						if(value >= 3){
							//this is a dim type. not sure what 2 does
							cmd = AUTELIS_CMD_DIM;
						}
					} else {
						logger.error("Equipment commands must be of Decimal type not {}", command);
						break;
					}
					String response = HttpUtil.executeUrl("GET", baseURL + "/set.cgi?name=" + name + "&" + cmd + "=" + value, TIMEOUT);
					logger.trace("equipment set {} {} {} : result {}", name, cmd, value, response);
				} else if(type.equals(AUTELIS_TYPES_TEMP)){
					String value;
					if(command == IncreaseDecreaseType.INCREASE){
						value = AUTELIS_CMD_UP;
					}else if(command == IncreaseDecreaseType.DECREASE){
						value = AUTELIS_CMD_DOWN;
					}else {
						value = command.toString();
					}
					
					String cmd;
					//name ending in sp are setpoints, ht are heat types?
					if(name.endsWith(AUTELIS_SETPOINT)){
						cmd = AUTELIS_TYPES_TEMP;
					} else if(name.endsWith(AUTELIS_HEATTYPE)){
						cmd = AUTELIS_CMD_HEAT;
					} else {
						logger.error("Unknown temp type {}", name);
						break;
					}
					
					String response = HttpUtil.executeUrl("GET", baseURL + "/set.cgi?wait=1&name=" + name + "&" + cmd + "=" + value, TIMEOUT);
					logger.trace("temp set {} {} : result {}", cmd, value, response);
				}
			} else if(config.equals(AUTELIS_TYPES_LIGHTS)){		
				/*
				 * lighting command
				 * possible values, but we will let anything through.
				 * alloff, allon, csync, cset, cswim, party, romance, caribbean, american,
				 * sunset, royalty, blue, green, red, white, magenta, hold, recall
				 */
				String response = HttpUtil.executeUrl("GET", baseURL + "lights.cgi?val=" +  command.toString(), TIMEOUT);
				logger.trace("lights set {} : result {}", command.toString(), response);
			} else {
				logger.error("Unsupported set config {}", config);
			}
		}
		scheduleClearTime(UPDATE_CLEARTIME);
	}
	
	/**
	 * Fetches the XML string from a Autelis controller.
	 * @return
	 */
	private String fetchStateFromController() {
		// we will reconstruct the document with all the responses combined for
		// XPATH
		StringBuilder sb = new StringBuilder("<response>");

		// pull down the three xml documents
		String[] statuses = { AUTELIS_TYPES_STATUS, AUTELIS_TYPES_CHEMISTRY, AUTELIS_TYPES_PUMPS };
		for (String status : statuses) {
			String response = HttpUtil.executeUrl("GET", baseURL + "/" + status
					+ ".xml", TIMEOUT);
			logger.trace(baseURL + "/" + status + ".xml \n {}", response);
			if (response == null) {
				logger.warn("No response from Autelis controller!");
				return null;
			}
			// get the xml data between the response tags and append to our main
			// doc
			Matcher m = responsePattern.matcher(response);
			if (m.find()) {
				sb.append(m.group(1));
			}
		}
		// finish our "new" XML Document
		sb.append("</response>");

		/*
		 * This xmlDoc will now contain the three XML documents we retrieved
		 * wrapped in response tags for easier querying in XPath.
		 */
		return sb.toString();
	}
	
	/**
	 * Converts a {@link String} value to a {@link State} for a given {@link Item}
	 * @param itemType
	 * @param value
	 * @return {@link State}
	 */
	private State toState(Class<? extends Item> itemType, String value) {
		if (itemType.isAssignableFrom(NumberItem.class)) {
			return new DecimalType(value);
		} else if (itemType.isAssignableFrom(SwitchItem.class)) {
			return Integer.parseInt(value) > 0 ? OnOffType.ON : OnOffType.OFF;
		} else {
			return StringType.valueOf(value);
		}
	}
	
	/**
	 * Clears our state if it is time
	 */
	private void clearState(){
		if(System.currentTimeMillis() >= clearTime){
			stateMap.clear();
			scheduleClearTime(NORMAL_CLEARTIME); 
		}
	}
	
	/**
	 * Schedule when our next clear cycle will be
	 * @param secs
	 */
	private void scheduleClearTime(int secs){
		clearTime = System.currentTimeMillis() + (secs * 1000);
	}
}
