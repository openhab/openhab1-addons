/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws.datatypes;

/**
 * <p>
 * Java class for WSDate complex type.
 * 
 */

public class WSDate {
	private int hours;
	private int minutes;
	private int seconds;
	private int year;
	private int day;
	private int monthWithJanuaryAsOne;

	public WSDate() {
	}

	public WSDate(int hours, int minutes, int seconds, int year, int day,
			int monthWithJanuaryAsOne) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.year = year;
		this.day = day;
		this.monthWithJanuaryAsOne = monthWithJanuaryAsOne;
	}

	/**
	 * Gets the hours value for this WSDate.
	 * 
	 * @return hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Sets the hours value for this WSDate.
	 * 
	 * @param hours
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * Gets the minutes value for this WSDate.
	 * 
	 * @return minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Sets the minutes value for this WSDate.
	 * 
	 * @param minutes
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * Gets the seconds value for this WSDate.
	 * 
	 * @return seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Sets the seconds value for this WSDate.
	 * 
	 * @param seconds
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * Gets the year value for this WSDate.
	 * 
	 * @return year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year value for this WSDate.
	 * 
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Gets the day value for this WSDate.
	 * 
	 * @return day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Sets the day value for this WSDate.
	 * 
	 * @param day
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * Gets the monthWithJanuaryAsOne value for this WSDate.
	 * 
	 * @return monthWithJanuaryAsOne
	 */
	public int getMonthWithJanuaryAsOne() {
		return monthWithJanuaryAsOne;
	}

	/**
	 * Sets the monthWithJanuaryAsOne value for this WSDate.
	 * 
	 * @param monthWithJanuaryAsOne
	 */
	public void setMonthWithJanuaryAsOne(int monthWithJanuaryAsOne) {
		this.monthWithJanuaryAsOne = monthWithJanuaryAsOne;
	}

}
