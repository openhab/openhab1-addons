package org.openhab.binding.velux.handler;

/// **
// * Copyright (c) 2010-2018 by the respective copyright holders.
// *
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// */
// package org.openhab.binding.velux.handler;
//
// import static org.openhab.binding.velux.VeluxBindingConstants.*;
//
// import java.util.Set;
//
// import org.apache.commons.lang.StringUtils;
// import org.eclipse.smarthome.core.library.types.OnOffType;
// import org.eclipse.smarthome.core.thing.Bridge;
// import org.eclipse.smarthome.core.thing.ChannelUID;
// import org.eclipse.smarthome.core.thing.Thing;
// import org.eclipse.smarthome.core.thing.ThingStatus;
// import org.eclipse.smarthome.core.thing.ThingStatusDetail;
// import org.eclipse.smarthome.core.thing.ThingTypeUID;
// import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
// import org.eclipse.smarthome.core.thing.binding.ThingHandler;
// import org.eclipse.smarthome.core.types.Command;
// import org.eclipse.smarthome.core.types.RefreshType;
// import org.openhab.binding.velux.VeluxBindingProperties;
// import org.openhab.binding.velux.bridge.VeluxBridgeExecute;
// import org.openhab.binding.velux.bridge.VeluxBridgeSceneMode;
// import org.openhab.binding.velux.internal.config.VeluxThingConfiguration;
// import org.openhab.binding.velux.things.VeluxScene;
// import org.openhab.binding.velux.things.VeluxScene.SceneName;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import com.google.common.collect.ImmutableSet;
//
/// **
// * The {@link VeluxHandler} is responsible for handling commands, which are
// * sent via {@link VeluxBridgeHandler} to one of the channels.
// *
// * @author Guenther Schreiner - Initial contribution
// */

//TODO: the commented code in OpenHAB2 stuff
/**
 * <B>OpenHAB2-only</B> The {@link VeluxHandler} is responsible for handling commands, which are sent via
 * {@link VeluxBridgeHandler} to one of the channels.
 */
