/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.config;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.alarm.config.AlarmCondition;
import org.openhab.binding.alarm.config.AlarmCondition.MatchingFunction;
import org.openhab.binding.alarm.internal.config.AlarmGenericBindingProvider.AlarmBindingConfig;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.AlarmState.AlarmClass;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * @author Volker Daube
 * @since 1.7.0
 */
public class AlarmGenericBindingProviderTest {

	private AlarmGenericBindingProvider provider;
	private Item item1;
	private ColorItem colorItem;
	private ContactItem contactItem;
	private DateTimeItem dateTimeItem;
	private DimmerItem dimmerItem;
	private NumberItem numberItem;
	private RollershutterItem rollershutterItem;
	private StringItem stringItem;
	private SwitchItem switchItem;

	@Before
	public void init() {
		provider = new AlarmGenericBindingProvider();
		item1 = new TestItem("item1");
		colorItem = new ColorItem("ColorItem");
		contactItem = new ContactItem("ContactItem");
		dateTimeItem = new DateTimeItem("DateTimeItem");
		dimmerItem = new DimmerItem("DimmerItem");
		numberItem = new NumberItem("NumberItem");
		rollershutterItem = new RollershutterItem("RollershutterItem");
		stringItem = new StringItem("StringItem");
		switchItem = new SwitchItem("SwitchItem");
	}	

	@Test(expected=BindingConfigParseException.class)
	public void testValidateItemTypeFail() throws BindingConfigParseException {
		provider.validateItemType(dateTimeItem, "");
		
	}

	@Test
	public void testValidateItemType() throws BindingConfigParseException {
		provider.validateItemType(colorItem, "");
		provider.validateItemType(contactItem, "");
		provider.validateItemType(dimmerItem, "");
		provider.validateItemType(numberItem, "");
		provider.validateItemType(rollershutterItem, "");
		provider.validateItemType(stringItem, "");
		provider.validateItemType(switchItem, "");
		
	}

	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfigStringNegative() throws BindingConfigParseException {
		provider.parseBindingConfigString(switchItem, null);
		provider.parseBindingConfigString(switchItem, "");
		provider.parseBindingConfigString(switchItem, "e");
		provider.parseBindingConfigString(switchItem, "ex");
		provider.parseBindingConfigString(switchItem, "ex()");
		provider.parseBindingConfigString(switchItem, "()");
		provider.parseBindingConfigString(switchItem, "ex(1)");
		provider.parseBindingConfigString(switchItem, "ex(1.1)");
		provider.parseBindingConfigString(switchItem, "ex('Hallo')");
		provider.parseBindingConfigString(switchItem, "eq()");
		provider.parseBindingConfigString(switchItem, "eq(Hallo)");
		provider.parseBindingConfigString(switchItem, " eq ( 1 ) , ");
		provider.parseBindingConfigString(switchItem,"=>(1,'Alarm')");
		provider.parseBindingConfigString(switchItem,"=<(1,'Alarm')");
		provider.parseBindingConfigString(switchItem,"><(1,'Alarm')");
		provider.parseBindingConfigString(switchItem,"<>(1,'Alarm')");
		provider.parseBindingConfigString(switchItem,"ge(1,1,'Alarm', class=xxx)");
		provider.parseBindingConfigString(switchItem,"eq('Hello','Alarm');eq('Hello','Alarm');xx('Hello','Alarm')");
		provider.parseBindingConfigString(switchItem,"eq(ON,'Alarm',)");
		provider.parseBindingConfigString(switchItem,"eq(OFF, delay=0)");
		provider.parseBindingConfigString(contactItem,"eq(off,'Alarm', delay=-200)");
		provider.parseBindingConfigString(contactItem,"eq(on,'Alarm', delay=-5, out=StringAlarmItem)");
		provider.parseBindingConfigString(contactItem,"stale('Alarm')");
		provider.parseBindingConfigString(contactItem,"STALE('Alarm', class=High, timeout=5, out=StringAlarmItem)");
		provider.parseBindingConfigString(switchItem,"eq(off,'Alarm', delay=-200)");
		provider.parseBindingConfigString(stringItem,"lt('Hello','Alarm')");
		provider.parseBindingConfigString(stringItem,"le('Hello','Alarm')");
		provider.parseBindingConfigString(stringItem,"gt('Hello','Alarm')");
		provider.parseBindingConfigString(stringItem,"ge('Hello','Alarm')");

		//ColorItem
		provider.parseBindingConfigString(colorItem,"eq((9,7,8),'Alarm')");

		//DateTimeItem
		provider.parseBindingConfigString(dateTimeItem,"eq(2000-01-01T09:00:00,'Alarm')");
	}


