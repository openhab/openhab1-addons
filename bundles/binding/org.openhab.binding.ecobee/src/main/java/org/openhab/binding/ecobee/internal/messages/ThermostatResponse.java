/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Retrieves a selection of thermostat data for one or more thermostats. 
 * The type of data retrieved is determined by the {@link Selection} object in the request. 
 * The <code>include*</code> properties of the selection retrieve specific portions 
 * of the thermostat. 
 * 
 * <p>
 * When retrieving thermostats, request only the parts of the thermostat you require as 
 * the whole thermostat with everything can be quite large and generally unnecessary.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/operations/get-thermostats.shtml">GET Thermostats</a>
 * @author John Cocula
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThermostatResponse extends ApiResponse {
	private Page page;
	private List<Thermostat> thermostatList;
	
	/**
	 * @return the page information for the response
	 */
	@JsonProperty("page")
	public Page getPage() {
		return this.page;
	}

	/**
	 * @return the list of thermostats returned by the request
	 */
	@JsonProperty("thermostatList")
	public List<Thermostat> getThermostatList() {
		return this.thermostatList;
	}
	
	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("page", this.page);
		builder.append("thermostatList", this.thermostatList);

		return builder.toString();
	}
}
