/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import javax.xml.ws.BindingProvider;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.withings.WithingsBindingConfig;
import org.openhab.binding.withings.WithingsBindingProvider;
import org.openhab.binding.withings.internal.model.MeasureGroup;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Dennis Nobel
 * @since 0.1.0
 */
public class WithingsBinding extends
		AbstractActiveBinding<WithingsBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(WithingsBinding.class);

	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the Withings
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 10000;

	private WithingsAuthenticator withingsAuthenticator;

	public WithingsBinding() {
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

			setProperlyConfigured(true);
		}
	}


	private void sendRequest() throws MalformedURLException,
			IOException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		// create an HTTP request to a protected resource

		if(!this.withingsAuthenticator.isAuthenticated()) {
			logger.info("Withings binding is not authenticated. Skipping data synchronization");
			return;
		}
		
		WithingsApiClient client = this.withingsAuthenticator.getClient();
		List<MeasureGroup> measures = client.getMeasures();
		logger.info(measures.toString());
	}

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
		setProperlyConfigured(true);
	}

	protected void deactivate(ComponentContext componentContext) {
		this.bundleContext = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		try {
			sendRequest();
			for(WithingsBindingProvider provider : this.providers) {
				Collection<String> itemNames = provider.getItemNames();
				for (String itemName : itemNames) {
					WithingsBindingConfig config = provider.getItemConfig(itemName);
					logger.info(config.toString());
				}
			}
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "Withings Refresh Service";
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
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
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

	protected void setWithingsAuthenticator(
			WithingsAuthenticator withingsAuthenticator) {
		this.withingsAuthenticator = withingsAuthenticator;
	}

	protected void unsetWithingsAuthenticator(
			WithingsAuthenticator withingsAuthenticator) {
		this.withingsAuthenticator = withingsAuthenticator;
	}

}
