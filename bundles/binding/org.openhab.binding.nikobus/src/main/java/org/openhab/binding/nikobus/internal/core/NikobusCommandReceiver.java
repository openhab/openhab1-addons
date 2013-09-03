/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
	
}
