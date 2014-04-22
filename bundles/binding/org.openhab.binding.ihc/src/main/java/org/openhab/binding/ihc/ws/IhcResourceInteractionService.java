/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ihc.ws.datatypes.WSBaseDataType;
import org.openhab.binding.ihc.ws.datatypes.WSBooleanValue;
import org.openhab.binding.ihc.ws.datatypes.WSDateValue;
import org.openhab.binding.ihc.ws.datatypes.WSEnumValue;
import org.openhab.binding.ihc.ws.datatypes.WSFloatingPointValue;
import org.openhab.binding.ihc.ws.datatypes.WSIntegerValue;
import org.openhab.binding.ihc.ws.datatypes.WSResourceValue;
import org.openhab.binding.ihc.ws.datatypes.WSTimeValue;
import org.openhab.binding.ihc.ws.datatypes.WSTimerValue;
import org.openhab.binding.ihc.ws.datatypes.WSWeekdayValue;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Class to handle IHC / ELKO LS Controller's resource interaction service.
 * 
 * Service is used to fetch or update resource values from/to controller.
 * 
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class IhcResourceInteractionService extends IhcHttpsClient {
	
	private String url;
	List<String> cookies;
	
	IhcResourceInteractionService(String host) {
		url = "https://" + host + "/ws/ResourceInteractionService";
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * Query resource value from controller.
	 * 
	 * 
	 * @param resoureId
	 *            Resource Identifier.
	 * @return Resource value.
	 */
	public WSResourceValue resourceQuery(int resoureId)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ " <ns1:getRuntimeValue1 xmlns:ns1=\"utcs\">%s</ns1:getRuntimeValue1>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		String query = String.format(soapQuery, String.valueOf(resoureId));

		openConnection(url);
		super.setCookies(cookies);
		String response = sendQuery(query);
		closeConnection();

		NodeList nodeList;
		try {
			nodeList = parseList(response,
					"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getRuntimeValue2");
		
			if (nodeList.getLength() == 1) {

				WSResourceValue val = parseResourceValue(nodeList.item(0), 2);

				if (val.getResourceID() == resoureId) {
					return val;
				} else {
					throw new IhcExecption("No resource id found");
				}
				
			} else {
				throw new IhcExecption("No resource value found");
			}
		
		} catch (XPathExpressionException e) {
			throw new IhcExecption(e);
		} catch (UnsupportedEncodingException e) {
			throw new IhcExecption(e);
		}
	}

	private NodeList parseList(String xml, String xpathExpression)
			throws XPathExpressionException, UnsupportedEncodingException {
		InputStream is = new ByteArrayInputStream(xml.getBytes("UTF8"));
		XPath xpath = XPathFactory.newInstance().newXPath();
		InputSource inputSource = new InputSource(is);

		xpath.setNamespaceContext(new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if (prefix == null)
					throw new NullPointerException("Null prefix");
				else if ("SOAP-ENV".equals(prefix))
					return "http://schemas.xmlsoap.org/soap/envelope/";
				else if ("ns1".equals(prefix))
					return "utcs";
				else if ("ns2".equals(prefix))
					return "utcs.values";
				return null;
			}

			public String getPrefix(String uri) {
				return null;
			}

			@SuppressWarnings("rawtypes")
			public Iterator getPrefixes(String uri) {
				throw new UnsupportedOperationException();
			}
		});

		return (NodeList) xpath.evaluate(xpathExpression, inputSource,
				XPathConstants.NODESET);
	}

	private WSResourceValue parseResourceValue(Node n, int index)
			throws XPathExpressionException {

		// parse resource id
		String resourceId = getValue(n, "ns1:resourceID");

		if (StringUtils.isNotBlank(resourceId)) {

			int id = Integer.parseInt(resourceId);

			// Parse floating point value

			String value = getValue(n, "ns1:value/ns" + index
					+ ":floatingPointValue");
			
			if (StringUtils.isNotBlank(value)) {
				
				WSFloatingPointValue val = new WSFloatingPointValue();
				val.setResourceID(id);
				val.setFloatingPointValue(Double.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":maximumValue");
				if (StringUtils.isNotBlank(value)) {
					val.setMaximumValue(Double.valueOf(value));
				}
				
				value = getValue(n, "ns1:value/ns" + index + ":minimumValue");
				if (StringUtils.isNotBlank(value)) {
					val.setMinimumValue(Double.valueOf(value));
				}
				
				return val;
			}

			// Parse boolean value

			value = getValue(n, "ns1:value/ns" + index + ":value");
			if (StringUtils.isNotBlank(value)) {
				WSBooleanValue val = new WSBooleanValue();
				val.setResourceID(id);
				val.setValue(Boolean.valueOf(value));
				return val;
			}

			// Parse integer value

			value = getValue(n, "ns1:value/ns" + index + ":integer");
			if (StringUtils.isNotBlank(value)) {
				
				WSIntegerValue val = new WSIntegerValue();
				val.setResourceID(id);
				val.setInteger(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":maximumValue");
				if (StringUtils.isNotBlank(value)) {
					val.setMaximumValue(Integer.valueOf(value));
				}
				
				value = getValue(n, "ns1:value/ns" + index + ":minimumValue");
				if (StringUtils.isNotBlank(value)) {
					val.setMinimumValue(Integer.valueOf(value));
				}
				
				return val;
			}

			// Parse timer value

			value = getValue(n, "ns1:value/ns" + index + ":milliseconds");
			if (StringUtils.isNotBlank(value)) {
				WSTimerValue val = new WSTimerValue();
				val.setResourceID(id);
				val.setMilliseconds(Integer.valueOf(value));

				return val;
			}

			// Parse time value

			value = getValue(n, "ns1:value/ns" + index + ":hours");
			if (StringUtils.isNotBlank(value)) {
				
				WSTimeValue val = new WSTimeValue();
				val.setResourceID(id);
				val.setHours(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":minutes");
				if (StringUtils.isNotBlank(value)) {
					val.setMinutes(Integer.valueOf(value));
				}
				
				value = getValue(n, "ns1:value/ns" + index + ":seconds");
				if (StringUtils.isNotBlank(value)) {
					val.setSeconds(Integer.valueOf(value));
				}
				
				return val;
			}

			// Parse date value

			value = getValue(n, "ns1:value/ns" + index + ":day");
			if (StringUtils.isNotBlank(value)) {
				
				WSDateValue val = new WSDateValue();
				val.setResourceID(id);
				val.setDay(Byte.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":month");
				if (StringUtils.isNotBlank(value)) {
					val.setMonth(Byte.valueOf(value));
				}
				
				value = getValue(n, "ns1:value/ns" + index + ":year");
				if (StringUtils.isNotBlank(value)) {
					val.setYear(Short.valueOf(value));
				}
				
				return val;
			}

			// Parse enum value

			value = getValue(n, "ns1:value/ns" + index + ":definitionTypeID");
			if (StringUtils.isNotBlank(value)) {
				
				WSEnumValue val = new WSEnumValue();
				val.setResourceID(id);
				val.setDefinitionTypeID(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":enumValueID");
				if (StringUtils.isNotBlank(value)) {
					val.setEnumValueID(Integer.valueOf(value));
				}
				
				value = getValue(n, "ns1:value/ns" + index + ":enumName");
				if (StringUtils.isNotBlank(value)) {
					val.setEnumName(value);
				}
				
				return val;
			}

			// Parse week day value

			value = getValue(n, "ns1:value/ns" + index + ":weekdayNumber");
			if (StringUtils.isNotBlank(value)) {
				WSWeekdayValue val = new WSWeekdayValue();
				val.setResourceID(id);
				val.setWeekdayNumber(Integer.valueOf(value));

				return val;
			}

			throw new IllegalArgumentException("Unsupported value type");
		}

		return null;

	}

	private String getValue(Node n, String expr)
			throws XPathExpressionException {
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			
			public String getNamespaceURI(String prefix) {
				
				if (prefix == null) {
					throw new NullPointerException("Null prefix");
				} else if ("SOAP-ENV".equals(prefix)) {
					return "http://schemas.xmlsoap.org/soap/envelope/";
				} else if ("ns1".equals(prefix)) {
					return "utcs";
				}
				// else if ("ns2".equals(prefix)) return "utcs.values";
				return "utcs.values";
				// return null;
			}

			public String getPrefix(String uri) {
				return null;
			}

			@SuppressWarnings("rawtypes")
			public Iterator getPrefixes(String uri) {
				throw new UnsupportedOperationException();
			}
		});

		XPathExpression pathExpr = xpath.compile(expr);
		return (String) pathExpr.evaluate(n, XPathConstants.STRING);
	}

	/**
	 * Update resource value to controller.
	 * 
	 * 
	 * @param value
	 *            Resource value.
	 * @return True if value is successfully updated.
	 */
	public boolean resourceUpdate(WSResourceValue value)
			throws IhcExecption {
		
		boolean retval = false;

		if (value instanceof WSFloatingPointValue) {
			retval = resourceUpdate((WSFloatingPointValue) value);
		}
		
		else if (value instanceof WSBooleanValue) {
			retval = resourceUpdate((WSBooleanValue) value);
		}
		
		else if (value instanceof WSIntegerValue) {
			retval = resourceUpdate((WSIntegerValue) value);
		}
		
		else if (value instanceof WSTimerValue) {
			retval = resourceUpdate((WSTimerValue) value);
		}
		
		else if (value instanceof WSWeekdayValue) {
			retval = resourceUpdate((WSWeekdayValue) value);
		}
		
		else if (value instanceof WSEnumValue) {
			retval = resourceUpdate((WSEnumValue) value);
		}
		
		else if (value instanceof WSTimeValue) {
			retval = resourceUpdate((WSTimeValue) value);
		}
		
		else if (value instanceof WSDateValue) {
			retval = resourceUpdate((WSDateValue) value);
		}
		
		else {
			throw new IhcExecption("Unsupported value type "
					+ value.getClass().toString());
		}
		
		return retval;
	}

	public boolean resourceUpdate(WSBooleanValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSBooleanValue\">"
				+ "   <q1:value>%s</q1:value>"
				+ "  </value>"
				+ "  <resourceID>%s</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.isValue() ? "true" : "false", value.getResourceID());
		
		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSFloatingPointValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSFloatingPointValue\">"
				+ "   <q1:maximumValue>%s</q1:maximumValue>"
				+ "   <q1:minimumValue>%s</q1:minimumValue>"
				+ "   <q1:floatingPointValue>%s</q1:floatingPointValue>"
				+ "  </value>"
				+ "  <resourceID>%s</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getMaximumValue(), value.getMinimumValue(), value.getFloatingPointValue(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSIntegerValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSIntegerValue\">"
				+ "   <q1:maximumValue>%s</q1:maximumValue>"
				+ "   <q1:minimumValue>%s</q1:minimumValue>"
				+ "   <q1:integer>%s</q1:integer>"
				+ "  </value>"
				+ "  <resourceID>%s</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getMaximumValue(), value.getMinimumValue(), value.getInteger(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSTimerValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSTimerValue\">"
				+ "   <q1:milliseconds>%s</q1:milliseconds>"
				+ "  </value>"
				+ "  <resourceID>%s</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getMilliseconds(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSWeekdayValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSWeekdayValue\">"
				+ "   <q1:weekdayNumber>%s</q1:weekdayNumber>" 
				+ "  </value>" 
				+ "  <resourceID>%s</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getWeekdayNumber(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSEnumValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSEnumValue\">"
				+ "   <q1:definitionTypeID>%s</q1:definitionTypeID>" 
				+ "   <q1:enumValueID>%s</q1:enumValueID>"
				+ "   <q1:enumName>%s</q1:enumName>"
				+ "  </value>" 
				+ "  <resourceID>%s</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getDefinitionTypeID(),  value.getEnumValueID(), value.getEnumName(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSTimeValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSTimeValue\">"
				+ "   <q1:hours>%s</q1:hours>"
				+ "   <q1:minutes>%s</q1:minutes>"
				+ "   <q1:seconds>%s</q1:seconds>"
				+ "  </value>" 
				+ "  <resourceID>%s</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getHours(),  value.getMinutes(), value.getSeconds(), value.getResourceID());

		return doResourceUpdate(query);
	}

	public boolean resourceUpdate(WSDateValue value)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSDateValue\">"
				+ "   <q1:month>%s</q1:month>"
				+ "   <q1:year>%s</q1:year>"
				+ "   <q1:day>%s</q1:day>" 
				+ "  </value>"
				+ "  <resourceID>%s</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, value.getMonth(),  value.getYear(), value.getDay(), value.getResourceID());

		return doResourceUpdate(query);
	}

	private boolean doResourceUpdate(String query)
			throws IhcExecption {

		openConnection(url);
		super.setCookies(cookies);
		String response = sendQuery(query);

		return Boolean.parseBoolean(WSBaseDataType.parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:setResourceValue2"));
	}
	
	/**
	 * Enable resources runtime value notifications.
	 * 
	 * @param resourceIdList
	 *            List of resource Identifiers.
	 * @return True is connection successfully opened.
	 */
	public void enableRuntimeValueNotifications(
			List<? extends Integer> resourceIdList)
			throws IhcExecption {

		final String soapQueryPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<enableRuntimeValueNotifications1 xmlns=\"utcs\">";

		final String soapQuerySuffix = "</enableRuntimeValueNotifications1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = soapQueryPrefix;
		for (int i : resourceIdList) {
			query += "<xsd:arrayItem>" + i + "</xsd:arrayItem>";
		}
		query += soapQuerySuffix;

		openConnection(url);
		super.setCookies(cookies);
		@SuppressWarnings("unused")
		String response = sendQuery(query);
		closeConnection();
	}

	/**
	 * Wait runtime value notifications.
	 * 
	 * Runtime value notification should firstly be activated by
	 * enableRuntimeValueNotifications function.
	 * 
	 * @param timeoutInSeconds
	 *            How many seconds to wait notifications.
	 * @return List of received runtime value notifications.
	 * @throws SocketTimeoutException 
	 */
	public List<? extends WSResourceValue> waitResourceValueNotifications(
			int timeoutInSeconds) throws IhcExecption, SocketTimeoutException {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:utcs=\"utcs\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ " <utcs:waitForResourceValueChanges1>%s</utcs:waitForResourceValueChanges1>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		String query = String.format(soapQuery, timeoutInSeconds);
		openConnection(url);
		super.setCookies(cookies);
		setTimeout(getTimeout() + timeoutInSeconds * 1000);
		String response = sendQuery(query);
		closeConnection();
		
		List<WSResourceValue> resourceValueList = new ArrayList<WSResourceValue>();

		NodeList nodeList;
		try {
			nodeList = parseList(
					response,
					"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:waitForResourceValueChanges2/ns1:arrayItem");
			
			if (nodeList.getLength() == 1) {
				String resourceId = getValue(nodeList.item(0), "ns1:resourceID");
				if (resourceId == null || resourceId == "") {
					throw new SocketTimeoutException();
				}
			}

			for (int i = 0; i < nodeList.getLength(); i++) {

				int index = i + 2;

				WSResourceValue newVal = parseResourceValue(nodeList.item(i), index);
				resourceValueList.add(newVal);
			}
			
			return resourceValueList;

		} catch (XPathExpressionException e) {
			throw new IhcExecption(e);
		} catch (UnsupportedEncodingException e) {
			throw new IhcExecption(e);
		}
	}

}
