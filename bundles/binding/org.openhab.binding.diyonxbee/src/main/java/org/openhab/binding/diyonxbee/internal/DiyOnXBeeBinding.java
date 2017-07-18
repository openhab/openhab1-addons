/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.diyonxbee.internal;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.diyonxbee.DiyOnXBeeBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.util.ByteUtils;

/**
 * 
 * @author juergen.richtsfeld@gmail.com
 * @since 1.9
 */
public class DiyOnXBeeBinding extends AbstractBinding<DiyOnXBeeBindingProvider> implements PacketListener,
		ManagedService {

	static final String ITEM_SEPARATOR = "\r\n";

	private static final Logger logger = LoggerFactory.getLogger(DiyOnXBeeBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	// private BundleContext bundleContext;

	/**
	 * the serialPort used to communicate with the XBee
	 */
	private String serialPort = "";

	/**
	 * The baud rate to use when communicating with the XBee module. Defaults to
	 * 9600
	 */
	private int baudRate = 9600;

	private XBee xbee;

	/**
	 * don't use directly, use {@link #xbeeUsageLock} or {@link #xbeeSetupLock}
	 */
	private final ReadWriteLock xbeeLock = new ReentrantReadWriteLock();

	/**
	 * use this lock for 'normal' usage of the xbee object
	 */
	private final Lock xbeeUsageLock = xbeeLock.readLock();

	/**
	 * use this lock when changing the {@link #xbee member}
	 */
	private final Lock xbeeSetupLock = xbeeLock.writeLock();

	private ItemRegistry itemRegistry;
	private EventPublisher eventPublisher;

	public DiyOnXBeeBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher evt) {
		this.eventPublisher = null;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void removeItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		// this.bundleContext = bundleContext;
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		xbeeSetupLock.lock();
		try {
			if (xbee != null) {
				xbee.removePacketListener(this);
				xbee.close();
				xbee = null;
			}
		} finally {
			xbeeSetupLock.unlock();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		for (DiyOnXBeeBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				internalReceiveCommand(provider, itemName, command);
			}
		}
	}

	/**
	 * 
	 * @return if the command was sent successfully
	 */
	private boolean internalReceiveCommand(DiyOnXBeeBindingProvider provider, String itemName, Command command) {
		final String remote = provider.getRemote(itemName);
		final int[] remoteAddress = FormatUtil.fromReadableAddress(remote);

		Item item;
		try {
			item = itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e1) {
			logger.error("unable to get item {}", itemName, e1);
			return false;
		}

		final String commandValue = createCommand(item, command);
		if (commandValue == null) {
			logger.warn("unable to create command {} for item {}", commandValue, itemName);
			return false;
		} else {
			logger.debug("created command {} for item {}", commandValue, itemName);
		}

		final String commandString = new StringBuilder().append(provider.getId(itemName)).append('=')
				.append(commandValue).append('\n').toString();
		final ZNetTxRequest request = new ZNetTxRequest(new XBeeAddress64(remoteAddress), createPayload(commandString));

		xbeeUsageLock.lock();
		try {
			if (xbee == null) {
				logger.error("cannot send command to {}  as the XBee module isn't initialized", itemName);
				return false;
			} else {
				final XBeeResponse response = xbee.sendSynchronous(request); // TODO:
																				// evaluate
																				// response?
				return true;
			}
		} catch (XBeeTimeoutException e) {
			logger.error("failed sending {} to {}", command, itemName, e);
		} catch (XBeeException e) {
			logger.error("failed sending {} to {}", command, itemName, e);
		} finally {
			xbeeUsageLock.unlock();
		}
		return false;
	}

	private int[] createPayload(String requestString) {
		return ByteUtils.stringToIntArray(requestString);
	}

	private String createCommand(final Item item, Command command) {
		if (command == OnOffType.ON) {
			return "ON";
		} else if (command == OnOffType.OFF)
			return "OFF";
		else if (command instanceof IncreaseDecreaseType) {
			final State state = item.getState();
			if (state instanceof HSBType) {
				final HSBType hsbType = (HSBType) state;
				return changeColorBrightness(hsbType, (IncreaseDecreaseType) command);
			} else if (state instanceof PercentType) {
				final PercentType percent = (PercentType) state;
				final PercentType newBrightness = changeBrightness((IncreaseDecreaseType) command, percent);
				return makeHUE(newBrightness.floatValue());
			}
		} else if (command instanceof HSBType) {
			final HSBType hsb = (HSBType) command;
			return makeRGB(hsb.toColor());
		} else if (command instanceof PercentType) {
			final PercentType percent = (PercentType) command;
			return makeHUE(percent.floatValue());
		}

		return null;
	}

	private String changeColorBrightness(final HSBType hsbType, IncreaseDecreaseType increaseDecrease) {
		final PercentType brightness = hsbType.getBrightness();
		final PercentType newBrightness = changeBrightness(increaseDecrease, brightness);
		final HSBType newHSB = new HSBType(hsbType.getHue(), hsbType.getSaturation(), newBrightness);
		return makeRGB(newHSB.toColor());
	}

	private PercentType changeBrightness(IncreaseDecreaseType increaseDecrease, final PercentType brightness) {
		final PercentType newBrightness;
		if (increaseDecrease == IncreaseDecreaseType.DECREASE) {
			BigDecimal changed = brightness.toBigDecimal().subtract(BigDecimal.ONE);
			if (changed.compareTo(BigDecimal.ZERO) < 0)
				changed = BigDecimal.ZERO;
			newBrightness = new PercentType(changed);
		} else {
			BigDecimal changed = brightness.toBigDecimal().add(BigDecimal.ONE);
			if (changed.compareTo(PercentType.HUNDRED.toBigDecimal()) > 0) {
				changed = PercentType.HUNDRED.toBigDecimal();
			}
			newBrightness = new PercentType(changed);
		}
		return newBrightness;
	}

	private String makeRGB(Color color) {
		final StringBuilder sb = new StringBuilder(12);
		sb.append("RGB");
		appendColor(sb, color.getRed());
		appendColor(sb, color.getGreen());
		appendColor(sb, color.getBlue());
		return sb.toString();
	}

	private String makeHUE(final float value) {
		final StringBuilder sb = new StringBuilder(6);
		sb.append("HUE");

		final float brightnessValue = value / 100f * 255f;

		appendColor(sb, (int) brightnessValue);
		return sb.toString();
	}

	private State parseRGBState(final String value) {
		if (!value.startsWith("RGB") || value.length() != 12)
			return null;

		try {
			final int red = Integer.valueOf(value.substring(3, 6));
			final int green = Integer.valueOf(value.substring(6, 9));
			final int blue = Integer.valueOf(value.substring(9, 12));

			return new HSBType(new Color(red, green, blue));
		} catch (NumberFormatException e) {
			logger.warn("cannot parse color from {}", value, e);
		}

		return null;
	}

	private State parseHUE(final String value) {
		if (!value.startsWith("HUE") || value.length() != 6)
			return null;

		try {
			final double hue = Integer.valueOf(value.substring(3, 6));
			return new PercentType(new BigDecimal(hue / 255d * 100d));
		} catch (NumberFormatException e) {
			logger.warn("cannot parse color from {}", value, e);
		}

		return null;
	}

	private void appendColor(StringBuilder sb, int channel) {
		if (channel < 100) {
			sb.append('0');
		}
		if (channel < 10) {
			sb.append('0');
		}
		sb.append(channel);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	private final Map<String, String> lastKeys = new HashMap<String, String>();

	@Override
	public void processResponse(XBeeResponse response) {
		if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
			final ZNetRxResponse rxResponse = (ZNetRxResponse) response;
			final String message = ByteUtils.toString(rxResponse.getData());
			final String remoteAddress = FormatUtil.readableAddress(rxResponse.getRemoteAddress64().getAddress());

			processResponse(message, remoteAddress);
		}
	}

	void processResponse(final String message, final String remoteAddress) {
		logger.debug("received message: '{}' from '{}'", message, remoteAddress);

		int startIdx = 0;
		do {
			final int idxEquals = message.indexOf('=', startIdx);
			final int idxEnd = message.indexOf(ITEM_SEPARATOR, startIdx);

			if (idxEquals > 0 && idxEnd > 0) {
				if (idxEnd > idxEquals) {
					final String key = message.substring(startIdx, idxEquals);
					final String value = message.substring(idxEquals + 1, idxEnd);
					startIdx = idxEnd + ITEM_SEPARATOR.length();
					tryUpdate(key, value, remoteAddress);
					lastKeys.remove(remoteAddress);
				} else {
					final String lastKey = lastKeys.remove(remoteAddress);
					if (lastKey != null) {
						final String value = message.substring(startIdx, idxEnd);
						tryUpdate(lastKey, value, remoteAddress);
					}
					startIdx = idxEnd + ITEM_SEPARATOR.length();
				}
			} else if (idxEquals > 0) {
				lastKeys.put(remoteAddress, message.substring(startIdx, idxEquals));
				startIdx = -1;
			} else {
				startIdx = -1;
			}
		} while (startIdx > 0);
	}

	private void tryUpdate(final String key, final String value, final String remoteAddress) {
		logger.debug("trying to set {} of {} to {}", key, remoteAddress, value);
		boolean updated = false;
		for (final DiyOnXBeeBindingProvider provider : providers) {

			for (final String itemName : provider.getItemNames()) {
				final String id = provider.getId(itemName);
				final String remote = provider.getRemote(itemName);

				if (key.equals(id) && remote.equals(remoteAddress)) {
					final List<Class<? extends State>> availableTypes = provider.getAvailableItemTypes(itemName);
					final State state = parseState(value, availableTypes, provider, itemName);
					if (state != null) {
						updated = true;
						eventPublisher.postUpdate(itemName, state);
					}
				}
			}
		}
		if (!updated) {
			logger.warn("unmatched item: key='{}', value='{}' from '{}'", key, value, remoteAddress);
		}
	}

	private State parseState(final String value, final List<Class<? extends State>> availableTypes,
			DiyOnXBeeBindingProvider provider, String itemName) {
		State state = TypeParser.parseState(availableTypes, value);
		if (state == null) {
			state = parseCustomState(value, availableTypes);
		} else if (state.getClass() == DecimalType.class) {
			final Integer max = provider.getMaxValue(itemName);

			if (max != null) {
				final DecimalType type = (DecimalType) state;
				final double percentage = type.doubleValue() / max.intValue() * 100d;
				return new DecimalType(percentage);
			}
		}
		return state;
	}

	private State parseCustomState(String value, List<Class<? extends State>> availableTypes) {
		for (Class<? extends State> clazz : availableTypes) {
			if (clazz == HSBType.class) {
				final State rgb = parseRGBState(value);
				if (rgb != null) {
					return rgb;
				}
			}
			if (clazz == PercentType.class) {
				final State hue = parseHUE(value);
				if (hue != null) {
					return hue;
				}
			}
		}
		return null;
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		{
			final String serialPort = (String) properties.get("serialPort");
			if (StringUtils.isNotBlank(serialPort)) {
				this.serialPort = serialPort;
			}
		}

		{
			final String baudRate = (String) properties.get("baudRate");
			if (StringUtils.isNotBlank(baudRate)) {
				this.baudRate = Integer.parseInt(baudRate);
			}
		}

		String canonical = serialPort;
		try {
			// This code below enables to use a device name in
			// /dev/serial/by-id/... on linux
			File device = new File(serialPort);
			if (device.canRead()) {
				canonical = device.getCanonicalPath();
			}
		} catch (IOException e1) {
			logger.info("unable to get canonical path for '{}'", serialPort);
			canonical = serialPort;
		}

		xbeeSetupLock.lock();
		try {
			if (xbee != null) {
				xbee.removePacketListener(this);
				xbee.close();
				xbee = null;
			}
			xbee = new XBee();

			try {
				logger.info("opening XBee communication on '{}'", canonical);
				xbee.open(canonical, baudRate);
			} catch (XBeeException e) {
				logger.error("failed to open connection to XBee module", e);
				xbee = null;
				return;
			}
		} finally {
			xbeeSetupLock.unlock();
		}

		xbee.addPacketListener(this);
	}
}
