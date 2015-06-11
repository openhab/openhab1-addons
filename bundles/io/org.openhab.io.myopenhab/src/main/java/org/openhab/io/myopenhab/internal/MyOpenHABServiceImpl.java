/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.myopenhab.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.io.myopenhab.MyOpenHABService;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class starts my.openHAB connection service and implements interface to communicate with my.openHAB.
 * It also acts as a persistence service to send commands and updates for selected items to my.openHAB
 * and processes commands for items received from my.openHAB.
 * 
 * @author Victor Belov
 * @since 1.3.0
 *
 */

public class MyOpenHABServiceImpl implements MyOpenHABService, PersistenceService, ManagedService, ActionService, MyOHClientListener {

	private static Logger logger = LoggerFactory.getLogger(MyOpenHABServiceImpl.class);

	public static String myohVersion = "1.7.0.0";
	private MyOHClient myOHClient;
	
	private static final String STATIC_CONTENT_DIR = "webapps" + File.separator + "static";
	private static final String UUID_FILE_NAME = "uuid";
	private static final String SECRET_FILE_NAME = "secret";	
	private static final String VERSION_FILE_NAME = "version";
	
	private String mMyOHBaseUrl;
	private int mLocalPort = 8080;
	
	protected ItemUIRegistry mItemUIRegistry = null;
	protected EventPublisher mEventPublisher = null;

	
	public MyOpenHABServiceImpl() {
	}
		
	/**
	 * @{inheritDoc}
	 */
	public void sendNotification(String userId, String message, String icon, String severity) {
		logger.debug("Sending message '{}' to user id {}", message, userId);
		myOHClient.sendNotification(userId, message, icon, severity);
	}

	/**
	 * @{inheritDoc}
	 */
	public void sendLogNotification(String message, String icon, String severity) {
		logger.debug("Sending log message '{}'", message);
		myOHClient.sendLogNotification(message, icon, severity);
	}

	/**
	 * @{inheritDoc}
	 */
	public void sendBroadcastNotification(String message, String icon, String severity) {
		logger.debug("Sending broadcast message '{}'", message);
		myOHClient.sendBroadcastNotification(message, icon, severity);
	}

