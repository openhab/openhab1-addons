/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.fatekplc.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.simplify4u.jfatek.io.FatekConfig;
import org.simplify4u.jfatek.io.FatekConnection;
import org.simplify4u.jfatek.io.FatekConnectionFactory;

/**
 * Serial connection for Fatek PLC binding.
 *
 * TODO - to implemented.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 *
 */
public class SerialFatekConnectionFactory implements FatekConnectionFactory {

	class Connection extends FatekConnection {

		protected Connection(FatekConfig fatekConfig) {
			super(fatekConfig);

			// TODO - init and open serial port here
		}

		@Override
		protected InputStream getInputStream() throws IOException {
			throw new IOException("Not implemented");
		}

		@Override
		protected OutputStream getOutputStream() throws IOException {
			throw new IOException("Not implemented");
		}

		@Override
		protected void closeConnection() throws IOException {
			throw new IOException("Not implemented");
		}

		@Override
		public boolean isConnected() {
			return false;
		}
	}

	@Override
	public FatekConnection getConnection(FatekConfig fatekConfig)
			throws IOException {
		return new Connection(fatekConfig);
	}

}
