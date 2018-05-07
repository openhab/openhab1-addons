/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
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
import java.util.Dictionary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.openhab.binding.tcp.AbstractSocketChannelBinding;
import org.openhab.binding.tcp.Direction;
import org.openhab.binding.tcp.internal.TCPActivator;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.binding.tcp.protocol.TCPBindingProvider;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TCPBinding is most "simple" implementation of a TCP based ASCII protocol. It sends and received
 * data as ASCII strings. Data sent out is padded with a CR/LF. This should be sufficient for a lot
 * of home automation devices that take simple ASCII based control commands, or that send back
 * text based status messages
 *
 *
 * @author Karel Goderis
 * @since 1.1.0
 *
 */
public class TCPBinding extends AbstractSocketChannelBinding<TCPBindingProvider> implements ManagedService {

    static private final Logger logger = LoggerFactory.getLogger(TCPBinding.class);

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
    // flag to use the reply of the remote end to update the status of the Item receving the data
    private static boolean updateWithResponse = true;
    // used character set
    private static String charset = "ASCII";

    @Override
    protected boolean internalReceiveChanneledCommand(String itemName, Command command, Channel sChannel,
            String commandAsString) {

        ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

        if (command != null) {

            String transformedMessage = transformResponse(provider.getProtocolCommand(itemName, command),
                    commandAsString);
            String tcpCommandName = preAmble + transformedMessage + postAmble;

            ByteBuffer outputBuffer = null;
            try {
                outputBuffer = ByteBuffer.allocate(tcpCommandName.getBytes(charset).length);
                outputBuffer.put(tcpCommandName.getBytes(charset));
            } catch (UnsupportedEncodingException e) {
                logger.warn("Exception while attempting an unsupported encoding scheme");
            }

            // send the buffer in an asynchronous way
            ByteBuffer result = null;
            try {
                result = writeBuffer(outputBuffer, sChannel, blocking, timeOut);
            } catch (Exception e) {
                logger.error("An exception occurred while writing a buffer to a channel: {}", e.getMessage());
            }

            if (result != null && blocking) {
                String resultString = "";
                try {
                    resultString = new String(result.array(), charset).split("\0")[0];
                } catch (UnsupportedEncodingException e) {
                    logger.warn("Exception while attempting an unsupported encoding scheme");
                }

                logger.info("Received {} from the remote end {}", resultString, sChannel.toString());
                String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, command),
                        resultString);

                // if the remote-end does not send a reply in response to the string we just sent, then the abstract
                // superclass will update
                // the openhab status of the item for us. If it does reply, then an additional update is done via
                // parseBuffer.
                // since this TCP binding does not know about the specific protocol, there might be two state updates
                // (the command, and if
                // the case, the reply from the remote-end)

                if (updateWithResponse) {

                    List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName, command);
                    State newState = createStateFromString(stateTypeList, transformedResponse);

                    if (newState != null) {
                        eventPublisher.postUpdate(itemName, newState);
                    } else {
                        logger.warn("Cannot parse transformed output " + transformedResponse
                                + " to match command {} on item {}  ", command, itemName);
                    }

                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
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
            theUpdate = new String(byteBuffer.array(), charset).split("\0")[0];
        } catch (UnsupportedEncodingException e) {
            logger.warn("Exception while attempting an unsupported encoding scheme");
        }

        ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

        List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName, aCommand);

        String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, aCommand), theUpdate);
        State newState = createStateFromString(stateTypeList, transformedResponse);

        if (newState != null) {
            eventPublisher.postUpdate(itemName, newState);
        } else {
            logger.warn("Cannot parse input " + theUpdate + " to match command {} on item {}  ", aCommand, itemName);
        }
    }

    protected void addBindingProvider(TCPBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(TCPBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void updated(Dictionary config) throws ConfigurationException {

        super.updated(config);

        if (config != null) {

            String timeOutString = (String) config.get("timeout");
            if (isNotBlank(timeOutString)) {
                timeOut = Integer.parseInt((timeOutString));
            } else {
                logger.info("The maximum time out for blocking write operations will be set to the default value of {}",
                        timeOut);
            }

            String blockingString = (String) config.get("blocking");
            if (isNotBlank(blockingString)) {
                blocking = Boolean.parseBoolean((blockingString));
            } else {
                logger.info("The blocking nature of read/write operations will be set to the default value of {}",
                        blocking);
            }

            String preambleString = (String) config.get("preamble");
            if (isNotBlank(preambleString)) {
                preAmble = StringEscapeUtils.unescapeJava(preambleString);
            } else {
                logger.info("The preamble for all write operations will be set to the default value of \"{}\"",
                        preAmble);
            }

            String postambleString = (String) config.get("postamble");
            if (isNotBlank(postambleString)) {
                postAmble = StringEscapeUtils.unescapeJava(postambleString);
            } else {
                logger.info("The postamble for all write operations will be set to the default value of \"{}\"",
                        postAmble);
            }

            String updatewithresponseString = (String) config.get("updatewithresponse");
            if (isNotBlank(updatewithresponseString)) {
                updateWithResponse = Boolean.parseBoolean((updatewithresponseString));
            } else {
                logger.info("Updating states with returned values will be set to the default value of {}",
                        updateWithResponse);
            }

            String charsetString = (String) config.get("charset");
            if (isNotBlank(charsetString)) {
                charset = charsetString;
            } else {
                logger.info("The characterset will be set to the default value of {}", charset);
            }

        }

    }

    @Override
    protected void configureChannel(Channel channel) {
    }

    protected String transformResponse(String transformation, String response) {
        String transformedResponse;

        if (isEmpty(transformation) || transformation.equalsIgnoreCase("default")) {
            transformedResponse = response;
        } else {
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
                        logger.warn(
                                "couldn't transform response because transformationService of type '{}' is unavailable",
                                transformationServiceName);
                    }
                } catch (Exception te) {
                    logger.error("transformation throws exception [transformation={}, response={}]", transformation,
                            response, te);

                    // in case of an error we return the response without any
                    // transformation
                    transformedResponse = response;
                }
            } else {
                transformedResponse = transformation;
            }
        }

        logger.debug("transformed response is '{}'", transformedResponse);

        return transformedResponse;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName() {
        return "TCP Refresh Service";
    }

}
