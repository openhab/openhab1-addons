/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.upb.internal;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.upb.UPBBindingProvider;
import org.openhab.binding.upb.internal.UPBMessage.Type;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Binding for Universal Powerline Bus (UPB) that reads and writes messages to
 * and from the UPB modem.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class UPBBinding extends AbstractActiveBinding<UPBBindingProvider> implements UPBReader.Listener {

    private static final Logger logger = LoggerFactory.getLogger(UPBBinding.class);

    private String port;
    private byte network = 0;
    private SerialPort serialPort;
    private UPBReader upbReader;
    private UPBWriter upbWriter;

    /**
     * the refresh interval which is used to poll values from the UPB server
     * (optional, defaults to 3600000ms)
     */
    private long refreshInterval = 3600000;

    /**
     * Called by the SCR to activate the component with its configuration read
     * from CAS
     *
     * @param bundleContext
     *            BundleContext of the Bundle that defines this component
     * @param configuration
     *            Configuration properties for this component obtained from the
     *            ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {

        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
        }

        parseConfiguration(configuration);

        logger.info("UPB binding starting up...");

        try {
            serialPort = openSerialPort();
            upbReader = new UPBReader(serialPort.getInputStream());
            upbWriter = new UPBWriter(serialPort.getOutputStream(), upbReader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open serial port.", e);
        }

        upbReader.addListener(this);

        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed
     * through the ConfigAdmin service.
     *
     * @param configuration
     *            Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
        parseConfiguration(configuration);
    }

    private void parseConfiguration(final Map<String, Object> configuration) {
        port = ObjectUtils.toString(configuration.get("port"), null);
        network = Integer.valueOf(ObjectUtils.toString(configuration.get("network"), "0")).byteValue();

        logger.debug("Parsed UPB configuration:");
        logger.debug("Serial port: {}", port);
        logger.debug("UPB Network: {}", network & 0xff);

    }

    /**
     * Called by the SCR to deactivate the component when either the
     * configuration is removed or mandatory references are no longer satisfied
     * or the component has simply been stopped.
     *
     * @param reason
     *            Reason code for the deactivation:<br>
     *            <ul>
     *            <li>0 – Unspecified
     *            <li>1 – The component was disabled
     *            <li>2 – A reference became unsatisfied
     *            <li>3 – A configuration was changed
     *            <li>4 – A configuration was deleted
     *            <li>5 – The component was disposed
     *            <li>6 – The bundle was stopped
     *            </ul>
     */
    public void deactivate(final int reason) {
        logger.info("UPB binding shutting down...");

        if (upbReader != null) {
            upbReader.shutdown();
        }

        if (upbWriter != null) {
            upbWriter.shutdown();
        }

        if (serialPort != null) {
            logger.debug("Closing serial port");
            serialPort.close();
        }
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
    protected String getName() {
        return "UPB Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        // the frequently executed code (polling) goes here ...
        for (UPBBindingProvider p : providers) {
            for (String s : p.getItemNames()) {
                UPBBindingConfig config = p.getConfig(s);
                if (!config.isLink()) {
                    MessageBuilder message = MessageBuilder.create().network(network).destination(config.getId())
                            .command(UPBMessage.Command.REPORT_STATE.toByte());
                    // Here we write the command to the PIM.
                    upbWriter.queueMessage(message);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        UPBBindingConfig config = getConfig(itemName);

        if (config != null) {
            byte[] commandByte = { UPBMessage.Command.DEACTIVATE.toByte() };
            if (command == OnOffType.ON) {
                commandByte = new byte[] { UPBMessage.Command.ACTIVATE.toByte() };
            } else if (command instanceof PercentType) {
                commandByte = new byte[] { UPBMessage.Command.GOTO.toByte(), ((PercentType) command).byteValue() };
            }

            MessageBuilder message = MessageBuilder.create().network(network).destination(config.getId())
                    .link(config.isLink()).command(commandByte);

            // Here we write the command to the PIM.
            upbWriter.queueMessage(message);
        }
    }

    private String getItemName(byte id, boolean link) {
        for (UPBBindingProvider p : providers) {
            for (String itemName : p.getItemNames()) {
                UPBBindingConfig config = p.getConfig(itemName);

                if (config != null && config.getId() == id && config.isLink() == link) {
                    return itemName;
                }
            }
        }

        return null;
    }

    private UPBBindingConfig getConfig(String itemName) {
        for (UPBBindingProvider p : providers) {
            UPBBindingConfig config = p.getConfig(itemName);

            if (config != null) {
                return config;
            }
        }

        return null;
    }

    private SerialPort openSerialPort() {
        SerialPort serialPort = null;
        CommPortIdentifier portId;
        try {
            portId = CommPortIdentifier.getPortIdentifier(port);
        } catch (NoSuchPortException e1) {
            throw new RuntimeException("Port does not exist", e1);
        }

        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(port)) {
                try {
                    serialPort = portId.open("UPB", 1000);
                } catch (PortInUseException e) {
                    throw new RuntimeException("Port is in use", e);
                }
                try {
                    serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                    serialPort.enableReceiveTimeout(100);
                } catch (UnsupportedCommOperationException e) {
                    throw new RuntimeException("Failed to configure serial port");
                }
            }
        }

        return serialPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(UPBMessage message) {
        if (message.getType() != Type.MESSAGE_REPORT) {
            return;
        }

        String sourceName = getItemName(message.getSource(), false);
        String destinationName = getItemName(message.getDestination(), message.getControlWord().isLink());
        UPBBindingConfig sourceConfig = getConfig(sourceName);
        UPBBindingConfig destinationConfig = getConfig(destinationName);

        String itemName = isValidId(message.getDestination()) ? destinationName : sourceName;
        UPBBindingConfig config = isValidId(message.getDestination()) ? destinationConfig : sourceConfig;

        if (itemName == null || config == null) {
            logger.debug("Received message for unknown {} with id {}.",
                    message.getControlWord().isLink() ? "Link" : "Device", message.getDestination() & 0xff);
            return;
        }

        State newState = null;
        byte level = 100;

        switch (message.getCommand()) {
            case GOTO:
            case DEVICE_STATE:
            case ACTIVATE:

                if (message.getArguments() != null && message.getArguments().length > 0) {
                    level = message.getArguments()[0];
                } else {
                    level = (byte) (message.getCommand() == UPBMessage.Command.ACTIVATE ? 100 : 0);
                }

                // Links will send FF (-1) for their level.
                if (level == -1 || level >= 100 || (level > 0 && !config.isDimmable())) {
                    newState = OnOffType.ON;
                } else if (level == 0) {
                    newState = OnOffType.OFF;
                } else {
                    newState = new PercentType(level);
                }
                break;
            case DEACTIVATE:
                newState = OnOffType.OFF;
                break;
            default:
                break;
        }

        if (newState != null) {
            logger.debug("Posting update: {},{}", itemName, newState);
            eventPublisher.postUpdate(itemName, newState);
        }
    }

    private boolean isValidId(byte id) {
        return id != 0 && id != -1;
    }
}
