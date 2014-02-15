/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.tel.types;

import java.util.Formatter;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;


/**
 * This type can be used for items that are dealing with telephony functionality.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 * 
 */
public class CallType implements ComplexType, Command, State {
	
	protected static final String DEST_NUM = "destNum";
	protected static final String ORIG_NUM = "origNum";
	private static final String SEPARATOR = "##";
	
	private SortedMap<String, PrimitiveType> callDetails;

	
	public static final State EMPTY = new CallType(new StringType(""), new StringType(""));
	
	
	public CallType() {
		callDetails = new TreeMap<String, PrimitiveType>();
	}
	
	public CallType(String value) {
		this();
		if (StringUtils.isNotBlank(value)) {
			String[] elements = value.split(SEPARATOR);
			if (elements.length == 2) {
				callDetails.put(DEST_NUM, new StringType(elements[0]));
				callDetails.put(ORIG_NUM, new StringType(elements[1]));
			}
		}
	}
	
	public CallType(String origNum, String destNum) {
		this(new StringType(origNum), new StringType(destNum));
	}
		
	public CallType(StringType origNum, StringType destNum) {
		this();
		callDetails.put(DEST_NUM, destNum);
		callDetails.put(ORIG_NUM, origNum);
	}
	
	
	public SortedMap<String, PrimitiveType> getConstituents() {
		return callDetails;
	}
	
	public PrimitiveType getDestNum() {
		return callDetails.get(DEST_NUM);
	}
	
	public PrimitiveType getOrigNum() {
		return callDetails.get(ORIG_NUM);
	}
	
	/**
	 * <p>Formats the value of this type according to a pattern (@see 
	 * {@link Formatter}). One single value of this type can be referenced
	 * by the pattern using an index. The item order is defined by the natural
	 * (alphabetical) order of their keys.</p>
	 * 
	 * <p>Index '1' will reference the call's destination number and index '2'
	 * will reference the call's origination number.</p>
	 * 
	 * @param pattern the pattern to use containing indexes to reference the
	 * single elements of this type.
	 */
	public String format(String pattern) {
		return String.format(pattern, callDetails.values().toArray());
	}
	
	public CallType valueOf(String value) {
		return new CallType(value);
	}
	
	@Override
	public String toString() {
		return getOrigNum() + SEPARATOR + getDestNum();
	}

	
}
