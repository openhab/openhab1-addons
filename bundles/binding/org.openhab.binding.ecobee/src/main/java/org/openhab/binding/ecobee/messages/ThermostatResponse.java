/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.ecobee.messages;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Retrieves a selection of thermostat data for one or more thermostats. The type of data retrieved is determined by the
 * {@link Selection} object in the request. The <code>include*</code> properties of the selection retrieve specific
 * portions of the thermostat.
 *
 * <p>
 * When retrieving thermostats, request only the parts of the thermostat you require as the whole thermostat with
 * everything can be quite large and generally unnecessary.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/operations/get-thermostats.shtml">GET
 *      Thermostats</a>
 * @author John Cocula
 * @since 1.7.0
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

    /**
     * Fix up any internal references and create name-based maps for easy traversal.
     */
    protected void sync() {
        if (this.thermostatList != null) {
            for (Thermostat t : this.thermostatList) {
                t.sync();
            }
        }
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
