/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panasonictv.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.panasonictv.PanasonicTVBindingConfig;
import org.openhab.binding.panasonictv.PanasonicTVBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Andre Heuer
 * @since 1.7.0
 */
public class PanasonicTVBinding extends
		AbstractActiveBinding<PanasonicTVBindingProvider> implements
		ManagedService {

	private Map<String, String> registeredTVs = new HashMap<String, String>();

	private static final Logger logger = LoggerFactory
			.getLogger(PanasonicTVBinding.class);

	/**
	 * the refresh interval which is used to poll values from the PanansonicTV
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public PanasonicTVBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "PanansonicTV Binding";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() for item: " + itemName
				+ " with command: " + command.toString());

		if (this.providers.isEmpty()) {
			logger.error("Binding is properly configured or loaded. No provider was found.");
			return;
		}

		for (PanasonicTVBindingProvider provider : this.providers) {
			PanasonicTVBindingConfig config = provider
					.getBindingConfigForItem(itemName);
			if (config == null)
				continue;
			int response = sendCommand(config);
			if (response != 200) {
				logger.warn("Command " + config.getCommand()
						+ " to TV with IP " + registeredTVs.get(config.getTv())
						+ " failed with HTTP Reponse Code " + response);
				continue;
			}
			eventPublisher.postUpdate(itemName, OnOffType.OFF);
		}

	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveUpdate() for item: " + itemName
				+ " with state: " + newState.toString());
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			for (Enumeration<?> e = config.keys(); e.hasMoreElements();) {
				String tv = (String) e.nextElement();
				if (tv.equalsIgnoreCase("service.pid")
						|| tv.equalsIgnoreCase("refresh"))
					continue;
				logger.info("TV registered '" + tv + "' with IP '"
						+ config.get(tv) + "'");
				registeredTVs.put(tv, config.get(tv).toString());
			}

			if (registeredTVs.isEmpty()) {
				setProperlyConfigured(true);
				logger.debug("No TV was registered in config file");
			} else
				setProperlyConfigured(true);
		}
	}

	int sendCommand(PanasonicTVBindingConfig config) {
		String command = config.getCommand().toUpperCase();

		String soaprequest = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><u:X_SendKey xmlns:u=\"urn:panasonic-com:service:p00NetworkControl:1\"><X_KeyEvent>NRC_"
				+ command
				+ "-ONOFF</X_KeyEvent></u:X_SendKey></s:Body></s:Envelope>\r";

		String tvIp = registeredTVs.get(config.getTv());

		if ((tvIp == null) || tvIp.isEmpty())
			return 0;

		int port = 55000;

		try {
			Socket client = new Socket(tvIp, port);

			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					client.getOutputStream(), "UTF8"));

			String header = "POST /nrc/control_0/ HTTP/1.1\r\n";
			header = header + "Host: " + tvIp + ":55000\r\n";
			header = header
					+ "SOAPACTION: \"urn:panasonic-com:service:p00NetworkControl:1#X_SendKey\"\r\n";
			header = header + "Content-Type: text/xml; charset=\"utf-8\"\r\n";
			header = header + "Content-Length: " + soaprequest.length()
					+ "\r\n";
			header = header + "\r\n";

			String request = header + soaprequest;

			logger.debug("Request send to TV with IP " + tvIp + ": " + request);

			wr.write(header);
			wr.write(soaprequest);

			wr.flush();

			InputStream inFromServer = client.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inFromServer));

			String response = reader.readLine();

			client.close();

			logger.debug("TV Response from " + tvIp + ": " + response);

			return Integer.parseInt(response.split(" ")[1]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception in binding during execution of command: "
					+ e.getStackTrace());
		}
		return 0;
	}

	@Override
	protected void execute() {

	}
}
