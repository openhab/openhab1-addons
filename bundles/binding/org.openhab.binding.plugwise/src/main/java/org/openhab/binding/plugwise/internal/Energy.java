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
package org.openhab.binding.plugwise.internal;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A simple class to represent energy usage, converting back and forward between Plugwise date representations
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class Energy {
			
	private DateTime time;
	private long pulses;
	private int interval;
	
	public Energy(String logdate, long l, int interval) {
				
		if(logdate.length() == 8) {

			if(!logdate.equals("FFFFFFFF")) {

				int year = 0;
				int month = 0;
				long minutes = 0;

				year = Integer.parseInt(StringUtils.left(logdate, 2), 16) + 2000;
				month = Integer.parseInt(StringUtils.mid(logdate, 2, 2), 16);
				minutes = Long.parseLong(StringUtils.right(logdate, 4), 16);

				time = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).plusMinutes((int) minutes).toDateTime(DateTimeZone.getDefault()).minusHours(1);
	
			} else {
				time = DateTime.now();
				this.interval = interval;
				this.pulses = 0;
			}
						
		} else {
			time = DateTime.now();
		}
		
		this.interval = interval;
		this.pulses = l;
		
	}
	
	public Energy(DateTime logdate, long pulses, int interval) {
		time  = logdate;
		this.interval = interval;
		this.pulses = pulses;
	}
	
	public String toString() {
		return time.toString() + "-" + Integer.toString(interval) + "-" + Long.toString(pulses);
	}

	public DateTime getTime() {
		return time;
	}

	public long getPulses() {
		return pulses;
	}

	public int getInterval() {
		return interval;
	}
		

}
