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
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ihc.ws.datatypes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
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

	/** Current state of the connection */
	public enum ConnectionState {
		DISCONNECTED, CONNECTING, CONNECTED
	}

	public final static String CONTROLLER_STATE_READY = "text.ctrl.state.ready";
	public final static String CONTROLLER_STATE_INITIALIZE = "text.ctrl.state.initialize";

	private static final Logger logger = LoggerFactory.getLogger(IhcClient.class);

	private static ConnectionState connState = ConnectionState.DISCONNECTED;
	
	/** Controller services */
	private static IhcAuthenticationService authenticationService = null;
	private static IhcResourceInteractionService resourceInteractionService = null;
	private static IhcControllerService controllerService = null;
	
	/** Thread to handle resource value notifications from the controller */
	private IhcResourceValueNotificationListener resourceValueNotificationListener = null;

	/** Thread to handle controller's state change notifications */
	private IhcControllerStateListener controllerStateListener = null;

	/** Holds cookie information (session id) from authentication procedure */
	private static List<String> cookies = null;

	private String username = "";
	private String password = "";
	private String ip = "";
	private int timeout = 5000; // milliseconds
	private String projectFile = null;
	private String dumpResourcesToFile = null;

	private Map<Integer, WSResourceValue> resourceValues = new HashMap<Integer, WSResourceValue>();
	private HashMap<Integer, ArrayList<IhcEnumValue>> enumDictionary = new HashMap<Integer, ArrayList<IhcEnumValue>>();
	private List<IhcEventListener> eventListeners = new ArrayList<IhcEventListener>();
	private WSControllerState controllerState = null;

	List<? extends Integer> resourceIdList = null;
	
	public IhcClient(String ip, String username, String password) {
		this.ip = ip;
		this.username = username;
		this.password = password;
	}
	
	public IhcClient(String ip, String username, String password, int timeout) {
		this(ip, username, password);
		setTimeoutInMillisecods(timeout);
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
	
	public String getDumpResourceInformationToFile() {
		return dumpResourcesToFile;
	}

	public void setDumpResourceInformationToFile(String value) {
		this.dumpResourcesToFile = value;
	}

	public synchronized ConnectionState getConnectionState() {
		return connState;
	}

	private synchronized void setConnectionState(ConnectionState newState) {
		IhcClient.connState = newState;
	}

	public void addEventListener(IhcEventListener listener) {
		eventListeners.add(listener);
	}
	
	public void removeEventListener(IhcEventListener listener) {
		eventListeners.remove(listener);
	}

	/**
	 * Open connection and authenticate session to IHC / ELKO LS controller.
	 * 
	 * @return
	 */
	public void closeConnection() throws IhcExecption {
		logger.debug("Close connection");

		if (resourceValueNotificationListener != null) {
			resourceValueNotificationListener.setInterrupted(true);
		}
		if (controllerStateListener != null) {
			controllerStateListener.setInterrupted(true);
		}

		setConnectionState(ConnectionState.DISCONNECTED);
	}

	/**
	 * Open connection and authenticate session to IHC / ELKO LS controller.
	 * 
	 * @throws IhcExecption 
	 */
	public void openConnection() throws IhcExecption {

		logger.debug("Open connection");

		setConnectionState(ConnectionState.CONNECTING);
		
		authenticationService = new IhcAuthenticationService(ip, timeout);
		WSLoginResult loginResult = authenticationService.authenticate(username, password, "treeview");

		if (!loginResult.isLoginWasSuccessful()) {

			// Login failed
			
			setConnectionState(ConnectionState.DISCONNECTED);
			
			if (loginResult.isLoginFailedDueToAccountInvalid()) {
				throw new IhcExecption("login failed because of invalid account");
			}
			
			if (loginResult.isLoginFailedDueToConnectionRestrictions()) {
				throw new IhcExecption("login failed because of connection restrictions");
			}

			if (loginResult.isLoginFailedDueToInsufficientUserRights()) {
				throw new IhcExecption("login failed because of insufficient user rights");
			}

			throw new IhcExecption("login failed because of unknown reason");	
		}

		logger.debug("Connection successfully opened");

		cookies = authenticationService.getCookies();
		resourceInteractionService = new IhcResourceInteractionService(ip);
		resourceInteractionService.setCookies(cookies);
		controllerService = new IhcControllerService(ip);
		controllerService.setCookies(cookies);
		controllerState = controllerService.getControllerState();
		loadProject();
		startIhcListener();
		setConnectionState(ConnectionState.CONNECTED);
	}

	private void startIhcListener() {
		logger.debug("startIhcListener");
		resourceValueNotificationListener = new IhcResourceValueNotificationListener();
		resourceValueNotificationListener.start();
		controllerStateListener = new IhcControllerStateListener();
		controllerStateListener.start();
	}

	/**
	 * Query project information from the controller.
	 * 
	 * @return project information.
	 * @throws IhcExecption 
	 */
	public synchronized WSProjectInfo getProjectInfo() throws IhcExecption {
		
		return controllerService.getProjectInfo();
	}

	/**
	 * Query controller current state.
	 * 
	 * @return controller's current state.
	 */
	public WSControllerState getControllerState() {
		
		return controllerState;
	}

	/**
	 * Load IHC / ELKO LS project file.
	 * 
	 */
	private synchronized void loadProject() throws IhcExecption {

		if (StringUtils.isNotBlank(projectFile)) {

			logger.debug("Loading IHC /ELKO LS project file from path {}...",
					projectFile);
			
			try {

				enumDictionary = IhcProjectFile.parseProject(projectFile, dumpResourcesToFile);

			} catch (IhcExecption e) {
				logger.error("Project file loading error", e);
			}

		} else {

			logger.debug("Loading IHC /ELKO LS project file from controller...");

			try {

				Document doc = LoadProjectFileFromController();
				IhcProjectFile.parseProject(doc, dumpResourcesToFile);

			} catch (IhcExecption e) {
				throw new IhcExecption("Project file loading error", e);
			}

		}
		
	}

	private Document LoadProjectFileFromController() throws IhcExecption {
		
		try {
			
			WSProjectInfo projectInfo = getProjectInfo();
			int numberOfSegments = controllerService.getProjectNumberOfSegments();
			int segmentationSize = controllerService.getProjectSegmentationSize();
	
			logger.debug("Number of segments: {}", numberOfSegments);
			logger.debug("Segmentation size: {}", segmentationSize);
	
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	
			for (int i = 0; i < numberOfSegments; i++) {
				logger.debug("Downloading segment {}", i);
	
				WSFile data = controllerService.getProjectSegment(i,
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
			return db.parse(reader);

		} catch( Exception e) {
			throw new IhcExecption(e);
		}

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
	private WSControllerState waitStateChangeNotifications(
			WSControllerState previousState, int timeoutInSeconds)
			throws IhcExecption {

		IhcControllerService service = new IhcControllerService(ip);
		service.setCookies(cookies);
		return service.waitStateChangeNotifications(previousState, timeoutInSeconds);
	}

	/**
	 * Returns all possible enumerated values for corresponding enum type.
	 * 
	 * @param typedefId
	 *            Enum type definition identifier.
	 * @return list of enum values.
	 */
	public ArrayList<IhcEnumValue> getEnumValues(int typedefId) {

		return enumDictionary.get(typedefId);
	}

	/**
	 * Enable resources runtime value notifications.
	 * 
	 * @param resourceIdList
	 *            List of resource Identifiers.
	 * @return True is connection successfully opened.
	 */
	public synchronized void enableRuntimeValueNotifications(
			List<? extends Integer> resourceIdList)
			throws IhcExecption {
		
		this.resourceIdList = resourceIdList;
		
		resourceInteractionService.enableRuntimeValueNotifications(resourceIdList);
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
	private List<? extends WSResourceValue> waitResourceValueNotifications(
			int timeoutInSeconds) throws IhcExecption, SocketTimeoutException {

		IhcResourceInteractionService service = new IhcResourceInteractionService(ip);
		service.setCookies(cookies);

		List<? extends WSResourceValue> list = service.waitResourceValueNotifications(timeoutInSeconds);
		
		for (WSResourceValue val : list) {
			resourceValues.put(val.getResourceID(), val);
		}

		return list;
	}

	/**
	 * Query resource value from controller.
	 * 
	 * 
	 * @param resoureId
	 *            Resource Identifier.
	 * @return Resource value.
	 */
	public WSResourceValue resourceQuery(int resoureId) throws IhcExecption {

		return resourceInteractionService.resourceQuery(resoureId);
		
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
			throws IhcExecption {

		WSResourceValue data = resourceValues.get(resourceId);

		if (data == null) {

			// data is not available, read it from the controller

			data = resourceInteractionService.resourceQuery(resourceId);
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
	public boolean resourceUpdate(WSResourceValue value) throws IhcExecption {
		
		return resourceInteractionService.resourceUpdate(value);
	}
	
	/**
	 * The IhcReader runs as a separate thread.
	 * 
	 * Thread listen resource value notifications from IHC / ELKO LS controller
	 * and post updates to openHAB bus when notifications are received.
	 * 
	 */
	private class IhcResourceValueNotificationListener extends Thread {

		private boolean interrupted = false;

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {

			logger.debug("IHC resource value listener started");

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				waitResourceNotifications();
			}

			logger.debug("IHC Listener stopped");

		}

		private void waitResourceNotifications() {

			try {

				logger.trace("Wait new resource value notifications from controller");

				List<? extends WSResourceValue> resourceValueList =
						waitResourceValueNotifications(10);

				logger.debug(
						"{} new notifications received from controller",
						resourceValueList.size());

				IhcStatusUpdateEvent event = new IhcStatusUpdateEvent(this);
				
				for (int i = 0; i < resourceValueList.size(); i++) {
					try {
						Iterator<IhcEventListener> iterator = eventListeners
								.iterator();

						while (iterator.hasNext()) {
							((IhcEventListener) iterator.next())
									.resourceValueUpdateReceived(event, resourceValueList.get(i));
						}

					} catch (Exception e) {
						logger.error("Event listener invoking error", e);
					}
					
				}

			} catch (SocketTimeoutException e) {
				logger.trace("Notifications timeout - no new notifications");

			} catch (IhcExecption e) {
				logger.error(
						"New notifications wait failed...", e);
				
				mysleep(1000L);
			}

		}

		private void mysleep(long milli) {
			try {
				sleep(milli);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
	}

	/**
	 * The IhcReader runs as a separate thread.
	 * 
	 * Thread listen controller state change notifications from IHC / ELKO LS
	 * controller and .
	 * 
	 */
	private class IhcControllerStateListener extends Thread {

		private boolean interrupted = false;

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {

			logger.debug("IHC controller state listener started");

			WSControllerState oldState = null;
			
			// as long as no interrupt is requested, continue running
			while (!interrupted) {

					try {

						if (oldState == null) {
							oldState = getControllerState();
							logger.debug("Controller initial state {}", oldState.getState());
						}
						
						logger.trace("Wait new state change notification from controller");

						WSControllerState currentState = waitStateChangeNotifications(oldState, 10);
						
						
						logger.trace("Controller state {}", currentState.getState());

						if (oldState.getState().equals(currentState.getState()) == false) {
							logger.info(
									"Controller state change detected ({} -> {})",
									oldState.getState(),
									currentState.getState());

							
							// send message to event listeners

							try {
								Iterator<IhcEventListener> iterator = eventListeners
										.iterator();

								IhcStatusUpdateEvent event = new IhcStatusUpdateEvent(this);
								
								while (iterator.hasNext()) {
									((IhcEventListener) iterator.next())
											.statusUpdateReceived(event, currentState);
								}

							} catch (Exception e) {
								logger.error("Event listener invoking error", e);
							}

							oldState.setState(currentState.getState());
						}

					} catch (IhcExecption e) {
						logger.error(
								"New controller state change notification wait failed...", 
								e);
						
						mysleep(1000L);
						
					} 
			}

		}

		private void mysleep(long milli) {
			try {
				sleep(milli);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
	}


}