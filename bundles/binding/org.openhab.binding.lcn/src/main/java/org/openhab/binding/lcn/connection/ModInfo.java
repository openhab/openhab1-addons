/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.connection;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.LcnDefs.Var;
import org.openhab.binding.lcn.common.PckGenerator;

/**
 * Holds data of an LCN module.
 * <ul>
 * <li>Stores the module's firmware version (if requested)
 * <li>Manages the scheduling of status-requests
 * <li>Manages the scheduling of acknowledged commands
 * </ul>
 *  
 * @author Tobias Jüttner
 */
public class ModInfo {
	
	/** Total number of request to sent before going into failed-state. */
	private static final int NUM_TRIES = 3;
	
	/** Poll interval for status values that automatically send their values on change. */
	private static final int MAX_STATUS_EVENTBASED_VALUEAGE_MSEC = 600000;
	
	/** Poll interval for status values that do not send their values on change (always polled). */
	private static final int MAX_STATUS_POLLED_VALUEAGE_MSEC = 30000;
	
	/** Status request delay after a command has been send which potentially changed that status. */
	public static final int STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC = 2000;
	
	/** The LCN module's address. */
	private final LcnAddrMod addr;
	
	/** Firmware date of the LCN module. -1 means "unknown". */
	private int swAge = -1;
	
	/**
	 * Gets the LCN module's firmware date.
	 * 
	 * @return the date
	 */
	public int getSwAge() {
		return this.swAge;
	}
	
	/**
	 * Sets the LCN module's firmware date.
	 * 
	 * @param swAge the date
	 */
	public void setSwAge(int swAge) {
		this.swAge = swAge;
	}
	
	/** Firmware version request status. */
	public final RequestStatus requestSwAge = new RequestStatus(-1, NUM_TRIES);
	
	/** Output-port request status (0..3). */
	public final ArrayList<RequestStatus> requestStatusOutputs = new ArrayList<RequestStatus>();
	
	/** Relays request status (all 8). */
	public final RequestStatus requestStatusRelays = new RequestStatus(MAX_STATUS_EVENTBASED_VALUEAGE_MSEC, NUM_TRIES);
	
	/** Binary-sensors request status (all 8). */
	public final RequestStatus requestStatusBinSensors = new RequestStatus(MAX_STATUS_EVENTBASED_VALUEAGE_MSEC, NUM_TRIES);
	
	/**
	 * Variables request status.
	 * Lazy initialization: Will be filled once the firmware version is known.
	 */
	public final TreeMap<LcnDefs.Var, RequestStatus> requestStatusVars = new TreeMap<LcnDefs.Var, RequestStatus>();

	/** LEDs and logic-operations request status (all 12+4). */
	public final RequestStatus requestStatusLedsAndLogicOps = new RequestStatus(MAX_STATUS_POLLED_VALUEAGE_MSEC, NUM_TRIES);
	
	/** Key lock-states request status (all tables, A-D). */
	public final RequestStatus requestStatusLockedKeys = new RequestStatus(MAX_STATUS_POLLED_VALUEAGE_MSEC, NUM_TRIES);
	
	/**
	 * Holds the last LCN variable requested whose response will not contain the variable's type.
	 * {@link Var#UNKNOWN} means there is currently no such request.
	 */
	private LcnDefs.Var lastRequestedVarWithoutTypeInResponse = LcnDefs.Var.UNKNOWN;
	
	/**
	 * List of queued PCK commands to be acknowledged by the LCN module.
	 * Commands are always without address header.
	 * Note that the first one might currently be "in progress".
	 */
	private final LinkedList<ByteBuffer> pckCommandsWithAck = new LinkedList<ByteBuffer>();
	
	/** Status data for the currently processed {@link PckCommandWithAck}. */ 
	private final RequestStatus requestCurrPckCommandWithAck = new RequestStatus(-1, NUM_TRIES); 
	
	/**
	 * Constructor.
	 * 
	 * @param addr the module's address
	 */
	public ModInfo(LcnAddrMod addr) {
		this.addr = addr;
		for (int i = 0; i < 4; ++i) {
			this.requestStatusOutputs.add(new RequestStatus(MAX_STATUS_EVENTBASED_VALUEAGE_MSEC, NUM_TRIES));
		}
	}
	
