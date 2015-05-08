package org.openmuc.jsml.tl;

/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("serial")
public final class XTrustProvider extends java.security.Provider {
	private final static String NAME = "XTrustJSSE";
	private final static String INFO = "XTrust JSSE Provider (implements trust factory with truststore validation disabled)";
	private final static double VERSION = 1.0D;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XTrustProvider() {
		super(NAME, VERSION, INFO);

		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				put("TrustManagerFactory." + TrustManagerFactoryImpl.getAlgorithm(),
						TrustManagerFactoryImpl.class.getName());
				return null;
			}
		});
	}

	public static void install() {
		if (Security.getProvider(NAME) == null) {
			Security.insertProviderAt(new XTrustProvider(), 2);
			Security.setProperty("ssl.TrustManagerFactory.algorithm", TrustManagerFactoryImpl.getAlgorithm());
		}
	}

	public final static class TrustManagerFactoryImpl extends TrustManagerFactorySpi {
		public TrustManagerFactoryImpl() {
		}

		public static String getAlgorithm() {
			return "XTrust509";
		}

		@Override
		protected void engineInit(KeyStore keystore) throws KeyStoreException {
		}

		@Override
		protected void engineInit(ManagerFactoryParameters mgrparams) throws InvalidAlgorithmParameterException {
			throw new InvalidAlgorithmParameterException(XTrustProvider.NAME + " does not use ManagerFactoryParameters");
		}

		@Override
		protected TrustManager[] engineGetTrustManagers() {
			return new TrustManager[] { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
		}
	}
}
