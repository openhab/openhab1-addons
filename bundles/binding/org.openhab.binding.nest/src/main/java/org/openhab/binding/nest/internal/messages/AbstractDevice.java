/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public AbstractDevice(@JsonProperty("device_id") String device_id) {
		this.device_id = device_id;
	}

	/**
	 * @return the unique thermostat identifier.
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

		return builder.toString();
	}
}
