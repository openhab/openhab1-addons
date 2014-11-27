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
public enum InputState implements StateType {
	violation(0x00), tamper(0x01), alarm(0x02), tamper_alarm(0x03), alarm_memory(0x04), tamper_alarm_memory(0x05), bypass(
			0x06), no_violation_trouble(0x07), long_violation_trouble(0x08), isolate(0x26), masked(0x28), masked_memory(
			0x29);

	private byte refreshCommand;

	InputState(int refreshCommand) {
		this.refreshCommand = (byte) refreshCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getRefreshCommand() {
		return refreshCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectType getObjectType() {
		return ObjectType.input;
	}
}