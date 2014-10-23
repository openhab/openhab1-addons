/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.5.0
 */
@RunWith(Parameterized.class)
public class DateTimeTypeTest {
	
	/**
	 * parameter test set class.
	 * each instance of this class represents a test which executes the test once.
	 */
	public static class ParameterSet {
		/**
		 * the java default time zone to set.
		 * this should not change a result, except for wrong time zone informations,
		 * then this default time zone is used.
		 */
		public final TimeZone defaultTimeZone;
		/**
		 * input time.
		 * used to call the {@link Calendar#set(int, int, int, int, int, int)} method to set the time.
		 */
		public final Map<String, Integer> inputTimeMap;
		/**
		 * input time zone.
		 * used to call the {@link Calendar#setTimeZone(TimeZone)} to set the time zone.
		 * the time zone offset has direct impact on the result.
		 */
		public final TimeZone inputTimeZone;
		/**
		 * direct input of a time string (with or without time zone).
		 * @see {@link DateTimeType#valueOf(String)}
		 * if this is set, the {@link ParameterSet#inputTimeMap} and {@link ParameterSet#inputTimeZone} are ignored
		 */
		public final String inputTimeString;
		/**
		 * the expected result of the test.
		 * golden rule:
		 * should always return the input time minus or plus the offset of the given time zone.
		 * if no time zone is specified or the time zone in the {@link ParameterSet#inputTimeString} is wrong, then the {@link ParameterSet#defaultTimeZone} is used. 
		 */
		public final String expectedResult;

		/**
		 * create a parameter set with {@link ParameterSet#inputTimeMap} and {@link ParameterSet#inputTimeZone} parameters.
		 * @param defaultTimeZone
		 * @param inputTimeMap
		 * @param inputTimeZone
		 * @param expectedResult
		 */
		public ParameterSet(TimeZone defaultTimeZone,
				Map<String, Integer> inputTimeMap, TimeZone inputTimeZone,
				String expectedResult) {
			this.defaultTimeZone = defaultTimeZone;
			this.inputTimeMap = inputTimeMap;
			this.inputTimeZone = inputTimeZone;
			this.inputTimeString = null;
			this.expectedResult = expectedResult;
		}
		
		/**
		 * create a parameter set with {@link ParameterSet#inputTimeString} parameter.
		 * @param defaultTimeZone
		 * @param inputTimeString
		 * @param expectedResult
		 */
		public ParameterSet(TimeZone defaultTimeZone, String inputTimeString,
				String expectedResult) {
			this.defaultTimeZone = defaultTimeZone;
			this.inputTimeMap = null;
			this.inputTimeZone = null;
			this.inputTimeString = inputTimeString;
			this.expectedResult = expectedResult;
		}

	}

	/**
	 * Test parameter maps collection.
	 * 
	 * @return collection
	 */
	@Parameters
	public static Collection<Object[]> parameters() {
		// for simplicity we use always the same input time.
		Map<String, Integer> inputTimeMap = new HashMap<String,Integer>();
		inputTimeMap.put("year", 2014);
		inputTimeMap.put("month", 2);
		inputTimeMap.put("date", 30);
		inputTimeMap.put("hourOfDay", 4);
		inputTimeMap.put("minute", 58);
		inputTimeMap.put("second", 47);

		return Arrays.asList(new Object[][] {
			{ new ParameterSet(TimeZone.getTimeZone("UTC")  , inputTimeMap, TimeZone.getTimeZone("UTC")   , "2014-03-30T04:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("UTC")  , inputTimeMap, TimeZone.getTimeZone("CET")   , "2014-03-30T02:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("UTC")  , "2014-03-30T04:58:47UTS"                    , "2014-03-30T04:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("CET")  , inputTimeMap, TimeZone.getTimeZone("UTC")   , "2014-03-30T04:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("CET")  , inputTimeMap, TimeZone.getTimeZone("CET")   , "2014-03-30T02:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("CET")  , "2014-03-30T04:58:47UTS"                    , "2014-03-30T04:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("GMT+2"), inputTimeMap, TimeZone.getTimeZone("GMT+3") , "2014-03-30T01:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("GMT-2"), inputTimeMap, TimeZone.getTimeZone("GMT+3") , "2014-03-30T01:58:47") },
			{ new ParameterSet(TimeZone.getTimeZone("GMT-2"), inputTimeMap, TimeZone.getTimeZone("GMT-3") , "2014-03-30T07:58:47") },
		});
	}

	private ParameterSet parameterSet;

	/**
	 * setup Test class with current parameter map.
	 * 
	 * @param parameterMap
	 *            parameter map
	 */
	public DateTimeTypeTest(ParameterSet parameterSet) {
		this.parameterSet = parameterSet;
	}

	@After
	public void afterTest() {
		System.out.println("");
	}

	@Test
	public void createDate() {
		String inputTimeString;
		
		// set default time zone
		TimeZone.setDefault(parameterSet.defaultTimeZone);
		// get formated time string
		if (parameterSet.inputTimeString == null) {
			final Calendar calendar = Calendar.getInstance(parameterSet.inputTimeZone);
			calendar.set(parameterSet.inputTimeMap.get("year"), parameterSet.inputTimeMap.get("month"), parameterSet.inputTimeMap.get("date"), parameterSet.inputTimeMap.get("hourOfDay"), parameterSet.inputTimeMap.get("minute"), parameterSet.inputTimeMap.get("second"));
			inputTimeString = DateTimeType.DATE_FORMATTER.format(calendar.getTime());
		} else {
			inputTimeString = parameterSet.inputTimeString;
		}
		DateTimeType dt = DateTimeType.valueOf(inputTimeString);
		
		// create debug output to reproduce
		System.out.println("createDate (Default TimeZone: " + parameterSet.defaultTimeZone.getDisplayName(false, TimeZone.SHORT, Locale.ROOT) + "):");
		if (parameterSet.inputTimeZone == null) {
			System.out.println("\tInput: " + inputTimeString);
		} else {
			System.out.println("\tInput: " + inputTimeString+parameterSet.inputTimeZone.getDisplayName(false, TimeZone.SHORT, Locale.ROOT));
		}
		System.out.println("\tExpected: " + parameterSet.expectedResult);
		System.out.println("\tResult  : " + dt.toString());
		// Test
		assertEquals(parameterSet.expectedResult, dt.toString());
	}
}
