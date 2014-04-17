/**
 * 
 */
package org.openhab.binding.iec6205621meter.internal;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.openmuc.j62056.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulates a DmlsMeter reading to be used in case a missing real device.
 * 
 * @author Günter Speckhofer
 * @version 0.0.1
 * 
 */
public class SimulateIec6205621MeterReader implements MeterReader {

	private static final Logger logger = LoggerFactory
			.getLogger(SimulateIec6205621MeterReader.class);

	// @formatter:off
	private static final String[] simulatedValues = { "F.F;00000000;",
			"0.0;  208578;", "1.8.1;0025420;kWh", "1.8.1*01;0025319",
			"1.8.1*12;0025389", "1.8.2;0037033;kWh", "1.8.2*01;0036822;",
			"1.8.0;0062453;kWh", "C.2.1;07-06-30 02:01;" };
	// @formatter:on

	private static int increment = 0;

	private final String name;

	private final MeterDeviceConfig config;

	/**
	 */
	public SimulateIec6205621MeterReader(String name, MeterDeviceConfig config) {
		this.name = name;
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.iec6205621meter.internal.MeterReader#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.iec6205621meter.internal.MeterReader#getConfig()
	 */
	@Override
	public MeterDeviceConfig getConfig() {
		return config;
	}

	/**
	 * simulates the reading of the meter.
	 * 
	 * @return a map of DataSet objects with the obis as key.
	 */
	public Map<String, DataSet> read() {
		Map<String, DataSet> dataSets = new HashMap<String, DataSet>();

		for (String row : simulatedValues) {
			try {
				DataSet dataSet = newInstance(parse(row));
				dataSets.put(dataSet.getId(), dataSet);
			} catch (Exception e) {
				logger.error("Failed create DataSet for row " + row, e);
			}
		}
		return dataSets;
	}

	/**
	 * Parses a single row of readings to be used as input for a DataSet
	 * 
	 * @param row
	 *            to be parsed
	 * @return a StringArray of length 3 contain the OBIS, the value and the
	 *         unit. If no unit is found in the row an empty string is added
	 */
	private String[] parse(String row) {
		String[] parsedRow = new String[3];
		StringTokenizer tokenizer = new StringTokenizer(row, ";");
		for (int i = 0; i < parsedRow.length; i++) {
			if (tokenizer.hasMoreElements()) {
				parsedRow[i] = tokenizer.nextToken();
			} else {
				parsedRow[i] = "";
			}
		}
		if (parsedRow[0].startsWith("1.8")) {
			parsedRow[1] = Double.toString(Double.parseDouble(parsedRow[1])
					+ increment++);
		}
		return parsedRow;

	}

	/**
	 * Helper method to create a new DataSet instance. Unfortunately the DataSet
	 * constructor is private. Therefore we use some reflection tricks.
	 * 
	 * @param parsedRow
	 * @return
	 * @throws Exception
	 */
	private DataSet newInstance(String[] parsedRow) throws Exception {
		Constructor<DataSet> constructor = DataSet.class
				.getDeclaredConstructor(String.class, String.class,
						String.class);
		constructor.setAccessible(true);
		return constructor
				.newInstance(parsedRow[0], parsedRow[1], parsedRow[2]);
	}

}
