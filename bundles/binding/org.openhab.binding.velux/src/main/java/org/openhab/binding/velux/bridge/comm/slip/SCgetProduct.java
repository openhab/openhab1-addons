/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetProduct;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProduct;
import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.openhab.binding.velux.things.VeluxProductName;
import org.openhab.binding.velux.things.VeluxProductPosition;
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
public class SCgetProduct extends GetProduct implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SCgetProduct.class);

	private final static String description = "get Product via SLIP";
	private final static Command command = Command.GW_GET_NODE_INFORMATION_REQ;


	/* ===========================================================
	 * Message Objects
	 */

	private byte[] requestData;
	private int nodeIdReq;

	private final static int RDI_NodeId = 0;

	/* ===========================================================
	 * Result Objects
	 */

	private boolean success = false;
	private boolean finished = false;

	private VeluxProduct product;

	/* ===========================================================
	 * Constructor Method
	 */

	public SCgetProduct() {
		logger.trace("SCgetProduct(Constructor) called.");
		requestData = new byte[1];
		logger.trace("SCgetProduct(Constructor) done.");
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
		case GW_GET_NODE_INFORMATION_CFM:
			if (thisResponseData.length != 2) {
				finished = true;
				logger.error("setResponse(): malformed response packet (length is {} unequal two).", thisResponseData.length);
				break;
			}
			int responseStatus = responseData.getOneByteValue(0);
			int nodeIdCfm = responseData.getOneByteValue(1);
			switch (responseStatus) {
			case 0:
				logger.trace("setResponse(): returned status: OK - Request accepted.");
				if (nodeIdCfm != this.nodeIdReq) {
					finished = true;
					logger.error("setResponse(): returned nodeId {} is NOT the requested nodeId {}.",nodeIdCfm,this.nodeIdReq);
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
				logger.error("setResponse(): returned status={} (Reserved/unknown).",responseStatus);
				break;
			}
			break;
		case GW_GET_NODE_INFORMATION_NTF:
			finished = true;
			if (responseData.length()< 103) {
				logger.error("setResponse(): malformed response packet (too short: length is {}).", responseData.length());
				break;
			}
			// Extracting information items
			int nodeIdNtf = responseData.getOneByteValue(0);
			int nodeOrder = responseData.getTwoByteValue(1);
			int nodePlacement = responseData.getOneByteValue(3);
			String nodeName = responseData.getString(4, 64);
			int productVelocity = responseData.getOneByteValue(68);
			int productNodeTypeSubType = responseData.getTwoByteValue(69);
			int productProductType = responseData.getTwoByteValue(71);
			int productNodeVariation = responseData.getOneByteValue(73);
			int productPowerMode = responseData.getOneByteValue(74);
			byte[] productSerialNumber = responseData.getByteArray(75,8);
			int productState = responseData.getOneByteValue(83);
			int productCurrentPosition = responseData.getTwoByteValue(84);
			int productTarget = responseData.getTwoByteValue(86);
			@SuppressWarnings("unused") int productFP1CurrentPosition = responseData.getTwoByteValue(88);
			@SuppressWarnings("unused") int productFP2CurrentPosition = responseData.getTwoByteValue(90);
			@SuppressWarnings("unused") int productFP3CurrentPosition = responseData.getTwoByteValue(92);
			@SuppressWarnings("unused") int productFP4CurrentPosition = responseData.getTwoByteValue(94);
			int productRemainingTime = responseData.getFourByteValue(96);
			int productTimeStamp = responseData.getTwoByteValue(98);
			@SuppressWarnings("unused") int productNbrOfAlias = responseData.getTwoByteValue(102);
			// ToDo: try to elaborate for what reason these aliases exist 
//			int productAlias... = responseData[104..];
			logger.trace("setResponse(): nodeId={} (requested {}).",nodeIdNtf,this.nodeIdReq);
			logger.trace("setResponse(): nodeName={}.",nodeName);
			logger.trace("setResponse(): productNodeTypeSubType={}.",productNodeTypeSubType);
			logger.trace("setResponse(): productNodeTypeSubTypeSYMBOLIC={}.",VeluxProductType.get(productNodeTypeSubType));
			logger.trace("setResponse(): productProductType={}.",productProductType);
			logger.warn("setResponse(): productCurrentPosition={}.",productCurrentPosition);
			logger.warn("setResponse(): productCurrentPosition=0x{},0x{}.",thisResponseData[84],thisResponseData[85]);
			logger.trace("setResponse(): productNodeVariation={}.",productNodeVariation);
			
//			ToDo: this enables debugging output for unknown actuator position.
			if (!new VeluxProductPosition(productCurrentPosition).isKnown()) {
				logger.error("setResponse(): cannot determine position. Packet is {}.", responseData.toString());
			}
			
			if (nodeIdNtf != this.nodeIdReq) {
				finished = true;
				logger.error("setResponse(): returned nodeId {} is NOT the requested nodeId {}.",nodeIdNtf,this.nodeIdReq);
				break;
			}
			product = new VeluxProduct(new VeluxProductName(nodeName), VeluxProductType.get(productNodeTypeSubType), new ProductBridgeIndex(nodeIdNtf),
						nodeOrder, nodePlacement, productVelocity, productNodeVariation, productPowerMode,
						new Packet(productSerialNumber).toString(":"),
						productState, productCurrentPosition, productTarget, productRemainingTime, productTimeStamp);
			success = true;
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

	public void setProductId(int nodeId) {
		logger.trace("setProductId({}) called.",nodeId);
		this.nodeIdReq = nodeId;
		this.requestData[RDI_NodeId] = (byte) nodeId;
		return;
	}

	public VeluxProduct getProduct() {
		logger.trace("getProduct(): returning product {}.", product);
		return product;
	}


}
/**
 * end-of-bridge/comm/SCgetProducts.java
 */
