/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.squeezeserver;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.io.squeezeserver.internal.MqttBrokerConnection;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  TODO
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class SqueezeServer implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(SqueezeServer.class);

    /** Configuration defaults for optional properties */
	private static int DEFAULT_CLI_PORT = 9090;
	private static int DEFAULT_WEB_PORT = 9000;

	/** RegEx to validate SqueezeServer config <code>'^(squeeze:)(host|cliport|webport)=.+$'</code> */
	private final Pattern EXTRACT_SERVER_CONFIG_PATTERN = Pattern.compile("^(server)\\.(host|cliport|webport)$");
	
	/** RegEx to validate a mpdPlayer config <code>'^(.*?)\\.(id)$'</code> */
	private final Pattern EXTRACT_PLAYER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(id)$");
	    
    private String host;
    private int cliPort;
    private int webPort;
	private Map<String, SqueezePlayer> players = new ConcurrentHashMap<String, SqueezePlayer>();
    private Socket clientSocket;
    
    /**
	 * Is the connection to the Squeezebox Server active.
	 */
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected());
    }

    /**
	 * Lookup a Squeezebox player by name.
	 */
	public SqueezePlayer getPlayer(String playerName) {
        if (!players.containsKey(playerName))
            throw new RuntimeException("No Squeezebox player exists for {}", playerName);
            
        return players.get(playerName);
	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

        disconnect();
        
        host = null;
        cliport = DEFAULT_CLI_PORT;
        webport = DEFAULT_WEB_PORT;
        playerMap = new HashMap<String, String>();
        
		if (properties == null || properties.isEmpty()) {
			return;
		}
        
        Enumeration<String> keys = config.keys();
        while (keys.hasMoreElements()) {
            
            String key = (String) keys.nextElement();
            
            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key)) {
                continue;
            }
            
            Matcher serverMatcher = EXTRACT_SERVER_CONFIG_PATTERN.matcher(key);
            Matcher playerMatcher = EXTRACT_PLAYER_CONFIG_PATTERN.matcher(key);
            
            String value = (String) config.get(key);
            
            if (serverMatcher.matches()) {	
                serverMatcher.reset();
                serverMatcher.find();
                
                if ("host".equals(serverMatcher.group(2))) {
                    host = value;
                }
                else if ("cliport".equals(serverMatcher.group(2))) {
                    cliport = Integer.valueOf(value);
                }
                else if ("webport".equals(serverMatcher.group(2))) {
                    webport = Integer.valueOf(value);
                }
            } else if (playerMatcher.matches()) {
                SqueezePlayer player = new SqueezePlayer(value.toString(), playerMatcher.group(1));
                playerMap.put(player.getName(), player);
            }
        }
        
        if (StringUtils.isEmpty(host))
            throw new ConfigurationException("host", "No Squeeze Server host specified - this property is mandatory");
        if (playerMap.size() == 0)
            throw new ConfigurationException("host", "No Squeezebox players specified - there must be at least one player");
            
        connect();
		logger.info("Squeeze Server connection established.");
	}

    private void connect() {
		try {
			clientSocket = new Socket(host, cliPort);
		} catch (IOException e) {
			logger.error("Failed to connect to SqueezeServer at " + host + ":" + cliPort, e);
		}        
    }

    private void disconnect() {
		try {
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			logger.error("Failed to disconnect from SqueezeServer at " + host + ":" + cliPort, e);
		} finally {
			clientSocket = null;
		}
    }
    
	/**
	 * Start service.
	 */
	public void activate() {
		logger.debug("Starting Squeeze Server connection...");
	}

	/**
	 * Stop service.
	 */
	public void deactivate() {
		logger.debug("Stopping Squeeze Server connection...");
        disconnect();
		logger.debug("Squeeze Server connection stopped.");
	}
}
