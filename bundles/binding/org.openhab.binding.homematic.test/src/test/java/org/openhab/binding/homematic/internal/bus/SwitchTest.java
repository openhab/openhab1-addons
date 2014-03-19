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
import org.openhab.core.library.types.OpenClosedType;

public class SwitchTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new SwitchItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void allBindingsChangedOn() {
        checkInitialValue(ParameterKey.STATE, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void allBindingsChangedOff() {
        checkInitialValue(ParameterKey.STATE, Boolean.FALSE, OnOffType.OFF);
    }

    @Test
    public void receiveValueUpdateOn() {
        checkValueReceived(ParameterKey.STATE, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void receiveValueUpdateOff() {
        checkValueReceived(ParameterKey.STATE, Boolean.FALSE, OnOffType.OFF);
    }

    @Test
    public void receiveCommandOFF() {
        checkReceiveCommand(ParameterKey.STATE, OnOffType.OFF, Boolean.FALSE);
    }

    @Test
    public void receiveCommandOn() {
        checkReceiveCommand(ParameterKey.STATE, OnOffType.ON, Boolean.TRUE);
    }

    @Test
    public void receiveCommandOPEN() {
        checkReceiveCommand(ParameterKey.STATE, OpenClosedType.OPEN, Boolean.TRUE);
    }

    @Test
    public void receiveCommandCLOSE() {
        checkReceiveCommand(ParameterKey.STATE, OpenClosedType.CLOSED, Boolean.FALSE);
    }

}
