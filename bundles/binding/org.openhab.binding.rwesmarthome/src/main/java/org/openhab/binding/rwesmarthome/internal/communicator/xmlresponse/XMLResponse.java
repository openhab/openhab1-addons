/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Base class to handle XML responses.
 * 
 * @author michael
 * 
 */
public class XMLResponse {

	/**
	 * Returns an integer from an xml element.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected int getIntValueFromElements(Element ele, String tagName) {
		return Integer.parseInt(getTextValueFromElements(ele, tagName));
	}

	/**
	 * Returns an integer from an xml attribute.
	 *  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected int getIntValueFromAttribute(Element ele, String tagName) {
		return Integer.parseInt(getTextValueFromAttribute(ele, tagName));
	}

	/**
	 * Returns a double value from an xml attribute.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected double getDoubleValueFromAttribute(Element ele, String tagName) {
		return Double.parseDouble(getTextValueFromAttribute(ele, tagName));
	}

	/**
	 * Returns a double value from an xml element.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected double getDoubleValueFromElements(Element ele, String tagName) {
		return Double.parseDouble(getTextValueFromElements(ele, tagName));
	}

	/**
	 * Returns a boolean value from an xml element.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected boolean getBooleanValueFromElements(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValueFromElements(ele, tagName));
	}

	/**
	 * Returns a boolean value from an xml attribute.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected Boolean getBooleanValueFromAttribute(Element ele, String tagName) {
		String textVal = getTextValueFromAttribute(ele, tagName);
		return Boolean.parseBoolean(textVal);
	}

	/**
	 * Returns a string from an xml element.
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected String getTextValueFromElements(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	/**
	 * Returns a string from an xml attribute.
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	protected String getTextValueFromAttribute(Element ele, String tagName) {
		String textVal = ele.getAttribute(tagName);
		return textVal;
	}
}
