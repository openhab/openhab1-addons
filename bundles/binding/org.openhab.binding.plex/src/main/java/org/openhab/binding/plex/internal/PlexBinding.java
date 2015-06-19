/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.plex.PlexBindingProvider;
import org.openhab.binding.plex.internal.annotations.ItemMapping;
import org.openhab.binding.plex.internal.annotations.ItemPlayerStateMapping;
import org.openhab.binding.plex.internal.communication.MediaContainer;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Binding that communicates with a Plex Media Server
* 
* {@link http://www.plex.tv/}
* 
* @author Jeroen Idserda
* @since 1.7.0
*/
public class PlexBinding extends AbstractActiveBinding<PlexBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(PlexBinding.class);
	
	private PlexConnector connector;
	
	private long refreshInterval = 5000;
	
	@Override
	protected void execute() {
		if (connector != null)  {
			connector.refresh();
		}
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Plex Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.trace("Plex binding changed");
		if (provider instanceof PlexBindingProvider) {
			PlexBindingProvider plexProvider = (PlexBindingProvider) provider;
			PlexBindingConfig config = plexProvider.getConfig(itemName);
			if (config != null) {
				setInitialState(config);
			}
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.trace("Plex all bindings changed");
		if (provider instanceof PlexBindingProvider) {
			PlexBindingProvider plexProvider = (PlexBindingProvider) provider;		
			setInitialState(plexProvider);
		}
	}
	
	/**
	 * Set initial state for all items for all providers  
	 */
	private void setInitialState() {
		for (PlexBindingProvider provider : providers) { 
			setInitialState(provider);
		}
	}
	
	private void setInitialState(PlexBindingProvider provider) {
		for (String itemName : provider.getItemNames()) {
			PlexBindingConfig config = provider.getConfig(itemName);
			if (config != null) {
				setInitialState(config);
			}
		}
	}

	private void setInitialState(PlexBindingConfig config) {
		if (connector != null) {
			PlexSession session = connector.getSessionByMachineId(config.getMachineIdentifier());
			if (session == null) {
				session = new PlexSession();
			}
	
			updateConfigFromSession(config, session);
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		PlexBindingConfig config = getConfig(itemName);
		if (!config.isReadOnly()) {
			try {
				connector.sendCommand(config, command);
			} catch (IOException e) {
				logger.error("Cannot send command {} for item {}", command, itemName, e);
			}
		} else {
			logger.warn("Cannot send command for item {}, property {} is read only", config.getItemName(), config.getProperty());
		}
	}
	
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		configureBinding(configuration);
	}
	
	public void modified(final Map<String, Object> configuration) {
		disconnect();
		configureBinding(configuration);
	}

	private void configureBinding(final Map<String, Object> configuration) {
		PlexConnectionProperties connectionProperties  = new PlexConnectionProperties();
		
		connectionProperties.setHost((String)configuration.get("host"));
		connectionProperties.setToken((String)configuration.get("token"));
		connectionProperties.setUsername((String)configuration.get("username"));
		connectionProperties.setPassword((String)configuration.get("password"));

		String port = (String)configuration.get("port");
		if (isNotBlank(port) && isNumeric(port)) {
			connectionProperties.setPort(Integer.valueOf(port));
		}
		
		String refresh = (String)configuration.get("refresh");
		if (isNotBlank(refresh) && isNumeric(refresh)) {
			refreshInterval = Long.parseLong(refresh);
		}
		
		logger.debug("Plex config, server at {}:{}", connectionProperties.getHost(), connectionProperties.getPort());
		
		if (isNotBlank(connectionProperties.getHost())) {
			connect(connectionProperties);
			setProperlyConfigured(true);
		} else {
			logger.warn("No host configured for Plex binding");
			setProperlyConfigured(false);
		}
	}
	
	public void deactivate(final int reason) {
		logger.trace("Plex binding deactived");
		disconnect();
	}

	private void disconnect() {
		if (connector != null) {
			connector.close();
		}
	}
	
	/**
	 * Get config from binding provider by Plex machine ID and property
	 */
	public PlexBindingConfig getConfig(String machineIdentifier, String property) {
		for (PlexBindingProvider provider : providers) {
			PlexBindingConfig config = provider.getConfig(machineIdentifier, property);
			if (config != null) 
				return config;
		}
		return null;
	}
	
	/**
	 * Get config from binding provider by itemName
	 */
	public PlexBindingConfig getConfig(String itemName) {
		for (PlexBindingProvider provider : providers) {
			PlexBindingConfig config = provider.getConfig(itemName);
			if (config != null) 
				return config;
		}
		return null;
	}
	
	/**
	 * Connect to the Plex server. 
	 */
	private void connect(PlexConnectionProperties connectionProperties) {
		connector = new PlexConnector(connectionProperties, new PlexUpdateReceivedCallback() {
			@Override
			public void updateReceived(PlexSession session) {
				processUpdateRecevied(session);
			}

			@Override
			public void serverListUpdated(MediaContainer container) {
				processServerList(container);
			}
		});
		connector.start();
		setInitialState();
	}
	
	/**
	 * Player state update received from Plex. Update all items
	 * for the machine ID this session is bound to.
	 * 
	 * @param session Plex session 
	 */
	private void processUpdateRecevied(PlexSession session) {
		for (PlexBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				PlexBindingConfig config = provider.getConfig(itemName);
				// In newer PMS versions, the machine identifier in the session also contains the type 
				// of media that is playing (<id>_Video for example). We'll keep it backwards compatible
				// by only matching the first part of the machine identifier.
				if (session.getMachineIdentifier().startsWith(config.getMachineIdentifier()))
					updateConfigFromSession(config, session);
			}
		}
	}

	/**
	 * Update all {@code PlexProperty.POWER} properties according to the 
	 * list of clients that are currently online.  
	 * 
	 * @param container MediaContainer, containing the clients that are currently online
	 */
	private void processServerList(MediaContainer container) {
		for (PlexBindingConfig config : getPowerConfigs()) {
			boolean online = container.getServer(config.getMachineIdentifier()) != null;
			eventPublisher.postUpdate(config.getItemName(), online ? OnOffType.ON : OnOffType.OFF);
		}
	}

	private List<PlexBindingConfig> getPowerConfigs() {
		List<PlexBindingConfig> configs = new ArrayList<PlexBindingConfig>();
		for (PlexBindingProvider provider : providers) {
			Collection<String> itemNames = provider.getItemNames();
			for (String itemName : itemNames) {
				PlexBindingConfig config = getConfig(itemName);
				if (config.getProperty().equals(PlexProperty.POWER.getName()))
					configs.add(config);
			}
		}
		return configs;
	}
	
	/**
	 * Maps properties from {@code session} to openHAB item {@code config}. Mapping is specified by {@link ItemMapping} annotations. 
	 * 
	 * @param config Binding config
	 * @param session Plex session
	 */
	private void updateConfigFromSession(PlexBindingConfig config, PlexSession session) {
		String property = config.getProperty();
		String itemName = config.getItemName();
		PlexPlayerState state = session.getState();
		
		for(Field field : session.getClass().getDeclaredFields()){
			ItemMapping itemMapping = field.getAnnotation(ItemMapping.class);
			if (itemMapping != null) {
 				if (itemMapping.property().getName().equals(property)) {
					if (itemMapping.type().equals(StringType.class)) {
						eventPublisher.postUpdate(itemName, getStringType(field, session));
					} else if (itemMapping.type().equals(PercentType.class)) {
						eventPublisher.postUpdate(itemName, getPercenteType(field, session));
					} else if (itemMapping.type().equals(DateTimeType.class)) {
						eventPublisher.postUpdate(itemName, getDateTimeType(field, session));
					}
				}
				for (ItemPlayerStateMapping stateMapping : itemMapping.stateMappings()) {
					if (stateMapping.property().getName().equals(property)) {
						eventPublisher.postUpdate(itemName, state.equals(stateMapping.state()) ? OnOffType.ON : OnOffType.OFF);
					}
				}
			}
		}
	}
	
	private State getStringType(Field field, PlexSession session) {
		return new StringType(getStringProperty(field, session));
	}
	
	private State getPercenteType(Field field, PlexSession session) {
		return new PercentType(getStringProperty(field, session));
	}
	
	private State getDateTimeType(Field field, PlexSession session) {
		Date date = getDateProperty(field, session);
		if (date != null) {
			return new DateTimeType(getCalendar(date));
		} else {
			return UnDefType.UNDEF;
		}
	}

	private Date getDateProperty(Field field, Object object) {
		Object value = invokeGetter(field, object);
		return value != null ? (Date)value : null;
	}
	
	private String getStringProperty(Field field, Object object) {
		Object value = invokeGetter(field, object);
		return value != null ? value.toString() : "";
	}
	
	private Object invokeGetter(Field field, Object object) {
		try {
			return object.getClass().getMethod("get" + capitalize(field.getName())).invoke(object);
		} catch (IllegalAccessException e) {
			logger.debug("Error getting property value", e);
		} catch (NoSuchMethodException e) {
			logger.debug("Error getting property value", e);
		} catch (SecurityException e) {
			logger.debug("Error getting property value", e);
		} catch (InvocationTargetException e) {
			logger.debug("Error getting property value", e);
		}
		
		return null;
	}

	private Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
}
