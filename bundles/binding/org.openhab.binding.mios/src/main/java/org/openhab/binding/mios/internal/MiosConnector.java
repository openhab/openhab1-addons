/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mios.internal.config.DeviceBindingConfig;
import org.openhab.binding.mios.internal.config.MiosBindingConfig;
import org.openhab.binding.mios.internal.config.SceneBindingConfig;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;

/**
 * Manages the web socket connection for a single MiOS instance.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(MiosConnector.class);

	private static final String ENCODING_CHARSET = "utf-8";

	// the MiOS instance and openHAB event publisher handles
	private final MiosUnit unit;
	private final MiosBinding binding;

	private final AsyncHttpClient client;
	private MiosPollCall pollCall;
	private Thread pollThread;
	boolean running;

	/**
	 * @param unit
	 *            The host to connect to. Give a reachable hostname or IP
	 *            address, without protocol or port
	 */
	public MiosConnector(MiosUnit unit, MiosBinding binding) {
		logger.debug("Constructor: unit '{}', binding '{}'", unit, binding);

		this.unit = unit;
		this.binding = binding;

		Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setRequestTimeoutInMs(unit.getTimeout());

		// Use the JDK Provider for now, we're not looking for server-level
		// scalability, and we'd like to lighten the load for folks wanting to
		// run atop RPi units.
		this.client = new AsyncHttpClient(new JDKAsyncHttpProvider(
				builder.build()));

		pollCall = new MiosPollCall(client, this);
		pollThread = new Thread(pollCall);
	}

	/***
	 * Check if the connection to the MiOS instance is active
	 * 
	 * @return true if an active connection to the MiOS instance exists, false
	 *         otherwise
	 */
	public boolean isConnected() {
		return isRunning() && pollCall.isConnected();
	}

	/**
	 * Attempts to create a connection to the MiOS host and begin listening for
	 * updates over the async HTTP connection.
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void open() throws IOException, InterruptedException,
			ExecutionException {
		running = true;
		pollThread.start();
	}

	/***
	 * Close this connection to the MiOS instance
	 */
	public void close() {
		running = false;
	}

	/**
	 * Poll the MiOS Unit...
	 */

	public boolean isRunning() {
		return running;
	}

	public void pollUnit() {
		logger.trace("pollUnit: Idly doing nothing");
	}

	private static final String SCENE_URL = "http://%s:%d/data_request?id=action&serviceId=urn:micasaverde-com:serviceId:HomeAutomationGateway1&action=RunScene&SceneNum=%d";

	private static final String DEVICE_URL = "http://%s:%d/data_request?id=action&DeviceNum=%d&serviceId=%s&action=%s";
	private static final String DEVICE_URL_PARAMS = DEVICE_URL + "&%s";

	private static final Pattern DEVICE_PATTERN = Pattern
			.compile("(?<serviceName>.+)/"
					+ "(?<serviceAction>.+)"
					+ "\\(((?<serviceParam>[a-zA-Z]+[a-zA-Z0-9]*)(=(?<serviceValue>.+))?)?\\)");

	private void callDevice(DeviceBindingConfig config, String upnpAction,
			State state) {
		logger.debug(
				"callDevice: Need to remote-invoke Device '{}' action '{}')",
				new Object[] { config.toProperty(), upnpAction });

		Matcher matcher = DEVICE_PATTERN.matcher(upnpAction);

		if (matcher.matches()) {
			try {
				MiosUnit u = getUnit();

				String serviceName = matcher.group("serviceName");
				String serviceAction = matcher.group("serviceAction");
				String serviceParam = matcher.group("serviceParam");
				String serviceValue = matcher.group("serviceValue");

				logger.debug(
						"callDevice: decoded as serviceName '{}' serviceAction '{}' serviceParam '{}' serviceValue '{}'",
						new Object[] { serviceName, serviceAction,
								serviceParam, serviceValue });

				// If the parameters to the URL are specified, then we need to
				// build
				// the parameter section of the URL, encoding parameter names
				// and
				// values... trust no-one 8)
				String p;
				if (serviceParam != null) {
					if (serviceValue == null || serviceValue.equals('?')) {
						p = URLEncoder.encode(serviceParam, ENCODING_CHARSET)
								+ '='
								+ URLEncoder.encode(state.toString(),
										ENCODING_CHARSET);
					} else {
						p = URLEncoder.encode(serviceParam, ENCODING_CHARSET)
								+ '='
								+ URLEncoder.encode(serviceValue,
										ENCODING_CHARSET);
					}
				} else {
					p = null;
				}

				callMios(String.format((p == null) ? DEVICE_URL
						: DEVICE_URL_PARAMS, u.getHostname(), u.getPort(),
						config.getId(), URLEncoder.encode(serviceName,
								ENCODING_CHARSET), URLEncoder.encode(
								serviceAction, ENCODING_CHARSET), p));
			} catch (UnsupportedEncodingException uee) {
				logger.debug(
						"Really, trust me, this won't happen ;)   exception='{}'",
						uee);
			}
		} else {
			logger.error(
					"callDevice: The parameter is in the wrong format.  BindingConfig '{}', UPnP Action '{}'",
					config, upnpAction);
		}

	}

	private void callScene(SceneBindingConfig config) {
		logger.debug("callScene: Need to remote-invoke Scene '{}'",
				config.toProperty());

		MiosUnit u = getUnit();
		callMios(String.format(SCENE_URL, u.getHostname(), u.getPort(),
				config.getId()));
	}

	private void callMios(String url) {
		logger.debug("callMios: Would like to fire off the URL '{}'", url);

		try {
			Future<Response> f = client.prepareGet(url).execute();

			// TODO: Run it and walk away?
			// Work out a better model to gather information about the
			// success/fail of the call, log details (etc) so things can be
			// diagnosed.

			// Response r = (Response) f.get();
			// Map<String, Object> json = readJson(r.getResponseBody());

			// if (json.containsKey("error"))
			// throw new IOException(json.get("error").toString());

		} catch (Exception e) {
			logger.debug(
					"callMios: Exception Error occurred fetching content: {}",
					e.getMessage(), e);
		}
	}

	public void invokeUpdate(MiosBindingConfig config, State newState)
			throws Exception {
		if (!config.supportsUpdates()) {
			logger.debug(
					"invokeUpdate: Received new state ({}), for config '{}', but it's not configured to handle them.",
					newState, config);

			return;
		}

		// TODO: Implement
	}

	public void invokeCommand(MiosBindingConfig config, Command command,
			State state) throws Exception {
		// If we don't support OutBound transmission, then bail out early.
		if (!config.supportsCommands()) {
			logger.debug(
					"invokeCommand: Received ({}), for config '{}', but it's not configured to handle them.",
					command, config);

			return;
		}

		// TODO: Hardcode to only take the ON Command, but externalize and make
		// more flexible.
		if (config instanceof SceneBindingConfig) {
			String newCommand = config.transformCommand(command);

			if (newCommand != null) {
				callScene((SceneBindingConfig) config);
			} else {
				logger.debug(
						"invokeScene: Command type not supported for Scenes '{}'",
						command.getClass());
			}
		} else if (config instanceof DeviceBindingConfig) {
			String newCommand = config.transformCommand(command);

			if (newCommand != null) {
				callDevice((DeviceBindingConfig) config, newCommand, state);
			} else {
				logger.debug(
						"invokeCommand: Need to remote-invoke Device '{}', but no action determined for Command '{}' ('{}')",
						new Object[] { config.toProperty(), command.toString(),
								command.getClass() });
			}
		} else {
			logger.warn(
					"Unhandled command execution for Command ('{}') on binding '{}'",
					command, config);
		}
	}

	public MiosUnit getUnit() {
		return unit;
	}

	public MiosBinding getMiosBinding() {
		return binding;
	}
}
