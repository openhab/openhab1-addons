/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.velux.handler;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.VeluxBindingProvider;
import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.VeluxBridgeActuators;
import org.openhab.binding.velux.bridge.VeluxBridgeDetectProducts;
import org.openhab.binding.velux.bridge.VeluxBridgeDeviceStatus;
import org.openhab.binding.velux.bridge.VeluxBridgeGetFirmware;
import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.VeluxBridgeLANConfig;
import org.openhab.binding.velux.bridge.VeluxBridgeModifyHouseStatusMonitor;
import org.openhab.binding.velux.bridge.VeluxBridgeProvider;
import org.openhab.binding.velux.bridge.VeluxBridgeRunScene;
import org.openhab.binding.velux.bridge.VeluxBridgeSceneMode;
import org.openhab.binding.velux.bridge.VeluxBridgeScenes;
import org.openhab.binding.velux.bridge.VeluxBridgeSendCommand;
import org.openhab.binding.velux.bridge.VeluxBridgeWLANConfig;
import org.openhab.binding.velux.bridge.common.BridgeAPI;
import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.common.GetProduct;
import org.openhab.binding.velux.bridge.json.JsonBridgeAPI;
import org.openhab.binding.velux.bridge.slip.SlipBridgeAPI;
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
 * @since 1.13.0
 */
