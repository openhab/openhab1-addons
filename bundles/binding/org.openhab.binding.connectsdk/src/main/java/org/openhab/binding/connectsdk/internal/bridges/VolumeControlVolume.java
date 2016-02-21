package org.openhab.binding.connectsdk.internal.bridges;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.VolumeControl.VolumeListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;

public class VolumeControlVolume extends AbstractOpenhabConnectSDKPropertyBridge<VolumeListener> {
	private static final Logger logger = LoggerFactory.getLogger(VolumeControlVolume.class);

	@Override
	protected String getItemProperty() {
		return "volume"; 
	}

	@Override
	protected String getItemClass() {
		return "VolumeControl"; 
	}

	private VolumeControl getControl(final ConnectableDevice device) {
		return device.getCapability(VolumeControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(VolumeControl.Volume_Set)) {

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
			
			getControl(d).setVolume(percent.floatValue() / 100.0f, createDefaultResponseListener());

		}

	}

	@Override
	protected ServiceSubscription<VolumeListener> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		if (device.hasCapability(VolumeControl.Volume_Subscribe)) {
			return getControl(device).subscribeVolume(new VolumeListener() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.error("error: ", error.getMessage());
				}

				@Override
				public void onSuccess(Float value) {
					for (ConnectSDKBindingProvider provider : providers) {
						for (String itemName : provider.getItemNames()) {
							try {
								if (matchClassAndProperty(provider.getClassForItem(itemName),
										provider.getPropertyForItem(itemName))
										&& device.getIpAddress().equals(
												InetAddress.getByName(provider.getDeviceForItem(itemName))
														.getHostAddress())) {
									if (eventPublisher != null) {
										eventPublisher.postUpdate(itemName,	new PercentType(Math.round(value * 100)));
									}
								}
							} catch (UnknownHostException e) {
								logger.error("Failed to resolve {} to IP address. Skipping update on item {}.", device,
										itemName);
							}
						}
					}
				}
			});
		} else
			return null;
	}

}