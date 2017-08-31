/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ecotouch.EcoTouchBindingProvider;
import org.openhab.binding.ecotouch.EcoTouchTags;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
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
public class EcoTouchBinding extends AbstractActiveBinding<EcoTouchBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(EcoTouchBinding.class);

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

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
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

            EcoTouchConnector connector = new EcoTouchConnector(ip, username, password, cookies);

            // collect raw values from heat pump
            HashMap<String, Integer> rawvalues = new HashMap<String, Integer>();

            // request values (this could later be handled more efficiently
            // inside connector.getValues(tags))
            for (String tag : tags) {
                try {
                    // raw value from heat pump (needs interpretation)
                    int rawvalue = connector.getValue(tag);
                    rawvalues.put(tag, rawvalue);
                } catch (Exception e) {
                    // the connector already logged the exception cause
                    // let's ignore it and try the next value (intermittent
                    // network problem?)
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
                    State value;
                    if (item.getType() == EcoTouchTags.Type.Analog) {
                        // analog value encoded as a scaled integer
                        BigDecimal decimal = new BigDecimal(heatpumpValue).divide(BigDecimal.valueOf(item.getDivisor()),
                                1, RoundingMode.HALF_UP);
                        value = new DecimalType(decimal);
                    } else if (item.getType() == EcoTouchTags.Type.Word) {
                        // integer
                        if (NumberItem.class.equals(item.getItemClass())) {
                            value = new DecimalType(heatpumpValue);
                        } else {
                            // assume SwitchItem
                            if (heatpumpValue == 0) {
                                value = OnOffType.OFF;
                            } else {
                                value = OnOffType.ON;
                            }
                        }
                    } else {
                        // bit field
                        heatpumpValue >>= item.getBitNum();
                        heatpumpValue &= 1;
                        if (NumberItem.class.equals(item.getItemClass())) {
                            value = new DecimalType(heatpumpValue);
                        } else {
                            // assume SwitchItem
                            if (heatpumpValue == 0) {
                                value = OnOffType.OFF;
                            } else {
                                value = OnOffType.ON;
                            }
                        }
                    }

                    // now consider special cases
                    if (item == EcoTouchTags.TYPE_ADAPT_HEATING) {
                        double adapt = ((DecimalType) value).intValue();
                        adapt = Math.max(0, adapt);
                        adapt = Math.min(8, adapt);
                        adapt = (adapt - 4) / 2.0;
                        value = new DecimalType(adapt);
                    }

                    handleEventType(value, item);
                }
            }

            // store authentication info
            cookies = connector.getCookies();

        } finally {

        }

    }

    private void handleEventType(State state, EcoTouchTags heatpumpCommandType) {
        for (EcoTouchBindingProvider provider : providers) {
            for (String itemName : provider.getItemNamesForType(heatpumpCommandType)) {
                eventPublisher.postUpdate(itemName, state);
            }
        }
    }

    protected void addBindingProvider(EcoTouchBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(EcoTouchBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {

        setProperlyConfigured(false);

        if (config != null) {

            String refreshIntervalString = Objects.toString(config.get("refresh"), null);
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            String ip = Objects.toString(config.get("ip"), null); //$NON-NLS-1$
            if (StringUtils.isBlank(ip)) {
                throw new ConfigurationException("ip", "The ip address must not be empty.");
            }
            this.ip = ip;

            String username = Objects.toString(config.get("username"), null); //$NON-NLS-1$
            if (StringUtils.isBlank(username)) {
                throw new ConfigurationException("username", "The username must not be empty.");
            }
            this.username = username;

            String password = Objects.toString(config.get("password"), null); //$NON-NLS-1$
            if (StringUtils.isBlank(password)) {
                throw new ConfigurationException("password", "The password must not be empty.");
            }
            this.password = password;

            setProperlyConfigured(true);
        }
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // find the EcoTouch binding for the itemName
        EcoTouchTags tag = null;
        for (EcoTouchBindingProvider provider : providers) {
            try {
                tag = provider.getTypeForItemName(itemName);
                break;
            } catch (Exception e) {
            }
        }

        // consider special cases
        if (tag == EcoTouchTags.TYPE_ADAPT_HEATING) {
            double adapt = Double.parseDouble(command.toString());
            adapt = (adapt + 2) * 2;
            adapt = Math.max(0, adapt);
            adapt = Math.min(8, adapt);
            command = new DecimalType((int) adapt);
        }

        EcoTouchConnector connector = new EcoTouchConnector(ip, username, password, cookies);
        int value = 0;
        switch (tag.getType()) {
            case Analog:
                value = (int) (Double.parseDouble(command.toString()) * 10);
                break;
            case Word:
                if (command == OnOffType.ON) {
                    value = 1;
                } else if (command == OnOffType.OFF) {
                    value = 0;
                } else {
                    value = Integer.parseInt(command.toString());
                }
                break;
            case Bitfield:
                try {
                    // read-modify-write style
                    value = connector.getValue(tag.getTagName());
                    int bitmask = 1 << tag.getBitNum();
                    if (command == OnOffType.OFF || Integer.parseInt(command.toString()) == 0) {
                        value = value & ~bitmask;
                    } else {
                        value = value | bitmask;
                    }
                } catch (Exception e1) {
                    // connector.getValue() already logged a specific debug message
                    logger.warn("cannot send command '" + command + "' to item '" + itemName + "'");
                    return;
                }
        }

        try {
            connector.setValue(tag.getTagName(), value);
            // It does not make sense to check the returned value from
            // setValue().
            // Even if the tag is read only, one would get the newly set value
            // back.
        } catch (Exception e) {
            // connector.setValue() already logged a specific debug message
            logger.warn("cannot send command '" + command + "' to item '" + itemName + "'");
        }
    }
}
