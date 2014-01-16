/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
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
	public void testOrFunction_differntTypes() {
		DimmerItem dimmer1 = new DimmerItem("TestDimmer1");
		dimmer1.setState(new DecimalType("42"));
		DimmerItem dimmer2 = new DimmerItem("TestDimmer2");
		dimmer2.setState(new DecimalType("0"));
		SwitchItem switch1 = new SwitchItem("TestSwitch1");
		switch1.setState(OnOffType.ON);
		SwitchItem switch2 = new SwitchItem("TestSwitch2");
		switch2.setState(OnOffType.OFF);
		
		items.add(dimmer1);
		items.add(dimmer2);
		items.add(switch1);
		items.add(switch2);
		
		function = new ArithmeticGroupFunction.Or(OnOffType.ON, OnOffType.OFF);
		State state = function.calculate(items);
		State decimalState = function.getStateAs(items, DecimalType.class);
		
		Assert.assertEquals(OnOffType.ON, state);
		Assert.assertEquals(new DecimalType("2"), decimalState);
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
	
	@Test
	public void testSumFunction() {
		items.add(new TestItem("TestItem1", new DecimalType("23.54")));
		items.add(new TestItem("TestItem2", UnDefType.NULL));
		items.add(new TestItem("TestItem3", new DecimalType("89")));
		items.add(new TestItem("TestItem4", UnDefType.UNDEF));
		items.add(new TestItem("TestItem5", new DecimalType("122.41")));
		
		function = new ArithmeticGroupFunction.Sum();
		State state = function.calculate(items);
		
		Assert.assertEquals(new DecimalType("234.95"), state);
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
