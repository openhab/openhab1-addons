/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * TODO document me!
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum ZoneControl implements ControlType {
	// TODO add force arming
	arm_mode_0(0x80), arm_mode_1(0x81), arm_mode_2(0x82), arm_mode_3(0x83), disarm(0x84);

	private byte controlCommand;

	ZoneControl(int controlCommand) {
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
		return ObjectType.zone;
	}
}
