/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.frontiersiliconradio.internal;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.CharacterData;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class hold the result of a request read from the radio. 
 * Upon a request the radio returns a XML document like this:
 * 
 *  <fsapiResponse>
 *	<status>FS_OK</status>
 *	<value><u8>1</u8></value>
 *	</fsapiResponse>
 * 
 * This class parses this XML data and provides funtion for reading and casting typical fields.
 * 
 * @author Rainer Ostendorf
 * @since 1.7.0
 *
 */
public class FrontierSiliconRadioApiResult {

	/**
	 * XML structure holding the parsed response
	 */
	Document xmlDoc;
	
	private static final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioConnection.class);
	
	public FrontierSiliconRadioApiResult(String requestResultString) {
		try {
			this.xmlDoc = getXmlDocFromString(requestResultString);
		}
		catch (Exception e) {
			logger.error("converting to XML failed: '"+requestResultString+"'");
		}
	}
	
	/**
	 * Extract the fied "status" from the result and return it
	 * 
	 * @return result field as string.
	 */
	public String getStatus() {
		Element fsApiResult = (Element)xmlDoc.getElementsByTagName("fsapiResponse").item(0);
		Element statusNode = (Element)fsApiResult.getElementsByTagName("status").item(0);
		String status = getCharacterDataFromElement( statusNode );
		logger.trace("status is: "+status);
		return status;
	}
	
	/**
	 * checks if the responses status code was "FS_OK"
	 * 
	 * @return true if status is "FS_OK", false else
	 */
	public Boolean isStatusOk() {
		return ("FS_OK").equals(getStatus());
	}
	
	/**
	 * read the <value><u8> field as boolean
	 * 
	 * @return value.u8 field as bool
	 */
	public Boolean getValueU8AsBoolean() {
		try {
			Element fsApiResult = (Element)xmlDoc.getElementsByTagName("fsapiResponse").item(0);			
			Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
			Element u8Node = (Element) valueNode.getElementsByTagName("u8").item(0);
			
			String value = getCharacterDataFromElement( u8Node );
			logger.trace("value is: "+value);
			
			return ("1".equals(value)?true:false);
		}
		catch (Exception e) {
			logger.error( "getting Value.U8 failed");
			return false;
		}
	}
	
	/**
	 * read the <value><u8> field as int
	 * 
	 * @return value.u8 field as int
	 */
	public int getValueU8AsInt() {
		try {
			Element fsApiResult = (Element)xmlDoc.getElementsByTagName("fsapiResponse").item(0);				
			Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
			Element u8Node = (Element) valueNode.getElementsByTagName("u8").item(0);
			
			String value = getCharacterDataFromElement( u8Node );
			
			logger.trace("value is: "+value);
			
			return Integer.parseInt(value);
		}
		catch (Exception e) {
			logger.error( "getting Value.U8 failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * read the <value><u32> field as int
	 * 
	 * @return value.u32 field as int
	 */
	public int getValueU32AsInt() {
		try {
			Element fsApiResult = (Element)xmlDoc.getElementsByTagName("fsapiResponse").item(0);
			Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
			Element u32Node = (Element) valueNode.getElementsByTagName("u32").item(0);
			
			String value = getCharacterDataFromElement( u32Node );
			logger.trace("value is: "+value);
			
			return Integer.parseInt(value);
		}
		catch (Exception e) {
			logger.error( "getting Value.U32 failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * read the <value><c8_array> field as String
	 * 
	 * @return value.c8_array field as String
	 */
	public String getValueC8ArrayAsString() {
		try {
			Element fsApiResult = (Element)xmlDoc.getElementsByTagName("fsapiResponse").item(0);
			Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
			Element c8Array = (Element) valueNode.getElementsByTagName("c8_array").item(0);
			
			String value = getCharacterDataFromElement( c8Array );
			logger.trace("value is: "+value);
			return value;
		}
		catch (Exception e) {
			logger.error( "getting Value.c8array failed");
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * read the <sessionId> field as String
	 * 
	 * @return value of sessionId field
	 */
	public String getSessionId() {
		NodeList sessionIdTagList = xmlDoc.getElementsByTagName("sessionId");
		String givenSessId = getCharacterDataFromElement( (Element)sessionIdTagList.item(0));
		return givenSessId;
	}
	
	
	/**
	 * converts the string we got from the radio to a parsable XML document
	 * 
	 * @param xmlString the XML string read from the radio
	 * @return the parsed XML document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getXmlDocFromString(String xmlString) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document xmlDocument = builder.parse( new InputSource( new StringReader(xmlString ) ) );
		return xmlDocument;
	}
	
	
	/**
	 *  convert the value of a given XML element to a string for further processing
	 *  
	 * @param e XML Element
	 * @return the elements value converted to string
	 */
	private static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    else
	    	return "";
	 }
}
