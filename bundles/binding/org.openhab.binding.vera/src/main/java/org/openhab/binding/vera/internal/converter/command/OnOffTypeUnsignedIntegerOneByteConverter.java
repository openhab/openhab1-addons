/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter.command;

import org.fourthline.cling.model.types.UnsignedIntegerOneByte;
import org.openhab.binding.vera.internal.converter.VeraCommandConverter;
import org.openhab.core.library.types.OnOffType;

/**
 * Converts an {@link OnOffType} command into an {@link UnsignedIntegerOneByte} value.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class OnOffTypeUnsignedIntegerOneByteConverter extends VeraCommandConverter<OnOffType, UnsignedIntegerOneByte> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UnsignedIntegerOneByte convert(OnOffType command) {
		return new UnsignedIntegerOneByte(OnOffType.OFF.equals(command) ? 0 : 100);
	}

}
