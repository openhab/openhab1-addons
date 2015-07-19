package org.openhab.binding.ebus.tools.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.ebus.internal.EBusTelegram;
import org.openhab.binding.ebus.internal.connection.EBusCommandProcessor;
import org.openhab.binding.ebus.internal.connection.EBusConnectorEventListener;
import org.openhab.binding.ebus.internal.connection.EBusTCPConnector;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;
import org.openhab.binding.ebus.internal.parser.EBusTelegramCSVWriter;
import org.openhab.binding.ebus.internal.parser.EBusTelegramParser;
import org.openhab.binding.ebus.internal.utils.EBusUtils;

public class TestClient {

	public static void main(String[] args) {
		
		final EBusTCPConnector connector = new EBusTCPConnector("openhab", 2020);
		connector.setSenderId((byte) 0x00);
		
		final EBusConfigurationProvider provider = new EBusConfigurationProvider();
		final EBusTelegramParser parser = new EBusTelegramParser(provider);
		
		try {
			File currentDir = new File("").getAbsoluteFile();
			
			File x = new File(currentDir, "src/main/resources/common-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());
			
			x = new File(currentDir, "src/main/resources/wolf-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());
			
			x = new File(currentDir, "src/main/resources/wolf-35-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());
			
			x = new File(currentDir, "src/main/resources/wolf-08-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());
			
			x = new File(currentDir, "src/main/resources/testing-configuration.json");
			provider.loadConfigurationFile(x.toURI().toURL());

			final EBusTelegramCSVWriter w = new EBusTelegramCSVWriter();
			w.open(new File(currentDir, "src/main/resources/wolf-log.csv"));
			
			if(connector.connect()) {
				System.out.println("TestClient.main()");
				connector.addEBusEventListener(new EBusConnectorEventListener() {
					
					@Override
					public void onTelegramReceived(EBusTelegram telegram) {
						Map<String, Object> parse = parser.parse(telegram);
						
						w.writeTelegram(telegram, parse != null ? parse.toString():"");

//						System.out.println(EBusUtils.toHexDumpString(telegram.getBuffer()));
					}
				});
				
//				connector.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		while(true) {
			int read;
			try {
				read = System.in.read();
				
				switch (read) {
				case '1':
					connector.addToSendQueue(EBusUtils.toByteArray("00 F6 08 04 06 00 01 31 05 FF FF FF FF FF FF 00"));
					break;
				case '2':
					connector.addToSendQueue(EBusUtils.toByteArray("00 08 50 22 03 CC 0E 00 8D"));
					break;
					
				case '3':
//					Map<String, Object> command = provider.getCommandById("setter", "bai00");
//					@SuppressWarnings({ "unused", "unchecked" })
//					Map<String, Object> valueEntry = (Map<String, Object>) command.get("values");
					
					Map<String, Object> values = new HashMap<String, Object>();
					values.put("byte", 12.2f);
					values.put("data2b", 12.6f);
					values.put("data2c", 12.1f);
					values.put("word", 30.05f);
					
					EBusCommandProcessor processor = new EBusCommandProcessor();
					processor.setConfigurationProvider(provider);
					
					byte[] xy = processor.composeEBusTelegram("setter", "bai00", 
							(byte)1, (byte)2, values);
					
					
					
					System.out.println(EBusUtils.toHexDumpString(xy));
//					provider.xy(commandId, commandClass, dst, src, type, value)
					break;
					
				default:
					break;
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}
		

		
	}

}
