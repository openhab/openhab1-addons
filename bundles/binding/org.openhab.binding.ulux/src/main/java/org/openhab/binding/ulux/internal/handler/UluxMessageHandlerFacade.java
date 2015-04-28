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
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.AudioVolume;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.Control;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.EditValue;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.Event;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.IdList;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.LED;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.Lux;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.PageCount;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.PageIndex;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.State;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.VideoState;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.handler.messages.AbstractMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.AudioVolumeMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.ControlMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.EditValueMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.EventMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.IdListMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.LedMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.LuxMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.PageCountMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.PageIndexMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.StateMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.UluxMessageHandler;
import org.openhab.binding.ulux.internal.handler.messages.VideoStateMessageHandler;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.ItemRegistry;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxMessageHandlerFacade {

	private final Map<UluxMessageId, AbstractMessageHandler<?>> messageHandlers;

	public UluxMessageHandlerFacade(Collection<UluxBindingProvider> providers) {
		messageHandlers = new EnumMap<UluxMessageId, AbstractMessageHandler<?>>(UluxMessageId.class);
		messageHandlers.put(AudioVolume, new AudioVolumeMessageHandler());
		messageHandlers.put(Control, new ControlMessageHandler());
		messageHandlers.put(EditValue, new EditValueMessageHandler());
		messageHandlers.put(Event, new EventMessageHandler());
		messageHandlers.put(IdList, new IdListMessageHandler());
		messageHandlers.put(LED, new LedMessageHandler());
		messageHandlers.put(Lux, new LuxMessageHandler());
		messageHandlers.put(PageCount, new PageCountMessageHandler());
		messageHandlers.put(PageIndex, new PageIndexMessageHandler());
		messageHandlers.put(State, new StateMessageHandler());
		messageHandlers.put(VideoState, new VideoStateMessageHandler());

		for (AbstractMessageHandler<?> messageHandler : messageHandlers.values()) {
			messageHandler.setProviders(providers);
		}
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		for (AbstractMessageHandler<?> messageHandler : messageHandlers.values()) {
			messageHandler.setEventPublisher(eventPublisher);
		}
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		for (AbstractMessageHandler<?> messageHandler : messageHandlers.values()) {
			messageHandler.setItemRegistry(itemRegistry);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends UluxMessage> void handleMessage(T message, UluxMessageDatagram response) {
		final UluxMessageHandler<T> messageHandler = (UluxMessageHandler<T>) messageHandlers
				.get(message.getMessageId());

		if (messageHandler != null) {
			messageHandler.handleMessage(message, response);
		} else {
			LOG.warn("No handler for message: {}", message);
		}
	}

}
