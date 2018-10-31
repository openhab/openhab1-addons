/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.openhab.binding.velux.VeluxBindingProvider;
import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.VeluxBridgeDetectProducts;
import org.openhab.binding.velux.bridge.VeluxBridgeDeviceStatus;
import org.openhab.binding.velux.bridge.VeluxBridgeExecute;
import org.openhab.binding.velux.bridge.VeluxBridgeGetFirmware;
import org.openhab.binding.velux.bridge.VeluxBridgeGetProducts;
import org.openhab.binding.velux.bridge.VeluxBridgeGetScenes;
import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.VeluxBridgeLANConfig;
import org.openhab.binding.velux.bridge.VeluxBridgeModifyHouseStatusMonitor;
import org.openhab.binding.velux.bridge.VeluxBridgeSceneMode;
import org.openhab.binding.velux.bridge.VeluxBridgeSendCommand;
import org.openhab.binding.velux.bridge.VeluxBridgeWLANConfig;
import org.openhab.binding.velux.bridge.comm.GetProduct;
import org.openhab.binding.velux.bridge.comm.json.JsonBridgeAPI;
import org.openhab.binding.velux.bridge.comm.slip.SlipBridgeAPI;
import org.openhab.binding.velux.internal.VeluxBindingConfig;
import org.openhab.binding.velux.internal.VeluxRSBindingConfig;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxExistingScenes;
import org.openhab.binding.velux.things.VeluxProduct;
import org.openhab.binding.velux.things.VeluxProductPosition;
import org.openhab.binding.velux.things.VeluxScene;
import org.openhab.binding.velux.things.VeluxScene.SceneName;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Common interaction with the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * It implements the communication between <B>OpenHAB</B> and the <I>Velux</I> Bridge:
 * <UL>
 * <LI><B>OpenHAB</B> Event Bus &rarr; <I>Velux</I> <B>bridge</B>
 * <P>
 * Sending commands and value updates.</LI>
 * <LI><I>Velux</I> <B>bridge</B> &rarr; <B>OpenHAB</B>:
 * <P>
 * Retrieving information by sending a Refresh command.</LI>
 * </UL>
 * <P>
 * Entry point for this class is the method
 * {@link org.openhab.binding.velux.handler.VeluxBridgeHandlerOH1#handleCommandOnChannel handleCommandOnChannel}.
 * <P>
 * <B>Note:</B> This is the same functionality as provided by the <I>OpenHAB2</I>-specific classes
 * org.openhab.binding.velux.handler.VeluxBridgeHandler and
 * org.openhab.binding.velux.handler.VeluxHandler}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class VeluxBridgeHandlerOH1 implements VeluxBridgeInstance {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeHandlerOH1.class);


	/*
	 *  ***** Objects and Methods for interface VeluxBridgeInstance *****
	 */

	/** Information retrieved by {@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
	 * and updated via {@link org.openhab.binding.velux.internal.VeluxBinding#updated} */
	private VeluxBridgeConfiguration veluxBridgeConfiguration;

	/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetProducts#getProducts} */
	private VeluxExistingProducts existingProducts = null;

	/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetScenes#getScenes} */
	private VeluxExistingScenes existingsScenes = null;

	/**
	 * Information retrieved by {{@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
	 */
	public VeluxBridgeConfiguration veluxBridgeConfiguration() {
		logger.trace("veluxBridgeConfiguration() called.");
		return this.veluxBridgeConfiguration;
	};

	/**
	 * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetProducts#getProducts}
	 */
	public VeluxExistingProducts existingProducts() {
		logger.trace("existingProducts() called.");
		return this.existingProducts;
	};

	/**
	 * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetScenes#getScenes}
	 */
	public VeluxExistingScenes existingScenes() {
		logger.trace("existingScenes() called.");
		return this.existingsScenes;
	}


	private class BridgeParameters {
		/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeDeviceStatus#retrieve} */
		private VeluxBridgeDeviceStatus.Channel gwState = null;

		/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetFirmware#retrieve} */
		private VeluxBridgeGetFirmware.Channel firmware = null;

		/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeLANConfig#retrieve} */
		private VeluxBridgeLANConfig.Channel lanConfig = null;

		/** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeWLANConfig#retrieve} */
		private VeluxBridgeWLANConfig.Channel wlanConfig = null;
	}


	/*
	 *  ***** Objects and Methods for interface VeluxBridgeInstance *****
	 */

	private BridgeParameters bridgeParameters;

	private JsonBridgeAPI myJsonBridge;
	private SlipBridgeAPI mySlipBridge;
	private VeluxBridge thisBridge;

	/**
	 * Creates an openHAB {@link State} in accordance to the class of the given {@code propertyValue}. Currently
	 * {@link Date}, {@link BigDecimal}, {@link Temperature} and {@link Boolean} are handled explicitly. All other
	 * {@code dataTypes} are mapped to {@link StringType}.
	 * <p>
	 * If {@code propertyValue} is {@code null}, {@link UnDefType#NULL} will be returned.
	 *
	 * Copied/adapted from the {@link org.openhab.binding.koubachi} binding.
	 *
	 * @param propertyValue
	 *
	 * @return the new {@link State} in accordance with {@code dataType}. Will never be {@code null}.
	 */
	private static State createState(Object propertyValue) {
		if (propertyValue == null) {
			return UnDefType.NULL;
		}

		Class<?> dataType = propertyValue.getClass();

		if (Integer.class.isAssignableFrom(dataType)) {
			return new DecimalType((Integer) propertyValue);
		} else if (BigDecimal.class.isAssignableFrom(dataType)) {
			return new DecimalType((BigDecimal) propertyValue);
		} else if (Boolean.class.isAssignableFrom(dataType)) {
			if ((Boolean) propertyValue) {
				return OnOffType.ON;
			} else {
				return OnOffType.OFF;
			}
		} else if (State.class.isAssignableFrom(dataType)) {
			return (State) propertyValue;
		} else {
			return new StringType(propertyValue.toString());
		}
	}

	/*
	 * Constructor.
	 */
	public VeluxBridgeHandlerOH1(VeluxBridgeConfiguration veluxBridgeConfiguration) {
		logger.debug("VeluxBridgeHandlerOH1(constructor) called.");
		
		logger.trace("VeluxBridgeHandlerOH1(): Initializing bridge configuration parameters.");
		this.veluxBridgeConfiguration = veluxBridgeConfiguration;
		
		logger.trace("VeluxBridgeHandlerOH1(): Initializing bridge status parameters.");
		this.bridgeParameters = new BridgeParameters();
		
		logger.trace("VeluxBridgeHandlerOH1(): Initializing the different bridge protocols.");
		this.myJsonBridge = new JsonBridgeAPI(this);
		this.mySlipBridge = new SlipBridgeAPI(this);
		this.thisBridge = null;

		logger.trace("Initializing empty storage for existing products.");
		this.existingProducts = new VeluxExistingProducts();
		logger.trace("Initializing empty storage for existing scenes.");
		this.existingsScenes = new VeluxExistingScenes();

		logger.trace("VeluxBridgeHandlerOH1() done.");
	}
	@Override
	public void finalize() {
		logger.trace("finalize({}) called.", this);
	}


	/***
	 *** Reconfiguration methods
	 ***/
	private void updated() {
			logger.debug("updated(constructor) called.");

			// Determine the appropriate bridge communication channel
			if (this.myJsonBridge.supportedProtocols.contains(this.veluxBridgeConfiguration.bridgeProtocol)) {
				logger.info("updated(): choosing JSON as communication method.");
				this.thisBridge = myJsonBridge;
			}
			if (this.mySlipBridge.supportedProtocols.contains(this.veluxBridgeConfiguration.bridgeProtocol)) {
				logger.info("updated(): choosing SLIP as communication method.");
				this.thisBridge = mySlipBridge;
			}
			if (this.thisBridge == null) {
				logger.warn("updated(): no valid bridgeProtocol selected, aborting this binding.");
				return;
			}

			if (this.thisBridge.bridgeAPI().modifyHouseStatusMonitor() != null) {
				logger.info("Activating HouseStatusMonitor.");
				new VeluxBridgeModifyHouseStatusMonitor().modifyHSM(thisBridge, true);
			}

			logger.trace("Adjusting communication parameters.");
			//JSON-only		if (this.thisBridge.bridgeLogin() && this.thisBridge.bridgeLogout()) {
			if (this.thisBridge.bridgeLogin()) {
				logger.debug("Velux Bridge is online, now.");
				logger.trace("Querying bridge state.");
				this.bridgeParameters.gwState = new VeluxBridgeDeviceStatus().retrieve(thisBridge);
				/*
				 * Fetch all scenes for further invocations
				 */
				logger.trace("Fetch existing scenes.");
				new VeluxBridgeGetScenes().getScenes(thisBridge, (VeluxBridgeInstance)this);
				logger.trace("Fetch existing actuators/products.");
				new VeluxBridgeGetProducts().getProducts(thisBridge, (VeluxBridgeInstance)this);
			} else {
				logger.info("Velux veluxBridge login/logout sequence failed; expecting veluxBridge is OFFLINE.");
			}
			logger.trace("VeluxBridgeHandlerOH1() done.");
		
	}

	/**
	 * General two-way communication method.
	 *
	 * It provides either information retrieval or information update according to the passed command.
	 *
	 * @param itemName
	 *                           The item passed as type {@link String} for which to following command is addressed to.
	 * @param command
	 *                           The command passed as type {@link Command} for the mentioned item. If
	 *                           {@code command} is {@code null}, an information retrieval via a <B>Refresh</B> command
	 *                           is initiated for this item.
	 * @param config         The item-specific configuration information passed as type {@link VeluxBindingProvider}.
	 * @param provider       The bridge-specific provides information passed as type {@link VeluxBindingConfig}.
	 * @param eventPublisher The connectivity to the OpenHAB event bus passed as type {@link EventPublisher}.
	 */
	public void handleCommandOnChannel(String itemName, Command command, VeluxBindingConfig config,
			VeluxBindingProvider provider, EventPublisher eventPublisher) {
		logger.debug("handleCommandOnChannel(item={},command={},config={},provider={}) called.", itemName, command,
				config, provider);
		assert config != null : "received an empty configuration.";
		synchronized (this) {
			/* ===========================================================
			 * Common part
			 */
			State newState = null;
			String actuatorSerial;

			if (this.veluxBridgeConfiguration.configHasChanged) {
				logger.trace("handleCommandOnChannel() work on updated bridge configuration parameters.");
				updated();
				this.veluxBridgeConfiguration.configHasChanged = false;
			}
				
			if (command == null) {
				/* ===========================================================
				 * Refresh part
				 */
				logger.trace("handleCommandOnChannel() work on refresh.");
				if (!provider.getConfigForItemName(itemName).getBindingItemType().isReadable()) {
					logger.error("handleCommandOnChannel() received a Refresh command for a non-readable item.");
				} else {

					logger.trace("handleCommandOnChannel() refreshing item {}.", itemName);
					switch (config.getBindingItemType()) {
					case BRIDGE_STATUS:
						if (this.bridgeParameters.gwState == null) {
							this.bridgeParameters.gwState = new VeluxBridgeDeviceStatus().retrieve(thisBridge);
						}
						if (this.bridgeParameters.gwState != null && this.bridgeParameters.gwState.isRetrieved) {
							newState = createState(this.bridgeParameters.gwState.toString());
						}
						break;

					case BRIDGE_FIRMWARE:
						if (this.bridgeParameters.firmware == null) {
							this.bridgeParameters.firmware = new VeluxBridgeGetFirmware().retrieve(thisBridge);
						}
						if (this.bridgeParameters.firmware != null && this.bridgeParameters.firmware.isRetrieved) {
							newState = createState(this.bridgeParameters.firmware.firmwareVersion);
						}
						break;

					case BRIDGE_IPADDRESS:
					case BRIDGE_SUBNETMASK:
					case BRIDGE_DEFAULTGW:
					case BRIDGE_DHCP:
						if (this.bridgeParameters.lanConfig == null) {
							this.bridgeParameters.lanConfig = new VeluxBridgeLANConfig().retrieve(myJsonBridge);
						}
						if (this.bridgeParameters.lanConfig != null && this.bridgeParameters.lanConfig.isRetrieved) {
							switch (config.getBindingItemType()) {
							case BRIDGE_IPADDRESS:
								newState = createState(this.bridgeParameters.lanConfig.ipAddress);
								break;
							case BRIDGE_SUBNETMASK:
								newState = createState(this.bridgeParameters.lanConfig.subnetMask);
								break;
							case BRIDGE_DEFAULTGW:
								newState = createState(this.bridgeParameters.lanConfig.defaultGW);
								break;
							case BRIDGE_DHCP:
								newState = createState(this.bridgeParameters.lanConfig.enabledDHCP);
							default:
							}
						}
						break;

					case BRIDGE_WLANSSID:
					case BRIDGE_WLANPASSWORD:
						if (this.bridgeParameters.wlanConfig == null) {
							this.bridgeParameters.wlanConfig = new VeluxBridgeWLANConfig().retrieve(myJsonBridge);
						}
						if (this.bridgeParameters.wlanConfig != null && this.bridgeParameters.wlanConfig.isRetrieved) {
							switch (config.getBindingItemType()) {
							case BRIDGE_WLANSSID:
								newState = createState(this.bridgeParameters.wlanConfig.wlanSSID);
								break;
							case BRIDGE_WLANPASSWORD:
								newState = createState(this.bridgeParameters.wlanConfig.wlanPassword);
								break;
							default:
							}
						}
						break;

					case BRIDGE_SCENES:
						if (this.existingsScenes.getNoMembers() == 0) {
							logger.trace("handleCommandOnChannel() is about to fetch existing scenes.");
							new VeluxBridgeGetScenes().getScenes(thisBridge, (VeluxBridgeInstance)this);
						}
						String sceneInfo = this.existingsScenes.toString();
						logger.trace("handleCommandOnChannel() found scenes {}.", sceneInfo);
						sceneInfo = sceneInfo.replaceAll("[^\\p{Punct}\\w]", "_");
						newState = createState(sceneInfo);
						break;

					case BRIDGE_PRODUCTS:
						if (this.existingProducts.getNoMembers() == 0) {
							logger.trace("handleCommandOnChannel() is about to fetch existing products.");
							new VeluxBridgeGetProducts().getProducts(myJsonBridge, (VeluxBridgeInstance)this);
						}
						String productInfo = this.existingProducts.toString();
						logger.trace("handleCommandOnChannel() found products {}.", productInfo);
						productInfo = productInfo.replaceAll("[^\\p{Punct}\\w]", "_");
						newState = createState(productInfo);
						break;

					case BRIDGE_CHECK:
						logger.trace("handleCommandOnChannel() loop through all existing scenes.");
						ArrayList<String> unusedScenes = new ArrayList<String>();
						for (VeluxScene scene : this.existingsScenes.values()) {
							boolean found = false;
							for (String thisItemName : provider.getInBindingItemNames()) {
								if (scene.getName().toString()
										.equals(provider.getConfigForItemName(thisItemName).getBindingConfig())) {
									logger.trace("handleCommandOnChannel() scene {} used within item {}.",
											scene.getName(), thisItemName);
									found = true;
								}
							}
							if (!found) {
								unusedScenes.add(scene.getName().toString());
								logger.trace("handleCommandOnChannel(): scene {} is currently unused.",
										scene.getName());
							}
						}
						String result;
						if (unusedScenes.size() > 0) {
							result = "check failed: The following scenes are unused: " + unusedScenes.toString() + ".";
						} else {
							result = "check ok. All scenes used within Items.";
						}
						logger.info("Result: {}", result);
						newState = createState(result);
						break;

					case ACTUATOR_SERIAL:
						assert this.existingProducts != null : "VeluxBridgeHandlerOH1.existingProducts not initialized.";
						if (this.existingProducts.getNoMembers() == 0) {
							logger.trace("handleCommandOnChannel(): is about to fetch existing products.");
							new VeluxBridgeGetProducts().getProducts(thisBridge, (VeluxBridgeInstance)this);
						}
						actuatorSerial = config.getBindingConfig();
						logger.trace("handleCommandOnChannel(): actuatorSerial={}",actuatorSerial);

						if (!this.existingProducts.isRegistered(actuatorSerial)) {
							logger.info("handleCommandOnChannel(): cannot work on unknown actuator: {}.", actuatorSerial);
							break;
						}
						logger.trace("handleCommandOnChannel(): fetching actuator for {}.",actuatorSerial);
						VeluxProduct thisProduct = this.existingProducts.get(actuatorSerial);
						logger.trace("handleCommandOnChannel(): found actuator {}.",thisProduct);

						GetProduct bcp = thisBridge.bridgeAPI().getProduct();
						if (bcp == null) {
							return;
						}
						bcp.setProductId(thisProduct.getBridgeProductIndex().toInt());
						if (thisBridge.bridgeCommunicate(bcp)) {
							thisProduct = bcp.getProduct();
							if (bcp.isCommunicationSuccessful()) {
								try {
									VeluxProductPosition position = new VeluxProductPosition(thisProduct.getCurrentPosition());
									logger.trace("handleCommandOnChannel(): found actuator at level {}.", position);
									newState = createState(position);
								} catch (Exception e) {
									logger.error("getProducts() exception.", e.getMessage());
								}
							}
						}
						break;


					default:
						logger.trace(
								"handleCommandOnChannel() cannot handle REFRESH on channel {} as it is of type {}.",
								itemName, config.getBindingItemType());
					}
				}
			} else {
				/* ===========================================================
				 * Modification part
				 */
				logger.trace("handleCommandOnChannel() found COMMAND {}.", command);
				String sceneName;

				switch (config.getBindingItemType()) {
				case SCENE_ACTION:
					assert this.existingsScenes != null : "VeluxBridgeHandlerOH1.existingsScenes not initialized.";
					if (command == OnOffType.ON) {
						sceneName = config.getBindingConfig();
						if (!this.existingsScenes.isRegistered(new SceneName(sceneName))) {
							logger.info("handleCommandOnChannel() cannot activate unknown scene: {}.", sceneName);
							break;
						}
						logger.debug("handleCommandOnChannel() activating known scene {}.", sceneName);
						VeluxScene thisScene = this.existingsScenes.get(new SceneName(sceneName));
						logger.trace("handleCommandOnChannel() execution scene {}.", thisScene);
						new VeluxBridgeExecute().execute(myJsonBridge, thisScene.getBridgeSceneIndex().toInt());
					} else {
						logger.trace("handleCommandOnChannel() ignoring OFF command.");
					}
					logger.trace("handleCommandOnChannel() execution done.");
					break;

				case BRIDGE_SHUTTER:
					if (true) {
						logger.trace("handleCommandOnChannel() working on virtual rollershutter.");
						VeluxRSBindingConfig thisRSBindingConfig = (VeluxRSBindingConfig) config;
						Integer rollershutterLevel = thisRSBindingConfig.getLevel();
						logger.trace("handleCommandOnChannel() current level is {}.", rollershutterLevel);
						if ((UpDownType) command == UpDownType.UP) {
							rollershutterLevel = thisRSBindingConfig.getNextDescendingLevel();
						} else if ((UpDownType) command == UpDownType.DOWN) {
							rollershutterLevel = thisRSBindingConfig.getNextAscendingLevel();
						} else {
							logger.info("handleCommandOnChannel() ignoring command {}.", command);
							break;
						}
						logger.trace("handleCommandOnChannel() next level is {}.", rollershutterLevel);
						sceneName = thisRSBindingConfig.getSceneName();
						assert eventPublisher != null : "eventPublisher not initialized.";
						eventPublisher.postUpdate(itemName, new PercentType(rollershutterLevel));
						VeluxScene thisScene = this.existingsScenes.get(new SceneName(sceneName));
						logger.trace("handleCommandOnChannel() execution scene {}.", thisScene);
						new VeluxBridgeExecute().execute(myJsonBridge, thisScene.getBridgeSceneIndex().toInt());
					}
					break;

				case SCENE_SILENTMODE:
					assert this.existingsScenes != null : "this.existingsScenes not initialized.";
					sceneName = config.getBindingConfig();
					if (!this.existingsScenes.isRegistered(new SceneName(sceneName))) {
						logger.info("handleCommandOnChannel() cannot activate unknown scene: {}.", sceneName);
						break;
					}
					boolean silentMode = command.equals(OnOffType.ON);
					logger.debug("handleCommandOnChannel() setting silent mode to {}.", silentMode);

					VeluxScene thisScene = this.existingsScenes.get(new SceneName(sceneName));
					logger.trace("handleCommandOnChannel() execution scene {}.", thisScene);
					int sceneNumber = thisScene.getBridgeSceneIndex().toInt();
					new VeluxBridgeSceneMode().setSilentMode(myJsonBridge, sceneNumber, silentMode);
					logger.trace("handleCommandOnChannel() execution done.");
					break;

				case BRIDGE_DO_DETECTION:
					if (command.equals(OnOffType.ON)) {
						logger.trace("handleCommandOnChannel() about to activate veluxBridge detection mode.");
						new VeluxBridgeDetectProducts().detectProducts(myJsonBridge);
					} else {
						logger.trace("handleCommandOnChannel() ignoring OFF command.");
					}
					break;


				case ACTUATOR_SERIAL:
					assert this.existingProducts != null : "VeluxBridgeHandlerOH1.existingProducts not initialized.";
					if (this.existingProducts.getNoMembers() == 0) {
						logger.trace("handleCommandOnChannel(): is about to fetch existing products.");
						new VeluxBridgeGetProducts().getProducts(thisBridge, (VeluxBridgeInstance)this);
					}
					actuatorSerial = config.getBindingConfig();
					logger.trace("handleCommandOnChannel(): actuatorSerial={}",actuatorSerial);

					if (!this.existingProducts.isRegistered(actuatorSerial)) {
						logger.info("handleCommandOnChannel(): cannot work on unknown actuator: {}.", actuatorSerial);
						break;
					}
					logger.trace("handleCommandOnChannel(): fetching product for {}.",actuatorSerial);
					VeluxProduct thisProduct = this.existingProducts.get(actuatorSerial);
					logger.trace("handleCommandOnChannel(): found product {}.",thisProduct);

					VeluxProductPosition targetLevel;
					if ((command instanceof UpDownType) && ((UpDownType) command == UpDownType.UP)) {
						logger.trace("handleCommandOnChannel(): found UP command.");
						targetLevel = new VeluxProductPosition(new PercentType(0));
					} else if ((command instanceof UpDownType) && ((UpDownType) command == UpDownType.DOWN)) {
						logger.trace("handleCommandOnChannel(): found DOWN command.");
						targetLevel = new VeluxProductPosition(new PercentType(100));
					} else if ((command instanceof StopMoveType) && ((StopMoveType) command == StopMoveType.STOP)) {
						logger.trace("handleCommandOnChannel(): found STOP command.");
						targetLevel = new VeluxProductPosition();
					} else if (command instanceof PercentType){
						logger.trace("handleCommandOnChannel(): found command of type PercentType.");
						PercentType ptCommand = (PercentType) command;
						logger.trace("handleCommandOnChannel(): found command to set level to {}.", ptCommand);
						targetLevel = new VeluxProductPosition(ptCommand);
					} else {
						logger.info("handleCommandOnChannel() ignoring command {}.", command);
						break;
					} 
					logger.trace("handleCommandOnChannel(): sending command with target level {}.", targetLevel);
					new VeluxBridgeSendCommand().sendCommand(thisBridge, thisProduct.getBridgeProductIndex().toInt(), targetLevel);
					newState = createState(targetLevel);
					break;

				default:
					logger.trace("handleCommand() cannot handle command {} on channel {} (type {}).", command, itemName,
							config.getBindingItemType());
				}
			}
			/* ===========================================================
			 * Common part
			 */
			if (newState != null) {
				logger.debug("handleCommandOnChannel() updating {} to {}.", itemName, newState);
				assert eventPublisher != null : "eventPublisher not initialized.";
				eventPublisher.postUpdate(itemName, newState);
			} else {
				logger.info("handleCommandOnChannel() updating of item {} (type {}) failed.", itemName,
						config.getBindingItemType().name());
			}
			logger.trace("handleCommandOnChannel() done.");
		}
	}

}

/**
 * end-of-handler/VeluxBridgeHandlerOH1.java
 */
