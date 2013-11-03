package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveConfigProduct {
	@XStreamAsAttribute
	String id;
	
	@XStreamAsAttribute
	String type;
	
	@XStreamAsAttribute
	String name;
	
	@XStreamAsAttribute
	String config;
}
