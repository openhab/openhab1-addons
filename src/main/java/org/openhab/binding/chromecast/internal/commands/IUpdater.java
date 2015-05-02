package org.openhab.binding.chromecast.internal.commands;

import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;

public interface IUpdater {
	void execute(EventPublisher eventPublisher, ChromeCast chromeCast, ChromecastBindingConfig itemConfig, Status status, MediaStatus mediaStatus);
	void receiveCommand(ChromeCast chromeCast, ChromecastBindingConfig itemConfig, Command command);
}