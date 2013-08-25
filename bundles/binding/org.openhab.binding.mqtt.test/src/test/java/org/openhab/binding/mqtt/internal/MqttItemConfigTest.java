/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org >admin@openhab.org<
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
 * along with this program; if not, see >http://www.gnu.org/licenses<.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.mqtt.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttItemConfigTest {

	@Test
	public void canParseInboundConfig() throws BindingConfigParseException {

		MqttItemConfig c = new MqttItemConfig("myItem",
				"<[publicweatherservice:/london-city/temperature:state:default]");
		assertEquals(0, c.getMessagePublishers().size());
		assertEquals(1, c.getMessageSubscribers().size());
	}

	@Test
	public void canParseOutboundConfig() throws BindingConfigParseException {
		MqttItemConfig c = new MqttItemConfig("myItem",
				">[mybroker:/mytopic:command:ON:1]");
		assertEquals(1, c.getMessagePublishers().size());
		assertEquals(0, c.getMessageSubscribers().size());
	}

	@Test
	public void canParseMultipleInboundConfigs()
			throws BindingConfigParseException {
		MqttItemConfig c = new MqttItemConfig(
				"myItem",
				"<[mybroker:/myHome/doorbell:state:XSLT(doorbell.xslt)], <[mybroker:/myHome/doorbell:command:ON], <[mybroker:/myHome/doorbell:state:XSLT(doorbell.xslt)]");
		assertEquals(0, c.getMessagePublishers().size());
		assertEquals(3, c.getMessageSubscribers().size());
	}

	@Test
	public void canParseMultipleOutboundConfigs()
			throws BindingConfigParseException {
		MqttItemConfig c = new MqttItemConfig("myItem",
				">[mybroker:/mytopic:command:ON:1],>[mybroker:/mytopic:command:OFF:0]");
		assertEquals(2, c.getMessagePublishers().size());
		assertEquals(0, c.getMessageSubscribers().size());
	}

	@Test
	public void canParseMultipleConfigs() throws BindingConfigParseException {
		MqttItemConfig c = new MqttItemConfig(
				"myItem",
				">[mybroker:/mytopic:command:ON:1],>[mybroker:/mytopic:command:OFF:0],<[mybroker:/myHome/doorbell:state:XSLT(doorbell.xslt)], <[mybroker:/myHome/doorbell:command:ON], <[mybroker:/myHome/doorbell:state:XSLT(doorbell.xslt)]");
		assertEquals(2, c.getMessagePublishers().size());
		assertEquals(3, c.getMessageSubscribers().size());
	}
}
