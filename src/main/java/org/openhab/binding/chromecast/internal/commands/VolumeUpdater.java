package org.openhab.binding.chromecast.internal.commands;

import java.io.IOException;

import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.binding.chromecast.internal.Helper;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;

public class VolumeUpdater implements IUpdater {

	private static final Logger logger = LoggerFactory.getLogger(VolumeUpdater.class);
	
	public void execute(EventPublisher eventPublisher, ChromeCast chromeCast,
			ChromecastBindingConfig itemConfig, Status status, MediaStatus mediaStatus) {

		eventPublisher.postUpdate(itemConfig.getItemName(), Helper.createState(itemConfig.getType(), String.valueOf(Math.round(status.volume.level*100))));
	}

	public void receiveCommand(ChromeCast chromeCast, ChromecastBindingConfig itemConfig, Command command) {
		
		try{
			float volume = Float.valueOf((command.toString()));
			chromeCast.setVolume(volume/100);
		} catch(NumberFormatException e){
			logger.info("Chromecast - Faulty state for " + itemConfig.getItemName() + " " + itemConfig.command + " volume");
		} catch (IOException e) {
			logger.info("Chromecast - Failed to set state " + itemConfig.getItemName() + " volume");
		}
	}
}
