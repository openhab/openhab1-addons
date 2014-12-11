/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NeoHubConnector handles the ASCII based TCP communication between openhab and
 * neohub.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class NeoHubConnector {
	private static final Logger logger = LoggerFactory
			.getLogger(NeoHubConnector.class);

	/**
	 * Name of host or IP to connect to.
	 */
	private final String hostname;

	/**
	 * The port to connect to.
	 */
	private final int port;

	/**
	 * Maximum time in ms to wait for neohub's response. Defaults to 5000ms.
	 */
	private final int timeout = 5000;

	public NeoHubConnector(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
	}

	/**
	 * Sends the message over the network and provides the response to the
	 * response handler.
	 * 
	 * @param msg
	 *            message to neohub
	 * @param handler
	 *            handler to process the response, may be <code>null</code>
	 * @return result of handler or <code>null</code> if network problem
	 *         occurred
	 */
	public <T> T sendMessage(final String msg, final ResponseHandler<T> handler) {
		final StringBuilder response = new StringBuilder();
		final Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(hostname, port), timeout);
			final InputStreamReader in = new InputStreamReader(
					socket.getInputStream(), US_ASCII);
			final OutputStreamWriter out = new OutputStreamWriter(
					socket.getOutputStream(), US_ASCII);
			logger.debug(">> {}", msg);
			out.write(msg);
			out.write(0); // NUL terminate the command string
			out.flush();

			int l;
			while ((l = in.read()) > 0) {// NUL termination & end of stream (-1)
				response.append((char) l);
			}
		} catch (final IOException e) {
			logger.error(
					"Failed to connect to neohub [host '{}' port '{}' timeout '{}']",
					new Object[] { hostname, port, timeout });
			logger.debug("Failed to connect to neohub.", e);
			return null;
		} finally {
			IOUtils.closeQuietly(socket);
		}
		final String responseStr = response.toString();
		logger.debug("<< {}", responseStr);

		if (handler != null) {
			return handler.onResponse(responseStr);
		} else
			return null;
	}
}