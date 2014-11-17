/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.Zibase;

/**
 * This class handle calendars stored in the Zibase
 * TODO : Implement
 * @author Julien Tiphaine
 *
 */
public class ZibaseBindingConfigCalendar extends ZibaseBindingConfig {

	public ZibaseBindingConfigCalendar(String[] configParameters) {
		super(configParameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State getOpenhabStateFromZibaseValue(Zibase zibase, String zbResponseStr) {
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
