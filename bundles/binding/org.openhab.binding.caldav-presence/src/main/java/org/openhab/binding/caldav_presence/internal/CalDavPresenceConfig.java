package org.openhab.binding.caldav_presence.internal;

import org.openhab.core.binding.BindingConfig;

public class CalDavPresenceConfig implements BindingConfig {
	private String id;

	public CalDavPresenceConfig() {
		super();
	}
	
	public CalDavPresenceConfig(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
