/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.expire.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.expire.ExpireBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding monitors state changes and sets the state to "Undefined" if not state change occurs within
 * the configured time
 *
 * @author Michael Wyraz
 * @since 1.9.0
 */
public class ExpireBinding extends AbstractActiveBinding<ExpireBindingProvider>
{

    private static final Logger logger = LoggerFactory.getLogger(ExpireBinding.class);

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
     * method and must not be accessed anymore once the deactivate() method was called or before activate()
     * was called.
     */
    private BundleContext bundleContext;

    /**
     * the refresh interval which is used to poll values from the Expire
     * server (optional, defaults to 60000ms)
     */
    private long refreshInterval = 1000;

    public ExpireBinding()
    {
    }

    /**
     * Called by the SCR to activate the component with its configuration read from CAS
     *
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration)
    {
        this.bundleContext = bundleContext;

        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
     *
     * @param configuration Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration)
    {
        // update the internal configuration accordingly
    }

    /**
     * Called by the SCR to deactivate the component when either the configuration is removed or
     * mandatory references are no longer satisfied or the component has simply been stopped.
     *
     * @param reason Reason code for the deactivation:<br>
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
    public void deactivate(final int reason)
    {
        this.bundleContext = null;
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval()
    {
        return refreshInterval;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName()
    {
        return "Expire Refresh Service";
    }

    protected static class ExpireConfig
    {
        protected long expireAfterMs;

        protected long nextExpireTs;
    }

    protected static Map<String, Long> nextExpireTsMap = new HashMap<>();

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute()
    {
        for (ExpireBindingProvider provider : providers)
        {
            for (String itemName : provider.getItemNames())
            {
                Long nextExpireTs = nextExpireTsMap.get(itemName);
                if (nextExpireTs == null || nextExpireTs <= System.currentTimeMillis())
                {
                    // value expired
                    logger.info("Item {} was not updated for {} - setting state to undefined", itemName, provider.getExpiresAfterAsText(itemName));
                    eventPublisher.postUpdate(itemName, UnDefType.UNDEF); // set to undefined
                    nextExpireTsMap.put(itemName, Long.MAX_VALUE); // disable expire trigger until next update
                }

            }
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState)
    {
        if (newState != UnDefType.UNDEF)
        {
            for (ExpireBindingProvider provider : providers)
            {
                if (provider.providesBindingFor(itemName))
                {
                    long expireAfterMs = provider.getExpiresAfterMs(itemName);
                    nextExpireTsMap.put(itemName, System.currentTimeMillis() + expireAfterMs);
                    break;
                }
            }
        }
    }

}
