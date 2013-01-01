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
package org.openhab.binding.ihc.utcs;

/**
 * <p>
 * Java class for WSDate complex type.
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
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
