package org.openhab.binding.caldav.internal;

import org.openhab.core.binding.BindingConfig;

public class CalDavConfig implements BindingConfig {
	private String id;

	public CalDavConfig() {
		super();
	}
	
	public CalDavConfig(String id) {
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
