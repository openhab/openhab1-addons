/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.binding.maxcube.internal.command.CubeCommand;
import org.openhab.binding.maxcube.internal.command.L_Command;
import org.openhab.binding.maxcube.internal.command.S_Command;
import org.openhab.binding.maxcube.internal.exceptions.IncompleteMessageException;
import org.openhab.binding.maxcube.internal.exceptions.IncorrectMultilineIndexException;
import org.openhab.binding.maxcube.internal.exceptions.MessageIsWaitingException;
import org.openhab.binding.maxcube.internal.exceptions.NoMessageAvailableException;
import org.openhab.binding.maxcube.internal.exceptions.UnprocessableMessageException;
import org.openhab.binding.maxcube.internal.exceptions.UnsupportedMessageTypeException;
import org.openhab.binding.maxcube.internal.message.C_Message;
import org.openhab.binding.maxcube.internal.message.Configuration;
import org.openhab.binding.maxcube.internal.message.Device;
import org.openhab.binding.maxcube.internal.message.DeviceInformation;
import org.openhab.binding.maxcube.internal.message.HeatingThermostat;
import org.openhab.binding.maxcube.internal.message.L_Message;
import org.openhab.binding.maxcube.internal.message.M_Message;
import org.openhab.binding.maxcube.internal.message.Message;
import org.openhab.binding.maxcube.internal.message.MessageProcessor;
import org.openhab.binding.maxcube.internal.message.MessageType;
import org.openhab.binding.maxcube.internal.message.S_Message;
import org.openhab.binding.maxcube.internal.message.ShutterContact;
import org.openhab.binding.maxcube.internal.message.ThermostatModeType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RefreshService polls the MAX!Cube frequently and updates the list of
 * configurations and devices. The refresh interval can be changed via
 * openhab.cfg.
 *
 * Note that the MAX Cube has a lock out that only allows a maximum of 36s of
 * transmissions (1%) in total in 1 hour. This means that if too many S messages
 * are sent then the cube no longer sends the data out.
 *
 * @author Andreas Heil (info@aheil.de)
 * @author Marcel Verpaalen
 * @author Bernd Michael Helm (bernd.helm at helmundwalter.de)
 *
 * @since 1.4.0
 */
