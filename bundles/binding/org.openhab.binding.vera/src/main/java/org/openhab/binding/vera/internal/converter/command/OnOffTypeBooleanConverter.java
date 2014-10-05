/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter.command;

import org.openhab.binding.vera.internal.converter.VeraCommandConverter;
import org.openhab.core.library.types.OnOffType;

/**
 * Converts an {@link OnOffType} command into a {@link Boolean} value.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class OnOffTypeBooleanConverter extends VeraCommandConverter<OnOffType, Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean convert(OnOffType command) {
		return OnOffType.ON.equals(command);
	}

}
