/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.mail.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.util.Dictionary;
import java.util.Objects;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class registers an OSGi service for the Mail action.
 *
 * @author Kai Kreuzer
 * @author John Cocula - added connectiontimeout and timeout properties, replaced String casting
 * @since 1.3.0
 */
public class MailActionService implements ActionService, ManagedService {

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the
     * action methods before executing code.
     */
    /* default */ static boolean isProperlyConfigured = false;

    public MailActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
        // deallocate Resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return Mail.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return Mail.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        if (config != null) {
            Mail.hostname = Objects.toString(config.get("hostname"), null);

            String portString = Objects.toString(config.get("port"), null);
            if (portString != null) {
                Mail.port = Integer.valueOf(portString);
            }

            Mail.username = Objects.toString(config.get("username"), null);
            Mail.password = Objects.toString(config.get("password"), null);
            Mail.from = Objects.toString(config.get("from"), null);

            String tlsString = Objects.toString(config.get("tls"), null);
            if (isNotBlank(tlsString)) {
                Mail.startTLSEnabled = tlsString.equalsIgnoreCase("true");
            }
            String sslString = Objects.toString(config.get("ssl"), null);
            if (isNotBlank(sslString)) {
                Mail.sslOnConnect = sslString.equalsIgnoreCase("true");
            }
            String popBeforeSmtpString = Objects.toString(config.get("popbeforesmtp"), null);
            if (isNotBlank(popBeforeSmtpString)) {
                Mail.popBeforeSmtp = popBeforeSmtpString.equalsIgnoreCase("true");
            }

            Mail.charset = Objects.toString(config.get("charset"), null);

            String socketConnectionTimeoutString = Objects.toString(config.get("connectiontimeout"), null);
            if (isNotBlank(socketConnectionTimeoutString)) {
                Mail.socketConnectionTimeout = Integer.valueOf(socketConnectionTimeoutString);
            }

            String socketTimeoutString = Objects.toString(config.get("timeout"), null);
            if (isNotBlank(socketTimeoutString)) {
                Mail.socketTimeout = Integer.valueOf(socketTimeoutString);
            }

            // check mandatory settings
            if (isBlank(Mail.hostname) || isBlank(Mail.from)) {
                throw new ConfigurationException("mail",
                        "Parameters 'hostname' and 'from' are mandatory and must be configured.");
            }

            // set defaults for optional settings
            if (Mail.port == null) {
                Mail.port = (Mail.startTLSEnabled || Mail.sslOnConnect) ? 587 : 25;
            }

            isProperlyConfigured = true;
        }
    }

}
