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

/**
 * Maps 'On' and 'Off' string values to a boolean
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class OnOffAdapter extends XmlAdapter<String, Boolean> {

	@Override
	public Boolean unmarshal(String v) throws Exception {
		if (v != null) {
			return v.toLowerCase().equals("on");
		}
		
		return Boolean.FALSE;
	}

	@Override
	public String marshal(Boolean v) throws Exception {
		return v ? "On" : "Off";
	}
}
