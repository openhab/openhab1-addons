/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.binding.nest.internal.messages.Structure.AwayState;
import org.openhab.binding.nest.internal.messages.Thermostat.HvacMode;
import org.openhab.binding.nest.internal.messages.Thermostat.HvacState;
import org.openhab.binding.nest.internal.messages.SmokeCOAlarm.AlarmState;
import org.openhab.binding.nest.internal.messages.SmokeCOAlarm.BatteryHealth;
import org.openhab.binding.nest.internal.messages.SmokeCOAlarm.ColorState;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;

/**
 * The DataModel Java Bean represents the entire Nest API data model.
 * 
 * @see <a href="https://developer.nest.com/documentation/api-reference">API Reference</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataModel extends AbstractMessagePart {

	private static BeanUtilsBean beanUtils;
	private static PropertyUtilsBean propertyUtils;
	static {
		/**
		 * Configure BeanUtilsBean to use our converters and resolver.
		 */
		ConvertUtilsBean convertUtils = new ConvertUtilsBean();

		// Register bean type converters
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return AwayState.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, AwayState.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return HvacMode.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, HvacMode.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return HvacState.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, HvacState.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return BatteryHealth.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, BatteryHealth.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return AlarmState.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, AlarmState.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof StringType) {
					return ColorState.forValue(value.toString());
				} else {
					return null;
				}
			}
		}, ColorState.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof DecimalType) {
					return ((DecimalType) value).intValue();
				} else {
					return null;
				}
			}
		}, Integer.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof OnOffType) {
					return ((OnOffType) value) == OnOffType.ON;
				} else {
					return null;
				}
			}
		}, Boolean.class);
		convertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				return value.toString();
			}
		}, String.class);

		propertyUtils = new PropertyUtilsBean();
		propertyUtils.setResolver(new DataModelPropertyResolver());
		beanUtils = new BeanUtilsBean(convertUtils, propertyUtils);
	}

	@JsonProperty("devices")
	private Devices devices;
	@JsonProperty("structures")
	private Map<String, Structure> structures_by_id;
	@JsonIgnore
	private Map<String, Structure> structures_by_name;
	@JsonIgnore
	Date last_connection;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Devices extends AbstractMessagePart implements DataModelElement {
		private Map<String, Thermostat> thermostats_by_id;
		@JsonIgnore
		private Map<String, Thermostat> thermostats_by_name;
		private Map<String, SmokeCOAlarm> smoke_co_alarms_by_id;
		@JsonIgnore
		private Map<String, SmokeCOAlarm> smoke_co_alarms_by_name;
		private Map<String, Camera> cameras_by_id;
		@JsonIgnore
		private Map<String, Camera> cameras_by_name;

		/**
		 * @return the thermostats_by_id
		 */
		@JsonProperty("thermostats")
		public Map<String, Thermostat> getThermostats_by_id() {
			return this.thermostats_by_id;
		}

		/**
		 * @param thermostats
		 *            the thermostats to set (mapped by ID)
		 */
		@JsonProperty("thermostats")
		public void setThermostats_by_id(Map<String, Thermostat> thermostats_by_id) {
			this.thermostats_by_id = thermostats_by_id;
		}

		/**
		 * Return the thermostats map, mapped by name.
		 * 
		 * @return the thermostats_by_name;
		 */
		@JsonIgnore
		public Map<String, Thermostat> getThermostats() {
			return this.thermostats_by_name;
		}

		/**
		 * @return the smoke_co_alarms_by_id
		 */
		@JsonProperty("smoke_co_alarms")
		public Map<String, SmokeCOAlarm> getSmoke_co_alarms_by_id() {
			return this.smoke_co_alarms_by_id;
		}

		/**
		 * @param smoke_co_alarms
		 *            the smoke_co_alarms to set (mapped by ID)
		 */
		@JsonProperty("smoke_co_alarms")
		public void setSmoke_co_alarms_by_id(Map<String, SmokeCOAlarm> smoke_co_alarms_by_id) {
			this.smoke_co_alarms_by_id = smoke_co_alarms_by_id;
		}

		/**
		 * @return the smoke_co_alarms_by_name
		 */
		@JsonIgnore
		public Map<String, SmokeCOAlarm> getSmoke_co_alarms() {
			return this.smoke_co_alarms_by_name;
		}

		/**
		 * @return the cameras_by_id
		 */
		@JsonProperty("cameras")
		public Map<String, Camera> getCameras_by_id() {
			return this.cameras_by_id;
		}

		/**
		 * @param cameras_by_id
		 *            the cameras to set (mapped by ID)
		 */
		@JsonProperty("cameras")
		public void setCameras_by_id(Map<String, Camera> cameras_by_id) {
			this.cameras_by_id = cameras_by_id;
		}

		/**
		 * @return the cameras_by_name
		 */
		@JsonIgnore
		public Map<String, Camera> getCameras() {
			return this.cameras_by_name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void sync(DataModel dataModel) {
			// Create our map of Thermostat names to objects
			this.thermostats_by_name = new HashMap<String, Thermostat>();
			if (this.thermostats_by_id != null) {
				for (Thermostat thermostat : this.thermostats_by_id.values()) {
					thermostat.sync(dataModel);
					this.thermostats_by_name.put(thermostat.getName(), thermostat);
				}
			}
			// Create our map of SmokeCOAlarm names to objects
			this.smoke_co_alarms_by_name = new HashMap<String, SmokeCOAlarm>();
			if (this.smoke_co_alarms_by_id != null) {
				for (SmokeCOAlarm smoke_co_alarm : this.smoke_co_alarms_by_id.values()) {
					smoke_co_alarm.sync(dataModel);
					this.smoke_co_alarms_by_name.put(smoke_co_alarm.getName(), smoke_co_alarm);
				}
			}
			// Create our map of Camera names to objects
			this.cameras_by_name = new HashMap<String, Camera>();
			if (this.cameras_by_id != null) {
				for (Camera camera : this.cameras_by_id.values()) {
					camera.sync(dataModel);
					this.cameras_by_name.put(camera.getName(), camera);
				}
			}
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("thermostats", this.thermostats_by_id);
			builder.append("smoke_co_alarms", this.smoke_co_alarms_by_id);
			builder.append("cameras", this.cameras_by_id);

			return builder.toString();
		}
	}

	public DataModel() {
	}

	/**
	 * Use a dialect of the BeanUtils property resolver that URL-decodes the keys that are used to retrieve mapped
	 * objects.
	 */
	public static class DataModelPropertyResolver extends org.apache.commons.beanutils.expression.DefaultResolver {
		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("deprecation")
		public String getKey(String expression) {
			String key = super.getKey(expression);
			if (key != null) {
				try {
					return URLDecoder.decode(key, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					return URLDecoder.decode(key);
				}
			}
			return null;
		}
	}

	/**
	 * Return a JavaBean property by name.
	 * 
	 * @param name
	 *            the named property to return
	 * @return the named property's value
	 * @see PropertyUtils#getProperty()
	 * @throws IllegalAccessException
	 *             if the caller does not have access to the property accessor method
	 * @throws InvocationTargetException
	 *             if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             if the accessor method for this property cannot be found
	 */
	public Object getProperty(String name) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		return propertyUtils.getProperty(this, name);
	}

	/**
	 * Set the specified property value, performing type conversions as required to conform to the type of the
	 * destination property.
	 * 
	 * @param name
	 *            property name (can be nested/indexed/mapped/combo)
	 * @param value
	 *            value to be set
	 * @throws IllegalAccessException
	 *             if the caller does not have access to the property accessor method
	 * @throws InvocationTargetException
	 *             if the property accessor method throws an exception
	 */
	public void setProperty(String name, Object value) throws IllegalAccessException, InvocationTargetException {

		beanUtils.setProperty(this, name, value);
	}

	/**
	 * @return the devices
	 */
	@JsonProperty("devices")
	public Devices getDevices() {
		return this.devices;
	}

	/**
	 * @param devices
	 *            the devices to set
	 */
	@JsonProperty("devices")
	public void setDevices(Devices devices) {
		this.devices = devices;
	}

	/**
	 * Convenience method so property specs don't have to include "devices." in each one.
	 * 
	 * @return name-based map of thermostats
	 */
	@JsonIgnore
	public Map<String, Thermostat> getThermostats() {
		return (devices == null) ? null : devices.getThermostats();
	}

	/**
	 * Convenience method so property specs don't have to include "devices." in each one.
	 * 
	 * @return name-based map of smoke_co_alarms
	 */
	@JsonIgnore
	public Map<String, SmokeCOAlarm> getSmoke_co_alarms() {
		return (devices == null) ? null : devices.getSmoke_co_alarms();
	}

	/**
	 * Convenience method so property specs don't have to include "devices." in each one.
	 * 
	 * @return name-based map of cameras
	 */
	@JsonIgnore
	public Map<String, Camera> getCameras() {
		return (devices == null) ? null : devices.getCameras();
	}

	/**
	 * @return the structures
	 */
	@JsonProperty("structures")
	public Map<String, Structure> getStructures_by_id() {
		return this.structures_by_id;
	}

	/**
	 * @param structures_by_id
	 *            the ID-keyed structure map to set
	 */
	@JsonProperty("structures")
	public void setStructures_by_id(Map<String, Structure> structures_by_id) {
		this.structures_by_id = structures_by_id;
	}

	@JsonIgnore
	public Map<String, Structure> getStructures() {
		return this.structures_by_name;
	}

	/**
	 * @param structures_by_name
	 *            the name-keyed structure map to set
	 */
	public void setStructures_by_name(final Map<String, Structure> structures_by_name) {
		this.structures_by_name = structures_by_name;
	}

	/**
	 * @param last_connection
	 *            the last time we obtained the data model from the Nest API
	 */
	@JsonIgnore
	public void setLast_connection(final Date last_connection) {
		this.last_connection = last_connection;
	}

	/**
	 * @return the last_connection
	 */
	@JsonIgnore
	public Date getLast_connection() {
		return this.last_connection;
	}

	/**
	 * Visit each data model element and call its sync method with the root data model element (this). Do this right
	 * after it's been unmarshalled from JSON.
	 */
	public void sync() {
		this.structures_by_name = new HashMap<String, Structure>();
		if (this.structures_by_id != null) {
			for (Structure st : this.structures_by_id.values()) {
				this.structures_by_name.put(st.getName(), st);
				st.sync(this);
			}
		}
		if (this.devices != null) {
			this.devices.sync(this);
		}
	}

	/**
	 * This method returns a new data model containing only the affected Structure, Thermostat, SmokeCOAlarm or Camera,
	 * and only the property of the bean that was changed. This new DataModel object can be sent to the Nest API in
	 * order to perform an update via HTTP PUT.
	 * 
	 * @param property
	 *            the property to change
	 * @param newState
	 *            the new state to set for the property
	 * @return a new data model that only contains the affected bean and only the property set
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public DataModel updateDataModel(String property, Object newState) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		/**
		 * Find the Structure, Thermostat, SmokeCOAlarm or Camera that the given property is trying to update.
		 */
		Object oldObject = null;
		String beanProperty = property;
		do {
			Object obj = this.getProperty(beanProperty);
			if (obj instanceof Structure || obj instanceof Thermostat || obj instanceof SmokeCOAlarm
					|| obj instanceof Camera) {
				oldObject = obj;
				break;
			}
			if (beanProperty.indexOf('.') != -1) {
				beanProperty = beanProperty.substring(0, beanProperty.lastIndexOf('.'));
			} else {
				break;
			}
		} while (beanProperty.length() > 0);

		/**
		 * Now based on the type of the object, create a new DataModel that has an empty one, properly mapped by ID and
		 * by name.
		 */
		DataModel updateDataModel = null;

		if (oldObject != null) {
			if (oldObject instanceof Structure) {
				String structureId = ((Structure) oldObject).getStructure_id();
				String structureName = ((Structure) oldObject).getName();

				updateDataModel = new DataModel();
				Structure structure = new Structure(null);
				updateDataModel.structures_by_id = new HashMap<String, Structure>();
				updateDataModel.structures_by_id.put(structureId, structure);
				updateDataModel.structures_by_name = new HashMap<String, Structure>();
				updateDataModel.structures_by_name.put(structureName, structure);
			} else if (oldObject instanceof Thermostat) {
				String deviceId = ((Thermostat) oldObject).getDevice_id();
				String deviceName = ((Thermostat) oldObject).getName();

				updateDataModel = new DataModel();
				updateDataModel.devices = new Devices();
				Thermostat thermostat = new Thermostat(null);
				updateDataModel.devices.thermostats_by_id = new HashMap<String, Thermostat>();
				updateDataModel.devices.thermostats_by_id.put(deviceId, thermostat);
				updateDataModel.devices.thermostats_by_name = new HashMap<String, Thermostat>();
				updateDataModel.devices.thermostats_by_name.put(deviceName, thermostat);
			} else if (oldObject instanceof SmokeCOAlarm) {
				String deviceId = ((SmokeCOAlarm) oldObject).getDevice_id();
				String deviceName = ((SmokeCOAlarm) oldObject).getName();

				updateDataModel = new DataModel();
				updateDataModel.devices = new Devices();
				SmokeCOAlarm smokeCOAlarm = new SmokeCOAlarm(null);
				updateDataModel.devices.smoke_co_alarms_by_id = new HashMap<String, SmokeCOAlarm>();
				updateDataModel.devices.smoke_co_alarms_by_id.put(deviceId, smokeCOAlarm);
				updateDataModel.devices.smoke_co_alarms_by_name = new HashMap<String, SmokeCOAlarm>();
				updateDataModel.devices.smoke_co_alarms_by_name.put(deviceName, smokeCOAlarm);
			} else if (oldObject instanceof Camera) {
				String deviceId = ((Camera) oldObject).getDevice_id();
				String deviceName = ((Camera) oldObject).getName();

				updateDataModel = new DataModel();
				updateDataModel.devices = new Devices();
				Camera camera = new Camera(null);
				updateDataModel.devices.cameras_by_id = new HashMap<String, Camera>();
				updateDataModel.devices.cameras_by_id.put(deviceId, camera);
				updateDataModel.devices.cameras_by_name = new HashMap<String, Camera>();
				updateDataModel.devices.cameras_by_name.put(deviceName, camera);
			}
		}

		/**
		 * Lastly, set the property into the update data model
		 * 
		 * TODO: cannot update a binding string of the form "=[structures(Name).thermostats(Name).X]" or
		 * "=[structures(Name).smoke_co_alarms(Name).X]" because the name-based map of structures is not present in the
		 * updateDataModel
		 */
		if (updateDataModel != null) {
			updateDataModel.setProperty(property, newState);
			updateDataModel.structures_by_name = null;
			if (updateDataModel.devices != null) {
				updateDataModel.devices.smoke_co_alarms_by_name = null;
				updateDataModel.devices.thermostats_by_name = null;
				updateDataModel.devices.cameras_by_name = null;
			}
		}

		return updateDataModel;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("devices", this.devices);
		builder.append("structures", this.structures_by_id);

		return builder.toString();
	}
}
