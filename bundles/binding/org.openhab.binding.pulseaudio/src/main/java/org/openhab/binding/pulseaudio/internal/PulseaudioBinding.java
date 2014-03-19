/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pulseaudio.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.pulseaudio.PulseaudioBindingProvider;
import org.openhab.binding.pulseaudio.internal.items.AbstractAudioDeviceConfig;
import org.openhab.binding.pulseaudio.internal.items.Sink;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class implements a binding of pulseaudio items to openHAB. The binding
 * configurations are provided by the {@link PulseaudioBindingProvider}.
 * </p>
 * 
 * <p>
 * The format of the binding configuration is simple and looks like this:
 * </p>
 * pulseaudio="&lt;serverId:item-name&gt;" where &lt;serverId&gt; is the
 * serverId the pulseaudio server as it is configured in the openhab.cfg and
 * &lt;item-name&gt; is the name of the an audio-item (audio-item are items of
 * type sink,source,sink-input,source-output) in the pulseaudio server
 * <p>
 * Switch items with this binding will allow to mute/unmute a pulseaudio item<br/>
 * Dimmer items will allow to change the volume of a pulseaudio item.
 * </p>
 * 
 * TODO: - add a possibility to move sink-inputs to other sinks - add a
 * possibility to change the members of a combined sink
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public class PulseaudioBinding extends AbstractActiveBinding<PulseaudioBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(PulseaudioBinding.class);

	public Map<String, PulseaudioServerConfig> serverConfigCache = new HashMap<String, PulseaudioServerConfig>();

	private Hashtable<String, PulseaudioClient> clients = new Hashtable<String, PulseaudioClient>();
	
	/**
	 * the refresh interval which is used to poll the pulseaudio servers
	 * (e.g. recording state; defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * RegEx to validate a pulseaudio server config
	 * <code>'^(.*?)\\.(host|port)$'</code>
	 */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port)$");
	
	
	public void activate(ComponentContext componentContext) {
		logger.debug("pulseaudio binding activated");
	}

	public void deactivate(ComponentContext componentContext) {
		logger.debug("pulseaudio binding deactivated");
		for (PulseaudioClient client : clients.values()) {
			client.disconnect();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Pulseaudio Monitor Service";
	}

	@Override
	public void activate() {
		super.activate();
		setProperlyConfigured(true);
	}

	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		PulseaudioBindingProvider provider = findFirstMatchingBindingProvider(
				itemName, command);
		if (provider == null) {
			logger.warn(
					"doesn't find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}

		String audioItemName = provider.getItemName(itemName);
		String serverId = provider.getServerId(itemName);
		// Item item = provider.getItem(itemName);
		String paCommand = provider.getCommand(itemName);
		PulseaudioCommandTypeMapping pulseaudioCommandType = null;
		if (paCommand != null && !paCommand.isEmpty()) {
			try {
				pulseaudioCommandType = PulseaudioCommandTypeMapping
						.valueOf(paCommand.toUpperCase());
			} catch (IllegalArgumentException e) {
				logger.warn(
						"unknown command specified for the given itemName [itemName={}, audio-item-name={}, serverId={}, command={}] => querying for values aborted!",
						new Object[] { itemName, audioItemName, serverId,
								command });
			}
		}

		PulseaudioClient client = clients.get(serverId);
		if (client==null) {
			// try to reconnect if the server is configured
			if (serverConfigCache.containsKey(serverId)) {
				connect(serverId, serverConfigCache.get(serverId));
				client = clients.get(serverId);
			}
		}		
		if (client == null) {
			logger.warn(
					"does't find matching pulseaudio client [itemName={}, serverId={}]",
					itemName, serverId);
			return;
		}

		if (audioItemName != null && !audioItemName.isEmpty()) {
			AbstractAudioDeviceConfig audioItem = client
					.getGenericAudioItem(audioItemName);
			if (audioItem == null) {
				logger.warn(
						"no corresponding audio-item found [audioItemName={}]",
						audioItemName);
				return;
			}
			State updateState = UnDefType.UNDEF;

			if (command instanceof IncreaseDecreaseType) {
				int volume = audioItem.getVolume();
				logger.debug(audioItemName + " volume is " + volume);
				if (command.equals(IncreaseDecreaseType.INCREASE))
					volume = Math.min(100, volume + 5);
				if (command.equals(IncreaseDecreaseType.DECREASE))
					volume = Math.max(0, volume - 5);
				logger.debug("setting " + audioItemName + " volume to "
						+ volume);
				client.setVolumePercent(audioItem, volume);
				updateState = new PercentType(volume);
			} else if (command instanceof PercentType) {
				client.setVolumePercent(audioItem,
						Integer.valueOf(command.toString()));
				updateState = (PercentType) command;
			} else if (command instanceof DecimalType) {
				if (pulseaudioCommandType == null
						|| pulseaudioCommandType
								.equals(PulseaudioCommandTypeMapping.VOLUME)) {
					// set volume
					client.setVolume(audioItem,
							Integer.valueOf(command.toString()));
					updateState = (DecimalType) command;
				}
				// all other pulseaudioCommandType's for DecimalTypes are
				// read-only and
				// therefore we do nothing here
			} else if (command instanceof OnOffType) {
				if (pulseaudioCommandType == null) {
					// Default behaviour when no command is specified => mute
					client.setMute(audioItem,
							((OnOffType) command).equals(OnOffType.ON));
					updateState = (OnOffType) command;
				} else {
					switch (pulseaudioCommandType) {
					case EXISTS:
						// logically the module of the audio item should be
						// unloaded here if command==OFF
						// but it cannot be loaded again, when unloaded once so
						// we better do nothing here
						break;
					case MUTED:
						client.setMute(audioItem,
								((OnOffType) command).equals(OnOffType.ON));
						updateState = (OnOffType) command;
						break;
					case RUNNING:
					case CORKED:
					case SUSPENDED:
					case IDLE:
						// the state of an audio-item cannot be changed
						break;
					case ID:
					case MODULE_ID:
						// the id or module-id of an audio-item cannot be changed
						break;
					case VOLUME:
						if (((OnOffType) command).equals(OnOffType.ON)) {
							// Set Volume to 100%
							client.setVolume(audioItem, 100);
						} else {
							// set volume to 0
							client.setVolume(audioItem, 100);
						}
						updateState = (OnOffType) command;
						break;
					case SLAVE_SINKS:
						// also an read-only field
						break;
					}
				}
			} else if (command instanceof StringType) {
				if (pulseaudioCommandType != null) {
					switch (pulseaudioCommandType) {
					case CORKED:
					case EXISTS:
					case ID:
					case IDLE:
					case MODULE_ID:
					case MUTED:
					case RUNNING:
					case SUSPENDED:
					case VOLUME:
						// no action here
						break;
					case SLAVE_SINKS:
						if (audioItem instanceof Sink && ((Sink)audioItem).isCombinedSink()) {
							// change the slave sinks of the given combined sink to the new value
							Sink mainSink = (Sink)audioItem;
							ArrayList<Sink> slaveSinks = new ArrayList<Sink>();
							for (String slaveSinkName : StringUtils.split(command.toString(),",")) {
								Sink slaveSink = client.getSink(slaveSinkName);
								if (slaveSink!=null) {
									slaveSinks.add(slaveSink);
								}
							}
							logger.debug(slaveSinks.size()+" slave sinks");
							if (slaveSinks.size()>0) {
								
								client.setCombinedSinkSlaves(mainSink, slaveSinks);
							}
						}
						break;
					}
				}
			}
			
			if (!updateState.equals(UnDefType.UNDEF))
				eventPublisher.postUpdate(itemName, updateState);
		} else if (command instanceof StringType) {
			// send the command directly to the pulseaudio server
			client.sendCommand(command.toString());
			eventPublisher.postUpdate(itemName, (StringType) command);
		}
	}

	/**
	 * Find the first matching {@link PulseaudioBindingProvider} according to
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private PulseaudioBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {

		PulseaudioBindingProvider firstMatchingProvider = null;
		Class<? extends Item> itemClass = mapCommandToItemType(command);

		for (PulseaudioBindingProvider provider : this.providers) {
			
			String audioItemName = provider.getItemName(itemName);
			Class<? extends Item> itemType = provider.getItemType(itemName);
			
			if (itemClass.equals(itemType)) {
				// StringItems do not need an audioItemName
				firstMatchingProvider = provider;
				break;
			} else if (audioItemName != null && itemType != null && itemType.isAssignableFrom(itemClass)) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	private Class<? extends Item> mapCommandToItemType(Command command) {
		if (command instanceof IncreaseDecreaseType) {
			return DimmerItem.class;
		} else if (command instanceof PercentType) {
			return DimmerItem.class;
		} else if (command instanceof DecimalType) {
			return NumberItem.class;
		} else if (command instanceof OnOffType) {
			return SwitchItem.class;
		} else if (command instanceof StringType) {
			return StringItem.class;
		} else {
			logger.warn("No explicit mapping found for command type '{}' - return StringItem.class instead", command.getClass().getSimpleName());
			return StringItem.class;
		}
	}

	/**
	 * Create a new TCP connection to the pulseaudio server with the given
	 * <code>host</code> and <code>port</code>
	 * 
	 * @param host
	 * @param port
	 */
	private void connect(String serverId, PulseaudioServerConfig serverConfig) {
		if (serverConfig.host != null && serverConfig.port > 0) {
			try {
				clients.put(serverId, 
					new PulseaudioClient(serverConfig.host, serverConfig.port));
				boolean isConnected = clients.get(serverId).isConnected();
				if (isConnected) {
					logger.info(
							"Established connection to Pulseaudio server on Host '{}' Port '{}'.",
							serverConfig.host, serverConfig.port);
					// refesh the states
					execute();
				} else {
					logger.warn(
							"Establishing connection to Pulseaudio server [Host '{}' Port '{}'] timed out.",
							serverConfig.host, serverConfig.port);
				}
			} catch (IOException ioe) {
				logger.error("Couldn't connect to Pulsaudio server [Host '"
					+ serverConfig.host + "' Port '" + serverConfig.port + "']: ", ioe.getLocalizedMessage());
			}
		} else {
			logger.warn(
					"Couldn't connect to Pulseaudio server because of missing connection parameters [Host '{}' Port '{}'].",
					serverConfig.host, serverConfig.port);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@SuppressWarnings("incomplete-switch")
	public void execute() {
		List<PulseaudioClient> updatedClients = new ArrayList<PulseaudioClient>();
		for (PulseaudioBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {

				String audioItemName = provider.getItemName(itemName);
				String serverId = provider.getServerId(itemName);
				Class<? extends Item> itemType = provider.getItemType(itemName);
				String command = provider.getCommand(itemName);
				PulseaudioCommandTypeMapping commandType = null;
				if (command != null && !command.isEmpty()) {
					try {
						commandType = PulseaudioCommandTypeMapping
								.valueOf(command.toUpperCase());
					} catch (IllegalArgumentException e) {
						logger.warn(
								"unknown command specified for the given itemName [itemName={}, audio-item-name={}, serverId={}, command={}] => querying for values aborted!",
								new Object[] { itemName, audioItemName,
										serverId, command });
						continue;
					}
				}

				if (itemType.isAssignableFrom(GroupItem.class) || itemType.isAssignableFrom(StringItem.class)) {
					// GroupItems/StringItems should not receive any updated
					// directly
					continue;
				}

				PulseaudioClient client = clients.get(serverId);
				if (client == null) {
					logger.warn(
							"connection to pulseaudio server in not available "
									+ "for the given itemName [itemName={}, audio-item-name={}, serverId={}, command={}] => querying for values aborted!",
							new Object[] { itemName, audioItemName, serverId,
									command });
					continue;
				}
				if (audioItemName == null) {
					logger.warn(
							"audio-item-name isn't configured properly "
									+ "for the given itemName [itemName={}, audio-item-name={}, serverId={}, command={}] => querying for values aborted!",
							new Object[] { itemName, audioItemName, serverId,
									command });
					continue;
				}
				if (!updatedClients.contains(client)) {
					// update the clients data structure to avoid
					// inconsistencies
					client.update();
					updatedClients.add(client);
				}

				State value = UnDefType.UNDEF;
				AbstractAudioDeviceConfig audioItem = client
						.getGenericAudioItem(audioItemName);

				if (audioItem != null) {
					// item found
					if (itemType.isAssignableFrom(DimmerItem.class)) {
						value = new PercentType(audioItem.getVolume());
					} else if (itemType.isAssignableFrom(NumberItem.class)) {
						if (commandType == null) {
							// when no other pulseaudioCommand specified, we use
							// VOLUME
							value = new DecimalType(audioItem.getVolume());
						} else {
							// NumberItems can only handle VOLUME, MODULE_ID and ID command types
							switch (commandType) {
							case VOLUME:
								value = new DecimalType(audioItem.getVolume());
								break;
							case ID:
								value = new DecimalType(audioItem.getId());
								break;
							case MODULE_ID:
								if (audioItem.getModule()!=null)
									value = new DecimalType(audioItem.getModule().getId());
								break;
							}
						}
					} else if (itemType.isAssignableFrom(SwitchItem.class)) {
						if (commandType == null) {
							// Check if item is unmuted and running
							if (!audioItem.isMuted()
									&& audioItem.getState() != null
									&& audioItem.getState().equals(
											AbstractAudioDeviceConfig.State.RUNNING)) {
								value = OnOffType.ON;
							} else {
								value = OnOffType.OFF;
							}
						} else {
							switch (commandType) {
							case EXISTS:
								value = OnOffType.ON;
								break;
							case MUTED:
								value = audioItem.isMuted() ? OnOffType.ON
										: OnOffType.OFF;
								break;
							case RUNNING:
							case CORKED:
							case SUSPENDED:
							case IDLE:
								try {
								value = audioItem.getState() != null
										&& audioItem.getState().equals(
												AbstractAudioDeviceConfig.State.valueOf(commandType.name())) ? OnOffType.ON
										: OnOffType.OFF;
								}
								catch (IllegalArgumentException e) {
									logger.warn("no corresponding AbstractAudioDeviceConfig.State found for "+commandType.name());
								}
								break;
							}
						}
					} else if (itemType.isAssignableFrom(StringItem.class)) {
						if (commandType == null) {
							value = new StringType(audioItem.toString());
						}
						else if (audioItem instanceof Sink) {
							Sink sink = (Sink)audioItem;
							switch (commandType) {
							case SLAVE_SINKS:
								if (sink.isCombinedSink()) {
									value = new StringType(StringUtils.join(sink.getCombinedSinkNames(),","));
								}
								break;
							}
						}
					} else {
						logger.debug("unhandled item type [type={}, name={}]",
								itemType.getClass(), audioItemName);
					}
				} else if (itemType.isAssignableFrom(SwitchItem.class)) {
					value = OnOffType.OFF;
				}

				eventPublisher.postUpdate(itemName, value);
			}
		}
	}

	/**
	 * Connects to all configured {@link PulseaudioClient}s
	 */
	private void connectAllPulseaudioServers() {
		for (String serverId : serverConfigCache.keySet()) {
			connect(serverId, serverConfigCache.get(serverId));
		}
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
	
		if (config != null) {
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				
				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given pulseaudio-config-key '" + key
						+ "' does not follow the expected pattern '<serverId>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String serverId = matcher.group(1);

				PulseaudioServerConfig serverConfig = serverConfigCache
						.get(serverId);
				if (serverConfig == null) {
					serverConfig = new PulseaudioServerConfig();
					serverConfigCache.put(serverId, serverConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					serverConfig.host = value;
				} else if ("port".equals(configKey)) {
					serverConfig.port = Integer.valueOf(value);
				} else {
					throw new ConfigurationException(
						configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
			connectAllPulseaudioServers();
		}
	}
	
	
	/**
	 * Internal data structure which carries the connection details of one
	 * Pulseaudio server (there could be several)
	 * 
	 * @author Tobias Bräutigam
	 */
	static class PulseaudioServerConfig {

		String host = "localhost";
		int port = 4712;
		PulseaudioClient client;

		@Override
		public String toString() {
			return "Pulseaudio [host=" + host + ", port=" + port + "]";
		}

	}
	
}
