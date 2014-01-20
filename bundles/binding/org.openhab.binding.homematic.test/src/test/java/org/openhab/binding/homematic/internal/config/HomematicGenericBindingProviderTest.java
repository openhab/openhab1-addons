/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import org.junit.Test;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.BindingConfigParseException;

public class HomematicGenericBindingProviderTest {

    HomematicGenericBindingProvider provider = new HomematicGenericBindingProvider();

    @Test
    public void parseAdminItem() throws BindingConfigParseException {
        provider.processBindingConfiguration("homematic", new StringItem("Test"), "ADMIN:DUMP_UNCONFIGURED_DEVICES");
    }

}
