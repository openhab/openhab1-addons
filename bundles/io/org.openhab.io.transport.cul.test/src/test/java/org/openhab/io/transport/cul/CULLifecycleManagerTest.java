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

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.io.transport.cul.internal.CULConfig;
import org.openhab.io.transport.cul.internal.CULConfigFactory;
import org.openhab.io.transport.cul.internal.CULHandlerInternal;
import org.openhab.io.transport.cul.internal.CULManager;
import org.osgi.service.cm.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class CULLifecycleManagerTest {

    private static final CULMode MODE = CULMode.values()[0];

    private CULLifecycleManager sut;
    @Mock
    private CULLifecycleListener listener;
    @Mock
    private CULManager manager;
    @Mock
    private CULHandlerInternal<CULConfig> cul;
    @Mock
    private CULHandlerInternal<CULConfig> newCul;
    @Mock
    private CULConfig config;
    @Mock
    private CULConfig differentConfig;
    @Mock
    private Dictionary<String, String> allConfig;
    @Mock
    private CULConfigFactory configFactory;

    @Before
    public void setUp() {
        sut = new CULLifecycleManager(MODE, listener, manager, cul, config);
    }

    @Test
    public void config_noConfig() throws Exception {
        sut.config(null);

        verifyNoMoreInteractions(manager);
    }

    @Test(expected = ConfigurationException.class)
    public void config_noDeviceName() throws Exception {
        sut.config(allConfig);

        verifyNoMoreInteractions(manager);
    }

    @Test(expected = ConfigurationException.class)
    public void config_invalidDeviceName() throws Exception {
        given(allConfig.get(CULLifecycleManager.KEY_DEVICE_NAME)).willReturn("foo");

        sut.config(allConfig);

        verifyNoMoreInteractions(manager);
    }

    @Test(expected = ConfigurationException.class)
    public void config_invalidDeviceType() throws Exception {
        given(allConfig.get(CULLifecycleManager.KEY_DEVICE_NAME)).willReturn("foo:bar");

        sut.config(allConfig);

        verifyNoMoreInteractions(manager);
    }

    @Test
    public void config_ok() throws Exception {
        sut = new CULLifecycleManager(MODE, listener, manager, null, null);
        given(allConfig.get(CULLifecycleManager.KEY_DEVICE_NAME)).willReturn("foo:bar");
        given(manager.getConfigFactory("foo")).willReturn(configFactory);
        given(configFactory.create("foo", "bar", MODE, allConfig)).willReturn(config);
        given(manager.getOpenCULHandler(config)).willReturn(newCul);

        sut.config(allConfig);

        then(listener).should().open(newCul);
    }

    @Test
    public void close_notOpened() throws Exception {
        sut = new CULLifecycleManager(MODE, listener, manager, null, null);

        sut.close();

        verifyNoMoreInteractions(listener, manager);
        assertNotReady();
    }

    @Test
    public void close_opened() throws Exception {
        sut.close();

        then(listener).should().close(cul);
        then(manager).should().close(cul);
        assertNotReady();
    }

    @Test
    public void open_noConfig() throws Exception {
        sut = new CULLifecycleManager(MODE, listener, manager, null, null);

        sut.open();

        verifyNoMoreInteractions(listener, manager);
        assertNotReady();
    }

    @Test
    public void open_sameConfig() throws Exception {
        given(cul.getConfig()).willReturn(config);

        sut.open();

        verifyNoMoreInteractions(listener, manager);
        assertReady();
    }

    @Test
    public void open_firstConfig() throws Exception {
        given(manager.getOpenCULHandler(config)).willReturn(newCul);
        sut = new CULLifecycleManager(MODE, listener, manager, null, config);

        sut.open();

        then(listener).should().open(newCul);
        assertReady();
    }

    @Test
    public void open_changedConfig() throws Exception {
        given(cul.getConfig()).willReturn(differentConfig);
        given(manager.getOpenCULHandler(config)).willReturn(newCul);

        sut.open();

        then(listener).should().close(cul);
        then(manager).should().close(cul);
        then(listener).should().open(newCul);
        assertReady();
    }

    @Test
    public void open_failsDevice() throws Exception {
        given(manager.getOpenCULHandler(config)).willThrow(new CULDeviceException());
        sut = new CULLifecycleManager(MODE, listener, manager, null, config);

        sut.open();
        assertNotReady();
    }

    @Test
    public void open_failsCommunication() throws Exception {
        given(manager.getOpenCULHandler(config)).willReturn(newCul);
        doThrow(new CULCommunicationException()).when(listener).open(newCul);
        sut = new CULLifecycleManager(MODE, listener, manager, null, config);

        sut.open();
        assertNotReady();
    }

    private void assertReady() {
        assertTrue(sut.isCulReady());
        assertNotNull(sut.getCul());
    }

    private void assertNotReady() {
        assertFalse(sut.isCulReady());
        assertNull(sut.getCul());
    }

}
