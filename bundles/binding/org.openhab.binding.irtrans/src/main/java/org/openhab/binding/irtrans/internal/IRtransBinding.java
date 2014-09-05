/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.irtrans.internal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.irtrans.IRtransBindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.tcp.AbstractSocketChannelBinding;
import org.openhab.binding.tcp.Direction;

/**
 * 
 * Main binding class for the IRtrans binding, based on the AbstractSocketChannelBinding class which also serves
 * as a basis for the "regular" TCP binding.
 * 
 * @author Karel Goderis
 * @since 1.4.0
 *
 */
public class IRtransBinding extends AbstractSocketChannelBinding<IRtransBindingProvider> implements ManagedService {

	@SuppressWarnings("restriction")
	static private final Logger logger = LoggerFactory.getLogger(IRtransBinding.class);

	// Data structure to store the infrared commands that are 'loaded' from the configuration files
	// command loading from pre-defined configuration files is not supported (anymore), but the code is maintained
	// in case this functionality is re-added in the future
	final static protected HashSet<IrCommand> irCommands = new HashSet<IrCommand>();

	// time to wait for a reply, in milliseconds
	private int timeOut = 1000;

	/**
	 * Structure to store IRTrans infrared commands. See IRTrans documentation for a detailed
	 * description of the structure of the infrared commands (or wikipedia for infrared in general)
	 *
	 */
	private class IrCommand {

		/**
		 * 
		 * Each infrared command is in essence a sequence of characters/pointers that refer
		 * to pulse/pause timing pairs. So, in order to build an infrared command one has to 
		 * collate the pulse/pause timings as defined by the sequence
		 * 
		 * PulsePair is a small datastructure to capture each pulse/pair timing pair
		 *
		 */
		private class PulsePair {
			public int Pulse;
			public int Pause;
		}

		public String remote;
		public String command;
		public String sequence;
		public ArrayList<PulsePair> pulsePairs;
		public int numberOfRepeats;
		public int frequency;
		public int frameLength;
		public int pause;
		public boolean startBit;
		public boolean repeatStartBit;
		public boolean noTog;
		public boolean rc5;
		public boolean rc6;

		/**
		 * Matches two IrCommands
		 * Commands match if they have the same remote and the same command
		 * 
		 * @param anotherCommand
		 *            the another command
		 * @return true, if successful
		 */
		public boolean matches(IrCommand anotherCommand) {
			return (matchRemote(anotherCommand) && matchCommand(anotherCommand));
		}

		/**
		 * Match remote fields of two IrCommands
		 * In everything we do in the IRtrans binding, the "*" stands for a wilcard
		 * character and will match anything
		 * 
		 * @param S
		 *            the s
		 * @return true, if successful
		 */
		private boolean matchRemote(IrCommand S){
			if(remote.equals("*") || S.remote.equals("*")) {
				return true;
			} else {
				if(S.remote.equals(remote)) {
					return true;
				} else {
					return false;
				}
			}
		} 

		/**
		 * Match command fields of two IrCommands
		 * 
		 * @param S
		 *            the s
		 * @return true, if successful
		 */
		private boolean matchCommand(IrCommand S){
			if(command.equals("*") || S.command.equals("*")) {
				return true;
			} else {
				if(S.command.equals(command)) {
					return true;
				} else {
					return false;
				}
			}
		} 


