/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class UnreachTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new SwitchItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedReachable() {
        checkInitialValue(ParameterKey.UNREACH, Boolean.FALSE, OnOffType.OFF);
    }

    @Test
    public void allBindingsChangedUnreachable() {
        checkInitialValue(ParameterKey.UNREACH, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void receiveUpdateReachable() {
        checkValueReceived(ParameterKey.UNREACH,  Boolean.FALSE, OnOffType.OFF);
    }
    
    @Test
    public void receiveUpdateUnreachable() {
        checkValueReceived(ParameterKey.UNREACH, Boolean.TRUE, OnOffType.ON);
    }
}
