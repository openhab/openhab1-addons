/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.media.types;

import java.util.SortedMap;
import java.util.TreeMap;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * This type can be used for items that are dealing with media player functionality.
 * 
 * @author Karel Goderis
 * @since 1.5.0
 * 
 */
public class PlayerType extends PercentType implements ComplexType, State, Command {

	// constants for the constituents
	static final public String KEY_STATE = "t";
	static final public String KEY_VOLUME = "v";
	static final public String KEY_SOURCE = "s";
	
	// constants for media player states
	static final public PlayerType ALLOFF = new PlayerType(PlayerStateType.OFF, PercentType.ZERO, StringType.EMPTY);
	static final public PlayerType POWERON = new PlayerType(PlayerStateType.ON, PercentType.ZERO, StringType.EMPTY);
	
	protected PlayerStateType state;
	protected String source;
	// the inherited field "value" of the parent DecimalType corresponds to the
	// "volume"
	
	
	public PlayerType(PlayerStateType t, PercentType v, StringType s) {
		this.state = t;
		this.value = v.toBigDecimal();
		this.source = s.toString();
	}

	@Override
	public SortedMap<String, PrimitiveType> getConstituents() {
		TreeMap<String, PrimitiveType> map = new TreeMap<String, PrimitiveType>();
		map.put(KEY_STATE, getState());
		map.put(KEY_VOLUME, getVolume());
		map.put(KEY_SOURCE, getSource());
		return map;
	}
	
	public PlayerStateType getState() {
		return state;
	}
	
	public PercentType getVolume() {
		return new PercentType(value);
	}
	
	public StringType getSource() {
		return new StringType(source);
	}
	
	public String toString() {
		return getState() + "," + getVolume() + "," + getSource();
	}

}
