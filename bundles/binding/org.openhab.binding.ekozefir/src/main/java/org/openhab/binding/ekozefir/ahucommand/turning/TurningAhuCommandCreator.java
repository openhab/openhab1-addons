/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand.turning;

import java.util.Map;
import java.util.Objects;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.AhuCommandCreator;
import org.openhab.binding.ekozefir.exception.UnexpectedCommandException;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

import com.google.common.collect.ImmutableMap;

/**
 * Creator of command for turning on or off ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class TurningAhuCommandCreator implements AhuCommandCreator {

	private final Map<OnOffType, AhuCommand> values = ImmutableMap.of(OnOffType.OFF, new TurnOffCommand(),
			OnOffType.ON, new TurnOnCommand());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AhuCommand create(Command state) {
		if (state instanceof OnOffType) {
			OnOffType type = (OnOffType) state;
			if (values.containsKey(type)) {
				return values.get(type);
			}
		}
		throw new UnexpectedCommandException(state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "turning";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Objects.toString("Ahu command creator id: " + getId());
	}

}
