/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.lang.annotation.Inherited;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.Zibase;

/**
 * This class handle scenarios stored in Zibase memory
 * @author Julien Tiphaine
 *
 */
public class ZibaseBindingConfigScenario extends ZibaseBindingConfig {

	private int scenarionNumber;
	
	public ZibaseBindingConfigScenario(String[] configParameters) {
		super(configParameters);
		
		scenarionNumber = Integer.parseInt(configParameters[ZibaseBindingConfig.POS_VALUES]);
	}

	/**
	 * command and dim ignored for scenario execution
	 * {@link Inherited}
	 */
	@Override
	public void sendCommand(Zibase zibase, Command command, int dim) {
		zibase.launchScenario(scenarionNumber);
	}

	/**
	 * {@link Inherited}
	 */
	protected boolean isItemConfigValid() {
		logger.info("Checking config for scenario item " + this.getId());
		
		try {
			Integer.parseInt(this.values[ZibaseBindingConfig.POS_VALUES]);
			return true;
		} catch (NumberFormatException ex) {
			logger.error("bad scenario id : " + this.getId());
		}
		
		return false;
	}

	/**
	 * TODO : check whether info about executed scenario can be used
	 * {@link Inherited}
	 */
	@Override
	public State getOpenhabStateFromZibaseValue(Zibase zibase, String zbResponseStr) {
		// TODO Auto-generated method stub
		return null;
	}

}
