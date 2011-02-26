package org.openhab.binding.http.internal;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.http.internal.HttpGenericBindingProvider.HttpBindingConfig;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class HttpGenericBindingProviderTest {
	
	private HttpGenericBindingProvider provider;
	
	@Before
	public void init() {
		provider = new HttpGenericBindingProvider();
	}
	
	@Test(expected=BindingConfigParseException.class)
	public void testParseBindingConfig_wrongDirection() throws BindingConfigParseException {
		provider.parseBindingConfig(null, "?");
	}

	@Test
	public void testParseBindingConfig() throws BindingConfigParseException {
		
		String bindingConfig = ">[ON:POST:http://www.domain.org/home/lights/23871/?status=on&type=\"text\"] >[OFF:GET:http://www.domain.org/home/lights/23871/?status=off] <[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*?<title>(.*?)</title>(.*))]";
		
		Item testItem = new GenericItem("TEST") {
			
			@Override
			public List<Class<? extends State>> getAcceptedDataTypes() {
				List<Class<? extends State>> list = new ArrayList<Class<? extends State>>();
				list.add(StringType.class);
				return list;
			}
			
			@Override
			public List<Class<? extends Command>> getAcceptedCommandTypes() {
				List<Class<? extends Command>> list = new ArrayList<Class<? extends Command>>();
				list.add(StringType.class);
				return list;
			}
			
		};
		
		// method under test
		HttpBindingConfig config = provider.parseBindingConfig(testItem, bindingConfig);
		
		// asserts
		Assert.assertEquals(true, config.containsKey(HttpGenericBindingProvider.IN_BINDING_KEY));
		Assert.assertEquals(null, config.get(HttpGenericBindingProvider.IN_BINDING_KEY).httpMethod);
		Assert.assertEquals("http://www.domain.org/weather/openhabcity/daily", config.get(HttpGenericBindingProvider.IN_BINDING_KEY).url);
		Assert.assertEquals(60000, config.get(HttpGenericBindingProvider.IN_BINDING_KEY).refreshInterval);
		Assert.assertEquals("REGEX(.*?<title>(.*?)</title>(.*))", config.get(HttpGenericBindingProvider.IN_BINDING_KEY).transformation);
		
		// asserts
		Assert.assertEquals(true, config.containsKey(StringType.valueOf("ON")));
		Assert.assertEquals("POST", config.get(StringType.valueOf("ON")).httpMethod);
		Assert.assertEquals("http://www.domain.org/home/lights/23871/?status=on&type=\"text\"", config.get(StringType.valueOf("ON")).url);
		
		Assert.assertEquals(true, config.containsKey(StringType.valueOf("OFF")));
		Assert.assertEquals("GET", config.get(StringType.valueOf("OFF")).httpMethod);
		Assert.assertEquals("http://www.domain.org/home/lights/23871/?status=off", config.get(StringType.valueOf("OFF")).url);
	}

}
