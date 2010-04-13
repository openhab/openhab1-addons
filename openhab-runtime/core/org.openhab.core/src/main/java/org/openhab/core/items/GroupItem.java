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

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class GroupItem extends GenericItem {
	
	protected List<Item> members;

	public GroupItem(String name) {
		super(name);
		members = new ArrayList<Item>();
	}

	public Item[] getMembers() {
		return members.toArray(new Item[members.size()]);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		for(Item item : members) {
			
		}
		return null;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
