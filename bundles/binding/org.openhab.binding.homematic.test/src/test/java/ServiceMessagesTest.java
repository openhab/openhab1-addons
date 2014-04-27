/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackServer;



public class ServiceMessagesTest {

	/**
	 * @param args
	 * @throws XmlRpcException 
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException, XmlRpcException {
		Logger.getLogger("").setLevel(Level.ALL);
		
		
        XmlRpcConnectionRF conn = new XmlRpcConnectionRF("homematic");
		
		CCURF ccu = new CCURF(conn);
			
		CallbackHandler handler = new CallbackHandler();
		handler.registerCallbackReceiver(ccu);
		
		CallbackServer cbServer = new CallbackServer(null, 12345, handler);
		cbServer.start();

        conn.init("http://localhost:12345/xmlrpc", "" + conn.hashCode());
		
		
		conn.getServiceMessages();

	
	}
	
}
