/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhab.binding.homematic.internal.model.adapter.TrimToNullStringAdapter;

/**
 * Simple class with the JAXB mapping for a TclRega script.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TclScript {

	@XmlAttribute(name = "name", required = true)
	private String name;

	@XmlElement(name = "data", required = true)
	@XmlJavaTypeAdapter(value = TrimToNullStringAdapter.class)
	private String data;

	/**
	 * Returns the name of the script.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the script data.
	 */
	public String getData() {
		return data;
	}

}
