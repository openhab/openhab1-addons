/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

public class LocalNetworkInterfaceTest {

    @Test @Ignore // ignored as it does not work on Jenkins at CloudBees
    public void testGetLocalNetworkInterface() {
        assertNotNull("Must find out a local interface", LocalNetworkInterface.getLocalNetworkInterface());
    }

    @Test
    public void testGetLocalNetworkInterfaceNotLocalhost() {
        assertFalse("May not be 127.0.0.1", "127.0.0.1".equals(LocalNetworkInterface.getLocalNetworkInterface()));
    }

}
