package org.openhab.binding.zwave.internal.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the configuration record. Used to pass configuration information
 * from a binding in openHAB to a GUI via the REST interface
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
	public String value;
	public Map<String, String> valuelist;
	public Map<String, String> actionlist;

	enum TYPE {
		LIST, BYTE, INT, LONG, STRING, GROUP
	}

	OpenHABConfigurationRecord(String domain, String name, String label, boolean readonly) {
		this.domain = domain + name;
		this.name = name;
		this.label = label;
		this.readonly = readonly;
	}

	/**
	 * Constructor for top level domain
	 * @param domain
	 * @param label
	 */
	OpenHABConfigurationRecord(String domain, String label) {
		this.domain = domain;
		this.label = label;
		this.readonly = true;
	}

	public void addAction(String key, String value) {
		if(actionlist == null)
			actionlist = new HashMap<String, String>();

		actionlist.put(key, value);
	}

	public void addValue(String key, String value) {
		if(valuelist == null)
			valuelist = new HashMap<String, String>();

		valuelist.put(key, value);
	}
	
	
}
