/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetProducts;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProduct;
import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.openhab.binding.velux.things.VeluxProductName;
import org.openhab.binding.velux.things.VeluxProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of product configurations.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCgetProducts extends GetProducts implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SCgetProducts.class);

	private final static String description = "get all Products via SLIP";
	private final static Command command = Command.GW_GET_ALL_NODES_INFORMATION_REQ;


	/* ===========================================================
	 * Message Objects
	 */

	private byte[] requestData;

	/* ===========================================================
	 * Result Objects
	 */

	private boolean success = false;
	private boolean finished = false;

	private VeluxProduct[] productArray;
	private int nextProductArrayItem;

	/* ===========================================================
	 * Constructor Method
	 */

	public SCgetProducts() {
		logger.trace("SCgetProducts(Constructor) called.");
		requestData = new byte[0];
		logger.trace("SCgetProducts(Constructor) done.");
	}

	/* ===========================================================
	 * Methods required for interface {@link BridgeCommunicationProtocol}.
	 */

	@Override
	public String name() {
		return description;
	}

	@Override
	public CommandNumber getRequestCommand() {
		success = false;
		finished = false;
		logger.trace("getRequestCommand() returns {}.", command.getCommand());
		return command.getCommand();
	}

	@Override
	public byte[] getRequestDataAsArrayOfBytes() {
		return requestData;
	}

	public void setResponse(short responseCommand, byte[] thisResponseData){
		logger.debug("setResponse({} with {} bytes of data) called.", Command.get(responseCommand).toString(), thisResponseData.length);
		logger.trace("setResponse(): handling response {} ({}).",
				Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString());
		Packet responseData = new Packet(thisResponseData);
		success = false;
		finished = false;
		switch (Command.get(responseCommand)) {
		case GW_GET_ALL_NODES_INFORMATION_CFM:
			if (thisResponseData.length != 2) {
				logger.error("setResponse(): malformed response packet (length is {} unequal two).", thisResponseData.length);
				finished = true;
				break;
			}
			int cfmStatus = responseData.getOneByteValue(0);
			int cfmTotalNumberOfNodes = responseData.getOneByteValue(1);
			logger.trace("setResponse(): status={}.",cfmStatus);
			logger.trace("setResponse(): TotalNumberOfNodes={}.",cfmTotalNumberOfNodes);

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
				logger.error("setResponse(): returned status={} (Reserved/unknown).",cfmStatus);
				break;
			}
			break;
		case GW_GET_ALL_NODES_INFORMATION_NTF:
			if (thisResponseData.length != 125) {
				logger.error("setResponse(): malformed response packet (length is {} unequal 125).", thisResponseData.length);
//ToDo: discuss with Velux the real length of response.
//				finished = true;
//				break;
			}
			if (productArray == null) {
				logger.error("setResponse(): sequence of answers unexpected.");
				finished = true;
				break;
			}
			// Extracting information items
			int ntfNodeID = responseData.getOneByteValue(0);
			int ntfOrder = responseData.getTwoByteValue(1);
			int ntfPlacement = responseData.getOneByteValue(3);
			String ntfName = responseData.getString(4, 64);
			int ntfVelocity = responseData.getOneByteValue(68);
			int ntfNodeTypeSubType = responseData.getTwoByteValue(69);
			@SuppressWarnings("unused")int ntfProductGroup = responseData.getOneByteValue(71);
			int ntfProductType = responseData.getOneByteValue(72);
			int ntfNodeVariation = responseData.getOneByteValue(73);
			int ntfPowerMode = responseData.getOneByteValue(74);
			@SuppressWarnings("unused")int ntfBuildNumber = responseData.getOneByteValue(75);
			byte[] ntfSerialNumber = responseData.getByteArray(76,8);
			int ntfState = responseData.getOneByteValue(84);
			int ntfCurrentPosition = responseData.getTwoByteValue(85);
			int ntfTarget = responseData.getTwoByteValue(87);
			@SuppressWarnings("unused") int ntfFP1CurrentPosition = responseData.getTwoByteValue(89);
			@SuppressWarnings("unused") int ntfFP2CurrentPosition = responseData.getTwoByteValue(91);
			@SuppressWarnings("unused") int ntfFP3CurrentPosition = responseData.getTwoByteValue(93);
			@SuppressWarnings("unused") int ntfFP4CurrentPosition = responseData.getTwoByteValue(95);
			int ntfRemainingTime = responseData.getTwoByteValue(97);
			int ntfTimeStamp = responseData.getFourByteValue(99);
			@SuppressWarnings("unused") int ntfNbrOfAlias = responseData.getOneByteValue(103);
			// ToDo: try to elaborate for what reason these aliases exist 
//			int ntfProductAlias... = responseData[104..];
			logger.trace("setResponse(): nodeId={}.",ntfNodeID);
			logger.trace("setResponse(): nodeName={}.",ntfName);
			logger.trace("setResponse(): productNodeTypeSubType={}.",ntfNodeTypeSubType);
			logger.trace("setResponse(): productNodeTypeSubTypeSYMBOLIC={}.",VeluxProductType.get(ntfNodeTypeSubType));
			logger.trace("setResponse(): productProductType={}.",ntfProductType);
			logger.trace("setResponse(): productCurrentPosition={}.",ntfCurrentPosition);
			logger.trace("setResponse(): productNodeVariation={}.",ntfNodeVariation);

			VeluxProduct	product = new VeluxProduct(new VeluxProductName(ntfName), VeluxProductType.get(ntfNodeTypeSubType), new ProductBridgeIndex(ntfNodeID),
						ntfOrder, ntfPlacement, ntfVelocity, ntfNodeVariation, ntfPowerMode,
					new Packet(ntfSerialNumber).toString(":"),
					ntfState, ntfCurrentPosition, ntfTarget, ntfRemainingTime, ntfTimeStamp);
			productArray[nextProductArrayItem++] = product;
			success = true;
			break;
		case GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF:
			logger.error("setResponse(): finished-packet received.");
			success = true;
			finished = true;
			break;
		default:
			logger.error("setResponse(): cannot handle response {} ({}).",
					Command.get(responseCommand).toString(), responseCommand);
			finished = true;
		}
		logger.trace("setResponse(): finished={},success={}.", finished, success);
	}



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
/**
 * end-of-bridge/comm/slip/SCgetProducts.java
 */
