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

import org.openhab.core.types.Command;

/**
 * Converts a <code>OPENHAB_COMMAND</code> command into a <code>VERA_TYPE</code> value.
 *
 * @param <OPENHAB_COMMAND>
 * @param <VERA_TYPE>
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public abstract class VeraCommandConverter<OPENHAB_COMMAND extends Command, VERA_TYPE> extends VeraConverter {

	/**
	 * {@inheritDoc}
	 */
	protected Class<?> getValueType() {
		return (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	/**
	 * Converts the given <code>command</code> to a <code>VERA_TYPE</code> value.
	 * @param command the <code>command</code> to convert
	 * @return the <code>VERA_TYPE</code> value
	 */
	@SuppressWarnings("unchecked")
	public VERA_TYPE convertFromCommandToValue(Object command) {
		if (command == null)
			return null;
		return convert((OPENHAB_COMMAND) command);
	}
	
	/**
	 * Converts the given <code>command</code> to a <code>VERA_TYPE</code> value.
	 * @param command the <code>command</code> to convert
	 * @return the <code>VERA_TYPE</code> value
	 */
	protected abstract VERA_TYPE convert(OPENHAB_COMMAND command);
	
}
