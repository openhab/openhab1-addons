/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

/**
 * JAXB Adapter to trim a string value to null.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public class TrimToNullStringAdapter extends XmlAdapter<String, Object> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object unmarshal(String value) throws Exception {
		return StringUtils.trimToNull(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

}
