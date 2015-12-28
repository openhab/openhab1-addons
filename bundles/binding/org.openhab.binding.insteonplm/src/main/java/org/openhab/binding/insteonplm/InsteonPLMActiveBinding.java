/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.DeviceFeatureListener;
import org.openhab.binding.insteonplm.internal.device.DeviceType;
import org.openhab.binding.insteonplm.internal.device.DeviceTypeLoader;
import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice.DeviceStatus;
import org.openhab.binding.insteonplm.internal.device.RequestQueueManager;
import org.openhab.binding.insteonplm.internal.driver.Driver;
import org.openhab.binding.insteonplm.internal.driver.DriverListener;
import org.openhab.binding.insteonplm.internal.driver.ModemDBEntry;
import org.openhab.binding.insteonplm.internal.driver.Poller;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents the actual implementation of the binding, and controls the high level flow
 * of messages to and from the InsteonModem.
 * 
 * Writing this binding has been an odyssey through the quirks of the Insteon protocol
 * and Insteon devices. A substantial redesign was necessary at some point along the way.
 * Here are some of the hard learned lessons that should be considered by anyone who wants
 * to re-architect the binding:
 * 
 * 1) The entries of the link database of the modem are not reliable. The category/subcategory entries in
 *    particular have junk data. Forget about using the modem database to generate a list of devices.
 *    The database should only be used to verify that a device has been linked.
 *    
 * 2) Querying devices for their product information does not work either. First of all, battery operated devices
 *    (and there are a lot of those) have their radio switched off, and may generally not respond to product
 *    queries. Even main stream hardwired devices sold presently (like the 2477s switch and the 2477d dimmer)
 *    don't even have a product ID. Although supposedly part of the Insteon protocol, we have yet to
 *    encounter a device that would cough up a product id when queried, even among very recent devices. They
 *    simply return zeros as product id. Lesson: forget about querying devices to generate a device list.
 *    
 * 3) Polling is a thorny issue: too much traffic on the network, and messages will be dropped left and right,
 *    and not just the poll related ones, but others as well. In particular sending back-to-back messages
 *    seemed to result in the second message simply never getting sent, without flow control back pressure
 *    (NACK) from the modem. For now the work-around is to space out the messages upon sending, and
 *    in general poll as infrequently as acceptable.
 * 
 * 4) Instantiating and tracking devices when reported by the modem (either from the database, or when
 *    messages are received) leads to complicated state management because there is no guarantee at what
 *    point (if at all) the binding configuration will be available. It gets even more difficult when
 *    items are created, destroyed, and modified while the binding runs.
 *    
 * For the above reasons, devices are only instantiated when they are referenced by binding information.
 * As nice as it would be to discover devices and their properties dynamically, we have abandoned that
 * path because it had led to a complicated and fragile system which due to the technical limitations
 * above was inherently squirrely.
 *  
 * 
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class InsteonPLMActiveBinding
	extends AbstractActiveBinding<InsteonPLMBindingProvider>
	implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(InsteonPLMActiveBinding.class);

	private Driver					m_driver			= null;
	private ConcurrentHashMap<InsteonAddress, InsteonDevice>  m_devices = null; // list of all configured devices
	private HashMap<String, String> m_config			= new HashMap<String, String>();
	private PortListener			m_portListener 		= new PortListener();
	private long					m_devicePollInterval 	= 300000L;	// in milliseconds
	private long					m_deadDeviceTimeout 	= -1L;
	private long					m_refreshInterval		= 600000L;	// in milliseconds
	private int						m_messagesReceived		= 0;
	private boolean					m_isActive		  		= false; // state of binding
	private boolean					m_hasInitialItemConfig	= false;
	private int						m_x10HouseUnit			= -1;

	/**
	 * Constructor
	 */
	public InsteonPLMActiveBinding() {
		m_driver	= new Driver();
		m_devices 	= new ConcurrentHashMap<InsteonAddress, InsteonDevice>();
	}

	/**
	 * Inherited from AbstractBinding. This method is invoked by the framework whenever
	 * a command is coming from openhab, i.e. a switch is flipped via the GUI or other
	 * controls. The binding translates this openhab command into a message to the modem.
	 * {@inheritDoc}
	 */

	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.info("Item: {} got command {}", itemName, command);

		if(!(isProperlyConfigured() && m_isActive)) {
			logger.debug("not ready to handle commands yet, returning.");
			return;
		}
		boolean commandHandled = false;
		for (InsteonPLMBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				commandHandled = true;
				InsteonPLMBindingConfig c = provider.getInsteonPLMBindingConfig(itemName);
				if (c == null) {
					logger.warn("could not find config for item {}", itemName);
				} else {
					sendCommand(c, command);
				}
			}
		}
		
		if (!commandHandled)
			logger.warn("No converter found for item = {}, command = {}, ignoring.",
						itemName, command.toString());
	}

	/**
	 * Inherited from AbstractBinding.
	 * Activates the binding. There is nothing we can do at this point
	 * if we don't have the configuration information, which usually comes
	 * in later, when updated() is called.
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		logger.debug("activating binding");
		if (isProperlyConfigured() && !m_isActive) {
			initialize();
		}
		m_isActive = true;
	}
	
	/**
	 * Inherited from AbstractBinding. Deactivates the binding.
	 * The Controller is stopped and the serial interface is closed as well.
	 * {@inheritDoc}
	 */
	@Override
	public void deactivate() {
		logger.debug("deactivating binding!");
		shutdown();
		m_isActive = false;
	}


	/**
	 * Inherited from AbstractActiveBinding.
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "InsteonPLM";
	}
	
	/**
	 * Inherited from AbstractActiveBinding.
	 * Periodically called by the framework to execute a refresh of the binding.
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		logDeviceStatistics();
	}

	/**
	 * Inherited from AbstractActiveBinding.
	 * Returns the refresh interval (time between calls to execute()) in milliseconds.
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return m_refreshInterval;
	}
	
	/**
	 * Inherited from AbstractActiveBinding.
	 * This method is called by the framework whenever there are changes to
	 * a binding configuration.
	 * @param provider the binding provider where the binding has changed
	 * @param itemName the item name for which the binding has changed
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		m_hasInitialItemConfig = true; // hack around openHAB bug
		InsteonPLMBindingConfig c =
					((InsteonPLMBindingProvider)provider).getInsteonPLMBindingConfig(itemName);
		logger.debug("item {} binding changed: {}", String.format("%-30s", itemName), c);
		if (c == null) {
			// Item has been removed. This condition is also found when *any*
			// change to the items file is made: the items are first removed (c == null),
			// and then added anew.
			removeFeatureListener(itemName);
		} else {
			InsteonDevice dev = getDevice(c.getAddress());
			if (dev == null) {
				dev = makeNewDevice(c);
			}
			addFeatureListener(dev, itemName, c);
		}
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
				logger.debug("global binding config has changed, resetting.");
				shutdown();
			} else {
				logger.debug("global binding config has arrived.");
			}
		}
		processBindingConfiguration();
		logger.debug("configuration update complete!");
		setProperlyConfigured(true);
		if (m_isActive) {
			initialize();
		}
		if (!m_hasInitialItemConfig) triggerBindingChangedCalls();
		return;
	}

	private void processBindingConfiguration() {
		if (m_config.containsKey("refresh")) {
			m_refreshInterval = Integer.parseInt(m_config.get("refresh"));
			logger.info("refresh interval set to {}s", m_refreshInterval / 1000);
		}
		long deadDeviceCount = 10;
		if (m_config.containsKey("device_dead_count")) {
			deadDeviceCount = s_parseLong(m_config.get("device_dead_count"), 2L, 100000L);
			logger.info("device_dead_count set to {} per config file", deadDeviceCount);
		}
		if (m_config.containsKey("poll_interval")) {
			m_devicePollInterval = s_parseLong(m_config.get("poll_interval"), 5000L, 3600000L);
			logger.info("poll interval set to {} per config file", m_devicePollInterval);
		}
		if (m_config.containsKey("more_devices")) {
			String fileName = m_config.get("more_devices");
			try {
				DeviceTypeLoader.s_instance().loadDeviceTypesXML(fileName);
				logger.info("read additional device definitions from {}", fileName);
			} catch (Exception e) {
				logger.error("error reading additional devices from {}", fileName, e);
			}
		}
		if (m_config.containsKey("modem_db_retry_timeout")) {
			int timeout = Integer.parseInt(m_config.get("modem_db_retry_timeout"));
			m_driver.setModemDBRetryTimeout(timeout);
			logger.info("setting modem db retry timeout to {}s", timeout / 1000);
		}

		if (m_config.containsKey("more_features")) {
			String fileName = m_config.get("more_features");
			logger.info("reading additional feature templates from {}", fileName);
			DeviceFeature.s_readFeatureTemplates(fileName);
		}
		m_deadDeviceTimeout = m_devicePollInterval * deadDeviceCount;
		logger.info("dead device timeout set to {}s", m_deadDeviceTimeout / 1000);
 		
	}
	
	/**
	 * Method to find a device by address
	 * @param aAddr the insteon address to search for
	 * @return reference to the device, or null if not found
	 */
	public InsteonDevice getDevice(InsteonAddress aAddr) {
		InsteonDevice dev = (aAddr == null) ? null : m_devices.get(aAddr);
		return (dev);
	}
	

	/**
	 * HACK around openHAB synchronization issues that don't show
	 * up in the IDE environment, but when running as packaged bundle:
	 * The InsteonPLMGenericBindingProvider is instantiated *before*
	 * the InsteonPLMActiveBinding. This means: the binding provider parses
	 * all the configuration strings, but bindingChanged() in the actual
	 * binding is not called, because the binding is not even instantiated
	 * at that point. Later when the binding is active, it has to artificially
	 * trigger these calls.
	 */
	private void triggerBindingChangedCalls() {
		for (InsteonPLMBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			for (Iterator<String> item = items.iterator(); item.hasNext();) {
				String itemName = item.next();
				bindingChanged(provider, itemName);
			}
        }
	}
	
	/**
	 * Initialize the binding: initialize the driver etc
	 */
	private void initialize() {
		logger.debug("initializing...");
		
		HashSet<String> ports = new HashSet<String>();

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
		logger.debug("setting driver listener");
		m_driver.setDriverListener(m_portListener);
		logger.debug("starting {} ports", m_driver.getNumberOfPorts());
		m_driver.startAllPorts();
		logger.debug("ports started");
		switch (m_driver.getNumberOfPorts()) {
		case 0:
			logger.error("initialization complete, but found no ports!");
			break;
		case 1:
			logger.debug("initialization complete, found 1 port!");
			break;
		default:
			logger.warn("initialization complete, found {} ports.",
					m_driver.getNumberOfPorts());
			break;
		}
	}
	
	/**
	 * Clean up all state.
	 */
	private void shutdown() {
		logger.debug("shutting down binding");
		m_driver.stopAllPorts();
		m_devices.clear();
		RequestQueueManager.s_destroyInstance();
		Poller.s_instance().stop();
	}
	
	/**
	 * Send command to insteon device
	 * @param c item binding configuration
	 * @param command The command to be sent
	 */
	private void sendCommand(InsteonPLMBindingConfig c, Command command) {
		InsteonDevice dev = getDevice(c.getAddress());
		if (dev == null) {
			logger.warn("no device found with insteon address {}", c.getAddress());
			return;
		}
		dev.processCommand(m_driver, c, command);
	}

	/**
	 * Finds the device that a particular item was bound to, and removes the
	 * item as a listener
	 * @param aItem The item (FeatureListener) to remove from all devices
	 */
	private void removeFeatureListener(String aItem) {
		for (Iterator<Entry<InsteonAddress, InsteonDevice>> it = m_devices.entrySet().iterator();
				it.hasNext(); ) {
			InsteonDevice dev = it.next().getValue();
			boolean removedListener = dev.removeFeatureListener(aItem);
			if (removedListener) {
				logger.trace("removed feature listener {} from dev {}", aItem, dev);
			}
			if (!dev.hasAnyListeners()) {
				Poller.s_instance().stopPolling(dev);
				it.remove();
				logger.trace("removing unreferenced {}", dev);
				if (m_devices.isEmpty()) {
					logger.debug("all devices removed!", dev);
				}
			}
		}
	}
	
	/**
	 * Creates a new insteon device for a given product key
	 * @param aConfig The binding configuration parameters, needed to make device.
	 * @return Reference to the new device that has been created
	 */
	private InsteonDevice makeNewDevice(InsteonPLMBindingConfig aConfig) {
		String prodKey = aConfig.getProductKey();
		DeviceType dt = DeviceTypeLoader.s_instance().getDeviceType(prodKey);
		if (dt == null) {
			logger.error("unknown product key: {} for config: {}." +
					" Add definition to xml file and try again", prodKey, aConfig);
			return null;
		}
		InsteonDevice dev =	InsteonDevice.s_makeDevice(dt);
		dev.setAddress(aConfig.getAddress());
		dev.setDriver(m_driver);
		dev.addPort(m_driver.getDefaultPort());
		if (!dev.hasValidPollingInterval()) {
			dev.setPollInterval(m_devicePollInterval);
		}
		if (m_driver.isModemDBComplete() && dev.getStatus() != DeviceStatus.POLLING) {
			int ndev = checkIfInModemDatabase(dev);
			if (dev.hasModemDBEntry()) {
				dev.setStatus(DeviceStatus.POLLING);
				Poller.s_instance().startPolling(dev, ndev);
			}
		}
		m_devices.put(aConfig.getAddress(), dev);
		return (dev);
	}
	
	/**
	 * Checks if a device is in the modem link database, and, if the database
	 * is complete, logs a warning if the device is not present
	 * @param dev The device to search for in the modem database
	 * @return number of devices in modem database
	 */
	private int checkIfInModemDatabase(InsteonDevice dev) {
		InsteonAddress addr = dev.getAddress();
		HashMap<InsteonAddress, ModemDBEntry> dbes = m_driver.lockModemDBEntries();
		if (dbes.containsKey(addr)) {
			if (!dev.hasModemDBEntry()) {
				logger.info("device {} found in the modem database and {}.", addr, getLinkInfo(dbes, addr));
				dev.setHasModemDBEntry(true);
			}
		} else {
			if (m_driver.isModemDBComplete() && !addr.isX10()) {
				logger.warn("device {} not found in the modem database. Did you forget to link?", addr);
			}
		}
		int ndev = dbes.size();
		m_driver.unlockModemDBEntries();
		return ndev;
	}
	/**
	 * Adds a feature listener (i.e. item to a feature of a device)
	 * @param aDev The device to add the feature listener
	 * @param aItemName The name of the item (needed for logging and later lookups)
	 * @param aConfig The binding configuration for the item
	 */
	private void addFeatureListener(InsteonDevice aDev, String aItemName,
				InsteonPLMBindingConfig aConfig) {
		if (aDev == null) {
			return;
		}
		DeviceFeature f = aDev.getFeature(aConfig.getFeature());
		if (f == null || f.isFeatureGroup()) {
			StringBuffer buf = new StringBuffer();
			ArrayList<String> names = new ArrayList<String>(aDev.getFeatures().keySet());
			Collections.sort(names);
			for (String name : names) {
				DeviceFeature feature = aDev.getFeature(name);
				if (!feature.isFeatureGroup()) {
					if (buf.length() > 0) {
						buf.append(", ");
					}
					buf.append(name);
				}
			}

			logger.error("item {} references unknown feature: {}, item disabled! Known features for {} are: {}.",
					aItemName, aConfig.getFeature(), aConfig.getProductKey(), buf.toString());
			return;
		}
		DeviceFeatureListener fl = new DeviceFeatureListener(this, aItemName, eventPublisher);
		fl.setParameters(aConfig.getParameters());
		f.addListener(fl);	
	}

	private String getLinkInfo(HashMap<InsteonAddress, ModemDBEntry> dbes,
			InsteonAddress a) {
		ModemDBEntry dbe = dbes.get(a);
		ArrayList<Byte> controls = dbe.getControls();
		ArrayList<Byte> responds = dbe.getRespondsTo();

		StringBuffer buf = new StringBuffer("the modem");
		if (!controls.isEmpty()) {
			buf.append(" controls groups [");
			buf.append(toGroupString(controls));
			buf.append("]");
		}

		if (!responds.isEmpty()) {
			if (!controls.isEmpty()) {
				buf.append(" and");
			}

			buf.append(" responds to groups [");
			buf.append(toGroupString(responds));
			buf.append("]");
		}

		return buf.toString();
	}

	private String toGroupString(ArrayList<Byte> group) {
		ArrayList<Byte> sorted = new ArrayList<Byte>(group);
		Collections.sort(sorted);

		StringBuffer buf = new StringBuffer();
		for (Byte b : sorted) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append("0x");
			buf.append(Utils.getHexString(b));
		}

		return buf.toString();
	}

	/**
	 * Handles messages that come in from the ports.
	 * Will only process one message at a time.
	 */
	private class PortListener implements MsgListener, DriverListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void msg(Msg msg, String fromPort) {
			if (msg.isEcho() || msg.isPureNack()) return;
			m_messagesReceived++;
			logger.debug("got msg: {}", msg);
			if (msg.isX10()) {
				handleX10Message(msg, fromPort);
			} else {
				handleInsteonMessage(msg, fromPort);
			}
			
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void driverCompletelyInitialized() {
			HashMap<InsteonAddress, ModemDBEntry> dbes = m_driver.lockModemDBEntries();
			logger.info("modem database has {} entries!", dbes.size());
			if (dbes.isEmpty()) {
				logger.warn("the modem link database is empty!");
			}
			for (InsteonAddress k : dbes.keySet()) {
				logger.debug("modem db entry: {}", k);
			}
			HashSet<InsteonAddress> addrs = new HashSet<InsteonAddress>();
			for (InsteonDevice dev : m_devices.values()) {
				InsteonAddress a = dev.getAddress();
				if (!dbes.containsKey(a)) {
					if (!a.isX10())
						logger.warn("device {} not found in the modem database. Did you forget to link?", a);
				} else {
					if (!dev.hasModemDBEntry()) {
						addrs.add(a);
						logger.info("device {} found in the modem database and {}.", a, getLinkInfo(dbes, a));
						dev.setHasModemDBEntry(true);
					}
					if (dev.getStatus() != DeviceStatus.POLLING) {
						Poller.s_instance().startPolling(dev, dbes.size());
					}
				}
			}
			for (InsteonAddress k : dbes.keySet()) {
				if (!addrs.contains(k) && !k.equals(dbes.get(k).getPort().getAddress())) {
					logger.info("device {} found in the modem database, but is not configured as an item and {}.",
							k, getLinkInfo(dbes, k));
				}
			}
			m_driver.unlockModemDBEntries();
		}
		private void handleInsteonMessage(Msg msg, String fromPort) {
			InsteonAddress toAddr = msg.getAddr("toAddress");
			if (!msg.isBroadcast() && !m_driver.isMsgForUs(toAddr)) {
				// not for one of our modems, do not process
				return;
			}
			InsteonAddress fromAddr = msg.getAddr("fromAddress");
			if (fromAddr == null) {
				logger.debug("invalid fromAddress, ignoring msg {}", msg);
				return;
			}
			handleMessage(fromPort, fromAddr, msg);
		}

		private void handleX10Message(Msg msg, String fromPort) {
			try {
				int x10Flag	= msg.getByte("X10Flag") & 0xff;
				int rawX10	= msg.getByte("rawX10") & 0xff;
				if (x10Flag == 0x80) { // actual command
					if (m_x10HouseUnit != -1) {
						InsteonAddress fromAddr = new InsteonAddress((byte)m_x10HouseUnit);
						handleMessage(fromPort, fromAddr, msg);
					}
				} else if (x10Flag == 0) {
					// what unit the next cmd will apply to
					m_x10HouseUnit = rawX10 & 0xFF; 
				}
			} catch (FieldException e) {
				logger.error("got bad X10 message: {}", msg, e);
				return;
			}
		}
		private void handleMessage(String fromPort, InsteonAddress fromAddr, Msg msg) {
			InsteonDevice  dev = getDevice(fromAddr);
			if (dev == null) {
				logger.debug("dropping message from unknown device with address {}", fromAddr);
			} else {
				dev.handleMessage(fromPort, msg);
			}
		}
	}
	
	private void logDeviceStatistics() {
		logger.info(String.format("devices: %3d configured, %3d polling, msgs received: %5d",
				m_devices.size(), Poller.s_instance().getSizeOfQueue(), m_messagesReceived));
		m_messagesReceived = 0;
		for (InsteonDevice dev : m_devices.values()) {
			if (dev.isModem()) continue;
			if (m_deadDeviceTimeout > 0 &&
					dev.getPollOverDueTime() > m_deadDeviceTimeout) {
				logger.info("device {} has not responded to polls for {} sec", dev.toString(),
						dev.getPollOverDueTime() / 3600);
			}
		}
	}

	private static long s_parseLong(String pi, long min, long max) {
		long t = Long.parseLong(pi);
		t = Math.max(t, min);
		t = Math.min(t, max);
		return t;
	}
}