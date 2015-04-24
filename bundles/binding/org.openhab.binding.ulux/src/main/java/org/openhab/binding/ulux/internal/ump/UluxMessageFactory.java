/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.ump.messages.ActivateMessage;
import org.openhab.binding.ulux.internal.ump.messages.AudioPlayLocalMessage;
import org.openhab.binding.ulux.internal.ump.messages.AudioRecordMessage;
import org.openhab.binding.ulux.internal.ump.messages.AudioStopMessage;
import org.openhab.binding.ulux.internal.ump.messages.AudioVolumeMessage;
import org.openhab.binding.ulux.internal.ump.messages.ControlMessage;
import org.openhab.binding.ulux.internal.ump.messages.DateTimeMessage;
import org.openhab.binding.ulux.internal.ump.messages.EditValueMessage;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage;
import org.openhab.binding.ulux.internal.ump.messages.IdListMessage;
import org.openhab.binding.ulux.internal.ump.messages.LedMessage;
import org.openhab.binding.ulux.internal.ump.messages.LuxMessage;
import org.openhab.binding.ulux.internal.ump.messages.PageCountMessage;
import org.openhab.binding.ulux.internal.ump.messages.PageIndexMessage;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;
import org.openhab.binding.ulux.internal.ump.messages.TextMessage;
import org.openhab.binding.ulux.internal.ump.messages.VideoStartMessage;
import org.openhab.binding.ulux.internal.ump.messages.VideoStopMessage;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;

/**
 * A factory for {@link UluxMessage}s.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxMessageFactory {

	/**
	 * Creates a message with the specified id for the incoming data buffer.
	 * 
	 * @return a {@link UluxMessage} or <code>null</code> if the message id is not supported
	 */
	public UluxMessage createMessage(final UluxMessageId messageId, final short actorId, final ByteBuffer data) {
		final UluxMessage message;

		// TODO
		// 'ID-I2C-Temperature (0x71)'
		// 'ID-I2C-Humidity (0x72)'
		// 'ID-I2C-CO2 (0x73)'
		// 'ID-I2C-IN2 (0x74)'

		switch (messageId) {
		case AudioVolume:
			message = new AudioVolumeMessage(actorId, data);
			break;
		case State:
			message = new StateMessage(actorId, data);
			break;
		case Control:
			message = new ControlMessage(actorId, data);
			break;
		case DateTime:
			message = new DateTimeMessage(actorId, data);
			break;
		case Event:
			message = new EventMessage(actorId, data);
			break;
		case IdList:
			message = new IdListMessage(actorId, data);
			break;
		case LED:
			message = new LedMessage(actorId, data);
			break;
		case Lux:
			message = new LuxMessage(actorId, data);
			break;
		case PageCount:
			message = new PageCountMessage(data);
			break;
		case PageIndex:
			message = new PageIndexMessage(actorId, data);
			break;
		case EditValue:
			message = new EditValueMessage(actorId, data);
			break;
		default:
			message = null;
			break;
		}

		if (message == null) {
			LOG.warn("Message '{}' not implemented!", messageId);
		}

		return message;
	}

	public ActivateMessage createActivateMessage(OnOffType onOff) {
		final boolean active;

		switch (onOff) {
		case ON:
			active = true;
			break;
		case OFF:
			active = false;
			break;
		default:
			LOG.warn("Unknown OnOffType '{}'!", onOff);
			active = false;
		}

		return new ActivateMessage(active);
	}

	public AudioPlayLocalMessage createAudioPlayLocalMessage(DecimalType decimal) {
		final short soundIndex = decimal.shortValue();

		return new AudioPlayLocalMessage(soundIndex);
	}

	public AudioStopMessage createAudioStopMessage(OnOffType onOff) {
		return new AudioStopMessage();
	}

	public AudioRecordMessage createAudioRecordMessage(UluxConfiguration configuration, OnOffType onOff) {
		final InetAddress destination = configuration.getBindAddress();
		final int microphoneSecurityId = configuration.getMicrophoneSecurityId();

		return new AudioRecordMessage(destination, microphoneSecurityId);
	}

	public AudioVolumeMessage createAudioVolumeMessage(DecimalType decimal) {
		final byte volume = decimal.byteValue();

		return new AudioVolumeMessage(volume);
	}

	public EditValueMessage createEditValueMessage(UluxBindingConfig config, DecimalType decimal) {
		final short value = decimal.shortValue();

		return new EditValueMessage(config.getActorId(), value);
	}

	public EditValueMessage createEditValueMessage(UluxBindingConfig config, OnOffType onOff) {
		final short value;

		switch (onOff) {
		case ON:
			value = EditValueMessage.ON;
			break;
		case OFF:
			value = EditValueMessage.OFF;
			break;
		default:
			LOG.warn("Unknown OnOffType '{}'!", onOff);
			value = EditValueMessage.OFF;
		}

		return new EditValueMessage(config.getActorId(), value);
	}

	public LedMessage createLedMessage(UluxBindingConfig config, boolean override) {
		// TODO color
		// TODO blinkMode

		return new LedMessage(config.getActorId(), config.getLed(), override);
	}

	public PageIndexMessage createPageIndexMessage(DecimalType decimal) {
		final byte value = decimal.byteValue();

		return new PageIndexMessage(value);
	}

	public TextMessage createTextMessage(UluxBindingConfig config, StringType text) {
		return new TextMessage(config.getActorId(), text.toString());
	}

	public VideoStartMessage createVideoStartMessage() {
		return new VideoStartMessage();
	}

	public VideoStopMessage createVideoStopMessage() {
		return new VideoStopMessage();
	}

}
