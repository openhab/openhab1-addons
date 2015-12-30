/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower.internal;

import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.mystromecopower.MyStromEcoPowerBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.binding.mystromecopower.internal.api.*;
import org.openhab.binding.mystromecopower.internal.api.mock.MockMystromClient;
import org.openhab.binding.mystromecopower.internal.api.model.MystromDevice;
import org.openhab.binding.mystromecopower.internal.util.ChangeStateJob;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The mystrom binding class.
 * 
 * @author Jordens Christophe
 * @since 1.8.0-SNAPSHOT
 */
public class MyStromEcoPowerBinding extends
		AbstractActiveBinding<MyStromEcoPowerBindingProvider> implements
		ManagedService {
	/**
	 * If set to true, use the mystrom client mock to simulate the mystrom
	 * server.
	 */
	private Boolean devMode = false;

	/**
	 * The user name to login on mystrom server.
	 */
	private String userName;

	/**
	 * The password to login on mystrom server.
	 */
	private String password;

	/**
	 * The mystrom client to call the mystrom server.
	 */
	private IMystromClient mystromClient;

	/**
	 * The openhab logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MyStromEcoPowerBinding.class);

	/**
	 * List of discovered devices with their names and id.
	 */
	protected Map<String, String> devicesMap = new HashMap<String, String>();

	/**
	 * The master device found after discovery, necessary for restart command.
	 */
	private MystromDevice masterDevice;

	/**
	 * The refresh interval which is used to poll values from the
	 * MyStromEcoPower server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/** Holds the local quartz scheduler instance */
	private Scheduler scheduler;

	/**
	 * The default constructor.
	 */
	public MyStromEcoPowerBinding() {
	}

	public void activate() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			super.activate();
		} catch (SchedulerException se) {
			logger.error("initializing scheduler throws exception", se);
		}
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "MyStromEcoPower Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");

		if (this.devicesMap.isEmpty()) {
			return;
		}

		for (MyStromEcoPowerBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				logger.debug(
						"Mystrom eco power switch '{}' state will be updated",
						itemName);

				String friendlyName = provider.getMystromFriendlyName(itemName);
				String id = this.devicesMap.get(friendlyName);

				if (id != null) {
					MystromDevice device;
					device = this.mystromClient.getDeviceInfo(id);

					if (device != null) {
						if (provider.getIsSwitch(itemName)) {
							State state = device.state.equals("on") ? OnOffType.ON
									: OnOffType.OFF;
							eventPublisher.postUpdate(itemName, state);
						}

						if (provider.getIsStringItem(itemName)) {
							// publish state of device, on/off/offline
							eventPublisher.postUpdate(itemName, new StringType(
									device.state));
						}

						if (provider.getIsNumberItem(itemName)) {
							eventPublisher.postUpdate(itemName,
									new DecimalType(device.power));
						}
					}
				} else {
					logger.warn(
							"The device itemName '{}' not found on discovery verify device is not offline",
							itemName);
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		String deviceId = null;

		for (MyStromEcoPowerBindingProvider provider : providers) {
			String switchFriendlyName = provider
					.getMystromFriendlyName(itemName);
			deviceId = this.devicesMap.get(switchFriendlyName);
			logger.debug("item '{}' is configured as '{}'", itemName,
					switchFriendlyName);

			if (deviceId != null) {
				if (provider.getIsSwitch(itemName)) {
					try {
						logger.debug(
								"Command '{}' is about to be send to item '{}'",
								command, itemName);

						if (OnOffType.ON.equals(command)
								|| OnOffType.OFF.equals(command)) {
							// on/off command
							boolean onOff = OnOffType.ON.equals(command);
							logger.debug("command '{}' transformed to '{}'",
									command, onOff ? "on" : "off");

							boolean actualState = this.mystromClient
									.getDeviceInfo(deviceId).state.equals("on");
							if (onOff == actualState) {
								// mystrom state is the same, may be due to
								// change state on/off too
								// rapidly, so postpone change state

								String scheduledCommand = deviceId + ";"
										+ onOff;
								logger.debug("Schedule command: "
										+ scheduledCommand);

								JobDetail job = JobBuilder
										.newJob(org.openhab.binding.mystromecopower.internal.util.ChangeStateJob.class)
										.usingJobData(
												org.openhab.binding.mystromecopower.internal.util.ChangeStateJob.JOB_DATA_CONTENT_KEY,
												scheduledCommand)
										.withIdentity(itemName,
												"MYSTROMECOPOWER").build();

								Date dateTrigger = new Date(
										System.currentTimeMillis() + 5000L);

								Trigger trigger = newTrigger()
										.forJob(job)
										.withIdentity(
												itemName + "_" + dateTrigger
														+ "_trigger",
												"MYSTROMECOPOWER")
										.startAt(dateTrigger).build();
								this.scheduler.scheduleJob(job, trigger);
							} else {
								if (this.masterDevice == null || (this.masterDevice != null && deviceId != this.masterDevice.id)) {
									// This is not the master device.
									if (!this.mystromClient.ChangeState(
											deviceId, onOff)) {
										// Unsuccessful state change, inform bus
										// that the good
										// state is the old one.
										eventPublisher.postUpdate(itemName,
												onOff ? OnOffType.OFF
														: OnOffType.ON);
									}
								} else {
									// This is the mater device.
									if (this.masterDevice != null && OnOffType.OFF.equals(command)) {
										// Do a reset if try to set OFF the
										// master device.
										logger.debug("Restart master device");
										this.mystromClient
												.RestartMaster(deviceId);
									}
								}
							}
						}
					} catch (Exception e) {
						logger.error("Failed to send '{}' command", command, e);
					}
				}
			} else {
				logger.error(
						"Unable to send command to '{}' device is not in discovery table",
						itemName);
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// read further config parameters here ...
			// read user name
			String userName = (String) config.get("userName");
			if (StringUtils.isNotBlank(userName)) {
				this.userName = userName;
			} else {
				throw new ConfigurationException("userName",
						"The userName to connect to myStrom must be specified in config file");
			}

			String password = (String) config.get("password");
			if (StringUtils.isNotBlank(password)) {
				this.password = password;
			} else {
				throw new ConfigurationException("password",
						"The password to connect to myStrom must be specified in config file");
			}

			if (this.devMode) {
				this.mystromClient = new MockMystromClient();
			} else {
				this.mystromClient = new MystromClient(this.userName,
						this.password, logger);
			}

			if (ChangeStateJob.MystromClient == null) {
				ChangeStateJob.MystromClient = this.mystromClient;
			}

			setProperlyConfigured(true);

			// do a discovery of all mystrom eco power to get id of devices
			try {
				this.mystromDiscovery();
			} catch (IOException e) {
				logger.error("Error doing discovery of your devices", e);
			}
		}
	}

	/**
	 * Do a discovery to find all devices on the mystrom server. Logs device's
	 * name and id.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void mystromDiscovery() throws MalformedURLException, IOException {
		List<MystromDevice> devices;
		logger.info("Do mystrom discovery");

		this.devicesMap.clear();

		if (this.mystromClient.login() == false) {
			logger.info("Invalid user or password");
			return;
		}

		devices = this.mystromClient.getDevices();

		for (MystromDevice mystromDevice : devices) {
			this.devicesMap.put(mystromDevice.name, mystromDevice.id);

			if (mystromDevice.type.equals("mst")) {
				this.masterDevice = mystromDevice;
			}

			logger.info("Mystrom device name: '{}', mystrom device id:'{}'",
					mystromDevice.name, mystromDevice.id);
		}
	}
}
