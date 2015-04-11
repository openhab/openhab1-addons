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
import static org.openhab.core.library.types.OnOffType.OFF;

import java.net.InetAddress;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * A factory for {@link UluxDatagram}s.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxDatagramFactory {

	private final UluxConfiguration configuration;

	private final UluxMessageFactory messageFactory = new UluxMessageFactory();

	/**
	 * Constructs a datagram factory for the given configuration.
	 */
	public UluxDatagramFactory(final UluxConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(short switchId, InetAddress sourceAddress) {
		return new UluxDatagram(switchId, sourceAddress);
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(UluxBindingConfig config) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = this.configuration.getSwitchAddress(switchId);

		return createDatagram(switchId, switchAddress);
	}

	/**
	 * Creates a datagram containing a message for the given command.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(UluxBindingConfig config, Command type) {
		final UluxDatagram datagram = createDatagram(config);
		final UluxMessage message;

		switch (config.getType()) {
		case AUDIO:
			if (type == OFF) {
				message = messageFactory.createAudioStopMessage((OnOffType) type);
			} else {
				message = null;
			}
			break;
		case AUDIO_PLAY_LOCAL:
			message = messageFactory.createAudioPlayLocalMessage((DecimalType) type);
			break;
		case AUDIO_RECORD:
			message = messageFactory.createAudioRecordMessage(configuration, (OnOffType) type);
			break;
		case AUDIO_VOLUME:
			message = messageFactory.createAudioVolumeMessage((DecimalType) type);
			break;
		case DISPLAY:
			message = messageFactory.createActivateMessage((OnOffType) type);
			break;
		case EDIT_VALUE:
			if (type instanceof DecimalType) {
				message = messageFactory.createEditValueMessage(config, (DecimalType) type);
			} else if (type instanceof OnOffType) {
				message = messageFactory.createEditValueMessage(config, (OnOffType) type);
			} else if (type instanceof StringType) {
				message = messageFactory.createTextMessage(config, (StringType) type);
			} else {
				// TODO IncreaseDecreaseType
				// TODO OpenClosedType
				// TODO StopMoveType
				// TODO UpDownType
				LOG.debug("Outgoing message '{}' for item '{}' not yet supported!", type, config);
				message = null;
			}
			break;
		case PAGE_INDEX:
			message = messageFactory.createPageIndexMessage((DecimalType) type);
			break;
		case AMBIENT_LIGHT:
		case KEY:
		case LUX:
		case PROXIMITY:
			message = null; // ignore
			break;
		default:
			LOG.debug("Outgoing message '{}' for item '{}' not yet supported!", type, config);
			message = null;
			break;
		}

		if (message != null) {
			datagram.addMessage(message);
		}

		return datagram;
	}

	/**
	 * Creates a datagram containing a message for the given state update.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(UluxBindingConfig config, State type) {
		final UluxDatagram datagram = createDatagram(config);
		final UluxMessage message;

		switch (config.getType()) {
		case AUDIO:
			if (type == OFF) {
				message = messageFactory.createAudioStopMessage((OnOffType) type);
			} else {
				message = null;
			}
			break;
		case AUDIO_VOLUME:
			message = messageFactory.createAudioVolumeMessage((DecimalType) type);
			break;
		case AUDIO_PLAY_LOCAL:
			message = messageFactory.createAudioPlayLocalMessage((DecimalType) type);
			break;
		case AUDIO_RECORD:
			message = messageFactory.createAudioRecordMessage(configuration, (OnOffType) type);
			break;
		case EDIT_VALUE:
			if (type instanceof DecimalType) {
				message = messageFactory.createEditValueMessage(config, (DecimalType) type);
			} else if (type instanceof OnOffType) {
				message = messageFactory.createEditValueMessage(config, (OnOffType) type);
			} else if (type instanceof StringType) {
				message = messageFactory.createTextMessage(config, (StringType) type);
			} else {
				// TODO OpenClosedType
				// TODO UpDownType
				LOG.debug("Outgoing message '{}' for item '{}' not yet supported!", type, config);
				message = null;
			}
			break;
		case PAGE_INDEX:
			message = messageFactory.createPageIndexMessage((DecimalType) type);
			break;
		case AMBIENT_LIGHT:
		case KEY:
		case LUX:
		case PROXIMITY:
			message = null; // ignore
			break;
		default:
			LOG.debug("Outgoing message '{}' for item '{}' not yet supported!", type, config);
			message = null;
			break;
		}

		if (message != null) {
			datagram.addMessage(message);
		}

		return datagram;
	}

}
