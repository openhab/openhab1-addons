/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhab.binding.ebus.internal.EBusTelegram;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple CSV writer to analyse eBUS telegrams with a external program
 * 
 * @author Christian Sowada
 * @since 1.7.1
 */
public class EBusTelegramCSVWriter {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusTelegramCSVWriter.class);
	
	private BufferedWriter writer;
	private FileWriter fw;

	public final static String CSV_FOLDER = getUserPersistenceDataFolder() + File.separator + "ebus";

	/**
	 * returns the path for user data
	 * @return
	 */
	static private String getUserPersistenceDataFolder() {
		String progArg = System.getProperty("smarthome.userdata");
		if (progArg != null) {
			return progArg + File.separator + "persistence";
		} else {
			return "etc";
		}
	}

	/**
	 * open a csv file user data
	 * @param filename
	 * @throws IOException
	 */
	public void openInUserData(String filename) throws IOException {

		File folder = new File(EBusTelegramCSVWriter.CSV_FOLDER);
		if(!folder.exists()) {
			folder.mkdirs();
		}

		open(new File(folder, filename));
	}

	/**
	 * Opens a CSV file
	 * @param csvFile The file object
	 * @throws IOException
	 */
	public void open(File csvFile) throws IOException {

		fw = new FileWriter(csvFile);
		writer = new BufferedWriter( fw );

		if (!csvFile.exists()) {
			csvFile.createNewFile();
		}

		writer.write("Date/Time;");
		writer.write("TYPE;");
		writer.write("SRC;");
		writer.write("DST;");
		writer.write("CMD;");
		writer.write("LEN;");
		writer.write("DATA;");
		writer.write("CRC;");
		writer.write("ACK;");
		writer.write("S LEN;");
		writer.write("S DATA;");
		writer.write("S CRC;");
		writer.write("COMMENT");
		writer.newLine();
		writer.flush();
	}

	public void close() throws IOException {
		if(writer != null) {
			writer.flush();
			writer.close();
		}

		if(fw != null)
			fw.close();
	}

	/**
	 * Writes a eBUS telegram to the CSV file
	 * @param telegram
	 * @param comment
	 * @throws IOException
	 */
	public synchronized void writeTelegram(EBusTelegram telegram, String comment) {

		try {
			if(writer == null)
				return;

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();

			writer.write(df.format(date));
			writer.write(";");

			writer.write('"'+EBusUtils.toHexDumpString(telegram.getType())+'"');
			writer.write(";");

			writer.write('"'+EBusUtils.toHexDumpString(telegram.getSource())+'"');
			writer.write(";");

			writer.write('"'+EBusUtils.toHexDumpString(telegram.getDestination())+'"');
			writer.write(";");

			short u = telegram.getCommand();

			byte x = (byte) u;
			byte y = (byte) (u>>8);

			writer.write(EBusUtils.toHexDumpString(y));
			writer.write(" ");
			writer.write(EBusUtils.toHexDumpString(x));
			writer.write(";");

			writer.write('"'+EBusUtils.toHexDumpString((byte) telegram.getDataLen())+'"');
			writer.write(";");

			writer.write(EBusUtils.toHexDumpString(telegram.getData()).toString());
			writer.write(";");

			writer.write('"'+EBusUtils.toHexDumpString(telegram.getCRC())+'"');
			writer.write(";");

			if(telegram.getType() != EBusTelegram.TYPE_MASTER_SLAVE) {
				writer.write(";");
				writer.write(";");
				writer.write(";");
				writer.write(";");
			} else {
				writer.write('"'+"00"+'"');
				writer.write(";");

				writer.write('"'+EBusUtils.toHexDumpString((byte)telegram.getSlaveDataLen())+'"');
				writer.write(";");

				writer.write(EBusUtils.toHexDumpString(telegram.getSlaveData()).toString());
				writer.write(";");

				writer.write('"'+EBusUtils.toHexDumpString((byte)telegram.getSlaveCRC())+'"');
				writer.write(";");
			}

			writer.write(comment);

			writer.newLine();
			writer.flush();
			
		} catch (IOException e) {
			logger.error("error", e);
		}
	}

}
