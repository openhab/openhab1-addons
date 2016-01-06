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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.openhab.binding.lcn.common.LcnBindingNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages all configured connections to LCN-PCHK. 
 * 
 * @author Tobias Jüttner
 */
public class ConnectionManager implements Connection.Callback, Runnable {
	
	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

	/** Must be implemented by the owner. */
	public interface Callback {
		
		/**
		 * Runs a notification on the refresh thread.
		 * 
		 * @param n the notification
		 */
		void runOnRefreshThreadAsync(LcnBindingNotification n);
		
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
		 * Get all connection settings.
		 * 
		 * @return the settings 
		 */
		Map<String, ConnectionSettings> getAllSets();
		
	}
	
	/** Time to wait before a reconnect attempt (in milliseconds). */
	private static final int RECONNECT_INTERVAL_MSEC = 5000;
	
	/** Used to detect closed channels. */
	private static final int DETECT_CLOSED_CHANNELS_INTERVAL_MSEC = 10000;
	
	/** The callback to the owner. */
	private final Callback callback;
	
	/** Thread for connecting and reading from all connections. */
	private Thread thread;
	
	/** Termination flag for for the thread. */
	private volatile boolean threadTreminate = false;
	
	/** Selector used to handle channel events. */
	private Selector selector;
	
	/**
	 * Sync. object used to register new channels.
	 * Will hinder the {@link #selector} to enter a new {@link Selector#select()} while the channel is still registering. 
	 */
	private final Object channelRegisterSync = new Object();
	
	/** List of all connections stored by their unique identifier (upper-case). */
	private final HashMap<String, Connection> connectionsById = new HashMap<String, Connection>();
	
	/**
	 * Constructs a connection manager with the given owner callback.
	 * 
	 * @param callback the owner
	 */
	public ConnectionManager(Callback callback) {
		this.callback = callback;
	}
	
	/**
	 * Adds a new connection.
	 * 
	 * @param conn the connection to add
	 */
	public void add(Connection conn) {
		this.connectionsById.put(conn.getSets().getId().toUpperCase(), conn);
	}
	
	/**
	 * Closes and removes the given connection.
	 * 
	 * @param id the connection's unique identifier
	 * @return true on success
	 */
	public boolean disconnectAndRemove(String id) {
		Connection conn = this.connectionsById.get(id.toUpperCase());
		if (conn == null) {
			return false;
		}
		conn.disconnect();
		this.connectionsById.remove(id);		
		return true;
	}

	/**
	 * Find a connection by its unique identifier.
	 * 
	 * @param id the connection to search for
	 * @return the found connection or null
	 */
	public Connection get(String id) {
		return this.connectionsById.get(id.toUpperCase());
	}

	/** Notifies that the binding has started. */
	public void activate() {
		try {
			this.selector = Selector.open();
			this.thread = new Thread(this);
			this.thread.start();			
		} catch (IOException e) {
			logger.error("Unable to open the Selector!");
		}	
	}

	/** Notifies that the binding has stopped. */	
	public void deactivate() {
		try {
			this.threadTreminate = true;
			this.selector.close();
			this.thread.join();
		} catch (InterruptedException ex) {
		} catch (IOException e) {
			logger.error("Unable to close the Selector!");
		}
	}
	
	/** Tells all connections to flush their queued data. */
	public void flush() {
		for (Connection conn : this.connectionsById.values()) {
			conn.flush();
		}
	}

	/**
	 * Updates the managed connections.
	 * Obsolete connections are closed and removed, while new and updated ones are (re-)established.
	 * Finally all connections are told to update themselves. 
	 */	
	public void update() {
		// Close and remove changed and no longer existing connections
		HashSet<String> removeIds = new HashSet<String>(); 
		for (Connection conn : this.connectionsById.values()) {
			ConnectionSettings sets = this.callback.getAllSets().get(conn.getSets().getId().toUpperCase());
			if (sets != null) {
				if (!sets.equals(conn.getSets())) {
					removeIds.add(conn.getSets().getId());
				}
			}
			else {
				removeIds.add(conn.getSets().getId());
			}
		}
		for (String id : removeIds) {
			this.disconnectAndRemove(id);
		}
		// Add new connections
		for (ConnectionSettings sets : this.callback.getAllSets().values()) {
			if (!this.connectionsById.containsKey(sets.getId().toUpperCase())) {
				Connection conn = new Connection(sets, this);
				this.add(conn);
				conn.beginConnect();
			}
		}
		// Finally update now existing connections
		for (Connection conn : this.connectionsById.values()) {
			conn.update();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Selector getSelector() {
		return this.selector;
	}
	
	/** {@inheritDoc} */
	@Override
	public void updateItems(Connection conn) {
		this.callback.updateItems(conn);
	}
	
	/** {@inheritDoc} */
	@Override
	public void onInputReceived(String input, Connection conn) {
		this.callback.onInputReceived(input, conn);
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getChannelRegisterSync() {
		return this.channelRegisterSync;
	}
	
	/**
	 * Main method of the thread.
	 * Establishes new connections and waits for input data.
	 */
	@Override
	public void run() {
		final Object sync = new Object();
		long lastCloseDetectTime = System.nanoTime();
		while (!this.threadTreminate) {
			long currTime = System.nanoTime();
			try {
				int timeoutMSec = DETECT_CLOSED_CHANNELS_INTERVAL_MSEC - (int)((currTime - lastCloseDetectTime) / 1000000L);
				if (timeoutMSec <= 0 || this.selector.select(timeoutMSec) == 0) {
					// Detect closed channels
					synchronized (sync) {
						this.callback.runOnRefreshThreadAsync(new LcnBindingNotification() {
							public void execute() {
								for (Connection conn : ConnectionManager.this.connectionsById.values()) {
									if (!conn.isChannelConnected() && !conn.isChannelConnecting()) {
										logger.warn(String.format("Channel \"%s\" was closed unexpectedly (reconnecting...).", conn.getSets().getId()));
										conn.beginReconnect(RECONNECT_INTERVAL_MSEC);
									}
								}
								synchronized (sync) {
									sync.notify();
								}
							}
						});
						sync.wait();					
					}
					lastCloseDetectTime = currTime;
				}
				else {
					// Process selected keys (connect or read events)
					Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						final SelectionKey key = iter.next();
						// The invocation is done sync. to keep the SelectionKey valid 
						synchronized (sync) {
							this.callback.runOnRefreshThreadAsync(new LcnBindingNotification() {
								public void execute() {
									Connection conn = (Connection)key.attachment();									
									try {
										if (key.isConnectable()) { 
											conn.finishConnect();
										}
										else if (key.isReadable()) {
											conn.readAndProcess();
										}
									} catch (IOException ex) {
										logger.warn(String.format("Cannot process channel \"%s\" (reconnecting...): %s", conn.getSets().getId(), ex.getMessage()));
										conn.beginReconnect(RECONNECT_INTERVAL_MSEC);
									}
									synchronized (sync) {
										sync.notify();
									}
								}
							});
							sync.wait();					
						}
						iter.remove();					
					}
				}
				synchronized (this.channelRegisterSync) { }  // Force a wait here if a new channel is registering
			} catch (InterruptedException ex) {
			} catch (IOException ex) {
				logger.error("Selection failure: " + ex.getMessage());
			}
		}
	}
	
}
