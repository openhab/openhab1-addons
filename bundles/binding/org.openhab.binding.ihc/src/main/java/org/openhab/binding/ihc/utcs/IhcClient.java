/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.utcs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * IhcClient provides interface to communicate IHC / ELKO LS Controller.
 * 
 * Controller interface is SOAP web service based via HTTPS link.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public class IhcClient {

	/*
	 * If you wonder, why e.g. Axis(2) or JAX-WS is not used to handle SOAP
	 * interface...
	 * 
	 * WSDL files are included, so feel free to try ;)
	 */

	private enum ConnectionState {
		DISCONNECTED, CONNECTING, CONNECTED,
	}

	public class EnumValue {

		public int id;
		public String name;
	}

	public final static String CONTROLLER_STATE_READY = "text.ctrl.state.ready";
	public final static String CONTROLLER_STATE_INITIALIZE = "text.ctrl.state.initialize";

	private static final Logger logger = LoggerFactory
			.getLogger(IhcClient.class);

	static ConnectionState connState = ConnectionState.DISCONNECTED;
	static IhcHttpClient authclient = null;
	static List<String> cookies = null;

	String username = "";
	String password = "";
	String ip = "";
	int timeout = 5000; // milliseconds
	String projectFile = null;

	private Map<Integer, WSResourceValue> resourceValues = new HashMap<Integer, WSResourceValue>();

	private HashMap<Integer, ArrayList<EnumValue>> enumDictionary = new HashMap<Integer, ArrayList<EnumValue>>();

	public IhcClient() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTimeoutInMillisecods() {
		return timeout;
	}

	public void setTimeoutInMillisecods(int timeout) {
		this.timeout = timeout;
	}

	public String getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(String path) {
		this.projectFile = path;
	}

	private synchronized ConnectionState getConnectionState() {
		return connState;
	}

	private synchronized void setConnectionState(ConnectionState newState) {
		IhcClient.connState = newState;
	}

	/**
	 * Open connection and authenticate session to IHC / ELKO LS controller.
	 * 
	 * @return
	 */
	public void closeConnection() throws UnsupportedEncodingException,
			IOException {
		logger.debug("Close connection");

		setConnectionState(ConnectionState.DISCONNECTED);
	}

	/**
	 * Open connection and authenticate session to IHC / ELKO LS controller.
	 * 
	 * @return True is connection successfully opened.
	 */
	public synchronized boolean openConnection()
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		logger.debug("Open connection");

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
				+ "<soapenv:Body>"
				+ " <authenticate1 xmlns=\"utcs\">"
				+ "  <password>" + password + "</password>"
				+ "  <username>" + username + "</username>"
				+ "  <application>treeview</application>"
				+ " </authenticate1>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		setConnectionState(ConnectionState.CONNECTING);
		authclient = new IhcHttpClient();
		authclient.openConnection(
				"https://" + ip + "/ws/AuthenticationService", timeout);
		
		String response = authclient.sendQuery(soapQuery, timeout);

		String strLoginWasSuccessful = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginWasSuccessful");

		boolean loginWasSuccessfull = Boolean
				.parseBoolean(strLoginWasSuccessful);

		if (loginWasSuccessfull != true) {

			setConnectionState(ConnectionState.DISCONNECTED);

			String strLoginFailedDueToConnectionRestrictions = parseValue(
					response,
					"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToConnectionRestrictions");
			
			String strLoginFailedDueToInsufficientUserRights = parseValue(
					response,
					"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToInsufficientUserRights");
			
			String strLoginFailedDueToAccountInvalid = parseValue(
					response,
					"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToAccountInvalid");

			boolean loginFailedDueToConnectionRestrictions = Boolean
					.parseBoolean(strLoginFailedDueToConnectionRestrictions);
			
			if (loginFailedDueToConnectionRestrictions)
				throw new IOException(
						"login failed because of connection restrictions");

			boolean loginFailedDueToInsufficientUserRights = Boolean
					.parseBoolean(strLoginFailedDueToInsufficientUserRights);
			
			if (loginFailedDueToInsufficientUserRights)
				throw new IOException(
						"login failed because of insufficient user rights");

			boolean loginFailedDueToAccountInvalid = Boolean
					.parseBoolean(strLoginFailedDueToAccountInvalid);
			
			if (loginFailedDueToAccountInvalid)
				throw new IOException("login failed because of invalid account");
			else
				throw new IOException("login failed because of unknown reason");
		}

		logger.debug("Connection successfully opened");
		cookies = authclient.getCookies();

		WSControllerState status = queryControllerState();
		logger.debug("Controller state {}", status.getState());

		setConnectionState(ConnectionState.CONNECTED);

		return true;
	}

	/**
	 * Load IHC / ELKO LS project file.
	 * 
	 */
	public synchronized void loadProject() {

		Document doc = null;

		if (projectFile != null && projectFile != "") {

			logger.debug("Loading IHC /ELKO LS project file from path {}...",
					projectFile);

			try {

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(new File(projectFile));

			} catch (Exception e) {
				logger.error("Project file loading error", e);
			}

		} else {

			logger.debug("Loading IHC /ELKO LS project file from controller...");

			try {

				WSProjectInfo projectInfo = queryProjectInfo();
				int numberOfSegments = queryProjectNumberOfSegments();
				int segmentationSize = queryProjectSegmentationSize();

				logger.debug("Number of segments: {}", numberOfSegments);
				logger.debug("Segmentation size: {}", segmentationSize);

				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

				for (int i = 0; i < numberOfSegments; i++) {
					logger.debug("Downloading segment {}", i);

					WSFile data = queryProjectSegment(i,
							projectInfo.getProjectMajorRevision(),
							projectInfo.getProjectMinorRevision());
					byteStream.write(data.getData());
				}

				logger.debug("File size before base64 encoding: {} bytes",
						byteStream.size());

				byte[] decodedBytes = javax.xml.bind.DatatypeConverter
						.parseBase64Binary(byteStream.toString());

				logger.debug("File size after base64 encoding: {} bytes",
						decodedBytes.length);

				GZIPInputStream gzis = new GZIPInputStream(
						new ByteArrayInputStream(decodedBytes));

				InputStreamReader in = new InputStreamReader(gzis, "ISO-8859-1");
				InputSource reader = new InputSource(in);

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(reader);

			} catch (Exception e) {

				logger.error("Project file loading error", e);
			}
		}

		parseProject(doc);

	}

	/**
	 * Parse IHC / ELKO LS project file.
	 * 
	 */
	private void parseProject(Document doc) {

		logger.debug("Parsing project file...");

		enumDictionary.clear();

		NodeList nodes = doc.getElementsByTagName("enum_definition");

		// iterate enum definitions from project

		for (int i = 0; i < nodes.getLength(); i++) {

			Element element = (Element) nodes.item(i);

			// String enumName = element.getAttribute("name");
			int typedefId = Integer.parseInt(element.getAttribute("id")
					.replace("_0x", ""), 16);

			ArrayList<EnumValue> enumValues = new ArrayList<EnumValue>();

			NodeList name = element.getElementsByTagName("enum_value");

			for (int j = 0; j < name.getLength(); j++) {
				Element val = (Element) name.item(j);
				EnumValue enumVal = new EnumValue();
				enumVal.id = Integer.parseInt(
						val.getAttribute("id").replace("_0x", ""), 16);
				enumVal.name = val.getAttribute("name");

				enumValues.add(enumVal);
			}

			enumDictionary.put(typedefId, enumValues);
		}

	}

	/**
	 * Query controller current state.
	 * 
	 * @return controller's current state.
	 */
	public WSControllerState queryControllerState()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "getState");
		String response = sendQuery("ControllerService", soapQuery, timeout,
				listOfProperties);
		String state = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getState1/ns1:state");

		WSControllerState controllerState = new WSControllerState();
		controllerState.setState(state);
		return controllerState;
	}

	/**
	 * Wait controller state change notification.
	 * 
	 * @param previousState
	 *            Previous controller state.
	 * @param timeoutInSeconds
	 *            How many seconds to wait notifications.
	 * @return current controller state.
	 */
	public WSControllerState waitStateChangeNotifications(
			WSControllerState previousState, int timeoutInSeconds)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ " <ns1:waitForControllerStateChange1 xmlns:ns1=\"utcs\" xsi:type=\"ns1:WSControllerState\">"
				+ "  <ns1:state xsi:type=\"xsd:string\">" + previousState.getState() + "</ns1:state>"
				+ " </ns1:waitForControllerStateChange1>"
				+ " <ns2:waitForControllerStateChange2 xmlns:ns2=\"utcs\" xsi:type=\"xsd:int\">" + timeoutInSeconds + "</ns2:waitForControllerStateChange2>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "waitForControllerStateChange");
		
		String response = sendQuery("ControllerService", soapQuery,
				timeoutInSeconds * 1000 + timeout, listOfProperties);

		String state = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:waitForControllerStateChange3/ns1:state");

		WSControllerState controllerState = new WSControllerState();
		controllerState.setState(state);

		return controllerState;
	}

	/**
	 * Query project information from the controller.
	 * 
	 * @return project information.
	 */
	public WSProjectInfo queryProjectInfo()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		
		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "getProjectInfo");
		String response = sendQuery("ControllerService", soapQuery, timeout,
				listOfProperties);

		WSProjectInfo projectInfo = new WSProjectInfo();
		String value = "";

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:visualMinorVersion");
		projectInfo.setVisualMinorVersion(Integer.parseInt(value));

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:visualMajorVersion");
		projectInfo.setVisualMajorVersion(Integer.parseInt(value));

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:projectMajorRevision");
		projectInfo.setProjectMajorRevision(Integer.parseInt(value));

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:projectMinorRevision");
		projectInfo.setProjectMinorRevision(Integer.parseInt(value));

		WSDate lastmodified = new WSDate();

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:day");
		lastmodified.setDay(Integer.parseInt(value));

		value = parseValue(
				response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:monthWithJanuaryAsOne");
		lastmodified.setMonthWithJanuaryAsOne(Integer.parseInt(value));

		value = parseValue(
				response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:hours");
		lastmodified.setHours(Integer.parseInt(value));

		value = parseValue(
				response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:minutes");
		lastmodified.setMinutes(Integer.parseInt(value));

		value = parseValue(
				response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:seconds");
		lastmodified.setSeconds(Integer.parseInt(value));

		value = parseValue(
				response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:lastmodified/ns1:year");
		lastmodified.setYear(Integer.parseInt(value));

		projectInfo.setLastmodified(lastmodified);

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:projectNumber");
		projectInfo.setProjectNumber(value);

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:customerName");
		projectInfo.setCustomerName(value);

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getProjectInfo1/ns1:installerName");
		projectInfo.setInstallerName(value);

		return projectInfo;
	}

	/**
	 * Query number of segments project contains.
	 * 
	 * @return number of segments.
	 */
	private int queryProjectNumberOfSegments()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		
		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "getIHCProjectNumberOfSegments");
		
		String response = sendQuery("ControllerService", soapQuery, timeout,
				listOfProperties);

		String numberOfSegments = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectNumberOfSegments1");

		return Integer.parseInt(numberOfSegments);
	}

	/**
	 * Query project segment size.
	 * 
	 * @return segments size.
	 */
	private int queryProjectSegmentationSize()
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "getIHCProjectSegmentationSize");
		String response = sendQuery("ControllerService", soapQuery, timeout,
				listOfProperties);

		String numberOfSegments = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegmentationSize1");

		return Integer.parseInt(numberOfSegments);
	}

	/**
	 * Query project segment data.
	 * 
	 * @param index
	 *            segments index.
	 * @param major
	 *            project major revision number.
	 * @param minor
	 *            project minor revision number.
	 * @return segments data.
	 */

	public WSFile queryProjectSegment(int index, int major, int minor)
			throws UnsupportedEncodingException, XPathExpressionException,
			IOException {
		
		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <ns1:getIHCProjectSegment1 xmlns:ns1=\"utcs\" xsi:type=\"xsd:int\">" + index + "</ns1:getIHCProjectSegment1>"
				+ " <ns2:getIHCProjectSegment2 xmlns:ns2=\"utcs\" xsi:type=\"xsd:int\">" + major + "</ns2:getIHCProjectSegment2>"
				+ " <ns3:getIHCProjectSegment3 xmlns:ns3=\"utcs\" xsi:type=\"xsd:int\">" + minor + "</ns3:getIHCProjectSegment3>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		Map<String, String> listOfProperties = new HashMap<String, String>();
		listOfProperties.put("SOAPAction", "getIHCProjectSegment");
		String response = sendQuery("ControllerService", soapQuery, timeout,
				listOfProperties);

		WSFile file = new WSFile();
		String value = "";

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegment4/ns1:filename");
		file.setFilename(value);

		value = parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegment4/ns1:data");
		file.setData(value.getBytes());

		return file;
	}

	/**
	 * Returns all possible enumerated values for corresponding enum type.
	 * 
	 * @param typedefId
	 *            Enum type definition identifier.
	 * @return list of enum values.
	 */
	public ArrayList<IhcClient.EnumValue> getEnumValues(int typedefId) {

		return enumDictionary.get(typedefId);
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
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<enableRuntimeValueNotifications1 xmlns=\"utcs\">";

		String soapQuerySuffix = "</enableRuntimeValueNotifications1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		for (int i : resourceIdList) {
			soapQuery += "<xsd:arrayItem>" + i + "</xsd:arrayItem>";
		}

		soapQuery += soapQuerySuffix;

		sendQuery("ResourceInteractionService", soapQuery, timeout, null);
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
	 */
	public List<? extends WSResourceValue> waitResourceValueNotifications(
			int timeoutInSeconds) throws UnsupportedEncodingException,
			IOException, XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:utcs=\"utcs\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ " <utcs:waitForResourceValueChanges1>" + timeoutInSeconds + "</utcs:waitForResourceValueChanges1>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		List<WSResourceValue> resourceValueList = new ArrayList<WSResourceValue>();

		String response = sendQuery("ResourceInteractionService", soapQuery,
				timeoutInSeconds * 1000 + timeout, null);

		NodeList nodeList = parseList(
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
			resourceValues.put(newVal.getResourceID(), newVal);
		}

		return resourceValueList;
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
			throws UnsupportedEncodingException, IOException,
			NumberFormatException, XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>"
				+ " <ns1:getRuntimeValue1 xmlns:ns1=\"utcs\">" + String.valueOf(resoureId) + "</ns1:getRuntimeValue1>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		String response = sendQuery("ResourceInteractionService", soapQuery,
				timeout, null);

		NodeList nodeList = parseList(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getRuntimeValue2");

		if (nodeList.getLength() == 1) {

			WSResourceValue val = parseResourceValue(nodeList.item(0), 2);

			if (val.resourceID == resoureId) {

				return val;

			} else {
				throw new IllegalArgumentException("No resource id found");

			}
		} else {

			throw new IllegalArgumentException("No resource value found");
		}

	}

	/**
	 * Get resource value information.
	 * 
	 * Function return resource value from internal memory, if data is not
	 * available information is read from the controller.
	 * 
	 * Resource value's value field (e.g. floatingPointValue) could be old
	 * information.
	 * 
	 * @param resoureId
	 *            Resource Identifier.
	 * @return Resource value.
	 */
	public WSResourceValue getResourceValueInformation(int resourceId)
			throws NumberFormatException, UnsupportedEncodingException,
			XPathExpressionException, IOException {

		WSResourceValue data = resourceValues.get(resourceId);

		if (data == null) {

			// data is not available, read it from the controller

			data = resourceQuery(resourceId);
		}

		return data;
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
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {
		boolean retval = false;

		if (value instanceof WSFloatingPointValue)
			retval = resourceUpdate((WSFloatingPointValue) value);

		else if (value instanceof WSBooleanValue)
			retval = resourceUpdate((WSBooleanValue) value);

		else if (value instanceof WSIntegerValue)
			retval = resourceUpdate((WSIntegerValue) value);

		else if (value instanceof WSTimerValue)
			retval = resourceUpdate((WSTimerValue) value);

		else if (value instanceof WSWeekdayValue)
			retval = resourceUpdate((WSWeekdayValue) value);

		else if (value instanceof WSEnumValue)
			retval = resourceUpdate((WSEnumValue) value);

		else if (value instanceof WSTimeValue)
			retval = resourceUpdate((WSTimeValue) value);

		else if (value instanceof WSDateValue)
			retval = resourceUpdate((WSDateValue) value);

		else
			throw new IllegalArgumentException("Unsupported value type"
					+ value.getClass().toString());

		return retval;
	}

	private String sendQuery(String service, String data,
			int timeoutInMilliseconds, Map<String, String> listOfProperties)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		if (getConnectionState() == ConnectionState.DISCONNECTED) {
			openConnection();
		}

		String url = "https://" + ip + "/ws/" + service;

		IhcHttpClient queryclient = new IhcHttpClient();
		queryclient.openConnection(url, timeout);
		queryclient.setCookies(cookies);
		String response = null;

		if (listOfProperties != null) {
			queryclient.setRequestProperties(listOfProperties);
		}

		try {
			response = queryclient.sendQuery(data, timeoutInMilliseconds);

			List<String> c = queryclient.getCookies();
			if (c != null)
				cookies = c;

		} catch (SocketTimeoutException e2) {
			throw e2;
		} catch (IOException e1) {
			openConnection();
			queryclient = new IhcHttpClient();
			queryclient.openConnection(url, timeout); // use global timeout for
														// connection
			queryclient.setCookies(cookies);

			if (listOfProperties != null) {
				queryclient.setRequestProperties(listOfProperties);
			}

			response = queryclient.sendQuery(data, timeoutInMilliseconds);

			List<String> c = queryclient.getCookies();
			if (c != null)
				cookies = c;
		}

		if (response != null && response != "")
			return response;
		else
			throw new IOException("Illegal response");
	}

	private boolean resourceUpdate(WSBooleanValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSBooleanValue\">"
				+ "   <q1:value>" + (value.isValue() ? "true" : "false") + "</q1:value>"
				+ "  </value>"
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSFloatingPointValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSFloatingPointValue\">"
				+ "   <q1:maximumValue>" + value.getMaximumValue() + "</q1:maximumValue>"
				+ "   <q1:minimumValue>" + value.getMinimumValue() + "</q1:minimumValue>"
				+ "   <q1:floatingPointValue>" + value.getFloatingPointValue() + "</q1:floatingPointValue>"
				+ "  </value>"
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSIntegerValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSIntegerValue\">"
				+ "   <q1:maximumValue>" + value.getMaximumValue() + "</q1:maximumValue>"
				+ "   <q1:minimumValue>" + value.getMinimumValue() + "</q1:minimumValue>"
				+ "   <q1:integer>" + value.getInteger() + "</q1:integer>"
				+ "  </value>"
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSTimerValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSTimerValue\">"
				+ "   <q1:milliseconds>" + value.getMilliseconds() + "</q1:milliseconds>"
				+ "  </value>"
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSWeekdayValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSWeekdayValue\">"
				+ "   <q1:weekdayNumber>" + value.getWeekdayNumber() + "</q1:weekdayNumber>" 
				+ "  </value>" 
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSEnumValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSEnumValue\">"
				+ "   <q1:definitionTypeID>" + value.getDefinitionTypeID() + "</q1:definitionTypeID>" 
				+ "   <q1:enumValueID>" + value.getEnumValueID() + "</q1:enumValueID>"
				+ "   <q1:enumName>" + value.getEnumName() + "</q1:enumName>"
				+ "  </value>" 
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSTimeValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSTimeValue\">"
				+ "   <q1:hours>" + value.getHours() + "</q1:hours>"
				+ "   <q1:minutes>" + value.getMinutes() + "</q1:minutes>"
				+ "   <q1:seconds>" + value.getSeconds() + "</q1:seconds>"
				+ "  </value>" 
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>" 
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean resourceUpdate(WSDateValue value)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <setResourceValue1 xmlns=\"utcs\">"
				+ "  <value xmlns:q1=\"utcs.values\" xsi:type=\"q1:WSDateValue\">"
				+ "   <q1:month>" + value.getMonth() + "</q1:month>"
				+ "   <q1:year>" + value.getYear() + "</q1:year>"
				+ "   <q1:day>" + value.getDay() + "</q1:day>" 
				+ "  </value>"
				+ "  <resourceID>" + value.getResourceID() + "</resourceID>"
				+ "  <isValueRuntime>true</isValueRuntime>"
				+ " </setResourceValue1>" 
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		return doResourceUpdate(soapQuery);
	}

	private boolean doResourceUpdate(String data)
			throws UnsupportedEncodingException, IOException,
			XPathExpressionException {

		String response = sendQuery("ResourceInteractionService", data,
				timeout, null);

		return Boolean.parseBoolean(parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:setResourceValue2"));
	}

	private WSResourceValue parseResourceValue(Node n, int index)
			throws XPathExpressionException {

		// parse resource id
		String resourceId = getValue(n, "ns1:resourceID");

		if (resourceId != null && resourceId != "") {

			int id = Integer.parseInt(resourceId);

			// Parse floating point value

			String value = getValue(n, "ns1:value/ns" + index
					+ ":floatingPointValue");
			if (value != null && value != "") {
				WSFloatingPointValue val = new WSFloatingPointValue();
				val.setResourceID(id);
				val.setFloatingPointValue(Double.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":maximumValue");
				if (value != null && value != "")
					val.setMaximumValue(Double.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":minimumValue");
				if (value != null && value != "")
					val.setMinimumValue(Double.valueOf(value));

				return val;
			}

			// Parse boolean value

			value = getValue(n, "ns1:value/ns" + index + ":value");
			if (value != null && value != "") {
				WSBooleanValue val = new WSBooleanValue();
				val.setResourceID(id);
				val.setValue(Boolean.valueOf(value));
				return val;
			}

			// Parse integer value

			value = getValue(n, "ns1:value/ns" + index + ":integer");
			if (value != null && value != "") {
				WSIntegerValue val = new WSIntegerValue();
				val.setResourceID(id);
				val.setInteger(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":maximumValue");
				if (value != null && value != "")
					val.setMaximumValue(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":minimumValue");
				if (value != null && value != "")
					val.setMinimumValue(Integer.valueOf(value));

				return val;
			}

			// Parse timer value

			value = getValue(n, "ns1:value/ns" + index + ":milliseconds");
			if (value != null && value != "") {
				WSTimerValue val = new WSTimerValue();
				val.setResourceID(id);
				val.setMilliseconds(Integer.valueOf(value));

				return val;
			}

			// Parse time value

			value = getValue(n, "ns1:value/ns" + index + ":hours");
			if (value != null && value != "") {
				WSTimeValue val = new WSTimeValue();
				val.setResourceID(id);
				val.setHours(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":minutes");
				if (value != null && value != "")
					val.setMinutes(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":seconds");
				if (value != null && value != "")
					val.setSeconds(Integer.valueOf(value));

				return val;
			}

			// Parse date value

			value = getValue(n, "ns1:value/ns" + index + ":day");
			if (value != null && value != "") {
				WSDateValue val = new WSDateValue();
				val.setResourceID(id);
				val.setDay(Byte.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":month");
				if (value != null && value != "")
					val.setMonth(Byte.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":year");
				if (value != null && value != "")
					val.setYear(Short.valueOf(value));

				return val;
			}

			// Parse enum value

			value = getValue(n, "ns1:value/ns" + index + ":definitionTypeID");
			if (value != null && value != "") {
				WSEnumValue val = new WSEnumValue();
				val.setResourceID(id);
				val.setDefinitionTypeID(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":enumValueID");
				if (value != null && value != "")
					val.setEnumValueID(Integer.valueOf(value));

				value = getValue(n, "ns1:value/ns" + index + ":enumName");
				if (value != null && value != "")
					val.setEnumName(value);

				return val;
			}

			// Parse week day value

			value = getValue(n, "ns1:value/ns" + index + ":weekdayNumber");
			if (value != null && value != "") {
				WSWeekdayValue val = new WSWeekdayValue();
				val.setResourceID(id);
				val.setWeekdayNumber(Integer.valueOf(value));

				return val;
			}

			throw new IllegalArgumentException("Unsupported value type");
		}

		return null;

	}

	private String parseValue(String xml, String xpathExpression)
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

		return (String) xpath.evaluate(xpathExpression, inputSource,
				XPathConstants.STRING);
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

	private String getValue(Node n, String expr)
			throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if (prefix == null)
					throw new NullPointerException("Null prefix");
				else if ("SOAP-ENV".equals(prefix))
					return "http://schemas.xmlsoap.org/soap/envelope/";
				else if ("ns1".equals(prefix))
					return "utcs";
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

}