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
 * DefaultReceiveFrameContainer for PLCBus in One - Phase - Configuration
 * 
 * @author Robin Lenz
 * @since 1.1.0 
 */
public class DefaultOnePhaseReceiveFrameContainer extends AbstractReceiveFrameContainer {

	@Override
	public boolean isReceivingCompleted() {
		return receiveFrames.size() == 2;
	}

	@Override
	public ReceiveFrame getAnswerFrame() {
		if (receiveFrames.size() < 2) {
			return null;
		}

		return receiveFrames.get(1);
	}

}
