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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.maxcube.internal.Utils;

/**
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class ConfigurationTest {

public final String rawData = "C:003508,0gA1CAEBFP9JRVEwMTA5MTI1KCg9CQcoAzAM/wBESFUIRSBFIEUgRSBFIEUgRSBFIEUgRSBFIERIVQhFIEUgRSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIERIUmxEzFUURSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIA==";
	
	private C_Message c_message = null;
	private Configuration configuration = null;
	
	@Before
	public void Before() {
		c_message = new C_Message(rawData);
		configuration =  Configuration.create(c_message);
	}
	
	@Test
	public void createTest() {
		Assert.assertNotNull(configuration);
	}
	
	@Test
	public void getRfAddressTest() {
		String rfAddress = configuration.getRFAddress();
		
		Assert.assertEquals("003508", rfAddress);
	}
	
	@Test
	public void getDeviceTypeTest() {
		
		DeviceType deviceType = configuration.getDeviceType();
		
		Assert.assertEquals(DeviceType.HeatingThermostat, deviceType);
	}
	
	@Test
	public void getSerialNumberTes() {
		String serialNumber = configuration.getSerialNumber();
		
		Assert.assertEquals("IEQ0109125", serialNumber);
	}
	
	
	
}
