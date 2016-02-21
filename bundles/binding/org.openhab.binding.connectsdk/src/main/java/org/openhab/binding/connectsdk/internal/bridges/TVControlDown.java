package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.VolumeControl.MuteListener;
import com.connectsdk.service.command.ServiceSubscription;

public class TVControlDown extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	
	@Override
	protected String getItemProperty() {
		return "down";
	}

	@Override
	protected String getItemClass() {
		return "TVControl";
	}

	private TVControl getControl(final ConnectableDevice device) {
		return device.getCapability(TVControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(TVControl.Channel_Down)) {
			getControl(d).channelDown(createDefaultResponseListener());
		}

	}

	@Override
	protected ServiceSubscription<Void> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		return null;
	}

}