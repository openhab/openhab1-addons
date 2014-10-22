/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.lib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the discovery and discarding of the GC-100 devices. It implements
 * IGC100IRControlPoint. This class follows the Singleton pattern, hence the
 * contructor is private. The singleton instance can be got by using the
 * getInstance() method
 * 
 * @author Parikshit Thakur & Team
 * @since 1.6.0
 */

public class GC100IRControlPoint implements IGC100IRControlPoint {

	/**
	 * Port number to connect to IR Port.
	 */
	public static final int IR_PORT = 4998;

	/**
	 * Port number to connect to device.
	 */
	public static final int CONNECT_PORT = 4998;

	private static final String DEVICE_TYPE_IR = "IR";

	public static final String GC_100_QUERY_MESSAGE_GET_DEVICES = "getdevices\r";

	private List<TCPIPSocket> socketList;

	private static final String END_LIST_DEVICES = "endlistdevices";

	private static Logger logger = LoggerFactory
			.getLogger(GC100IRControlPoint.class);

	Map<GC100IRDevice, Long> deviceLastDisTimeMap;

	Map<GC100IRDevice, Map<String, String>> deviceGCPropsMap;

	private static GC100IRControlPoint gc100ControlPoint;

	/**
	 * Returns an Instance of GC100IRControlPoint. Singleton Pattern.
	 * 
	 * @return an Object of GC100IRControlPoint
	 */
	public static GC100IRControlPoint getInstance() {

		if (gc100ControlPoint == null)
			gc100ControlPoint = new GC100IRControlPoint();

		return gc100ControlPoint;
	}

