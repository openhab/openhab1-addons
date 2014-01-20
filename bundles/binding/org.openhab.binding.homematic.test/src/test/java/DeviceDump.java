/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
import org.apache.xmlrpc.XmlRpcException;
import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackHandler;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackServer;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

public class DeviceDump {

    public static void main(String[] args) throws Exception {

        final XmlRpcConnectionRF conn = new XmlRpcConnectionRF("homematic-ccu2");

        CCURF ccu = new CCURF(conn);

        CallbackHandler handler = new CallbackHandler();
        handler.registerCallbackReceiver(ccu);

        final CallbackServer cbServer = new CallbackServer(null, 12345, handler);
        cbServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                conn.init("", "" + conn.hashCode());
                cbServer.stop();
            }
        });
        conn.init("http://laptop-dell-linux:12345/xmlrpc", "" + conn.hashCode());
        printDeviceInfo(conn.getDeviceDescription("KEQ0516655"), conn);
    }

    private static void printDeviceInfo(DeviceDescription devDescr, XmlRpcConnectionRF conn) throws XmlRpcException, InterruptedException {
        System.out.println(devDescr);

        String[] pSetNames = devDescr.getParamsets();
        for (String pSetName : pSetNames) {
            System.out.print(pSetName + ": ");
            try {
                ParamsetDescription pSet = conn.getParamsetDescription(devDescr.getAddress(), pSetName);
                System.out.println(pSet);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("failed");
            }
        }

        System.out.println("");

        String[] childAddresses = devDescr.getChildren();
        if (childAddresses != null) {
            for (String childAddr : childAddresses) {
                DeviceDescription childDescr = conn.getDeviceDescription(childAddr);
                printDeviceInfo(childDescr, conn);
                System.out.println("");
            }
        }
    }

}
