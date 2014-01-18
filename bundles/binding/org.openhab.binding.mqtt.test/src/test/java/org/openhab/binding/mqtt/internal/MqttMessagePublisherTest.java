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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.binding.mqtt.internal.AbstractMqttMessagePubSub.MessageType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.transport.mqtt.MqttSenderChannel;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MqttMessagePublisherTest {

	@Mock
	private TransformationService transformer;

	private void validateConfig(String configString, String broker,
			String topic, MessageType type, String trigger,
			String transformString) throws BindingConfigParseException {

		MqttMessagePublisher publisher = new MqttMessagePublisher(configString);
		if (publisher.getTransformationServiceName() != null) {
			publisher.setTransformationService(transformer);
		}

		assertEquals(broker, publisher.getBroker());
		assertEquals(topic, publisher.getTopic());
		assertEquals(type, publisher.getMessageType());
		assertEquals(trigger, publisher.getTrigger());
		assertEquals(transformString, publisher.getTransformationRule());

	}

	private void validateBadConfig(String configString) {
		try {
			new MqttMessagePublisher(configString);
			fail("Missing exception..");
		} catch (BindingConfigParseException e) {
			return;
		}
	}

	@Test
	public void canParseValidConfigurations()
			throws BindingConfigParseException {

		validateConfig("mybroker:/mytopic:command:ON:1", "mybroker",
				"/mytopic", MessageType.COMMAND, "ON", "1");

		validateConfig("mybroker1:/a/long/topic/goes/here:command:ON:default",
				"mybroker1", "/a/long/topic/goes/here", MessageType.COMMAND,
				"ON", "default");

		validateConfig(
				"mybroker1:/wildcard/not/topic:command:ON:xslt(myfile.xslt)",
				"mybroker1", "/wildcard/not/topic", MessageType.COMMAND, "ON",
				"xslt(myfile.xslt)");

		validateConfig("mybroker:/mytopic:command:*:file(/tmp/myfile.txt)",
				"mybroker", "/mytopic", MessageType.COMMAND, "*",
				"file(/tmp/myfile.txt)");

		validateConfig("mybroker:/mytopic:command:*:file(/tmp/myfile.txt)",
				"mybroker", "/mytopic", MessageType.COMMAND, "*",
				"file(/tmp/myfile.txt)");

		String jsonMessage = "{\"message\"\\: \"command\",\"origin\"\\: \"openhab\",\"data\"\\: {\"lamp1\"\\: \"ON\"}} ";
		String jsonMessageNoEsc = "{\"message\": \"command\",\"origin\": \"openhab\",\"data\": {\"lamp1\": \"ON\"}}";
		validateConfig("testbroker:/mytopic:state:OFF:" + jsonMessage + "",
				"testbroker", "/mytopic", MessageType.STATE, "OFF",
				jsonMessageNoEsc);

	}

	@Test
	public void canDetectInvalidConfigurations() {
		validateBadConfig(":/mytopic:command:ON:1");
		validateBadConfig("mybroker::command:ON:1:99");
		validateBadConfig("mybroker:/mytopic:command:ON:");
		validateBadConfig("mybroker:command:ON:1");
		validateBadConfig("mybroker:/mytopic:command2:ON:1");
		validateBadConfig("mybroker:/test:/mytopic:command:ON:1");
		validateBadConfig(null);
		validateBadConfig("");
		validateBadConfig(" mybroker ; /mytopic : command : * : file(/tmp/myfile.txt)");

	}

	private void testStateSupport(String configString, State state,
			boolean expectedStateSupport) throws BindingConfigParseException {
		MqttMessagePublisher publisher = new MqttMessagePublisher(configString);
		assertEquals(expectedStateSupport, publisher.supportsState(state));
	}

	private void testCommandSupport(String configString, Command command,
			boolean expectedStateSupport) throws BindingConfigParseException {
		MqttMessagePublisher publisher = new MqttMessagePublisher(configString);
		assertEquals(expectedStateSupport, publisher.supportsCommand(command));
	}

	@Test
	public void canDetectSupportForStates() throws BindingConfigParseException {
		testStateSupport("broker:/topic:state:OFF:0", OnOffType.OFF, true);
		testStateSupport("broker:/topic:state:OFF:0", OnOffType.ON, false);
		testStateSupport("broker:/topic:state:*:0", OnOffType.OFF, true);
		testStateSupport("broker:/topic:state:off:0", OnOffType.OFF, true);
		testStateSupport("broker:/topic:stAte:100:0",
				DecimalType.valueOf("100"), true);
		testStateSupport("broker:/topic:state:100:0",
				DecimalType.valueOf("99"), false);
		testStateSupport("broker:/topic:state:20:0", PercentType.HUNDRED, false);
		testStateSupport("broker:/topic:state:100:0", PercentType.HUNDRED, true);
		testStateSupport("broker:/topic:state:100:0", PercentType.ZERO, false);
		testStateSupport("broker:/topic:state:0:0", PercentType.ZERO, true);
		testStateSupport("broker:/topic:state:CLOSED:0", OpenClosedType.CLOSED,
				true);
		testStateSupport("broker:/topic:state:OPEN:0", OpenClosedType.CLOSED,
				false);
		testStateSupport("broker:/topic:command:*:0", OpenClosedType.CLOSED,
				false);
		testStateSupport("broker:/topic:command:CLOSED:0",
				OpenClosedType.CLOSED, false);
		testStateSupport("broker:/topic:state:240.0,100.0,100.0:0",
				HSBType.BLUE, true);
	}

	@Test
	public void canDetectSupportForCommands()
			throws BindingConfigParseException {
		testCommandSupport("broker:/topic:command:OFF:0", OnOffType.OFF, true);
		testCommandSupport("broker:/topic:command:OFF:0", OnOffType.ON, false);
		testCommandSupport("broker:/topic:command:*:0", OnOffType.OFF, true);
		testCommandSupport("broker:/topic:command:off:0", OnOffType.OFF, true);
		testCommandSupport("broker:/topic:commAnd:100:0",
				DecimalType.valueOf("100"), true);
		testCommandSupport("broker:/topic:command:100:0",
				DecimalType.valueOf("99"), false);
		testCommandSupport("broker:/topic:command:20:0", PercentType.HUNDRED,
				false);
		testCommandSupport("broker:/topic:command:100:0", PercentType.HUNDRED,
				true);
		testCommandSupport("broker:/topic:command:100:0", PercentType.ZERO,
				false);
		testCommandSupport("broker:/topic:command:5:0", PercentType.ZERO, false);
		testCommandSupport("broker:/topic:command:CLOSED:0",
				OpenClosedType.CLOSED, true);
		testCommandSupport("broker:/topic:command:OPEN:0",
				OpenClosedType.CLOSED, false);
		testCommandSupport("broker:/topic:state:*:0", OpenClosedType.CLOSED,
				false);
		testCommandSupport("broker:/topic:state:CLOSED:0",
				OpenClosedType.CLOSED, false);
		System.out.println(HSBType.BLUE);
		testCommandSupport("broker:/topic:command:240.0,100.0,100.0:0",
				HSBType.BLUE, true);
	}

	@Test
	public void canCreateMessageFromCommandOrStateWithStaticTransformation()
			throws Exception {

		assertEquals("test", getPublishedMessage("broker:/topic:command:*:test", "dummy"));
		assertEquals("test", getPublishedMessage("broker:/topic:state:*:test", "dummy"));

		assertEquals("eisen",
			getPublishedMessage("broker:/topic:state:*:eisen", OnOffType.ON.toString()));
		assertEquals("funk",
			getPublishedMessage("broker:/topic:state:*:funk", OnOffType.ON.toString()));

		assertEquals(
				"{\"person\"{\"name\":\"me\"}}",
				getPublishedMessage(
						"broker:/topic:state:*:{\"person\"{\"name\"\\:\"me\"}}",
						OnOffType.ON.toString()));
		assertEquals(
				"ON",
				getPublishedMessage("broker:/topic:command:*:default",
						OnOffType.ON.toString()));
		assertEquals(
				"OFF",
				getPublishedMessage("broker:/topic:command:*:default",
						OnOffType.OFF.toString()));
		assertEquals(
				"UP",
				getPublishedMessage("broker:/topic:command:*:default",
						UpDownType.UP.toString()));
		assertEquals(
				"100",
				getPublishedMessage("broker:/topic:command:*:default",
						PercentType.HUNDRED.toString()));
		assertEquals(
				"80,81,82",
				getPublishedMessage("broker:/topic:command:*:default", HSBType
						.valueOf("80,81,82").toString()));
		assertEquals(
				"0",
				getPublishedMessage("broker:/topic:command:*:default",
						DecimalType.ZERO.toString()));
		assertEquals(
				"ahah",
				getPublishedMessage("broker:/topic:command:*:default",
						StringType.valueOf("ahah").toString()));
	}

	private String getPublishedMessage(String configString, String message) throws BindingConfigParseException {

		final List<String> sentMessage = new ArrayList<String>();
		MqttMessagePublisher publisher = new MqttMessagePublisher(configString);

		publisher.setSenderChannel(new MqttSenderChannel() {
			@Override
			public void publish(String topic, byte[] message) {
				sentMessage.add(new String(message));
			}
		});
		publisher.publish(publisher.getTopic(), message.getBytes());
		return sentMessage.get(0);
	}
	
}
