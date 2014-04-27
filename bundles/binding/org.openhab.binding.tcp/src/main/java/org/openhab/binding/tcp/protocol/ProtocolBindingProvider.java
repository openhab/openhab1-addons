/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tcp.protocol;

import java.util.List;

import org.openhab.binding.tcp.ChannelBindingProvider;
import org.openhab.binding.tcp.Direction;
import org.openhab.core.types.Command;

/**
 * Further specialisation of the ChannelBindingProvider interface that serves
 * as base for the "default" ASCII based TCP and UDP network bindings
 * 
 * In all of the defined methods below, "ProtocolCommand" is the 'command' to be
 * executed by the protocol, which is mapped on a openHAB Command through the  binding
 * configuration. See TCP and UDP implementations. In a lot of protocols the ProtocolCommand
 * will just be a (data)string that is sent "as is" to the remote-end
 * 
 * 
 * @author Karel Goderis
 * @since 1.1.0
 *
 **/

public interface ProtocolBindingProvider extends ChannelBindingProvider {

	/* Get the direction of the binding configuration associated with the combination of the given Item and Command */
	public Direction getDirection(String itemName, Command command);

	/* Get the command, as used or known by the protocol, of the binding configuration associated with the combination of the given Item and Command */
	public String getProtocolCommand(String itemName, Command command);

	/* Get a list of the openHAB Commands associated with the combination of the given Item and protocol command */
	public List<String> getItemNames(String protocolCommand);

	/* Get a list of the openHAB Commands associated with the combingation of the given Item and protocol command */
	public List<Command> getAllCommands(String itemName, String protocolCommand);

	/* Get a list of the openHAB Commands associated with the given Item  */
	public List<Command> getAllCommands(String itemName);

}
