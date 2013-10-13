/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
