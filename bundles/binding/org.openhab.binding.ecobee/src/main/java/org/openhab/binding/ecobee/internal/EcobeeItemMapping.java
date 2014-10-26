/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.ecobee.internal.messages.Thermostat;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * This class manages the mapping of openHAB items to elements of the Ecobee
 * API.
 * 
 * @author John Cocula
 */
public class EcobeeItemMapping {
	private interface Mapper {

		/**
		 * Return a State object for this item mapping.
		 * 
		 * @param t
		 *            the thermostat object to inspect
		 * @return the State object derived from the thermostat
		 */
		public State getState(Thermostat t);

		/**
		 * Return the subclass of the State class whose instances would be
		 * returned when {@code #getState} is called.
		 * 
		 * @return the state class
		 */
		public Class<? extends State> getStateClass();

		/**
		 * Return <code>true</code> if Ecobee would return an acceptable data
		 * type for the given item.
		 * 
		 * @param item
		 *            the item to test
		 * @return <code>true</code> if we can return a data type that is
		 *         acceptable to this item
		 */
		public boolean isValidItemType(Item item);

		/**
		 * Return a Thermostat object with the named {@code property} set to the
		 * given {@code newState}.
		 * 
		 * @param property
		 *            the thermostat property to set
		 * @param newState
		 *            the new state to set the property to
		 * @return a thermostat object to send to the Ecobee API.
		 */
		public Thermostat getThermostat(State newState);
	}

	private static final Map<String, Mapper> mappers = new HashMap<String, Mapper>();

	private static abstract class ItemMapper implements Mapper {
		private final Class<? extends State> stateClass;

		ItemMapper(Class<? extends State> stateClass) {
			this.stateClass = stateClass;
		}

		public Class<? extends State> getStateClass() {
			return this.stateClass;
		}

		public boolean isValidItemType(Item item) {
			for (Class<? extends State> sc : item.getAcceptedDataTypes()) {
				if (sc.equals(stateClass)) {
					return true;
				}
			}
			return false;
		}
	}

	static {
		mappers.put("name", new ItemMapper(StringType.class) {
			public State getState(Thermostat t) {
				return StringType.valueOf(t.getName());
			}

			public Thermostat getThermostat(State newState) {
				Thermostat t = new Thermostat(null);
				t.setName(newState.toString());
				return t;
			}
		});
		mappers.put("isRegistered", new ItemMapper(OnOffType.class) {
			public State getState(Thermostat t) {
				return t.isRegistered() ? OnOffType.ON : OnOffType.OFF;
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		mappers.put("modelNumber", new ItemMapper(StringType.class) {
			public State getState(Thermostat t) {
				return StringType.valueOf(t.getModelNumber());
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		mappers.put("lastModified", new ItemMapper(DateTimeType.class) {
			public State getState(Thermostat t) {
				Calendar c = Calendar.getInstance();
				c.setTime(t.getLastModified());
				return new DateTimeType(c);
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		mappers.put("thermostatTime", new ItemMapper(DateTimeType.class) {
			public State getState(Thermostat t) {
				Calendar c = Calendar.getInstance();
				c.setTime(t.getThermostatTime());
				return new DateTimeType(c);
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		mappers.put("utcTime", new ItemMapper(DateTimeType.class) {
			public State getState(Thermostat t) {
				Calendar c = Calendar.getInstance();
				c.setTime(t.getUtcTime());
				return new DateTimeType(c);
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		// ...
		mappers.put("runtime/actualTemperature", new ItemMapper(
				DecimalType.class) {
			public State getState(Thermostat t) {
				return new DecimalType(t.getRuntime().getActualTemperature()
						.toLocalTemperature());
			}

			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
		mappers.put("runtime/actualHumidity",
				new ItemMapper(DecimalType.class) {
					public State getState(Thermostat t) {
						return new DecimalType(t.getRuntime()
								.getActualHumidity());
					}

					public Thermostat getThermostat(State newState) {
						return null;
					}
				});
		// ...
		mappers.put("settings/hvacMode", new ItemMapper(StringType.class) {
			public State getState(Thermostat t) {
				return StringType
						.valueOf(t.getSettings().getHvacMode().value());
			}

			public Thermostat getThermostat(State newState) {
				Thermostat t = new Thermostat(null);
				Thermostat.Settings s = new Thermostat.Settings();
				s.setHvacMode(Thermostat.HvacMode.forValue(newState.toString()));
				t.setSettings(s);
				return t;
			}
		});
		// ...
		mappers.put("version/thermostatFirmwareVersion", new ItemMapper(StringType.class) {
			public State getState(Thermostat t) {
				return StringType
						.valueOf(t.getVersion().getThermostatFirmwareVersion());
			}
			
			public Thermostat getThermostat(State newState) {
				return null;
			}
		});
	}

	/**
	 * Determine if the given property supports the given item.
	 * 
	 * @param item
	 *            the item to test
	 * @param property
	 *            the property to test
	 * @return <code>true</code> if the property supports the item
	 */
	public static boolean isValidItemType(Item item, String property) {
		Mapper r = mappers.get(property);
		return (r == null) ? false : r.isValidItemType(item);
	}

	/**
	 * Return the State object for the given property from the given thermostat.
	 * 
	 * @param t
	 *            the thermostat to inspect
	 * @param property
	 *            the property to retrieve
	 * @return the State object for the property, or <code>null</code> if the
	 *         given property is not found
	 */
	public static State getState(Thermostat t, String property) {
		Mapper r = mappers.get(property);
		return (r == null) ? null : r.getState(t);
	}

	/**
	 * Returns a Thermostat object that will contain an updated property to send
	 * to the Ecobee API.
	 */
	public static Thermostat getThermostat(String property, State newState) {
		Mapper r = mappers.get(property);
		return (r == null) ? null : r.getThermostat(newState);
	}

	/**
	 * return the State subclass whose instances would be returned by getState
	 * 
	 * @return the subclass of the State class that would be returned, or
	 *         <code>null</code> if the given property is unknown.
	 */
	public static Class<? extends State> getStateClass(String property) {
		Mapper r = mappers.get(property);
		return (r == null) ? null : r.getStateClass();
	}
}
