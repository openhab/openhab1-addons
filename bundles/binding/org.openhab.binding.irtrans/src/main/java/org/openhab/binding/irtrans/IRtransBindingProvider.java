/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.irtrans;

import java.util.List;

import org.openhab.binding.irtrans.internal.Leds;
import org.openhab.binding.tcp.ChannelBindingProvider;
import org.openhab.core.types.Command;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and IRtrans items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Karel Goderis
 * @since 1.4.0
 *
 */
public interface IRtransBindingProvider extends ChannelBindingProvider {

	//Get the LED corresponding to the given Item and Command
	public Leds getLed(String itemName, Command command);

	//Get the name of the remote/manufacturer corresponding to the given Item and Command
	public String getRemote(String itemName, Command command);

	//Get the name of the infrared command corresponding to the given Item and Command
	public String getIrCommand(String itemName, Command command);

	//Get the list of items that accept the corresponding manufacturer/remote's infrared commands
	public List<String> getItemNames(String remote, String irCommand);

	//Get the list of Commands that matched a given Item and manufacturer/remote's and infrared commands
	public List<Command> getAllCommands(String itemName, String remote, String name);

}
