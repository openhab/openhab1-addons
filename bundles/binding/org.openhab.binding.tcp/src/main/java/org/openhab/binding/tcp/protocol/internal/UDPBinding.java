/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tcp.protocol.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Dictionary;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.tcp.AbstractDatagramChannelBinding;
import org.openhab.binding.tcp.Direction;
import org.openhab.binding.tcp.internal.TCPActivator;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.binding.tcp.protocol.UDPBindingProvider;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UDPBinding is an implementation of a UDP based ASCII protocol. It sends and receives
 * data as ASCII strings. Data sent out is padded with a CR/LF. This should be sufficient for a lot
 * of home automation devices that take simple ASCII based control commands, or that send back
 * text based status messages.
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class UDPBinding extends AbstractDatagramChannelBinding<UDPBindingProvider> implements ManagedService {

    static private final Logger logger = LoggerFactory.getLogger(UDPBinding.class);

    /** RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code> */
    private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");

    // time to wait for a reply, in milliseconds
    private static int timeOut = 3000;
    // flag to use only blocking write/read operations
    private static boolean blocking = false;
    // string to prepend to data being sent
    private static String preAmble = "";
    // string to append to data being sent
    private static String postAmble = "";
    // flag to use the reply of the remote end to update the status of the Item receiving the data
    private static boolean updateWithResponse = true;
    // used character set
    private static String charset = "ASCII";

    @Override
    protected boolean internalReceiveChanneledCommand(String itemName, Command command, Channel sChannel,
            String commandAsString) {

        ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

        if (command == null) {
            return false;
        }

        String transformedMessage = transformResponse(provider.getProtocolCommand(itemName, command), commandAsString);
        String UDPCommandName = preAmble + transformedMessage + postAmble;

        ByteBuffer outputBuffer = null;
        try {
            outputBuffer = ByteBuffer.allocate(UDPCommandName.getBytes(charset).length);
            outputBuffer.put(UDPCommandName.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            logger.warn("Data for output buffer appears to be using an unsupported encoding");
        }

        // send the buffer in an asynchronous way
        ByteBuffer result = null;
        try {
            result = writeBuffer(outputBuffer, sChannel, blocking, timeOut);
        } catch (Exception e) {
            logger.warn("An exception occurred while writing a buffer to a channel", e);
        }

        if (result != null && blocking) {
            String resultString = "";
            try {
                resultString = new String(result.array(), charset);
            } catch (UnsupportedEncodingException e) {
                logger.warn("Data received from write operation appears to be using an unsupported encoding");
            }

            logger.info("Received {} from the remote end {}", resultString, sChannel.toString());
            String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, command),
                    resultString);

            // if the remote-end does not send a reply in response to the string we just sent, then the
            // abstract superclass will update the openhab status of the item for us. If it does reply,
            // then an additional update is done via parseBuffer.
            // since this binding does not know about the specific protocol, there might be two state
            // updates (the command, and if the case, the reply from the remote-end)

            if (updateWithResponse) {

                List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName, command);
                State newState = createStateFromString(stateTypeList, transformedResponse);

                if (newState != null) {
                    eventPublisher.postUpdate(itemName, newState);
                } else {
                    logger.warn("Cannot parse transformed input {} to match command {} on item {}",
                            transformedResponse, command, itemName);
                }

                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     *
     * Main function to parse ASCII string received
     *
     * @return
     *
     */
    @Override
    protected void parseBuffer(String itemName, Command aCommand, Direction theDirection, ByteBuffer byteBuffer) {

        String theUpdate = "";
        try {
            theUpdate = new String(byteBuffer.array(), charset);
            logger.debug("parseBuffer constructed update: '{}'", theUpdate);
        } catch (UnsupportedEncodingException e) {
            logger.warn("Exception while attempting an unsupported encoding scheme");
        }

        ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);
        if (provider == null) {
            logger.warn("No provider could be found for the item '{}'", itemName);
            return;
        }

        List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName, aCommand);

        String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, aCommand), theUpdate);
        State newState = createStateFromString(stateTypeList, transformedResponse);

        if (newState != null) {
            eventPublisher.postUpdate(itemName, newState);
        } else {
            logger.warn("Cannot parse input {} to match command {} on item {}", theUpdate, aCommand, itemName);
        }
    }

    protected void addBindingProvider(UDPBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(UDPBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void updated(Dictionary config) throws ConfigurationException {

        super.updated(config);

        if (config == null) {
            return;
        }

        String timeOutString = Objects.toString(config.get("buffersize"), null);
        if (isNotBlank(timeOutString)) {
            timeOut = Integer.parseInt(timeOutString);
        } else {
            logger.info("The maximum timeout for blocking write operations will be set to the default value of {}",
                    timeOut);
        }

        String blockingString = Objects.toString(config.get("retryinterval"), null);
        if (isNotBlank(blockingString)) {
            blocking = Boolean.parseBoolean(blockingString);
        } else {
            logger.info("The blocking nature of read/write operations will be set to the default value of {}",
                    blocking);
        }

        String preambleString = Objects.toString(config.get("preamble"), null);
        if (isNotBlank(preambleString)) {
            try {
                preAmble = preambleString.replaceAll("\\\\", "\\");
            } catch (Exception e) {
                preAmble = preambleString;
            }
        } else {
            logger.info("The preamble for all write operations will be set to the default value of {}", preAmble);
        }

        String postambleString = Objects.toString(config.get("postamble"), null);
        if (isNotBlank(postambleString)) {
            try {
                postAmble = postambleString.replaceAll("\\\\", "\\");
            } catch (Exception e) {
                postAmble = postambleString;
            }
        } else {
            logger.info("The postamble for all write operations will be set to the default value of {}", postAmble);
        }

        String updateWithResponseString = Objects.toString(config.get("updatewithresponse"), null);
        if (isNotBlank(updateWithResponseString)) {
            updateWithResponse = Boolean.parseBoolean(updateWithResponseString);
        } else {
            logger.info("Updating states with returned values will be set to the default value of {}",
                    updateWithResponse);
        }

        String charsetString = Objects.toString(config.get("charset"), null);
        if (isNotBlank(charsetString)) {
            charset = charsetString;
        } else {
            logger.info("The character set will be set to the default value of {}", charset);
        }
    }

    @Override
    protected void configureChannel(DatagramChannel channel) {
    }

    protected String transformResponse(String transformation, String response) {
        String transformedResponse;

        if (isEmpty(transformation) || transformation.equalsIgnoreCase("default")) {
            logger.debug("transformed response is '{}'", response);
            return response;
        }

        Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);
        if (matcher.matches()) {
            matcher.reset();
            matcher.find();
            String transformationServiceName = matcher.group(1);
            String transformationServiceParam = matcher.group(2);
            try {
                TransformationService transformationService = TransformationHelper
                        .getTransformationService(TCPActivator.getContext(), transformationServiceName);
                if (transformationService != null) {
                    transformedResponse = transformationService.transform(transformationServiceParam, response);
                } else {
                    transformedResponse = response;
                    logger.warn("couldn't transform response because transformationService of type '{}' is unavailable",
                            transformationServiceName);
                }
            } catch (Exception te) {
                logger.warn("Transformation threw an exception. [transformation={}, response={}]", transformation,
                        response, te);

                // in case of an error we return the response without any
                // transformation
                transformedResponse = response;
            }
        } else {
            transformedResponse = transformation;
        }

        logger.debug("transformed response is '{}'", transformedResponse);

        return transformedResponse;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName() {
        return "UDP Refresh Service";
    }
}
