/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

import java.util.Dictionary;
import java.util.List;
import org.openhab.binding.dsmr.DSMRBindingProvider;
import org.openhab.binding.dsmr.internal.messages.OBISMessage;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class periodically read out the P1 port of the Smart Meter.
 * <p>
 * The frequency is once every 10 seconds. This frequency is based on the update
 * interval of the Smart Meter.
 * The resulting values will be posted to the event bus.
 * <p>
 * At this moment the binding supports only a single Smart Meter.
 * <p>
 * The binding needs 2 necessary configuration parameters from openhab.cfg:
 * <p>
 * <ul>
 * <li>dsmr.port (serial port device)
 * <li>dsmr.version {@link DSMRVersion}
 * </ul>
 * <p>
 * The implementation of the binding is based on the Dutch Smart Meter Requirements (DSMR)
 * 
 * @author M.Volaart
 * @since 1.7.0
 */
public class DSMRBinding extends AbstractActiveBinding<DSMRBindingProvider>
		implements ManagedService {

	/** Update interval as specified by DSMR */
	public static final int DSMR_UPDATE_INTERVAL = 10000;

	/* Logger */
	private static final Logger logger = LoggerFactory
			.getLogger(DSMRBinding.class);

	/* Serial port (configurable via openhab.cfg) */
	private String port = "";
	/* DSMR Version (configurable via openhab.cfg) */
	private DSMRVersion version = DSMRVersion.NONE;
	
	/* DSMR Port object */
	private DSMRPort dsmrPort;

	/*
	 * the refresh interval which is used to poll values from the DSMR server
	 * 
	 * Since we the device only updates every 10 seconds we let OpenHab
	 * introduce a pause before execute is called again.
	 * 
	 * We use here half the update interval time so we have some time
	 * to read the serial port (refreshInterval starts after previous executes ends) 
	 */
	private long refreshInterval = DSMR_UPDATE_INTERVAL / 2;

	/**
	 * Default Constructor
	 */
	public DSMRBinding() {
	}

	/**
	 * Activate the binding. We don't do anything special here.
	 * <p>
	 * To simplify synchronization issues we initiate the DSMR Port
	 * in the execute() method
	 */
	public void activate() {
		logger.debug("Activate DSMRBinding");
	}

	/**
	 * Deactivates the binding.
	 * <p>
	 * This will close the DSMR Port
	 */
	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		logger.info("Deactivating DSMRBinding");
		if (dsmrPort != null) {
			logger.debug("Closing DSMR port");
			dsmrPort.close();
		} else {
			logger.info("DSMR port was not initialised");
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
		return "DSMR Binding";
	}

	/**
	 * The execute method of the DSMR binding will execute if:
	 * <p>
	 * <ul>
	 * <li>Global binding is properly configured (i.e. port and version are set properly)
	 * <li>Item bindings are present
	 * </ul>
	 * <p>
	 * If there is no DSMR port yet, or the port is closed for some reason a new DSMR Port
	 * is created.
	 * <p>
	 * The DSMR port is read and received messages are posted on the bus.
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");

		// Check valid global Binding configuration
		if (!isProperlyConfigured()) {
			logger.debug("Binding is not yet configured");
			return;
		}

		// Check if there are any item bindings
		if (!bindingsExist()) {
			logger.debug("There is no existing DSMR binding configuration => refresh cycle aborted!");
			return;
		}
		
		// Check if a valid DSMR port exists. Open a new one if necessary
		if (dsmrPort == null || !dsmrPort.isOpen()) {
			logger.debug("Creating DSMR Port:" + port);
			dsmrPort = new DSMRPort(port, version, DSMR_UPDATE_INTERVAL);
		}

		// Read the DSMRPort
		List<OBISMessage<? extends State>> messages = dsmrPort.read();

		// Publish messages on the event bus
		for (OBISMessage<? extends State> msg : messages) {
			logger.debug("Read message:" + msg);
			for (DSMRBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String item = provider.getItem(itemName);
					if (item.equals(msg.getType().bindingReference)) {
						logger.debug("Publish data(" + item + ") to "
								+ itemName);

						eventPublisher.postUpdate(itemName,
								msg.getOpenHabValue());
					}
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
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}

	/**
	 * Read the following properties:
	 * - dsmr:port. Serial port where DSMR can be read (e.g. /dev/ttyUSB0) 
	 * - dsmr:version. Version of the DSMR protocol of
	 * the meter. See also {@link DSMRVersion}
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug("updated() is called!");
		if (config != null) {
			// Read port string
			String portString = (String) config.get("port");
			logger.debug("Set port:" + portString);
			if (StringUtils.isNotBlank(portString)) {
				port = portString;
			} else {
				logger.warn("DSMR setting is empty");
			}
			
			// Read version string
			String versionString = (String) config.get("version");
			logger.debug("Version:" + versionString);
			version = DSMRVersion.getDSMRVersion(versionString);

			// Validate configuration
			if (version != DSMRVersion.NONE && port.length() > 0) {
				logger.debug("Configuration succeeded");
				setProperlyConfigured(true);
			} else {
				logger.debug("Configuration failed");
				setProperlyConfigured(false);
			}
		}
	}
}