/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios;

import java.util.List;
import java.util.Map.Entry;

/**
 * openHAB Action Provider interface for MiOS Devices.
 * 
 * Defines how to get properties from a MiOS-specific binding configuration.
 * 
 * @author Mark Clark
 * @since 1.7.0
 */
public interface MiosActionProvider {
	/**
	 * Invoke the named MiOS Scene.
	 * 
	 * Requests the named MiOS Scene, associated with the [Scene] Item be invoked. The invocation itself is
	 * asynchronous, and may fail. The return status of this call is whether we were successful in queuing the Scene
	 * request.
	 * 
	 * @param itemName
	 *            the name of of the openHAB Item to which the Scene invocation should be delivered.
	 * @return true if the Scene request was sent.
	 */
	public boolean invokeMiosScene(String itemName);

	/**
	 * Invoke the named MiOS Action.
	 * 
	 * Requests the named MiOS Action, associated with the [Device] Item be invoked. Callers can pass an Action to be
	 * invoked, in the form: <serviceName>/<actionName> OR; <serviceAlias>/<actionName>
	 * 
	 * The invocation itself is asynchronous, and may fail. The return status of this call is whether we were successful
	 * in queuing the Action request.
	 * 
	 * @param itemName
	 *            the name of of the openHAB Item to which the Action invocation should be delivered.
	 * @param actionName
	 *            the name of the Action to request upon the specified ItemName.
	 * @param params
	 *            a list of String name/value pairs to be used as the Parameter list for the Action call
	 * @return true if the Action request was sent.
	 */
	public boolean invokeMiosAction(String itemName, String actionName, List<Entry<String, Object>> params);
}
