/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.comm.GetScenes;
import org.openhab.binding.velux.things.VeluxScene;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProductState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of scene configurations.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SCgetScenes extends GetScenes implements SlipBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(SCgetScenes.class);

	private final static String description = "get Scenes via SLIP";
	private final static Command command = Command.GW_GET_SCENE_LIST_REQ;

	/*
	 * Message Objects
	 */

	private boolean success;
	private boolean finished;

	private int sceneIdx;
	VeluxScene[] scenes;

	/*
	 * Private methods.
	 */
	private void cleanup() {
		logger.trace("cleanup() called.");
		success = false;
		finished = false;
		scenes = new VeluxScene[0];
		logger.trace("cleanup() finished.");
	}


	/*
	 * Constructor.
	 */

	SCgetScenes() {
		logger.trace("SCgetScenes(Constructor) called.");
		cleanup();
		logger.trace("SCgetScenes(Constructor) done.");
	}


	/*
	 * ===========================================================
	 * Methods required for interface {@link BridgeCommunicationProtocol}.
	 */

	@Override
	public String name() {
		return description;
	}

	@Override
	public CommandNumber getRequestCommand() {
		logger.trace("getRequestCommand() return {}.", command.getCommand());
		cleanup();
		return command.getCommand();
	}

	@Override
	public byte[] getRequestDataAsArrayOfBytes() {
		return emptyData;
	}


	public void setResponse(short responseCommand, byte[] thisResponseData){
		logger.trace("setResponse({} with {} bytes of data) called.", Command.get(responseCommand).toString(), thisResponseData.length);
		logger.trace("setResponse(): handling response {} ({}).",
				Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString());
		Packet responseData = new Packet(thisResponseData);
		switch (Command.get(responseCommand)) {
		case GW_GET_SCENE_LIST_CFM:
			if (thisResponseData.length != 1) {
				logger.trace("setResponse(): malformed response packet (length is {} unequal one).", thisResponseData.length);
				finished = true;
				break;
			}
			int ntfTotalNumberOfObjects = responseData.getOneByteValue(0);
			scenes = new VeluxScene[ntfTotalNumberOfObjects];
			if (ntfTotalNumberOfObjects == 0) {
				logger.trace("setResponse(): no scenes defined.");
				success = true;
			} else {
				logger.trace("setResponse(): {} scenes defined.", ntfTotalNumberOfObjects);
			}
			sceneIdx = 0;
			break;
		case GW_GET_SCENE_LIST_NTF:
			if (thisResponseData.length < 1) {
				logger.trace("setResponse(): malformed response packet (length is {} less then one).", thisResponseData.length);
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
						thisResponseData.length, (2+ 65 * ntfNumberOfObject));
				finished = true;
				break;
			}
			for(int objectIndex = 0; objectIndex < ntfNumberOfObject; objectIndex++) {
				int ntfSceneID = responseData.getOneByteValue(1 + 65 * objectIndex);
				int beginOfString = 2 + 65 * objectIndex;
				String ntfSceneName = responseData.getString(beginOfString, 64);
				logger.trace("setResponse(): scene {}, name {}.", ntfSceneID, ntfSceneName);
				scenes[sceneIdx++] = new VeluxScene(ntfSceneName, 1 + 65 * objectIndex, false, new VeluxProductState[0]);
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
			logger.trace("setResponse(): cannot handle response {} ({}).",
					Command.get(responseCommand).toString(), responseCommand);
			finished = true;
		}
		logger.trace("setResponse(): finished={},success={}.", finished, success);
		logger.trace(">>>getScenes(): returning {} scenes.", scenes.length);
	}

	public boolean isCommunicationFinished() {
		return finished;
	}

	@Override
	public boolean isCommunicationSuccessful() {
		return success;
	}

	/**
	 * ===========================================================
	 * <P>Public Methods required for abstract class {@link GetScenes}.
	 */
	public VeluxScene[] getScenes() {
		logger.trace("getScenes(): returning {} scenes.", scenes.length);
		return scenes;
	}

}
/**
 * end-of-bridge/comm/slip/SCgetScenes.java
 */
