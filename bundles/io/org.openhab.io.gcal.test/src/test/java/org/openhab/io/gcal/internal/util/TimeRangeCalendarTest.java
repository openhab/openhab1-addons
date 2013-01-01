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
package org.openhab.io.gcal.internal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.math.LongRange;
import org.junit.Before;
import org.junit.Test;
import org.openhab.io.gcal.internal.util.TimeRangeCalendar;


/**
 * @author Thomas.Eichstaedt-Engelen
 */
public class TimeRangeCalendarTest {
	
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
	
	TimeRangeCalendar calendar;
	
	@Before
	public void init() {
		calendar = new TimeRangeCalendar();
	}

	@Test
	public void testIsTimeIncluded() throws ParseException {
		Date startTime = DATE_FORMATTER.parse("04.04.2012 16:00:00:000");
		Date endTime = DATE_FORMATTER.parse("04.04.2012 19:15:00:000");
		
		Date included = DATE_FORMATTER.parse("04.04.2012 19:00:00:000");
		Date excluded_before = DATE_FORMATTER.parse("04.04.2012 15:59:59:999");
		Date excluded_after = DATE_FORMATTER.parse("04.04.2012 19:15:00:001");
		
		LongRange timeRange = new LongRange(startTime.getTime(), endTime.getTime());
		calendar.addTimeRange(timeRange);
		
		Assert.assertEquals(true, calendar.isTimeIncluded(included.getTime()));
		Assert.assertEquals(false, calendar.isTimeIncluded(excluded_before.getTime()));
		Assert.assertEquals(false, calendar.isTimeIncluded(excluded_after.getTime()));
	}

	@Test
	public void testGetNextIncludedTime() throws ParseException {
		Date startTime = DATE_FORMATTER.parse("04.04.2012 16:00:00:000");
		Date endTime = DATE_FORMATTER.parse("04.04.2012 19:15:00:000");
		
		Date included = DATE_FORMATTER.parse("04.04.2012 17:23:21:000");
		Date expected = DATE_FORMATTER.parse("04.04.2012 19:15:00:001");
		
		LongRange timeRange = new LongRange(startTime.getTime(), endTime.getTime());
		calendar.addTimeRange(timeRange);
		
		Assert.assertEquals(expected.getTime(), calendar.getNextIncludedTime(included.getTime()));
	}

	@Test
	public void testAddRemoveExcludedDate() throws ParseException {
		Date startTime = DATE_FORMATTER.parse("04.04.2012 16:00:00:000");
		Date endTime = DATE_FORMATTER.parse("04.04.2012 19:15:00:000");
		
		LongRange timeRange_1 = new LongRange(startTime.getTime(), endTime.getTime());
		LongRange timeRange_2 = new LongRange(startTime.getTime(), endTime.getTime());
		
		calendar.addTimeRange(timeRange_2);
		calendar.addTimeRange(timeRange_1);
		
		Assert.assertEquals(2, calendar.getExcludedRanges().size());
		Assert.assertEquals(timeRange_1, calendar.getExcludedRanges().get(1));
		Assert.assertEquals(timeRange_2, calendar.getExcludedRanges().get(0));
		
		calendar.removeExcludedDate(timeRange_1);
		Assert.assertEquals(1, calendar.getExcludedRanges().size());
		Assert.assertEquals(timeRange_2, calendar.getExcludedRanges().get(0));
	}
	
}
