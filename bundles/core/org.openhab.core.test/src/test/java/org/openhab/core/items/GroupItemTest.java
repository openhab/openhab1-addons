/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

package org.openhab.core.items;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;


/**
 * @author Thomas.Eichstaedt-Engelen
 */
public class GroupItemTest {
	
	private GroupItem rootGroupItem;
	
	@Before
	public void setup() {
		rootGroupItem = new GroupItem("root");
		rootGroupItem.addMember(new TestItem("member1"));
		rootGroupItem.addMember(new TestItem("member2"));
		GroupItem subGroup = new GroupItem("subGroup1");
		subGroup.addMember(new TestItem("subGroup member 1"));
		subGroup.addMember(new TestItem("subGroup member 2"));
		subGroup.addMember(new TestItem("subGroup member 3"));
		rootGroupItem.addMember(subGroup);
	}
	

	@Test
	public void testGetAllMembers() {
		Assert.assertEquals(5, rootGroupItem.getAllMembers().length);
		for (Item member : rootGroupItem.getAllMembers()) {
			if (member instanceof GroupItem) {
				fail("There are no GroupItems allowed in this Collection");
			}
		}
	}
	
	
	class TestItem extends GenericItem {

		public TestItem(String name) {
			super(name);
		}

		@Override
		public List<Class<? extends State>> getAcceptedDataTypes() {
			return null;
		}

		@Override
		public List<Class<? extends Command>> getAcceptedCommandTypes() {
			return null;
		}
		
	}
	

}
