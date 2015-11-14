/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.util.Map;

import org.openhab.binding.myq.myqBindingProvider;
import org.openhab.binding.myq.internal.myqBindingConfig;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class polls the Chamberlain MyQ API and sends updates to the event bus
 * of configured items in openHAB
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class myqBinding extends AbstractActiveBinding<myqBindingProvider> {
	private static final Logger logger = LoggerFactory
			.getLogger(myqBinding.class);

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
	private myqData myqOnlineData = null;

	/**
	 * The GarageDoorData. This object stores the garage door opener status
	 */
	private GarageDoorData garageStatus = null;

	/**
	 * the refresh interval which is used to poll values from the myq server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public myqBinding() {
	}

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

		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		String usernameString = (String) configuration.get("username");
		String passwordString = (String) configuration.get("password");

		// initialize connection object if username and password is set
		if (StringUtils.isNotBlank(usernameString)
				&& StringUtils.isNotBlank(passwordString)) {
			myqOnlineData = new myqData(usernameString, passwordString);
		}

		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
		String usernameString = (String) configuration.get("username");
		String passwordString = (String) configuration.get("password");

		// reinitialize connection object if username and password is changed
		if (StringUtils.isNotBlank(usernameString)
				&& StringUtils.isNotBlank(passwordString)) {
			myqOnlineData = new myqData(usernameString, passwordString);
		}
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
		return "myq Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		if (this.myqOnlineData == null) {
			return;
		}
		// Get myQ Data
		this.garageStatus = myqOnlineData.getMyqData();

		for (myqBindingProvider provider : this.providers) {
			for (String mygItemName : provider.getInBindingItemNames()) {
				myqBindingConfig deviceConfig = getConfigForItemName(mygItemName);

				if (deviceConfig != null) {
					if (this.garageStatus.getDevices().containsKey(
							deviceConfig.DeviceID)) {
						Device garageopener = this.garageStatus.getDevices()
								.get(deviceConfig.DeviceID);
						if (deviceConfig.Type == myqBindingConfig.ITEMTYPE.StringStatus)
							eventPublisher
									.postUpdate(mygItemName, new StringType(
											garageopener.GetStrStatus()));
						if (deviceConfig.Type == myqBindingConfig.ITEMTYPE.ContactStatus) {
							if (garageopener.IsDoorClosed()) {
								eventPublisher.postUpdate(mygItemName,
										OpenClosedType.CLOSED);
							} else {
								eventPublisher.postUpdate(mygItemName,
										OpenClosedType.OPEN);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		super.internalReceiveCommand(itemName, command);

		logger.debug("MyQ binding received command '" + command
				+ "' for item '" + itemName + "'");

		if (this.myqOnlineData != null) {
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
		myqBindingConfig deviceConfig = getConfigForItemName(itemName);
		if (deviceConfig == null) {
			return;
		}
		// only switch type is valid
		if (deviceConfig.Type != myqBindingConfig.ITEMTYPE.Switch) {
			return;
		}
		// only send command if switch is flipped on like pushbutton
		if ((command instanceof OnOffType) && OnOffType.ON.equals(command)) {
			this.garageStatus = myqOnlineData.getMyqData();

			if (this.garageStatus.getDevices().containsKey(
					deviceConfig.DeviceID)) {
				Device garageopener = this.garageStatus.getDevices().get(
						deviceConfig.DeviceID);
				if (garageopener.IsDoorClosed()) {//1 = open
					myqOnlineData.executeCommand(deviceConfig.DeviceID, 1, 0); 					
				} else {//0 = close
					myqOnlineData.executeCommand(deviceConfig.DeviceID, 0, 0);
				}
				// get garage status again
				this.execute();
			} else {
				logger.warn("no MyQ device found with id: "
						+ Integer.toString(deviceConfig.DeviceID));
			}
		}
	}

	/**
	 * get item config based on item name(copied from HUE binding)
	 */
	private myqBindingConfig getConfigForItemName(String itemName) {
		for (myqBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null)
				return provider.getItemConfig(itemName);
		}
		return null;
	}
}