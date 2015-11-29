/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

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
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.DeviceService.PairingType;
import com.connectsdk.service.capability.ToastControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

/**
 * 
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKBinding extends AbstractBinding<ConnectSDKBindingProvider> implements DiscoveryManagerListener {
	private static final Logger logger = LoggerFactory.getLogger(ConnectSDKBinding.class);
	private DiscoveryManager discoveryManager;

	private static OpenhabConnectSDKPropertyBridge[] bridges = new OpenhabConnectSDKPropertyBridge[] {
			new VolumeControlVolume(), new VolumeControlMute(), new VolumeControlUp(), new VolumeControlDown(),
			new TVControlChannel(), new TVControlUp(), new TVControlDown(), new VolumeControlUpDown(), new TVControlChannelName(), new TVControlProgram(),
			new PowerControlPower(), new ExternalInputControlInput(),
			new MediaControlForward(), new MediaControlPause(), new MediaControlPlay(), new MediaControlRewind(), new MediaControlStop(), new MediaControlPlayState(),
			new ToastControlToast()
			
	};

	@Override
	public void activate() {
		logger.debug("activated");

		ContextImpl ctx = new ContextImpl();
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
				handleSubscriptions(device);

			}

			@Override
			public void onDeviceDisconnected(ConnectableDevice device) {
				logger.info("Device disconnected: {}", device);

			}

			@Override
			public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
				logger.warn("Connection failed: {} - error: {}", device, error.getMessage());

			}

			@Override
			public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
				logger.debug("Capabilities updated: {} - added: {} - removed: {}", device, added, removed);
				handleSubscriptions(device);
			}
		});

		handleSubscriptions(device);
		logger.debug("Capabilities: " + device.getFriendlyName() + " : " + device.getCapabilities().toString());

		sendHelloWorld(device);

	}

	@Override
	public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("Device updated: {}", device);
		handleSubscriptions(device);
	}

	@Override
	public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("Device removed: {}", device);
		for (OpenhabConnectSDKPropertyBridge b : bridges) {
			b.removeAnySubscription(device);
		}
	}

	@Override
	public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
		logger.warn("Discovery failed: {}", error.getMessage());
	}

	private void handleSubscriptions(ConnectableDevice device) {
		for (OpenhabConnectSDKPropertyBridge b : bridges) {
			b.updateSubscription(device, providers, eventPublisher);
		}
	}

	private void sendHelloWorld(ConnectableDevice device) {
		if (device.hasCapability(ToastControl.Show_Toast)) {
			try {
				BufferedImage bi = ImageIO.read(getClass().getResource("/openhab-logo-square.png"));

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				OutputStream b64 = Base64.getEncoder().wrap(os);
				ImageIO.write(bi, "png", b64);
				String result = os.toString("UTF-8");
				if (!device.isConnected()) {
					device.connect();
				}
				device.getCapability(ToastControl.class).showToast("Welcome to Openhab!", result, "png",
						new ResponseListener<Object>() {

							@Override
							public void onSuccess(Object object) {
								logger.debug("toast: {}", object);
							}

							@Override
							public void onError(ServiceCommandError error) {
								logger.error(error.getMessage());
							}
						});

			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}

		}
	}


}
