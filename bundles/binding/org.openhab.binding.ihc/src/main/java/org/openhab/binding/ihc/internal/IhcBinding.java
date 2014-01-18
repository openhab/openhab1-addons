/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.openhab.binding.ihc.IhcBindingProvider;
import org.openhab.binding.ihc.utcs.IhcClient;
import org.openhab.binding.ihc.utcs.WSBooleanValue;
import org.openhab.binding.ihc.utcs.WSControllerState;
import org.openhab.binding.ihc.utcs.WSDateValue;
import org.openhab.binding.ihc.utcs.WSEnumValue;
import org.openhab.binding.ihc.utcs.WSFloatingPointValue;
import org.openhab.binding.ihc.utcs.WSIntegerValue;
import org.openhab.binding.ihc.utcs.WSResourceValue;
import org.openhab.binding.ihc.utcs.WSTimeValue;
import org.openhab.binding.ihc.utcs.WSTimerValue;
import org.openhab.binding.ihc.utcs.WSWeekdayValue;
import org.openhab.binding.ihc.utcs.IhcClient.EnumValue;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IhcBinding order runtime value notifications from IHC / ELKO LS controller
 * and post values to the openHAB event bus when notification is received.
 * 
 * Binding also polls resources from controller where interval is configured.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public class IhcBinding extends AbstractActiveBinding<IhcBindingProvider>
		implements ManagedService, BindingChangeListener {

	private static final Logger logger = 
		LoggerFactory.getLogger(IhcBinding.class);

	private long refreshInterval = 1000;

	/** Thread to handle resource value notifications from the controller */
	private IhcResourceValueNotificationListener resourceValueNotificationListener = null;

	/** Thread to handle controller's state change notifications */
	private IhcControllerStateListener controllerStateListener = null;

	/** Holds time in seconds when configuration is changed */
	private long lastConfigurationChangeTime = 0;

	/** Holds time stamps in seconds when binding items states are refreshed */
	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	private boolean listenersStarted = false;
	
	
	@Override
	protected String getName() {
		return "IHC / ELKO LS refresh and notification listener service";
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	public void activate(ComponentContext componentContext) {
		listenersStarted = false;
		startIhcListener();
	}

	public void deactivate(ComponentContext componentContext) {
		for (IhcBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();
		resourceValueNotificationListener.setInterrupted(true);
		controllerStateListener.setInterrupted(true);
		listenersStarted = false;
	}
	
	
	public synchronized void touchLastConfigurationChangeTime() {
		lastConfigurationChangeTime = System.currentTimeMillis();
	}

	public synchronized long getLastConfigurationChangeTime() {
		return lastConfigurationChangeTime;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {

		IhcClient ihc = IhcConnection.getCommunicator();

		if (ihc != null) {
			for (IhcBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {

					int resourceId = provider.getResourceId(itemName);
					int itemRefreshInterval = provider
							.getRefreshInterval(itemName) * 1000;

					if (itemRefreshInterval > 0) {

						Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
						if (lastUpdateTimeStamp == null) {
							lastUpdateTimeStamp = 0L;
						}

						long age = System.currentTimeMillis()
								- lastUpdateTimeStamp;
						boolean needsUpdate = age >= itemRefreshInterval;

						if (needsUpdate) {

							logger.debug(
									"Item '{}' is about to be refreshed now",
									itemName);

							try {
								WSResourceValue resourceValue = null;

								try {
									resourceValue = ihc
											.resourceQuery(resourceId);
								} catch (IOException e1) {
									logger.warn("Value could not be read from controller - retrying one time.");

									try {
										IhcConnection.reconnect();
										resourceValue = ihc
												.resourceQuery(resourceId);
									} catch (Exception e2) {
										logger.error("Communication error", e2);
									}

								}

								if (resourceValue != null) {
									Class<? extends Item> itemType = provider
											.getItemType(itemName);
									State value = convertResourceValueToState(
											itemType, resourceValue);
									eventPublisher.postUpdate(itemName, value);
								}

							} catch (Exception e) {
								logger.error("Exception", e);
							}

							lastUpdateMap.put(itemName,
									System.currentTimeMillis());
						}
					}
				}
			}
		} else {
			logger.debug("Controller is null => refresh cycle aborted!");
		}

	}

	/**
	 * Convert IHC data type to openHAB data type.
	 * 
	 * @param year
	 * 
	 * @param type
	 *            IHC data type
	 * 
	 * @return openHAB data type
	 */
	private State convertResourceValueToState(Class<? extends Item> itemType,
			WSResourceValue value) throws NumberFormatException {

		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (itemType == NumberItem.class) {

			if (value.getClass() == WSFloatingPointValue.class) {
				// state = new
				// DecimalType(((WSFloatingPointValue)value).getFloatingPointValue());

				// Controller might send floating point value with >10 decimals
				// (22.299999237060546875), so round value to have max 2
				// decimals
				double d = ((WSFloatingPointValue) value)
						.getFloatingPointValue();
				BigDecimal bd = new BigDecimal(d).setScale(2,
						RoundingMode.HALF_EVEN);
				state = new DecimalType(bd);
			}

			else if (value.getClass() == WSBooleanValue.class)
				state = new DecimalType(((WSBooleanValue) value).isValue() ? 1
						: 0);

			else if (value.getClass() == WSIntegerValue.class)
				state = new DecimalType(((WSIntegerValue) value).getInteger());

			else if (value.getClass() == WSTimerValue.class)
				state = new DecimalType(
						((WSTimerValue) value).getMilliseconds());

			else if (value.getClass() == WSWeekdayValue.class)
				state = new DecimalType(
						((WSWeekdayValue) value).getWeekdayNumber());

			else
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");

		} else if (itemType == DimmerItem.class) {

			// Dimmer item extends SwitchItem, so it need to be handled before
			// SwitchItem

			if (value.getClass() == WSIntegerValue.class)
				state = new PercentType(((WSIntegerValue) value).getInteger());

			else
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");

		} else if (itemType == SwitchItem.class) {

			if (value.getClass() == WSBooleanValue.class) {
				if (((WSBooleanValue) value).isValue())
					state = OnOffType.ON;
				else
					state = OnOffType.OFF;
			} else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to SwitchItem");
			}

		} else if (itemType == ContactItem.class) {

			if (value.getClass() == WSBooleanValue.class) {
				if (((WSBooleanValue) value).isValue())
					state = OpenClosedType.OPEN;
				else
					state = OpenClosedType.CLOSED;
			} else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to ContactItem");
			}

		} else if (itemType == DateTimeItem.class) {

			if (value.getClass() == WSDateValue.class) {

				Calendar cal = WSDateTimeToCalendar((WSDateValue) value, null);
				state = new DateTimeType(cal);

			} else if (value.getClass() == WSTimeValue.class) {

				Calendar cal = WSDateTimeToCalendar(null, (WSTimeValue) value);
				state = new DateTimeType(cal);

			} else {

				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to DateTimeItem");
			}

		} else if (itemType == StringItem.class) {

			if (value.getClass() == WSEnumValue.class) {

				state = new StringType(((WSEnumValue) value).getEnumName());

			} else {

				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to StringItem");
			}

		} else if (itemType == RollershutterItem.class) {

			if (value.getClass() == WSIntegerValue.class)
				state = new PercentType(((WSIntegerValue) value).getInteger());

			else
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");

		}

		return state;
	}

	private Calendar WSDateTimeToCalendar(WSDateValue date, WSTimeValue time) {

		Calendar cal = new GregorianCalendar(1900, 01, 01);

		if (date != null) {
			short year = date.getYear();
			short month = date.getMonth();
			short day = date.getDay();

			cal.set(year, month, day, 0, 0, 0);
		}

		if (time != null) {
			int hour = time.getHours();
			int minute = time.getMinutes();
			int second = time.getSeconds();

			cal.set(1900, 1, 1, hour, minute, second);
		}

		return cal;
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		touchLastConfigurationChangeTime();
		super.bindingChanged(provider, itemName);
		startIhcListener();
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		touchLastConfigurationChangeTime();

		if (config != null) {
		}

		setProperlyConfigured(true);
	}

	private void startIhcListener() {
		if (listenersStarted == false && bindingsExist()) {
			logger.debug("startIhcListener");
			resourceValueNotificationListener = new IhcResourceValueNotificationListener();
			resourceValueNotificationListener.start();
			controllerStateListener = new IhcControllerStateListener();
			controllerStateListener.start();
			listenersStarted = true;
		}
	}

	/**
	 * The IhcReader runs as a separate thread.
	 * 
	 * Thread listen resource value notifications from IHC / ELKO LS controller
	 * and post updates to openHAB bus when notifications are received.
	 * 
	 */
	private class IhcResourceValueNotificationListener extends Thread {

		private boolean interrupted = false;
		private long lastNotificationOrderTime = 0;

		IhcResourceValueNotificationListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		private void enableResourceValueNotification()
				throws UnsupportedEncodingException, XPathExpressionException,
				IOException {
			logger.debug("Order resource runtime value notifications from controller");

			List<Integer> resourceIdList = new ArrayList<Integer>();

			IhcClient ihc = IhcConnection.getCommunicator();

			if (ihc != null) {
				for (IhcBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames()) {
						resourceIdList.add(provider.getResourceId(itemName));
					}
				}
			}

			if (resourceIdList.size() > 0) {
				logger.debug("Enable runtime notfications for {} resources",
						resourceIdList.size());
				ihc.enableRuntimeValueNotifications(resourceIdList);
				lastNotificationOrderTime = System.currentTimeMillis();
			}
		}

		@Override
		public void run() {

			logger.debug("IHC resource value listener started");

			boolean ready = false;

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				boolean orderResourceValueNotifications = false;

				final long lastConfigChangeTime = getLastConfigurationChangeTime();

				if (lastConfigChangeTime > lastNotificationOrderTime) {

					logger.debug("Configuration change detected");

					ready = false;

					if ((lastConfigChangeTime + 1000) < System
							.currentTimeMillis()) {

						ready = true;
						orderResourceValueNotifications = true;

					} else {
						logger.debug("Waiting 1 seconds before reorder runtime value notifications");
					}
				}

				if (IhcConnection.getLastOpenTime() > lastNotificationOrderTime) {
					logger.debug("Controller connection reopen detected");
					orderResourceValueNotifications = true;
				}

				if (ready)
					waitResourceNotifications(orderResourceValueNotifications);
				else
					mysleep(1000L);
			}

			logger.debug("IHC Listener stopped");

		}

		private void waitResourceNotifications(
				boolean orderResourceValueNotifications) {

			IhcClient ihc = IhcConnection.getCommunicator();

			if (ihc != null) {

				try {

					if (orderResourceValueNotifications)
						enableResourceValueNotification();

					logger.debug("Wait new notifications from controller");

					List<? extends WSResourceValue> resourceValueList = ihc
							.waitResourceValueNotifications(10);

					logger.debug(
							"{} new notifications received from controller",
							resourceValueList.size());

					for (WSResourceValue val : resourceValueList) {
						for (IhcBindingProvider provider : providers) {
							for (String itemName : provider.getItemNames()) {

								int resourceId = provider
										.getResourceId(itemName);

								if (val.getResourceID() == resourceId) {

									if (provider.isOutBindingOnly(itemName)) {

										logger.debug(
												"{} is out binding only...skip update to OpenHAB bus",
												itemName);

									} else {

										Class<? extends Item> itemType = provider
												.getItemType(itemName);
										org.openhab.core.types.State value = convertResourceValueToState(
												itemType, val);
										eventPublisher.postUpdate(itemName,
												value);

									}
								}

							}
						}
					}

				} catch (SocketTimeoutException e2) {
					logger.debug("Notifications timeout - no new notifications");

				} catch (IOException e) {
					logger.error(
							"New notifications wait failed...reinitialize connection",
							e);

					try {
						ihc.openConnection();
						enableResourceValueNotification();

					} catch (Exception e2) {
						logger.error("Communication error", e2);

						// fatal error occurred, be sure that notifications is
						// reordered
						lastNotificationOrderTime = 0;

						// sleep a while, before retry
						mysleep(1000L);
					}
				} catch (Exception e) {
					logger.error("Exception", e);

					// sleep a while, before retry
					mysleep(5000L);
				}

			} else {
				logger.warn("Controller is null => resource value notfications waiting aborted!");
				mysleep(5000L);
			}

		}

		private void mysleep(long milli) {
			try {
				sleep(5000L);
			} catch (InterruptedException e3) {
				interrupted = true;
			}
		}
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null) {

			IhcBindingProvider provider = findFirstMatchingBindingProvider(itemName);

			if (provider == null) {
				logger.warn(
						"Doesn't find matching binding provider [itemName={}]",
						itemName);
				return;
			}

			logger.debug(
					"Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(),
							command.getClass().toString() });

			IhcClient ihc = IhcConnection.getCommunicator();

			if (ihc == null) {
				logger.warn("IHC / ELKO LS controller is null!");
				return;
			}

			try {

				int resourceId = provider.getResourceId(itemName);
				WSResourceValue value = ihc
						.getResourceValueInformation(resourceId);
				value = convertCommandToResourceValue(command, value);

				boolean result = false;

				try {
					result = ihc.resourceUpdate(value);

				} catch (IOException e1) {

					logger.warn(
							"Value could not be set - retrying one time: {}",
							e1.getMessage());

					try {
						IhcConnection.reconnect();
						result = ihc.resourceUpdate(value);

					} catch (IOException e2) {

						logger.error("Communication error - giving up: {}",
								e2.getMessage());
						return;

					} catch (Exception e) {
						logger.error("Communication error", e);
					}
				} catch (Exception e) {

					logger.error("Exception", e);
				}

				if (result == true)
					logger.debug("Item updated '{}' succesfully sent", itemName);
				else
					logger.error("Item '{}' update failed", itemName);

			} catch (Exception e) {

				logger.error("Exception ", e);
			}
		}

	}

	@Override
	public void internalReceiveUpdate(String itemName,
			org.openhab.core.types.State newState) {

		if (itemName != null) {

			IhcBindingProvider provider = findFirstMatchingBindingProvider(itemName);

			if (provider == null) {
				logger.warn(
						"Doesn't find matching binding provider [itemName={}]",
						itemName);
				return;
			}

			if (provider.isOutBindingOnly(itemName)) {

				logger.debug(
						"Received out binding update (item='{}', state='{}', class='{}')",
						new Object[] { itemName, newState.toString(),
								newState.getClass().toString() });

				IhcClient ihc = IhcConnection.getCommunicator();

				if (ihc == null) {
					logger.warn("IHC / ELKO LS controller is null!");
					return;
				}

				try {

					int resourceId = provider.getResourceId(itemName);
					WSResourceValue value = ihc
							.getResourceValueInformation(resourceId);
					value = convertCommandToResourceValue(newState, value);

					boolean result = false;

					try {
						result = ihc.resourceUpdate(value);

					} catch (IOException e1) {

						logger.warn(
								"Value could not be set - retrying one time: {}",
								e1.getMessage());

						try {
							IhcConnection.reconnect();
							result = ihc.resourceUpdate(value);

						} catch (IOException e2) {

							logger.error("Communication error - giving up: {}",
									e2.getMessage());
							return;

						} catch (Exception e) {
							logger.error("Communication error", e);
						}
					} catch (Exception e) {

						logger.error("Exception", e);
					}

					if (result == true)
						logger.debug("Item updated '{}' succesfully sent",
								itemName);
					else
						logger.error("Item '{}' update failed", itemName);

				} catch (Exception e) {

					logger.error("Exception ", e);
				}

			}

		}

	}

	/**
	 * Find the first matching {@link IhcBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private IhcBindingProvider findFirstMatchingBindingProvider(String itemName) {

		IhcBindingProvider firstMatchingProvider = null;

		for (IhcBindingProvider provider : this.providers) {

			int resourceId = provider.getResourceId(itemName);

			if (resourceId > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	/**
	 * Convert openHAB data type to IHC data type.
	 * 
	 * @param type
	 *            openHAB data type
	 * 
	 * @return IHC data type
	 */
	private WSResourceValue convertCommandToResourceValue(Type type,
			WSResourceValue value) {

		if (type instanceof DecimalType) {

			if (value instanceof WSFloatingPointValue) {

				double newVal = ((DecimalType) type).doubleValue();
				double max = ((WSFloatingPointValue) value).getMaximumValue();
				double min = ((WSFloatingPointValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max)
					((WSFloatingPointValue) value)
							.setFloatingPointValue(newVal);
				else
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");

			} else if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value).setValue(((DecimalType) type)
						.intValue() > 0 ? true : false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = ((DecimalType) type).intValue();
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max)
					((WSIntegerValue) value).setInteger(newVal);
				else
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");

			} else if (value instanceof WSTimerValue) {

				((WSTimerValue) value).setMilliseconds(((DecimalType) type)
						.longValue());

			} else if (value instanceof WSWeekdayValue) {

				((WSWeekdayValue) value).setWeekdayNumber(((DecimalType) type)
						.intValue());

			} else {

				throw new NumberFormatException("Can't convert DecimalType to "
						+ value.getClass());

			}

		} else if (type instanceof OnOffType) {

			if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value).setValue(type == OnOffType.ON ? true
						: false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = type == OnOffType.ON ? 100 : 0;
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max)
					((WSIntegerValue) value).setInteger(newVal);
				else
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");

			} else {

				throw new NumberFormatException("Can't convert OnOffType to "
						+ value.getClass());

			}
		} else if (type instanceof OpenClosedType) {

			((WSBooleanValue) value)
					.setValue(type == OpenClosedType.OPEN ? true : false);

		} else if (type instanceof DateTimeItem) {

			if (value instanceof WSDateValue) {

				short year = Short.parseShort(type.format("yyyy"));
				byte month = Byte.parseByte(type.format("MM"));
				byte day = Byte.parseByte(type.format("dd"));

				((WSDateValue) value).setYear(year);
				((WSDateValue) value).setMonth(month);
				((WSDateValue) value).setDay(day);

			} else if (value instanceof WSTimeValue) {

				int hours = Integer.parseInt(type.format("hh"));
				int minutes = Integer.parseInt(type.format("mm"));
				int seconds = Integer.parseInt(type.format("ss"));

				((WSTimeValue) value).setHours(hours);
				((WSTimeValue) value).setMinutes(minutes);
				((WSTimeValue) value).setSeconds(seconds);

			} else {

				throw new NumberFormatException(
						"Can't convert DateTimeItem to " + value.getClass());

			}

		} else if (type instanceof StringType) {

			if (value instanceof WSEnumValue) {
				IhcClient ihc = IhcConnection.getCommunicator();

				ArrayList<IhcClient.EnumValue> enumValues = ihc
						.getEnumValues(((WSEnumValue) value)
								.getDefinitionTypeID());

				boolean found = false;

				for (EnumValue item : enumValues) {

					if (item.name.equals(type.toString())) {

						((WSEnumValue) value).setEnumValueID(item.id);
						((WSEnumValue) value).setEnumName(type.toString());
						found = true;
						break;
					}
				}

				if (found == false) {
					throw new NumberFormatException(
							"Can't find enum value for string "
									+ type.toString());
				}

			} else {

				throw new NumberFormatException("Can't convert StringType to "
						+ value.getClass());

			}

		} else if (type instanceof PercentType) {

			if (value instanceof WSIntegerValue) {

				int newVal = ((DecimalType) type).intValue();
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max)
					((WSIntegerValue) value).setInteger(newVal);
				else
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");

			} else {

				throw new NumberFormatException("Can't convert PercentType to "
						+ value.getClass());

			}

		} else if (type instanceof UpDownType) {

			if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value)
						.setValue(type == UpDownType.DOWN ? true : false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = type == UpDownType.DOWN ? 100 : 0;
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max)
					((WSIntegerValue) value).setInteger(newVal);
				else
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");

			} else {

				throw new NumberFormatException("Can't convert UpDownType to "
						+ value.getClass());

			}

		} else {

			throw new NumberFormatException("Can't convert "
					+ type.getClass().toString());

		}

		return value;
	}

	/**
	 * The IhcReader runs as a separate thread.
	 * 
	 * Thread listen controller state change notifications from IHC / ELKO LS
	 * controller and .
	 * 
	 */
	private class IhcControllerStateListener extends Thread {

		private boolean interrupted = false;

		IhcControllerStateListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {

			logger.debug("IHC controller state listener started");

			WSControllerState oldState = null;

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				IhcClient ihc = IhcConnection.getCommunicator();

				if (ihc != null) {

					try {

						if (oldState == null) {

							oldState = ihc.queryControllerState();
							logger.debug("Controller initial state {}",
									oldState.getState());
						}

						logger.debug("Wait new state change notification from controller");

						WSControllerState currentState = ihc
								.waitStateChangeNotifications(oldState, 10);
						logger.debug("Controller state {}",
								currentState.getState());

						if (oldState.getState().equals(currentState.getState()) == false) {
							logger.info(
									"Controller state change detected ({} -> {})",
									oldState.getState(),
									currentState.getState());

							if (oldState.getState().equals(
									IhcClient.CONTROLLER_STATE_INITIALIZE)
									|| currentState.getState().equals(
											IhcClient.CONTROLLER_STATE_READY)) {

								logger.debug("Reopen connection...");
								IhcConnection.connect();
							}

							oldState.setState(currentState.getState());
						}

					} catch (IOException e) {
						logger.error(
								"New controller state change notification wait failed...reinitialize connection",
								e);

						try {
							IhcConnection.reconnect();

						} catch (Exception e2) {
							logger.error("Communication error", e2);

							// sleep a while, before retry
							mysleep(1000L);
						}
					} catch (Exception e) {
						logger.error("Exception", e);

						// sleep a while, before retry
						mysleep(5000L);
					}

				} else {
					logger.warn("Controller is null => resource value notfications waiting aborted!");
					mysleep(5000L);
				}
			}

		}

		private void mysleep(long milli) {
			try {
				sleep(5000L);
			} catch (InterruptedException e3) {
				interrupted = true;
			}
		}
	}

}
