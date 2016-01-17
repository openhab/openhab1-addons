/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;


/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public interface OpenhabConnectSDKPropertyBridge {

	public abstract void onReceiveCommand(ConnectableDevice d, String clazz, String property, Command command);

	public abstract void refreshSubscription(ConnectableDevice device, Collection<ConnectSDKBindingProvider> providers,
			EventPublisher eventPublisher);

	public abstract void removeAnySubscription(ConnectableDevice device);

	public abstract void onDeviceRemoved(final ConnectableDevice device, final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher);

	public abstract void onDeviceReady(final ConnectableDevice device, final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher);

}