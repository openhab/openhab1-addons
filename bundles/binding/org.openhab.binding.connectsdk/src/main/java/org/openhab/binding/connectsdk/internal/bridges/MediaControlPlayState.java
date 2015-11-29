package org.openhab.binding.connectsdk.internal.bridges;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaControl.PlayStateListener;
import com.connectsdk.service.capability.MediaControl.PlayStateStatus;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
// TODO: somehow this does not work with LG 60UF7709 spotify app or netflix
public class MediaControlPlayState extends AbstractOpenhabConnectSDKPropertyBridge<PlayStateListener> {
	private static final Logger logger = LoggerFactory.getLogger(MediaControlPlayState.class);
	@Override
	protected String getItemClass() {
		return "MediaControl";
	}
	
	@Override
	protected String getItemProperty() {
		return "playState";
	}

	private MediaControl getControl(final ConnectableDevice device) {
		return device.getCapability(MediaControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		
	}

	@Override
	protected ServiceSubscription<PlayStateListener> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		if (device.hasCapability(MediaControl.PlayState_Subscribe)) {
			return getControl(device).subscribePlayState(new PlayStateListener() {

						@Override
						public void onError(ServiceCommandError error) {
							logger.error("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
						}

						@Override
						public void onSuccess(PlayStateStatus status) {
							for (ConnectSDKBindingProvider provider : providers) {
								for (String itemName : provider.getItemNames()) {
									try {
										if (matchClassAndProperty(provider.getClassForItem(itemName),provider.getPropertyForItem(itemName))
												&& device.getIpAddress().equals(
														InetAddress.getByName(provider.getDeviceForItem(itemName))
																.getHostAddress())) {
											if (eventPublisher != null) {
												eventPublisher.postUpdate(itemName,
														new StringType(status.name()));
											}
										}
									} catch (UnknownHostException e) {
										logger.error("Failed to resolve {} to IP address. Skipping update on item {}.",
												device, itemName);
									}
								}
							}
						}
					});
		} else return null;
	}

}