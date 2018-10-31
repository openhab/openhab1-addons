/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import org.openhab.binding.velux.bridge.VeluxBridge;
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
import org.openhab.binding.velux.bridge.comm.json.JCcheckLostNodes;
import org.openhab.binding.velux.bridge.comm.json.JCdetectProducts;
import org.openhab.binding.velux.bridge.comm.json.JCidentifyProduct;
import org.openhab.binding.velux.bridge.comm.json.JCrunScene;
import org.openhab.binding.velux.bridge.comm.json.JCsetSilentMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLIP-based 3rd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides the one-and-only protocol specific 1st-level communication class.
 * Additionally it provides all methods for different gateway interactions.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link SlipBridgeAPI#bridgeDirectCommunicate} as base-level SLIP-specific communication with the bridge,</LI>
 * <LI></LI>
 * <LI>{@link SlipBridgeAPI#checkLostNodes} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#detectProducts} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getDeviceStatus} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getFirmware} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getLANConfig} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getProduct} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getProducts} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getScenes} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getWLANConfig} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#identifyProduct} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#login} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#logout} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#modifyHouseStatusMonitor} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#runScene} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#setSilentMode} as method for ...</LI>
 * </UL>
 * <P>
 * As most derived class of the several inheritance levels it defines an
 * interfacing method {@link VeluxBridge#bridgeAPI} which
 * returns the SLIP-protocol-specific communication for gateway interaction.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SlipBridgeAPI extends SlipVeluxBridge implements BridgeAPI  {
	private final Logger logger = LoggerFactory.getLogger(SlipBridgeAPI.class);

	private final static CheckLostNodes checkLostNodes = new JCcheckLostNodes();
	private final static DetectProducts detectProducts = new JCdetectProducts();
	private final static GetDeviceStatus getDeviceStatus = new SCgetDeviceStatus();
	private final static GetFirmware getFirmware = new SCgetFirmware();
	private final static GetLANConfig getLANConfig = new SCgetLANConfig();
	private final static GetProduct getProduct = new SCgetProduct();
	private final static GetProducts getProducts = new SCgetProducts();
	private final static GetScenes getScenes = new SCgetScenes();
	private final static GetWLANConfig getWLANConfig = new SCgetWLANConfig();
	private final static IdentifyProduct identifyProduct = new JCidentifyProduct();
	private final static Login login = new SClogin();
	private final static Logout logout = new SClogout();
	private final static ModifyHouseStatusMonitor modifyHouseStatusMonitor = new SCmodifyHouseStatusMonitor();
	private final static RunScene runScene = new JCrunScene();
	private final static SendCommand sendCommand = new SCsendCommand();
	private final static SetSilentMode setSilentMode = new JCsetSilentMode();

	/**
	 * Constructor.<P>
	 * Inherits the initialization of the binding-wide instance for dealing for common informations and
	 * initializes the handler {@link org.openhab.binding.velux.bridge.comm.slip.SlipVeluxBridge#bridgeAPI SlipVeluxBridge.bridgeAPI}
	 * to pass the interface methods.
	 * 
 	 * @param bridgeInstance   refers to the binding-wide instance for dealing for common informations.
	 */
	public SlipBridgeAPI(VeluxBridgeInstance bridgeInstance) {
		super(bridgeInstance);
		logger.trace("SlipBridgeAPI(constructor) called.");
		this.bridgeAPI = this;
	}


	@Override
	public boolean bridgeDirectCommunicate(BridgeCommunicationProtocol communication, boolean useAuthentication) {
		logger.trace("bridgeDirectCommunicate({},{}) called.", communication, useAuthentication);
		return this.bridgeDirectCommunicate((SlipBridgeCommunicationProtocol) communication, useAuthentication);
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
	public GetProduct getProduct() {
		return getProduct;
	}
	@Override
	public GetProducts getProducts() {
		return getProducts;
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
		return modifyHouseStatusMonitor;
	}
	@Override
	public RunScene runScene() {
		return runScene;
	}
	@Override
	public SendCommand sendCommand() {
		return sendCommand;
	}
	@Override
	public SetSilentMode setSilentMode() {
		return setSilentMode;
	}

}
/**
 * end-of-bridge/comm/slip/API.java
 */
