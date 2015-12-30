/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sitewhere.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sitewhere.agent.Agent;
import com.sitewhere.agent.IAgentConfiguration;
import com.sitewhere.agent.SiteWhereAgentException;

/**
 * Implementation of {@link PersistenceService} for interacting with a <a
 * href="http://www.sitewhere.org/">SiteWhere</a> server instance. This service
 * sends events to a device (specified by the defaultHardwareId configuration
 * parameter) so that SiteWhere can store and process them. It also takes
 * commands from SiteWhere and pushes them to the openHAB bus to allow two-way
 * communication. If the given hardware id is not already registered in
 * SiteWhere, a new device instance will be created based on the specification
 * token provided in the configuration. The default specification token
 * corresponds to the openHAB device type included in the SiteWhere sample data.
 * 
 * @author Derek Adams
 */
public class SiteWherePersistenceService implements PersistenceService,
		QueryablePersistenceService, ManagedService {

	/** Static logger instance */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SiteWherePersistenceService.class);

	/** Alert type indicator for a DateTimeType */
	public static final String TYPE_OPENHAB_DATETIME = "openhab.datetime";

	/** Alert type indicator for a OnOffType */
	public static final String TYPE_OPENHAB_ONOFF = "openhab.onoff";

	/** Alert type indicator for a OpenClosedType */
	public static final String TYPE_OPENHAB_OPENCLOSED = "openhab.openclosed";

	/** Alert type indicator for a StringType */
	public static final String TYPE_OPENHAB_STRING = "openhab.string";

	/** Service name */
	private static final String SERVICE_NAME = "sitewhere";

	/** SiteWhere MQTT agent */
	private Agent agent;

	/** Command processor that interacts with SiteWhere */
	private OpenHabCommandProcessor sitewhere;

	/** Configuration settings */
	private SiteWhereConfiguration configuration;

	/** Reference to openHAB item registry */
	protected ItemRegistry itemRegistry;

	/** Event publisher for openHAB */
	private EventPublisher eventPublisher;

	/** Indicates if the service has been configured */
	protected boolean configured = false;

	/** Indicates if SiteWhere agent is connected */
	protected boolean agentConnected = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.core.persistence.PersistenceService#getName()
	 */
	public String getName() {
		return SERVICE_NAME;
	}

	/**
	 * OSGI setter for {@link EventPublisher}.
	 * 
	 * @param publisher
	 */
	public void setEventPublisher(EventPublisher publisher) {
		this.eventPublisher = publisher;
	}

	/**
	 * OSGI unsetter for {@link EventPublisher}.
	 * 
	 * @param publisher
	 */
	public void unsetEventPublisher(EventPublisher publisher) {
		this.eventPublisher = null;
	}

	/**
	 * Method called when bundle is activated.
	 * 
	 * @param bundleContext
	 * @param config
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> config) {
		LOGGER.trace("Activate method called on persistence service.");
		if (!isConfigured()) {
			SiteWhereConfiguration configuration = new SiteWhereConfiguration(
					config, LOGGER);
			setConfiguration(configuration);

			try {
				if (validateSiteWhereAgent()) {
					setConfigured(true);
				}
			} catch (Throwable t) {
				LOGGER.error("Error connecting to SiteWhere.", t);
			}
		}
	}

	/**
	 * Method called when bundle is deactivated.
	 */
	public void deactivate() {
		LOGGER.trace("Deactivate method called on persistence service.");
		setConfigured(false);
	}

	/**
	 * Set reference to item registry.
	 * 
	 * @param itemRegistry
	 */
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
		LOGGER.trace("Item registry set for SiteWhere persistence.");
	}

	/**
	 * Unset reference to item registry.
	 * 
	 * @param itemRegistry
	 */
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * Validate that the SiteWhere MQTT agent can connect.
	 * 
	 * @return
	 */
	protected boolean validateSiteWhereAgent() {
		LOGGER.info("About to connect using SiteWhere Agent...");
		LOGGER.info("Sending event to device with hardware id: "
				+ getConfiguration().getDefaultHardwareId());
		LOGGER.info("If no device registered, using specification: "
				+ getConfiguration().getDeviceSpecificationToken());
		LOGGER.info("MQTT Host: "
				+ getConfiguration().getConnection().getMqttHost());
		LOGGER.info("MQTT Port: "
				+ getConfiguration().getConnection().getMqttPort());

		// Create properties used to configure the SiteWhere Java agent.
		Properties props = new Properties();
		props.setProperty(IAgentConfiguration.COMMAND_PROCESSOR_CLASSNAME, "");
		props.setProperty(IAgentConfiguration.DEVICE_SPECIFICATION_TOKEN,
				getConfiguration().getDeviceSpecificationToken());
		props.setProperty(IAgentConfiguration.DEVICE_HARDWARE_ID,
				getConfiguration().getDefaultHardwareId());
		props.setProperty(IAgentConfiguration.MQTT_HOSTNAME, getConfiguration()
				.getConnection().getMqttHost());
		props.setProperty(IAgentConfiguration.MQTT_PORT, String
				.valueOf(getConfiguration().getConnection().getMqttPort()));

		// Create the agent.
		this.agent = new Agent();
		agent.load(props);

		// Create a processor that facilitates communication with SiteWhere.
		this.sitewhere = new OpenHabCommandProcessor(eventPublisher);

		try {
			agent.start(sitewhere);
			setAgentConnected(true);
			return true;
		} catch (SiteWhereAgentException e) {
			setAgentConnected(false);
			LOGGER.error("Unable to start SiteWhere MQTT agent.", e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.core.persistence.PersistenceService#store(org.openhab.core
	 * .items.Item)
	 */
	public void store(Item item) {
		store(item, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.core.persistence.PersistenceService#store(org.openhab.core
	 * .items.Item, java.lang.String)
	 */
	public void store(Item item, String alias) {
		if (isAgentConnected()) {
			String hwid = getConfiguration().getDefaultHardwareId();
			try {
				State state = item.getState();
				if (state instanceof PercentType) {
					addDeviceMeasurement(hwid, item, ((PercentType) state)
							.toBigDecimal().doubleValue());
				} else if (state instanceof DecimalType) {
					addDeviceMeasurement(hwid, item, ((DecimalType) state)
							.toBigDecimal().doubleValue());
				} else if (state instanceof DateTimeType) {
					addDeviceAlert(hwid, item, (DateTimeType) state);
				} else if (state instanceof OnOffType) {
					addDeviceAlert(hwid, item, (OnOffType) state);
				} else if (state instanceof OpenClosedType) {
					addDeviceAlert(hwid, item, (OpenClosedType) state);
				} else if (state instanceof StringType) {
					addDeviceAlert(hwid, item, (StringType) state);
				} else if (state instanceof PointType) {
					addDeviceLocation(hwid, item, (PointType) state);
				} else {
					LOGGER.debug("Unable to store item of type: "
							+ item.getState().getClass().getSimpleName());
					return;
				}
			} catch (SiteWhereAgentException e) {
				LOGGER.warn("Unable to store item: " + item.getName(), e);
			}
		}
	}

	/**
	 * Send a device measurement to SiteWhere.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceMeasurement(String hardwareId, Item item,
			double value) throws SiteWhereAgentException {
		sitewhere.sendMeasurement(hardwareId, item.getName(), value, null);
	}

	/**
	 * Send an alert for a {@link DateTimeType}.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceAlert(String hardwareId, Item item,
			DateTimeType value) throws SiteWhereAgentException {
		addDeviceAlert(hardwareId, item, TYPE_OPENHAB_DATETIME,
				value.toString());
	}

	/**
	 * Send an alert for an {@link OnOffType}.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceAlert(String hardwareId, Item item, OnOffType value)
			throws SiteWhereAgentException {
		addDeviceAlert(hardwareId, item, TYPE_OPENHAB_ONOFF, value.toString());
	}

	/**
	 * Send an alert for an {@link OpenClosedType}.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceAlert(String hardwareId, Item item,
			OpenClosedType value) throws SiteWhereAgentException {
		addDeviceAlert(hardwareId, item, TYPE_OPENHAB_OPENCLOSED,
				value.toString());
	}

	/**
	 * Send an alert for a {@link StringType}.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceAlert(String hardwareId, Item item, StringType value)
			throws SiteWhereAgentException {
		addDeviceAlert(hardwareId, item, TYPE_OPENHAB_STRING, value.toString());
	}

	/**
	 * Send a device alert to SiteWhere.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param type
	 * @param value
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceAlert(String hardwareId, Item item, String type,
			String value) throws SiteWhereAgentException {
		String habType = type + ":" + item.getName();
		sitewhere.sendAlert(hardwareId, habType, value, null);
	}

	/**
	 * Send a device location to SiteWhere.
	 * 
	 * @param hardwareId
	 * @param item
	 * @param point
	 * @throws SiteWhereAgentException
	 */
	protected void addDeviceLocation(String hardwareId, Item item,
			PointType point) throws SiteWhereAgentException {
		sitewhere.sendLocation(hardwareId, point.getLatitude().doubleValue(),
				point.getLongitude().doubleValue(), point.getAltitude()
						.doubleValue(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openhab.core.persistence.QueryablePersistenceService#query(org.openhab
	 * .core.persistence.FilterCriteria)
	 */
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		return new ArrayList<HistoricItem>();
	}

	protected SiteWhereConfiguration getConfiguration() {
		return configuration;
	}

	protected void setConfiguration(SiteWhereConfiguration configuration) {
		this.configuration = configuration;
	}

	protected boolean isConfigured() {
		return configured;
	}

	protected void setConfigured(boolean configured) {
		this.configured = configured;
	}

	protected boolean isAgentConnected() {
		return agentConnected;
	}

	protected void setAgentConnected(boolean agentConnected) {
		this.agentConnected = agentConnected;
	}
}