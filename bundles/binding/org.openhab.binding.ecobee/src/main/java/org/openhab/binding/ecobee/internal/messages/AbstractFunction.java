/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The function object is defined by its functionType and one or more additional properties. 
 * The property list is variable depending on the type of function. 
 * 
 * <p>
 * Functions are used to perform more complex operations on a thermostat or user which are 
 * too complex with simple property modifications. Functions are used to modify read-only 
 * objects where appropriate.
 * 
 * <p>
 * Each function takes different parameters.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Function.shtml">Function</a>
 * @author John Cocula
 * @author Ecobee
 */
public abstract class AbstractFunction extends AbstractMessagePart {

	// TODO needs to be in specific thermostat's local timezone
	protected final static DateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	protected final static DateFormat hms = new SimpleDateFormat("HH:mm:ss");
	
	private String type;
	private Map<String, Object> params;

	/**
	 * Construct a function of given type with zero params.
	 * 
	 * @param type the function type name
	 */
	protected AbstractFunction( String type ) {
		this.type = type;
	}

	protected final Map<String, Object> makeParams() {
		if (params == null)
			params = new HashMap<String, Object>();
		return params;
	}

	/**
	 * @return the function type name
	 * See the type name in the function documentation
	 */
	@JsonProperty("type")
	public String getType() {
		return this.type;
	}

	/**
	 * @return a map of <code>key=value</code> pairs as the parameters to the function. 
	 * See individual function documentation for the properties.
	 */
	@JsonProperty("params")
	public Map<String, Object> getParams() {
		return this.params;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("type", this.type);
		builder.append("params", this.params);

		return builder.toString();
	}
}
