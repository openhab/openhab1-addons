/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import javax.xml.bind.Unmarshaller.Listener;

/**
 * JAXB Listener which sets the parent objects and prepares the values in a
 * HmValueItem.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class CommonUnmarshallerListener extends Listener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterUnmarshal(Object target, Object parent) {
		if (target instanceof HmDatapoint) {
			((HmDatapoint) target).setChannel((HmChannel) parent);
		} else if (target instanceof HmChannel) {
			((HmChannel) target).setDevice((HmDevice) parent);
		}

		if (target instanceof HmValueItem) {
			HmValueItem vi = (HmValueItem) target;
			vi.validate();
		}
	}
}
