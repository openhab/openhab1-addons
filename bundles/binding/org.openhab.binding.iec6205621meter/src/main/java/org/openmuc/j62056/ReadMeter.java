/*
 * Copyright 2013-14 Fraunhofer ISE
 *
 * This file is part of j62056.
 * For more information visit http://www.openmuc.org
 *
 * j62056 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * j62056 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with j62056.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.j62056;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ReadMeter {

	private static void printUsage() {
		System.out
				.println("SYNOPSIS\n\torg.openmuc.j62056.ReadMeter [-e] [-d <baud_rate_change_delay>] <serial_port>");
		System.out
				.println("DESCRIPTION\n\tReads the meter connected to the given serial port and prints the received data to stdout. First prints the identification string received from the meter. Then the data sets received are printed. Each data set is printed on a single line with the format: \"<id>;<value>;<unit>\". Errors are printed to stderr.");
		System.out.println("OPTIONS");
		System.out
				.println("\t<serial_port>\n\t    The serial port used for communication. Examples are /dev/ttyS0 (Linux) or COM1 (Windows)\n");
		System.out
				.println("\t-e\n\t    Enable handling of echos caused by some optical tranceivers\n");
		System.out
				.println("\t-d <baud_rate_change_delay>\n\t    Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms.\n");
	}

	public static void main(String[] args) {
		if (args.length < 1 || args.length > 4) {
			printUsage();
			System.exit(1);
		}

		String serialPortName = "";
		boolean echoHandling = false;
		int baudRateChangeDelay = 0;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-e")) {
				echoHandling = true;
			} else if (args[i].equals("-d")) {
				i++;
				if (i == args.length) {
					printUsage();
					System.exit(1);
				}
				try {
					baudRateChangeDelay = Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					printUsage();
					System.exit(1);
				}
			} else {
				serialPortName = args[i];
			}
		}

		Connection connection = new Connection(serialPortName, echoHandling,
				baudRateChangeDelay);

		try {
			connection.open();
		} catch (IOException e) {
			System.err.println("Failed to open serial port: " + e.getMessage());
			System.exit(1);
		}

		List<DataSet> dataSets = null;
		try {
			dataSets = connection.read();
		} catch (IOException e) {
			System.err.println("IOException while trying to read: "
					+ e.getMessage());
			connection.close();
			System.exit(1);
		} catch (TimeoutException e) {
			System.err.print("Read attempt timed out");
			connection.close();
			System.exit(1);
		}

		Iterator<DataSet> dataSetIt = dataSets.iterator();

		// print identification string
		System.out.println(dataSetIt.next().getId());

		// print data sets on the following lines
		while (dataSetIt.hasNext()) {
			DataSet dataSet = dataSetIt.next();
			System.out.println(dataSet.getId() + ";" + dataSet.getValue() + ";"
					+ dataSet.getUnit());
		}

		connection.close();

	}

}
