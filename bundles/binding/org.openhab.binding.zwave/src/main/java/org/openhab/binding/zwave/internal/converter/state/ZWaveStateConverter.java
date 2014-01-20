/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.state;

import java.lang.reflect.ParameterizedType;

import org.openhab.core.types.State;

/**
 * Converts raw Z-Wave values to openHAB states.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 * @param <ZWAVE_TYPE> the type of the raw Z-Wave value.
 * @param <OPENHAB_TYPE> the type of the openHAB state.
 */
public abstract class ZWaveStateConverter<ZWAVE_TYPE, OPENHAB_TYPE extends State> {
	
	/**
	 * Returns the type of the openHAB {@link State} that this {@link ZWaveStateConverter} converts to.
	 * @return the supported {@link State}
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends State> getState() {
		return (Class<? extends State>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	/**
	 * Returns the type the state converter converts from.
	 * @return the supported Z-Wave type
	 */
	public Class<?> getType() {
		return (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * Converts a Z-Wave value to an OpenHab state.
	 * @param value the Z-Wave value to convert.
	 * @return the openHAB state to convert to.
	 */
	protected abstract OPENHAB_TYPE convert(ZWAVE_TYPE value);
	
	/**
	 * Converts a Z-Wave value to an OpenHab state.
	 * @param value the Z-Wave value to convert.
	 * @return the openHAB state to convert to.
	 */
	@SuppressWarnings("unchecked")
	public State convertFromValueToState(Object value) {
		
		if (value == null)
			return null;
		
		return convert((ZWAVE_TYPE) value);
	}
}
