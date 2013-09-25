/**
 * 
 */
package org.openhab.binding.maxcube.internal.message;

import org.junit.Test;

import org.junit.Before;
import junit.framework.Assert;

/**
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class C_MessageTest {

	public final String rawData = "C:003508,0gA1CAEBFP9JRVEwMTA5MTI1KCg9CQcoAzAM/wBESFUIRSBFIEUgRSBFIEUgRSBFIEUgRSBFIERIVQhFIEUgRSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIERIUmxEzFUURSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIA==";
	
	private C_Message message = null;
	
	@Before
	public void Before() {
		message = new C_Message(rawData);
	}
	
	@Test
	public void getMessageTypeTest() {
		
		MessageType messageType = ((Message)message).getType();
		
		Assert.assertEquals(MessageType.C, messageType);
	}
	
	@Test
	public void getRFAddressTest() {
		
		String rfAddress = message.getRFAddress();
		
		Assert.assertEquals("003508", rfAddress);
	}
	
	@Test
	public void getDeviceTypeTest() {
		
		DeviceType deviceType = message.getDeviceType();
		
		Assert.assertEquals(DeviceType.HeatingThermostat, deviceType);
	}
	
	@Test
	public void getSerialNumberTes() {
		String serialNumber = message.getSerialNumber();
		
		Assert.assertEquals("IEQ0109125", serialNumber);
	}
}