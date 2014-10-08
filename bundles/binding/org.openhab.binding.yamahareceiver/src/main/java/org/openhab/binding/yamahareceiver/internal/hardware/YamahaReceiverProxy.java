/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal.hardware;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openhab.binding.yamahareceiver.internal.YamahaReceiverBindingConfig.Zone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Yamaha Receiver Proxy used to control a yamaha receiver with HTTP/XML
 * 
 * @author Eric Thill
 * @author Ben Jones
 * @since 1.6.0
 */
public class YamahaReceiverProxy {

	public static final int VOLUME_MIN = -80;
	public static final int VOLUME_MAX = 16;

	private final DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();
	
	private final String host;

	public YamahaReceiverProxy(String host) {
		this.host = host;
	}
	
	public String getHost() {
		return host;
	}

	public void setPower(Zone zone, boolean on) throws IOException {
		if (on) {
			postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Power_Control><Power>On</Power></Power_Control></" + zone + "></YAMAHA_AV>");
		} else {
			postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Power_Control><Power>Standby</Power></Power_Control></" + zone + "></YAMAHA_AV>");
		}
	}

	public void setVolume(Zone zone, float volume) throws IOException {
		postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Volume><Lvl><Val>"
				+ (int) (volume * 10)
				+ "</Val><Exp>1</Exp><Unit>dB</Unit></Lvl></Volume></" + zone + "></YAMAHA_AV>");
	}

	public void setMute(Zone zone, boolean mute) throws IOException {
		if (mute) {
			postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Volume><Mute>On</Mute></Volume></" + zone + "></YAMAHA_AV>");
		} else {
			postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Volume><Mute>Off</Mute></Volume></" + zone + "></YAMAHA_AV>");
		}
	}

	public void setInput(Zone zone, String name) throws IOException {
		postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Input><Input_Sel>"
				+ name + "</Input_Sel></Input></" + zone + "></YAMAHA_AV>");
	}

	public void setSurroundProgram(Zone zone, String name) throws IOException {
		postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><" + zone + "><Surround><Program_Sel><Current><Sound_Program>"
				+ name + "</Sound_Program></Current></Program_Sel></Surround></" + zone + "></YAMAHA_AV>");
	}

	public void setNetRadio(int lineNo) throws IOException {
		 /* Jump to specified line in preset list */
		postAndGetResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"PUT\"><NET_RADIO><Play_Control><Preset><Preset_Sel>"
				+ lineNo + "</Preset_Sel></Preset></Play_Control></NET_RADIO></YAMAHA_AV>");
	}
	
	public YamahaReceiverState getState(Zone zone) throws IOException {
		Document doc = postAndGetXmlResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"GET\"><" + zone + "><Basic_Status>GetParam</Basic_Status></" + zone + "></YAMAHA_AV>");
		Node basicStatus = getNode(doc.getFirstChild(),
				"" + zone + "/Basic_Status");

		Node powerNode = getNode(basicStatus, "Power_Control/Power");
		boolean power = powerNode != null ? "On".equalsIgnoreCase(powerNode
				.getTextContent()) : false;
		Node inputNode = getNode(basicStatus, "Input/Input_Sel");
		String input = inputNode != null ? inputNode.getTextContent() : null;
		Node soundProgramNode = getNode(basicStatus,
				"Surround/Program_Sel/Current/Sound_Program");
		String soundProgram = soundProgramNode != null ? soundProgramNode
				.getTextContent() : null;
		Node volumeNode = getNode(basicStatus, "Volume/Lvl/Val");
		float volume = volumeNode != null ? Float.parseFloat(volumeNode
				.getTextContent()) * .1f : VOLUME_MIN;
		Node muteNode = getNode(basicStatus, "Volume/Mute");
		boolean mute = muteNode != null ? "On".equalsIgnoreCase(muteNode
				.getTextContent()) : false;

		return new YamahaReceiverState(power, input, soundProgram, volume, mute);
	}

	public List<String> getInputsList(Zone zone) throws IOException {
		List<String> names = new ArrayList<String>();
		Document doc = postAndGetXmlResponse("<?xml version=\"1.0\" encoding=\"utf-8\"?><YAMAHA_AV cmd=\"GET\"><" + zone + "><Input><Input_Sel_Item>GetParam</Input_Sel_Item></Input></" + zone + "></YAMAHA_AV>");
		Node inputSelItem = getNode(doc.getFirstChild(),
				zone + "/Input/Input_Sel_Item");
		NodeList items = inputSelItem.getChildNodes();
		for (int i = 0; i < items.getLength(); i++) {
			Element item = (Element) items.item(i);
			String name = item.getElementsByTagName("Param").item(0)
					.getTextContent();
			boolean writable = item.getElementsByTagName("RW").item(0)
					.getTextContent().contains("W");
			if (writable) {
				names.add(name);
			}
		}
		return names;
	}

	private static Node getNode(Node root, String nodePath) {
		String[] nodePathArr = nodePath.split("/");
		return getNode(root, nodePathArr, 0);
	}

	private static Node getNode(Node parent, String[] nodePath, int offset) {
		if (parent == null)
			return null;
		if (offset < nodePath.length - 1) {
			return getNode(
					((Element) parent).getElementsByTagName(nodePath[offset])
							.item(0), nodePath, offset + 1);
		} else {
			return ((Element) parent).getElementsByTagName(nodePath[offset])
					.item(0);
		}
	}

	private Document postAndGetXmlResponse(String message) throws IOException {
		String response = postAndGetResponse(message);
		String xml = response.toString();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			throw new IOException("Could not handle response", e);
		}
	}

	private String postAndGetResponse(String message) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://" + host + "/YamahaRemoteControl/ctrl");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(message.length()));

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(message);
			wr.flush();
			wr.close();
			
			// Read response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			throw new IOException("Could not handle http post", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
