/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.openhab.binding.omnilink.OmniLinkBindingProvider;
import org.openhab.binding.omnilink.internal.model.Area;
import org.openhab.binding.omnilink.internal.model.AudioSource;
import org.openhab.binding.omnilink.internal.model.AudioZone;
import org.openhab.binding.omnilink.internal.model.Auxiliary;
import org.openhab.binding.omnilink.internal.model.Button;
import org.openhab.binding.omnilink.internal.model.OmnilinkDevice;
import org.openhab.binding.omnilink.internal.model.Thermostat;
import org.openhab.binding.omnilink.internal.model.Unit;
import org.openhab.binding.omnilink.internal.model.Zone;
import org.openhab.binding.omnilink.internal.ui.OmnilinkItemGenerator;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.Connection;
import com.digitaldan.jomnilinkII.DisconnectListener;
import com.digitaldan.jomnilinkII.Message;
import com.digitaldan.jomnilinkII.NotificationListener;
import com.digitaldan.jomnilinkII.OmniInvalidResponseException;
import com.digitaldan.jomnilinkII.OmniNotConnectedException;
import com.digitaldan.jomnilinkII.OmniUnknownMessageTypeException;
import com.digitaldan.jomnilinkII.MessageTypes.AudioSourceStatus;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectProperties;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.OtherEventNotifications;
import com.digitaldan.jomnilinkII.MessageTypes.SystemStatus;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AreaProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioSourceProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioZoneProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AuxSensorProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ButtonProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ThermostatProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.UnitProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ZoneProperties;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AreaStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AudioZoneStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.Status;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ThermostatStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.UnitStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ZoneStatus;

