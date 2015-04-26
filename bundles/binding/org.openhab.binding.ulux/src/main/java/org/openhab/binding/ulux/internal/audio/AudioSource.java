package org.openhab.binding.ulux.internal.audio;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.UluxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receiver for the audio stream sent by an u::Lux switch.
 * 
 * @author Andreas Brenk
 */
public class AudioSource implements Runnable {

	public static final int AUDIO_CHANNELS = 1;
	public static final int AUDIO_SAMPLE_RATE = 22050;
	public static final int AUDIO_SAMPLE_SIZE = 16;
	public static final int AUDIO_FRAME_SIZE = 882;
	public static final int AUDIO_FRAME_RATE = 50;
	public static final Encoding AUDIO_ENCODING = Encoding.PCM_SIGNED;
	public static final Type AUDIO_TYPE = Type.WAVE;

	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(AUDIO_ENCODING, AUDIO_SAMPLE_RATE,
			AUDIO_SAMPLE_SIZE, AUDIO_CHANNELS, AUDIO_FRAME_SIZE, AUDIO_FRAME_RATE, false);

	private static final Logger LOG = LoggerFactory.getLogger(AudioSource.class);

	private static final int BUFFER_SIZE = 1024;

	private final UluxConfiguration configuration;

	private final Collection<UluxBindingProvider> providers;

	private DatagramChannel channel;

	private volatile Thread listenerThread;

	private final Map<Short, PipedOutputStream> pipeMap;

	public AudioSource(UluxConfiguration configuration, Collection<UluxBindingProvider> providers) {
		this.configuration = configuration;
		this.providers = providers;
		this.pipeMap = new HashMap<Short, PipedOutputStream>();
	}

	public void start() {
		try {
			this.channel = DatagramChannel.open(StandardProtocolFamily.INET);
			this.channel.socket().bind(configuration.getAudioSocketAddress());
		} catch (final IOException e) {
			throw new UluxException("Could not open UDP port for listening!", e);
		}

		this.listenerThread = new Thread(this, "u::Lux audio listener");
		this.listenerThread.start();
	}

	public void stop() {
		final Thread thread = this.listenerThread;
		this.listenerThread = null;
		thread.interrupt();

		try {
			this.channel.close();
			this.channel = null;
		} catch (final IOException e) {
			LOG.warn("Error closing channel!", e);
			// swallow exception
		}
	}

	/**
	 * This method is run by the thread created in {@link #start()}.
	 */
	@Override
	public void run() {
		final Thread currentThread = Thread.currentThread();

		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		while (this.listenerThread == currentThread) {
			try {
				doRun(buffer);
			} catch (final ClosedByInterruptException e) {
				// normal behavior during shutdown
			} catch (final Exception e) {
				LOG.error("Error while running u::Lux audio source thread!", e);
			}
		}
	}

	private void doRun(final ByteBuffer input) throws Exception {
		final InetSocketAddress source = (InetSocketAddress) this.channel.receive(input);

		if (source != null) {
			if (input.position() > 0) {
				input.flip();
			}

			final InetAddress switchAddress = processHeader(input);
			final short switchId = this.configuration.getSwitchId(switchAddress);

			final PipedOutputStream pipe = preparePipe(switchId);

			for (int i = 0; i < AUDIO_FRAME_SIZE; i++) {
				pipe.write(input.get());
			}
		}

		input.clear();
	}

	/**
	 * @return the switch address
	 */
	private InetAddress processHeader(ByteBuffer input) throws Exception {
		// EtherCAT frame header
		// .... .011 1000 0110 Length: 0x0386 = 902
		// .... 0... .... .... Reserved: Valid
		// 0100 .... .... .... Type: NV
		input.getShort();

		final byte[] addr = new byte[4];
		addr[0] = input.get();
		addr[1] = input.get();
		addr[2] = input.get();
		addr[3] = input.get();
		InetAddress switchAddress = InetAddress.getByAddress(addr);

		// TwinCAT Network Vars
		input.get(); // .1
		input.get(); // .1

		input.getShort(); // Count, always 0x0001
		input.getShort(); // Cycle index, increases every packet, seems to start with 9
		input.getShort(); // ???, always 0x0000

		input.getShort(); // Variable Id, always 0x0000
		input.getShort(); // Hash, always 0x0000
		short length = input.getShort(); // Length, i.e. 0x0372 = 882
		input.getShort(); // Quality, always 0x0000

		if (length != (short) AUDIO_FRAME_SIZE) {
			LOG.debug("Unexpected audio frame size: ", length);
		}

		return switchAddress;
	}

	private PipedOutputStream preparePipe(short switchId) throws IOException {
		if (!this.pipeMap.containsKey(switchId)) {
			final AudioSink audioSink = new AudioSink(this.providers, switchId);
			final PipedOutputStream stream = audioSink.start();

			this.pipeMap.put(switchId, stream);
		}

		return this.pipeMap.get(switchId);
	}

}
