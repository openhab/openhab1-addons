/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver.hub;

import java.util.Arrays;
/**
 * ReadByteBuffer buffer class
 * 
 * @author Daniel Pfrommer
 * @since 1.7.0
 */

public class ReadByteBuffer {
    private byte	m_buf[]; // the actual buffer
    private	int		m_count; // number of valid bytes
    private int 	m_index = 0; // current read index

    /**
     * Constructor for ByteArrayIO with dynamic size
     * @param   size initial size, but will grow dynamically
     */
    public ReadByteBuffer(int size) {
        m_buf = new byte[size];
    }
    /**
     * Number of unread bytes
     * @return number of bytes not yet read
     */
    public synchronized int remaining() {
    	return m_count - m_index;
    }
    
    /**
     * Blocking read of a single byte
     * @return byte read
     */
    public synchronized byte get() {
    	while (remaining() < 1) {
    		try {
    			wait();
    		} catch (InterruptedException e) {
    			return (0);
    		}
    	}
    	return m_buf[m_index++];
    }
    /**
     * Blocking read of multiple bytes
     * @param bytes destination array for bytes read
     * @param off offset into dest array
     * @param len max number of bytes to read into dest array
     * @return number of bytes actually read
     */
    public synchronized int get(byte[] bytes, int off, int len) {
    	while (remaining() < 1) {
    		try {
    			wait();
    		} catch (InterruptedException e) {
    			return 0;
    		}
    	}
    	int b = Math.min(len, remaining());
    	System.arraycopy(m_buf, m_index, bytes, off, b);
    	m_index += b;
    	return b;
    }
    /**
     * Adds bytes to the byte buffer
     * @param b byte array with new bytes
     * @param off starting offset into buffer
     * @param len number of bytes to add
     */
    private synchronized void add(byte b[], int off, int len) {
    	if ((off < 0) || (off > b.length) || (len < 0) ||
    			((off + len) > b.length) || ((off + len) < 0)) {
    		throw new IndexOutOfBoundsException();
    	} else if (len == 0) {
    		return;
    	}
    	int nCount = m_count + len;
    	if (nCount > m_buf.length) {
    		// dynamically grow the array
    		m_buf = Arrays.copyOf(m_buf, Math.max(m_buf.length << 1, nCount));
    	}
    	// append new data to end of buffer
    	System.arraycopy(b, off, m_buf, m_count, len);
    	m_count = nCount;
    	notifyAll();
    }
    /**
     * Adds bytes to the byte buffer 
     * @param b the new bytes to be added 
     */
    public void add(byte[] b) {
    	add(b, 0, b.length);
    }
    
    /**
     * Shrink the buffer to smallest size possible
     */
    public synchronized void makeCompact() {
    	if (m_index == 0) return;
    	byte[] newBuf = new byte[remaining()];
    	System.arraycopy(m_buf, m_index, newBuf, 0, newBuf.length);
    	m_index	= 0;
    	m_count	= newBuf.length;
    	m_buf	= newBuf;
    }
}