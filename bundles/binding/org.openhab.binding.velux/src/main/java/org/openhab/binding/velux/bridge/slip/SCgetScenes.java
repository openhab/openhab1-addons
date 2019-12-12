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
package org.openhab.binding.velux.bridge.slip;

import org.openhab.binding.velux.bridge.common.GetScenes;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProductState;
import org.openhab.binding.velux.things.VeluxScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Retrieve Scenes</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class
 * itself.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface {@link org.openhab.binding.velux.bridge.json.JsonBridgeCommunicationProtocol
 * SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link #getScenes()} to retrieve the set of current scenes.</LI>
 * </UL>
 *
 * @see GetScenes
 * @see SlipBridgeCommunicationProtocol
 *
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCgetScenes extends GetScenes implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCgetScenes.class);

    private static final String DESCRIPTION = "Retrieve Scenes";
    private static final Command COMMAND = Command.GW_GET_SCENE_LIST_REQ;

    /*
     * Message Objects
     */

    private boolean success;
    private boolean finished;

    private int sceneIdx;
    private VeluxScene[] scenes;

    /*
     * ===========================================================
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return DESCRIPTION;
    }

    @Override
    public CommandNumber getRequestCommand() {
        success = false;
        finished = false;
        logger.debug("getRequestCommand() returns {} ({}).", COMMAND.name(), COMMAND.getCommand());
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        return EMPTYDATA;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = false;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_GET_SCENE_LIST_CFM:
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 1)) {
                    finished = true;
                    break;
                }
                int ntfTotalNumberOfObjects = responseData.getOneByteValue(0);
                scenes = new VeluxScene[ntfTotalNumberOfObjects];
                if (ntfTotalNumberOfObjects == 0) {
                    logger.trace("setResponse(): no scenes defined.");
                    success = true;
                    finished = true;
                } else {
                    logger.trace("setResponse(): {} scenes defined.", ntfTotalNumberOfObjects);
                }
                sceneIdx = 0;
                break;
            case GW_GET_SCENE_LIST_NTF:
                if (thisResponseData.length < 1) {
                    logger.trace("setResponse(): malformed response packet (length is {} less than one).",
                            thisResponseData.length);
                    finished = true;
                    break;
                }
                int ntfNumberOfObject = responseData.getOneByteValue(0);
                logger.trace("setResponse(): NTF number of objects={}.", ntfNumberOfObject);
                if (ntfNumberOfObject == 0) {
                    logger.trace("setResponse(): finished.");
                    finished = true;
                    success = true;
                }
                if (thisResponseData.length != (2 + 65 * ntfNumberOfObject)) {
                    logger.trace("setResponse(): malformed response packet (real length {}, expected length {}).",
                            thisResponseData.length, (2 + 65 * ntfNumberOfObject));
                    finished = true;
                    break;
                }
                for (int objectIndex = 0; objectIndex < ntfNumberOfObject; objectIndex++) {
                    int ntfSceneID = responseData.getOneByteValue(1 + 65 * objectIndex);
                    int beginOfString = 2 + 65 * objectIndex;
                    String ntfSceneName = responseData.getString(beginOfString, 64);
                    logger.trace("setResponse(): scene {}, name {}.", ntfSceneID, ntfSceneName);
                    scenes[sceneIdx++] = new VeluxScene(ntfSceneName, ntfSceneID, false, new VeluxProductState[0]);
                }
                int ntfRemainingNumberOfObject = responseData.getOneByteValue(1 + 65 * ntfNumberOfObject);
                logger.trace("setResponse(): {} scenes remaining.", ntfRemainingNumberOfObject);
                if (ntfRemainingNumberOfObject == 0) {
                    logger.trace("setResponse(): finished.");
                    finished = true;
                    success = true;
                }
                break;
            default:
                KLF200Response.errorLogging(logger, responseCommand);
                finished = true;
        }
        KLF200Response.outroLogging(logger, success, finished);
    }

    @Override
    public boolean isCommunicationFinished() {
        return finished;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return success;
    }

    /**
     * ===========================================================
     * <P>
     * Public Methods required for abstract class {@link GetScenes}.
     */
    @Override
    public VeluxScene[] getScenes() {
        logger.trace("getScenes(): returning {} scenes.", scenes.length);
        return scenes;
    }

}
