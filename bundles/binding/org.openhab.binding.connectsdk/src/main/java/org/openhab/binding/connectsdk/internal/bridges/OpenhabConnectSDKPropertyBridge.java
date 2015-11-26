package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

import com.connectsdk.device.ConnectableDevice;

public interface OpenhabConnectSDKPropertyBridge {

	public abstract void onReceiveCommand(ConnectableDevice d, String clazz, String property, Command command);

	public abstract void updateSubscription(ConnectableDevice device, Collection<ConnectSDKBindingProvider> providers,
			EventPublisher eventPublisher);

	public abstract void removeAnySubscription(ConnectableDevice device);

}