	/**
	 * Resets all status requests.
	 * Helpful to re-request initial data in case a new {@link LcnBindingConfig} has been loaded.
	 */
	public void resetNotCachedStatusRequests() {
		for (RequestStatus s : this.requestStatusOutputs) {
			s.reset();
		}
		this.requestStatusRelays.reset();
		this.requestStatusBinSensors.reset();
		for (RequestStatus s : this.requestStatusVars.values()) {
			s.reset();
		}
		this.requestStatusLedsAndLogicOps.reset();
		this.requestStatusLockedKeys.reset();
		this.lastRequestedVarWithoutTypeInResponse = LcnDefs.Var.UNKNOWN;
	}
	
	/**
	 * Gets the last requested variable whose response will not contain the variables type.
	 * 
	 * @return the "typeless" variable
	 */
	public LcnDefs.Var getLastRequestedVarWithoutTypeInResponse() {
		return this.lastRequestedVarWithoutTypeInResponse;
	}
	
	/**
	 * Sets the last requested variable whose response will not contain the variables type.
	 * 
	 * @param var the "typeless" variable
	 */
	public void setLastRequestedVarWithoutTypeInResponse(LcnDefs.Var var) {
		this.lastRequestedVarWithoutTypeInResponse = var;
	}
	
	/**
	 * Queues a PCK command to be sent.
	 * It will request an acknowledge from the LCN module on receipt.
	 * If there is no response within the request timeout, the command is retried.
	 * 
	 * @param data the PCK command to send (without address header)
	 * @param timeoutMSec the time to wait for a response before retrying a request
	 * @param currTime the current time stamp
	 */
	public void queuePckCommandWithAck(ByteBuffer data, Connection conn, long timeoutMSec, long currTime) {
		this.pckCommandsWithAck.add(data);
		// Try to process the new acknowledged command. Will do nothing if another one is still in progress.
		this.tryProcessNextCommandWithAck(conn, timeoutMSec, currTime);
	}
	
	/**
	 * Called whenever an acknowledge is received from the LCN module.
	 * 
	 * @param code the LCN internal code. -1 means "positive" acknowledge
	 * @param timeoutMSec the time to wait for a response before retrying a request
	 * @param currTime the current time stamp 
	 */
	public void onAck(int code, Connection conn, long timeoutMSec, long currTime) {
		if (this.requestCurrPckCommandWithAck.isActive()) {  // Check if we wait for an ack.
			this.pckCommandsWithAck.pollFirst();
			this.requestCurrPckCommandWithAck.reset();
			// Try to process next acknowledged command
			this.tryProcessNextCommandWithAck(conn, timeoutMSec, currTime);
		}
	}
	
	/**
	 * Sends the next acknowledged command from the queue.
	 * 
	 * @param conn the {@link Connection} belonging to this {@link ModInfo}
	 * @param timeoutMSec the time to wait for a response before retrying a request
	 * @param currTime the current time stamp
	 * @return true if a new command was sent
	 */
	private boolean tryProcessNextCommandWithAck(Connection conn, long timeoutMSec, long currTime) {
		// Use the chance to remove a failed command first
		if (this.requestCurrPckCommandWithAck.isFailed(timeoutMSec, currTime)) {
			this.pckCommandsWithAck.pollFirst();
			this.requestCurrPckCommandWithAck.reset();
		}
		// Peek new command
		if (!this.pckCommandsWithAck.isEmpty() && !this.requestCurrPckCommandWithAck.isActive()) {
			this.requestCurrPckCommandWithAck.nextRequestIn(0, currTime);
		}
		ByteBuffer data = this.requestCurrPckCommandWithAck.shouldSendNextRequest(timeoutMSec, currTime) ?
			this.pckCommandsWithAck.peekFirst() : null;		
		if (data == null) {
			return false;
		}
		conn.queue(new SendData.PckSendData(this.addr, true, data));
		this.requestCurrPckCommandWithAck.onRequestSent(currTime);
		return true;		
	}
	
