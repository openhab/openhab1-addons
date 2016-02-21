package org.openhab.binding.connectsdk.internal.bridges;

import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MediaControl;

public class MediaControlForward extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	
	@Override
	protected String getItemClass() {
		return "MediaControl";
	}
	
	@Override
	protected String getItemProperty() {
		return "forward";
	}

	private MediaControl getControl(final ConnectableDevice device) {
		return device.getCapability(MediaControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(MediaControl.FastForward)) {
			getControl(d).fastForward(createDefaultResponseListener());
		}

	}

}