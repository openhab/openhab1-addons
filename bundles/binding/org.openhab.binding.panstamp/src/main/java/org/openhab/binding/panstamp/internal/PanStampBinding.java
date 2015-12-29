/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.EndpointNotFoundException;
import me.legrange.panstamp.ModemException;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.NetworkListener;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.definition.CompoundDeviceLibrary;
import me.legrange.panstamp.event.AbstractNetworkListener;
import me.legrange.panstamp.event.AbstractPanStampListener;
import me.legrange.panstamp.event.AbstractRegisterListener;
import me.legrange.panstamp.xml.ClassLoaderLibrary;
import me.legrange.panstamp.xml.FileLibrary;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SerialModem;
import me.legrange.swap.SwapException;
import me.legrange.swap.SwapModem;
import me.legrange.swap.tcp.TcpException;
import me.legrange.swap.tcp.TcpServer;

import org.openhab.binding.panstamp.PanStampBindingConfig;
import org.openhab.binding.panstamp.PanStampBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding connects to a panStamp network and handles traffic to and from it.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
public class PanStampBinding extends AbstractBinding<PanStampBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(PanStampBinding.class);
	private PanStampBindingSettings cfg;
	private SwapModem modem;
	private Network network;
	private ConfigDeviceStore store;
	private TcpServer debugServer;

	/**
	 * @{inheritDoc
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		logger.debug("activate()");
		try {
			startup(PanStampBindingSettings.parseConfig(configuration));
		} catch (ValueException e) {
			logger.error("Configuration error: {} ", e.getMessage());
		}
	}

	/**
	 * @{inheritDoc
	 */
	public void modified(final Map<String, Object> configuration) {
		logger.debug("modified()");
		try {
			PanStampBindingSettings newCfg = PanStampBindingSettings.parseConfig(configuration);
			if (cfg == null) {
				startup(newCfg);
			} else if (cfg.equals(newCfg)) {
				logger.info("Settings not changed, continuing");
			} else {
				// difficult case, cfg changed
				// the serial config has changed
				if (cfg.serialDiffers(newCfg)) {
					logger.info("Serial configuration changed. Restarting panStamp binding");
					stop();
					startup(newCfg);
					return;
				}
				if (cfg.networkDiffers(newCfg)) {
					setupModem(newCfg);
				} // the modem setup has changed
				if (cfg.directoriesDiffers(newCfg)) {
					setupDirectories(newCfg);
				}
				if (cfg.debugDiffers(newCfg)) {
					stopDebug();
					startDebug(newCfg);
				}
			}
			cfg = newCfg;
		} catch (ValueException e) {
			logger.error("Configuration error: {}", e.getMessage());
		}
	}

	/**
	 * @{inheritDoc
	 */
	public void deactivate(final int reason) {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		logger.debug("deactivate()");
		stop();
		super.deactivate();
	}

	/**
	 * @{inheritDoc
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{})", itemName, command);
		List<PanStampBindingConfig<?>> confs = getConfigs(itemName);
		for (PanStampBindingConfig<?> conf : confs) {
			try {
				PanStamp dev;
				if (!network.hasDevice(conf.getAddress())) {
					dev = new PanStamp(network, conf.getAddress());
					network.addDevice(dev);
				} else {
					dev = network.getDevice(conf.getAddress());
				}
				if (dev.hasRegister(conf.getRegister())) {
					Register reg = dev.getRegister(conf.getRegister());
					if (reg.hasEndpoint(conf.getEndpoint())) {
						Endpoint<Object> ep = reg.getEndpoint(conf.getEndpoint());
						if (ep.isOutput()) {
							Object val = null;
							switch (ep.getType()) {
							case BINARY:
								val = PanStampConversions.toBoolean(command);
								break;
							case INTEGER:
								val = PanStampConversions.toLong(command);
								break;
							case NUMBER:
								val = PanStampConversions.toDouble(command);
								break;
							case STRING:
								val = PanStampConversions.toString(command);
								break;
							default:
								logger.error("Unsupported panStamp endpoint type '{}' for endpoint {}. BUG!",
										ep.getType(), PanStampConversions.toString(ep));
							}
							if (val != null) {
								if (!conf.getUnit().equals("")) {
									ep.setValue(conf.getUnit(), val);
								} else {
									ep.setValue(val);
								}
							}
						} else {
							logger.error("Internal update received for input-only endpoint {}",
									PanStampConversions.toString(ep));
						}
					} else {
						logger.error("No endpoint '{}' found for register '{}' on panStamp '{}' linked to item '{}'",
								conf.getEndpoint(), conf.getRegister(), conf.getAddress(), itemName);
					}
				} else {
					logger.error("No register '{}' found on panStamp '{}' linked to item '{}'", conf.getRegister(),
							conf.getAddress(), itemName);
				}
			} catch (EndpointNotFoundException e) {
				logger.error(e.getMessage());
			} catch (NetworkException e) {
				logger.error(e.getMessage());
			} catch (ValueException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private void startup(PanStampBindingSettings cfg) {
		store = new ConfigDeviceStore();
		for (PanStampBindingProvider prov : providers) {
			prov.addBindingChangeListener(bindingChangeListener);
			for (String itemName : prov.getItemNames()) {
				PanStampBindingConfig<?> conf = prov.getConfig(itemName);
				updateProductCode(conf);
				logger.debug("found item {} on startup -> {}", itemName, conf);
			}
		}
		startNetwork(cfg);
		startDebug(cfg);
		if (network != null) {
			for (PanStampBindingProvider prov : providers) {
				for (String itemName : prov.getItemNames()) {
					PanStampBindingConfig<?> conf = prov.getConfig(itemName);
					if (!network.hasDevice(conf.getAddress())) {
						try {
							network.addDevice(new PanStamp(network, conf.getAddress()));
							logger.debug("Added device {} to network", conf.getAddress());
						} catch (NetworkException e) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		}

	}

	private void stop() {
		stopDebug();
		stopNetwork();
	}

	private void startNetwork(final PanStampBindingSettings cfg) {
		try {
			logger.debug("startNetwork()");
			modem = new SerialModem(cfg.serialPort, cfg.serialSpeed);
			modem.open();
			logger.info("Opened panStamp network on serial port {} at {}bps", cfg.serialPort, cfg.serialSpeed);
			setupModem(cfg);
			network = Network.create(modem);
			setupDirectories(cfg);
			network.setDeviceStore(store);
			logger.info("Configured binding product codes");
			network.addListener(networkListener);
			network.open();
			logger.info("Opened network");
		} catch (NetworkException e) {
			logger.error("Error connecting to serial network on {}: {}", cfg.serialPort, e.getMessage());

		} catch (SwapException e) {
			logger.error("Error reading modem settings for serial network on {}: {}", cfg.serialPort, e.getMessage());
		} catch (Throwable e) {
			logger.error("Fatal error: {}", e.getMessage(), e);
		}
	}

	private void stopNetwork() {
		logger.debug("stopNetwork()");
		try {
			network.close();
			network = null;
			logger.info("Closed panStamp network");
		} catch (ModemException e) {
			logger.error("Error closing panStamp network: " + e.getMessage(), e);
		}
	}

	private void startDebug(final PanStampBindingSettings cfg) {
		if (cfg.debugEnabled) {
			try {
				debugServer = new TcpServer(network.getSWAPModem(), cfg.debugPort);
				logger.info("TCP Debug enabled on port {}", cfg.debugPort);
			} catch (TcpException e) {
				logger.error("Error creating debug service:  {}" + e.getMessage());
			}
		}
	}

	private void stopDebug() {
		try {
			if (debugServer != null) {
				debugServer.close();
				debugServer = null;
			}
		} catch (TcpException e) {
			logger.error("Error closing debug port: " + e.getMessage(), e);
		}
	}

	private void setupModem(PanStampBindingSettings cfg) {
		try {
			ModemSetup setup = modem.getSetup();
			if (cfg.networkId != -1) {
				setup.setNetworkID(cfg.networkId);
			}
			if (cfg.networkDeviceAddress != -1) {
				setup.setDeviceAddress(cfg.networkDeviceAddress);
			}
			if (cfg.networkChannel != -1) {
				setup.setChannel(cfg.networkChannel);
			}
			if ((cfg.networkId != -1) || (cfg.networkChannel != -1) || (cfg.networkDeviceAddress != -1)) {
				modem.setSetup(setup);
				logger.info("Modem setup updated to address {}, channel {}, and network ID {}",
						setup.getDeviceAddress(), setup.getChannel(), setup.getNetworkID());
			}
		} catch (SwapException e) {
			logger.error("Error configuring network: {}", e.getMessage());
		}
	}

	private void setupDirectories(PanStampBindingSettings cfg) {
		File xmlDir = new File(cfg.xmlDir);
		if (xmlDir.exists() && xmlDir.isDirectory() && xmlDir.canRead()) {
			File devFile = new File(xmlDir, "devices.xml");
			if (devFile.exists() && devFile.canRead() && devFile.isFile()) {
				network.setDeviceLibrary(new CompoundDeviceLibrary(new FileLibrary(xmlDir), new ClassLoaderLibrary()));
				logger.info("Configured XML directory to {}", cfg.xmlDir);
			} else {
				logger.error("devices.xml in XML directory {} can not be read", cfg.xmlDir);
			}
		} else {
			logger.error("XML directory {} can not be read", cfg.xmlDir);
		}
	}

	private void updateProductCode(final PanStampBindingConfig<?> conf) {
		store.addProductCode(conf.getAddress(), conf.getManufacturerId(), conf.getProductId());
		logger.debug("Product code for {} updated to {}/{}", conf.getAddress(), conf.getManufacturerId(),
				conf.getProductId());

	}

	/* Register a detected panStamp device with the binding */
	private void addDevice(PanStamp dev) {
		try {
			logger.debug("addDevice({} ({}/{})", PanStampConversions.toString(dev), dev.getManufacturerId(),
					dev.getProductId());
		} catch (NetworkException e) {
			logger.error(e.getMessage());
		}
		dev.addListener(panStampListener);
		for (Register reg : dev.getRegisters()) {
			if (!reg.isStandard())
				addRegister(reg);
		}
	}

	/* Unregiter a panStamp device from the binding */
	private void removeDevice(PanStamp dev) {
		try {
			logger.debug("removeDevice({} ({}/{}))", PanStampConversions.toString(dev), dev.getManufacturerId(),
					dev.getProductId());
		} catch (NetworkException e) {
			logger.error(e.getMessage());
		}
		dev.removeListener(panStampListener);
		for (Register reg : dev.getRegisters()) {
			if (!reg.isStandard())
				removeRegister(reg);
		}
	}

	private void addRegister(Register reg) {
		logger.debug("addRegister({})", PanStampConversions.toString(reg));
		reg.addListener(registerListener);
		for (Endpoint<?> ep : reg.getEndpoints()) {
			addEndpoint(ep);
		}
	}

	private void removeRegister(Register reg) {
		logger.debug("removeRegister({})", PanStampConversions.toString(reg));
		reg.removeListener(registerListener);
		for (Endpoint<?> ep : reg.getEndpoints()) {
			removeEndpoint(ep);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addEndpoint(Endpoint ep) {
		logger.debug("addEndpoint({})", PanStampConversions.toString(ep));
		ep.addListener(endpointListener);
	}

	@SuppressWarnings("unchecked")
	private void removeEndpoint(Endpoint<?> ep) {
		logger.debug("removeEndpoint({})", PanStampConversions.toString(ep));
		ep.removeListener(endpointListener);
	}

	/**
	 * Return all available binding configs for the given item name
	 * 
	 * @param itemName
	 *            The item name for which configs are requested.
	 * @return The configs found.
	 */
	private List<PanStampBindingConfig<?>> getConfigs(String itemName) {
		List<PanStampBindingConfig<?>> confs = new ArrayList<PanStampBindingConfig<?>>();
		for (PanStampBindingProvider prov : providers) {
			PanStampBindingConfig<?> conf = prov.getConfig(itemName);
			if (conf != null) {
				confs.add(conf);
			}
		}
		return confs;
	}

	/**
	 * Return all available binding configs for the given endpoint
	 * 
	 * @param ep
	 *            The endpoint for which configs are requested.
	 * @return The configs found.
	 */
	private List<PanStampBindingConfig<?>> getConfigs(Endpoint<?> ep) {
		List<PanStampBindingConfig<?>> configs = new ArrayList<PanStampBindingConfig<?>>();
		Register reg = ep.getRegister();
		PanStamp ps = reg.getDevice();
		for (PanStampBindingProvider prov : providers) {
			for (String itemName : prov.getItemNames()) {
				PanStampBindingConfig<?> conf = prov.getConfig(itemName);
				if ((conf.getAddress() == ps.getAddress() && (conf.getRegister() == reg.getId()) && conf.getEndpoint()
						.equals(ep.getName()))) {
					configs.add(conf);
				}
			}
		}
		return configs;
	}

	/**
	 * Update all items associated with an endpoint with it's new value
	 * 
	 * @param ep
	 *            The endpoint for which a new value is available
	 * @param val
	 *            The value.
	 */
	private void updateValue(Endpoint<?> ep, Object val) {
		List<PanStampBindingConfig<?>> confs = getConfigs(ep);
		for (PanStampBindingConfig<?> conf : confs) {
			try {
				State state;
				if (!conf.getUnit().equals("")) {
					state = PanStampConversions.toState(ep, ep.getValue(conf.getUnit()));
				} else {
					state = PanStampConversions.toState(ep, ep.getValue());
				}
				eventPublisher.postUpdate(conf.getItemName(), state);
			} catch (ValueException e) {
				logger.error(e.getMessage());
			} catch (NetworkException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private final BindingChangeListener bindingChangeListener = new BindingChangeListener() {

		@Override
		public void bindingChanged(BindingProvider provider, String itemName) {
			logger.debug("bindingChanged({}, {}", provider, itemName);
			PanStampBindingConfig<?> c = ((PanStampBindingProvider) provider).getConfig(itemName);
			if (c != null) {
				updateProductCode(c);
			}
			logger.debug("{} => {}", itemName, c);
		}

		@Override
		public void allBindingsChanged(BindingProvider provider) {
		}
	};

	/**
	 * implement a network listener to receive events when devices are added to or removed from the network.
	 */
	private final NetworkListener networkListener = new AbstractNetworkListener() {

		@Override
		public void deviceDetected(Network gw, PanStamp dev) {
			logger.debug("device detected: {}", PanStampConversions.toString(dev));
			addDevice(dev);
		}

		@Override
		public void deviceRemoved(Network gw, PanStamp dev) {
			logger.debug("device removed: {}", PanStampConversions.toString(dev));
			removeDevice(dev);
		}

	};

	private final PanStampListener panStampListener = new AbstractPanStampListener() {

		@Override
		public void productCodeChange(PanStamp dev, int manufacturerId, int productId) {
			logger.debug("product code changed: {} {}/{}", PanStampConversions.toString(dev), manufacturerId, productId);
			removeDevice(dev);
			store.addProductCode(dev.getAddress(), manufacturerId, productId);
			addDevice(dev);
		}

		@Override
		public void registerDetected(PanStamp dev, Register reg) {
			try {
				logger.debug("register detected: {}", PanStampConversions.toString(reg));
				addRegister(reg);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	};

	private final RegisterListener registerListener = new AbstractRegisterListener() {

		@SuppressWarnings({ "rawtypes" })
		@Override
		public void endpointAdded(Register reg, Endpoint ep) {
			logger.debug("endpoint added {}", PanStampConversions.toString(ep));
			addEndpoint(ep);
		}

	};

	@SuppressWarnings("rawtypes")
	private final EndpointListener endpointListener = new EndpointListener() {

		@Override
		public void valueReceived(Endpoint ep, Object val) {
			logger.debug("value received {} = {}", PanStampConversions.toString(ep), val);
			updateValue(ep, val);
		}
	};

}
