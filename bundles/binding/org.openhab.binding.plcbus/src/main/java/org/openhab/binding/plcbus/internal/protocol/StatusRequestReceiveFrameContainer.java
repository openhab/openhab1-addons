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
 * ReceiveFrameContainer for the StatusRequest Command
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class StatusRequestReceiveFrameContainer extends
		AbstractReceiveFrameContainer {

	@Override
	public boolean isReceivingCompleted() {
		return receiveFrames.size() == 3;
	}

	@Override
	public ReceiveFrame getAnswerFrame() {
		if (receiveFrames.size() < 3) {
			return null;
		}

		return receiveFrames.get(2);
	}

}
