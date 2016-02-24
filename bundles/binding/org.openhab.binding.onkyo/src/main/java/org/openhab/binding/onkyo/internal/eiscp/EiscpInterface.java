/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal.eiscp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onkyo.internal.OnkyoEventListener;
import org.openhab.binding.onkyo.internal.OnkyoStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Note:</b>Created this interface to handle both ethernet and serial 
 * connections to device</p><br> 
 * 
 * @author Tom Gutwin P.Eng
 * @author Thomas.Eichstaedt-Engelen (Refactoring)
 * @author Pauli Anttila (Simplified, rewritten and added status update listener functionality)
 * @author Sriram Balakrishnan (Added serial port support)
 */
public interface EiscpInterface {
	
	/**
	 * Add event listener, which will be invoked when status upadte is received from receiver.
	 **/
	public void addEventListener(OnkyoEventListener listener); 

	/**
	 * Remove event listener.
	 **/
	public void removeEventListener(OnkyoEventListener listener); 
	
	/**
	 * Get retry count value.
	 **/
	public int getRetryCount(); 

	/**
	 * Set retry count value. How many times command is retried when error occurs.
	 **/
	public void setRetryCount(int retryCount); 

	/**
	 * Connects to the receiver by opening a socket connection through the
	 * IP and port defined on constructor.
	 **/
	public boolean connectSocket(); 
	
	/**
	 * Closes the socket connection.
	 * 
	 * @return true if the closed successfully
	 **/
	public boolean closeSocket(); 

	/**
	 * Sends to command to the receiver.
	 * It does not wait for a reply.
	 * @param eiscpCmd the eISCP command to send.
	 **/
	public void sendCommand(String eiscpCmd); 

}
