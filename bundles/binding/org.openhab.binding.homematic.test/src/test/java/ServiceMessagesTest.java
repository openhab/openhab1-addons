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
