/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.openhab.binding.sonos.SonosCommandType;
import org.openhab.binding.sonos.internal.SonosBinding.SonosZonePlayerState;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.UpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.meta.StateVariable;
import org.teleal.cling.model.meta.StateVariableTypeDetails;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.model.types.Datatype;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.xml.sax.SAXException;

/**
 * Internal data structure which carries the connection details of one Sonos player
 * (there could be several)
 * 
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
class SonosZonePlayer {

	private static Logger logger = LoggerFactory.getLogger(SonosZonePlayer.class);

	protected final int interval = 600;
	private boolean isConfigured = false;

	/** the default socket timeout when requesting an url */
	private static final int SO_TIMEOUT = 5000;

	private RemoteDevice device = null;
	private UDN udn;
	private String id;
	private DateTime lastOPMLQuery;
	private SonosZonePlayerState savedState = null;


	static protected UpnpService upnpService;
	protected SonosBinding sonosBinding;

	private Map<String, StateVariableValue> stateMap = Collections.synchronizedMap(new HashMap<String,StateVariableValue>());

	/**
	 * @return the stateMap
	 */
	public Map<String, StateVariableValue> getStateMap() {
		return stateMap;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	SonosZonePlayer(String id, SonosBinding binding) {

		if( binding != null) {
			this.id = id;
			sonosBinding = binding;
		}	
	}


	private void enableGENASubscriptions(){

		if(device!=null && isConfigured()) {

			// Create a GENA subscription of each service for this device, if supported by the device        
			List<SonosCommandType> subscriptionCommands = SonosCommandType.getSubscriptions();
			List<String> addedSubscriptions = new ArrayList<String>();

			for(SonosCommandType c : subscriptionCommands){
				Service service = device.findService(new UDAServiceId(c.getService()));			
				if(service != null && !addedSubscriptions.contains(c.getService())) {
					SonosPlayerSubscriptionCallback callback = new SonosPlayerSubscriptionCallback(service,interval);
					addedSubscriptions.add(c.getService());
					upnpService.getControlPoint().execute(callback);
				}
			}
		}
	}


	protected boolean isUpdatedValue(String valueName,StateVariableValue newValue) {
		if(newValue != null && valueName != null) {
			StateVariableValue oldValue = stateMap.get(valueName);

			if(newValue.getValue()== null) {
				// we will *not* store an empty value, thank you.
				return false;
			} else {
				if(oldValue == null) {
					// there was nothing stored before
					return true;
				} else {
					if (oldValue.getValue() == null) {
						// something was defined, but no value present
						return true;
					} else {
						if(newValue.getValue().equals(oldValue.getValue())) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}	

		return false;
	}



	protected void processStateVariableValue(String valueName,StateVariableValue newValue) {
		if(newValue!=null && isUpdatedValue(valueName,newValue)) {
			Map<String, StateVariableValue> mapToProcess = new HashMap<String, StateVariableValue>();
			mapToProcess.put(valueName,newValue);
			stateMap.putAll(mapToProcess);
			sonosBinding.processVariableMap(device,mapToProcess);
		}
	}

	/**
	 * @return the device
	 */
	public RemoteDevice getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(RemoteDevice device) {
		this.device = device;
		if(upnpService !=null && device!=null) {
			isConfigured = true;
			enableGENASubscriptions();
		}
	}

	public class SonosPlayerSubscriptionCallback extends SubscriptionCallback {


		public SonosPlayerSubscriptionCallback(Service service) {
			super(service);
		}

		public SonosPlayerSubscriptionCallback(Service service,
				int requestedDurationSeconds) {
			super(service, requestedDurationSeconds);
		}

		@Override
		public void established(GENASubscription sub) {
		}

		@Override
		protected void failed(GENASubscription subscription,
				UpnpResponse responseStatus,
				Exception exception,
				String defaultMsg) {
		}

		public void eventReceived(GENASubscription sub) {

			// get the device linked to this service linked to this subscription
			Map<String, StateVariableValue> values = sub.getCurrentValues();        
			Map<String, StateVariableValue> mapToProcess = new HashMap<String, StateVariableValue>();
			Map<String, StateVariableValue> parsedValues = null;

			// now, lets deal with the specials - some UPNP responses require some XML parsing
			// or we need to update our internal data structure
			// or are things we want to store for further reference

			for(String stateVariable : values.keySet()){
				if(stateVariable.equals("LastChange") && service.getServiceType().getType().equals("AVTransport")){
					try {
						parsedValues = SonosXMLParser.getAVTransportFromXML(values.get(stateVariable).toString());
						for(String someValue : parsedValues.keySet()) {
							//							logger.debug("Lastchange parsed into {}:{}",someValue,parsedValues.get(someValue));
							if(isUpdatedValue(someValue,parsedValues.get(someValue))){
								mapToProcess.put(someValue,parsedValues.get(someValue));
							}
						}
					} catch (SAXException e) {
						logger.error("Could not parse AVTransport from String {}",values.get(stateVariable).toString());
					}

				} else if(stateVariable.equals("LastChange") && service.getServiceType().getType().equals("RenderingControl")){
					try {
						parsedValues = SonosXMLParser.getRenderingControlFromXML(values.get(stateVariable).toString());
						for(String someValue : parsedValues.keySet()) {
							if(isUpdatedValue(someValue,parsedValues.get(someValue))){
								mapToProcess.put(someValue,parsedValues.get(someValue));
							}
						}
					} catch (SAXException e) {
						logger.error("Could not parse RenderingControl from String {}",values.get(stateVariable).toString());
					}
				} else if(isUpdatedValue(stateVariable,values.get(stateVariable))){
					mapToProcess.put(stateVariable, values.get(stateVariable));
				}

			}    		

			if(isConfigured) {
				stateMap.putAll(mapToProcess);
				sonosBinding.processVariableMap(device,mapToProcess);
			}
		}

		public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
			logger.warn("Missed events: " + numberOfMissedEvents);
		}

		@Override
		protected void ended(GENASubscription subscription,
				CancelReason reason, UpnpResponse responseStatus) {			

			if(device!=null && isConfigured()) {
				//rebooting the GENA subscription
				Service service = subscription.getService();			
				SonosPlayerSubscriptionCallback callback = new SonosPlayerSubscriptionCallback(service,interval);
				upnpService.getControlPoint().execute(callback);

			}
		}
	}

	public void setService(UpnpService service) {
		if(upnpService == null) {
			upnpService = service; 
		}
		if(upnpService !=null && device!=null) {
			isConfigured = true;
			enableGENASubscriptions();
		}

	}

	public String getModel() {
		if(device!=null) {
			return device.getDetails().getModelDetails().getModelNumber();
		} else {
			return "Unknown";
		}
	}

	/**
	 * @return the udn
	 */
	public UDN getUdn() {
		return udn;
	}

	/**
	 * @param udn the udn to set
	 */
	public void setUdn(UDN udn) {
		this.udn = udn;
	}

	public String getId() {
		return id;
	}


	@Override
	public String toString() {
		return "Sonos [udn=" + udn + ", device=" + device +"]";
	}

	public boolean play() {
		if(isConfigured()) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Play");
			ActionInvocation invocation = new ActionInvocation(action);

			invocation.setInput("Speed", "1");

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}
	}

	public boolean playRadio(String station){

		if(isConfigured()) {

			List<SonosEntry> stations = getFavoriteRadios();

			SonosEntry theEntry = null;

			// search for the appropriate radio based on its name (title)
			for(SonosEntry someStation : stations){
				if(someStation.getTitle().equals(station)){
					theEntry = someStation;
					break;
				}
			}

			// set the URI of the group coordinator
			if(theEntry != null) {

				SonosZonePlayer coordinator = sonosBinding.getCoordinatorForZonePlayer(this);
				coordinator.setCurrentURI(theEntry);
				coordinator.play();

				return true;
			}
			else {
				return false;
			}	
		} else {
			return false;
		}

	}

	public boolean playPlayList(String playlist){

		if(isConfigured()) {

			List<SonosEntry> playlists = getPlayLists();

			SonosEntry theEntry = null;

			// search for the appropriate play list based on its name (title)
			for(SonosEntry somePlaylist : playlists){
				if(somePlaylist.getTitle().equals(playlist)){
					theEntry = somePlaylist;
					break;
				}
			}

			// set the URI of the group coordinator
			if(theEntry != null) {

				SonosZonePlayer coordinator = sonosBinding.getCoordinatorForZonePlayer(this);
				//coordinator.setCurrentURI(theEntry);
				coordinator.addURIToQueue(theEntry);

				if(stateMap != null && isConfigured()) {
					StateVariableValue firstTrackNumberEnqueued = stateMap.get("FirstTrackNumberEnqueued");
					if(firstTrackNumberEnqueued!=null) {
						coordinator.seek("TRACK_NR", firstTrackNumberEnqueued.getValue().toString());
					}
				}

				coordinator.play();

				return true;
			}
			else {
				return false;
			}	
		} else {
			return false;
		}

	}

	public boolean stop() {
		if(isConfigured()) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Stop");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}
	}

	public boolean pause() {
		if(isConfigured()) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Pause");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}
	}

	public boolean next() {
		if(isConfigured()) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Next");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}
	}

	public boolean previous() {
		if(isConfigured()) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Previous");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}
	}

	public String getZoneName() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("ZoneName");
			if(value != null) {
				return value.getValue().toString();
			}
		}
		return null;

	}

	public String getZoneGroupID() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("LocalGroupUUID");
			if(value != null) {
				return value.getValue().toString();
			}
		}
		return null;

	}

	public boolean isGroupCoordinator() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("GroupCoordinatorIsLocal");
			if(value != null) {
				return (Boolean) value.getValue();
			}
		}

		return false;

	}

	public SonosZonePlayer getCoordinator(){
		return sonosBinding.getCoordinatorForZonePlayer(this);
	}

	public boolean isCoordinator() {
		return this.equals(getCoordinator());
	}

	public boolean addMember(SonosZonePlayer newMember) {
		if(newMember != null && isConfigured()) {

			SonosEntry entry = new SonosEntry("", "", "", "", "", "", "", "x-rincon:"+udn.getIdentifierString());
			return newMember.setCurrentURI(entry);		

		} else {
			return false;
		}
	}

	public boolean removeMember(SonosZonePlayer oldMember){
		if(oldMember != null && isConfigured()) {

			oldMember.becomeStandAlonePlayer();
			SonosEntry entry = new SonosEntry("", "", "", "", "", "", "", "x-rincon-queue:"+oldMember.getUdn().getIdentifierString()+"#0");
			return oldMember.setCurrentURI(entry);		

		} else {
			return false;
		}
	}

	public boolean becomeStandAlonePlayer() {

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("BecomeCoordinatorOfStandaloneGroup");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}

	}

	public boolean setMute(String string) {
		if(string != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("RenderingControl"));
			Action action = service.getAction("SetMute");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("Channel", "Master");

				if(string.equals("ON") || string.equals("OPEN") || string.equals("UP") ) {
					invocation.setInput("DesiredMute", "True");	        		
				} else 

					if(string.equals("OFF") || string.equals("CLOSED") || string.equals("DOWN") ) {
						invocation.setInput("DesiredMute", "False");	        		
					} else {
						return false;
					}
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}
			executeActionInvocation(invocation);

			return true;			

		} else {
			return false;
		}
	}

	public String getMute(){
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("MuteMaster");
			if(value != null) {
				return value.getValue().toString();
			}
		}

		return null;

	}

	public boolean setVolume(String value) {
		if(value != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("RenderingControl"));
			Action action = service.getAction("SetVolume");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				String newValue = value;
				if(value.equals("INCREASE")) {
					int i = Integer.valueOf(this.getVolume());
					newValue = String.valueOf(Math.min(100, i+1));
				} else if (value.equals("DECREASE")) {
					int i = Integer.valueOf(this.getVolume());
					newValue = String.valueOf(Math.max(0, i-1));					
				} else if (value.equals("ON")) {
					newValue = "100";
				} else if (value.equals("OFF")) {
					newValue = "0";
				} else {
					newValue = value;
				}
				invocation.setInput("Channel", "Master");
				invocation.setInput("DesiredVolume",newValue);
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;			

		} else {
			return false;
		}
	}

	public String getVolume() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("VolumeMaster");
			if(value != null) {
				return value.getValue().toString();
			}
		}

		return null;

	}

	public boolean updateTime() {

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("AlarmClock"));
			Action action = service.getAction("GetTimeNow");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;
		} else {
			return false;
		}

	}

	public String getTime() {
		if(isConfigured()) {
			updateTime();
			if(stateMap != null) {
				StateVariableValue value = stateMap.get("CurrentLocalTime");
				if(value != null) {
					return value.getValue().toString();
				}

			}
		}
		return null;


	}

	protected void executeActionInvocation(ActionInvocation invocation) {
		if(invocation != null) {
			new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();

			ActionException anException = invocation.getFailure();
			if(anException!= null && anException.getMessage()!=null) {
				logger.warn(anException.getMessage());
			}

			Map<String, ActionArgumentValue> result =  invocation.getOutputMap();
			Map<String, StateVariableValue> mapToProcess = new HashMap<String, StateVariableValue>();
			if(result != null) {

				// only process the variables that have changed value
				for(String variable : result.keySet()) {
					ActionArgumentValue newArgument = result.get(variable);

					StateVariable newVariable = new StateVariable(variable,new StateVariableTypeDetails(newArgument.getDatatype()));
					StateVariableValue newValue = new StateVariableValue(newVariable, newArgument.getValue());

					if(isUpdatedValue(variable,newValue)) {
						mapToProcess.put(variable, newValue);
					}
				}

				stateMap.putAll(mapToProcess);
				sonosBinding.processVariableMap(device,mapToProcess);
			}
		}
	}

	public boolean updateRunningAlarmProperties() {

		if(stateMap != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("GetRunningAlarmProperties");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			// for this property we would like to "compile" a more friendly variable.
			// this newly created "variable" is also store in the stateMap
			StateVariableValue alarmID  = stateMap.get("AlarmID");
			StateVariableValue groupID  = stateMap.get("GroupID");
			StateVariableValue loggedStartTime  = stateMap.get("LoggedStartTime");
			String newStringValue = null;
			if(alarmID != null && loggedStartTime != null) {
				newStringValue = alarmID.getValue() + " - " + loggedStartTime.getValue();
			} else {
				newStringValue = "No running alarm";
			}

			StateVariable newVariable = new StateVariable("RunningAlarmProperties",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));
			StateVariableValue newValue = new StateVariableValue(newVariable, newStringValue);

			processStateVariableValue(newVariable.getName(),newValue);

			return true;
		} else {
			return false;
		}
	}

	public String getRunningAlarmProperties() {
		if(isConfigured()) {
			updateRunningAlarmProperties();
			if(stateMap != null) {
				StateVariableValue value = stateMap.get("RunningAlarmProperties");
				if(value != null) {
					return value.getValue().toString();
				}
			}
		}

		return null;

	}

	public boolean updateZoneInfo() {
		if(stateMap != null && isConfigured()) {
			Service service = device.findService(new UDAServiceId("DeviceProperties"));
			Action action = service.getAction("GetZoneInfo");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			Service anotherservice = device.findService(new UDAServiceId("DeviceProperties"));
			Action anotheraction = service.getAction("GetZoneAttributes");
			ActionInvocation anotherinvocation = new ActionInvocation(anotheraction);

			executeActionInvocation(anotherinvocation);


			//			 anotherservice = device.findService(new UDAServiceId("ZoneGroupTopology"));
			//			 anotheraction = service.getAction("GetZoneGroupState");
			//			 anotherinvocation = new ActionInvocation(anotheraction);

			//			executeActionInvocation(anotherinvocation);

			return true;
		} else {
			return false;
		}
	}

	public String getMACAddress() {
		if(isConfigured()) {
			updateZoneInfo();
			if(stateMap != null) {
				StateVariableValue value = stateMap.get("MACAddress");
				if(value != null) {
					return value.getValue().toString();
				}
			}
		}
		return null;

	}


	public boolean setLed(String string) {

		if(string != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("DeviceProperties"));
			Action action = service.getAction("SetLEDState");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				if(string.equals("ON") || string.equals("OPEN") || string.equals("UP") ) {
					invocation.setInput("DesiredLEDState", "On");	        		
				} else

					if(string.equals("OFF") || string.equals("CLOSED") || string.equals("DOWN") ) {
						invocation.setInput("DesiredLEDState", "Off");	        		
					} else {
						return false;
					}
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}
			executeActionInvocation(invocation);

			return true;			

		} else {
			return false;
		}
	}


	public boolean updateLed() {

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("DeviceProperties"));
			Action action = service.getAction("GetLEDState");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;	
		}
		else {
			return false;
		}
	}

	public boolean getLed() {

		if(isConfigured()) {

			updateLed();
			if(stateMap != null) {
				StateVariableValue variable = stateMap.get("CurrentLEDState");
				if(variable != null) {
					return variable.getValue().equals("On") ? true : false;
				}
			} 
		}	

		return false;
	}

	public String getCurrentZoneName() {

		if(isConfigured()) {

			updateCurrentZoneName();
			if(stateMap != null) {
				StateVariableValue variable = stateMap.get("CurrentZoneName");
				if(variable != null) {
					return variable.getValue().toString();
				}
			} 
		}	

		return null;		
	}

	public boolean updateCurrentZoneName() {

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("DeviceProperties"));
			Action action = service.getAction("GetZoneAttributes");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;	
		}
		else {
			return false;
		}

	}

	public boolean updatePosition() {

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("GetPositionInfo");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			return true;	
		}
		else {
			return false;
		}
	}

	public String getPosition() {
		if(stateMap != null && isConfigured()) {

			updatePosition();
			if(stateMap != null) {
				StateVariableValue variable = stateMap.get("RelTime");
				if(variable != null) {
					return variable.getValue().toString();
				}
			}	
		}
		return null;
	}

	public boolean setPosition(String relTime) {
		return seek("REL_TIME",relTime);
	}

	public boolean setPositionTrack(long tracknr) {
		return seek("TRACK_NR",Long.toString(tracknr));
	}

	protected boolean seek(String unit, String target) {
		if(isConfigured() && unit != null && target != null) {
			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("Seek");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("InstanceID","0");
				invocation.setInput("Unit", unit);
				invocation.setInput("Target", target);
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;
		}

		return false;
	}


	public Boolean isLineInConnected() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue statusLineIn = stateMap.get("LineInConnected");
			if(statusLineIn != null) {
				return (Boolean) statusLineIn.getValue();
			}
		}
		return null;

	}

	public Boolean isAlarmRunning() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue status = stateMap.get("AlarmRunning");
			if(status!=null) {
				return status.getValue().equals("1") ? true : false;
			}
		}
		return null;

	}

	public String getTransportState() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue status = stateMap.get("TransportState");
			if(status != null) {
				return status.getValue().toString();
			}
		}

		return null;

	}

	public boolean addURIToQueue(String URI, String meta,int desiredFirstTrack, boolean enqueueAsNext) {

		if(isConfigured() && URI != null && meta != null) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("AddURIToQueue");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("InstanceID","0");
				invocation.setInput("EnqueuedURI",URI);
				invocation.setInput("EnqueuedURIMetaData",meta);
				invocation.setInput("DesiredFirstTrackNumberEnqueued",new UnsignedIntegerFourBytes(desiredFirstTrack));
				invocation.setInput("EnqueueAsNext",enqueueAsNext);

			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;
		}

		return false;
	}

	public String getCurrentURI(){
		updateMediaInfo();
		if(stateMap != null && isConfigured()) {
			StateVariableValue status = stateMap.get("CurrentURI");
			if(status != null) {
				return status.getValue().toString();
			}
		}
		return null;

	}

	public long getCurrenTrackNr() {

		if(stateMap != null && isConfigured()) {

			updatePosition();
			if(stateMap != null) {
				StateVariableValue variable = stateMap.get("Track");
				if(variable != null) {
					return ((UnsignedIntegerFourBytes)variable.getValue()).getValue();
				}
			}	
		}
		return (long) -1;
	}


	public boolean updateMediaInfo(){

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("GetMediaInfo");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("InstanceID","0");
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;
		}

		return false;

	}

	public boolean addURIToQueue(SonosEntry newEntry) {
		return addURIToQueue(newEntry.getRes(),SonosXMLParser.compileMetadataString(newEntry),1,true);
	}

	public boolean setCurrentURI(String URI, String URIMetaData ) {
		if(URI != null && URIMetaData != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("SetAVTransportURI");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("InstanceID","0");
				invocation.setInput("CurrentURI",URI);
				invocation.setInput("CurrentURIMetaData", URIMetaData);
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;			

		} else {
			return false;
		}
	}

	public boolean setCurrentURI(SonosEntry newEntry) {
		return setCurrentURI(newEntry.getRes(),SonosXMLParser.compileMetadataString(newEntry));
	}

	public boolean updateCurrentURIFormatted() {
		
		if(stateMap != null && isConfigured()) {

			String currentURI = null;
			SonosMetaData currentURIMetaData = null;
			SonosMetaData currentTrack = null;

			if(!isGroupCoordinator()) {
				currentURI = getCoordinator().getCurrentURI();
				currentURIMetaData = getCoordinator().getCurrentURIMetadata();
				currentTrack = getCoordinator().getTrackMetadata();

			} else {
				currentURI = getCurrentURI();
				currentURIMetaData = getCurrentURIMetadata();
				currentTrack = getTrackMetadata();
			}

			if(currentURI != null) {

				String resultString = null;
				String artist = null;
				String album = null;
				String title = null;

				if(currentURI.contains("x-sonosapi-stream")) {
					//TODO: Get partner ID for openhab.org

					String stationID = StringUtils.substringBetween(currentURI, ":s", "?sid");

					StateVariable newVariable = new StateVariable("StationID",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));
					StateVariableValue newValue = new StateVariableValue(newVariable, stationID);

					if(this.isUpdatedValue("StationID", newValue) || lastOPMLQuery ==null || lastOPMLQuery.plusMinutes(1).isBeforeNow()) {

						processStateVariableValue(newVariable.getName(),newValue);	

						String url = "http://opml.radiotime.com/Describe.ashx?c=nowplaying&id=" + stationID + "&partnerId=IAeIhU42&serial=" + getMACAddress();

						String response = HttpUtil.executeUrl("GET", url, SO_TIMEOUT);
						//logger.debug("RadioTime Response: {}",response);

						lastOPMLQuery = DateTime.now();

						List<String> fields = null;
						try {
							fields = SonosXMLParser.getRadioTimeFromXML(response);
						} catch (SAXException e) {
							logger.error("Could not parse RadioTime from String {}",response);
						}

						resultString = new String();

						if(fields != null && fields.size()>1) {

							artist = fields.get(0);
							title = fields.get(1);
							
							Iterator<String> listIterator = fields.listIterator();
							while(listIterator.hasNext()){
								String field = listIterator.next();
								resultString = resultString + field;
								if(listIterator.hasNext()) {
									resultString = resultString + " - ";
								}
							}
						}
					} else {
						resultString = stateMap.get("CurrentURIFormatted").getValue().toString();
						title = stateMap.get("CurrentTitle").getValue().toString();
						artist = stateMap.get("CurrentArtist").getValue().toString();
					}


				} else {
					if(currentTrack != null) {
						if(currentTrack.getResource().contains("x-rincon-stream")) {
							title = currentTrack.getTitle();
							album = " ";
							artist = " ";
							resultString = title;
						} else if(!currentTrack.getResource().contains("x-sonosapi-stream")) {
							if (currentTrack.getAlbumArtist().equals("")) {
								resultString = currentTrack.getCreator() + " - " + currentTrack.getAlbum() + " - " + currentTrack.getTitle();
								artist = currentTrack.getCreator();
							} else {
								resultString = currentTrack.getAlbumArtist() + " - " + currentTrack.getAlbum() + " - " + currentTrack.getTitle();
								artist = currentTrack.getAlbumArtist();
							}

							album = currentTrack.getAlbum();
							title = currentTrack.getTitle();
							
							if(album.equals("")) {
								album= " ";
							}

							if(artist.equals("")) {
								artist= " ";
							}
						}
					} else {
						title=" ";
						album = " ";
						artist = " ";
						resultString = " ";
					}
				}

				StateVariable newVariable = new StateVariable("CurrentURIFormatted",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));
				StateVariableValue newValue = new StateVariableValue(newVariable, resultString);
				processStateVariableValue(newVariable.getName(),newValue);		

				// update individual variables
				newVariable = new StateVariable("CurrentArtist",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));

				if (artist != null) {
					newValue = new StateVariableValue(newVariable, artist);
				} else {
					newValue = new StateVariableValue(newVariable, " ");
				}
				processStateVariableValue(newVariable.getName(), newValue);		

				newVariable = new StateVariable("CurrentTitle",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));
				if (title != null) {
					newValue = new StateVariableValue(newVariable, title);
				} else {
					newValue = new StateVariableValue(newVariable, " ");
				}
				processStateVariableValue(newVariable.getName(), newValue);		

				newVariable = new StateVariable("CurrentAlbum",new StateVariableTypeDetails(Datatype.Builtin.STRING.getDatatype()));
				if (album != null) {
					newValue = new StateVariableValue(newVariable, album);
				} else {
					newValue = new StateVariableValue(newVariable, " ");
				}
				processStateVariableValue(newVariable.getName(), newValue);		

				return true;


			}
		}

		return false;
	}

	public String getCurrentURIFormatted(){
		updateCurrentURIFormatted();
		if(stateMap != null && isConfigured()) {
			StateVariableValue status = stateMap.get("CurrentURIFormatted");
			if(status != null) {
				return status.getValue().toString();
			}
		}

		return null;

	}

	public String getCurrentURIMetadataAsString() {
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("CurrentTrackMetaData");
			if(value != null) {
				return value.getValue().toString();
			}
		}
		return null;

	}


	public SonosMetaData getCurrentURIMetadata(){
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("CurrentURIMetaData");
			SonosMetaData currentTrack = null;
			if(value != null) {
				try {
					if(((String)value.getValue()).length()!=0) {
						currentTrack = SonosXMLParser.getMetaDataFromXML((String)value.getValue());
					}
				} catch (SAXException e) {
					logger.error("Could not parse MetaData from String {}",value.getValue().toString());
				}
				return currentTrack;
			} else {
				return null;
			}
		} else {
			return null;
		}		
	}

	public SonosMetaData getTrackMetadata(){
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("CurrentTrackMetaData");
			SonosMetaData currentTrack = null;
			if(value != null) {
				try {
					if(((String)value.getValue()).length()!=0) {
						currentTrack = SonosXMLParser.getMetaDataFromXML((String)value.getValue());
					}
				} catch (SAXException e) {
					logger.error("Could not parse MetaData from String {}",value.getValue().toString());
				}
				return currentTrack;
			} else {
				return null;
			}
		} else {
			return null;
		}		
	}

	public SonosMetaData getEnqueuedTransportURIMetaData(){
		if(stateMap != null && isConfigured()) {
			StateVariableValue value = stateMap.get("EnqueuedTransportURIMetaData");
			SonosMetaData currentTrack = null;
			if(value != null) {
				try {
					if(((String)value.getValue()).length()!=0) {
						currentTrack = SonosXMLParser.getMetaDataFromXML((String)value.getValue());
					}
				} catch (SAXException e) {
					logger.error("Could not parse MetaData from String {}",value.getValue().toString());
				}
				return currentTrack;
			} else {
				return null;
			}
		} else {
			return null;
		}		
	}


	public String getCurrentVolume(){
		if(stateMap != null && isConfigured()) {
			StateVariableValue status = stateMap.get("VolumeMaster");
			return status.getValue().toString();
		} else {
			return null;
		}		
	}

	protected List<SonosEntry> getEntries(String type, String filter){

		List<SonosEntry> resultList = null;


		if(isConfigured()) {

			long startAt = 0;

			Service service = device.findService(new UDAServiceId("ContentDirectory"));
			Action action = service.getAction("Browse");
			ActionInvocation invocation = new ActionInvocation(action);
			try {
				invocation.setInput("ObjectID",type);
				invocation.setInput("BrowseFlag","BrowseDirectChildren");
				invocation.setInput("Filter", filter);
				invocation.setInput("StartingIndex",new UnsignedIntegerFourBytes(startAt));
				invocation.setInput("RequestedCount",new UnsignedIntegerFourBytes( 200));
				invocation.setInput("SortCriteria","");
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}
			// Execute this action synchronously 
			new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();

			Long totalMatches  = ((UnsignedIntegerFourBytes) invocation.getOutput("TotalMatches").getValue()).getValue();
			Long initialNumberReturned  = ((UnsignedIntegerFourBytes) invocation.getOutput("NumberReturned").getValue()).getValue();
			String initialResult = (String) invocation.getOutput("Result").getValue();

			try {
				resultList = SonosXMLParser.getEntriesFromString(initialResult);
			} catch (SAXException e) {
				logger.error("Could not parse Entries from String {}",initialResult);
			}

			startAt = startAt + initialNumberReturned;

			while(startAt<totalMatches){
				invocation = new ActionInvocation(action);
				try {
					invocation.setInput("ObjectID",type);
					invocation.setInput("BrowseFlag","BrowseDirectChildren");
					invocation.setInput("Filter", filter);
					invocation.setInput("StartingIndex",new UnsignedIntegerFourBytes(startAt));
					invocation.setInput("RequestedCount",new UnsignedIntegerFourBytes( 200));
					invocation.setInput("SortCriteria","");
				} catch (InvalidValueException ex) {
					logger.error("Action Invalid Value Exception {}",ex.getMessage());
				} catch (NumberFormatException ex) {
					logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
				}
				// Execute this action synchronously 
				new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
				String result = (String) invocation.getOutput("Result").getValue();
				int numberReturned  = (Integer) invocation.getOutput("NumberReturned").getValue();

				try {
					resultList.addAll(SonosXMLParser.getEntriesFromString(result));
				} catch (SAXException e) {
					logger.error("Could not parse Entries from String {}",result);
				}
				startAt = startAt + numberReturned;
			}
		}

		return resultList;


	}

	public List<SonosEntry> getArtists( String filter){
		return getEntries("A:",filter);
	}

	public List<SonosEntry> getArtists(){
		return getEntries("A:","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosEntry> getAlbums(String filter){
		return getEntries("A:ALBUM",filter);
	}

	public List<SonosEntry> getAlbums(){
		return getEntries("A:ALBUM","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosEntry> getTracks( String filter){
		return getEntries("A:TRACKS",filter);
	}

	public List<SonosEntry> getTracks(){
		return getEntries("A:TRACKS","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosEntry> getQueue(String filter){
		return getEntries("Q:0",filter);
	}

	public List<SonosEntry> getQueue(){
		return getEntries("Q:0","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosEntry> getPlayLists(String filter){
		return getEntries("SQ:",filter);
	}

	public List<SonosEntry> getPlayLists(){
		return getEntries("SQ:","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosEntry> getFavoriteRadios(String filter){
		return getEntries("R:0/0",filter);
	}

	public List<SonosEntry> getFavoriteRadios(){
		return getEntries("R:0/0","dc:title,res,dc:creator,upnp:artist,upnp:album");
	}

	public List<SonosAlarm> getCurrentAlarmList(){


		List<SonosAlarm> sonosAlarms = null;

		if(isConfigured()) {

			Service service = device.findService(new UDAServiceId("AlarmClock"));
			Action action = service.getAction("ListAlarms");
			ActionInvocation invocation = new ActionInvocation(action);

			executeActionInvocation(invocation);

			try {
				sonosAlarms = SonosXMLParser.getAlarmsFromStringResult(invocation.getOutput("CurrentAlarmList").toString());
			} catch (SAXException e) {
				logger.error("Could not parse Alarms from String {}",invocation.getOutput("CurrentAlarmList").toString());
			}
		}

		return sonosAlarms;
	}

	public boolean updateAlarm(SonosAlarm alarm) {
		if(alarm != null && isConfigured()) {
			Service service = device.findService(new UDAServiceId("AlarmClock"));
			Action action = service.getAction("ListAlarms");
			ActionInvocation invocation = new ActionInvocation(action);


			DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");

			PeriodFormatter pFormatter= new PeriodFormatterBuilder()
			.printZeroAlways()
			.appendHours()
			.appendSeparator(":")
			.appendMinutes()
			.appendSeparator(":")
			.appendSeconds()
			.toFormatter();


			try {
				invocation.setInput("ID",Integer.toString(alarm.getID()));
				invocation.setInput("StartLocalTime",formatter.print(alarm.getStartTime()));
				invocation.setInput("Duration",pFormatter.print(alarm.getDuration()));
				invocation.setInput("Recurrence",alarm.getRecurrence());
				invocation.setInput("RoomUUID",alarm.getRoomUUID());
				invocation.setInput("ProgramURI",alarm.getProgramURI());
				invocation.setInput("ProgramMetaData",alarm.getProgramMetaData());
				invocation.setInput("PlayMode",alarm.getPlayMode());
				invocation.setInput("Volume",Integer.toString(alarm.getVolume()));
				if(alarm.getIncludeLinkedZones()) {
					invocation.setInput("IncludeLinkedZones","1");
				} else {
					invocation.setInput("IncludeLinkedZones","0");
				}

				if(alarm.getEnabled()) {
					invocation.setInput("Enabled", "1");	        		
				} else {
					invocation.setInput("Enabled", "0");	        		
				}
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;
		}
		else {
			return false;
		}

	}

	public boolean setAlarm(String alarmSwitch) {
		if(alarmSwitch.equals("ON") || alarmSwitch.equals("OPEN") || alarmSwitch.equals("UP") ) {
			return setAlarm(true);	        		
		} else 

			if(alarmSwitch.equals("OFF") || alarmSwitch.equals("CLOSED") || alarmSwitch.equals("DOWN") ) {
				return setAlarm(false);        		
			} else {
				return false;
			}
	}

	public boolean setAlarm(boolean alarmSwitch) {

		List<SonosAlarm> sonosAlarms = getCurrentAlarmList();

		if(isConfigured()) {

			// find the nearest alarm - take the current time from the Sonos System, not the system where openhab is running

			String currentLocalTime = getTime();
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

			DateTime currentDateTime = fmt.parseDateTime(currentLocalTime);

			Duration shortestDuration = Period.days(10).toStandardDuration();
			SonosAlarm firstAlarm = null;

			for(SonosAlarm anAlarm : sonosAlarms) {
				Duration duration = new Duration(currentDateTime,anAlarm.getStartTime());
				if(anAlarm.getStartTime().isBefore(currentDateTime.plus(shortestDuration)) && anAlarm.getRoomUUID().equals(udn.getIdentifierString())) {
					shortestDuration = duration;
					firstAlarm = anAlarm;
				}
			}

			// Set the Alarm
			if(firstAlarm != null) {

				if(alarmSwitch) {
					firstAlarm.setEnabled(true);
				} else {
					firstAlarm.setEnabled(false);
				}

				return updateAlarm(firstAlarm);

			} else {
				return false;
			}
		} else {
			return false;
		}


	}

	public boolean snoozeAlarm(int minutes){
		if(isAlarmRunning() && isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("SnoozeAlarm");
			ActionInvocation invocation = new ActionInvocation(action);

			Period snoozePeriod = Period.minutes(minutes);
			PeriodFormatter pFormatter= new PeriodFormatterBuilder()
			.printZeroAlways()
			.appendHours()
			.appendSeparator(":")
			.appendMinutes()
			.appendSeparator(":")
			.appendSeconds()
			.toFormatter();

			try {
				invocation.setInput("Duration",pFormatter.print(snoozePeriod));
			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}

			executeActionInvocation(invocation);

			return true;

		} else {
			logger.warn("There is no alarm running on {} ",this);
			return false;
		}
	}

	public boolean publicAddress(){

		//check if sourcePlayer has a line-in connected
		if(isLineInConnected() && isConfigured()) {

			//first remove this player from its own group if any
			becomeStandAlonePlayer();

			List<SonosZoneGroup> currentSonosZoneGroups = new ArrayList<SonosZoneGroup>(sonosBinding.getSonosZoneGroups().size());
			for(SonosZoneGroup grp : sonosBinding.getSonosZoneGroups()){
				currentSonosZoneGroups.add((SonosZoneGroup) grp.clone());
			}

			//add all other players to this new group
			for(SonosZoneGroup group : currentSonosZoneGroups){
				for(String player : group.getMembers()){
					SonosZonePlayer somePlayer = sonosBinding.getPlayerForID(player);
					if(somePlayer != this){
						somePlayer.becomeStandAlonePlayer();
						somePlayer.stop();
						addMember(somePlayer);
					}
				}
			}


			//set the URI of the group to the line-in
			//TODO : check if this needs to be set on the group coordinator or can be done on any member
			SonosZonePlayer coordinator = getCoordinator();
			SonosEntry entry = new SonosEntry("", "", "", "", "", "", "", "x-rincon-stream:"+udn.getIdentifierString());
			coordinator.setCurrentURI(entry);
			coordinator.play();

			return true;

		} else {
			logger.warn("Line-in of {} is not connected",this);
			return false;
		}

	}

	public boolean saveQueue(String name, String queueID) {

		if(name != null && queueID != null && isConfigured()) {

			Service service = device.findService(new UDAServiceId("AVTransport"));
			Action action = service.getAction("SaveQueue");
			ActionInvocation invocation = new ActionInvocation(action);

			try {
				invocation.setInput("Title", name);	        		
				invocation.setInput("ObjectID", queueID);	        		

			} catch (InvalidValueException ex) {
				logger.error("Action Invalid Value Exception {}",ex.getMessage());
			} catch (NumberFormatException ex) {
				logger.error("Action Invalid Value Format Exception {}",ex.getMessage());	
			}
			executeActionInvocation(invocation);

			return true;			

		} else {
			return false;
		}

	}

	/**
	 * 	Play a given url to music in one of the music libraries.
	 * 
	 * 	@param url in the format of //host/folder/filename.mp3
	 * 	@return true if the url started to play
	 */
	public boolean playURI(String url) {
		if (!isConfigured) {
			return false;
		}

		SonosZonePlayer coordinator = sonosBinding
				.getCoordinatorForZonePlayer(this);

		// stop whatever is currently playing
		coordinator.stop();

		// clear any tracks which are pending in the queue
		coordinator.removeAllTracksFromQueue();

		// add the new track we want to play to the queue
		if (!url.startsWith("x-")) {
			// default to file based url
			url = "x-file-cifs:" + url;
		}
		coordinator.addURIToQueue(url, "", 0, true);

		// set the current playlist to our new queue
		coordinator.setCurrentURI("x-rincon-queue:" + udn.getIdentifierString()
				+ "#0", "");

		// take the system off mute
		coordinator.setMute("OFF");

		// start jammin'
		return coordinator.play();

	}

	/**
	 * 	Play music from the line-in of the Player given its name or UDN
	 * 
	 * 	@param udn
	 * 	@return true if the sonos device started to play
	 */
	public boolean playLineIn(String remotePlayerName) {
		if (!isConfigured) {
			return false;
		}

		SonosZonePlayer coordinator = sonosBinding.getCoordinatorForZonePlayer(this);
		SonosZonePlayer remotePlayer = sonosBinding.getPlayerForID(remotePlayerName);

		// stop whatever is currently playing
		coordinator.stop();

		// set the
		coordinator.setCurrentURI("x-rincon-stream:" + remotePlayer.getUdn().getIdentifierString(), "");

		// take the system off mute
		coordinator.setMute("OFF");

		// start jammin'
		return coordinator.play();

	}


	/**
	 *	Clear all scheduled music from the current queue.
	 * 
	 * @return true if no error occurred.
	 */
	public boolean removeAllTracksFromQueue() {

		if (!isConfigured) {
			return false;
		}

		Service service = device.findService(new UDAServiceId("AVTransport"));
		Action action = service.getAction("RemoveAllTracksFromQueue");
		ActionInvocation invocation = new ActionInvocation(action);

		try {
			invocation.setInput("InstanceID", "0");

		} catch (InvalidValueException ex) {
			logger.error("Action Invalid Value Exception {}", ex.getMessage());
		} catch (NumberFormatException ex) {
			logger.error("Action Invalid Value Format Exception {}",
					ex.getMessage());
		}

		executeActionInvocation(invocation);

		return true;

	}

	/**
	 *	Save the state (track, position etc) of the Sonos Zone player.
	 * 
	 * @return true if no error occurred.
	 */
	protected boolean saveState() {

		synchronized (this) {

			savedState = sonosBinding.new SonosZonePlayerState();
			String currentURI = getCurrentURI();

			if (currentURI != null) {

				if (currentURI.contains("x-sonosapi-stream:")) {
					// we are streaming music
					SonosMetaData track = getTrackMetadata();
					SonosMetaData current = getCurrentURIMetadata();
					if (track != null) {
						savedState.entry = new SonosEntry("",
								current.getTitle(), "", "",
								track.getAlbumArtUri(), "",
								current.getUpnpClass(), currentURI);
					}
				} else if (currentURI.contains("x-rincon:")) {
					// we are a slave to some coordinator
					savedState.entry = new SonosEntry("", "", "", "",
							"", "", "", currentURI);
				} else if (currentURI.contains("x-rincon-stream:")) {
					// we are streaming from the Line In connection
					savedState.entry = new SonosEntry("", "", "", "",
							"", "", "", currentURI);
				} else if (currentURI.contains("x-rincon-queue:")) {
					// we are playing something that sits in the queue
					SonosMetaData queued = getEnqueuedTransportURIMetaData();
					if (queued != null) {

						savedState.track = getCurrenTrackNr();

						if (queued.getUpnpClass().contains(
								"object.container.playlistContainer")) {
							// we are playing a real 'saved' playlist
							List<SonosEntry> playLists = getPlayLists();
							for (SonosEntry someList : playLists) {
								if (someList.getTitle().equals(
										queued.getTitle())) {
									savedState.entry = new SonosEntry(
											someList.getId(),
											someList.getTitle(),
											someList.getParentId(), "",
											"", "",
											someList.getUpnpClass(),
											someList.getRes());
									break;
								}
							}

						} else if (queued.getUpnpClass().contains(
								"object.container")) {
							// we are playing some other sort of
							// 'container' - we will save that to a
							// playlist for our convenience
							logger.debug(
									"Save State for a container of type {}",
									queued.getUpnpClass());

							// save the playlist
							String existingList = "";
							List<SonosEntry> playLists = getPlayLists();
							for (SonosEntry someList : playLists) {
								if (someList.getTitle().equals(
										"openHAB-" + getUdn())) {
									existingList = someList.getId();
									break;
								}
							}

							saveQueue(
									"openHAB-" + getUdn(),
									existingList);

							// get all the playlists and a ref to our
							// saved list
							playLists = getPlayLists();
							for (SonosEntry someList : playLists) {
								if (someList.getTitle().equals(
										"openHAB-" + getUdn())) {
									savedState.entry = new SonosEntry(
											someList.getId(),
											someList.getTitle(),
											someList.getParentId(), "",
											"", "",
											someList.getUpnpClass(),
											someList.getRes());
									break;
								}
							}

						}
					} else {
						savedState.entry = new SonosEntry("", "", "",
								"", "", "", "", "x-rincon-queue:"
										+ getUdn()
										.getIdentifierString()
										+ "#0");
					}
				}

				savedState.transportState = getTransportState();
				savedState.volume = getCurrentVolume();
				savedState.relTime = getPosition();
			} else {
				savedState.entry = null;
			}
			
			return true;
		}
	}

	/**
	 *	Restore the state (track, position etc) of the Sonos Zone player.
	 * 
	 * @return true if no error occurred.
	 */
	protected boolean restoreState() {

		synchronized (this) {
			if (savedState != null) {
				// put settings back
				setVolume(savedState.volume);

				if (isCoordinator()) {
					if (savedState.entry != null) {
						// check if we have a playlist to deal with
						if (savedState.entry
								.getUpnpClass()
								.contains(
										"object.container.playlistContainer")) {

							addURIToQueue(
									savedState.entry.getRes(),
									SonosXMLParser
									.compileMetadataString(savedState.entry),
									0, true);
							SonosEntry entry = new SonosEntry(
									"",
									"",
									"",
									"",
									"",
									"",
									"",
									"x-rincon-queue:"
											+ getUdn()
											.getIdentifierString()
											+ "#0");
							setCurrentURI(entry);
							setPositionTrack(savedState.track);

						} else {
							setCurrentURI(savedState.entry);
							setPosition(savedState.relTime);
						}

						if (savedState.transportState
								.equals("PLAYING")) {
							play();
						} else if (savedState.transportState
								.equals("STOPPED")) {
							stop();
						} else if (savedState.transportState
								.equals("PAUSED_PLAYBACK")) {
							pause();
						}
					}
				}
			}
			
			return true;
		}
	}

}