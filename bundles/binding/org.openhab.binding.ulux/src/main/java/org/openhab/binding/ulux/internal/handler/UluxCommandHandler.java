package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.internal.audio.AudioReceiver.AUDIO_FRAME_SIZE;
import static org.openhab.core.library.types.OnOffType.OFF;
import static org.openhab.core.library.types.OnOffType.ON;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.UluxException;
import org.openhab.binding.ulux.internal.ump.UluxAudioDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagramFactory;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessageFactory;
import org.openhab.binding.ulux.internal.ump.UluxVideoDatagram;
import org.openhab.binding.ulux.internal.ump.messages.VideoStreamMessage;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UluxCommandHandler extends AbstractEventHandler<Command> {

	private static final Logger LOG = LoggerFactory.getLogger(UluxCommandHandler.class);

	public UluxCommandHandler(UluxConfiguration configuration, UluxMessageFactory messageFactory,
			UluxDatagramFactory datagramFactory) {
		super(configuration, messageFactory, datagramFactory);
	}

	/**
	 * Creates a list of datagrams for the given command.
	 * 
	 * @return never {@code null}
	 */
	@Override
	public Queue<UluxDatagram> handleEvent(UluxBindingConfig config, Command type) {
		final Queue<UluxDatagram> datagramList = new ConcurrentLinkedQueue<UluxDatagram>();

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
		case AUDIO_PLAY_REMOTE:
			message = messageFactory.createAudioPlayRemoteMessage();
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
			final UluxMessageDatagram datagram = datagramFactory.createMessageDatagram(config);
			datagram.addMessage(message);

			datagramList.add(datagram);
		}

		switch (config.getType()) {
		case AUDIO_PLAY_REMOTE:
			addAudioDatagrams(datagramList, config, ((StringType) type).toString());
			break;
		case IMAGE:
			addVideoDatagrams(datagramList, config, ((StringType) type).toString());
			break;
		default:
			// nothing to do
			break;
		}

		return datagramList;
	}

	private void addAudioDatagrams(Queue<UluxDatagram> datagramList, UluxBindingConfig config, String audioUrl) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = configuration.getSwitchAddress(switchId);

		try {
			final AudioInputStream audio = AudioSystem.getAudioInputStream(new URL(audioUrl));

			// TODO conversion
			// AudioSystem.getAudioInputStream(AUDIO_FORMAT, audio);

			int bytesRead = 0;
			for (short index = 0; bytesRead > -1; index++) {
				byte[] audioFrame = new byte[AUDIO_FRAME_SIZE];
				bytesRead = audio.read(audioFrame);

				datagramList.add(new UluxAudioDatagram(switchId, switchAddress, index, audioFrame));
			}
		} catch (Exception e) {
			LOG.error("Cannot read audio!", e);
			throw new UluxException("Cannot read audio!", e);
		}
	}

	private void addVideoDatagrams(Queue<UluxDatagram> datagramList, UluxBindingConfig config, String imageName) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = configuration.getSwitchAddress(switchId);

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

			final VideoStreamMessage message = messageFactory.createVideoStreamMessage((short) startLine,
					(short) lineCount, frameData);

			datagramList.add(new UluxVideoDatagram(switchId, switchAddress, message));
		}
	}

}