		@Test
		public void testParseBindingConfigString() throws BindingConfigParseException {

		provider.parseBindingConfigString(numberItem,"eq(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"==(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"Eq(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eQ(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"EQ(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eq(-1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eq(-1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eq(1,'Alarm');");
		provider.parseBindingConfigString(numberItem," eq ( 1 , 'Alarm' ) ");
		provider.parseBindingConfigString(numberItem," eq ( 1 , 'Alarm' ); ");
		provider.parseBindingConfigString(numberItem,"ne(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"!=(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"lt(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"<(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"le(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"<=(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"gt(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,">(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ge(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,">=(1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eq(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ne(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"lt(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"le(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"gt(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ge(1.1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"eq(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ne(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"lt(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"le(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"gt(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ge(1,1,'Alarm')");
		provider.parseBindingConfigString(numberItem,"ge(1,1,'Alarm', class=High)");
		provider.parseBindingConfigString(numberItem,"ge(1,1,'Alarm', class=MEDIUM)");
		provider.parseBindingConfigString(numberItem,"ge(1,1,'Alarm', class=low)");
		provider.parseBindingConfigString(stringItem,"eq('','Alarm')");
		provider.parseBindingConfigString(stringItem,"eq('Hello','Alarm')");
		provider.parseBindingConfigString(stringItem,"ne('Hello','Alarm')");
		provider.parseBindingConfigString(stringItem,"eq('üÜöÖäÄß[](){}!§$%&/=?,;.','üÜöÖäÄß[](){}!§$%&/=?,;.')");
		provider.parseBindingConfigString(stringItem,"eq('Hello', 'Alarm');eq('Hello','Alarm');");
		provider.parseBindingConfigString(stringItem,"eq('Hello','Alarm');eq('Hello','Alarm');eq('Hello','Alarm')");
		provider.parseBindingConfigString(numberItem,"ne(1, 'Alarm1')");

		//ContactItem
		provider.parseBindingConfigString(contactItem,"eq(OPEN,'Alarm')");
		provider.parseBindingConfigString(contactItem,"eq(CLOSED,'Alarm')");
		provider.parseBindingConfigString(contactItem,"eq(open,'Alarm')");
		provider.parseBindingConfigString(contactItem,"eq(closed,'Alarm')");

		provider.parseBindingConfigString(switchItem,"eq(ON,'Alarm')");
		provider.parseBindingConfigString(switchItem,"eq(OFF,'Alarm')");
		provider.parseBindingConfigString(switchItem,"eq(on,'Alarm')");
		provider.parseBindingConfigString(switchItem,"eq(off,'Alarm')");
		provider.parseBindingConfigString(switchItem,"eq(off,'Alarm', outitem = StringItem)");

		provider.parseBindingConfigString(switchItem,"eq(on,'Alarm', delay=0)");
		provider.parseBindingConfigString(switchItem,"eq(off,'Alarm', delay=200)");

		provider.parseBindingConfigString(switchItem,"eq(on,'Alarm', delay=0, outitem=StringAlarmItem)");
		provider.parseBindingConfigString(switchItem,"eq(on,'Alarm', class=High, delay=0, outitem=StringAlarmItem)");
		provider.parseBindingConfigString(switchItem,"eq(on,'Alarm', delay=0, class=High, outitem=StringAlarmItem)");
		provider.parseBindingConfigString(switchItem,"eq(off,'Alarm', delay=200)");
		provider.parseBindingConfigString(contactItem,"stale('Alarm',period=1)");
		provider.parseBindingConfigString(contactItem,"Stale('Alarm', period=1)");
		provider.parseBindingConfigString(contactItem,"STALE('Alarm', period=5, outitem=StringAlarmItem, class=High)");
		
		provider.parseBindingConfigString(switchItem,"eq(ON,'On Alarm');eq(OFF,'Off Alarm');stale('Stale Alarm', period=5)");
		provider.parseBindingConfigString(switchItem,"stale  ('Stale Alarm' , period=5)  ; eq  (  ON,  'On Alarm'  )  ; eq ( OFF , 'Off Alarm' ) ; ");
		provider.parseBindingConfigString(switchItem,"eq(OFF,'Off Alarm'); stale('Stale Alarm', period=5); eq(ON,'On Alarm'); ");

}

	@Test
	public void testParseBindingConfigStale() throws BindingConfigParseException {
		AlarmBindingConfig cfg=provider.parseBindingConfigString(
				switchItem, "stale('Message', period=5);stale('Message2', period=500, class=LoW, outitem = StringItem)");
		assertEquals(cfg.alarmConditions.size(),2);
		assertEquals(cfg.itemName, "SwitchItem");
		AlarmCondition ac=cfg.alarmConditions.get(0);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.STALE);
		assertNull(ac.getTriggerValue());
		assertEquals(ac.getAlarmText(),"Message");
		assertEquals(ac.getAlarmTimeInSeconds(),5);
		assertNull(ac.getMessageItemName());
		assertEquals(ac.getAlarmClass(), AlarmClass.HIGH);

		ac=cfg.alarmConditions.get(1);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.STALE);
		assertNull(ac.getTriggerValue());
		assertEquals(ac.getAlarmText(),"Message2");
		assertEquals(ac.getAlarmTimeInSeconds(),500);
		assertEquals(ac.getMessageItemName(), "StringItem");
		assertEquals(ac.getAlarmClass(), AlarmClass.LOW);
	}


	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfig_toManyArguments() throws BindingConfigParseException {
		provider.parseBindingConfigString(new TestItem(), "fq");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfigUnknownItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				item1, "lt(1); gt(2); ge(1); le(1); eq(1); ne(1)");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfigColorItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				colorItem, "eq(ON,'Message');ne(OFF,'Message2');lt(1.0,'Message3');gt((255,255,255), 'Message4')");
	}

	@Test
	public void testParseBindingConfigContactItem() throws BindingConfigParseException {
		AlarmBindingConfig cfg=provider.parseBindingConfigString(
				contactItem, "eq(OPEN,'Message', class=low ,outitem=StringItem);ne(CLOSED,'Message2')");
		assertEquals(cfg.alarmConditions.size(),2);
		assertEquals(cfg.itemName, "ContactItem");
		
		AlarmCondition ac=cfg.alarmConditions.get(0);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.EQ);
		assertEquals(ac.getTriggerValue(), OpenClosedType.OPEN);
		assertEquals(ac.getAlarmText(),"Message");
		assertEquals(ac.getAlarmTimeInSeconds(),0);
		assertEquals(ac.getMessageItemName(), "StringItem");
		assertEquals(ac.getAlarmClass(), AlarmClass.LOW);
		ac=cfg.alarmConditions.get(1);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.NE);
		assertEquals(ac.getTriggerValue(), OpenClosedType.CLOSED);
		assertEquals(ac.getAlarmText(),"Message2");
		assertEquals(ac.getAlarmTimeInSeconds(),0);
		assertNull(ac.getMessageItemName());
		assertEquals(ac.getAlarmClass(), AlarmClass.HIGH);
	}

	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfigDateTimeItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				dateTimeItem, "eq(2014-02-01T00:00:00,'Message');");
	}

	@Test
	public void testParseBindingConfigDimmerItem() throws BindingConfigParseException {
		AlarmBindingConfig cfg=provider.parseBindingConfigString(
				dimmerItem, "eq(ON,'Message1');eq(OFF,'Message2');gt(100.0,'Message3', outitem=String_Item);");
		
		assertEquals(3, cfg.alarmConditions.size());
		assertEquals("DimmerItem", cfg.itemName );
		
		AlarmCondition ac=cfg.alarmConditions.get(0);
		assertEquals(MatchingFunction.EQ, ac.getMatchingFunction());
		assertEquals(OnOffType.ON, ac.getTriggerValue());
		assertEquals("Message1", ac.getAlarmText());
		assertEquals(0, ac.getAlarmTimeInSeconds());
		assertNull(ac.getMessageItemName());
		
		ac=cfg.alarmConditions.get(1);
		assertEquals(MatchingFunction.EQ, ac.getMatchingFunction());
		assertEquals(OnOffType.OFF, ac.getTriggerValue());
		assertEquals("Message2", ac.getAlarmText());
		assertEquals(0, ac.getAlarmTimeInSeconds());
		assertNull(ac.getMessageItemName());

		ac=cfg.alarmConditions.get(2);
		assertEquals(MatchingFunction.GT, ac.getMatchingFunction());
		if (ac.getTriggerValue() instanceof PercentType) {
			PercentType p=(PercentType)ac.getTriggerValue();
			assertEquals(100.0, p.doubleValue());
		}
		else {
			fail("trigger value not of required type (PercentType)");
		}
		assertEquals("Message3", ac.getAlarmText());
		assertEquals(0, ac.getAlarmTimeInSeconds());
		assertEquals("String_Item", ac.getMessageItemName());
	}

	@Test
	public void testParseBindingConfigNumberItem() throws BindingConfigParseException {
		AlarmBindingConfig config=provider.parseBindingConfigString(
				numberItem, "eq(1,'Message1');eq(-100,'Message2');gt(-100039283083893283039283.0,'Message3');");
		config=provider.parseBindingConfigString(
				numberItem, "==(1,'Message', outitem=StringItem);!=(2,'Message2');>=(3,'Message3');>(4,'Message4');<=(10,'Message5');<(11,'Message6');");
		AlarmCondition ac = config.alarmConditions.get(0);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.EQ);
		if (ac.getTriggerValue() instanceof DecimalType) {
			DecimalType p=(DecimalType)ac.getTriggerValue();
			assertEquals(1, p.intValue());
		}
		else {
			fail("trigger value not of required type (PercentType)");
		}
		assertEquals("Message", ac.getAlarmText());
		assertEquals(0, ac.getAlarmTimeInSeconds());
		assertEquals("StringItem", ac.getMessageItemName());

		ac = config.alarmConditions.get(1);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.NE);
		ac = config.alarmConditions.get(2);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.GE);
		ac = config.alarmConditions.get(3);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.GT);
		ac = config.alarmConditions.get(4);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.LE);
		ac = config.alarmConditions.get(5);
		assertEquals(ac.getMatchingFunction(), MatchingFunction.LT);
	}

	@Test
	public void testParseBindingConfigRollershutterItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				rollershutterItem, "eq(UP,'Message1');eq(DOWN,'Message2');gt(100.0,'Message3');");
	}

	@Test
	public void testParseBindingConfigStringItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				stringItem, "eq('UP','Message1');eq('','Message2')");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfigSwitchItemFail() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				switchItem, "lt(1); gt(2); ge(1); le(1); eq(1); ne(1); <(1); >(2); >=(1); <=(1); ==(1); !=(1)");
	}

	@Test
	public void testParseBindingConfigSwitchItem() throws BindingConfigParseException {
		provider.parseBindingConfigString(
				switchItem, "eq(ON,'Message');ne(OFF,'Message2');");
	}

	@Test
	public void testIsFloat() {
		assertFalse("Empty String", provider.isFloat(""));
		assertFalse("Integer",provider.isFloat("1"));
		assertTrue("Float 0.1 failed", provider.isFloat("0.1"));
		assertFalse(".1", provider.isFloat(".1"));
		assertFalse("Mixed string and float", provider.isFloat("Hallo 2.0"));
		assertTrue("Float", provider.isFloat("0,1"));
		assertFalse(",1", provider.isFloat(",1"));
	}

	@Test
	public void testIsInt() {
		assertFalse("Empty String", provider.isInt(""));
		assertTrue("Integer",provider.isInt("1"));
		assertFalse("Float", provider.isInt("0.1"));
		assertFalse(".1", provider.isInt(".1"));
		assertFalse("Mixed string and int", provider.isInt("Hallo 2"));
	}
	@Test
	public void testIsBoolean() {
		assertFalse("Empty String", provider.isFloat(""));
		assertFalse("Integer",provider.isFloat("1"));
		assertTrue("Float", provider.isFloat("0.1"));
		assertFalse(".1", provider.isFloat(".1"));
		assertFalse("Mixed string and float", provider.isFloat("Hallo 2.0"));
	}


	private class TestItem extends GenericItem {

		private List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
		private List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

		public TestItem() {
			this("TestItem");
		}

		public TestItem(String itemName) {
			super(itemName);

			acceptedDataTypes.add(DateTimeType.class);
			acceptedDataTypes.add(DecimalType.class);
			acceptedDataTypes.add(HSBType.class);
			acceptedDataTypes.add(OnOffType.class);
			acceptedDataTypes.add(OpenClosedType.class);
			acceptedDataTypes.add(PercentType.class);
			acceptedDataTypes.add(StringType.class);
			acceptedDataTypes.add(UpDownType.class);

			acceptedCommandTypes.add(UpDownType.class);
			acceptedCommandTypes.add(StopMoveType.class);
			acceptedCommandTypes.add(PercentType.class);
			acceptedCommandTypes.add(StringType.class);
			acceptedCommandTypes.add(IncreaseDecreaseType.class);
		}

		public List<Class<? extends State>> getAcceptedDataTypes() {
			return acceptedDataTypes;
		}

		public List<Class<? extends Command>> getAcceptedCommandTypes() {
			return acceptedCommandTypes;
		}
	}

}
