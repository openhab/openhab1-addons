package org.openhab.binding.zwave.internal.config;

import java.util.Map;

public class OpenHABConfigurationRecord {
	String name;
	String label;
	String description;
	boolean optional;
	boolean readonly;
	TYPE type;
	String value;
	Map<String, String> valuelist;
	
	enum TYPE {
		LIST, BYTE, INT, LONG, STRING, GROUP
	}
	
	OpenHABConfigurationRecord(String name, String label, boolean readonly) {
		this.name = name;
		this.label = label;
		this.readonly = readonly;
	}
}
