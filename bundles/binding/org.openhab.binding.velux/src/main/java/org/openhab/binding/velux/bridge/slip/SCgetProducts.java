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

import org.openhab.binding.velux.bridge.common.GetProducts;
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
 * <B>Retrieve Products</B>
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
 * <LI>{@link #getProducts()} to retrieve the currently registered products.</LI>
 * </UL>
 *
 * @see GetProducts
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCgetProducts extends GetProducts implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCgetProducts.class);

    private static final String DESCRIPTION = "Retrieve Products";
    private static final Command COMMAND = Command.GW_GET_ALL_NODES_INFORMATION_REQ;

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

    private VeluxProduct[] productArray;
    private int nextProductArrayItem;

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
        requestData = new byte[0];
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = false;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_GET_ALL_NODES_INFORMATION_CFM:
                logger.trace("setResponse(): got GW_GET_ALL_NODES_INFORMATION_CFM.");
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 2)) {
                    finished = true;
                    break;
                }
                int cfmStatus = responseData.getOneByteValue(0);
                int cfmTotalNumberOfNodes = responseData.getOneByteValue(1);
                logger.trace("setResponse(): status={}.", cfmStatus);
                logger.trace("setResponse(): TotalNumberOfNodes={}.", cfmTotalNumberOfNodes);

                // Initialize storage area
                productArray = null;
                nextProductArrayItem = 0;
                switch (cfmStatus) {
                    case 0:
                        logger.trace("setResponse(): returned status: OK - Request accepted.");
                        productArray = new VeluxProduct[cfmTotalNumberOfNodes];
                        break;
                    case 1:
                        logger.trace("setResponse(): returned status: Error – System table empty.");
                        finished = true;
                        break;
                    default:
                        finished = true;
                        logger.warn("setResponse({}): returned status={} (Reserved/unknown).",
                                Command.get(responseCommand).toString(), cfmStatus);
                        break;
                }
                break;
            case GW_GET_ALL_NODES_INFORMATION_NTF:
                logger.trace("setResponse(): got GW_GET_ALL_NODES_INFORMATION_NTF.");
                // ToDo: discuss with Velux the real length of response.
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 124)) {
                    finished = true;
                    break;
                }
                if (productArray == null) {
                    logger.warn("setResponse({}): sequence of answers unexpected.",
                            Command.get(responseCommand).toString());
                    finished = true;
                    break;
                }
                // Extracting information items
                int ntfNodeID = responseData.getOneByteValue(0);
                logger.trace("setResponse(): ntfNodeID={}.", ntfNodeID);
                int ntfOrder = responseData.getTwoByteValue(1);
                logger.trace("setResponse(): ntfOrder={}.", ntfOrder);
                int ntfPlacement = responseData.getOneByteValue(3);
                logger.trace("setResponse(): ntfPlacement={}.", ntfPlacement);
                String ntfName = responseData.getString(4, 64);
                logger.trace("setResponse(): ntfName={}.", ntfName);
                int ntfVelocity = responseData.getOneByteValue(68);
                logger.trace("setResponse(): ntfVelocity={}.", ntfVelocity);
                int ntfNodeTypeSubType = responseData.getTwoByteValue(69);
                logger.trace("setResponse(): ntfNodeTypeSubType={}.", ntfNodeTypeSubType);
                @SuppressWarnings("unused")
                int ntfProductGroup = responseData.getOneByteValue(71);
                logger.trace("setResponse(): ntfProductGroup={}.", ntfProductGroup);
                int ntfProductType = responseData.getOneByteValue(72);
                logger.trace("setResponse(): ntfProductType={}.", ntfProductType);
                int ntfNodeVariation = responseData.getOneByteValue(73);
                logger.trace("setResponse(): ntfNodeVariation={}.", ntfNodeVariation);
                int ntfPowerMode = responseData.getOneByteValue(74);
                logger.trace("setResponse(): ntfPowerMode={}.", ntfPowerMode);
                @SuppressWarnings("unused")
                int ntfBuildNumber = responseData.getOneByteValue(75);
                byte[] ntfSerialNumber = responseData.getByteArray(76, 8);
                logger.trace("setResponse(): ntfSerialNumber={}.", ntfSerialNumber);
                int ntfState = responseData.getOneByteValue(84);
                logger.trace("setResponse(): ntfState={}.", ntfState);
                int ntfCurrentPosition = responseData.getTwoByteValue(85);
                logger.trace("setResponse(): ntfCurrentPosition={}.", ntfCurrentPosition);
                int ntfTarget = responseData.getTwoByteValue(87);
                logger.trace("setResponse(): ntfTarget={}.", ntfTarget);
                @SuppressWarnings("unused")
                int ntfFP1CurrentPosition = responseData.getTwoByteValue(89);
                logger.trace("setResponse(): ntfFP1CurrentPosition={}.", ntfFP1CurrentPosition);
                @SuppressWarnings("unused")
                int ntfFP2CurrentPosition = responseData.getTwoByteValue(91);
                logger.trace("setResponse(): ntfFP2CurrentPosition={}.", ntfFP2CurrentPosition);
                @SuppressWarnings("unused")
                int ntfFP3CurrentPosition = responseData.getTwoByteValue(93);
                logger.trace("setResponse(): ntfFP3CurrentPosition={}.", ntfFP3CurrentPosition);
                @SuppressWarnings("unused")
                int ntfFP4CurrentPosition = responseData.getTwoByteValue(95);
                logger.trace("setResponse(): ntfFP4CurrentPosition={}.", ntfFP4CurrentPosition);
                int ntfRemainingTime = responseData.getTwoByteValue(97);
                logger.trace("setResponse(): ntfRemainingTime={}.", ntfRemainingTime);
                int ntfTimeStamp = responseData.getFourByteValue(99);
                logger.trace("setResponse(): ntfTimeStamp={}.", ntfTimeStamp);
                @SuppressWarnings("unused")
                int ntfNbrOfAlias = responseData.getOneByteValue(103);
                logger.trace("setResponse(): ntfNbrOfAlias={}.", ntfNbrOfAlias);
                @SuppressWarnings("unused")
                int ntfAliasOne = responseData.getFourByteValue(104);
                logger.trace("setResponse(): ntfAliasOne={}.", ntfAliasOne);
                @SuppressWarnings("unused")
                int ntfAliasTwo = responseData.getFourByteValue(108);
                logger.trace("setResponse(): ntfAliasTwo={}.", ntfAliasTwo);
                @SuppressWarnings("unused")
                int ntfAliasThree = responseData.getFourByteValue(112);
                logger.trace("setResponse(): ntfAliasThree={}.", ntfAliasThree);
                @SuppressWarnings("unused")
                int ntfAliasFour = responseData.getFourByteValue(116);
                logger.trace("setResponse(): ntfAliasFour={}.", ntfAliasFour);
                @SuppressWarnings("unused")
                int ntfAliasFive = responseData.getFourByteValue(120);
                logger.trace("setResponse(): ntfAliasFive={}.", ntfAliasFive);

                logger.trace("setResponse(): nodeId={}.", ntfNodeID);
                logger.trace("setResponse(): nodeName={}.", ntfName);
                logger.trace("setResponse(): productNodeTypeSubType={} ({}).", ntfNodeTypeSubType,
                        VeluxProductType.get(ntfNodeTypeSubType));
                logger.trace("setResponse(): productProductType={}.", ntfProductType);
                logger.trace("setResponse(): productCurrentPosition={}.", ntfCurrentPosition);
                logger.trace("setResponse(): productNodeVariation={}.", ntfNodeVariation);

                String commonSerialNumber = new Packet(ntfSerialNumber).toString(":");
                if ((ntfSerialNumber[0] == 0) && (ntfSerialNumber[1] == 0) && (ntfSerialNumber[2] == 0)
                        && (ntfSerialNumber[3] == 0) && (ntfSerialNumber[4] == 0) && (ntfSerialNumber[5] == 0)
                        && (ntfSerialNumber[6] == 0) && (ntfSerialNumber[7] == 0)) {
                    commonSerialNumber = new String(ntfName);
                    logger.debug("setResponse(): device provided invalid serial number, using name '{}' instead.",
                            commonSerialNumber);
                }

                VeluxProduct product = new VeluxProduct(new VeluxProductName(ntfName),
                        VeluxProductType.get(ntfNodeTypeSubType), new ProductBridgeIndex(ntfNodeID), ntfOrder,
                        ntfPlacement, ntfVelocity, ntfNodeVariation, ntfPowerMode, commonSerialNumber, ntfState,
                        ntfCurrentPosition, ntfTarget, ntfRemainingTime, ntfTimeStamp);
                productArray[nextProductArrayItem++] = product;
                success = true;
                break;
            case GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF:
                logger.trace("setResponse(): got GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF.");
                logger.debug("setResponse(): finished-packet received.");
                success = true;
                finished = true;
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

    /*
     * ===========================================================
     * Methods in addition to the interface {@link BridgeCommunicationProtocol}
     * and the abstract class {@link GetProducts}
     */

    @Override
    public VeluxProduct[] getProducts() {
        if (success && finished && (productArray != null)) {
            logger.trace("getProducts(): returning array of {} products.", productArray.length);
            return productArray;
        } else {
            logger.trace("getProducts(): returning null.");
            return null;
        }
    }

}
