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

public class LowBatTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new SwitchItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedBatterieIsFine() {
        checkInitialValue(ParameterKey.LOWBAT, Boolean.FALSE, OnOffType.OFF);
    }

    @Test
    public void allBindingsChangedBatterieIsLow() {
        checkInitialValue(ParameterKey.LOWBAT, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void receiveUpdateBatterieIsFine() {
        checkValueReceived(ParameterKey.LOWBAT,  Boolean.FALSE, OnOffType.OFF);
    }
    
    @Test
    public void receiveUpdateBatterieIsLow() {
        checkValueReceived(ParameterKey.LOWBAT, Boolean.TRUE, OnOffType.ON);
    }
    
}
