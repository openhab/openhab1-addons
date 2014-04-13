package org.openhab.binding.stiebelheatpump.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.stiebelheatpump.internal.ConfigLocator;
import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition;
import org.openhab.binding.stiebelheatpump.protocol.Request;

public class ConfigParserTest {

	private ConfigParser configParser = new ConfigParser();
	public static String XMLLocation = "C:/2.06.xml";
	
	public ConfigParserTest() {
	}

	@Test
	public void CreateParserConfiguration() throws StiebelHeatPumpException, IOException, JAXBException {       
		List<Request> configuration = getHeatPumpConfiguration();
		configParser.marshal(configuration, new File(XMLLocation));
	}

	public List<Request> getHeatPumpConfiguration() {
		List<Request> configuration = new ArrayList<Request>();

		Request version = new Request("Version", "Read version information",
				(byte) 0xfd);
		version.getRecordDefinitions().add(
				new RecordDefinition("Version", 4, 2, 0.01,
						RecordDefinition.Type.Status));
		configuration.add(version);

		Request time = new Request("Time", "Reads Time", (byte) 0xfc);
		time.getRecordDefinitions().add(
				new RecordDefinition("WeekDay", 5, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Hours", 6, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Minutes", 7, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Seconds", 8, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Year", 9, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Month", 11, 1, 1,
						RecordDefinition.Type.Status));
		time.getRecordDefinitions().add(
				new RecordDefinition("Day", 12, 1, 1,
						RecordDefinition.Type.Status));
		configuration.add(time);

		Request operationStatus = new Request("OperationCounters",
				"Reads Operation counters and Time", (byte) 0x09);
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CompressorA", 4, 2, 1,
						RecordDefinition.Type.Status));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CompressorB", 6, 2, 1,
						RecordDefinition.Type.Status));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("HeatingMode", 8, 2, 1,
						RecordDefinition.Type.Status));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("DHWMode", 10, 2, 1,
						RecordDefinition.Type.Status));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CoolingMode", 12, 2, 1,
						RecordDefinition.Type.Status));
		configuration.add(operationStatus);

		Request currentValues = new Request("CurrentValues",
				"Reads measurements", (byte) 0xfb);
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CollectorTemperatur", 4, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("OutsideTemperature", 6, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("FlowTemperature", 8, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ReturnTemperature", 10, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("HotGasTemperature", 12, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CylinderTemperature", 14, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("EvaporatorTemperature", 20, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CondenserTemperature", 22, 2, 0.1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ExtractFanSpeed", 30, 1, 1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("SuppyFanSpeed", 31, 1, 1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ExhaustFanSpeed", 32, 1, 1,
						RecordDefinition.Type.Sensor));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("FilteredOutsideTemperature", 34, 2, 0.1,
						RecordDefinition.Type.Sensor));
		configuration.add(currentValues);

		Request settingsNominalValues = new Request("SettingsNominalValues",
				"Reads nominal setting values", (byte) 0x17);
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P01RoomTemperatureStandardMode", 4, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P02RoomTemperatureSetbackMode", 6, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P03RoomTemperatureStandby", 8, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P04DHWTemperatureStandardMode", 10, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P05DHWTemperaturSetbackMode", 12, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P06DHWTemperatureStandby", 14, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P07FanStageStandardMode", 16, 1, 1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P08FanStageSetbackMode", 17, 1, 1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P09FanStageStandby", 18, 1, 1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P10HeatingCircuitTemperatureManualMode",
						19, 2, 0.1, RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P11DHWTemperatureManualMode", 21, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P12FanStageManualMode", 23, 1, 1,
						RecordDefinition.Type.Settings));
		configuration.add(settingsNominalValues);

		Request settingsVentilation = new Request("SettingsVentilation",
				"Reads ventilation setting values", (byte) 0x01);
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P37FanStageSupplyAir1", 4, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P38FanStageSupplyAir2", 6, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P39FanStageSupplyAir3", 8, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P40FanStageExtractyAir1", 10, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P41FanStageExtractyAir2", 12, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P42FanStageExtractyAir3", 14, 2, 1,
						RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P43VentilationTimeUnscheduledStage3", 16,
						2, 1, RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P44VentilationTimeUnscheduledStage2", 18,
						2, 1, RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P45VentilationTimeUnscheduledStage1", 20,
						2, 1, RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P46VentilationTimeUnscheduledStage0", 22,
						2, 1, RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P75OperatingModePassiveCooling", 24, 1,
						1, RecordDefinition.Type.Settings));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("StoveFireplaceOperation", 25, 1, 1,
						RecordDefinition.Type.Settings));
		configuration.add(settingsVentilation);

		Request settingsHeating1 = new Request("SettingsHeating1",
				"Read heating setting values", (byte) 0x06);
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P21HysteresisHeating1", 4, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P22HysteresisHeating2", 5, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P23HysteresisHeating3", 6, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P24HysteresisHeating4", 7, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P25HysteresisHeating5", 8, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P26HysteresisHeating6", 9, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P27HysteresisHeating7", 10, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P28HysteresisHeating8", 11, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P29SwitchingHysteresisAsymmetry", 12, 1,
						1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P30SwitchingValueIntegralPortionHeating",
						13, 2, 1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition(
						"P31AmountOfUnlockedElectricalBoosterStages", 15, 1, 1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("MaximumFlowTemperatureHeatingMode", 16,
						2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P49ChangeoverTemperatureSummerWinter",
						18, 2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition(
						"P50HysteresisChangeoverTemperatureSummerWinter", 20,
						2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P77OutsideTemperatureAdjustment", 22, 2,
						1, RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P78BivalencePoint", 24, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P79DelayedEnableReheating", 26, 1, 1,
						RecordDefinition.Type.Settings));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("OutputElectricalHeatingStage1", 28, 1,
						0.1, RecordDefinition.Type.Settings));
		configuration.add(settingsHeating1);

		Request settingsHeating2 = new Request("SettingsHeating2",
				"Read heating setting values", (byte) 0x7c);
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P13IncreaseHeatingHC1", 4, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P14LowEndPointHeatingHC1", 6, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P15RoomInfluenceHeatingHC1", 8, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P16IncreaseHeatingHC2", 10, 1, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P17LowEndPointHeatingHC2", 11, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P18RoomInfluenceHeatingHC2", 13, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P19TemperatureCaptureReturnFlowHC1", 15,
						1, 1.1, RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P20TemperatureCaptureReturnFlowHC2", 16,
						2, 1.1, RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MaxSetHeatingCircuitTemperatureHC1", 18,
						2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MinSetHeatingCircuitTemperatureHC1", 20,
						2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MaxSetHeatingCircuitTemperatureHC2", 22,
						2, 0.1, RecordDefinition.Type.Settings));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MinSetHeatingCircuitTemperatureHC2", 24,
						2, 0.1, RecordDefinition.Type.Settings));
		configuration.add(settingsHeating2);

		Request settingsEvaporator1 = new Request("SettingsEvaporator1",
				"Read evaporator settings values", (byte) 0x0e);
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition(
						"UpperLimitEvaporatorTemperatureForDefrostEnd", 4, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("MaxEvaporatorDefrostTime", 6, 2, 1,
						RecordDefinition.Type.Settings));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition(
						"LimitTemperatureCondenserElectricalReheating", 8, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition(
						"LimitTemperatureCondenserDefrostTermination", 10, 2,
						0.1, RecordDefinition.Type.Settings));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("P47CompressorRestartDelay", 12, 1, 1,
						RecordDefinition.Type.Settings));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("P48ExhaustFanSpeed", 14, 1, 1,
						RecordDefinition.Type.Settings));
		configuration.add(settingsEvaporator1);

		Request settingsEvaporator2 = new Request("SettingsEvaporator2",
				"Read evaporator settings values", (byte) 0x04);
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("MaxDefrostDurationAAExchanger", 4, 1, 1,
						RecordDefinition.Type.Settings));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("DefrostStartThreshold", 5, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("VolumeFlowFilterReplacement", 7, 2, 1,
						RecordDefinition.Type.Settings));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("P85DefrostModeAAHE", 9, 1, 1,
						RecordDefinition.Type.Settings));
		configuration.add(settingsEvaporator2);

		Request settingsDryHeatingProgram = new Request(
				"SettingsDryHeatingProgram",
				"Read dry heatingprogram settings values", (byte) 0x10);
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P70Start", 5, 1, 1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P71BaseTemperature", 6, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P72PeakTemperature", 8, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P73BaseTemperatureDuration", 10, 2, 1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P74Increase", 12, 2, 0.1,
						RecordDefinition.Type.Settings));
		configuration.add(settingsDryHeatingProgram);

		Request settingsCirculationPump = new Request("SettingsCirculationPump", "Read circulation pump setting values",
				(byte) 0x0a);
		settingsCirculationPump.getRecordDefinitions().add(new RecordDefinition(
				"P54minStartupCycles", 4, 1, 1, RecordDefinition.Type.Settings));
		settingsCirculationPump.getRecordDefinitions().add(new RecordDefinition(
				"P55maxStartupCycles", 5, 2, 1, RecordDefinition.Type.Settings));
		settingsCirculationPump.getRecordDefinitions().add(new RecordDefinition(
				"P56OutsideTemperatureMinHeatingCycles", 7, 2, 0.1,
				RecordDefinition.Type.Settings));
		settingsCirculationPump.getRecordDefinitions().add(new RecordDefinition(
				"P57OutsideTemperatureMaxHeatingCycles", 9, 2, 0.1,
				RecordDefinition.Type.Settings));
		settingsCirculationPump.getRecordDefinitions().add(new RecordDefinition(
				"P58SuppressTemperatureCaptureDuringPumpStart", 11, 2, 1,
				RecordDefinition.Type.Settings));
		configuration.add(settingsCirculationPump);
		return configuration;
	}

	@Test
	public void LoadParserConfiguration() throws StiebelHeatPumpException {       
		List<Request> configuration = new ArrayList<Request>();
		
		configuration = (List<Request>) configParser.unmarshal(new File(XMLLocation));
		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version",firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());
	}

	@Test
	public void LoadParserConfigurationFromResources() throws StiebelHeatPumpException {
		List<Request> configuration =  configParser.parseConfig("2.06.xml");
		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version",firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());		
	}
}
