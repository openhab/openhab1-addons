/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openpaths.internal;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Simple extension class for the Scribe OAuth library
 * 
 * Since we don't do authorisation requesting this doesn't need
 * to do anything - other than extend the default implementation
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class OpenPathsApi extends DefaultApi10a {
	@Override
	public String getRequestTokenEndpoint() {
		return null;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return null;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return null;
	}
}