public class MaxCubeBinding extends AbstractActiveBinding<MaxCubeBindingProvider>implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(MaxCubeBinding.class);

    /** The IP address of the MAX!Cube LAN gateway */
    private static String ipAddress;

    /**
     * The port of the MAX!Cube LAN gateway as provided at
     * http://www.elv.de/controller.aspx?cid=824&detail=10&detail2=3484
     */
    private static int port = 62910;

    /**
     * Duty cycle of the cube
     */
    private int dutyCycle = 0;

    /**
     * The available memory slots of the cube
     */
    private int freeMemorySlots;

    /** The refresh interval which is used to poll given MAX!Cube */
    private static long refreshInterval = 10000;

    /**
     * If set to true, the binding will leave the connection to the cube
     * open and just request new informations.
     * This allows much higher poll rates and causes less load than the
     * non-exclusive polling but has the drawback that no other apps
     * (i.E. original software) can use the cube while this binding is
     * running.
     */
    private static boolean exclusive = true;

    /**
     * in exclusive mode, how many requests are allowed until connection is closed and reopened
     */
    private static int maxRequestsPerConnection = 1000;

    private int requestCount = 0;

    /** MaxCube's default off temperature */
    private static final DecimalType DEFAULT_OFF_TEMPERATURE = new DecimalType(4.5);

    /** MaxCubes default on temperature */
    private static final DecimalType DEFAULT_ON_TEMPERATURE = new DecimalType(30.5);

    /** timeout on network connection **/
    private static final int NETWORK_TIMEOUT = 10000;

    /**
     * Configuration and device lists, kept during the overall lifetime of the
     * binding
     */
    private List<Configuration> configurations = new ArrayList<Configuration>();
    private List<Device> devices = new ArrayList<Device>();

    /**
     * connection socket and reader/writer for execute method
     */
    private Socket socket = null;
    private BufferedReader reader = null;
    private OutputStreamWriter writer = null;

    /** maximum queue size that we're allowing */
    private static final int MAX_COMMANDS = 50;
    private ArrayBlockingQueue<SendCommand> commandQueue = new ArrayBlockingQueue<SendCommand>(MAX_COMMANDS);

    private SendCommand lastCommandId = null;

    /**
     * Processor that handles lines received from MAX!Cube
     */
    MessageProcessor messageProcessor = new MessageProcessor();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "MAX!Cube Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        super.activate();
        setProperlyConfigured(false);
    }

    @Override
    public void deactivate() {
        socketClose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void execute() {
        if (ipAddress == null) {
            logger.debug("Update prior to completion of interface IP configuration");
            return;
        }

        if (exclusive == true && commandQueue.isEmpty()) {
            if (sendCubeCommand(new L_Command())) {
                processUpdates();
            }
        } else {
            if (commandQueue.isEmpty()) {
                if (sendCubeCommand(new L_Command())) {
                    processUpdates();
                }
            }
            while (sendCommands() != 0) {
                // send commands in queue
                processUpdates();
            }
        }

    }

    /**
     *
     */
    private void processUpdates() {
        for (MaxCubeBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                String serialNumber = provider.getSerialNumber(itemName);

                Device device = findDevice(serialNumber, devices);

                if (device == null) {
                    logger.info("Cannot find MAX!cube device with serial number '{}'", serialNumber);
                    logAvailableMaxDevices();
                    continue;
                }
                // all devices have a battery state, so this is type-independent
                if (provider.getBindingType(itemName) == BindingType.BATTERY) {
                    if (device.battery().isChargeUpdated()) {
                        eventPublisher.postUpdate(itemName, device.battery().getCharge());
                    }
                } else if (provider.getBindingType(itemName) == BindingType.CONNECTION_ERROR) {
                    if (device.isErrorUpdated()) {
                        OnOffType connectionError = device.isError() ? OnOffType.ON : OnOffType.OFF;
                        eventPublisher.postUpdate(itemName, connectionError);
                    }
                } else {
                    switch (device.getType()) {
                        case HeatingThermostatPlus:
                        case HeatingThermostat:
                            if (provider.getBindingType(itemName) == BindingType.VALVE
                                    && ((HeatingThermostat) device).isValvePositionUpdated()) {
                                eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getValvePosition());
                                break;
                            }
                            // omitted break, fall through
                        case WallMountedThermostat: // and also HeatingThermostat
                            if (provider.getBindingType(itemName) == BindingType.MODE
                                    && ((HeatingThermostat) device).isModeUpdated()) {
                                eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getModeString());
                            } else if (provider.getBindingType(itemName) == BindingType.ACTUAL
                                    && ((HeatingThermostat) device).isTemperatureActualUpdated()) {
                                eventPublisher.postUpdate(itemName,
                                        ((HeatingThermostat) device).getTemperatureActual());
                            } else if (((HeatingThermostat) device).isTemperatureSetpointUpdated()
                                    && provider.getBindingType(itemName) == null) {
                                eventPublisher.postUpdate(itemName,
                                        ((HeatingThermostat) device).getTemperatureSetpoint());
                            }
                            break;
                        case ShutterContact:
                            if (((ShutterContact) device).isShutterStateUpdated()) {
                                eventPublisher.postUpdate(itemName, ((ShutterContact) device).getShutterState());
                            }
                            break;
                        default:
                            // no further devices supported yet
                    }
                }
            }
        }
    }

    private void logAvailableMaxDevices() {
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Available MAX! devices are:");
            for (Device d : devices) {
                sb.append("\n\t");
                sb.append(d.getSerialNumber());
            }
            logger.debug(sb.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalReceiveCommand(String itemName, Command command) {
        logger.debug("Received command from {}", itemName);

        // resolve serial number for item
        String serialNumber = null;

        for (MaxCubeBindingProvider provider : providers) {
            serialNumber = provider.getSerialNumber(itemName);

            if (StringUtils.isBlank(serialNumber)) {
                continue;
            }

            // send command to MAX!Cube LAN Gateway
            Device device = findDevice(serialNumber, devices);

            if (device == null) {
                logger.debug("Cannot send command to device with serial number {}, device not listed.", serialNumber);
                continue;
            }

            String rfAddress = device.getRFAddress();
            S_Command cmd = null;
            SendCommand sendcommand = null;

            if (command instanceof DecimalType || command instanceof OnOffType) {
                DecimalType decimalType = DEFAULT_OFF_TEMPERATURE;
                if (command instanceof DecimalType) {
                    decimalType = (DecimalType) command;
                } else if (command instanceof OnOffType) {
                    decimalType = OnOffType.ON.equals(command) ? DEFAULT_ON_TEMPERATURE : DEFAULT_OFF_TEMPERATURE;
                }
                cmd = new S_Command(rfAddress, device.getRoomId(), ((HeatingThermostat) device).getMode(),
                        decimalType.doubleValue());
                sendcommand = new SendCommand(serialNumber, cmd, "SetTemp", decimalType.toString());
            } else if (command instanceof StringType) {
                String commandContent = command.toString().trim().toUpperCase();
                ThermostatModeType commandThermoType = null;
                if (commandContent.contentEquals(ThermostatModeType.AUTOMATIC.toString())) {
                    commandThermoType = ThermostatModeType.AUTOMATIC;
                    cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType, 0D);
                    sendcommand = new SendCommand(serialNumber, cmd, "SetMode", commandContent);
                } else if (commandContent.contentEquals(ThermostatModeType.BOOST.toString())) {
                    commandThermoType = ThermostatModeType.BOOST;
                    Double setTemp = Double
                            .parseDouble(((HeatingThermostat) device).getTemperatureSetpoint().toString());
                    cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType, setTemp);
                    sendcommand = new SendCommand(serialNumber, cmd, "SetMode", commandContent);
                } else if (commandContent.contentEquals(ThermostatModeType.MANUAL.toString())) {
                    commandThermoType = ThermostatModeType.MANUAL;
                    Double setTemp = Double
                            .parseDouble(((HeatingThermostat) device).getTemperatureSetpoint().toString());
                    cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType, setTemp);
                    logger.debug("updates to MANUAL mode with temperature '{}'", setTemp);
                    sendcommand = new SendCommand(serialNumber, cmd, "SetMode", commandContent);
                } else {
                    logger.debug("Only updates to AUTOMATIC, MANUAL & BOOST supported, received value ;'{}'",
                            commandContent);
                    continue;
                }
            }
            if (cmd != null) {
                queueCommand(sendcommand);
            } else {
                logger.debug("Null Command cannot be send");
            }
        }
    }

    private boolean socketConnect() throws UnknownHostException, IOException {
        socket = new Socket(ipAddress, port);
        socket.setSoTimeout(NETWORK_TIMEOUT);
        logger.debug("open new connection... to " + ipAddress + " port " + port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());
        requestCount = 0;
        return true;
    }

    private void socketClose() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
            socket = null;
        }
    }

    private Device findDevice(String serialNumber, List<Device> devices) {
        for (Device device : devices) {
            if (device.getSerialNumber().toUpperCase().equals(serialNumber)) {
                return device;
            }
        }
        return null;
    }

    protected void addBindingProvider(MaxCubeBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(MaxCubeBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        if (config != null) {
            ipAddress = (String) config.get("ip");
            if (StringUtils.isBlank(ipAddress)) {
                ipAddress = discoveryGatewayIp();
            }

            String portString = (String) config.get("port");
            if (portString != null && !portString.isEmpty()) {
                if (port > 0 && port <= 65535) {
                    port = Integer.parseInt(portString);
                }
            }

            String refreshIntervalString = (String) config.get("refreshInterval");
            if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            String exclusiveString = (String) config.get("exclusive");
            if (StringUtils.isNotBlank(exclusiveString)) {
                exclusive = Boolean.parseBoolean(exclusiveString);
            }

            String maxRequestsPerConnectionString = (String) config.get("maxRequestsPerConnection");
            if (maxRequestsPerConnectionString != null && !maxRequestsPerConnectionString.isEmpty()) {
                maxRequestsPerConnection = Integer.parseInt(maxRequestsPerConnectionString);
            }
        } else {
            ipAddress = discoveryGatewayIp();
        }

        setProperlyConfigured(ipAddress != null);
    }

    /**
     * Discovers the MAX!CUbe LAN Gateway IP address.
     *
     * @return the cube IP if available, a blank string otherwise.
     * @throws ConfigurationException
     */
    private String discoveryGatewayIp() throws ConfigurationException {
        String ip = MaxCubeDiscover.discoverIp();
        if (ip == null) {
            throw new ConfigurationException("maxcube:ip", "IP address for MAX!Cube must be set");
        } else {
            logger.info("Discovered MAX!Cube lan gateway at '{}'", ip);
        }
        return ip;
    }

    /**
     * Connects to the Max! Cube Lan gateway and send a command to Cube
     * and process the message
     *
     * @param {@link CubeCommand}
     * @return boolean success
     */
    private synchronized boolean sendCubeCommand(CubeCommand command) {
        synchronized (MaxCubeBinding.class) {
            boolean sendSuccess = false;
            try {
                if (socket == null || socket.isClosed()) {
                    this.socketConnect();
                } else {
                    if (maxRequestsPerConnection > 0 && requestCount >= maxRequestsPerConnection) {
                        logger.debug("maxRequestsPerConnection reached, reconnecting.");
                        socket.close();
                        this.socketConnect();
                    }
                }
                if (requestCount == 0) {
                    logger.debug("Connect to MAX! Cube");
                    readliness("L:");

                }
                if (!(requestCount == 0 && command instanceof L_Command)) {

                    logger.debug("Sending request #{} to MAX! Cube", this.requestCount);
                    if (writer == null) {
                        logger.warn("Can't write to MAX! Cube");
                        this.socketConnect();
                    }

                    writer.write(command.getCommandString());
                    logger.trace("Write string to Max! Cube {}: {}", ipAddress, command.getCommandString());
                    writer.flush();
                    if (command.getReturnStrings() != null) {
                        readliness(command.getReturnStrings());
                    } else {
                        socketClose();
                    }
                }

                requestCount++;
                sendSuccess = true;

                if (!exclusive) {
                    socketClose();
                }
            } catch (ConnectException e) {
                logger.debug("Connection timed out on {} port {}", ipAddress, port);
                sendSuccess = false;
                socketClose(); // reconnect on next execution
            } catch (UnknownHostException e) {
                logger.debug("Host error occurred during execution: {}", e.getMessage());
                sendSuccess = false;
                socketClose(); // reconnect on next execution
            } catch (IOException e) {
                logger.debug("IO error occurred during execution: {}", e.getMessage());
                sendSuccess = false;
                socketClose(); // reconnect on next execution
            } catch (Exception e) {
                logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                sendSuccess = false;
                socketClose(); // reconnect on next execution
            }
            return sendSuccess;
        }

    }

    /**
     * Read line from the Cube and process the message.
     *
     * @param terminator String with ending messagetype e.g. L:
     * @throws IOException
     */
    private void readliness(String terminator) throws IOException {
        if (terminator == null) {
            return;
        }
        boolean cont = true;
        while (cont) {
            String raw = reader.readLine();
            if (raw != null) {
                logger.trace("message block: '{}'", raw);
                try {
                    this.messageProcessor.addReceivedLine(raw);
                    if (this.messageProcessor.isMessageAvailable()) {
                        Message message = this.messageProcessor.pull();
                        processMessage(message);

                    }
                } catch (IncorrectMultilineIndexException ex) {
                    logger.info(
                            "Incorrect MAX!Cube multiline message detected. Stopping processing and continue with next Line.");
                    this.messageProcessor.reset();
                } catch (NoMessageAvailableException ex) {
                    logger.info("Could not process MAX!Cube message. Stopping processing and continue with next Line.");
                    this.messageProcessor.reset();
                } catch (IncompleteMessageException ex) {
                    logger.info(
                            "Error while parsing MAX!Cube multiline message. Stopping processing, and continue with next Line.");
                    this.messageProcessor.reset();
                } catch (UnsupportedMessageTypeException ex) {
                    logger.info("Unsupported MAX!Cube message detected. Ignoring and continue with next Line.");
                    this.messageProcessor.reset();
                } catch (MessageIsWaitingException ex) {
                    logger.info("There was an unhandled message waiting. Ignoring and continue with next Line.");
                    this.messageProcessor.reset();
                } catch (UnprocessableMessageException e) {
                    if (raw.contentEquals("M:")) {
                        logger.info("No Rooms information found. Configure your MAX! Cube: {}", ipAddress);
                        this.messageProcessor.reset();
                    } else {
                        logger.info("Message could not be processed: '{}' from MAX! Cube lan gateway: {}:", raw,
                                ipAddress);
                        this.messageProcessor.reset();
                    }
                } catch (Exception e) {
                    logger.info("Error while handling message block: '{}' from MAX! Cube lan gateway: {}:", raw,
                            ipAddress, e.getMessage(), e);
                    this.messageProcessor.reset();
                }
                if (terminator == null || raw.startsWith(terminator)) {
                    cont = false;
                }
            } else {
                cont = false;
            }
        }
    }

    /**
     * Processes the message
     *
     * @param Message
     *            the decoded message data
     */
    private void processMessage(Message message) {

        if (message != null) {
            message.debug(logger);

            if (message.getType() == MessageType.M) {
                M_Message msg = (M_Message) message;
                for (DeviceInformation di : msg.devices) {
                    Configuration c = null;
                    for (Configuration conf : configurations) {
                        if (conf.getSerialNumber().equalsIgnoreCase(di.getSerialNumber())) {
                            c = conf;
                            break;
                        }
                    }

                    if (c != null) {
                        configurations.remove(c);
                    }

                    c = Configuration.create(di);
                    configurations.add(c);

                    c.setRoomId(di.getRoomId());
                }
            } else if (message.getType() == MessageType.C) {
                Configuration c = null;
                for (Configuration conf : configurations) {
                    if (conf.getSerialNumber().equalsIgnoreCase(((C_Message) message).getSerialNumber())) {
                        c = conf;
                        break;
                    }
                }

                if (c == null) {
                    configurations.add(Configuration.create(message));
                } else {
                    c.setValues((C_Message) message);
                }

            } else if (message.getType() == MessageType.L) {
                ((L_Message) message).updateDevices(devices, configurations);
                logger.trace("{} devices found.", devices.size());
            } else if (message.getType() == MessageType.S) {
                dutyCycle = ((S_Message) message).getDutyCycle();
                freeMemorySlots = ((S_Message) message).getFreeMemorySlots();
                if (((S_Message) message).isCommandDiscarded()) {
                    logger.info("Last Send Command discarded. Duty Cycle: {}, Free Memory Slots: {}", dutyCycle,
                            freeMemorySlots);
                } else {
                    logger.debug("S message. Duty Cycle: {}, Free Memory Slots: {}", dutyCycle, freeMemorySlots);
                }
            }
        }
    }

    /**
     * Takes a command from the command queue and send it to
     * {@link executeCommand} for execution.
     *
     * @return # of remaining elements
     *
     */
    private synchronized int sendCommands() {

        SendCommand sendCommand = commandQueue.poll();
        if (sendCommand != null) {
            CubeCommand cmd = sendCommand.getCubeCommand();
            if (cmd == null) {
                // cmd = getCommand(sendCommand);
            }
            if (cmd != null) {
                // Actual sending of the data to the Max! Cube Lan Gateway
                logger.debug("Command {} ({}:{}) sent to MAX! Cube at IP: {}", sendCommand.getId(),
                        sendCommand.getKey(), sendCommand.getCommandText(), ipAddress);

                if (sendCubeCommand(cmd)) {
                    logger.trace("Command {} ({}:{}) completed for MAX! Cube at IP: {}", sendCommand.getId(),
                            sendCommand.getKey(), sendCommand.getCommandText(), ipAddress);
                } else {
                    logger.warn("Error sending command {} ({}:{}) to MAX! Cube at IP: {}", sendCommand.getId(),
                            sendCommand.getKey(), sendCommand.getCommandText(), ipAddress);
                }
            }
        }
        return commandQueue.size();
    }

    /**
     * Takes the device command and puts it on the command queue to be processed
     * by the MAX! Cube Lan Gateway. Note that if multiple commands for the same
     * item-channel combination are send prior that they are processed by the
     * Max! Cube, they will be removed from the queue as they would not be
     * meaningful. This will improve the behavior when using sliders in the GUI.
     *
     * @param SendCommand
     *            the SendCommand containing the serial number of the device as
     *            String the channelUID used to send the command and the the
     *            command data
     */
    public synchronized void queueCommand(SendCommand sendCommand) {

        if (commandQueue.offer(sendCommand)) {
            if (lastCommandId != null) {
                if (lastCommandId.getKey().equals(sendCommand.getKey())) {
                    if (commandQueue.remove(lastCommandId)) {
                        logger.debug("Removed Command id {} ({}) from queue. Superceeded by {}", lastCommandId.getId(),
                                lastCommandId.getKey(), sendCommand.getId());
                    }
                }
            }
            lastCommandId = sendCommand;
            logger.debug("Command queued id {} ({}:{}).", sendCommand.getId(), sendCommand.getKey(),
                    sendCommand.getCommandText());

        } else {
            logger.debug("Command queued full dropping command id {} ({}).", sendCommand.getId(), sendCommand.getKey());
        }

    }

}
