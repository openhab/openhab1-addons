package org.openhab.ui.webapp.internal.render;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.UnDefType;
import org.openhab.model.sitemap.SitemapFactory;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;
import org.openhab.ui.webapp.internal.WebAppActivator;
import org.openhab.ui.webapp.render.RenderException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class AbstractWidgetRendererTest {

	static AbstractWidgetRenderer renderer = new AbstractWidgetRenderer() {
		
		public EList<Widget> renderWidget(Widget w, StringBuilder sb)
				throws RenderException {
			return null;
		}
		
		public boolean canRender(Widget w) {
			return false;
		}
	};
	
	static private ItemRegistry registry;

	@Before
	public void prepareRegistry() {
		registry = mock(ItemRegistry.class);
		renderer.setItemRegistry(registry);
		renderer.activate(null);
	}
	
	@Test
	public void getLabel_plainLabel() {
		String testLabel = "This is a plain text";
		Widget w = mock(Widget.class);
		when(w.getLabel()).thenReturn(testLabel);
		String label = renderer.getLabel(w);
		assertEquals(testLabel, label);
	}

	@Test
	public void getLabel_labelWithStaticValue() {
		String testLabel = "Label [value]";
		Widget w = mock(Widget.class);
		when(w.getLabel()).thenReturn(testLabel);
		String label = renderer.getLabel(w);
		assertEquals("Label <span>value</span>", label);
	}

	@Test
	public void getLabel_labelWithStringValue() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>State</span>", label);
	}

	@Test
	public void getLabel_labelWithIntegerValue() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%d]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(20));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>20</span>", label);
	}

	@Test
	public void getLabel_labelWithDecimalValue() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%.3f]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(10f/3f));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>3.333</span>", label);
	}

	@Test
	public void getLabel_labelWithPercent() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%.1f %%]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getStateAs(DecimalType.class)).thenReturn(new DecimalType(10f/3f));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>3.3 %</span>", label);
	}

	@Test
	public void getLabel_labelWithDate() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%1$td.%1$tm.%1$tY]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DateTimeType("2011-06-01T00:00:00"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>01.06.2011</span>", label);
	}

	@Test
	public void getLabel_labelWithTime() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%1$tT]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new DateTimeType("2011-06-01T15:30:59"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>15:30:59</span>", label);
	}

	@Test
	public void getLabel_widgetWithoutLabelAndItem() throws ItemNotFoundException, ItemNotUniqueException {
		Widget w = mock(Widget.class);
		String label = renderer.getLabel(w);
		assertEquals("", label);
	}

	@Test
	public void getLabel_widgetWithoutLabel() throws ItemNotFoundException, ItemNotUniqueException {
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		String label = renderer.getLabel(w);
		assertEquals("Item", label);
	}

	@Test
	public void getLabel_labelFromUIProvider() throws ItemNotFoundException, ItemNotUniqueException {
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		ItemUIProvider provider = mock(ItemUIProvider.class);
		renderer.addItemUIProvider(provider);
		when(provider.getLabel(anyString())).thenReturn("ProviderLabel");
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		String label = renderer.getLabel(w);
		assertEquals("ProviderLabel", label);
		renderer.removeItemUIProvider(provider);
	}

	@Test
	public void getLabel_labelForUndefinedStringItemState() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = renderer.getLabel(w);
		assertEquals("Label <span>undefined</span>", label);
	}

	@Test
	public void getLabel_labelForUndefinedIntegerItemState() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%d]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = renderer.getLabel(w);
		assertEquals("Label <span>0</span>", label);
	}

	@Test
	public void getLabel_labelForUndefinedDecimalItemState() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%.2f]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = renderer.getLabel(w);
		assertEquals("Label <span>0.00</span>", label);
	}

	@Test
	public void getLabel_labelForUndefinedDateItemState() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%1$td.%1$tm.%1$tY]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(UnDefType.UNDEF);
		String label = renderer.getLabel(w);
		assertEquals("Label <span>" + String.format("%1$td.%1$tm.%1$tY", Calendar.getInstance()) + "</span>", label);
	}

	@Test
	public void getLabel_itemNotFound() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(w.eClass()).thenReturn(SitemapFactory.eINSTANCE.createText().eClass());
		when(registry.getItem("Item")).thenThrow(new ItemNotFoundException("Item"));
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>undefined</span>", label);
	}

	@Test
	public void getLabel_itemNotUnique() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(w.eClass()).thenReturn(SitemapFactory.eINSTANCE.createText().eClass());
		when(registry.getItem("Item")).thenThrow(new ItemNotUniqueException("Item", Collections.<Item> emptyList()));
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>undefined</span>", label);
	}
	
	@Test
	public void getLabel_labelWithFunctionValueWithoutServiceAvailable() throws ItemNotFoundException, ItemNotUniqueException {
		String testLabel = "Label [MAP(de.map):%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>State</span>", label);
	}

	@Test
	public void getLabel_labelWithFunctionValueWithServiceAvailable() throws Exception {
		// prepare the transformation service
		BundleContext bc = mock(BundleContext.class);
		ServiceReference serviceRef = mock(ServiceReference.class);
		TransformationService service = mock(TransformationService.class);
		when(bc.getServiceReferences(anyString(), anyString())).thenReturn(new ServiceReference[] { serviceRef });
		when(bc.getService(any(ServiceReference.class))).thenReturn(service);
		when(service.transform("de.map", "State")).thenReturn("Transformed");
		WebAppActivator activator = new WebAppActivator();
		activator.start(bc);
		
		String testLabel = "Label [MAP(de.map):%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>Transformed</span>", label);
	}

	@Test
	public void getLabel_labelWithFunctionValueWithTransformationException() throws Exception {
		// prepare the transformation service
		BundleContext bc = mock(BundleContext.class);
		ServiceReference serviceRef = mock(ServiceReference.class);
		TransformationService service = mock(TransformationService.class);
		when(bc.getServiceReferences(anyString(), anyString())).thenReturn(new ServiceReference[] { serviceRef });
		when(bc.getService(any(ServiceReference.class))).thenReturn(service);
		when(service.transform("de.map", "State")).thenThrow(new TransformationException("Error"));
		WebAppActivator activator = new WebAppActivator();
		activator.start(bc);
		
		String testLabel = "Label [MAP(de.map):%s]";
		Widget w = mock(Widget.class);
		Item item = mock(Item.class);
		when(w.getLabel()).thenReturn(testLabel);
		when(w.getItem()).thenReturn("Item");
		when(registry.getItem("Item")).thenReturn(item);
		when(item.getState()).thenReturn(new StringType("State"));
		String label = renderer.getLabel(w);
		assertEquals("Label <span>State</span>", label);
	}

}
