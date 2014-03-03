package org.openhab.binding.dmlsmeter.internal;

import java.util.Map;

import org.openmuc.j62056.DataSet;
/**
 * 
 * @author Günter Speckhofer
 *
 */
public interface DmlsMeterReader {

	/**
	 * Return the name of the DmlsMeter 
	 * @returnthe namee of the DMSL Meter
	 */
	public String getName();

	/**
	 * Return the configuration of this meter
	 * @return  the DMLS Meter configuration
	 */
	public DmlsMeterDeviceConfig getConfig();

	/**
	 * Reads from a serial interface a DMLSMeter
	 * 
	 * @return a map of DataSet objects with the obis as key.
	 */
	public Map<String, DataSet> read();

}
