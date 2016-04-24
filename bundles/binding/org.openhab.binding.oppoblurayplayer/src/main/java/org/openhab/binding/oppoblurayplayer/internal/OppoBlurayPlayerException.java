/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

/**
 * Exception for Oppo Bluray player errors.
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerException extends Exception {

	private static final long serialVersionUID = -8048415193494625295L;

	public OppoBlurayPlayerException() {
		super();
	}

	public OppoBlurayPlayerException(String message) {
		super(message);
	}

	public OppoBlurayPlayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public OppoBlurayPlayerException(Throwable cause) {
		super(cause);
	}

}
