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

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteSigned;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorDate;
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
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 1.001 DPT_SWITCH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_001() throws KNXFormatException {
		DPT dpt = DPTXlatorBoolean.DPT_SWITCH;

		testToTypeClass(dpt, OnOffType.class);

		Type type=testToType(dpt, new byte[] { 0x00 }, OnOffType.class);
		testToDPTValue(dpt, type, "off");

		type=testToType(dpt, new byte[] { 0x01 }, OnOffType.class);
		testToDPTValue(dpt, type, "on");

		type=testToType(dpt, new byte[] { 0x02 }, OnOffType.class);
		testToDPTValue(dpt, type, "off");

		type=testToType(dpt, new byte[] { 0x03 }, OnOffType.class);
		testToDPTValue(dpt, type, "on");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, OnOffType.class);
		testToDPTValue(dpt, type, "on");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, OnOffType.class);
		testToDPTValue(dpt, type, "on");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 1.007 DPT_STEP
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_007() throws KNXFormatException {
		DPT dpt = DPTXlatorBoolean.DPT_STEP;

		testToTypeClass(dpt, IncreaseDecreaseType.class);

		Type type=testToType(dpt, new byte[] { 0x00 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "decrease 5");

		type=testToType(dpt, new byte[] { 0x01 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "increase 5");

		type=testToType(dpt, new byte[] { 0x02 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "decrease 5");

		type=testToType(dpt, new byte[] { 0x03 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "increase 5");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "increase 5");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "increase 5");

	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 1.008 DPT_UPDOWN
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_008() throws KNXFormatException {
		DPT dpt = DPTXlatorBoolean.DPT_UPDOWN;

		testToTypeClass(dpt, UpDownType.class);

		Type type=testToType(dpt, new byte[] { 0 }, UpDownType.class);
		testToDPTValue(dpt, type, "up");

		type=testToType(dpt, new byte[] { 1 }, UpDownType.class);
		testToDPTValue(dpt, type, "down");

		type=testToType(dpt, new byte[] { 2 }, UpDownType.class);
		testToDPTValue(dpt, type, "up");

		type=testToType(dpt, new byte[] { 3 }, UpDownType.class);
		testToDPTValue(dpt, type, "down");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, UpDownType.class);
		testToDPTValue(dpt, type, "down");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, UpDownType.class);
		testToDPTValue(dpt, type, "down");

	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 1.010 DPT_START
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_010() throws KNXFormatException {
		DPT dpt = DPTXlatorBoolean.DPT_START;

		testToTypeClass(dpt, StopMoveType.class);

		Type type=testToType(dpt, new byte[] { 0 }, StopMoveType.class);
		testToDPTValue(dpt, type, "stop");

		type=testToType(dpt, new byte[] { 1 }, StopMoveType.class);
		testToDPTValue(dpt, type, "start");

		type=testToType(dpt, new byte[] { 2 }, StopMoveType.class);
		testToDPTValue(dpt, type, "stop");

		type=testToType(dpt, new byte[] { 3 }, StopMoveType.class);
		testToDPTValue(dpt, type, "start");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, StopMoveType.class);
		testToDPTValue(dpt, type, "start");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, StopMoveType.class);
		testToDPTValue(dpt, type, "start");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “8-Bit Unsigned Value" KNX ID: 1.019 DPT_WINDOW_DOOR
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1_1_019() throws KNXFormatException {
		DPT dpt = DPTXlatorBoolean.DPT_WINDOW_DOOR;

		testToTypeClass(dpt, OpenClosedType.class);

		Type type=testToType(dpt, new byte[] { 0 }, OpenClosedType.class);
		testToDPTValue(dpt, type, "closed");

		type=testToType(dpt, new byte[] { 1 }, OpenClosedType.class);
		testToDPTValue(dpt, type, "open");

		type=testToType(dpt, new byte[] { 2 }, OpenClosedType.class);
		testToDPTValue(dpt, type, "closed");

		type=testToType(dpt, new byte[] { 3 }, OpenClosedType.class);
		testToDPTValue(dpt, type, "open");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, OpenClosedType.class);
		testToDPTValue(dpt, type, "open");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0 }, OpenClosedType.class);
		testToDPTValue(dpt, type, "open");

	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type B1U3 KNX ID: 3.007 DPT_CONTROL_DIMMING
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingB1U3_3_007() throws KNXFormatException {
		DPT dpt = DPTXlator3BitControlled.DPT_CONTROL_DIMMING;

		testToTypeClass(dpt, IncreaseDecreaseType.class);

		Type type=testToType(dpt, new byte[] { 0x00 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "decrease 5");

		type=testToType(dpt, new byte[] { 0x01 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "decrease 5");

		type=testToType(dpt, new byte[] { 0x08 }, IncreaseDecreaseType.class);
		testToDPTValue(dpt, type, "increase 5");

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

		Type type=testToType(dpt, new byte[] { 0x0 }, PercentType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0x80 }, PercentType.class);
		testToDPTValue(dpt, type, "50");

		type=testToType(dpt, new byte[] { (byte) 0xFF }, PercentType.class);
		testToDPTValue(dpt, type, "100");
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

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { 50 }, DecimalType.class);
		testToDPTValue(dpt, type, "50");
		
		type=testToType(dpt, new byte[] { 100 }, DecimalType.class);
		testToDPTValue(dpt, type, "100");

		type=testToType(dpt, new byte[] { (byte) 255 }, DecimalType.class);
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

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 255 }, DecimalType.class);
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

		Type type=testToType(dpt, new byte[] { 0 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 255 }, DecimalType.class);
		testToDPTValue(dpt, type, "255");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.001 DPT_VALUE_2_UCOUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_001() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_VALUE_2_UCOUNT;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "65280");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.007 DPT_TIMEPERIOD_HOURS
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_007() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_HOURS;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "65280");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.012 DPT_ELECTRICAL_CURRENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_012() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_ELECTRICAL_CURRENT;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "65280");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Unsigned Value" KNX ID: 7.013 DPT_BRIGHTNESS
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteUnsigned_7_013() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteUnsigned.DPT_BRIGHTNESS;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(dpt, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(dpt, new byte[] { (byte) 0xFF, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "65280");

		type=testToType(dpt, new byte[] { (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "65535");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “2-Octet Float Value". KNX ID: 9.001. DPT_TEMPERATURE
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping2ByteFloat_9_001() throws KNXFormatException {
		DPT dpt =DPTXlator2ByteFloat.DPT_TEMPERATURE;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator2ByteFloat.DPT_TEMPERATURE, new byte[] { 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		/*
		 * Test the maximum positive value
		 * 
		 * FIXME: Calimero lib (Version 2.2.0) seems to have a bug in private method: tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.fromDPT(final int
		 * index). The accuracy when calculating the value is Float, which is insufficient and Double should be used instead.
		 * 
		 * The following test case tests the erroneous calculation. 0x7FFF should result in 670760.96 but results in 670760.94 due to rounding problems.
		 */
		type=testToType(dpt, new byte[] { (byte) 0x7F, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "670760.94");

		type=testToType(dpt, new byte[] { (byte) 0x07, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "20.47");

		type=testToType(dpt, new byte[] { (byte) 0x87, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.01");

		type=testToType(dpt, new byte[] { (byte) 0x80, (byte) 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-20.48");

		/*
		 * Test the maximum negative value
		 * 
		 * FIXME: Calimero lib (Version 2.2.0) seems to have a bug in private method: tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat.fromDPT(final int
		 * index). The accuracy when calculating the Float value is Float, which is insufficient and Double should be used instead.
		 * 
		 * The following test case tests the erroneous calculation. 0xF800 should result in -671088.64 but results in -671088.6 due to rounding problems.
		 */
		type=testToType(dpt, new byte[] { (byte) 0xF8, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-671088.6");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Test case all bytes 0, representing no-day, 00:00:00
	 * 
	 * @throws KNXFormatException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testTypeMappingTime_10_001_fail() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		/*
		 * Set day to no day, 0 hours, 0 minutes and 0 seconds
		 * 
		 * FIXME: This is permissible according KNX Spec but causes an IllegalArgumentException in org.openhab.core.library.types.DateTimeType.<init> caused by
		 * a java.text.ParseException: Unparseable date: ""
		 * 
		 * Root cause seems to be that the most significant 3 bits of the most significant byte specifying "day of week" cannot be all 0 indicating "no day"
		 */
		Type type=testToType(dpt, new byte[] { 0x00, 0x00, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "no-day, 00:00:00");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Test case: positive tests
	 * 1970-01-05T
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMappingTime_10_001() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		/*
		 * Set day to Monday, 0 hours, 0 minutes and 0 seconds January 5th, 1970 was a Monday
		 */
		Type type=testToType(dpt, new byte[] { 0x20, 0x00, 0x00 }, DateTimeType.class);
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
	 * Test case: Set day to Monday, 24 hours, 59 minutes and 59 seconds This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingTime_10_001_HoursOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(dpt, new byte[] { 0x38, 59, 59 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1970-01-05T01:59:59");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Set day to Monday, 23 hours, 60 minutes and 59 seconds This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingTime_10_001_MinutesOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(dpt, new byte[] { 0x37, 60, 59 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1970-01-05T01:59:59");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 10.001 DPT_TIMEOFDAY
	 * 
	 * Set day to Monday, 23 hours, 59 minutes and 60 seconds This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingTime_10_001_SecondsOutOfBounds() throws KNXFormatException {
		DPT dpt =DPTXlatorTime.DPT_TIMEOFDAY;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(dpt, new byte[] { 0x37, 59, 60 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1970-01-05T01:59:59");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal data (day and month cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingDate_11_001_ZeroDay() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x00, 0x00, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "0");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal day (cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingDate_11_001__DayZero() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x00, 0x01, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "0");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “Time" KNX ID: 11.001 DPT_DATE
	 * 
	 * Test illegal month (cannot be 0) This should throw an KNXIllegalArgumentException
	 * 
	 * @throws KNXFormatException
	 */
	@Test(expected = KNXIllegalArgumentException.class)
	public void testTypeMappingDate_11_001__ZeroMonth() throws KNXFormatException {
		DPT dpt =DPTXlatorDate.DPT_DATE;

		testToTypeClass(dpt, DateTimeType.class);

		Type type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x01, 0x00, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "0");
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

		Type type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x01, 0x01, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2000-01-01");

		type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x01, 0x01, 99 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1999-01-01");

		type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x01, 0x01, 90 }, DateTimeType.class);
		testToDPTValue(dpt, type, "1990-01-01");

		type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 0x01, 0x01, 89 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2089-01-01");

		// Test roll over (which is actually not in the KNX spec)
		type=testToType(DPTXlatorDate.DPT_DATE, new byte[] { 31, 0x02, 0x00 }, DateTimeType.class);
		testToDPTValue(dpt, type, "2000-03-02");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.001 DPT_COUNT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_001() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteSigned.DPT_COUNT;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteSigned.DPT_COUNT, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(DPTXlator4ByteSigned.DPT_COUNT, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(DPTXlator4ByteSigned.DPT_COUNT, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(DPTXlator4ByteSigned.DPT_COUNT, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.010 DPT_ACTIVE_ENERGY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_010() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.011 DPT_APPARENT_ENERGY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_011() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteSigned.DPT_APPARENT_ENERGY;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.013 DPT_ACTIVE_ENERGY_KWH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_013() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(DPTXlator4ByteSigned.DPT_ACTIVE_ENERGY_KWH, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Signed Value" KNX ID: 13.014 DPT_APPARENT_ENERGY_KVAH
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteSigned_13_014() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0");
		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-1");

		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH, new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "-2147483648");

		type=testToType(DPTXlator4ByteSigned.DPT_APPARENT_ENERGY_KVAH, new byte[] { (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "2147483647");
	}

	/**
<<<<<<< HEAD
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14
	 * Tested is if the maximum value according to KNX Spec (and IEEE 754) is handled correctly
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_MAX() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR;

		testToTypeClass(dpt, DecimalType.class);

		/*
		 * Test the maximum positive value
		 * 
		 * FIXME: There seems to be a bug in how openhab treats Floats according IEEE 754. The maximum value is given by
		 * Float.MAX_VALUE which represented by 0x7f7ffffff, but openhab throws a NumberFormatException in DecimalType
		 * 
		 * The following test case tests that the erroneous Exception is thrown.
		 */
		Type type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.
	 * Tested is if the minimum value according to KNX Spec (and IEEE 754) is handled correctly
	 * 
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_MIN() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR;

		testToTypeClass(dpt, DecimalType.class);

		/*
		 * Test the maximum negative value
		 * 
		 * FIXME: There seems to be a bug in how openhab treats Floats according IEEE 754. The minimum value is given by
		 * -Float.MAX_VALUE which represented by 0xff7ffffff, but openhab throws a NumberFormatException in DecimalType
		 * 
		 * The following test case tests that the erroneous Exception is thrown.
		 */
		try {
		Type type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.001 DPT_ACCELERATION_ANGULAR
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_001() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ACCELERATION_ANGULAR, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.007 DPT_ANGLE_DEG
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_007() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ANGLE_DEG;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_ANGLE_DEG, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_ANGLE_DEG, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_ANGLE_DEG, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ANGLE_DEG, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ANGLE_DEG, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.019 DPT_ELECTRIC_CURRENT
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_019() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_CURRENT, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.027 DPT_ELECTRIC_POTENTIAL
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_027() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_ELECTRIC_POTENTIAL, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.033 DPT_FREQUENCY
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_033() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_FREQUENCY;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_FREQUENCY, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_FREQUENCY, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_FREQUENCY, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_FREQUENCY, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_FREQUENCY, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
	}

	/**
	 * KNXCoreTypeMapper tests method typeMapper.toType() for type “4-Octet Float Value" KNX ID: 14.056 DPT_POWER
	 * 
	 * @throws KNXFormatException
	 */
	@Test
	public void testTypeMapping4ByteFloat_14_056() throws KNXFormatException {
		DPT dpt =DPTXlator4ByteFloat.DPT_POWER;

		testToTypeClass(dpt, DecimalType.class);

		Type type=testToType(DPTXlator4ByteFloat.DPT_POWER, new byte[] { 0x00, 0x00, 0x00, 0x00 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0");

		// Test the smallest positive value 
		type=testToType(DPTXlator4ByteFloat.DPT_POWER, new byte[] { 0x00, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "0.0000000000000000000000000000000000000000000014");

		// Test the smallest negative value 
		type=testToType(DPTXlator4ByteFloat.DPT_POWER, new byte[] { (byte)0x80, 0x00, 0x00, 0x01 }, DecimalType.class);
		testToDPTValue(dpt, type, "-0.0000000000000000000000000000000000000000000014");

		/*
		 * Test the maximum positive value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_POWER, new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "340282000000000000000000000000000000000");

		/*
		 * Test the maximum negative value
		 */
		type=testToType(DPTXlator4ByteFloat.DPT_POWER, new byte[] { (byte) 0xFF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF }, DecimalType.class);
		testToDPTValue(dpt, type, "-340282000000000000000000000000000000000");
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
		 * FIXME: According to spec the length of this DPT is fixed to 14 bytes. Calimero lib (V 2.2.0) isn't checking this correctly and has a bug in
		 * tuwien.auto.calimero.dptxlator.DPTXlatorString.toDPT(final byte[] buf, final int offset). Calimero accepts any byte array larger or equal to 14 bytes
		 * without error. As a result: anything less then 14 bytes and above a multiple of 14 bytes will be accepted but cutoff. Even for the failed check (less
		 * then 14 bytes) calimero is not throwing an exception but is logging an error which we cannot check for here.
		 * 
		 * Test the erroneous behavior that a too short array results in an empty string. There should be an error logged by calimero lib (V2.2.0).
		 */
		Type type=testToType(DPTXlatorString.DPT_STRING_8859_1, new byte[] { 0x61, 0x62 }, StringType.class);
		testToDPTValue(dpt, type, "");

		/*
		 * FIXME: According to spec the length of this DPT is fixed to 14 bytes. Calimero lib (V 2.2.0) isn't checking this correctly and has a bug in
		 * tuwien.auto.calimero.dptxlator.DPTXlatorString.toDPT(final byte[] buf, final int offset). Calimero accepts any byte array larger or equal to 14 bytes
		 * without error. As a result: anything less then 14 bytes and above a multiple of 14 bytes will be accepted but cutoff. Even for the failed check (less
		 * then 14 bytes) calimero is not throwing an exception but is logging an error which we cannot check for here.
		 * 
		 * Test the erroneous behavior that a too long arrays result in a cutoff string. There probably won't be an error logged by calimero lib (V2.2.0).
		 */
		type=testToType(DPTXlatorString.DPT_STRING_8859_1, new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F }, StringType.class);
		testToDPTValue(dpt, type, "abcdefghijklmn");

		/*
		 * Test a 14 byte array.
		 */
		type=testToType(DPTXlatorString.DPT_STRING_8859_1, new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E }, StringType.class);
		testToDPTValue(dpt, type, "abcdefghijklmn");

		/*
		 * Test that a byte array filled with 0 and correct length is resulting in an empty string.
		 */
		type=testToType(DPTXlatorString.DPT_STRING_8859_1, new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, StringType.class);
		testToDPTValue(dpt, type, "");
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
		assertEquals("KNXCoreTypeMapper.toType() returned object of wrong class for datapoint type \""+dpt.getID()+"\"", expectedClass, type.getClass());
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
