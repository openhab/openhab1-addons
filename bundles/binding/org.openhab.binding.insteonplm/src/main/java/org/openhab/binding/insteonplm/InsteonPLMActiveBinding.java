/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.DeviceQuerier;
import org.openhab.binding.insteonplm.internal.device.DeviceQueryListener;
import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.openhab.binding.insteonplm.internal.device.StatePublisher.StateListener;
import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.driver.DriverListener;
import org.openhab.binding.insteonplm.internal.driver.Port;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the main class that holds the binding together.
 * 
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class InsteonPLMActiveBinding
	extends AbstractActiveBinding<InsteonPLMBindingProvider>
	implements ManagedService, DeviceQueryListener {
	private static final Logger logger = LoggerFactory.getLogger(InsteonPLMActiveBinding.class);

	private Driver						m_driver	= null;
	private HashMap<InsteonAddress, InsteonDevice>  m_devices 	= null;
	private HashMap<String, String> 	m_config	= new HashMap<String, String>();
	private PortListener				m_portListener = new PortListener();
	private long						m_devicePollInterval = 300000L;
	private long						m_deadDeviceTimeout = -1L;
	private DeviceQuerier				m_deviceQuerier = null;
	
	// Configuration values set on initialize
	private int			m_refreshInterval = 600000;
	
	// Various flags that indicate the state of the binding
	private boolean		m_isActive		  		= false;
	private boolean		m_gotInitialConfig		= false;
	
	/**
	 * Constructor
	 */
	public InsteonPLMActiveBinding() {
		m_driver	= new Driver();
		m_devices 	= new HashMap<InsteonAddress, InsteonDevice>();
		m_deviceQuerier = new DeviceQuerier(m_devices, m_driver, this);
	}

	/**
	 * Called by the framework to execute a refresh of the binding
	 */
	@Override
	protected void execute() {
		logDeviceStatistics();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		// from AbstractActiveBinding
		return m_refreshInterval;
	}
	
	/*
	 * This method is invoked by the framework whenever a command is coming from openhab, i.e.
	 * a switch is flipped via the GUI or other controls. The binding translates this openhab
	 * command into a message to the modem.
	 * @see org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.lang.String, org.openhab.core.types.Command)
	 */

	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.info("Item: {} got command {}", itemName, command);

		if(!(this.isProperlyConfigured() && m_isActive)) {
			logger.debug("not ready to handle commands yet, returning.");
			return;
		}
		boolean commandHandled = false;
		for (InsteonPLMBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				commandHandled = true;
				InsteonAddress	ia		= provider.getAddress(itemName);
				String			feature = provider.getFeature(itemName);
				if (ia == null || feature == null) {
					logger.warn("could not find config for item {}", itemName);
				} else {
					sendCommand(ia, feature, command);
				}
			}
		}
		
		if (!commandHandled)
			logger.warn("No converter found for item = {}, command = {}, ignoring.", itemName, command.toString());
	}
	
	/**
	 * send command to insteon device
	 */
	private void sendCommand(InsteonAddress a, String feature, Command command) {
		InsteonDevice dev = getDevice(a);
		if (dev == null) {
			logger.warn("no device found with insteon address {}", a);
			return;
		}
		if (!dev.isInitialized()) {
			logger.warn("device {} not ready for commands yet!", a);
		} else {
			dev.processCommand(m_driver, command);
		}
	}
	
	private InsteonDevice getDevice(InsteonAddress a) {
		if (a == null) return null;
		return m_devices.get(a);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "InsteonPLM refresh";
	}
	
	/**
	 * @param provider the binding provider where the binding has changed
	 * @param itemName the item name for which the binding has changed
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		InsteonPLMBindingConfig c =
					((InsteonPLMBindingProvider)provider).getInsteonPLMBindingConfig(itemName);
		if (c == null) {
			// this condition happened when updating the .items file and
			// changing the insteon address of a switch that had been replaced
			return;	
		}
		m_gotInitialConfig = true;
		if (!m_driver.isDeviceListComplete()) {
			logger.debug("ignoring binding configuration until the device list is complete");
			return;
		}
		InsteonAddress addr = c.getAddress();
		synchronized (m_devices) {
			if (m_driver.isDeviceListComplete() && (!m_devices.containsKey(addr))) {
				logger.warn("device {} not found in modem link database (is it linked?), disabling it.",
						c.getAddress());
				return;
			}
			InsteonDevice dev = getDevice(addr);
			if (dev == null) { // should never happen because we check before, but just for safety
				logger.error("addr {} referenced in items file is not found!");
				return;
			}
			bindingChangedForDevice(dev, itemName, c);
		}
	}
	
	private void bindingChangedForDevice(InsteonDevice dev, String itemName,
										   InsteonPLMBindingConfig c) {
		dev.setIsInItemsFile(true);
		if (dev.needsQuerying()) {
			m_deviceQuerier.addDevice(dev);
			dev.setNeedsQuerying(false);
		}
		if (!dev.hasValidDescriptors()) {
			// no point applying configuration to unknown device
			return;
		}
		if (!dev.descriptorsHaveChanged()) {
			// only apply binding config if this is the first time, or if the
			// descriptors have changed because of a successfull query
			if (!dev.needsQuerying()) {
				logger.debug("descriptors for dev {} have not changed, no binding config applied.", dev);
			}
			return;
		}
		dev.setDescriptorsHaveChanged(false);

		logger.debug("applying binding for item {} address {}", itemName, dev.getAddress());
		String productKey = c.getProductKey();
		if (!dev.hasProductKey()) {
			dev.setProductKey(productKey);
			dev.instantiateFeatures();
		} else if (!dev.hasProductKey(productKey)) {
			logger.error("inconsistent product key {} for device {}, already has {}",
					productKey, dev.getAddress(), dev.getProductKey());
		}
		if (c.getFeature() == null) {
			logger.error("feature string missing for item {}", itemName);
		} else {
			DeviceFeature f = dev.getFeature(c.getFeature());
			if (f == null) {
				if (dev.isInitialized()) {
					logger.error(".items file references unknown feature {} for device {},"
							+ " is the product key correct?", c.getFeature(), dev.getAddress());
					logger.error("modem reports device {} as: {}. " +
							"If that is not correct, unlink from modem, and link again.",
							dev.getAddress(), dev.getDescriptorsAsString());
				} else {
					// We have no information about the device yet, but already getting
					// a request for an item wanting to bind to a feature. We enter
					// it as a placeholder, and will try to resolve it when
					// we get more information on the device
					logger.debug("adding place holder for feature {} ", c.getFeature());
					f = DeviceFeature.s_makeDeviceFeature("PLACEHOLDER");
					f.setParameters(c.getParameters());
					f.addListener(new FeatureListener(itemName));
					dev.addFeature(c.getFeature(), f);
				}
			} else {
				logger.debug("now listening for feature {} item {}", f.toString(), itemName);
				f.setParameters(c.getParameters());
				f.addListener(new FeatureListener(itemName));
			}
		}
		if (!dev.hasValidPollingInterval()) {
			dev.setPollInterval(m_devicePollInterval);
		}
		try {
			Port p = m_driver.getPort(dev.getPort());
			p.getPoller().startPolling(dev);
		} catch (IOException e) {
			logger.error("no port found for device {}, disabled polling.", dev);
		}
	}

	/**
	 * Activates the binding. There is nothing we can do at this point
	 * if we don't have the configuration information, which usually comes
	 * in later, when updated() is called. */
	@Override
	public void activate() {
		logger.debug("called activate()!");
		if (m_isActive) {
			// if the binding is already in the active state,
			// reinitialize it!
			shutdown();
			initialize();
		}
		m_isActive = true;
	}
	
	
	/**
	 * Apply binding config to device. Done by calling bindingChanged() on all items
	 */
	private void applyConfigurationToDevices() {
		for (InsteonPLMBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			for (Iterator<String> item = items.iterator(); item.hasNext();) {
				bindingChanged(provider, item.next());
			}
		}
	}
	
	/**
	 * Applies all item configurations pertaining to a given device 
	 * @param dev address of device to apply config to
	 */
	public void applyConfigurationForDevice(InsteonAddress dev) {
		for (InsteonPLMBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			InsteonPLMBindingProvider bp = (InsteonPLMBindingProvider)provider;
			for (Iterator<String> item = items.iterator(); item.hasNext();) {
				String itemName = item.next();
				InsteonPLMBindingConfig c =	bp.getInsteonPLMBindingConfig(itemName);
				if (c == null) continue;
				if (c.getAddress().equals(dev)) {
					bindingChanged(provider, itemName);
				}
			}
		}
	}
	/**
	 * Checks if a given device is referenced in the items file
	 * @param dev the device to check
	 */
	private boolean isInItemsFile(InsteonAddress dev) {
		for (InsteonPLMBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			InsteonPLMBindingProvider bp = (InsteonPLMBindingProvider)provider;
			for (Iterator<String> item = items.iterator(); item.hasNext();) {
				String itemName = item.next();
				InsteonPLMBindingConfig c =	bp.getInsteonPLMBindingConfig(itemName);
				if (c == null) continue;
				if (c.getAddress().equals(dev)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Deactivates the binding. The Controller is stopped and the serial interface
	 * is closed as well.
	 */
	@Override
	public void deactivate() {
		logger.debug("called deactivate()!");
		shutdown();
		m_isActive = false;
	}

	/**
	 * Inherited from the ManagedService interface. This method is called whenever the configuration
	 * is updated. This could be signaling that e.g. the port has changed etc.
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		HashMap<String, String> newConfig = new HashMap<String, String>();
		if (config == null) {
			logger.debug("seems like our configuration has been erased, will reset everything!");
		} else {
			// turn config into new HashMap
			for (Enumeration<String> e = config.keys(); e.hasMoreElements();) {
				String key   = e.nextElement();
				String value = config.get(key).toString();
				newConfig.put(key, value);
			}
		}
		
		if (newConfig.entrySet().equals(m_config.entrySet())) {
			logger.debug("config has not changed, done.");
			return;
		}
		m_config = newConfig;

		// configuration has changed
		if (m_isActive) {
			if (isProperlyConfigured()) {
				logger.debug("global binding config has changed, initializing.");
			} else {
				logger.debug("global binding config has arrived.");
			}
			shutdown();
			initialize();
		}
		if (!m_gotInitialConfig) {
			applyConfigurationToDevices();
		}
		long deadDeviceCount = 10;
		if (m_config.containsKey("device_dead_count")) {
			deadDeviceCount = s_parseLong(m_config.get("device_dead_count"), 2L, 100000L);
		}
		if (m_config.containsKey("poll_interval")) {
			m_devicePollInterval = s_parseLong(m_config.get("poll_interval"), 5000L, 3600000L);
		}
		m_deadDeviceTimeout = m_devicePollInterval * deadDeviceCount;
		logger.debug("configuration update complete!");
		setProperlyConfigured(true);
		return;
	}
	
	/**
	 * Initialize the binding: initialize the driver etc
	 */
	private void initialize() {
		logger.debug("initializing...");
		
		HashSet<String> ports = new HashSet<String>();
		//Intialize other config
		if (m_config.containsKey("refresh")) {
			m_refreshInterval = Integer.parseInt(m_config.get("refresh"));
		}

		//Initialize ports
		for (Map.Entry<String, String> e : m_config.entrySet()) {
			String name = e.getKey();
			String port  = e.getValue();
			if (ports.contains(port)) {
				logger.warn("port {} {} already in use, check config!", name, port);
				continue;
			}
			logger.info("config: {} -> {}", name, port);
			if (name.startsWith("port_")) {
				m_driver.addPort(name,  port);
				m_driver.addMsgListener(m_portListener, port);
			}
		}
		m_driver.setDriverListener(m_portListener);
		m_driver.startAllPorts();
		switch (m_driver.getNumberOfPorts()) {
		case 0:
			logger.error("initialization complete, but found no ports!");
			break;
		case 1:
			logger.debug("initialization complete, found 1 port!");
			break;
		case 2:
			logger.warn("initialization complete, found {} ports.",
					m_driver.getNumberOfPorts());
			break;
		}
	}
	
	/**
	 * Clean up all state.
	 */
	private void shutdown() {
		m_driver.stopAllPorts();
		m_devices.clear();
	}
	
	/**
	 * Will listen to any feature status updates
	 * @author daniel
	 *
	 */
	private class FeatureListener implements StateListener {
		private String			m_itemName;
		
		public FeatureListener(String item) {
			m_itemName = item;
		}
		
		@Override
		public void stateChanged(State state) {
			eventPublisher.postUpdate(m_itemName, state);
		}
	}
	
	/*
	 * Handles messages that come in from the ports.
	 * Will only process one message at a time.
	 */
	private class PortListener implements MsgListener, DriverListener {
		@Override
		public void msg(Msg msg, String fromPort) {
			if (msg.isEcho() || msg.isPureNack()) return;
			logger.debug("got msg: {}", msg);
			InsteonAddress toAddr = msg.getAddr("toAddress");
			if (!msg.isBroadcast() && !m_driver.isMsgForUs(toAddr)) {
				// not for one of our modems, do not process
				//logger.debug("msg not for us: {}", msg);
				return;
			}

			InsteonAddress fromAddr = msg.getAddr("fromAddress");
			if (fromAddr == null) {
				logger.debug("invalid fromAddress, ignoring msg {}", msg);
				return;
			}
			synchronized (m_devices) {
				InsteonDevice  dev = getDevice(fromAddr);
				if (dev == null) {
					logger.debug("dropping message from unknown device with address {}", fromAddr);
					return;
				}
				if (!dev.hasValidPorts()) {
					// we got a message from a new device out of the blue
					logger.info("got message from unknown device with addr {}", fromAddr);
					dev.addPort(fromPort); // tell the new device what port it belongs to
				}
				if (!dev.isInitialized()) {
					logger.debug("dropping message for uninitialized device {}", dev);
				} else {
					dev.handleMessage(fromPort, msg, eventPublisher);
				}
			}
		}

		@Override
		public void driverCompletelyInitialized() {
			logger.info("driver is completely initialized");
			for (InsteonDevice dev : m_devices.values()) {
				if (!dev.hasValidDescriptors()) {
					logger.warn("device {} has invalid modem db entry, probably caused by houselinc", dev.getAddress());
					logger.warn("will attempt to query device {}, if that fails consider re-linking", dev.getAddress());
				}
			}
			applyConfigurationToDevices();
			m_deviceQuerier.start();
			dumpAllDevices();
			logDeviceProblems();
		}

		@Override
		public HashMap<InsteonAddress, InsteonDevice> getDeviceList() {
			return m_devices;
		}

	}
	
	
	private void dumpAllDevices() {
		for (InsteonDevice dev : m_devices.values()) {
			if (!dev.isModem())
				logger.info("modem is linked to: {}", dev.toString());
		}
	}
	private void logDeviceStatistics() {
		synchronized (m_devices) {
			for (InsteonDevice dev : m_devices.values()) {
				if (dev.isModem()) continue;
				if (m_deadDeviceTimeout > 0 &&
						dev.getPollOverDueTime() > m_deadDeviceTimeout) {
					logger.info("device {} has not responded to polls for {} sec", dev.toString(),
							dev.getPollOverDueTime() / 3600);
				}
			}
		}
	}

	private void logDeviceProblems() {
		synchronized (m_devices) {
			for (InsteonDevice dev : m_devices.values()) {
				if (dev.isModem()) continue;
				if (!dev.isReferenced() && !isInItemsFile(dev.getAddress())) {
					logger.info("device {} is known to modem, but not in items file!", dev.toString());
				}
				if (!dev.isInitialized()) {
					logger.info("device {} is in items file, but not linked to modem!", dev.toString());
				}
			}
		}
	}
	
	public static long s_parseLong(String pi, long min, long max) {
		long t = Long.parseLong(pi);
		t = Math.max(t, min);
		t = Math.min(t, max);
		return t;
	}

	@Override
	public void deviceQueryComplete(InsteonAddress devAddress) {
		applyConfigurationForDevice(devAddress);
	}

}