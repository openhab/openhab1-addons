/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.binding.connectsdk.internal.bridges.ExternalInputControlInput;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlForward;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlPause;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlPlay;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlPlayState;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlRewind;
import org.openhab.binding.connectsdk.internal.bridges.MediaControlStop;
import org.openhab.binding.connectsdk.internal.bridges.OpenhabConnectSDKPropertyBridge;
import org.openhab.binding.connectsdk.internal.bridges.PowerControlPower;
import org.openhab.binding.connectsdk.internal.bridges.TVControlChannel;
import org.openhab.binding.connectsdk.internal.bridges.TVControlChannelName;
import org.openhab.binding.connectsdk.internal.bridges.TVControlDown;
import org.openhab.binding.connectsdk.internal.bridges.TVControlProgram;
import org.openhab.binding.connectsdk.internal.bridges.TVControlUp;
import org.openhab.binding.connectsdk.internal.bridges.ToastControlToast;
import org.openhab.binding.connectsdk.internal.bridges.VolumeControlDown;
import org.openhab.binding.connectsdk.internal.bridges.VolumeControlMute;
import org.openhab.binding.connectsdk.internal.bridges.VolumeControlUp;
import org.openhab.binding.connectsdk.internal.bridges.VolumeControlUpDown;
import org.openhab.binding.connectsdk.internal.bridges.VolumeControlVolume;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.DeviceService.PairingType;
import com.connectsdk.service.command.ServiceCommandError;

