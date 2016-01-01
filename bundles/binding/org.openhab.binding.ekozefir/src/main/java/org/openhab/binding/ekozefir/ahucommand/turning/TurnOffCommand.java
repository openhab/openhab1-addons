/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand.turning;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.MessageBuilder;

/**
 * Command for turning off ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class TurnOffCommand implements AhuCommand {
	private final int turningMessageType = 0x01;
	private final int turnOffValue = 0x01;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] createMessage() {
		return MessageBuilder.setType(turningMessageType).appendFirstParameter(turnOffValue).build();
	}
}
