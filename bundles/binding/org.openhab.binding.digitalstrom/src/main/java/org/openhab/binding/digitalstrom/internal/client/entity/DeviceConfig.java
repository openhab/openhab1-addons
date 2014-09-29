/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public interface DeviceConfig {
	
	public int getClass_();
	
	public int getIndex();
	
	public int getValue();
}
