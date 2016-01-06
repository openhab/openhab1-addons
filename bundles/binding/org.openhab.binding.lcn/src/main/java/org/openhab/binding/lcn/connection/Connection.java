/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnAddrGrp;
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a configured connection to LCN-PCHK.
 * It uses a {@link SocketChannel} to connect to LCN-PCHK.
 * Included logic:<ul>
 * <li>Reconnection on connection loss
 * <li>Segment scan (to detect the local segment)
 * <li>Acknowledge handling
 * <li>Periodic value requests</ul>
 * It also caches runtime data about the underlying LCN bus.
 * 
 * @author Tobias Jüttner
 */
public class Connection {
	
	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(Connection.class);
	
	/** Must be implemented by the owner. */
	public interface Callback {
		
		/**
		 * Gets the NIO selector used for connect and read events.
		 * 
		 * @return the selector
		 */
		Selector getSelector();
		
		/**
		 * Updates all openHAB items associated with the given connection.
		 * 
		 * @param conn the connection
		 */
		void updateItems(Connection conn);
		
		/**
		 * Process input received from the given connection.
		 * 
		 * @param input the received input
		 * @param conn the source connection
		 */
		void onInputReceived(String input, Connection conn);
		
		/**
		 * Get the sync.-monitor to use when registering new channels with the NIO selector.
		 * 
		 * @return the sync.-monitor object
		 */
		Object getChannelRegisterSync();
		
	}
	
	/** Interval between keep-alive packets (keeps the LCN-PCHK connection open). */
	private static final long PING_INTERVAL_MSEC = 600000;
	
	/** The connection's settings. Never changed. */
	private final ConnectionSettings sets;
	
	/** The callback to the owner. */
	private final Callback callback;
	
	/** Indicates async. connecting is in progress. */
	private boolean isChannelConnecting;
	
	/** The actual NIO channel. null if disconnected. */
	private SocketChannel channel;
	
	/** != 0 if we are currently reconnecting. */
	private long reconnectTimestamp;
	
	/** Moment the last ping was sent (0 if disconnected). */
	private long lastPingTimeStamp;
	
	/** Counter used in pings ("LCN-PCHK best practice"). */
	private int pingCounter;
	
	/** Connection state of the LCN bus (LCN-PK/PKU). */
	private boolean isLcnConnected;
	
	/** The local segment id. -1 means "unknown". */
	private int localSegId;
	
	/** Status of segment-scan. */
	private final RequestStatus statusSegmentScan = new RequestStatus(-1, 3);
	
	/** Holds data read from the {@link #channel} that has not been processed yet. */ 
	private final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	
	/** Queued data that has to be sent. */
	private final LinkedList<SendData> sendQueue = new LinkedList<SendData>();
	
	/** Buffer used in {@link #flush()}. Reused for optimization. */
	private final ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	
	/** Stores information about LCN modules and coordinates status requests. */
	private final HashMap<LcnAddrMod, ModInfo> modData = new HashMap<LcnAddrMod, ModInfo>();
	
	/**
	 * Constructs a clean (disconnected) connection with the given settings.
	 * This does not start the actual connection process.
	 * 
	 * @param sets the settings to use for the new connection
	 * @param callback the callback to the owner
	 */
	public Connection(ConnectionSettings sets, Callback callback) {
		this.sets = sets;
		this.callback = callback;
		this.clearRuntimeData();
	}
	
	/** Clears all runtime data. */
	private void clearRuntimeData() {
		this.isChannelConnecting = false;
		this.channel = null;			
		this.reconnectTimestamp = 0;
		this.lastPingTimeStamp = 0;
		this.pingCounter = 0;
		this.isLcnConnected = false;			
		this.localSegId = -1;
		this.statusSegmentScan.reset();
		this.readBuffer.clear();
		this.sendQueue.clear();
		this.sendBuffer.clear();
		this.modData.clear();		
	}
	
	/**
	 * Retrieves the settings for this connection (never changed).
	 * 
	 * @return the settings
	 */
	public ConnectionSettings getSets() {
		return this.sets;
	}
	
