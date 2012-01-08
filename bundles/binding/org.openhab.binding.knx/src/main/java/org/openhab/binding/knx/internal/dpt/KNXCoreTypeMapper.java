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
package org.openhab.binding.knx.internal.dpt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorDate;
import tuwien.auto.calimero.dptxlator.DPTXlatorScene;
import tuwien.auto.calimero.dptxlator.DPTXlatorString;
import tuwien.auto.calimero.dptxlator.DPTXlatorTime;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;
import tuwien.auto.calimero.exception.KNXException;

/** 
 * This class provides type mapping between all openHAB core types and KNX data point types.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class KNXCoreTypeMapper implements KNXTypeMapper {
	
	static private final Logger logger = LoggerFactory.getLogger(KNXCoreTypeMapper.class);
	
	private final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("EEE, HH:mm:ss", Locale.US);
	private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	/** stores the openHAB type class for all (supported) KNX datapoint types */
	static private Map<String, Class<? extends Type>> dptTypeMap;
	
	static {
		dptTypeMap = new HashMap<String, Class<? extends Type>>();
		dptTypeMap.put(DPTXlatorBoolean.DPT_UPDOWN.getID(), UpDownType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_STEP.getID(), IncreaseDecreaseType.class);
		dptTypeMap.put(DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID(), IncreaseDecreaseType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_SWITCH.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_PERCENT_U8.getID(), PercentType.class);
		dptTypeMap.put("9.001", DecimalType.class);
		dptTypeMap.put(DPTXlatorString.DPT_STRING_8859_1.getID(), StringType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_WINDOW_DOOR.getID(), OpenClosedType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_START.getID(), StopMoveType.class);
		dptTypeMap.put(DPTXlatorDate.DPT_DATE.getID(), DateTimeType.class);
		dptTypeMap.put(DPTXlatorTime.DPT_TIMEOFDAY.getID(), DateTimeType.class);
	}
	

	public String toDPTValue(Type type, String dpt) {
		
		if(type instanceof OnOffType) return type.toString().toLowerCase();
		if(type instanceof UpDownType) return type.toString().toLowerCase();
		if(type instanceof IncreaseDecreaseType) return type.toString().toLowerCase() + " 5";
		if(type instanceof PercentType) return mapTo8bit((PercentType) type);
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
			if(datapoint.getMainNumber()==9) id = "9.001"; // we do not care about the unit of a value, so map everything to 9.001
			Class<? extends Type> typeClass = toTypeClass(id);
	
			if(typeClass.equals(UpDownType.class)) return UpDownType.valueOf(value.toUpperCase());
			if(typeClass.equals(IncreaseDecreaseType.class)) return IncreaseDecreaseType.valueOf(StringUtils.substringBefore(value.toUpperCase(), " "));
			if(typeClass.equals(OnOffType.class)) return OnOffType.valueOf(value.toUpperCase());
			if(typeClass.equals(PercentType.class)) return PercentType.valueOf(mapToPercent(value));
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
	 * @return the openHAB type (command or state) class
	 */
	static public Class<? extends Type> toTypeClass(String dptId) {
		/*
		 * DecimalType is by default associated to 9.001, so for 12.001 
		 * or 17.001, we need to do exceptional handling
		 */
		if ("12.001".equals(dptId)) { 
			return DecimalType.class;
		}
		else if (DPTXlatorScene.DPT_SCENE_NUMBER.getID().equals(dptId)) {
			return DecimalType.class;
		}
		else {
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
		for(Entry<String, Class<? extends Type>> entry : dptTypeMap.entrySet()) {
			if(entry.getValue().equals(typeClass)) return entry.getKey();
		}
		return null;
	}

	/**
	 * Maps an 8-bit KNX percent value (range 0-255) to a "real" percent value as a string.
	 * 
	 * @param value the 8-bit KNX percent value
	 * @return the real value as a string (e.g. "99.5")
	 */
	static private String mapToPercent(String value) {
		int percent = Integer.parseInt(StringUtils.substringBefore(value.toString(), " "));
		return Integer.toString(percent * 100 / 255);
	}

	/**
	 * Maps an openHAB percent value to an 8-bit KNX percent value (0-255) as a string.
	 * The mapping is linear and starts with 0->0 and ends with 100->255.
	 * 
	 * @param type the openHAB percent value
	 * @return the 8-bit KNX percent value 
	 */
	static private String mapTo8bit(PercentType type) {
		int value = Integer.parseInt(type.toString());
		return Integer.toString(value * 255 / 100);
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
