/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.exec.internal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.exec.internal.ExecGenericBindingProvider.ExecBindingConfig;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.model.item.binding.BindingConfigParseException;


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
	public void testParseBindingConfig() throws BindingConfigParseException {
		ExecBindingConfig config = new ExecGenericBindingProvider.ExecBindingConfig();
		String bindingConfig = "ON:some command to execute, OFF: 'other command with comma\\, and \\'quotes\\' and slashes \\\\ ', *:and a fallback";
		SwitchItem item = new SwitchItem("");
		
		provider.parseLegacyOutBindingConfig(item, bindingConfig, config);
		
		Assert.assertEquals(3, config.size());
		Assert.assertEquals("some command to execute", config.get(OnOffType.ON).commandLine);
		Assert.assertEquals("other command with comma, and 'quotes' and slashes \\ ", config.get(OnOffType.OFF).commandLine);
		Assert.assertEquals("and a fallback", config.get(StringType.valueOf("*")).commandLine);
	}

}
