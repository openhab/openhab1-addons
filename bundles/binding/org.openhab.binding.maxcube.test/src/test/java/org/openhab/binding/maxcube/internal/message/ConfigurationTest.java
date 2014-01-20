/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
