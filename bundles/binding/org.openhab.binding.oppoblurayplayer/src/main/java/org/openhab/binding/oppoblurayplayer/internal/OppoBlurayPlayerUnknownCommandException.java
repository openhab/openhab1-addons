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
 * OppoBlurayPlayerUnknownCommandException
 * Thrown when the command received from the player is unknown. 
 * 
 * @author  (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerUnknownCommandException extends OppoBlurayPlayerException {

	private static final long serialVersionUID = 1748479003112491205L;

	public OppoBlurayPlayerUnknownCommandException(String string) {
		super(string);
	}

}
