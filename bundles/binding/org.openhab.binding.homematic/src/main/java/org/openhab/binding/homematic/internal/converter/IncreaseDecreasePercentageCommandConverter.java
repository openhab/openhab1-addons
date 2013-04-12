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

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Applies an {@link IncreaseDecreaseType} to a {@link PercentType}. The resulting {@link PercentType} is increased / decrease by 10%
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class IncreaseDecreasePercentageCommandConverter extends CommandConverter<PercentType, IncreaseDecreaseType> {

    private static final Logger logger = LoggerFactory.getLogger(IncreaseDecreasePercentageCommandConverter.class);

    private static final int INCREASE = 10;
    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;

    @Override
    protected PercentType convertImpl(State actualState, IncreaseDecreaseType command) {
        if (actualState instanceof PercentType) {
            PercentType actualPercentage = (PercentType) actualState;
            return calculateNewState(command, actualPercentage);
        } else if (actualState instanceof UnDefType) {
            return calculateNewState(command, new PercentType(0));
        } else {
            logger.warn("Could not increase / decrease actualState " + actualState + ". Expected PercentageType, got " + actualState.getClass());
            return null;
        }
    }

    private PercentType calculateNewState(IncreaseDecreaseType command, PercentType actualPercentage) {
        if (command.equals(IncreaseDecreaseType.INCREASE)) {
            int newValue = increaseValue(actualPercentage.intValue());
            return new PercentType(newValue);
        } else {
            int newValue = decreaseValue(actualPercentage.intValue());
            return new PercentType(newValue);
        }
    }

    private int decreaseValue(int value) {
        value = value - INCREASE;
        if (value < MIN_VALUE) {
            value = MIN_VALUE;
        }
        return value;
    }

    private int increaseValue(int value) {
        value = value + INCREASE;
        if (value > MAX_VALUE) {
            value = MAX_VALUE;
        }
        return value;
    }

}
