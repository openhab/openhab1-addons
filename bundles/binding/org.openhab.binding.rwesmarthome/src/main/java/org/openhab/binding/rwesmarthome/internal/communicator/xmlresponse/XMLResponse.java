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
*
* @author michael
*/
public class XMLResponse {

   /**
    * Calls getTextValue and returns a int value
    */
   protected int getIntValueFromElements(Element ele, String tagName) {
       //in production application you would catch the exception
       return Integer.parseInt(getTextValueFromElements(ele, tagName));
   }

   protected int getIntValueFromAttribute(Element ele, String tagName) {
       //in production application you would catch the exception
       return Integer.parseInt(getTextValueFromAttribute(ele, tagName));
   }
   protected double getDoubleValueFromAttribute(Element ele, String tagName) {
       //in production application you would catch the exception
       return Double.parseDouble(getTextValueFromAttribute(ele, tagName));
   }    
   protected double getDoubleValueFromElements(Element ele, String tagName) {
       //in production application you would catch the exception
       return Double.parseDouble(getTextValueFromElements(ele, tagName));
   }    
   protected boolean getBooleanValueFromElements(Element ele, String tagName) {
       //in production application you would catch the exception
       return Boolean.parseBoolean(getTextValueFromElements(ele, tagName));
   }    
   protected Boolean getBooleanValueFromAttribute(Element ele, String tagName) {
       String textVal = getTextValueFromAttribute(ele, tagName);
       return Boolean.parseBoolean(textVal);
   }
   protected String getTextValueFromElements(Element ele, String tagName) {
       String textVal = null;
       NodeList nl = ele.getElementsByTagName(tagName);
       if (nl != null && nl.getLength() > 0) {
           Element el = (Element) nl.item(0);
           textVal = el.getFirstChild().getNodeValue();
       }
       return textVal;
   }

   protected String getTextValueFromAttribute(Element ele, String tagName) {
       String textVal = ele.getAttribute(tagName);
       return textVal;
   }
}

