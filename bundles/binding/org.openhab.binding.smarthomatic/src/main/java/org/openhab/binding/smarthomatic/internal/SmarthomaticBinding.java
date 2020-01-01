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
package org.openhab.binding.smarthomatic.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.smarthomatic.SmarthomaticBindingProvider;
import org.openhab.binding.smarthomatic.internal.SHCMessage.SHCData;
import org.openhab.binding.smarthomatic.internal.SHCMessage.SHCHeader;
import org.openhab.binding.smarthomatic.internal.packetData.Packet;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.framework.Bundle;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * These class processes data that is send on the event bus and destined for a
 * smarthomatic device. The data is parsed and the put into smarthomatic data
 * structures. Likewise if received data is to be send on the event bus
 *
 * @author arohde
 * @author mcjobo
 * @since 1.9.0
 */
public class SmarthomaticBinding extends AbstractActiveBinding<SmarthomaticBindingProvider>
        implements ManagedService, SerialEventWorker {

    private static final Logger logger = LoggerFactory.getLogger(SmarthomaticBinding.class);
    /**
     * RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code>
     */
    private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");
    private BaseStation baseStation;
    private String serialPortname;
    private int serialBaudrate;
    private Packet packet;

    /**
     * the refresh interval which is used to poll values from the Smarthomatic
     * server (optional, defaults to 60000ms)
     *
     */
    private long refreshInterval = 60000;

    public SmarthomaticBinding() {
    }

    /**
     * activate binding
     *
     */
    @Override
    public void activate() {
        // log activate of binding
        if (baseStation != null) {
            logger.info("Smarthomatic Binding activated. BaseStation= {}", baseStation.toString());
        }

        Bundle bundle = SmarthomaticActivator.getContext().getBundle();
        URL fileURL = bundle.getEntry("packet_layout.xml");
        Packet packet = null;
        try {
            InputStream inputStream = fileURL.openConnection().getInputStream();
            JAXBContext jaxbContext = JAXBContext.newInstance(Packet.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            packet = (Packet) jaxbUnmarshaller.unmarshal(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        this.packet = packet;
    }

    /**
     * deactivate binding
     *
     */
    @Override
    public void deactivate() {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    protected String getName() {
        return "Smarthomatic Refresh Service";
    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    protected void execute() {
        // the frequently executed code (polling) goes here ...
        // logger.debug("execute() method is called!");
    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.

        for (SmarthomaticBindingProvider provider : this.providers) {
            if (provider.providesBindingFor(itemName)) {
                if (baseStation != null) {
                    baseStation.sendCommand(provider.getDeviceId(itemName), provider.getMessageGroupId(itemName),
                            provider.getMessageId(itemName), 0, command);
                    // provider.getType(itemName),
                    // provider.getToggleTime(itemName), command);
                }
            }
        }

        logger.debug("internalReceiveCommand() is called! {},{}", new String[] { itemName, command.toString() });
    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveUpdate() is called!{},{}", new String[] { itemName, newState.toString() });

    }

    /**
     * @{inheritDoc
     *
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            // to override the default refresh interval one has to add a
            // parameter to openhab.cfg like
            // <bindingName>:refresh=<intervalInMs>
            String refreshIntervalString = (String) config.get("refresh");
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            boolean changed = false;

            if (serialPortname != (String) config.get("serialPort")) {
                serialPortname = (String) config.get("serialPort");
                changed = true;
            }
            String dummy = (String) config.get("baud");
            try {
                if (serialBaudrate != Integer.parseInt(dummy)) {
                    serialBaudrate = Integer.parseInt(dummy);
                    changed = true;
                }
            } catch (NumberFormatException e) {
                logger.info("reading exception");
            }

            if (changed | (baseStation == null)) {
                if (baseStation != null) {
                    baseStation.closeSerialPort();
                }

                baseStation = new BaseStation(serialPortname, serialBaudrate, this);
                logger.debug("Smarthomatic Binding:update creates new basestation");
            }

            Enumeration<String> keys = config.keys();
            for (int i = 0; i < config.size(); i++) {
                String key = keys.nextElement();
                StringTokenizer tokens = new StringTokenizer(key, ":");

                if (tokens.nextToken().equals("device")) {
                    if (tokens.hasMoreElements()) {
                        dummy = tokens.nextToken();
                        int deviceID = Integer.parseInt(dummy);
                        String name = (String) config.get(key);
                        SmarthomaticGenericBindingProvider.addDevice(name, deviceID);
                        logger.debug("Smarthomatic device {} can be indexed by name {}", new String[] { dummy, name });
                    }
                }
                logger.debug("KEY: {}", key);
            }

            setProperlyConfigured(true);
        }
    }

    private String processTransformation(String transformation, String response) {
        String transformedResponse = response;
        if (transformation == null) {
            return transformedResponse;
        }
        try {
            String[] parts = splitTransformationConfig(transformation);
            String transformationType = parts[0];
            String transformationFunction = parts[1];

            TransformationService transformationService = TransformationHelper
                    .getTransformationService(SmarthomaticActivator.getContext(), transformationType);
            if (transformationService != null) {
                transformedResponse = transformationService.transform(transformationFunction, response);
            } else {
                transformedResponse = response;
                logger.warn("couldn't transform response because transformationService of type '{}' is unavailable",
                        transformationType);
            }
        } catch (TransformationException te) {
            logger.error("transformation throws exception [transformation= {}, response= {}]", transformation,
                    response);
            logger.error("received transformation exception", te);

            // in case of an error we return the response without any
            // transformation
            transformedResponse = response;
        }

        logger.debug("transformed response is '{}'", transformedResponse);

        return transformedResponse;
    }

    /**
     * Splits a transformation configuration string into its two parts - the
     * transformation type and the function/pattern to apply.
     *
     * @param transformation
     *            the string to split
     * @return a string array with exactly two entries for the type and the
     *         function
     */
    protected String[] splitTransformationConfig(String transformation) {
        Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("given transformation function '" + transformation
                    + "' does not follow the expected pattern '<function>(<pattern>)'");
        }
        matcher.reset();

        matcher.find();
        String type = matcher.group(1);
        String pattern = matcher.group(2);

        return new String[] { type, pattern };
    }

    @Override
    public void eventOccured(String message) {
        StringTokenizer strTok = new StringTokenizer(message, "\n");
        String data = null;

        // check incoming data
        int i = 0;
        while (strTok.hasMoreTokens()) {
            String s = strTok.nextToken();
            if (s.contains(SHCMessage.DATA_FLAG)) {
                data = s;
                logger.debug("<BaseStation data>[{}]: {}", i, data);
            }
        }
        if (data != null) {
            SHCMessage shcMessage = new SHCMessage(data, packet);
            SHCHeader shcHeader = shcMessage.getHeader();

            logger.debug("BaseStation SenderID: {} MsgType: {} MsgGroupID: {} MsgID: {} MsgData: {}",
                    shcHeader.getSenderID(), shcHeader.getMessageType(), shcHeader.getMessageGroupID(),
                    shcHeader.getMessageID(), shcHeader.getMessageData());

            SHCData info = shcMessage.getData();
            if (info != null) {
                logger.debug(info.toString());
            }

            // search all matching providers where DeviceID == SenderID of
            // Message-Header
            for (SmarthomaticBindingProvider provider : this.providers) {
                for (String itemName : provider.getItemNames()) {
                    if (shcHeader.getSenderID() == provider.getDeviceId(itemName)
                            && shcHeader.getMessageGroupID() == provider.getMessageGroupId(itemName)
                            && shcHeader.getMessageID() == provider.getMessageId(itemName)) {
                        Type type = shcMessage.openHABStateFromSHCMessage(provider.getItem(itemName))
                                .get(provider.getMessagePartId(itemName));
                        String transformed = processTransformation(provider.getConfigParam(itemName, "transformation"),
                                type.toString());
                        if (type instanceof DecimalType) {
                            type = DecimalType.valueOf(transformed);
                        }
                        if (isDataTypeSupported(provider.getItem(itemName), type)) {
                            eventPublisher.postUpdate(itemName, (State) type);
                        }
                    }
                }
            }
        }
    }

    private boolean isDataTypeSupported(Item item, Type type) {
        boolean result = false;

        for (Class<? extends State> supportedState : item.getAcceptedDataTypes()) {
            if (supportedState.isAssignableFrom(type.getClass())) {
                result = true;
            }
        }

        return result;
    }

}
