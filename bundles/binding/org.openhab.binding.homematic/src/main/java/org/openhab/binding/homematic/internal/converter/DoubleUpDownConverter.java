package org.openhab.binding.homematic.internal.converter;

import org.openhab.core.library.types.UpDownType;

/**
 * Converts UpDown Command / State to a Double value. Currently used for roller
 * shutter control.<br>
 * DOWN = 0, UP = 1
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class DoubleUpDownConverter extends StateConverter<Double, UpDownType> {

    @Override
    protected UpDownType convertToImpl(Double source) {
        return null;
    }

    @Override
    protected Double convertFromImpl(UpDownType source) {
        return source.equals(UpDownType.DOWN) ? new Double(0) : new Double(1);
    }

}
