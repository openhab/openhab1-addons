/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model.common.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * JAXB Adapter to convert between a string and a ProviderName object.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ProviderNameAdapter extends XmlAdapter<String, ProviderName> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(ProviderName providerName) throws Exception {
		return providerName == null ? null : providerName.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProviderName unmarshal(String providerName) throws Exception {
		return ProviderName.parse(providerName);
	}
}
