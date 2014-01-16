/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.knx.internal.config.KNXGenericBindingProvider.KNXBindingConfig;
import org.openhab.binding.knx.internal.config.KNXGenericBindingProvider.KNXBindingConfigItem;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.exception.KNXFormatException;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class KNXGenericBindingProviderTest {
	
	private KNXGenericBindingProvider provider;
	private Item item1;
	private Item item2;
	
	@Before
	public void init() {
		provider = new KNXGenericBindingProvider();
		item1 = new TestItem("item1");
		item2 = new TestItem("item2");
	}	
	
	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfig_toManyArguments() throws BindingConfigParseException {
		provider.parseBindingConfigString(new TestItem(), "0/0/0, 0/0/0, 0/0/0, 0/0/0, 0/0/0");
	}

	@Test
	public void testParseBindingConfig() throws BindingConfigParseException, KNXFormatException {
		
		// method under Test
		KNXBindingConfig bindingConfigs = provider.parseBindingConfigString(
			item1, "<4/2/10+0/2/10, 5.006:4/2/11+0/2/11, +4/2/12, 4/2/13");
		
		// Assertions
		assertEquals(4, bindingConfigs.size());
		
		for (KNXBindingConfigItem bindingConfig : bindingConfigs) {
			assertEquals("item1", bindingConfig.itemName);
		}
		
		
		assertNotNull(bindingConfigs.get(0).readableDataPoint);
		assertNull(bindingConfigs.get(1).readableDataPoint);
		assertNull(bindingConfigs.get(2).readableDataPoint);
		assertNull(bindingConfigs.get(3).readableDataPoint);
		
		assertTrue(bindingConfigs.get(0).allDataPoints.contains(new GroupAddress("4/2/10")));
		assertTrue(bindingConfigs.get(0).allDataPoints.contains(new GroupAddress("0/2/10")));
		assertTrue(bindingConfigs.get(1).allDataPoints.contains(new GroupAddress("4/2/11")));
		assertTrue(bindingConfigs.get(1).allDataPoints.contains(new GroupAddress("0/2/11")));
		assertTrue(bindingConfigs.get(2).allDataPoints.contains(new GroupAddress("4/2/12")));
		assertTrue(bindingConfigs.get(3).allDataPoints.contains(new GroupAddress("4/2/13")));
		
		assertEquals(true, bindingConfigs.get(0).mainDataPoint instanceof CommandDP);
		assertEquals(true, bindingConfigs.get(1).mainDataPoint instanceof CommandDP);
		assertEquals(true, bindingConfigs.get(2).mainDataPoint instanceof StateDP);
		assertEquals(true, bindingConfigs.get(3).mainDataPoint instanceof CommandDP);
	}

	@Test
	public void testIsCommandGA() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, 
				"<4/2/10+0/2/10, 5.006:4/2/11+0/2/11, +4/2/12, 4/2/13");

		// method under Test
		assertEquals(true, provider.isCommandGA(new GroupAddress("4/2/10")));
		assertEquals(true, provider.isCommandGA(new GroupAddress("4/2/11")));
		assertEquals(true, provider.isCommandGA(new GroupAddress("4/2/13")));

		assertEquals(false, provider.isCommandGA(new GroupAddress("0/2/10")));
		assertEquals(false, provider.isCommandGA(new GroupAddress("0/2/11")));
		assertEquals(false, provider.isCommandGA(new GroupAddress("4/2/12")));
	}

	@Test
	public void testReadFlagWithDPT() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, "<5.001:4/2/10");

		// method under Test
		Iterator<Datapoint> readableDatapoints = provider.getReadableDatapoints().iterator();
		assertEquals(true, readableDatapoints.hasNext());
		assertEquals(true, readableDatapoints.next().getDPT().equals("5.001"));
	}

	@Test
	public void testReadFromThirdGA() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, "2/1/5+2/4/5, 2/2/5, <0/3/5");

		// method under Test
		Iterator<Datapoint> readableDatapoints = provider.getReadableDatapoints().iterator();
		assertEquals(true, readableDatapoints.hasNext());
		assertEquals(0, readableDatapoints.next().getMainAddress().getMainGroup());
	}
	
	@Test
	public void testReadFromListeningGA() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, "2/1/5+<0/4/5");

		// method under Test
		Iterator<Datapoint> readableDatapoints = provider.getReadableDatapoints().iterator();
		assertEquals(true, readableDatapoints.hasNext());
		assertEquals(0, readableDatapoints.next().getMainAddress().getMainGroup());
	}

	@Test
	public void testAutoUpdate() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, 
				"<4/2/10+0/2/10, 5.006:4/2/11+0/2/11, +4/2/12, 4/2/13");
		provider.processBindingConfiguration("text", item2, 
				"<4/2/10, 5.006:4/2/11,, 4/2/13");

		// method under Test
		assertEquals(Boolean.FALSE, provider.autoUpdate(item1.getName()));
		assertEquals(null, provider.autoUpdate(item2.getName()));
	}

	@Test
	public void testProvidesBindingFor() throws BindingConfigParseException, KNXFormatException {
		
		provider.processBindingConfiguration("text", item1, 
				"<4/2/10+0/2/10, 5.006:4/2/11+0/2/11, +4/2/12, 4/2/13");
		provider.processBindingConfiguration("text", item2, 
				"<4/2/10, 5.006:4/2/11,, 4/2/13");

		// method under Test
		assertEquals(true, provider.providesBindingFor(item1.getName()));
		assertEquals(true, provider.providesBindingFor(item1.getName()));
		assertEquals(false, provider.providesBindingFor("someotheritem"));
	}


	private class TestItem extends GenericItem {

		private List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
		private List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();
		
		public TestItem() {
			this("TestItem");
		}
		
		public TestItem(String itemName) {
			super(itemName);
			
			acceptedDataTypes.add(UnDefType.class);
			acceptedDataTypes.add(UpDownType.class);
			acceptedDataTypes.add(PercentType.class);
			acceptedDataTypes.add(StringType.class);
			
			acceptedCommandTypes.add(UpDownType.class);
			acceptedCommandTypes.add(StopMoveType.class);
			acceptedCommandTypes.add(PercentType.class);
			acceptedCommandTypes.add(StringType.class);
		}
		
		public List<Class<? extends State>> getAcceptedDataTypes() {
			return acceptedDataTypes;
		}
		
		public List<Class<? extends Command>> getAcceptedCommandTypes() {
			return acceptedCommandTypes;
		}
	}
	
	
}
