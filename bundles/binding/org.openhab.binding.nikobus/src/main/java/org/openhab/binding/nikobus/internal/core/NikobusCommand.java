/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

/**
 * NikobusCommand. Represents a (repeated) serial command which was sent to the
 * bus or received from the bus.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusCommand {

	public static int MAX_REPEAT = 25;

	private String command;

	private int repeats = 1;

	private long timeout = 100;

	private String ack;

	private boolean waitForSilence;

	private int maxRetryCount = 1;

	private boolean allowDuplicates;

	private int sentCount;

	/**
	 * Create new command.
	 * 
	 * @param command
	 *            with CR.
	 */
	public NikobusCommand(String command) {
		this.command = command.toUpperCase();
	}

	/**
	 * Create new command.
	 * 
	 * @param command
	 *            with CR.
	 * @param ack
	 *            expected ACK to be received after sending command
	 * @param timeout
	 *            in milliseconds to wait for ACK to be received
	 */
	public NikobusCommand(String command, String ack, long timeout) {
		this.command = command;
		this.ack = ack;
		this.timeout = timeout;
	}

	/**
	 * Create new command.
	 * 
	 * @param command
	 *            with CR.
	 * @param repeats
	 *            number of times a command is to be repeated to mimic long
	 *            button presses
	 */
	public NikobusCommand(String command, int repeats) {
		this.command = command;
		this.repeats = repeats;
	}

	/**
	 * Increment the number of times this command was repeated.
	 */
	public void incrementCount() {
		repeats++;
	}

	/**
	 * @return command string with CR
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return number of times command was or should be repeated
	 */
	public int getRepeats() {
		return repeats;
	}

	/**
	 * @return ACK to wait for or null if no ACK is expected
	 */
	public String getAck() {
		return ack;
	}

	/**
	 * @return timeout in milliseconds to wait for ACK before throwing a
	 *         TimeoutException.
	 */
	public long getTimeout() {
		return timeout;
	}

	@Override
	public String toString() {
		return "NikobusCommand [command=" + command + ", repeats=" + repeats
				+ "]";
	}

	/**
	 * @return true if the command represents a button press
	 */
	public boolean isButtonPress() {
		return command != null && command.startsWith("#");
	}

	/**
	 * Set the nikobus command string without CR
	 * 
	 * @param command
	 *            string
	 */
	public void setCommand(String commandString) {
		command = commandString.toUpperCase();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof NikobusCommand)) {
			return false;
		}
		return command.equals(((NikobusCommand) obj).getCommand());
	}

	@Override
	public int hashCode() {
		return command.hashCode();
	}

	/**
	 * When true, the command sender will wait until there is no signal on the
	 * bus before sending the command.
	 * 
	 * @param wait
	 *            true if we should wait.
	 */
	public void setWaitForSilence(boolean wait) {
		waitForSilence = wait;
	}

	/**
	 * @return true if bus should be clear before command is sent.
	 */
	public boolean getWaitForSilence() {
		return waitForSilence;
	}

	/**
	 * @return number of times the command may be retransmitted if no ACK was
	 *         received within the specified timeout.
	 */
	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	/**
	 * @param maxRetryCount
	 *            number of times the command may be retransmitted if no ACK was
	 *            received within the specified timeout.
	 */
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	/**
	 * @return true when the command can be sent multiple times to the nikobus
	 */
	public boolean getAllowDuplicates() {
		return allowDuplicates;
	}

	/**
	 * When no duplicates are allowed, the same command can not be queued for
	 * sending when a similar command is already in the to send queue.
	 * 
	 * @param allowDuplicates
	 *            true to allow.
	 */
	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	/**
	 * @return number of times the message was sent.
	 */
	public int getSentCount() {
		return sentCount;
	}
	
	public void incrementSentCount() {
		sentCount++;
	}
	
}
