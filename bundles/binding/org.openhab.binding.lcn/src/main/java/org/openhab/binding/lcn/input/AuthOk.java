/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.lcn.input;

import java.util.Collection;
import java.util.LinkedList;

import org.openhab.binding.lcn.common.PckParser;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Authentication success received from LCN-PCHK.
 *
 * @author Tobias J�ttner
 */
public class AuthOk extends Input {

    /**
     * Tries to parse the given input received from LCN-PCHK.
     * 
     * @param input the input
     * @return list of {@link AuthOk} (might be empty, but not null}
     */
    static Collection<Input> tryParseInput(String input) {
        LinkedList<Input> ret = new LinkedList<Input>();
        if (input.equals(PckParser.AUTH_OK)) {
            ret.add(new AuthOk());
        }
        return ret;
    }

    /**
     * Notifies the connection about the successful authentication.
     * {@inheritDoc}
     */
    @Override
    public void process(Connection conn) {
        conn.onAuthOk();
    }

    /** {@inheritDoc} */
    @Override
    public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item,
            EventPublisher eventPublisher) {
        // Not used for visualization
        return false;
    }

}
