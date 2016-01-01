/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.response.statuslistener;

import java.util.Map;
import java.util.Objects;

import org.openhab.binding.ekozefir.exception.UnsupportedByteValueException;
import org.openhab.binding.ekozefir.response.Response;
import org.openhab.binding.ekozefir.response.ResponseListener;
import org.openhab.binding.ekozefir.response.ResponseListenerCreator;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;

/**
 * Creator for status of ahu response listener. Listen for response from ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class StatusResponseListenerCreator implements ResponseListenerCreator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseListener create(final String item, final EventPublisher publisher) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(publisher);
		return new ResponseListener() {
			private final int byteNumberOfResponse = 3;
			private final Map<Integer, StringType> values = ImmutableMap.of(1, new StringType("STARTING"), 2,
					new StringType("WORK"), 3, new StringType("SHUTDOWN"), 4, new StringType("STANDBY"), 5,
					new StringType("ALERT"));

			@Subscribe
			@Override
			public void listen(Response event) {
				Objects.requireNonNull(event);
				int value = event.convertByteOfNumberToInt(byteNumberOfResponse);
				if (!values.containsKey(value)) {
					throw new UnsupportedByteValueException(getClass(), value);
				}
				publisher.postUpdate(item, values.get(value));
			}

		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "status";
	}

	@Override
	public String toString() {
		return Objects.toString("Response listener creator id: " + getId());
	}
}
