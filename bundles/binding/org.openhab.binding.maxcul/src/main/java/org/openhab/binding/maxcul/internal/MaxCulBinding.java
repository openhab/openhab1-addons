/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.maxcul.MaxCulBindingProvider;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulBindingMessageProcessor;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgHandler;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.openhab.binding.maxcul.internal.messages.PairPingMsg;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULManager;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.5.0
 */
public class MaxCulBinding extends AbstractActiveBinding<MaxCulBindingProvider> implements ManagedService, MaxCulBindingMessageProcessor {

	private static final Logger logger =
		LoggerFactory.getLogger(MaxCulBinding.class);


	/**
	 * the refresh interval which is used to poll values from the MaxCul
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * The device that is used to access the CUL hardware
	 */
	private String accessDevice;

	/**
	 * This provides access to the CULFW device (e.g. USB stick)
	 */
	private CULHandler cul;

	/**
	 * This sets the address of the controller i.e. us!
	 */
	private String srcAddr = "010203";
	private final String BROADCAST_ADDRESS = "000000";

	/**
	 * Flag to indicate if we are in pairing mode. Default timeout
	 * is 60 seconds.
	 */
	private boolean pairMode = false;
	private int pairModeTimeout = 60000;

	private Map<String,Timer> timers = new HashMap<String,Timer>();

	MaxCulMsgHandler messageHandler;

	public MaxCulBinding() {
	}

	public void activate() {
		super.activate();
		setProperlyConfigured(false);
		logger.debug("Activating MaxCul binding");
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		if (cul != null)
		{
			CULManager.close(cul);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "MaxCul Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		messageHandler.checkPendingAcks();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(final String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		Timer timer = null;

		MaxCulBindingConfig bindingConfig = null;
		for (MaxCulBindingProvider provider : super.providers) {
			bindingConfig = provider.getConfigForItemName(itemName);
			if (bindingConfig != null) {
				break;
			}
		}
		logger.debug("Received command " + command.toString()
				+ " for item " + itemName);
		if (bindingConfig != null) {
			logger.debug("Found config for "+itemName);

			if (bindingConfig.deviceType == MaxCulDevice.PAIR_MODE && (command instanceof OnOffType))
			{
				switch ((OnOffType)command)
				{
					case ON:
						/* turn on pair mode and schedule disabling of pairing mode */
						pairMode = true;
						TimerTask task = new TimerTask() {
                            public void run() {
                            	logger.debug(itemName+" pairMode time out executed");
                                pairMode = false;
                                eventPublisher.postUpdate(itemName, OnOffType.OFF);
                            }
						};
						timer = timers.get(itemName);
						if(timer!=null) {
                            timer.cancel();
                            timers.remove(itemName);
						}
						timer = new Timer();
						timers.put(itemName, timer);
						timer.schedule(task, pairModeTimeout);
						logger.debug(itemName+" pairMode enabled & timeout scheduled");
						break;
					case OFF:
						/* we are manually disabling, so clear the timer and the flag */
						pairMode = false;
						timer = timers.get(itemName);
						if(timer!=null) {
							logger.debug(itemName+" pairMode timer cancelled");
                            timer.cancel();
                            timers.remove(itemName);
						}
						logger.debug(itemName+" pairMode cleared");
						break;
				}
			}
			else if ((bindingConfig.deviceType == MaxCulDevice.RADIATOR_THERMOSTAT ||
					bindingConfig.deviceType == MaxCulDevice.RADIATOR_THERMOSTAT_PLUS ||
					bindingConfig.deviceType == MaxCulDevice.WALL_THERMOSTAT) &&
					bindingConfig.feature == MaxCulFeature.THERMOSTAT)
			{
				if (command instanceof OnOffType)
				{
					// TODO handle setting thermostat to On or Off
				} else if (command instanceof DecimalType)
				{
					// TODO handle sending temperature to device
				}
			}
			else logger.warn("Command ignored as it doesn't make sense");
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("MaxCUL Reading config");
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like maxcul:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// read further config parameters here ...
			String deviceString = (String) config.get("device");
			if (StringUtils.isNotBlank(deviceString)) {
				logger.debug("Setting up device "+deviceString);
				setupDevice(deviceString);
				if (cul == null)
					throw new ConfigurationException("device", "Configuration failed. Unable to access CUL device " + deviceString);
			} else {
				setProperlyConfigured(false);
				throw new ConfigurationException("device", "No device set - please set one");
			}
			setProperlyConfigured(true);
		}
	}

	private void setupDevice(String device)
	{
		if (cul != null) {
			CULManager.close(cul);
		}
		try {
			accessDevice = device;
			logger.debug("Opening CUL device on " + accessDevice);
			cul = CULManager.getOpenCULHandler(accessDevice, CULMode.MAX);
			messageHandler = new MaxCulMsgHandler(this.srcAddr,cul);
			messageHandler.registerMaxCulBindingMessageProcessor(this);
		} catch (CULDeviceException e) {
			logger.error("Cannot open CUL device", e);
			cul = null;
			accessDevice = null;
		}
	}

	@Override
	public void MaxCulMsgReceived(String data) {
		logger.debug("Received data from CUL: "+data);
		if (data.startsWith("Z"))
		{
			MaxCulMsgType msgType = BaseMsg.getMsgType(data);
			if (pairMode && msgType == MaxCulMsgType.PAIR_PING)
			{
				logger.debug("Got PAIR_PING message");
				/* process packet */
				PairPingMsg pkt = new PairPingMsg(data);
				/* is it valid? and is this for us? or a broadcast? */
				if (pkt.len > 0 && (pkt.dstAddrStr.compareToIgnoreCase(this.srcAddr) == 0 || (pkt.dstAddrStr.compareToIgnoreCase(BROADCAST_ADDRESS) == 0)))
				{
					/* Match serial number to binding configuration */
					Collection<MaxCulBindingConfig> bindingConfigs = null;
					for (MaxCulBindingProvider provider : super.providers) {
						bindingConfigs = provider.getConfigsForSerialNumber(pkt.serial);
						if (bindingConfigs != null) {
							break;
						}
					}
					if (bindingConfigs == null)
					{
						logger.error("Unable to find configuration for serial "+pkt.serial+". Do you have a binding for it?");
						return;
					}
					/* Set pairing information */
					for (MaxCulBindingConfig bc : bindingConfigs)
						bc.setPairedInfo(pkt.srcAddrStr); /* where it came from gives the addr of the device */

					/* send response to unit */
					messageHandler.sendPairPong(pkt.srcAddrStr);
				} else {
					logger.debug("Got pairing message for another controller");
				}
			}
			else
			{
				/* TODO handle all other incoming messages */
			}
		}
	}
}