	/**
	 * Checks whether the underlying channel is currently connecting.
	 *  
	 * @return true if connecting is in progress
	 */
	boolean isChannelConnecting() {
		return this.isChannelConnecting;
	}
	
	/**
	 * Returns the connection state of the underlying channel.
	 * 
	 * @return the connection state
	 */
	boolean isChannelConnected() {
		return this.channel != null && this.channel.isConnected();
	}
	
	/** Called after successful authentication. */ 
	public void onAuthOk() {
		// Legacy support for LCN-PCHK 2.2 and earlier:
		// There was no explicit "LCN connected" notification after successful authentication.
		// Only "LCN disconnected" would be reported immediately. That means "LCN connected" used to be the default.
		// Note that LCN-PCHK 2.3 and later will set the connection state a second time. 
		this.setLcnConnected(true);  // Only guessed
	}
	
	/**
	 * Retrieves the current connection state to the LCN bus (LCN-PK/PKU).
	 * 
	 * @return the LCN connection state
	 */
	public boolean isLcnConnected() {
		return this.isLcnConnected;
	}
	
	/**
	 * Sets the current connection state.
	 * 
	 * @param isLcnConnected the state
	 */
	public void setLcnConnected(boolean isLcnConnected) {
		if (isLcnConnected) {
			if (!this.statusSegmentScan.isActive()) {
				this.statusSegmentScan.nextRequestIn(0, System.nanoTime());
			}
		}
		else {
			// Repeat segment scan on next connect
			this.localSegId = -1;
			this.statusSegmentScan.reset();
			// While we are disconnected we will miss all status messages.
			// Clearing our runtime data will give us a fresh start.
			this.modData.clear();
		}
		this.isLcnConnected = isLcnConnected;
	}
	
	/**
	 * Sets the local segment id.
	 * 
	 * @param localSegId the new local segment id
	 */
	public void setLocalSegId(int localSegId) {
		this.localSegId = localSegId;
		this.statusSegmentScan.onResponseReceived();
	}
	
	/**
	 * Translates the given physical address into its logical equivalent.
	 * 
	 * @param addr the (source) address as received directly from the LCN bus
	 * @return the translated address (segment 0 will be replaced with the "real" segment id)
	 */
	public LcnAddrMod physicalToLogical(LcnAddrMod addr) {
		return new LcnAddrMod(addr.getSegId() == 0 ? this.localSegId : addr.getSegId(), addr.getModId());
	}
	
	/**
	 * Called whenever an acknowledge is received.
	 * 
	 * @param addr the source LCN module
	 * @param code the LCN internal code (-1 = "positive")
	 */
	public void onAck(LcnAddrMod addr, int code) {
		ModInfo info = this.modData.get(addr);
		if (info != null) {
			info.onAck(code, this, this.sets.getTimeout(), System.nanoTime());
		}
	}
	
	/**
	 * Retrieves the completion state.
	 * Nothing should be sent before this is signaled.
	 * 
	 * @return true if everything is set-up
	 */
	public boolean isReady() {
		return this.isChannelConnected() && this.isLcnConnected && this.localSegId != -1;
	}
	
	/**
	 * Retrieves cached data for the given LCN module.
	 * Must be created by calling {@link #updateModuleData}.
	 * 
	 * @param addr the module's address
	 * @return the data or null
	 */
	public ModInfo getModInfo(LcnAddrMod addr) {
		return this.modData.get(addr);
	}
	
	/**
	 * Creates and/or returns cached data for the given LCN module.
	 * 
	 * @param addr the module's address
	 * @return the data (never null)
	 */
	public ModInfo updateModuleData(LcnAddrMod addr) {
		ModInfo data = this.modData.get(addr);
		if (data == null) {
			data = new ModInfo(addr);
			this.modData.put(addr, data);
		}
		return data;
	}
	
	/**
	 * Must be called once the connection is established.
	 * 
	 * @throws ClosedChannelException if channel is not open
	 */
	private void onConnected() throws ClosedChannelException {
		this.isChannelConnecting = false;
		this.lastPingTimeStamp = System.nanoTime();  // Start ping timer
		synchronized (this.callback.getChannelRegisterSync()) {
			this.callback.getSelector().wakeup();  // Wakes up a current or next select
			this.channel.register(this.callback.getSelector(), SelectionKey.OP_READ, this);				
		}		
	}	
	
