/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.config;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assume.*;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPT;
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

/**
 * This class provides test of the KNXCoreTyperMapper .
 * Tests datapoint types according KNX Association System Specification AS v1.07.00
 * 
 * @author Kai Kreuzer
 * @author Volker Daube
 * 
 */
public class KNXCoreTypeMapperTest {

	private KNXCoreTypeMapper knxCoreTypeMapper;

	@Before
	public void init() throws KNXFormatException {
		knxCoreTypeMapper = new KNXCoreTypeMapper();
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toDPTid()
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testToDPTid() throws KNXFormatException {

		//Test mapping of org.openhab.core.library.types.OnOffType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+OnOffType.class+"\"",
				DPTXlatorBoolean.DPT_SWITCH.getID(), KNXCoreTypeMapper.toDPTid(OnOffType.class));

		//Test mapping of org.openhab.core.library.types.IncreaseDecreaseType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+IncreaseDecreaseType.class+"\"",
				DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID(), KNXCoreTypeMapper.toDPTid(IncreaseDecreaseType.class));

		//Test mapping of org.openhab.core.library.types.UpDownType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+UpDownType.class+"\"",
				DPTXlatorBoolean.DPT_UPDOWN.getID(), KNXCoreTypeMapper.toDPTid(UpDownType.class));

		//Test mapping of org.openhab.core.library.types.StopMoveType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+StopMoveType.class+"\"",
				DPTXlatorBoolean.DPT_START.getID(), KNXCoreTypeMapper.toDPTid(StopMoveType.class));

		//Test mapping of org.openhab.core.library.types.OpenClosedType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+OpenClosedType.class+"\"",
				DPTXlatorBoolean.DPT_WINDOW_DOOR.getID(), KNXCoreTypeMapper.toDPTid(OpenClosedType.class));

		//Test mapping of org.openhab.core.library.types.PercentType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+PercentType.class+"\"",
				DPTXlator8BitUnsigned.DPT_SCALING.getID(), KNXCoreTypeMapper.toDPTid(PercentType.class));

		//Test mapping of org.openhab.core.library.types.DecimalType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+DecimalType.class+"\"",
				DPTXlator2ByteFloat.DPT_TEMPERATURE.getID(), KNXCoreTypeMapper.toDPTid(DecimalType.class));

		//Test mapping of org.openhab.core.library.types.DateTimeType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+DateTimeType.class+"\"",
				DPTXlatorTime.DPT_TIMEOFDAY.getID(), KNXCoreTypeMapper.toDPTid(DateTimeType.class));

		//Test mapping of org.openhab.core.library.types.StringType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+StringType.class+"\"",
				DPTXlatorString.DPT_STRING_8859_1.getID(), KNXCoreTypeMapper.toDPTid(StringType.class));

		//Test mapping of org.openhab.core.library.types.HSBType
		assertEquals("KNXCoreTypeMapper.toDPTid returned datapoint type for class  \""+HSBType.class+"\"",
				DPTXlatorRGB.DPT_RGB.getID(), KNXCoreTypeMapper.toDPTid(HSBType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.001 DPT_SWITCH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_001() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_SWITCH, OnOffType.class, "off", "on");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.002 DPT_BOOL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_002() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_BOOL, OnOffType.class, "false", "true");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.003 DPT_ENABLE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_003() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_ENABLE, OnOffType.class, "disable", "enable");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.004 DPT_RAMP
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_004() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_RAMP, OnOffType.class, "no ramp", "ramp");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.005 DPT_ALARM
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_005() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_ALARM, OnOffType.class, "no alarm", "alarm");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.006 DPT_BINARYVALUE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_006() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_BINARYVALUE, OnOffType.class, "low", "high");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.007 DPT_STEP
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_007() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_STEP, OnOffType.class, "decrease", "increase");

	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.008 DPT_UPDOWN
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_008() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_UPDOWN, UpDownType.class, "up", "down");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.009 DPT_UPDOWN
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_009() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_OPENCLOSE, OpenClosedType.class, "open", "close");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.010 DPT_START
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_010() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_START, StopMoveType.class, "stop", "start");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.011 DPT_STATE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_011() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_STATE, OnOffType.class, "inactive", "active");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.012 DPT_INVERT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_012() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_INVERT, OnOffType.class, "not inverted", "inverted");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.013 DPT_DIMSENDSTYLE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_013() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_DIMSENDSTYLE, OnOffType.class, "start/stop", "cyclic");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.014 DPT_INPUTSOURCE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_014() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_INPUTSOURCE, OnOffType.class, "fixed", "calculated");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.015 DPT_RESET
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_015() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_RESET, OnOffType.class, "no action", "reset");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.018 DPT_OCCUPANCY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_018() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_OCCUPANCY, OnOffType.class, "not occupied", "occupied");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.019 DPT_WINDOW_DOOR
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_019() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_WINDOW_DOOR, OpenClosedType.class, "closed", "open");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.021 DPT_LOGICAL_FUNCTION
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_021() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_LOGICAL_FUNCTION, OnOffType.class, "OR", "AND");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.022 DPT_SCENE_AB
	 * Remark: mapped to DecimalType
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_022() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_SCENE_AB, DecimalType.class, "0", "1");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B1" KNX ID: 1.023 DPT_SHUTTER_BLINDS_MODE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_023() throws KNXFormatException {
		testTypeMappingB1(DPTXlatorBoolean.DPT_SHUTTER_BLINDS_MODE, OnOffType.class, "only move up/down", "move up/down + step-stop");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.001 DPT_SWITCH_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_001() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_SWITCH_CONTROL, "off", "on");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.002 DPT_BOOL_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_002() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_BOOL_CONTROL, "false", "true");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.003 DPT_ENABLE_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_003() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_ENABLE_CONTROL,"disable", "enable");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.004 DPT_RAMP_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_004() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_RAMP_CONTROL, "no ramp", "ramp");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.005 DPT_ALARM_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_005() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_ALARM_CONTROL, "no alarm", "alarm");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.006 DPT_BINARY_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_006() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_BINARY_CONTROL, "low", "high");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.007 DPT_STEP_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_007() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_STEP_CONTROL, "decrease", "increase");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.008 DPT_UPDOWN_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_008() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_UPDOWN_CONTROL, "up", "down");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.009 DPT_SWITCH_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_009() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_OPENCLOSE_CONTROL, "open", "close");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.010 DPT_START_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_010() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_START_CONTROL, "stop", "start");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.011 DPT_STATE_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_011() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_STATE_CONTROL, "inactive", "active");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Datapoint Types B2" KNX ID: 2.012 DPT_INVERT_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB2_2_012() throws KNXFormatException {
		testTypeMappingB1_Controlled(DPTXlator1BitControlled.DPT_INVERT_CONTROL, "not inverted", "inverted");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type B1U3 KNX ID: 3.007 DPT_CONTROL_DIMMING
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1U3_3_007() throws KNXFormatException {
		testTypeMappingB1U3(DPTXlator3BitControlled.DPT_CONTROL_DIMMING, IncreaseDecreaseType.class, "decrease 5", "increase 5");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 5.001 DPT_SCALING
	 * 
	 * This data type is a “Multi-state” type, according KNX spec. No exact linear conversion from value to byte(s) and reverse is required, since rounding is
	 * involved.
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_001() throws KNXFormatException {
		DPT dpt = DPTXlator8BitUnsigned.DPT_SCALING;

		testToTypeClass(dpt, PercentType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, PercentType.class));

		Type type=testToType(dpt, new byte[] { 0x0 }, PercentType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0x80 }, PercentType.class);
		testToDPTValue(dpt, type, "50");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, PercentType.class);
		testToDPTValue(dpt, type, "100");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, PercentType.class);
		testToDPTValue(dpt, type, "100");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 5.003 DPT_ANGLE
	 * 
	 * This data type is a “Multi-state” type, according KNX spec. No exact linear conversion from value to byte(s) and reverse is required, since rounding is
	 * involved.
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_003() throws KNXFormatException {
		DPT dpt = DPTXlator8BitUnsigned.DPT_ANGLE;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0x7F }, DecimalType.class);
		testToDPTValue(dpt, type, "179");

		type=testToType(dpt, new byte[] { (byte) 0x80 }, DecimalType.class);
		testToDPTValue(dpt, type, "181");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "360");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "360");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType()for type “8-Bit Unsigned Value" KNX ID: 5.004 DPT_PERCENT_U8 (previously name DPT_RelPos_Valve)
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_004() throws KNXFormatException {
		DPT dpt =DPTXlator8BitUnsigned.DPT_PERCENT_U8;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { 50 }, DecimalType.class);
		testToDPTValue(dpt, type, "50");

		type=testToType(dpt, new byte[] { 100 }, DecimalType.class);
		testToDPTValue(dpt, type, "100");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "255");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 5.005 DPT_DECIMALFACTOR
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_005() throws KNXFormatException {
		DPT dpt =DPTXlator8BitUnsigned.DPT_DECIMALFACTOR;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] {  }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "255");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 5.006 DPT_TARRIF
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_006() throws KNXFormatException {
		DPT dpt =DPTXlator8BitUnsigned.DPT_TARIFF;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] {  }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "255");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 5.010 DPT_VALUE_1_UCOUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping8BitUnsigned_5_010() throws KNXFormatException {
		DPT dpt =DPTXlator8BitUnsigned.DPT_VALUE_1_UCOUNT;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "255");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.001 DPT_VALUE_2_UCOUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_001() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_VALUE_2_UCOUNT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.002 DPT_TIMEPERIOD
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_002() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.003 DPT_TIMEPERIOD_10
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_003() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_10;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "652800");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "655350");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF  }, DecimalType.class);
		testToDPTValue(dpt, type, "655350");
	}

	/**
	 * KNXCoreTypeMapper tests for method typeMapper.toType() type “2-Octet Unsigned Value" KNX ID: 7.004 DPT_TIMEPERIOD_100
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_004() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_100;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "6528000");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "6553500");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF  }, DecimalType.class);
		testToDPTValue(dpt, type, "6553500");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.005 DPT_TIMEPERIOD_SEC
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_005() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_SEC);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.006 DPT_TIMEPERIOD_MIN
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_006() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_MIN);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.007 DPT_TIMEPERIOD_HOURS
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_007() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_HOURS);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.010 DPT_PROP_DATATYPE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_010() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_PROP_DATATYPE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.011 DPT_LENGTH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_011() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_LENGTH);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.012 DPT_ELECTRICAL_CURRENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_012() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_ELECTRICAL_CURRENT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.013 DPT_BRIGHTNESS
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_013() throws KNXFormatException {
		testTypeMapping2ByteUnsigned(DPTXlator2ByteUnsigned.DPT_BRIGHTNESS);

	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.001. DPT_TEMPERATURE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_001() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.002. DPT_TEMPERATURE_DIFFERENCE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_002() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE_DIFFERENCE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.003. DPT_TEMPERATURE_GRADIENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_003() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE_GRADIENT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.004. DPT_INTENSITY_OF_LIGHT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_004() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.005. DPT_WIND_SPEED
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_005() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_WIND_SPEED);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.006. DPT_AIR_PRESSURE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_006() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_AIR_PRESSURE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.007. DPT_HUMIDITY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_007() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_HUMIDITY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.008. DPT_AIRQUALITY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_008() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_AIRQUALITY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.010. DPT_TIME_DIFFERENCE1
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_010() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE1);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.011. DPT_TIME_DIFFERENCE2
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_011() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE2);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.020. DPT_VOLTAGE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_020() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_VOLTAGE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.021. DPT_ELECTRICAL_CURRENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_021() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_ELECTRICAL_CURRENT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.022. DPT_POWERDENSITY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_022() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_POWERDENSITY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.023. DPT_KELVIN_PER_PERCENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_023() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_KELVIN_PER_PERCENT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.024. DPT_POWER
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_024() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_POWER);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.025. DPT_VOLUME_FLOW
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_025() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_VOLUME_FLOW);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.026. DPT_RAIN_AMOUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_026() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_RAIN_AMOUNT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.027. DPT_TEMP_F
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_027() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_TEMP_F);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.028. DPT_WIND_SPEED_KMH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_028() throws KNXFormatException {
		testTypeMapping2ByteFloat(DPTXlator2ByteFloat.DPT_WIND_SPEED_KMH);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Test case: positive tests
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingTime_10_001() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DateTimeType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] { 0x20, 0x00, 0x00, (byte) 0xFF }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 00:00:00");

		/*
		 * Set day to no day, 0 hours, 0 minutes and 0 seconds
		 * 
		 */
		type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "00:00:00");

		/*
		 * Set day to Monday, 0 hours, 0 minutes and 0 seconds January 5th, 1970 was a Monday
		 */
		type=testToType(dpt, new byte[] { 0x20, 0x00, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 00:00:00");

		/*
		 * * Set day to Tuesday, 0 hours, 0 minutes and 0 seconds January 6th, 1970 was a Tuesday
		 */
		type=testToType(dpt, new byte[] { 0x40, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Tue, 00:00:00");

		/*
		 * Set day to Wednesday, 0 hours, 0 minutes and 0 seconds January 7th, 1970 was a Wednesday
		 */
		type=testToType(dpt, new byte[] { 0x60, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Wed, 00:00:00");

		/*
		 * Set day to Thursday, 0 hours, 0 minutes and 0 seconds January 1st, 1970 was a Thursday
		 */
		type=testToType(dpt, new byte[] { (byte) 0x80, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Thu, 00:00:00");

		/*
		 * Set day to Friday, 0 hours, 0 minutes and 0 seconds January 2nd, 1970 was a Friday
		 */
		type=testToType(dpt, new byte[] { (byte) 0xA0, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Fri, 00:00:00");

		/*
		 * Set day to Saturday, 0 hours, 0 minutes and 0 seconds January 3rd, 1970 was a Saturday
		 */
		type=testToType(dpt, new byte[] { (byte) 0xC0, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Sat, 00:00:00");

		/*
		 * Set day to Sunday, 0 hours, 0 minutes and 0 seconds January 4th, 1970 was a Sunday
		 */
		type=testToType(dpt, new byte[] { (byte) 0xE0, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Sun, 00:00:00");

		/*
		 * Set day to Monday, 1 hour, 0 minutes and 0 seconds
		 */
		type=testToType(dpt, new byte[] { 0x21, 0, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 01:00:00");

		/*
		 * Set day to Monday, 0 hour, 1 minute and 0 seconds
		 */
		type=testToType(dpt, new byte[] { 0x20, 1, 0 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 00:01:00");

		/*
		 * Set day to Monday, 0 hour, 0 minute and 1 seconds
		 */
		type=testToType(dpt, new byte[] { 0x20, 0, 1 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 00:00:01");

		/*
		 * Set day to Monday, 23 hours, 59 minutes and 59 seconds
		 */
		type=testToType(dpt, new byte[] { 0x37, 59, 59 }, DateTimeType.class);
		testToDPTValue(dpt, type, "Mon, 23:59:59");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Test case: Set day to Monday, 24 hours, 59 minutes and 59 seconds
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingTime_10_001_HoursOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x38, 59, 59 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Set day to Monday, 23 hours, 60 minutes and 59 seconds
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingTime_10_001_MinutesOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x37, 60, 59 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Set day to Monday, 23 hours, 59 minutes and 60 seconds
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingTime_10_001_SecondsOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x37, 59, 60 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal data (day and month cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDate_11_001_ZeroDay() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x00, 0x00, 0x00 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal day (cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDate_11_001__DayZero() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x00, 0x01, 0x00 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal month (cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDate_11_001__ZeroMonth() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		assertNull("KNXCoreTypeMapper.toType() should return null",
				testToType(dpt, new byte[] { 0x01, 0x00, 0x00 }, DateTimeType.class));
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test correct year evaluation according KNX spec.
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDate_11_001() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DateTimeType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  0x01, 0x01, 0x00, (byte) 0xFF }, DateTimeType.class);
		testToDPTValue(dpt, type, "2000-01-01");

		type=testToType(dpt, new byte[] { 0x01, 0x01, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2000-01-01");

		type=testToType(dpt, new byte[] { 0x01, 0x01, 99 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1999-01-01");

		type=testToType(dpt, new byte[] { 0x01, 0x01, 90 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1990-01-01");

		type=testToType(dpt, new byte[] { 0x01, 0x01, 89 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2089-01-01");

		// Test roll over (which is actually not in the KNX spec)
		type=testToType(dpt, new byte[] { 31, 0x02, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2000-03-02");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Unsigned Value" KNX ID: 12.001 DPT_VALUE_4_UCOUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteUnsigned_12_001() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteUnsigned.DPT_VALUE_4_UCOUNT;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF , (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "4294967295");

		type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "4294967295");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.001 DPT_COUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_001() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_COUNT);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.002 DPT_FLOWRATE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_002() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_FLOWRATE);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.010 DPT_ACTIVE_ENERGY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_010() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.011 DPT_APPARENT_ENERGY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_011() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.012 DPT_REACTIVE_ENERGY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_012() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_REACTIVE_ENERGY);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.013 DPT_ACTIVE_ENERGY_KWH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_013() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.014 DPT_APPARENT_ENERGY_KVAH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_014() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.015 DPT_REACTIVE_ENERGY_KVARH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_015() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_REACTIVE_ENERGY_KVARH);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.100 DPT_DELTA_TIME
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_100() throws KNXFormatException {
		testTypeMapping4ByteSigned(DPTXlator4ByteSigned.DPT_DELTA_TIME);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14() throws KNXFormatException {
		Locale defaultLocale = Locale.getDefault();

		Locale[] locales = {defaultLocale, Locale.ENGLISH, Locale.GERMAN};
		DPT[] dpts = {DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, DPTXlator4ByteFloat.DPT_ANGLE_DEG,
				DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, DPTXlator4ByteFloat.DPT_FREQUENCY,
				DPTXlator4ByteFloat.DPT_POWER};
		// Iterate over the locales
		for (Locale locale : locales) {
			//Iterate over the subtypes to be tested
			for (DPT dpt : dpts) {
				Locale.setDefault(locale);

				testToTypeClass(dpt, DecimalType.class);

				// Use a too short byte array
				assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
						testToType(dpt, new byte[] { }, DecimalType.class));

				try {
					// Use a too long byte array expecting that additional bytes will be ignored
					Type type=testToType(dpt, new byte[] {   (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
					testToDPTValue(dpt, type, "340282000000000000000000000000000000000");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting 0.0");
				}

				try {
					Type type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
					testToDPTValue(dpt, type, "0.0");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting 0.0");
				}

				try {
					// Test the smallest positive value 
					Type type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
					testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting 0.0000000000000000000000000000000000000000000014");
				}

				try {
					// Test the smallest negative value 
					Type type=testToType(dpt, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
					testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting -0.0000000000000000000000000000000000000000000014");
				}

				try {
					// Test the maximum positive value
					Type type=testToType(dpt, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
					testToDPTValue(dpt, type, "340282000000000000000000000000000000000");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting 340282000000000000000000000000000000000");
				}

				try {
					// Test the maximum negative value
					Type type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
					testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
				}
				catch (NumberFormatException nfe) {
					fail("DptId: "+dpt.getID()+", locale: "+locale+", NumberFormatException. Expecting -340282000000000000000000000000000000000");
				}
			} 
		}

		Locale.setDefault(defaultLocale);
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “String" KNX ID: 16.001 DPT_STRING_8859_1
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingString() throws KNXFormatException {
		DPT dpt =DPTXlatorString.DPT_STRING_8859_1;

		testToTypeClass(dpt, StringType.class);

		/*
		 * According to spec the length of this DPT is fixed to 14 bytes. 
		 * 
		 * Test the that a too short array results in an <null> string. There should be an error logged by calimero lib (V2.2.0).
		 */
		Type type=testToType(dpt, new byte[] { 0x61, 0x62 }, StringType.class);
		testToDPTValue(dpt, type, null);

		/*
		 * FIXME: According to spec the length of this DPT is fixed to 14 bytes. Calimero lib (V 2.2.0) isn't checking this correctly and has a bug in
		 * tuwien.auto.calimero.dptxlator.DPTXlatorString.toDPT(final byte[] buf, final int offset). Calimero accepts any byte array larger or equal to 14 bytes
		 * without error. As a result: anything less then 14 bytes and above a multiple of 14 bytes will be accepted but cutoff. Even for the failed check (less
		 * then 14 bytes) calimero is not throwing an exception but is logging an error which we cannot check for here.
		 * 
		 * Test the erroneous behavior that a too long arrays result in a cutoff string. There probably won't be an error logged by calimero lib (V2.2.0).
		 */
		type=testToType(dpt, new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F }, StringType.class);
		testToDPTValue(dpt, type, "abcdefghijklmn");

		/*
		 * Test a 14 byte array.
		 */
		type=testToType(dpt, new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E }, StringType.class);
		testToDPTValue(dpt, type, "abcdefghijklmn");

		/*
		 * Test that a byte array filled with 0 and correct length is resulting in an empty string.
		 */
		type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, StringType.class);
		testToDPTValue(dpt, type, "");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Scene Number" KNX ID: 17.001 DPT_SCENE_NUMBER
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingSceneNumber_17_001() throws KNXFormatException {
		DPT dpt =DPTXlatorSceneNumber.DPT_SCENE_NUMBER;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "63");

		type=testToType(dpt, new byte[] { 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { 0x3F }, DecimalType.class);
		testToDPTValue(dpt, type, "63");

		//Test that the 2 msb (reserved) are ignored
		type=testToType(dpt, new byte[] { (byte) 0xC0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		//Test that the 2 msb (reserved) are ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "63");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Scene Number" KNX ID: 18.001 DPT_SCENE_CONTROL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingSceneNumber_18_001() throws KNXFormatException {
		DPT dpt =DPTXlatorSceneControl.DPT_SCENE_CONTROL;

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "learn 63");

		type=testToType(dpt, new byte[] { 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "activate 0");

		type=testToType(dpt, new byte[] { 0x3F }, DecimalType.class);
		testToDPTValue(dpt, type, "activate 63");

		//Test that the second msb (reserved) are ignored
		type=testToType(dpt, new byte[] { (byte) 0xC0 }, DecimalType.class);
		testToDPTValue(dpt, type, "learn 0");

		//Test that the second msb (reserved) are ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "learn 63");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Date Time" KNX ID: 19.001 DPT_DATE_TIME
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDateTime_19_001() throws KNXFormatException {
		DPT dpt =DPTXlatorDateTime.DPT_DATE_TIME;

		testToTypeClass(dpt, DateTimeType.class);

		TimeZone defaultTimeZone = TimeZone.getDefault();

		TimeZone[] timeZones = {defaultTimeZone, TimeZone.getTimeZone("America/New_York"), TimeZone.getTimeZone("Europe/Berlin"), TimeZone.getTimeZone("Asia/Shanghai")};

		try {
			Field field = DPTXlatorDateTime.class.getDeclaredField("c");
			field.setAccessible(true);

			for (TimeZone timeZone : timeZones) {
				/*
				 *  DPTXlatorDateTime initialized it's calendar in a static method only once, including timezone.
				 *  Crude solution: we're trying to reset that classes static private field, such that the default timezone will be evaluated again.
				 *  Should this throw a NoSuchFieldException, IllegalAccessException or SecurityException, then we'll just skip this test.  
				 */
				field.set(null, null);

				TimeZone.setDefault(timeZone);

				assertNull("KNXCoreTypeMapper.toType() should return null (no-day)",
						testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00 }, DateTimeType.class));

				assertNull("KNXCoreTypeMapper.toType() should return null (illegal date)",
						testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, DateTimeType.class));

				/* 
				 * Reference testcase
				 * Monday, January 1st, 1900 00:00:00, Fault: Normal (no fault), Working Day: Bank day (No working day), Working Day Field: valid,
				 * Year Field valid, Months and Day fields valid, Day of week field valid, Hour of day, Minutes and Seconds fields valid,
				 * Standard Summer Time: Time = UT+X, Quality of Clock: clock without ext. sync signal
				 */
				Type type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x00, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1900-01-01 00:00:00");

				/* 
				 * Reference testcase + Fault: Fault => not supported
				 */
				assertNull("KNXCoreTypeMapper.toType() should return null (faulty clock)", testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, (byte) 0x80, 0x00 }, DateTimeType.class));

				/* 
				 * Reference testcase + Year Field invalid => not supported
				 */
				assertNull("KNXCoreTypeMapper.toType() should return null (date but no year)", testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x10, 0x00 }, DateTimeType.class));
				/* 
				 * Reference testcase + Months and Day fields invalid => not supported
				 */
				assertNull("KNXCoreTypeMapper.toType() should return null (date but no day and month)", testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x08, 0x00 }, DateTimeType.class));
				/* 
				 * Reference testcase + Year Field invalid + Months and Day fields invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x18, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1970-01-01 00:00:00");
				/* 
				 * Reference testcase + Year Field invalid + Months and Day fields invalid + Day of week field invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x1C, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1970-01-01 00:00:00");
				/* 
				 * Reference testcase + Year Field invalid + Months and Day fields invalid + Day of week field invalid
				 * Working day field invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x3C, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1970-01-01 00:00:00");
				/* 
				 * Reference testcase + Year Field invalid + Months and Day fields invalid + Day of week field invalid
				 * Working day field invalid + Hour of day, Minutes and Seconds fields invalid
				 */
				assertNull("KNXCoreTypeMapper.toType() should return null (neither date nor time)", testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x3E, 0x00 }, DateTimeType.class));

				/* 
				 * Reference testcase + Year Field invalid + Months and Day fields invalid + Day of week field invalid
				 * Working day field invalid + Hour of day, Minutes and Seconds fields invalid, Standard Summer Time: Time = UT+X+1
				 */
				assertNull("KNXCoreTypeMapper.toType() should return null (neither date nor time, but summertime flag)", type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x3F, 0x00 }, DateTimeType.class));
				/* 
				 * Reference testcase + day of week=Any day, Day of week field invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x04, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1900-01-01 00:00:00");
				/* 
				 * Reference testcase + Day of week field invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, 0x04, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1900-01-01 00:00:00");
				/* 
				 * Reference testcase + day of week=Any day, Day of week field invalid, working day, working day field invalid
				 */
				type=testToType(dpt, new byte[] { 0x00, 0x01, 0x01, 0x20, 0x00, 0x00, (byte) 0x60, 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "1900-01-01 00:00:00");

				/* 
				 * December 31st, 2155 day of week=Any day, Day of week field invalid
				 */
				type=testToType(dpt, new byte[] { (byte) 0xFF, 0x0C, 0x1F, 0x17, 0x3B, 0x3B, (byte) 0x04, (byte) 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "2155-12-31 23:59:59");
				/* 
				 * December 31st, 2155, 24:00:00, day of week=Any day, Day of week field invalid
				 * 
				 * TODO: this test case should test for "2155-12-31 24:00:00" since that is what the (valid) KNX bytes represent.
				 * Nevertheless, calimero is "cheating" by using the milliseconds such that "23:59:59.999" is interpreted as "23:59:59"
				 * OpenHAB's DateTimeType doesn't support milliseconds (at least not when parsing from a String), hence 24:00:00 cannot be mapped.
				 */
				type=testToType(dpt, new byte[] { (byte) 0xFF, 0x0C, 0x1F, 0x18, 0x00, 0x00, (byte) 0x04, (byte) 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "2155-12-31 23:59:59");
				/* 
				 * December 31st, 2014 24:00:00, day of week=Any day, Day of week field invalid
				 * 
				 * TODO: this test case should test for "2155-12-31 24:00:00" since that is what the (valid) KNX bytes represent.
				 * Nevertheless, calimero is "cheating" by using the milliseconds such that "23:59:59.999" is interpreted as "23:59:59"
				 * OpenHAB's DateTimeType doesn't support milliseconds (at least not when parsing from a String), hence 24:00:00 cannot be mapped.
				 */
				type=testToType(dpt, new byte[] { (byte) 0x72, 0x0C, 0x1F, 0x18, 0x00, 0x00, (byte) 0x04, (byte) 0x00 }, DateTimeType.class);
				testToDPTValue(dpt, type, "2014-12-31 23:59:59");
			}
			
			field.set(null, null);
			TimeZone.setDefault(defaultTimeZone);
		}
		catch (IllegalArgumentException e) {
			//Shouldn't be thrown, since field.set() set's a static field
			fail();
		}
		catch (IllegalAccessException e) {
			//Stop test and ignore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: IllegalAccessException");
			assumeNoException(e);
		}
		catch (NoSuchFieldException e) {
			//Stop test and ignore if the field is not existing anymore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: NoSuchFieldException");
			assumeNoException(e);
		}
		catch (SecurityException e) {
			//Stop test and ignore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: SecurityException");
			assumeNoException(e);
		}
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Date Time" KNX ID: 19.001 DPT_DATE_TIME
	 * Testcase tests handling of Daylight Savings Time flag (DST).
	 * Interpretation of DST is depending on default timezone, hence we're trying to test using
	 * different timezones: default, New York, Berlin and Shanghai. Shanghai not having a DST.
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingDateTime_19_001_DST() throws KNXFormatException {
		DPT dpt =DPTXlatorDateTime.DPT_DATE_TIME;

		//2014-07-31 00:00:00 DST flag set
		byte[] testDataDST   = new byte[] { 0x72, 0x07, 0x1F, 0x00, 0x00, 0x00, (byte) 0x05, (byte) 0x00 };
		//2014-07-31 00:00:00 DST flag cleared
		byte[] testDataNoDST = new byte[] { 0x72, 0x07, 0x1F, 0x00, 0x00, 0x00, (byte) 0x04, (byte) 0x00 };

		testToTypeClass(dpt, DateTimeType.class);

		TimeZone defaultTimeZone = TimeZone.getDefault();

		TimeZone[] timeZones = {defaultTimeZone, TimeZone.getTimeZone("America/New_York"), TimeZone.getTimeZone("Europe/Berlin"), TimeZone.getTimeZone("Asia/Shanghai")};

		try {
			Field field = DPTXlatorDateTime.class.getDeclaredField("c");
			field.setAccessible(true);

			for (TimeZone timeZone : timeZones) {
				/*
				 *  DPTXlatorDateTime initialized it's calendar in a static method only once, including timezone.
				 *  Crude solution: we're trying to reset that classes static private field, such that the default timezone will be evaluated again.
				 *  Should this throw a NoSuchFieldException, IllegalAccessException or SecurityException, then we'll just skip this test.  
				 */
				field.set(null, null);

				TimeZone.setDefault(timeZone);

				Calendar c = Calendar.getInstance();
				c.set(2014, 7, 31);

				if (c.get(Calendar.DST_OFFSET)>0) {
					//Should be null since we have a DST timezone but non-DST data: should be rejected
					assertNull(testToType(dpt, testDataNoDST, DateTimeType.class));

					Type type = testToType(dpt, testDataDST, DateTimeType.class);
					testToDPTValue(dpt, type, "2014-07-31 00:00:00");
				}
				else {
					//Should be null since we don't have a non-DST timezone but DST data: should be rejected
					assertNull(testToType(dpt, testDataDST, DateTimeType.class));

					Type type = testToType(dpt, testDataNoDST, DateTimeType.class);
					testToDPTValue(dpt, type, "2014-07-31 00:00:00");
				}
			}
			field.set(null, null);
			TimeZone.setDefault(defaultTimeZone);
		}
		catch (IllegalArgumentException e) {
			//Shouldn't be thrown, since field.set() set's a static field
			fail();
		}
		catch (IllegalAccessException e) {
			//Stop test and ignore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: IllegalAccessException");
			assumeNoException(e);
		}
		catch (NoSuchFieldException e) {
			//Stop test and ignore if the field is not existing anymore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: NoSuchFieldException");
			assumeNoException(e);
		}
		catch (SecurityException e) {
			//Stop test and ignore
			System.out.println("Warning: Test testTypeMappingDateTime_19_001_DST skipped: SecurityException");
			assumeNoException(e);
		}
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “3-byte RGB value" KNX ID: 232.600 DPT_Colour_RGB
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingColourRGB_232_600() throws KNXFormatException {
		DPT dpt =DPTXlatorRGB.DPT_RGB;

		testToTypeClass(dpt, HSBType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, HSBType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0 }, HSBType.class);
		testToDPTValue(dpt, type, "r:255 g:255 b:255");

		// Test max value
		type=testToType(dpt, new byte[] {  (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0 }, HSBType.class);
		testToDPTValue(dpt, type, "r:255 g:255 b:255");

		// Test min value
		type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00 }, HSBType.class);
		testToDPTValue(dpt, type, "r:0 g:0 b:0");

		// Test correct interpretation of r value
		type=testToType(dpt, new byte[] { (byte)0xff, 0x00, 0x00 }, HSBType.class);
		testToDPTValue(dpt, type, "r:255 g:0 b:0");

		// Test correct interpretation of g value
		type=testToType(dpt, new byte[] { 0x00, (byte)0xff, 0x00 }, HSBType.class);
		testToDPTValue(dpt, type, "r:0 g:255 b:0");

		// Test correct interpretation of b value
		type=testToType(dpt, new byte[] { 0x00, 0x00, (byte)0xff }, HSBType.class);
		testToDPTValue(dpt, type, "r:0 g:0 b:255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toTypeClass()
	 * 
	 * @param dpt
	 * @param expectedClass
	 * @throws KNXFormatException
	 */
	private void testToTypeClass(DPT dpt, Class<? extends Type> expectedClass) throws KNXFormatException {
		Class<? extends Type> cls=KNXCoreTypeMapper.toTypeClass(dpt.getID());
		assertEquals("KNXCoreTypeMapper.toTypeClass returned wrong class for datapoint type \""+dpt.getID()+"\"", expectedClass, cls);
	}

	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types B1"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMappingB1(DPT dpt, Class<? extends Type> expectedClass, String valueLow, String valueHigh) throws KNXFormatException {

		testToTypeClass(dpt, expectedClass);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, expectedClass));

		Type type=testToType(dpt, new byte[] { 0 }, expectedClass);
		testToDPTValue(dpt, type, valueLow);

		type=testToType(dpt, new byte[] { 1 }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);

		type=testToType(dpt, new byte[] { 2 }, expectedClass);
		testToDPTValue(dpt, type, valueLow);

		type=testToType(dpt, new byte[] { 3 }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);

		type=testToType(dpt, new byte[] { (byte) 0xFF }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);
	}

	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types B1 Controlled"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMappingB1_Controlled(DPT dpt, String valueLow, String valueHigh) throws KNXFormatException {

		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0 "+valueLow);

		type=testToType(dpt, new byte[] { 1 }, DecimalType.class);
		testToDPTValue(dpt, type, "0 "+valueHigh);

		type=testToType(dpt, new byte[] { 2 }, DecimalType.class);
		testToDPTValue(dpt, type, "1 "+valueLow);

		type=testToType(dpt, new byte[] { 3 }, DecimalType.class);
		testToDPTValue(dpt, type, "1 "+valueHigh);

		type=testToType(dpt, new byte[] { (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "1 "+valueHigh);

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "1 "+valueHigh);
	}

	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types B1U3 Controlled"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMappingB1U3(DPT dpt, Class<? extends Type> expectedClass, String valueLow, String valueHigh) throws KNXFormatException {

		testToTypeClass(dpt, expectedClass);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, expectedClass));

		// 3 lsb set to 0 indicate a break. oenHAB doesn't support this state or command
		assertNull("KNXCoreTypeMapper.toType() should return null (decrease break control needs to be ignored)",
				testToType(dpt, new byte[] { 0x00 }, expectedClass));

		// 3 lsb set to 0 indicate a break. oenHAB doesn't support this state or command
		assertNull("KNXCoreTypeMapper.toType() should return null (increase break control needs to be ignored)",
				testToType(dpt, new byte[] { 0x08 }, expectedClass));

		Type type=testToType(dpt, new byte[] { 0x01 }, expectedClass);
		testToDPTValue(dpt, type, valueLow);

		type=testToType(dpt, new byte[] { 0x0F }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);

		// Check that additional bit (4 msb) will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0x0F, 0 }, expectedClass);
		testToDPTValue(dpt, type, valueHigh);
	}

	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types 2-Byte Float"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMapping2ByteUnsigned(DPT dpt) throws KNXFormatException {
		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "65280");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF  }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");
	}

	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types 2-Byte Float"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMapping2ByteFloat(DPT dpt) throws KNXFormatException {
		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		/*
		 * Test the maximum positive value
		 * 
		 */
		type=testToType(dpt, new byte[] { (byte) 0x7F, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "670760.96");

		type=testToType(dpt, new byte[] { (byte) 0x07, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "20.47");

		type=testToType(dpt, new byte[] { (byte) 0x87, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.01");

		type=testToType(dpt, new byte[] { (byte) 0x80, (byte) 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-20.48");

		/*
		 * Test the maximum negative value
		 * 
		 */
		type=testToType(dpt, new byte[] { (byte) 0xF8, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-671088.64");

		// Use a too long byte array expecting that additional bytes will be ignored
		type=testToType(dpt, new byte[] { (byte) 0xF8, (byte) 0x00, (byte) 0xFF  }, DecimalType.class);
		testToDPTValue(dpt, type, "-671088.64");
	}


	/**
	 * Convenience method: testing KNXCoretypeMapper for type “Datapoint Types 4-Byte Signed"
	 * 
	 * @throws KNXFormatException
	 */
	private void testTypeMapping4ByteSigned(DPT dpt) throws KNXFormatException {
		testToTypeClass(dpt, DecimalType.class);

		// Use a too short byte array
		assertNull("KNXCoreTypeMapper.toType() should return null (required data length too short)",
				testToType(dpt, new byte[] { }, DecimalType.class));

		// Use a too long byte array expecting that additional bytes will be ignored
		Type type=testToType(dpt, new byte[] {  (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF , (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");

		type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(dpt, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(dpt, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
	 * Convenience method: testing KNXCoreTypeMapper.toType() method.
	 * This test checks whether the returned Type Object is of the desired Class 
	 * @param dpt requested datapoint type ({@link tuwien.auto.calimero.dptxlator.DPT})
	 * @param data byte array with KNX raw data
	 * @param expectedClass the desired class
	 * @return the {@link Type} object matching the dpt or null
	 * @throws KNXFormatException
	 */
	private Type testToType(DPT dpt, byte[] data, Class<? extends Type> expectedClass) throws KNXFormatException {
		Type type = knxCoreTypeMapper.toType(createDP(dpt.getID()), data);
		if (type != null) {
			assertEquals("KNXCoreTypeMapper.toType() returned object of wrong class for datapoint type \""+dpt.getID()+"\"", expectedClass, type.getClass());
		}
		return type;
	}

	/**
	 * Convenience method: testing KNXCoreTypeMapper.toDPTValue() method.
	 * This test checks whether the returned Type Object contains the correct KNX value 
	 * @param dpt requested datapoint type ({@link tuwien.auto.calimero.dptxlator.DPT})
	 * @param type the Type object holding the value
	 * @param expectedStringResult a string expected to be returned by KNXCoreTypeMapper.toDPTValue()
	 */
	private void testToDPTValue(DPT dpt, Type type, String expectedStringResult) {
		String value=knxCoreTypeMapper.toDPTValue(type, dpt.getID());
		assertEquals("KNXCoreTypeMapper.toDPTValue() test failed for datapoint type \"" + dpt.getID() + "\"", expectedStringResult, value);
	}

	/**
	 * Convenience method creating a Datapoint
	 * 
	 * @param dpt datapoint type
	 * @return a new CommandDP
	 * @throws KNXFormatException
	 */
	private Datapoint createDP(String dpt) throws KNXFormatException {
		int mainNumber=Integer.parseInt(dpt.substring(0, dpt.indexOf('.')));
		return new CommandDP(new GroupAddress("1/2/3"), "test", mainNumber, dpt);
	}

}
