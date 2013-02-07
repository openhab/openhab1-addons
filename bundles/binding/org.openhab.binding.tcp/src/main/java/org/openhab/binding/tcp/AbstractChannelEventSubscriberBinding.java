/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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

import static org.quartz.DateBuilder.evenHourDateAfterNow;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ACESB is the base for all network based connectivity and communication. It requires a SelectableChannel type
 * parameter which is either connection-oriented (SocketChannel) or connection-less (DatagramChannel). It also
 * requires a ChannelBindingProvider based binding provider. 
 *  
 * @author Karel Goderis
 * @since 1.1.0
 * 
 */
public abstract class AbstractChannelEventSubscriberBinding<C extends AbstractSelectableChannel, P extends ChannelBindingProvider>
extends AbstractBinding<P> {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractChannelEventSubscriberBinding.class);

	static protected EventPublisher eventPublisher;
	static protected ItemRegistry itemRegistry;

	ChannelTracker channelTracker = new ChannelTracker();
	
	/**
	 * ChannelTracker is a data structure to manage MuxChannel and the references that use them. When the last reference using a MuxChannel is removed, only then a MuxChannel will be finally closed
	 * 
	 * @author Karel Goderis
	 * @since 1.2.0
	 */
	protected class ChannelTracker extends ArrayList<MuxChannel> {

		private static final long serialVersionUID = -4573615176463415520L;

		public synchronized ArrayList<MuxChannel> getAll(String anItem) {
			return null;
		}

		/**
		 * Get the channel that is associated with the given host and port number
		 * 
		 * @param host
		 * @param port
		 * @return
		 */
		public synchronized MuxChannel get(String host, Integer port) {
			for(MuxChannel aChannel : this) {
				if (aChannel.host.equals(host) && aChannel.port == port) {
					return aChannel;
				}
			}
			return null;
		}

		/**
		 * Add a new MuxChannel to be tracked
		 * 
		 * @param binding that is adding the MuxChannel
		 * @param host
		 * @param port
		 * @param reference that is "using" the MuxChannel
		 * @return
		 */
		public synchronized MuxChannel add(AbstractChannelEventSubscriberBinding<C,P> binding, String host, Integer port, String reference) {
			MuxChannel existingChannel = get(host,port);
			if(existingChannel == null) {
				existingChannel =  createMuxInstance(host,port);
				existingChannel.binding = binding;
				existingChannel.add(reference);
				this.add(existingChannel);
				try {
					existingChannel.connect();
				} catch (IOException e) {
					logger.error("An exception occured while connecting a channel: {}",e.getMessage());
				}
			}  else {
				logger.info("{} channel already exists",existingChannel);
			}
			return existingChannel;
		}

		/**
		 * Remove the reference from all tracked MuxChannels. If the reference happens to be the last one using the MuxChannel, then the MuxChanne is closed down
		 * 
		 * @param reference
		 * @throws IOException
		 */
		public synchronized void remove(String reference) throws IOException {
			for(MuxChannel m : this) {
				if(m.providesFor(reference)) {
					m.remove(reference);
				}
				if(!m.provides()) {
					m.close();
					remove(m);
				}
			}
		}

		/**
		 * Remove the reference from the MuxChannels associated with given host and port number
		 * 
		 * @param host
		 * @param port
		 * @param reference
		 * @throws IOException
		 */
		public synchronized void remove(String host, Integer port, String reference) throws IOException {
			MuxChannel m = this.get(host,port);
			if (m != null) {
				if(m.providesFor(reference)) {
					m.remove(reference);
				}
				if(!m.provides()) {
					m.close();
					remove(m);
				}
			}
		}
	}

	/**
	 * MuxChannel is an encapsulating class in the sense that it encapsulates the parameterized AbstractSelectableChannel, adding additional functionality sunch as blocking read/write operations, tracking
	 * tracking of references that uses the Channel, IO Exception counting to help close down faulty channels,...
	 * 
	 * This class is abstract. Any (final) implementation of AbstractChannelEventSubscriberBinding has to implement this class, taking into account the real nature of the AbstractSelectableChannel used to
	 * parameterize the class
	 * 
	 * @author Karel Goderis
	 * @since 1.2.0
	 */
	protected abstract class MuxChannel{

		protected C channel ;
		protected AbstractChannelEventSubscriberBinding<C,P> binding;
		protected Set<String> references = Collections.synchronizedSet(new HashSet<String>());

		protected String host = null;
		protected int port = 0;

		protected int IOExceptionCount = 0;
		private int MAX_EXCEPTION_COUNT = 10;
		
		protected boolean isConnecting = false;
		protected boolean inBlockingWriteRead = false;
		protected ByteBuffer blockingReadBuffer = null;
		protected ReentrantLock blockingLock = new ReentrantLock();

		protected int maxBufferSize = 1024;
		protected Selector writeSelector;
		protected Selector readSelector;
		protected Selector connectSelector;

		public MuxChannel(String host, int port){
			this.host = host;
			this.port = port;
			channel = open();
			configure();
			scheduleReconnectJob();
		}
		
		protected abstract C open();

		public boolean isOpen() {
			return channel.isOpen();
		}

		public abstract boolean connect(SocketAddress address) throws IOException;
		
		/**
		 * Connect the channel to host:port, thereby scheduling a Quartz job to take car of "finishing" the connection attempt.
		 * 
		 * @return
		 * @throws IOException
		 */
		public synchronized boolean connect() throws IOException {
			if(!isConnecting) {
				if(!isFaulty()) {
					if(host!=null && port != 0) {
						configure();
						isConnecting = true;
						scheduleFinishConnectJob();
						logger.info("Setting up a connection to {}",(new InetSocketAddress(host, port).toString()));
						connect(new InetSocketAddress(host, port));
						return true;
					} else {
						return false;
					} 
				} else {
					logger.error("The channel servicing {} has experienced too many errors",(new InetSocketAddress(host, port).toString()));
					return false;
				} 
			} else {
				logger.error("Already trying to setup a connection to {}",(new InetSocketAddress(host, port).toString()));
				return false;
			}
		}

		/**
		 * Attempt to connect the channel until it is faulty
		 * 
		 * @return
		 * @throws IOException
		 */
		public boolean connectUntilFaulty() throws IOException {

			while(!isConnected() && !isFaulty()) {
				if(!isConnecting) {
					if(!isConnectionPending() || !isOpen()) {
						try {
							reconnect();
						} catch (IOException e) {
							logger.error("An exception occured while reconnecting a channel: {}",e.getMessage());
						}
					}
				}
			}

			if(isFaulty()) {
				logger.error("The channel servicing {} has experienced too many errors",(new InetSocketAddress(host, port).toString()));
				return false;
			} else {
				if(this.isConnectionOriented()) {
					IOExceptionCount = 0;
				}
				return true;
			}
		}
		
		public abstract boolean isConnected();

		public abstract boolean isConnectionPending();
		
		/**
		 * @return <code>true</code> if this service is connection oriented
		 */
		public abstract boolean isConnectionOriented();

		public abstract boolean finishConnect() throws IOException;

		/**
		 * Reconnect a channel properly, e.g. close it down, get a new channel handle, and connect it
		 * 
		 * @throws IOException
		 */
		public synchronized void reconnect() throws IOException {
			channel.close();
			channel = open();
			boolean result = connect();
			if(result == false) {
				channel.close();
			}
		}

		/**
		 * Close down a Channel and cancel any Selector keys
		 * 
		 * @throws IOException
		 */
		public synchronized void close() throws IOException {
			SelectionKey sKey = channel.keyFor(writeSelector);
			sKey.cancel();		

			sKey = channel.keyFor(readSelector);
			sKey.cancel();

			sKey = channel.keyFor(connectSelector);
			sKey.cancel();

			channel.close();
		}

		/**
		 * Configure a Channel and register the connect, read and write selectors
		 * 
		 */
		protected synchronized void configure() {

			if(channel != null) {

				try {
					channel.configureBlocking(false);
					setKeepAlive(true);
				} catch (IOException e2) {
					logger.error("An exception occured while configuring a channel: {}",e2.getMessage());
				}

				//register the selectors
				try {
					connectSelector = Selector.open();
				} catch (IOException e) {
					logger.error("An exception occured while opening a channel: {}",e.getMessage());
				}
				synchronized(connectSelector) {
					if(isConnectionOriented()) {
						connectSelector.wakeup();
						try {
							channel.register(connectSelector, SelectionKey.OP_CONNECT);
						} catch (ClosedChannelException e1) {
							logger.error("An exception occured while registering a channel: {}",e1.getMessage());
						} catch (IllegalArgumentException e2) {

						}
					}
				}

				try {
					writeSelector = Selector.open();
				} catch (IOException e) {
					logger.error("An exception occured while opening a selectir: {}",e.getMessage());
				}
				synchronized(writeSelector) {
					writeSelector.wakeup();
					try {
						channel.register(writeSelector, SelectionKey.OP_WRITE);
					} catch (ClosedChannelException e1) {
						logger.error("An exception occured while registering a selector: {}",e1.getMessage());
					}
				}

				try {
					readSelector = Selector.open();
				} catch (IOException e) {
					logger.error("An exception occured while connecting a selector: {}",e.getMessage());
				}
				synchronized(readSelector) {
					readSelector.wakeup();
					try {
						channel.register(readSelector, SelectionKey.OP_READ);
					} catch (ClosedChannelException e1) {
						logger.error("An exception occured while registering a selector: {}",e1.getMessage());
					}
				}
			}
		}

		public boolean isFaulty() {
			return (IOExceptionCount > this.MAX_EXCEPTION_COUNT);
		}
		
		public void logFault() {
			IOExceptionCount = IOExceptionCount + 1;
		}

		protected abstract int read(ByteBuffer buffer) throws IOException;

		/**
		 * Write a buffer to the Channel
		 * 
		 * @param buffer to be written
		 * @param isBlockingWriteRead - when set to true, the buffer will be written and the method will wait for/read a reply from the remote host, until timeOut happens
		 * @param timeOut - time out in milleseconds to wait for a reply from the remote host in case of a blocking read/write operation
		 * @return
		 * @throws Exception
		 */
		public ByteBuffer writeBuffer(ByteBuffer buffer, boolean isBlockingWriteRead, long timeOut) throws Exception {
			if(isFaulty()) {
				logger.info("Nice try. The channel servicing the connection to {} is faulty, I do not service it anymore. Pls check your network setup",(new InetSocketAddress(host, port).toString()));
				IOException TooManyExceptions = new IOException("Faulty Channel");
				throw TooManyExceptions;
			} else {
				if(buffer != null) {
					if(!isConnected()) {
						connectUntilFaulty();
					}

					if(!isFaulty()) {
						lock();

						if(isBlockingWriteRead) {
							synchronized (this) {
								inBlockingWriteRead = true;
							}
						}

						Scheduler scheduler = null;
						try {
							scheduler = StdSchedulerFactory.getDefaultScheduler();
						} catch (SchedulerException e1) {
							logger.error("An exception occured while getting the Quartz scheduler: {}",e1.getMessage());
						}

						JobDataMap map = new JobDataMap();
						map.put("MuxChannel", this);
						map.put("Buffer", buffer);

						JobDetail job = newJob(WriteJob.class)
								.withIdentity(Integer.toHexString(hashCode()) +"-Write-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
								.usingJobData(map)
								.build();

						Trigger trigger = newTrigger()
								.withIdentity(Integer.toHexString(hashCode()) +"-Write-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
								.startNow()         
								.build();

						try {
							scheduler.scheduleJob(job, trigger);
						} catch (SchedulerException e) {
							logger.error("Error scheduling a write job with the Quartz Scheduler : {}",e.getMessage());
						}

						if(isBlockingWriteRead) {

							long currentElapsedTimeMillis = System.currentTimeMillis();

							while(blockingReadBuffer != null && (System.currentTimeMillis()-currentElapsedTimeMillis)<timeOut ) {
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									logger.warn("Exception occured while waiting waiting during a blocking buffer write");
								}
							}

							ByteBuffer responseBuffer = null;
							synchronized(this) {
								responseBuffer = blockingReadBuffer;
								blockingReadBuffer = null;
								inBlockingWriteRead = false;
							}

							unlock();
							return responseBuffer;

						} else {
							unlock();
							return buffer;
						}
					} else {
						IOException TooManyExceptions = new IOException("Faulty Channel");
						throw TooManyExceptions;
					}
				} else {
					IllegalArgumentException anException = new IllegalArgumentException("Buffer is null");
					throw anException;
				}	
			}

		}

		protected abstract int write(ByteBuffer buffer) throws IOException;

		public abstract void setKeepAlive(boolean setting) throws SocketException;	

		public boolean providesFor(String reference) {
			return references.contains(reference);
		}

		public void add(String anItem) {
			references.add(anItem);
		}

		public void remove(String anItem) {
			references.remove(anItem);
		}

		public boolean provides() {
			return !references.isEmpty();
		}

		public void lock() {
			blockingLock.lock();
		}

		public void unlock() {
			blockingLock.unlock();
		}
		
		/**
		 * Schedule a Quartz job to handle the "finishConnect" of the underlying channel
		 * 
		 */
		private void scheduleFinishConnectJob() {
			Scheduler scheduler = null;
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e1) {
				logger.error("An exception occured while getting the Quartz scheduler: {}",e1.getMessage());
			}

			JobDataMap map = new JobDataMap();
			map.put("MuxChannel", this);

			JobDetail job = newJob(FinishConnectJob.class)
					.withIdentity(Integer.toHexString(hashCode()) +"-FinishConnect-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.usingJobData(map)
					.build();

			Trigger trigger = newTrigger()
					.withIdentity(Integer.toHexString(hashCode()) +"-FinishConnect-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.startNow()             
					.build();
			
			try {
				scheduler.getListenerManager().addJobListener(new FinishConnectJobListener(), KeyMatcher.keyEquals(job.getKey()));
			} catch (SchedulerException e1) {
				logger.error("An exception occured while getting a Quartz Listener Manager: {}",e1.getMessage());
			}

			try {
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				logger.error("Error scheduling a finish job with the Quartz Scheduler : {}",e.getMessage());
			}
		}

		/**
		 * Schedule a Quartz job that will reconnect the underlying channel every getReconnectInterval() hours. That way any "stalled" connection will be gracefully reset
		 * 
		 */
		private void scheduleReconnectJob() {

			Scheduler scheduler = null;
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e1) {
				logger.error("An exception occured while getting the Quartz scheduler: {}",e1.getMessage());
			}

			JobDataMap map = new JobDataMap();
			map.put("MuxChannel", this);

			JobDetail job = newJob(ReconnectJob.class)
					.withIdentity(Integer.toHexString(hashCode()) +"-Reconnect-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.usingJobData(map)
					.build();

			Trigger trigger = newTrigger()
					.withIdentity(Integer.toHexString(hashCode()) +"-Reconnect-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.withSchedule(simpleSchedule().withIntervalInHours(getReconnectInterval()) 
							.repeatForever())
							.startAt(evenHourDateAfterNow())
							.build();

			try {
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				logger.error("Error scheduling a reconnect job with the Quartz Scheduler");
			}
		}
	}

	/**
	 * 
	 * Abstract method that will return an instance of MuxChannel.
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	abstract MuxChannel createMuxInstance(String host, int port);

	/**
	 * Instantiates a new abstract channel event subscriber binding.
	 */
	public AbstractChannelEventSubscriberBinding() {
	}

	/**
	 * Activate.
	 */
	public void activate() {
	}

	/**
	 * Deactivate.
	 */
	public void deactivate() {
		//TODO : remove all jobs from the Quartz scheduler
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

		List<InetSocketAddress> socketAddresses = ((P) provider).getInetSocketAddresses(itemName);

		for(MuxChannel aChannel : channelTracker) {
			if (!socketAddresses.contains(new InetSocketAddress(aChannel.host,aChannel.port))) {
				try {
					channelTracker.remove(aChannel.host, aChannel.port, itemName);
				} catch (IOException e) {
					logger.error("An exception occured while remvoing a channel: {}",e.getMessage());
				}
			}
		}

		for (SocketAddress anAddress : socketAddresses) {
			channelTracker.add(this,((InetSocketAddress)anAddress).getAddress().getHostAddress(), ((InetSocketAddress)anAddress).getPort() , itemName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		P provider = findFirstMatchingBindingProvider(itemName);
		MuxChannel channel = null;

		if (provider == null) {
			logger.warn(
					"cannot find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}

		if(command != null){
			List<Command> commands = provider.getQualifiedCommands(itemName,command);
			
			for(Command someCommand : commands) {
				InetSocketAddress tempAddress = new InetSocketAddress(provider.getHost(itemName, someCommand),provider.getPort(itemName, someCommand));
				channel = channelTracker.get(tempAddress.getAddress().getHostAddress(), tempAddress.getPort());
				
				if (channel != null) {
					boolean result = internalReceiveChanneledCommand(itemName, someCommand, channel,command.toString());
					
					if(result) {
						List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(itemName,someCommand);
						State newState = createStateFromString(stateTypeList,command.toString());

						if(newState != null) {
							eventPublisher.postUpdate(itemName, newState);							        						
						}
					}

				} else {
					logger.error(
							"there is no channel that services [itemName={}, command={}]",
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
	 * @param channel the network channel
	 * @param commandAsString the command as String
	 * @return true, if successful
	 */
	abstract protected boolean internalReceiveChanneledCommand(String itemName,
			Command command, MuxChannel channel, String commandAsString);

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
	 * Parses the buffer received from the Channel
	 *
	 * @param networkChannel the network channel
	 * @param byteBuffer the byte buffer
	 */
	protected void parseChanneledBuffer(MuxChannel channel, ByteBuffer byteBuffer) {
		if(channel != null && byteBuffer != null && byteBuffer.limit() != 0) {			
			logger.debug("Received "+new String(byteBuffer.array())+" from "+channel.toString());

			// get the Items that do match the ip:port of the given channel
			Collection<String> qualifiedItems = new ArrayList<String>();
			for (P provider : this.providers) {
				qualifiedItems.addAll(provider.getItemNames(channel.host, channel.port));
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
	 * This function should be  implemented and used to "setup" the
	 * ASCII protocol after that the socket channel has been created. This because some ASCII
	 * protocols need to first emit a certain sequence of characters to
	 * configure or setup the communication channel with the remote end
	 **/
	abstract protected void configureChannel(MuxChannel channel);
	
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
	 * Quartz Job to actually write a buffer to the underlying channel
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class WriteJob implements Job {

		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");
			ByteBuffer buffer = (ByteBuffer) dataMap.get("Buffer");

			boolean jobDone = false;

			try {
				if(!muxChannel.connectUntilFaulty()) {
					jobDone = true;
				}
			} catch (IOException e2) {
				logger.error("An exception occured while connecting a channel: {}",e2.getMessage());
			}

			while(!jobDone) {
				synchronized(muxChannel.writeSelector) {
					try {
						// Wait for an event
						muxChannel.writeSelector.selectNow();
					} catch (IOException e) {
						// Handle error with selector
					}

					// Get list of selection keys with pending events
					Iterator<SelectionKey> it = muxChannel.writeSelector.selectedKeys().iterator();

					// Process each key at a time
					while (it.hasNext()) {
						SelectionKey selKey = (SelectionKey) it.next();
						it.remove();

						if (selKey.isValid() && selKey.isWritable()) {

							buffer.rewind();
							try {
								muxChannel.write(buffer);
							} catch (NotYetConnectedException e) {
								logger.warn("{} is apparently not yet connected",muxChannel);
							} catch (IOException e) {
								// If some other I/O error occurs
								logger.warn("{} has encountered an unknown IO Exception: {}",muxChannel,e.getMessage());

								muxChannel.logFault();

								// let's try to reconnect
								try {
									if(!muxChannel.isFaulty()) {
										muxChannel.reconnect();
									}
								} catch (IOException e1) {
									logger.error("An exception occured while connecting a channel: {}",e1.getMessage());
								}
							}
							jobDone = true;
						}
					}
				}
			}
		}
	}

	/**
	 * Quartz Job to reconnect a channel
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class ReconnectJob implements Job {
		
		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");

			try {
				muxChannel.reconnect();
			} catch (IOException e) {
				logger.error("An exception occured while reconnecting a channel: {}",e.getMessage());
			}
		}
	}

	/**
	 * Quartz Job to handle the "finish connect" of a channel
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class FinishConnectJob implements Job {
		
		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");

			boolean jobDone = false;

			while(!jobDone) {

				if(!muxChannel.isOpen()) {
					logger.warn("{} apparently is not open.",muxChannel);	
					jobDone = true;
					muxChannel.isConnecting = false;
					break;
				}
				
				if(!muxChannel.isConnectionOriented()) {
					jobDone = true;
					muxChannel.isConnecting = false;
					break;
				}				

				synchronized(muxChannel.connectSelector) {

					try {
						// Wait for an event
						muxChannel.connectSelector.selectNow();
					} catch (IOException e) {
						// Handle error with selector
					}

					// Get list of selection keys with pending events
					Iterator<SelectionKey> it = muxChannel.connectSelector.selectedKeys().iterator();

					// Process each key at a time
					while (it.hasNext()) {
						SelectionKey selKey = (SelectionKey) it.next();
						it.remove();

						if (selKey.isValid() && selKey.isConnectable()) {

							if(muxChannel.isConnectionPending()) {
								try {
									muxChannel.finishConnect();
								} catch (NoConnectionPendingException e) {
									// this channel is not connected and a connection operation
									// has not been initiated
									logger.warn("{} has no conection pending",muxChannel);
								} catch (ClosedChannelException e) {
									// If some other I/O error occurs
									logger.warn("{} apparently is closed.",muxChannel);	
								} catch (IOException e) {
									// If some other I/O error occurs
									logger.warn("{} has encountered an unknown IO Exception: {}",muxChannel,e.getMessage());

									muxChannel.logFault();
								}

								muxChannel.isConnecting = false;
							}
							jobDone = true;
						}
					}
				}
			}
		}
	}

	/**
	 * Quartz Job to actually read data from a channel
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class ReadJob implements Job {

		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			// get the reference to the Stick
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");

			boolean jobDone = false;

			if(!muxChannel.isFaulty()) {
				try {
					if(!muxChannel.connectUntilFaulty()) {
						jobDone = true;
					}
				} catch (IOException e2) {
					logger.error("An exception occured while connecting a channel: {}",e2.getMessage());
				}

				while(!jobDone) {

					synchronized(muxChannel.readSelector) {
						try {
							// Wait for an event
							muxChannel.readSelector.selectNow();
						} catch (IOException e) {
							// Handle error with selector
						}

						// Get list of selection keys with pending events
						Iterator<SelectionKey> it = muxChannel.readSelector.selectedKeys().iterator();

						// Process each key at a time
						while (it.hasNext()) {
							SelectionKey selKey = (SelectionKey) it.next();
							it.remove();

							if (selKey.isValid() && selKey.isReadable()) {

								ByteBuffer readBuffer = ByteBuffer.allocate(muxChannel.maxBufferSize);

								int numberBytesRead = 0;
								try {
									//TODO: Additional code to split readBuffer in multiple parts, in case the data send by the remote end is not correctly fragemented. Could be handed of to implementation class if for example, the buffer needs to be split based on a special character like line feed or carriage return
									numberBytesRead = muxChannel.read(readBuffer);
								} catch (NotYetConnectedException e) {
									logger.warn("{} is apparently not yet connected",muxChannel);
								} catch (PortUnreachableException e) {
									logger.warn("An ICMP Port Unreachable message has been received on the connected channel {}",muxChannel);
									muxChannel.logFault();

									try {
										if(!muxChannel.isFaulty()) {
											muxChannel.reconnect();
										} else {
											jobDone = true;
										}
									} catch (IOException e1) {
										logger.error("An exception occured while reconnecting a channel: {}",e1.getMessage());
									}

								} catch (IOException e) {
									// If some other I/O error occurs
									logger.warn("{} has encountered an unknown IO Exception: {}",muxChannel,e.getMessage());
									// let's try to reconnect

									muxChannel.logFault();

									try {
										if(!muxChannel.isFaulty()) {
											muxChannel.reconnect();
										} else {
											jobDone = true;
										}
									} catch (IOException e1) {
										logger.error("An exception occured while reconnecting a channel: {}",e1.getMessage());
									}
								}


								if (numberBytesRead == -1) {
									// seems to be disconnected, try to reconnect
									if(!muxChannel.isConnected()) {
										try {
											if(!muxChannel.isFaulty()) {
												muxChannel.reconnect();
											} else {
												jobDone = true;
											}
										} catch (IOException e1) {
											logger.error("An exception occured while reconnecting a channel: {}",e1.getMessage());
										}
									}
								} else {
									if (muxChannel.inBlockingWriteRead == true) {

										readBuffer.flip();
										synchronized (muxChannel) {
											muxChannel.blockingReadBuffer = readBuffer;
											muxChannel.inBlockingWriteRead = false;
										}
									} else {								
										readBuffer.flip();
										muxChannel.binding.parseChanneledBuffer(muxChannel,readBuffer);
									}
									jobDone = true;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Quartz Job Listener that will schedule a new ReadJob when the previous one is "done"
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class ReadJobListener implements JobListener {

		public ReadJobListener() {
		}

		public void jobToBeExecuted(JobExecutionContext context) {
			// do something with the event
		}

		public void jobWasExecuted(JobExecutionContext context,
				JobExecutionException jobException) {

			Scheduler scheduler = null;
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e1) {
				logger.error("An exception occured while getting the Quartz scheduler: {}",e1.getMessage());
			}

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");
			
			JobDataMap map = new JobDataMap();
			map.put("MuxChannel", muxChannel);
			
			JobDetail job = newJob(ReadJob.class)
					.withIdentity(Integer.toHexString(hashCode()) +"-Read-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.usingJobData(map)
					.build();

			Trigger trigger = newTrigger()
					.withIdentity(Integer.toHexString(hashCode()) +"-Read-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
					.startNow()         
					.build();
			
			try {
				scheduler.getListenerManager().addJobListener(new ReadJobListener(), KeyMatcher.keyEquals(job.getKey()));
			} catch (SchedulerException e1) {
				logger.error("An exception occured while getting a Quartz Listener Manager: {}",e1.getMessage());
			}

			try {
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				logger.error("Error scheduling a read job with the Quartz Scheduler");
			}		   
		}

		public void jobExecutionVetoed(JobExecutionContext context) {
			// do something with the event
		}

		@Override
		public String getName() {
			return "ReadJobListener";
		}
	}

	/**
	 * Quartz Job Listener that will schedule the first ReadJob when the FinisConnect job has finished
	 * 
	 * @author Karel Goderis
	 * @since  1.2.0
	 *
	 */
	public static class FinishConnectJobListener implements JobListener {

		public FinishConnectJobListener() {
		}

		public void jobToBeExecuted(JobExecutionContext context) {
			// do something with the event
		}

		public void jobWasExecuted(JobExecutionContext context,
				JobExecutionException jobException) {

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			@SuppressWarnings("rawtypes")
			AbstractChannelEventSubscriberBinding.MuxChannel muxChannel = (AbstractChannelEventSubscriberBinding.MuxChannel) dataMap.get("MuxChannel");

			if(muxChannel.isConnected()) {

				Scheduler scheduler = null;
				try {
					scheduler = StdSchedulerFactory.getDefaultScheduler();
				} catch (SchedulerException e1) {
					logger.error("An exception occured while getting the Quartz scheduler: {}",e1.getMessage());
				}

				JobDataMap map = new JobDataMap();
				map.put("MuxChannel", muxChannel);

				JobDetail job = newJob(ReadJob.class)
						.withIdentity(Integer.toHexString(hashCode()) +"-Read-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
						.usingJobData(map)
						.build();

				Trigger trigger = newTrigger()
						.withIdentity(Integer.toHexString(hashCode()) +"-Read-"+Long.toString(System.currentTimeMillis()), "AbstractChannelEventSubscriberBinding")
						.startNow()         
						.build();
				
				try {
					scheduler.getListenerManager().addJobListener(new ReadJobListener(), KeyMatcher.keyEquals(job.getKey()));
				} catch (SchedulerException e1) {
					logger.error("An exception occured while getting a Quartz Listener Manager: {}",e1.getMessage());
				}

				try {
					scheduler.scheduleJob(job, trigger);
				} catch (SchedulerException e) {
					logger.error("Error scheduling a read job with the Quartz Scheduler : {}",e.getMessage());
				}	
			}
		}

		public void jobExecutionVetoed(JobExecutionContext context) {
			// do something with the event
		}

		@Override
		public String getName() {
			return "ReadJobListener";
		}
	}
}
