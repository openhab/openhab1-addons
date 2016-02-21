package org.openhab.binding.connectsdk.internal.bridges;

import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.VolumeControl;

public class VolumeControlUp extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	
	@Override
	protected String getItemProperty() {
		return "up";
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
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(VolumeControl.Volume_Up_Down)) {
			getControl(d).volumeUp(createDefaultResponseListener());
		}

	}

}