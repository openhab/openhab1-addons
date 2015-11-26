package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.PowerControl;
import com.connectsdk.service.command.ServiceSubscription;

public class PowerControlPower extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	private static final Logger logger = LoggerFactory.getLogger(PowerControlPower.class);

	@Override
	protected String getItemProperty() {
		return "power";
	}

	@Override
	protected String getItemClass() {
		return "PowerControl";
	}

	private PowerControl getControl(final ConnectableDevice device) {
		return device.getCapability(PowerControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property)) {
			OnOffType onOffType;
			if (command instanceof OnOffType) {
				onOffType = (OnOffType) command;
			} else if (command instanceof StringType) {
				onOffType = OnOffType.valueOf(command.toString());
			} else {
				logger.warn("only accept OnOffType");
				return;
			}

			if (OnOffType.ON.equals(onOffType) && d.hasCapabilities(PowerControl.On)) {
				getControl(d).powerOn(createDefaultResponseListener());
			}
			if (OnOffType.OFF.equals(onOffType) && d.hasCapabilities(PowerControl.Off)) {
				getControl(d).powerOff(createDefaultResponseListener());
			}
		}

	}

	@Override
	protected ServiceSubscription<Void> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		return null; // TODO: ideas how to set correct status on/off ? is presence of device or response to ping enough?
	}

}