package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class ZWaveConfigGroup {
	@XStreamAsAttribute
	int index;

	@XStreamAsAttribute
	int max_associations;

	@XStreamAsAttribute
	String label;

	@XStreamAsAttribute
	boolean auto;
}
