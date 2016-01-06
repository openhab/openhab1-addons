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

import org.openhab.binding.satel.internal.types.DoorsState;
import org.openhab.binding.satel.internal.types.ZoneState;
import org.openhab.binding.satel.internal.types.OutputState;
import org.openhab.binding.satel.internal.types.StateType;
import org.openhab.binding.satel.internal.types.PartitionState;

/**
 * Event class describing current state of zones/partitions/outputs/doors.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateEvent implements SatelEvent {

	private StateType stateType;
	private BitSet stateBits;

	/**
	 * Constructs new event instance from given state type and state bits.
	 * 
	 * @param stateType
	 *            type of state
	 * @param stateBits
	 *            state bits as byte array
	 */
	public IntegraStateEvent(StateType stateType, byte[] stateBits) {
		this.stateType = stateType;
		this.stateBits = BitSet.valueOf(stateBits);
	}

	/**
	 * Returns type of state described by this event object.
	 * 
	 * @return type of state for this event
	 * @see ZoneState
	 * @see PartitionState
	 * @see OutputState
	 * @see DoorsState
	 */
	public StateType getStateType() {
		return this.stateType;
	}

	/**
	 * Returns state bits as {@link BitSet}.
	 * 
	 * @return state bits
	 */
	public BitSet getStateBits() {
		return stateBits;
	}

	/**
	 * Returns <code>true</code> if specified state bit is set.
	 * 
	 * @param nbr
	 *            state bit number
	 * @return <code>true</code> if state bit is set
	 */
	public boolean isSet(int nbr) {
		return stateBits.get(nbr);
	}

	/**
	 * Returns number of state bits that are active.
	 * 
	 * @return number of active states
	 */
	public int statesSet() {
		return stateBits.cardinality();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder bitsStr = new StringBuilder();
		for (int i = this.stateBits.nextSetBit(0); i >= 0; i = this.stateBits.nextSetBit(i + 1)) {
			if (bitsStr.length() > 0) {
				bitsStr.append(",");
			}
			bitsStr.append(Integer.toString(i + 1));
		}
		return String.format("IntegraStateEvent: object = %s, state = %s, active = [%s]", stateType.getObjectType(),
				stateType, bitsStr);
	}
}
