/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal.core;

import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.oppoblurayplayer.OppoBlurayPlayerBindingProvider;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerBinding;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerCommand;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerUnknownCommandException;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command Receiver. Runs in dedicated thread to receive commands asynchronously from
 * the Oppo Bluray Player.
 * 
 * @author  (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerCommandReceiver implements Runnable {

	private static Logger log = LoggerFactory.getLogger(OppoBlurayPlayerCommandReceiver.class);

	private LinkedBlockingQueue<String> receiveQueue = new LinkedBlockingQueue<String>();
	private boolean stopped;
	private final OppoBlurayPlayerBinding oppoBlurayPlayerBinding;
	private final String playerName;
	
	public OppoBlurayPlayerCommandReceiver(OppoBlurayPlayerBinding binding, String playerName) {
		this.oppoBlurayPlayerBinding = binding;
		this.playerName = playerName;
	}

	/**
	 * Start receiving thread. 
	 * 
	 * Waits for updates to be added to the status receiveQueue
	 * and then looks them up and tries to find the matching
	 * binding update, so that the update can be posted on the
	 * OpenHAB event bus. 
	 */
	@Override
	public void run() {
		log.debug("Command receiver started.");

		try {
			while (true && !stopped) {
				String message = receiveQueue.take();
				log.debug("Received command {} on receiveQueue. Will attempt to send it to OpenHAB eventbus", message);
				OppoBlurayPlayerCommand command = null;
				try {
					command = OppoBlurayPlayerCommand.findMatchingCommandFromResponse(message);
					oppoBlurayPlayerBinding.postUpdate(oppoBlurayPlayerBinding.findFirstMatchingItemForCommand(this.playerName, command), createState(command.getItemType(), command, command.toString()));
				} catch (OppoBlurayPlayerNoMatchingItemException ex){
					log.warn("Command received from player but no matching item configured in *.items. Command was: {}", command.getOppoBlurayPlayerCommandType());
					log.debug("Command received from player but no matching item configured in *.items.", ex);
				} catch (OppoBlurayPlayerUnknownCommandException ex){
					log.warn("Ignoring unknown command received from player. Command was: {}", message);
				}
			}

		} catch (InterruptedException e) {
			log.debug("Command sender stopped.");
		} catch (Exception e) {
			log.error("Error writing command.", e);
		}
	}
	
	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param itemType
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>item</code> is <code>null</code> 
	 */
	private State createState(Class<? extends Item> itemType, OppoBlurayPlayerCommand command, String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				return OpenClosedType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return command.getState();
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			log.debug("Couldn't create state of type '{}' for value '{}'", itemType, transformedResponse);
			return StringType.valueOf(transformedResponse);
		}
	}
	
	/**
	 * Receives an update from the bluray player. Sending is done asynchronously so this
	 * method will return immediately.
	 * 
	 * This method expects to be called by the class that has a connection
	 * to the bluray player. When status updates come from the player
 	 * they are added to the receiveQueue to be processed.
	 * 
	 * @param cmd
	 *            Raw message from bluray player. eg, "@OK ON"
	 */
	public void receivedCommand(String cmd) {
		log.debug("Received command {}. Attempting to add it to the receiveQueue.", cmd);
		receiveQueue.add(cmd);
	}

	/**
	 * Stop execution.
	 */
	public void stop() {
		log.debug("Stop requested.");
		stopped = true;
	}
	
}
