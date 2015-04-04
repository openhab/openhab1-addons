/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.messages.PageIndexMessage;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class PageIndexMessageHandler extends AbstractMessageHandler<PageIndexMessage> {

	@Override
	public void handleMessage(PageIndexMessage message, UluxDatagram response) {
		LOG.debug("Page {}", message.getPageIndex());

		// TODO pageIndex
		// eventPublisher.postUpdate("PageIndex", new DecimalType(message.getPageIndex()));
	}

}
