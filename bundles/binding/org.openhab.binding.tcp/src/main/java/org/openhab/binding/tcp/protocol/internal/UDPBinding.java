/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.tcp.AbstractDatagramChannelEventSubscriberBinding;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.core.ModelRepository;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPBinding extends AbstractDatagramChannelEventSubscriberBinding<ProtocolBindingProvider> implements ManagedService {

	static private final Logger logger = LoggerFactory.getLogger(UDPBinding.class);

	static protected ModelRepository modelRepository;

	@Override
	protected boolean internalReceiveChanneledCommand(String itemName,
			org.openhab.core.types.Command command, DatagramChannel dChannel) {

		ProtocolBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		Item theItem = getItemFromItemName(itemName);

		String UDPCommandName = null;
		Command finalCommand = null;

		if(command instanceof StringType && theItem != null) {
			// create a command from String
			try {
				finalCommand = createCommandFromString(theItem,"*");
			} catch (Exception e) {
				logger.warn("Exception occured whilst creating a Command for item {}",theItem);
			}

			UDPCommandName = command.toString();

		} else {
			finalCommand = command;
			UDPCommandName = provider.getProtocolCommand(itemName,finalCommand);
		}

		// slap a CR and LF at the end of the command - should be what most remote server expect when 
		// they receive some ASCII based command/string
		UDPCommandName = UDPCommandName + ((char)13) + ((char)10);

		if(finalCommand != null ){

			Direction direction = provider.getDirection(itemName,finalCommand);

			if(direction.equals(Direction.OUT) | direction.equals(Direction.BIDIRECTIONAL)) {

				ByteBuffer outputBuffer = ByteBuffer.allocate(1024);
				try {
					outputBuffer.put(UDPCommandName.getBytes("ASCII"));
				} catch (UnsupportedEncodingException e) {
					logger.warn("Exception occured whilst creating a Command for item {}",theItem);
				}

				// send the buffer in an assyncrhonous way
				@SuppressWarnings("unused")
				ByteBuffer response = writeBuffer(dChannel,outputBuffer,false,3000);
				
				return true;

			} else {
				logger.error("UDPCommand has the wrong direction");
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
	protected void parseBuffer(Collection<String> qualifiedItems,ByteBuffer byteBuffer){


		String theUpdate = new String(byteBuffer.array());

		for(String itemName : qualifiedItems) {
			for (ProtocolBindingProvider provider : providers) {
				if(provider.providesBindingFor(itemName)) {
					List<org.openhab.core.types.Command> commands = provider.getCommands(itemName);

					// first check if commands are defined, and that they have the correct DirectionType
					Iterator<org.openhab.core.types.Command> listIterator = commands.listIterator();
					while(listIterator.hasNext()){
						org.openhab.core.types.Command aCommand = listIterator.next();
						Direction theDirection = provider.getDirection(itemName,aCommand);
						String providerCommand = provider.getProtocolCommand(itemName, aCommand);

						if((theDirection == Direction.BIDIRECTIONAL | theDirection==Direction.IN)){
							if(aCommand instanceof StringType) {
								eventPublisher.postUpdate(itemName, new StringType(theUpdate));
							} else if ((providerCommand.equals(theUpdate))){
								eventPublisher.postUpdate(itemName, (State) aCommand);				
							} else {
								logger.warn("Can not parse input "+theUpdate+" to match command {} on item {}  ",aCommand,itemName);
							}
						}
						else {
							logger.debug("UDPCommand has the wrong direction");
						}
					}
				}
			}
		}
	}

	@Override
	protected void configurePersistentConnection(DatagramChannel dChannel) {
	}

	@Override
	protected int getReconnectInterval() {
		return 24;
	}

	@Override
	public boolean isProperlyConfigured() {
		return true;
	}

	@Override
	protected long getRefreshInterval() {
		return 50;
	}

	@Override
	protected String getName() {
		return "UDPBinding";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
	}



}