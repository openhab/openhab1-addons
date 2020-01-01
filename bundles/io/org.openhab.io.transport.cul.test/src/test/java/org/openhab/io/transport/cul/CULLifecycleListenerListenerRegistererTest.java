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
package org.openhab.io.transport.cul;

import static org.mockito.BDDMockito.then;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CULLifecycleListenerListenerRegistererTest {
    public CULLifecycleListenerListenerRegisterer sut;
    @Mock
    private CULListener listener;
    @Mock
    private CULHandler cul;

    @Before
    public void setUp() {
        sut = new CULLifecycleListenerListenerRegisterer(listener);
    }

    @Test
    public void open() throws Exception {
        sut.open(cul);
        then(cul).should().registerListener(listener);
    }

    @Test
    public void close() throws Exception {
        sut.close(cul);
        then(cul).should().unregisterListener(listener);
    }

}
