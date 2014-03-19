/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;



public class CompleteDump {

	/**
	 * @param args
	 * @throws XmlRpcException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws XmlRpcException, IOException, InterruptedException {
	
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.OFF);		
		
		
		XmlRpcConnectionRF conn = new XmlRpcConnectionRF("10.0.0.18");
		
		CCURF ccu = new CCURF(conn);
			
		CallbackHandler handler = new CallbackHandler();
		handler.registerCallbackReceiver(ccu);
		
		//CallbackServer cbServer = new CallbackServer(12345, handler);
		//cbServer.start();

		//conn.init("http://10.0.0.102:12345/xmlrpc", "" + conn.hashCode());
		
		Set<DeviceDescription> devices = conn.listDevices();
		for(DeviceDescription dev: devices) {
			if(dev.getAddress().startsWith("BidCoS")) { continue; }
			if(!dev.getAddress().contains(":")) {
				System.out.println("---------------------------------------------");
				printDeviceInfo(dev, conn);	
				System.out.println("---------------------------------------------");
			}
		}
	
		System.out.println("DONE");
	}

	
	private static void printDeviceInfo(DeviceDescription devDescr, XmlRpcConnectionRF conn) throws XmlRpcException {
		System.out.println(devDescr);	
		
		String[] pSetNames = devDescr.getParamsets();
		for(String pSetName: pSetNames) {
			System.out.print(pSetName + ": ");
			try {
				Paramset pSet = conn.getParamset(devDescr.getAddress(), pSetName);
				System.out.println(pSet);
			} catch(Exception ex) {
				System.out.println("failed");
			}
		}
		
		System.out.println("");
		
		String[] childAddresses = devDescr.getChildren();
		if(childAddresses != null) {
			for(String childAddr: childAddresses) {
				DeviceDescription childDescr = conn.getDeviceDescription(childAddr);
				printDeviceInfo(childDescr, conn);
				System.out.println("");
			}
		}
	}
	
}
