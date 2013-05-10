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
package org.openhab.binding.urtsi.internal

import org.openhab.core.library.types.StopMoveType
import org.openhab.core.library.types.UpDownType
import org.openhab.core.types.Command
import org.openhab.core.types.State
import org.openhab.core.types.Type
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.openhab.binding.urtsi.internal.UrtsiBinding.*
import static org.openhab.core.library.types.StopMoveType.*
import static org.openhab.core.library.types.UpDownType.*
import org.openhab.core.binding.AbstractBinding
import org.openhab.binding.urtsi.UrtsiBindingProvider

/**
 * Main implementation of the Somdy URTSI II Binding. This binding is responsible for delegating the received commands and updates to the {@link UrtsiDevice}.
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
class UrtsiBinding extends AbstractBinding<UrtsiBindingProvider> {

	static val Logger logger = LoggerFactory::getLogger(typeof(UrtsiBinding))
	
	static val String COMMAND_UP = "U"
	static val String COMMAND_DOWN = "D"
	static val String COMMAND_STOP = "S"	
	
	/**
	 * The method delegates the received command to the URTSI device and updates the item's state, if the command has been executed successfully.
	 */
	override protected void internalReceiveCommand(String itemName, Command command) {
		if (logger.debugEnabled) {
			logger.debug("Received command for " + itemName + "! Command: " + command)
		}
		val executedSuccessfully = sendToUrtsi(itemName, command)
		switch command {
			State case executedSuccessfully : {
				eventPublisher.postUpdate(itemName, command)
			} 
		}
	}
	
	/**
	 * The method delegates the received state-update to the URTSI device.
	 */
	override protected void internalReceiveUpdate(String itemName, State newState) {
		if (logger.debugEnabled) {
			logger.debug("Received update for " + itemName + "! New state: " + newState)
		}
		sendToUrtsi(itemName, newState)
	}

	/**
	 * The method determines the appropriate {@link org.openhab.core.binding.BindingProvider} and uses it to get the corresponding URTSI device and channel.
	 * Bases on the given type a command is send to the device.
	 * @param itemName name of the item
	 * @param type Type of the command or status update
	 * @return Returns true, if the command has been executed successfully. Returns false otherwise.
	 */
	def private boolean sendToUrtsi(String itemName, Type type) {
		val provider = this.providers.head
		if (provider == null) {
			if (logger.traceEnabled) {
				logger.trace("doesn't find matching binding provider [itemName={}, type={}]", itemName, type)
			}
			return false
		}
		val urtsiDevice = provider.getDevice(itemName)
		val channel = provider.getChannel(itemName)
		
		if (urtsiDevice != null && channel != null) {
			if (logger.debugEnabled) {
				logger.debug("Send to URTSI for item: " + itemName + "; Type: " + type)
			}
			val actionKey= 
				switch type {
					UpDownType case UP : COMMAND_UP
					UpDownType case DOWN : COMMAND_DOWN
					StopMoveType case STOP : COMMAND_STOP
				}
			if (logger.debugEnabled) {
				logger.debug("Action key: " + actionKey)
			}
			if (actionKey != null) {
				val channelString = String::format("%02d", channel)
				val command = "01" + channelString + actionKey
				val executedSuccessfully = urtsiDevice.writeString(command)
				if (!executedSuccessfully) {
					if (logger.errorEnabled) {
						logger.error("Command has not been processed [itemName={}, command={}]", itemName, command)
					}
				}
				return executedSuccessfully
			}
		}
		false
	}


	
}