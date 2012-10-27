/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ACESB is the base for all network based connectivity and communication. It requires a SelectableChannel type
 * parameter which is either connection-oriented (SocketChannel) or connection-less (DatagramChannel). It also
 * requires a ChannelBindingProvider based binding provider. Data is pushed around using the BufferElement class,
 * which is essentially a ByteBuffer with an indicator for blocking/non-blocking (synchronous/asynchronous) communication
 * 
 * Each instance of the derived implementation class will start various threads:
 *  ParserThread : thread to parse incoming data which it takes from a readQueue
 *  ReconnectThread : thread that will monitor network connections, and reconnect them if required
 *  SelectorThread : thread that processes SelectionKeys, e.g. it deals with connecting Channels, reading from them (and storing to 
 *  	to the readQueue), and writing data to them that is polled from the writeQueue
 *  
 * @author Karel Goderis
 * @since 1.1.0
 * 
 */

public abstract class AbstractChannelEventSubscriberBinding<C extends AbstractSelectableChannel, P extends ChannelBindingProvider>
extends AbstractEventSubscriberBinding<P> {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractChannelEventSubscriberBinding.class);

	/* ChannelTracker is a simple structure to keep track of Channels */
	protected Map<C,ChannelTracker> channelTrackers = Collections.synchronizedMap(new WeakHashMap<C,ChannelTracker>());

	static protected EventPublisher eventPublisher;
	static protected ItemRegistry itemRegistry;

	protected Thread selectorThread = null;
	protected Thread parserThread = null;
	protected Thread reconnectThread = null;
	protected Selector selector;
	protected final ReentrantLock selectorLock = new ReentrantLock();

	protected int maxBufferSize = 1024;
	protected boolean shutdown = false;


	/**
	 * Instantiates a new abstract channel event subscriber binding.
	 */
	public AbstractChannelEventSubscriberBinding() {
		try {
			selector = Selector.open();
		} catch (IOException e) {

		}
	}

	/**
	 * Simple datastructure to keep track of Channels. Each Channel has its own read and write queue, it 
	 * also keeps track of which items are referencing (make use of) the Channel
	 * 
	 * @author kgoderis
	 *
	 */
	private class ChannelTracker {
		// need to keep track of host:port because in the NIO socket
		// implementation(s) there is no way to get hold of the host:port once
		// connect() is called. e.g. RemoteAddress is only valid if connected, so no other solution
		// to track host:port for connections that fail(ed)
		public String host;
		public int port;
		// list of references that make use of this channel, e.g. Items
		public Collection<String> referers = new HashSet<String>();
		// Queues to manage and track the ByteBuffer we have to send/have
		// received per channel
		public ArrayBlockingQueue<BufferElement> readQueue = new ArrayBlockingQueue<BufferElement>(
				maxBufferSize);
		public ArrayBlockingQueue<BufferElement> writeQueue = new ArrayBlockingQueue<BufferElement>(
				maxBufferSize);
		// Keep track of time so that we can reset blocked sockets every [x]
		// time
		public long timeOfSocketConnection = 0;

		// members to help us manage "blocking" communication
		public boolean inBlockingWriteRead = false;
		public ByteBuffer blockingReadBuffer = null;
		public ReentrantLock blockingLock = new ReentrantLock();
	}

	/**
	 * BufferElements are the containers of data that are handled (received/sent/...) by the binding
	 * 
	 * @author kgoderis
	 *
	 */
	protected class BufferElement {
		public ByteBuffer byteBuffer;
		public boolean isBlocking = false;
	}

	private class ReconnectThread extends Thread {

		private long refreshInterval;
		private int reconnectInterval;

		/**
		 * Instantiates a new reconnect thread.
		 *
		 * @param name the name
		 * @param refreshInterval the refresh interval
		 * @param reconnectInterval the reconnect interval
		 */
		public ReconnectThread(String name, long refreshInterval,
				int reconnectInterval) {
			super(name);
			this.setDaemon(true);
			this.refreshInterval = refreshInterval;
			this.reconnectInterval = reconnectInterval;

			// reset 'interrupted' after stopping this refresh thread ...
			shutdown = false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			logger.debug("{} has been started",getName());

			while (!shutdown) {
				for(C networkChannel : channelTrackers.keySet() ) {
					long elapsedTimeMillis = System.currentTimeMillis()
							- channelTrackers.get(networkChannel).timeOfSocketConnection;
					if (elapsedTimeMillis > reconnectInterval * 60 * 60 * 1000) {
						// disconnect and reconnect the channel	
						logger.debug("It is time to reconnect {}",networkChannel);
						try {
							reconnectPersistentConnection(networkChannel);
						}
						catch(IOException e){
							e.printStackTrace();
						}
					}
				}
				pause(refreshInterval);
			}

			logger.info( "{} has been shut down",getName());

		}

		/**
		 * Pause polling for the given <code>refreshInterval</code>. Possible
		 * {@link InterruptedException} is logged with no further action.
		 * 
		 * @param refreshInterval
		 */
		protected void pause(long refreshInterval) {

			try {
				Thread.sleep(refreshInterval * 600);
			} catch (InterruptedException e) {
				logger.debug("pausing thread " + super.getName()
						+ " interrupted");

			}
		}
	}

	/**
	 * 
	 * ParserThread is the thread class that will do the actual parsing of
	 * received buffer for each of the channels
	 * 
	 * @author Karel Goderis
	 * 
	 */
	private class ParserThread extends Thread {

		private long refreshInterval;

		/**
		 * Instantiates a new parser thread.
		 *
		 * @param name the name
		 * @param refreshInterval the refresh interval
		 */
		public ParserThread(String name, long refreshInterval) {
			super(name);
			this.setDaemon(true);
			this.refreshInterval = refreshInterval;

			// reset 'interrupted' after stopping this refresh thread ...
			shutdown = false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			logger.debug(getName() + " has been started");

			while (!shutdown) {
				// traverse all the ChannelTrackers and see if any has some
				// buffers sitting in its readQueue
				for(C networkChannel : channelTrackers.keySet() ) {

					BufferElement bufferElement = null;

					if (!channelTrackers.get(networkChannel).readQueue.isEmpty()) {
						bufferElement = channelTrackers.get(networkChannel).readQueue.poll();
					}


					if (bufferElement != null) {
						if (bufferElement.isBlocking) {
							channelTrackers.get(networkChannel).blockingReadBuffer = bufferElement.byteBuffer;
						} else {
							parseChanneledBuffer(networkChannel,bufferElement.byteBuffer);
						}
					}
				}
				pause(refreshInterval);
			}

			logger.info(getName() + " has been shut down");

		}

		/**
		 * Pause polling for the given <code>refreshInterval</code>. Possible
		 * {@link InterruptedException} is logged with no further action.
		 * 
		 * @param refreshInterval
		 */
		protected void pause(long refreshInterval) {

			try {
				Thread.sleep(refreshInterval);
			} catch (InterruptedException e) {
				logger.debug("pausing thread " + super.getName()
						+ " interrupted");
			}
		}

	}

	/**
	 * 
	 * SelectorThread is the thread class that will handle the actual transmission
	 * of BufferElements to/from the network
	 * 
	 * @author Karel Goderis
	 * 
	 */
	private class SelectorThread extends Thread {

		private long refreshInterval;

		/**
		 * Instantiates a new selector thread.
		 *
		 * @param name the name
		 * @param refreshInterval the refresh interval
		 */
		public SelectorThread(String name, long refreshInterval) {
			super(name);
			this.setDaemon(true);
			this.refreshInterval = refreshInterval;

			// reset 'interrupted' after stopping this refresh thread ...
			shutdown = false;
		}

		/**
		 * 
		 * Return the first buffer in the queue that is of the blocking kind
		 * 
		 * @param queue
		 * @return BufferElement
		 */
		protected BufferElement findBlockingBuffer(
				ArrayBlockingQueue<BufferElement> queue) {

			Iterator<BufferElement> iterator = queue.iterator();
			while (iterator.hasNext()) {
				BufferElement bufferElement = iterator.next();
				if (bufferElement.isBlocking) {
					queue.remove(bufferElement);
					return bufferElement;
				}
			}

			return null;
		}

		/**
		 * Process selection key.
		 *
		 * @param selKey the sel key
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@SuppressWarnings("unchecked")
		protected void processSelectionKey(SelectionKey selKey)
				throws IOException {

			// Since the ready operations are cumulative,
			// need to check readiness for each operation
			if (selKey.isValid() && selKey.isConnectable()) {
				// Get channel with connection request
				C networkChannel = (C) selKey.channel();

				boolean success = false;
				try {
					success = finishConnectChannel(networkChannel);
				} catch (NoConnectionPendingException e) {
					// this channel is not connected and a connection operation
					// has not been initiated
					logger.warn("{} has no conection pending",networkChannel);
				} catch (ClosedChannelException e) {
					reconnectPersistentConnection(networkChannel);
				} catch (IOException e) {
					// If some other I/O error occurs
					logger.warn("{} has encountered an IO Exception. We close it down",networkChannel);
					// Unregister the channel with this selector and close it down
					selKey.cancel();
					networkChannel.close();
				}

				if (!success || !isConnectedChannel(networkChannel)) {
					// the channel might not be connected yet - we move on

				} else {
					channelTrackers.get(networkChannel).timeOfSocketConnection = System
							.currentTimeMillis();
				}
			}

			if (selKey.isValid() && selKey.isReadable()) {
				// Get channel with bytes to read
				C networkChannel = (C) selKey.channel();

				if (!isConnectedChannel(networkChannel) && !isConnectionPendingChannel(networkChannel)) {
					// the channel is not connected anymore, try to reconnect
					reconnectPersistentConnection(networkChannel);
				} else {

					ByteBuffer readBuffer = ByteBuffer.allocate(maxBufferSize);

					int numberBytesRead = 0;
					try {
						//TODO: Additional code to split readBuffer in multiple parts, in case the data send by the remote end is not correctly fragemented. Could be handed of to implementation class if for example, the buffer needs to be split based on a special character like line feed or carriage return
						numberBytesRead = readChannel(networkChannel,readBuffer);
					} catch (NotYetConnectedException e) {
						// If this channel is not yet connected
					} catch (IOException e) {
						// If some other I/O error occurs
						logger.warn("{} has encountered an IO Exception. We close it down",networkChannel);

						// Unregister the channel with this selector and close it down
						selKey.cancel();
						networkChannel.close();
					}

					if (numberBytesRead == -1) {
						// seems to be disconnected, try to reconnect
						if (!isConnectedChannel(networkChannel)) {
							reconnectPersistentConnection(networkChannel);
						}
					} else {
						if (channelTrackers.get(networkChannel).inBlockingWriteRead == true
								&& findBlockingBuffer(channelTrackers.get(networkChannel).writeQueue) == null) {
							// Handle a "blocking" call; these do not go through the regular queue
							readBuffer.flip();
							synchronized (channelTrackers.get(networkChannel)) {
								channelTrackers.get(networkChannel).blockingReadBuffer = readBuffer;
								channelTrackers.get(networkChannel).inBlockingWriteRead = false;
							}
						} else {
							// non-blocking operations are simply added to the end of the queue
							BufferElement bufferElement = new BufferElement();
							bufferElement.byteBuffer = readBuffer;
							bufferElement.byteBuffer.flip();
							bufferElement.isBlocking = false;

							channelTrackers.get(networkChannel).readQueue.add(bufferElement);
						}
					}
				}

			}

			if (selKey.isValid() && selKey.isWritable()) {
				// Get channel that's ready for more bytes
				C networkChannel = (C) selKey.channel();
				BufferElement bufferElement = null;

				if (!isConnectedChannel(networkChannel) && !isConnectionPendingChannel(networkChannel)) {
					// the channel is not connected anymore, try to reconnect
					reconnectPersistentConnection(networkChannel);
				} else {
					if (channelTrackers.get(networkChannel).inBlockingWriteRead) {
						// keep looping until we find a "blocking" buffer in the writeQueue
						bufferElement = findBlockingBuffer(channelTrackers.get(networkChannel).writeQueue);
						while (bufferElement == null) {
							bufferElement = findBlockingBuffer(channelTrackers.get(networkChannel).writeQueue);
						}
					} else {
						// if not blocking, we simply pop the next buffer from the queue
						if (!channelTrackers.get(networkChannel).writeQueue.isEmpty()) {
							bufferElement = channelTrackers.get(networkChannel).writeQueue.poll();
						}
					}

					if (bufferElement != null) {
						bufferElement.byteBuffer.rewind();

						if (bufferElement.byteBuffer.limit() > 0) {
							try {
								writeChannel(networkChannel,bufferElement.byteBuffer);
							} catch (NotYetConnectedException e) {
								// If this channel is not yet connected
							} catch (IOException e) {
								// If some other I/O error occurs
								logger.warn("{} has encountered an IO Exception. We close it down",networkChannel);

								// Unregister the channel with this selector and close it down
								selKey.cancel();
								networkChannel.close();
							}
						}
					}
				}
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			logger.debug(getName() + " has been started");

			while (!shutdown) {
				try {
					// Wait for an event
					selectorLock.lock();
					selector.selectNow();
					selectorLock.unlock();
				} catch (IOException e) {
					// Handle error with selector
					break;
				}

				// Get list of selection keys with pending events
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();

				// Process each key at a time
				while (it.hasNext()) {
					// Get the selection key
					SelectionKey selKey = (SelectionKey) it.next();

					// Remove it from the list to indicate that it is being
					// processed
					it.remove();

					try {
						processSelectionKey(selKey);
					} catch (IOException e) {
						// Handle error with channel and unregister
						selKey.cancel();
					}
				}
				pause(refreshInterval);
			}

			logger.info(getName() + " has been shut down");
		}

		/**
		 * Pause polling for the given <code>refreshInterval</code>. Possible
		 * {@link InterruptedException} is logged with no further action.
		 * 
		 * @param refreshInterval
		 */
		protected void pause(long refreshInterval) {

			try {
				Thread.sleep(refreshInterval);
			} catch (InterruptedException e) {
				logger.debug("pausing thread " + super.getName()
						+ " interrupted");
			}
		}

	}


	/**
	 * Activate.
	 */
	public void activate() {
		start();
	}

	/**
	 * Deactivate.
	 */
	public void deactivate() {
		shutdown();
	}

	/**
	 * Interrupts the various threads immediately.
	 */
	public void interrupt() {
		if (this.selectorThread != null) {
			this.selectorThread.interrupt();
			logger.trace("{} has been interrupted.",
					this.selectorThread.getName());
		}
		if (this.parserThread != null) {
			this.parserThread.interrupt();
			logger.trace("{} has been interrupted.",
					this.parserThread.getName());
		}
		if (this.reconnectThread != null && isConnectionOriented()) {
			this.reconnectThread.interrupt();
			logger.trace("{} has been interrupted.",
					this.reconnectThread.getName());
		}

	}

	/**
	 * Takes care about starting the various threads. It creates a new
	 * Thread if no instance exists.
	 */
	protected void start() {

		if (this.selectorThread == null) {
			this.selectorThread = new SelectorThread(getName()
					+ " Selector Thread", getRefreshInterval());
			this.selectorThread.start();
		}

		if (this.parserThread == null) {
			this.parserThread = new ParserThread(getName() + " Parser Thread",
					getRefreshInterval());
			this.parserThread.start();
		}

		if (this.reconnectThread == null & isConnectionOriented()) {
			this.reconnectThread = new ReconnectThread(getName()
					+ " Reconnect Thread", getRefreshInterval(),
					getReconnectInterval());
			this.reconnectThread.start();
		}

	}

	/**
	 * Gracefully shuts down the refresh threads. It will shuts down
	 * after the current execution cycle.
	 */
	public void shutdown() {
		this.shutdown = true;
	}

	/**
	 * Creates all persistent connections for all the Items have connections defined
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void addAllPersistentConnections() throws IOException {
		for (P provider : providers) {
			ArrayList<String> items = (ArrayList<String>) provider
					.getItemNames();
			for (String anItem : items) {
				List<InetSocketAddress> socketAddresses = provider
						.getInetSocketAddresses(anItem);
				ListIterator<InetSocketAddress> listIterator = socketAddresses
						.listIterator();
				while (listIterator.hasNext()) {
					InetSocketAddress socketAddress = listIterator.next();
					addPersistentConnection(anItem, socketAddress);
				}
			}
		}
	}

	/**
	 * Creates the persistent connection to a socket address for a given reference (in casu, an Item) 
	 *
	 * @param reference the reference
	 * @param socketAddress the socket address
	 * @return the resulting Channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected C addPersistentConnection(String reference,
			InetSocketAddress socketAddress)  throws IOException {

		if (selector != null) {
			if (getActiveChannelCount(socketAddress) == 0) {
				// this seems to be a new Channel, so set it up
				C networkChannel= null;
				networkChannel = openChannel();
				networkChannel.configureBlocking(false);
				connectChannel(networkChannel, socketAddress);
				setKeepAliveChannel(networkChannel, true);


				ChannelTracker newTracker = new ChannelTracker();
				newTracker.referers.add(reference);
				newTracker.host = socketAddress.getHostName();
				newTracker.port = socketAddress.getPort();
				newTracker.timeOfSocketConnection = System.currentTimeMillis();

				channelTrackers.put(networkChannel,newTracker);

				selectorLock.lock();
				selector.wakeup();
				((SelectableChannel) networkChannel).register(selector, networkChannel.validOps());
				selectorLock.unlock();

				configurePersistentConnection(networkChannel);

				return networkChannel;
			} else {
				// this is an existing Channel, simply update it
				for(C networkChannel : channelTrackers.keySet() ) {
					if (channelTrackers.get(networkChannel).host == socketAddress.getHostName()
							&& channelTrackers.get(networkChannel).port == socketAddress.getPort()) {
						if (!channelTrackers.get(networkChannel).referers.contains(reference)) {
							channelTrackers.get(networkChannel).referers.add(reference);
						} 
						return networkChannel;
					}
				}
				return null;
			}
		} else
			return null;
	}

	/**
	 * Creates the persistent connection for a reference for the given host:port combination
	 *
	 * @param reference the reference
	 * @param host the host
	 * @param port the port
	 * @return the c
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected C addPersistentConnection(String reference, String host,
			int port) throws IOException {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		return addPersistentConnection(reference, socketAddress);
	}

	/**
	 * Removes the persistent connection.
	 *
	 * @param reference the reference
	 * @param networkChannel the network channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void removePersistentConnection(String reference,
			C networkChannel) throws IOException {
		if (selector != null) {
			if (channelTrackers.get(networkChannel).referers.size() == 1) {

				SelectionKey sKey = networkChannel
						.keyFor(selector);
				sKey.cancel();

				synchronized (channelTrackers.get(networkChannel)) {
					networkChannel.close();
				}

				channelTrackers.get(networkChannel).referers.remove(reference);
				channelTrackers.remove(networkChannel);

			} else {
				channelTrackers.get(networkChannel).referers.remove(reference);
			}

		}
	}

	/**
	 * Removes the persistent connection.
	 *
	 * @param reference the reference
	 * @param host the host
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void removePersistentConnection(String reference, String host,
			int port) throws IOException {
		if (selector != null) {
			for(C networkChannel : channelTrackers.keySet() ) {
				if (channelTrackers.get(networkChannel).host.equals(host)
						&& channelTrackers.get(networkChannel).port == port) {
					removePersistentConnection(reference,networkChannel);
				}
			}
		}
	}

	/**
	 * This function should be  implemented and used to "setup" the
	 * ASCII protocol after that the socket channel has been created. This because some ASCII
	 * protocols need to first emit a certain sequence of characters to
	 * configure or setup the communication channel with the remote end
	 **/
	abstract protected void configurePersistentConnection(C sChannel);

	/**
	 * Reconnect persistent connection.
	 *
	 * @param networkChannel the network channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void reconnectPersistentConnection(C networkChannel) throws IOException {
		InetSocketAddress socketAddress = null;

		if (getActiveChannelCount(networkChannel) > 0 && !isConnectedChannel(networkChannel)) {

			synchronized(channelTrackers.get(networkChannel)) {
				ChannelTracker aTracker = channelTrackers.get(networkChannel);
				socketAddress = new InetSocketAddress(aTracker.host, aTracker.port);

				channelTrackers.remove(networkChannel);
				networkChannel.close();

				C newNetworkChannel = openChannel();
				newNetworkChannel.configureBlocking(false);
				connectChannel(networkChannel,socketAddress);

				aTracker.timeOfSocketConnection = System.currentTimeMillis();

				setKeepAliveChannel(newNetworkChannel,true);

				selectorLock.lock();
				selector.wakeup();
				newNetworkChannel.register(selector, newNetworkChannel.validOps());
				selectorLock.unlock();

				channelTrackers.put(newNetworkChannel,aTracker);

			}

		}

	}

	/**
	 * Gets the active channel that makes use of the given socket address
	 *
	 * @param socketAddress the socket address
	 * @return the active channel
	 */
	protected C getActiveChannel(InetSocketAddress socketAddress) {
		for(C networkChannel : channelTrackers.keySet() ) {
			if (channelTrackers.get(networkChannel).host.equals(socketAddress.getHostName())
					&& channelTrackers.get(networkChannel).port == socketAddress.getPort()) {
				return networkChannel;
			}
		}
		return null;
	}

	/**
	 * Gets the active channel that is connected to the given host:port combination
	 *
	 * @param host the host
	 * @param port the port
	 * @return the active channel
	 */
	protected C getActiveChannel(String host, int port) {
		if(host != null) {
			for(C networkChannel : channelTrackers.keySet() ) {
				if (channelTrackers.get(networkChannel).host.equals(host) && channelTrackers.get(networkChannel).port == port) {
					return networkChannel;
				}
			}
		}
		return null;
	}

	/**
	 * Gets a list of all the active channels for a given reference (e.g. Item)
	 *
	 * @param reference the reference
	 * @return the active channels
	 */
	protected List<C> getActiveChannels(String reference) {
		List<C> activeSocketChannels = new ArrayList<C>();
		for(C networkChannel : channelTrackers.keySet() ) {
			if (channelTrackers.get(networkChannel).referers.contains(reference)) {
				activeSocketChannels.add(networkChannel);
			}
		}
		return activeSocketChannels;
	}

	/**
	 * Gets the number of referers that are tied to the channel that is connected to the given socket address
	 *
	 * @param socketAddress the socket address
	 * @return the active channel count
	 */
	protected int getActiveChannelCount(InetSocketAddress socketAddress) {
		for(C networkChannel : channelTrackers.keySet() ) {
			if (channelTrackers.get(networkChannel).host.equals(socketAddress.getHostName())
					&& channelTrackers.get(networkChannel).port == socketAddress.getPort()) {
				return channelTrackers.get(networkChannel).referers.size();
			}
		}
		return 0;
	}

	/**
	 * Gets number of referers that are tied to the given channel
	 *
	 * @param networkChannel the network channel
	 * @return the active channel count
	 */
	protected int getActiveChannelCount(C networkChannel) {
		return channelTrackers.get(networkChannel).referers.size();
	}

	/**
	 * Gets number of referers that are tied to the given host:port combination
	 *
	 * @param host the host
	 * @param port the port
	 * @return the active channel count
	 */
	protected int getActiveChannelCount(String host, int port) {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		return getActiveChannelCount(socketAddress);
	}

	/**
	 * Find the first matching {@link ChannelBindingProvider}
	 * according to <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected P findFirstMatchingBindingProvider(String itemName) {
		P firstMatchingProvider = null;
		for (P provider : this.providers) {
			List<InetSocketAddress> socketAddresses = provider
					.getInetSocketAddresses(itemName);
			if (socketAddresses != null && socketAddresses.size() > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		Collection<String> providerItems = provider.getItemNames();

		for (String anItem : providerItems) {
			bindingChanged(provider, anItem);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {

		// If a binding changed for a given item we will tear down existing
		// SocketChannels and/or put a new one in place

		List<C> socketChannels = getActiveChannels(itemName);
		List<InetSocketAddress> socketAddresses = ((P) provider)
				.getInetSocketAddresses(itemName);

		// disconnect the Channels that are no longer required
		ListIterator<C> listIterator = socketChannels
				.listIterator();
		while (listIterator.hasNext()) {
			C networkChannel = listIterator.next();
			if (!socketAddresses.contains(new InetSocketAddress(channelTrackers.get(networkChannel).host,channelTrackers.get(networkChannel).port)) && isConnectedChannel(networkChannel)) {
				// the new list of addresses kept by the Item does not contain
				// the existing socketchannel anymore, so remove it
				try {
					removePersistentConnection(itemName, networkChannel);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// now reverse the operation, and traverse the new addresses and set up
		// new Channels if required
		ListIterator<InetSocketAddress> addrIterator = socketAddresses
				.listIterator();
		while (addrIterator.hasNext()) {
			InetSocketAddress inetSocketAddress = addrIterator.next();
			boolean channelExists = false;
			ListIterator<C> subIterator = socketChannels
					.listIterator();
			while (subIterator.hasNext()) {
				C networkChannel = subIterator.next();
				if (channelTrackers.get(networkChannel).host.equals( inetSocketAddress.getHostName())
						&& channelTrackers.get(networkChannel).port == inetSocketAddress.getPort()) {
					channelExists = true;
					break;
				}
			}
			if (!channelExists) {
				try {
					addPersistentConnection(itemName,inetSocketAddress);					
				}
				catch (IOException e) {
					logger.debug("Could not add a persitent connection for item {} to address {}",itemName,inetSocketAddress.toString());
				}
			}
		}

	}


	/**
	 * {@inheritDoc}
	 */
	protected void internalReceiveCommand(String itemName, Command command) {

		P provider = findFirstMatchingBindingProvider(itemName);
		C sChannel = null;

		if (provider == null) {
			logger.warn(
					"cannot find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}
		
		if(command != null){

			List<Command> commands = provider.getAllCommands(itemName);

			for(Command someCommand : commands) {
				
				sChannel = getActiveChannel(provider.getHost(itemName, someCommand),
						provider.getPort(itemName, someCommand));
				
				if (sChannel != null) {
					boolean result = internalReceiveChanneledCommand(itemName, someCommand, sChannel,command.toString());
					
					if(result) {

						List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,someCommand);
						State newState = createStateFromString(stateTypeList,command.toString());
						
						if(newState != null) {
							eventPublisher.postUpdate(itemName, newState);							        						
						}
						
					}
					
				} else {
					logger.error(
							"there is no active channel for [itemName={}, command={}]",
							itemName, command);
				}
			}               
		}
	}
	

	/**
	 * The actual implementation for receiving a command from the openhab runtime should go here
	 *
	 * @param itemName the item name
	 * @param command the command
	 * @param networkChannel the network channel
	 * @param commandAsString the command as String
	 * @return true, if successful
	 */
	abstract protected boolean internalReceiveChanneledCommand(String itemName,
			Command command, C networkChannel, String commandAsString);
	
	/**
	 * Returns a {@link State} which is inherited from provide list of DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>stateTypeList</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param stateTypeList - a list of state types that we should parse against
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>stateTypeList</code> is <code>null</code> 
	 */
	protected State createStateFromString(List<Class<? extends State>> stateTypeList, String transformedResponse) {

		if (stateTypeList != null) {
			return TypeParser.parseState(stateTypeList, transformedResponse);
		}
		else {
			return StringType.valueOf(transformedResponse);
		}
	}
	
	/**
	 * Write buffer, asynchronously
	 *
	 * @param networkChannel the network channel
	 * @param byteBuffer the byte buffer
	 * @param timeOut the time out
	 * @return the byte buffer
	 */
	protected ByteBuffer writeBuffer(C networkChannel,
			ByteBuffer byteBuffer, long timeOut) {
		return writeBuffer(networkChannel, byteBuffer, false,timeOut);
	}

	/**
	 * Write buffer to the given Channel
	 *
	 * @param networkChannel the network channel
	 * @param byteBuffer the byte buffer
	 * @param isBlockingWriteRead the is blocking write read
	 * @param timeOut the time out
	 * @return the byte buffer, in case the write buffer is executed in a synchronous way, null otherwise
	 */
	protected ByteBuffer writeBuffer(C networkChannel,
			ByteBuffer byteBuffer, boolean isBlockingWriteRead, long timeOut) {
		if (selector != null && getActiveChannelCount(networkChannel) > 0) {

			byteBuffer.flip();
			// create a new buffer so that we are sure that the buffer is
			// modified after this call to writebuffer()

			BufferElement bufferElement = new BufferElement();
			bufferElement.byteBuffer = ByteBuffer.allocate(byteBuffer.limit());
			bufferElement.byteBuffer.put(byteBuffer);
			bufferElement.byteBuffer.rewind();
			bufferElement.isBlocking = isBlockingWriteRead;

			if (isBlockingWriteRead) {

				// only one thread can do a blocking call at the same time
				channelTrackers.get(networkChannel).blockingLock.lock();

				synchronized (channelTrackers.get(networkChannel)) {
					channelTrackers.get(networkChannel).inBlockingWriteRead = true;
				}
			}

			channelTrackers.get(networkChannel).writeQueue.add(bufferElement);

			if (isBlockingWriteRead) {

				long currentElapsedTimeMillis = System.currentTimeMillis();

				// wait for the response until we reach the pre-defined time out
				while (channelTrackers.get(networkChannel).blockingReadBuffer == null && (System.currentTimeMillis()-currentElapsedTimeMillis)<timeOut) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						logger.warn("Exception occured while waiting waiting during a blocking buffer write");
					}
				}


				ByteBuffer responseBuffer = channelTrackers.get(networkChannel).blockingReadBuffer;
				synchronized (channelTrackers.get(networkChannel)) {
					channelTrackers.get(networkChannel).blockingReadBuffer = null;
				}
				
				channelTrackers.get(networkChannel).blockingLock.unlock();
				
				return responseBuffer;
			}
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * Parses the buffer received from the Channel
	 *
	 * @param networkChannel the network channel
	 * @param byteBuffer the byte buffer
	 */
	protected void parseChanneledBuffer(C networkChannel, ByteBuffer byteBuffer) {
		if(networkChannel != null && byteBuffer != null) {
			
			logger.debug("Received "+new String(byteBuffer.array())+" from "+networkChannel.toString());
			
			String host = channelTrackers.get(networkChannel).host;
			int port = channelTrackers.get(networkChannel).port;
						
			// get the Items that do match the ip:port of the given channel
			Collection<String> qualifiedItems = new ArrayList<String>();
			for (P provider : this.providers) {
				qualifiedItems.addAll(provider.getItemNames(host, port));
			}
			
			// now, parse the whole lot
			if(qualifiedItems.size() > 0) {
				parseBuffer(qualifiedItems,byteBuffer);
			}
			
		}
		
	}
	

	/**
	 * 
	 * Callback that will be called when data is received on a given channel.
	 * This method should deal with the actual details of the protocol being implemented
	 */
	abstract protected void parseBuffer(Collection<String> qualifiedItems,
			ByteBuffer byteBuffer);

	/**
	 * 
	 * Series of abstract functions that encapsulate access to DatagramChannel and SocketChannel methods
	 * (required as java.nio.channels.spi.AbstractSelectableChannel unfortunately is not defining the 
	 * methods common to DC and SC)
	 * 
	 */

	abstract protected void connectChannel(C channel, SocketAddress address) throws IOException;

	/**
	 * Open channel.
	 *
	 * @return the c
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	abstract protected C openChannel() throws IOException;

	/**
	 * Checks if is connected channel.
	 *
	 * @param channel the channel
	 * @return true, if is connected channel
	 */
	abstract protected boolean isConnectedChannel(C channel);

	/**
	 * Checks if is connection pending channel.
	 *
	 * @param channel the channel
	 * @return true, if is connection pending channel
	 */
	abstract protected boolean isConnectionPendingChannel(C channel);

	/**
	 * Finish connect channel.
	 *
	 * @param channel the channel
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	abstract protected boolean finishConnectChannel(C channel) throws IOException;

	/**
	 * Read channel.
	 *
	 * @param channel the channel
	 * @param byteBuffer the byte buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	abstract protected int readChannel(C channel, ByteBuffer byteBuffer) throws IOException;

	/**
	 * Write channel.
	 *
	 * @param channel the channel
	 * @param byteBuffer the byte buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	abstract protected int writeChannel(C channel, ByteBuffer byteBuffer) throws IOException;

	/**
	 * Sets the keep alive channel.
	 *
	 * @param channel the channel
	 * @param aBoolean the a boolean
	 * @throws SocketException the socket exception
	 */
	abstract protected void setKeepAliveChannel(C channel, boolean aBoolean) throws SocketException;

	public void setEventPublisher(EventPublisher eventPublisher) {
		AbstractChannelEventSubscriberBinding.eventPublisher = eventPublisher;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		AbstractChannelEventSubscriberBinding.itemRegistry = itemRegistry;
	}

	/**
	 * Unset event publisher.
	 *
	 * @param eventPublisher the event publisher
	 */
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		AbstractChannelEventSubscriberBinding.eventPublisher = null;
	}

	/**
	 * Unset item registry.
	 *
	 * @param itemRegistry the item registry
	 */
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		AbstractChannelEventSubscriberBinding.itemRegistry = null;
	}

	/**
	 * Returns the basename of the Threads.
	 * 
	 * @return the basename
	 */
	protected abstract String getName();

	/**
	 * Returns the refresh interval to be used by the Threads between to calls
	 * of the execute method.
	 * 
	 * @return the refresh interval
	 */
	protected abstract long getRefreshInterval();

	/**
	 * Returns the reconnect interval to be used by the Threads after which a
	 * channel connection will be reset. Expressed in number of Hours
	 * 
	 * @return the refresh interval
	 */
	protected abstract int getReconnectInterval();

	/**
	 * @return <code>true</code> if this service is configured properly which
	 *         means that all necessary data is available
	 */
	public abstract boolean isProperlyConfigured();

	/**
	 * @return <code>true</code> if this service is connection oriented
	 */
	public abstract boolean isConnectionOriented();


}
