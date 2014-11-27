/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * Holds a provider configuration from the openhab.cfg.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */

public class ProviderConfig {
	private ProviderName providerName;
	private String apiKey;
	private String apiKey2;

	/**
	 * Returns the apikey.
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the apikey.
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Returns the apikey2.
	 */
	public String getApiKey2() {
		return apiKey2;
	}

	/**
	 * Sets the apikey2.
	 */
	public void setApiKey2(String apiKey2) {
		this.apiKey2 = apiKey2;
	}

	/**
	 * Returns the providerName.
	 */
	public ProviderName getProviderName() {
		return providerName;
	}

	/**
	 * Sets the providerName.
	 */
	public void setProviderName(ProviderName providerName) {
		this.providerName = providerName;
	}

	/**
	 * Returns true, if this config is valid.
	 */
	public boolean isValid() {
		return providerName != null
				&& apiKey != null
				&& (providerName != ProviderName.HAMWEATHER || (providerName == ProviderName.HAMWEATHER && apiKey2 != null));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("providerName",
				providerName).append("apiKey", apiKey);
		if (apiKey2 != null) {
			tsb.append("apiKey2", apiKey2);
		}
		return tsb.toString();
	}

}
