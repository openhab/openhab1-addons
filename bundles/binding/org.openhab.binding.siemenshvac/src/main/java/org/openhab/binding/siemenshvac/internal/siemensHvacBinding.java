/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.siemenshvac.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;
import org.openhab.binding.siemenshvac.siemensHvacBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author laurent@clae.net
 * @since 1.9.0-SNAPSHOT
 */
public class siemensHvacBinding extends
		AbstractActiveBinding<siemensHvacBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(siemensHvacBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	private BundleContext bundleContext;
	private siemensHvacConnector hvacConnector;

	/**
	 * the refresh interval which is used to poll values from the siemensHvac
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public siemensHvacBinding() {
		logger.debug("siemensHvac:siemensHvacBinding() constructor is called!");
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
		logger.debug("siemensHvac:activate() method is called!");
		this.bundleContext = bundleContext;

		hvacConnector = new siemensHvacConnector(this);

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		String baseUrl = (String) configuration.get("baseUrl");
		if (StringUtils.isNotBlank(baseUrl)) {
			hvacConnector.setBaseUrl(baseUrl);
		}

		String userName = (String) configuration.get("userName");
		if (StringUtils.isNotBlank(userName)) {
			hvacConnector.setUserName(userName);
		}

		String userPassword = (String) configuration.get("userPassword");
		if (StringUtils.isNotBlank(userPassword)) {
			hvacConnector.setUserPassword(userPassword);
		}

		// read further config parameters here ...

		setProperlyConfigured(true);
		logger.debug("siemensHvac:activate() method end!");
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
		return "siemensHvac Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("siemensHvac:execute() method is called!");

		hvacConnector.ReadAllDp();

		logger.debug("siemensHvac:execute() method return !");
		return;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("siemensHvac:internalReceiveCommand({},{}) is called!", itemName, command);
		hvacConnector.AddDpUpdate(itemName, command);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		// logger.debug("hvac:internalReceiveUpdate({},{}) is called!", itemName, newState);
		// hvacConnector.AddDpUpdate(itemName, newState);
	}

	public siemensHvacGenericBindingProvider getProvider() {
		return (siemensHvacGenericBindingProvider) providers.toArray()[0];
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

}
