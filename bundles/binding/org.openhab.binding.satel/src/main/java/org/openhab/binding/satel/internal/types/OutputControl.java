/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * Available output control types:
 * <ul>
 * <li>ON - sets an output</li>
 * <li>OFF - resets an output</li>
 * <li>TOGGLE - inverts output state</li>
 * </ul>
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum OutputControl implements ControlType {
	ON(0x88), OFF(0x89), TOGGLE(0x91);

	private byte controlCommand;

	OutputControl(int controlCommand) {
		this.controlCommand = (byte) controlCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getControlCommand() {
		return controlCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectType getObjectType() {
		return ObjectType.OUTPUT;
	}
}
