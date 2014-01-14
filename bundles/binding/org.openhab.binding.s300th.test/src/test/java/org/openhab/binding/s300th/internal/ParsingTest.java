package org.openhab.binding.s300th.internal;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests to ensure parsing of binary messages works as expected
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class ParsingTest {

	private static String S300TH_DATA_1 = "K013282501B";
	private static String S300TH_DATA_2 = "K013282502E";

	private static String S300TH_DATA_3 = "K1136425012";

	private static String[] ALL_S300TH_DATA = { "K013282501B", "K013282502E", "K1136425012", "K0132825022",
			"K0132825028", "K0130425128", "K1185615410", "K0130425128", "K013042512D", "K116961560E", "K013042512C",
			"K013042512A", "K116861580E", "K116721580C", "K0131625330", "K013392512A", "K013492512C", "K013442512D",
			"K116591580E", "K013442512D", "K116291580F" };

	@Test
	public void testS300THParsing() throws Exception {
		double temp = ParseUtils.parseTemperature(S300TH_DATA_1);
		Assert.assertEquals(23.2, temp, 0.01);

		double humidity = ParseUtils.parseS300THHumidity(S300TH_DATA_1);
		Assert.assertEquals(50.8, humidity, 0.01);

		String address = ParseUtils.parseS300THAddress(S300TH_DATA_1);
		Assert.assertEquals("1", address);

		String address2 = ParseUtils.parseS300THAddress(S300TH_DATA_2);
		Assert.assertEquals(address, address2);

		String address3 = ParseUtils.parseS300THAddress(S300TH_DATA_3);
		Assert.assertEquals("2", address3);

		for (String s : ALL_S300TH_DATA) {
			String addr = ParseUtils.parseS300THAddress(s);
			double temperature = ParseUtils.parseTemperature(s);
			// Plausibility checks. not necessary valid
			if (temperature < 10.0 || temperature > 25.0) {
				fail("Temp was " + temp);
			}
			if (!(addr.equals("1") || addr.equals("2"))) {
				fail("Address was " + addr);
			}
		}
	}

}
