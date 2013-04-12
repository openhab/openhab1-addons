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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DmxColorItem configuration tests.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxColorItemTest extends DmxDimmerItemTest {

	@Override
	protected DmxItem getItemInstance(String configString)
			throws BindingConfigParseException {
		return new DmxColorItem("testDimmerItem", configString, null);
	}

	@Override
	protected DmxItem getValidInstance() throws BindingConfigParseException {
		return new DmxColorItem("goodColorItem", "CHANNEL[3/3:100]", null);
	}

	@Test
	public void canBeSetWithHsbCommand() throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);

		HSBType hsb = new HSBType(DecimalType.ZERO, PercentType.HUNDRED,
				PercentType.HUNDRED);
		item.processCommand(service, hsb);

		Mockito.verify(service).setChannelValue(3, 255);
		Mockito.verify(service).setChannelValue(4, 0);
		Mockito.verify(service).setChannelValue(5, 0);
	}

	@Test
	@Override
	public void decreasesWhenDecreaseCommandReceived()
			throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);

		HSBType hsb = new HSBType(new DecimalType(150), new PercentType(50),
				new PercentType(50));
		item.processCommand(service, hsb);

		Mockito.verify(service, Mockito.times(1)).setChannelValue(3, 65);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(4, 129);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(5, 97);

		item.processCommand(service, IncreaseDecreaseType.DECREASE);

		Mockito.verify(service, Mockito.times(1)).setChannelValue(3, 57);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(4, 116);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(5, 87);
	}

	@Test
	@Override
	public void increasesWhenIncreaseCommandReceived()
			throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);

		HSBType hsb = new HSBType(new DecimalType(150), new PercentType(50),
				new PercentType(50));
		item.processCommand(service, hsb);

		Mockito.verify(service, Mockito.times(1)).setChannelValue(3, 65);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(4, 129);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(5, 97);

		item.processCommand(service, IncreaseDecreaseType.INCREASE);

		Mockito.verify(service, Mockito.times(1)).setChannelValue(3, 70);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(4, 140);
		Mockito.verify(service, Mockito.times(1)).setChannelValue(5, 106);

	}

	@Test
	public void canBeSetWith0PercentType() throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);
		item.processCommand(service, new PercentType(0));
		Mockito.verify(service).setChannelValue(3, 0);
		Mockito.verify(service).setChannelValue(4, 0);
		Mockito.verify(service).setChannelValue(5, 0);
	}

	@Test
	public void canBeSetWithPercentType() throws BindingConfigParseException {

		DmxItem item = getValidInstance();
		DmxService service = Mockito.mock(DmxService.class);
		item.processCommand(service, new PercentType(50));
		Mockito.verify(service).setChannelValue(3, 129);
		Mockito.verify(service).setChannelValue(4, 129);
		Mockito.verify(service).setChannelValue(5, 129);
	}
}
