package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.VolumeControl.MuteListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;

public class VolumeControlMute extends AbstractOpenhabConnectSDKPropertyBridge<MuteListener> {
	private static final Logger logger = LoggerFactory.getLogger(VolumeControlMute.class);

	@Override
	protected String getItemProperty() {
		return "mute";
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
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(VolumeControl.Mute_Set)) {
			OnOffType onOffType;
			if (command instanceof OnOffType) {
				onOffType = (OnOffType) command;
			} else if (command instanceof StringType) {
				onOffType = OnOffType.valueOf(command.toString());
			} else {
				logger.warn("only accept OnOffType");
				return;
			}

			getControl(d).setMute(OnOffType.ON.equals(onOffType), createDefaultResponseListener());

		}

	}

	@Override
	protected ServiceSubscription<MuteListener> getSubscription(final ConnectableDevice device,
			final Collection<String> itemNames, final EventPublisher eventPublisher) {
		if (device.hasCapability(VolumeControl.Mute_Subscribe)) {
			return getControl(device).subscribeMute(new MuteListener() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.debug("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
				}

				@Override
				public void onSuccess(Boolean value) {
					if (eventPublisher != null) {
						for (String itemName : itemNames) {
							eventPublisher.postUpdate(itemName, value ? OnOffType.ON : OnOffType.OFF);
						}
					}
				}
			});
		} else
			return null;

	}

}