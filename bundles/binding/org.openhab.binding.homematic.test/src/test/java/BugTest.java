/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class BugTest {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws XmlRpcException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws MalformedURLException, XmlRpcException, InterruptedException {
		
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.ALL);		
		ConsoleHandler sysOut = new ConsoleHandler();
		sysOut.setFormatter(new SimpleFormatter());
		for(Handler h: rootLogger.getHandlers()) {
			rootLogger.removeHandler(h);
		}
		rootLogger.addHandler(sysOut);
		
		
		URL url = new URL("http://10.0.0.18:2001");
		
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);
		config.setEnabledForExceptions(true);
		
		XmlRpcClient xmlRpcClient = new XmlRpcClient();
		xmlRpcClient.setConfig(config);	
		
		/*
		 * NO PROBLEM HERE
		 */
//		String[] devices = {"IEQ0000477:1", "IEQ0001542:1", "IEQ0003329:1",
//				"JEQ0028861:1", "JEQ0028861:2"};
//		for(String addr: devices) {
//			System.out.println(addr + " => VALUES");
//			Object[] parameters = {addr, "VALUES"};
//			xmlRpcClient.execute("getParamset", parameters);		
//		}

		
		
		/*
		 * PROBLEM OCCURS HERE FOR DEVICES 
		 * IEQ0000477:1, IEQ0001542:1, IEQ0003329:1,
		 * JEQ0028861:1, JEQ0028861:2
		 */

		Object[] params = {};
		Object[] results = (Object[])xmlRpcClient.execute("listDevices", params);
		
		for(Object result: results) {
			@SuppressWarnings("unchecked")
			Map<String, Object> descr = (Map<String, Object>)result;
			String address = descr.get("ADDRESS").toString();
			
			if(address.startsWith("BidCoS")) { continue; }

			System.out.println("\nFOUND DEVICE: " + address);
			Object[] paramsets = (Object[])descr.get("PARAMSETS");
			for(Object o: paramsets) {
				String name = o.toString();
				System.out.println("FETCHING PARAMSET " + name);
				Object[] info = {address, name};
				xmlRpcClient.execute("getParamset", info);
				Thread.sleep(100);
			}
			Thread.sleep(100);
		}
	}

}
