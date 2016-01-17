/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal.bridges;

import java.util.List;
import java.util.NoSuchElementException;

import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.core.ExternalInputInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.ExternalInputControl;
import com.connectsdk.service.capability.ExternalInputControl.ExternalInputListListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ExternalInputControlInput extends AbstractOpenhabConnectSDKPropertyBridge<ExternalInputListListener> {
	private static final Logger logger = LoggerFactory.getLogger(ExternalInputControlInput.class);

	@Override
	protected String getItemClass() {
		return "ExternalInputControl";
	}

	@Override
	protected String getItemProperty() {
		return "input";
	}

	private ExternalInputControl getControl(final ConnectableDevice device) {
		return device.getCapability(ExternalInputControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property)
				&& d.hasCapabilities(ExternalInputControl.List, ExternalInputControl.Set)) {
			final String value = command.toString();
			final ExternalInputControl control = getControl(d);
			control.getExternalInputList(new ExternalInputListListener() {
				@Override
				public void onError(ServiceCommandError error) {
					logger.error("error requesting external input list: {}.", error.getMessage());
				}

				@Override
				public void onSuccess(List<ExternalInputInfo> infos) {
					if (logger.isDebugEnabled()) {
						for (ExternalInputInfo c : infos) {
							logger.debug("Input {} - {}", c.getId(), c.getName());
						}
					}
					try {
						ExternalInputInfo channelInfo = Iterables.find(infos, new Predicate<ExternalInputInfo>() {
							public boolean apply(ExternalInputInfo c) {
								return c.getId().equals(value);
							};
						});
						control.setExternalInput(channelInfo, createDefaultResponseListener());
					} catch (NoSuchElementException ex) {
						logger.warn("Device does not have input: {}.", value);
					}

				}
			});

		}

	}

}