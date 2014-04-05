/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

/**
 * Trigger a "reset" of the plugwise network - currently not used in the binding, maybe in the future
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class NetworkResetRequestMessage extends Message {

	public NetworkResetRequestMessage(String payLoad) {
		super(payLoad);
		type = MessageType.NETWORK_RESET_REQUEST;
		payLoad = "00";
		MAC = "";
	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}

}
