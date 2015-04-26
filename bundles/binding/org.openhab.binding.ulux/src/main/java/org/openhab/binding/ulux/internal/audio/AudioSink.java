package org.openhab.binding.ulux.internal.audio;

import static javax.sound.sampled.AudioSystem.NOT_SPECIFIED;
import static javax.sound.sampled.AudioSystem.write;
import static org.openhab.binding.ulux.UluxBindingConfigType.AUDIO_RECORD;
import static org.openhab.binding.ulux.internal.audio.AudioSource.AUDIO_FORMAT;
import static org.openhab.binding.ulux.internal.audio.AudioSource.AUDIO_FRAME_SIZE;
import static org.openhab.binding.ulux.internal.audio.AudioSource.AUDIO_TYPE;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.channels.ClosedByInterruptException;
import java.util.Collection;

import javax.sound.sampled.AudioInputStream;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Brenk
 */
public class AudioSink implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(AudioSink.class);

	private static final int PIPE_SIZE = AUDIO_FRAME_SIZE * 2;

	private AudioInputStream audioInput;

	private final File outputFile;

	private volatile Thread thread;

	public AudioSink(Collection<UluxBindingProvider> providers, short switchId) throws IOException {
		this.outputFile = getOutputFile(providers, switchId);
	}

	public PipedOutputStream start() throws IOException {
		final PipedOutputStream src = new PipedOutputStream();
		final PipedInputStream snk = new PipedInputStream(src, PIPE_SIZE);

		this.audioInput = new AudioInputStream(snk, AUDIO_FORMAT, NOT_SPECIFIED);

		this.thread = new Thread(this, "u::Lux audio writer");
		this.thread.start();

		return src;
	}

	public void stop() {
		final Thread thread = this.thread;
		this.thread = null;
		thread.interrupt();

		try {
			this.audioInput.close();
			this.audioInput = null;
		} catch (IOException e) {
			LOG.warn("Error closing audio stream!", e);
			// swallow exception
		}
	}

	/**
	 * This method is run by the thread created in {@link #start()}.
	 */
	@Override
	public void run() {
		final Thread currentThread = Thread.currentThread();

		while (this.thread == currentThread) {
			try {
				write(this.audioInput, AUDIO_TYPE, this.outputFile);
			} catch (final ClosedByInterruptException e) {
				// normal behavior during shutdown
			} catch (final Exception e) {
				LOG.error("Error while running u::Lux audio sink thread!", e);
			}
		}
	}

	private File getOutputFile(Collection<UluxBindingProvider> providers, short switchId) throws IOException {
		File outputFile = null;

		for (final UluxBindingProvider provider : providers) {
			for (final String itemName : provider.getItemNames()) {
				final UluxBindingConfig binding = provider.getBinding(itemName);

				if (binding.getType() == AUDIO_RECORD) {
					outputFile = binding.getFile();
				}
			}
		}

		if (outputFile == null) {
			outputFile = File.createTempFile("openhab-", ".wav");
			LOG.warn("No output file configured for switch {}! Writing to {}.", switchId, outputFile);
		} else {
			LOG.debug("Writing audio stream from switch {} to {}.", switchId, outputFile);
		}

		return outputFile;
	}
}
