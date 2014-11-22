/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.common.adapter.ProviderNameAdapter;
import org.openhab.binding.weather.internal.model.common.adapter.ValueListAdapter;

/**
 * Simple class with the JAXB mapping for a provider id configuration.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@XmlRootElement(name = "provider")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonIdProvider {

	@XmlAttribute(name = "name", required = true)
	@XmlJavaTypeAdapter(value = ProviderNameAdapter.class)
	private ProviderName name;

	@XmlAttribute(name = "ids")
	@XmlJavaTypeAdapter(value = ValueListAdapter.class)
	private String[] ids;

	@XmlAttribute(name = "icons")
	@XmlJavaTypeAdapter(value = ValueListAdapter.class)
	private String[] icons;

	/**
	 * Returns the ProviderName.
	 */
	public ProviderName getName() {
		return name;
	}

	/**
	 * Returns the mapped ids.
	 */
	public String[] getIds() {
		return ids;
	}

	/**
	 * Returns the mapped icons.
	 */
	public String[] getIcons() {
		return icons;
	}
}
