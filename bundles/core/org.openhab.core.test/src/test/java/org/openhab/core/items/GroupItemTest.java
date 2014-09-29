/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		rootGroupItem.addMember(new TestItem("member2"));
		GroupItem subGroup = new GroupItem("subGroup1");
		subGroup.addMember(new TestItem("subGroup member 1"));
		subGroup.addMember(new TestItem("subGroup member 2"));
		subGroup.addMember(new TestItem("subGroup member 3"));
		subGroup.addMember(new TestItem("member1"));
		rootGroupItem.addMember(subGroup);
	}
	

	@Test
	public void testGetAllMembers() {
		int expectedAmountOfMembers = 5;
		Assert.assertEquals(expectedAmountOfMembers, rootGroupItem.getAllMembers().size());
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