		/**
		 * Convert/Parse the IRCommand into a ByteBuffer that is compatible with the IRTrans devices
		 * 
		 * @return the byte buffer
		 */
		@SuppressWarnings("restriction")
		public ByteBuffer toByteBuffer(){

			ByteBuffer byteBuffer = ByteBuffer.allocate(44+210+1);

			// skip first byte for length - we will fill it in at the end
			byteBuffer.position(1);

			//Checksum - 1 byte - not used in the ethernet version of the device
			byteBuffer.put((byte) 0);

			//Command - 1 byte - not used
			byteBuffer.put((byte) 0);                       

			// Address - 1 byte - not used
			byteBuffer.put((byte) 0);

			//Mask - 2 bytes - not used
			byteBuffer.putShort((short)0);

			//Number of pulse pairs - 1 byte

			try{
				byte[] byteSequence = sequence.getBytes("ASCII");
				byteBuffer.put((byte)(byteSequence.length));
			} catch(UnsupportedEncodingException e){
				logger.debug("An exception occurred while encoding a bytebuffer");
			}

			//Frequency - 1 byte
			byteBuffer.put((byte)frequency);

			//Mode / Flags - 1 byte
			byte modeFlags = 0;
			if(startBit) modeFlags = (byte) (modeFlags | 1);
			if(repeatStartBit) modeFlags = (byte) (modeFlags | 2);
			if(rc5) modeFlags = (byte)(modeFlags | 4);
			if(rc6) modeFlags = (byte)(modeFlags | 8);
			byteBuffer.put(modeFlags);

			//Pause timings - 8 Shorts = 16 bytes
			for(int i=0;i<pulsePairs.size();i++){
				byteBuffer.putShort((short)Math.round(pulsePairs.get(i).Pause/8));
			}
			for(int i=pulsePairs.size();i<=7;i++){
				byteBuffer.putShort((short)0);
			}

			//Pulse timings - 8 Shorts = 16 bytes
			for(int i=0;i<pulsePairs.size();i++){
				byteBuffer.putShort((short)Math.round(pulsePairs.get(i).Pulse/8));
			}
			for(int i=pulsePairs.size();i<=7;i++){
				byteBuffer.putShort((short)0);
			}

			// Time Counts - 1 Byte
			byteBuffer.put((byte)pulsePairs.size());

			//Repeats - 1 Byte
			byte repeat = (byte)0;
			repeat=(byte)numberOfRepeats;
			if(frameLength >0) {
				repeat = (byte) (repeat | 128);
			}
			byteBuffer.put(repeat);

			// Repeat Pause or Frame Length - 1 byte
			if((repeat & 128)==128) {
				byteBuffer.put((byte)frameLength);
			} else {
				byteBuffer.put((byte)pause);
			}

			// IR pulse sequence
			try {
				byteBuffer.put(sequence.getBytes("ASCII"));
			} catch(UnsupportedEncodingException e) {
			}

			// Add <CR> (ASCII 13) at the end of the sequence
			byteBuffer.put((byte)((char)13));

			// set the length of the byte sequence
			byteBuffer.flip();
			byteBuffer.position(0);
			byteBuffer.put((byte)(byteBuffer.limit()-1));
			byteBuffer.position(0);

			return byteBuffer;

		}


		/**
		 * Convert the the infrared command to a Hexadecimal notation/string that can be 
		 * interpreted by the IRTrans device
		 * 
		 * Convert the first 44 bytes to hex notation, then copy the remainder 
		 * (= IR command piece) as ASCII string 
		 * 
		 * @return the byte buffer in Hex format
		 */
		public ByteBuffer toHEXByteBuffer(){


			byte hexDigit[] = {
					'0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
			};

			ByteBuffer byteBuffer = toByteBuffer();
			byte[] toConvert = new byte[byteBuffer.limit()];
			byteBuffer.get(toConvert,0,byteBuffer.limit());

			byte[] converted = new byte[toConvert.length * 2];

			for (int k = 0; k < toConvert.length-1; k++) {
				converted[2*k]= hexDigit[(toConvert[k] >> 4) & 0x0f];
				converted[2*k+1] = hexDigit[toConvert[k] & 0x0f];

			}

			ByteBuffer convertedBuffer = ByteBuffer.allocate(converted.length);
			convertedBuffer.put(converted);
			convertedBuffer.flip();

			return convertedBuffer;

		}

		/**
		 * Convert 'sequence' bit of the IRTrans compatible byte buffer to a Hexidecimal string
		 * 
		 * @return the string
		 */
		public String sequenceToHEXString(){
			byte[] byteArray = toHEXByteArray();
			return new String(byteArray,88,byteArray.length-88-2);
		}

		/**
		 * Convert the IRTrans compatible byte buffer to a string
		 * 
		 * @return the string
		 */
		public String toHEXString(){
			return new String(toHEXByteArray());
		}

