/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.Constants;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.ActionArgument.Direction;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.meta.StateVariable;
import org.fourthline.cling.model.meta.StateVariableEventDetails;
import org.fourthline.cling.model.meta.StateVariableTypeDetails;
import org.fourthline.cling.model.types.Datatype;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDN;
import org.seamless.http.HttpFetch;
import org.seamless.http.Representation;
import org.seamless.xml.XmlPullParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Represents a single Vera unit. This class is responsible
 * for mapping Vera devices into {@link RemoteDevice}s.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraUnit {
	
	private static final Logger logger = LoggerFactory.getLogger(VeraUnit.class);

	/**
	 * The user data url for Vera.
	 */
	private static final String USER_DATA_XML_PATH = "/data_request?id=user_data&output_format=xml";

	// constants used in the device / service xml files
	private static final String ACTION = "action";
	private static final String ACTION_LIST = "actionList";
	private static final String ARGUMENT = "argument";
	private static final String ARGUMENT_LIST = "argumentList";
	private static final String CONTROL_URL = "ControlURL";
	private static final String CONTROL_URLS = "ControlURLs";
	private static final String DATA_TYPE = "dataType";
	private static final String DEFAULT_VALUE = "defaultValue";
	private static final String DEVICE = "device";
	private static final String DEVICE_FILE = "device_file";
	private static final String DEVICE_TYPE = "device_type";
	private static final String DEVICES = "devices";
	private static final String DIRECTION = "direction";
	private static final String EVENT_URL = "EventURL";
	private static final String INVISIBLE = "invisible";
	private static final String ID = "id";
	private static final String LOCAL_UDN = "local_udn";
	private static final String NAME = "name";
	private static final String ONE = "1";
	private static final String RELATED_STATE_VARIABLE = "relatedStateVariable";
	private static final String SCPDURL = "SCPDURL";
	private static final String SEND_EVENTS = "sendEvents";
	private static final String SERVICE = "service";
	private static final String SERVICE_ID = "serviceId";
	private static final String SERVICE_STATE_TABLE = "serviceStateTable";
	private static final String SERVICE_TYPE = "serviceType";
	private static final String STATE_VARIABLE = "stateVariable";
	private static final String YES = "yes";

	/**
	 * The hostname of the unit.
	 */
	private String host;
	
	/**
	 * The web port of the unit.
	 */
	private int webPort;
	
	/**
	 * The upnp port of the unit.
	 */
	private int upnpPort;
	
	/**
	 * The timeout of the unit.
	 */
	private int timeout;

	/**
	 * The local {@link InetAddress} used when
	 * communicating with this unit.
	 */
	private InetAddress localAddress;
	
	/**
	 * A device / service xml cache used during parsing
	 * to reduce redundant round-trips. The xml
	 * files are device neutral and do not need to 
	 * be re-downloaded for each device.
	 */
	private Map<String, String> xmlCache;
	
	/**
	 * The databased of {@link RemoteDevice}s index by device id. 
	 */
	private Map<Integer, RemoteDevice> devices;
	
	/**
	 * Flag to ignore invisible devices. (defaults to <code>true</code>)
	 */
	private boolean ignoreInvisibleDevices = true;

	/**
	 * Constructs a new vera unit and attempts to connect to it immediately.
	 * 
	 * @param host the host name of the unit
	 * @param webPort the web port of the unit
	 * @param upnpPort the upnp port of the unit
	 * @param timeout the timeout of the unit
	 * @throws IOException if error communicating with the unit
	 */
	public VeraUnit(String host, int webPort, int upnpPort, int timeout) throws IOException {
		this.host = host;
		this.webPort = webPort;
		this.upnpPort = upnpPort;
		this.timeout = timeout;
		Socket socket = new Socket(host, webPort);
		localAddress = socket.getLocalAddress();
		socket.close();
	}
	
	/**
	 * Gets the unit timeout.
	 * @return the unit timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * Refreshes the local databse of {@link RemoteDevice}s by querying the unit
	 * for its latest user data and re-parsing all the xml files.
	 */
	public void refresh() {
		try {
			URL userDataUrl = buildUserDataUrl();
			Representation<String> fetch = HttpFetch.fetchString(userDataUrl, 10000, 5000);
			String userDataXml = fetch.getEntity();
			XmlPullParser xpp = XmlPullParserUtils.createParser(userDataXml);
			xmlCache = new HashMap<String, String>();
			devices = parseVeraDevices(xpp);
			xmlCache = null;
			if (logger.isTraceEnabled()) {
				for (Map.Entry<Integer, RemoteDevice> entry: devices.entrySet()) {
					Integer deviceId = entry.getKey();
					RemoteDevice device = entry.getValue();
					logger.trace("[{}] {}", deviceId, device);
					RemoteService[] services = device.getServices();
					for (RemoteService service: services) {
						logger.trace("[{}] - {}", deviceId, service);
						StateVariable<RemoteService>[] stateVariables = service.getStateVariables();
						for (StateVariable<RemoteService> stateVariable: stateVariables) {
							logger.trace("[{}] -- {}", deviceId, stateVariable);
						}
						Action<RemoteService>[] actions = service.getActions();
						for (Action<RemoteService> action: actions) {
							logger.trace("[{}] -- {}", deviceId, action);
							@SuppressWarnings("unchecked")
							ActionArgument<RemoteService>[] arguments = action.getArguments();
							for (ActionArgument<RemoteService> argument: arguments) {
								logger.trace("[{}] --- {}", deviceId, argument);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the current node's attributes as a map.
	 * 
	 * @param xpp the parser in use
	 * @return the current node's attributes
	 */
	private Map<String, String> getAttributes(XmlPullParser xpp) {
		int n = xpp.getAttributeCount();
		Map<String, String> attrs = new HashMap<String, String>(n);
		for (int i = 0; i < n; i++) {
			String name = xpp.getAttributeName(i);
			String value = xpp.getAttributeValue(i);
			attrs.put(name, value);
		}
		return attrs;
	}
	
	/**
	 * Gets the specified <code>descriptor</code>'s xml from
	 * the cache or directly from the unit if necessary.
	 * 
	 * @param descriptor the descriptor to get
	 * @return the descriptor's xml
	 * @throws IOException if error communicating with the unit
	 */
	private String getDescriptorXml(String descriptor) throws IOException {
		if (!xmlCache.containsKey(descriptor)) {
			URL url = buildDescriptorUrl(descriptor);
			Representation<String> fetch = HttpFetch.fetchString(url, 10000, 10000);
			String xml = fetch.getEntity();
			xmlCache.put(descriptor, xml);
		}
		return xmlCache.get(descriptor);
	}
	
	/**
	 * Builds the {@link URL} based on the given <code>host</code>, <code>port</code> and <code>path</code>.
	 * 
	 * The url pattern is http://{host}:{port}{path}
	 *  
	 * @param host the host
	 * @param port the port
	 * @param path the path
	 * @return the url
	 */
	private static URL buildUrl(String host, int port, String path) {
		if (!StringUtils.startsWith(path, "/")) {
			path = "/" + path;
		}
		URL url = null;
		try {
			url = new URL("http://" + host + ":" + port + path);
		} catch (MalformedURLException e) {
			// nop
		}
		return url;
	}
	
	/**
	 * Builds the descriptor {@link URL}.
	 * @param path the descriptor path
	 * @return the descriptor {@link URL}.
	 */
	private URL buildDescriptorUrl(String path) {
		return buildUrl(host, webPort, path);
	}
	
	/**
	 * Builds the upnp {@link URL}
	 * @param path the upnp path
	 * @return the upnp {@link URL}
	 */
	private URL buildUpnpUrl(String path) {
		return buildUrl(host, upnpPort, path);
	}
	
	/**
	 * Builds the user data {@link URL}
	 * @return the user data {@link URL}
	 */
	private URL buildUserDataUrl() {
		return buildUrl(host, webPort, USER_DATA_XML_PATH);
	}
	
	/**
	 * Parses a &lt;devices&gt;...&lt;/devices&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws ValidationException
	 * @throws XmlPullParserException
	 */
	private Map<Integer, RemoteDevice> parseVeraDevices(XmlPullParser xpp) throws IOException, ValidationException, XmlPullParserException {
		// <devices>
		XmlPullParserUtils.searchTag(xpp, DEVICES);
		Map<Integer, RemoteDevice> devices = new HashMap<Integer, RemoteDevice>();
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
			
			// <device>
			if (event == XmlPullParser.START_TAG && DEVICE.equals(xpp.getName())) {
				Map<String, String> attrs = getAttributes(xpp);
				Integer deviceId = Integer.valueOf(attrs.get(ID));
				RemoteDevice device = parseVeraDevice(xpp, attrs);
				if (device != null) {
					devices.put(deviceId, device);
				}
			}
			
			// </devices>
			if (event == XmlPullParser.END_TAG && DEVICES.equals(xpp.getName())) {
				break;
			}
		}
		return devices;
	}
	
	/**
	 * Parses a &lt;device&gt;...&lt;/device&gt; tag.
	 * 
	 * @param xpp
	 * @param attrs
	 * @return
	 * @throws IOException
	 * @throws ValidationException
	 * @throws XmlPullParserException
	 */
	private RemoteDevice parseVeraDevice(XmlPullParser xpp, Map<String, String> attrs) throws IOException, ValidationException, XmlPullParserException {
		// <device>
		if (ignoreInvisibleDevices  && ONE.equals(attrs.get(INVISIBLE))) {
			return null;
		}

		UDN udn = UDN.valueOf(attrs.get(LOCAL_UDN));
		Integer maxAgeSeconds = Constants.MIN_ADVERTISEMENT_AGE_SECONDS;
		String deviceDescriptor = attrs.get(DEVICE_FILE);
		URL descriptorURL = buildDescriptorUrl(deviceDescriptor);
		RemoteDeviceIdentity identity = new RemoteDeviceIdentity(udn, maxAgeSeconds, descriptorURL, null, localAddress);
		
		DeviceType type = DeviceType.valueOf(attrs.get(DEVICE_TYPE));
		DeviceDetails details = new DeviceDetails(attrs.get(NAME));
		Map<ServiceId, Map<String, String>> deviceUrls = parseDeviceUrls(xpp);
		RemoteService[] services = parseDeviceDescriptor(deviceDescriptor, deviceUrls);
		RemoteDevice device = new RemoteDevice(identity, type, details, services);
		
		return device;
	}
	
	/**
	 * Parse a device descriptor.
	 * 
	 * @param deviceDescriptor
	 * @param deviceUrls
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private RemoteService[] parseDeviceDescriptor(String deviceDescriptor, Map<ServiceId, Map<String, String>> deviceUrls) throws IOException, XmlPullParserException {
		
		Map<ServiceId, String> serviceDescriptors = parseServiceDescriptors(deviceDescriptor);
		
		List<RemoteService> services = new ArrayList<RemoteService>();
		
		try {
			
			for (Map.Entry<ServiceId, String> entry: serviceDescriptors.entrySet()) {
				
				ServiceId serviceId = entry.getKey();
				ServiceType serviceType = ServiceType.valueOf(deviceUrls.get(serviceId).get(SERVICE_TYPE));
				String serviceDescriptor = entry.getValue();
	
				URI descriptorURI = null;
				URI controlURI = null;
				URI eventURI = null;
				try {
					descriptorURI = buildDescriptorUrl(serviceDescriptor).toURI();
					controlURI = buildUpnpUrl(deviceUrls.get(serviceId).get(CONTROL_URL)).toURI();
					eventURI = buildUpnpUrl(deviceUrls.get(serviceId).get(EVENT_URL)).toURI();
				} catch (Exception e) {
					// nop - drop this service
					continue;
				}
				
				String serviceDescriptorXml = getDescriptorXml(serviceDescriptor);
				XmlPullParser serviceXpp = XmlPullParserUtils.createParser(serviceDescriptorXml);
				StateVariable<RemoteService>[] stateVariables = parseStateVariables(serviceXpp);
				Action<RemoteService>[] actions = parseActions(serviceXpp);
				
				RemoteService service = new RemoteService(serviceType, serviceId, descriptorURI, controlURI, eventURI, actions, stateVariables);
				services.add(service);
			}
			
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return services.toArray(new RemoteService[services.size()]);
	}
	
	/**
	 * Parsers a &lt;ControlURLs&gt;...&lt;/ControlURLs&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Map<ServiceId, Map<String, String>> parseDeviceUrls(XmlPullParser xpp) throws IOException, XmlPullParserException {

		// <ControlURLs>
		XmlPullParserUtils.searchTag(xpp, CONTROL_URLS);

		Map<ServiceId, Map<String, String>> urls = new HashMap<ServiceId, Map<String, String>>();
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
		
			// <service_ ... />
			if (event == XmlPullParser.START_TAG && StringUtils.startsWith(xpp.getName(), SERVICE)) {
				Map<String, String> attributes = getAttributes(xpp);
				ServiceId serviceId = ServiceId.valueOf(attributes.get(SERVICE));
				urls.put(serviceId, attributes);
			}
			
			// </ControlURLs> - no more urls
			else if (event == XmlPullParser.END_TAG && xpp.getName().equals(CONTROL_URLS)) {
				break;
			}
			
		}
		
		return urls;
	}

	/**
	 * Parses a service descriptor.
	 * 
	 * @param deviceDescriptor
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Map<ServiceId, String> parseServiceDescriptors(String deviceDescriptor) throws IOException, XmlPullParserException {
		Map<ServiceId, String> serviceDescriptors = new HashMap<ServiceId, String>();
		
		String deviceDescriptorXml = getDescriptorXml(deviceDescriptor);
		XmlPullParser xpp = XmlPullParserUtils.createParser(deviceDescriptorXml);

		try {
			while (true) {
				
				// <serviceId>
				XmlPullParserUtils.searchTag(xpp, SERVICE_ID);
				ServiceId serviceId = ServiceId.valueOf(xpp.nextText());

				// <SCPDURL>
				XmlPullParserUtils.searchTag(xpp, SCPDURL);
				String serviceDescriptor = xpp.nextText();
				
				serviceDescriptors.put(serviceId, serviceDescriptor);
			}
		} catch (IOException e) {
			// nop - end of file
		}
		
		return serviceDescriptors;
	}
	
	/**
	 * Parses a &lt;serviceStateTable&gt;...&lt;/serviceStateTable&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	private StateVariable<RemoteService>[] parseStateVariables(XmlPullParser xpp) throws IOException, XmlPullParserException {
		List<StateVariable<RemoteService>> stateVariables = new ArrayList<StateVariable<RemoteService>>();

		// <serviceStateTable>
		XmlPullParserUtils.searchTag(xpp, SERVICE_STATE_TABLE);
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {

			// <stateVariable>
			if (event == XmlPullParser.START_TAG && xpp.getName().equals(STATE_VARIABLE)) {
				StateVariable<RemoteService> stateVariable = parseStateVariable(xpp);
				stateVariables.add(stateVariable);
			}
			
			// </serviceStateTable>
			else if (event == XmlPullParser.END_TAG && xpp.getName().equals(SERVICE_STATE_TABLE)) {
				break;
			}
		}
		
		return stateVariables.toArray(new StateVariable[stateVariables.size()]);
	}
	
	/**
	 * Parses a &lt;stateVariable&gt;...&lt;/stateVariable&gt; tag.
	 *  
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private StateVariable<RemoteService> parseStateVariable(XmlPullParser xpp) throws IOException, XmlPullParserException {
		
		// <stateVariable sendEvents="...">
		Map<String, String> attrs = getAttributes(xpp);
		boolean sendEvents = YES.equals(attrs.get(SEND_EVENTS));
		
		String name = null;
		Datatype<?> datatype = null;
		String defaultValue = null;
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {

			// <name>
			if (event == XmlPullParser.START_TAG && NAME.equals(xpp.getName())) {
				name = xpp.nextText();
			}
			
			// <dataType>
			else if (event == XmlPullParser.START_TAG && DATA_TYPE.equals(xpp.getName())) {
				String datatypeText = xpp.nextText();
				datatype = Datatype.Builtin.getByDescriptorName(datatypeText).getDatatype();
			}
			
			// <defaultValue>
			else if (event == XmlPullParser.START_TAG && DEFAULT_VALUE.equals(xpp.getName())) {
				defaultValue = xpp.nextText();
			}
			
			// </stateVariable>
			else if (event == XmlPullParser.END_TAG && STATE_VARIABLE.equals(xpp.getName())) {
				break;
			}
		}
		
		StateVariableTypeDetails typeDetails = new StateVariableTypeDetails(datatype, defaultValue);
		StateVariableEventDetails eventDetails = new StateVariableEventDetails(sendEvents);
		StateVariable<RemoteService> stateVariable = new StateVariable<RemoteService>(name, typeDetails, eventDetails);
		return stateVariable;
	}
	
	/**
	 * Parses an &lt;actionList&gt;...&lt;/actionList&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	private Action<RemoteService>[] parseActions(XmlPullParser xpp) throws IOException, XmlPullParserException {

		try {
			
			// <actionList>
			XmlPullParserUtils.searchTag(xpp, ACTION_LIST);
	
			List<Action<RemoteService>> actions = new ArrayList<Action<RemoteService>>();
			
			int event;
			while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
				
				// <action>
				if (event == XmlPullParser.START_TAG && ACTION.equals(xpp.getName())) {
					Action<RemoteService> action = parseAction(xpp);
					actions.add(action);
				}
				
				// </actionList>
				else if (event == XmlPullParser.END_TAG && ACTION_LIST.equals(xpp.getName())) {
					break;
				}
				
			}
		
			return actions.toArray(new Action[actions.size()]);
			
		} catch (IOException e) {
			// nop - the service has no actions
			return null;
		}
	}
	
	/**
	 * Parses an &lt;action&gt;...&lt;/action&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Action<RemoteService> parseAction(XmlPullParser xpp) throws IOException, XmlPullParserException {
		
		// <action>
		
		String name = null;
		ActionArgument<RemoteService>[] argumentList = null;
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
			
			if (event == XmlPullParser.START_TAG) {
			
				// <name>
				if (NAME.equals(xpp.getName())) {
					name = xpp.nextText();
				}
				
				// <argumentList>
				if (ARGUMENT_LIST.equals(xpp.getName())) {
					argumentList = parseArgumentList(xpp);
				}
			
			}
			
			// </action>
			else if (event == XmlPullParser.END_TAG && ACTION.equals(xpp.getName())) {
				break;
			}
		}
		
		Action<RemoteService> action = new Action<RemoteService>(name, argumentList);
		return action;
	}
	
	/**
	 * Parses an &lt;argumentList&gt;...&lt;/argumentList&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	private ActionArgument<RemoteService>[] parseArgumentList(XmlPullParser xpp) throws IOException, XmlPullParserException {

		// <argumentList>
		
		List<ActionArgument<RemoteService>> arguments = new ArrayList<ActionArgument<RemoteService>>();
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
			
			// <argument>
			if (event == XmlPullParser.START_TAG && ARGUMENT.equals(xpp.getName())) {
				ActionArgument<RemoteService> argument = parseArgument(xpp);
				arguments.add(argument);
			}
			
			// </argumentList>
			else if (event == XmlPullParser.END_TAG && ARGUMENT_LIST.equals(xpp.getName())) {
				break;
			}
				
		}
		
		return arguments.toArray(new ActionArgument[arguments.size()]);
	}
	
	/**
	 * Parses an &lt;argument&gt;...&lt;/argument&gt; tag.
	 * 
	 * @param xpp
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private ActionArgument<RemoteService> parseArgument(XmlPullParser xpp) throws IOException, XmlPullParserException {

		// <argument>
		
		String name = null;
		Direction direction = null;
		String relatedStateVariable = null;
		
		int event;
		while ((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {

			if (event == XmlPullParser.START_TAG) {
			
				// <name>
				if (NAME.equals(xpp.getName())) {
					name = xpp.nextText();
				}
				
				// <direction>
				else if (DIRECTION.equals(xpp.getName())) {
					String directionText = xpp.nextText();
					direction = Direction.valueOf(directionText.toUpperCase());	
				}
				
				// <relatedStateVariable>
				else if (RELATED_STATE_VARIABLE.equals(xpp.getName())) {
					relatedStateVariable = xpp.nextText();
				}
				
			}
			
			// </argument>
			else if (event == XmlPullParser.END_TAG && ARGUMENT.equals(xpp.getName())) {
				break;
			}
		}
		
		ActionArgument<RemoteService> argument = new ActionArgument<RemoteService>(name, relatedStateVariable, direction);
		return argument;
	}

	/**
	 * Lookups the {@link RemoteDevice} by the given <code>deviceId</code>.
	 * @param deviceId the device id to lookup
	 * @return the {@link RemoteDevice}; or <code>null</code> if not found
	 */
	public RemoteDevice getDevice(int deviceId) {
		if (devices == null) {
			refresh();
		}
		return devices.get(deviceId);
	}

}
