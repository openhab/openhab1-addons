/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.tcp.protocol.internal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import org.openhab.binding.tcp.AbstractChannelEventSubscriberBinding;
import org.openhab.binding.tcp.AbstractSocketChannelEventSubscriberBinding;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.core.library.types.DecimalType;
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
public class TCPBinding extends AbstractSocketChannelEventSubscriberBinding<ProtocolBindingProvider> implements ManagedService {

	static private final Logger logger = LoggerFactory.getLogger(TCPBinding.class);
	
    static private int RECONNECT_INTERVAL = 24;    

	protected boolean internalReceiveChanneledCommand(String itemName,
			Command command, AbstractChannelEventSubscriberBinding<SocketChannel,ProtocolBindingProvider>.MuxChannel sChannel, String commandAsString) {

		ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		if(command != null ){		
			String tcpCommandName = null;
					
			if(command instanceof DecimalType) {
				tcpCommandName = commandAsString;
			} else {
				tcpCommandName = provider.getProtocolCommand(itemName,command);
			}

			// slap a CR and LF at the end of the command - should be what most remote server expect when 
			// they receive some ASCII based command/string
			tcpCommandName = tcpCommandName + ((char)13) + ((char)10);
			Direction direction = provider.getDirection(itemName,command);

			if(direction.equals(Direction.OUT) | direction.equals(Direction.BIDIRECTIONAL)) {

				ByteBuffer outputBuffer = ByteBuffer.allocate(1024);
				try {
					outputBuffer.put(tcpCommandName.getBytes("ASCII"));
				} catch (UnsupportedEncodingException e) {
					logger.warn("Exception while attempting an unsupported encoding scheme");
				}

				// send the buffer in an asynchronous way
				try {
					@SuppressWarnings("unused")
					ByteBuffer response = sChannel.writeBuffer(outputBuffer,false,3000);
				} catch (Exception e) {
					logger.error("An exception occured while writing a buffer to a channel: {}",e.getMessage());
				}

				// if the remote-end does not send a reply in response to the string we just sent, then the abstract superclass will update
				// the openhab status of the item for us. If it does reply, then an additional update is done via parseBuffer.
				// since this TCP binding does not know about the specific protocol, there might be two state updates (the command, and if
				// the case, the reply from the remote-end)
				return true;

			} else {
				logger.error("TCPCommand has the wrong direction");
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
	protected void parseBuffer(Collection<String> qualifiedItems,ByteBuffer byteBuffer){

		String theUpdate = new String(byteBuffer.array());

		for(String itemName : qualifiedItems) {
			for (ProtocolBindingProvider provider : providers) {
				if(provider.providesBindingFor(itemName)) {
					List<Command> commands = provider.getAllCommands(itemName);

					// first check if commands are defined, and that they have the correct DirectionType
					Iterator<Command> listIterator = commands.listIterator();
					while(listIterator.hasNext()){
						Command aCommand = listIterator.next();
						Direction theDirection = provider.getDirection(itemName,aCommand);

						if((theDirection == Direction.BIDIRECTIONAL | theDirection==Direction.IN)){
							
							List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,aCommand);
							State newState = null;
							
							if(aCommand instanceof DecimalType) {
								newState = createStateFromString(stateTypeList,theUpdate);
							} else {
								newState = createStateFromString(stateTypeList,aCommand.toString());
							}
							
							if(newState != null) {
								eventPublisher.postUpdate(itemName, newState);							        						
							} else {
								logger.warn("Can not parse input "+theUpdate+" to match command {} on item {}  ",aCommand,itemName);
							}
						}
						else {
							logger.debug("TCPCommand has the wrong direction");
						}
					}
				}
			}
		}
	}

	@Override
	protected int getReconnectInterval() {
		return RECONNECT_INTERVAL;
	}

	@Override
	public boolean isProperlyConfigured() {
		for (ProtocolBindingProvider provider : providers) {
			if(provider.providesBinding()) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// Nothing to do here in this "base"/"example" protocol implementation
	}

	protected void configureChannel(AbstractChannelEventSubscriberBinding<SocketChannel,ProtocolBindingProvider>.MuxChannel channel) {
		// Nothing to do here in this "base"/"example" protocol implementation
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "TCP Refresh Service";
	}

}