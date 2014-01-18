/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command receiver. Runs in dedicated thread to receive command from the
 * Nikobus interface and broadcast them to all registered listeners.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusCommandReceiver implements Runnable {

	private static Logger log = LoggerFactory.getLogger(NikobusCommandReceiver.class);

	private AtomicReference<List<NikobusCommandListener>> atomicListReference;

	private LinkedBlockingQueue<byte[]> bufferQueue;

	private NikobusCommand command;

	private StringBuilder commandBuilder = new StringBuilder();

	private boolean stopped;

	private NikobusBinding binding;

	/**
	 * Default constructor.
	 */
	public NikobusCommandReceiver(NikobusBinding binding) {
		atomicListReference = new AtomicReference<List<NikobusCommandListener>>();
		atomicListReference.set(new ArrayList<NikobusCommandListener>());
		this.binding = binding;
	}

	/**
	 * Start listening for commands until stopped.
	 */
	@Override
	public void run() {

		if (bufferQueue == null) {
			log.error("Missing buffer.");
			return;
		}

		try {

			commandBuilder = new StringBuilder();
			byte[] buffer = null;

			while (true && !stopped) {

				if (buffer == null) {
					// read all incoming chars
					buffer = bufferQueue.take();
				}

				for (int i = 0; i < buffer.length; i++) {

					byte b = buffer[i];
					char c = (char) b;

					if (b == 13) {
						// end of command
						completeCommandInstance();
						continue;
					}

					if ((c == '#' || c == '$') && commandBuilder.length() > 0) {
						// start of new command
						completeCommandInstance();
					}

					commandBuilder.append(c);

					if (commandBuilder.length() == 5 && commandBuilder.toString().startsWith("$05")) {
						// we received an ACK message
						completeCommandInstance();
					}
				}

				buffer = null;

				if (command != null && command.isButtonPress()) {
					// wait briefly for next command to see if button
					// is still being pressed..
					buffer = bufferQueue.poll(85, TimeUnit.MILLISECONDS);
				}

				if (buffer == null && command != null) {
					// no more data received..
					publishCommand();
					command = null;
				}
			}

		} catch (InterruptedException e) {
			log.info("Command processor stopped.");
		}

	}

	/**
	 * Consider the data received so far as an atomic command.
	 */
	private void completeCommandInstance() {

		if (command != null) {
			if (command.getCommand().equals(commandBuilder.toString())) {
				// same command received as previous
				command.incrementCount();
			} else {
				// different command received, publish previous one
				publishCommand();
				command = new NikobusCommand(commandBuilder.toString());
			}
		} else {
			// new command received
			command = new NikobusCommand(commandBuilder.toString());
		}

		if (command.getRepeats() >= NikobusCommand.MAX_REPEAT) {
			publishCommand();
		}

		commandBuilder = new StringBuilder();
	}

	/**
	 * Broadcast a received command to all listeners.
	 */
	private void publishCommand() {

		if ((command.getRepeats() >= NikobusCommand.MAX_REPEAT)
				&& ((command.getRepeats() - NikobusCommand.MAX_REPEAT)
						% (NikobusCommand.MAX_REPEAT / 2) != 0)) {
			// when the button is pressed long e.g > 1 second, the first
			// command is send on 1 second and subsequent commands are sent
			// every 0.5 seconds.
			return;
		}

		log.debug("Received {}", command.toString());
		List<NikobusCommandListener> currentListeners = atomicListReference.get();

		for (NikobusCommandListener receiver : currentListeners) {
			receiver.processNikobusCommand(command, binding);
		}
	}

	/**
	 * Add a new Nikobus device which can listen for commands.
	 * 
	 * @param receiver
	 *            device.
	 */
	public void register(NikobusCommandListener receiver) {

		// rather than using synchronized methods/list, we consider the
		// listener list to be unmutable and always create a new copy
		// to avoid concurrent access issues.
		List<NikobusCommandListener> currentListeners = atomicListReference.get();
		
		if (currentListeners.contains(receiver)) {
			return;
		}
		
		List<NikobusCommandListener> newListeners = new ArrayList<NikobusCommandListener>();
		newListeners.addAll(currentListeners);
		newListeners.add(receiver);
		atomicListReference.set(newListeners);
	}

	/**
	 * Remove a Nikobus device listener.
	 * 
	 * @param receiver
	 *            device to remove.
	 */
	public void unregister(NikobusCommandListener receiver) {
		List<NikobusCommandListener> currentListeners = atomicListReference.get();
		List<NikobusCommandListener> newListeners = new ArrayList<NikobusCommandListener>();
		newListeners.addAll(currentListeners);
		newListeners.remove(receiver);
		atomicListReference.set(newListeners);
	}

	/**
	 * @param bufferQueue
	 *            buffer to poll for received characters
	 */
	public void setBufferQueue(LinkedBlockingQueue<byte[]> bufferQueue) {
		this.bufferQueue = bufferQueue;
	}

	/**
	 * Stop execution.
	 */
	public void stop() {
		stopped = true;
	}

	/**
	 * Drop all registered command listeners.
	 */
	public void unregisterAll() {
		List<NikobusCommandListener> newListeners = new ArrayList<NikobusCommandListener>();
		atomicListReference.set(newListeners);		
	}

	/**
	 * Unregister an command listener by name
	 * @param itemName
	 */
	public void unregisterItem(String itemName) {
		List<NikobusCommandListener> currentListeners = atomicListReference.get();
		List<NikobusCommandListener> newListeners = new ArrayList<NikobusCommandListener>();
		
		for (NikobusCommandListener listener : currentListeners) {
			if (listener.getName().equals(itemName)) {
					continue;
			} 
			newListeners.add(listener);
		}
		atomicListReference.set(newListeners);		
	}
	
}
