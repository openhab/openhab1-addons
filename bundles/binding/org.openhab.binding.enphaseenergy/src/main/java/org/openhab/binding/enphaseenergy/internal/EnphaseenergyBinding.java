/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Calendar;

import org.openhab.binding.enphaseenergy.EnphaseenergyBindingProvider;
import org.openhab.binding.enphaseenergy.internal.messages.SystemsRequest;
import org.openhab.binding.enphaseenergy.internal.messages.SystemsResponse;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that gets measurements from the Enphase Energy API every couple of minutes.
 * 
 * @author Markus Fritze
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @since 1.4.0
 */
public class EnphaseenergyBinding extends AbstractActiveBinding<EnphaseenergyBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(EnphaseenergyBinding.class);

    protected static final String CONFIG_REFRESH = "refresh";
    protected static final String CONFIG_USER_ID = "user_id";
    protected static final String CONFIG_KEY = "key";

	private String	user_id;
	private String 	key;

    /**
     * The refresh interval which is used to poll values from the Enphase Energy server
     * (optional, defaults to 900000ms)
     */
    private long refreshInterval = 900000;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Enphaseenergy Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return this.refreshInterval;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("incomplete-switch")
    @Override
    protected void execute() {
        logger.debug("Querying Enphase Energy API");
		try {
			// Find all systemId for active items 
			Set<Integer>	systemIds = new HashSet<Integer>();
			for (final EnphaseenergyBindingProvider provider : this.providers) {
				for (final String itemName : provider.getItemNames()) {
					final Integer systemId = provider.getSystemId(itemName);
					systemIds.add(systemId);
				}
			}
			for (final Integer systemId : systemIds) {
				final SystemsRequest request = new SystemsRequest(this.key, this.user_id, systemId);
				logger.debug("Request: {} as {}", request, request.prepare());

				final SystemsResponse response = request.execute();
				logger.debug("Response: {}", response);

				for (final EnphaseenergyBindingProvider provider : this.providers) {
					for (final String itemName : provider.getItemNames()) {
						final Integer	itemSystemId = provider.getSystemId(itemName);
						if(!systemId.equals(itemSystemId)) {
							continue;
						}
						State state = null;
						final EnphaseenergyItemType itemType = provider.getItemType(itemName);
						logger.debug("itemName {} for {} and {}", itemName, systemId, itemType);
						switch (itemType) {
						case MODULES:
							state = new DecimalType(response.getModules());
							break;
						case SIZE_W:
							state = new DecimalType(response.getSize_w());
							break;
						case CURRENT_POWER:
							state = new DecimalType(response.getCurrent_power());
							break;
						case ENERGY_TODAY:
							state = new DecimalType(response.getEnergy_today() * 0.001);
							break;
						case ENERGY_LIFETIME:
							state = new DecimalType(response.getEnergy_lifetime() * 0.001 * 0.001);
							break;
						case SUMMARY_DATE:
							state = new DateTimeType(response.getSummary_date());
							break;
						case SOURCE:
							state = new StringType(response.getSource());
							break;
						case STATUS:
							state = new StringType(response.getStatus());
							break;
						case OPERATIONAL_AT:
							state = new DateTimeType(response.getOperational_at());
							break;
						case LAST_REPORT_AT:
							state = new DateTimeType(response.getLast_report_at());
							break;
						}
						if (state != null) {
							this.eventPublisher.postUpdate(itemName, state);
						}
					}
				}
            }
		} catch (EnphaseenergyException ne) {
			logger.error(ne.getMessage());
		}
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            Enumeration<String> configKeys = config.keys();
            while (configKeys.hasMoreElements()) {
                String configKey = (String) configKeys.nextElement();
                if ("service.pid".equals(configKey)) {
                	continue;
                }

                String value = (String) config.get(configKey);
				logger.debug("Set config {} to {}",configKey,value);
				if(CONFIG_REFRESH.equals(configKey)) {
					if (isNotBlank(value)) {
						this.refreshInterval = Long.parseLong(value);
					}
				}
                else if (CONFIG_USER_ID.equals(configKey)) {
                    this.user_id = value;
                }
                else if (CONFIG_KEY.equals(configKey)) {
                    this.key = value;
                }
                else {
                    throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
                }
            }

            setProperlyConfigured(true);
        }
    }

}
