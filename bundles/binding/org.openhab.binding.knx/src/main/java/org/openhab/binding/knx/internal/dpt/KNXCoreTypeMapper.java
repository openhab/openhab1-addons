/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.dpt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.knx.config.KNXTypeMapper;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteSigned;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorDate;
import tuwien.auto.calimero.dptxlator.DPTXlatorSceneNumber;
import tuwien.auto.calimero.dptxlator.DPTXlatorString;
import tuwien.auto.calimero.dptxlator.DPTXlatorTime;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;
import tuwien.auto.calimero.exception.KNXException;

/** 
 * This class provides type mapping between all openHAB core types and KNX data point types.
 * 
 * @author Kai Kreuzer
 * @author Volker Daube
 * @since 0.3.0
 *
 */
public class KNXCoreTypeMapper implements KNXTypeMapper {
	
	static private final Logger logger = LoggerFactory.getLogger(KNXCoreTypeMapper.class);
	
	private final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("EEE, HH:mm:ss", Locale.US);
	private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	/** stores the openHAB type class for all (supported) KNX datapoint types */
	static private Map<String, Class<? extends Type>> dptTypeMap;

	/** stores the default KNX DPT to use for each openHAB type */
	static private Map<Class<? extends Type>, String> defaultDptMap;
	
	static {
		dptTypeMap = new HashMap<String, Class<? extends Type>>();
		// Datapoint Types "B1", Main number 1
		dptTypeMap.put(DPTXlatorBoolean.DPT_SWITCH.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_STEP.getID(), IncreaseDecreaseType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_UPDOWN.getID(), UpDownType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_START.getID(), StopMoveType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_WINDOW_DOOR.getID(), OpenClosedType.class);

		// Datapoint Types "B1U3", Main number 3
		dptTypeMap.put(DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID(), IncreaseDecreaseType.class);

		// Datapoint Types "8-Bit Unsigned Value", Main number 5
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_SCALING.getID(), PercentType.class);	
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_ANGLE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_PERCENT_U8.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_DECIMALFACTOR.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_VALUE_1_UCOUNT.getID(), DecimalType.class);

		//Datapoint Types "2-Octet Unsigned Value", Main number 7
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_VALUE_2_UCOUNT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_HOURS.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_PROP_DATATYPE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_ELECTRICAL_CURRENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_BRIGHTNESS.getID(), DecimalType.class);

		// Datapoint Types "2-Octet Float Value", Main number 9
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TEMPERATURE.getID(), DecimalType.class);

		// Datapoint Types "Time", Main number 10
		dptTypeMap.put(DPTXlatorTime.DPT_TIMEOFDAY.getID(), DateTimeType.class);

		// Datapoint Types “Date”", Main number 11
		dptTypeMap.put(DPTXlatorDate.DPT_DATE.getID(), DateTimeType.class);

		// Datapoint Types "4-Octet Signed Value", Main number 13
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_COUNT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH.getID(), DecimalType.class);

		// Datapoint Types "4-Octet Float Value", Main number 14
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ANGLE_DEG.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_FREQUENCY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_POWER.getID(), DecimalType.class);

		// Datapoint Types "String", Main number 16
		dptTypeMap.put(DPTXlatorString.DPT_STRING_8859_1.getID(),StringType.class);

		defaultDptMap = new HashMap<Class<? extends Type>, String>();
		defaultDptMap.put(OnOffType.class, DPTXlatorBoolean.DPT_SWITCH.getID());
		defaultDptMap.put(UpDownType.class, DPTXlatorBoolean.DPT_UPDOWN.getID());
		defaultDptMap.put(StopMoveType.class, DPTXlatorBoolean.DPT_START.getID());
		defaultDptMap.put(OpenClosedType.class, DPTXlatorBoolean.DPT_WINDOW_DOOR.getID());

		defaultDptMap.put(IncreaseDecreaseType.class, DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID());

		defaultDptMap.put(PercentType.class, DPTXlator8BitUnsigned.DPT_SCALING.getID());

		defaultDptMap.put(DecimalType.class, DPTXlator2ByteFloat.DPT_TEMPERATURE.getID());

		defaultDptMap.put(DateTimeType.class, DPTXlatorTime.DPT_TIMEOFDAY.getID());

		defaultDptMap.put(StringType.class,	DPTXlatorString.DPT_STRING_8859_1.getID());
	}
	

	public String toDPTValue(Type type, String dpt) {
		
		if(type instanceof OnOffType) return type.toString().toLowerCase();
		if(type instanceof UpDownType) return type.toString().toLowerCase();
		if(type instanceof IncreaseDecreaseType) return type.toString().toLowerCase() + " 5";
		if(type instanceof PercentType) return type.toString();
		if(type instanceof DecimalType) return type.toString();
		if(type instanceof StringType) return type.toString();
		if(type instanceof OpenClosedType) return type.toString().toLowerCase();
		if(type==StopMoveType.MOVE) return "start";
		if(type==StopMoveType.STOP) return "stop";
		if(type instanceof DateTimeType) return formatDateTime((DateTimeType) type, dpt);

		return null;
	}

	public Type toType(Datapoint datapoint, byte[] data) {
		try {
			DPTXlator translator = TranslatorTypes.createTranslator(datapoint.getMainNumber(), datapoint.getDPT());
			translator.setData(data);
			String value = translator.getValue();
			
			String id = translator.getType().getID();
			logger.trace("toType datapoint DPT = " + datapoint.getDPT());
			logger.trace("toType datapoint getMainNumber = " + datapoint.getMainNumber());

			if(datapoint.getMainNumber()==9) id = DPTXlator2ByteFloat.DPT_TEMPERATURE.getID(); // we do not care about the unit of a value, so map everything to 9.001
			if(datapoint.getMainNumber()==14) {
				id = DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR.getID(); // we do not care about the unit of a value, so map everything to 14.001
				/*
				* FIXME: Workaround for a bug in Calimero
				* DPTXlator4ByteFloat.makeString(). The locale is being used when
				* translating a float to String. It could happen the a ',' is used a separator, such as 3,14159E20
				* Openhab expects this to be in US format an expects '.': 3.14159E20
				* There is no issue with DPTXlator2ByteFloat since calimero is using a non-localized translation.
				*/
				if (value.contains(",")) {
					value=value.replaceFirst(",", "\\.");
				}
			}
			
			Class<? extends Type> typeClass = toTypeClass(id);
			if (typeClass == null) {
				return null;
			}
	
			if(typeClass.equals(UpDownType.class)) return UpDownType.valueOf(value.toUpperCase());
			if(typeClass.equals(IncreaseDecreaseType.class)) return IncreaseDecreaseType.valueOf(StringUtils.substringBefore(value.toUpperCase(), " "));
			if(typeClass.equals(OnOffType.class)) return OnOffType.valueOf(value.toUpperCase());
			if(typeClass.equals(PercentType.class)) return PercentType.valueOf(value.split(" ")[0]);
			if(typeClass.equals(DecimalType.class)) return DecimalType.valueOf(value.split(" ")[0]);
			if(typeClass.equals(StringType.class)) return StringType.valueOf(value);
			if(typeClass.equals(OpenClosedType.class)) return OpenClosedType.valueOf(value.toUpperCase());
			if(typeClass.equals(StopMoveType.class)) return value.equals("start")?StopMoveType.MOVE:StopMoveType.STOP;
			if(typeClass.equals(DateTimeType.class)) return DateTimeType.valueOf(formatDateTime(value, datapoint.getDPT()));
		} 
		catch (KNXException e) {
			logger.warn("Failed creating a translator for datapoint type ‘{}‘.", datapoint.getDPT(), e);
		}
		
		return null;
	}
	
	/**
	 * Converts a datapoint type id into an openHAB type class
	 * 
	 * @param dptId the datapoint type id
	 * @return the openHAB type (command or state) class or {@code null} if the datapoint type id is not supported.
	 */
	static public Class<? extends Type> toTypeClass(String dptId) {
		/*
		 * DecimalType is by default associated to 9.001, so for 12.001, 14.001 
		 * or 17.001, we need to do exceptional handling
		 */
		logger.trace("toTypeClass looking for dptId = " + dptId);
		if (DPTXlator4ByteUnsigned.DPT_VALUE_4_UCOUNT.getID().equals(dptId)) { 
			return DecimalType.class;
		} else if (DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR.getID().equals(dptId)) {
			return DecimalType.class;
		} else if (DPTXlatorSceneNumber.DPT_SCENE_NUMBER.getID().equals(dptId)) {
			return DecimalType.class;
		} else {
			return dptTypeMap.get(dptId);
		}
	}

	/**
	 * Converts an openHAB type class into a datapoint type id.
	 * 
	 * @param typeClass the openHAB type class
	 * @return the datapoint type id
	 */
	static public String toDPTid(Class<? extends Type> typeClass) {
		return defaultDptMap.get(typeClass);
	}

	/**
	 * Formats the given <code>value</code> according to the datapoint type
	 * <code>dpt</code> to a String which can be processed by {@link DateTimeType}.
	 * 
	 * @param value
	 * @param dpt
	 * 
	 * @return a formatted String like </code>yyyy-MM-dd'T'HH:mm:ss</code> which
	 * is target format of the {@link DateTimeType}
	 */
	private String formatDateTime(String value, String dpt) {
		Date date = null;
		
		try {
			if (DPTXlatorDate.DPT_DATE.getID().equals(dpt)) {
				date = DATE_FORMATTER.parse(value);
			}
			else if (DPTXlatorTime.DPT_TIMEOFDAY.getID().equals(dpt)) {
				date = TIME_FORMATTER.parse(value);
			}
		}
		catch (ParseException pe) {
			// do nothing but logging
			logger.warn("Could not parse '{}' to a valid date", value);
		}

		return date != null ? DateTimeType.DATE_FORMATTER.format(date) : "";
	}

	/**
	 * Formats the given internal <code>dateType</code> to a knx readable String
	 * according to the target datapoint type <code>dpt</code>.
	 * 
	 * @param dateType
	 * @param dpt the target datapoint type 
	 * 
	 * @return a String which contains either an ISO8601 formatted date (yyyy-mm-dd) or
	 * a formatted 24-hour clock with the day of week prepended (Mon, 12:00:00)
	 * 
	 * @throws IllegalArgumentException if none of the datapoint types DPT_DATE or
	 * DPT_TIMEOFDAY has been used.
	 */
	static private String formatDateTime(DateTimeType dateType, String dpt) {
		if (DPTXlatorDate.DPT_DATE.getID().equals(dpt)) {
			return dateType.format("%tF");
		}
		else if (DPTXlatorTime.DPT_TIMEOFDAY.getID().equals(dpt)) {
			return dateType.format(Locale.US, "%1$ta, %1$tT");
		}
		else {
			throw new IllegalArgumentException("Could not format date to datapoint type '" + dpt + "'");
		}
	}
	
	
}
