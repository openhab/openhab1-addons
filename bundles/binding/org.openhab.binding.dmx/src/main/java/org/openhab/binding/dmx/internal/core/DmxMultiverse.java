/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Multiverse. Contains several DMX universes
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */
public class DmxMultiverse {

    private static final Logger logger = LoggerFactory.getLogger(DmxMultiverse.class);

    private Map<Integer, DmxUniverse> universes = new HashMap<Integer, DmxUniverse>();

    /**
     * get a universe, create if not found
     *
     * @param universeId
     *            id of requested universe
     * @return
     *         the universe itself
     */
    public DmxUniverse getUniverse(int universeId) {
        Integer thisUniverseId = Integer.valueOf(universeId);
        if (universes.containsKey(thisUniverseId)) {
            logger.trace("returning existing universe {}", thisUniverseId);
            return universes.get(thisUniverseId);
        } else {
            universes.put(thisUniverseId, new DmxUniverse(universeId));
            logger.debug("creating and returning universe {}", thisUniverseId);
            return universes.get(thisUniverseId);
        }
    }

    public Map<Integer, DmxUniverse> getUniverses() {
        return universes;
    }

    public DmxChannel getChannel(DmxSimpleChannel channelId) {
        return getUniverse(channelId.getUniverseId()).getChannel(channelId.getChannelId());
    }

    public int getLength() {
        return universes.size();
    }
}
