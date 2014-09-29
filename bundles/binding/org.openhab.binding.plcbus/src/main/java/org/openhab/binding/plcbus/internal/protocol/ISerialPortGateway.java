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
 * Interface for a SerialPortGateway
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public interface ISerialPortGateway {

	public void send(TransmitFrame frame, IReceiveFrameContainer container);

	public void close();
	
}
