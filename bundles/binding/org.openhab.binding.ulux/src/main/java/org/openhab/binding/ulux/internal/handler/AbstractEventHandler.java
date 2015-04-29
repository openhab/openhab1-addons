package org.openhab.binding.ulux.internal.handler;

import java.nio.channels.DatagramChannel;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.ump.UluxAudioDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagramFactory;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessageFactory;
import org.openhab.binding.ulux.internal.ump.UluxVideoDatagram;
import org.openhab.core.types.Type;

public abstract class AbstractEventHandler<T extends Type> {

	protected final UluxConfiguration configuration;

	protected final UluxMessageFactory messageFactory;

	protected final UluxDatagramFactory datagramFactory;

	private DatagramChannel audioChannel;

	private DatagramChannel messageChannel;

	private ScheduledExecutorService executorService;

	public AbstractEventHandler(UluxConfiguration configuration, UluxMessageFactory messageFactory,
			UluxDatagramFactory datagramFactory) {
		this.configuration = configuration;
		this.messageFactory = messageFactory;
		this.datagramFactory = datagramFactory;
	}

	public void activate(DatagramChannel audioChannel, DatagramChannel messageChannel,
			ScheduledExecutorService executorService) {
		this.audioChannel = audioChannel;
		this.messageChannel = messageChannel;
		this.executorService = executorService;
	}

	public void deactivate() {
		this.audioChannel = null;
		this.messageChannel = null;
		this.executorService = null;
	}

	public final Runnable createHandlerTask(final UluxBindingConfig binding, final T event) {
		return new Runnable() {

			@Override
			public void run() {
				final Queue<UluxDatagram> datagramQueue = handleEvent(binding, event);

				if (!datagramQueue.isEmpty()) {
					processDatagramQueue(datagramQueue);
				}
			}
		};
	}

	protected abstract Queue<UluxDatagram> handleEvent(UluxBindingConfig binding, T event);

	protected final void processDatagramQueue(Queue<UluxDatagram> queue) {
		ScheduledFuture<?> task = null;

		while (!queue.isEmpty()) {
			if (task == null) {
				final UluxDatagram datagram = queue.peek();

				if (datagram instanceof UluxMessageDatagram && !(datagram instanceof UluxVideoDatagram)) {
					// immediately send a single datagram
					final UluxDatagram messageDatagram = queue.poll();
					messageDatagram.send(this.messageChannel);
					continue;
				}

				if (datagram instanceof UluxAudioDatagram) {
					// send a datagram using the audio channel every 20ms (= 1000ms / AUDIO_FRAME_RATE)
					task = this.executorService.scheduleAtFixedRate(new SendTask(queue, this.audioChannel, this), 0,
							20, TimeUnit.MILLISECONDS);
					continue;
				}

				if (datagram instanceof UluxVideoDatagram) {
					// send a datagram using the message channel every 5ms (TODO 5ms is just a guess)
					task = this.executorService.scheduleAtFixedRate(new SendTask(queue, this.messageChannel, this), 0,
							5, TimeUnit.MILLISECONDS);
					continue;
				}
			} else {
				try {
					// wait until queue is empty then cancel task
					wait();
				} catch (InterruptedException e) {
					continue;
				}
			}
		}

		if (task != null) {
			task.cancel(false);
		}
	}

	private static class SendTask implements Runnable {

		private final Queue<UluxDatagram> queue;
		private final DatagramChannel channel;
		private final Object parentTask;

		public SendTask(Queue<UluxDatagram> queue, DatagramChannel channel, Object parentTask) {
			this.queue = queue;
			this.channel = channel;
			this.parentTask = parentTask;
		}

		@Override
		public void run() {
			final UluxDatagram datagram = this.queue.poll();

			if (datagram != null) {
				datagram.send(this.channel);
			}

			if (this.queue.isEmpty()) {
				parentTask.notify();
			}
		}
	}

}
