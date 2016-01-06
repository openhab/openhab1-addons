/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.connection;

import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_USERNAME_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_AUTH_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_HOST_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_PORT_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_SMTP_STARTTLS_ENABLE_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_HOST_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_PORT_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY;
import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY;

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class ConnectorBuilder {
    Properties mailProperties;

    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";

    private static final String SMTPHOST_PROPERTY = "smtphost";
    private static final String SMTPPORT_PROPERTY = "smtpport";
    private static final String SMTPAUTH_PROPERTY = "smtpauth";
    private static final String SMTPSTARTTLS_PROPERTY = "smtpstarttls";
    private static final String SMTPSOCKETFACTORYCLASS_PROPERTY = "smtpsocketfactoryclass";
    private static final String SMTPSOCKETFACTORYPORT_PROPERTY = "smtpsocketfactoryport";

    private static final String POP3HOST_PROPERTY = "pop3host";
    private static final String POP3PORT_PROPERTY = "pop3port";
    private static final String POP3SOCKETFACTORYCLASS_PROPERTY = "pop3socketfactoryclass";
    private static final String POP3SOCKETFACTORYPORT_PROPERTY = "pop3socketfactoryport";

    private static final Logger logger = LoggerFactory.getLogger(ConnectorBuilder.class);

    public ConnectorBuilder(Dictionary<String, ?> config) {
        this.mailProperties = createMailProperties(config);
    }

    public MailConnector createAndCheckMailConnector() throws ConfigurationException {
        MailConnector connector = null;
        
        try {
            connector = new MailConnector(mailProperties);
        } catch (Exception ex) {
            logger.error("", ex);
            ex.printStackTrace();
            throw new ConfigurationException("", "", ex);
        }

        try {
            connector.checkSMTPConnection();
        } catch (ConnectorException ex) {
            logger.error("Cannot establish SMTP connection", ex);
            throw new ConfigurationException("SMTP connection", "Cannot establish SMTP connection", ex);
        }
        try {
            connector.checkPOP3Connection();
        } catch (ConnectorException ex) {
            logger.error("Cannot establish POP3 connection", ex);
            throw new ConfigurationException("POP3 connection", "Cannot establish POP3 connection", ex);
        }

        return connector;
    }

    public MailConnector createMailConnector() {
        return new MailConnector(mailProperties);
    }

    private Properties createMailProperties(Dictionary<String, ?> config) {
        Properties props = new Properties();

        copyProperty(config, USERNAME_PROPERTY, props, MAIL_USERNAME_PROPERTY);
        copyProperty(config, PASSWORD_PROPERTY, props, MAIL_PASSWORD_PROPERTY);

        copyProperty(config, SMTPHOST_PROPERTY, props, MAIL_SMTP_HOST_PROPERTY);
        copyProperty(config, SMTPPORT_PROPERTY, props, MAIL_SMTP_PORT_PROPERTY);
        copyProperty(config, SMTPAUTH_PROPERTY, props, MAIL_SMTP_AUTH_PROPERTY);
        copyProperty(config, SMTPSTARTTLS_PROPERTY, props, MAIL_SMTP_STARTTLS_ENABLE_PROPERTY);
        copyProperty(config, SMTPSOCKETFACTORYCLASS_PROPERTY, props, MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY);
        copyProperty(config, SMTPSOCKETFACTORYPORT_PROPERTY, props, MAIL_SMTP_SOCKET_FACTORY_PORT_PROPERTY);

        copyProperty(config, POP3HOST_PROPERTY, props, MAIL_POP3_HOST_PROPERTY);
        copyProperty(config, POP3PORT_PROPERTY, props, MAIL_POP3_PORT_PROPERTY);
        copyProperty(config, POP3SOCKETFACTORYCLASS_PROPERTY, props, MAIL_POP3_SOCKET_FACTORY_CLASS_PROPERTY);
        copyProperty(config, POP3SOCKETFACTORYPORT_PROPERTY, props, MAIL_POP3_SOCKET_FACTORY_PORT_PROPERTY);

        return props;
    }

    private void copyProperty(Dictionary<String, ?> config, String configKey, Properties props, String propertyKey) {
        String val = (String) config.get(configKey);
        if (val != null) {
            props.setProperty(propertyKey, val);
        }
    }
}
