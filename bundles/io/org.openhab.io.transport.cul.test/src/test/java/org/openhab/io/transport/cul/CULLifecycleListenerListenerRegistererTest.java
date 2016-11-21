/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
