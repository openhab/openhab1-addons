/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.adapters;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Maps Denon volume values in db to percentage   
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class VolumeAdapter extends XmlAdapter<String, BigDecimal> {
	
	private static final BigDecimal OFFSET = new BigDecimal("80");

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		if (v != null && !v.trim().equals("--"))  {
				return new BigDecimal(v).add(OFFSET);
		}
		
		return BigDecimal.ZERO;
	}

	@Override
	public String marshal(BigDecimal v) throws Exception {
		if (v.equals(BigDecimal.ZERO)) {
			return "--";
		}
		
		return v.subtract(OFFSET).toString();
	}
}
