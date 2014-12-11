/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.util.List;

import org.openhab.binding.ihc.ws.datatypes.WSBaseDataType;
import org.openhab.binding.ihc.ws.datatypes.WSControllerState;
import org.openhab.binding.ihc.ws.datatypes.WSFile;
import org.openhab.binding.ihc.ws.datatypes.WSProjectInfo;

/**
 * Class to handle IHC / ELKO LS Controller's controller service.
 * 
 * Controller service is used to fetch information from the controller.
 * E.g. Project file or controller status.
 * 
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class IhcControllerService extends IhcHttpsClient {

	private static String emptyQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
			+ "<soapenv:Body>"
			+ "</soapenv:Body>"
			+ "</soapenv:Envelope>";

	private String url;
	List<String> cookies;
	
	IhcControllerService(String host) {
		url = "https://" + host + "/ws/ControllerService";
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * Query project information from the controller.
	 * 
	 * @return project information.
	 * @throws IhcExecption 
	 */
	public synchronized WSProjectInfo getProjectInfo() throws IhcExecption {
		
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "getProjectInfo");
		String response = sendQuery(emptyQuery);
		closeConnection();
		WSProjectInfo projectInfo = new WSProjectInfo();
		projectInfo.encodeData(response);
		return projectInfo;
	}
	
	/**
	 * Query number of segments project contains.
	 * 
	 * @return number of segments.
	 */
	public synchronized int getProjectNumberOfSegments() throws IhcExecption {
		
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "getIHCProjectNumberOfSegments");
		String response = sendQuery(emptyQuery);

		String numberOfSegments = WSBaseDataType.parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectNumberOfSegments1");

		closeConnection();
		return Integer.parseInt(numberOfSegments);
	}

	/**
	 * Query segmentation size.
	 * 
	 * @return segmentation size in bytes.
	 */
	public synchronized int getProjectSegmentationSize() throws IhcExecption {
		
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "getIHCProjectSegmentationSize");
		String response = sendQuery(emptyQuery);

		String segmentationSize = WSBaseDataType.parseValue(response,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegmentationSize1");

		closeConnection();
		return Integer.parseInt(segmentationSize);
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

	public synchronized WSFile getProjectSegment(int index, int major, int minor)
			throws IhcExecption {
		
		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ " <ns1:getIHCProjectSegment1 xmlns:ns1=\"utcs\" xsi:type=\"xsd:int\">%s</ns1:getIHCProjectSegment1>"
				+ " <ns2:getIHCProjectSegment2 xmlns:ns2=\"utcs\" xsi:type=\"xsd:int\">%s</ns2:getIHCProjectSegment2>"
				+ " <ns3:getIHCProjectSegment3 xmlns:ns3=\"utcs\" xsi:type=\"xsd:int\">%s</ns3:getIHCProjectSegment3>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		String query = String.format(soapQuery, index, major, minor);
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "getIHCProjectSegment");
		String response = sendQuery(query);
		closeConnection();
		WSFile file = new WSFile();
		file.encodeData(response);
		return file;
	}
	
	/**
	 * Query controller current state.
	 * 
	 * @return controller's current state.
	 */
	public synchronized WSControllerState getControllerState()
			throws IhcExecption {
		
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "getState");
		String response = sendQuery(emptyQuery);
		WSControllerState controllerState = new WSControllerState();
		controllerState.encodeData(response);
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
	public synchronized WSControllerState waitStateChangeNotifications(
			WSControllerState previousState, int timeoutInSeconds)
			throws IhcExecption {

		final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ " <ns1:waitForControllerStateChange1 xmlns:ns1=\"utcs\" xsi:type=\"ns1:WSControllerState\">"
				+ "  <ns1:state xsi:type=\"xsd:string\">%s</ns1:state>"
				+ " </ns1:waitForControllerStateChange1>"
				+ " <ns2:waitForControllerStateChange2 xmlns:ns2=\"utcs\" xsi:type=\"xsd:int\">%s</ns2:waitForControllerStateChange2>"
				+ "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		String query = String.format(soapQuery, previousState.getState(), timeoutInSeconds);
		openConnection(url);
		super.setCookies(cookies);
		setRequestProperty("SOAPAction", "waitForControllerStateChange");
		setTimeout(getTimeout() + timeoutInSeconds * 1000);
		String response = sendQuery(query);
		closeConnection();
		WSControllerState controllerState = new WSControllerState();
		controllerState.encodeData(response);
		return controllerState;
	}


}
