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
package org.openhab.binding.ihc.internal;

import java.io.IOException;
import java.util.ArrayList;

import org.openhab.binding.ihc.IhcBindingProvider;
import org.openhab.binding.ihc.utcs.IhcClient;
import org.openhab.binding.ihc.utcs.IhcClient.EnumValue;
import org.openhab.binding.ihc.utcs.WSBooleanValue;
import org.openhab.binding.ihc.utcs.WSDateValue;
import org.openhab.binding.ihc.utcs.WSEnumValue;
import org.openhab.binding.ihc.utcs.WSFloatingPointValue;
import org.openhab.binding.ihc.utcs.WSIntegerValue;
import org.openhab.binding.ihc.utcs.WSResourceValue;
import org.openhab.binding.ihc.utcs.WSTimeValue;
import org.openhab.binding.ihc.utcs.WSTimerValue;
import org.openhab.binding.ihc.utcs.WSWeekdayValue;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Type;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IhcOutBinding post resource updates from openHAB internal bus to IHC / ELKO
 * LS controller.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public class IhcOutBinding extends
		AbstractEventSubscriberBinding<IhcBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcOutBinding.class);

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
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

				((WSBooleanValue) value).setValue(type == OnOffType.ON ? true : false);
			
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

				((WSBooleanValue) value).setValue(type == UpDownType.DOWN ? true : false);
			
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

}
