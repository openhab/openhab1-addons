/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.event;

import java.util.BitSet;

/**
 * Event class describing changes in Integra state since last state read.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class NewStatesEvent implements SatelEvent {

	private BitSet newStates;

	/**
	 * Constructs event class from given {@link BitSet}.
	 * 
	 * @param newStates
	 *            changed states as {@link BitSet}
	 */
	public NewStatesEvent(BitSet newStates) {
		this.newStates = newStates;
	}

	/**
	 * Constructs event class from given byte array.
	 * 
	 * @param newStates
	 *            changed states as byte array
	 */
	public NewStatesEvent(byte[] newStates) {
		this(BitSet.valueOf(newStates));
	}

	/**
	 * Checks if specified state has changed since last read.
	 * 
	 * @param nbr
	 *            state number to check
	 * @return <code>true</code> if state has changed
	 */
	public boolean isNew(int nbr) {
		return newStates.get(nbr);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder newStatesStr = new StringBuilder();
		for (int i = this.newStates.nextSetBit(0); i >= 0; i = this.newStates.nextSetBit(i + 1)) {
			if (newStatesStr.length() > 0) {
				newStatesStr.append(",");
			}
			newStatesStr.append(String.format("%02X", i));
		}
		return String.format("NewStatesEvent: changed = [%s]", newStatesStr);
	}
}
