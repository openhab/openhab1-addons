/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal.core;

import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerException;

/**
 * OppoBlurayPlayerNoMatchingItemException
 * Thrown when the command received from the player has no item configured in *.items. 
 * 
 * @author  (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */

public class OppoBlurayPlayerNoMatchingItemException extends OppoBlurayPlayerException {
	private static final long serialVersionUID = -2233461058371646855L;

	public OppoBlurayPlayerNoMatchingItemException(String message) {
		super(message);
	}

}
