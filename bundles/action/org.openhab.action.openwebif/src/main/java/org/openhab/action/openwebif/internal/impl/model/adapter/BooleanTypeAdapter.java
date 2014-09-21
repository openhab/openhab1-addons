/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.model.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * JAXB Adapter to parse a boolean receiver result value.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class BooleanTypeAdapter extends XmlAdapter<String, Boolean> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean unmarshal(String value) throws Exception {
		return Boolean.parseBoolean(StringUtils.trim(ObjectUtils.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(Boolean value) throws Exception {
		return ObjectUtils.toString(value);
	}

}
