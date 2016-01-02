package org.openhab.binding.stiebelheatpump.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition;
import org.openhab.binding.stiebelheatpump.protocol.Request;

public class ConfigParserTest {

	private ConfigParser configParser = new ConfigParser();
	public static String XMLLocation = "C:/TEMP/2.06.xml";

	public ConfigParserTest() {
	}

	@Test
	public void CreateParserConfiguration() throws StiebelHeatPumpException,
			IOException, JAXBException {
		List<Request> configuration = getHeatPumpConfiguration();
		configParser.marshal(configuration, new File(XMLLocation));
	}

	public List<Request> getHeatPumpConfiguration() {
		List<Request> configuration = new ArrayList<Request>();

		Request version = new Request("Version", "Read version information",
				(byte) 0xfd);
		version.getRecordDefinitions().add(
				new RecordDefinition("Version", 4, 2, 0.01,
						RecordDefinition.Type.Status, ""));
		configuration.add(version);

		Request time = new Request("Time", "Reads Time", (byte) 0xfc);
		time.getRecordDefinitions().add(
				new RecordDefinition("WeekDay", 5, 1, 1,
						RecordDefinition.Type.Settings, 0, 6, 1.0, "d"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Hours", 6, 1, 1,
						RecordDefinition.Type.Settings, 0, 23, 1.0, "h"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Minutes", 7, 1, 1,
						RecordDefinition.Type.Settings, 0, 59, 1.0, "min"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Seconds", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 59, 1.0, "s"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Year", 9, 1, 1,
						RecordDefinition.Type.Settings, 0, 99, 1.0, "Y"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Month", 11, 1, 1,
						RecordDefinition.Type.Settings, 1, 12, 1.0, "M"));
		time.getRecordDefinitions().add(
				new RecordDefinition("Day", 12, 1, 1,
						RecordDefinition.Type.Settings, 1, 31, 1.0, "d"));
		configuration.add(time);

		Request operationStatus = new Request("OperationCounters",
				"Reads Operation counters and Time", (byte) 0x09);
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CompressorA", 4, 2, 1,
						RecordDefinition.Type.Status, "h"));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CompressorB", 6, 2, 1,
						RecordDefinition.Type.Status, "h"));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("HeatingMode", 8, 2, 1,
						RecordDefinition.Type.Status, "h"));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("DHWMode", 10, 2, 1,
						RecordDefinition.Type.Status, "h"));
		operationStatus.getRecordDefinitions().add(
				new RecordDefinition("CoolingMode", 12, 2, 1,
						RecordDefinition.Type.Status, "h"));
		configuration.add(operationStatus);

		Request currentValues = new Request("CurrentValues",
				"Reads measurements", (byte) 0xfb);
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CollectorTemperatur", 4, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("OutsideTemperature", 6, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("FlowTemperature", 8, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ReturnTemperature", 10, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("HotGasTemperature", 12, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CylinderTemperature", 14, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("EvaporatorTemperature", 20, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("CondenserTemperature", 22, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ExtractFanSpeed", 30, 1, 1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("SupplyFanSpeed", 31, 1, 1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("ExhaustFanSpeed", 32, 1, 1,
						RecordDefinition.Type.Sensor, "°C"));
		currentValues.getRecordDefinitions().add(
				new RecordDefinition("FilteredOutsideTemperature", 34, 2, 0.1,
						RecordDefinition.Type.Sensor, "°C"));
		configuration.add(currentValues);

		Request settingsNominalValues = new Request("SettingsNominalValues",
				"Reads nominal setting values", (byte) 0x17);
		settingsNominalValues.getRecordDefinitions()
				.add(new RecordDefinition("P01RoomTemperatureStandardMode", 4,
						2, 0.1, RecordDefinition.Type.Settings, 10, 30, 0.1,
						"°C"));
		settingsNominalValues.getRecordDefinitions()
				.add(new RecordDefinition("P02RoomTemperatureSetbackMode", 6,
						2, 0.1, RecordDefinition.Type.Settings, 10, 30, 0.1,
						"°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P03RoomTemperatureStandby", 8, 2, 0.1,
						RecordDefinition.Type.Settings, 10, 30, 0.1, "°C"));
		settingsNominalValues.getRecordDefinitions()
				.add(new RecordDefinition("P04DHWTemperatureStandardMode", 10,
						2, 0.1, RecordDefinition.Type.Settings, 10, 55, 0.1,
						"°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P05DHWTemperaturSetbackMode", 12, 2, 0.1,
						RecordDefinition.Type.Settings, 10, 55, 1, "°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P06DHWTemperatureStandby", 14, 2, 0.1,
						RecordDefinition.Type.Settings, 10, 55, 1, "°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P07FanStageStandardMode", 16, 1, 1,
						RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P08FanStageSetbackMode", 17, 1, 1,
						RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P09FanStageStandby", 18, 1, 1,
						RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P10HeatingCircuitTemperatureManualMode",
						19, 2, 0.1, RecordDefinition.Type.Settings, 10, 65,
						0.1, "°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P11DHWTemperatureManualMode", 21, 2, 0.1,
						RecordDefinition.Type.Settings, 10, 65, 0.1, "°C"));
		settingsNominalValues.getRecordDefinitions().add(
				new RecordDefinition("P12FanStageManualMode", 23, 1, 1,
						RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		configuration.add(settingsNominalValues);

		Request settingsVentilation = new Request("SettingsVentilation",
				"Reads ventilation setting values", (byte) 0x01);
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P37FanStageSupplyAir1", 4, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P38FanStageSupplyAir2", 6, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P39FanStageSupplyAir3", 8, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P40FanStageExtractyAir1", 10, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P41FanStageExtractyAir2", 12, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P42FanStageExtractyAir3", 14, 2, 1,
						RecordDefinition.Type.Settings, 60, 280, 1.0, "m^3/s"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P43VentilationTimeUnscheduledStage3", 16,
						2, 1, RecordDefinition.Type.Settings, 0, 1000, 1.0,
						"min"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P44VentilationTimeUnscheduledStage2", 18,
						2, 1, RecordDefinition.Type.Settings, 0, 1000, 1.0,
						"min"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P45VentilationTimeUnscheduledStage1", 20,
						2, 1, RecordDefinition.Type.Settings, 0, 1000, 1.0,
						"min"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P46VentilationTimeUnscheduledStage0", 22,
						2, 1, RecordDefinition.Type.Settings, 0, 1000, 1.0,
						"min"));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("P75OperatingModePassiveCooling", 24, 1,
						1, RecordDefinition.Type.Settings, 0, 2, 1.0, ""));
		settingsVentilation.getRecordDefinitions().add(
				new RecordDefinition("StoveFireplaceOperation", 25, 1, 1,
						RecordDefinition.Type.Settings, 0, 4, 1.0, ""));
		configuration.add(settingsVentilation);

		Request settingsHeating1 = new Request("SettingsHeating1",
				"Read heating setting values", (byte) 0x06);
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P21HysteresisHeating1", 4, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 10, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P22HysteresisHeating2", 5, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 10, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P23HysteresisHeating3", 6, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P24HysteresisHeating4", 7, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P25HysteresisHeating5", 8, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P26HysteresisHeating6", 9, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P27HysteresisHeating7", 10, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P28HysteresisHeating8", 11, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P29SwitchingHysteresisAsymmetry", 12, 1,
						1, RecordDefinition.Type.Settings, 1, 5, 1.0, ""));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P30SwitchingValueIntegralPortionHeating",
						13, 2, 1, RecordDefinition.Type.Settings, 1, 999, 1.0,
						"Kmin"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition(
						"P31AmountOfUnlockedElectricalBoosterStages", 15, 1, 1,
						RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("MaximumFlowTemperatureHeatingMode", 16,
						2, 0.1, RecordDefinition.Type.Settings, 10, 75, 0.1,
						"°C"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P49ChangeoverTemperatureSummerWinter",
						18, 2, 0.1, RecordDefinition.Type.Settings, 10, 25,
						0.1, "°C"));
		settingsHeating1
				.getRecordDefinitions()
				.add(new RecordDefinition(
						"P50HysteresisChangeoverTemperatureSummerWinter", 20,
						2, 0.1, RecordDefinition.Type.Settings, 1, 5, 0.1, "K"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P77OutsideTemperatureAdjustment", 22, 2,
						1, RecordDefinition.Type.Settings, 0, 24, 1.0, "h"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P78BivalencePoint", 24, 2, 0.1,
						RecordDefinition.Type.Settings, -10, 20, 0.1, "°C"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("P79DelayedEnableReheating", 26, 1, 1,
						RecordDefinition.Type.Settings, 0, 60, 1.0, "min"));
		settingsHeating1.getRecordDefinitions().add(
				new RecordDefinition("OutputElectricalHeatingStage1", 27, 1,
						0.1, RecordDefinition.Type.Settings, 0, 10, 1.0, "kW"));
		configuration.add(settingsHeating1);

		Request settingsHeating2 = new Request("SettingsHeating2",
				"Read heating setting values", (byte) 0x05);
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P13IncreaseHeatingHC1", 4, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 5, 0.1, ""));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P14LowEndPointHeatingHC1", 6, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 20, 0.1, "K"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P15RoomInfluenceHeatingHC1", 8, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 10, 0.1, ""));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P16IncreaseHeatingHC2", 10, 1, 0.1,
						RecordDefinition.Type.Settings, 0, 5, 0.1, ""));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P17LowEndPointHeatingHC2", 11, 2, 0.1,
						RecordDefinition.Type.Settings, 0, 20, 0.1, "K"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P18RoomInfluenceHeatingHC2", 13, 2, 0.1,
						RecordDefinition.Type.Settings, 0, 10, 0.1, ""));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P19TemperatureCaptureReturnFlowHC1", 15,
						1, 1.0, RecordDefinition.Type.Settings, 1, 100, 1.0,
						"%"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("P20TemperatureCaptureReturnFlowHC2", 16,
						2, 1.0, RecordDefinition.Type.Settings, 1, 100, 1.0,
						"%"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MaxSetHeatingCircuitTemperatureHC1", 18,
						2, 0.1, RecordDefinition.Type.Settings, 20, 65, 0.1,
						"°C"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MinSetHeatingCircuitTemperatureHC1", 20,
						2, 0.1, RecordDefinition.Type.Settings, 5, 40, 0.1,
						"°C"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MaxSetHeatingCircuitTemperatureHC2", 22,
						2, 0.1, RecordDefinition.Type.Settings, 20, 65, 0.1,
						"°C"));
		settingsHeating2.getRecordDefinitions().add(
				new RecordDefinition("MinSetHeatingCircuitTemperatureHC2", 24,
						2, 0.1, RecordDefinition.Type.Settings, 5, 40, 0.1,
						"°C"));
		configuration.add(settingsHeating2);

		Request settingsEvaporator1 = new Request("SettingsEvaporator1",
				"Read evaporator settings values", (byte) 0x03);
		settingsEvaporator1
				.getRecordDefinitions()
				.add(new RecordDefinition(
						"UpperLimitEvaporatorTemperatureForDefrostEnd", 4, 2,
						0.1, RecordDefinition.Type.Settings, 10, 30, 0.1, "°C"));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("MaxEvaporatorDefrostTime", 6, 2, 1,
						RecordDefinition.Type.Settings, 2, 20, 1.0, "min"));
		settingsEvaporator1
				.getRecordDefinitions()
				.add(new RecordDefinition(
						"LimitTemperatureCondenserElectricalReheating", 8, 2,
						0.1, RecordDefinition.Type.Settings, 10, 30, 0.1, "°C"));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition(
						"LimitTemperatureCondenserDefrostTermination", 10, 2,
						0.1, RecordDefinition.Type.Settings, 5, 10, 0.1, "°C"));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("P47CompressorRestartDelay", 12, 1, 1,
						RecordDefinition.Type.Settings, 0, 20, 1.0, "min"));
		settingsEvaporator1.getRecordDefinitions().add(
				new RecordDefinition("P48ExhaustFanSpeed", 13, 1, 1,
						RecordDefinition.Type.Settings, 10, 100, 1.0, "%"));
		configuration.add(settingsEvaporator1);

		Request settingsEvaporator2 = new Request("SettingsEvaporator2",
				"Read evaporator settings values", (byte) 0x04);
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("MaxDefrostDurationAAExchanger", 4, 1, 1,
						RecordDefinition.Type.Settings, 0, 120, 1.0, "min"));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("DefrostStartThreshold", 5, 2, 0.1,
						RecordDefinition.Type.Settings, 0, 10, 0.1, "%"));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("VolumeFlowFilterReplacement", 7, 2, 1,
						RecordDefinition.Type.Settings, 50, 100, 1.0, "%"));
		settingsEvaporator2.getRecordDefinitions().add(
				new RecordDefinition("P85DefrostModeAAHE", 9, 1, 1,
						RecordDefinition.Type.Settings, 1, 5, 1.0, ""));
		configuration.add(settingsEvaporator2);

		Request settingsDryHeatingProgram = new Request(
				"SettingsDryHeatingProgram",
				"Read dry heatingprogram settings values", (byte) 0x10);
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P70Start", 4, 1, 1,
						RecordDefinition.Type.Settings, 0, 0, 1.0, ""));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P71BaseTemperature", 5, 2, 0.1,
						RecordDefinition.Type.Settings, 20, 40, 0.1, "°C"));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P72PeakTemperature", 7, 2, 0.1,
						RecordDefinition.Type.Settings, 25, 50, 0.1, "°C"));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P73BaseTemperatureDuration", 9, 2, 1,
						RecordDefinition.Type.Settings, 0, 5, 1.0, "days"));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P74Increase", 11, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 10, 0.1, "K/days"));
		configuration.add(settingsDryHeatingProgram);

		Request settingsCirculationPump = new Request(
				"SettingsCirculationPump",
				"Read circulation pump setting values", (byte) 0x0a);
		settingsCirculationPump.getRecordDefinitions().add(
				new RecordDefinition("P54minStartupCycles", 4, 1, 1,
						RecordDefinition.Type.Settings, 1, 24, 1.0, "per/day"));
		settingsCirculationPump
				.getRecordDefinitions()
				.add(new RecordDefinition("P55maxStartupCycles", 5, 2, 1,
						RecordDefinition.Type.Settings, 1, 288, 1.0, "per/day"));
		settingsCirculationPump.getRecordDefinitions().add(
				new RecordDefinition("P56OutsideTemperatureMinHeatingCycles",
						7, 2, 0.1, RecordDefinition.Type.Settings, 0, 20, 0.1,
						"°C"));
		settingsCirculationPump.getRecordDefinitions().add(
				new RecordDefinition("P57OutsideTemperatureMaxHeatingCycles",
						9, 2, 0.1, RecordDefinition.Type.Settings, 0, 25, 0.1,
						"°C"));
		settingsCirculationPump.getRecordDefinitions().add(
				new RecordDefinition(
						"P58SuppressTemperatureCaptureDuringPumpStart", 11, 2,
						1, RecordDefinition.Type.Settings, 0, 120, 1.0, "sec"));
		configuration.add(settingsCirculationPump);

		Request settingsHeatingProgram = new Request("SettingsHeatingProgram",
				"Read heating program 1 and 2 setting values", (byte) 0x0b);
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1StartTime", 4, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1StopTime", 6, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Monday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 7, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Tuesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 6, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Wednesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 5, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Thusday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 4, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Friday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 3, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Saturday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 2, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Sunday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP1Enabled", 9, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "On/Off"));

		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2StartTime", 10, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2StopTime", 12, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Monday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 7, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Tuesday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 6, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Wednesday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 5, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Thusday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 4, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Friday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 3, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Saturday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 2, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Sunday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "weekday"));
		settingsHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("HP2Enabled", 15, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "On/Off"));

		configuration.add(settingsHeatingProgram);

		Request settingsDomesticWaterProgram = new Request(
				"SettingsDomesticHotWaterProgram",
				"Read Domestic Water program setting values", (byte) 0x0c);
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1StartTime", 4, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1StopTime", 6, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Monday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 7, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Tuesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 6, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Wednesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 5, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Thusday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 4, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Friday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 3, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Saturday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 2, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Sunday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "weekday"));
		settingsDomesticWaterProgram.getRecordDefinitions().add(
				new RecordDefinition("BP1Enabled", 9, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "On/Off"));

		configuration.add(settingsDomesticWaterProgram);

		Request settingsVentilationProgram = new Request(
				"SettingsVentilationProgram",
				"Read ventilation program 1 and 2 setting values", (byte) 0x0d);
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1StartTime", 4, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1StopTime", 6, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Monday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 7, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Tuesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 6, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Wednesday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 5, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Thusday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 4, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Friday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 3, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Saturday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 2, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Sunday", 8, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP1Enabled", 9, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "On/Off"));

		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2StartTime", 10, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2StopTime", 12, 2, 1,
						RecordDefinition.Type.Settings, 0, 2359, 1.0, "hh:mm"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Monday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 7, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Tuesday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 6, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Wednesday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 5, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Thusday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 4, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Friday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 3, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Saturday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 2, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Sunday", 14, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "weekday"));
		settingsVentilationProgram.getRecordDefinitions().add(
				new RecordDefinition("LP2Enabled", 15, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1, 1, "On/Off"));

		configuration.add(settingsVentilationProgram);

		Request settingsAbsenceProgram = new Request("SettingsAbsenceProgram",
				"Read absence program setting values", (byte) 0x0f);
		settingsAbsenceProgram.getRecordDefinitions().add(
				new RecordDefinition("AP0DurationUntilAbsenceStart", 4, 2, 0.1,
						RecordDefinition.Type.Settings, 0, 2388, 0.1, "h"));
		settingsAbsenceProgram.getRecordDefinitions().add(
				new RecordDefinition("AP0AbsenceDuration", 6, 2, 0.1,
						RecordDefinition.Type.Settings, 0, 2388, 0.1, "h"));
		settingsAbsenceProgram.getRecordDefinitions().add(
				new RecordDefinition("AP0EnableAbsenceProgram", 7, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1.0, ""));

		configuration.add(settingsAbsenceProgram);

		Request settingsRestartTime = new Request(
				"SettingsRestartAndMixerTime",
				"Read restart and mixer time setting values", (byte) 0x0e);
		settingsRestartTime.getRecordDefinitions().add(
				new RecordDefinition("P59RestartBeforSetbackEnd", 4, 2, 1,
						RecordDefinition.Type.Settings, 0, 300, 1.0, "min"));
		settingsRestartTime.getRecordDefinitions().add(
				new RecordDefinition("MixerProportionalRange", 6, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 100, 0.1, "%"));
		settingsRestartTime.getRecordDefinitions().add(
				new RecordDefinition("DerivativeMixerTime", 8, 2, 0.1,
						RecordDefinition.Type.Settings, 1, 100, 0.1, "sec"));
		settingsRestartTime.getRecordDefinitions().add(
				new RecordDefinition("MixerTimeInterval", 10, 2, 1,
						RecordDefinition.Type.Settings, 1, 10, 0.1, "K/day"));
		configuration.add(settingsRestartTime);

		Request SettingsDomesticHotWater = new Request(
				"SettingsDomesticHotWater",
				"Read Domestic Water setting values", (byte) 0x07);
		SettingsDomesticHotWater.getRecordDefinitions()
				.add(new RecordDefinition("P32StartupHysteresisDHWTemperature",
						4, 1, 0.1, RecordDefinition.Type.Settings, 2, 10, 0.1,
						"K"));
		SettingsDomesticHotWater.getRecordDefinitions()
				.add(new RecordDefinition("P33TimeDelayElectricalReheating", 5,
						1, 1.0, RecordDefinition.Type.Settings, 0, 240, 1.0,
						"min"));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition(
						"P34OutsideTemperatureLimitForImmElectricalReheating",
						6, 2, 0.1, RecordDefinition.Type.Settings, -10, 10,
						1.0, "°C"));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition("P35PasteurisationInterval", 8, 1, 1.0,
						RecordDefinition.Type.Settings, 1, 30, 1.0, "K/day"));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition("P36MaxDurationDHWLoading", 9, 1, 1.0,
						RecordDefinition.Type.Settings, 6, 12, 1.0, "h"));
		SettingsDomesticHotWater.getRecordDefinitions()
				.add(new RecordDefinition("PasteurisationHeatupTemperature",
						10, 2, 0.1, RecordDefinition.Type.Settings, 10, 65,
						0.1, "°C"));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition(
						"NoOfEnabledElectricalReheatStagesDHWLoading", 12, 1,
						1, RecordDefinition.Type.Settings, 0, 3, 1.0, ""));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition("MaxFlowTemperatureDHWMode", 13, 2, 0.1,
						RecordDefinition.Type.Settings, 10, 75, 0.1, "°C"));
		SettingsDomesticHotWater.getRecordDefinitions().add(
				new RecordDefinition("CompressorShutdownDHWLoading", 15, 1, 1,
						RecordDefinition.Type.Settings, 0, 1, 1.0, ""));

		configuration.add(SettingsDomesticHotWater);

		return configuration;
	}

	@Test
	public void LoadParserConfiguration() throws StiebelHeatPumpException {
		List<Request> configuration = new ArrayList<Request>();

		configuration = configParser.unmarshal(new File(
				XMLLocation));
		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version", firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());
	}

	@Test
	public void LoadParserConfigurationFromResources()
			throws StiebelHeatPumpException {
		List<Request> configuration = configParser.parseConfig("2.06.xml");
		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version", firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());

	}
}
