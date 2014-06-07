/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch.internal;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openhab.binding.ecotouch.EcoTouchBindingProvider;
import org.openhab.binding.ecotouch.EcoTouchTags;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */
public class EcoTouchBinding extends
		AbstractActiveBinding<EcoTouchBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(EcoTouchBinding.class);

	/**
	 * the refresh interval which is used to poll values from the EcoTouch
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	private String ip;
	private String username;
	private String password;
	private List<String> cookies = null; // authentication information

	public EcoTouchBinding() {
	}

	public void activate() {
		// logger.debug("activate() method is called!");
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		// logger.debug("deactivate() method is called!");
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
		return "EcoTouch Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		// logger.debug("execute() method is called!");

		if (!bindingsExist()) {
			logger.debug("There is no existing EcoTouch binding configuration => refresh cycle aborted!");
			return;
		}
		try {
			// collect all tags which are actively used in items
			Set<String> tags = new HashSet<String>();
			for (EcoTouchBindingProvider provider : providers) {
				for (String tag : provider.getActiveTags()) {
					tags.add(tag);
				}
			}

			EcoTouchConnector connector = new EcoTouchConnector(ip, username,
					password, cookies);

			// collect raw values from heat pump
			HashMap<String, Integer> rawvalues = new HashMap<String, Integer>();

			// request values (this could later be handled more efficiently
			// inside connector.getValues(tags))
			for (String tag : tags) {
				try {
					int rawvalue = connector.getValue(tag); // raw value from
															// heat pump (needs
															// interpretation)
					rawvalues.put(tag, rawvalue);
				} catch (Exception e) {
					continue;
				}
			}

			// post updates to event bus
			for (EcoTouchBindingProvider provider : providers) {
				for (EcoTouchTags item : provider.getActiveItems()) {
					if (!rawvalues.containsKey(item.getTagName())) {
						// could not get the value from the heat pump
						continue;
					}
					int heatpumpValue = rawvalues.get(item.getTagName());
					if (item.getType() == EcoTouchTags.Type.Analog) {
						// analog value encoded as a scaled integer
						BigDecimal decimal = new BigDecimal(heatpumpValue)
								.divide(new BigDecimal(10));
						handleEventType(new DecimalType(decimal), item);
					} else if (item.getType() == EcoTouchTags.Type.Word) {
						// integer
						handleEventType(new DecimalType(heatpumpValue), item);
					} else {
						// bit field
						heatpumpValue >>= item.getBitNum();
						heatpumpValue &= 1;
						handleEventType(new DecimalType(heatpumpValue), item);
					}
				}
			}

			// store authentication info
			cookies = connector.getCookies();

		} finally {

		}

	}

	private void handleEventType(org.openhab.core.types.State state,
			EcoTouchTags heatpumpCommandType) {
		for (EcoTouchBindingProvider provider : providers) {
			for (String itemName : provider
					.getItemNamesForType(heatpumpCommandType)) {
				eventPublisher.postUpdate(itemName, state);
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
		// logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		// logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		// logger.debug("updated() is called!");

		setProperlyConfigured(false);

		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String ip = (String) config.get("ip"); //$NON-NLS-1$
			if (StringUtils.isBlank(ip)) {
				return;
			}
			this.ip = ip;

			String username = (String) config.get("username"); //$NON-NLS-1$
			if (StringUtils.isBlank(username)) {
				return;
			}
			this.username = username;

			String password = (String) config.get("password"); //$NON-NLS-1$
			if (StringUtils.isBlank(password)) {
				return;
			}
			this.password = password;

			setProperlyConfigured(true);
		}
	}

}