	/**
	 * Keeps the request logic active.
	 * Must be called periodically.
	 * 
	 * @param conn the {@link Connection} belonging to this {@link ModInfo}
	 * @param timeoutMSec the time to wait for a response before retrying a request
	 * @param currTime the current time stamp
	 */
	void update(Connection conn, long timeoutMSec, long currTime) {
		// Firmware request
		RequestStatus r;
		if ((r = this.requestSwAge).shouldSendNextRequest(timeoutMSec, currTime)) {
			conn.queue(this.addr, false, PckGenerator.requestSn());
			r.onRequestSent(currTime);
		}
		// Output-port requests
		for (int i = 0; i < 4; ++i) {
			if ((r = this.requestStatusOutputs.get(i)).shouldSendNextRequest(timeoutMSec, currTime)) {
				conn.queue(this.addr, false, PckGenerator.requestOutputStatus(i));
				r.onRequestSent(currTime);
			}
		}
		// Relays request
		if ((r = this.requestStatusRelays).shouldSendNextRequest(timeoutMSec, currTime)) {
			conn.queue(this.addr, false, PckGenerator.requestRelaysStatus());
			r.onRequestSent(currTime);
		}
		// Binary-sensors request
		if ((r = this.requestStatusBinSensors).shouldSendNextRequest(timeoutMSec, currTime)) {
			conn.queue(this.addr, false, PckGenerator.requestBinSensorsStatus());
			r.onRequestSent(currTime);
		}
		// Variable requests
		if (this.swAge != -1) {  // Firmware version is required
			// Initialize if not done yet (we had to wait for the firmware version)
			if (this.requestStatusVars.isEmpty()) {
				for (LcnDefs.Var var : LcnDefs.Var.values()) {
					if (var != LcnDefs.Var.UNKNOWN) {
						this.requestStatusVars.put(var, new RequestStatus(swAge >= 0x170206 ?
							MAX_STATUS_EVENTBASED_VALUEAGE_MSEC : MAX_STATUS_POLLED_VALUEAGE_MSEC, NUM_TRIES));
					}
				}
			}
			// Use the chance to remove a failed "typeless variable" request
			if (this.lastRequestedVarWithoutTypeInResponse != LcnDefs.Var.UNKNOWN) {
				if (this.requestStatusVars.get(this.lastRequestedVarWithoutTypeInResponse).isTimeout(timeoutMSec, currTime)) {
					this.lastRequestedVarWithoutTypeInResponse = LcnDefs.Var.UNKNOWN;
				}
			}			
			// Variables
			for (Map.Entry<LcnDefs.Var, RequestStatus> kv : this.requestStatusVars.entrySet()) {
				if ((r = kv.getValue()).shouldSendNextRequest(timeoutMSec, currTime)) {
					// Detect if we can send immediately or if we have to wait for a "typeless" request first
					boolean hasTypeInResponse = LcnDefs.Var.hasTypeInResponse(kv.getKey(), this.swAge);
					if (hasTypeInResponse || this.lastRequestedVarWithoutTypeInResponse == LcnDefs.Var.UNKNOWN) {
						try {
							conn.queue(this.addr, false, PckGenerator.requestVarStatus(kv.getKey(), this.swAge));
							r.onRequestSent(currTime);
							if (!hasTypeInResponse) {									
								this.lastRequestedVarWithoutTypeInResponse = kv.getKey();
							}
						}
						catch (IllegalArgumentException ex) {
							r.reset();
						}
					}
				}
			}
		}
		// LEDs and logic-operations request
		if ((r = this.requestStatusLedsAndLogicOps).shouldSendNextRequest(timeoutMSec, currTime)) {
			conn.queue(this.addr, false, PckGenerator.requestLedsAndLogicOpsStatus());
			r.onRequestSent(currTime);
		}
		// Key-locks request
		if ((r = this.requestStatusLockedKeys).shouldSendNextRequest(timeoutMSec, currTime)) {
			conn.queue(this.addr, false, PckGenerator.requestKeyLocksStatus());
			r.onRequestSent(currTime);
		}
		// Try to send next acknowledged command. Will also detect failed ones.
		this.tryProcessNextCommandWithAck(conn, timeoutMSec, currTime);
	}
	
}
