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

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.refreshparameters.RefreshParametersAhuCommand;
import org.openhab.binding.ekozefir.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of EkozefirMessageService.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirSerialMessageService implements EkozefirMessageService {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirSerialMessageService.class);

	private final EkozefirConnector connector;

	private final int bytesNumber = 51;

	public EkozefirSerialMessageService(EkozefirConnector connector) {
		Objects.requireNonNull(connector);
		this.connector = connector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response getResponse() {
		byte bytes[] = connector.receiveBytes(bytesNumber);
		return new Response(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(AhuCommand command) {
		Objects.requireNonNull(command);
		connector.sendBytes(command.createMessage());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() {
		connector.connect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() {
		connector.disconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getAhuName() {
		connector.sendBytes(new RefreshParametersAhuCommand().createMessage());
		byte bytes[] = connector.receiveBytes(bytesNumber);
		logger.debug("Ahu name {}", bytes[2]);
		return (char) (bytes[2]);

	}

}
