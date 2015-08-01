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
 * Available doors states.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum DoorsState implements StateType {
	OPENED(0x18), OPENED_LONG(0x19);

	private byte refreshCommand;

	DoorsState(int refreshCommand) {
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
		return ObjectType.DOORS;
	}
}
