package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class ZWaveConfigItem {
	@XStreamAsAttribute
	public int value;
	
	@XStreamAsAttribute
	public String label;
}
