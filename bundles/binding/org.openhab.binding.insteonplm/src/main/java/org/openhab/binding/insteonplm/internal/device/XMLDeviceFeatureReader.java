package org.openhab.binding.insteonplm.internal.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.openhab.binding.insteonplm.internal.utils.Utils.ParsingException;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLDeviceFeatureReader {
	public static ArrayList<FeatureTemplate> s_readTemplates(InputStream input)
			throws IOException, ParsingException {
		ArrayList<FeatureTemplate> features = new ArrayList<FeatureTemplate>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Parse it!
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();

			Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node  = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) node;
					if (e.getTagName().equals("feature")) features.add(s_parseFeature(e));
				}
			}
		} catch (SAXException e) {
			throw new ParsingException("Failed to parse XML!", e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException("Got parser config exception! ", e);
		}
		return features;
	}
	private static FeatureTemplate s_parseFeature(Element e) throws ParsingException {
		String name = e.getAttribute("name");
		boolean statusFeature = e.getAttribute("statusFeature").equals("true");
		FeatureTemplate feature = new FeatureTemplate();
		feature.setName(name);
		feature.setStatusFeature(statusFeature);
		
		NodeList nodes = e.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node  = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) node;
				if (child.getTagName().equals("message-handler")) {
					s_parseMessageHandler(child, feature);
				} else if (child.getTagName().equals("command-handler")) {
					s_parseCommandHandler(child, feature);
				} else if (child.getTagName().equals("message-dispatcher")) {
					s_parseMessageDispatcher(child, feature);
				} else if (child.getTagName().equals("poll-handler")) {
					s_parsePollHandler(child, feature);
				}
			}
		}
		
		return feature;
	}
	private static void s_parseMessageHandler(Element e, FeatureTemplate f) throws DOMException, ParsingException {
		String handler = e.getTextContent();
		if (handler == null) throw new ParsingException("Could not find MessageHandler for: " + e.getTextContent());
		if (e.getAttribute("default").equals("true")) {
			f.setDefaultMessageHandler(handler);
		} else {
			int command = Utils.from0xHexString(e.getAttribute("cmd"));
			f.addMessageHandler(command, handler);
		}
	}
	private static void s_parseCommandHandler(Element e, FeatureTemplate f) throws ParsingException {
		String handler = e.getTextContent();
		if (handler == null) throw new ParsingException("Could not find CommandHandler for: " + e.getTextContent());
		if (e.getAttribute("default").equals("true")) {
			f.setDefaultCommandHandler(handler);
		} else {
			Class<? extends Command> command = s_parseCommandClass(e.getAttribute("command"));
			f.addCommandHandler(command, handler);
		}
	}
	private static void s_parseMessageDispatcher(Element e, FeatureTemplate f) throws DOMException, ParsingException {
		String dispatcher = e.getTextContent();
		if (dispatcher == null) throw new ParsingException("Could not find MessageDispatcher for: " + e.getTextContent());
		f.setMessageDispatcher(dispatcher);
	}
	private static void s_parsePollHandler(Element e, FeatureTemplate f) throws ParsingException {
		String pollHandler = e.getTextContent();
		if (pollHandler == null) throw new ParsingException("Could not find PollHandler: " + pollHandler);
		f.setPollHandler(pollHandler);
	}
	
	private static Class<? extends Command> s_parseCommandClass(String c) throws ParsingException {
		if (c.equals("OnOffType")) return OnOffType.class;
		else if (c.equals("PercentType")) return PercentType.class;
		else if (c.equals("DecimalType")) return DecimalType.class;
		else throw new ParsingException("Unknown Command Type");
	}
	
	public static void main(String[] args) throws Exception {
		File f = new File(System.getProperty("user.home") + 
				"/workspace/openhab/bundles/binding/org.openhab.binding.insteonplm/src/main/resources/device_features.xml");
		InputStream s = new FileInputStream(f);
		ArrayList<FeatureTemplate> features = s_readTemplates(s);
		for (FeatureTemplate feature : features) {
			System.out.println(feature);
			System.out.println("\tPOLL: " + feature.getPollHandler() + "\n\tDISPATCH: " + feature.getDispatcher());
			System.out.println("\tDCH: " + feature.getDefaultCommandHandler() + "\n\tDMH: " + feature.getDefaultMessageHandler());
			System.out.println("\tMSG HANDLERS: " + feature.getMessageHandlers());
			System.out.println("\tCMD HANDLERS: " + feature.getCommandHandlers());
		}
	}
}
