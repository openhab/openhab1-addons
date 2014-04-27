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
 * DataFrame received from PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class ReceiveDataFrame extends DataFrame {

	private boolean acknowledgement;
	private boolean itself;
	private boolean risc;

	public ReceiveDataFrame() {
		super();
	}
	

	public boolean isAcknowledgement() {
		return acknowledgement;
	}

	public boolean isItself() {
		return itself;
	}

	public boolean isRisc() {
		return risc;
	}

	public void parse(byte[] data) {
		super.parse(data);
		acknowledgement = ((data[5] & 0x20) == 0x20);
		itself = ((data[5] & 0x10) == 0x10);
		risc = ((data[5] & 0x08) == 0x08);
	}
	
}
