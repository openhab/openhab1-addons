/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupItem extends GenericItem {
	
	private static final Logger logger = LoggerFactory.getLogger(GroupItem.class);
	
	protected final List<Item> members;

	public GroupItem(String name) {
		super(name);
		members = new ArrayList<Item>();
	}

	public Item[] getMembers() {
		return members.toArray(new Item[members.size()]);
	}

	public void addMember(Item item) {
		members.add(item);
	}
	
	public void removeMember(Item item) {
		members.remove(item);
	}
	
	/** 
	 * The accepted data types of a group item is the intersection of all
	 * sets of accepted data types of all its members.
	 * 
	 * @return the accepted data types of this group item
	 */
	@SuppressWarnings("unchecked")
	public List<Class<? extends State>> getAcceptedDataTypes() {
		List<Class<? extends State>> acceptedDataTypes = null;
		
		for(Item item : members) {
			if(acceptedDataTypes==null) {
				acceptedDataTypes = item.getAcceptedDataTypes();
			} else {
				acceptedDataTypes = ListUtils.intersection(acceptedDataTypes, item.getAcceptedDataTypes());
			}
		}
		return acceptedDataTypes == null ? ListUtils.EMPTY_LIST : acceptedDataTypes;
	}

	/** 
	 * The accepted command types of a group item is the intersection of all
	 * sets of accepted command types of all its members.
	 * 
	 * @return the accepted command types of this group item
	 */
	@SuppressWarnings("unchecked")
	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		List<Class<? extends Command>> acceptedCommandTypes = null;
		
		for(Item item : members) {
			if(acceptedCommandTypes==null) {
				acceptedCommandTypes = item.getAcceptedCommandTypes();
			} else {
				acceptedCommandTypes = ListUtils.intersection(acceptedCommandTypes, item.getAcceptedCommandTypes());
			}
		}
		return acceptedCommandTypes == null ? ListUtils.EMPTY_LIST : acceptedCommandTypes;
	}
	
	public void send(Command command) {
		if(getAcceptedCommandTypes().contains(command.getClass())) {
			internalSend(command);
		} else {
			logger.warn("Command '{}' has been ignored for group '{}' as it is not accepted.", command.toString(), getName());
		}
	}
	
	@Override
	protected void internalSend(Command command) {
		if(eventPublisher!=null) {
			for(Item member : members) {
				// try to send the command to the bus
				eventPublisher.sendCommand(member.getName(), command);
			}		
		}
	}
	
	@Override
	public String toString() {
		return getName() + " (" +
		"Type=" + getClass().getSimpleName() + ", " +
		"Members=" + members.size() + ", " +
		"State=" + getState() + ")";
	}
}
