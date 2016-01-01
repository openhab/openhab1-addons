/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand.refreshparameters;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.MessageBuilder;

/**
 * Command for refreshing all parameters of ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class RefreshParametersAhuCommand implements AhuCommand {

	private final int refreshParametersMessageType = 0xAA;
	/**
	 * Parameter used to Ekotouch driver
	 */
	private final int centralName = 'A';

	private final int refreshParameter = 0x55;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] createMessage() {
		return MessageBuilder.setType(refreshParametersMessageType).appendFirstParameter(refreshParameter)
				.appendSecondParameter(centralName).build();
	}

}
