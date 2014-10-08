/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter.state;

import org.openhab.binding.vera.internal.converter.VeraStateConverter;
import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts a {@link Boolean} value into an {@link OpenClosedType} state.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class BooleanOpenClosedTypeConverter extends VeraStateConverter<Boolean, OpenClosedType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OpenClosedType convert(Boolean value) {
		return Boolean.TRUE.equals(value) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
	}

}
