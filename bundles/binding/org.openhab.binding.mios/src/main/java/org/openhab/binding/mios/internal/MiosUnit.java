/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection properties for an MiOS Unit.
 *
 * Class used internally to contain the configuration of a <i>named</i> MiOS Unit, after it's been materialized from
 * openHAB's configuration components.
 *
 * The MiOS Unit is a <i>named</i> set of attributes that the MiOS Binding uses to determine where communications need
 * to be established:
 * <p>
 * <ul>
 * <li><code>Hostname</code> - the name, or IP Address of the host that's running the MiOS Unit.
 * <li><code>Port</code> - the (numeric) TCP port of the host that's running the MiOS Unit (default: <code>3480</code>)
 * <li><code>Timeout</code> - the Timeout to use for HTTP connections to the MiOS Unit (default: <code>60000</code> ms)
 * </ul>
 * <p>
 *
 * @see MiosUnitConnector
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosUnit {
    public static final String CONFIG_DEFAULT_UNIT = "default";

    /**
     * The minimal permissible timeout to be specified in the Unit configuration. This allows us some headroom to tell
     * the MiOS unit that we want to long-poll for less than this amount of time.
     */
    private static final int CONFIG_MIN_TIMEOUT = 5000;
    private static final int CONFIG_DEFAULT_TIMEOUT = 60000;

    private static final int CONFIG_MIN_MINIMUM_DELAY = 0;
    private static final int CONFIG_DEFAULT_MINIMUM_DELAY = 0;

    private static final int CONFIG_DEFAULT_PORT = 3480;
    private static final String CONFIG_DEFAULT_HOSTNAME = "127.0.0.1";

    private static final int CONFIG_DISABLE_REFRESH_COUNT = 0;
    private static final int CONFIG_DEFAULT_REFRESH_COUNT = CONFIG_DISABLE_REFRESH_COUNT;

    private static final int CONFIG_DISABLE_ERROR_COUNT = 0;
    private static final int CONFIG_DEFAULT_ERROR_COUNT = 1;

    private static final int CONFIG_MIN_STARTUP_DELAY = 0;
    private static final int CONFIG_DEFAULT_STARTUP_DELAY = 5000;

    private String name = null;
    private String hostname = CONFIG_DEFAULT_HOSTNAME;
    private int port = CONFIG_DEFAULT_PORT;
    private int timeout = CONFIG_DEFAULT_TIMEOUT;
    private int minimumDelay = CONFIG_DEFAULT_MINIMUM_DELAY;
    private int startupDelay = CONFIG_DEFAULT_STARTUP_DELAY;

    private int refreshCount = CONFIG_DEFAULT_REFRESH_COUNT;
    private int errorCount = CONFIG_DEFAULT_ERROR_COUNT;

    private static final Logger logger = LoggerFactory.getLogger(MiosUnit.class);

    /**
     * All MiOS units have a name, that must be specified as the configuration is being loaded.
     */
    public MiosUnit(String name) {
        this.name = name;
    }

    /**
     * Get the Hostname setting for the MiOS unit configuration.
     *
     * @return the Hostname/IP associated with this Unit
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the Port setting for the MiOS unit configuration.
     *
     * The default Port for a MiOS Unit is 3480, but this can be overridden in config as needed.
     *
     * @return the Port associated with the MiOS Unit.
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the Timeout setting for the MiOS Unit configuration.
     *
     * If this configuration is not specified, then it will default to 60000ms.
     *
     * @return the Timeout of the MiOS Unit, in milliseconds.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Get the Minimum time, in ms, that the MiOS Unit should wait/delay in order to "bundle" changes in their response.
     *
     * If this configuration is not specified, then it will default to 0ms, or no-delay.
     *
     * @return the Minimum Delay for dealing with the MiOS Unit, in milliseconds.
     */
    public int getMinimumDelay() {
        return minimumDelay;
    }

    /**
     * Get the Startup delay time, in ms, the MiOS Binding should wait before starting it's event loop.
     *
     * If this configuration is not specified, then it will default to 0ms, or no-delay.
     *
     * @return the Minimum Delay for dealing with the MiOS Unit, in milliseconds.
     */
    public int getStartupDelay() {
        return startupDelay;
    }

    /**
     * Get the Refresh Count setting for the MiOS Unit configuration.
     *
     * This setting is used to control how often (loop cycles) should be performed loading incremental data, before a
     * full-refresh of data should be performed from the MiOS Unit under control.
     *
     * If this setting is not specified, it will default to never performing the full-refresh on the MiOS Unit
     * (internally, a 0 value).
     *
     * @return the Full Refresh cycle count. A value of 0 will disable the Full Refresh from occurring.
     */
    public int getRefreshCount() {
        return refreshCount;
    }

    /**
     * Get the Error Count setting for the MiOS Unit configuration.
     *
     * This setting is used to control how many errors are permitted, in attempting to retrieve data from the MiOS Unit,
     * prior to forcing a full-refresh. By default, this logic is disabled (a 0 value), but it can be reset so that
     * errors in the retrieval will trigger a full data-set to be fetched.
     *
     * If this setting is not specified, it will default to never performing the full-refresh on the MiOS Unit
     * (internally, a 0 value).
     *
     * @return the Error count. A value of 0 will disable the Full Refresh from occurring upon errors.
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * Set the Hostname of the MiOS Unit configuration.
     *
     * @param hostname
     *            the hostname to use for this MiOS Unit configuration.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Set the Port of the MiOS Unit configuration.
     *
     * @param port
     *            the port to use for this MiOS Unit configuration.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set the Timeout of the MiOS Unit configuration.
     *
     * @param timeout
     *            the timeout to set for any connections associated with this MiOS Unit.
     */
    public void setTimeout(int timeout) {
        if (timeout < CONFIG_MIN_TIMEOUT) {
            logger.warn("Timeout of {} below minimum permitted, {} used.", timeout, CONFIG_MIN_TIMEOUT);
            timeout = CONFIG_MIN_TIMEOUT;
        }
        this.timeout = timeout;
    }

    /**
     * Set the Minimum Delay of the MiOS Unit configuration.
     *
     * @param delay
     *            the minimum delay, in ms, to use for any connections associated with this MiOS Unit.
     */
    public void setMinimumDelay(int delay) {
        if (delay < CONFIG_MIN_MINIMUM_DELAY) {
            logger.warn("Minimum Delay of {} below minimum permitted, {} used.", minimumDelay,
                    CONFIG_MIN_MINIMUM_DELAY);
            delay = CONFIG_MIN_MINIMUM_DELAY;
        }
        this.minimumDelay = delay;
    }

    /**
     * Set the Startup Delay of the MiOS Unit configuration.
     *
     * @param delay
     *            the minimum delay, in ms, to use for any connections associated with this MiOS Unit.
     */
    public void setStartupDelay(int delay) {
        if (delay < CONFIG_MIN_STARTUP_DELAY) {
            logger.warn("Startup Delay of {} below minimum permitted, {} used.", startupDelay,
                    CONFIG_MIN_STARTUP_DELAY);
            delay = CONFIG_MIN_STARTUP_DELAY;
        }
        this.startupDelay = delay;
    }

    /**
     * Set the Refresh Count of the MiOS Unit configuration.
     *
     * @param count
     *            the number of loop/cycles before a Full-data refresh is performed from the MiOS Unit. A value of 0
     *            disables the refresh processing.
     */
    public void setRefreshCount(int count) {
        if (count < 0) {
            logger.warn("RefreshCount {} below minimum permitted, {} used.", count, CONFIG_DISABLE_REFRESH_COUNT);
            count = CONFIG_DISABLE_REFRESH_COUNT;
        }
        this.refreshCount = count;
    }

    /**
     * Set the Error Count of the MiOS Unit configuration.
     *
     * @param errors
     *            the number of error loop/cycles before a Full-data refresh is performed from the MiOS Unit. A value of
     *            0 disables the processing.
     */
    public void setErrorCount(int errors) {
        if (errors < 0) {
            logger.warn("RefreshCount {} below minimum permitted, {} used.", errors, CONFIG_DISABLE_ERROR_COUNT);
            errors = CONFIG_DISABLE_ERROR_COUNT;
        }
        this.errorCount = errors;
    }

    /**
     * Get the name of the MiOS Unit configuration.
     *
     * Each MiOS Unit has a name within the configuration properties. If it's not named, then the "default" name is
     * "default".
     * <p>
     *
     * Otherwise, the name is that specified in the openHAB configuration.
     * <p>
     *
     * eg. <tt>mios:venice.hostname=venice.myhouse.example.com</tt>
     * <p>
     *
     * In this case, the MiOS Unit name is "<tt>venice</tt>".
     *
     * @return the name of this MiOS Unit.
     */
    public String getName() {
        return name;
    }

    /**
     * Provide any unit-specific prefix to a given property name.
     *
     * Internally this method is used to augment a "MiOS Unit neutral" property string with the name of <i>this</i> MiOS
     * Unit.
     * <p>
     *
     * In many cases, the code that builds the property string isn't aware of the MiOS Unit that it's part of, and the
     * MiOS Unit needs to be added on later.
     *
     * @param property
     *            an unformatted property name.
     * @return the property name, with the prefix for this MiOS Unit.
     */
    public String formatProperty(String property) {
        if (isDefaultUnit()) {
            return property;
        } else {
            return "unit:" + getName() + ',' + property;
        }
    }

    public boolean isDefaultUnit() {
        return (getName() == null);
    }
}
