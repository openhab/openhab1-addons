/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.i18n;

import org.eclipse.osgi.util.NLS;

/**
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "org.openhab.binding.oppoblurayplayer.internal.messages"; //$NON-NLS-1$
	
	public static String OppoBlurayPlayerBinding_NO_ERROR;
	public static String OppoBlurayPlayerBinding_ERROR1;
	public static String OppoBlurayPlayerBinding_UNKNOWN_ERROR;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
