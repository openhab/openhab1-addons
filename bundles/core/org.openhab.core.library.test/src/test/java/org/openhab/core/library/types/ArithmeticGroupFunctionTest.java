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
package org.openhab.core.library.types;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupFunction;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
public class ArithmeticGroupFunctionTest {
	
	private GroupFunction function;
	private List<Item> items;
	
	
	@Before
	public void init() {
		items = new ArrayList<Item>();
	}
	

	@Test
	public void testOrFunction() {
		items.add(new TestItem("TestItem1", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem2", UnDefType.UNDEF));
		items.add(new TestItem("TestItem3", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem4", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem5", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.Or(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.OPEN, state);
	}

	@Test
	public void testOrFunction_negative() {
		items.add(new TestItem("TestItem1", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem2", UnDefType.UNDEF));
		items.add(new TestItem("TestItem3", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem4", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem5", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.Or(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}
	
	@Test
	public void testOrFunction_justsOneItem() {
		items.add(new TestItem("TestItem1", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.Or(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}
	
	@Test
	public void testNOrFunction() {
		items.add(new TestItem("TestItem1", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem2", UnDefType.UNDEF));
		items.add(new TestItem("TestItem3", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem4", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem5", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.NOr(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}
	
	@Test
	public void testNOrFunction_negative() {
		items.add(new TestItem("TestItem1", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem2", UnDefType.UNDEF));
		items.add(new TestItem("TestItem3", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem4", OpenClosedType.CLOSED));
		items.add(new TestItem("TestItem5", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.NOr(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.OPEN, state);
	}
	
	@Test
	public void testAndFunction() {
		items.add(new TestItem("TestItem1", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem2", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem3", OpenClosedType.OPEN));
		
		function = new ArithmeticGroupFunction.And(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.OPEN, state);
	}

	@Test
	public void testAndFunction_negative() {
		items.add(new TestItem("TestItem1", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem2", UnDefType.UNDEF));
		items.add(new TestItem("TestItem3", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem4", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem5", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.And(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}

	@Test
	public void testAndFunction_justsOneItem() {
		items.add(new TestItem("TestItem1", UnDefType.UNDEF));
		
		function = new ArithmeticGroupFunction.And(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}
	
	@Test
	public void testNAndFunction() {
		items.add(new TestItem("TestItem1", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem2", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem3", OpenClosedType.OPEN));
		
		function = new ArithmeticGroupFunction.NAnd(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.CLOSED, state);
	}

	@Test
	public void testNAndFunction_negative() {
		items.add(new TestItem("TestItem1", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem2", OpenClosedType.OPEN));
		items.add(new TestItem("TestItem3", OpenClosedType.CLOSED));
		
		function = new ArithmeticGroupFunction.NAnd(OpenClosedType.OPEN, OpenClosedType.CLOSED);
		State state = function.calculate(items);
		
		Assert.assertEquals(OpenClosedType.OPEN, state);
	}
	
	
	class TestItem extends GenericItem {

		public TestItem(String name, State state) {
			super(name);
			setState(state);
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
