/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;


public class PhoneBookEntry {
	
	//default logger
	private static final Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);
	
	// Phone numbers
	private String _privateTel;
	private String _businessTel;
	private String _mobileTel;
	private String _fax;
	
	//Phonebook Name this entry is contained in
	private String _phoneBookName;
	
	//Real Name 
	private String _name;
	
	//unique ID
	private String _uniqueid;
	
	/***
	 * Creates Entry Object by parsing the Node <contact>
	 * @param xmlNode
	 */
	public PhoneBookEntry() {
		
	}
	
	/***
	 * Parses the Object from xml node. 
	 * @param xmlNode needs to be node with <contact>...</contact>
	 * @return treu if successfully parsed
	 */
	public boolean parseFromNode(Node xmlNode) {
		boolean success = false;
		XPath xPath = XPathFactory.newInstance().newXPath();
		logger.debug(Helper.nodeToString(xmlNode));
		try {
			this._name = (String) xPath.evaluate("person/realName", xmlNode, XPathConstants.STRING);
			this._uniqueid = (String) xPath.evaluate("uniqueid", xmlNode, XPathConstants.STRING);
			this._businessTel = (String) xPath.evaluate("telephony/number[@type='work']", xmlNode, XPathConstants.STRING);
			this._privateTel = (String) xPath.evaluate("telephony/number[@type='home']", xmlNode, XPathConstants.STRING);
			this._mobileTel = (String) xPath.evaluate("telephony/number[@type='mobile']", xmlNode, XPathConstants.STRING);
			this._fax = (String) xPath.evaluate("telephony/number[@type='fax']", xmlNode, XPathConstants.STRING);
			// xpath is awesome :)
			
		} catch (XPathExpressionException e) {
			logger.error("Could not parse Phonebook Entry ", e);
		}
		//check if id could be parsed as success
		if(!this._uniqueid.isEmpty()){
			success = true;
		}
		else{
			logger.warn("Could not parse phone book entry: {}",Helper.nodeToString(xmlNode));
		}
		return success;
	}
	
	


	public String getPrivateTel() {
		return _privateTel;
	}

	public void setPrivateTel(String _privateTel) {
		this._privateTel = _privateTel;
	}

	public String getBusinessTel() {
		return _businessTel;
	}

	public void setBusinessTel(String _businessTel) {
		this._businessTel = _businessTel;
	}

	public String getMobileTel() {
		return _mobileTel;
	}

	public void setMobileTel(String _mobileTel) {
		this._mobileTel = _mobileTel;
	}

	public String getFax() {
		return _fax;
	}

	public void setFax(String _fax) {
		this._fax = _fax;
	}

	public String getPhoneBookName() {
		return _phoneBookName;
	}

	public void setPhoneBookName(String _phoneBookName) {
		this._phoneBookName = _phoneBookName;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public String getId() {
		return _uniqueid;
	}

	public void setId(String _id) {
		this._uniqueid = _id;
	}
	

	
	
	
}
