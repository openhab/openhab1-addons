/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.response.targettemplistener;

import java.util.Objects;

import org.openhab.binding.ekozefir.response.Response;
import org.openhab.binding.ekozefir.response.ResponseListener;
import org.openhab.binding.ekozefir.response.ResponseListenerCreator;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;

import com.google.common.eventbus.Subscribe;

/**
 * Creator for target temperature response listener. Listen for response from
 * ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class TargetTempResponseListenerCreator implements ResponseListenerCreator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseListener create(final String item, final EventPublisher publisher) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(publisher);
		return new ResponseListener() {
			private final int highNumber = 36;
			private final int lowNumber = 35;

			@Override
			@Subscribe
			public void listen(Response event) {
				Objects.requireNonNull(event);
				if (event.isTempBytesOfNumbersOk(highNumber, lowNumber)) {
					publisher.postUpdate(item,
							new DecimalType(event.convertBytesOfNumberToFloat(highNumber, lowNumber)));
				}
			}

		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "target_temp";
	}

	@Override
	public String toString() {
		return Objects.toString("Response listener creator id: " + getId());
	}
}
