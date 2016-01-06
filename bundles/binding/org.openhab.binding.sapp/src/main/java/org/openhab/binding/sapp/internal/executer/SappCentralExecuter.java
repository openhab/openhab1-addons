/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.executer;

import java.io.IOException;
import java.util.Map;

import com.github.paolodenti.jsapp.core.command.Sapp74Command;
import com.github.paolodenti.jsapp.core.command.Sapp75Command;
import com.github.paolodenti.jsapp.core.command.Sapp7CCommand;
import com.github.paolodenti.jsapp.core.command.Sapp7DCommand;
import com.github.paolodenti.jsapp.core.command.Sapp80Command;
import com.github.paolodenti.jsapp.core.command.Sapp81Command;
import com.github.paolodenti.jsapp.core.command.Sapp82Command;
import com.github.paolodenti.jsapp.core.command.base.SappCommand;
import com.github.paolodenti.jsapp.core.command.base.SappConnection;
import com.github.paolodenti.jsapp.core.command.base.SappException;

/**
 * Centralized picnet command execution queue executions on pnmas are synchronized on a single serial queue
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappCentralExecuter {

	private static SappCentralExecuter sappCentralExecuterInstance;

	private SappCentralExecuter() {
	}

	/**
	 * singleton instance
	 */
	public static SappCentralExecuter getInstance() {

		if (sappCentralExecuterInstance == null) {
			sappCentralExecuterInstance = new SappCentralExecuter();
		}

		return sappCentralExecuterInstance;
	}

	/**
	 * runs polling on virtuals, inputs, outputs
	 */
	public PollingResult executePollingSappCommands(String ip, int port) throws SappException {

		synchronized (this) {

			PollingResult pollingResult = new PollingResult();

			SappConnection sappConnection = null;
			try {
				sappConnection = new SappConnection(ip, port);

				sappConnection.openConnection();

				SappCommand sappCommand;

				sappCommand = new Sapp80Command();
				sappCommand.run(sappConnection);
				if (!sappCommand.isResponseOk()) {
					throw new SappException("Sapp80Command command execution failed");
				}
				pollingResult.changedOutputs = sappCommand.getResponse().getDataAsByteWordMap();

				sappCommand = new Sapp81Command();
				sappCommand.run(sappConnection);
				if (!sappCommand.isResponseOk()) {
					throw new SappException("Sapp81Command command execution failed");
				}
				pollingResult.changedInputs = sappCommand.getResponse().getDataAsByteWordMap();

				sappCommand = new Sapp82Command();
				sappCommand.run(sappConnection);
				if (!sappCommand.isResponseOk()) {
					throw new SappException("Sapp82Command command execution failed");
				}
				pollingResult.changedVirtuals = sappCommand.getResponse().getDataAsWordWordMap();

				return pollingResult;
			} catch (IOException e) {
				throw new SappException(e.getMessage());
			} finally {
				if (sappConnection != null) {
					sappConnection.closeConnection();
				}
			}
		}
	}

	/**
	 * runs 7C command
	 */
	public int executeSapp7CCommand(String ip, int port, int address) throws SappException {

		synchronized (this) {
			SappCommand sappCommand = new Sapp7CCommand(address);
			sappCommand.run(ip, port);
			if (!sappCommand.isResponseOk()) {
				throw new SappException("command execution failed");
			}
			return sappCommand.getResponse().getDataAsWord();
		}
	}

	/**
	 * runs 74 command
	 */
	public int executeSapp74Command(String ip, int port, byte address) throws SappException {

		synchronized (this) {
			SappCommand sappCommand = new Sapp74Command(address);
			sappCommand.run(ip, port);
			if (!sappCommand.isResponseOk()) {
				throw new SappException("command execution failed");
			}
			return sappCommand.getResponse().getDataAsWord();
		}
	}

	/**
	 * runs 75 command
	 */
	public int executeSapp75Command(String ip, int port, byte address) throws SappException {

		synchronized (this) {
			SappCommand sappCommand = new Sapp75Command(address);
			sappCommand.run(ip, port);
			if (!sappCommand.isResponseOk()) {
				throw new SappException("command execution failed");
			}
			return sappCommand.getResponse().getDataAsWord();
		}
	}

	/**
	 * runs 7D command
	 */
	public void executeSapp7DCommand(String ip, int port, int address, int value) throws SappException {

		synchronized (this) {
			SappCommand sappCommand = new Sapp7DCommand(address, value);
			sappCommand.run(ip, port);
			if (!sappCommand.isResponseOk()) {
				throw new SappException("command execution failed");
			}
		}
	}

	/**
	 * model for polling results
	 */
	public class PollingResult {

		public Map<Byte, Integer> changedOutputs;
		public Map<Byte, Integer> changedInputs;
		public Map<Integer, Integer> changedVirtuals;
	}
}
