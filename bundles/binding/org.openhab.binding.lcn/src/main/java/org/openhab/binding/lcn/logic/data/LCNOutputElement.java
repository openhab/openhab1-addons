/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

import java.nio.ByteBuffer;

/**
 * Data class that combines LCNChannels and an output buffer for that channel.
 * 
 * @author Patrik Pastuschek
 * @since  1.7.0
 * 
 **/
public class LCNOutputElement {
	
	public LCNChannel lCNChannel;
	public ByteBuffer buffer;
	public String moduleKey;

	/**
	 * Constructor for the LCNOutputElement.
	 * @param lCNChannel A LCNChannel that defines the target.
	 * @param buffer A ByteBuffer with the output.
	 * @param key The Key to the LCN Module to check back on firmware etc.
	 */
	public LCNOutputElement(LCNChannel lCNChannel, ByteBuffer buffer, String key) {
		this.lCNChannel = lCNChannel;
		this.buffer = buffer;		
		this.moduleKey = key;
	}

}