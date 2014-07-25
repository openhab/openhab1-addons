/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.util;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;
import org.openhab.binding.astro.internal.model.Range;

/**
 * Common used DateTime functions.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class DateTimeUtils {
	/**
	 * Truncates the time from the calendar object.
	 */
	public static Calendar truncateToMidnight(Calendar calendar) {
		return DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Creates a Range object within the specified months and days. The start
	 * time is midnight, the end time is end of the day.
	 */
	public static Range getRange(int startMonth, int startDay, int endMonth, int endDay) {
		Calendar start = Calendar.getInstance();
		start.set(Calendar.MONTH, startMonth);
		start.set(Calendar.DAY_OF_MONTH, startMonth);
		start = truncateToMidnight(start);

		Calendar end = Calendar.getInstance();
		end.set(Calendar.MONTH, endMonth);
		end.set(Calendar.DAY_OF_MONTH, endDay);
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 999);

		return new Range(start, end);
	}

}
