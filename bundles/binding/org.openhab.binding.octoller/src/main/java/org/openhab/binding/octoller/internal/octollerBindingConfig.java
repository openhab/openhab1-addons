/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.octoller.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * For openHAB binding for octoller (www.octoller.com) Binding of a specific
 * block in the octoller device DeviceHost is the name or IP of the octoller
 * device GatewayHost is the name or IP of the octoller gateway BlockName is the
 * name of the block in the octoller device to control BlockID is the hash value
 * of the block in the octoller device to control
 * 
 * @author JPlenert
 * @since 1.8.0
 */
public class OctollerBindingConfig implements BindingConfig {
	public String DeviceHost;
	public String GatewayHost;
	public String BlockName;
	public int BlockID;
}
