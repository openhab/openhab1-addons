/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.linux;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.openhab.io.gpio.GPIOPin;
import org.openhab.io.gpio.GPIOPinEventHandler;



import com.sun.jna.LastErrorException;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.ByteByReference;

/**
 * Implementation of <code>GPIOPin</code> interface for boards running
 * Linux OS. Based on kernel GPIO framework exposed to user space
 * through <code>sysfs</code> pseudo filesystem.
 * 
 * @author Dancho Penev
 * @since 1.5.0
 */
public class GPIOPinLinux implements GPIOPin {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
	private static final long PINLOCK_TIMEOUT = 10;
	private static final TimeUnit PINLOCK_TIMEOUT_UNITS = TimeUnit.SECONDS;

	/** Pin wide read/write lock. */
	private ReentrantReadWriteLock pinLock = new ReentrantReadWriteLock();

	/** Set of registered pin event handlers. */
	private Set<GPIOPinEventHandler> eventHandlers = new HashSet<GPIOPinEventHandler>();

	/**
	 * Interrupt listening thread, running only if there are registered
	 * event handlers.
	 */
	private EventListener eventListenerThread = null;

	private int pinNumber;
	private long debounceInterval;
	private Path activelowPath = null;
	private Path directionPath = null;
	private Path edgePath = null;
	private Path valuePath = null;

	/**
	 * Initializes paths to special files exposed to user space by kernel
	 * GPIO framework.
	 * 
	 * @param pinNumber the pin number as seen by the kernel
	 * @param gpioPinDirectory path to pin directory in <code>sysfs</code>,
	 * 		e.g. "/sys/class/gpio/gpio1"
	 * @param debounceInterval default debounce interval
	 */
	public GPIOPinLinux(int pinNumber, String gpioPinDirectory, long debounceInterval) {

		this.pinNumber = pinNumber;

		activelowPath = Paths.get(gpioPinDirectory + "/active_low");

		/* The 'direction' attribute will not exist if the kernel doesn't
		 * support changing the direction of a GPIO, or it was exported by
		 * kernel code that didn't explicitly allow user space to reconfigure
		 * this GPIO's direction.
		 */
		if (Files.exists(Paths.get(gpioPinDirectory + "/direction"))) {
			directionPath = Paths.get(gpioPinDirectory + "/direction");
		}

		/* This file exists only if the pin can be configured as an	interrupt
		 * generating input pin.
		 */
		if (Files.exists(Paths.get(gpioPinDirectory + "/edge"))) {
			edgePath = Paths.get(gpioPinDirectory + "/edge");
		}

		valuePath = Paths.get(gpioPinDirectory + "/value");

		this.debounceInterval = debounceInterval;
	}

	/**
	 * Stops all spawned pin threads.
	 * 
	 * @throws IOException if can't obtain pin lock in timely fashion
	 * 		or was interrupted while waiting for lock
	 */
	public void stopEventProcessing() throws IOException {

		try {
			if (pinLock.writeLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
					try {
						if (eventListenerThread != null) {
							eventHandlers = null;
							eventListenerThread.interrupt();
							eventListenerThread = null;
	
							/* Give some time to event listener thread to notice the interrupt and do cleanup */
							Thread.sleep(EventListener.POLL_TIMEOUT * 2);
						}
					} catch (InterruptedException e) {
						throw new IOException("The thread was interrupted while waiting for GPIO pin event listener thread to finish");
					} finally {
						pinLock.writeLock().unlock();
					}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO pin lock");
		}
	}

	public int getActiveLow() throws IOException {

		int activeLow;

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* Gets the value of first line from 'activelow' file parsed to integer. */
					activeLow = Integer.parseInt(Files.readAllLines(activelowPath, DEFAULT_ENCODING).get(0));
				} catch (NumberFormatException e) {
					throw new IOException("Unsupported, not numeric 'activelow' value", e);
				} finally {
					pinLock.readLock().unlock();
				}

				if ((activeLow != GPIOPin.ACTIVELOW_DISABLED) && (activeLow != GPIOPin.ACTIVELOW_ENABLED)) {
					throw new IOException("Unsupported 'activelow' value (" + activeLow + ")");
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}

		return activeLow;
	}

