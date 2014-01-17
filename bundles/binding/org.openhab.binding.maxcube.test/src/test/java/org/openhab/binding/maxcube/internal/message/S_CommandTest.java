/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * 
 */
package org.openhab.binding.maxcube.internal.message;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import org.junit.Before;
import org.openhab.binding.maxcube.internal.Utils;

import junit.framework.Assert;

/**
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class S_CommandTest {

	public final String foo = "s:AARAAAAAAP4wAaiLix8=\r\n";
	
	@Test
	public void PrefixTest() {
		S_Command scmd = new S_Command("00FE30", 1, 20.0);
		
		String commandStr = scmd.getCommandString();
		
		String prefix = commandStr.substring(0, 2);
		
		Assert.assertEquals("s:", prefix);
		
	}
	
	@Test
	public void BaseCommandTest() {
		S_Command scmd = new S_Command("00FE30", 1, 20.0);
		
		String commandStr = scmd.getCommandString();
		
		String base64Data = commandStr.substring(3);
		byte[] bytes = Base64.decodeBase64(base64Data.getBytes());
		
		int[] data = new int[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			data[i] = bytes[i] & 0xFF;
		}
		
		String decodedString = Utils.toHex(data);
		System.out.print("\n" + decodedString);
		
	}
}