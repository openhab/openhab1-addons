/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openhab.binding.homematic.internal.model.adapter.TypeGuessAdapter;
import org.openhab.binding.homematic.internal.model.adapter.ValueListAdapter;

/**
 * Object that holds the metadata and values for a datapoint or a variable.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HmValueItem {

	@XmlAttribute(name = "name", required = true)
	protected String name;

	@XmlAttribute(name = "value")
	@XmlJavaTypeAdapter(value = TypeGuessAdapter.class)
	protected Object value;

	@XmlAttribute(name = "valueType", required = true)
	protected Integer valueType;

	@XmlAttribute(name = "subType")
	protected Integer subType = -1;

	@XmlAttribute(name = "min")
	@XmlJavaTypeAdapter(value = TypeGuessAdapter.class)
	protected Number minValue;

	@XmlAttribute(name = "max")
	@XmlJavaTypeAdapter(value = TypeGuessAdapter.class)
	protected Number maxValue;

	@XmlAttribute(name = "valueList")
	@XmlJavaTypeAdapter(value = ValueListAdapter.class)
	protected String[] valueList = null;

	@XmlAttribute(name = "writeable")
	protected boolean writeable;

	/**
	 * Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the valueList.
	 */
	public String[] getValueList() {
		return valueList;
	}

	/**
	 * Returns the index of the value in a valueList variable.
	 */
	public int getValueListIndex(String valueListValue) {
		if (valueList != null) {
			for (int i = 0; i < valueList.length; i++) {
				String value = valueList[i];
				if (valueListValue.equalsIgnoreCase(value)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the value of a valueList variable.
	 */
	public String getValueListValue() {
		if (valueList != null) {
			int idx = 0;
			if (isBooleanValue()) {
				idx = Boolean.TRUE == value ? 1 : 0;
			} else {
				idx = Integer.parseInt(value.toString());
			}
			if (idx < valueList.length) {
				return valueList[idx];
			}
		}
		return null;
	}

	/**
	 * Sets the value and validates it.
	 */
	public void setValue(Object value) {
		this.value = value;
		validate();
	}

	/**
	 * Returns the max value used for percentage calculation.
	 */
	public Number getMaxValue() {
		return maxValue;
	}

	/**
	 * Returns the min value used for percentage calculation.
	 */
	public Number getMinValue() {
		return minValue;
	}

	/**
	 * Returns true if this valueItem is a valueList variable.
	 */
	public boolean hasValueList() {
		return valueList != null;
	}

	/**
	 * Returns true if a new value can be set in this valueItem.
	 */
	public boolean isWriteable() {
		return writeable;
	}

	/**
	 * True if the valueItem is writeable.
	 */
	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
	}

	/**
	 * Validates the values after unmarshaling or setting a new value.
	 */
	protected void validate() {
		if (minValue == null) {
			if (isDoubleValueType()) {
				minValue = 0.0d;
			} else {
				minValue = 0;
			}
		}

		if (maxValue == null) {
			if (isBooleanValueType() || isIntegerValueType()) {
				maxValue = 1;
			} else if (isDoubleValueType()) {
				maxValue = 1.0d;
			} else {
				maxValue = 0;
			}
		}

		if (value == null) {
			if (isBooleanValueType()) {
				value = Boolean.FALSE;
			} else if (isDoubleValueType()) {
				value = 0.0d;
			} else if (isIntegerValueType()) {
				value = 0;
			} else {
				value = "";
			}
		}

		if (isStringValueType() && value.getClass() != String.class) {
			value = value.toString();
		}
	}

	/**
	 * Returns the valueType.
	 */
	public Integer getValueType() {
		return valueType;
	}

	/**
	 * Sets the valueType.
	 */
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	/**
	 * Checks if the value is from type boolean.
	 */
	public boolean isBooleanValue() {
		return value.getClass() == Boolean.class;
	}

	/**
	 * Checks if the value is from type integer.
	 */
	public boolean isIntegerValue() {
		return value.getClass() == Integer.class;
	}

	/**
	 * Checks if the value is from type double.
	 */
	public boolean isDoubleValue() {
		return value.getClass() == Double.class;
	}

	/**
	 * Checks if the value is a number.
	 */
	public boolean isNumberValue() {
		return isIntegerValue() || isDoubleValue();
	}

	/**
	 * Checks if the value is from type string.
	 */
	public boolean isStringValue() {
		return value.getClass() == String.class;
	}

	/**
	 * Checks if the Homematic valuetype is a boolean.
	 */
	private boolean isBooleanValueType() {
		return valueType == 2 || (valueType == 16 && (subType == 2 || subType == 6));
	}

	/**
	 * Checks if the Homematic valuetype is a double.
	 */
	private boolean isDoubleValueType() {
		return valueType == 4 || valueType == 6;
	}

	/**
	 * Checks if the Homematic valuetype is a integer.
	 */
	private boolean isIntegerValueType() {
		return valueType == 8 || (valueType == 16 && (subType == 29 || subType == 27 || subType == 0));
	}

	/**
	 * Checks if the Homematic valuetype is a number.
	 */
	public boolean isNumberValueType() {
		return isIntegerValueType() || isDoubleValueType();
	}

	/**
	 * Checks if the Homematic valuetype is from type string.
	 */
	private boolean isStringValueType() {
		return valueType == 20;
	}
}
