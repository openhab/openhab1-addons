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
package org.openhab.binding.nest.internal.messages;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The AbstractDevice Java Bean represents an abstract Nest device.
 *
 * @see <a href="https://developer.nest.com/documentation/api-reference">API Reference</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDevice extends AbstractMessagePart implements DataModelElement {

    private String device_id;
    private String locale;
    private String software_version;
    private String structure_id;
    private Structure structure;
    private String name;
    private String name_long;
    private Date last_connection;
    private Boolean is_online;
    private String where_id;
    private String where_name;

    public AbstractDevice(@JsonProperty("device_id") String device_id) {
        this.device_id = device_id;
    }

    /**
     * @return the unique device identifier.
     */
    @JsonProperty("device_id")
    public String getDevice_id() {
        return this.device_id;
    }

    /**
     * @return Country and language preference, in IETF Language Tag format
     */
    @JsonProperty("locale")
    public String getLocale() {
        return this.locale;
    }

    /**
     * @return Software version
     */
    @JsonProperty("software_version")
    public String getSoftware_version() {
        return this.software_version;
    }

    /**
     * @return Unique structure identifier
     */
    @JsonProperty("structure_id")
    public String getStructure_id() {
        return this.structure_id;
    }

    /**
     * @return the structure
     */
    public Structure getStructure() {
        return this.structure;
    }

    /**
     * @return Display name of the device
     */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
     * @return Long display name of the device
     */
    @JsonProperty("name_long")
    public String getName_long() {
        return this.name_long;
    }

    /**
     * @return Time of the last successful interaction with the Nest service
     */
    @JsonProperty("last_connection")
    public Date getLast_connection() {
        return this.last_connection;
    }

    /**
     * @return Device connection status with the Nest Service
     */
    @JsonProperty("is_online")
    public Boolean getIs_online() {
        return this.is_online;
    }

    /**
     * @return Where unique identifier.
     */
    @JsonProperty("where_id")
    public String getWhere_id() {
        return this.where_id;
    }

    /**
     * @return The display name of the device. Associated with the where_id.
     *         Can be any room name from a list we provide, or a custom name.
     */
    @JsonProperty("where_name")
    public String getWhere_name() {
        return this.where_name;
    }

    @Override
    public void sync(DataModel dataModel) {
        // Link to structure
        this.structure = dataModel.getStructures_by_id().get(this.structure_id);
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = createToStringBuilder();
        builder.appendSuper(super.toString());
        builder.append("device_id", this.device_id);
        builder.append("name", this.name);
        builder.append("locale", this.locale);
        builder.append("software_version", this.software_version);
        builder.append("structure_id", this.structure_id);
        builder.append("name", this.name);
        builder.append("name_long", this.name_long);
        builder.append("last_connection", this.last_connection);
        builder.append("is_online", this.is_online);
        builder.append("where_id", this.where_id);
        builder.append("where_name", this.where_name);

        return builder.toString();
    }
}
