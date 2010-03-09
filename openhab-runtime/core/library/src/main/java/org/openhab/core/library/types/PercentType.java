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
package org.openhab.core.library.types;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;
import org.openhab.core.types.PrimitiveType;

public class PercentType implements PrimitiveType, DataType, CommandType {

	private int value; 
	
	public PercentType() {
		this.value = 0;
	}
	
	public PercentType(int value) {
		if(value<0 || value>100) {
			throw new IllegalArgumentException("Value must be between 0 and 100");
		}
		this.value = value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

	public static PercentType valueOf(String value) {
		return new PercentType(Integer.valueOf(value));
	}
}
