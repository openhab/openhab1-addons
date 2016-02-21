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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.core.ChannelInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.DeviceService.PairingType;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TVControl.ChannelListener;
import com.connectsdk.service.capability.ToastControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.VolumeControl.VolumeListener;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKBinding extends AbstractActiveBinding<ConnectSDKBindingProvider> implements
		DiscoveryManagerListener { // maybe move to AbstractBinding ... active
									// may not be required as connectsdk manages
									// own threads.

	private static final Logger logger = LoggerFactory.getLogger(ConnectSDKBinding.class);
	private DiscoveryManager mDiscoveryManager;

	/**
	 * Device IP to TVControl.ChannelListener map
	 * 
	 */
	private Map<String, ServiceSubscription<ChannelListener>> tvControlsChannelSubscriptions = new ConcurrentHashMap<String, ServiceSubscription<ChannelListener>>();
	/**
	 * Device IP to VolumeControl.VolumeListener map
	 * 
	 */
	private Map<String, ServiceSubscription<VolumeListener>> volumeControlVolumeSubscriptions = new ConcurrentHashMap<String, ServiceSubscription<VolumeListener>>();
	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate() method and must not
	 * be accessed anymore once the deactivate() method was called or before activate() was called.
	 */
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the ConnectSDK server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public ConnectSDKBinding() {

	}

	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		logger.debug("activated");
		this.bundleContext = bundleContext;

		setProperlyConfigured(true);

		// start connect sdk
		ContextImpl ctx = new ContextImpl();
		DiscoveryManager.init(ctx);

		// This step could even happen in your app's delegate
		mDiscoveryManager = DiscoveryManager.getInstance();
		mDiscoveryManager.setPairingLevel(DiscoveryManager.PairingLevel.ON);
		mDiscoveryManager.addListener(this);
		mDiscoveryManager.start();
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		logger.debug("modified");
		// update the internal configuration accordingly
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
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		this.mDiscoveryManager = null;
		DiscoveryManager.destroy();

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
		return "ConnectSDK Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		// logger.debug("execute() method is called!");
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

			ConnectableDevice d;
			try {
				d = this.mDiscoveryManager.getCompatibleDevices().get(InetAddress.getByName(device).getHostAddress());
				if (d == null) {
					logger.warn("Device not found {} under connectsdk devices. Skipping processing for item {}",
							device, itemName);
					continue;
				}
			} catch (UnknownHostException e) {
				logger.error("Failed to resolve {} to IP address. Skipping processing for item {}", device, itemName);
				continue;
			}

			final String clazz = provider.getClassForItem(itemName);
			final String property = provider.getPropertyForItem(itemName);
			onReceiveCommandTVControlChannel(d, clazz, property, command);
			onReceiveCommandVolumeControlVolume(d, clazz, property, command);
		}

	}

	private void onReceiveCommandTVControlChannel(final ConnectableDevice d, final String clazz, final String property,
			Command command) {
		if ("TVControl".equals(clazz) && "channel".equals(property)
				&& d.hasCapabilities(TVControl.Channel_List, TVControl.Channel_Set)) {// TODO use connect sdk control
																						// classes or create enum
			final String value = command.toString();
			final TVControl control = d.getCapability(TVControl.class);
			control.getChannelList(new TVControl.ChannelListListener() {
				@Override
				public void onError(ServiceCommandError error) {
					logger.error("error requesting channel list: {}.", error.getMessage());

				}

				@Override
				public void onSuccess(List<ChannelInfo> channels) {
					if (logger.isDebugEnabled()) {
						for (ChannelInfo c : channels) {
							logger.debug("Channel {} - {}", c.getNumber(), c.getName());
						}
					}
					try {
						ChannelInfo channelInfo = Iterables.find(channels, new Predicate<ChannelInfo>() {
							public boolean apply(ChannelInfo c) {
								return c.getNumber().equals(value);
							};
						});
						control.setChannel(channelInfo, new ResponseListener<Object>() {

							@Override
							public void onError(ServiceCommandError error) {
								logger.error("error changing channel: {}.", error.getMessage());

							}

							@Override
							public void onSuccess(Object object) {
								logger.info("successfully changed channel: {}.", object);

							}
						});
					} catch (NoSuchElementException ex) {
						logger.warn("TV does not have a channel: {}.", value);
					}

				}
			});

		}

	}

	private void onReceiveCommandVolumeControlVolume(final ConnectableDevice d, final String clazz,
			final String property, Command command) {
		if ("VolumeControl".equals(clazz) && "volume".equals(property) && d.hasCapabilities(VolumeControl.Volume_Set)) {

			PercentType percent;
			if (command instanceof PercentType) {
				percent = (PercentType) command;
			} else if (command instanceof DecimalType) {
				percent = new PercentType(((DecimalType) command).toBigDecimal());
			} else if (command instanceof StringType) {
				percent = new PercentType(((StringType) command).toString());
			} else {
				logger.warn("only accept precentType");
				return;
			}
			final float value = percent.floatValue() / 100.0f;
			final VolumeControl control = d.getCapability(VolumeControl.class);

			control.setVolume(value, new ResponseListener<Object>() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.error("error changing volume: {}.", error.getMessage());

				}

				@Override
				public void onSuccess(Object object) {
					logger.info("successfully changed volume: {}.", object);

				}
			});

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

				logger.info("pairing required ");
