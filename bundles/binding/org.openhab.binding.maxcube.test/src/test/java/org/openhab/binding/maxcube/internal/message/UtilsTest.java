/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;
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
}