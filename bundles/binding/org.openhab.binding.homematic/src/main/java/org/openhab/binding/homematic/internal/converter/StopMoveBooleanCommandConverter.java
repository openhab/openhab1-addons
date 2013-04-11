package org.openhab.binding.homematic.internal.converter;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.types.State;

/**
 * Converts a Stop/Move command to a ON/ OFF type.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 *
 */
public class StopMoveBooleanCommandConverter extends CommandConverter<OnOffType, StopMoveType> {

    @Override
    protected OnOffType convertImpl(State actualState, StopMoveType command) {
        return command.equals(StopMoveType.MOVE) ? OnOffType.ON : OnOffType.OFF;
    }

}
