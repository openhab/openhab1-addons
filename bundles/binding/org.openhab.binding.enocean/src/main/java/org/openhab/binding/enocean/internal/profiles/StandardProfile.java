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
package org.openhab.binding.enocean.internal.profiles;

import org.enocean.java.common.ParameterAddress;
import org.enocean.java.common.values.Value;
import org.enocean.java.eep.RockerSwitch;
import org.enocean.java.eep.SingleInputContact;
import org.enocean.java.eep.TemperaturSensor;
import org.openhab.binding.enocean.internal.converter.ButtonStateConverter;
import org.openhab.binding.enocean.internal.converter.ContactStateConverter;
import org.openhab.binding.enocean.internal.converter.ConverterFactory;
import org.openhab.binding.enocean.internal.converter.StateConverter;
import org.openhab.binding.enocean.internal.converter.TemperatureNumberWithUnitConverter;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default profile which maps some parameters to some states / command.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class StandardProfile extends BasicProfile {

    private static final Logger logger = LoggerFactory.getLogger(StandardProfile.class);

    private ConverterFactory converterFactory = new ConverterFactory();

    public StandardProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
        converterFactory.addStateConverter(TemperaturSensor.PARAMETER_ID, DecimalType.class, TemperatureNumberWithUnitConverter.class);
        converterFactory.addStateConverter(RockerSwitch.BUTTON_I, OnOffType.class, ButtonStateConverter.class);
        converterFactory.addStateConverter(RockerSwitch.BUTTON_O, OnOffType.class, ButtonStateConverter.class);
        converterFactory.addStateConverter(SingleInputContact.PARAMETER_ID, OpenClosedType.class, ContactStateConverter.class);
    }

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            StateConverter<?, ?> converter = converterFactory.getToStateConverter(parameterAddress.getParameterId(), item);
            if (converter == null) {
                logger.warn("No converter found for " + parameterAddress + " - doing nothing.");
                return;
            }
            State value = converter.convertTo(valueObject);
            logger.debug("Received new value {} for items {}", value, items);
            eventPublisher.postUpdate(item.getName(), value);
        }
    }

}
