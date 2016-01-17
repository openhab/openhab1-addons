/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal.bridges;

import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MediaControl;


/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class MediaControlForward extends AbstractOpenhabConnectSDKPropertyBridge<Void> {
	
	@Override
	protected String getItemClass() {
		return "MediaControl";
	}
	
	@Override
	protected String getItemProperty() {
		return "forward";
	}

	private MediaControl getControl(final ConnectableDevice device) {
		return device.getCapability(MediaControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(MediaControl.FastForward)) {
			getControl(d).fastForward(createDefaultResponseListener());
		}

	}

}