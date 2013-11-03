package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveConfigManufacturer {
	@XStreamAsAttribute
	String id;
	
	@XStreamAsAttribute
	String name;
	
	@XStreamImplicit
	List<ZWaveConfigProduct> Product;
}
