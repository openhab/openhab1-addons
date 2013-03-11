/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.sonos.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sonos.SonosBindingProvider;
import org.openhab.binding.sonos.SonosCommandType;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.DefaultUpnpServiceConfiguration;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.message.header.UDADeviceTypeHeader;
import org.teleal.cling.model.message.header.UDNHeader;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;
import org.teleal.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.teleal.cling.transport.impl.apache.StreamClientImpl;
import org.teleal.cling.transport.impl.apache.StreamServerConfigurationImpl;
import org.teleal.cling.transport.impl.apache.StreamServerImpl;
import org.teleal.cling.transport.spi.NetworkAddressFactory;
import org.teleal.cling.transport.spi.StreamClient;
import org.teleal.cling.transport.spi.StreamServer;
import org.xml.sax.SAXException;

/**
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
public class SonosBinding extends AbstractBinding<SonosBindingProvider> implements ManagedService {
	
	private static Logger logger = LoggerFactory.getLogger(SonosBinding.class);
	
	private static final Pattern EXTRACT_SONOS_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(udn)$");
	
	private Map<String, SonosZonePlayer> sonosZonePlayerCache = Collections.synchronizedMap(new HashMap<String,SonosZonePlayer>());
	
	private List<SonosZoneGroup> sonosZoneGroups = null;

	static protected UpnpService upnpService;
	static protected SonosBinding self;
	
	static protected Integer interval = 600;
	static protected boolean bindingStarted = false;
	
	
	private Map<String,SonosZonePlayerState> sonosSavedPlayerState = null;
	private List<SonosZoneGroup> sonosSavedGroupState = null;
	

	
	public class SonosUpnpServiceConfiguration extends
	DefaultUpnpServiceConfiguration {

		@SuppressWarnings("rawtypes")
		@Override
		public StreamClient createStreamClient() {
			return new StreamClientImpl(new StreamClientConfigurationImpl());
		}

		@SuppressWarnings("rawtypes")
		@Override
		public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
			return new StreamServerImpl(
					new StreamServerConfigurationImpl(
							networkAddressFactory.getStreamListenPort()
							)
					);
		}

	}
	
	RegistryListener listener = new RegistryListener() {

        public void remoteDeviceDiscoveryStarted(Registry registry,
                                                 RemoteDevice device) {
            logger.debug(
                    "Discovery started: " + device.getDisplayString()
            );
        }

        public void remoteDeviceDiscoveryFailed(Registry registry,
                                                RemoteDevice device,
                                                Exception ex) {
            logger.debug(
                    "Discovery failed: " + device.getDisplayString() + " => " + ex
            );
        }

        @SuppressWarnings("rawtypes")
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            logger.debug(
                    "Remote device available: " + device.getDisplayString()
            );

            UDN udn = device.getIdentity().getUdn();
            boolean existingDevice = false;
            
            // Check if we already received a configuration for this device through the .cfg
            for(String item : sonosZonePlayerCache.keySet()){
            	SonosZonePlayer sonosConfig = sonosZonePlayerCache.get(item);
            	if(sonosConfig.getUdn().equals(udn)) {
            		// We already have an (empty) config, populate it
            		logger.debug("Found UPNP device {} matchig a pre-defined config {}",device,sonosConfig);
            		sonosConfig.setDevice(device);
            		sonosConfig.setService(upnpService);
            	            		
            		existingDevice = true;
            	}
            }
            
            if(!existingDevice) {
            	// Add device to the cached Configs
            	SonosZonePlayer newConfig = new SonosZonePlayer(self);
            	newConfig.setUdn(udn);
            	newConfig.setDevice(device);
            	newConfig.setService(upnpService);
            	
        		String sonosID = StringUtils.substringAfter(newConfig.getUdn().toString(),":");

            	sonosZonePlayerCache.put(sonosID, newConfig);
            	logger.debug("Added a new ZonePlayer with ID {} as configuration for device {}",sonosID,newConfig);
            
            }
            
            
        	// add GENA service to capture zonegroup information
        	Service service = device.findService(new UDAServiceId("ZoneGroupTopology"));			
    		SonosSubscriptionCallback callback = new SonosSubscriptionCallback(service,interval);
    		upnpService.getControlPoint().execute(callback);  
			//logger.debug("Added a GENA Subscription in the Sonos Binding for service {} on device {}",service,device);

 
        }

        public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
            logger.debug(
                    "Remote device updated: " + device.getDisplayString()
            );
        }

        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            logger.debug(
                    "Remote device removed: " + device.getDisplayString()
            );       
        }

        public void localDeviceAdded(Registry registry, LocalDevice device) {
            logger.debug(
                    "Local device added: " + device.getDisplayString()
            );
        }

        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            logger.debug(
                    "Local device removed: " + device.getDisplayString()
            );
        }

        public void beforeShutdown(Registry registry) {
            logger.debug(
                    "Before shutdown, the registry has devices: "
                    + registry.getDevices().size()
            );
        }

        public void afterShutdown() {
            logger.debug("Shutdown of registry complete!");

        }
    };
        
    public SonosBinding(){
    	// TODO 
    	self=this;
    }
    
    /**
	 * Find the first matching {@link ChannelBindingProvider}
	 * according to <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected SonosBindingProvider findFirstMatchingBindingProvider(String itemName) {
		SonosBindingProvider firstMatchingProvider = null;
		for (SonosBindingProvider provider : providers) {
			List<String> sonosIDs = provider.getSonosID(itemName);
			if (sonosIDs != null && sonosIDs.size() > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}
	 
	@Override
	protected void internalReceiveCommand(String itemName,
			Command command) {

		SonosBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String commandAsString = command.toString();

		if(command != null){
						
			List<Command> commands = new ArrayList<Command>();

			if(command instanceof StringType || command instanceof DecimalType ){
				commands = provider.getVariableCommands(itemName);
			} else {
				commands.add(command);
			}
			
			for(Command someCommand : commands) {
				
				 String sonosID = provider.getSonosID(itemName,someCommand);
				 Direction direction = provider.getDirection(itemName,someCommand);
				 SonosCommandType sonosCommandType = provider.getSonosCommandType(itemName,someCommand,direction);

				 if(sonosID != null && direction != null) {
					 if(sonosCommandType != null){
						 if(direction.equals(Direction.OUT) | direction.equals(Direction.BIDIRECTIONAL)) {
							 executeCommand(itemName, someCommand, sonosID,sonosCommandType,commandAsString);
						 } else {
							 logger.error(
									 "wrong command direction for binding [Item={}, command={}]",
									 itemName, commandAsString);
						 }
					 } else {
						 logger.error(
								 "wrong command type for binding [Item={}, command={}]",
								 itemName, commandAsString);
					 }
				 } else {
					 logger.error("{} is an unrecognised command for Item {}",commandAsString,itemName);
				 }
			}
			
		}

	}
	
	@SuppressWarnings("unchecked")
	private Type createStateForType(SonosCommandType ctype, String value) throws BindingConfigParseException {
		
		if(ctype != null && value != null) {

		Class<? extends Type> typeClass  = ctype.getTypeClass();
		List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();

		stateTypeList.add((Class<? extends State>) typeClass);
		
		String finalValue = value;
		
		// Note to Kai or Thomas: sonos devices return some "true" "false" values for specific variables. We convert those
		// into  ON OFF if the commandTypes allow so. This is a little hack, but IMHO OnOffType should
		// be enhanced, or a TrueFalseType should be developed
		if(typeClass.equals(OnOffType.class)) {
			finalValue = StringUtils.upperCase(value);
			if(finalValue.equals("TRUE")) {
				finalValue = "ON";
			} else if (finalValue.equals("FALSE")) {
				finalValue = "OFF";
			}
		}

		State state = TypeParser.parseState(stateTypeList, finalValue);

		return state;	
		}
		else {
			return null;
		}
	}
	
	private String createStringFromCommand(Command command,String commandAsString) {
		
		String value = null;		
		
		if(command instanceof StringType || command instanceof DecimalType) {
			value = commandAsString;
		} else {
			 value = command.toString();
		}
		
		return value;
		
	}
    
	@SuppressWarnings("rawtypes")
	public void processVariableMap(RemoteDevice device, Map<String, StateVariableValue> values) {

		if(device!=null && values != null) {

			// get the device linked to this service linked to this subscription
			String sonosID = getSonosIDforDevice(device);

			for(String stateVariable : values.keySet()){

				// find all the CommandTypes that are defined for each StateVariable
				List<SonosCommandType> supportedCommands = SonosCommandType.getCommandByVariable(stateVariable);
			
				StateVariableValue status = values.get(stateVariable);

				for(SonosCommandType sonosCommandType : supportedCommands){
					
					// create a new State based on the type of Sonos Command and the status value in the map
					Type newState = null;
					try {
						newState = createStateForType(sonosCommandType,status.getValue().toString());
					} catch (BindingConfigParseException e) {
						logger.error("Error parsing a value {} to a state variable of type {}",status.toString(),sonosCommandType.getTypeClass().toString());
					}

					for(SonosBindingProvider provider : providers) {
						List<String> qualifiedItems = provider.getItemNames(sonosID,sonosCommandType);
						for(String anItem : qualifiedItems){
							// get the openHAB commands attached to each Item at this given Provider
							List<Command> commands = provider.getCommands(anItem,sonosCommandType);
							for(Command aCommand : commands){
								Direction theDirection = provider.getDirection(anItem, aCommand);
								Direction otherDirection = sonosCommandType.getDirection();
								if((theDirection == Direction.IN || theDirection == Direction.BIDIRECTIONAL)&&(otherDirection != Direction.OUT)){

									if(newState != null) {
										if(newState.equals((State) aCommand) || newState instanceof StringType || newState instanceof DecimalType) {
											eventPublisher.postUpdate(anItem, (State) newState);		
										}
									}
									else {
										throw new IllegalClassException("Cannot process update for the command of type " + sonosCommandType.toString());
									}

								}			        					
							}
						}
					}		
				}
			}
		}
	}
	
	protected class SonosSubscriptionCallback extends SubscriptionCallback {

			
			@SuppressWarnings("rawtypes")
			public SonosSubscriptionCallback(Service service, Integer interval) {
				super(service,interval);
			}

		    @SuppressWarnings("rawtypes")
			@Override
		    public void established(GENASubscription sub) {
		        //logger.debug("Established: " + sub.getSubscriptionId());
		    }

		    @SuppressWarnings("rawtypes")
			@Override
		    protected void failed(GENASubscription subscription,
		                          UpnpResponse responseStatus,
		                          Exception exception,
		                          String defaultMsg) {
		        logger.error(defaultMsg);
		    }

		    @SuppressWarnings({ "rawtypes", "unchecked" })
			public void eventReceived(GENASubscription sub) {
		    	
		    	// get the device linked to this service linked to this subscription

		    	Map<String, StateVariableValue> values = sub.getCurrentValues();

		    	// now, lets deal with the specials - some UPNP responses require some XML parsing
		    	// or we need to update our internal data structure
		    	// or are things we want to store for further reference
		    	for(String stateVariable : values.keySet()){

		    		if(stateVariable.equals("ZoneGroupState")){
		    			try {
		    				setSonosZoneGroups(SonosXMLParser.getZoneGroupFromXML(values.get(stateVariable).toString()));
		    			} catch (SAXException e) {
		    				logger.error("Could not parse XML variable {}",values.get(stateVariable).toString());
		    			}
		    		}
		    	}
		    }

		    @SuppressWarnings("rawtypes")
			public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
		    	logger.warn("Missed events: " + numberOfMissedEvents);
		    }

			@SuppressWarnings("rawtypes")
			@Override
			protected void ended(GENASubscription subscription,
					CancelReason reason, UpnpResponse responseStatus) {
				// TODO Auto-generated method stub
				
			}
	}

	
	private class SonosZonePlayerState {
		
		public String transportState;
		public String volume;
		public String relTime;
		public SonosEntry entry;
		public long track;
	}
	
	protected boolean savePlayerState() {
		
		synchronized(this)
		{		
			if(sonosSavedGroupState != null && sonosSavedPlayerState != null) {
				// TODO issue warning

			} 

			sonosSavedPlayerState = new HashMap<String,SonosZonePlayerState>();
			sonosSavedGroupState = new ArrayList<SonosZoneGroup>();

			for(SonosZoneGroup group : getSonosZoneGroups()) {
				sonosSavedGroupState.add((SonosZoneGroup) group.clone());
			}

			for(SonosZoneGroup group : sonosSavedGroupState) {
				for(String playerName : group.getMembers()) {
					SonosZonePlayer player = getPlayerForID(playerName);

					SonosZonePlayerState saveState = new SonosZonePlayerState();

					String currentURI = player.getCurrentURI();

					if(currentURI != null) {

						if(currentURI.contains("x-sonosapi-stream:")) {
							// we are streaming music
							SonosMetaData track = player.getTrackMetadata();
							SonosMetaData current = player.getCurrentURIMetadata();
							if(track != null) {
								saveState.entry = new SonosEntry("", current.getTitle(), "", "", track.getAlbumArtUri(), "", current.getUpnpClass(), currentURI);
							}		
						} else if(currentURI.contains("x-rincon:")) {
							// we are a slave to some coordinator
							saveState.entry = new SonosEntry("", "", "", "", "", "", "", currentURI);
						} else if(currentURI.contains("x-rincon-stream:")) {
							// we are streaming from the Line In connection
							saveState.entry = new SonosEntry("", "", "", "", "", "", "", currentURI);
						} else if(currentURI.contains("x-rincon-queue:")) {
							// we are playing something that sits in the queue
							SonosMetaData queued = player.getEnqueuedTransportURIMetaData();
							if(queued != null) {
								
								saveState.track = player.getCurrenTrackNr(); 
								
								if(queued.getUpnpClass().contains("object.container.playlistContainer")) {
									// we are playing a real 'saved' playlist
									List<SonosEntry>playLists  = player.getPlayLists();
									for(SonosEntry someList : playLists) {
										if(someList.getTitle().equals(queued.getTitle())) {
											saveState.entry = new SonosEntry(someList.getId(), someList.getTitle(), someList.getParentId(), "", "", "", someList.getUpnpClass(), someList.getRes());
											break;
										}
									}
									
									
								} else if(queued.getUpnpClass().contains("object.container")) {
									// we are playing some other sort of 'container' - we will save that to a playlist for our convenience
									logger.debug("Save State for a container of type {}",queued.getUpnpClass());

									// save the playlist
									String existingList = "";
									List<SonosEntry> playLists  = player.getPlayLists();
									for(SonosEntry someList : playLists) {
										if(someList.getTitle().equals("openHAB-"+player.getUdn())) {
											existingList = someList.getId();
											break;
										}
									}

									player.saveQueue("openHAB-"+player.getUdn(),existingList);

									// get all the playlists and a ref to our saved list
									playLists  = player.getPlayLists();
									for(SonosEntry someList : playLists) {
										if(someList.getTitle().equals("openHAB-"+player.getUdn())) {
											saveState.entry = new SonosEntry(someList.getId(), someList.getTitle(), someList.getParentId(), "", "", "", someList.getUpnpClass(), someList.getRes());
											break;
										}
									}

								}
							} else {
								saveState.entry = new SonosEntry("", "", "", "", "", "", "", "x-rincon-queue:"+player.getUdn().getIdentifierString()+"#0");
							}
						}

						saveState.transportState = player.getTransportState();
						saveState.volume = player.getCurrentVolume();			
						saveState.relTime = player.getPosition();
					} else {
						saveState.entry = null;
					}

					sonosSavedPlayerState.put(playerName,saveState);		
				}
			}
			return true;

		}		
	}
	
	protected boolean restorePlayerState() {
		
		synchronized(this)
		{	

			if(sonosSavedGroupState != null && sonosSavedPlayerState != null) {

				// make every player independent
				for(SonosZoneGroup group : sonosSavedGroupState) {
					for(String playerName : group.getMembers()) {
						SonosZonePlayer player = getPlayerForID(playerName);
						if(player != null) {
							player.becomeStandAlonePlayer();
						}
					}
				}

				// re-create the groups
				for(SonosZoneGroup group : sonosSavedGroupState) {
					SonosZonePlayer coordinator = getPlayerForID(group.getCoordinator());
					if(coordinator != null) {
						for(String playerName : group.getMembers()) {
							SonosZonePlayer player = getPlayerForID(playerName);
							if(player != null) {
								coordinator.addMember(player);
							}
						}
					}
				}

				// put settings back
				for(SonosZoneGroup group : sonosSavedGroupState) {
					SonosZonePlayer coordinator = getPlayerForID(group.getCoordinator());
					for(String playerName : group.getMembers()) {
						SonosZonePlayer player = getPlayerForID(playerName);
						if(player != null) {

							SonosZonePlayerState saveState = sonosSavedPlayerState.get(playerName);

							player.setVolume(saveState.volume);

							if(player == coordinator) {

								if(player == coordinator) {
									
									if(saveState.entry != null) {

										// check if we have a playlist to deal with
										if(saveState.entry.getUpnpClass().contains("object.container.playlistContainer")) {

											player.addURIToQueue(saveState.entry.getRes(), SonosXMLParser.compileMetadataString(saveState.entry), 0, true);
											SonosEntry entry = new SonosEntry("", "", "", "", "", "", "", "x-rincon-queue:"+player.getUdn().getIdentifierString()+"#0");
											player.setCurrentURI(entry);
											player.setPositionTrack(saveState.track);


										} else {
											player.setCurrentURI(saveState.entry);		
											player.setPosition(saveState.relTime);
										}


										if(saveState.transportState.equals("PLAYING")) {
											player.play();
										} else if(saveState.transportState.equals("STOPPED")){
											player.stop();
										} else if(saveState.transportState.equals("PAUSED_PLAYBACK")) {
											player.pause();
										}
									}
								}
							}

						}
					}
				}

				return true;
			} else {
				return false;
			}
		}
	}

	
	private void executeCommand(String itemName,
			Command command, String sonosID,
			SonosCommandType sonosCommandType, String commandAsString) {
		
		boolean result = false;
		
		if(sonosID != null) {
			SonosZonePlayer player = sonosZonePlayerCache.get(sonosID);
			if(player != null) {
				switch (sonosCommandType) {
				case SETLED:
					result = player.setLed(createStringFromCommand(command,commandAsString));
					break;
				case PLAY:
					result = player.play();
					break;
				case STOP:
					result = player.stop();
					break;
				case PAUSE:
					result = player.pause();
					break;
				case NEXT:
					result = player.next();
					break;
				case PREVIOUS:
					result = player.previous();
					break;
				case SETVOLUME:
					result = player.setVolume(commandAsString);
					break;
				case GETVOLUME:
					break;
				case ADDMEMBER:
					result = player.addMember(getPlayerForID(commandAsString));
					break;
				case REMOVEMEMBER:
					result = player.removeMember(getPlayerForID(commandAsString));
					break;
				case BECOMESTANDALONEGROUP:
					result = player.becomeStandAlonePlayer();
					break;
				case SETMUTE:
					result = player.setMute(commandAsString);
					break;
				case PA:
					result = player.publicAddress();
					break;
				case RADIO:
					result = player.playRadio(commandAsString);
					break;
				case SETALARM:
					result = player.setAlarm(commandAsString);
					break;
				case SNOOZE:
					result = player.snoozeAlarm(Integer.parseInt(commandAsString));
					break;
				case SAVE:
					result = savePlayerState();
					break;
				case RESTORE:
					result = restorePlayerState();
					break;
				default:
					break;
			
				};

			} else {
				logger.error(
						"UPNP device is not defined for Sonos Player with ID {}",sonosID);
				return;
			}
		}

		if(result) {
			
			// create a new State based on the type of Sonos Command and the status value in the map
			Type newState = null;
			try {
				newState = createStateForType(sonosCommandType,commandAsString);
			} catch (BindingConfigParseException e) {
				logger.error("Error parsing a value {} to a state variable of type {}",commandAsString,sonosCommandType.getTypeClass().toString());
			}
			
			if(newState != null) {
				if(newState.equals((State) command) || newState instanceof StringType || newState instanceof DecimalType) {
					eventPublisher.postUpdate(itemName, (State) newState);		
				} else {
					eventPublisher.postUpdate(itemName, (State) command);
				}
			}
			else {
				throw new IllegalClassException("Cannot process update for the command of type " + sonosCommandType.toString());
			}

		}

	}
	
	Thread pollingThread = new Thread("Sonos Polling Thread") {
		
		boolean shutdown = false;	
		protected final int pollingPeriod = 1000;

		@Override
		public void run() {

			logger.debug(getName() + " has been started");

			while (!shutdown) {

				if(upnpService != null) {
					// get all the CommandTypes that require polling
					List<SonosCommandType> supportedCommands = SonosCommandType.getPolling();

					for(SonosCommandType sonosCommandType : supportedCommands) {
						// loop through all the player and poll for each of the supportedCommands
						for(String sonosID : sonosZonePlayerCache.keySet()){
							SonosZonePlayer player = sonosZonePlayerCache.get(sonosID);
							if(player != null && player.isConfigured()) {
								switch (sonosCommandType) {
								case GETLED:
									player.updateLed();
									break;
								case RUNNINGALARMPROPERTIES:
									player.updateRunningAlarmProperties();
									break;
								case CURRENTTRACK:
									player.updateCurrentURIFormatted();
									break;
								case ZONEINFO:
									player.updateZoneInfo();
									break;
								case MEDIAINFO:
									player.updateMediaInfo();
									break;
								default:
									break;
								};
							}

						}
					}


					try {
						Thread.sleep(pollingPeriod);
					} catch (InterruptedException e) {
						logger.debug("pausing thread " + getName()
								+ " interrupted");

					}
				}
			
			}
		}
	};
	

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {

			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_SONOS_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given sonos-config-key '"
							+ key
							+ "' does not follow the expected pattern '<sonosId>.<udn>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String sonosID = matcher.group(1);

				SonosZonePlayer sonosConfig = sonosZonePlayerCache.get(sonosID);
				if (sonosConfig == null) {
					sonosConfig = new SonosZonePlayer(self);
					sonosZonePlayerCache.put(sonosID, sonosConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("udn".equals(configKey)) {
					sonosConfig.setUdn(new UDN(value));
					
					logger.debug("Querying network for UPNP with UDN {}",sonosConfig.getUdn());
					// Query the network for this UDN
					upnpService.getControlPoint().search(
					        new UDNHeader(sonosConfig.getUdn())
					);
					
				}
				else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey + "' is unknown");
				}
			}
		}	
	}


	public void activate() {
	}
	
	public void start() {
        // This will create necessary network resources for UPnP right away
        logger.debug("Sonos binding has been started.");

        upnpService = new UpnpServiceImpl(new SonosUpnpServiceConfiguration(),listener);
        
        // Send a search message to all devices and services, they should respond soon
        //upnpService.getControlPoint().search(new STAllHeader());
        UDADeviceType udaType = new UDADeviceType("ZonePlayer");
		upnpService.getControlPoint().search(new UDADeviceTypeHeader(udaType));
		
		// start the thread that will poll some devices
    	pollingThread.setDaemon(true);
    	pollingThread.start();
	}
	
	protected String getSonosIDforDevice(RemoteDevice device){
		for(String id : sonosZonePlayerCache.keySet()){
			SonosZonePlayer config = sonosZonePlayerCache.get(id);
			if(config.getDevice() == device){
				return id;
			}
		}
		return null;
	}
	
	protected SonosZonePlayer getPlayerForID(String name){
			
	    	String sonosID = null;
	    	
	    	for(String playerName : sonosZonePlayerCache.keySet()){
	    		if(playerName.equals(name)){
	    			sonosID = playerName;
	    			break;
	    		}
	    	}
	    	
	    	if(sonosID == null) {
	    		
	        	for(String playerName : sonosZonePlayerCache.keySet()){
	        		SonosZonePlayer player = sonosZonePlayerCache.get(playerName);
	        		if(player.getUdn().getIdentifierString().equals(name)){
	        			sonosID = playerName;
	        			break;
	        		}
	        	}
	        	
	    	} 
			
			return sonosZonePlayerCache.get(sonosID);
			
		}
	
	
	  public String getCoordinatorForZonePlayer(String playerName) {
		  
		  	SonosZonePlayer zonePlayer = sonosZonePlayerCache.get(playerName);
		  
		    if (zonePlayer == null || getSonosZoneGroups() == null) {
		      return playerName;
		    }
		    for (SonosZoneGroup zg : getSonosZoneGroups()) {
		      if (zg.getMembers().contains(zonePlayer.getUdn().getIdentifierString())) {
		    	  String coordinator = zg.getCoordinator();
		    	  for (String player : sonosZonePlayerCache.keySet()){
		    		  if(sonosZonePlayerCache.get(player).getUdn().getIdentifierString().equals(coordinator)){
		    			  return player;
		    		  }
		    	  }
		      }
		    }
		    return playerName;
		  }
	  
	  public SonosZonePlayer getCoordinatorForZonePlayer(SonosZonePlayer zonePlayer) {
		  
		    if (getSonosZoneGroups() == null) {
		      return zonePlayer;
		    }
		    for (SonosZoneGroup zg : getSonosZoneGroups()) {
		      if (zg.getMembers().contains(zonePlayer.getUdn().getIdentifierString())) {
		    	  String coordinator = zg.getCoordinator();
		    	  for (String player : sonosZonePlayerCache.keySet()){
		    		  if(sonosZonePlayerCache.get(player).getUdn().getIdentifierString().equals(coordinator)){
		    			  return sonosZonePlayerCache.get(player);
		    		  }
		    	  }
		      }
		    }
		    return zonePlayer;
		  }

	public List<SonosZoneGroup> getSonosZoneGroups() {
		return sonosZoneGroups;
	}

	public void setSonosZoneGroups(List<SonosZoneGroup> sonosZoneGroups) {
		this.sonosZoneGroups = sonosZoneGroups;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if(bindingStarted == false) {
			start();
			bindingStarted = true;
		}
	}

}
