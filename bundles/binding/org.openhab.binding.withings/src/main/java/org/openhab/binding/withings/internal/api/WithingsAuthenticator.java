package org.openhab.binding.withings.internal.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author Dennis Nobel
 * @since 1.5.0
 */
public class WithingsAuthenticator implements ManagedService {

	public static final class OAuthTokens implements Serializable {

		private static final long serialVersionUID = 6071735616022465845L;

		public String token;

		public String tokenSecret;

		public OAuthTokens() {
		}

		public OAuthTokens(String token, String tokenSecret) {
			this.token = token;
			this.tokenSecret = tokenSecret;
		}
	}

	/**
	 * Default OAuth consumer key
	 */
	private static final String DEFAULT_CONSUMER_KEY = "74c0e77021ef5be1ec8dcb4dd88c15539f9541c86799dcbbfcb8fc8b236";
	/**
	 * Default OAuth consumer secret
	 */
	private static final String DEFAULT_CONSUMER_SECRET = "25f1098263e511711b3287288f90740ff45532cef91658c5043db0b0e0c851c";

	/**
	 * Default content dir for data storage
	 */
	private static final String DEFAULT_CONTENT_DIR = "data/withings";

	/**
	 * Default Redirect URL to which the user is redirected after the login
	 */
	private static final String DEFAULT_REDIRECT_URL = "http://www.openhab.org/oauth/withings";

	private static final String FILE_NAME_OAUTH_TOKEN = "oauth_tokens";

	private static final String FILE_NAME_USER_ID = "user";

	private static final String LINE = "#########################################################################################";

	private static final Logger logger = LoggerFactory
			.getLogger(WithingsAuthenticator.class);

	private static final String OAUTH_ACCESS_TOKEN_ENDPOINT_URL = "https://oauth.withings.com/account/access_token";

	private static final String OAUTH_AUTHORIZE_ENDPOINT_URL = "https://oauth.withings.com/account/authorize";

	private static final String OAUTH_REQUEST_TOKEN_ENDPOINT = "https://oauth.withings.com/account/request_token";

	private BundleContext bundleContext;

	private ServiceRegistration<?> clientServiceRegistration;

	private OAuthConsumer consumer;

	/**
	 * OAuth consumer key
	 */
	private String consumerKey = DEFAULT_CONSUMER_KEY;

	/**
	 * OAuth consumer secret
	 */
	private String consumerSecret = DEFAULT_CONSUMER_SECRET;

	private String contentDir = DEFAULT_CONTENT_DIR;

	private OAuthProvider provider;

	/**
	 * Redirect URL to which the user is redirected after the login
	 */
	private String redirectUrl = DEFAULT_REDIRECT_URL;

	/**
	 * Finishes the OAuth authentication flow.
	 * 
	 * @param verificationCode
	 *            OAuth verification code
	 * @param userId
	 *            user id
	 */
	public synchronized void finishAuthentication(String verificationCode,
			String userId) {

		if (provider == null || consumer == null) {
			logger.warn("Could not finish authentication. Please execute 'startAuthentication' first.");
			return;
		}

		try {
			provider.retrieveAccessToken(consumer, verificationCode);
		} catch (OAuthException ex) {
			logger.error(ex.getMessage(), ex);
			printAuthenticationFailed(ex);
		}

		OAuthTokens oAuthTokens = new OAuthTokens(consumer.getToken(),
				consumer.getTokenSecret());

		writeToFile(oAuthTokens, FILE_NAME_OAUTH_TOKEN);
		writeToFile(userId, FILE_NAME_USER_ID);

		registerClientAsService(userId);

		printAuthenticationSuccessful();
	}