public class VeluxHandler {
}
// public class VeluxHandler extends BaseThingHandler {
// private final Logger logger = LoggerFactory.getLogger(VeluxHandler.class);
// /**
// * Set of things provided by {@link VeluxHandler}.
// */
// public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_VELUX_SCENE);
// /**
// * Separator between channel identifier and suffix which describes a subchannel.
// */
// public static final String SUBSEPARATUR = "#";
//
// private VeluxThingConfiguration configuration = null;
// private VeluxBridgeHandler bridgeHandler;
//
// private boolean propertiesInitializedSuccessfully = false;
//
// public VeluxHandler(Thing thing) {
// super(thing);
// }
//
// @Override
// public void initialize() {
// logger.debug("Initializing thing {}", getThing().getUID());
// Bridge thisBridge = getBridge();
// initializeThing((thisBridge == null) ? null : thisBridge.getStatus());
// logger.trace("done with initialization of thing {}", getThing().getUID());
// }
//
// private void initializeThing(ThingStatus bridgeStatus) {
// logger.debug("initializeThing thing {} bridge status {}", getThing().getUID(), bridgeStatus);
//
// if (getVeluxBridgeHandler() != null) {
// if (bridgeStatus == ThingStatus.ONLINE) {
// updateStatus(ThingStatus.ONLINE);
// initializeProperties();
// } else {
// updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
// }
// } else {
// updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING);
// }
// }
//
// private synchronized VeluxBridgeHandler getVeluxBridgeHandler() {
// logger.trace("getVeluxBridgeHandler() called.");
// if (this.bridgeHandler == null) {
// logger.trace("no bridge yet active.");
// Bridge bridge = getBridge();
// logger.trace("returned bridge {}.", bridge);
// if (bridge == null) {
// logger.trace("returning null");
// return null;
// }
// logger.trace("continuing");
// ThingHandler handler = bridge.getHandler();
// if (handler instanceof VeluxBridgeHandler) {
// this.bridgeHandler = (VeluxBridgeHandler) handler;
// } else {
// return null;
// }
// }
// return this.bridgeHandler;
// }
//
// @SuppressWarnings({ "unused", "null" })
// private synchronized void initializeProperties() {
// configuration = getConfigAs(VeluxThingConfiguration.class);
// if (configuration == null) {
// logger.debug("No configuration found, using default values.");
// configuration = new VeluxThingConfiguration();
// }
//
// if (!propertiesInitializedSuccessfully) {
// ThingTypeUID thingTypeUID = getThing().getThingTypeUID();
// // Determine the ThingType and choose appropriate device type
// if (thingTypeUID.equals(THING_TYPE_VELUX_SCENE)) {
// logger.trace("initializeProperties(): scene {} initialized.", thingTypeUID);
// } else {
// logger.error("Could not initialize query as UID {} is unknown.", thingTypeUID);
// return;
// }
// propertiesInitializedSuccessfully = true;
// }
// }
//
// @Override
// public void dispose() {
// logger.trace("VeluxHandler disposed.");
// super.dispose();
// }
//
// @Override
// public void channelLinked(ChannelUID channelUID) {
// logger.trace("channelLinked({}) called.", channelUID.getAsString());
//
// if (thing.getStatus() == ThingStatus.ONLINE) {
// handleCommand(channelUID, RefreshType.REFRESH);
// }
// }
//
// @Override
// public void handleCommand(ChannelUID channelUID, Command command) {
// logger.trace("handleCommand({},{}) called.", channelUID.getAsString(), command);
//
// Bridge veluxBridge = getBridge();
// if (veluxBridge == null) {
// logger.warn("Cannot handle command without bridge.");
// return;
// }
// VeluxBridgeHandler veluxBridgeHandler = (VeluxBridgeHandler) veluxBridge.getHandler();
// if (veluxBridgeHandler == null) {
// logger.warn("veluxBridge.getHandler() returned null.");
// return;
// }
//
// String channelId = getChannelId(channelUID.getId());
// logger.trace("handleCommand(): working with channelId {}.", channelId);
// String channelSubId = getChannelSubId(channelUID.getId());
// logger.trace("handleCommand(): working with channelSubId {}.", channelSubId);
//
// VeluxScene thisScene;
//
// String sceneName = getPropertyValueAsString(VeluxBindingProperties.PROPERTY_SCENE_NAME);
//
// if (sceneName != null) {
// logger.trace("handleCommand(): working with PROPERTY_SCENE_NAME {}.", sceneName);
// } else {
// sceneName = channelSubId;
// logger.trace("handleCommand(): working with channelSubId {}.", channelSubId);
//
// }
// if (command instanceof RefreshType) {
// if (!veluxBridgeHandler.bridgeLogin()) {
// logger.trace("Cannot handle refresh command for the time being.");
// veluxBridgeHandler.bridgeLogout();
// updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.BRIDGE_OFFLINE);
// return;
// }
//
// switch (channelId) {
// case CHANNEL_SCENE_ACTION:
// logger.trace("Cannot handle REFRESH.");
// logger.debug("handleCommand() had recently discovered products: {}",
// veluxBridgeHandler.existingsProducts);
// logger.debug("handleCommand() had recently discovered scenes: {}",
// veluxBridgeHandler.existingsScenes);
// break;
// default:
// logger.trace("handleCommand() cannot handle REFRESH on channel {}.", channelId);
// }
// return;
//
// } else if (command instanceof OnOffType) {
// switch (channelId) {
// case CHANNEL_SCENE_ACTION:
// logger.trace("Try to handle ON/OFF with channelSubId {}.", sceneName);
// if (!command.equals(OnOffType.ON)) {
// logger.trace("handleCommand() nothing to do for anything but ON.");
// break;
// }
//
// assert veluxBridgeHandler.existingsScenes != null : "veluxBridgeHandler.existingsScenes not initialized.";
// if (!veluxBridgeHandler.existingsScenes.isRegistered(new SceneName(sceneName))) {
// logger.info("handleCommand() cannot activate unknown scene: {}.", sceneName);
// break;
// }
// logger.debug("handleCommand() activating known scene {}.", sceneName);
// thisScene = veluxBridgeHandler.existingsScenes.get(new SceneName(sceneName));
// logger.trace("handleCommand() execution scene {}.", thisScene);
// // was:
// // VeluxBridgeExecute.execute(veluxBridgeHandler, thisScene.getBridgeSceneIndex().toInt());
// new VeluxBridgeExecute().execute(veluxBridgeHandler, thisScene.getBridgeSceneIndex().toInt());
// logger.trace("handleCommand() execution done.");
// break;
//
// case CHANNEL_SCENE_SILENTMODE:
// logger.trace("Try to handle ON/OFF with channelSubId {}.", sceneName);
// if (!veluxBridgeHandler.existingsScenes.isRegistered(new SceneName(sceneName))) {
// logger.info("handleCommand() cannot activate unknown scene: {}.", sceneName);
// break;
// }
// boolean silentMode = command.equals(OnOffType.ON);
// logger.debug("handleCommand() setting silent mode to {}.", silentMode);
//
// assert veluxBridgeHandler.existingsScenes != null : "veluxBridgeHandler.existingsScenes not initialized.";
// thisScene = veluxBridgeHandler.existingsScenes.get(new SceneName(sceneName));
// logger.trace("handleCommand() execution scene {}.", thisScene);
// int sceneNumber = thisScene.getBridgeSceneIndex().toInt();
// new VeluxBridgeSceneMode().setSilentMode(veluxBridgeHandler, sceneNumber, silentMode);
// logger.trace("handleCommand() execution done.");
// break;
//
// default:
// logger.trace("handleCommand() cannot handle ON/OFF on channel {}.", channelId);
// break;
// }
// return;
// }
// logger.error("Command {} is not supported for channel: {}.", command, channelUID.getId());
// }
//
// private Object getPropertyValue(String configKey) {
// Object property = getThing().getProperties().get(configKey);
// if (property != null) {
// return property;
// } else {
// return getThing().getConfiguration().getProperties().get(configKey);
// }
// }
//
// private String getPropertyValueAsString(String configKey) {
// return (String) getPropertyValue(configKey);
// }
//
// public static String getChannelId(String getId) {
// return StringUtils.substringBefore(getId, SUBSEPARATUR);
// }
//
// public static String getChannelSubId(String getId) {
// return StringUtils.substringAfter(getId, SUBSEPARATUR);
// }
//
// }
/**
 * end-of-handler/VeluxHandler.java
 */
