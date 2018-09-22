/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cardio2e DateTime structured data model
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eDateTime implements Cloneable, Serializable {
	private static final long serialVersionUID = 4310694960378727235L;
	public static final DateFormat CARDIO2E_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm");
	public static final byte CARDIO2E_DATE_TIME_LENGTH = 12;
	public static final short CARDIO2E_DATE_MIN_YEAR = 1995;
	public static final short CARDIO2E_DATE_MAX_YEAR = 2095;
	private static final Logger logger = LoggerFactory
			.getLogger(Cardio2eDateTime.class);
	private String dateTime;

	public Cardio2eDateTime() {// No args -> sets date and time from system
								// clock
		fromDate(new Date());
	}

	public Cardio2eDateTime(String dateTime) {
		fromString(dateTime);
	}

	public Cardio2eDateTime(Date dateTime) {
		fromDate(dateTime);
	}

	public Cardio2eDateTime(Calendar dateTime) {
		fromCalendar(dateTime);
	}

	public Cardio2eDateTime(int year, int month, int day, int hour, int minute) {
		fromIntValues(year, month, day, hour, minute);
	}

	public Cardio2eDateTime clone() {
		try {
			return (Cardio2eDateTime) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public Cardio2eDateTime deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Cardio2eDateTime) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public String toString() {
		return dateTime;
	}

	public Date toDate() {
		Date dateTime = null;
		try {
			dateTime = CARDIO2E_DATE_TIME_FORMAT.parse(this.dateTime);
		} catch (ParseException ex) {
			logger.warn("Unknown error in parsing to Date(): '{}'",
					ex.toString());
		}
		return dateTime;
	}

	public Calendar toCalendar() {
		Calendar dateTime = null;
		try {
			dateTime = Calendar.getInstance();
			dateTime.setTime((Date) CARDIO2E_DATE_TIME_FORMAT
					.parse(this.dateTime));
		} catch (ParseException ex) {
			logger.warn("Unknown error in parsing to Calendar(): '{}'",
					ex.toString());
		}
		return dateTime;
	}

	public int[] toIntArray() { // Returns a dateTime int array with year,
								// month, day, hour and day.
		return cardio2eDateTimeToInt(dateTime);
	}

	public void fromString(String dateTime) { // Sets date from a Cardio2e
												// string formatted
		if (isValidCardio2eDateTime(dateTime)) {
			this.dateTime = dateTime;
		} else {
			throw new IllegalArgumentException("invalid dateTime '" + dateTime
					+ "'");
		}
	}

	public void fromDate(Date dateTime) { // Sets date and time from a Date type
											// value
		fromString(CARDIO2E_DATE_TIME_FORMAT.format(dateTime));
	}

	public void fromCalendar(Calendar dateTime) { // Sets date and time from a
													// Calendar type value
		fromString(CARDIO2E_DATE_TIME_FORMAT.format(dateTime.getTime()));
	}

	public void fromIntValues(int year, int month, int day, int hour, int minute) { // Sets
																					// Cardio2e
																					// dateTime
																					// from
																					// year,
																					// month,
																					// day,
																					// hour
																					// and
																					// minute
																					// integer
																					// values
		fromString(String.format("%04d%02d%02d%02d%02d", year, month, day,
				hour, minute));
	}

	private boolean isValidCardio2eDateTime(String dateTime) { // Verifies if
																// dateTime
																// string is
																// valid for
																// Cardio2e
		boolean isValid = false;
		int dateTimeIntArray[] = cardio2eDateTimeToInt(dateTime);
		int year, month, day, hour, minute;
		Calendar myCalendar;
		year = dateTimeIntArray[0];
		month = dateTimeIntArray[1];
		day = dateTimeIntArray[2];
		hour = dateTimeIntArray[3];
		minute = dateTimeIntArray[4];
		if ((year >= CARDIO2E_DATE_MIN_YEAR)
				&& (year <= CARDIO2E_DATE_MAX_YEAR) && (month >= 1)
				&& (month <= 12) && (day >= 1) && (day <= 31) && (hour >= 0)
				&& (hour <= 23) && (minute >= 0) && (minute <= 59)) {
			// Checks day of the month
			try {
				myCalendar = new GregorianCalendar(year, (month - 1), 1);
				if (day <= myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					isValid = true;
				}
			} catch (Exception ex) {
				isValid = false;
			}
		}
		return isValid;
	}

	private int[] cardio2eDateTimeToInt(String dateTime) {// Parses Cardio2e
															// string dateTime
															// to int array with
															// year, month, day,
															// hour and day.
		int dateTimeArray[] = new int[5];
		if ((dateTime.length() == CARDIO2E_DATE_TIME_LENGTH)
				&& StringUtils.isNumeric(dateTime)) {
			dateTimeArray[0] = Integer.parseInt(dateTime.substring(0, 4));// year
			dateTimeArray[1] = Integer.parseInt(dateTime.substring(4, 6)); // month
			dateTimeArray[2] = Integer.parseInt(dateTime.substring(6, 8)); // day
			dateTimeArray[3] = Integer.parseInt(dateTime.substring(8, 10));// hour
			dateTimeArray[4] = Integer.parseInt(dateTime.substring(10)); // minute
		}
		return dateTimeArray;
	}
}