	/**
	 * Starts the OAuth authentication flow.
	 */
	public synchronized void startAuthentication() {

		this.consumer = createConsumer();

		provider = new DefaultOAuthProvider(OAUTH_REQUEST_TOKEN_ENDPOINT,
				OAUTH_ACCESS_TOKEN_ENDPOINT_URL, OAUTH_AUTHORIZE_ENDPOINT_URL);

		try {
			String url = provider.retrieveRequestToken(consumer,
					this.redirectUrl);
			printSetupInstructions(url);
		} catch (OAuthException ex) {
			logger.error(ex.getMessage(), ex);
			printAuthenticationFailed(ex);
		}

	}

	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties != null) {

			String redirectUrl = (String) properties.get("redirectUrl");
			if (redirectUrl != null) {
				this.redirectUrl = redirectUrl;
			}

			String consumerKey = (String) properties.get("consumerKey");
			if (consumerKey != null) {
				this.consumerKey = consumerKey;
			}

			String consumerSecret = (String) properties.get("consumerSecret");
			if (consumerSecret != null) {
				this.consumerSecret = consumerSecret;
			}

			String contentDir = (String) properties.get("contentDir");
			if (contentDir != null) {
				this.contentDir = contentDir;
			}
		}
	}

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();

		OAuthTokens oAuthTokens = (OAuthTokens) readFromFile(FILE_NAME_OAUTH_TOKEN);
		String userId = (String) readFromFile(FILE_NAME_USER_ID);

		if (oAuthTokens != null) {
			this.consumer = createConsumer();
			this.consumer.setTokenWithSecret(oAuthTokens.token,
					oAuthTokens.tokenSecret);
			this.consumer.setAdditionalParameters(new HttpParameters());

			registerClientAsService(userId);

			logger.info("Withings OAuth tokens successfully restored.");
			logger.info("Withings Binding is ready to work.");
		} else {
			printAuthenticationInfo();
		}
	}

	protected void deactivate(ComponentContext componentContext) {
		if (this.clientServiceRegistration != null) {
			this.clientServiceRegistration.unregister();
		}
	}

	private OAuthConsumer createConsumer() {
		OAuthConsumer consumer = new DefaultOAuthConsumer(this.consumerKey,
				this.consumerSecret);
		consumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
		consumer.setMessageSigner(new HmacSha1MessageSigner());
		return consumer;
	}

	private void printAuthenticationFailed(OAuthException ex) {
		logger.info(LINE);
		logger.info("# Withings authentication FAILED: " + ex.getMessage());
		logger.info("# Try to restart authentication by executing 'withings:startAuthentication'");
		logger.info(LINE);
	}

	private void printAuthenticationInfo() {
		logger.info(LINE);
		logger.info("# Withings Binding needs authentication.");
		logger.info("# Execute 'withings:startAuthentication' on OSGi console.");
		logger.info(LINE);
	}

	private void printAuthenticationSuccessful() {
		logger.info(LINE);
		logger.info("# Withings authentication SUCCEEDED. Binding is now ready to work.");
		logger.info(LINE);
	}

	private void printSetupInstructions(String url) {
		logger.info(LINE);
		logger.info("# Withings Binding Setup: ");
		logger.info("# 1. Open URL '" + url + "' in your webbrowser");
		logger.info("# 2. Login, choose your user and allow openHAB to access your Withings data");
		logger.info("# 3. Execute 'withings:finishAuthentication \"<verifier>\" \"<userId>\"' on OSGi console");
		logger.info(LINE);
	}

	private Object readFromFile(String fileName) {
		File file = new File(contentDir + File.separator + fileName);

		if (file.exists()) {
			logger.debug("Loading object from file '{}'",
					file.getAbsolutePath());

			ObjectInput input = null;
			try {
				InputStream fis = new FileInputStream(file);
				InputStream buffer = new BufferedInputStream(fis);
				input = new ObjectInputStream(buffer);
				return input.readObject();
			} catch (Exception ex) {
				logger.error(
						"Could not load object from file: " + ex.getMessage(),
						ex);
				return null;
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException ignored) {
				}
			}
		} else {
			logger.debug("File '{}' does not exists.", fileName);
			return null;
		}
	}

	private void registerClientAsService(String userId) {
		Dictionary<String, Object> serviceProperties = new Hashtable<String, Object>();
		serviceProperties.put("withings.userid", userId);

		if (this.clientServiceRegistration != null) {
			this.clientServiceRegistration.unregister();
		}

		this.clientServiceRegistration = this.bundleContext.registerService(
				WithingsApiClient.class.getName(), new WithingsApiClient(
						consumer, userId), serviceProperties);
	}

	private void writeToFile(Serializable object, String fileName) {
		File file = new File(this.contentDir + File.separator + fileName);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			logger.error("Could not file: " + ex.getMessage(), ex);
		}
		logger.debug("Storing object to file '{}'", file.getAbsolutePath());
		ObjectOutput output = null;
		try {
			OutputStream out = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(out);
			output = new ObjectOutputStream(buffer);
			output.writeObject(object);
		} catch (IOException ex) {
			logger.error("Could not store file: " + ex.getMessage(), ex);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException ignored) {
			}
		}
	}

}
