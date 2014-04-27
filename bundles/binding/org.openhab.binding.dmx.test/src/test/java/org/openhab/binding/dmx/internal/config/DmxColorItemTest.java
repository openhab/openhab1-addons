/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
