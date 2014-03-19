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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.binding.mqtt.internal.AbstractMqttMessagePubSub.MessageType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationService;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MqttMessageSubscriberTest {

	@Mock
	private TransformationService transformer;

	private void validateConfig(String configString, String broker,
			String topic, MessageType type, String transformString)
			throws BindingConfigParseException {

		MqttMessageSubscriber subscriber = new MqttMessageSubscriber(configString);

		assertEquals(broker, subscriber.getBroker());
		assertEquals(topic, subscriber.getTopic());
		assertEquals(type, subscriber.getMessageType());
		assertEquals(transformString, subscriber.getTransformationRule());
	}

	private void validateBadConfig(String configString) {
		try {
			new MqttMessageSubscriber(configString);
			fail("Missing exception..");
		} catch (BindingConfigParseException e) {
			return;
		}
	}

	@Test
	public void canParseValidConfigurations() throws BindingConfigParseException {

		try {

			validateConfig("mybroker:/mytopic:command:1", "mybroker",
					"/mytopic", MessageType.COMMAND, "1");

			validateConfig("mybroker1:/a/long/topic/goes/here:command:default",
					"mybroker1", "/a/long/topic/goes/here",
					MessageType.COMMAND, "default");

			validateConfig(
					"mybroker1:/a/long/topic/goes/here:command:dummyfunction",
					"mybroker1", "/a/long/topic/goes/here",
					MessageType.COMMAND, "dummyfunction");

			validateConfig(
					"mybroker1:/wildcard/+/topic/#:command:xslt(myfile.xslt)",
					"mybroker1", "/wildcard/+/topic/#", MessageType.COMMAND,
					"xslt(myfile.xslt)");

			validateConfig(
					"testbroker:/mytopic:state:json(/group/person[2]/value)",
					"testbroker", "/mytopic", MessageType.STATE,
					"json(/group/person[2]/value)");

			validateConfig("mybroker:/mytopic:command:file(/tmp/myfile.txt)",
					"mybroker", "/mytopic", MessageType.COMMAND,
					"file(/tmp/myfile.txt)");

			validateConfig("mybroker:/mytopic:command:file(/tmp/myfile.txt)",
					"mybroker", "/mytopic", MessageType.COMMAND,
					"file(/tmp/myfile.txt)");
			validateConfig(
					"mybroker:/mytopic:command:file(c\\:\\tmp\\myfile.txt)",
					"mybroker", "/mytopic", MessageType.COMMAND,
					"file(c:\\tmp\\myfile.txt)");

		} catch (BindingConfigParseException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void canDetectInvalidConfigurations() {

		validateBadConfig(" :/mytopic:command:ON:1");
		validateBadConfig("mybroker:/mytopic:command:ON:1");
		validateBadConfig("mybroker:/mytopic:command:ON:1:99");
		validateBadConfig("mybroker::/mytopic:command:ON:1");
		validateBadConfig("mybroker:command:ON:1");
		validateBadConfig("mybroker:/mytopic:command:ON:1");
		validateBadConfig("mybroker:/mytopic:command:ON:1");
		validateBadConfig(null);
		validateBadConfig("");
		validateBadConfig("  mybroker : /mytopic : command : * : file(/tmp/myfile.txt)");
		validateBadConfig("mybroker:/mytopic:command:ON:1");
	}

	@Test
	public void canParseCommand() throws Exception {

		MqttMessageSubscriber subscriber = new MqttMessageSubscriber(
				"mybroker:/mytopic:command:default");
		assertEquals(StringType.valueOf("test"), subscriber.getCommand("test"));
		assertEquals(StringType.valueOf("{\"person\"{\"name\":\"me\"}}"),
				subscriber.getCommand("{\"person\"{\"name\":\"me\"}}"));
		assertEquals(StringType.valueOf(""), subscriber.getCommand(""));
		assertEquals(OnOffType.ON, subscriber.getCommand("ON"));
		assertEquals(HSBType.valueOf("5,6,5"), subscriber.getCommand("5,6,5"));
		assertEquals(DecimalType.ZERO,
				subscriber.getCommand(DecimalType.ZERO.toString()));
		assertEquals(PercentType.HUNDRED,
				subscriber.getCommand(PercentType.HUNDRED.toString()));
		assertEquals(PercentType.valueOf("80"),
				subscriber.getCommand(PercentType.valueOf("80").toString()));

	}

	@Test
	public void canParseState() throws Exception {

		MqttMessageSubscriber subscriber = new MqttMessageSubscriber(
				"mybroker:/mytopic:state:default");
		assertEquals(OnOffType.ON, subscriber.getState("ON"));
		assertEquals(StringType.valueOf(""), subscriber.getState(""));
		assertEquals(StringType.valueOf("test"), subscriber.getState("test"));
		assertEquals(StringType.valueOf("{\"person\"{\"name\":\"me\"}}"),
				subscriber.getState("{\"person\"{\"name\":\"me\"}}"));
	}

}
