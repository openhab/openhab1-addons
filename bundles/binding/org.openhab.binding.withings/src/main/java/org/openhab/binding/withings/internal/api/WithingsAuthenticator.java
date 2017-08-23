/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings.internal.api;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthException;

/**
 * {@link WithingsAuthenticator} is responsible for authenticating openHAB
 * against the Withings API. It uses the OSGi console to instruct the user how
 * to execute the OAuth flow in the web browser. First the user needs to execute
 * <code>withings:startAuthentication</code>. A URL for an OAuth login site is
 * printed to the OSGi console. After login the user is redirected to a callback
 * page where he finds the necessary parameters. Then he needs to execute
 * <code>withings:finishAuthentication "&lt;oauth-verifier&gt;" "&lt;user-id&gt;"</code>
 * to finish the authentication process. The {@link WithingsAuthenticator} will
 * store the oauth tokens and the user id to the file system in the
 * {@link WithingsAuthenticator#contentDir} folder.
 *
 * @see http://www.withings.com/de/api/oauthguide
 *
 * @author Dennis Nobel
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.5.0
 */
public class WithingsAuthenticator implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(WithingsAuthenticator.class);

    /** Default OAuth consumer key */
    private static final String DEFAULT_CONSUMER_KEY = "74c0e77021ef5be1ec8dcb4dd88c15539f9541c86799dcbbfcb8fc8b236";

    /** Default OAuth consumer secret */
    private static final String DEFAULT_CONSUMER_SECRET = "25f1098263e511711b3287288f90740ff45532cef91658c5043db0b0e0c851c";

    /** Default Redirect URL to which the user is redirected after the login */
    private static final String DEFAULT_REDIRECT_URL = "http://www.openhab.org/oauth/withings";

    private static final String LINE = "#########################################################################################";

    private static final String OAUTH_ACCESS_TOKEN_ENDPOINT_URL = "https://oauth.withings.com/account/access_token";

    private static final String OAUTH_AUTHORIZE_ENDPOINT_URL = "https://oauth.withings.com/account/authorize";

    private static final String OAUTH_REQUEST_TOKEN_ENDPOINT = "https://oauth.withings.com/account/request_token";

    static final String DEFAULT_ACCOUNT_ID = "DEFAULT_ACCOUNT_ID";

    /** Redirect URL to which the user is redirected after the login */
    private String redirectUrl = DEFAULT_REDIRECT_URL;

    private String consumerKey = DEFAULT_CONSUMER_KEY;
    private String consumerSecret = DEFAULT_CONSUMER_SECRET;

    private OAuthProvider provider;

    private ComponentContext componentContext;

    private Map<String, WithingsAccount> accountsCache = new HashMap<String, WithingsAccount>();

    protected void activate(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    protected void deactivate(ComponentContext componentContext) {
        this.componentContext = null;
        unregisterAccounts();
    }

    private WithingsAccount getAccount(String accountId) {
        return accountsCache.get(accountId);
    }

    /**
     * Starts the OAuth authentication flow.
     */
    public synchronized void startAuthentication(String accountId) {

        WithingsAccount withingsAccount = getAccount(accountId);
        if (withingsAccount == null) {
            logger.warn("Couldn't find Credentials of Account '{}'. Please check openhab.cfg or withings.cfg.",
                    accountId);
            return;
        }

        OAuthConsumer consumer = withingsAccount.createConsumer();

        provider = new DefaultOAuthProvider(OAUTH_REQUEST_TOKEN_ENDPOINT, OAUTH_ACCESS_TOKEN_ENDPOINT_URL,
                OAUTH_AUTHORIZE_ENDPOINT_URL);

        try {
            String url = provider.retrieveRequestToken(consumer, this.redirectUrl);
            printSetupInstructions(url);
        } catch (OAuthException ex) {
            logger.error(ex.getMessage(), ex);
            printAuthenticationFailed(ex);
        }

    }

    /**
     * Finishes the OAuth authentication flow.
     * 
     * @param verificationCode
     *            OAuth verification code
     * @param userId
     *            user id
     */
    public synchronized void finishAuthentication(String accountId, String verificationCode, String userId) {

        WithingsAccount withingsAccount = getAccount(accountId);
        if (withingsAccount == null) {
            logger.warn("Couldn't find Credentials of Account '{}'. Please check openhab.cfg or withings.cfg.",
                    accountId);
            return;
        }

        OAuthConsumer consumer = withingsAccount.consumer;

        if (provider == null || consumer == null) {
            logger.warn("Could not finish authentication. Please execute 'startAuthentication' first.");
            return;
        }

        try {
            provider.retrieveAccessToken(consumer, verificationCode);
        } catch (OAuthException ex) {
            logger.error(ex.getMessage(), ex);
            printAuthenticationFailed(ex);
            return;
        }

        withingsAccount.userId = userId;
        withingsAccount.setOuathToken(consumer.getToken(), consumer.getTokenSecret());
        withingsAccount.registerAccount(componentContext.getBundleContext());
        withingsAccount.persist();

        printAuthenticationSuccessful();
    }

    private void printSetupInstructions(String url) {
        logger.info(LINE);
        logger.info("# Withings Binding Setup: ");
        logger.info("# 1. Open URL '" + url + "' in your web browser");
        logger.info("# 2. Login, choose your user and allow openHAB to access your Withings data");
        logger.info(
                "# 3. Execute 'withings:finishAuthentication \"<accountId>\" \"<verifier>\" \"<userId>\"' on OSGi console");
        logger.info(LINE);
    }

    private void printAuthenticationInfo(String accountId) {
        logger.info(LINE);
        logger.info("# Withings Binding needs authentication of Account '{}'.", accountId);
        logger.info("# Execute 'withings:startAuthentication' \"<accountId>\" on OSGi console.");
        logger.info(LINE);
    }

    private void printAuthenticationSuccessful() {
        logger.info(LINE);
        logger.info("# Withings authentication SUCCEEDED. Binding is now ready to work.");
        logger.info(LINE);
    }

    private void printAuthenticationFailed(OAuthException ex) {
        logger.info(LINE);
        logger.info("# Withings authentication FAILED: " + ex.getMessage());
        logger.info("# Try to restart authentication by executing 'withings:startAuthentication'");
        logger.info(LINE);
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            String redirectUrl = (String) config.get("redirectUrl");
            if (StringUtils.isNotBlank(redirectUrl)) {
                this.redirectUrl = redirectUrl;
            }

            String consumerKeyString = (String) config.get("consumerkey");
            if (StringUtils.isNotBlank(consumerKeyString)) {
                this.consumerKey = consumerKeyString;
            }

            String consumerSecretString = (String) config.get("consumersecret");
            if (StringUtils.isNotBlank(consumerSecretString)) {
                this.consumerSecret = consumerSecretString;
            }

            Enumeration<String> configKeys = config.keys();
            while (configKeys.hasMoreElements()) {
                String configKey = configKeys.nextElement();

                // the config-key enumeration contains additional keys that we
                // don't want to process here ...
                if ("redirectUrl".equals(configKey) || "consumerkey".equals(configKey)
                        || "consumersecret".equals(configKey) || "service.pid".equals(configKey)) {

                    continue;
                }

                String accountId;
                String configKeyTail;

                if (configKey.contains(".")) {
                    String[] keyElements = configKey.split("\\.");
                    accountId = keyElements[0];
                    configKeyTail = keyElements[1];

                } else {
                    accountId = DEFAULT_ACCOUNT_ID;
                    configKeyTail = configKey;
                }

                WithingsAccount account = accountsCache.get(accountId);
                if (account == null) {
                    account = new WithingsAccount(accountId, consumerKey, consumerSecret);
                    accountsCache.put(accountId, account);
                }

                String value = (String) config.get(configKeyTail);

                if ("userid".equals(configKeyTail)) {
                    account.userId = value;
                } else if ("token".equals(configKeyTail)) {
                    account.token = value;
                } else if ("tokensecret".equals(configKeyTail)) {
                    account.tokenSecret = value;
                } else {
                    String msg = "The configuration key '" + configKey + "' is not recognized and will be ignored.";
                    logger.debug(msg);
                    throw new ConfigurationException(configKey, msg);
                }
            }

            registerAccounts();
        }
    }

    private void registerAccounts() {
        for (Entry<String, WithingsAccount> entry : accountsCache.entrySet()) {

            String accountId = entry.getKey();
            WithingsAccount account = entry.getValue();

            if (account.isAuthenticated()) {
                account.registerAccount(componentContext.getBundleContext());
            } else if (account.isValid()) {
                printAuthenticationInfo(accountId);
            } else {
                logger.warn(
                        "Configuration details of Account '{}' are invalid. Please check the configuration.",
                        accountId);
            }
        }
    }

    private void unregisterAccounts() {
        for (WithingsAccount account : accountsCache.values()) {
            account.unregisterAccount();
        }
    }

}
