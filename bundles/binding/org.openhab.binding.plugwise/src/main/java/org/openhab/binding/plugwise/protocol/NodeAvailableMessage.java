/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Node Available messages are broadcasted by Circles that are not yet part of a network
 * This is has to be tested because typically the network is "set up" using the Plugwise Source software, and never changed after
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class NodeAvailableMessage extends Message {
	

	public NodeAvailableMessage(int sequenceNumber,String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.NODE_AVAILABLE;	
		}
	
	
	@Override
	protected String payLoadToHexString() {
		return "";
	}

	@Override
	protected void parsePayLoad() {
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()){
			MAC = matcher.group(1);		
		}
		else {
			logger.debug("Plugwise protocol NodeAvailableMessage error: {} does not match", payLoad);
		}
		
	}

}
