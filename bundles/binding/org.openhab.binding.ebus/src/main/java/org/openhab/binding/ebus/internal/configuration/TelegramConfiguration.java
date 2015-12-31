/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.configuration;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This class stores the json configuration files for an eBus bytes telegram.
 * 
 * @author Christian Sowada
 * @since 1.8.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TelegramConfiguration {

	private Pattern cfilter;
	private String clazz;
	private String command;
	private String comment;
	private Map<String, TelegramValue> computedValues;
	private String data;
	private Integer debug;
	private String device;
	private String dst;
	private String filter;

	private String id;
	private Map<String, TelegramValue> values;

	/**
	 * The class of the eBus telegram
	 * @return
	 */
	@JsonProperty("class")
	public String getClazz() {
		return clazz;
	}

	/**
	 * The command bytes of the eBus telegram
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * The comment of the eBus telegram
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * The computed values of the eBus telegram (optional)
	 * @return
	 */
	public Map<String, TelegramValue> getComputedValues() {
		return computedValues;
	}

	/**
	 * The data bytes of the eBus telegram
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * A debug flag for this telegram
	 * @return
	 */
	public Integer getDebug() {
		return debug;
	}

	/**
	 * The device that should work with this telegram
	 * @return
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * The destination byte of the eBus telegram
	 * @return
	 */
	public String getDst() {
		return dst;
	}

	/**
	 * The filter string (regex)
	 * @return
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * The compiled filter pattern
	 * @return
	 */
	public Pattern getFilterPattern() {
		return cfilter;
	}

	/**
	 * The ID of the eBus telegram
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * A list of values of the eBus telegram
	 * @return
	 */
	public Map<String, TelegramValue> getValues() {
		return values;
	}

	/**
	 * @param clazz
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param computedValues
	 */
	@JsonProperty("computed_values")
	public void setComputedValues(Map<String, TelegramValue> computedValues) {
		this.computedValues = computedValues;
	}

	/**
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @param debug
	 */
	public void setDebug(Integer debug) {
		this.debug = debug;
	}

	/**
	 * @param device
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @param dst
	 */
	public void setDst(String dst) {
		this.dst = dst;
	}

	/**
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param cfilter
	 */
	public void setFilterPattern(Pattern cfilter) {
		this.cfilter = cfilter;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param values
	 */
	public void setValues(Map<String, TelegramValue> values) {
		this.values = values;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelegramConfiguration [clazz=" + clazz + ", command=" + command
				+ ", comment=" + comment + ", computedValues=" + computedValues
				+ ", data=" + data + ", debug=" + debug + ", device=" + device
				+ ", dst=" + dst + ", filter=" + filter + ", id=" + id
				+ ", values=" + values + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(clazz).append(command).append(comment).append(computedValues).append(data)
		.append(debug).append(device).append(dst).append(filter).append(id).append(values);

		return hash.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TelegramConfiguration other = (TelegramConfiguration) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (computedValues == null) {
			if (other.computedValues != null)
				return false;
		} else if (!computedValues.equals(other.computedValues))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (debug == null) {
			if (other.debug != null)
				return false;
		} else if (!debug.equals(other.debug))
			return false;
		if (device == null) {
			if (other.device != null)
				return false;
		} else if (!device.equals(other.device))
			return false;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		if (filter == null) {
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
}
