/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.openhab.binding.rfxcom.internal.messages.RFXComTransmitterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose is to register and provide {@link Future futures} to conveniently receive results.
 * 
 * @author Jürgen Richtsfeld
 * @since 1.7
 */
class ResultRegistry {
	private static final Logger logger = LoggerFactory.getLogger(ResultRegistry.class);

	private final Map<Byte, RFXComResponse> results = new HashMap<Byte, RFXComResponse>(40); 

	public Future<RFXComTransmitterMessage> registerCommand(final byte seqNumber) {
		synchronized(results) {
			final Future<RFXComTransmitterMessage> old = results.get(seqNumber);
			assert old == null : "found a leftover future";
			if(old != null) {
				logger.warn("cancelling old future of sequence number" + seqNumber);
				old.cancel(true);
			}
			
			final RFXComResponse result = new RFXComResponse();
			results.put(seqNumber, result);
			return result;
		}
	}
	
	public void responseReceived(final RFXComTransmitterMessage response) {
		synchronized(results) {
			final RFXComResponse future = results.get(response.seqNbr);
			if(future != null) {
				logger.debug("Transmitter response received:\n{}", response.toString());
				future.set(response);
				results.remove(response.seqNbr);
			} else {
				logger.warn("Transmitter response received for sequence {} but no listener is registered:\n{}", 
						response.seqNbr, response.toString());
			}
		}
	}

}
