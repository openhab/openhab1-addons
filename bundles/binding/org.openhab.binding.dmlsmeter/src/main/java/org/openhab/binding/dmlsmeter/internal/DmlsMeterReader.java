package org.openhab.binding.dmlsmeter.internal;

import java.util.Map;

import org.openmuc.j62056.DataSet;

public interface DmlsMeterReader {
	
	/**
	 * Reads from a serial interface a DMLSMeter
	 * @return a map of DataSet objects with the obis as key.
	 */
	public Map<String,DataSet> read();

}
