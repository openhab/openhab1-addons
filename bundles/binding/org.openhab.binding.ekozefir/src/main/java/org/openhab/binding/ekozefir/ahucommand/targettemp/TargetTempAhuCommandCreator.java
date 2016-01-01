/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand.targettemp;

import java.util.Objects;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.AhuCommandCreator;
import org.openhab.binding.ekozefir.ahucommand.MessageBuilder;
import org.openhab.binding.ekozefir.exception.UnexpectedCommandException;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;

/**
 * Creator of command for changing target temperature in ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class TargetTempAhuCommandCreator implements AhuCommandCreator {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AhuCommand create(final Command state) {
		if (state instanceof DecimalType) {
			final float value = ((DecimalType) state).floatValue();
			return new AhuCommand() {
				private final int targetTempMessageType = 0x06;

				@Override
				public byte[] createMessage() {
					return MessageBuilder.setType(targetTempMessageType).appendFirstAndSecondParameter(value).build();
				}
			};
		}
		throw new UnexpectedCommandException(state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "target_temp";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Objects.toString("Ahu command creator id: " + getId());
	}
}
