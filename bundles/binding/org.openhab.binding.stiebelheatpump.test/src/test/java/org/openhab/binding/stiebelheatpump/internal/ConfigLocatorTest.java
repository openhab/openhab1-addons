package org.openhab.binding.stiebelheatpump.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition.Type;
import org.openhab.binding.stiebelheatpump.protocol.Request;

public class ConfigLocatorTest {

	public ConfigLocatorTest() {
	}

	@Test
	public void LoadResourceFiles() throws StiebelHeatPumpException {

		String workingDir = System.getProperty("user.dir");
		String FILEDUMP = "itemDump.txt";
		FILEDUMP = workingDir + File.separator + FILEDUMP;
		String GROUPPREFIX = "gEnergieWaermepumpe_";
		String PARENTGROUP = "gEnergieWaermepumpe";

		ConfigLocator configLocator = new ConfigLocator("2.06.xml");
		List<Request> configuration = configLocator.getConfig();

		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version", firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());

		Assert.assertEquals((byte) 0x05, configuration.get(7).getRequestByte());

		FileWriter dumpFile;
		try {
			dumpFile = new FileWriter(FILEDUMP);

			String newLine = System.getProperty("line.separator");

			// dump items definition
			for (Request request : configuration) {
				// create group item
				String groupItem = String.format("Group %s%s \"%s\" (%s) ",
						GROUPPREFIX, request.getName(), request.getName(),
						PARENTGROUP);
				dumpFile.write(groupItem + newLine);

				// add item definition per record
				for (RecordDefinition record : request.getRecordDefinitions()) {
					String itemType;
					String itemFormat;

					if (record.getDataType() == Type.Status) {
						if (record.getUnit() == "") {
							itemType = "String";
							itemFormat = "[%s]";
						} else if (record.getScale() != 1.0) {
							itemType = "Number";
							itemFormat = "[%.1f " + record.getUnit() + "]";
						} else {
							itemType = "Number";
							itemFormat = "[%d " + record.getUnit() + "]";
						}
					} else {
						itemType = "Number";
						if (record.getScale() != 1.0) {
							itemFormat = "[%.1f " + record.getUnit() + "]";
						} else {
							itemFormat = "[%d " + record.getUnit() + "]";
						}
					}

					// { stiebelheatpump="OutputElectricalHeatingStage1" }
					String itemDefinition = String
							.format("%s %s \"%s %s\" (%s,%s) { stiebelheatpump=\"%s\" }",
									itemType, record.getName(),
									record.getName(), itemFormat, GROUPPREFIX
											+ request.getName(), PARENTGROUP,
									record.getName());
					dumpFile.write(itemDefinition + newLine);
				}

				// add site item definition per record
				for (RecordDefinition record : request.getRecordDefinitions()) {
					String itemType = "";
					String itemFormat = "";

					Type dataType = record.getDataType();
					switch (dataType) {
					case Settings:
						itemType = "Setpoint item=";
						itemFormat = String.format(
								"%s minValue=%s maxValue=%s step=%s",
								record.getName(), record.getMin(),
								record.getMax(), record.getStep());
						break;
					case Status:
					case Sensor:
						itemType = "Text item=";
						itemFormat = record.getName();
						break;
					}

					dumpFile.write(itemType + itemFormat + newLine);
				}

				dumpFile.write(newLine);
			}

			for (Request request : configuration) {
				for (RecordDefinition record : request.getRecordDefinitions()) {
					if (record.getDataType() == Type.Settings) {
						dumpFile.write(record.getName() + ",");
					}
				}
			}

			dumpFile.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