//				if (DeviceService.PairingType.PIN_CODE.equals(pairingType)) {
//					String key = "123";
//					service.sendPairingKey(key);
//				}

			}

			@Override
			public void onDeviceReady(ConnectableDevice device) {
				logger.info("device ready: " + device.toJSONObject());

			}

			@Override
			public void onDeviceDisconnected(ConnectableDevice device) {
				logger.info("device disconnected: " + device.toJSONObject());

			}

			@Override
			public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
				logger.warn("connection failed: " + device.toJSONObject() + " - error: " + error.getMessage());

			}

			@Override
			public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
				logger.info("compatibility updated: " + device.toJSONObject() + " - added: " + added.toString()
						+ " - removed: " + removed.toString());
				handleSubscriptions(device);
			}
		});

		handleSubscriptions(device);
		logger.info("capabilities: " + device.getFriendlyName() + " : " + device.getCapabilities().toString());

		sendHelloWorld(device);

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
				device.getCapability(ToastControl.class).showToast("Hello World", result, "png",
						new ResponseListener<Object>() {

							@Override
							public void onSuccess(Object object) {
								logger.info("toast: " + object.toString());
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

	@Override
	public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("update: " + device.toJSONObject());
		handleSubscriptions(device);
	}

	private void handleSubscriptions(ConnectableDevice device) {
		handleTVControlChannelSubscription(device);
		handleVolumeControlVolumeSubscription(device);
	}

	@Override
	public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {
		logger.info("removed: " + device.toJSONObject());
		removeAnyChannelSubscription(device);
		removeAnyVolumeSubscription(device);
	}

	@Override
	public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
		logger.warn("failed: " + error.getMessage());
	}

	private void handleTVControlChannelSubscription(final ConnectableDevice device) {
		removeAnyChannelSubscription(device);
		if (device.hasCapability(TVControl.Channel_Subscribe)) {
			logger.info("subscribe channel listener on ip: " + device.getIpAddress());
			ServiceSubscription<ChannelListener> listener = device.getCapability(TVControl.class)
					.subscribeCurrentChannel(new ChannelListener() {

						@Override
						public void onError(ServiceCommandError error) {
							logger.error("error: ", error.getMessage());
						}

						@Override
						public void onSuccess(ChannelInfo channelInfo) {
							for (ConnectSDKBindingProvider provider : providers) {
								for (String itemName : provider.getItemNames()) {
									try {
										if ("TVControl".equals(provider.getClassForItem(itemName))
												&& "channel".equals(provider.getPropertyForItem(itemName))
												&& device.getIpAddress().equals(
														InetAddress.getByName(provider.getDeviceForItem(itemName))
																.getHostAddress())) {
											if (eventPublisher != null) {
												eventPublisher.postUpdate(itemName,
														new StringType(channelInfo.getNumber())); // channel
																									// ->
																									// channelName
																									// and
																									// channelNumber
											}
										}
									} catch (UnknownHostException e) {
										logger.error("failed to resolve {} to IP address", device);
									}
								}
							}
						}
					});
			tvControlsChannelSubscriptions.put(device.getIpAddress(), listener);

		}
	}

	private void removeAnyChannelSubscription(final ConnectableDevice device) {
		ServiceSubscription<ChannelListener> l = tvControlsChannelSubscriptions.remove(device.getIpAddress());
		if (l != null) {
			l.unsubscribe();
			logger.debug("unsubscribed channel listener on ip: " + device.getIpAddress());
		}
	}

	private void handleVolumeControlVolumeSubscription(final ConnectableDevice device) {
		removeAnyVolumeSubscription(device);

		if (device.hasCapability(VolumeControl.Volume_Subscribe)) {
			ServiceSubscription<VolumeListener> listener = device.getCapability(VolumeControl.class).subscribeVolume(
					new VolumeListener() {

						@Override
						public void onError(ServiceCommandError error) {
							logger.error("error: ", error.getMessage());
						}

						@Override
						public void onSuccess(Float value) {
							for (ConnectSDKBindingProvider provider : providers) {
								for (String itemName : provider.getItemNames()) {
									try {
										if ("VolumeControl".equals(provider.getClassForItem(itemName))
												&& "volume".equals(provider.getPropertyForItem(itemName))
												&& device.getIpAddress().equals(
														InetAddress.getByName(provider.getDeviceForItem(itemName))
																.getHostAddress())) {
											if (eventPublisher != null) {
												eventPublisher.postUpdate(itemName,
														new PercentType(Math.round(value * 100)));
											}
										}
									} catch (UnknownHostException e) {
										logger.error("failed to resolve {} to IP address", device);
									}
								}
							}
						}
					});
			volumeControlVolumeSubscriptions.put(device.getIpAddress(), listener);

		}
	}

	private void removeAnyVolumeSubscription(final ConnectableDevice device) {
		ServiceSubscription<VolumeListener> l = volumeControlVolumeSubscriptions.remove(device.getIpAddress());
		if (l != null) {
			l.unsubscribe();
		}
	}

}
