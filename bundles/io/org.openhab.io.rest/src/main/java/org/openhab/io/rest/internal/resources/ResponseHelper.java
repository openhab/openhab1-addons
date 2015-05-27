/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.resources;

import com.sun.jersey.api.json.JSONWithPadding;

public class ResponseHelper {
	
	public static Object wrapContentIfNeccessary(String callback,
			final String responseType, final Object content) {
		if (responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT)) {
			return new JSONWithPadding(content, callback);
		}
		return content;
	}
	
}
