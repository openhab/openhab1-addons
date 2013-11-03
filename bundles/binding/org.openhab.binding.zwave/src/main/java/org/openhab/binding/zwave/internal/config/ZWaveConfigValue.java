package org.openhab.binding.zwave.internal.config;

import java.util.List;

/**
 * Implements a storage class for Z-Wave configuration parameters
 * This is used in the configuration database, and a limited subset is used
 * to record the parameters in the node.
 *   
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveConfigValue {
	public String label;
	public Integer size;
	public Integer index;
	public Integer value;
	public String type;
	public String help;
	public List<ZWaveConfigItem> items;
}
