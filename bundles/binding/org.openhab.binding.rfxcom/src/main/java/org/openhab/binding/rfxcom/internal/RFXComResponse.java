/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openhab.binding.rfxcom.internal.messages.RFXComTransmitterMessage;

/**
 * The Response of a Command is always a {@link RFXComTransmitterMessage}. This implements
 * a very simple {@link Future} for that type.
 * 
 * @author Jürgen Richtsfeld
 * @since 1.7
 */
class RFXComResponse implements Future<RFXComTransmitterMessage> {

	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	
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
		lock.lock();
		try {
			return done;
		} finally {
			lock.unlock();
		}
		
	}

	@Override
	public RFXComTransmitterMessage get() throws InterruptedException,
			ExecutionException {
		lock.lock();
		try {
			if(!done) {
				condition.await();
			}
			return result;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RFXComTransmitterMessage get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		lock.lock();
		try {
			if(!done) {
				final boolean timedOut = !condition.await(timeout, unit);
				if(timedOut) {
					throw new TimeoutException("waiting timed out");
				}
			}
			return result;
		} finally {
			lock.unlock();
		}
	}
	
	public void set(final RFXComTransmitterMessage result) {
		lock.lock();
		try {
			this.result = result;
			this.done = true;
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
}
