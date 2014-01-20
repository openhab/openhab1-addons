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
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.types.UnDefType;

/**
 * A StringItem can be used for any kind of string to either send or receive
 * from a device.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class StringItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

	static {
		acceptedDataTypes.add(StringType.class);
		acceptedDataTypes.add((DateTimeType.class));
		acceptedDataTypes.add(UnDefType.class);

		acceptedCommandTypes.add(StringType.class);
	}
	
	public StringItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	@Override
	public State getStateAs(Class<? extends State> typeClass) {
		ArrayList<Class<? extends State>> list = new ArrayList<Class<? extends State>>();
		list.add(typeClass);
		State convertedState = TypeParser.parseState(list, state.toString());
		if(convertedState!=null) {
			return convertedState;
		} else {
			return super.getStateAs(typeClass);
		}
	}
}
