/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sonos.SonosBindingProvider;
import org.openhab.binding.sonos.SonosCommandType;
import org.openhab.core.binding.AbstractActiveBinding;
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
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.DefaultUpnpServiceConfiguration;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.message.header.UDAServiceTypeHeader;
import org.teleal.cling.model.message.header.UDNHeader;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.model.types.UDAServiceType;
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
 * @author Pauli Anttila
 * @since 1.1.0
 * 
 */
public class SonosBinding extends AbstractActiveBinding<SonosBindingProvider>
implements ManagedService {

	private static Logger logger = LoggerFactory.getLogger(SonosBinding.class);

	private static final Pattern EXTRACT_SONOS_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(udn)$");

	static protected UpnpService upnpService;
	static protected SonosBinding self;

	/** the refresh interval which is used to check for changes in the binding configurations */
	private static long refreshInterval = 5000;
	/** polling interval in milliseconds for variables that need to be polled */
	private int pollingPeriod = 1000;
	/** timeout interval used by the Cling GENA subscriptions */
	static protected int interval = 600;

	static protected boolean bindingStarted = false;

	private PlayerCache sonosZonePlayerCache = new PlayerCache();
	private List<SonosZoneGroup> sonosZoneGroups = null;
	private Map<String, SonosZonePlayerState> sonosSavedPlayerState = null;
	private List<SonosZoneGroup> sonosSavedGroupState = null;

	private class PlayerCache extends ArrayList<SonosZonePlayer> {

		private static final long serialVersionUID = 7973128806169191738L;

		public boolean contains(String id) {
			Iterator<SonosZonePlayer> it = this.iterator();
			while(it.hasNext()){
				SonosZonePlayer aPlayer = it.next();
				if (aPlayer.getUdn().getIdentifierString().equals(id) || aPlayer.getId().equals(id)) {
					return true;
				}
			}
			return false;
		}

		public SonosZonePlayer getById(String id) {
			Iterator<SonosZonePlayer> it = this.iterator();
			while(it.hasNext()){
				SonosZonePlayer aPlayer = it.next();
				if (aPlayer.getUdn().getIdentifierString().equals(id) || aPlayer.getId().equals(id)) {
					return aPlayer;
				}
			}
			return null;	
		}

		public SonosZonePlayer getByUDN(String udn) {	
			Iterator<SonosZonePlayer> it = this.iterator();
			while(it.hasNext()){
				SonosZonePlayer aPlayer = it.next();
				if (aPlayer.getUdn().getIdentifierString().equals(udn)) {
					return aPlayer;
				}
			}
			return null;
		}

		public SonosZonePlayer getByDevice(RemoteDevice device) {	
			Iterator<SonosZonePlayer> it = this.iterator();
			while(it.hasNext()){
				SonosZonePlayer aPlayer = it.next();
				if (aPlayer.getDevice().equals(device)) {
					return aPlayer;
				}
			}
			return null;
		}

	}

	public class SonosUpnpServiceConfiguration extends
	DefaultUpnpServiceConfiguration {

		@SuppressWarnings("rawtypes")
		@Override
		public StreamClient createStreamClient() {
			return new StreamClientImpl(new StreamClientConfigurationImpl());
		}

		@SuppressWarnings("rawtypes")
		@Override
		public StreamServer createStreamServer(
				NetworkAddressFactory networkAddressFactory) {
			return new StreamServerImpl(new StreamServerConfigurationImpl(
					networkAddressFactory.getStreamListenPort()));
		}

	}

	RegistryListener listener = new RegistryListener() {

		public void remoteDeviceDiscoveryStarted(Registry registry,
				RemoteDevice device) {
			logger.debug("Discovery started: " + device.getDisplayString());
		}

		public void remoteDeviceDiscoveryFailed(Registry registry,
				RemoteDevice device, Exception ex) {
			logger.debug("Discovery failed: " + device.getDisplayString()
					+ " => " + ex);
		}

		@SuppressWarnings("rawtypes")
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

			// add only Sonos devices
			if (device.getDetails().getManufacturerDetails().getManufacturer()
					.toUpperCase().contains("SONOS")) {

				UDN udn = device.getIdentity().getUdn();
				boolean existingDevice = false;

				logger.info("Found a Sonos device ({}) with UDN {}",device.getDetails().getModelDetails().getModelNumber(),udn);

				// Check if we already received a configuration for this
				// device through the .cfg
				SonosZonePlayer thePlayer = sonosZonePlayerCache.getByUDN(udn.getIdentifierString());

				if (thePlayer == null) {
					// Add device to the cached Configs
					thePlayer = new SonosZonePlayer(udn.getIdentifierString(),self);
					thePlayer.setUdn(udn);

					sonosZonePlayerCache.add(thePlayer);
				}

				thePlayer.setDevice(device);
				thePlayer.setService(upnpService);

				// add GENA service to capture zonegroup information
				Service service = device.findService(new UDAServiceId(
						"ZoneGroupTopology"));
				SonosSubscriptionCallback callback = new SonosSubscriptionCallback(
						service, interval);
				upnpService.getControlPoint().execute(callback);
			} else {
				logger.info("A non-Sonos device ({}) is found and will be ignored",device.getDisplayString());
			}
		}

		public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
			logger.trace("Remote device updated: " + device.getDisplayString());
		}

		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
			logger.trace("Remote device removed: " + device.getDisplayString());
		}

		public void localDeviceAdded(Registry registry, LocalDevice device) {
			logger.trace("Local device added: " + device.getDisplayString());
		}

		public void localDeviceRemoved(Registry registry, LocalDevice device) {
			logger.trace("Local device removed: " + device.getDisplayString());
		}

		public void beforeShutdown(Registry registry) {
			logger.debug("Before shutdown, the registry has devices: "
					+ registry.getDevices().size());
		}

		public void afterShutdown() {
			logger.debug("Shutdown of registry complete!");

		}
	};

	public SonosBinding() {
		self = this;
	}

	/**
	 * Find the first matching {@link ChannelBindingProvider} according to
	 * <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected SonosBindingProvider findFirstMatchingBindingProvider(
			String itemName) {
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

	public void activate() {
		// Nothing to do here. We start the binding when the first item bindigconfig is processed
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		SonosBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String commandAsString = command.toString();

		if (command != null) {

			List<Command> commands = new ArrayList<Command>();

			if (command instanceof StringType || command instanceof DecimalType) {
				commands = provider.getVariableCommands(itemName);
			} else {
				commands.add(command);
			}

			for (Command someCommand : commands) {

				String sonosID = provider.getSonosID(itemName, someCommand);
				String sonosCommand = provider.getSonosCommand(itemName,someCommand);
				SonosCommandType sonosCommandType = null;
				
				try {
					sonosCommandType = SonosCommandType.getCommandType(sonosCommand, Direction.OUT);
				} catch (Exception e) {
					logger.error("An exception occured while verifying command compatibility ({})",e.getMessage());
				}

				if (sonosID != null) {
					if (sonosCommandType != null) {
						executeCommand(itemName, someCommand, sonosID,
								sonosCommandType, commandAsString);
					} else {
						logger.error(
								"wrong command type for binding [Item={}, command={}]",
								itemName, commandAsString);
					}
				} else {
					logger.error("{} is an unrecognised command for Item {}",
							commandAsString, itemName);
				}
			}

		}

	}

	@SuppressWarnings("unchecked")
	private Type createStateForType(Class<? extends State> ctype, String value)
			throws BindingConfigParseException {

		if (ctype != null && value != null) {

			List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();
			stateTypeList.add(ctype);

			String finalValue = value;

			// Note to Kai or Thomas: sonos devices return some "true" "false"
			// values for specific variables. We convert those
			// into ON OFF if the commandTypes allow so. This is a little hack,
			// but IMHO OnOffType should
			// be enhanced, or a TrueFalseType should be developed
			if (ctype.equals(OnOffType.class)) {
				finalValue = StringUtils.upperCase(value);
				if (finalValue.equals("TRUE") || finalValue.equals("1")) {
					finalValue = "ON";
				} else if (finalValue.equals("FALSE") || finalValue.equals("0")) {
					finalValue = "OFF";
				} 
			}

			State state = TypeParser.parseState(stateTypeList, finalValue);

			return state;
		} else {
			return null;
		}
	}

	private String createStringFromCommand(Command command,
			String commandAsString) {

		String value = null;

		if (command instanceof StringType || command instanceof DecimalType) {
			value = commandAsString;
		} else {
			value = command.toString();
		}

		return value;

	}

	@SuppressWarnings("rawtypes")
	public void processVariableMap(RemoteDevice device,
			Map<String, StateVariableValue> values) {

		if (device != null && values != null) {

			for (String stateVariable : values.keySet()) {

				// find all the CommandTypes that are defined for each
				// StateVariable
				List<SonosCommandType> supportedCommands = SonosCommandType
						.getCommandByVariable(stateVariable);

				StateVariableValue status = values.get(stateVariable);

				for (SonosCommandType sonosCommandType : supportedCommands) {

					// create a new State based on the type of Sonos Command and
					// the status value in the map
					Type newState = null;
					try {
						newState = createStateForType((Class<? extends State>) sonosCommandType.getTypeClass(), status
								.getValue().toString());
					} catch (BindingConfigParseException e) {
						logger.error(
								"Error parsing a value {} to a state variable of type {}",
								status.toString(), sonosCommandType
								.getTypeClass().toString());
					}

					for (SonosBindingProvider provider : providers) {
						List<String> qualifiedItems = provider.getItemNames(sonosZonePlayerCache.getByDevice(device).getId(), sonosCommandType.getSonosCommand());
						List<String> qualifiedItemsByUDN = provider.getItemNames(sonosZonePlayerCache.getByDevice(device).getUdn().getIdentifierString(), sonosCommandType.getSonosCommand());

						for(String item : qualifiedItemsByUDN) {
							if(!qualifiedItems.contains(item)) {
								qualifiedItems.add(item);
							}
						}

						for (String anItem : qualifiedItems) {
							// get the openHAB commands attached to each Item at
							// this given Provider
							List<Command> commands = provider.getCommands(anItem, sonosCommandType.getSonosCommand());

							if( provider.getAcceptedDataTypes(anItem).contains(sonosCommandType.getTypeClass())) {
								if(newState != null) {
									eventPublisher.postUpdate(anItem,(State) newState);
								} else {
									throw new IllegalClassException(
											"Cannot process update for the command of type "
													+ sonosCommandType
													.toString());
								}
							} else {
								logger.warn("Cannot cast {} to an accepted state type for item {}",sonosCommandType.getTypeClass().toString(),anItem);
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
			super(service, interval);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void established(GENASubscription sub) {
			logger.debug("The GENA Subscription for serviceID {} is established for device {}",sub.getService().getServiceId(),sub.getService().getDevice());
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected void failed(GENASubscription subscription,
				UpnpResponse responseStatus, Exception exception,
				String defaultMsg) {
			logger.error(defaultMsg);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void eventReceived(GENASubscription sub) {

			// get the device linked to this service linked to this subscription

			Map<String, StateVariableValue> values = sub.getCurrentValues();
			Map<String, StateVariableValue> mapToProcess = new HashMap<String, StateVariableValue>();

			// now, lets deal with the specials - some UPNP responses require
			// some XML parsing
			// or we need to update our internal data structure
			// or are things we want to store for further reference
			for (String stateVariable : values.keySet()) {
				
				if (stateVariable.equals("ZoneGroupState")) {
					try {
						setSonosZoneGroups(SonosXMLParser
								.getZoneGroupFromXML(values.get(stateVariable)
										.toString()));
					} catch (SAXException e) {
						logger.error("Could not parse XML variable {}", values
								.get(stateVariable).toString());
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
			logger.debug("The GENA Subscription for serviceID {} ended for device {}",subscription.getService().getServiceId(),subscription.getService().getDevice());

			//rebooting the GENA subscription
			Service service = subscription.getService();			
			SonosSubscriptionCallback callback = new SonosSubscriptionCallback(service,interval);
			upnpService.getControlPoint().execute(callback);

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

		synchronized (this) {
			if (sonosSavedGroupState != null && sonosSavedPlayerState != null) {
				// TODO issue warning

			}

			sonosSavedPlayerState = new HashMap<String, SonosZonePlayerState>();
			sonosSavedGroupState = new ArrayList<SonosZoneGroup>();

			for (SonosZoneGroup group : getSonosZoneGroups()) {
				sonosSavedGroupState.add((SonosZoneGroup) group.clone());
			}

			for (SonosZoneGroup group : sonosSavedGroupState) {
				for (String playerName : group.getMembers()) {
					SonosZonePlayer player = sonosZonePlayerCache.getById(playerName);

					SonosZonePlayerState saveState = new SonosZonePlayerState();

					String currentURI = player.getCurrentURI();

					if (currentURI != null) {

						if (currentURI.contains("x-sonosapi-stream:")) {
							// we are streaming music
							SonosMetaData track = player.getTrackMetadata();
							SonosMetaData current = player
									.getCurrentURIMetadata();
							if (track != null) {
								saveState.entry = new SonosEntry("",
										current.getTitle(), "", "",
										track.getAlbumArtUri(), "",
										current.getUpnpClass(), currentURI);
							}
						} else if (currentURI.contains("x-rincon:")) {
							// we are a slave to some coordinator
							saveState.entry = new SonosEntry("", "", "", "",
									"", "", "", currentURI);
						} else if (currentURI.contains("x-rincon-stream:")) {
							// we are streaming from the Line In connection
							saveState.entry = new SonosEntry("", "", "", "",
									"", "", "", currentURI);
						} else if (currentURI.contains("x-rincon-queue:")) {
							// we are playing something that sits in the queue
							SonosMetaData queued = player
									.getEnqueuedTransportURIMetaData();
							if (queued != null) {

								saveState.track = player.getCurrenTrackNr();

								if (queued.getUpnpClass().contains(
										"object.container.playlistContainer")) {
									// we are playing a real 'saved' playlist
									List<SonosEntry> playLists = player
											.getPlayLists();
									for (SonosEntry someList : playLists) {
										if (someList.getTitle().equals(
												queued.getTitle())) {
											saveState.entry = new SonosEntry(
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
									List<SonosEntry> playLists = player
											.getPlayLists();
									for (SonosEntry someList : playLists) {
										if (someList.getTitle().equals(
												"openHAB-" + player.getUdn())) {
											existingList = someList.getId();
											break;
										}
									}

									player.saveQueue(
											"openHAB-" + player.getUdn(),
											existingList);

									// get all the playlists and a ref to our
									// saved list
									playLists = player.getPlayLists();
									for (SonosEntry someList : playLists) {
										if (someList.getTitle().equals(
												"openHAB-" + player.getUdn())) {
											saveState.entry = new SonosEntry(
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
								saveState.entry = new SonosEntry("", "", "",
										"", "", "", "", "x-rincon-queue:"
												+ player.getUdn()
												.getIdentifierString()
												+ "#0");
							}
						}

						saveState.transportState = player.getTransportState();
						saveState.volume = player.getCurrentVolume();
						saveState.relTime = player.getPosition();
					} else {
						saveState.entry = null;
					}

					sonosSavedPlayerState.put(playerName, saveState);
				}
			}
			return true;

		}
	}

	protected boolean restorePlayerState() {

		synchronized (this) {

			if (sonosSavedGroupState != null && sonosSavedPlayerState != null) {

				// make every player independent
				for (SonosZoneGroup group : sonosSavedGroupState) {
					for (String playerName : group.getMembers()) {
						SonosZonePlayer player = sonosZonePlayerCache.getById(playerName);
						if (player != null) {
							player.becomeStandAlonePlayer();
						}
					}
				}

				// re-create the groups
				for (SonosZoneGroup group : sonosSavedGroupState) {
					SonosZonePlayer coordinator = sonosZonePlayerCache.getById(group.getCoordinator());
					if (coordinator != null) {
						for (String playerName : group.getMembers()) {
							SonosZonePlayer player = sonosZonePlayerCache.getById(playerName);
							if (player != null) {
								coordinator.addMember(player);
							}
						}
					}
				}

				// put settings back
				for (SonosZoneGroup group : sonosSavedGroupState) {
					SonosZonePlayer coordinator = sonosZonePlayerCache.getById(group
							.getCoordinator());
					for (String playerName : group.getMembers()) {
						SonosZonePlayer player = sonosZonePlayerCache.getById(playerName);
						if (player != null) {

							SonosZonePlayerState saveState = sonosSavedPlayerState
									.get(playerName);

							player.setVolume(saveState.volume);

							if (player == coordinator) {

								if (player == coordinator) {

									if (saveState.entry != null) {

										// check if we have a playlist to deal
										// with
										if (saveState.entry
												.getUpnpClass()
												.contains(
														"object.container.playlistContainer")) {

											player.addURIToQueue(
													saveState.entry.getRes(),
													SonosXMLParser
													.compileMetadataString(saveState.entry),
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
															+ player.getUdn()
															.getIdentifierString()
															+ "#0");
											player.setCurrentURI(entry);
											player.setPositionTrack(saveState.track);

										} else {
											player.setCurrentURI(saveState.entry);
											player.setPosition(saveState.relTime);
										}

										if (saveState.transportState
												.equals("PLAYING")) {
											player.play();
										} else if (saveState.transportState
												.equals("STOPPED")) {
											player.stop();
										} else if (saveState.transportState
												.equals("PAUSED_PLAYBACK")) {
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

	private void executeCommand(String itemName, Command command,
			String sonosID, SonosCommandType sonosCommandType,
			String commandAsString) {

		boolean result = false;

		if (sonosID != null && sonosZonePlayerCache.contains(sonosID)) {
			SonosZonePlayer player = sonosZonePlayerCache.getById(sonosID);
			if (player != null) {
				switch (sonosCommandType) {
				case SETLED:
					result = player.setLed(createStringFromCommand(command,
							commandAsString));
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
					result = player.addMember(sonosZonePlayerCache.getById(commandAsString));
					break;
				case REMOVEMEMBER:
					result = player
					.removeMember(sonosZonePlayerCache.getById(commandAsString));
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
					result = player.snoozeAlarm(Integer
							.parseInt(commandAsString));
					break;
				case SAVE:
					result = savePlayerState();
					break;
				case RESTORE:
					result = restorePlayerState();
					break;
				case PLAYLIST:
					result = player.playPlayList(commandAsString);
					break;
				case SETURI:
					result = player.playURI(commandAsString);
					break;
				default:
					break;

				}
				;

			} else {
				logger.error(
						"UPNP device is not defined for Sonos Player with ID {}",
						sonosID);
				return;
			}
		}
	}

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

				if ("pollingPeriod".equals(key)) {
					pollingPeriod = Integer.parseInt((String) config.get(key));
					logger.debug("Setting polling period to {} ms", pollingPeriod);
					continue;
				}

				Matcher matcher = EXTRACT_SONOS_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given sonos-config-key '"
							+ key + "' does not follow the expected pattern '<sonosId>.<udn>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String sonosID = matcher.group(1);

				SonosZonePlayer sonosConfig = sonosZonePlayerCache.getById(sonosID);
				if (sonosConfig == null) {
					sonosConfig = new SonosZonePlayer(sonosID,self);
					sonosZonePlayerCache.add(sonosConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("udn".equals(configKey)) {
					sonosConfig.setUdn(new UDN(value));
					logger.debug("Add predefined Sonos device with UDN {}", sonosConfig.getUdn());
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey + "' is unknown");
				}
			}
		}

		setProperlyConfigured(true);

	}

	@Override
	protected void execute() {
		if(isProperlyConfigured()) {

			if(!bindingStarted) {

				// This will create necessary network resources for UPnP right away
				upnpService = new UpnpServiceImpl(new SonosUpnpServiceConfiguration(), listener);

				try {

					Iterator<SonosZonePlayer> it = sonosZonePlayerCache.iterator();
					while(it.hasNext()){
						SonosZonePlayer aPlayer = it.next();
						if(aPlayer.getDevice() == null) {
							logger.info("Querying the network for a predefined Sonos device with UDN {}", aPlayer.getUdn());
							upnpService.getControlPoint().search(new UDNHeader(aPlayer.getUdn()));
						}
					}

					logger.info("Querying the network for any other Sonos device");

					final UDAServiceType udaType = new UDAServiceType("AVTransport");
					upnpService.getControlPoint().search( new UDAServiceTypeHeader(udaType));

				} catch (Exception e) {
					logger.warn("An exception occurred while searching the network for Sonos devices: ", e.getMessage());
				}

				bindingStarted = true;
			}

			Scheduler sched = null;
			try {
				sched =  StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e) {
				logger.error("An exception occurred while getting a reference to the Quartz Scheduler");
			}

			// Cycle through the Items and setup sonos zone players if required
			for (SonosBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					for(String sonosID : ((SonosBindingProvider) provider).getSonosID(itemName)) {
						if(!sonosZonePlayerCache.contains(sonosID)) {
							// the device is not yet discovered on the network or not defined in the .cfg

							//Verify that the sonosID has the format of a valid UDN
							Pattern SONOS_UDN_PATTERN = Pattern.compile("RINCON_(\\w{17})");
							Matcher matcher = SONOS_UDN_PATTERN.matcher(sonosID);
							if(matcher.matches()){
								// Add device to the cached Configs
								SonosZonePlayer thePlayer = new SonosZonePlayer(sonosID,self);
								thePlayer.setUdn(new UDN(sonosID));

								sonosZonePlayerCache.add(thePlayer);

								//Query the network for this device
								logger.info("Querying the network for a predefined Sonos device with UDN '{}'", thePlayer.getUdn());
								upnpService.getControlPoint().search(new UDNHeader(thePlayer.getUdn()));
							}

						}
					}
				}
			}

			// Cycle through the item binding configuration that define polling criteria
			for (SonosCommandType sonosCommandType : SonosCommandType.getPolling()) {
				for(SonosBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames(sonosCommandType.getSonosCommand())) {
						for(Command aCommand : ((SonosBindingProvider) provider).getCommands(itemName,sonosCommandType.getSonosCommand())) {

							// We are dealing with a valid device
							SonosZonePlayer thePlayer = sonosZonePlayerCache.getById(provider.getSonosID(itemName, aCommand));

							if(thePlayer != null) {

								RemoteDevice theDevice = thePlayer.getDevice(); 

								// Only set up a polling job if the device supports the given SonosCommandType
								// Not all Sonos devices have the same capabilities
								if(	theDevice!=null) {
									if(theDevice.findService(new UDAServiceId(sonosCommandType.getService())) != null){			

										boolean jobExists = false;

										// enumerate each job group
										try {
											for(String group: sched.getJobGroupNames()) {
												// enumerate each job in group
												for(JobKey jobKey : sched.getJobKeys(jobGroupEquals(group))) {
													if(jobKey.getName().equals(provider.getSonosID(itemName, aCommand)+"-"+sonosCommandType.getJobClass().toString())) {
														jobExists = true;
														break;
													}
												}
											}
										} catch (SchedulerException e1) {
											logger.error("An exception occurred while quering the Quartz Scheduler ({})",e1.getMessage());
										}

										if(!jobExists) {
											// set up the Quartz jobs
											JobDataMap map = new JobDataMap();
											map.put("Player", thePlayer);

											JobDetail job = newJob(sonosCommandType.getJobClass())
													.withIdentity(provider.getSonosID(itemName, aCommand)+"-"+sonosCommandType.getJobClass().toString(), "Sonos-"+provider.toString())
													.usingJobData(map)
													.build();

											Trigger trigger = newTrigger()
													.withIdentity(provider.getSonosID(itemName, aCommand)+"-"+sonosCommandType.getJobClass().toString(), "Sonos-"+provider.toString())
													.startNow()
													.withSchedule(simpleSchedule()
															.repeatForever()
															.withIntervalInMilliseconds(pollingPeriod))            
															.build();

											try {
												sched.scheduleJob(job, trigger);
											} catch (SchedulerException e) {
												logger.error("An exception occurred while scheduling a Quartz Job ({})",e.getMessage());
											}
										}
									}
								}
							}			
						}
					}
				}
			}
		}
	}

	public String getCoordinatorForZonePlayer(String playerName) {

		SonosZonePlayer zonePlayer = sonosZonePlayerCache.getById(playerName);

		if (zonePlayer == null || getSonosZoneGroups() == null) {
			return playerName;
		}
		for (SonosZoneGroup zg : getSonosZoneGroups()) {
			if (zg.getMembers().contains(
					zonePlayer.getUdn().getIdentifierString())) {
				String coordinator = zg.getCoordinator();
				return sonosZonePlayerCache.getByUDN(coordinator).getId();
			}
		}
		return playerName;
	}

	public SonosZonePlayer getPlayerForID(String player) {
		return sonosZonePlayerCache.getById(player);
	}

	public SonosZonePlayer getCoordinatorForZonePlayer(
			SonosZonePlayer zonePlayer) {

		if (getSonosZoneGroups() == null) {
			return zonePlayer;
		}
		for (SonosZoneGroup zg : getSonosZoneGroups()) {
			if (zg.getMembers().contains(
					zonePlayer.getUdn().getIdentifierString())) {
				String coordinator = zg.getCoordinator();
				return sonosZonePlayerCache.getByUDN(coordinator);
			}
		}
		return zonePlayer;
	}

	public List<SonosZoneGroup> getSonosZoneGroups() {
		return sonosZoneGroups;
	}

	public void setSonosZoneGroups(List<SonosZoneGroup> sonosZoneGroups) {
		this.sonosZoneGroups = sonosZoneGroups;
		logger.debug("The following Zone Groups are in operation {}",sonosZoneGroups.toString());
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Sonos Refresh Service";
	}

	public static class LedJob implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			SonosZonePlayer thePlayer = (SonosZonePlayer) dataMap.get("Player");

			thePlayer.getLed();

		}
	}

	public static class RunningAlarmPropertiesJob implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			SonosZonePlayer thePlayer = (SonosZonePlayer) dataMap.get("Player");

			thePlayer.updateRunningAlarmProperties();

		}
	}

	public static class CurrentURIFormattedJob implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			SonosZonePlayer thePlayer = (SonosZonePlayer) dataMap.get("Player");

			thePlayer.updateCurrentURIFormatted();

		}
	}


	public static class ZoneInfoJob implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			SonosZonePlayer thePlayer = (SonosZonePlayer) dataMap.get("Player");

			thePlayer.updateZoneInfo();

		}
	}

	public static class MediaInfoJob implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			SonosZonePlayer thePlayer = (SonosZonePlayer) dataMap.get("Player");

			thePlayer.updateMediaInfo();

		}
	}

}
