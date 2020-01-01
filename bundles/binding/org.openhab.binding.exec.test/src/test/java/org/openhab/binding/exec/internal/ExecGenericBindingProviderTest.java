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
package org.openhab.binding.exec.internal;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.exec.internal.ExecGenericBindingProvider.ExecBindingConfig;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.model.item.binding.BindingConfigParseException;

import junit.framework.Assert;

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
        Assert.assertEquals("other command with comma, and 'quotes' and slashes \\ ",
                config.get(OnOffType.OFF).commandLine);
        Assert.assertEquals("and a fallback", config.get(StringType.valueOf("*")).commandLine);
    }

    @Test
    public void testParseBindingConfigIn() throws BindingConfigParseException {
        String cmdLine = "/usr/bin/uptime";
        String itemName = "Switch";
        SwitchItem item = new SwitchItem(itemName);
        String bindingConfig = "<[" + cmdLine + ":60000:]";

        provider.processBindingConfiguration("New", item, bindingConfig);

        Assert.assertTrue(provider.providesBinding());
        Assert.assertTrue(provider.providesBindingFor(itemName));
        Assert.assertEquals(cmdLine, provider.getCommandLine(itemName));

        Assert.assertEquals(60000, provider.getRefreshInterval(itemName));
        Assert.assertEquals("", provider.getTransformation(itemName));

        List<String> itemNames = provider.getInBindingItemNames();
        Assert.assertEquals(itemName, itemNames.get(0));
    }
}
