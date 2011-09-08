/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.io.gcal.internal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ExecuteCommandJobTest {
	
	ExecuteCommandJob commandJob;
	
	@Before
	public void init() {
		commandJob = new ExecuteCommandJob();
	}

	@Test
	public void testParseCommand() {
		String[] content;
		
		content = commandJob.parseCommand("send ItemName value");
		Assert.assertEquals("send", content[0]);
		Assert.assertEquals("ItemName", content[1]);
		Assert.assertEquals("value", content[2]);
		
		content = commandJob.parseCommand("send ItemName \"value value\"");
		Assert.assertEquals("send", content[0]);
		Assert.assertEquals("ItemName", content[1]);
		Assert.assertEquals("value value", content[2]);
		
		content = commandJob.parseCommand("send ItemName \"125\"");
		Assert.assertEquals("send", content[0]);
		Assert.assertEquals("ItemName", content[1]);
		Assert.assertEquals("125", content[2]);
		
		content = commandJob.parseCommand("send ItemName 125");
		Assert.assertEquals("send", content[0]);
		Assert.assertEquals("ItemName", content[1]);
		Assert.assertEquals("125.0", content[2]);
	}

}
