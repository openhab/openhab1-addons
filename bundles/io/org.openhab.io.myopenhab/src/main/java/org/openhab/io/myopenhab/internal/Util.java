/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.myopenhab.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** 
 * This class provides utility functions for org.openhab.io.myopenhab bundle
 * 
 * @author Victor Belov
 * @since 1.3.0
 *
 */


public class Util {
	public static String dateToISODate(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		dateFormat.setTimeZone(tz);
		return dateFormat.format(date);
	}
	
	public static String longToISODate(long longDate) {
		Date date = new Date(longDate);
		return dateToISODate(date);
	}
	
	public static Date dateFromISODate(String isoDate) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		dateFormat.setTimeZone(tz);
		try {
			return dateFormat.parse(isoDate);
		} catch (ParseException e) {
			return null;
		}
	}
}
