/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Frame received from PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class ReceiveFrame extends Frame<ReceiveDataFrame> {

	private byte endByte;

	public ReceiveFrame() {
		super();
	}

	
	@Override
	public byte getEndByte() {
		return endByte;
	}

	public boolean isAcknowledgement() {
		return getData().isAcknowledgement();
	}

	public int getFirstParameter() {
		return getData().getFirstParameter();
	}

	public int getSecondParameter() {
		return getData().getSecondParameter();
	}

	public void parse(byte[] bytes) {
		data = new ReceiveDataFrame();
		data.parse(bytes);
		endByte = bytes[bytes.length - 1];
	}
	
}
