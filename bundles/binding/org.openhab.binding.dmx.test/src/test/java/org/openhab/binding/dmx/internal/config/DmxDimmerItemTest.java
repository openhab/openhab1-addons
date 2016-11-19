/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.config;

import org.junit.Test;
import org.mockito.Mockito;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.core.DmxChannel;
import org.openhab.binding.dmx.internal.core.DmxSimpleChannel;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DmxDimmerItem configuration tests.
 *
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxDimmerItemTest extends DmxSwitchItemTest {

    DmxSimpleChannel ch3 = new DmxSimpleChannel(3);
    DmxSimpleChannel ch4 = new DmxSimpleChannel(4);

    @Override
    protected DmxItem getItemInstance(String configString) throws BindingConfigParseException {
        return new DmxDimmerItem("testDimmerItem", configString, null);
    }

    @Override
    protected DmxItem getValidInstance() throws BindingConfigParseException {
        return new DmxDimmerItem("goodDimmerItem", "CHANNEL[3,4:100]", null);
    }

    @Test
    public void decreasesWhenDecreaseCommandReceived() throws BindingConfigParseException {

        DmxItem item = getValidInstance();
        DmxService service = Mockito.mock(DmxService.class);

        Mockito.when(service.getChannelValue(ch3)).thenReturn(95);
        Mockito.when(service.getChannelValue(ch4)).thenReturn(3);

        item.processCommand(service, IncreaseDecreaseType.DECREASE);

        Mockito.verify(service).decreaseChannel(item.getChannel(0), DmxDimmerItem.DIMMER_STEP_SIZE);
        Mockito.verify(service).decreaseChannel(item.getChannel(1), DmxDimmerItem.DIMMER_STEP_SIZE);

    }

    @Test
    public void increasesWhenIncreaseCommandReceived() throws BindingConfigParseException {

        DmxItem item = getValidInstance();
        DmxService service = Mockito.mock(DmxService.class);

        Mockito.when(service.getChannelValue(ch3)).thenReturn(95);
        Mockito.when(service.getChannelValue(ch4)).thenReturn(3);

        item.processCommand(service, IncreaseDecreaseType.INCREASE);

        Mockito.verify(service).enableChannel(item.getChannel(0));
        Mockito.verify(service).increaseChannel(item.getChannel(0), DmxDimmerItem.DIMMER_STEP_SIZE);
        Mockito.verify(service).enableChannel(item.getChannel(1));
        Mockito.verify(service).increaseChannel(item.getChannel(1), DmxDimmerItem.DIMMER_STEP_SIZE);

    }

    @Test
    public void canBeSetWithPercentType() throws BindingConfigParseException {

        DmxItem item = getValidInstance();
        DmxService service = Mockito.mock(DmxService.class);

        Mockito.when(service.getChannelValue(ch3)).thenReturn(0);
        Mockito.when(service.getChannelValue(ch4)).thenReturn(155);

        item.processCommand(service, new PercentType(0));
        Mockito.verify(service).setChannelValue(item.getChannel(0), DmxChannel.DMX_MAX_VALUE);
        Mockito.verify(service).setChannelValue(item.getChannel(0), PercentType.ZERO);
        Mockito.verify(service).setChannelValue(item.getChannel(1), DmxChannel.DMX_MAX_VALUE);
        Mockito.verify(service).setChannelValue(item.getChannel(1), PercentType.ZERO);
    }

}
