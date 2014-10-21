/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.gfx;

/**
 * Interface which is used by TokenReplacingReader to replace a token in a
 * stream.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface TokenResolver {

	/**
	 * Returns the value to which the token is replaced, null to not replace the
	 * token.
	 */
	public String resolveToken(String tokenName);

}
