/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.myq.MyqBindingProvider;
import org.openhab.binding.myq.internal.MyqBindingConfig;
import org.openhab.binding.myq.internal.GarageDoorDevice.GarageDoorStatus;
import org.openhab.binding.myq.internal.MyqData.InvalidLoginException;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class polls the Chamberlain MyQ API and sends updates to the event bus
 * of configured items in openHAB
 * 
 * @author Scott Hanson
 * @author Dan Cunningham
 * @since 1.8.0
 */
public class MyqBinding extends AbstractBinding<MyqBindingProvider> {
	private static final Logger logger = LoggerFactory
			.getLogger(MyqBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * The myqData. This object stores the connection data and makes API
	 * requests
	 */
	private MyqData myqOnlineData = null;

	/**
	 * the refresh interval which is used to poll values from the myq server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * We use our own polling service so we can adjust the polling rate during
	 * periods of activity when a device has been sent a command or is in motion
	 */
	private ScheduledExecutorService pollService = Executors
			.newSingleThreadScheduledExecutor();

	/**
	 * The regular polling task
	 */
	private ScheduledFuture<?> pollFuture;

	/**
	 * This task will reset the poll interval back to normal after a rapid poll
	 * cycle
	 */
	private ScheduledFuture<?> pollResetFuture;

	/**
	 * When polling quickly, how often do we poll
	 */
	private static int RAPID_REFRESH = 2000;

	/**
	 * Cap the time we poll rapidly to not overwhelm the servers with api
	 * requests.
	 */
	private static int MAX_RAPID_REFRESH = 30 * 1000;

	/**
	 * If our login credentials are invalid then we will stop api requests until
	 * our configuration is changed
	 */
	private boolean invalidCredentials;

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;
		modified(configuration);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {

		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// update the internal configuration accordingly
		String usernameString = (String) configuration.get("username");
		String passwordString = (String) configuration.get("password");

		// reinitialize connection object if username and password is changed
		if (StringUtils.isNotBlank(usernameString)
				&& StringUtils.isNotBlank(passwordString)) {
			myqOnlineData = new MyqData(usernameString, passwordString);
		}
		invalidCredentials = false;
		schedulePoll(refreshInterval);
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again

		if (pollFuture != null && !pollFuture.isCancelled())
			pollFuture.cancel(true);

		if (pollResetFuture != null && !pollResetFuture.isCancelled())
			pollResetFuture.cancel(true);
	}

	/**
	 * Poll for device changes
	 */
	private void poll() {
		if (invalidCredentials || this.myqOnlineData == null) {
			return;
		}

		try {
			// Get myQ Data
			GarageDoorData garageStatus = myqOnlineData.getGarageData();

			for (MyqBindingProvider provider : providers) {
				for (String mygItemName : provider.getInBindingItemNames()) {
					MyqBindingConfig deviceConfig = getConfigForItemName(mygItemName);

					if (deviceConfig != null) {
						GarageDoorDevice garageopener = garageStatus.getDevice(deviceConfig.deviceIndex);
						if (garageopener != null) {
							if (deviceConfig.type == MyqBindingConfig.ITEMTYPE.StringStatus) {
								eventPublisher.postUpdate(mygItemName,
										new StringType(garageopener.getStatus()
												.getLabel()));
							}
							if (deviceConfig.type == MyqBindingConfig.ITEMTYPE.ContactStatus) {
								if (garageopener.getStatus() == GarageDoorStatus.CLOSED) {
									eventPublisher.postUpdate(mygItemName,
											OpenClosedType.CLOSED);
								} else {
									eventPublisher.postUpdate(mygItemName,
											OpenClosedType.OPEN);
								}
							}
							if (deviceConfig.type == MyqBindingConfig.ITEMTYPE.Rollershutter) {
								if (garageopener.getStatus() == GarageDoorStatus.CLOSED) {
									eventPublisher.postUpdate(mygItemName,
											UpDownType.DOWN);
								} else if (garageopener.getStatus() == GarageDoorStatus.OPEN) {
									eventPublisher.postUpdate(mygItemName,
											UpDownType.UP);
								}
								// if its not open, close or unknown then its in
								// a half-way state.
								else if (garageopener.getStatus() != GarageDoorStatus.UNKNOWN) {
									eventPublisher.postUpdate(mygItemName,
											new PercentType(50));
								}
							}

							// make sure we are polling frequently
							if (garageopener.getStatus().inMotion()) {
								beginRapidPoll(false);
							}
						}
					}
				}
			}
		} catch (InvalidLoginException e) {
			logger.error("Could not log in, please check your credentials.", e);
			invalidCredentials = true;
		} catch (IOException e) {
			logger.error("Could not connect to MyQ service", e);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		super.internalReceiveCommand(itemName, command);
		logger.debug("MyQ binding received command '{}' for item '{}'",
				command, itemName);
		if (myqOnlineData != null) {
			computeCommandForItem(command, itemName);
		}
	}

	/**
	 * Checks whether the command is value and if the deviceID exists then get
	 * status of Garage Door Opener and send command to change it's state
	 * opposite of its current state
	 * 
	 * @param command
	 *            The command from the openHAB bus.
	 * @param itemName
	 *            The name of the targeted item.
	 */
	private void computeCommandForItem(Command command, String itemName) {
		
		MyqBindingConfig deviceConfig = getConfigForItemName(itemName);
		
		if (invalidCredentials || deviceConfig == null) {
			return;
		}

		try {
			GarageDoorData garageStatus = myqOnlineData.getGarageData();
			GarageDoorDevice garageopener = garageStatus.getDevice(deviceConfig.deviceIndex);
			if (garageopener != null) {
				// only send command if switch is flipped on like pushbutton
				if ((command instanceof OnOffType && OnOffType.ON
						.equals(command))) {
					if (garageopener.getStatus().isClosedOrClosing()) {
						myqOnlineData.executeGarageDoorCommand(
								garageopener.getDeviceId(), 1);
					} else {
						myqOnlineData.executeGarageDoorCommand(
								garageopener.getDeviceId(), 0);
					}
					beginRapidPoll(true);
				} else if (command instanceof UpDownType) {
					if (UpDownType.UP.equals(command)) {
						myqOnlineData.executeGarageDoorCommand(
								garageopener.getDeviceId(), 1);
					} else {
						myqOnlineData.executeGarageDoorCommand(
								garageopener.getDeviceId(), 0);
					}
					beginRapidPoll(true);
				} else {
					logger.warn("Unknown command {}", command);
				}
			} else {
				logger.warn("no MyQ device found with index: {}",
						deviceConfig.deviceIndex);
			}
		} catch (InvalidLoginException e) {
			logger.error("Could not log in, please check your credentials.", e);
			invalidCredentials = true;
		} catch (IOException e) {
			logger.error("Could not connect to MyQ service", e);
		}
	}

	/**
	 * get item config based on item name(copied from HUE binding)
	 */
	private MyqBindingConfig getConfigForItemName(String itemName) {
		for (MyqBindingProvider provider : providers) {
			if (provider.getItemConfig(itemName) != null)
				return provider.getItemConfig(itemName);
		}
		return null;
	}

	/**
	 * Schedule our polling task
	 * 
	 * @param millis
	 */
	private void schedulePoll(long millis) {

		if (pollFuture != null && !pollFuture.isCancelled())
			pollFuture.cancel(false);

		logger.debug("rapidRefreshFuture scheduleing for {} millis", millis);
		// start polling at the RAPID_REFRESH_SECS interval
		pollFuture = pollService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				poll();
			}
		}, 0, millis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Schedule the task to reset out poll rate in a future time
	 */
	private void scheduleFuturePollReset() {
		// stop rapid polling after MAX_RAPID_REFRESH_SECS
		pollResetFuture = pollService.schedule(new Runnable() {
			public void run() {
				logger.debug("rapidRefreshFutureEnd stopping");
				schedulePoll(refreshInterval);
			}
		}, MAX_RAPID_REFRESH, TimeUnit.MILLISECONDS);
	}

	/**
	 * Start rapid polling
	 * 
	 * @param restart
	 *            if already running, otherwise ignore.
	 */
	private void beginRapidPoll(boolean restart) {
		if (restart && pollResetFuture != null) {
			pollResetFuture.cancel(true);
			pollResetFuture = null;
		}

		if (pollResetFuture == null || pollResetFuture.isCancelled()) {
			schedulePoll(RAPID_REFRESH);
			scheduleFuturePollReset();
		}
	}
}