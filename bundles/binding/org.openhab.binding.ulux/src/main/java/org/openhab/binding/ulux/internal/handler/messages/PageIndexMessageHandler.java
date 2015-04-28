/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler.messages;

import static org.openhab.binding.ulux.UluxBindingConfigType.PAGE_INDEX;
import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.PageIndexMessage;
import org.openhab.core.library.types.DecimalType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public final class PageIndexMessageHandler extends AbstractMessageHandler<PageIndexMessage> {

	@Override
	public void handleMessage(PageIndexMessage message, UluxMessageDatagram response) {
		LOG.debug("Page {}", message.getPageIndex());

		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(PAGE_INDEX).entrySet()) {
			final String itemName = entry.getKey();

			this.eventPublisher.postUpdate(itemName, new DecimalType(message.getPageIndex()));
		}
	}

}
