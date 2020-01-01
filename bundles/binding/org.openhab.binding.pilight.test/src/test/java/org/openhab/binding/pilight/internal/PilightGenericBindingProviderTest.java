/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.pilight.internal;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.model.item.binding.BindingConfigParseException;

import junit.framework.Assert;

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
        testItem = new NumberItem("NumberItem");
        ;
    }

    @Test
    public void testNumberItemScale() throws BindingConfigParseException {

        String bindingConfig = "kaku#weather,property=temperature";

        PilightBindingConfig config = provider.parseBindingConfig(testItem, bindingConfig);
        Assert.assertNotNull(config);

        DecimalType number0 = (DecimalType) binding.getState("23", config);
        Assert.assertEquals(number0.toBigDecimal().compareTo(new BigDecimal("23")), 0);
    }
}
