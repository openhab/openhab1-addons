/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.ControlMessage;
import org.openhab.binding.ulux.internal.ump.messages.IdListMessage;
import org.openhab.binding.ulux.internal.ump.messages.PageCountMessage;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxMessageParserTest extends AbstractMessageTest {

	@Test
	public void test() {
		StringBuilder data = new StringBuilder();
		data.append("01:86:32:00:06:02:00:00:01:00:23:01:08:00:01:00");
		data.append(":");
		data.append("08:01:00:00:60:00:00:00");
		data.append(":");
		data.append("08:21:00:00:00:00:00:00");
		data.append(":");
		data.append("0c:0f:00:00:03:00:02:00:01:00:a9:02");
		data.append(":");
		data.append("06:0e:00:00:02:00");

		List<UluxMessage> messages = parser.parse(toBuffer(data));

		assertThat(messages.size(), equalTo(4));
		assertThat(messages.get(0), instanceOf(StateMessage.class));
		assertThat(messages.get(1), instanceOf(ControlMessage.class));
		assertThat(messages.get(2), instanceOf(IdListMessage.class));
		assertThat(messages.get(3), instanceOf(PageCountMessage.class));
	}

}
