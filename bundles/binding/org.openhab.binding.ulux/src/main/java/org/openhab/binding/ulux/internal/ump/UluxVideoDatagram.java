/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import java.net.InetAddress;

import org.openhab.binding.ulux.internal.ump.messages.VideoStreamMessage;

/**
 * A {@link UluxVideoDatagram} is a special {@link UluxDatagram} (distinguishable by a different magic byte) that
 * contains exactly {@link VideoStreamMessage}.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxVideoDatagram extends UluxMessageDatagram {

	public UluxVideoDatagram(short switchId, InetAddress switchAddress, VideoStreamMessage message) {
		super(switchId, switchAddress);
		super.addMessage(message);
	}

	@Override
	public void addMessage(UluxMessage message) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected byte getMagicByte() {
		return (byte) 0x02;
	}

}
