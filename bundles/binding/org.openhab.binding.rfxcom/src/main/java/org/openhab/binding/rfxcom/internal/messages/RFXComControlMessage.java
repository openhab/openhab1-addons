/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.List;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * RFXCOM data class for control message.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComControlMessage extends RFXComBaseMessage {

	public RFXComControlMessage() {

	}

	public RFXComControlMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public byte[] decodeMessage() {
		return null;
	}

	@Override
	public void encodeMessage(byte[] data) {
		super.encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();

		return str;
	}

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		
		throw new RFXComException("Not supported");
	}

	@Override
	public void convertFromState(RFXComValueSelector valueSelector, String id,
			Object subType, Type type, byte seqNumber) throws RFXComException {
		
		throw new RFXComException("Not supported");
	}

	@Override
	public Object convertSubType(String subType) throws RFXComException {
		
		throw new RFXComException("Not supported");
	}

	@Override
	public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
		return null;
	}
}
