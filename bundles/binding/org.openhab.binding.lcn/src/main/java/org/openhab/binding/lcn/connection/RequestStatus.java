/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.connection;

/**
 * Manages timeout and retry logic for an LCN request.
 *  
 * @author Tobias Jüttner
 */
public class RequestStatus {
	
	/** Interval for forced updates. -1 if not used. */
	private final long maxAgeMSec;
	
	/** Tells how often a request will be sent if no response was received. */
	private final int numTries;
	
	/** true if request logic is activated. */
	private boolean isActive;
	
	/** The time the current request was sent out or 0. */
	private long currRequestTimeStamp;

	/** The time stamp of the next scheduled request or 0. */
	private long nextRequestTimeStamp;
	
	/** Number of retries left until the request is marked as failed. */
	private int numRetriesLeft;
	
	/**
	 * Constructor.
	 * 
	 * @param maxAgeMSec the forced-updates interval (-1 if not used)
	 * @param numTries the maximum number of tries until the request is marked as failed
	 */
	RequestStatus(long maxAgeMSec, int numTries) {
		this.maxAgeMSec = maxAgeMSec;
		this.numTries = numTries;
		this.reset();
	}
	
	/** Resets the runtime data to the initial states. */
	void reset() {
		this.isActive = false;
		this.currRequestTimeStamp = 0;
		this.nextRequestTimeStamp = 0;
		this.numRetriesLeft = 0;		
	}	
	
	/**
	 * Checks whether the request logic is active.
	 *  
	 * @return true if active
	 */
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Checks whether a request is waiting for a response.
	 * 
	 * @return true if waiting for a response
	 */
	boolean isPending() {
		return this.currRequestTimeStamp != 0;
	}
	
	/**
	 * Checks whether the request is active and ran into timeout while waiting for a response.
	 * 
	 * @param timeoutMSec the timeout in milliseconds
	 * @param currTime the current time stamp
	 * @return true if request timed out
	 */
	boolean isTimeout(long timeoutMSec, long currTime) {
		return this.isPending() && currTime - this.currRequestTimeStamp >= timeoutMSec * 1000000L;
	}
	
	/**
	 * Checks for failed requests (active and out of retries).
	 * 
	 * @param timeoutMSec the timeout in milliseconds
	 * @param currTime the current time stamp 
	 * @return true if no response was received and no retries are left
	 */
	boolean isFailed(long timeoutMSec, long currTime) {
		return this.isTimeout(timeoutMSec, currTime) && this.numRetriesLeft == 0;
	}
	
	/**
	 * Schedules the next request.
	 * 
	 * @param delayMSec the delay in milliseconds
	 * @param currTime the current time stamp 
	 */
	public void nextRequestIn(long delayMSec, long currTime) {
		this.isActive = true;
		this.nextRequestTimeStamp = currTime + delayMSec * 1000000L;
	}	
	
	/**
	 * Checks whether sending a new request is required (should be called periodically).
	 * 
	 * @param timeoutMSec the time to wait for a response before retrying the request
	 * @param currTime the current time stamp
	 * @return true to indicate a new request should be sent
	 */
	boolean shouldSendNextRequest(long timeoutMSec, long currTime) {
		if (this.isActive) {
			if (this.nextRequestTimeStamp != 0 && currTime >= this.nextRequestTimeStamp) {
				return true;
			}
			// Retry of current request (after no response was received)
			if (this.isTimeout(timeoutMSec, currTime)) {
				return this.numRetriesLeft > 0;
			}
		}
		return false;
	}
	
	/**
	 * Must be called right after a new request has been sent.
	 * Must be activated first.
	 * 
	 * @param currTime the current time stamp
	 */
	public void onRequestSent(long currTime) {
		if (!this.isActive) {
			throw new Error();
		}
		// Updates retry counter
		if (this.currRequestTimeStamp == 0) {
			this.numRetriesLeft = this.numTries - 1;
		}
		else if (this.numRetriesLeft > 0) {  // Should not happen if used correctly
			--this.numRetriesLeft;
		}
		// Mark request as pending
		this.currRequestTimeStamp = currTime;
		// Schedule next request
		if (this.maxAgeMSec != -1) {
			this.nextRequestIn(this.maxAgeMSec, currTime);
		}
		else {
			this.nextRequestTimeStamp = 0;
		}
	}
	
	/** Must be called when a response (requested or not) has been received. */
	public void onResponseReceived() {
		if (this.isActive) {
			this.currRequestTimeStamp = 0;  // Mark request (if any) as successful
		}
	}
	
}
