package org.openhab.binding.exec.internal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.exec.internal.ExecGenericBindingProvider.ExecBindingConfig;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class ExecGenericBindingProviderTest {
	
	private ExecGenericBindingProvider provider;
	
	@Before
	public void init() {
		provider = new ExecGenericBindingProvider();
	}

	@Test
	public void testParseBindingConfig() {
		ExecBindingConfig config = provider.new ExecBindingConfig();
		String bindingConfig = "ON:some command to execute, OFF: 'other command\\, with comma and \\'quotes\\'', *:and a fallback";
		
		provider.parseBindingConfig(bindingConfig, config);
		
		Assert.assertEquals(3, config.size());
		Assert.assertEquals("some command to execute", config.get("ON"));
		Assert.assertEquals("other command\\, with comma and \\'quotes\\'", config.get("OFF"));
		Assert.assertEquals("and a fallback", config.get("*"));
	}

}
