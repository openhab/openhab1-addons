/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

/**
 * Adapter to clean up string values  
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class StringAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {
		if (v != null) {
			v = StringUtils.trimToEmpty(v);
		}
		
		return v;
	}

	@Override
	public String marshal(String v) throws Exception {
		return v;
	}
}
