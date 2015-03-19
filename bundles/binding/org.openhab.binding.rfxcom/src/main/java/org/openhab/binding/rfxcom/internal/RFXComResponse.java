package org.openhab.binding.rfxcom.internal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openhab.binding.rfxcom.internal.messages.RFXComTransmitterMessage;

/**
 * The Response of a Command is always a {@link RFXComTransmitterMessage}. This implements
 * a very simple {@link Future} for that type.
 * 
 * @author Jürgen Richtsfeld
 * @since 1.7
 */
class RFXComResponse implements Future<RFXComTransmitterMessage> {

	private final Object mutex = new Object(); // don't use 'this' for synchronization to prevent misuse from outside
	
	private RFXComTransmitterMessage result = null;
	private boolean done = false;

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		synchronized(mutex) {
			return done;
		}
	}

	@Override
	public RFXComTransmitterMessage get() throws InterruptedException,
			ExecutionException {
		synchronized(mutex) {
			if(!done) {
				mutex.wait();
			}
			return result;
		}
	}

	@Override
	public RFXComTransmitterMessage get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		synchronized(mutex) {
			if(!done) {
				mutex.wait(unit.toMillis(timeout));
			}
			return result;
		}
	}
	
	public void set(final RFXComTransmitterMessage result) {
		synchronized(mutex) {
			this.result = result;
			this.done = true;
			mutex.notifyAll();
		}
	}
}
