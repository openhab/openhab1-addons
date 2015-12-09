/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.connection;

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckGenerator;

/**
 * A packet to be send to LCN-PCHK.
 * 
 * @author Tobias Jüttner
 */
abstract class SendData {
	
	/**
	 * Writes the packet's data into the given buffer.
	 * Called right before the packet is actually sent to LCN-PCHK.
	 * 
	 * @param buffer the target buffer
	 * @param localSegId the local segment id
	 * @return true if everything was set-up correctly and data was written
	 * @throws UnsupportedEncodingException if text could not be encoded for LCN-PCHK 
	 * @throws BufferOverflowException if target buffer has not enough space left (buffer will not be altered)
	 */
	abstract boolean write(ByteBuffer buffer, int localSegId) throws UnsupportedEncodingException, BufferOverflowException;
	
	/** A plain text to be send to LCN-PCHK. */
	static class PlainText extends SendData {
		
		/** The text. */
		private final String text;
		
		/**
		 * Constructor.
		 * 
		 * @param text the text
		 */
		PlainText(String text) {
			this.text = text;
		}

		/**
		 * Gets the text.
		 * 
		 * @return the text
		 */
		String getText() {
			return this.text;
		}
		
		/** {@inheritDoc} */
		@Override
		boolean write(ByteBuffer buffer, int localSegId) throws UnsupportedEncodingException, BufferOverflowException {
			buffer.put((this.text + PckGenerator.TERMINATION).getBytes(LcnDefs.LCN_ENCODING));
			return true;
		}
		
	}
	
	/**
	 * A PCK command to be send to LCN-PCHK.
	 * It is already encoded as bytes to allow different text-encodings (ANSI, UTF-8).
	 */
	static class PckSendData extends SendData {
		
		/** The target LCN address. */
		private final LcnAddr addr;
		
		/** true to acknowledge the command on receipt. */
		private final boolean wantsAck;
		
		/** PCK command (without address header) encoded as bytes. */
		private final ByteBuffer data;
		
		/**
		 * Constructor.
		 * 
		 * @param addr the target LCN address
		 * @param wantsAck true to claim receipt
		 * @param data the PCK command encoded as bytes
		 */
		PckSendData(LcnAddr addr, boolean wantsAck, ByteBuffer data) {
			this.addr = addr;
			this.wantsAck = wantsAck;
			this.data = data;
		}
		
		/**
		 * Gets the PCK command.
		 * 
		 * @return the PCK command encoded as bytes
		 */
		ByteBuffer getData() {
			return this.data;
		}
		
		/** {@inheritDoc} */
		@Override
		boolean write(ByteBuffer buffer, int localSegId) throws UnsupportedEncodingException, BufferOverflowException {
			if (this.addr.getSegId() != 3 && localSegId == -1) {  // Always allow commands to all segments (3)
				return false;
			}
			buffer.put(PckGenerator.generateAddressHeader(this.addr, localSegId, this.wantsAck).getBytes(LcnDefs.LCN_ENCODING));
			buffer.put(this.data);
			buffer.put(PckGenerator.TERMINATION.getBytes(LcnDefs.LCN_ENCODING));
			return true;
		}
		
	}
	
}
