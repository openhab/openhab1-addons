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

import java.math.BigDecimal;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.PrimitiveType;

public class DecimalType implements PrimitiveType, State, Command {

	private BigDecimal value; 
	
	public DecimalType() {
		this.value = BigDecimal.ZERO;
	}
	
	public DecimalType(long value) {
		this.value = new BigDecimal(value);
	}

	public DecimalType(double value) {
		this.value = new BigDecimal(value);
	}

	public String toString() {
		return value.toPlainString();
	}

	public static DecimalType valueOf(String value) {
		return new DecimalType(Double.valueOf(value));
	}
}
