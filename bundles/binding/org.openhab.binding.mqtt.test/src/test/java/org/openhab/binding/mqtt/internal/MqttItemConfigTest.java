/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
