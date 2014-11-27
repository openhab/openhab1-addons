/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple class with the JAXB mapping for a list of common id mappings.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "common-id-mappings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonIdList {

	@XmlElement(name = "common-id-mapping")
	private List<CommonId> commonIds = new ArrayList<CommonId>();

	public List<CommonId> getCommonIds() {
		return commonIds;
	}
}
