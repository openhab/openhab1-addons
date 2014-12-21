/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

public class ParameterDefaults {
	private String mInTransform;
	private String mOutTransform;
	private String mCommandTransform;
	private String mFormatted;

	private ParameterDefaults(String formatted, String in, String out,
			String command) {
		mFormatted = formatted;
		mInTransform = in;
		mOutTransform = out;
		mCommandTransform = command;
	}

	public static ParameterDefaults parse(String value) {
		String in = null;
		String out = null;
		String command = null;

		for (String v : value.split(",")) {
			if (v.startsWith("in:")) {
				in = v.substring(3);
			} else if (v.startsWith("out:")) {
				out = v.substring(4);
			} else if (v.startsWith("command:")) {
				command = v.substring(8);
			} else {
				System.out.println("parseEntry: Unknown Key found, key=" + v);
			}
		}

		if ((in == null) && (out == null) && (command == null)) {
			return null;
		} else {
			return new ParameterDefaults(value, in, out, command);
		}
	}

	public String getInTransform() {
		return mInTransform;
	}

	public String getOutTransform() {
		return mOutTransform;
	}

	public String getCommandTransform() {
		return mCommandTransform;
	}

	public String toString() {
		return mFormatted;
	}
}
