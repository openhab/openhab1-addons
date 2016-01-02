/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.stiebelheatpump;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.stiebelheatpump.internal.CommunicationService;
import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDate {

	private static String configFile;
	private static int baudRate = 9600;
	private static String serialPortName = "";
	private static final Logger logger = LoggerFactory.getLogger(SetDate.class);

	public SetDate() {
	}

	private static void printUsage() {
		System.out
				.println("SYNOPSIS\n\torg.stiebelheatpump.SetDate [-b <baud_rate>] -c <config_file> -d <serial_port>");
		System.out
				.println("DESCRIPTION\n\tWrite updated time to the heat pump version connected to the given serial port and prints the received data to stdout. "
						+ "Errors are printed to stderr.");
		System.out.println("OPTIONS");
		System.out
				.println("\t-d <serial_port>\n\t    The serial port used for communication. Examples are /dev/ttyS0 (Linux) or COM1 (Windows)\n");
		System.out
				.println("\t-b <baud_rate>\n\t    Baud rate. Default is 9600.\n");
		System.out
				.println("\t-c <config_file>\n\t    Configuration file containing the request definitions for heatpump version.\n");
	}

	public static void main(String args[]) {
		if (args.length < 1 || args.length > 5) {
			printUsage();
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-b")) {
				if (++i == args.length) {
					printUsage();
					System.exit(1);
				}
				try {
					baudRate = Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					printUsage();
					System.exit(1);
				}
			} else if (args[i].equals("-c")) {
				if (++i == args.length) {
					printUsage();
					System.exit(1);
				}
				configFile = args[i];
			} else if (args[i].equals("-d")) {
				if (++i == args.length) {
					printUsage();
					System.exit(1);
				}
				serialPortName = args[i];
			}
		}
		
//		try {
//			CommunicationService communicationService = new CommunicationService(
//					serialPortName, baudRate);
//			Map<String, String> data = new HashMap<String, String>();
//
//			communicationService.getHeatPumpConfiguration(configFile);
//
//			String version = communicationService.getversion();
//			logger.info("Heat pump has version {}", version);
//
//			data = communicationService.setTime();
//			for (Map.Entry<String, String> entry : data.entrySet()) {
//				logger.info("Data {} has value {}", entry.getKey(),
//						entry.getValue());
//			}
//
//			communicationService.finalizer();
//		} catch (StiebelHeatPumpException e) {
//			logger.error("Error : {}", e.toString());
//		}
		
		System.exit(0);
	}
}
