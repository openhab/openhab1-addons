/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Dictionary;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.eclipse.jetty.plus.jaas.callback.ObjectCallback;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of {@link HttpContext} which adds Basic-Authentication 
 * functionality to openHAB.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class SecureHttpContext implements HttpContext, ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(SecureHttpContext.class);

	private static final String HTTP_HEADER__AUTHENTICATE = "WWW-Authenticate";

	private static final String HTTP_HEADER__AUTHORIZATION = "Authorization";
	
	private HttpContext defaultContext = null;

	private String realm = null;
	
	private static SecurityOptions securityOptions = SecurityOptions.OFF;
	
	private static SubnetInfo subnetUtils;
	
	public SecureHttpContext() {
		// default constructor
	}
	
	public SecureHttpContext(HttpContext defaultContext, final String realm) {
		this.defaultContext = defaultContext;
		this.realm = realm;
	}

	
	/**
	 * <p>@{inheritDoc}</p>
	 * <p>Delegates to <code>defaultContext.getMimeType()</code> 
	 */
	public String getMimeType(String name) {
		return this.defaultContext.getMimeType(name);
	}

	/**
	 * <p>@{inheritDoc}</p>
	 * <p>Delegates to <code>defaultContext.getResource()</code> 
	 */
	public URL getResource(String name) {
		return this.defaultContext.getResource(name);
	}

	/**
	 * @{inheritDoc}
	 */
	public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) {
		
		if (!isSecurityEnabled(request)) {
			logger.debug("security is disabled - processing aborted!");
			return true; 
		}

		boolean authenticationResult = false;
		
		try {
			String authHeader = request.getHeader(HTTP_HEADER__AUTHORIZATION);
			if (StringUtils.isBlank(authHeader)) {
				// we have never been here before ... send AuthHeader!
				sendAuthenticationHeader(response, realm);
			}
			else {
				authenticationResult = computeAuthHeader(request, authHeader, realm);
				if (!authenticationResult) {
					try {
						// login failure! wait for 5secs. and try again ...
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					sendAuthenticationHeader(response, realm);
				}
			}
		}
		catch (IOException ioe) {
			logger.warn("sending response failed", ioe.getLocalizedMessage());
		}

		return authenticationResult;
	}

	private boolean isSecurityEnabled(HttpServletRequest request) {
		switch (SecureHttpContext.securityOptions) {
			case OFF : return false;
			case EXTERNAL : return isExternalRequest(request);
			case ON  : 
			default : return true;
		}
	}

	/**
	 * Checks whether the <code>request</code>s remote address is external or
	 * internal.
	 *  
	 * @param request
	 * @return <code>true</code> if the <code>request</code>s remote address
	 * is out of range of the given netmask (see <code>security:netmask</code>
	 * configuration in openhab.cfg) or if any error occured and <code>false</code>
	 * in all other cases.
	 */
	private boolean isExternalRequest(HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		
		String remoteAddr = request.getRemoteAddr();
		
		try {
			InetAddress remoteIp = InetAddress.getByName(remoteAddr);
			if (remoteIp.isLoopbackAddress()) {
				// by definition: the loopback address is NOT external!
				return false;
			}
			
			boolean isExternal = !subnetUtils.isInRange(remoteAddr);
			logger.trace("http request is originated by '{}' which is identified as '{}'",
					remoteAddr, isExternal ? "external" : "internal");
			
			return isExternal;
		} catch (UnknownHostException uhe) {
			logger.error(uhe.getLocalizedMessage());
		}
		catch (IllegalArgumentException iae) {
			logger.warn("couldn't parse '{}' to a valid ip address", remoteAddr);
		}
		finally {
			logger.debug("checking ip is in range took {}ms", System.currentTimeMillis() - startTime);
		}
		
		// if there are any doubts we assume this request to be external!
		return true; 
	}
	
	/**
	 * Sets the authentication header for BasicAuthentication and sends the
	 * response back to the client (HTTP-StatusCode '401' UNAUTHORIZED).
	 * 
	 * @param response to set the authentication header
	 * @param realm the given <code>realm</code>
	 * 
	 * @throws IOException if an error occurred while sending <code>response</code> 
	 */
	private void sendAuthenticationHeader(HttpServletResponse response, final String realm) throws IOException {
		response.setHeader(HTTP_HEADER__AUTHENTICATE,
			HttpServletRequest.BASIC_AUTH + " realm=\"" + realm + "\"");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Parses the given <code>authHeader</code>, extracts username and password
	 * and tries to authenticate with these credentials. If the login succeeded
	 * it sets the appropriate headers to the <code>request</code>
	 * 
	 * @param request
	 * @param authHeader
	 * @param realm
	 * 
	 * @return <code>true</code> if the login succeeded and <code>false</code>
	 * in all other cases.
	 */
	private boolean computeAuthHeader(HttpServletRequest request, final String authHeader, final String realm) {
		logger.trace("received authentication request '{}'", authHeader);
		
		String[] authHeaders = authHeader.trim().split(" ");
		if (authHeaders.length == 2) {
			String authType = StringUtils.trim(authHeaders[0]);
			String authInfo = StringUtils.trim(authHeaders[1]);

			if (HttpServletRequest.BASIC_AUTH.equalsIgnoreCase(authType)) {
				String authInfoString = new String(Base64.decodeBase64(authInfo));
				String[] authInfos = authInfoString.split(":");
				if (authInfos.length < 2) {
					logger.warn("authInfos '{}' must contain two elements separated by a colon", authInfoString);
					return false;
				}		
				
				String username = authInfos[0];
				String password = authInfos[1];
				
				Subject subject = authenticate(realm, username, password);
				if (subject != null) {
					request.setAttribute(
							HttpContext.AUTHENTICATION_TYPE,
							HttpServletRequest.BASIC_AUTH);
					request.setAttribute(HttpContext.REMOTE_USER, username);
					logger.trace("authentication of user '{}' succeeded!", username);
					return true;
				}
			}
			else {
				logger.warn("we don't support '{}' authentication -> processing aborted", authType);
			}
		}
		else {
			logger.warn("authentication header '{}' must contain of two parts separated by a blank", authHeader);
		}
		
		return false;
	}

	/**
	 * <p>Authenticates the given <code>username</code> and <code>password</code>
	 * with respect to the given <code>realm</code> against the configured
	 * {@link LoginModule} (see login.conf in &lt;openhabhome&gt;/etc to learn
	 * more about the configured {@link LoginModule})</p>
	 * <p><b>Note:</b>Roles aren't supported yet!</p>
	 * 
	 * @param realm the realm used by the configured {@link LoginModule}. 
	 * <i>Note:</i> the given <code>realm</code> must be same name as configured
	 * in <code>login.conf</code>
	 * @param username
	 * @param password
	 * 
	 * @return a {@link Subject} filled with username, password, realm, etc. or
	 * <code>null</code> if the login failed
	 * @throws UnsupportedCallbackException if a {@link Callback}-instance other
	 * than {@link NameCallback} or {@link ObjectCallback} is going to be handled
	 */
	private Subject authenticate(final String realm, final String username, final String password) {
		try {
			logger.trace("going to authenticate user '{}', realm '{}'", username, realm);

			Subject subject = new Subject();
			
			LoginContext lContext = new LoginContext(realm, subject,
				new CallbackHandler() {
					public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
						for (int i = 0; i < callbacks.length; i++) {
							if (callbacks[i] instanceof NameCallback) {
								((NameCallback) callbacks[i]).setName(username);
							}
							else if (callbacks[i] instanceof ObjectCallback) {
								((ObjectCallback) callbacks[i]).setObject(password);
							}
							else {
								throw new UnsupportedCallbackException(callbacks[i]);
							}
						}
					}
				});
			lContext.login();

			// TODO: TEE: implement role handling here!
			
			return subject;
		}
		catch (LoginException le) {
			logger.warn("authentication of user '" + username + "' failed", le);
			return null;
		}
	}


	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String securityOptionsString = (String) config.get("option");
			if (StringUtils.isNotBlank(securityOptionsString)) {
				try {
					SecureHttpContext.securityOptions = SecurityOptions.valueOf(securityOptionsString.toUpperCase());
				}
				catch (IllegalArgumentException iae) {
					logger.warn("couldn't create SecurityOption '{}' - valid values are {}", securityOptionsString, SecurityOptions.values());
					SecureHttpContext.securityOptions = SecurityOptions.OFF;
				}
			} else {
				SecureHttpContext.securityOptions = SecurityOptions.OFF;
			}
			
			String netmask = (String) config.get("netmask");
			if (StringUtils.isNotBlank(netmask)) {
				SecureHttpContext.subnetUtils = new SubnetUtils(netmask).getInfo();
			}
			else {
				// set default a value ...
				SecureHttpContext.subnetUtils = new SubnetUtils("192.168.1.0/24").getInfo();
				logger.debug("couldn't find netmask configuration -> using '{}' instead", SecureHttpContext.subnetUtils.getCidrSignature());
			}
		}
	}
	
	
	/**
	 * Provides the valid SecurityOptions. Valid values are
	 * <ul>
	 * <li>ON - security is enabled in general</li>
	 * <li>INTERNET - security is enabled when request doesn't originate from the internal network</li>
	 * <li>OFF - security is disabled
	 * </ul>
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 */
	enum SecurityOptions {
		ON, EXTERNAL, OFF;
	}
	
	
}
