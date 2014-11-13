package org.openhab.binding.zibase.internal;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;

public class zibaseBindingConfigCalendar extends zibaseBindingConfig {

	public zibaseBindingConfigCalendar(String[] configParameters) {
		super(configParameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State getOpenhabStateFromZibaseValue(ZbResponse zbResponse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCommand(Zibase zibase, Command command, int dim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isItemConfigValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
