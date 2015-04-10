/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 ********************************************************************************************************
 * openHAP binding for octoller (www.octoller.com)
 * (c) Joerg Plenert
 ********************************************************************************************************
 */
package org.openhab.binding.octoller.internal;

import org.openhab.core.binding.BindingConfig;

public class octollerBindingConfig implements BindingConfig {
	public String DeviceHost;
	public String GatewayHost;
	public String BlockName;
	public int BlockID;
}
