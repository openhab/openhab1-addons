/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.protocol;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of EkozefirConnector.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirSerialThreadedConnector implements EkozefirConnector {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirSerialThreadedConnector.class);
	private final int timeoutSeconds = 5;
	private final EkozefirConnector connector;
	private ExecutorService service;

	public EkozefirSerialThreadedConnector(EkozefirConnector connector) {
		Objects.requireNonNull(connector);
		this.connector = connector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() {
		connector.connect();
		service = Executors.newSingleThreadExecutor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() {
		connector.disconnect();
		service.shutdown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] receiveBytes(final int number) {
		Future<byte[]> future = service.submit(new Callable<byte[]>() {

			@Override
			public byte[] call() throws Exception {
				return connector.receiveBytes(number);
			}

		});
		try {
			return future.get(timeoutSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (ExecutionException e) {
			logger.error(e.toString());
		} catch (TimeoutException e) {
			logger.error(e.toString());
		}
		throw new IllegalStateException("Task not finished");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendBytes(final byte[] bytes) {
		Objects.requireNonNull(bytes);
		service.submit(new Runnable() {

			@Override
			public void run() {
				connector.sendBytes(bytes);
			}

		});

	}

}
