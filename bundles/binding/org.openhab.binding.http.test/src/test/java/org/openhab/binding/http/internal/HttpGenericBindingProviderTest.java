package org.openhab.binding.http.internal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.http.internal.HttpGenericBindingProvider.HttpBindingConfig;
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
	public void testParseInBindingConfig() throws BindingConfigParseException {
		
		HttpBindingConfig config = provider.new HttpBindingConfig();
		String bindingConfig = "[\"http://www.domain.org/weather/openhabcity/daily\":60000:\"REGEX(.*?<title>(.*?)</title>(.*))\"]";
		
		// method under test
		provider.parseInBindingConfig(bindingConfig, config);
		
		// asserts
		Assert.assertEquals(true, config.containsKey("!IN!"));
		Assert.assertEquals(null, config.get("!IN!").httpMethod);
		Assert.assertEquals("http://www.domain.org/weather/openhabcity/daily", config.get("!IN!").url);
		Assert.assertEquals(60000, config.get("!IN!").refreshInterval);
		Assert.assertEquals("REGEX(.*?<title>(.*?)</title>(.*))", config.get("!IN!").transformation);
	}
	
	@Test
	public void testParseOutBindingConfig() throws BindingConfigParseException {
		
		HttpBindingConfig config = provider.new HttpBindingConfig();
		String bindingConfig = "[ON:POST:\"http://www.domain.org/home/lights/23871/?status=on&type=\\\"text\\\"\", OFF:GET:\"http://www.domain.org/home/lights/23871/?status=off\"]";
		
		// method under test
		provider.parseOutBindingConfig(bindingConfig, config);
		
		// asserts
		Assert.assertEquals(true, config.containsKey("ON"));
		Assert.assertEquals("POST", config.get("ON").httpMethod);
		Assert.assertEquals("http://www.domain.org/home/lights/23871/?status=on&type=\"text\"", config.get("ON").url);
		
		Assert.assertEquals(true, config.containsKey("OFF"));
		Assert.assertEquals("GET", config.get("OFF").httpMethod);
		Assert.assertEquals("http://www.domain.org/home/lights/23871/?status=off", config.get("OFF").url);
	}

}
