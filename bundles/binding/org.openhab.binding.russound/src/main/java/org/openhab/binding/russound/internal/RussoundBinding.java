/**
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * Copyright (c) 2010-2015, openHAB.org and others.
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.russound.RussoundBindingConfig;
import org.openhab.binding.russound.RussoundBindingProvider;
import org.openhab.binding.russound.connection.CommandProvider;
import org.openhab.binding.russound.connection.ConnectionProvider;
import org.openhab.binding.russound.connection.ConnectionStateListener;
import org.openhab.binding.russound.connection.DeviceConnection;
import org.openhab.binding.russound.connection.TcpConnectionProvider;
import org.openhab.binding.russound.internal.busparser.RussoundInputParser;
import org.openhab.binding.russound.internal.command.RequestZoneInfoCommand;
import org.openhab.binding.russound.internal.command.RussoundCommand;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Hamilton
 * @since 1.7.0
 */
public class RussoundBinding extends AbstractBinding<RussoundBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(RussoundBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	// private BundleContext bundleContext;
	private DeviceConnection connection;

	private ZoneStack mZoneAddressStack = new ZoneStack();
	// private Set<AudioZone> mZones = new HashSet<AudioZone>();

	/**
	 * the refresh interval which is used to poll values from the Russound
	 * server (optional, defaults to 60000ms)
	 */
	// private long refreshInterval = 60000;

	private int mPort = 7777;

	public RussoundBinding() {
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
		// super.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		long refreshInterval = 0;
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		String host = (String) configuration.get("host");
		logger.debug("Russound host: " + host);

		String portString = (String) configuration.get("port");
		if (!StringUtils.isBlank(portString))
			mPort = Integer.parseInt((String) configuration.get("port"));
		// read further config parameters here ...

		ConnectionProvider connectionProvider = new TcpConnectionProvider(host,
				mPort);

		RussoundInputParser inputParser = new RussoundInputParser(
				mZoneAddressStack);

		inputParser.addPropertyChangeListener(new RussoundVolumeChangeDetector(
				providers, eventPublisher));
		inputParser.addPropertyChangeListener(new RussoundPowerChangeDetector(
				providers, eventPublisher));

		CommandProvider commandProvider = new CommandProvider() {

			public byte[] getCommand() {
				logger.debug("zone stack size: " + mZoneAddressStack.size());
				ZoneAddress address;
				if (mZoneAddressStack.size() > 0)
					address = mZoneAddressStack.popAndPutEnd();

				else {
					logger.debug("using zone 0,0 because stack is empty");
					address = new ZoneAddress(0, 0);
				}
				return new RequestZoneInfoCommand(address).getCommand(null);

			}
		};

		
		connection = new DeviceConnection(connectionProvider, inputParser,
				commandProvider);
		connection.setSendCommandFormatter(new RussoundSendCommandFormatter());
		connection.setConnectionStateListener(new ConnectionStateListener() {

			public void isConnected(boolean value) {
				if (value)
					eventPublisher.postUpdate("Russound_Connection",
							OpenClosedType.CLOSED);
				else {
					eventPublisher.postUpdate("Russound_Connection",
							OpenClosedType.OPEN);

				}

			}
		});

		connection.connect();

		// setProperlyConfigured(true);
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

		if (configuration != null) {

			String host = (String) configuration.get("host");
			logger.debug("Russound host: " + host);
			int port = Integer.parseInt((String) configuration.get("port"));
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
		// this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		Collection<RussoundBindingProvider> test = providers;
		for (RussoundBindingProvider russoundBindingProvider : test) {
			RussoundBindingConfig config = russoundBindingProvider
					.getBindingConfig(itemName);

			logger.debug("Config: " + config.toString());
			RussoundCommand russoundCommand = config.getCommand();
			connection.sendCommand(russoundCommand.getCommand(command));
			mZoneAddressStack.push(russoundCommand.getZoneAddress());

		}
		logger.debug("internalReceiveCommand({},{}) is called!", itemName,
				command);

	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName,
				newState);
	}

	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("Item binding changed: " + itemName);

		if ("Russound_Connection".equals(itemName)) {
			connection
					.setConnectionStateListener(new ConnectionStateListener() {

						public void isConnected(boolean value) {
							if (value)
								eventPublisher.postUpdate(
										"Russound_Connection",
										OpenClosedType.CLOSED);
							else {
								eventPublisher.postUpdate(
										"Russound_Connection",
										OpenClosedType.OPEN);

							}

						}
					});
		} else if (provider instanceof RussoundBindingProvider) {
			RussoundBindingProvider omniProvider = (RussoundBindingProvider) provider;
			logger.debug("binding changed");
			RussoundBindingConfig config = omniProvider
					.getBindingConfig(itemName);
			// let's register this as a zone to refresh periodically
			logger.debug("Config is: " + config);
			mZoneAddressStack.push(config.getZoneAddress());
		}

	}

}
