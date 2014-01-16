/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the configuration record. Used to pass configuration information from
 * a binding in openHAB to a GUI via the REST interface
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class OpenHABConfigurationRecord {
	public String domain;
	public String name;
	public String label;
	public String description;
	public boolean optional;
	public boolean readonly;
	public TYPE type;
	public STATE state;
	public String value;
	public Integer minimum;
	public Integer maximum;
	public Map<String, String> valuelist;
	public Map<String, String> actionlist;

	enum TYPE {
		LIST, BYTE, SHORT, LONG, STRING, GROUP
	}
	
	enum STATE {
		OK, WARNING, ERROR, INITIALIZING
	}
	
	/**
	 * Default constructor
	 */
	OpenHABConfigurationRecord() {
	}

	/**
	 * Constructor for a record.
	 * 
	 * @param domain
	 *            Domain in which this record sits. The domain allows the system
	 *            to identify a record in a multitierd array. If the domain
	 *            terminates in a / it is assumed to have children
	 * @param name
	 *            The name is the identifier that the system uses to uniquely
	 *            identify the record within the domain. It is not shown to the
	 *            user.
	 * @param label
	 *            The label is the record name presented to the user
	 * @param readonly
	 *            If set to true, the record will be read only.
	 */
	OpenHABConfigurationRecord(String domain, String name, String label, boolean readonly) {
		this.domain = domain + name;
		this.name = name;
		this.label = label;
		this.readonly = readonly;
	}

	/**
	 * Constructor for top level domain. This constructor is intended for use with top level domains
	 * 
	 * @param domain
	 *            Domain in which this record sits. The domain allows the system
	 *            to identify a record in a multitierd array. If the domain
	 *            terminates in a / it is assumed to have children
	 * @param label
	 *            The label is the record name presented to the user
	 */
	OpenHABConfigurationRecord(String domain, String label) {
		this.domain = domain;
		this.label = label;
		this.readonly = true;
	}

	/**
	 * Adds an action to the record. An action will be displayed in the GUI with
	 * a button that the user can press.
	 * 
	 * @param key
	 * @param value
	 */
	public void addAction(String key, String value) {
		if (actionlist == null)
			actionlist = new HashMap<String, String>();

		actionlist.put(key, value);
	}

	/**
	 * Adds the value to the record
	 * 
	 * @param key
	 * @param value
	 */
	public void addValue(String key, String value) {
		if (valuelist == null)
			valuelist = new HashMap<String, String>();

		valuelist.put(key, value);
	}

	class Action {
		String name;
	}
}