		/**
		 * Convert the IRTrans compatible byte buffer to a byte array.
		 * 
		 * @return the byte[]
		 */
		public byte[] toHEXByteArray(){
			return toHEXByteBuffer().array();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "restriction" })
	public void updated(Dictionary config) throws ConfigurationException {

		super.updated(config);

		if (config != null) {

			String timeOutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeOutString)) {
				timeOut = Integer.parseInt((timeOutString));
			} else {
				logger.info("The maximum time out for blocking write operations will be set to the default vaulue of {}",timeOut);
			}

		}

		// Prevent users from doing funny stuff with this binding
		useAddressMask = false;
		itemShareChannels = true;
		bindingShareChannels = true;
		directionsShareChannels = false;
		maximumBufferSize = 1024;
		
		setProperlyConfigured(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean internalReceiveChanneledCommand(String itemName,
			Command command, Channel sChannel,String commandAsString) {

		IRtransBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		String remoteName = null;
		String irCommandName = null;

		if(command != null && provider != null){		

			if(command instanceof DecimalType) {
				remoteName = StringUtils.substringBefore(commandAsString, ",");
				irCommandName = StringUtils.substringAfter(commandAsString,",");

				IrCommand firstCommand = new IrCommand();
				firstCommand.remote = remoteName;
				firstCommand.command = irCommandName;
				IrCommand secondCommand = new IrCommand();
				secondCommand.remote = provider.getRemote(itemName,command);
				secondCommand.command = provider.getIrCommand(itemName,command);

				if(!firstCommand.matches(secondCommand)) {
					remoteName = null;
					irCommandName = null;
				} 
			} else {
				remoteName = provider.getRemote(itemName,command);
				irCommandName = provider.getIrCommand(itemName,command);
			}		
		}	

		if(remoteName != null && irCommandName != null) {

			Leds led = provider.getLed(itemName,command);

			IrCommand theCommand = new IrCommand();
			theCommand.remote = remoteName;
			theCommand.command = irCommandName;

			// construct the string we need to send to the IRtrans device
			String output = packIRDBCommand(led, theCommand);

			ByteBuffer outputBuffer = ByteBuffer.allocate(output.getBytes().length);
			ByteBuffer response = null;
			try {
				outputBuffer.put(output.getBytes("ASCII"));
				response = writeBuffer(outputBuffer,sChannel,true,timeOut);
			} catch (UnsupportedEncodingException e) {
				logger.error("An exception occurred while encoding an infrared command: {}",e.getMessage());
			}

			if(response != null) {	
				String message = stripByteCount(response);
				if(message != null) {
					if(message.contains("RESULT OK")){

						List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,command);
						State newState = createStateFromString(stateTypeList,commandAsString);

						if(newState != null) {
							eventPublisher.postUpdate(itemName, newState);						        						
						} else {
							logger.warn("Can not parse "+commandAsString+" to match command {} on item {}  ",command,itemName);
						}

					} else {
						logger.warn("Received an unexecpted response {}",StringUtils.substringAfter(message,
								"RESULT "));
					}
				}
			} else {
				logger.warn("Did not receive an answer from the IRtrans device - Parsing is skipped");
			}
		}  else {
			logger.warn("Invalid command {} for Item {} - Transmission is skipped",commandAsString,itemName);
		}

		// we will always return false, because the irBinding itself will postUpdate new values, the underlying tcp binding should
		// not deal with that anymore.
		return false;

	}

	/**
	 * Main method to parse ASCII string received from the IR Transceiver.
	 * 
	 * @param qualifiedItems
	 *            the qualified items, e.g. only those items that have the host:port combination
	 *            in its binding configuration string will "receive" the ASCII string from the 
	 *            IRTrans device with host:port 
	 * @param byteBuffer
	 *            the byte buffer
	 */
	@Override
	protected void parseBuffer(String itemName, Command aCommand, Direction theDirection,ByteBuffer byteBuffer){

		String message = stripByteCount(byteBuffer);

		if(message != null) {

			// IRTrans devices return "RESULT OK" when it succeeds to emit an infrared sequence
			if(message.contains("RESULT OK")){
				parseOKMessage(itemName,message);
			}

			// IRTrans devices return a string starting with RCV_HEX each time it captures an
			// infrared sequence from a remote control
			if(message.contains("RCV_HEX")){
				parseHexMessage(itemName,message, aCommand);
			}

			// IRTrans devices return a string starting with RCV_COM each time it captures an
			// infrared sequence from a remote control that is stored in the device's internal dB
			if(message.contains("RCV_COM")){
				parseIRDBMessage(itemName,message, aCommand);
			}
		} else {
			logger.warn("Received some non-compliant garbage ({})- Parsing is skipped",byteBuffer.toString());
		}

	}

	/**
	 * Parses the rcv hex.
	 * 
	 * @param itemName
	 *            the qualified items
	 * @param message
	 *            the message
	 * @param ohCommand
	 * 			  the openHAB command
	 */
	protected void parseHexMessage(String itemName,String message, Command ohCommand){

		Pattern HEX_PATTERN = Pattern.compile("RCV_HEX (.*)");
		Matcher matcher = HEX_PATTERN.matcher(message);

		if(matcher.matches())
		{
			String command = matcher.group(1);
			IrCommand theCommand =  getIrCommand(command);

			if(theCommand != null ) {
				parseDecodedCommand(itemName,theCommand,ohCommand);
			} else {
				logger.error("{} does not match any know IRtrans command",command);
			}

		} else {
			logger.error("{} does not match the IRtrans message formet ({})",message,matcher.pattern());
		}
	}


	protected void parseIRDBMessage(String itemName,String message, Command ohCommand){

		Pattern IRDB_PATTERN = Pattern.compile("RCV_COM (.*),(.*),(.*),(.*)");
		Matcher matcher = IRDB_PATTERN.matcher(message);

		if(matcher.matches()) {

			IrCommand theCommand = new IrCommand();
			theCommand.remote = matcher.group(1);
			theCommand.command = matcher.group(2);		

			parseDecodedCommand(itemName,theCommand,ohCommand);

		} else {
			logger.error("{} does not match the IRDB IRtrans message format ({})",message,matcher.pattern());
		}
	}

	protected void parseDecodedCommand(String itemName, IrCommand theCommand, Command ohCommand) {

		if(theCommand != null) {
			//traverse the providers, for each provider, check each binding if it matches theCommand
			for (IRtransBindingProvider provider : providers) {
				if(provider.providesBindingFor(itemName)) {
					List<org.openhab.core.types.Command> commands = provider.getAllCommands(itemName);

					// first check if commands are defined, and that they have the correct DirectionType
					Iterator<org.openhab.core.types.Command> listIterator = commands.listIterator();
					while(listIterator.hasNext()){
						org.openhab.core.types.Command aCommand = listIterator.next();
						IrCommand providerCommand = new IrCommand();
						providerCommand.remote = provider.getRemote(itemName,aCommand);
						providerCommand.command = provider.getIrCommand(itemName, aCommand);

						if(aCommand==ohCommand) {
							if(providerCommand.matches(theCommand)){

								List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,aCommand);
								State newState = null;

								if(aCommand instanceof DecimalType) {
									newState = createStateFromString(stateTypeList,theCommand.remote+","+theCommand.command);
								} else {
									newState = createStateFromString(stateTypeList,aCommand.toString());
								}

								if(newState != null) {
									eventPublisher.postUpdate(itemName, newState);							        						
								} else {
									logger.warn("Can not create an Item State to match command {} on item {}  ",aCommand,itemName);
								}
							}
							else {
								logger.info("The IRtrans command '{},{}' does not match the command '{}' of the binding configuration for item '{}'",new Object[] {theCommand.remote,theCommand.command,ohCommand,itemName});
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Parses the result send.
	 * 
	 * @param itemName
	 *            the qualified items
	 * @param message
	 *            the message
	 */
	protected void parseOKMessage(String itemName,String message){
		// nothing interesting to do here
	}

	/**
	 * "Pack" the infrared command so that it can be sent to the IRTrans device
	 * 
	 * @param led
	 *            the led
	 * @param theCommand
	 *            the the command
	 * @return a string which is the full command to be sent to the device
	 */
	protected String packHexCommand(Leds led, IrCommand theCommand){
		String output = new String();

		output = "Asndhex ";
		output += "L";
		switch(led) {
		case ALL:
			output += "B";
			break;
		case ONE:
			output += "1";
			break;
		case TWO:
			output += "2";
			break;
		case THREE:
			output += "3";
			break;
		case FOUR:
			output += "4";
			break;
		case FIVE:
			output += "5";
			break;
		case SIX:
			output += "6";
			break;
		case SEVEN:
			output += "7";
			break;
		case EIGHT:
			output += "8";
			break;
		case INTERNAL:
			output += "I";
			break;
		case EXTERNAL:
			output += "E";
			break;
		case DEFAULT:
			output += "D";
			break;
		}

		output += ",";
		output += "H"+theCommand.toHEXString();

		output += (char) 13;

		return output;

	}

	/**
	 * "Pack" the infrared command so that it can be sent to the IRTrans device
	 * 
	 * @param led
	 *            the led
	 * @param theCommand
	 *            the the command
	 * @return a string which is the full command to be sent to the device
	 */
	protected String packIRDBCommand(Leds led, IrCommand theCommand){
		String output = new String();

		output = "Asnd ";
		output += theCommand.remote;
		output += ",";
		output += theCommand.command;
		output += ",l";
		switch(led) {
		case ALL:
			output += "B";
			break;
		case ONE:
			output += "1";
			break;
		case TWO:
			output += "2";
			break;
		case THREE:
			output += "3";
			break;
		case FOUR:
			output += "4";
			break;
		case FIVE:
			output += "5";
			break;
		case SIX:
			output += "6";
			break;
		case SEVEN:
			output += "7";
			break;
		case EIGHT:
			output += "8";
			break;
		case INTERNAL:
			output += "I";
			break;
		case EXTERNAL:
			output += "E";
			break;
		case DEFAULT:
			output += "D";
			break;
		}

		output += "\r\n";

		return output;

	}

	/**
	 * Strip byte count from the bytebuffer. IRTrans devices include the number of bytes sent
	 * in each response it sends back to the connected host. This is a simple error checking 
	 * mechanism - we do need that information, and so, we strip it
	 * 
	 * @param byteBuffer
	 *            the byte buffer
	 * @return the string
	 */
	protected String stripByteCount(ByteBuffer byteBuffer){
		/** {@link Pattern} which matches a binding configuration part */
		Pattern RESPONSE_PATTERN = Pattern.compile("..(\\d{5}) (.*)");
		String message = null;

		String response = new String(byteBuffer.array(),0,byteBuffer.limit());
		response = StringUtils.chomp(response);

		Matcher matcher = RESPONSE_PATTERN.matcher(response);
		if(matcher.matches()){
			String byteCountAsString = matcher.group(1);
			int byteCount = Integer.parseInt(byteCountAsString);
			message = matcher.group(2);
		}

		return message;
	}

	/**
	 * Fetch the IrCommand that corresponds with the given (hex)String.
	 * 
	 * @param someString
	 *            the some string
	 * @return the ir command
	 */
	protected IrCommand getIrCommand(String someString){

		IrCommand theCommand = null;

		if(someString != null) {
			// Run through the dB if IrCommands to see which one is matching, if any, the payload we just received
			Iterator<IrCommand> commandIterator = irCommands.iterator();
			while(commandIterator.hasNext()){
				IrCommand aCommand = commandIterator.next();
				if(aCommand.sequenceToHEXString().equals(someString)){
					theCommand = aCommand;
					break;
				}
			}
		}

		return theCommand;
	}

	/**
	 * Fetch the IrCommand for a given remote:command combination
	 * 
	 * @param remote
	 *            the remote
	 * @param command
	 *            the command
	 * @return the ir command
	 */
	protected IrCommand getIrCommand(String remote, String command) {
		IrCommand theCommand = null;

		if(remote != null && command != null) {
			// Run through the dB if IrCommands to see which one is matching, if any, the payload we just received
			Iterator<IrCommand> commandIterator = irCommands.iterator();
			while(commandIterator.hasNext()){
				IrCommand aCommand = commandIterator.next();
				if(aCommand.remote.equals(remote) && aCommand.command.equals(command)){
					theCommand = aCommand;
					break;
				}
			}
		}

		return theCommand;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureChannel(Channel channel) {

		String putInASCIImode = "ASCI";

		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		try {
			byteBuffer.put(putInASCIImode.getBytes("ASCII"));
			writeBuffer(byteBuffer,channel,false,timeOut);
		} catch (UnsupportedEncodingException e) {
			logger.error("An exception occurred while configurting the IRtrans device: {}",e.getMessage());
		}

		String getFirmwareVersion = "Aver" + (char) 13;
		ByteBuffer response = null;

		byteBuffer = ByteBuffer.allocate(5);
		try {
			byteBuffer.put(getFirmwareVersion.getBytes("ASCII"));
			response = writeBuffer(byteBuffer,channel,true,timeOut);
		} catch (UnsupportedEncodingException e) {
			logger.error("An exception occurred while configurting the IRtrans device: {}",e.getMessage());
		}

		if(response != null) {	
			String message = stripByteCount(response);
			if(message != null) {
				if(message.contains("VERSION")){
					logger.info("Found an IRtrans device with firmware {}", message);
				} else {
					logger.warn("Received some non-compliant garbage ({})",message);
				}
			}
		} else {
			logger.warn("Did not receive an answer from the IRtrans device - Parsing is skipped");
		}

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "IRtrans Refresh Service";
	}

	// FOR FUTURE USAGE - ORG.OPENHAB.MODEL.* RELATED - Parsing of IRtrans .rem files
	// KEPT IN PLACE IN CASE A NEW MODEL RELATED PARSING APPROACH IS CHOSEN IN THE FUTURE

	//	/**
	//	 * Populates our infrared command database with the contents of the IRTrans .rem config files
	//	 * 
	//	 * @param aModel
	//	 *            
	//	 */
	//	private void addIRtransModel(IRtransModel aModel){
	//		String remoteName = aModel.getRemote().getName();
	//
	//		for (org.openhab.model.irtrans.IRtrans.Command aCommand : aModel.getCommands()) {
	//
	//			int timingId = Integer.parseInt(aCommand.getTiming());
	//
	//			Timing theTiming = null;
	//			for (Timing atiming: aModel.getTimings()) {
	//				if(Integer.parseInt(atiming.getId()) == timingId)
	//				{
	//					theTiming = atiming;
	//					break;
	//				}
	//			}
	//
	//			if(theTiming != null && Integer.parseInt(theTiming.getCount()) == theTiming.getPairs().size()) {
	//				IrCommand newCommand = new IrCommand();
	//				newCommand.remote = remoteName;
	//				newCommand.command = aCommand.getName();
	//				newCommand.sequence = aCommand.getCommand();
	//
	//				newCommand.pulsePairs = new ArrayList<PulsePair>();
	//
	//				for(int i=1;i<=Integer.parseInt(theTiming.getCount());i++ ) {
	//					PulsePair newPair = new PulsePair();
	//
	//					TimingPair thePair = null;
	//
	//					for (TimingPair aPair: theTiming.getPairs()) {
	//						if(Integer.parseInt(aPair.getId()) == i){
	//							thePair = aPair;
	//							break;
	//						}
	//					}
	//
	//					newPair.Pause = Integer.parseInt(thePair.getPause());
	//					newPair.Pulse = Integer.parseInt(thePair.getPulse());
	//
	//					newCommand.pulsePairs.add(newPair);
	//				}       
	//
	//				TimingOptions theOptions = theTiming.getOptions();
	//				if(theOptions.getFrequency()!=null)newCommand.frequency = Integer.parseInt(theOptions.getFrequency());
	//				if(theOptions.getLength()!=null)newCommand.frameLength = Integer.parseInt(theOptions.getLength());
	//				if(theOptions.getPause()!=null) newCommand.pause = Integer.parseInt(theOptions.getPause());
	//				if(theOptions.getRepeats()!=null)newCommand.numberOfRepeats = Integer.parseInt(theOptions.getRepeats());
	//				newCommand.startBit = theOptions.isStartbit();
	//				newCommand.noTog = theOptions.isNotog();
	//				newCommand.rc5 = theOptions.isRc5();
	//				newCommand.rc6 = theOptions.isRc6();
	//				newCommand.repeatStartBit = theOptions.isRepeatstartbit();
	//
	//				irCommands.add(newCommand);
	//
	//			}
	//		}
	//	}
	//
	//	/**
	//	 * Removes the ir trans model.
	//	 * 
	//	 * @param aModel
	//	 *            the a model
	//	 */
	//	private void removeIRtransModel(IRtransModel aModel){		
	//		String remoteName = aModel.getRemote().getName();
	//		
	//		Iterator<IrCommand> commandIterator = irCommands.iterator();
	//		while(commandIterator.hasNext()){
	//			IrCommand aCommand = commandIterator.next();
	//			if(aCommand.remote.equals(remoteName)) {
	//				irCommands.remove(aCommand);
	//			}
	//		}
	//		
	//
	//	}

}
