/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.openhab.binding.maxcube.internal.Utils;

/**
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class UtilsTest {

	@Test
	public void fromHexTest() {
		
		int ar0 = Utils.fromHex("00");
		int ar1 = Utils.fromHex("01");
		int ar31 = Utils.fromHex("1F");
		int ar255 = Utils.fromHex("FF");
	
		Assert.assertEquals(0, ar0);
		Assert.assertEquals(1, ar1);
		Assert.assertEquals(31, ar31);
		Assert.assertEquals(255, ar255);
	}
	
	@Test
	public void fromByteTest() {
		
		byte b0 = 0;
		byte b127 = 127;
		byte b128 = (byte) 128; // overflow due to 
		byte bn128 = -128;		// signed bytes
		byte bn1 = -1;
		
		int ar0 = Utils.fromByte(b0);
		int ar127 = Utils.fromByte(b127);
		int ar128 = Utils.fromByte(b128);
		int arn128 = Utils.fromByte(bn128);
		int arn1 = Utils.fromByte(bn1);
		
		Assert.assertEquals(0, ar0);
		Assert.assertEquals(127, ar127);
		Assert.assertEquals(128, ar128);
		Assert.assertEquals(128, arn128);
		Assert.assertEquals(255, arn1);
	}
	
	@Test
	public void toHexNoArgTest() {
		
		String actualResult = Utils.toHex();
		
		Assert.assertEquals("", actualResult );
	}
	
	@Test
	public void toHexOneArgTest() {
		
		String actualResult = Utils.toHex(15);
		
		Assert.assertEquals("0F", actualResult );
	}
	
	@Test
	public void toHexMultipleArgTest() {
		
		String actualResult = Utils.toHex(4863);
		
		Assert.assertEquals("12FF", actualResult );
	}
	
	@Test
	public void resolveDateTimeTest() {
		
		int date = Utils.fromHex("858B"); // 05-09-2011
		int time = Utils.fromHex("2E"); // 23:00
			
		Date result = Utils.resolveDateTime(date, time);

		Assert.assertEquals(5, result.getDate());
		Assert.assertEquals(9, result.getMonth());
		Assert.assertEquals(2011, result.getYear());
		
		Assert.assertEquals(23, result.getHours());
		Assert.assertEquals(00, result.getMinutes());
	}
	
	@Test
	  public void getBitsTest() {
	    boolean b1[] = Utils.getBits(0xFF);
	    
	    Assert.assertEquals(b1.length, 8);
	    for (int i = 0; i < 8; i++)
	    {
	      Assert.assertEquals(true, b1[i]);
	    }
	    
	    boolean b2[] = Utils.getBits(0x5A);
	    
	    Assert.assertEquals(b2.length, 8);
	    Assert.assertEquals(false, b2[0]);
	    Assert.assertEquals(true, b2[1]);
	    Assert.assertEquals(false, b2[2]);
	    Assert.assertEquals(true, b2[3]);
	    Assert.assertEquals(true, b2[4]);
	    Assert.assertEquals(false, b2[5]);
	    Assert.assertEquals(true, b2[6]);
	    Assert.assertEquals(false, b2[7]);
	}
	
	@Test
	public void hexStringToByteArrayTest() {
		String s = "000102030AFF";
		
		byte[] result = Utils.hexStringToByteArray(s);
		
		Assert.assertEquals(0, result[0] & 0xFF);
		Assert.assertEquals(1, result[1] & 0xFF);
		Assert.assertEquals(2, result[2] & 0xFF);
		Assert.assertEquals(3, result[3] & 0xFF);
		Assert.assertEquals(10, result[4] & 0xFF);
		Assert.assertEquals(255, result[5] & 0xFF);	
	}
}