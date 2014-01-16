/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 * This is a java bean that is used with JAXB to serialize items to XML or JSON.
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 * 
 */
@XmlRootElement(name = "item")
public class ItemBean {
	@XmlAnyElement
	public List<JAXBElement> entries = new ArrayList<JAXBElement>();

	public ItemBean() {
	}

	public ItemBean(String name, String value) {
		entries.add(new JAXBElement(new QName(name), String.class, value));
	}
}