	public void setActiveLow(Integer activeLow) throws IOException {

		if ((activeLow != GPIOPin.ACTIVELOW_DISABLED) && (activeLow != GPIOPin.ACTIVELOW_ENABLED)) {
			throw new IllegalArgumentException("Unsupported argument for 'activeLow' parameter (" + activeLow + ")");
		}

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {
					Files.write(activelowPath, activeLow.toString().getBytes());
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}
}

	public int getDirection() throws IOException {

		String direction;

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* The board/pin may not support direction change */
					if (directionPath == null) {
						throw new IOException("The pin doesn't support 'get direction' operation");
					}

					/* Read first line from 'direction' file */
					direction = Files.readAllLines(directionPath, DEFAULT_ENCODING).get(0);
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}

		if (direction.compareToIgnoreCase("in") == 0) {
			return GPIOPin.DIRECTION_IN;
		} else {
			if (direction.compareToIgnoreCase("out") == 0) {
				return GPIOPin.DIRECTION_OUT;
			} else {
				if (direction.compareToIgnoreCase("high") == 0) {
					return GPIOPin.DIRECTION_OUT_HIGH;
				} else {
					if (direction.compareToIgnoreCase("low") == 0) {
						return GPIOPin.DIRECTION_OUT_LOW;
					} else {
						throw new IOException("Unsupported 'direction' value (" + direction + ")");
					}
				}
			}
		}
	}

	public void setDirection(int direction) throws IOException {

		String newDirection;

		switch (direction) {
		case GPIOPin.DIRECTION_IN:
			newDirection = "in";
			break;
		case GPIOPin.DIRECTION_OUT:
			newDirection = "out";
			break;
		case GPIOPin.DIRECTION_OUT_HIGH:
			newDirection = "high";
			break;
		case GPIOPin.DIRECTION_OUT_LOW:
			newDirection = "low";
			break;
		default:
			throw new IllegalArgumentException("Unsupported argument for 'direction' parameter (" + direction + ")");
		}

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* The board/pin may not support direction change */
					if (directionPath == null) {
						throw new IOException("The pin doesn't support 'set direction' operation");
					}

					Files.write(directionPath, newDirection.getBytes());
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}
	}

	public int getEdgeDetection() throws IOException {

		String edgeDetection;

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* The board/pin may not support interrupts */
					if (edgePath == null) {
						throw new IOException("The pin doesn't support 'get edge detection' operation");
					}
					
					edgeDetection = Files.readAllLines(edgePath, DEFAULT_ENCODING).get(0);
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}
		
		if (edgeDetection.compareToIgnoreCase("none") == 0) {
			return GPIOPin.EDGEDETECTION_NONE;
		} else {
			if (edgeDetection.compareToIgnoreCase("rising") == 0) {
				return GPIOPin.EDGEDETECTION_RISING;
			} else {
				if (edgeDetection.compareToIgnoreCase("falling") == 0) {
					return GPIOPin.EDGEDETECTION_FALLING;
				} else {
					if (edgeDetection.compareToIgnoreCase("both") == 0) {
						return GPIOPin.EDGEDETECTION_BOTH;
					} else {
						throw new IOException("Unsupported 'edge detection' value (" + edgeDetection + ")");
					}
				}
			}
		}
	}

	public void setEdgeDetection(int edgeDetection) throws IOException {

		String newEdgeDetection;

		switch (edgeDetection) {
		case GPIOPin.EDGEDETECTION_NONE:
			newEdgeDetection = "none";
			break;
		case GPIOPin.EDGEDETECTION_RISING:
			newEdgeDetection = "rising";
			break;
		case GPIOPin.EDGEDETECTION_FALLING:
			newEdgeDetection = "falling";
			break;
		case GPIOPin.EDGEDETECTION_BOTH:
			newEdgeDetection = "both";
			break;
		default:
			throw new IllegalArgumentException("Unsupported argument for 'edge detection' parameter (" + edgeDetection + ")");
		}

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* The board/pin may not support interrupts */
					if (edgePath == null) {
						throw new IOException("The pin doesn't support 'set edge detection' operation");
					}

					Files.write(edgePath, newEdgeDetection.getBytes());
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}
	}

	public int getValue() throws IOException {

		int value;

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* Gets the value of first line from 'value' file parsed to integer. */
					value = Integer.parseInt(Files.readAllLines(valuePath, DEFAULT_ENCODING).get(0));	
				} catch (NumberFormatException e) {
					throw new IOException("Unsupported, not numeric 'value' value", e);
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}

		if ((value != GPIOPin.VALUE_LOW) && (value != GPIOPin.VALUE_HIGH)) {
			throw new IOException("Unsupported 'value' value (" + value + ")");
		}

		return value;
	}

	public void setValue(Integer value) throws IOException {

		if ((value != GPIOPin.VALUE_LOW) && (value != GPIOPin.VALUE_HIGH)) {
			throw new IllegalArgumentException("Unsupported argument for 'value' parameter (" + value + ")");
		}

		try {
			if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {
					Files.write(valuePath, value.toString().getBytes());
				} finally {
					pinLock.readLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
		}
	}

	public int getPinNumber() {
		return pinNumber;
	}

	public long getDebounceInterval() {
		return debounceInterval;
	}

	public void setDebounceInterval(long debounceInterval) throws IOException {
		
		if (debounceInterval < 0) {
			throw new IllegalArgumentException("Unsupported argument for 'debounceInterval' parameter (" + debounceInterval + ")");
		}

		try {
			if (pinLock.writeLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {
					this.debounceInterval = debounceInterval;
				} finally {
					pinLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO pin lock");
		}	
	}

	public void addEventHandler(GPIOPinEventHandler eventHandler) throws IOException {

		if (eventHandler == null) {
			throw new IllegalArgumentException("Unsupported argument for 'eventHandler' parameter (null)");
		}

		try {
			if (pinLock.writeLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {

					/* The board/pin may not support interrupts */
					if (edgePath == null) {
						throw new IOException("Interrupts aren't supported for this pin");
					}

					if (!eventHandlers.add(eventHandler)) {
						throw new IllegalArgumentException("The event handler is already registered");
					}

					/* Start event listener thread if not running */
					if (eventListenerThread == null) {
						eventListenerThread = new EventListener(this);
						eventListenerThread.setName("openHAB GPIO event listener (pin " + pinNumber + ")");
						eventListenerThread.start();
					}
				} finally {
					pinLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO pin lock");
		}
	}

	public void removeEventHandler(GPIOPinEventHandler eventHandler) throws IOException {

		if (eventHandler == null) {
			throw new IllegalArgumentException("Unsupported argument for 'eventHandler' parameter (null)");
		}

		try {
			if (pinLock.writeLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
				try {
					if (!eventHandlers.remove(eventHandler)) {
						throw new IllegalArgumentException("The event handler isn't registered");
					}

					/* Stop event listener thread if there are no other registered handlers */
					if (eventHandlers.isEmpty()) {
						eventListenerThread.interrupt();
						eventListenerThread = null;
					}
				} finally {
					pinLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO pin lock");
		}
	}

	/**
	 * Event listener thread, listens for interrupts and executes registered
	 * handlers each in its own thread.
	 *
	 * @author Dancho Penev
	 * @since 1.5.0
	 */
	private class EventListener extends Thread {

		private static final int POLL_TIMEOUT = 1000;

		/** Pin object for which events are listened. */
		private GPIOPinLinux pin;

		public EventListener(GPIOPinLinux pin) {
			this.pin = pin;
		}

		public void run() {

			/* File descriptor for 'value' file */
			int fd = -1;

			/* Thread pool for event handlers */
			ExecutorService executorService = Executors.newCachedThreadPool();

			try {
				int rc;
				pollfd[] pollfdset = { new pollfd() };
				ByteByReference value = new ByteByReference();
				NativeLong zero = new NativeLong(0);

				/* Last time (in milliseconds) when the interrupt was generated */
				long lastInterruptTime = 0;

				fd = LibC.INSTANCE.open(pin.valuePath.toString(), LibC.O_RDONLY | LibC.O_NONBLOCK);

				pollfdset[0].fd = fd;
				pollfdset[0].events = LibC.POLLPRI;

				/* Prior calling poll() the file needs to be read or poll() will return immediately without real interrupt received */
				try {
					LibC.INSTANCE.read(pollfdset[0].fd, value.getPointer(), 1);
				} catch (LastErrorException e) {}

				while(!interrupted()) {

					/* Wait for GPIO interrupt or timeout to occur. Timeouts provides possibility to check thread interrupt status */
					rc = LibC.INSTANCE.poll(pollfdset, 1, POLL_TIMEOUT);

					switch(rc) {

					/* Timeout, poll() again */
					case 0:
						continue;

					/* There is one file descriptor ready */
					case 1:
						/* Is interrupt received? */
						if ((pollfdset[0].revents & LibC.POLLPRI) > 0) {

							/* Calculate times for software debounce */
							long interruptTime = System.currentTimeMillis();
							long timeDifference = interruptTime - lastInterruptTime;

							/* Go to file start and read first byte */
							LibC.INSTANCE.lseek(fd, zero, LibC.SEEK_SET);
							rc = LibC.INSTANCE.read(pollfdset[0].fd, value.getPointer(), 1);

							/* There is exactly one byte read */
							if (rc == 1) {

								/* Execute event handlers each in its own thread */
								try {
									if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
										try {

											/* Software debounce */
											if ((timeDifference > pin.debounceInterval) || (timeDifference < 0)) {

												for (GPIOPinEventHandler eventHandler : pin.eventHandlers) {
													EventHandlerExecutor eventHandlerExecutor = new EventHandlerExecutor(pin, eventHandler,
															Character.getNumericValue(value.getValue()));
													executorService.execute(eventHandlerExecutor);
												}

												lastInterruptTime = interruptTime;
											}
										} finally {
											pin.pinLock.readLock().unlock();
										}
									} else {

										/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
										throw new IOException("Read GPIO pin lock can't be aquired for " + PINLOCK_TIMEOUT + " " + PINLOCK_TIMEOUT_UNITS.toString());
									}
								} catch (InterruptedException e) {
									throw new IOException("The thread was interrupted while waiting for read GPIO pin lock");
								}

								/* poll() again */
								continue;
							} else {
								throw new IOException("Unsupported return value from native 'read' function (" + rc + ")");
							}
						}
						break;
					default:
						throw new IOException("Unsupported return value from native 'poll' function (" + rc + ")");
					}
				}
			} catch (Exception e) {

				/* Execute error handlers each in its own thread */
				try {
					if (pinLock.readLock().tryLock(PINLOCK_TIMEOUT, PINLOCK_TIMEOUT_UNITS)) {
						try {
							for (GPIOPinEventHandler eventHandler : pin.eventHandlers) {
								EventHandlerExecutor eventHandlerExecutor = new EventHandlerExecutor(pin, eventHandler, e);
								executorService.execute(eventHandlerExecutor);
							}
						} finally {
							pin.pinLock.readLock().unlock();
						}
					}
				} catch (InterruptedException ex) {}
			} finally {

				/* Cleanup */
				LibC.INSTANCE.close(fd);
				executorService.shutdown();
			}
		}

		/**
		 * Executes callback functions.
		 *
		 * @author Dancho Penev
		 * @since 1.5.0
		 */
		private class EventHandlerExecutor implements Runnable {

			private static final int TYPE_EVENT = 0;
			private static final int TYPE_ERROR = 1;

			private int type;
			private GPIOPinLinux pin;
			private GPIOPinEventHandler eventHandler;
			private int value;
			private Exception exception;

			public EventHandlerExecutor(GPIOPinLinux pin, GPIOPinEventHandler eventHandler, int value) {
				this.type = TYPE_EVENT;
				this.pin = pin;
				this.eventHandler = eventHandler;
				this.value = value;
			}

			public EventHandlerExecutor(GPIOPinLinux pin, GPIOPinEventHandler eventHandler, Exception exception) {
				this.type = TYPE_ERROR;
				this.pin = pin;
				this.eventHandler = eventHandler;
				this.exception = exception;
			}

			public void run() {
				switch (type) {
				case TYPE_EVENT:
					eventHandler.onEvent(pin, value);
					break;
				case TYPE_ERROR:
					eventHandler.onError(pin, exception);
					break;
				}
			}
		}
	}
}
