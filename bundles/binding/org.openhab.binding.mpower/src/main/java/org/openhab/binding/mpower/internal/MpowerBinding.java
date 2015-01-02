/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpower.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mpower.MpowerBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerBinding extends AbstractActiveBinding<MpowerBindingProvider>
		implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(MpowerBinding.class);

	/**
	 * the refresh interval which is used to poll values from the mPower server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	private static final String CONFIG_USERNAME = "user";
	private static final String CONFIG_HOST = "host";
	private static final String CONFIG_PASSWORD = "password";
	private static final String CONFIG_SECURE = "secure";

	private Map<String, MpowerConnector> connectors = new HashMap<String, MpowerConnector>();

	public MpowerBinding() {
	}

	public void activate() {
		// MpowerConnector conn = new MpowerConnector("mpower.lan", "mp1",
		// this);
		// connectors.put("mp1", conn);
		// conn.start();
	}

	public void deactivate() {
		// stop all connectors
		for (MpowerConnector connector : connectors.values()) {
			connector.stop();
			connector = null;
		}
		connectors.clear();
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
		return "mPower Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (itemName != null && command instanceof OnOffType) {
			OnOffType type = (OnOffType) command;
			connectors.get("mp1").send(6, type);
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

			Enumeration<String> keys = config.keys();
			//
			// put all configurations into a nice structure
			//
			HashMap<String, MpowerConfig> bindingConfigs = new HashMap<String, MpowerConfig>();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String mpowerId = StringUtils.substringBefore(key, ".");
				String configOption = StringUtils.substringAfterLast(key, ".");
				if (!"service".equals(mpowerId)) {

					MpowerConfig aConfig = null;
					if (bindingConfigs.containsKey(mpowerId)) {
						aConfig = bindingConfigs.get(mpowerId);
					} else {
						aConfig = new MpowerConfig();
						aConfig.setId(mpowerId);
						bindingConfigs.put(mpowerId, aConfig);
					}

					if (CONFIG_USERNAME.equals(configOption)) {
						aConfig.setUser((String) config.get(key));
					}

					if (CONFIG_PASSWORD.equals(configOption)) {
						aConfig.setPassword((String) config.get(key));
					}

					if (CONFIG_HOST.equals(configOption)) {
						aConfig.setHost((String) config.get(key));
					}
				}
			}

			//
			// now start or stop the connectors
			//
			for (Map.Entry<String, MpowerConfig> entry : bindingConfigs
					.entrySet()) {
				// we already know this mpower instance, lets stop and remove it
				if (connectors.containsKey(entry.getKey())) {
					MpowerConnector connector = connectors.get(entry.getKey());
					logger.debug("Stopping existing connector ", entry.getKey());
					connector.stop();
					connectors.remove(entry.getKey());
					connector = null;
				}

				// create and start
				MpowerConfig aConfig = entry.getValue();
				logger.debug("Creating and starting new connector ",
						aConfig.getId());
				MpowerConnector conn = new MpowerConnector(aConfig.getHost(),
						aConfig.getId(), aConfig.getUser(),
						aConfig.getPassword(), aConfig.isSecure(), this);
				connectors.put(aConfig.getId(), conn);
				conn.start();
			}

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

	public void receivedData(MpowerSocketState state) {
		for (MpowerBindingProvider provider : providers) {
			MpowerBindingConfig bindingCfg = provider.getConfigForAddress(state
					.getAddress());
			String volItemName = bindingCfg.getVoltageItemName(state
					.getSocket());
			State itemState = new DecimalType(state.getVoltage());
			eventPublisher.postUpdate(volItemName, itemState);

			String powerItemname = bindingCfg.getPowerItemName(state
					.getSocket());
			itemState = new DecimalType(state.getPower());
			eventPublisher.postUpdate(powerItemname, itemState);
		}

	}
}
