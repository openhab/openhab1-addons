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

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.core.ProgramInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TVControl.ProgramInfoListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;


/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class TVControlProgram extends AbstractOpenhabConnectSDKPropertyBridge<ProgramInfoListener> {
	private static final Logger logger = LoggerFactory.getLogger(TVControlProgram.class);

	@Override
	protected String getItemClass() {
		return "TVControl";
	}

	@Override
	protected String getItemProperty() {
		return "program";
	}

	private TVControl getControl(final ConnectableDevice device) {
		return device.getCapability(TVControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		// nothing to do, this is read only.
	}

	@Override
	protected ServiceSubscription<ProgramInfoListener> getSubscription(final ConnectableDevice device,
			final Collection<String> itemNames, final EventPublisher eventPublisher) {
		if (device.hasCapability(TVControl.Program_Subscribe)) {
			return getControl(device).subscribeProgramInfo(new ProgramInfoListener() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.debug("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
				}

				@Override
				public void onSuccess(ProgramInfo programInfo) {
					if (eventPublisher != null) {
						for (String itemName : itemNames) {
							eventPublisher.postUpdate(itemName, new StringType(programInfo.getName()));
						}
					}
				}
			});
		} else
			return null;
	}

}