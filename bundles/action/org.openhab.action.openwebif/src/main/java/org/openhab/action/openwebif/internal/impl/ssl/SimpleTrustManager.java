/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Simple TrustManager used in SSL connection that trusts everything.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SimpleTrustManager implements X509TrustManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

}
