/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings.internal.api;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;

import org.apache.commons.io.IOUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds the Withings Account Credentials. It can also load 
 * and store the credentials the either the {@code openhab.cfg} (legacy)
 * or the {@code services/withings-ouath.cfg} file. Where to store values
 * is decided whether {@code openhab.cfg} contains already keys 
 * starting with {@code withings-oauth} or not.
 * 
 * Note: We'd decided to reimplement {@code store} and {@code load} rather
 * than using the {@link java.util.Properties} class because our keys can
 * contain ":" which unfortunately is a reserved delimiter in 
 * {@link java.util.Properties}. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.7.0
 */
public final class WithingsAccount {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(WithingsAccount.class);

	private static final String SERVICE_NAME = "withings-oauth";

	private static final String CONFIG_DIR = "." + File.separator + "configurations";

	private static final String CONTENT_DIR = CONFIG_DIR + File.separator + "services";

	private String accountId;
	
	String userId;
	String consumerKey;
	String consumerSecret;
	String token;
	String tokenSecret;
	
	OAuthConsumer consumer;
	
	ServiceRegistration<?> clientServiceRegistration;

	public WithingsAccount(String accountId, String consumerKey, String consumerSecret) {
		this.accountId = accountId;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public boolean isValid() {
		return isNotBlank(consumerKey) && isNotBlank(consumerSecret);
	}
	
	public void registerAccount(BundleContext bundleContext) {
		
		Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
			serviceProperties.put("withings.accountid", accountId);
			serviceProperties.put("withings.userid", userId);

		if (this.clientServiceRegistration != null) {
			this.clientServiceRegistration.unregister();
		}

		this.clientServiceRegistration = bundleContext.registerService(
				WithingsApiClient.class.getName(), 
				new WithingsApiClient(consumer, userId), serviceProperties);
	}

	public boolean isAuthenticated() {
		return isNotBlank(userId)
			&& isNotBlank(consumerKey) && isNotBlank(consumerSecret)
			&& isNotBlank(token) && isNotBlank(tokenSecret);
	}
	
	public OAuthConsumer createConsumer() {
		consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
		consumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
		consumer.setMessageSigner(new HmacSha1MessageSigner());
		return consumer;
	}
	
	public void setOuathToken(String token, String tokenSecret) {
		this.token = token;
		this.tokenSecret = tokenSecret;
		consumer.setTokenWithSecret(token, tokenSecret);
		consumer.setAdditionalParameters(new HttpParameters());
	}
	
	public void unregisterAccount() {
		if (this.clientServiceRegistration != null) {
			this.clientServiceRegistration.unregister();
		}
	}
	
	public void persist() {
		File file = null;
		String prefix = "";
					
		try {
			// store properties either to openhab.cfg (legacy) or
			// services/withings-oauth.cfg
			if (isLegacyConfiguration()) {
				file = new File(CONFIG_DIR + File.separator + "openhab.cfg");
				// in legacy case each property has to be prefixed with 
				prefix = SERVICE_NAME + ":";
			} else {
				file = new File(CONTENT_DIR + File.separator + SERVICE_NAME + ".cfg");
			}
			
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			
			// if an account different from the default account is used
			// it get's prefixed separated by "."
			if (!WithingsAuthenticator.DEFAULT_ACCOUNT_ID.equals(accountId)) {
				prefix += accountId + ".";
			}
			
			Map<String, String> config = load(file);
			
			config.put(prefix + "userid", userId);
			config.put(prefix + "token", token);
			config.put(prefix + "tokensecret", tokenSecret);
			
			store(config, file);
			
			logger.debug("Saved WithingsAccount to file '{}'.", file.getAbsolutePath());
		}
		catch (IOException ioe) {
			logger.error("Couldn't write WithingsAccount to file '{}'.", file.getAbsolutePath());
		}
	}
	
	/**
	 * Checks whether the Binding configuration has been store to
	 * openhab.cfg rather than the /services/withings-oauth.cfg dir
	 *  
	 * @return true, if there is a configuration key like "withings-oauth"
	 * in openhab.cfg and false in all other cases. 
	 */
	private boolean isLegacyConfiguration() {
		File file = new File(CONFIG_DIR + File.separator + "openhab.cfg");
		
		try {
			if (file.exists()) {
				Map<String, String> config = load(file);
				for (Object key : config.keySet()) {
					if (key.toString().startsWith(SERVICE_NAME)) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			logger.warn("Couldn't open Configuration File '{}'", file.getAbsolutePath());
		}
		
		return false;
	}
	
	private Map<String, String> load(File file) throws IOException {
		Map<String, String> config = new LinkedHashMap<String, String>();
		FileInputStream is = new FileInputStream(file);
		List<String> lines = IOUtils.readLines(is);
		for (String line : lines) {
			String[] parameterPair = line.split("=");
			if (parameterPair.length == 2) {
				config.put(parameterPair[0].trim(), parameterPair[1].trim());
			} else {
				config.put(parameterPair[0], "");
			}
		}
		IOUtils.closeQuietly(is);
		return config;
	}

	private Map<String, String> store(Map<String, String> config, File file) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		
		List<String> lines = new ArrayList<String>();
		for (Entry<String, String> line: config.entrySet()) {
			String value = isBlank(line.getValue()) ? "" : "=" + line.getValue();
			lines.add(line.getKey() + value);
		}
		
		IOUtils.writeLines(lines, System.getProperty("line.separator"), os);
		IOUtils.closeQuietly(os);
		return config;
	}
	
	@Override
	public String toString() {
		return "WithingsAccount [userId=" + userId + ", token=" + token + ", tokenSecret=" + tokenSecret + "]";
	}
	
}
