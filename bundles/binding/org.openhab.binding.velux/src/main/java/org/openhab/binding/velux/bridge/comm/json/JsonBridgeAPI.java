/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.json;

import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.comm.BridgeAPI;
import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.comm.CheckLostNodes;
import org.openhab.binding.velux.bridge.comm.DetectProducts;
import org.openhab.binding.velux.bridge.comm.GetDeviceStatus;
import org.openhab.binding.velux.bridge.comm.GetFirmware;
import org.openhab.binding.velux.bridge.comm.GetLANConfig;
import org.openhab.binding.velux.bridge.comm.GetProduct;
import org.openhab.binding.velux.bridge.comm.GetProducts;
import org.openhab.binding.velux.bridge.comm.GetScenes;
import org.openhab.binding.velux.bridge.comm.GetWLANConfig;
import org.openhab.binding.velux.bridge.comm.IdentifyProduct;
import org.openhab.binding.velux.bridge.comm.Login;
import org.openhab.binding.velux.bridge.comm.Logout;
import org.openhab.binding.velux.bridge.comm.ModifyHouseStatusMonitor;
import org.openhab.binding.velux.bridge.comm.RunScene;
import org.openhab.binding.velux.bridge.comm.SendCommand;
import org.openhab.binding.velux.bridge.comm.SetSilentMode;
import org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of Bridge configuration.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class JsonBridgeAPI extends JsonVeluxBridge implements BridgeAPI  {
	private final Logger logger = LoggerFactory.getLogger(JsonBridgeAPI.class);

	private final static CheckLostNodes checkLostNodes = new JCcheckLostNodes();
	private final static DetectProducts detectProducts = new JCdetectProducts();
	private final static GetDeviceStatus getDeviceStatus = new JCgetDeviceStatus();
	private final static GetFirmware getFirmware = new JCgetFirmware();
	private final static GetLANConfig getLANConfig = new JCgetLANConfig();
	private final static GetProducts getProducts = new JCgetProducts();
	private final static GetScenes getScenes = new JCgetScenes();
	private final static GetWLANConfig getWLANConfig = new JCgetWLANConfig();
	private final static IdentifyProduct identifyProduct = new JCidentifyProduct();
	private final static Login login = new JClogin();
	private final static Logout logout = new JClogout();
	private final static RunScene runScene = new JCrunScene();
	private final static SetSilentMode setSilentMode = new JCsetSilentMode();

	/**
	 * Constructor.<P>
	 * Inherits the initialization of the binding-wide instance for dealing for common informations and
	 * initializes the handler {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge#bridgeAPI JsonVeluxBridge.bridgeAPI}
	 * to pass the interface methods.
	 * 
 	 * @param bridgeInstance   refers to the binding-wide instance for dealing for common informations.
	 */
	public JsonBridgeAPI(VeluxBridgeInstance bridgeInstance) {
		super(bridgeInstance);
		logger.trace("JsonBridgeAPI(constructor) called.");
		this.bridgeAPI = this;
	}


	@Override
	public boolean bridgeDirectCommunicate(BridgeCommunicationProtocol communication, boolean useAuthentication) {
		logger.trace("bridgeDirectCommunicate({},{}) called.", communication, useAuthentication);
		return this.bridgeDirectCommunicate((JsonBridgeCommunicationProtocol) communication, useAuthentication);
	}

	@Override
	public CheckLostNodes checkLostNodes() {
		return checkLostNodes;
	}

	@Override
	public DetectProducts detectProducts() {
		return detectProducts;
	}
	@Override
	public GetDeviceStatus getDeviceStatus() {
		return getDeviceStatus;
	}
	@Override
	public GetFirmware getFirmware() {
		return getFirmware;
	}
	@Override
	public GetLANConfig getLANConfig() {
		return getLANConfig;
	}
	@Override
	public GetProducts getProducts() {
		return getProducts;
	}
	@Override
	public GetProduct getProduct() {
		return null;
	}
	@Override
	public GetScenes getScenes() {
		return getScenes;
	}
	@Override
	public GetWLANConfig getWLANConfig() {
		return getWLANConfig;
	}
	@Override
	public IdentifyProduct identifyProduct() {
		return identifyProduct;
	}
	@Override
	public Login login() {
		return login;
	}
	@Override
	public Logout logout() {
		return logout;
	}
	@Override
	public ModifyHouseStatusMonitor modifyHouseStatusMonitor() {
		return null;
	}
	@Override
	public RunScene runScene() {
		return runScene;
	}
	@Override
	public SendCommand sendCommand() {
		return null;
	}
	@Override
	public SetSilentMode setSilentMode() {
		return setSilentMode;
	}

}
/**
 * end-of-bridge/comm/json/API.java
 */
