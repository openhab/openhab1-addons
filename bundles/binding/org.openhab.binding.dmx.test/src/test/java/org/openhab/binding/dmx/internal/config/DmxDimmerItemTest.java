/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.dmx.internal.config;

import org.junit.Test;
import org.mockito.Mockito;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.core.DmxChannel;
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

	@Override
	protected DmxItem getItemInstance(String configString)
			throws BindingConfigParseException {
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

		Mockito.when(service.getChannelValue(3)).thenReturn(95);
		Mockito.when(service.getChannelValue(4)).thenReturn(3);

		item.processCommand(service, IncreaseDecreaseType.DECREASE);

		Mockito.verify(service).decreaseChannel(3, DmxDimmerItem.DIMMER_STEP_SIZE);
		Mockito.verify(service).decreaseChannel(4, DmxDimmerItem.DIMMER_STEP_SIZE);

	}
	
	@Test
	public void increasesWhenIncreaseCommandReceived() throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);

		Mockito.when(service.getChannelValue(3)).thenReturn(95);
		Mockito.when(service.getChannelValue(4)).thenReturn(3);

		item.processCommand(service, IncreaseDecreaseType.INCREASE);

		Mockito.verify(service).enableChannel(3);
		Mockito.verify(service).increaseChannel(3, DmxDimmerItem.DIMMER_STEP_SIZE);
		Mockito.verify(service).enableChannel(4);
		Mockito.verify(service).increaseChannel(4, DmxDimmerItem.DIMMER_STEP_SIZE);

	}
			
	@Test
	public void canBeSetWithPercentType() throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);

		Mockito.when(service.getChannelValue(3)).thenReturn(0);
		Mockito.when(service.getChannelValue(4)).thenReturn(155);

		item.processCommand(service, new PercentType(0));
		Mockito.verify(service).setChannelValue(3, DmxChannel.DMX_MAX_VALUE);
		Mockito.verify(service).setChannelValue(3, PercentType.ZERO);
		Mockito.verify(service).setChannelValue(4, DmxChannel.DMX_MAX_VALUE);
		Mockito.verify(service).setChannelValue(4, PercentType.ZERO);
	}

}
