package org.openhab.binding.maxcul.internal;

import java.util.TimerTask;

import org.openhab.binding.maxcul.internal.messages.SetTemperatureMsg;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

public class MaxCulPacedThermostatTransmitTask extends TimerTask {

	private MaxCulMsgHandler messageHandler;
	private Command command;
	private MaxCulBindingConfig bindingConfig;

	public MaxCulPacedThermostatTransmitTask( Command cmd, MaxCulBindingConfig cfg, MaxCulMsgHandler msgHandler)
	{
		command = cmd;
		bindingConfig = cfg;
		messageHandler=msgHandler;
	}

	@Override
	public void run() {
		if (command instanceof OnOffType) {
			if (((OnOffType)command) == OnOffType.ON)
				messageHandler.sendSetTemperature(bindingConfig.devAddr, SetTemperatureMsg.TEMPERATURE_ON);
			else if (((OnOffType)command) == OnOffType.OFF)
				messageHandler.sendSetTemperature(bindingConfig.devAddr, SetTemperatureMsg.TEMPERATURE_OFF);
		} else if (command instanceof DecimalType) {
			messageHandler.sendSetTemperature(bindingConfig.devAddr, ((DecimalType)command).doubleValue());
		}
	}

}
