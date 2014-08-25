/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.dpt;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.awt.Color;

import org.openhab.binding.knx.config.KNXTypeMapper;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
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
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator1BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteSigned;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorDate;
import tuwien.auto.calimero.dptxlator.DPTXlatorDateTime;
import tuwien.auto.calimero.dptxlator.DPTXlatorRGB;
import tuwien.auto.calimero.dptxlator.DPTXlatorSceneControl;
import tuwien.auto.calimero.dptxlator.DPTXlatorSceneNumber;
import tuwien.auto.calimero.dptxlator.DPTXlatorString;
import tuwien.auto.calimero.dptxlator.DPTXlatorTime;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;

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

	private final static SimpleDateFormat TIME_DAY_FORMATTER = new SimpleDateFormat("EEE, HH:mm:ss", Locale.US);
	private final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss", Locale.US);
	private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	/** stores the openHAB type class for all (supported) KNX datapoint types */
	static private Map<String, Class<? extends Type>> dptTypeMap;

	/** stores the default KNX DPT to use for each openHAB type */
	static private Map<Class<? extends Type>, String> defaultDptMap;

	static {
		dptTypeMap = new HashMap<String, Class<? extends Type>>();

		// Datapoint Types "B1", Main number 1
		dptTypeMap.put(DPTXlatorBoolean.DPT_SWITCH.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_BOOL.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_ENABLE.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_RAMP.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_ALARM.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_BINARYVALUE.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_STEP.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_UPDOWN.getID(), UpDownType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_OPENCLOSE.getID(), OpenClosedType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_START.getID(), StopMoveType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_STATE.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_INVERT.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_DIMSENDSTYLE.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_INPUTSOURCE.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_RESET.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_OCCUPANCY.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_WINDOW_DOOR.getID(), OpenClosedType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_LOGICAL_FUNCTION.getID(), OnOffType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_SCENE_AB.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlatorBoolean.DPT_SHUTTER_BLINDS_MODE.getID(), OnOffType.class);

		// Datapoint Types "B2", Main number 2
		dptTypeMap.put(DPTXlator1BitControlled.DPT_SWITCH_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_BOOL_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_ENABLE_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_RAMP_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_ALARM_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_BINARY_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_STEP_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_UPDOWN_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_OPENCLOSE_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_START_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_STATE_CONTROL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator1BitControlled.DPT_INVERT_CONTROL.getID(), DecimalType.class);

		// Datapoint Types "B1U3", Main number 3
		dptTypeMap.put(DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID(), IncreaseDecreaseType.class);

		// Datapoint Types "8-Bit Unsigned Value", Main number 5
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_SCALING.getID(), PercentType.class);	
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_ANGLE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_PERCENT_U8.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_DECIMALFACTOR.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_TARIFF.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator8BitUnsigned.DPT_VALUE_1_UCOUNT.getID(), DecimalType.class);

		//Datapoint Types "2-Octet Unsigned Value", Main number 7
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_VALUE_2_UCOUNT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_10.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_100.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_SEC.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_MIN.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_HOURS.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_PROP_DATATYPE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_LENGTH.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_ELECTRICAL_CURRENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteUnsigned.DPT_BRIGHTNESS.getID(), DecimalType.class);

		// Datapoint Types "2-Octet Float Value", Main number 9
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TEMPERATURE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TEMPERATURE_DIFFERENCE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TEMPERATURE_GRADIENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_WIND_SPEED.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_AIR_PRESSURE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_HUMIDITY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_AIRQUALITY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE1.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE2.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_VOLTAGE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_ELECTRICAL_CURRENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_POWERDENSITY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_KELVIN_PER_PERCENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_POWER.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_VOLUME_FLOW.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_RAIN_AMOUNT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_TEMP_F.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator2ByteFloat.DPT_WIND_SPEED_KMH.getID(), DecimalType.class);

		// Datapoint Types "Time", Main number 10
		dptTypeMap.put(DPTXlatorTime.DPT_TIMEOFDAY.getID(), DateTimeType.class);

		// Datapoint Types “Date”", Main number 11
		dptTypeMap.put(DPTXlatorDate.DPT_DATE.getID(), DateTimeType.class);

		// Datapoint Types "4-Octet Unsigned Value", Main number 12
		dptTypeMap.put(DPTXlator4ByteUnsigned.DPT_VALUE_4_UCOUNT.getID(), DecimalType.class);

		// Datapoint Types "4-Octet Signed Value", Main number 13
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_COUNT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_FLOWRATE.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_REACTIVE_ENERGY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_REACTIVE_ENERGY_KVARH.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteSigned.DPT_DELTA_TIME.getID(), DecimalType.class);

		// Datapoint Types "4-Octet Float Value", Main number 14
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ANGLE_DEG.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_FREQUENCY.getID(), DecimalType.class);
		dptTypeMap.put(DPTXlator4ByteFloat.DPT_POWER.getID(), DecimalType.class);

		// Datapoint Types "String", Main number 16
		dptTypeMap.put(DPTXlatorString.DPT_STRING_8859_1.getID(), StringType.class);

		// Datapoint Types "Scene Number", Main number 17
		dptTypeMap.put(DPTXlatorSceneNumber.DPT_SCENE_NUMBER.getID(), DecimalType.class);

		// Datapoint Types "Scene Control", Main number 18
		dptTypeMap.put(DPTXlatorSceneControl.DPT_SCENE_CONTROL.getID(), DecimalType.class);

		// Datapoint Types "DateTime", Main number 19
		dptTypeMap.put(DPTXlatorDateTime.DPT_DATE_TIME.getID(), DateTimeType.class);

		// Datapoint Types "RGB Color", Main number 232
		dptTypeMap.put(DPTXlatorRGB.DPT_RGB.getID(), HSBType.class);

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

		defaultDptMap.put(HSBType.class, DPTXlatorRGB.DPT_RGB.getID());
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.knx.config.KNXTypeMapper#toDPTValue(org.openhab.core.types.Type, java.lang.String)
	 */
	public String toDPTValue(Type type, String dptID) {

		DPT dpt;
		int mainNumber = getMainNumber(dptID);
		if (mainNumber == -1) {
			logger.error("toDPTValue couldn't identify mainnumber in dptID: {}",dptID);
			return null;
		}

		try {
			DPTXlator translator = TranslatorTypes.createTranslator(mainNumber, dptID);
			dpt=translator.getType();

		} catch (KNXException e) {
			e.printStackTrace();
			return null;
		}

		//check for HSBType first, because it extends PercentType as well
		if(type instanceof HSBType) {
			Color color = ((HSBType)type).toColor();
		
			return "r:" + Integer.toString(color.getRed()) + " g:" + Integer.toString(color.getGreen()) + " b:" + Integer.toString(color.getBlue());
		} else if(type instanceof OnOffType) {
			return type.equals(OnOffType.OFF)?dpt.getLowerValue():dpt.getUpperValue();
		} else if(type instanceof UpDownType) {
			return type.equals(UpDownType.UP)?dpt.getLowerValue():dpt.getUpperValue();
		} else if(type instanceof IncreaseDecreaseType) {
			DPT valueDPT = ((DPTXlator3BitControlled.DPT3BitControlled) dpt).getControlDPT();
			return type.equals(IncreaseDecreaseType.DECREASE)?valueDPT.getLowerValue()+" 5":valueDPT.getUpperValue()+" 5";
		} else if(type instanceof OpenClosedType) {
			return type.equals(OpenClosedType.CLOSED)?dpt.getLowerValue():dpt.getUpperValue();
		} else if(type instanceof StopMoveType) {
			return type.equals(StopMoveType.STOP)?dpt.getLowerValue():dpt.getUpperValue();
		} else if(type instanceof PercentType) {
			return type.toString();
		} else if(type instanceof DecimalType) {
			switch (mainNumber) {
			case 2:
				DPT valueDPT = ((DPTXlator1BitControlled.DPT1BitControlled) dpt).getValueDPT();
				switch (((DecimalType) type).intValue()) {
				case 0: return "0 "+valueDPT.getLowerValue();
				case 1: return "0 "+valueDPT.getUpperValue();
				case 2: return "1 "+valueDPT.getLowerValue();
				default:
					return "1 "+valueDPT.getUpperValue();
				}
			case 18:
				int intVal=((DecimalType) type).intValue();
				if (intVal > 63) {
					return "learn "+(intVal-0x80);
				}
				else {
					return "activate "+intVal;
				}
			default:
				return type.toString();
			}
		} else if(type instanceof StringType){
			return type.toString();
		} else if(type instanceof DateTimeType) {
			return formatDateTime((DateTimeType) type, dptID);
		}

		logger.debug("toDPTValue: Couldn't get value for {} dpt id {} (no mapping).", type, dptID);

		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.knx.config.KNXTypeMapper#toType(tuwien.auto.calimero.datapoint.Datapoint, byte[])
	 */
	public Type toType(Datapoint datapoint, byte[] data) {
		try {
			DPTXlator translator = TranslatorTypes.createTranslator(datapoint.getMainNumber(), datapoint.getDPT());
			translator.setData(data);
			String value = translator.getValue();

			String id = translator.getType().getID();
			logger.trace("toType datapoint DPT = " + datapoint.getDPT());

			int mainNumber = getMainNumber(id);
			if (mainNumber == -1) {
				logger.debug("toType: couldn't identify mainnumber in dptID: {}.", id);
				return null;
			}
			int subNumber = getSubNumber(id);
			if (subNumber == -1) {
				logger.debug("toType: couldn't identify su number in dptID: {}.", id);
				return null;
			}
			/*
			 *  Following code section deals with specific mapping of values from KNX to openHAB types were the String
			 *  received from the DPTXlator is not sufficient to set the openHAB type or has bugs    
			 */
			switch (mainNumber) {
			case 1:
				DPTXlatorBoolean translatorBoolean = (DPTXlatorBoolean) translator;
				switch (subNumber) {
				case 8:
					return translatorBoolean.getValueBoolean() ? UpDownType.DOWN:UpDownType.UP;
				case 9:
					return translatorBoolean.getValueBoolean() ? OpenClosedType.OPEN:OpenClosedType.CLOSED;
				case 10:
					return translatorBoolean.getValueBoolean() ? StopMoveType.MOVE:StopMoveType.STOP;
				case 19:
					return translatorBoolean.getValueBoolean() ? OpenClosedType.OPEN:OpenClosedType.CLOSED;
				case 22:
					return DecimalType.valueOf(translatorBoolean.getValueBoolean() ? "1": "0");
				default:
					return translatorBoolean.getValueBoolean() ? OnOffType.ON:OnOffType.OFF;
				}
			case 2:
				DPTXlator1BitControlled translator1BitControlled =  (DPTXlator1BitControlled) translator;
				int decValue= (translator1BitControlled.getControlBit() ? 2:0) + (translator1BitControlled.getValueBit() ? 1:0) ; 
				return new DecimalType (decValue);
			case 3:
				DPTXlator3BitControlled translator3BitControlled = (DPTXlator3BitControlled) translator;
				if (translator3BitControlled.getStepCode()==0) {
					// Not supported: break
					logger.debug("toType: KNX DPT_Control_Dimming: break ignored.");
					return null;
				}
				switch (subNumber) {
				case 7:
					return translator3BitControlled.getControlBit() ? IncreaseDecreaseType.INCREASE:IncreaseDecreaseType.DECREASE;
				case 8:
					return translator3BitControlled.getControlBit() ? UpDownType.DOWN:UpDownType.UP;
				}
			case 14:
				/*
				 * FIXME: Workaround for a bug in Calimero / Openhab DPTXlator4ByteFloat.makeString(): is using a locale when
				 * translating a Float to String. It could happen the a ',' is used as separator, such as 3,14159E20.
				 * Openhab's DecimalType expects this to be in US format and expects '.': 3.14159E20.
				 * There is no issue with DPTXlator2ByteFloat since calimero is using a non-localized translation there.
				 */
				DPTXlator4ByteFloat translator4ByteFloat = (DPTXlator4ByteFloat) translator;
				Float f=translator4ByteFloat.getValueFloat();
				if (Math.abs(f) < 100000) {
					value=String.valueOf(f);
				}
				else {
					NumberFormat dcf = NumberFormat.getInstance(Locale.US);
					if (dcf instanceof DecimalFormat) {
						((DecimalFormat) dcf).applyPattern("0.#####E0");
					}
					value = dcf.format(f);
				}
				break;
			case 18:
				DPTXlatorSceneControl translatorSceneControl = (DPTXlatorSceneControl) translator;
				int decimalValue=translatorSceneControl.getSceneNumber();
				if (value.startsWith("learn")) {
					decimalValue += 0x80;
				}
				value = String.valueOf(decimalValue);
				
			break;
			case 19:
				DPTXlatorDateTime translatorDateTime = (DPTXlatorDateTime) translator;
				if (translatorDateTime.isFaultyClock()) {
					//Not supported: faulty clock
					logger.debug("toType: KNX clock msg ignored: clock faulty bit set, which is not supported");
					return null;
				}
				else if (!translatorDateTime.isValidField(DPTXlatorDateTime.YEAR) && translatorDateTime.isValidField(DPTXlatorDateTime.DATE)){
					//Not supported: "/1/1" (month and day without year)
					logger.debug("toType: KNX clock msg ignored: no year, but day and month, which is not supported");
					return null;
				}
				else if (translatorDateTime.isValidField(DPTXlatorDateTime.YEAR) && !translatorDateTime.isValidField(DPTXlatorDateTime.DATE)){
					//Not supported: "1900" (year without month and day)
					logger.debug("toType: KNX clock msg ignored: no day and month, but year, which is not supported");
					return null;
				}
				else if (!translatorDateTime.isValidField(DPTXlatorDateTime.YEAR)
						&& !translatorDateTime.isValidField(DPTXlatorDateTime.DATE)
						&& !translatorDateTime.isValidField(DPTXlatorDateTime.TIME)) {
					// Not supported: No year, no date and no time
					logger.debug("toType: KNX clock msg ignored: no day and month or year, which is not supported");
					return null;
				}				

				Calendar cal = Calendar.getInstance();
				if (translatorDateTime.isValidField(DPTXlatorDateTime.YEAR) && !translatorDateTime.isValidField(DPTXlatorDateTime.TIME)) {
					// Pure date format, no time information
					cal.setTimeInMillis(translatorDateTime.getValueMilliseconds());
					value=DateTimeType.DATE_FORMATTER.format(cal.getTime());
					return DateTimeType.valueOf(value);
				}
				else if (!translatorDateTime.isValidField(DPTXlatorDateTime.YEAR) && translatorDateTime.isValidField(DPTXlatorDateTime.TIME)) {
					// Pure time format, no date information
					cal.clear();
					cal.set(Calendar.HOUR_OF_DAY, translatorDateTime.getHour());
					cal.set(Calendar.MINUTE, translatorDateTime.getHour());
					cal.set(Calendar.SECOND, translatorDateTime.getSecond());
					value=DateTimeType.DATE_FORMATTER.format(cal.getTime());
					return DateTimeType.valueOf(value);
				}
				else if (translatorDateTime.isValidField(DPTXlatorDateTime.YEAR) && translatorDateTime.isValidField(DPTXlatorDateTime.TIME)) {
					// Date format and time information
					cal.setTimeInMillis(translatorDateTime.getValueMilliseconds());
					value=DateTimeType.DATE_FORMATTER.format(cal.getTime());
					return DateTimeType.valueOf(value);
				}
				break;
			}

			Class<? extends Type> typeClass = toTypeClass(id);
			if (typeClass == null) {
				return null;
			}

			if(typeClass.equals(PercentType.class)) return PercentType.valueOf(value.split(" ")[0]);
			if(typeClass.equals(DecimalType.class)) {
				return DecimalType.valueOf(value.split(" ")[0]);
			}
			if(typeClass.equals(StringType.class)) return StringType.valueOf(value);

			if(typeClass.equals(DateTimeType.class)) {
				String date=formatDateTime(value, datapoint.getDPT());
				if ((date == null) || (date.isEmpty())) {
					logger.debug("toType: KNX clock msg ignored: date object null or empty {}.", date);
					return null;
				}
				else {
					return DateTimeType.valueOf(date);
				}
			}

			if(typeClass.equals(HSBType.class)) {
				//value has format of "r:<red value> g:<green value> b:<blue value>"
				int r = Integer.parseInt (value.split(" ")[0].split(":")[1]);
				int g = Integer.parseInt (value.split(" ")[1].split(":")[1]);
				int b = Integer.parseInt (value.split(" ")[2].split(":")[1]);

				Color color = new Color (r, g, b);
				return new HSBType(color);
			}
		}
		catch (KNXFormatException kfe) {
			logger.info("Translator couldn't parse data for datapoint type ‘{}‘ (KNXFormatException).", datapoint.getDPT());
		}
		catch (KNXIllegalArgumentException kiae) {
			logger.info("Translator couldn't parse data for datapoint type ‘{}‘ (KNXIllegalArgumentException).", datapoint.getDPT());
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
		logger.trace("toTypeClass looking for dptId = " + dptId);
		return dptTypeMap.get(dptId);
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
				if (value.contains("no-day, ")) {
					/* 
					 * KNX "no-day" needs special treatment since openHAB's DateTimeType doesn't support "no-day".
					 * Workaround: remove the "no-day" String, parse the remaining time string, which will result in a date of "1970-01-01".
					 * Increase the month value as a marker, that "no-day" was in the KNX message. This shouldn't matter since year, month and day
					 * haven't been set anyways.
					 */
					StringBuffer stb = new StringBuffer(value);
					int start =stb.indexOf("no-day, ");
					int end =start+"no-day, ".length();
					stb.delete(start, end);
					value = stb.toString();

					date = TIME_FORMATTER.parse(value);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.MONTH, 2);
					date = cal.getTime();
				}
				else {
					date = TIME_DAY_FORMATTER.parse(value);
				}
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
	 * @return a String which contains either an ISO8601 formatted date (yyyy-mm-dd),
	 * a formatted 24-hour clock with the day of week prepended (Mon, 12:00:00) or
	 * a formatted 24-hour clock (12:00:00)
	 * 
	 * @throws IllegalArgumentException if none of the datapoint types DPT_DATE or
	 * DPT_TIMEOFDAY has been used.
	 */
	static private String formatDateTime(DateTimeType dateType, String dpt) {
		if (DPTXlatorDate.DPT_DATE.getID().equals(dpt)) {
			return dateType.format("%tF");
		}
		else if (DPTXlatorTime.DPT_TIMEOFDAY.getID().equals(dpt)) {
			/*
			 * Check if the calendar's month was set to February. This is "marker" indicating
			 * that actually "no-day" was set. (see {@link private String formatDateTime(String value, String dpt)} above)
			 */
			Calendar cal=dateType.getCalendar();
			if (cal.get(Calendar.MONTH)==2) {
				return dateType.format(Locale.US, "%1$tT");
			}
			else {
				return dateType.format(Locale.US, "%1$ta, %1$tT");
			}
		}
		else if (DPTXlatorDateTime.DPT_DATE_TIME.getID().equals(dpt)) {
			return dateType.format(Locale.US, "%tF %1$tT");
		}
		else {
			throw new IllegalArgumentException("Could not format date to datapoint type '" + dpt + "'");
		}
	}

	/**
	 * Retrieves sub number from a DTP ID such as "14.001"
	 * 
	 * @param dptID String with DPT ID
	 * @return sub number or -1 
	 */
	private int getSubNumber(String dptID) {
		int result = -1;
		if (dptID == null)
			throw new IllegalArgumentException("Parameter dptID cannot be null");

		int dptSepratorPosition = dptID.indexOf('.');
		if (dptSepratorPosition>0) {
			try {
				result=Integer.parseInt(dptID.substring(dptSepratorPosition+1, dptID.length()));
			}
			catch (NumberFormatException nfe) {
				logger.error("toType couldn't identify main and/or sub number in dptID (NumberFormatException): {}",dptID);
			}
			catch (IndexOutOfBoundsException ioobe) {
				logger.error("toType couldn't identify main and/or sub number in dptID (IndexOutOfBoundsException): {}",dptID);
			}
		}
		return result;
	}

	/**
	 * Retrieves main number from a DTP ID such as "14.001"
	 * 
	 * @param dptID String with DPT ID
	 * @return main number or -1 
	 */
	private int getMainNumber(String dptID) {
		int result = -1;
		if (dptID == null)
			throw new IllegalArgumentException("Parameter dptID cannot be null");

		int dptSepratorPosition = dptID.indexOf('.');
		if (dptSepratorPosition>0) {
			try {
				result=Integer.parseInt(dptID.substring(0, dptSepratorPosition));
			}
			catch (NumberFormatException nfe) {
				logger.error("toType couldn't identify main and/or sub number in dptID (NumberFormatException): {}",dptID);
			}
			catch (IndexOutOfBoundsException ioobe) {
				logger.error("toType couldn't identify main and/or sub number in dptID (IndexOutOfBoundsException): {}",dptID);
			}
		}
		return result;
	}
}
