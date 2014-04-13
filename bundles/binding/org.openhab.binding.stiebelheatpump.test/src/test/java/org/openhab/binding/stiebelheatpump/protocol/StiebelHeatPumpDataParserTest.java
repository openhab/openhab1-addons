package org.openhab.binding.stiebelheatpump.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.stiebelheatpump.internal.ConfigParserTest;
import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.openhab.binding.stiebelheatpump.protocol.Requests.Matcher;

public class StiebelHeatPumpDataParserTest {

	public static String XMLLocation = "C:/2.06.xml";
	
	private StiebelHeatPumpDataParser parser = new StiebelHeatPumpDataParser();
	private ConfigParserTest configParserTest = new ConfigParserTest();
	private List<Request> configuration = null;
	
	@Before
	public void setUp() throws StiebelHeatPumpException {
		configuration = configParserTest.getHeatPumpConfiguration();
	}

	@Test
	public void testParseVersion() throws StiebelHeatPumpException {
		// verify the version from heat pump
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "Version";
		                  }});		
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xb5,
				(byte) 0xfd, (byte) 0x01, (byte) 0xb6, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		data.get("Version");
		Assert.assertEquals("4.38",data.get("Version"));
	}

	@Test
	public void testParseTime() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "Time";
		                  }});	
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x79,
				(byte) 0xfc, (byte) 0x00, (byte) 0x02, (byte) 0x0a,
				(byte) 0x21, (byte) 0x24, (byte) 0x0e, (byte) 0x00,
				(byte) 0x03, (byte) 0x1a, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("2", data.get("WeekDay"));
		Assert.assertEquals("10", data.get("Hours"));
		Assert.assertEquals("33",  data.get("Minutes"));
		Assert.assertEquals("36",  data.get("Seconds"));
		Assert.assertEquals("14",  data.get("Year"));
		Assert.assertEquals("3",  data.get("Month"));
		Assert.assertEquals("26",  data.get("Day"));
		Assert.assertEquals((byte) 0x79, parser.calculateChecksum(response));
	}

	@Test
	public void testParseOperationCounters() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "OperationCounters";
		                  }});
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xea,
				(byte) 0x09, (byte) 0x00, (byte) 0x1a, (byte) 0x00,
				(byte) 0x19, (byte) 0x0c, (byte) 0x77, (byte) 0x02,
				(byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("26",  data.get("CompressorA"));
		Assert.assertEquals("25",  data.get("CompressorB"));
		Assert.assertEquals("3191",  data.get("HeatingMode"));
		Assert.assertEquals("552",  data.get("DHWMode"));
		Assert.assertEquals("0",  data.get("CoolingMode"));
		Assert.assertEquals((byte) 0xea, parser.calculateChecksum(response));
	}

	@Test
	public void testParseCurrentValues() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "CurrentValues";
		                  }});
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x48,
				(byte) 0xfb, (byte) 0x01, (byte) 0xaa, (byte) 0x00,
				(byte) 0x72, (byte) 0x01, (byte) 0x3a, (byte) 0x00,
				(byte) 0xa2, (byte) 0x02, (byte) 0xdc, (byte) 0x01,
				(byte) 0xce, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x59, (byte) 0x01,
				(byte) 0x73, (byte) 0x11, (byte) 0x18, (byte) 0x27,
				(byte) 0x27, (byte) 0xf4, (byte) 0xca, (byte) 0x0c,
				(byte) 0x22, (byte) 0x1d, (byte) 0x00, (byte) 0x00,
				(byte) 0x57, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));

		Assert.assertEquals("42.6", data.get("CollectorTemperatur"));
		Assert.assertEquals("11.4", data.get("OutsideTemperature"));
		Assert.assertEquals("31.4", data.get("FlowTemperature"));
		Assert.assertEquals("16.2", data.get("ReturnTemperature"));
		Assert.assertEquals("73.2", data.get("HotGasTemperature"));
		Assert.assertEquals("46.2", data.get("CylinderTemperature"));
		Assert.assertEquals("8.9", data.get("EvaporatorTemperature"));
		Assert.assertEquals("37.1", data.get("CondenserTemperature"));
		Assert.assertEquals("8.9", data.get("EvaporatorTemperature"));
		Assert.assertEquals("12", data.get("ExtractFanSpeed"));
		Assert.assertEquals("34", data.get("SuppyFanSpeed"));
		Assert.assertEquals("29", data.get("ExhaustFanSpeed"));
		Assert.assertEquals("8.7", data.get("FilteredOutsideTemperature"));
		Assert.assertEquals((byte) 0x48, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsNominalValues()
			throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsNominalValues";
		                  }});
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xec,
				(byte) 0x17, (byte) 0x00, (byte) 0xa5, (byte) 0x00,
				(byte) 0x9d, (byte) 0x00, (byte) 0x64, (byte) 0x01,
				(byte) 0xc2, (byte) 0x01, (byte) 0xe0, (byte) 0x00,
				(byte) 0x64, (byte) 0x01, (byte) 0x01, (byte) 0x00,
				(byte) 0x01, (byte) 0x5e, (byte) 0x01, (byte) 0xc2,
				(byte) 0x02, (byte) 0x10, (byte) 0x03 };

		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("16.5", data.get("P01RoomTemperatureStandardMode"));
		Assert.assertEquals("15.7", data.get("P02RoomTemperatureSetbackMode"));
		Assert.assertEquals("10.0", data.get("P03RoomTemperatureStandby"));
		Assert.assertEquals("45.0", data.get("P04DHWTemperatureStandardMode"));
		Assert.assertEquals("48.0", data.get("P05DHWTemperaturSetbackMode"));
		Assert.assertEquals("10.0", data.get("P06DHWTemperatureStandby"));
		Assert.assertEquals("1", data.get("P07FanStageStandardMode"));
		Assert.assertEquals("1", data.get("P08FanStageSetbackMode"));
		Assert.assertEquals("0", data.get("P09FanStageStandby"));
		Assert.assertEquals("35.0", data.get("P10HeatingCircuitTemperatureManualMode"));
		Assert.assertEquals("45.0", data.get("P11DHWTemperatureManualMode"));
		Assert.assertEquals("2", data.get("P12FanStageManualMode"));
		Assert.assertEquals((byte) 0xec, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsVentilation() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsVentilation";
		                  }});

		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x2f,
				(byte) 0x01, (byte) 0x00, (byte) 0x73, (byte) 0x00,
				(byte) 0x77, (byte) 0x00, (byte) 0xa6, (byte) 0x00,
				(byte) 0x73, (byte) 0x00, (byte) 0x86, (byte) 0x00,
				(byte) 0xc4, (byte) 0x00, (byte) 0x38, (byte) 0x00,
				(byte) 0x38, (byte) 0x00, (byte) 0x38, (byte) 0x00,
				(byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x03 };

		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("115", data.get("P37FanStageSupplyAir1"));
		Assert.assertEquals("119", data.get("P38FanStageSupplyAir2"));
		Assert.assertEquals("166", data.get("P39FanStageSupplyAir3"));
		Assert.assertEquals("115", data.get("P40FanStageExtractyAir1"));
		Assert.assertEquals("134", data.get("P41FanStageExtractyAir2"));
		Assert.assertEquals("196", data.get("P42FanStageExtractyAir3"));
		Assert.assertEquals("56", data.get("P43VentilationTimeUnscheduledStage3"));
		Assert.assertEquals("56", data.get("P44VentilationTimeUnscheduledStage2"));
		Assert.assertEquals("56", data.get("P45VentilationTimeUnscheduledStage1"));
		Assert.assertEquals("56", data.get("P46VentilationTimeUnscheduledStage0"));
		Assert.assertEquals("0", data.get("P75OperatingModePassiveCooling"));
		Assert.assertEquals("0", data.get("StoveFireplaceOperation"));
		Assert.assertEquals((byte) 0x2f, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsHeating1() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsHeating1";
		                  }});
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xac,
				(byte) 0x06, (byte) 0x28, (byte) 0x1e, (byte) 0x1e,
				(byte) 0x14, (byte) 0x0a, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x64,
				(byte) 0x03, (byte) 0x02, (byte) 0xee, (byte) 0x00,
				(byte) 0xc8, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
				(byte) 0x01, (byte) 0xff, (byte) 0xce, (byte) 0x10,
				(byte) 0x10, (byte) 0x1a, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));

		Assert.assertEquals("4.0", data.get("P21HysteresisHeating1"));
		Assert.assertEquals("3.0", data.get("P22HysteresisHeating2"));
		Assert.assertEquals("3.0", data.get("P23HysteresisHeating3"));
		Assert.assertEquals("2.0", data.get("P24HysteresisHeating4"));
		Assert.assertEquals("1.0", data.get("P25HysteresisHeating5"));
		Assert.assertEquals("0.0", data.get("P26HysteresisHeating6"));
		Assert.assertEquals("0.0", data.get("P27HysteresisHeating7"));
		Assert.assertEquals("0.0", data.get("P28HysteresisHeating8"));
		Assert.assertEquals("2", data.get("P29SwitchingHysteresisAsymmetry"));
		Assert.assertEquals("100", data.get("P30SwitchingValueIntegralPortionHeating"));
		Assert.assertEquals("3", data.get("P31AmountOfUnlockedElectricalBoosterStages"));
		Assert.assertEquals("75.0", data.get("MaximumFlowTemperatureHeatingMode"));
		Assert.assertEquals("20.0",  data.get("P49ChangeoverTemperatureSummerWinter"));
		Assert.assertEquals("1.0",  data.get("P50HysteresisChangeoverTemperatureSummerWinter"));
		Assert.assertEquals("1", data.get("P77OutsideTemperatureAdjustment"));
		Assert.assertEquals("-5.0", data.get("P78BivalencePoint"));
		Assert.assertEquals("16", data.get("P79DelayedEnableReheating"));
		Assert.assertEquals("2.6", data.get("OutputElectricalHeatingStage1"));
		Assert.assertEquals((byte) 0xac, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsHeating2() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsHeating2";
		                  }});

		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x7c,
				(byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x1e, (byte) 0x00, (byte) 0x64, (byte) 0x02,
				(byte) 0x26, (byte) 0x00, (byte) 0x32, (byte) 0x01,
				(byte) 0x5e, (byte) 0x00, (byte) 0x32, (byte) 0x10, (byte) 0x03 };

		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("0.4", data.get("P13IncreaseHeatingHC1"));
		Assert.assertEquals("0.0", data.get("P14LowEndPointHeatingHC1"));
		Assert.assertEquals("0.0", data.get("P15RoomInfluenceHeatingHC1"));
		Assert.assertEquals("0.5", data.get("P16IncreaseHeatingHC2"));
		Assert.assertEquals("0.0", data.get("P17LowEndPointHeatingHC2"));
		Assert.assertEquals("0.0", data.get("P18RoomInfluenceHeatingHC2"));
		Assert.assertEquals("30", data.get("P19TemperatureCaptureReturnFlowHC1"));
		Assert.assertEquals("100",  data.get("P20TemperatureCaptureReturnFlowHC2"));
		Assert.assertEquals("55.0",  data.get("MaxSetHeatingCircuitTemperatureHC1"));
		Assert.assertEquals("5.0",  data.get("MinSetHeatingCircuitTemperatureHC1"));
		Assert.assertEquals("35.0", data.get("MaxSetHeatingCircuitTemperatureHC2"));
		Assert.assertEquals("5.0",  data.get("MinSetHeatingCircuitTemperatureHC2"));
		Assert.assertEquals((byte) 0x7c, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsEvaporator1() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsEvaporator1";
		                  }});

		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x0e,
				(byte) 0x03, (byte) 0x00, (byte) 0x96, (byte) 0x00,
				(byte) 0x0a, (byte) 0x00, (byte) 0x96, (byte) 0x00,
				(byte) 0x64, (byte) 0x10, (byte) 0x10, (byte) 0x60,
				(byte) 0x10, (byte) 0x03 };

		Map<String,String> data = parser.parseRecords(response, result.get(0));

		Assert.assertEquals("15.0", data.get("UpperLimitEvaporatorTemperatureForDefrostEnd"));
		Assert.assertEquals("10", data.get("MaxEvaporatorDefrostTime"));
		Assert.assertEquals("15.0",  data.get("LimitTemperatureCondenserElectricalReheating"));
		Assert.assertEquals("10.0", data.get("LimitTemperatureCondenserDefrostTermination"));
		Assert.assertEquals("16",data.get("P47CompressorRestartDelay"));
		Assert.assertEquals("96",data.get("P48ExhaustFanSpeed"));
		Assert.assertEquals((byte) 0x0e, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsEvaporator2() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsEvaporator2";
		                  }});

		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00,
				(byte) 0x04, (byte) 0x3c, (byte) 0x00, (byte) 0x64,
				(byte) 0x00, (byte) 0x5a, (byte) 0x01, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));

		Assert.assertEquals("60", data.get("MaxDefrostDurationAAExchanger"));
		Assert.assertEquals("10.0", data.get("DefrostStartThreshold"));
		Assert.assertEquals("90", data.get("VolumeFlowFilterReplacement"));
		Assert.assertEquals("1", data.get("P85DefrostModeAAHE"));

		Assert.assertEquals((byte) 0x00, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsDryHeatingProgram() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsDryHeatingProgram";
		                  }});

		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xa8,
				(byte) 0x10, (byte) 0x10, (byte) 0x00, (byte) 0x00,
				(byte) 0xfa, (byte) 0x01, (byte) 0x90, (byte) 0x00,
				(byte) 0x02, (byte) 0x00, (byte) 0x0a, (byte) 0x10, (byte) 0x03 };
		
		Map<String,String> data = parser.parseRecords(response, result.get(0));

		Assert.assertEquals("0", data.get("P70Start"));
		Assert.assertEquals("25.0", data.get("P71BaseTemperature"));
		Assert.assertEquals("40.0", data.get("P72PeakTemperature"));
		Assert.assertEquals("2", data.get("P73BaseTemperatureDuration"));
		Assert.assertEquals("1.0", data.get("P74Increase"));
		Assert.assertEquals((byte) 0xa8, parser.calculateChecksum(response));
	}

	@Test
	public void testParseSettingsCirculationPump() throws StiebelHeatPumpException {
		List<Request> result = Requests.searchIn( configuration,
		          new Matcher<Request>() { 
		              public boolean matches( Request r ) { 
		                  return r.getName() == "SettingsCirculationPump";
		                  }});
		
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xeb,
				(byte) 0x0a, (byte) 0x01, (byte) 0x01, (byte) 0x20,
				(byte) 0x00, (byte) 0xba, (byte) 0x00, (byte) 0xc8,
				(byte) 0x00, (byte) 0x3c, (byte) 0x10, (byte) 0x03 };

		Map<String,String> data = parser.parseRecords(response, result.get(0));
		
		Assert.assertEquals("1", data.get("P54minStartupCycles"));
		Assert.assertEquals("288", data.get("P55maxStartupCycles"));
		Assert.assertEquals("18.6", data.get("P56OutsideTemperatureMinHeatingCycles"));
		Assert.assertEquals("20.0", data.get("P57OutsideTemperatureMaxHeatingCycles"));
		Assert.assertEquals("60", data.get("P58SuppressTemperatureCaptureDuringPumpStart"));
		Assert.assertEquals((byte) 0xeb, parser.calculateChecksum(response));
	}

	@Test
	public void testParseFindAndReplace() throws StiebelHeatPumpException {
		byte[] response = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0xac,
				(byte) 0x06, (byte) 0x28, (byte) 0x1e, (byte) 0x1e,
				(byte) 0x14, (byte) 0x0a, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x64,
				(byte) 0x03, (byte) 0x02, (byte) 0xee, (byte) 0x00,
				(byte) 0xc8, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
				(byte) 0x01, (byte) 0xff, (byte) 0xce, (byte) 0x10,
				(byte) 0x10, (byte) 0x1a, (byte) 0x10, (byte) 0x03 };
		int originalLength = response.length;
		response = parser.findReplace(response, new byte[] { (byte) 0x10,
				(byte) 0x10 }, new byte[] { (byte) 0x10 });
		response = parser.findReplace(response, new byte[] { (byte) 0x2b,
				(byte) 0x18 }, new byte[] { (byte) 0x2b });
		Assert.assertEquals(1, originalLength - response.length);

	}

	@Test
	public void testDataAvailable() {
		try {
			Assert.assertTrue(parser.dataAvailable(new byte[] { 10, 02 }));

		} catch (Exception e) {
			Assert.fail("unexpected exception");
		}
		try {
			parser.dataAvailable(new byte[] {});
			Assert.fail("expected exception");

		} catch (Exception e) {
		}
	}

}