	private GC100IRControlPoint() {

		deviceGCPropsMap = new LinkedHashMap<GC100IRDevice, Map<String, String>>();
		deviceLastDisTimeMap = new HashMap<GC100IRDevice, Long>();
		socketList = new ArrayList<TCPIPSocket>();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean connectTarget(String gc100host, int module, int connector) {

		if (gc100host == null || module <= 0 || connector <= 0) {
			logger.warn("hostname, module or connector is invalid");
			return false;
		}

		Set<GC100IRDevice> deviceSet = null;

		synchronized (deviceLastDisTimeMap) {
			deviceSet = deviceLastDisTimeMap.keySet();
		}

		if ((deviceSet == null) || (deviceSet.size() == 0)) {
			logger.warn("deviceSet is null or its size is 0.");
			return false;
		}

		GC100IRDevice gcDevice = null;

		for (GC100IRDevice gcDev : deviceSet) {

			String ipOfDevice = gcDev.getIPAddressString();

			if (ipOfDevice.equals(gc100host)) {

				if ((module != gcDev.getModule())
						|| (connector != gcDev.getConnector())) {
					continue;
				}

				gcDevice = gcDev;
				break;
			}
		}

		if (gcDevice == null) {
			logger.warn("gcDevice is null.");
			return false;
		}

		TCPIPSocket tcpIpSoc = gcDevice.getTCPIPSocket();

		if (tcpIpSoc != null) {

			if (!tcpIpSoc.isConnected())
				try {
					tcpIpSoc.connect();
					return true;
				} catch (IOException e) {
					logger.error("IOException : " + e.getMessage());
				}
		}

		int port = 4998;
		InetAddress ipAddress = null;

		try {
			ipAddress = gcDevice.getInetAddress();
		} catch (UnknownHostException e) {
			logger.warn("UnknownHostException : " + e.getMessage());
			return false;
		}

		String deviceType = gcDevice.getConnectorType();

		if (deviceType.equals(DEVICE_TYPE_IR)) {

			port = IR_PORT;

		}

		Socket socket = null;

		try {

			synchronized (socketList) {

				for (TCPIPSocket tcpIpSocket : socketList) {

					if (tcpIpSocket == null)
						continue;

					InetAddress tcpInetAddress = tcpIpSocket.getInetAddress();
					int tcpPort = tcpIpSocket.getPort();

					if (tcpInetAddress.equals(ipAddress) && (tcpPort == port)) {

						String tcpIPString = tcpIpSocket.getInetAddress()
								.toString();

						if (tcpIPString.contains("/"))
							tcpIPString = tcpIPString.substring(1);

						if (gc100host != null && gc100host.equals(tcpIPString)
								&& (module == tcpIpSocket.getModule())
								&& (connector == tcpIpSocket.getConnector())) {

							if (!tcpIpSocket.isConnected())
								tcpIpSocket.connect();

							return true;
						}

						if (socket == null) {

							if (!tcpIpSocket.isConnected())
								tcpIpSocket.connect();

							socket = tcpIpSocket.getSocket();
						}
					}
				}
			}

			String gchost = null;
			if (ipAddress != null && ipAddress.toString().contains("/"))
				gchost = ipAddress.toString().substring(1);
			else if (ipAddress != null)
				gchost = ipAddress.toString();

			if (socket != null) {

				TCPIPSocket tcpIpSocket = new TCPIPSocket(ipAddress, gchost,
						port, module, connector, deviceType, socket);

				synchronized (FLOW_CONTROL_VALUE_FLOW_HARDWARE) {
					socketList.add(tcpIpSocket);
				}

				gcDevice.setTCPIPSocket(tcpIpSocket);
				return true;

			} else {

				TCPIPSocket tcpIpSocket = new TCPIPSocket(ipAddress, gchost,
						port, module, connector, deviceType);

				if (tcpIpSocket.connect()) {

					synchronized (FLOW_CONTROL_VALUE_FLOW_HARDWARE) {
						socketList.add(tcpIpSocket);
					}

					gcDevice.setTCPIPSocket(tcpIpSocket);
					return true;
				}

			}

		} catch (UnknownHostException e) {
			logger.error("UnknownHostException :" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException :" + e.getMessage());
		}

		return false;
	}

	/**
	 * Used to check TCP/IP socket is connected or not.
	 * 
	 * @param ipAddress
	 * @return true if socket is connected with ipAddress
	 */
	public boolean isGC100ItemConnected(String ipAddress) {

		synchronized (socketList) {

			for (TCPIPSocket tcpIpSocket : socketList) {

				if (tcpIpSocket == null)
					continue;

				String tcpIpString = tcpIpSocket.getInetAddress().toString();
				if (tcpIpString.contains("/"))
					tcpIpString = tcpIpString.substring(1);
				if (ipAddress.equals(tcpIpString))
					return tcpIpSocket.isConnected();
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void disConnectTarget(String ipAddress, int module, int connector) {

		Set<GC100IRDevice> deviceSet = null;

		synchronized (deviceLastDisTimeMap) {
			deviceSet = deviceLastDisTimeMap.keySet();
		}

		if ((deviceSet == null) || (deviceSet.size() == 0))
			return;

		for (GC100IRDevice gcDevice : deviceSet) {

			String ipAddressOfDevice = gcDevice.getIPAddressString();
			if (ipAddressOfDevice.equals(ipAddress)) {
				deviceLastDisTimeMap.remove(gcDevice);
			}
		}
	}

	/**
	 * Returns GC100IRDevice instance.
	 * 
	 * @param ipAddress
	 * @param module
	 * @param connector
	 * @return GC100Device object
	 */
	private GC100IRDevice getGCDevice(String ipAddress, int module,
			int connector) {

		if ((ipAddress == null) || (module == 0) || (connector == 0))
			return null;

		synchronized (deviceLastDisTimeMap) {

			for (GC100IRDevice device : deviceLastDisTimeMap.keySet()) {

				if (device == null)
					continue;

				if (ipAddress.equals(device.getIPAddressString())
						&& module == device.getModule()
						&& connector == device.getConnector())
					return device;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized String doAction(String ipAddressString, int module,
			int connector, String code) {

		if (code == null)
			return null;

		GC100IRDevice device = getGCDevice(ipAddressString, module, connector);

		if (device == null) {
			logger.warn("Unable to get GC 100 Device for IP Address '"
					+ ipAddressString + "', module '" + module
					+ "' and connector '" + connector + "'.");
			return null;
		}

		TCPIPSocket tcpIPSocket = device.getTCPIPSocket();

		if (tcpIPSocket == null) {

			if (connectTarget(ipAddressString, module, connector)) {

				device = getGCDevice(ipAddressString, module, connector);

				if (device == null) {
					logger.warn("Unable to get GC 100 Device for IP Address '"
							+ ipAddressString + "', module '" + module
							+ "' and connector '" + connector + "'.");
					return null;
				}

				tcpIPSocket = device.getTCPIPSocket();

				if (tcpIPSocket == null) {
					logger.warn("Unable to get TCP IP Socket for IP Address '"
							+ ipAddressString + "', module '" + module
							+ "' and connector '" + connector + "'.");
					return null;
				}
			}

		}

		String deviceType = tcpIPSocket.getDeviceType();

		if (deviceType == null) {
			logger.warn("Unable to get Device Type for IP Address '"
					+ ipAddressString + "', module '" + module
					+ "' and connector '" + connector + "'.");
			return null;
		}

		try {

			if (deviceType.equals(DEVICE_TYPE_IR)) {

				Socket socket = tcpIPSocket.getSocket();

				if (socket == null) {
					logger.warn("Unable to get Socket for IP Address '"
							+ ipAddressString + "', module '" + module
							+ "' and connector '" + connector + "'.");
					return null;
				}

				if (!tcpIPSocket.isConnected())
					tcpIPSocket.connect();

				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());

				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				while (in.ready()) {
					in.read();
				}

				out.write(code.getBytes());

				StringBuilder response = new StringBuilder();

				int count = 0;
				while (!in.ready()) {

					count++;

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						logger.warn("InterruptedException");
					}

					if (count > 20) {
						return null;
					}
				} // Wait till data available to input stream

				while (true) {

					response.append((char) in.read());

					if (!in.ready())
						break;
				}

				return responseOfDevice(response.toString());

			}

		} catch (IOException e) {
			logger.warn("IOException");
		}

		return null;

	}

	/**
	 * Returns response of device for the specified 'response'.
	 * 
	 * @param response
	 *            a String value of response
	 * @return a String value of response.
	 */
	private String responseOfDevice(String response) {

		if (response.equals("unknowncommand, 1"))
			return ("Time out occurred because carriage return <CR> not received. The request was not processed.");
		else if (response.equals("unknowncommand, 2"))
			return ("Invalid module address (module does not exist) received when attempting to ascertain the version number (getversion).");
		else if (response.equals("unknowncommand, 3"))
			return ("Invalid module address (module does not exist).");
		else if (response.equals("unknowncommand, 4"))
			return ("Invalid connector address.");
		else if (response.equals("unknowncommand, 5"))
			return ("Connector address 1 is set up as �sensor in� when attempting to send an IR command.");
		else if (response.equals("unknowncommand, 6"))
			return ("Connector address 2 is set up as �sensor in� when attempting to send an IR command.");
		else if (response.equals("unknowncommand, 7"))
			return ("Connector address 3 is set up as �sensor in� when attempting to send an IR command.");
		else if (response.equals("unknowncommand, 8"))
			return ("Offset is set to an even transition number, but should be set to an odd transition number in the IR command.");
		else if (response.equals("unknowncommand, 9"))
			return ("Maximum number of transitions exceeded (256 total on/off transitions allowed).");
		else if (response.equals("unknowncommand, 10"))
			return ("Number of transitions in the IR command is not even (the same number of on and off transitions is required).");
		else if (response.equals("unknowncommand, 11"))
			return ("Contact closure command sent to a module that is not a relay..");
		else if (response.equals("unknowncommand, 12"))
			return ("Missing carriage return. All commands must end with a carriage return.");
		else if (response.equals("unknowncommand, 13"))
			return ("State was requested of an invalid connector address, or the connector is programmed as IR out and not sensor in.");
		else if (response.equals("unknowncommand, 14"))
			return ("Command sent to the unit is not supported by the GC-100.");
		else if (response.equals("unknowncommand, 15"))
			return ("Maximum number of IR transitions exceeded. (SM_IR_INPROCESS)");
		else if (response.equals("unknowncommand, 16"))
			return ("Invalid number of IR transitions (must be an even number).");
		else if (response.equals("unknowncommand, 21"))
			return ("Attempted to send an IR command to a non-IR module.");
		else if (response.equals("unknowncommand, 23"))
			return ("Command sent is not supported by this type of module.");
		else
			return response;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<GC100IRDevice> getAvailableDevices() {

		if (deviceGCPropsMap == null)
			return null;

		synchronized (deviceGCPropsMap) {
			return deviceGCPropsMap.keySet();
		}

	}

	/**
	 * Gets the list of devices.
	 * 
	 * @param getDevices
	 *            a String value of getDevices
	 * @param sourceAddress
	 *            a String value of sourceAddress
	 * @return a String value for no. of devices that can be attached to the
	 *         GC-100
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public String queryGC100(String getDevices, String sourceAddress)
			throws UnknownHostException, IOException {

		List<String> deviceList = new ArrayList<String>();

		Socket socket = new Socket(sourceAddress,
				GC100IRControlPoint.CONNECT_PORT);

		SocketAddress socketAddress = socket.getRemoteSocketAddress();

		if (socket.isConnected())
			logger.info("Connected Successfully to " + socketAddress);

		DataOutputStream outToServer = new DataOutputStream(
				socket.getOutputStream());

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		byte[] commandInBytes = getDevices.getBytes();

		outToServer.write(commandInBytes);
		int c;
		StringBuilder response = new StringBuilder();

		while ((c = inFromServer.read()) != '\0') {

			response.append((char) c);

			if (c == 13) {
				deviceList.add(response.toString());
				response = new StringBuilder();
			}

			if (response.toString().equals(END_LIST_DEVICES))
				break;
		}

		StringBuilder temp = new StringBuilder();

		for (String deviceStr : deviceList)
			temp.append(deviceStr);

		try {

			socket.close();

		} catch (SocketException e) {
			logger.warn("Socket Exception occured");
		}

		return temp.toString();
	}

	/**
	 * Parses the list of devices available to the GC-100 device.
	 * 
	 * @param ipAddress
	 *            a String value of ipAddress
	 * @param getDevicesResponse
	 *            a String value of get Devices Response
	 */
	public void parseDevices(String ipAddress, String getDevicesResponse) {

		if ((getDevicesResponse == null))
			return;

		StringTokenizer st = new StringTokenizer(getDevicesResponse, "\r ,");

		String configURL = ipAddress;

		if (configURL == null)
			return;

		Date date = new Date();

		Map<String, String> gcProps = new HashMap<String, String>();

		while (st.hasMoreTokens()) {

			if (st.nextToken().equals("device")) {

				int m = Integer.parseInt(st.nextToken());
				gcProps.put("module", String.valueOf(m));

				int c = Integer.parseInt(st.nextToken());
				gcProps.put("connector", String.valueOf(c));

				String deviceType = st.nextToken();
				gcProps.put("deviceType", deviceType);

				for (int i = 1; i <= c; i++) {

					GC100IRDevice gcDevice = new GC100IRDevice(
							gc100ControlPoint, configURL, m, i, deviceType);
					try {
						gcProps.put("inetAddress", gcDevice.getInetAddress()
								.toString());
					} catch (UnknownHostException e) {
						logger.error("UnknownHostException :" + e.getMessage());
					}
					deviceGCPropsMap.put(gcDevice, gcProps);

					synchronized (deviceLastDisTimeMap) {
						deviceLastDisTimeMap.put(gcDevice, date.getTime());
					}
				}
			}
		}
	}
}
