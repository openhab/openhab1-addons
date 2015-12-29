/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower.internal.api.model;

import java.util.List;

/**
 * Structure class for Json deserialization.
 * 
 * @since 1.8.0-SNAPSHOT
 * @author Jordens Christophe
 * 
 */
public class GetDevicesResult {
	/**
	 * List of devices found on the mystrom server.
	 */
	public List<MystromDevice> devices;
}
