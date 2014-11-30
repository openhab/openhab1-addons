package org.openhab.binding.mailcontrol.model.command;

import org.openhab.core.types.Command;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public interface OpenhabCommandTransformable {
    Command getCommandValue();
}
