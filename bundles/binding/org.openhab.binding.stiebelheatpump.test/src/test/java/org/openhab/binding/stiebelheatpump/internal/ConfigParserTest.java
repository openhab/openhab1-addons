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
				new RecordDefinition("OutputElectricalHeatingStage1", 27, 1,
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
				new RecordDefinition("P48ExhaustFanSpeed", 13, 1, 1,
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
				new RecordDefinition("P70Start", 4, 1, 1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P71BaseTemperature", 5, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P72PeakTemperature", 7, 2, 0.1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P73BaseTemperatureDuration", 9, 2, 1,
						RecordDefinition.Type.Settings));
		settingsDryHeatingProgram.getRecordDefinitions().add(
				new RecordDefinition("P74Increase", 11, 2, 0.1,
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
		
		Request settingsHeatingProgram = new Request("HeatingProgram", "Read heating program 1 and 2 setting values",
				(byte) 0x0b);
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1StartTime", 4, 2, 1, RecordDefinition.Type.Settings));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1StopTime", 6, 2, 1, RecordDefinition.Type.Settings));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Monday", 8, 1, 1,
				RecordDefinition.Type.Settings, 7));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Tuesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 6));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Wednesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 5));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Thusday", 8, 1, 1,
				RecordDefinition.Type.Settings, 4));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Friday", 8, 1, 1,
				RecordDefinition.Type.Settings, 3));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Saturday", 8, 1, 1,
				RecordDefinition.Type.Settings, 2));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Sunday", 8, 1, 1,
				RecordDefinition.Type.Settings, 1));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP1Enabled", 9, 1, 1,
				RecordDefinition.Type.Settings, 1));
		
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2StartTime", 10, 2, 1, RecordDefinition.Type.Settings));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2StopTime", 12, 2, 1, RecordDefinition.Type.Settings));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Monday", 14, 1, 1,
				RecordDefinition.Type.Settings, 7));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Tuesday", 14, 1, 1,
				RecordDefinition.Type.Settings, 6));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Wednesday", 14, 1, 1,
				RecordDefinition.Type.Settings, 5));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Thusday", 14, 1, 1,
				RecordDefinition.Type.Settings, 4));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Friday", 14, 1, 1,
				RecordDefinition.Type.Settings, 3));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Saturday", 14, 1, 1,
				RecordDefinition.Type.Settings, 2));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Sunday", 14, 1, 1,
				RecordDefinition.Type.Settings, 1));
		settingsHeatingProgram.getRecordDefinitions().add(new RecordDefinition(
				"HP2Enabled", 15, 1, 1,
				RecordDefinition.Type.Settings, 1));
		
		configuration.add(settingsHeatingProgram);
		
		Request settingsDomesticWaterProgram = new Request("DomesticHotWaterProgram", "Read Domestic Water program setting values",
				(byte) 0x0c);
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1StartTime", 4, 2, 1, RecordDefinition.Type.Settings));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1StopTime", 6, 2, 1, RecordDefinition.Type.Settings));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Monday", 8, 1, 1,
				RecordDefinition.Type.Settings, 7));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Tuesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 6));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Wednesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 5));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Thusday", 8, 1, 1,
				RecordDefinition.Type.Settings, 4));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Friday", 8, 1, 1,
				RecordDefinition.Type.Settings, 3));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Saturday", 8, 1, 1,
				RecordDefinition.Type.Settings, 2));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Sunday", 8, 1, 1,
				RecordDefinition.Type.Settings, 1));
		settingsDomesticWaterProgram.getRecordDefinitions().add(new RecordDefinition(
				"BP1Enabled", 9, 1, 1,
				RecordDefinition.Type.Settings, 1));
		
		configuration.add(settingsDomesticWaterProgram);
		
		Request settingsVentilationProgram = new Request("VentilationProgram", "Read vetilation program 1 and 2 setting values",
				(byte) 0x0d);
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1StartTime", 4, 2, 1, RecordDefinition.Type.Settings));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1StopTime", 6, 2, 1, RecordDefinition.Type.Settings));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Monday", 8, 1, 1,
				RecordDefinition.Type.Settings, 7));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Tuesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 6));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Wednesday", 8, 1, 1,
				RecordDefinition.Type.Settings, 5));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Thusday", 8, 1, 1,
				RecordDefinition.Type.Settings, 4));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Friday", 8, 1, 1,
				RecordDefinition.Type.Settings, 3));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Saturday", 8, 1, 1,
				RecordDefinition.Type.Settings, 2));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Sunday", 8, 1, 1,
				RecordDefinition.Type.Settings, 1));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP1Enabled", 9, 1, 1,
				RecordDefinition.Type.Settings, 1));
		
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2StartTime", 10, 2, 1, RecordDefinition.Type.Settings));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2StopTime", 12, 2, 1, RecordDefinition.Type.Settings));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Monday", 14, 1, 1,
				RecordDefinition.Type.Settings, 7));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Tuesday", 14, 1, 1,
				RecordDefinition.Type.Settings, 6));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Wednesday", 14, 1, 1,
				RecordDefinition.Type.Settings, 5));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Thusday", 14, 1, 1,
				RecordDefinition.Type.Settings, 4));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Friday", 14, 1, 1,
				RecordDefinition.Type.Settings, 3));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Saturday", 14, 1, 1,
				RecordDefinition.Type.Settings, 2));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Sunday", 14, 1, 1,
				RecordDefinition.Type.Settings, 1));
		settingsVentilationProgram.getRecordDefinitions().add(new RecordDefinition(
				"LP2Enabled", 15, 1, 1,
				RecordDefinition.Type.Settings, 1));
		
		configuration.add(settingsVentilationProgram);
		
		Request settingsAbsenceProgram = new Request("AbsenceProgram", "Read absence program setting values",
				(byte) 0x0a);
		settingsAbsenceProgram.getRecordDefinitions().add(new RecordDefinition(
				"AP0DurationUntilAbsenceStart", 4, 2, 0.1, RecordDefinition.Type.Settings));
		settingsAbsenceProgram.getRecordDefinitions().add(new RecordDefinition(
				"AP0AbsenceDuration", 6, 2, 0.1, RecordDefinition.Type.Settings));
		settingsAbsenceProgram.getRecordDefinitions().add(new RecordDefinition(
				"AP0EnableAbsenceProgram", 7, 1, 1,
				RecordDefinition.Type.Settings));

		configuration.add(settingsAbsenceProgram);

		Request settingsRestartTime = new Request("RestartAndMixerTime", "Read restart and mixer time setting values",
				(byte) 0x0a);
		settingsRestartTime.getRecordDefinitions().add(new RecordDefinition(
				"P59RestartBeforSetbackEnd", 4, 2, 1, RecordDefinition.Type.Settings));
		settingsRestartTime.getRecordDefinitions().add(new RecordDefinition(
				"MixerProportionalRange", 6, 2, 0.1, RecordDefinition.Type.Settings));
		settingsRestartTime.getRecordDefinitions().add(new RecordDefinition(
				"DerivativeMixerTime", 8, 2, 0.1,
				RecordDefinition.Type.Settings));
		settingsRestartTime.getRecordDefinitions().add(new RecordDefinition(
				"MixerTimeInterval", 10, 2, 1,
				RecordDefinition.Type.Settings));
		configuration.add(settingsRestartTime);
		
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
