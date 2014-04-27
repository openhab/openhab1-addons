/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpd.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bff.javampd.MPD;
import org.bff.javampd.MPDPlayer;
import org.bff.javampd.MPDPlayer.PlayerStatus;
import org.bff.javampd.events.PlayerBasicChangeEvent;
import org.bff.javampd.events.PlayerBasicChangeListener;
import org.bff.javampd.events.PlayerChangeEvent;
import org.bff.javampd.events.VolumeChangeEvent;
import org.bff.javampd.events.VolumeChangeListener;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.monitor.MPDStandAloneMonitor;
import org.bff.javampd.objects.MPDSong;
import org.openhab.binding.mpd.MpdBindingProvider;
import org.openhab.binding.mpd.internal.MultiClickDetector.MultiClickListener;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// to manually detect changes
import org.bff.javampd.events.TrackPositionChangeEvent;
import org.bff.javampd.events.TrackPositionChangeListener;


/**
 * Binding which communicates with (one or many) MPDs. It registers as listener
 * of the MPDs to accomplish real bidirectional communication.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Petr.Klus
 *
 * @since 0.8.0
 */
public class MpdBinding extends AbstractBinding<MpdBindingProvider> implements ManagedService, VolumeChangeListener, PlayerBasicChangeListener, TrackPositionChangeListener, MultiClickListener<Command> {

	private static final String MPD_SCHEDULER_GROUP = "MPD";

	private static final Logger logger = LoggerFactory.getLogger(MpdBinding.class);
		
	private Map<String, MpdPlayerConfig> playerConfigCache = new HashMap<String, MpdPlayerConfig>();
	
