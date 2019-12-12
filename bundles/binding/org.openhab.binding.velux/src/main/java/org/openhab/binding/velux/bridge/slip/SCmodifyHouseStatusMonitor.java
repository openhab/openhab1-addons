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

import org.openhab.binding.velux.bridge.common.ModifyHouseStatusMonitor;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Modify HouseStatusMonitor</B>
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
 * <LI>{@link #serviceActivation} to define the new service activation state.</LI>
 * </UL>
 *
 * @see ModifyHouseStatusMonitor
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCmodifyHouseStatusMonitor extends ModifyHouseStatusMonitor implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCmodifyHouseStatusMonitor.class);

    private static final String DESCRIPTION = "Modify HouseStatusMonitor";

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private boolean activateService = false;

    /*
     * ===========================================================
     * Message Objects
     */

    private byte[] requestData = new byte[0];

    /*
     * ===========================================================
     * Result Objects
     */

    private boolean success = false;
    private boolean finished = false;

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
        Command command = activateService ? Command.GW_HOUSE_STATUS_MONITOR_ENABLE_REQ
                : Command.GW_HOUSE_STATUS_MONITOR_DISABLE_REQ;
        success = false;
        finished = false;
        logger.debug("getRequestCommand() returns {} ({}).", command.name(), command.getCommand());
        return command.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        logger.debug("getRequestDataAsArrayOfBytes() data is {}.", new Packet(requestData).toString());
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = true;
        switch (Command.get(responseCommand)) {
            case GW_HOUSE_STATUS_MONITOR_ENABLE_CFM:
                logger.trace("setResponse(): service enable confirmed by bridge.");
                success = true;
                break;
            case GW_HOUSE_STATUS_MONITOR_DISABLE_CFM:
                logger.trace("setResponse(): service disable confirmed by bridge.");
                success = true;
                break;

            default:
                KLF200Response.errorLogging(logger, responseCommand);
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

    /*
     * ===========================================================
     * Methods in addition to the interface {@link BridgeCommunicationProtocol}
     * and the abstract class {@link ModifyHouseStatusMonitor}
     */

    @Override
    public ModifyHouseStatusMonitor serviceActivation(boolean enableService) {
        this.activateService = enableService;
        return this;
    }

}
