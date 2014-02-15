/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.internal.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.UnDefType;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapFactory;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;

public class ItemUIRegistryImplTest {

	static private ItemRegistry registry;
	static private ItemUIRegistryImpl uiRegistry = new ItemUIRegistryImpl();

	@Before
	public void prepareRegistry() {
		registry = mock(ItemRegistry.class);
		uiRegistry.setItemRegistry(registry);
	}
	
	@Test
	public void getLabel_plainLabel() {
		String testLabel = "This is a plain text";
		Widget w = mock(Widget.class);
		when(w.getLabel()).thenReturn(testLabel);
		String label = uiRegistry.getLabel(w);
		assertEquals(testLabel, label);
	}

	@Test
	public void getLabel_labelWithStaticValue() {
		String testLabel = "Label [value]";
		Widget w = mock(Widget.class);
		when(w.getLabel()).thenReturn(testLabel);
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [value]", label);
	}

	@Test
	public void getLabel_labelWithStringValue() throws ItemNotFoundException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [State]", label);
	}

	@Test
	public void getLabel_labelWithIntegerValue() throws ItemNotFoundException {
		String testLabel = "Label [%d]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DecimalType(20));
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(20));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [20]", label);
	}

	@Test
	public void getLabel_labelWithDecimalValue() throws ItemNotFoundException {
		String testLabel = "Label [%.3f]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DecimalType(10f/3f));
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(10f/3f));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [3.333]", label);
	}

	@Test
	public void getLabel_labelWithPercent() throws ItemNotFoundException {
		String testLabel = "Label [%.1f %%]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DecimalType(10f/3f));
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(10f/3f));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [3.3 %]", label);
	}

	@Test
	public void getLabel_labelWithDate() throws ItemNotFoundException {
		String testLabel = "Label [%1$td.%1$tm.%1$tY]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DateTimeType("2011-06-01T00:00:00"));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [01.06.2011]", label);
	}

	@Test
	public void getLabel_labelWithTime() throws ItemNotFoundException {
		String testLabel = "Label [%1$tT]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DateTimeType("2011-06-01T15:30:59"));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [15:30:59]", label);
	}

	@Test
	public void getLabel_widgetWithoutLabelAndItem() throws ItemNotFoundException {
		Widget w = mock(Widget.class);
		String label = uiRegistry.getLabel(w);
		assertEquals("", label);
	}

	@Test
	public void getLabel_widgetWithoutLabel() throws ItemNotFoundException {
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		String label = uiRegistry.getLabel(w);
		assertEquals("Item", label);
	}

	@Test
	public void getLabel_labelFromUIProvider() throws ItemNotFoundException {
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		ItemUIProvider provider = mock(ItemUIProvider.class);
		uiRegistry.addItemUIProvider(provider);
		when(provider.getLabel(anyString())).thenReturn("ProviderLabel");
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		String label = uiRegistry.getLabel(w);
		assertEquals("ProviderLabel", label);
		uiRegistry.removeItemUIProvider(provider);
	}

	@Test
	public void getLabel_labelForUndefinedStringItemState() throws ItemNotFoundException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [-]", label);
	}

	@Test
	public void getLabel_labelForUndefinedIntegerItemState() throws ItemNotFoundException {
		String testLabel = "Label [%d]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [-]", label);
	}

	@Test
	public void getLabel_labelForUndefinedDecimalItemState() throws ItemNotFoundException {
		String testLabel = "Label [%.2f]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [-]", label);
	}

	@Test
	public void getLabel_labelForUndefinedDateItemState() throws ItemNotFoundException {
		String testLabel = "Label [%1$td.%1$tm.%1$tY]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [-.-.-]", label);
	}

	@Test
	public void getLabel_itemNotFound() throws ItemNotFoundException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(w.eClass()).thenReturn(SitemapFactory.eINSTANCE.createText().eClass());
		when(registry.getItem("Item")).thenThrow(new ItemNotFoundException("Item"));
		when(item.getState()).thenReturn(new StringType("State"));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [-]", label);
	}
	
	@Test
	public void getLabel_labelWithFunctionValue() throws ItemNotFoundException {
		String testLabel = "Label [MAP(de.map):%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [State]", label);
	}
	
	@Test
	public void getLabel_groupLabelWithValue() throws ItemNotFoundException {
		String testLabel = "Label [%d]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(OnOffType.ON);
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(5));
		String label = uiRegistry.getLabel(w);
		assertEquals("Label [5]", label);
	}
	
	@Test
	public void getWidget_UnknownPageId() throws ItemNotFoundException {
		Sitemap sitemap = SitemapFactory.eINSTANCE.createSitemap();
		when(registry.getItem("unknown")).thenThrow(new ItemNotFoundException("unknown"));
		Widget w = uiRegistry.getWidget(sitemap, "unknown");
		assertNull(w);
	}
	
	@Test
	public void testFormatDefault() {
		Assert.assertEquals("Server [(-)]", uiRegistry.formatUndefined("Server [(%d)]"));
		Assert.assertEquals("Anruf [von - an -]", uiRegistry.formatUndefined("Anruf [von %2$s an %1$s]"));
		Assert.assertEquals("Zeit [-.-.- -]", uiRegistry.formatUndefined("Zeit [%1$td.%1$tm.%1$tY %1$tT]"));
		Assert.assertEquals("Temperatur [- °C]", uiRegistry.formatUndefined("Temperatur [%.1f °C]"));
		Assert.assertEquals("Luftfeuchte [- %]", uiRegistry.formatUndefined("Luftfeuchte [%.1f %%]"));
	}

}
