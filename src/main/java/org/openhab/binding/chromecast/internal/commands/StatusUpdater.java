package org.openhab.binding.chromecast.internal.commands;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.binding.chromecast.internal.Constants;
import org.openhab.binding.chromecast.internal.Helper;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.litvak.chromecast.api.v2.Application;
import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;

public class StatusUpdater implements IUpdater {

	private static final Logger logger = LoggerFactory.getLogger(StatusUpdater.class);
	
	public static final Map<MediaStatus.PlayerState, String> STATE_MAP = new HashMap<MediaStatus.PlayerState, String>();
	static {
		STATE_MAP.put(MediaStatus.PlayerState.PLAYING, 	Constants.STATE_PLAYING);
		STATE_MAP.put(MediaStatus.PlayerState.PAUSED, 	Constants.STATE_PAUSED);
		STATE_MAP.put(MediaStatus.PlayerState.BUFFERING,Constants.STATE_BUFFERING);
		STATE_MAP.put(MediaStatus.PlayerState.IDLE, 	Constants.STATE_IDLE);
	}
	
	public void execute(EventPublisher eventPublisher, ChromeCast chromeCast,
			ChromecastBindingConfig itemConfig, Status status, MediaStatus mediaStatus) {
		
		try{
			State state = null;
			if(mediaStatus != null && mediaStatus.idleReason != null && STATE_MAP.containsKey(mediaStatus.idleReason)){
				Helper.createState(itemConfig.getType(), String.valueOf(STATE_MAP.get(mediaStatus.idleReason)));
			}else {
				Application application = status.getRunningApp();
				if(!Constants.APP_DEFAULT.equals(application.name.toLowerCase())){
					state = Helper.createState(itemConfig.getType(), String.valueOf(Constants.STATE_PLAYING));
				} else {
					state = Helper.createState(itemConfig.getType(), String.valueOf(Constants.STATE_IDLE));
				}
			}
			
			if(state != null){
				eventPublisher.postUpdate(itemConfig.getItemName(), state);
			}
			
			logger.info("State updated to " + mediaStatus.playerState);
		}catch (Exception e){
			logger.error("Status update failed " + e.getMessage());
		}
	}

	public void receiveCommand(ChromeCast chromeCast, ChromecastBindingConfig itemConfig, Command command) {
		
		try{ 
			if(OnOffType.ON == command){
				chromeCast.play();
			} 
			else if(OnOffType.OFF == command){
				chromeCast.pause();
			} 
			else {
				String sCommand = command.toString().trim().toLowerCase();
				if(Constants.STATE_PLAYING.equals(sCommand)){
					chromeCast.play();
				} 
				else if(Constants.STATE_PAUSED.equals(sCommand)) {
					chromeCast.pause();
				}
			}
		} catch (Exception e){}
	}
}
