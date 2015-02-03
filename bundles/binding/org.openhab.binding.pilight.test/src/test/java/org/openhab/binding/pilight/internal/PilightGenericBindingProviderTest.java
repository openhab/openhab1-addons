/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.pilight.internal.PilightBinding;
import org.openhab.binding.pilight.internal.PilightBindingConfig;
import org.openhab.binding.pilight.internal.PilightGenericBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class PilightGenericBindingProviderTest {
	
	private PilightGenericBindingProvider provider;
	private PilightBinding binding;
	private Item testItem;
	
	@Before
	public void init() {
		provider = new PilightGenericBindingProvider();
		binding = new PilightBinding();
		testItem = new NumberItem("NumberItem");;
	}

	@Test
	public void testNumberItemScale() throws BindingConfigParseException {
		
		String bindingConfig = "kaku#outside:weather,property=temperature";
		
		PilightBindingConfig config = provider.parseBindingConfig(testItem, bindingConfig);
		Assert.assertNotNull(config);
		
		DecimalType number0 = (DecimalType) binding.getState("23", config);
		Assert.assertEquals(number0.toBigDecimal().compareTo(new BigDecimal("23")), 0);

		config.setScale(1);
		DecimalType number1 = (DecimalType) binding.getState("233", config);
		Assert.assertEquals(number1.toBigDecimal().compareTo(new BigDecimal("23.3")), 0);

		config.setScale(2);
		DecimalType number2 = (DecimalType) binding.getState("2233", config);
		Assert.assertEquals(number2.toBigDecimal().compareTo(new BigDecimal("22.33")), 0);
		
		config.setScale(-1);
		DecimalType number3 = (DecimalType) binding.getState("233", config);
		Assert.assertEquals(number3.toBigDecimal().compareTo(new BigDecimal("2330")), 0);
	}
}
