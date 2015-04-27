package org.openhab.binding.ulux.internal.audio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.util.Collection;

import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.UluxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioChannel implements Runnable {

	private static final int BUFFER_SIZE = 1024;

	private static final Logger LOG = LoggerFactory.getLogger(AudioChannel.class);

	private final UluxConfiguration configuration;

	private volatile Thread listenerThread;

	private DatagramChannel channel;

	private AudioReceiver audioSource;

	public AudioChannel(UluxConfiguration configuration, Collection<UluxBindingProvider> providers) {
		this.configuration = configuration;
		this.audioSource = new AudioReceiver(configuration, providers);
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

	public DatagramChannel getChannel() {
		return channel;
	}

	/**
	 * This method is run by the thread created in {@link #start()}.
	 */
	@Override
	public void run() {
		final Thread currentThread = Thread.currentThread();

		while (this.listenerThread == currentThread) {
			try {
				doRun();
			} catch (final ClosedByInterruptException e) {
				// normal behavior during shutdown
			} catch (final Exception e) {
				LOG.error("Error while running u::Lux listener thread!", e);
			}
		}
	}

	private void doRun() throws Exception {
		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		final InetSocketAddress source = (InetSocketAddress) this.channel.receive(buffer);

		if (source != null) {
			if (buffer.position() > 0) {
				buffer.flip();
			}

			this.audioSource.receive(buffer);
		}
	}

}
