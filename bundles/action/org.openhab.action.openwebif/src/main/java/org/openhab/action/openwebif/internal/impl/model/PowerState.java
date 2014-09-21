/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhab.action.openwebif.internal.impl.model.adapter.BooleanTypeAdapter;

/**
 * Object that represents a powerstate result.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "e2powerstate")
@XmlAccessorType(XmlAccessType.FIELD)
public class PowerState {

	@XmlElement(name = "e2instandby")
	@XmlJavaTypeAdapter(value = BooleanTypeAdapter.class)
	private Boolean standby;

	/**
	 * Returns true, if the receiver is in standby.
	 */
	public boolean isStandby() {
		return standby != null && standby == true;
	}
}