	/**
	 * @{inheritDoc}
	 */
	public void sendSMS(String phone, String message) {
		logger.debug("Sending SMS '" + message + "' to phone # " + phone);
		myOHClient.sendSMS(phone, message);
	}

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			String baseUrlString = (String) config.get("baseUrl");
			if (StringUtils.isNotBlank(baseUrlString)) {
				mMyOHBaseUrl = baseUrlString;
			}
			String localPortString = (String) config.get("localPort");
			if (StringUtils.isNotBlank(localPortString)) {
				mLocalPort = Integer.valueOf(localPortString);
			}
		} else {
			logger.debug("config is null");
		}
		logger.debug("UUID = " + getUUID() + ", secret = " + getSecret());
		myOHClient = new MyOHClient(getUUID(), getSecret());
		if (mMyOHBaseUrl != null) {
			myOHClient.setMyOHBaseUrl(mMyOHBaseUrl);
		}
		if (mLocalPort != 8080) {
			myOHClient.setOHBaseUrl("http://localhost:" + String.valueOf(mLocalPort));
		}
		myOHClient.setOpenHABVersion(getVersion());
		myOHClient.connect();
		myOHClient.setListener(this);
		MyOpenHAB.mMyOpenHABService = this;
	}
	
	public void activate() {
		logger.debug("my.openHAB service activated");
	}

	public void deactivate() {
		logger.debug("my.openHAB service deactivated");
		myOHClient.shutdown();
	}

	@Override
	public String getActionClassName() {
		return MyOpenHAB.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return MyOpenHAB.class;
	}
	
	/**
	 * 
	 */
	private String getVersion() {
		File file = new File(STATIC_CONTENT_DIR + File.separator + VERSION_FILE_NAME);
		String versionString = "";
		
		if (file.exists()) {
			versionString = readFirstLine(file);
			logger.debug("Version file found at '{}' with content '{}'", file.getAbsolutePath(), versionString);
		}
		return versionString;
	}
	
	/**
	 * Gets UUID from file which is created by org.openhab.core.internal.CoreActivator
	 */
	private String getUUID() {
		File file = new File(STATIC_CONTENT_DIR + File.separator + UUID_FILE_NAME);
		String uuidString = "";
		
		if (file.exists()) {
			uuidString = readFirstLine(file);
			logger.debug("UUID file found at '{}' with content '{}'", file.getAbsolutePath(), uuidString);
		}		
		return uuidString;
	}
	
	/**
	 * Reads the first line from specified file
	 */

	private String readFirstLine(File file) {
		List<String> lines = null;
		try {
			lines = IOUtils.readLines(new FileInputStream(file));
		} catch (IOException ioe) {
			// no exception handling - we just return the empty String
		}
		return lines != null && lines.size() > 0 ? lines.get(0) : "";
	}
	
	/**
	 * Writes a String to a specified file
	 */

	private void writeFile(File file, String content) {
		// create intermediary directories
		file.getParentFile().mkdirs();
		try {
			IOUtils.write(content, new FileOutputStream(file));
			logger.debug("Created file '{}' with content '{}'", file.getAbsolutePath(), content);
		} catch (FileNotFoundException e) {
			logger.error("Couldn't create file '" + file.getPath() + "'.", e);
		} catch (IOException e) {
			logger.error("Couldn't write to file '" + file.getPath() + "'.", e);
		}
	}
	
	/**
	 * Creates a random secret and writes it to the <code>webapps/static</code>
	 * directory. An existing <code>secret</code> file won't be overwritten.
	 * Returns either existing secret from the file or newly created secret.
	 */

	private String getSecret() {
		File file = new File(STATIC_CONTENT_DIR + File.separator + SECRET_FILE_NAME);
		String newSecretString = "";
		
		if (!file.exists()) {
			newSecretString = RandomStringUtils.randomAlphanumeric(20);
			logger.debug("New password = " + newSecretString);
			writeFile(file, newSecretString);
		} else {
			newSecretString = readFirstLine(file);
			logger.debug("Secret file already exists at '{}' with content '{}'", file.getAbsolutePath(), newSecretString);
		}
		
		return newSecretString;
	}
	
	/*
	 * @see org.openhab.io.myopenhab.internal.MyOHClientListener#sendCommand(java.lang.String, java.lang.String)
	 */

	@Override
	public void sendCommand(String itemName, String commandString) {
		try {
			if (this.mItemUIRegistry != null) {
				Item item = this.mItemUIRegistry.getItem(itemName);
		    	Command command = null;
		    	if (item != null) {
					if (this.mEventPublisher != null) {
			    		if("toggle".equalsIgnoreCase(commandString) && 
			    				(item instanceof SwitchItem || 
			    				 item instanceof RollershutterItem)) {
			    			if(OnOffType.ON.equals(item.getStateAs(OnOffType.class))) command = OnOffType.OFF;
			    			if(OnOffType.OFF.equals(item.getStateAs(OnOffType.class))) command = OnOffType.ON;
			    			if(UpDownType.UP.equals(item.getStateAs(UpDownType.class))) command = UpDownType.DOWN;
			    			if(UpDownType.DOWN.equals(item.getStateAs(UpDownType.class))) command = UpDownType.UP;
			    		} else {
			    			command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
			    		}
			    		if (command != null) {
			    			logger.debug("Received command {} for item {}", commandString, itemName);
			    			this.mEventPublisher.postCommand(itemName,command);
			    		} else {
			    			logger.warn("Received invalid command {} for item {}", commandString, itemName);
			    		}
					}
		    	} else {
		    		logger.warn("Received command {} for non existing item {}", commandString, itemName);
		    	}
			} else {
				return;
			}
		} catch (ItemNotFoundException e) {
			logger.warn("Received my.openHAB command for a non-existant item {}", itemName);
		}
	}

	@Override
	public void store(Item item) {
		logger.debug("store({}), state = {}", item.getName(), item.getState().toString());
		myOHClient.sendItemUpdate(item.getName(), item.getState().toString());
	}

	@Override
	public void store(Item item, String alias) {
		logger.debug("store({}), state = {}", item.getName(), item.getState().toString());
		myOHClient.sendItemUpdate(item.getName(), item.getState().toString());		
	}

	@Override
	public String getName() {
		return "myopenhab";
	}

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.mItemUIRegistry = itemUIRegistry;
	}

	public void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.mItemUIRegistry = null;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		logger.debug("setEventPublisher");
		this.mEventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		logger.debug("unsetEventPublisher");
		this.mEventPublisher = null;
	}
}
