/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.homematic.internal.device.ParameterKey;
import org.openhab.binding.homematic.test.HomematicBindingProviderMock;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class ButtonTest extends BasicBindingTest {

    @Before
    public void setupProvider() {
        provider.setItem(new SwitchItem(HomematicBindingProviderMock.DEFAULT_ITEM_NAME));
    }

    @Test
    public void receiveShortPress() {
        checkValueReceived(ParameterKey.PRESS_SHORT, Boolean.TRUE, OnOffType.ON);
        assertEquals("State received", OnOffType.OFF, publisher.popLastState());
    }

    @Test
    public void receiveLongPress() {
        checkValueReceived(ParameterKey.PRESS_LONG, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void receiveLongPressContinued() {
        checkValueReceived(ParameterKey.PRESS_CONT, Boolean.TRUE, OnOffType.ON);
    }

    @Test
    public void receiveLongPressEnd() {
        checkValueReceived(ParameterKey.PRESS_LONG_RELEASE, Boolean.TRUE, OnOffType.OFF);
    }

    @Test
    public void securityBugWorkaround() {
        // For HM-SwI-3-FM
        checkValueReceived(ParameterKey.INSTALL_TEST, Boolean.TRUE, OnOffType.ON);
    }
}
