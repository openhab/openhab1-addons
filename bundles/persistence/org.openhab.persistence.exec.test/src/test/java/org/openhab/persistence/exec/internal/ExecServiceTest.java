/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.exec.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.1.0
 */
public class ExecServiceTest {
	
	ExecService service;
	
	@Before
	public void init() {
		service= new ExecService();
	}

	@Test
	public void testFormatAlias() throws IOException {
		Date date = Calendar.getInstance().getTime();
		String dateFormatString = "%2$tY-%2$tm-%2$td %2$tT %2$ts : %1$s";
		String value = "testValue";
		String formattedDate = String.format(dateFormatString, value, date);
		
		String alias = "/Users/me/writeToFile.sh /Users/me/TempOut.txt " + dateFormatString;
		String expected = "/Users/me/writeToFile.sh /Users/me/TempOut.txt " + formattedDate;
		System.out.println("expected String: " + expected);
		
		// Method under Test
		String result = service.formatAlias(alias, value, date);
		
		// Expected results ...
		Assert.assertEquals(expected, result);
	}


}
