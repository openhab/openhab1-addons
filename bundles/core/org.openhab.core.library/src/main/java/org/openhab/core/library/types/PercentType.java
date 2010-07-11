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

/** 
 * The PercentType extends the {@link DecimalType} by putting constraints for its value on top (0-100).
 * 
 * @author Kai Kreuzer
 *
 */
public class PercentType extends DecimalType {
	
	public PercentType() {
		super();
	}
	
	public PercentType(int value) {
		super(value);
		if(value<0 || value>100) {
			throw new IllegalArgumentException("Value must be between 0 and 100");
		}
	}
}
