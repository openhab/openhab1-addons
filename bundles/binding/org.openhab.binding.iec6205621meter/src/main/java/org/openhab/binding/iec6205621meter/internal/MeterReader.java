package org.openhab.binding.iec6205621meter.internal;

import java.util.Map;

import org.openmuc.j62056.DataSet;

/**
 * 
 * @author Günter Speckhofer
 * 
 */
public interface MeterReader {

	/**
	 * Return the name of meter
	 * 
	 * @return the name of meter
	 */
	public String getName();

	/**
	 * Return the configuration of this meter
	 * 
	 * @return the meter configuration
	 */
	public MeterDeviceConfig getConfig();

	/**
	 * Reads data from meter
	 * 
	 * @return a map of DataSet objects with the obis as key.
	 */
	public Map<String, DataSet> read();

}