/**
 * Connect SDK Binding implementation.
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKBinding extends AbstractBinding<ConnectSDKBindingProvider> implements DiscoveryManagerListener {
	private static final Logger logger = LoggerFactory.getLogger(ConnectSDKBinding.class);
	private DiscoveryManager discoveryManager;

	/**
	 * Array of bridges. To add a new functionality all it take is to create a new bridge and add here.
	 */
	private static OpenhabConnectSDKPropertyBridge[] bridges = new OpenhabConnectSDKPropertyBridge[] {
			new VolumeControlVolume(), new VolumeControlMute(), new VolumeControlUp(), new VolumeControlDown(),
			new TVControlChannel(), new TVControlUp(), new TVControlDown(), new VolumeControlUpDown(),
			new TVControlChannelName(), new TVControlProgram(), new PowerControlPower(),
			new ExternalInputControlInput(), new MediaControlForward(), new MediaControlPause(),
			new MediaControlPlay(), new MediaControlRewind(), new MediaControlStop(), new MediaControlPlayState(),
			new ToastControlToast()

	};

	/**
	 * Configured or auto-detected local IP address.
	 */
	private Inet4Address localIp;

	/**
     * Called by the SCR to activate the component with its configuration read from CAS
     * 
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, ?> configuration) {
		logger.debug("activated");
		try {
    		configure(configuration);
    	} catch(RuntimeException e) { // openhab does not seem to log this exception, so i do it here
    		logger.error(e.getMessage()); 
    		throw e;
    	} 
    	start();
	}
    
    /**
     * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
     * @param configuration Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
    	logger.debug("modified");
    	stop();
    	try {
    		configure(configuration);
    	} catch(RuntimeException e) { // openhab does not seem to log this exception, so i do it here
    		logger.error(e.getMessage());
    		throw e;
    	}
    	start();
    }

	private void start() {
		ContextImpl ctx = new ContextImpl(this);
		DiscoveryManager.init(ctx);
		discoveryManager = DiscoveryManager.getInstance();
		discoveryManager.setPairingLevel(DiscoveryManager.PairingLevel.ON);
		discoveryManager.addListener(this);
		discoveryManager.start();
	}

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or mandatory references
	 * are no longer satisfied or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		logger.debug("deactivate {}", reason);
		stop();
		
	}

	private void stop() {
		discoveryManager = null;
		DiscoveryManager.destroy();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		for (ConnectSDKBindingProvider provider : providers) {
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}

			final String device = provider.getDeviceForItem(itemName);

			final ConnectableDevice d;
			try {
				d = this.discoveryManager.getCompatibleDevices().get(InetAddress.getByName(device).getHostAddress());
				if (d == null) {
					logger.warn("{} not found under connect sdk devices. Skipping item {}", device, itemName);
					continue;
				}
			} catch (UnknownHostException e) {
				logger.error("Failed to resolve {} to IP address. Skipping item {}", device, itemName);
				continue;
			}

			final String clazz = provider.getClassForItem(itemName);
			final String property = provider.getPropertyForItem(itemName);

			for (OpenhabConnectSDKPropertyBridge b : bridges) {
				b.onReceiveCommand(d, clazz, property, command);
			}
		}

	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	// DiscoveryManagerListener methods

	@Override
	public void onDeviceAdded(final DiscoveryManager manager, final ConnectableDevice device) {
		logger.info("added: " + device.toJSONObject());
		// device.setPairingType(PairingType.PIN_CODE);
		device.addListener(new ConnectableDeviceListener() {

			@Override
			public void onPairingRequired(ConnectableDevice device, DeviceService service, PairingType pairingType) {

				logger.info("Pairing required.");
				// if (DeviceService.PairingType.PIN_CODE.equals(pairingType)) {
				// String key = "123";
				// service.sendPairingKey(key);
				// }

			}

			@Override
			public void onDeviceReady(ConnectableDevice device) {
				logger.info("Device ready: {}", device);
				refreshSubscriptions(device);
				for (OpenhabConnectSDKPropertyBridge b : bridges) {
					b.onDeviceReady(device, providers, eventPublisher);
				}
			}

			@Override
			public void onDeviceDisconnected(ConnectableDevice device) {
				logger.debug("Device disconnected: {}", device);
				// nothing to do here, we disconnect bridges in onDeviceRemoved method
			}

			@Override
			public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
				logger.debug("Connection failed: {} - error: {}", device, error.getMessage());
				// nothing to do here, only called when pairing fails...
			}

			@Override
			public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
				logger.debug("Capabilities updated: {} - added: {} - removed: {}", device, added, removed);
				refreshSubscriptions(device);
			}
		});

		connectIfAnyItemIsConfiguredFor(device);
		// logger.debug("Capabilities: " + device.getFriendlyName() + " : " + device.getCapabilities().toString());
	}

	private void connectIfAnyItemIsConfiguredFor(final ConnectableDevice device) {
		for (ConnectSDKBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				try {
					if (device.getIpAddress().equals(
							InetAddress.getByName(provider.getDeviceForItem(itemName)).getHostAddress())) {
						device.connect();
						return;
					}
				} catch (UnknownHostException e) {
					logger.error("Failed to resolve {} to IP address. Skipping update on item {}.", device, itemName);
				}
			}
		}
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		logger.debug("Item {} binding changed, refresh all listeners", itemName);
		/*
		 * Every time the item model reloads this method will be called once for every item bound to this binding.
		 * Recreating all subscriptions each time is not elegant, but there is no callback that gets called once after a
		 * model change. Tested allBindingsChanged, but that does not get called after editing the items file.
		 * Performance impact seems acceptable, as model reloads do not happen all that often. If we find a better way,
		 * let's improve it.
		 */
		for (ConnectableDevice device : discoveryManager.getCompatibleDevices().values()) {
			refreshSubscriptions(device);
		}

	}

	private void refreshSubscriptions(ConnectableDevice device) {
		for (OpenhabConnectSDKPropertyBridge b : bridges) {
			b.refreshSubscription(device, providers, eventPublisher);
		}
	}

	@Override
	public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("Device updated: {}", device);
		refreshSubscriptions(device);
	}

	@Override
	public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("Device removed: {}", device);
		for (OpenhabConnectSDKPropertyBridge b : bridges) {
			b.onDeviceRemoved(device, providers, eventPublisher);
			b.removeAnySubscription(device);
		}
		// no need to call device.disconnect this is done by connect sdk framework for us
	}

	@Override
	public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
		logger.warn("Discovery failed: {}", error.getMessage());
	}

	private void configure(Map<String, ?> config) {
		if (config != null) {
			final String configEntry = (String) config.get("localIp");
			if (StringUtils.isNotBlank(configEntry)) {
				try {
					InetAddress inetAddress = InetAddress.getByName(configEntry);
					if(NetworkInterface.getByInetAddress(inetAddress) == null) {
						throw new IllegalArgumentException(String.format("Config Parameter connectsdk:localIp - Configured IP address %s is incorrect. Connot find a matching network interface.", configEntry)); 
					} 
					if (!(inetAddress instanceof Inet4Address)) {
						throw new IllegalArgumentException("Config Parameter connectsdk:localIp - Connect SDK only supports IPv4 addresses."); // Util.convertIpAddress
					}
					this.localIp = (Inet4Address) inetAddress;
				} catch (UnknownHostException e) {
					throw new IllegalArgumentException(String.format("Config Parameter connectsdk:localIp - Hostname cannot be resolved: %s", configEntry));
				} catch (SocketException e) {
					throw new IllegalArgumentException(String.format("Config Parameter connectsdk:localIp - Unable to determine the correct network interface for IP %s.", configEntry)); 
				}
			} else {
				this.localIp = findLocalInetAddresses();
			}
			if (this.localIp == null) {
				throw new IllegalArgumentException("Config Parameter connectsdk:localIp - Auto-detection failed. Please configure connectsdk:localIp in openhab.cfg.");
			}
		}
	}

	/**
	 * Returns the local IP on the network.
	 * 
	 * @return the local IP address
	 */
	public Inet4Address getLocalIPAddress() {
		return localIp;
	}

	/**
	 * Attempts to autodetect the ip on the local network. It will ignore loopback and IPv6 addresses.
	 * @return local ip or <code>null</code> if detection was not possible.
	 */
	private Inet4Address findLocalInetAddresses() {
		// try to find IP via Java method (one some systems this returns the loopback interface though
		try {
			final InetAddress inetAddress = Inet4Address.getLocalHost();
			if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
				logger.debug("Autodetected (via getLocalHost) local IP: {}", inetAddress);
				return (Inet4Address) inetAddress;
			}
		} catch (UnknownHostException ex) {
			logger.warn("Unable to resolve your hostname", ex);
		}

		// try to find the single non-loop back interface available
		final List<Inet4Address> interfaces = new ArrayList<Inet4Address>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				if (networkInterface.isUp() && !networkInterface.isLoopback()) {
					for (InterfaceAddress adr : networkInterface.getInterfaceAddresses()) {
						InetAddress inadr = adr.getAddress();
						if (inadr instanceof Inet4Address) {
							interfaces.add((Inet4Address) inadr);
						}
					}
				}
			}
			
			if (interfaces.size() == 1) { // found exactly one interface, good
				logger.debug("Autodetected (via getNetworkInterfaces) local IP: {}", interfaces.get(0));
				return interfaces.get(0);
			}
		} catch (SocketException e) {
			logger.warn("Failed to detect network interfaces and addresses", e);
		}

		logger.error(
				"Your hostname resolves to a loopback address and the plugin cannot autodetect your network interface out of the following available options: {}.",
				interfaces);
		return null;
	}
}
