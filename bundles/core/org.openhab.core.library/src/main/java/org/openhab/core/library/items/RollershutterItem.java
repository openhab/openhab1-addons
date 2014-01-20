/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.items;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * A RollershutterItem allows the control of roller shutters, i.e. 
 * moving them up, down, stopping or setting it to close to a certain percentage.
 *  
 * @author Kai Kreuzer
 *
 */
public class RollershutterItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();
	
	static {
		acceptedDataTypes.add(UnDefType.class);
		acceptedDataTypes.add(UpDownType.class);
		acceptedDataTypes.add(PercentType.class);
		
		acceptedCommandTypes.add(UpDownType.class);
		acceptedCommandTypes.add(StopMoveType.class);
		acceptedCommandTypes.add(PercentType.class);
	}
	
	public RollershutterItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(State state) {
		// we map UP/DOWN values to the percent values 0 and 100
		if(state==UpDownType.UP) {
			super.setState(PercentType.ZERO);
		} else if(state==UpDownType.DOWN) {
			super.setState(PercentType.HUNDRED);
		} else {
			super.setState(state);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State getStateAs(Class<? extends State> typeClass) {
		if(typeClass==UpDownType.class) {
			if(state.equals(PercentType.ZERO)) {
				return UpDownType.UP;
			} else if(state.equals(PercentType.HUNDRED)) {
				return UpDownType.DOWN;
			} else {
				return UnDefType.UNDEF;
			}
		} else if(typeClass==DecimalType.class) {
			if(state instanceof PercentType) {
				return new DecimalType(((PercentType) state).toBigDecimal().divide(new BigDecimal(100), 8, RoundingMode.UP));
			}
		}
		return super.getStateAs(typeClass);
	}

}
