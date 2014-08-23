/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

/**
 * Functional interface to handle the processing of a neohub response.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public interface ResponseHandler<T> {
	T onResponse(String response);
}
