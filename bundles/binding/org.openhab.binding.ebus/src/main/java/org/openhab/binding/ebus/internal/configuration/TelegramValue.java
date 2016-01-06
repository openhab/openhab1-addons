/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.configuration;

import java.math.BigDecimal;
import java.util.Map;

import javax.script.CompiledScript;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This class stores the values of an eBus bytes telegram.
 * 
 * @author Christian Sowada
 * @since 1.8.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TelegramValue {

	private Integer bit;
	private CompiledScript csript;
	private String debug;
	private BigDecimal factor;
	private String label;
	private Map<String, String> mapping;
	private BigDecimal max;
	private BigDecimal min;
	private Integer pos;
	private BigDecimal replaceValue;
	private String script;
	private String step;
	private String type;
	private String typeHint;

	/**
	 * Returns a bit of 
	 * @return
	 */
	public Integer getBit() {
		return bit;
	}

	/**
	 * Returns compiled script
	 * @return
	 */
	public CompiledScript getCsript() {
		return csript;
	}

	/**
	 * Get debug string
	 * @return
	 */
	public String getDebug() {
		return debug;
	}

	/**
	 * Returns factor
	 * @return
	 */
	public BigDecimal getFactor() {
		return factor;
	}

	/**
	 * Returns label of value
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns mapping to this value
	 * @return
	 */
	public Map<String, String> getMapping() {
		return mapping;
	}

	/**
	 * Get max value
	 * @return
	 */
	public BigDecimal getMax() {
		return max;
	}

	/**
	 * Get min value
	 * @return
	 */
	public BigDecimal getMin() {
		return min;
	}

	/**
	 * Get telegram position for this value
	 * @return
	 */
	public Integer getPos() {
		return pos;
	}

	/**
	 * Get replace value
	 * @return
	 */
	public BigDecimal getReplaceValue() {
		return replaceValue;
	}

	/**
	 * Returns uncompiled script
	 * @return
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Returns the step wide for this value
	 * @return
	 */
	public String getStep() {
		return step;
	}

	/**
	 * Returns the value eBus type
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns a hint for documentation
	 * @return
	 */
	public String getTypeHint() {
		return typeHint;
	}

	/**
	 * @param bit
	 */
	public void setBit(Integer bit) {
		this.bit = bit;
	}

	/**
	 * @param csript
	 */
	public void setCsript(CompiledScript csript) {
		this.csript = csript;
	}

	/**
	 * @param debug
	 */
	public void setDebug(String debug) {
		this.debug = debug;
	}

	/**
	 * @param factor
	 */
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param mapping
	 */
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}

	/**
	 * @param max
	 */
	public void setMax(BigDecimal max) {
		this.max = max;
	}

	/**
	 * @param min
	 */
	public void setMin(BigDecimal min) {
		this.min = min;
	}

	/**
	 * @param pos
	 */
	public void setPos(Integer pos) {
		this.pos = pos;
	}

	/**
	 * @param replaceValue
	 */
	public void setReplaceValue(BigDecimal replaceValue) {
		this.replaceValue = replaceValue;
	}

	/**
	 * @param script
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @param step
	 */
	public void setStep(String step) {
		this.step = step;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param typeHint
	 */
	@JsonProperty("type_hint")
	public void setTypeHint(String typeHint) {
		this.typeHint = typeHint;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelegramValue [bit=" + bit + ", factor=" + factor + ", label="
				+ label + ", mapping=" + mapping + ", max=" + max + ", min="
				+ min + ", pos=" + pos + ", replaceValue=" + replaceValue
				+ ", script=" + script + ", step=" + step + ", type=" + type
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(bit).append(factor).append(factor).append(label).append(mapping)
		.append(max).append(min).append(pos).append(replaceValue).append(script)
		.append(step).append(type);

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
		TelegramValue other = (TelegramValue) obj;
		if (bit == null) {
			if (other.bit != null)
				return false;
		} else if (!bit.equals(other.bit))
			return false;
		if (debug == null) {
			if (other.debug != null)
				return false;
		} else if (!debug.equals(other.debug))
			return false;
		if (factor == null) {
			if (other.factor != null)
				return false;
		} else if (!factor.equals(other.factor))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (replaceValue == null) {
			if (other.replaceValue != null)
				return false;
		} else if (!replaceValue.equals(other.replaceValue))
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		if (step == null) {
			if (other.step != null)
				return false;
		} else if (!step.equals(other.step))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
