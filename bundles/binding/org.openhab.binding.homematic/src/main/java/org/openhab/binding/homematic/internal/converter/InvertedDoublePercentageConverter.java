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
package org.openhab.binding.homematic.internal.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.openhab.core.library.types.PercentType;

/**
 * Converts a Double value into a {@link PercentType}. The resulting
 * {@link PercentType} is inverted (e.g. a double value of 20 gets 80 percent, a
 * 40 gets 60) and rounded to 3 digits.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3
 */
public class InvertedDoublePercentageConverter extends StateConverter<Double, PercentType> {

    @Override
    protected PercentType convertToImpl(Double source) {
        BigDecimal bValue = BigDecimal.valueOf(100 - source * 100);
        bValue.setScale(3, RoundingMode.HALF_UP);
        return new PercentType(bValue);
    }

    @Override
    protected Double convertFromImpl(PercentType source) {
        Double value = (double) (100 - source.intValue()) / 100;
        return value;
    }

}
