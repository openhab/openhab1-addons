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

import org.openhab.binding.velux.bridge.common.GetProduct;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProduct;
import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.openhab.binding.velux.things.VeluxProductName;
import org.openhab.binding.velux.things.VeluxProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Retrieve Product</B>
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
 * <LI>{@link #setProductId(int)} to define the one specific product.</LI>
 * <LI>{@link #getProduct} to retrieve one specific product.</LI>
 * </UL>
 *
 * @see GetProduct
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCgetProduct extends GetProduct implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCgetProduct.class);

    private static final String DESCRIPTION = "Retrieve Product";
    private static final Command COMMAND = Command.GW_GET_NODE_INFORMATION_REQ;

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private int reqNodeID;

    /*
     * ===========================================================
     * Message Objects
     */

    private byte[] requestData;

    /*
     * ===========================================================
     * Result Objects
     */

    private boolean success = false;
    private boolean finished = false;

    private VeluxProduct product;

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
        logger.trace("getRequestDataAsArrayOfBytes() returns data for retrieving node with id {}.", reqNodeID);
        Packet request = new Packet(new byte[1]);
        request.setOneByteValue(0, reqNodeID);
        requestData = request.toByteArray();
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = false;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_GET_NODE_INFORMATION_CFM:
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 2)) {
                    finished = true;
                    break;
                }
                int cfmStatus = responseData.getOneByteValue(0);
                int cfmNodeID = responseData.getOneByteValue(1);
                switch (cfmStatus) {
                    case 0:
                        logger.trace("setResponse(): returned status: OK - Request accepted.");
                        if (!KLF200Response.check4matchingNodeID(logger, reqNodeID, cfmNodeID)) {
                            finished = true;
                        }
                        break;
                    case 1:
                        finished = true;
                        logger.trace("setResponse(): returned status: Error – Request rejected.");
                        break;
                    case 2:
                        finished = true;
                        logger.trace("setResponse(): returned status: Error – Invalid node index.");
                        break;
                    default:
                        finished = true;
                        logger.warn("setResponse({}): returned status={} (Reserved/unknown).",
                                Command.get(responseCommand).toString(), cfmStatus);
                        break;
                }
                break;

            case GW_GET_NODE_INFORMATION_NTF:
                finished = true;
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 124)) {
                    break;
                }
                // Extracting information items
                int ntfNodeID = responseData.getOneByteValue(0);
                int nodeOrder = responseData.getTwoByteValue(1);
                int nodePlacement = responseData.getOneByteValue(3);
                String nodeName = responseData.getString(4, 64);
                int productVelocity = responseData.getOneByteValue(68);
                int productNodeTypeSubType = responseData.getTwoByteValue(69);
                @SuppressWarnings("unused")
                int ntfProductGroup = responseData.getTwoByteValue(71);
                int productProductType = responseData.getOneByteValue(72);
                int productNodeVariation = responseData.getOneByteValue(73);
                int productPowerMode = responseData.getOneByteValue(74);
                @SuppressWarnings("unused")
                int ntfBuildNumber = responseData.getOneByteValue(75);
                byte[] productSerialNumber = responseData.getByteArray(76, 8);
                int productState = responseData.getOneByteValue(84);
                int productCurrentPosition = responseData.getTwoByteValue(85);
                int productTarget = responseData.getTwoByteValue(87);
                @SuppressWarnings("unused")
                int productFP1CurrentPosition = responseData.getTwoByteValue(89);
                @SuppressWarnings("unused")
                int productFP2CurrentPosition = responseData.getTwoByteValue(91);
                @SuppressWarnings("unused")
                int productFP3CurrentPosition = responseData.getTwoByteValue(93);
                @SuppressWarnings("unused")
                int productFP4CurrentPosition = responseData.getTwoByteValue(95);
                int productRemainingTime = responseData.getFourByteValue(97);
                int productTimeStamp = responseData.getFourByteValue(99);
                @SuppressWarnings("unused")
                int productNbrOfAlias = responseData.getOneByteValue(103);
                @SuppressWarnings("unused")
                int ntfAliasOne = responseData.getFourByteValue(104);
                @SuppressWarnings("unused")
                int ntfAliasTwo = responseData.getFourByteValue(108);
                @SuppressWarnings("unused")
                int ntfAliasThree = responseData.getFourByteValue(112);
                @SuppressWarnings("unused")
                int ntfAliasFour = responseData.getFourByteValue(116);
                @SuppressWarnings("unused")
                int ntfAliasFive = responseData.getFourByteValue(120);

                if (!KLF200Response.check4matchingNodeID(logger, reqNodeID, ntfNodeID)) {
                    break;
                }

                logger.trace("setResponse(): nodeId={}.", ntfNodeID);
                logger.trace("setResponse(): nodeName={}.", nodeName);
                logger.trace("setResponse(): productNodeTypeSubType={}.", productNodeTypeSubType);
                logger.trace("setResponse(): productNodeTypeSubTypeSYMBOLIC={}.",
                        VeluxProductType.get(productNodeTypeSubType));
                logger.trace("setResponse(): productProductType={}.", productProductType);
                logger.trace("setResponse(): productCurrentPosition={}.", productCurrentPosition);
                logger.trace("setResponse(): productNodeVariation={}.", productNodeVariation);

                String commonSerialNumber = new Packet(productSerialNumber).toString(":");
                if ((productSerialNumber[0] == 0) && (productSerialNumber[1] == 0) && (productSerialNumber[2] == 0)
                        && (productSerialNumber[3] == 0) && (productSerialNumber[4] == 0)
                        && (productSerialNumber[5] == 0) && (productSerialNumber[6] == 0)
                        && (productSerialNumber[7] == 0)) {
                    commonSerialNumber = new String(nodeName);
                    logger.debug("setResponse(): device provided invalid serial number, using name '{}' instead.",
                            commonSerialNumber);
                }

                product = new VeluxProduct(new VeluxProductName(nodeName), VeluxProductType.get(productNodeTypeSubType),
                        new ProductBridgeIndex(ntfNodeID), nodeOrder, nodePlacement, productVelocity,
                        productNodeVariation, productPowerMode, commonSerialNumber, productState,
                        productCurrentPosition, productTarget, productRemainingTime, productTimeStamp);
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
     * and the abstract class {@link GetProduct}
     */

    @Override
    public void setProductId(int nodeId) {
        logger.trace("setProductId({}) called.", nodeId);
        this.reqNodeID = nodeId;
        return;
    }

    @Override
    public VeluxProduct getProduct() {
        logger.trace("getProduct(): returning product {}.", product);
        return product;
    }

}
