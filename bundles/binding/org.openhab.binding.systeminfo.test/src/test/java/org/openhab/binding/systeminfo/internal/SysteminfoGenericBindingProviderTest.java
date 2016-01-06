/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.systeminfo.internal;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.systeminfo.internal.SysteminfoGenericBindingProvider;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * @author Chris Carman
 * @since 1.7.0
 */
public class SysteminfoGenericBindingProviderTest {
	
	private SysteminfoGenericBindingProvider provider;
	private Item testItem;
	private String simpleConfig;

	@Before
	public void init() {
		provider = new SysteminfoGenericBindingProvider();
		testItem = new StringTestItem();
		simpleConfig = "DirFiles:60000:.";
	}
	
	@Test
	public void testGetBindingType() {
		Assert.assertEquals("systeminfo", provider.getBindingType());
	}

	@Test(expected=BindingConfigParseException.class)
	public void testValidateItemType_nullItem() throws BindingConfigParseException {
		Item s = null;
		provider.validateItemType(s, "?");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testValidateItemType_nullConfig() throws BindingConfigParseException {
		String s = null;
		provider.validateItemType(testItem, s);
	}

	@Test(expected=BindingConfigParseException.class)
	public void testValidateItemType_itemNotNumOrStr() throws BindingConfigParseException {
		Item newItem = new DummyItem();
		provider.validateItemType(newItem, "?");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testValidateItemType_emptyConfig() throws BindingConfigParseException {
		provider.validateItemType(testItem, "");
	}

	@Test(expected=BindingConfigParseException.class)
	public void testProcessBindingConfig_nullContext() throws BindingConfigParseException {
		provider.processBindingConfiguration(null, testItem, simpleConfig);
	}

	@Test(expected=BindingConfigParseException.class)
	public void testProcessBindingConfig_nullItem() throws BindingConfigParseException {
		provider.processBindingConfiguration("systeminfo", null, simpleConfig);
	}

	@Test(expected=BindingConfigParseException.class)
	public void testProcessBindingConfig_nullConfig() throws BindingConfigParseException {
		provider.processBindingConfiguration("systeminfo", testItem, null);
	}

	@Test(expected=BindingConfigParseException.class)
	public void testProcessBindingConfig_emptyConfig() throws BindingConfigParseException {
		provider.processBindingConfiguration("systeminfo", testItem, "");
	}

	/*
	 * "DirFiles" tests
	 */
	@Test
	/* Verify a simple file count configuration: "DirFiles:60000:." */
	public void testProcessBindingConfig_FileCount01() throws BindingConfigParseException {
		NumberItem testItem = new NumberItem("DirFiles01");
		String simpleConfig = "DirFiles:60000:.";
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertNull(provider.getCommandType(null));
		Assert.assertNull(provider.getCommandType(""));
		Assert.assertEquals("DirFiles", provider.getCommandType("DirFiles01").toString());

		Assert.assertNull(provider.getItemType(null));
		Assert.assertNull(provider.getItemType(""));
		Assert.assertNull(provider.getItemType("DirFiles01"));

		Assert.assertEquals(0, provider.getRefreshInterval(null));
		Assert.assertEquals(0, provider.getRefreshInterval(""));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirFiles01"));

		Assert.assertNull(provider.getTarget(null));
		Assert.assertNull(provider.getTarget(""));
		Assert.assertEquals(".", provider.getTarget("DirFiles01"));
	}

	@Test
	/* Verify a relative path file count configuration (*nix): "DirFiles:60000:../resources/" */
	public void testProcessBindingConfig_FileCount02() throws BindingConfigParseException {
		NumberItem testItem = new NumberItem("DirFiles02");
		String simpleConfig = "DirFiles:60000:../resources/";
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertEquals("DirFiles", provider.getCommandType("DirFiles02").toString());
		Assert.assertNull(provider.getItemType("DirFiles02"));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirFiles02"));
		Assert.assertEquals("../resources/", provider.getTarget("DirFiles02"));
	}

	@Test
	/* Verify a relative path file count configuration (Windows): "DirFiles:60000:..\resources\" */
	public void testProcessBindingConfig_FileCount03() throws BindingConfigParseException {
		String simpleConfig = "DirFiles:60000:..\\resources\\";

		NumberItem testItem = new NumberItem("DirFiles03");
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertEquals("DirFiles", provider.getCommandType("DirFiles03").toString());
		Assert.assertNull(provider.getItemType("DirFiles03"));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirFiles03"));
		Assert.assertEquals("..\\resources\\", provider.getTarget("DirFiles03"));
	}

	/* Verify an absolute path file count configuration (*nix): "DirFiles:60000:/usr/bin/" */
	public void testProcessBindingConfig_FileCount04() throws BindingConfigParseException {
		String simpleConfig = "DirFiles:60000:/usr/bin/";

		NumberItem testItem = new NumberItem("DirFiles04");
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertEquals("DirFiles", provider.getCommandType("DirFiles04").toString());
		Assert.assertNull(provider.getItemType("DirFiles04"));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirFiles04"));
		Assert.assertEquals("/usr/bin/", provider.getTarget("DirFiles04"));
	}

	@Test
	/* Verify an absolute path file count configuration (Windows): "DirFiles:60000:c:\windows\" */
	public void testProcessBindingConfig_FileCount05() throws BindingConfigParseException {
		String simpleConfig = "DirFiles:60000:c:\\temp\\";

		NumberItem testItem = new NumberItem("DirFiles05");
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertEquals("DirFiles", provider.getCommandType("DirFiles05").toString());
		Assert.assertNull(provider.getItemType("DirFiles05"));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirFiles05"));
		Assert.assertEquals("c:\\temp\\", provider.getTarget("DirFiles05"));
	}
	/*** END of "DirFiles" tests ***/

	/*
	 * "DirUsage" tests
	 */
	@Test
	/* Verify a simple dir usage configuration: "DirUsage:60000:/temp" */
	public void testProcessBindingConfig_FileUsage01() throws BindingConfigParseException {
		NumberItem testItem = new NumberItem("DirUsage01");
		String simpleConfig = "DirUsage:60000:/temp";
		provider.processBindingConfiguration("systeminfo", testItem, simpleConfig);

		Assert.assertNull(provider.getCommandType(null));
		Assert.assertNull(provider.getCommandType(""));
		Assert.assertEquals("DirUsage", provider.getCommandType("DirUsage01").toString());

		Assert.assertNull(provider.getItemType(null));
		Assert.assertNull(provider.getItemType(""));
		Assert.assertNull(provider.getItemType("DirUsage01"));

		Assert.assertEquals(0, provider.getRefreshInterval(null));
		Assert.assertEquals(0, provider.getRefreshInterval(""));
		Assert.assertEquals(60000, provider.getRefreshInterval("DirUsage01"));

		Assert.assertNull(provider.getTarget(null));
		Assert.assertNull(provider.getTarget(""));
		Assert.assertEquals("/temp", provider.getTarget("DirUsage01"));
	}
	/*** END of "DirUsage" tests ***/


	class StringTestItem extends GenericItem {
		
		public StringTestItem() {
			super("TEST");
		}
		
		public StringTestItem(String name) {
			super(name);
		}
		
		public List<Class<? extends State>> getAcceptedDataTypes() {
			List<Class<? extends State>> list = new ArrayList<Class<? extends State>>();
			list.add(StringType.class);
			return list;
		}
		
		public List<Class<? extends Command>> getAcceptedCommandTypes() {
			List<Class<? extends Command>> list = new ArrayList<Class<? extends Command>>();
			list.add(StringType.class);
			return list;
		}

		@Override
		public State getStateAs(Class<? extends State> typeClass) {
			return null;
		}
		
	};


	class DummyItem extends GenericItem {
		public DummyItem() {
			super("DUMMY!");
		}

		public DummyItem(String name) {
			super(name);
		}

		public List<Class<? extends State>> getAcceptedDataTypes() {
			List<Class<? extends State>> list = new ArrayList<Class<? extends State>>();
				/*list.add(OnOffType.class);
				list.add(PercentType.class);
				list.add(UnDefType.class);*/
			return list;
		}
		
		public List<Class<? extends Command>> getAcceptedCommandTypes() {
			List<Class<? extends Command>> list = new ArrayList<Class<? extends Command>>();
				/*list.add(OnOffType.class);
				list.add(IncreaseDecreaseType.class);
				list.add(PercentType.class);*/
			return list;
		}

		@Override
		public State getStateAs(Class<? extends State> typeClass) {
			return null;
		}
	}
}
