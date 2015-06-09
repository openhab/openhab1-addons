/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.internal;

import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;

/**
 * @author Michael Heckmann
 * @since 1.8.0
 */

public interface ResolVBUSListener {
	
	/**
	 * Inform all the interested items in this method
	 * @param name of the Item
	 * @param value of the Item
	 */
	public void publishUpdate(String name, String value);
	
	public void processInputStream(ResolVBUSInputStream vbusStream);

}
