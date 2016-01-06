package org.openhab.binding.ipx800.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.ipx800.internal.Ipx800Config.Ipx800DeviceConfig;
import org.openhab.binding.ipx800.internal.command.Ipx800Port;
import org.openhab.binding.ipx800.internal.command.Ipx800PortType;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800OutputItem;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800DeviceConnector extends Thread {
	private static final Logger logger = 
			LoggerFactory.getLogger(Ipx800DeviceConnector.class);
	/** End line separator */
	private final static String ENDL = "\r\n";
	/** Time before reconnecting in case of failure */
	private static final int reconnectTimeout = 5000;
	/** */
	private static final int sendTimeout = 1000;
	/** Time before sending a keepalive to device */
	private static final int keepaliveTimeout = 30000;
	/** Max keep alive failure before reconnecting */
	private static final int maxKeepAliveFailure = 1;
	/** The configuration */
	private Ipx800DeviceConfig config;
	/** Interruption indicator for listening thread. */
	private boolean interrupted = false;
	/** Interruption indicator for listening thread. */
	private boolean connected = false;
	/** Client socket */
	private Socket client;
	/** The reader */
	private BufferedReader in;
	/** The writer */
	private PrintWriter out;
	/** All the response */
	private static enum ResponseType { NONE, OK, GET_INPUT, GET_INPUTS, GET_OUTPUTS };
	/** Response expected */
	private ResponseType expectedResponse = ResponseType.NONE;
	/** */
	private String globalResponse = null;
	/** List of ipx800 ports */
	private Vector<Ipx800Port> portList;
	/** Failed keepalive count */
	private int failedKeepalive = 0;
	/** Waiting for keepalive response */
	private boolean waitingKeepaliveResponse = false;
	
	
	/**
	 * 
	 * @param config
	 */
	public Ipx800DeviceConnector(Ipx800DeviceConfig config) {
		this.config = config;
		createPorts();
		logger.debug("Initialisation of Ipx800 device {}", this);
	}
	
	@Override
	public String toString() {
		String descr = this.config.name + "@" + this.config.host;
		int i = 0;
		for (String extName : this.config.x880extensions) {
			i++;
			if (extName != null) {
				descr += " + " + extName + "@x880." + i;
			}
		}
		i = 0;
		for (String extName : this.config.x400extensions) {
			i++;
			if (extName != null) {
				descr += " + " + extName + "@x400." + i;
			}
		}
		return descr;
	}
	
	/**
	 * Create the ports regarding to the configuration
	 */
	private void createPorts() {
		int inputs = Ipx800PortType.INPUT.getPortPerDevice() * (1 + this.config.getX880length());
		int analogs = Ipx800PortType.ANALOG.getPortPerDevice() * (1 +this.config.getX400length());
		int counters = Ipx800PortType.COUNTER.getPortPerDevice();
		portList = new Vector<Ipx800Port>();
		
		for (int i = 0; i < inputs; i++) {
			portList.add(new Ipx800Port(Ipx800PortType.INPUT, i + 1, this));
			portList.add(new Ipx800Port(Ipx800PortType.OUPUT, i + 1, this));
		}
		for (int i = 0; i < analogs; i++) {
			portList.add(new Ipx800Port(Ipx800PortType.ANALOG, i + 1, this));
		}
		for (int i = 0; i < counters; i++) {
			portList.add(new Ipx800Port(Ipx800PortType.COUNTER, i + 1, this));
		}
	}
	
	/**
	 * Retrieve the port with the type and the portNumber
	 * @param type
	 * @param portNumber
	 * @return the ipx port or null if not found
	 */
	public Ipx800Port getPort(Ipx800PortType type, int portNumber) {
		for (Ipx800Port port : portList) {
			if (port.getCommandType() == type && port.getPortNumber() == portNumber) {
				return port;
			}
		}
		return null;
	}
	
	/**
	 * Retrieve the port using a config string
	 * @param configPortString using following format : I01
	 * @return the ipx port or null if not found
	 */
	public Ipx800Port getPort(String configPortString, int extensionDelta) {
		assert(configPortString.length() == 3);
		String prefix = configPortString.substring(0, 1);
		int portNumber = Integer.parseInt(configPortString.substring(1));
		Ipx800PortType slotType = Ipx800PortType.getSlotByPrefix(prefix);
		return getPort(slotType, portNumber + extensionDelta);
	}
	/**
	 * 
	 * @param configPortString
	 * @return
	 */
	public Ipx800Port getPort(String configPortString) {
		return getPort(configPortString, 0);
	}
	
	/**
	 * Return all ports
	 * @return
	 */
	public Vector<Ipx800Port> getAllPorts() {
		return portList;
	}
	
	/**
	 * Get the port number delta (first extension is 8)
	 * @param extensionName
	 * @return the port number delta or 0 if not found
	 */
	public int getExtensionDelta(String extensionName) {
		for(int i = 0; i < config.getX400length(); i++) {
			String extName = config.x400extensions[i];
			if (extensionName.equals(extName)) {
				return Ipx800PortType.ANALOG.getPortPerDevice() * (i + 1);
			}
		}
		for(int i = 0; i < config.getX880length(); i++) {
			String extName = config.x880extensions[i];
			if (extensionName.equals(extName)) {
				return Ipx800PortType.INPUT.getPortPerDevice() * (i + 1);
			}
		}
		return 0;
	}
	
	/**
	 * Set output of the device sending the command corresponding to the state to the device
	 * @param slot
	 * @param state
	 */
	public synchronized void setOutput(Ipx800Port slot, org.openhab.core.types.State state) {
		if (slot.getCommandType() == Ipx800PortType.OUPUT) {
			if (state != null) {
				logger.debug("Sending {} to {}", state, slot);
				out.format("Set%02d%d" + ENDL, slot.getPortNumber(), state == OnOffType.ON ? 1 : 0);
			}
		}
	}
	/**
	 * FIXME use only this method using items also for redirect
	 * @param slot
	 * @param item
	 */
	public synchronized void setOutput(Ipx800OutputItem item) {
		org.openhab.core.types.State state = item.getState();
		Ipx800Port port = item.getPort();
		if (item.isPulseMode()) {
			logger.debug("Sending {} to {} in pulse mode", state, port);
			out.format("Set%02d%dp" + ENDL, port.getPortNumber(), state == OnOffType.ON ? 1 : 0);
		} else {
			logger.debug("Sending {} to {}", state, port);
			out.format("Set%02d%d" + ENDL, port.getPortNumber(), state == OnOffType.ON ? 1 : 0);
		}
	}
	
	/**
	 * Wait for a response of the ipx800
	 * @return
	 */
	private synchronized String waitResponse() {
		String resp;
		try {
			logger.debug("Will wait");
			wait(sendTimeout);
		} catch (InterruptedException e) {
		}
		
		resp = globalResponse;
		if (globalResponse == null) {
			logger.debug("Cannot receive response");
			resp = "";
		}
		globalResponse = null;
		return resp;
	}
	
	/**
	 * This should be called from the reception loop to send response to mainthread.
	 * Disabled for now. Not really useful
	 * @param command
	 */
	private synchronized void sendResponse(String command) {
		globalResponse = command;
		notify();
	}
	/**
	 * 
	 * @param data
	 * @param slotType
	 */
	public void onBitUpdate(String data, Ipx800PortType slotType) {
		onBitUpdate(data, slotType, -1);
	}
	
	/**
	 * 
	 * @param data
	 * @param slotType
	 * @param slotNumber
	 */
	public void onBitUpdate(String data, Ipx800PortType slotType, int slotNumber) {
		logger.trace("onBitUpdate with data='{}' for type '{}'...", data, slotType.name());
		if (slotType == Ipx800PortType.INPUT || slotType == Ipx800PortType.OUPUT) {
			if (data.length() != slotType.getMaxSlots()) {
				logger.error("Received data doesn't match expected size");
				return;
			}
		}
		if (slotNumber >= 0) {
			logger.trace("... for slot '{}'", slotNumber);
			postUpdate(data, slotType, slotNumber);
		} else {
			for (int i = 0; i < data.length(); i++) {
				postUpdate(data.substring(i), slotType, i + 1);
			}
		}
	}

	/**
	 * 
	 * @param data
	 * @param slotType
	 * @param slotNumber
	 */
	private void postUpdate(String data, Ipx800PortType slotType, int slotNumber) {
		Ipx800Port slot = getPort(slotType, slotNumber);
		if (slot != null) {
			slot.updateStateIfChanged(data);
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	private void unsollicitedUpdate(String data) {
		// Example of 
		// I=00000000000000000000000000000000&O=10000000000000000000000000000000&\
		// A0=0&A1=0&A2=0&A3=0&A4=0&A5=0&A6=0&A7=0&A8=0&A9=0&A10=0&A11=0&A12=0&A13=0&A14=0&A15=0&C1=47&C2=0&C3=0&C4=0&C5=0&C6=0&C7=0&C8=0
		final Pattern VALIDATION_PATTERN = Pattern.compile("I=(\\d{32})&O=(\\d{32})&([AC]\\d{1,2}=\\d+&)*[^I]*");
		final Matcher matcher = VALIDATION_PATTERN.matcher(data);
		while(matcher.find()) { // Workaround of an IPX800 bug
			String completeCommand = matcher.group();
			logger.debug("Command : " + completeCommand);
			for (String command : completeCommand.split("&")) {
				int sepIndex = command.indexOf("=");
				if (sepIndex == -1) {
					continue;
				}
				String prefix = command.substring(0, sepIndex);
				Ipx800PortType slotType = Ipx800PortType.getSlotByPrefix(prefix.substring(0, 1));
				if (slotType == null) {
					logger.error("Not supported type for now '{}'", prefix);
					continue;
				}
				if (sepIndex == 1) {
					onBitUpdate(command.substring(sepIndex + 1), slotType);
				} else {
					onBitUpdate(command.substring(sepIndex + 1), slotType, Integer.parseInt(prefix.substring(1)));
				}
			}
		}
	}
	
	/**
	 * Unused for now
	 */
	@SuppressWarnings("unused")
	private void updateState() {
		// Update internal state
		expectedResponse = ResponseType.GET_OUTPUTS;
		out.print("GetOutputs" + ENDL);
		String resp = waitResponse();
		if (resp == null) {
			// throw exc
			return;
		}
		onBitUpdate(resp, Ipx800PortType.OUPUT);
	}
	
	/**
	 * Connect to the ipx800
	 * @throws IOException
	 */
	private void connect() throws IOException {
		disconnect();
		logger.debug("Connecting {}@ {}:{}...", config.name, config.host, config.port);
		client = new Socket(config.host, Integer.parseInt(config.port));
		client.setSoTimeout(keepaliveTimeout);
		client.getInputStream().skip(client.getInputStream().available());
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		connected = true;
		logger.debug("Connected to {}@ {}:{}", config.name, config.host, config.port);
	}
	
	/**
	 * Disconnect the device
	 */
	public void disconnect() {
		if (connected) {
			logger.debug("Disconnecting");
			try {
				client.close();
			} catch (IOException e) {
				logger.error("Unable to disconnect {}", e.getMessage());
			}
			connected = false;
			logger.debug("Disconnected");
		}
	}
	
	/**
	 * Stop the device thread
	 */
	public void destroyAndExit() {
		interrupted = true;
		disconnect();
		for(Ipx800Port port : portList) {
			port.destroy();
		}
	}
	/**
	 * Send an arbitrary keepalive command which cause the IPX to send an update.
	 * If we don't receive the update maxKeepAliveFailure time, the connection is closed and reopened
	 */
	private void sendKeepalive() {
		if (waitingKeepaliveResponse) {
			failedKeepalive++;
			logger.debug("Sending keepalive, attempt {}", failedKeepalive);
		} else {
			failedKeepalive = 0;
			logger.trace("Sending keepalive");
		}
		out.println("GetIn01");
		out.flush();
		waitingKeepaliveResponse = true;
	}
	
	@Override
	public void run() {
		interrupted = false;
		while(!interrupted) {
			try {
				waitingKeepaliveResponse = false;
				failedKeepalive = 0;
				connect();
				String command;
				while (!interrupted) {
					if (failedKeepalive > maxKeepAliveFailure) {
						throw new IOException("Max keep alive attempts has been reached");
					}
					try {
						command = in.readLine();
						if (command.equals("0") || command.equals("1")) {
							logger.trace("Keepalive response ok");
						} else {
							logger.debug("Receiving {}", command);
						}
						waitingKeepaliveResponse = false; // Reseting keepalive state each time we receive a command
						unsollicitedUpdate(command);
						expectedResponse = ResponseType.NONE;
					} catch (SocketTimeoutException e) {
						sendKeepalive();
					}
				}
				disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage() + " will retry in " + reconnectTimeout + "ms");
			}
			try {
				Thread.sleep(reconnectTimeout);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}	
}