	/** RegEx to validate a mpdPlayer config <code>'^(.*?)\\.(host|port|password)$'</code> */
	private static final Pattern EXTRACT_PLAYER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port|password)$");
	
	/** The value by which the volume is changed by each INCREASE or DECREASE-Event */ 
	private static final int VOLUME_CHANGE_SIZE = 5;

	/** The connection timeout to wait for a MPD connection */
	private static final int CONNECTION_TIMEOUT = 5000;
	
	private static MultiClickDetector<Command> clickDetector;
	
	
	public MpdBinding() {
		playerConfigCache = new HashMap<String, MpdBinding.MpdPlayerConfig>();
		clickDetector = new MultiClickDetector<Command>(this, 300);
	}
	
	
	public void activate() {
		connectAllPlayersAndMonitors();
	}
	
	public void deactivate() {
		disconnectPlayersAndMonitors();
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		
		MpdBindingProvider provider;
		String matchingPlayerCommand;
		Object params = new Object(); //nothing by default
		if  (command instanceof PercentType) {
//			we have received volume adjustment request
			matchingPlayerCommand = "PERCENT";		
			params = command;
		} else {
			matchingPlayerCommand = command.toString();
		}
		
		provider = findFirstMatchingBindingProvider(itemName, matchingPlayerCommand);	
		
		if (provider == null) {			
			logger.warn("cannot find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String playerCommand = 
			provider.getPlayerCommand(itemName, matchingPlayerCommand);
		if (StringUtils.isNotBlank(playerCommand)) {
			executePlayerCommand(playerCommand, params);
		}
		
	}

	/**
	 * Find the first matching {@link MpdBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 * provider could be found
	 */
	private MpdBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		MpdBindingProvider firstMatchingProvider = null;
		for (MpdBindingProvider provider : this.providers) {
			
			String playerCommand = provider.getPlayerCommand(itemName, command);
			if (playerCommand != null) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}	
	
	/**
	 * Executes the given <code>playerCommandLine</code> on the MPD. The
	 * <code>playerCommandLine</code> is split into it's properties 
	 * <code>playerId</code> and <code>playerCommand</code>.
	 * 
	 * @param playerCommandLine the complete commandLine which gets splitted into
	 * it's properties.
	 */
	private void executePlayerCommand(String playerCommandLine, Object commandParams) {
		String[] commandParts = playerCommandLine.split(":");
		String playerId = commandParts[0];
		String playerCommand = commandParts[1];

		MPD daemon = findMPDInstance(playerId);
		if (daemon == null) {
			// we give that player another chance -> try to reconnect
			reconnect(playerId);
		}
		
		if (daemon != null) {
			PlayerCommandTypeMapping pCommand = null;			
			try {
				pCommand = PlayerCommandTypeMapping.fromString(playerCommand);
				MPDPlayer player = daemon.getMPDPlayer();
				
				switch (pCommand) {
					case PAUSE: player.pause(); break;
					case PLAY: player.play(); break;
					case STOP: player.stop(); break;
					case VOLUME_INCREASE: player.setVolume(player.getVolume() + VOLUME_CHANGE_SIZE); break;
					case VOLUME_DECREASE: player.setVolume(player.getVolume() - VOLUME_CHANGE_SIZE); break;
					case NEXT: player.playNext(); break;
					case PREV: player.playPrev(); break;
					case VOLUME: 
						logger.debug("Volume adjustment received: '{}' '{}'", pCommand, commandParams);
						player.setVolume(((PercentType) commandParams).intValue());
						break;

				}
			}
			catch (MPDPlayerException pe) {
				logger.error("error while executing {} command: " + pe.getMessage(), pCommand);
			}
			catch (Exception e) {
				logger.warn("unknow playerCommand '{}'", playerCommand);
			}
		}
		else {
			logger.warn("didn't find player configuration instance for playerId '{}'", playerId);
		}
			
		logger.info("executed commandLine '{}' for player '{}'", playerCommand, playerId);
	}
	
	private MPD findMPDInstance(String playerId) {
		MpdPlayerConfig playerConfig = playerConfigCache.get(playerId);
		if (playerConfig != null) {
			return playerConfig.instance;
		}
		return null;
	}

	private String findPlayerId(Object object) {
		for (String playerId : playerConfigCache.keySet()) {
			MpdPlayerConfig playerConfig = playerConfigCache.get(playerId);
			if (playerConfig != null && playerConfig.monitor != null && playerConfig.monitor.equals(object)) {
				return playerId;
			}
		}
		return null;
	}
	
	/**
	 * Implementation of {@link PlayerBasicChangeListener}. Posts the translated
	 * type of the {@link PlayerChangeEvent} onto the internal event bus. 
	 * <code>PLAYER_STARTED</code>-Events are translated to <code>ON</code> whereas
	 * <code>PLAYER_PAUSED</code> and <code>PLAYER_STOPPED</code>-Events are 
	 * translated to <code>OFF</code>.
	 * 
	 * In this case, we use the play state change to trigger full state update, including
	 * artist and song name.
	 * 
	 * @param pbce the event which type is translated and posted onto the internal
	 * event bus
	 */
	public void playerBasicChange(PlayerBasicChangeEvent pbce) {
		String playerId = findPlayerId(pbce.getSource());
				
		//	trigger track name update
		determineSongChange(playerId);
		
		// trigger our play state change		
		determinePlayStateChange(playerId);
	}
	

	HashMap<String, MPDSong> songInfoCache = new HashMap<String, MPDSong>();
	HashMap<String, PlayerStatus> playerStatusCache = new HashMap<String, PlayerStatus>();
	public void trackPositionChanged(TrackPositionChangeEvent tpce) {
		
		// cache song name internally, we do not want to fire every time		
		String playerId = findPlayerId(tpce.getSource());		
		// update track name				
		determineSongChange(playerId);
		
		// also update play state		
		determinePlayStateChange(playerId);
	}
	
	private void broadcastPlayerStateChange(String playerId, PlayerCommandTypeMapping reportTo, OnOffType reportStatus) {
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(playerId, reportTo);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, reportStatus);
			}
		}		
	}
	
	private void determinePlayStateChange(String playerId) {		
		MPD daemon = findMPDInstance(playerId);
		if (daemon == null) {
			// we give that player another chance -> try to reconnect
			reconnect(playerId);
		}
		if (daemon != null) {
			try {
				MPDPlayer player = daemon.getMPDPlayer();
				
				// get the song object here
				PlayerStatus ps = player.getStatus();
				
				PlayerStatus curPs = playerStatusCache.get(playerId);
				if (curPs != null) {
					if (ps != curPs) {
						logger.debug("Play state of '{}' changed", playerId);
						playerStatusCache.put(playerId, ps);
						
						PlayerCommandTypeMapping reportTo;
						OnOffType reportStatus;
						// trigger song update
						if (ps.equals(PlayerStatus.STATUS_PAUSED) || ps.equals(PlayerStatus.STATUS_STOPPED)) {
							// stopped	
							reportTo = PlayerCommandTypeMapping.STOP;
							reportStatus = OnOffType.OFF;
						} else {
							//	playing
							reportTo = PlayerCommandTypeMapping.PLAY;
							reportStatus = OnOffType.ON;
						}						
						broadcastPlayerStateChange(playerId, reportTo, reportStatus);						
					} else {
						// nothing, same state						
					}
				} else {
					playerStatusCache.put(playerId, ps);
				}								
			} catch (MPDPlayerException pe) {
				logger.error("error while updating player status: {}" + pe.getMessage(), playerId);
			} catch (Exception e) {
				logger.warn("Failed to communicate with '{}'", playerId);
			}
		} else {
			logger.warn("didn't find player configuration instance for playerId '{}'", playerId);
		}				
	}
	
	private void determineSongChange(String playerId) {
		MPD daemon = findMPDInstance(playerId);
		if (daemon == null) {
			// we give that player another chance -> try to reconnect
			reconnect(playerId);
		}
		if (daemon != null) {
			try {
				MPDPlayer player = daemon.getMPDPlayer();
				
				// get the song object here
				MPDSong curSong = player.getCurrentSong();				
				
				MPDSong curSongCache = songInfoCache.get(playerId);
				if (curSongCache != null) {
					// we have some info
					if (curSong.getId() != curSongCache.getId()) {
						songInfoCache.put(playerId, curSong);
						songChanged(playerId, curSong);
					} else {
						// nothing, same song
						// detect play state						
					}
				} else {
					// no info about player's playback state					
					songInfoCache.put(playerId, curSong);
					// action the song change&notification 
					songChanged(playerId, curSong);
				}
			}
			catch (MPDPlayerException pe) {
				logger.error("error while updating player status: {}" + pe.getMessage(), playerId);
			} catch (Exception e) {
				logger.warn("Failed to communicate with '{}'", playerId);
			}
		}
		else {
			logger.warn("didn't find player configuration instance for playerId '{}'", playerId);
		}
	}
	

	
	private void songChanged(String playerId, MPDSong newSong) {
		
		logger.debug("Current song {}: {}", playerId, newSong.getTitle().toString());
		
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(playerId, PlayerCommandTypeMapping.TRACKINFO);
		//move to utilities?
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, new StringType(newSong.getTitle().toString()));		
				logger.debug("Updated title: {} {}", itemName, newSong.getTitle().toString());
			}
		}
		
		itemNames = getItemNamesByPlayerAndPlayerCommand(playerId, PlayerCommandTypeMapping.TRACKARTIST);
		for (String itemName : itemNames) {
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, new StringType(newSong.getArtist().toString()));		
				logger.debug("Updated artist: {}, {}", itemName, newSong.getArtist().toString());
			}
		}
	}
	
	
	/**
	 * More advanced state detection - allows to detect track changes etc. 
	 * However, the underlying MPD library does not seem to support this well
	 */
	public void playerChanged(PlayerChangeEvent pce) {
				

	
		
	}
	

	/**
	 * Implementation of {@link VolumeChangeListener}. Posts the volume value
	 * of the {@link VolumeChangeEvent} onto the internal event bus.
	 * 
	 * @param vce the event which volume value is posted onto the internal event bus
	 */
	public void volumeChanged(VolumeChangeEvent vce) {
		String playerId = findPlayerId(vce.getSource());
		logger.debug("Volume on {} changed to {}", playerId, vce.getVolume());
		String[] itemNames = getItemNamesByPlayerAndPlayerCommand(playerId, PlayerCommandTypeMapping.VOLUME);
		for (String itemName : itemNames) {		
			if (StringUtils.isNotBlank(itemName)) {
				eventPublisher.postUpdate(itemName, new PercentType(vce.getVolume()));						
			}
		}
	}
	
	private String[] getItemNamesByPlayerAndPlayerCommand(String playerId, PlayerCommandTypeMapping playerCommand) {
		Set<String> itemNames = new HashSet<String>();
		for (MpdBindingProvider provider : this.providers) {
			itemNames.addAll(Arrays.asList(
				provider.getItemNamesByPlayerAndPlayerCommand(playerId, playerCommand)));
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}
	
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			disconnectPlayersAndMonitors();
			cancelScheduler();
			
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				
				String key = (String) keys.nextElement();
				
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				
				Matcher matcher = EXTRACT_PLAYER_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given mpd player-config-key '" + key + "' does not follow the expected pattern '<playername>.<host|port>'");
					continue;
				}
				
				matcher.reset();
				matcher.find();
				
				String playerId = matcher.group(1);
				
				MpdPlayerConfig playerConfig = playerConfigCache.get(playerId);
				if (playerConfig == null) {
					playerConfig = new MpdPlayerConfig();
					playerConfigCache.put(playerId, playerConfig);
				}
				
				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					playerConfig.host = value;
				}
				else if ("port".equals(configKey)) {
					playerConfig.port = Integer.valueOf(value);
				}
				else if ("password".equals(configKey)) {
					playerConfig.password = value;
				}
				else {
					throw new ConfigurationException(
						configKey, "the given configKey '" + configKey + "' is unknown");
				}
				
			}
			
			connectAllPlayersAndMonitors();
			scheduleReconnect();
		}
	}


	private void scheduleReconnect() {
		Scheduler sched;
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
			JobDetail job = newJob(ReconnectJob.class)
			    .withIdentity("Reconnect", MPD_SCHEDULER_GROUP)
			    .build();
			CronTrigger trigger = newTrigger()
			    .withIdentity("Reconnect", MPD_SCHEDULER_GROUP)
			    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
			    .build();
	
			sched.scheduleJob(job, trigger);
			logger.debug("Scheduled a daily MPD Reconnect of all MPDs");
		} catch (SchedulerException se) {
			logger.warn("scheduling MPD Reconnect failed", se);
		}
	}
	
	/**
	 * Delete all quartz scheduler jobs of the group <code>MPD</code>.
	 */
	private void cancelScheduler() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			Set<JobKey> jobKeys = sched.getJobKeys(jobGroupEquals(MPD_SCHEDULER_GROUP));
			if (jobKeys.size() > 0) {
				sched.deleteJobs(new ArrayList<JobKey>(jobKeys));
				logger.debug("Found {} jobs to delete from DefaulScheduler (keys={})", jobKeys.size(), jobKeys);
			}
		} catch (SchedulerException e) {
			logger.warn("Couldn't remove job: {}", e.getMessage());
		}		
	}
	
	/**
	 * Connects to all configured {@link MPD}s
	 */
	private void connectAllPlayersAndMonitors() {
		logger.debug("MPD binding connecting to players");
		for (String playerId : playerConfigCache.keySet()) {
			connect(playerId);
		}
	}
	
	/**
	 * Connects the player <code>playerId</code> to the given <code>host</code>
	 * and <code>port</code> and registers this binding as MPD-Event listener.
	 * 
	 * @param playerId
	 * @param host
	 * @param port
	 */
	private void connect(String playerId) {
		MpdPlayerConfig config = null;
		try {
	    	
	    	config = playerConfigCache.get(playerId);
	    	if (config != null && config.instance == null) {
	    		
	    		MPD mpd = new MPD(config.host, config.port, config.password, CONNECTION_TIMEOUT);
	    		
	    	    MPDStandAloneMonitor mpdStandAloneMonitor = new MPDStandAloneMonitor(mpd, 500);
	    	    	mpdStandAloneMonitor.addVolumeChangeListener(this);
	    	    	mpdStandAloneMonitor.addPlayerChangeListener(this);
	    	    	mpdStandAloneMonitor.addTrackPositionChangeListener(this);	    	    	
	    	    Thread monitorThread = new Thread(
	    	    	mpdStandAloneMonitor, "MPD Monitor (player:" + playerId + ")");
	    	    monitorThread.start();
	    	    
    			config.instance = mpd;
    			config.monitor = mpdStandAloneMonitor;
	    		
	    		logger.debug("Connected to player '{}' with config {}", playerId, config);
	    	}
	    	
	    }
		catch(MPDConnectionException ce) {
	        logger.error("Error connecting to player '" + playerId + "' with config {}", config, ce);
	    }
		catch (UnknownHostException uhe) {
	    	logger.error("Wrong connection details for player {}", playerId, uhe);
		}
	}

	/**
	 * Disconnects all available {@link MPD}s and their {@link MPDStandAloneMonitor}-Thread.
	 */
	private void disconnectPlayersAndMonitors() {
		for (String playerId : playerConfigCache.keySet()) {
			disconnect(playerId);
		}
	}

	/**
	 * Disconnects the player <code>playerId</code>
	 * 
	 * @param playerId the id of the player to disconnect from
	 */
	private void disconnect(String playerId) {
        try {
			MpdPlayerConfig playerConfig = playerConfigCache.get(playerId);
			MPDStandAloneMonitor monitor = playerConfig.monitor;
			if (monitor != null) {
				monitor.stop();
			}
        	MPD mpd = playerConfig.instance;
        	if (mpd != null) {
        		mpd.close();
        	}
		} catch (MPDConnectionException ce) {
			logger.warn("couldn't disconnect player {}", playerId);
		} catch (MPDResponseException re) {
			logger.warn("received response error {}", re.getLocalizedMessage());
		}
	}
	
	/**
	 * Reconnects to <code>playerId</code> that means disconnect first and try
	 * to connect again.
	 *  
	 * @param playerId the id of the player to disconnect from
	 */
	private void reconnect(String playerId) {
		logger.info("reconnect player {}", playerId);
		disconnect(playerId);
		connect(playerId);
	}
	
	/**
	 * Reconnects all MPDs and Monitors. Meaning disconnect first and try
	 * to connect again.
	 */
	private void reconnectAllPlayerAndMonitors() {
		disconnectPlayersAndMonitors();
		connectAllPlayersAndMonitors();
	}

		
	/**
	 * Internal data structure which carries the connection details of one
	 * MPD (there could be several)
	 *  
	 * @author Thomas.Eichstaedt-Engelen
	 */
	static class MpdPlayerConfig {
		
		String host;
		int port;
		String password;
		
		MPD instance;
		MPDStandAloneMonitor monitor;
		
		@Override
		public String toString() {
			return "MPD [host=" + host + ", port=" + port + ", password=" + password +"]";
		}
		
	}


	public void onClick(Command type) {
	}

	public void onDoubleClick(Command type) {
	}
	
	
	/**
	 * A quartz scheduler job to simply do a reconnection to the MPDs.
	 */
	public class ReconnectJob implements Job {
		public void execute(JobExecutionContext context) throws JobExecutionException {
			reconnectAllPlayerAndMonitors();
		}
	}

}
