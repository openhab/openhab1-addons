/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.tivo.internal;

import java.util.Dictionary;

import org.openhab.binding.tivo.TivoBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jonathangiles.tivo.TivoRemote;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.4.0
 */
public class TivoBinding extends AbstractBinding<TivoBindingProvider>implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(TivoBinding.class);

    private String host;

    public TivoBinding() {
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        final TivoBindingProvider bindingProvider = findFirstMatchingBindingProvider(itemName, command);
        final String tivoCommand = bindingProvider.getTivoCommand(itemName);

        if (host != null && !host.isEmpty()) {
            new TivoRemote(host).sendCommand(tivoCommand);
            logger.debug("Sent '" + tivoCommand + "' to Tivo");
        } else {
            logger.error("Cannot send Tivo command as host is invalid: '" + host + "'.");
        }
    }

    protected void addBindingProvider(TivoBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(TivoBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            // to specify the host one has to add a
            // parameter to openhab.cfg like tivo:host=<host>
            host = (String) config.get("host");
        }
    }

    /**
     * Find the first matching {@link TivoBindingProvider} according to
     * <code>itemName</code> and <code>command</code>.
     *
     * @param itemName
     * @param command
     *
     * @return the matching binding provider or <code>null</code> if no binding
     *         provider could be found
     */
    private TivoBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {
        TivoBindingProvider firstMatchingProvider = null;

        for (TivoBindingProvider provider : this.providers) {
            boolean match = provider.providesBindingFor(itemName);
            if (match) {
                firstMatchingProvider = provider;
                break;
            }
        }

        return firstMatchingProvider;
    }
}
