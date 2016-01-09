package org.openhab.binding.connectsdk.internal.bridges;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.VolumeControl;
// this class is only an experiment - it seems difficult to setup an item in the gui that sends Increase decrease commands
public class VolumeControlUpDown extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	private static final Logger logger = LoggerFactory.getLogger(VolumeControlUpDown.class);
	
	@Override
	protected String getItemProperty() {
		return "updown";
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
			IncreaseDecreaseType onOffType;
			if (command instanceof IncreaseDecreaseType) {
				onOffType = (IncreaseDecreaseType) command;
			} else if (command instanceof StringType) {
				onOffType = IncreaseDecreaseType.valueOf(command.toString());
			} else {
				logger.warn("only accept IncreaseDecreaseType");
				return;
			}
			if(IncreaseDecreaseType.INCREASE.equals(onOffType)) {
				getControl(d).volumeUp(createDefaultResponseListener());				
			} else {
				getControl(d).volumeDown(createDefaultResponseListener());	
			}
		}

	}

}