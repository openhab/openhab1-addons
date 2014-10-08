/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter;

import java.lang.reflect.ParameterizedType;

import org.openhab.core.types.State;

/**
 * Converts a <code>VERA_TYPE</code> value into a <code>OPENHAB_STATE</code> state.
 *
 * @param <VERA_TYPE>
 * @param <OPENHAB_STATE>
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public abstract class VeraStateConverter<VERA_TYPE, OPENHAB_STATE extends State> extends VeraConverter {

	/**
	 * {@inheritDoc}
	 */
	protected Class<?> getValueType() {
		return (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * Converts the given <code>value</code> to a <code>OPENHAB_STATE</code> state.
	 * @param value the <code>value</code> to convert
	 * @return the <code>OPENHAB_STATE</code> state
	 */	
	@SuppressWarnings("unchecked")
	public OPENHAB_STATE convertFromValueToState(Object value) {
		if (value == null)
			return null;
		return convert((VERA_TYPE) value);
	}
	
	/**
	 * Converts the given <code>value</code> to a <code>OPENHAB_STATE</code> state.
	 * @param value the <code>value</code> to convert
	 * @return the <code>OPENHAB_STATE</code> state
	 */	
	protected abstract OPENHAB_STATE convert(VERA_TYPE value);
	
}
