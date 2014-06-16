package org.openhab.binding.enocean.internal.converter;

import java.math.BigDecimal;
import org.enocean.java.common.values.NumberWithUnit;
import org.enocean.java.common.values.Unit;
import org.openhab.core.library.types.PercentType;

public class DimValueConverter extends StateConverter<NumberWithUnit, PercentType> {

    @Override
    protected PercentType convertToImpl(NumberWithUnit source) {
        return new PercentType((BigDecimal) source.getValue());
    }

    @Override
    protected NumberWithUnit convertFromImpl(PercentType source) {
        return new NumberWithUnit(Unit.LEVEL, source.byteValue());
    }

}