/**
 * Omnilink Binding allows full control over a HAI Omni or Lumina system
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkBinding extends AbstractBinding<OmniLinkBindingProvider>
		implements ManagedService, NotificationListener {


	private static final Logger logger = LoggerFactory
			.getLogger(OmniLinkBinding.class);
	
	/*
	 * the current OmniWorker thread
	 */
	private OmniConnectionThread omniWorker = null;
	
	/**
	 * New items or items needing to be refreshed get added to refreshmao
	 * the worker thread will refresh and remove them
	 */
	private Map<String, OmniLinkBindingConfig> refreshMap = Collections.synchronizedMap(new HashMap<String, OmniLinkBindingConfig>());
	
	/**
	 * we keep maps of the various object numbers to a specific object
	 */
	private Map<Integer, Area> areaMap = Collections.synchronizedMap(new HashMap<Integer, Area>());
	private Map<Integer, AudioSource> audioSourceMap = Collections.synchronizedMap(new HashMap<Integer, AudioSource>());
	private Map<Integer, AudioZone> audioZoneMap = Collections.synchronizedMap(new HashMap<Integer, AudioZone>());
	private Map<Integer, Auxiliary> auxMap = Collections.synchronizedMap(new HashMap<Integer, Auxiliary>());
	private Map<Integer, Thermostat> thermostatMap = Collections.synchronizedMap(new HashMap<Integer, Thermostat>());
	private Map<Integer, Unit> unitMap = Collections.synchronizedMap(new HashMap<Integer, Unit>());
	private Map<Integer, Zone> zoneMap = Collections.synchronizedMap(new HashMap<Integer, Zone>());
	private Map<Integer, Button> buttonMap = Collections.synchronizedMap(new HashMap<Integer, Button>());

	/**
	 * We need to poll for audio source changes, that action
	 * waits on this lock, if notified it will imediately
	 * check for updates, useful of a zone is changing
	 * tracks
	 */
	private Object audioUpdateLock = new Object();

	public OmniLinkBinding() {
	}

	@Override
	public void activate() {
		logger.trace("OmniLinkBinding activate");
	}

	@Override
	public void deactivate() {
		logger.debug("OmniLinkBinding disconnecting");
		if (omniWorker != null)
			omniWorker.setRunning(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof OmniLinkBindingProvider) {
			OmniLinkBindingProvider omniProvider = (OmniLinkBindingProvider) provider;
			logger.debug("binding changed");
			OmniLinkBindingConfig config = omniProvider.getOmniLinkBindingConfig(itemName);
			refreshMap.put(itemName, config);

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		if (provider instanceof OmniLinkBindingProvider) {
			OmniLinkBindingProvider omniProvider = (OmniLinkBindingProvider) provider;
			populateRefreshMapFromProvider(omniProvider);
			logger.debug("all binding changed");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!!! {} {} ", itemName,
				command);
		if (omniWorker != null && omniWorker.isConnected()) {
			for (OmniLinkBindingProvider provider : providers) {
				OmniLinkBindingConfig config = provider.getOmniLinkBindingConfig(itemName);
				Item item = provider.getItem(itemName);
				List<OmniLinkControllerCommand> commands = OmniLinkCommandMapper.getCommand(item, config, command);
				
				/*
				 * send each command we get back
				 */
				for (OmniLinkControllerCommand cmd : commands) {
					try {
						logger.debug(
								"Sending command {}/{}/{}",
								new Object[] { cmd.getCommand(),
										cmd.getParameter1(),
										cmd.getParameter2() });
						
						omniWorker.getOmniConnection().controllerCommand(
								cmd.getCommand(), cmd.getParameter1(),
								cmd.getParameter2());
						
						// little hack to get audio updates faster.
						if (config.getObjectType() == OmniLinkItemType.AUDIOZONE_KEY)
							audioUpdateLock.notifyAll();
						
					} catch (IOException e) {
						logger.error("Could not send command", e);
					} catch (OmniNotConnectedException e) {
						logger.error("Could not send command", e);
					} catch (OmniInvalidResponseException e) {
						logger.error("Could not send command", e);
					} catch (OmniUnknownMessageTypeException e) {
						logger.error("Could not send command", e);
					}
				}
			}
		} else {
			logger.debug(
					"Could not send message, conncetion not established {}",
					omniWorker == null);
		}

		// get the
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called! {} {}", itemName, newState);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (omniWorker != null)
			omniWorker.setRunning(false);

		if (config != null) {
			
			String host = (String) config.get("host");
			int port = Integer.parseInt((String) config.get("port"));
			String key1 = (String) config.get("key1");
			String key2 = (String) config.get("key2");
			
			boolean generateItems = Boolean.parseBoolean((String)config.get("generateItems"));

			if (StringUtils.isEmpty(host) || StringUtils.isEmpty(key1)
					|| StringUtils.isEmpty(key2))
				throw new ConfigurationException("omnilink",
						"host, key1 or key2 was not found");

			logger.debug("Starting update");

			omniWorker = new OmniConnectionThread(host, port,
					key1 + ":" + key2, generateItems, this);
			omniWorker.start();
		}
	}

	/**
	 * Add items to be refreshed by our connection thread
	 * @param omniProvider
	 */
	private void populateRefreshMapFromProvider(
			OmniLinkBindingProvider omniProvider) {
		for (String itemName : omniProvider.getItemNames()) {
			OmniLinkBindingConfig config = omniProvider
					.getOmniLinkBindingConfig(itemName);
			refreshMap.put(itemName, config);
		}
	}
	
	/**
	 * add items from all providers to be refreshed by our
	 * connection thread
	 */
	private void populateRefreshMapFromAllProviders() {
		for (OmniLinkBindingProvider provider : providers) {
			populateRefreshMapFromProvider(provider);
		}
	}

	/**
	 * This represents our internal connection thread, to stop set running = false
	 * @author daniel
	 *
	 */
	private class OmniConnectionThread extends Thread {
		private boolean running = true;
		private boolean connected = false;
		private String host;
		private int port;
		private String key;
		private NotificationListener listener;
		private boolean celius;
		private boolean omni;
		private boolean generateItems;
		private Connection c;

		/**
		 * This initializes, but does not start our main connection thread
		 * @param host
		 * @param port
		 * @param key
		 * @param generateItems is if we should print a items list to the log
		 * @param listener callback for events
		 */
		public OmniConnectionThread(String host, int port, String key,
				boolean generateItems, NotificationListener listener) {
			super("OmniConnectionThread");
			this.host = host;
			this.port = port;
			this.key = key;
			this.listener = listener;
			this.generateItems = generateItems;
			this.running = false;
			// audioSources = new ConcurrentHashMap<Integer, AudioSource>();
			logger.debug("OmniConnectionThread init");
		}

		/*
		 * is our thread running
		 */
		public boolean isRunning() {
			return running;
		}

		/*
		 * Stop running and disconnect
		 */
		public void setRunning(boolean running) {
			this.running = running;
			if (!running)
				audioUpdateLock.notifyAll();
		}

		/*
		 * do we have a valid connection
		 */
		public boolean isConnected() {
			return connected;
		}

		/*
		 * the connection object for sending commands
		 */
		public Connection getOmniConnection() {
			return c;
		}

		/**
		 * Main processing loop
		 */
		@Override
		public void run() {
			running = true;
			logger.debug("OmniConnectionThread running");
			while (running) {
				connected = false;
				
				/*
				 * Connect to the system
				 */
				
				logger.debug("OmniConnectionThread trying to connect");
				
				try {
					c = new Connection(host, port, key);
					connected = true;
					logger.debug("OmniConnectionThread connected");
				} catch (Exception e) {
					logger.error("Could not connect", e);
				}

				/*
				 * If we fail to connect sleep a bit before trying again
				 */
				if (!connected) {
					try {
						Thread.currentThread();
						Thread.sleep(10 * 1000);
					} catch (InterruptedException ignored) {
					}

				} else {

					/*
					 * If we get disconnected then do nothing
					 */
					c.addDisconnectListener(new DisconnectListener() {
						@Override
						public void notConnectedEvent(Exception e) {
							logger.error("OmniConnectionThread was disconnected, will try again", e);
							connected = false;
						}
					});

					/*
					 * Real time device changes get processed here
					 */
					c.addNotificationListener(listener);

					/*
					 * Load everything and main audio source text loop
					 */
					try {
						SystemStatus sysstatus = c.reqSystemStatus();
						logger.info("System: " + sysstatus.toString());
						
						omni = c.reqSystemInformation().getModel() < 36;
						
						/*
						 * We need to explicitly tell the controller to send us
						 * real time notifications
						 */
						c.enableNotifications();

						if (generateItems) {
							OmnilinkItemGenerator gen = new OmnilinkItemGenerator(c);
							logger.info(gen.generateItemsAndGroups());
						}

						// update known items with state
						populateRefreshMapFromAllProviders();

						// load audio sources, we won't get updates on these
						readAllAudioSources();

						/*
						 * if we get disconnected then refresh any devices that
						 * we have to keep them up to date.
						 */

						while (running && c.connected()) {
							updateRefreshItems();
							/*
							 * Audio source text is not pushed in real time, so
							 * we poll for it
							 */
							updateAudioSourceTexts();
							try {
								synchronized (audioUpdateLock) {
									audioUpdateLock.wait(5000);
								}
							} catch (InterruptedException ignored) {
							}
						}
					} catch (IOException ex) {
						logger.error("Could not connect to system", ex);
					} catch (OmniNotConnectedException ex) {
						logger.error("Could not connect to system", ex.getNotConnectedReason());
					} catch (OmniInvalidResponseException ex) {
						logger.error("Could not connect to system", ex);
					} catch (OmniUnknownMessageTypeException ex) {
						logger.error("Could not connect to system", ex);
						// is this needed? I just added this without looking at
						// the code for 2 years
					} catch (Exception ex) {
						logger.error("Could not connect to system", ex);
					} finally {
						c.disconnect();
						c = null;
					}
				}
			}
		}

		/**
		 * This goes through a map of item names and updates their state's
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private void updateRefreshItems() throws IOException,
				OmniNotConnectedException, OmniInvalidResponseException,
				OmniUnknownMessageTypeException {

			if (refreshMap.size() == 0)
				return;

			Map<String, OmniLinkBindingConfig> itemMap = 
					new HashMap<String, OmniLinkBindingConfig>(refreshMap);
			
			for (String itemName : itemMap.keySet()) {
				OmniLinkBindingConfig config = itemMap.get(itemName);
				refreshMap.remove(itemName);
				if (config != null) {
					Integer number = new Integer(config.getNumber());
					for (OmniLinkBindingProvider provider : providers) {
						switch (config.getObjectType()) {
						case UNIT: {
							UnitProperties p = readUnitProperties(config
									.getNumber());
							Unit unit = unitMap.get(number);
							if (unit == null) {
								unit = new Unit(p);
								unitMap.put(number, unit);
							}
							config.setDevice(unit);
							unit.setProperties(p);
							unit.updateItem(provider.getItem(itemName), config,
									eventPublisher);
						}
							break;
						case THERMO_COOL_POINT:
						case THERMO_FAN_MODE:
						case THERMO_HEAT_POINT:
						case THERMO_HOLD_MODE:
						case THERMO_SYSTEM_MODE:
						case THERMO_TEMP: {
							ThermostatProperties p = readThermoProperties(
									config.getNumber());
							Thermostat thermo = thermostatMap.get(number);
							if (thermo == null) {
								thermo = new Thermostat(p, celius);
								thermostatMap.put(number, thermo);
							}
							config.setDevice(thermo);
							thermo.setProperties(p);
							thermo.updateItem(provider.getItem(itemName),
									config, eventPublisher);
						}
							break;
						case AUDIOZONE_MUTE:
						case AUDIOZONE_POWER:
						case AUDIOZONE_SOURCE:
						case AUDIOZONE_KEY:
						case AUDIOZONE_TEXT:
						case AUDIOZONE_TEXT_FIELD1:
						case AUDIOZONE_TEXT_FIELD2:
						case AUDIOZONE_TEXT_FIELD3:
						case AUDIOZONE_VOLUME: {
							AudioZoneProperties p = readAudioZoneProperties(config.getNumber());
							AudioZone audioZone = audioZoneMap.get(number);
							if (audioZone == null) {
								audioZone = new AudioZone(p);
								audioZone.setAudioSource(audioSourceMap);
								audioZoneMap.put(number, audioZone);
							}
							config.setDevice(audioZone);
							audioZone.setProperties(p);
							audioZone.updateItem(provider.getItem(itemName),
									config, eventPublisher);
						}
							break;
						case AUDIOSOURCE_TEXT:
						case AUDIOSOURCE_TEXT_FIELD1:
						case AUDIOSOURCE_TEXT_FIELD2:
						case AUDIOSOURCE_TEXT_FIELD3: {
							AudioSource as = audioSourceMap.get(number);
							if (as != null) {
								config.setDevice(as);
								as.updateItem(provider.getItem(itemName),
										config, eventPublisher);
							}
						}
							break;
						case AREA_ENTRY_TIMER:
						case AREA_EXIT_TIMER:
						case AREA_STATUS_ALARM:
						case AREA_STATUS_ENTRY_DELAY:
						case AREA_STATUS_EXIT_DELAY:
						case AREA_STATUS_MODE: {
							AreaProperties p = readAreaProperties(config.getNumber());
							Area area = areaMap.get(number);
							if (area == null) {
								area = new Area(p, omni);
								areaMap.put(number, area);
							}
							config.setDevice(area);
							area.setProperties(p);
							area.updateItem(provider.getItem(itemName), config,
									eventPublisher);
						}
							break;
						case AUX_CURRENT:
						case AUX_HIGH:
						case AUX_LOW:
						case AUX_STATUS: {
							AuxSensorProperties p = readAuxProperties(config.getNumber());
							Auxiliary auxiliary = auxMap.get(number);
							if (auxiliary == null) {
								auxiliary = new Auxiliary(p, celius);
								auxMap.put(number, auxiliary);
							}
							config.setDevice(auxiliary);
							auxiliary.setProperties(p);
							auxiliary.updateItem(provider.getItem(itemName), config,
									eventPublisher);
						}
							break;
						case ZONE_STATUS_ARMING:
						case ZONE_STATUS_CURRENT:
						case ZONE_STATUS_LATCHED:
						case ZONE_STATUS_ALL: {
							ZoneProperties p = readZoneProperties(config.getNumber());
							Zone zone = zoneMap.get(number);
							if (zone == null) {
								zone = new Zone(p);
								zoneMap.put(number, zone);
							}
							config.setDevice(zone);
							zone.setProperties(p);
							zone.updateItem(provider.getItem(itemName), config,
									eventPublisher);
						}
							break;
						case BUTTON: {
							ButtonProperties p = readButtonProperties(config.getNumber());
							Button button = buttonMap.get(number);
							if (button == null) {
								button = new Button(p);
								buttonMap.put(number, button);
							}
							config.setDevice(button);
						}
							break;
						default:
							break;
						}
					}
				}
			}
		}

		/**
		 * Iterate through the system units (lights)
		 * @param number of the unti
		 * @return UnitProperties of unit, null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private UnitProperties readUnitProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {
			Message m = c.reqObjectProperties(Message.OBJ_TYPE_UNIT, number, 0,
					ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_AREA_ALL,
					ObjectProperties.FILTER_3_ANY_LOAD);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((UnitProperties) m);
			}
			return null;
		}
		
		/**
		 * Read the properties of a thermostat
		 * 
		 * @param number of the thermostat
		 * @return ThermostatProperties of thermostat or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private ThermostatProperties readThermoProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_THERMO, number,
					0, ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_AREA_ALL,
					ObjectProperties.FILTER_3_ANY_LOAD);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((ThermostatProperties) m);
			}
			return null;
		}

		/**
		 * Read the properties of a Auxiliary sensor
		 * @param number of Auxiliary sensor
		 * @return AuxSensorProperties of Auxiliary sensor or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private AuxSensorProperties readAuxProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_AUX_SENSOR,
					number, 0, ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_AREA_ALL,
					ObjectProperties.FILTER_3_ANY_LOAD);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((AuxSensorProperties) m);
			}
			return null;
		}

		/**
		 * Read the properties of a audio zone
		 * @param number of audio zone
		 * @return AudioZoneProperties of audio zone, or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private AudioZoneProperties readAudioZoneProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_AUDIO_ZONE,
					number, 0, ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_NONE,
					ObjectProperties.FILTER_3_NONE);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((AudioZoneProperties) m);
			}
			return null;
		}

		/**
		 * Read the properties of a area
		 * @param number of area
		 * @return AreaProperties of area or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private AreaProperties readAreaProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_AREA, number, 0,
					ObjectProperties.FILTER_1_NAMED_UNAMED,
					ObjectProperties.FILTER_2_NONE,
					ObjectProperties.FILTER_3_NONE);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((AreaProperties) m);
			}
			return null;
		}

		/**
		 * Read the properties of a zone
		 * @param number of zone
		 * @return ZoneProperties of zone, or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private ZoneProperties readZoneProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_ZONE, number, 0,
					ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_AREA_ALL,
					ObjectProperties.FILTER_3_ANY_LOAD);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((ZoneProperties) m);
			}
			return null;
		}

		/**
		 * Read the properties of a button
		 * @param number of button
		 * @return ButtonProperties of button, or null if not found
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private ButtonProperties readButtonProperties(int number)
				throws IOException, OmniNotConnectedException,
				OmniInvalidResponseException, OmniUnknownMessageTypeException {

			Message m = c.reqObjectProperties(Message.OBJ_TYPE_BUTTON, number,
					0, ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_NONE,
					ObjectProperties.FILTER_3_NONE);
			if (m.getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				return ((ButtonProperties) m);
			}
			return null;
		}

		/**
		 * Populates all know audio sources which are used by known zones
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private void readAllAudioSources() throws IOException,
				OmniNotConnectedException, OmniInvalidResponseException,
				OmniUnknownMessageTypeException {
			int objnum = 0;
			Message m;
			while ((m = c.reqObjectProperties(Message.OBJ_TYPE_AUDIO_SOURCE,
					objnum, 1, ObjectProperties.FILTER_1_NAMED,
					ObjectProperties.FILTER_2_NONE,
					ObjectProperties.FILTER_3_NONE)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
				// logger.info(m.toString());
				AudioSourceProperties o = ((AudioSourceProperties) m);
				objnum = ((ObjectProperties) m).getNumber();

				Integer number = new Integer(o.getNumber());
				AudioSource as = audioSourceMap.get(number);
				if (as == null) {
					as = new AudioSource(o);
					audioSourceMap.put(number, as);
				}
				audioSourceMap.put(new Integer(o.getNumber()), as);
			}

		}

		/**
		 * Update audio zone text fields, this is one of the few 
		 * things we need to poll for
		 * @throws IOException
		 * @throws OmniNotConnectedException
		 * @throws OmniInvalidResponseException
		 * @throws OmniUnknownMessageTypeException
		 */
		private void updateAudioSourceTexts() throws IOException,
				OmniNotConnectedException, OmniInvalidResponseException,
				OmniUnknownMessageTypeException {
			Iterator<Integer> it = audioSourceMap.keySet().iterator();
			while (it.hasNext()) {
				Integer source = it.next();
				int pos = 0;
				Message m;
				boolean updated = false;
				Vector<String> text = new Vector<String>();
				while ((m = c.reqAudioSourceStatus(source.intValue(), pos)).getMessageType() == Message.MESG_TYPE_AUDIO_SOURCE_STATUS) {
					AudioSourceStatus a = (AudioSourceStatus) m;
					text.add(a.getSourceData());
					pos = a.getPosition();
				}

				AudioSource as = audioSourceMap.get(source);

				String text2[] = as.getAudioText();

				if (text.size() == text2.length) {
					for (int i = 0; i < text.size(); i++) {
						if (!text2[i].equals(text.get(i))) {
							updated = true;
						}
					}
				} else {
					updated = true;
				}

				if (updated) {
					as.setAudioText(text.toArray(new String[0]));
					updateAudioZoneText(as);
				}
			}
		}
	}

	/**
	 * Updates a AudioZone's source text fields when a
	 * AudioSource changes
	 * @param as the audio source that has been updated
	 */
	private void updateAudioZoneText(AudioSource as) {
		for (OmniLinkBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				OmniLinkBindingConfig config = provider
						.getOmniLinkBindingConfig(itemName);
				if (config != null) {
					switch (config.getObjectType()) {
					case AUDIOZONE_TEXT:
					case AUDIOZONE_TEXT_FIELD1:
					case AUDIOZONE_TEXT_FIELD2:
					case AUDIOZONE_TEXT_FIELD3: {
						AudioZone az = (AudioZone) config.getDevice();
						az.setAudioSource(audioSourceMap);
						logger.debug("looking if audio zone matches:"
								+ az.getProperties().getSource() + "=="
								+ as.getProperties().getNumber());
						if (az.getProperties().getSource() == as
								.getProperties().getNumber()) {
							az.updateItem(provider.getItem(itemName), config,
									eventPublisher);
						}
					}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	/**
	 * Update a device based on a status message from the system
	 * @param status
	 */
	protected void updateDeviceStatus(Status status) {
		
		logger.debug("updateDeviceStatus {} {}", status.getNumber(),
				status.getClass());
		
		Integer number = new Integer(status.getNumber());
		
		if (status instanceof UnitStatus && unitMap.containsKey(number)) {
			Unit unit = unitMap.get(number);
			unit.getProperties().updateUnit((UnitStatus) status);
			updateItemsForDevice(unit);
		} else if (status instanceof ThermostatStatus
				&& thermostatMap.containsKey(number)) {
			logger.debug("Updating thermo " + number);
			Thermostat thermo = thermostatMap.get(number);
			thermo.getProperties().updateThermostat((ThermostatStatus) status);
			updateItemsForDevice(thermo);
		} else if (status instanceof AudioZoneStatus
				&& audioZoneMap.containsKey(number)) {
			logger.debug("Updating audioZone " + number);
			AudioZone az = audioZoneMap.get(number);
			az.getProperties().updateAudioZone((AudioZoneStatus) status);
			updateItemsForDevice(az);
		} else if (status instanceof AreaStatus && areaMap.containsKey(number)) {
			logger.debug("Updating area " + number);
			Area area = areaMap.get(number);
			area.getProperties().updateArea((AreaStatus) status);
			updateItemsForDevice(area);
		} else if (status instanceof ZoneStatus && zoneMap.containsKey(number)) {
			logger.debug("Updating zone " + number);
			Zone zone = zoneMap.get(number);
			zone.getProperties().updateZone((ZoneStatus) status);
			updateItemsForDevice(zone);
		}
	}

	/**
	 * Update any items linked to a Omni device.
	 * @param device
	 */
	protected void updateItemsForDevice(OmnilinkDevice device) {
		for (OmniLinkBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				OmniLinkBindingConfig bindingConfig = provider.getOmniLinkBindingConfig(itemName);
				OmnilinkDevice itemDevice = bindingConfig.getDevice();
				Item item = provider.getItem(itemName);
				if (itemDevice != null && itemDevice == device) {
					device.updateItem(item, bindingConfig, eventPublisher);
				}
			}
		}
	}

	@Override
	public void objectStausNotification(ObjectStatus status) {
		Status[] statuses = status.getStatuses();
		for (Status s : statuses) {
			updateDeviceStatus(s);
		}

	}

	@Override
	public void otherEventNotification(OtherEventNotifications other) {

	}

}
