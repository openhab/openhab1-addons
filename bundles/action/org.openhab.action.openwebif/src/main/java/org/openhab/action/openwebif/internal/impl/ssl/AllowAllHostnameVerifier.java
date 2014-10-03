/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Simple HostnameVerifier used in SSL connection that allows everything.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class AllowAllHostnameVerifier implements HostnameVerifier {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return "ALLOW_ALL";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify(final String arg0, final SSLSession arg1) {
		return true;
	}

}