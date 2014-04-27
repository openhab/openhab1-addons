/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
