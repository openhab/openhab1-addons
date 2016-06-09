/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CubeCommand} is the base class for commands to be send to the MAX! Cube.
 *
 * @author Marcel Verpaalen - Initial contribution, backport from OH2
 * @since 1.9
 *
 */
public abstract class CubeCommand {

    protected final static Logger logger = LoggerFactory.getLogger(CubeCommand.class);

    /**
     * @return the String to be send to the MAX! Cube
     */
    public abstract String getCommandString();

    /**
     * @return the String expected to be received from the Cube to signify the
     *         end of the message
     */
    public abstract String getReturnStrings();

}
