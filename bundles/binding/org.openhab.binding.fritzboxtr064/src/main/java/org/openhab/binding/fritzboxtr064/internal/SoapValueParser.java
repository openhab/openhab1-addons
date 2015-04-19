package org.openhab.binding.fritzboxtr064.internal;

import javax.xml.soap.SOAPMessage;

public interface SoapValueParser {
	String parseValueFromSoapMessage(SOAPMessage sm, ItemMap mapping);

}
