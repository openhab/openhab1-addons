/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import javax.xml.soap.SOAPMessage;

/***
 * 
 * @author gitbock
 * @since 1.8.0
 *
 */

public interface SoapValueParser {
	/***
	 * 
	 * @param sm soap message to parse
	 * @param mapping itemmap with information about all TR064 parameters
	 * @param request the raw original request which was used in itemconfig
	 * @return the value which was parsed from soap message
	 */
	String parseValueFromSoapMessage(SOAPMessage sm, ItemMap mapping, String request);

}
