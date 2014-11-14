package org.openhab.binding.zibase.internal;

import java.lang.annotation.Inherited;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;

public class zibaseBindingConfigScenario extends zibaseBindingConfig {

	public zibaseBindingConfigScenario(String[] configParameters) {
		super(configParameters);
	}

	/**
	 * {@link Inherited}
	 */
	@Override
	public void sendCommand(Zibase zibase, Command command, int dim) {
		logger.error("sendCommand : not implemeted for Config receiver" );
	}

	/**
	 * {@link Inherited}
	 */
	protected boolean isItemConfigValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public State getOpenhabStateFromZibaseValue(String zbResponseStr) {
		// TODO Auto-generated method stub
		return null;
	}

}
