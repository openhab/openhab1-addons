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
 * JAXB Adapter to trim a string value to null.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */

public class TrimToNullStringAdapter extends XmlAdapter<String, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String unmarshal(String value) throws Exception {
		return StringUtils.trimToNull(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(String value) throws Exception {
		return ObjectUtils.toString(value);
	}

}
