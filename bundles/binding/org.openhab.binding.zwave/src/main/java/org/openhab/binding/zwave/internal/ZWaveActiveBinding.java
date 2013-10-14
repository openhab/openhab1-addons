/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.zwave.ZWaveBindingAction;
import org.openhab.binding.zwave.ZWaveBindingConfig;
import org.openhab.binding.zwave.ZWaveBindingProvider;
import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.SerialInterfaceException;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent.ZWaveEventType;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveActiveBinding Class. Polls Z-Wave nodes frequently, responds to item commands, and also
 * handles events coming from the Z-Wave controller.
 * 
 * @author Victor Belov
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveActiveBinding extends AbstractActiveBinding<ZWaveBindingProvider> implements ManagedService, ZWaveEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveActiveBinding.class);
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MMM d yyyy HH:mm:ss");
	private String port;
	private volatile ZWaveController zController;

	private boolean isZwaveNetworkReady = false;

	/**
	 * The refresh interval which is used to poll values from the ZWave binding (optional, defaults to 10,000ms). 
	 */
	private long refreshInterval = 10000;
	
	/**
	 * The threshold defines at every x times the interval values are refreshed
	 * that require Z-Wave network traffic. 
	 */
	private int refreshThreshold = 6;
	private int refreshCount = 0;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "ZWave Refresh Service";
	}

	/**
	 * Working method that executes refreshing of the bound items. The method is executed
	 * at every refresh interval. The nodes are polled only every 6 refreshes.
	 */
	@Override
	protected void execute() {
		
		if (!isZwaveNetworkReady) {
			logger.debug("Zwave Network isn't ready yet!");
			if (this.zController != null)
				this.zController.checkForDeadOrSleepingNodes();
			return;
		}
		
		refreshCount++;
		if (refreshCount > refreshThreshold) {
			logger.trace("Reseting Refresh Count to Zero");
			refreshCount = 0;
		} else {
			logger.trace(String.format("Refresh Count: %d", refreshCount));
		}
			
		// loop all binding providers for the Z-wave binding.
		for (ZWaveBindingProvider provider : providers) {
			// loop all bound items for this provider
			for (String itemName : provider.getItemNames()) {
				
				// default value for the binding.
				State value = UnDefType.UNDEF;
				int nodeId = provider.getZwaveBindingConfig(itemName).getNodeId();
				int endpoint = provider.getZwaveBindingConfig(itemName).getEndpoint();
				ZWaveBindingAction action = provider.getZwaveBindingConfig(itemName).getAction();
				
				ZWaveNode zNode = this.zController.getNode(nodeId);
				
				//TODO: implement a better means then polling to get values.
				//JWS: Suggestion: use a binding action to indicate polling, do not poll other devices.
				switch (action) {
					case RESTORE_LAST_VALUE:
					case NONE: // just a plain node; no reporting.
						if (refreshCount == 0)
							this.zController.requestValue(zNode.getNodeId(), endpoint);
						continue; // next item
					case REPORT_BATTERY_LEVEL:
						if (refreshCount == 0)
							this.zController.requestBatteryLevel(zNode.getNodeId(), endpoint);
						continue; // next item
					case REPORT_HOMEID:
						value = new StringType(String.format("0x%08X", zNode.getHomeId()));
						break;
					case REPORT_NODEID:
						value = new StringType(String.format("%d", zNode.getNodeId()));
						break;
					case REPORT_MANUFACTURER:
						value = new StringType(String.format("0x%04x", zNode.getManufacturer()));
						break;
					case REPORT_DEVICE_TYPE:
						value = new StringType(String.format("0x%04x", zNode.getDeviceType()));
						break;
					case REPORT_DEVICE_TYPE_ID:
						value = new StringType(String.format("0x%04x", zNode.getDeviceId()));
						break;
					case REPORT_BASIC:
						value = new StringType(String.format("0x%02x", zNode.getDeviceClass().getBasicDeviceClass().getKey()));
						break;
					case REPORT_BASIC_LABEL:
						value = new StringType(String.format("%s", zNode.getDeviceClass().getBasicDeviceClass().getLabel()));
						break;
					case REPORT_GENERIC:
						value = new StringType(String.format("0x%02x", zNode.getDeviceClass().getGenericDeviceClass().getKey()));
						break;
					case REPORT_GENERIC_LABEL:
						value = new StringType(String.format("%s", zNode.getDeviceClass().getGenericDeviceClass().getLabel()));
						break;
					case REPORT_SPECIFIC:
						value = new StringType(String.format("0x%02x", zNode.getDeviceClass().getSpecificDeviceClass().getKey()));
						break;
					case REPORT_SPECIFIC_LABEL:
						value = new StringType(String.format("%s", zNode.getDeviceClass().getSpecificDeviceClass().getLabel()));
						break;
					case REPORT_VERSION:
						value = new StringType(String.format("%s", zNode.getVersion()));
						break;
					case REPORT_ROUTING:
						value = new StringType(String.format("%s", (zNode.isRouting() == true) ? "True" : "False"));
						break;
					case REPORT_LISTENING:
						value = new StringType(String.format("%s", (zNode.isListening() == true) ? "True" : "False"));
						break;
					case REPORT_SLEEPING_DEAD:
						value = new StringType(String.format("%s", (zNode.isSleepingOrDead() == true) ? "True" : "False"));
						break;
					case REPORT_NAK:
						value = new StringType(String.format("%d", this.zController.getNAKCount()));
						break;
					case REPORT_SOF:
						value = new StringType(String.format("%d", this.zController.getSOFCount()));
						break;
					case REPORT_CAN:
						value = new StringType(String.format("%d", this.zController.getCANCount()));
						break;
					case REPORT_ACK:
						value = new StringType(String.format("%d", this.zController.getACKCount()));
						break;
					case REPORT_OOF:
						value = new StringType(String.format("%d", this.zController.getOOFCount()));
						break;
					case REPORT_LASTUPDATE:
						value = new StringType(SIMPLE_DATE_FORMAT.format(zNode.getLastUpdated()).toString());
						break;
					case REPORT_WAKE_UP_INTERVAL:
						value = new StringType("");
						ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)zNode.getCommandClass(CommandClass.WAKE_UP);
						if (wakeUpCommandClass != null)
							value = new StringType(String.format("%d", wakeUpCommandClass.getInterval()));
						break;
					default:
						logger.warn("ZWave Binding Action not supported! ZWave Binding Action = {}", action);
						continue; // next item
				}
				// post update on the bus
				eventPublisher.postUpdate(itemName, value);
			}
		}		
	}
	
	/**
	 * Handles a command update by sending the appropriate Z-Wave instructions
	 * to the controller.
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// if we are not yet initialized, don't waste time and return
		if((this.isProperlyConfigured() == false) | (isZwaveNetworkReady == false)) {
			logger.debug("internalReceiveCommand Called, But Not Properly Configure yet or Zwave Network Isn't Ready, returning.");
			return;
		}

		logger.trace("internalReceiveCommand(itemname = {}, Command = {})", itemName, command.toString());
		for (ZWaveBindingProvider provider : providers) {
			ZWaveBindingConfig bindingConfig = provider.getZwaveBindingConfig(itemName);
			
			int nodeId = bindingConfig.getNodeId();
			int endpoint = bindingConfig.getEndpoint();
			ZWaveBindingAction action = bindingConfig.getAction();
			boolean restoreLastValue = false;
			boolean onSent = false;
			
			ZWaveNode node = zController.getNode(nodeId);
			
			if (node == null) {
				logger.error("Node with id {} not found, ignoring command", nodeId);
				return;
			}
			
			logger.trace("BindingProvider = {}", provider.toString());
			logger.debug("Got nodeId = {}, endpoint = {}, action = {}", new Object[] { nodeId, endpoint, action });
			
			switch (action) {
				case RESTORE_LAST_VALUE:
					restoreLastValue = true;
				case NONE:
					if (this.zController.isConnected()) {
						logger.trace("ZWaveController is connected");
						if (command == OnOffType.ON) {
							logger.trace("Sending ON");
							// send maximum value instead of "ON" to the multilevel switch.
							ZWaveCommandClass multiLevel = 
								node.resolveCommandClass(CommandClass.SWITCH_MULTILEVEL, endpoint);
							if (multiLevel != null && !restoreLastValue) {
								this.zController.sendValue(nodeId, endpoint, 99);
							} else {
								this.zController.sendValue(nodeId, endpoint, 255);
								onSent = multiLevel != null;
							}
						} else if (command == OnOffType.OFF) {
							logger.trace("Sending OFF");
							this.zController.sendValue(nodeId, endpoint, 0);
						} else if (command == IncreaseDecreaseType.INCREASE) {
							this.zController.increaseLevel(nodeId, endpoint);
						} else if (command == IncreaseDecreaseType.DECREASE) {
							this.zController.decreaseLevel(nodeId, endpoint);
						} else if (command instanceof PercentType) {
							PercentType pt = (PercentType) command;
							int value = pt.intValue();
							if (value == 100) {
								value = 99;
							}
							logger.trace("Sending PercentType, value " + value);
							this.zController.sendValue(nodeId, endpoint, value);
						} else {
							logger.warn("Unknown command >{}<", command.toString());
						}
					} else {
						logger.warn("ZWaveController is not connected");
					}
					break;
				default:
					logger.warn("ZWave Binding Action not supported for a command! ZWave Binding Action = {}", action);
					return; // next item
				}

	 		// check if we have to update manually. We have to if we did not send an On Command.
			if (!onSent && command instanceof State) {
				eventPublisher.postUpdate(itemName, (State) command);
			}
		}
			
	}
	
	/**
	 * Activates the binding. Actually does nothing, because on activation
	 * OpenHAB always calls updated to indicate that the config is updated.
	 * Activation is done there.
	 */
	@Override
	public void activate() {
		
	}
	
	/**
	 * Deactivates the binding. The Controller is stopped and the serial interface
	 * is closed as well.
	 */
	@Override
	public void deactivate() {
		isZwaveNetworkReady = false;
		ZWaveController controller = this.zController;
		if (controller != null) {
			this.zController = null;
			controller.close();
			controller.removeEventListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config == null)
			return;
		
		// Check refresh interval configuration value
		String refreshIntervalString = (String) config.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			try {
				refreshInterval = Long.parseLong(refreshIntervalString);
			} catch (NumberFormatException ex) {
				this.setProperlyConfigured(false);
				throw new ConfigurationException("refresh", ex.getLocalizedMessage(), ex);
			}
		}
		
		// Check refresh delay configuration value.
		String refreshDelayString = (String) config.get("refreshThreshold");
		if (StringUtils.isNotBlank(refreshDelayString)) {
			try {
				refreshThreshold = Integer.parseInt(refreshDelayString);
			} catch (NumberFormatException ex) {
				this.setProperlyConfigured(false);
				throw new ConfigurationException("refreshThreshold", ex.getLocalizedMessage(), ex);
			}
		}
		
		// Check the serial port configuration value.
		// This value is mandatory.
		if (StringUtils.isNotBlank((String) config.get("port"))) {
			try {
				port = (String) config.get("port");
				logger.info("Update config, port = {}", port);
				this.setProperlyConfigured(true);
				this.deactivate();
				this.zController = new ZWaveController(port);
				zController.initialize();
				zController.addEventListener(this);
				return;
			} catch (SerialInterfaceException ex) {
				this.setProperlyConfigured(false);
				throw new ConfigurationException("port", ex.getLocalizedMessage(), ex);
			}
		}
		
		this.setProperlyConfigured(false);
	}

	/**
	 * Returns the port value.
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Event handler method for incoming Z-Wave events.
	 * @param event the incoming Z-Wave event.
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		
		// if we are not yet initialized, don't waste time and return
		if (!this.isProperlyConfigured()) {
			return;
		}
		
		if (!isZwaveNetworkReady) {
			if (event.getEventType() == ZWaveEventType.NETWORK_EVENT && ((String)event.getEventValue()).equalsIgnoreCase("INIT_DONE")) {
				logger.debug("ZWaveIncomingEvent Called, Network Event, Init Done. Setting ZWave Network Ready.");
				isZwaveNetworkReady = true;
			} else {
				logger.debug("Zwave Network Not Ready yet.");
			}			
			return;
		}
		
		logger.debug("Incoming ZWave Event [{}]", event.toString());
		switch (event.getEventType()) {
			case TRANSACTION_COMPLETED_EVENT:
				return;
			case BASIC_EVENT:
			case SWITCH_EVENT:
			case DIMMER_EVENT:
			case SENSOR_EVENT:
			case BATTERY_EVENT:
				logger.debug("Got a " + event.getEventType() + " event from Z-Wave network for nodeId = {}, state = {}, endpoint = {}", new Object[] { event.getNodeId(), event.getEventValue(), event.getEndpoint() } );
				for (ZWaveBindingProvider provider : providers) {
					logger.trace("Trying to find Item through {} provider", provider.toString());
					for (String itemName : provider.getItemNames()) {
						logger.trace("Looking in {}", itemName);
						ZWaveBindingConfig bindingConfig = provider.getZwaveBindingConfig(itemName);
						logger.trace("{} {} {}", new Object[] { bindingConfig.getNodeId(), bindingConfig.getAction(), bindingConfig.getEndpoint() });
						logger.trace("{}", String.valueOf(event.getNodeId()));
						if (bindingConfig.getNodeId() == event.getNodeId() && bindingConfig.getEndpoint() == event.getEndpoint()) {
							switch (bindingConfig.getAction()) {
							// only update NONE or RESTORE_LAST_VALUE items
							case NONE:
							case RESTORE_LAST_VALUE:
								if (event.getEventType() == ZWaveEventType.BATTERY_EVENT)
									continue;
								
								logger.debug("Will send an update to {}", itemName);
								if (event.getEventValue().equals("ON")) {
									eventPublisher.postUpdate(itemName, OnOffType.ON);
								} else if (event.getEventValue().equals("OFF")) {
									eventPublisher.postUpdate(itemName, OnOffType.OFF);
								} else if (event.getEventValue().equals("CLOSED")) {
									eventPublisher.postUpdate(itemName, OpenClosedType.CLOSED);
								} else if (event.getEventValue().equals("OPEN")) {
									eventPublisher.postUpdate(itemName, OpenClosedType.OPEN);
								} else {
									Object eventValue = event.getEventValue();
									if (eventValue instanceof Integer)
										eventPublisher.postUpdate(itemName, new PercentType((Integer)eventValue));
									else if (eventValue instanceof BigDecimal)
										eventPublisher.postUpdate(itemName, new DecimalType((BigDecimal)eventValue));
									else
										eventPublisher.postUpdate(itemName, new StringType(event.getEventValue().toString()));
								}
								break;
							case REPORT_BATTERY_LEVEL:
								if (event.getEventType() != ZWaveEventType.BATTERY_EVENT)
									continue;
								
								logger.debug("Will send an update to {}", itemName);
								eventPublisher.postUpdate(itemName, new PercentType((Integer)event.getEventValue()));
								break;
							default:
								continue; // next item
							}
						}
					}
				}
				break;
			default:
				logger.warn("Unknown event type {}", event.getEventType());
				break;
		}
	}
	
}
