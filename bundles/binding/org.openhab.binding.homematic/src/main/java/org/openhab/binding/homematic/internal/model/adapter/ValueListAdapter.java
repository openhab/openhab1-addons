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
 * JAXB Adapter to convert a valuelist from a variable.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class ValueListAdapter extends XmlAdapter<String, String[]> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] unmarshal(String value) throws Exception {
		String[] result = StringUtils.splitByWholeSeparatorPreserveAllTokens(value, ";");
		return (result.length == 0 ? null : result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(String[] values) throws Exception {
		return StringUtils.join(values, ";");
	}

}