	/** Begin to connect (async.). */
	void beginConnect() {
		this.disconnect();  // Be kind
		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(false);
			synchronized (this.callback.getChannelRegisterSync()) {
				this.callback.getSelector().wakeup();  // Wakes up a current or next select
				channel.register(this.callback.getSelector(), SelectionKey.OP_CONNECT, this);
			}
			logger.info(String.format("Connecting channel \"%s\".", channel));				
			String resolvedAddress = InetAddress.getByName(sets.getAddress()).getHostAddress();
			if (channel.connect(new InetSocketAddress(resolvedAddress, sets.getPort()))) {
				// Connection was established immediately.
				// Otherwise the selector will be used to finish the connection.
				this.onConnected();
			}
			this.channel = channel;
			this.isChannelConnecting = true;			
		} catch (UnknownHostException ex) {
			logger.warn(String.format("Unable to resolve host: %s", ex.getMessage()));
		} catch (IOException ex) {
			logger.warn(String.format("Unable to set up a new SocketChannel: %s", ex.getMessage()));		
		}
	}
	
	/**
	 * Must be called once the connection is established.
	 * 	
	 * @throws IOException if connection cannot be established
	 */
	void finishConnect() throws IOException {
		if (this.channel.finishConnect()) {
			this.onConnected();
		}
	}
	
	/** Disconnect (sync.). */
	void disconnect() {
		if (this.channel != null) {
			this.channel.keyFor(this.callback.getSelector()).cancel();
			this.callback.getSelector().wakeup();  // Unblock a current selection to actually remove the key						
			try {
				logger.debug("...resetting channel...");
				this.channel.close();
			} catch (IOException e) {
				logger.error(String.format("An exception occurred while closing a channel: %s", e.getMessage()));
			}				
			this.clearRuntimeData();
		}
	}

	/**
	 * Schedules a reconnect attempt after the given time.
	 * Can be called multiple times without side effects.
	 *  
	 * @param timeMSec the delay in milliseconds.
	 */
	void beginReconnect(int timeMSec) {
		this.disconnect();  // Be kind
		this.reconnectTimestamp = System.nanoTime() + (long)timeMSec * 1000000L;
	}
	
	/**
	 * Reads and processes input from the underlying channel.
	 * Fragmented input is kept in {@link #readBuffer} and will be processed with the next call.
	 * 
	 * @throws IOException if connection was closed or a generic channel error occurred
	 */
	void readAndProcess() throws IOException {
		try {
			int n;
			if ((n = this.channel.read(this.readBuffer)) == -1) {
				throw new IOException("Connection was closed by foreign host.");
			}
			this.readBuffer.flip();
			int aPos = this.readBuffer.position();  // 0			
			String s = new String(this.readBuffer.array(), aPos, n, LcnDefs.LCN_ENCODING);
			int pos1 = 0, pos2 = s.indexOf(PckGenerator.TERMINATION, pos1);
			while (pos2 != -1) {
				this.callback.onInputReceived(s.substring(pos1, pos2), this);
				// Seek position in input array
				aPos += s.substring(pos1, pos2 + 1).getBytes(LcnDefs.LCN_ENCODING).length;
				// Next input
				pos1 = pos2 + 1;
				pos2 = s.indexOf(PckGenerator.TERMINATION, pos1);
			}
			this.readBuffer.limit(this.readBuffer.capacity());
			this.readBuffer.position(n - aPos);  // Keeps fragments for the next call
		} catch (UnsupportedEncodingException ex) {
			logger.warn(String.format("Unable to decode input from channel \"%s\": %s", this.sets.getId(), ex.getMessage()));
		}
	}
	
	/**
	 * Queues data to be sent to LCN-PCHK.
	 * Sending will be done the next time {@link #flush()} is called.
	 * 
	 * @param data the data
	 */
	void queue(SendData data) {
		this.sendQueue.add(data);
	}

	/**
	 * Queues plain text to be sent to LCN-PCHK.
	 * Sending will be done the next time {@link #flush()} is called.
	 * 
	 * @param plainText the text
	 */
	public void queue(String plainText) {
		this.queue(new SendData.PlainText(plainText));
	}
	
	/**
	 * Queues a PCK command to be sent.
	 * 
	 * @param addr the target LCN address
	 * @param wantsAck true to wait for acknowledge on receipt (should be false for group addresses)
	 * @param data the pure PCK command (without address header)
	 */
	public void queue(LcnAddr addr, boolean wantsAck, ByteBuffer data) {
		if (!addr.isGroup() && wantsAck) {
			this.updateModuleData((LcnAddrMod)addr).queuePckCommandWithAck(data, this, this.sets.getTimeout(), System.nanoTime());
		}
		else {
			this.queue(new SendData.PckSendData(addr, wantsAck, data));
		}
	}
	
	/**
	 * Queues a PCK command to be sent.
	 * 
	 * @param addr the target LCN address
	 * @param wantsAck true to wait for acknowledge on receipt (should be false for group addresses)
	 * @param pck the pure PCK command (without address header)
	 */
	public void queue(LcnAddr addr, boolean wantsAck, String pck) {
		try {
			this.queue(addr, wantsAck, ByteBuffer.wrap(pck.getBytes(LcnDefs.LCN_ENCODING)));
		}
		catch (UnsupportedEncodingException ex) {
			logger.error(String.format("Failed to encode PCK command: %s", pck));
		}
	}
	
	/**
	 * Writes all queued data.
	 * Will try to write all data at once to reduce overhead.
	 */
	void flush() {
		if (this.isChannelConnected()) {
			// Write send-queue to buffer
			try {
				this.sendBuffer.clear();				
				Iterator<SendData> iter = this.sendQueue.iterator();
				while (iter.hasNext()) {
					try {
						if (!((SendData)iter.next()).write(this.sendBuffer, this.localSegId))
							break;
					} catch (UnsupportedEncodingException ex) { }
					iter.remove();
				}
			} catch (BufferOverflowException ex) {
				// Not critical. Our buffer is too small to hold all data.
				// The rest will be processed in the next flush.
			}
			// Write buffer to channel
			try {
				this.sendBuffer.flip();
				if (this.channel.write(this.sendBuffer) != this.sendBuffer.limit()) {
					logger.warn(String.format("Data loss while writing to channel \"%s\".", this.sets.getAddress()));
				}
			} catch (IOException ex) {
				logger.warn(String.format("Writing to channel \"%s\" failed: %s", this.sets.getAddress(), ex.getMessage()));
			}
		}
	}
	
	/** Must be called periodically to keep the inner logic active. */
	void update() {
		long currTime = System.nanoTime();
		// Reconnect logic
		if (this.reconnectTimestamp != 0 && currTime >= this.reconnectTimestamp) {
			this.beginConnect();
			this.reconnectTimestamp = 0;
		}
		else {
			if (this.isChannelConnected()) {
				// Keep-alive / ping logic				
				if (currTime - this.lastPingTimeStamp > PING_INTERVAL_MSEC * 1000000L) {
					this.queue(PckGenerator.ping(++this.pingCounter));
					this.lastPingTimeStamp = currTime;
				}
				// Segment scan logic
				if (this.statusSegmentScan.shouldSendNextRequest(this.sets.getTimeout(), currTime)) {
					this.queue(new LcnAddrGrp(3, 3), false, PckGenerator.segmentCouplerScan());
					this.statusSegmentScan.onRequestSent(currTime);
				} else if (this.statusSegmentScan.isFailed(this.sets.getTimeout(), currTime)) {
					// Give up. Probably no segments available.
					this.setLocalSegId(0);
				}
			}
			// LcnModInfo logic
			this.callback.updateItems(this);
			if (this.isReady()) {
				for (Map.Entry<LcnAddrMod, ModInfo> entry : this.modData.entrySet()) {
					entry.getValue().update(this, this.sets.getTimeout(), currTime);
				}
			}			
		}
	}
	
}
