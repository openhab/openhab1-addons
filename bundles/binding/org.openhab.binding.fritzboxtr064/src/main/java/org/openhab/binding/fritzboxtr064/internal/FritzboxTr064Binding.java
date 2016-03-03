/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.fritzboxtr064.FritzboxTr064BindingProvider;
import org.openhab.binding.fritzboxtr064.internal.FritzboxTr064GenericBindingProvider.FritzboxTr064BindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author gitbock
 * @since 1.8.0
 */
public class FritzboxTr064Binding extends AbstractActiveBinding<FritzboxTr064BindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
     * method and must not be accessed anymore once the deactivate() method was called or before activate()
     * was called.
     */
    private BundleContext bundleContext;

    // URL to connect to fbox. Provided in main cfg file
    private String _url;

    // Username to use to connect to fbox
    private String _user;

    // PW
    private String _pw;

    // Call monitor class/including thread
    private CallMonitor _callMonitor;

    /**
     * the refresh interval which is used to poll values from the FritzboxTr064
     * server (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    // holds Fbox TR064 connection
    private Tr064Comm _fboxComm = null;

    private PhonebookManager _pbm = null;

    public FritzboxTr064Binding() {
    }

    /**
     * Called by the SCR to activate the component with its configuration read from CAS
     *
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        this.bundleContext = bundleContext;
        logger.debug("FritzBox TR064 Binding activated!");

        // to override the default refresh interval one has to add a
        // parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
            logger.debug("Custom refresh interval set to " + refreshInterval);
        }

        // Check if fritzbox parameters were provided in config, otherwise does not make sense going on...
        String fboxurl = (String) configuration.get("url");
        String fboxuser = (String) configuration.get("user");
        String fboxpw = (String) configuration.get("pass");
        if (fboxurl == null) {
            logger.warn("Fritzbox URL was not provided in config. Shutting down binding.");
            // how to shutdown??
            setProperlyConfigured(false);
            return;
        }
        if (fboxuser == null) {
            logger.warn("Fritzbox User was not provided in config. Using default username.");
        }
        if (fboxpw == null) {
            logger.warn("Fritzbox Password was not provided in config. Shutting down binding.");
            // how to shutdown??
            setProperlyConfigured(false);
            return;
        }
        this._pw = fboxpw;
        this._user = fboxuser;
        this._url = fboxurl;

        if (_fboxComm == null) {
            _fboxComm = new Tr064Comm(_url, _user, _pw);
        }

        setProperlyConfigured(true);

    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName() {
        return "FritzboxTr064 Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {
        logger.trace("FritzboxTr064 executing...");

        for (FritzboxTr064BindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) { // check each item relevant for this binding
                FritzboxTr064BindingConfig conf = provider.getBindingConfigByItemName(itemName); // extract itemconfig
                                                                                                 // for current item
                if (conf.getConfigString().startsWith("callmonitor")) {
                    // check if we need to start call monitor
                    if (_callMonitor == null) { // not started yet
                        logger.debug(
                                "call monitor is not running. Configured items require call monitor -> Starting call monitor...");

                        if (_pbm == null) {
                            logger.debug("Downloading phonebooks");
                            _pbm = new PhonebookManager(_fboxComm);
                            _pbm.downloadPhonebooks();
                        }

                        _callMonitor = new CallMonitor(_url, eventPublisher, providers, _pbm);
                        _callMonitor.setupReconnectJob();
                        _callMonitor.startThread();
                    }
                    continue; // make sure, no callmonitor items are processed by tr064
                }

                // TR064 protocol usage
                String tr064result = _fboxComm.getTr064Value(conf.getConfigString()); // try to get value for this item
                                                                                      // config string from fbox
                if (tr064result == null) { // if value cannot be read
                    tr064result = "ERR";
                }
                Class<? extends Item> itemType = conf.getItemType();
                if (itemType.isAssignableFrom(StringItem.class)) {
                    eventPublisher.postUpdate(itemName, new StringType(tr064result));
                } else if (itemType.isAssignableFrom(ContactItem.class)) {
                    State newState = tr064result.equals("1") ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
                    eventPublisher.postUpdate(itemName, newState);
                } else if (itemType.isAssignableFrom(SwitchItem.class)) {
                    State newState = tr064result.equals("1") ? OnOffType.ON : OnOffType.OFF;
                    eventPublisher.postUpdate(itemName, newState);
                } else if (itemType.isAssignableFrom(NumberItem.class)) { // number items e.g. TAM messages
                    // tr064 retrieves only Strings, trying to parse value returned
                    int val = 0;
                    try {
                        val = Integer.parseInt(tr064result);
                    } catch (NumberFormatException ex) {
                        val = -1; // indicate error as -1
                    }

                    State newState = new DecimalType(val);
                    eventPublisher.postUpdate(itemName, newState);
                }

            }

        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.trace("internalReceiveCommand({},{}) is called!", itemName, command);
        if (_fboxComm == null) {
            _fboxComm = new Tr064Comm(_url, _user, _pw);
        }
        // Search Item Binding config for this itemName
        for (FritzboxTr064BindingProvider provider : providers) {
            FritzboxTr064BindingConfig conf = provider.getBindingConfigByItemName(itemName);
            if (conf != null) {
                _fboxComm.setTr064Value(conf.getConfigString(), command); // pass config String because config string
                                                                          // needed for finding item map
            }
        }
    }

    protected void addBindingProvider(FritzboxTr064BindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(FritzboxTr064BindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

}
