package org.openhab.binding.sapp.internal;

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

public class SappCentralExecuter {

	private static SappCentralExecuter sappCentralExecuterInstance;

	private SappCentralExecuter() {
	}

	public static SappCentralExecuter getInstance() {

		if (sappCentralExecuterInstance == null) {
			sappCentralExecuterInstance = new SappCentralExecuter();
		}

		return sappCentralExecuterInstance;
	}

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

	public void executeSapp7DCommand(String ip, int port, int address, int value) throws SappException {

		synchronized (this) {
			SappCommand sappCommand = new Sapp7DCommand(address, value);
			sappCommand.run(ip, port);
			if (!sappCommand.isResponseOk()) {
				throw new SappException("command execution failed");
			}
		}
	}

	public class PollingResult {

		public Map<Byte, Integer> changedOutputs;
		public Map<Byte, Integer> changedInputs;
		public Map<Integer, Integer> changedVirtuals;
	}
}
