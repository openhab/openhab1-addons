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
package org.openhab.core.library.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;


public class DateTimeType implements PrimitiveType, State {
	
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	protected Calendar calendar;
	
	
	public DateTimeType() {
		this(Calendar.getInstance());
	}
	
	public DateTimeType(Calendar calendar) {
		this.calendar = calendar;
	}
	
	public DateTimeType(String calendarValue) {
		Date date = null;
		
		try {
			date = DATE_FORMATTER.parse(calendarValue);
		}
		catch (ParseException fpe) {
			throw new IllegalArgumentException(calendarValue + " is not in a valid format.", fpe);
		}
		
		if (date != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		}
	}
	
	
	public Calendar getCalendar() {
		return calendar;
	}
		
	public static DateTimeType valueOf(String value) {
		return new DateTimeType(value);
	}
	
	public String format(String pattern) {
		try {
			return String.format(pattern, calendar);
		} catch (NullPointerException npe) {
			return DATE_FORMATTER.format(calendar.getTime());
		}
	}
	
	public String format(Locale locale, String pattern) {
		return String.format(locale, pattern, calendar);
	}

	@Override
	public String toString() {
		return DATE_FORMATTER.format(calendar.getTime());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calendar == null) ? 0 : calendar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateTimeType other = (DateTimeType) obj;
		if (calendar == null) {
			if (other.calendar != null)
				return false;
		} else if (!calendar.equals(other.calendar))
			return false;
		return true;
	}
	

}
