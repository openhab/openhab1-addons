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
package org.openhab.core.library.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UnDefType;
import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;

public class SwitchItem extends GenericItem {
	
	private static List<Class<? extends DataType>> acceptedDataTypes = new ArrayList<Class<? extends DataType>>();
	private static List<Class<? extends CommandType>> acceptedCommandTypes = new ArrayList<Class<? extends CommandType>>();

	static {
		acceptedDataTypes.add(OnOffType.class);
		acceptedDataTypes.add(UnDefType.class);

		acceptedCommandTypes.add(OnOffType.class);
	}
	
	public SwitchItem(String name) {
		super(name);
	}

	public void send(OnOffType command) {
		internalSend(command);
	}

	public List<Class<? extends DataType>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends CommandType>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
}
