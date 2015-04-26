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
import static org.openhab.core.library.types.OnOffType.ON;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingConfigType;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.ump.messages.VideoStreamMessage;
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
	 * Creates a list of datagrams for the given command.
	 * 
	 * @return never {@code null}
	 */
	public List<UluxDatagram> createDatagram(UluxBindingConfig config, Command type) {
		final List<UluxDatagram> datagramList = new LinkedList<UluxDatagram>();

		final UluxMessage message;

		switch (config.getType()) {
		case AUDIO:
			if (type == OFF) {
				message = messageFactory.createAudioStopMessage();
			} else {
				message = null;
			}
			break;
		case AUDIO_PLAY_LOCAL:
			message = messageFactory.createAudioPlayLocalMessage((DecimalType) type);
			break;
		case AUDIO_RECORD:
			if (type == ON) {
				message = messageFactory.createAudioRecordMessage(configuration);
			} else {
				message = messageFactory.createAudioStopMessage();
			}
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
		case IMAGE:
			message = messageFactory.createVideoStartMessage();
			break;
		case LED:
			message = messageFactory.createLedMessage(config, (DecimalType) type);
			break;
		case PAGE_INDEX:
			message = messageFactory.createPageIndexMessage((DecimalType) type);
			break;
		case VIDEO:
			if (type == ON) {
				message = messageFactory.createVideoStartMessage();
			} else {
				message = messageFactory.createVideoStopMessage();
			}
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
			final UluxDatagram datagram = createDatagram(config);
			datagram.addMessage(message);

			datagramList.add(datagram);
		}

		if (config.getType() == UluxBindingConfigType.IMAGE) {
			addVideoDatagrams(datagramList, config, ((StringType) type).toString());
		}

		return datagramList;
	}

	/**
	 * Creates a list of datagrams for the given state update.
	 * 
	 * @return never {@code null}
	 */
	public List<UluxDatagram> createDatagram(UluxBindingConfig config, State type) {
		final List<UluxDatagram> datagramList = new LinkedList<UluxDatagram>();
		final UluxMessage message;

		switch (config.getType()) {
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
		default:
			message = null; // ignore
			break;
		}

		if (message != null) {
			final UluxDatagram datagram = createDatagram(config);
			datagram.addMessage(message);

			datagramList.add(datagram);
		}

		return datagramList;
	}

	private void addVideoDatagrams(List<UluxDatagram> datagramList, UluxBindingConfig config, String imageName) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = this.configuration.getSwitchAddress(switchId);

		if (imageName.length() == 0) {
			return;
		}

		final BufferedImage originalImage;
		try {
			originalImage = ImageIO.read(new URL(imageName));
		} catch (IOException e) {
			LOG.error("Cannot read image!", e);
			return;
		}

		// TODO query switch
		int resizedWidth = 176;
		int resizedHeight = 184;

		BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(originalImage, 0, 0, 176, 184, null);
		g.dispose();

		final int[] videoData = resizedImage.getRGB(0, 0, resizedWidth, resizedHeight, null, 0, resizedWidth);

		final int maxPixelPerMessage = 704;

		int width = 176;
		int height = 184;

		final int linesPerMessage = maxPixelPerMessage / width;
		final int pixelPerMessage = linesPerMessage * width;
		final int numberOfMessages = height / linesPerMessage + (height % linesPerMessage == 0 ? 0 : 1);

		for (short messageNumber = 0; messageNumber < numberOfMessages; messageNumber++) {
			final int[] frameData = new int[pixelPerMessage];
			final int videoDataPosition = messageNumber * pixelPerMessage;
			final int startLine = (short) (messageNumber * linesPerMessage);
			boolean lastMessage = (messageNumber == numberOfMessages - 1);

			final int lineCount;
			if (lastMessage && (height % linesPerMessage != 0)) {
				lineCount = (height - startLine) % linesPerMessage;
			} else {
				lineCount = linesPerMessage;
			}

			System.arraycopy(videoData, videoDataPosition, frameData, 0, frameData.length);

			final VideoStreamMessage message = this.messageFactory.createVideoStreamMessage((short) startLine,
					(short) lineCount, frameData);

			datagramList.add(new UluxVideoDatagram(switchId, switchAddress, message));
		}
	}
}
