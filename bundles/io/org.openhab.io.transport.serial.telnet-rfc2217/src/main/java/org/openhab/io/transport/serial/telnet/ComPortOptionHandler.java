
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.net.telnet.TelnetOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFC 2217 telnet COM-PORT-OPTION.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class ComPortOptionHandler extends TelnetOptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final TelnetSerialPort port;
    private Set<ComportOptionChangedListener> listeners;
    
    
    protected ComPortOptionHandler(TelnetSerialPort telnetSerialPort) {
        super(RFC2217.COM_PORT_OPTION, true, false, true, false);
        this.listeners = new HashSet<ComportOptionChangedListener>();
        if (telnetSerialPort == null)
            throw new IllegalArgumentException("null telnetSerialPort");
        this.port = telnetSerialPort;
    }

    @Override
    public int[] answerSubnegotiation(int[] data, int length) {

        ComPortCommand command = getComPortCommand(data, length);
        
        // Notify port
        if(command != null) {
        	for(ComportOptionChangedListener listener : this.listeners) {
        		listener.comPortOptionChanged(command);
        	}
        	this.port.handleCommand(command);
        }
        return null;
    }

	protected ComPortCommand getComPortCommand(int[] data, int length) {
		// Copy data into buffer of the correct size
        if (data.length != length) {
            int[] data2 = new int[length];
            System.arraycopy(data, 0, data2, 0, length);
            data = data2;
        }

        // Decode option
        ComPortCommand command;
        try {
            command = RFC2217.decodeComPortCommand(data);
        } catch (IllegalArgumentException e) {
            log.error(this.port.getName() + ": rec'd invalid COM-PORT-OPTION command: " + e.getMessage());
            return null;
        }
		return command;
	}

    @Override
    public int[] startSubnegotiationLocal() {
        this.port.startSubnegotiation();
        return null;
    }

    @Override
    public int[] startSubnegotiationRemote() {
        return null;
    }
    
    public void addComPortOptionListener(ComportOptionChangedListener listener) {
    	this.listeners.add(listener);
    }
    
    public void removeComPortOptionListener(ComportOptionChangedListener listener) {
    	this.listeners.remove(listener);
    }
}

