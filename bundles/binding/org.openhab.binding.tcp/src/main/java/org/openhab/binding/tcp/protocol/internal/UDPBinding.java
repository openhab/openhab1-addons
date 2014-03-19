/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tcp.protocol.internal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Dictionary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.tcp.AbstractDatagramChannelBinding;
import org.openhab.binding.tcp.Direction;
import org.openhab.binding.tcp.internal.TCPActivator;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.binding.tcp.protocol.UDPBindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UDPBinding is most "simple" implementation of a UDP based ASCII protocol. It sends and received 
 * data as ASCII strings. Data sent out is padded with a CR/LF. This should be sufficient for a lot
 * of home automation devices that take simple ASCII based control commands, or that send back
 * text based status messages
 * 
 * 
 * @author Karel Goderis
 * @since 1.1.0
 *
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
	private static String postAmble = "\r\n";
	// flag to use the reply of the remote end to update the status of the Item receiving the data
	private static boolean updateWithResponse = true;

	@Override
	protected boolean internalReceiveChanneledCommand(String itemName,
			Command command, Channel sChannel, String commandAsString) {

		ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		if(command != null ){		
			
			String transformedMessage = transformResponse(provider.getProtocolCommand(itemName, command),commandAsString);
			String UDPCommandName = preAmble + transformedMessage + postAmble ;

			ByteBuffer outputBuffer = ByteBuffer.allocate(UDPCommandName.getBytes().length);
			try {
				outputBuffer.put(UDPCommandName.getBytes("ASCII"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("Exception while attempting an unsupported encoding scheme");
			}

			// send the buffer in an asynchronous way
			ByteBuffer result = null;
			try {
				result = writeBuffer(outputBuffer,sChannel,blocking,timeOut);
			} catch (Exception e) {
				logger.error("An exception occurred while writing a buffer to a channel: {}",e.getMessage());
			}

			if(result!=null && blocking) {
				logger.info("Received {} from the remote end {}",new String(result.array()),sChannel.toString());
				String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, command),new String(result.array()));

				// if the remote-end does not send a reply in response to the string we just sent, then the abstract superclass will update
				// the openhab status of the item for us. If it does reply, then an additional update is done via parseBuffer.
				// since this TCP binding does not know about the specific protocol, there might be two state updates (the command, and if
				// the case, the reply from the remote-end)

				if(updateWithResponse) {

					List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,command);
					State newState = createStateFromString(stateTypeList,transformedResponse);

					if(newState != null) {
						eventPublisher.postUpdate(itemName, newState);						        						
					} else {
						logger.warn("Can not parse transformed input "+transformedResponse+" to match command {} on item {}  ",command,itemName);
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
	 * @return 
	 * 
	 */
	@Override
	protected void parseBuffer(String itemName, Command aCommand, Direction theDirection,ByteBuffer byteBuffer){

		String theUpdate = new String(byteBuffer.array());
		ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,aCommand);

		String transformedResponse = transformResponse(provider.getProtocolCommand(itemName, aCommand),theUpdate);
		State newState = createStateFromString(stateTypeList,transformedResponse);

		if(newState != null) {
			eventPublisher.postUpdate(itemName, newState);							        						
		} else {
			logger.warn("Can not parse input "+theUpdate+" to match command {} on item {}  ",aCommand,itemName);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {

		super.updated(config);

		if (config != null) {

			String timeOutString = (String) config.get("buffersize");
			if (StringUtils.isNotBlank(timeOutString)) {
				timeOut = Integer.parseInt((timeOutString));
			} else {
				logger.info("The maximum time out for blocking write operations will be set to the default vaulue of {}",timeOut);
			}

			String blockingString = (String) config.get("retryinterval");
			if (StringUtils.isNotBlank(blockingString)) {
				blocking = Boolean.parseBoolean((blockingString));
			} else {
				logger.info("The blocking nature of read/write operations will be set to the default vaulue of {}",blocking);
			}

			String preambleString = (String) config.get("preamble");
			if (StringUtils.isNotBlank(preambleString)) {
				preAmble = preambleString.replaceAll("\\\\", "\\");
			} else {
				logger.info("The preamble for all write operations will be set to the default vaulue of {}",preAmble);
			}

			String postambleString = (String) config.get("postamble");
			if (StringUtils.isNotBlank(postambleString)) {
				postAmble = postambleString.replaceAll("\\\\", "\\");;
			} else {
				logger.info("The postamble for all write operations will be set to the default vaulue of {}",postAmble);
			}
			
			String updatewithresponseString = (String) config.get("updatewithresponse");
			if (StringUtils.isNotBlank(updatewithresponseString)) {
				updateWithResponse = Boolean.parseBoolean((updatewithresponseString));
			} else {
				logger.info("Updating states with returned values will be set to the default vaulue of {}",updateWithResponse);
			}

		}

	}

	@Override
	protected void configureChannel(DatagramChannel channel) {
	}
	
	/**
	 * Splits a transformation configuration string into its two parts - the
	 * transformation type and the function/pattern to apply.
	 * 
	 * @param transformation the string to split
	 * @return a string array with exactly two entries for the type and the function
	 */
	protected String[] splitTransformationConfig(String transformation) {
		Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("given transformation function '" + transformation + "' does not follow the expected pattern '<function>(<pattern>)'");
		}
		matcher.reset();

		matcher.find();			
		String type = matcher.group(1);
		String pattern = matcher.group(2);

		return new String[] { type, pattern };
	}

	protected String transformResponse(String transformation, String response) {
		String transformedResponse;

		try {
			String[] parts = splitTransformationConfig(transformation);
			String transformationType = parts[0];
			String transformationFunction = parts[1];

			TransformationService transformationService = 
					TransformationHelper.getTransformationService(TCPActivator.getContext(), transformationType);
			if (transformationService != null) {
				transformedResponse = transformationService.transform(transformationFunction, response);
			} else {
				transformedResponse = response;
				logger.warn("couldn't transform response because transformationService of type '{}' is unavailable", transformationType);
			}
		}
		catch (TransformationException te) {
			logger.error("transformation throws exception [transformation="
					+ transformation + ", response=" + response + "]", te);

			// in case of an error we return the response without any
			// transformation
			transformedResponse = response;
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