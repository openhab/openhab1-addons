package org.openhab.binding.withings.internal;

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

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithingsAuthenticator {

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

	private static final String CONSUMER_KEY = "8d512d30824ee862602f7df249e31d0476cf286bc99b4068b60d4e1c541";

	private static final String CONSUMER_SECRET = "1d0117ec8f6f4cb4cf123484f2b39d8f3524264ebefc7edbd435e0e28e60";

	private static final String DEFAULT_CONTENT_DIR = "data/withings";

	private static final Logger logger = LoggerFactory
			.getLogger(WithingsAuthenticator.class);

	private static final String OAUTH_ACCESS_TOKEN_ENDPOINT_URL = "https://oauth.withings.com/account/access_token";

	private static final String OAUTH_AUTHORIZE_ENDPOINT_URL = "https://oauth.withings.com/account/authorize";

	private static final String OAUTH_REQUEST_TOKEN_ENDPOINT = "https://oauth.withings.com/account/request_token";

	private static final String OAUTH_TOKEN_FILE_NAME = "oauth_tokens";

	private static final String USER_ID_FILE_NAME = "user";

	private WithingsApiClient client;

	private OAuthConsumer consumer;

	private String contentDir = DEFAULT_CONTENT_DIR;

	private OAuthProvider provider;

	public void finishAuthentication(String verificationCode, String userId)
			throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			IOException {

		if (provider == null || consumer == null) {
			logger.warn("Could not finish authentication. Please execute 'startAuthentication' first.");
			return;
		}

		provider.retrieveAccessToken(consumer, verificationCode);

		OAuthTokens oAuthTokens = new OAuthTokens(consumer.getToken(),
				consumer.getTokenSecret());
		
		writeToFile(oAuthTokens, OAUTH_TOKEN_FILE_NAME);
		writeToFile(userId, USER_ID_FILE_NAME);
		
		this.client = new WithingsApiClient(consumer, userId);
	}


	public WithingsApiClient getClient() {
		return client;
	}

	public boolean isAuthenticated() {
		return consumer != null && consumer.getToken() != null
				&& consumer.getTokenSecret() != null;
	}

	public void startAuthentication() throws OAuthMessageSignerException,
			OAuthNotAuthorizedException, OAuthExpectationFailedException,
			OAuthCommunicationException {

		this.consumer = createConsumer();

		provider = new DefaultOAuthProvider(OAUTH_REQUEST_TOKEN_ENDPOINT,
				OAUTH_ACCESS_TOKEN_ENDPOINT_URL, OAUTH_AUTHORIZE_ENDPOINT_URL);

		String url = provider
				.retrieveRequestToken(consumer, "http://dnobel.de");

		logger.info("Open URL '" + url + "'");
	}



	
	private OAuthConsumer createConsumer() {
		OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY,
				CONSUMER_SECRET);
		consumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
		consumer.setMessageSigner(new HmacSha1MessageSigner());
		return consumer;
	}


	private Object readFromFile(String fileName) {
		File file = new File(contentDir + File.separator + fileName);

		if (file.exists()) {
			logger.debug("Loading object from file "
					+ file.getAbsolutePath());
			try (InputStream fis = new FileInputStream(file);
					InputStream buffer = new BufferedInputStream(fis);
					ObjectInput input = new ObjectInputStream(buffer);) {
				return input.readObject();
			} catch (ClassNotFoundException | ClassCastException | IOException ex) {
				logger.error("Could not load object from file: " + ex.getMessage(),
						ex);
				return null;
			}
		} else {
			logger.debug("File does not exists.");
			return null;
		}
	}
	

	private void writeToFile(Serializable object, String fileName) {
		File file = new File(this.contentDir + File.separator + fileName);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			logger.error("Could not file: " + ex.getMessage(), ex);
		}
		logger.debug("Storing object to file " + file.getAbsolutePath());
		try (OutputStream out = new FileOutputStream(file);
				OutputStream buffer = new BufferedOutputStream(out);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(object);
		} catch (IOException ex) {
			logger.error("Could not store file: " + ex.getMessage(), ex);
		}
	}
	
	protected void activate(ComponentContext componentContext) {
		OAuthTokens oAuthTokens = (OAuthTokens) readFromFile(OAUTH_TOKEN_FILE_NAME);
		String userId = (String) readFromFile(USER_ID_FILE_NAME);
		
		if (oAuthTokens != null) {
			this.consumer = createConsumer();
			this.consumer.setTokenWithSecret(oAuthTokens.token,
					oAuthTokens.tokenSecret);
			this.consumer.setAdditionalParameters(new HttpParameters());
			this.client = new WithingsApiClient(consumer, userId);
			logger.info("Withings OAuth tokens successfully restored.");
		} else {
			logger.info("Withings binding needs authentication.");
			logger.info("Execute 'startAuthentication' on OSGi console.");
		}
	}

	protected void deactivate(ComponentContext componentContext) {
	}

}