public class VeluxBridgeHandlerOH1 extends VeluxBridge implements VeluxBridgeInstance, VeluxBridgeProvider {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeHandlerOH1.class);

    /*
     * ***** Objects and Methods for interface VeluxBridgeInstance *****
     */

    /**
     * Information retrieved by {@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
     * and updated via {@link org.openhab.binding.velux.internal.VeluxBinding#updated}
     */
    private VeluxBridgeConfiguration veluxBridgeConfiguration;

    /**
     * Information retrieved by {{@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
     */
    @Override
    public VeluxBridgeConfiguration veluxBridgeConfiguration() {
        return veluxBridgeConfiguration;
    };

    /**
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeActuators#getProducts}
     */
    @Override
    public VeluxExistingProducts existingProducts() {
        return bridgeParameters.actuators.getChannel().existingProducts;
    };

    /**
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeScenes#getScenes}
     */
    @Override
    public VeluxExistingScenes existingScenes() {
        return bridgeParameters.scenes.getChannel().existingScenes;
    }

    private class BridgeParameters {
        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeActuators#getProducts} */
        private VeluxBridgeActuators actuators = null;

        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeScenes#getScenes} */
        private VeluxBridgeScenes scenes = null;

        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeDeviceStatus#retrieve} */
        private VeluxBridgeDeviceStatus.Channel gateway = null;

        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetFirmware#retrieve} */
        private VeluxBridgeGetFirmware.Channel firmware = null;

        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeLANConfig#retrieve} */
        private VeluxBridgeLANConfig.Channel lanConfig = null;

        /** Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeWLANConfig#retrieve} */
        private VeluxBridgeWLANConfig.Channel wlanConfig = null;
    }

    /*
     * ***** Objects and Methods for interface VeluxBridgeInstance *****
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
     * @return <b>state</B> of type {@link State} in accordance with {@code dataType}. Will never be {@code null}.
     */
    private static State createState(Object propertyValue) {
        if (propertyValue == null) {
            return UnDefType.NULL;
        }

        Class<?> dataType = propertyValue.getClass();

        if (PercentType.class.isAssignableFrom(dataType)) {
            return new PercentType((Integer) propertyValue);
        } else if (Integer.class.isAssignableFrom(dataType)) {
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
        bridgeParameters = new BridgeParameters();

        logger.trace("VeluxBridgeHandlerOH1(): Initializing the different bridge protocols.");
        myJsonBridge = new JsonBridgeAPI(this);
        mySlipBridge = new SlipBridgeAPI(this);
        thisBridge = null;

        logger.trace("Initializing empty storage for existing products.");
        bridgeParameters.actuators = new VeluxBridgeActuators();

        logger.trace("Initializing empty storage for existing scenes.");
        bridgeParameters.scenes = new VeluxBridgeScenes();

        logger.trace("VeluxBridgeHandlerOH1() done.");
    }

    @Override
    public void finalize() {
        logger.trace("finalize({}) called.", this);
        if (myJsonBridge != null) {
            myJsonBridge.shutdown();
        }
        if (mySlipBridge != null) {
            mySlipBridge.shutdown();
        }
        logger.trace("finalize() done.");
    }

    /***
     *** Reconfiguration methods
     ***/
    private void bridgeParamsUpdated() {
        logger.debug("bridgeParamsUpdated() called.");

        // Determine the appropriate bridge communication channel
        thisBridge = null;
        if (myJsonBridge.supportedProtocols.contains(veluxBridgeConfiguration.bridgeProtocol)) {
            logger.debug("bridgeParamsUpdated(): choosing JSON as communication method.");
            thisBridge = myJsonBridge;
        }
        if (mySlipBridge.supportedProtocols.contains(veluxBridgeConfiguration.bridgeProtocol)) {
            logger.debug("bridgeParamsUpdated(): choosing SLIP as communication method.");
            thisBridge = mySlipBridge;
        }
        if (thisBridge == null) {
            logger.warn("No valid bridgeProtocol selected, aborting this {} binding.",
                    VeluxBindingConstants.BINDING_ID);
            return;
        }

        if (!thisBridge.bridgeLogin()) {
            logger.warn("{} bridge login sequence failed; expecting bridge is OFFLINE.",
                    VeluxBindingConstants.BINDING_ID);
            return;
        }

        logger.trace("bridgeParamsUpdated(): Querying bridge state.");
        bridgeParameters.gateway = new VeluxBridgeDeviceStatus().retrieve(thisBridge);

        logger.trace("bridgeParamsUpdated(): Fetching existing scenes.");
        bridgeParameters.scenes.getScenes(thisBridge);
        String scenes = bridgeParameters.scenes.getChannel().existingScenes.toString(false, "\n\t");
        logger.info("Found {} scenes:\n\t{}.", VeluxBindingConstants.BINDING_ID, scenes);

        logger.trace("bridgeParamsUpdated(): Fetching existing actuators/products.");
        bridgeParameters.actuators.getProducts(thisBridge);
        String products = bridgeParameters.actuators.getChannel().existingProducts.toString(false, "\n\t");
        logger.info("Found {} actuators:\n\t{}.", VeluxBindingConstants.BINDING_ID, products);

        if (thisBridge.bridgeAPI().modifyHouseStatusMonitor() != null) {
            logger.trace("bridgeParamsUpdated(): Activating HouseStatusMonitor.");
            if (new VeluxBridgeModifyHouseStatusMonitor().modifyHSM(thisBridge, true)) {
                logger.trace("bridgeParamsUpdated(): HSM activated.");
            } else {
                logger.warn("Activation of House-Status-Monitoring failed (might lead to a lack of status updates).");
            }
        }

        veluxBridgeConfiguration.hasChanged = false;
        logger.info("{} Bridge is online with {} scenes and {} actuators, now.", VeluxBindingConstants.BINDING_ID,
                bridgeParameters.scenes.getChannel().existingScenes.getNoMembers(),
                bridgeParameters.actuators.getChannel().existingProducts.getNoMembers());
        logger.trace("bridgeParamsUpdated() successfully finished.");
    }

    private boolean _actuatorIsInverted(String actuatorSerial) {
        return actuatorSerial.charAt(actuatorSerial.length() - 1) == '*';
    }

    private String _actuatorCleanupSerial(String actuatorSerial) {
        return _actuatorIsInverted(actuatorSerial) ? actuatorSerial.substring(0, actuatorSerial.length() - 1)
                : actuatorSerial;
    }

    /**
     * General two-way communication method.
     *
     * It provides either information retrieval or information update according to the passed command.
     *
     * @param itemName The item passed as type {@link String} for which to following command is addressed to.
     * @param command The command passed as type {@link Command} for the mentioned item. If
     *            {@code command} is {@code null}, an information retrieval via a <B>Refresh</B> command
     *            is initiated for this item.
     * @param config The item-specific configuration information passed as type {@link VeluxBindingProvider}.
     * @param provider The bridge-specific provides information passed as type {@link VeluxBindingConfig}.
     * @param eventPublisher The connectivity to the OpenHAB event bus passed as type {@link EventPublisher}.
     */
    public void handleCommandOnChannel(String itemName, Command command, VeluxBindingConfig config,
            VeluxBindingProvider provider, EventPublisher eventPublisher) {
        logger.debug("handleCommandOnChannel(item={},command={},config={},provider={}) called.", itemName, command,
                config, provider);
        assert config != null : "received an empty configuration.";
        synchronized (this) {
            /*
             * ===========================================================
             * Common part
             */
            State newState = null;
            String actuatorSerial;

            if (veluxBridgeConfiguration.hasChanged) {
                logger.trace("handleCommandOnChannel(): work on updated bridge configuration parameters.");
                bridgeParamsUpdated();
            }

            if (bridgeParameters.actuators.updateOH(provider, eventPublisher)) {
                logger.trace("handleCommandOnChannel(): openHAB items updated.");
            }

            if (command == null) {
                /*
                 * ===========================================================
                 * Refresh part
                 */
                logger.trace("handleCommandOnChannel(): work on refresh.");
                if (!provider.getConfigForItemName(itemName).getBindingItemType().isReadable()) {
                    logger.warn("handleCommandOnChannel(): received a Refresh command for a non-readable item.");
                } else {
                    logger.trace("handleCommandOnChannel(): refreshing item {}.", itemName);
                    switch (config.getBindingItemType()) {
                        case BRIDGE_STATUS:
                            bridgeParameters.gateway = new VeluxBridgeDeviceStatus().retrieve(thisBridge);
                            if (bridgeParameters.gateway != null && bridgeParameters.gateway.isRetrieved) {
                                newState = bridgeParameters.gateway.gwState;
                            }
                            break;

                        case BRIDGE_TIMESTAMP:
                            DecimalType timestampValue = new DecimalType(
                                    thisBridge.bridgeAPI().bridgeLastSuccessfullCommunication());
                            newState = timestampValue;
                            break;

                        case BRIDGE_FIRMWARE:
                            bridgeParameters.firmware = new VeluxBridgeGetFirmware().retrieve(thisBridge);
                            if (bridgeParameters.firmware != null && bridgeParameters.firmware.isRetrieved) {
                                newState = bridgeParameters.firmware.firmwareVersion;
                            }
                            break;

                        case BRIDGE_IPADDRESS:
                        case BRIDGE_SUBNETMASK:
                        case BRIDGE_DEFAULTGW:
                        case BRIDGE_DHCP:
                            bridgeParameters.lanConfig = new VeluxBridgeLANConfig().retrieve(thisBridge);
                            if (bridgeParameters.lanConfig != null && bridgeParameters.lanConfig.isRetrieved) {
                                switch (config.getBindingItemType()) {
                                    case BRIDGE_IPADDRESS:
                                        newState = bridgeParameters.lanConfig.openHABipAddress;
                                        break;
                                    case BRIDGE_SUBNETMASK:
                                        newState = bridgeParameters.lanConfig.openHABsubnetMask;
                                        break;
                                    case BRIDGE_DEFAULTGW:
                                        newState = bridgeParameters.lanConfig.openHABdefaultGW;
                                        break;
                                    case BRIDGE_DHCP:
                                        newState = bridgeParameters.lanConfig.openHABenabledDHCP;
                                    default:
                                }
                            }
                            break;

                        case BRIDGE_WLANSSID:
                        case BRIDGE_WLANPASSWORD:
                            bridgeParameters.wlanConfig = new VeluxBridgeWLANConfig().retrieve(thisBridge);
                            if (bridgeParameters.wlanConfig != null && bridgeParameters.wlanConfig.isRetrieved) {
                                switch (config.getBindingItemType()) {
                                    case BRIDGE_WLANSSID:
                                        newState = bridgeParameters.wlanConfig.openHABwlanSSID;
                                        break;
                                    case BRIDGE_WLANPASSWORD:
                                        newState = bridgeParameters.wlanConfig.openHABwlanPassword;
                                        break;
                                    default:
                                }
                            }
                            break;

                        case BRIDGE_SCENES:
                            if (bridgeParameters.scenes.autoRefresh(thisBridge)) {
                                logger.trace("handleCommandOnChannel(): there are some existing scenes.");
                            }
                            String sceneInfo = bridgeParameters.scenes.getChannel().existingScenes.toString();
                            logger.trace("handleCommandOnChannel(): found scenes {}.", sceneInfo);
                            sceneInfo = sceneInfo.replaceAll("[^\\p{Punct}\\w]", "_");
                            newState = createState(sceneInfo);
                            break;

                        case BRIDGE_PRODUCTS:
                            if (bridgeParameters.actuators.autoRefresh(thisBridge)) {
                                logger.trace("handleCommandOnChannel(): there are some existing products.");
                            }
                            String products = bridgeParameters.actuators.getChannel().existingProducts.toString();
                            logger.trace("handleCommandOnChannel(): found products {}.", products);
                            products = products.replaceAll("[^\\p{Punct}\\w]", "_");
                            newState = createState(products);
                            break;

                        case BRIDGE_CHECK:
                            logger.trace("handleCommandOnChannel(): loop through all existing scenes.");
                            ArrayList<String> unusedScenes = new ArrayList<String>();
                            for (VeluxScene scene : bridgeParameters.scenes.getChannel().existingScenes.values()) {
                                boolean found = false;
                                for (String thisItemName : provider.getInBindingItemNames()) {
                                    if (scene.getName().toString()
                                            .equals(provider.getConfigForItemName(thisItemName).getBindingConfig())) {
                                        logger.trace("handleCommandOnChannel(): scene {} used within item {}.",
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
                                result = "check failed: The following scenes are unused: " + unusedScenes.toString()
                                        + ".";
                            } else {
                                result = "check ok. All scenes are used within Items.";
                            }
                            logger.info("Result: {}", result);
                            newState = createState(result);
                            break;

                        case ACTUATOR_SERIAL:
                            assert bridgeParameters.actuators != null : "VeluxBridgeHandlerOH1.bridgeParameters.actuators not initialized.";
                            if (bridgeParameters.actuators.autoRefresh(thisBridge)) {
                                logger.trace("handleCommandOnChannel(): there are some existing products.");
                            }
                            actuatorSerial = config.getBindingConfig();
                            logger.trace("handleCommandOnChannel(): actuatorSerial={}", actuatorSerial);
                            boolean isInverted = _actuatorIsInverted(actuatorSerial);
                            actuatorSerial = _actuatorCleanupSerial(actuatorSerial);
                            logger.trace("handleCommandOnChannel(): actuatorSerial={}", actuatorSerial);

                            if (!bridgeParameters.actuators.getChannel().existingProducts
                                    .isRegistered(actuatorSerial)) {
                                logger.info("handleCommandOnChannel(): cannot work on unknown actuator: {}.",
                                        actuatorSerial);
                                break;
                            }
                            logger.trace("handleCommandOnChannel(): fetching actuator for {}.", actuatorSerial);
                            VeluxProduct thisProduct = bridgeParameters.actuators.getChannel().existingProducts
                                    .get(actuatorSerial);
                            logger.trace("handleCommandOnChannel(): found actuator {}.", thisProduct);

                            GetProduct bcp = thisBridge.bridgeAPI().getProduct();
                            if (bcp == null) {
                                return;
                            }
                            bcp.setProductId(thisProduct.getBridgeProductIndex().toInt());
                            if (thisBridge.bridgeCommunicate(bcp)) {
                                thisProduct = bcp.getProduct();
                                if (bcp.isCommunicationSuccessful()) {
                                    try {
                                        PercentType positionAsPercent = new VeluxProductPosition(
                                                thisProduct.getCurrentPosition()).getPositionAsPercentType(isInverted);
                                        if (positionAsPercent != null) {
                                            logger.trace("handleCommandOnChannel(): found actuator at level {}.",
                                                    positionAsPercent);
                                            newState = positionAsPercent;
                                        } else {
                                            logger.trace("handleCommandOnChannel(): level of actuator is unknown.");
                                        }
                                    } catch (Exception e) {
                                        logger.warn("handleCommandOnChannel(): getProducts() exception: {}.",
                                                e.getMessage());
                                    }
                                }
                            }
                            break;

                        default:
                            logger.trace(
                                    "handleCommandOnChannel(): cannot handle REFRESH on channel {} as it is of type {}.",
                                    itemName, config.getBindingItemType());
                    }
                }
                if (newState != null) {
                    logger.debug("handleCommandOnChannel(): updating {} to {}.", itemName, newState);
                    assert eventPublisher != null : "eventPublisher not initialized.";
                    eventPublisher.postUpdate(itemName, newState);
                } else {
                    logger.info("handleCommandOnChannel(): updating of item {} (type {}) failed.", itemName,
                            config.getBindingItemType().name());
                }
            } else {
                /*
                 * ===========================================================
                 * Modification part
                 */
                logger.trace("handleCommandOnChannel(): found COMMAND {}.", command);
                String sceneName;

                switch (config.getBindingItemType()) {
                    case BRIDGE_RELOAD:
                        if (command.equals(OnOffType.ON)) {
                            logger.trace("handleCommandOnChannel(): about to reload informations from veluxBridge.");
                            bridgeParamsUpdated();
                        } else {
                            logger.trace("handleCommandOnChannel(): ignoring OFF command.");
                        }
                        break;

                    case SCENE_ACTION:
                        assert bridgeParameters.scenes
                                .getChannel().existingScenes != null : "VeluxBridgeHandlerOH1.existingScenes not initialized.";
                        if (command == OnOffType.ON) {
                            sceneName = config.getBindingConfig();
                            if (!bridgeParameters.scenes.getChannel().existingScenes
                                    .isRegistered(new SceneName(sceneName))) {
                                logger.info("handleCommandOnChannel(): cannot activate unknown scene: {}.", sceneName);
                                break;
                            }
                            logger.debug("handleCommandOnChannel(): activating known scene {}.", sceneName);
                            VeluxScene thisScene = bridgeParameters.scenes.getChannel().existingScenes
                                    .get(new SceneName(sceneName));
                            logger.trace("handleCommandOnChannel(): execution scene {}.", thisScene);
                            new VeluxBridgeRunScene().execute(thisBridge, thisScene.getBridgeSceneIndex().toInt());
                        } else {
                            logger.trace("handleCommandOnChannel(): ignoring OFF command.");
                        }
                        logger.trace("handleCommandOnChannel(): execution done.");
                        break;

                    case BRIDGE_SHUTTER:
                        logger.trace("handleCommandOnChannel(): working on virtual rollershutter.");
                        VeluxRSBindingConfig thisRSBindingConfig = (VeluxRSBindingConfig) config;
                        Integer rollershutterLevel = thisRSBindingConfig.getLevel();
                        logger.trace("handleCommandOnChannel(): current level is {}.", rollershutterLevel);
                        if ((UpDownType) command == UpDownType.UP) {
                            rollershutterLevel = thisRSBindingConfig.getNextDescendingLevel();
                        } else if ((UpDownType) command == UpDownType.DOWN) {
                            rollershutterLevel = thisRSBindingConfig.getNextAscendingLevel();
                        } else {
                            logger.info("handleCommandOnChannel(): ignoring command {}.", command);
                            break;
                        }
                        logger.trace("handleCommandOnChannel(): next level is {}.", rollershutterLevel);
                        sceneName = thisRSBindingConfig.getSceneName();
                        logger.trace("handleCommandOnChannel(): scene name is {}.", sceneName);
                        assert eventPublisher != null : "eventPublisher not initialized.";
                        eventPublisher.postUpdate(itemName, new PercentType(rollershutterLevel));
                        VeluxScene thisScene2 = bridgeParameters.scenes.getChannel().existingScenes
                                .get(new SceneName(sceneName));
                        if (thisScene2 == null) {
                            logger.warn(
                                    "handleCommandOnChannel(): aborting command as scene with name {} is not registered; please check your KLF scene definitions.",
                                    sceneName);
                            break;
                        }
                        logger.trace("handleCommandOnChannel(): executing scene {} with index {}.", thisScene2,
                                thisScene2.getBridgeSceneIndex().toInt());
                        new VeluxBridgeRunScene().execute(thisBridge, thisScene2.getBridgeSceneIndex().toInt());
                        break;

                    case SCENE_SILENTMODE:
                        assert bridgeParameters.scenes
                                .getChannel().existingScenes != null : "existingScenes not initialized.";
                        sceneName = config.getBindingConfig();
                        if (!bridgeParameters.scenes.getChannel().existingScenes
                                .isRegistered(new SceneName(sceneName))) {
                            logger.info("handleCommandOnChannel(): cannot activate unknown scene: {}.", sceneName);
                            break;
                        }
                        boolean silentMode = command.equals(OnOffType.ON);
                        logger.debug("handleCommandOnChannel(): setting silent mode to {}.", silentMode);

                        VeluxScene thisScene = bridgeParameters.scenes.getChannel().existingScenes
                                .get(new SceneName(sceneName));
                        logger.trace("handleCommandOnChannel(): execution scene {}.", thisScene);
                        int sceneNumber = thisScene.getBridgeSceneIndex().toInt();
                        new VeluxBridgeSceneMode().setSilentMode(thisBridge, sceneNumber, silentMode);
                        logger.trace("handleCommandOnChannel(): execution done.");
                        break;

                    case BRIDGE_DO_DETECTION:
                        if (command.equals(OnOffType.ON)) {
                            logger.trace("handleCommandOnChannel(): about to activate veluxBridge detection mode.");
                            new VeluxBridgeDetectProducts().detectProducts(thisBridge);
                        } else {
                            logger.trace("handleCommandOnChannel(): ignoring OFF command.");
                        }
                        break;

                    case ACTUATOR_SERIAL:
                        assert bridgeParameters.actuators != null : "VeluxBridgeHandlerOH1.bridgeParameters.actuators not initialized.";
                        if (bridgeParameters.actuators.autoRefresh(thisBridge)) {
                            logger.trace("handleCommandOnChannel(): there are some existing products.");
                        }
                        actuatorSerial = config.getBindingConfig();
                        logger.trace("handleCommandOnChannel(): actuatorSerial={}", actuatorSerial);
                        boolean isInverted = _actuatorIsInverted(actuatorSerial);
                        actuatorSerial = _actuatorCleanupSerial(actuatorSerial);
                        logger.trace("handleCommandOnChannel(): actuatorSerial={}", actuatorSerial);

                        if (!bridgeParameters.actuators.getChannel().existingProducts.isRegistered(actuatorSerial)) {
                            logger.info("handleCommandOnChannel(): cannot work on unknown actuator: {}.",
                                    actuatorSerial);
                            break;
                        }
                        logger.trace("handleCommandOnChannel(): fetching product for {}.", actuatorSerial);
                        VeluxProduct thisProduct = bridgeParameters.actuators.getChannel().existingProducts
                                .get(actuatorSerial);
                        logger.trace("handleCommandOnChannel(): found product {}.", thisProduct);

                        VeluxProductPosition targetLevel;
                        if ((command instanceof UpDownType) && ((UpDownType) command == UpDownType.UP)) {
                            logger.trace("handleCommandOnChannel(): found UP command.");
                            targetLevel = isInverted ? new VeluxProductPosition(PercentType.HUNDRED)
                                    : new VeluxProductPosition(PercentType.ZERO);
                        } else if ((command instanceof UpDownType) && ((UpDownType) command == UpDownType.DOWN)) {
                            logger.trace("handleCommandOnChannel(): found DOWN command.");
                            targetLevel = isInverted ? new VeluxProductPosition(PercentType.ZERO)
                                    : new VeluxProductPosition(PercentType.HUNDRED);
                        } else if ((command instanceof StopMoveType) && ((StopMoveType) command == StopMoveType.STOP)) {
                            logger.trace("handleCommandOnChannel(): found STOP command.");
                            targetLevel = new VeluxProductPosition();
                        } else if (command instanceof PercentType) {
                            logger.trace("handleCommandOnChannel(): found command of type PercentType.");
                            PercentType ptCommand = (PercentType) command;
                            if (isInverted) {
                                ptCommand = new PercentType(PercentType.HUNDRED.intValue() - ptCommand.intValue());
                            }
                            logger.trace("handleCommandOnChannel(): found command to set level to {}.", ptCommand);
                            targetLevel = new VeluxProductPosition(ptCommand);
                        } else {
                            logger.info("handleCommandOnChannel(): ignoring command {}.", command);
                            break;
                        }
                        logger.debug("handleCommandOnChannel(): sending command with target level {}.", targetLevel);
                        new VeluxBridgeSendCommand().sendCommand(thisBridge,
                                thisProduct.getBridgeProductIndex().toInt(), targetLevel);
                        logger.trace(
                                "handleCommandOnChannel(): The new shutter level will be send through the home monitoring events.");
                        if (bridgeParameters.actuators.autoRefresh(thisBridge)) {
                            logger.trace("handleCommandOnChannel(): position of actuators are updated.");
                        }
                        break;

                    default:
                        logger.trace("handleCommand() cannot handle command {} on channel {} (type {}).", command,
                                itemName, config.getBindingItemType());
                }
            }
            logger.trace("handleCommandOnChannel() done.");
        }
    }

    @Override
    @Deprecated
    public VeluxExistingProducts getExistingsProducts() {
        return null;
    }

    @Override
    @Deprecated
    public VeluxExistingScenes getExistingsScenes() {
        return null;
    }

    @Deprecated
    public VeluxBridgeHandlerOH1() {
    }

    @Deprecated
    @Override
    public boolean bridgeCommunicate(BridgeCommunicationProtocol communication) {
        return false;
    }

    @Deprecated
    @Override
    public <T> T bridgeCommunicate(org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol<T> communication) {
        return null;
    }

    @Deprecated
    @Override
    public BridgeAPI bridgeAPI() {
        return null;
    }

    @Deprecated
    @Override
    public boolean bridgeLogin() {
        return false;
    }

    @Deprecated
    @Override
    public boolean bridgeLogout() {
        return false;
    }

    @Deprecated
    @Override
    public void bridgeOverwriteConfig(int retries, int waitIntervalInMSecs) {
    }

    @Deprecated
    @Override
    public <T> T bridgeCommunicate(org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol<T> communication,
            boolean useAuthentication) {
        return null;
    }
}
