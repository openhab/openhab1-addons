/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * A SwitchItem represents a normal switch that can be ON or OFF.
 * Useful for normal lights, presence detection etc.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class SwitchItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

	static {
		acceptedDataTypes.add(OnOffType.class);
		acceptedDataTypes.add(UnDefType.class);

		acceptedCommandTypes.add(OnOffType.class);
	}
	
	public SwitchItem(String name) {
		super(name);
	}

	public void send(OnOffType command) {
		internalSend(command);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	@Override
	public State getStateAs(Class<? extends State> typeClass) {
		if(typeClass==DecimalType.class) {
			return state==OnOffType.ON ? new DecimalType(1) : DecimalType.ZERO;
		} else if(typeClass==PercentType.class) {
			return state==OnOffType.ON ? PercentType.HUNDRED : PercentType.ZERO;
		} else {
			return super.getStateAs(typeClass);
		}
	}
}
