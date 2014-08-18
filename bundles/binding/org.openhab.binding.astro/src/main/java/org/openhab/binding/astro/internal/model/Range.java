/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.model;

import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Range class which holds a start and a end calendar object.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Range {
	private Calendar start;
	private Calendar end;

	public Range() {
	}

	public Range(Calendar start, Calendar end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the start of the range.
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * Returns the end of the range.
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * Returns the duration in minutes.
	 */
	public long getDuration() {
		if (start == null || end == null) {
			return -1;
		}
		if (start.after(end)) {
			return 0;
		}
		long diff = end.getTimeInMillis() - start.getTimeInMillis();
		return diff / 60000;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("start", DateTimeUtils.getDate(start)).append("end", DateTimeUtils.getDate(end)).toString();
	}
}
