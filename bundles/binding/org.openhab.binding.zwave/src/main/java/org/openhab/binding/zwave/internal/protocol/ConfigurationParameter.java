/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class provides a storage class for zwave configuration parameters
 * within the configuration command class. This is then serialized to XML.
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XStreamAlias("configurationParameter")
public class ConfigurationParameter {

	private final Integer index;
	private final Integer size;
	private Integer value;
	private boolean readOnly;
	private boolean writeOnly;
	
	/***
	 * Constructor. Creates a new instance of the {@link ConfigurationParameter} class.
	 * @param index. The parameter index.
	 * @param value. The parameter value;
	 * @throws IllegalArgumentException thrown when the index or size arguments are out of range.
	 */
	public ConfigurationParameter(Integer index, Integer value, Integer size) throws IllegalArgumentException {
		
		if (size < 1 || size > 4) {
			throw new IllegalArgumentException("illegal parameter size");
		}
		
		if (index < 0 || index > 0xFF) {
			throw new IllegalArgumentException("illegal parameter index");
		}
		
		this.index = index;
		this.size = size;
		this.setValue(value);
	}
	
	/**
	 * Gets the configuration parameter value
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * Sets the configuration parameter value.
	 * @param value the value to set
	 */
	public void setValue(Integer value) throws IllegalArgumentException {
		this.value = value;
	}

	/**
	 * Returns the parameter index.
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Returns the parameter size.
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}
	
	/**
	 * Sets the parameter as a WriteOnly parameter
	 * @param write true if the parameter should not be read
	 */
	public void setWriteOnly(boolean write) {
		writeOnly = write;
	}

	/**
	 * Returns true if this parameter is write only
	 * @return true if the parameter should not be read back
	 */
	public boolean getWriteOnly() {
		return writeOnly;
	}

	/**
	 * Sets the parameter as a ReadOnly parameter
	 * @param read true if the parameters is readonly
	 */
	public void setReadOnly(boolean read) {
		readOnly = read;
	}

	/**
	 * Returns true if this parameter is read only
	 * @return true if the parameter should not be written to
	 */
	public boolean getReadOnly() {
		return readOnly;
	}
}
