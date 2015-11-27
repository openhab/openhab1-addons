package org.openhab.binding.connectsdk.internal.bridges;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.core.ChannelInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TVControl.ChannelListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class TVControlChannel extends AbstractOpenhabConnectSDKPropertyBridge<ChannelListener> {
	private static final Logger logger = LoggerFactory.getLogger(TVControlChannel.class);

	@Override
	protected String getItemClass() {
		return "TVControl";
	}
	
	@Override
	protected String getItemProperty() {
		return "channel"; 
	}

	
	private TVControl getControl(final ConnectableDevice device) {
		return device.getCapability(TVControl.class);
	}
	
	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(TVControl.Channel_List, TVControl.Channel_Set)) {
		
				final String value = command.toString();
				final TVControl control = getControl(d);
				control.getChannelList(new TVControl.ChannelListListener() {
					@Override
					public void onError(ServiceCommandError error) {
						logger.error("error requesting channel list: {}.", error.getMessage());
					}

					@Override
					public void onSuccess(List<ChannelInfo> channels) {
						if (logger.isDebugEnabled()) {
							for (ChannelInfo c : channels) {
								logger.debug("Channel {} - {}", c.getNumber(), c.getName());
							}
						}
						try {
							ChannelInfo channelInfo = Iterables.find(channels, new Predicate<ChannelInfo>() {
								public boolean apply(ChannelInfo c) {
									return c.getNumber().equals(value);
								};
							});
							control.setChannel(channelInfo, createDefaultResponseListener());
						} catch (NoSuchElementException ex) {
							logger.warn("TV does not have a channel: {}.", value);
						}

					}
				});

			}
		

	}
	
	@Override
	protected ServiceSubscription<ChannelListener> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		if (device.hasCapability(TVControl.Channel_Subscribe)) {
			return getControl(device).subscribeCurrentChannel(new ChannelListener() {

						@Override
						public void onError(ServiceCommandError error) {
							logger.error("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
						}

						@Override
						public void onSuccess(ChannelInfo channelInfo) {
							for (ConnectSDKBindingProvider provider : providers) {
								for (String itemName : provider.getItemNames()) {
									try {
										if (matchClassAndProperty(provider.getClassForItem(itemName),provider.getPropertyForItem(itemName))
												&& device.getIpAddress().equals(
														InetAddress.getByName(provider.getDeviceForItem(itemName))
																.getHostAddress())) {
											if (eventPublisher != null) {
												eventPublisher.postUpdate(itemName,
														new StringType(channelInfo.getNumber()));
											}
										}
									} catch (UnknownHostException e) {
										logger.error("Failed to resolve {} to IP address. Skipping update on item {}.",
												device, itemName);
									}
								}
							}
						}
					});
		} else return null;
	}

	
}