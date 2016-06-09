/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.command;

/**
 * The {@link Q_Command} Quits the connection to the MAX! Cube.
 *
 * @author Marcel Verpaalen - Initial Contribution, backport from OH2
 * @since 1.9
 */

public class Q_Command extends CubeCommand {

    @Override
    public String getCommandString() {
        return "q:" + '\r' + '\n';
    }

    @Override
    public String getReturnStrings() {
        return null;
    }
}
