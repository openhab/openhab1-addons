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
import org.openhab.action.openwebif.internal.impl.model.adapter.TrimToNullStringAdapter;

/**
 * Object that represents a common result.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "e2simplexmlresult")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleResult {

	@XmlElement(name = "e2state")
	@XmlJavaTypeAdapter(value = BooleanTypeAdapter.class)
	private Boolean state;

	@XmlElement(name = "e2statetext")
	@XmlJavaTypeAdapter(value = TrimToNullStringAdapter.class)
	private String stateText;

	/**
	 * Returns true, if the request is accepted by the receiver.
	 */
	public boolean isValid() {
		return state != null && state == true;
	}

	/**
	 * Returns the message from the receiver.
	 */
	public String getStateText() {
		return stateText;
	}
}
