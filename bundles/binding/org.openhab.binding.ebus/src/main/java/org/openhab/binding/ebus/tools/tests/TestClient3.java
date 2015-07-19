package org.openhab.binding.ebus.tools.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.ebus.internal.connection.EBusCommandProcessor;
import org.openhab.binding.ebus.internal.connection.EBusTCPConnector;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;
import org.openhab.binding.ebus.internal.parser.EBusTelegramCSVWriter;
import org.openhab.binding.ebus.internal.parser.EBusTelegramParser;
import org.openhab.binding.ebus.internal.utils.EBusUtils;

public class TestClient3 {

	public static void main(String[] args) {

//		String a = "(CC AA FF CC";
//
//		Pattern pattern = Pattern.compile("(\\([0-9A-Z]{2}\\))");
//		String replaceAll = StringUtils.strip(pattern.matcher(a).replaceAll(""));
//		
////		String replaceAll = a.replaceAll("(\\([0-9A-Z]{2}\\))", "--");
//		String replaceAll2 = a.replaceAll("(\\(|\\))", "");
//		System.out.println("TestClient3.main() " + replaceAll);
//		System.out.println("TestClient3.main() " + replaceAll2);
//		
//		if(true)
//			return;
		
		final EBusTCPConnector connector = new EBusTCPConnector("openhab", 2020);
		connector.setSenderId((byte) 0x00);

		final EBusConfigurationProvider provider = new EBusConfigurationProvider();
		@SuppressWarnings("unused")
		final EBusTelegramParser parser = new EBusTelegramParser(provider);

		File currentDir = new File("").getAbsoluteFile();

		try {
			File x = null;
			
//			x = new File(currentDir, "src/main/resources/common-configuration.json");
//			provider.loadConfigurationFile(x.toURI().toURL());

			x = new File(currentDir, "src/main/resources/wolf-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());

//			x = new File(currentDir, "src/main/resources/wolf-35-configuration.json");
//			provider.loadConfigurationFile(x.toURI().toURL());
//
			x = new File(currentDir, "src/main/resources/wolf-08-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());
//
//			x = new File(currentDir, "src/main/resources/testing-configuration.json");
//			provider.loadConfigurationFile(x.toURI().toURL());

			final EBusTelegramCSVWriter w = new EBusTelegramCSVWriter();
			w.open(new File(currentDir, "src/main/resources/wolf-log.csv"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("program", 3);
//		values.put("data2b", 12.6f);
//		values.put("data2c", 12.1f);
//		values.put("word", 30.05f);

		EBusCommandProcessor processor = new EBusCommandProcessor();
		processor.setConfigurationProvider(provider);

		byte[] xy = processor.composeEBusTelegram("set_heating_circuit_program", "heating_kw", 
				(byte)0x30, (byte)0xFF, values);
		System.out.println(EBusUtils.toHexDumpString(xy));

		values.put("program", 0);
		xy = processor.composeEBusTelegram("set_heating_circuit_program", "heating_kw", 
				(byte)0x30, (byte)0xFF, values);
		System.out.println(EBusUtils.toHexDumpString(xy));

		

		
	}

}
