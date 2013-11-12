package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Implements a storage class for Z-Wave configuration parameters
 * This is used in the configuration database, and a limited subset is used
 * to record the parameters in the node.
 *   
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XStreamAlias("value")
public class ZWaveConfigValue {
	@XStreamAsAttribute
	public String label;
	@XStreamAsAttribute
	public Integer size;
	@XStreamAsAttribute
	public Integer index;
	@XStreamAsAttribute
	public Integer value;
	@XStreamAsAttribute
	public String type;

	@XStreamAsAttribute
	public String genre;
	
	@XStreamAsAttribute
	public int instance;


	public String Help;
	
	@XStreamImplicit
	public List<ZWaveConfigItem> Item;
}
