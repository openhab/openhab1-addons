package org.openhab.binding.chromecast.internal.commands;

import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.binding.chromecast.internal.Helper;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;

public class ApplicationUpdater implements IUpdater {

	public void execute(EventPublisher eventPublisher, ChromeCast chromeCast,
			ChromecastBindingConfig itemConfig, Status status, MediaStatus mediaStatus) {

		eventPublisher.postUpdate(itemConfig.getItemName(), Helper.createState(itemConfig.getType(), status.getRunningApp().name));
	}

	public void receiveCommand(ChromeCast chromeCast, ChromecastBindingConfig itemConfig, Command command) {}
}
