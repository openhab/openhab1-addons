/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oauth.signpost.exception.OAuthException;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.withings.WithingsBindingConfig;
import org.openhab.binding.withings.WithingsBindingProvider;
import org.openhab.binding.withings.internal.api.WithingsApiClient;
import org.openhab.binding.withings.internal.api.WithingsAuthenticator;
import org.openhab.binding.withings.internal.api.WithingsConnectionException;
import org.openhab.binding.withings.internal.model.Category;
import org.openhab.binding.withings.internal.model.Measure;
import org.openhab.binding.withings.internal.model.MeasureGroup;
import org.openhab.binding.withings.internal.model.MeasureType;
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
 * @author Dennis Nobel
 * @since 0.1.0
 */
public class WithingsBinding extends
		AbstractActiveBinding<WithingsBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(WithingsBinding.class);

	/**
	 * the refresh interval which is used to poll values from the Withings
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 10000;

	private WithingsAuthenticator withingsAuthenticator;

	@Override
	public void activate() {
		setProperlyConfigured(true);
	}

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
		}
	}

	@Override
	protected void execute() {

		Map<String, WithingsBindingConfig> withingsBindings = getWithingsBindings();
		if (withingsBindings.isEmpty()) {
			logger.info("No item -> withings binding found. Skipping data refresh.");
			return;
		}

		if (!this.withingsAuthenticator.isAuthenticated()) {
			logger.info("Withings binding is not authenticated. Skipping data refresh.");
			return;
		}

		updateItemStates(withingsBindings);
	}

	@Override
	protected String getName() {
		return "Withings Refresh Service";
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.warn("Withings binding does not support commands");
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// nothing to do
	}

	protected void setWithingsAuthenticator(
			WithingsAuthenticator withingsAuthenticator) {
		this.withingsAuthenticator = withingsAuthenticator;
	}

	protected void unsetWithingsAuthenticator(
			WithingsAuthenticator withingsAuthenticator) {
		this.withingsAuthenticator = withingsAuthenticator;
	}

	private Float findLastMeasureValue(List<MeasureGroup> measures,
			MeasureType measureType) {
		for (MeasureGroup measureGroup : measures) {
			if (measureGroup.category == Category.MEASURE) {
				for (Measure measure : measureGroup.measures) {
					if (measure.type == measureType) {
						return measure.getActualValue();
					}
				}
			}
		}
		return null;
	}

	private Map<String, WithingsBindingConfig> getWithingsBindings() {
		Map<String, WithingsBindingConfig> bindings = new HashMap<>();

		for (WithingsBindingProvider provider : this.providers) {
			Collection<String> itemNames = provider.getItemNames();
			for (String itemName : itemNames) {
				WithingsBindingConfig config = provider.getItemConfig(itemName);
				bindings.put(itemName, config);
			}
		}

		return bindings;
	}

	private void updateItemState(String itemName,
			WithingsBindingConfig withingsBindingConfig,
			List<MeasureGroup> measures) {

		MeasureType measureType = withingsBindingConfig.measureType;

		Float lastMeasureValue = findLastMeasureValue(measures, measureType);

		if (lastMeasureValue != null) {

			eventPublisher.postUpdate(itemName, new DecimalType(
					lastMeasureValue));
		}
	}

	private void updateItemStates(
			Map<String, WithingsBindingConfig> withingsBindings) {
		try {

			WithingsApiClient client = this.withingsAuthenticator.getClient();
			List<MeasureGroup> measures = client.getMeasures();

			if (measures == null || measures.isEmpty()) {
				logger.info("No new measures found since the last update.");
				return;
			}

			for (Entry<String, WithingsBindingConfig> withingBinding : withingsBindings
					.entrySet()) {

				WithingsBindingConfig withingsBindingConfig = withingBinding
						.getValue();
				String itemName = withingBinding.getKey();

				updateItemState(itemName, withingsBindingConfig, measures);
			}

		} catch (OAuthException | WithingsConnectionException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

}
