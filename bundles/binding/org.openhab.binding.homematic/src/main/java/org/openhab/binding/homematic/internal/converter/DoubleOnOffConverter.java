package org.openhab.binding.homematic.internal.converter;

import org.openhab.core.library.types.OnOffType;

/**
 * Converts a double value into a On/Off type. OFF = 0, ON = 1.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class DoubleOnOffConverter extends StateConverter<Double, OnOffType> {

    @Override
    protected OnOffType convertToImpl(Double source) {
        return null;
    }

    @Override
    protected Double convertFromImpl(OnOffType source) {
        return source.equals(OnOffType.OFF) ? new Double(0) : new Double(1);
    }

}
