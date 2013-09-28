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
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

/**
*  MAX!Cube wall mounted thermostat. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public class WallMountedThermostat extends Device {

	public WallMountedThermostat(Configuration c) {
		super(c);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.maxcube.internal.message.Device#getType()
	 */
	@Override
	public DeviceType getType() {
		return DeviceType.WallMountedThermostat;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.maxcube.internal.message.Device#getRFAddress()
	 */
	@Override
	public String getRFAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.maxcube.internal.message.Device#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.maxcube.internal.message.Device#getLastUpdate()
	 */
	@Override
	public Calendar getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

}
