package org.openhab.binding.enocean.internal.converter;

import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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