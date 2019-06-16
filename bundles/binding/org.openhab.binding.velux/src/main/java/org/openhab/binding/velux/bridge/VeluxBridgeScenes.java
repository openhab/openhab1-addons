/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.common.GetScenes;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingScenes;
import org.openhab.binding.velux.things.VeluxScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeScenes} represents a complete set of transactions
 * for retrieving of any available scenes into a structure {@link VeluxExistingScenes}
 * defined on the <B>Velux</B> bridge.
 * <P>
 * It provides the following methods:
 * <UL>
 * <LI>{@link #getScenes} for retrieval of information.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * <LI>{@link #autoRefresh} for retrieval of information in case of an
 * empty list of actuators.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxScene
 * @see VeluxExistingScenes
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeScenes {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeScenes.class);

    // Type definitions, class-internal variables

    /**
     * Actuator information consisting of:
     * <ul>
     * <li>isRetrieved (boolean),
     * <li>existingScenes ({@link VeluxExistingScenes}).
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public VeluxExistingScenes existingScenes = new VeluxExistingScenes();
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux scenes,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeScenes() {
        logger.trace("VeluxBridgeScenes(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of scenes.
     *
     * @return a channel describing the overall scenes situation.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Login into bridge, retrieve all scenes and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}. The results
     * are stored within a public structure {@link org.openhab.binding.velux.things.VeluxExistingScenes
     * VeluxExistingScenes}.
     *
     * @param bridge Initialized Velux bridge (communication) handler.
     * @return true if successful, or false otherwise.
     */
    public boolean getScenes(VeluxBridge bridge) {
        logger.trace("getScenes() called.");

        GetScenes bcp = bridge.bridgeAPI().getScenes();
        if (bridge.bridgeCommunicate(bcp) && bcp.isCommunicationSuccessful()) {
            for (VeluxScene scene : bcp.getScenes()) {
                logger.trace("getScenes() found scene {}.", scene.toString());

                VeluxScene veluxScene = new VeluxScene(scene);
                logger.trace("getScenes() storing scene {}.", veluxScene);
                if (!channel.existingScenes.isRegistered(veluxScene)) {
                    channel.existingScenes.register(veluxScene);
                }
                logger.trace("getScenes() stored scene {}.", veluxScene);
            }
            logger.debug("getScenes() finally has found scenes {}.", channel.existingScenes);
            return true;
        } else {
            logger.trace("getScenes() finished with failure.");
            return false;
        }
    }

    /**
     * In case of an empty list of recognized scenes, the method will
     * initiate a product retrieval using {@link #getScenes(VeluxBridge)}.
     *
     * @param bridge Initialized Velux bridge (communication) handler.
     * @return true if at lease one scene was found, and false otherwise.
     */
    public boolean autoRefresh(VeluxBridge bridge) {
        if (channel.existingScenes.getNoMembers() == 0) {
            logger.trace("autoRefresh(): is about to fetch existing scenes.");
            getScenes(bridge);
        }
        return (channel.existingScenes.getNoMembers() > 0);
    }

}
