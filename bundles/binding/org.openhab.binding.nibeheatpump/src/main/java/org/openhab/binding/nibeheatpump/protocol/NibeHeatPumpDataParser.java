/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.protocol;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;

/**
 * Class for parse data packets from Nibe heat pumps
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpDataParser {

	public enum NibeDataType {
		U8, U16, U32, S8, S16, S32;
	}

	public enum Type {
		Sensor, Status, Settings;
	}

	public static class VariableInformation {

		public int factor;
		public String variable;
		public NibeDataType dataType;
		public Type type;

		public VariableInformation() {
		}

		public VariableInformation(int factor, String variable,
				NibeDataType dataType, Type type) {
			this.factor = factor;
			this.variable = variable;
			this.dataType = dataType;
			this.type = type;
		}

	}

	@SuppressWarnings("serial")
	public static final Map<Integer, VariableInformation> VARIABLE_INFO_F1145_F1245 = 
	Collections.unmodifiableMap(new HashMap<Integer, VariableInformation>() {{
		put(40004, new VariableInformation(10,	"BT1 outdoor temp",						NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40005, new VariableInformation(10,	"EB23-BT2 supply temp S4",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40006, new VariableInformation(10,	"EB22-BT2 supply temp S3",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40007, new VariableInformation(10,	"EB21-BT2 supply temp S2",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40008, new VariableInformation(10,	"BT2 supply temp S1",					NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40012, new VariableInformation(10,	"EB100-EP14-BT3 return temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40013, new VariableInformation(10,	"BT7 hot water top",					NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40014, new VariableInformation(10,	"BT6 hot water load",					NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40015, new VariableInformation(10,	"EB100-EP14-BT10 brine in temp",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40016, new VariableInformation(10,	"EB100-EP14-BT11 brine out temp",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40017, new VariableInformation(10,	"EB100-EP14-BT12 cond out",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40018, new VariableInformation(10,	"EB100-EP14-BT14 hot gas temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40019, new VariableInformation(10,	"EB100-EP14-BT15 liquid line",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40022, new VariableInformation(10,	"EB100-EP14-BT17 suction",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40025, new VariableInformation(10,	"EB100-BT20 exhaust air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40026, new VariableInformation(10,	"EB100-BT21 vented air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40028, new VariableInformation(10,	"AZ1-BT26 temp collector in FLM 1",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40029, new VariableInformation(10,	"AZ1-BT27 temp collector out FLM 1",	NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40030, new VariableInformation(10,	"EB23-BT50 room temp S4",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40031, new VariableInformation(10,	"EB22-BT50 room temp S3",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40032, new VariableInformation(10,	"EB21-BT50 room temp S2",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40033, new VariableInformation(10,	"BT50 room temp S1",					NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40042, new VariableInformation(10,	"CL11-BT51 pool 1 temp",		 		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40043, new VariableInformation(10,	"EP8-BT53 solar panel temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40044, new VariableInformation(10,	"EP8-BT54 solar load temp",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40045, new VariableInformation(10,	"EQ1-BT64 PCS4 supply temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40046, new VariableInformation(10,	"EQ1-BT65 PCS4 return temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40054, new VariableInformation(1,	"EB100-FD1 temperature limiter",		NibeDataType.S16,	Type.Sensor));   // Unit: none
		put(40067, new VariableInformation(10,	"EM1-BT52 boiler temperature",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40070, new VariableInformation(10,	"BT25 external supply temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40071, new VariableInformation(10,	"BT25 external supply temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40072, new VariableInformation(10,	"BF1 Flow",								NibeDataType.S16,	Type.Sensor));   // Unit: l/m
		put(40074, new VariableInformation(1,	"EB100-FR1 anode status",				NibeDataType.S16,	Type.Sensor));   // Unit: none
		put(40079, new VariableInformation(10,	"EB100-BE1 current phase 3",			NibeDataType.S32,	Type.Sensor));   // Unit: A
		put(40081, new VariableInformation(10,	"EB100-BE1 current phase 2",			NibeDataType.S32,	Type.Sensor));   // Unit: A
		put(40083, new VariableInformation(10,	"EB100-BE1 current phase 1",			NibeDataType.S32,	Type.Sensor));   // Unit: A
		put(40107, new VariableInformation(10,	"EB100-BT20 exhaust air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40108, new VariableInformation(10,	"EB100-BT20 exhaust air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40109, new VariableInformation(10,	"EB100-BT20 exhaust air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40110, new VariableInformation(10,	"EB100-BT21 vented air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40111, new VariableInformation(10,	"EB100-BT21 vented air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40112, new VariableInformation(10,	"EB100-BT21 vented air temp",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40113, new VariableInformation(10,	"AZ1-BT26 temp collector in FLM 4",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40114, new VariableInformation(10,	"AZ1-BT26 temp collector in FLM 3",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40115, new VariableInformation(10,	"AZ1-BT26 temp collector in FLM 2",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40116, new VariableInformation(10,	"AZ1-BT27 temp collector out FLM 4",	NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40117, new VariableInformation(10,	"AZ1-BT27 temp collector out FLM 3",	NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40118, new VariableInformation(10,	"AZ1-BT27 temp collector out FLM 2",	NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40127, new VariableInformation(10,	"EB23-BT3 return temp S4",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40128, new VariableInformation(10,	"EB22-BT3 return temp S3",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40129, new VariableInformation(10,	"EB21-BT3 return temp S2",				NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(40152, new VariableInformation(10,	"BT71 ext return temp",					NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43001, new VariableInformation(1,	"Software version",						NibeDataType.U16,	Type.Sensor));   // Unit: none
		put(43005, new VariableInformation(10,	"Degree minutes",						NibeDataType.S16,	Type.Sensor));   // Unit: DM
		put(43006, new VariableInformation(10,	"Calculated supply temp S4",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43007, new VariableInformation(10,	"Calculated supply temp S3",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43008, new VariableInformation(10,	"Calculated supply temp S2",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43009, new VariableInformation(10,	"Calculated supply temp S1",			NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43010, new VariableInformation(10,	"Calculated cooling supply temp",		NibeDataType.S16,	Type.Sensor));   // Unit: C
		put(43013, new VariableInformation(1,	"Freeze protection status",				NibeDataType.U8,	Type.Sensor));   // Unit: none, 1=Freeze protection active
		put(43024, new VariableInformation(1,	"Status cooling",						NibeDataType.U8,	Type.Sensor));	 // Unit: none, 0=OFF, 1=ON
		put(43081, new VariableInformation(10,	"Total operation time addition",		NibeDataType.S32,	Type.Sensor));   // Unit: hours
		put(43084, new VariableInformation(100,	"Internal electrical addition power",	NibeDataType.S16,	Type.Sensor));   // Unit: kW
		put(43086, new VariableInformation(1,	"Prio",									NibeDataType.U8,	Type.Sensor));   // Unit: none
		put(43091, new VariableInformation(1,	"Internal electrical addition state",	NibeDataType.U16,	Type.Sensor));   // Unit: none
		put(43103, new VariableInformation(1,	"HPAC state",							NibeDataType.U8,	Type.Sensor));   // Unit: none
		put(43230, new VariableInformation(10,	"Accumulated energy",					NibeDataType.U32,	Type.Sensor));   // Unit: kWh
		put(43239, new VariableInformation(1,	"Total hot water operation time add",	NibeDataType.S32,	Type.Sensor));   // Unit: hours
		put(43395, new VariableInformation(1,	"HPAC relays",							NibeDataType.U8,	Type.Sensor));   // Unit: none
		put(43416, new VariableInformation(1,	"Compressor starts EB100-EP14",			NibeDataType.S32,	Type.Sensor));   // Unit: none
		put(43420, new VariableInformation(10,	"Total operation time compressor",		NibeDataType.S32,	Type.Sensor));   // Unit: hours
		put(43424, new VariableInformation(1,	"Total hot water operation time compr",	NibeDataType.S32,	Type.Sensor));   // Unit: hours
		put(43427, new VariableInformation(1,	"Compressor state EP14",				NibeDataType.U8,	Type.Sensor));   // Unit: none, // 20 = Stopped, 40 = Starting, 60 = Running, 100 = Stopping 
		put(43514, new VariableInformation(1,	"PCA-Base relayes EP14",				NibeDataType.U8,	Type.Sensor));   // Unit: none 
		put(43516, new VariableInformation(1,	"PCA-Power relayes EP14",				NibeDataType.U8,	Type.Sensor));   // Unit: none 

		put(45001, new VariableInformation(1,	"Alarm number",							NibeDataType.S16,	Type.Status));   // Unit: none

		put(47004, new VariableInformation(1,	"Heat curve S4",						NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47005, new VariableInformation(1,	"Heat curve S3",						NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47006, new VariableInformation(1,	"Heat curve S2",						NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47007, new VariableInformation(1,	"Heat curve S1",						NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47008, new VariableInformation(1,	"Offset S4",							NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47009, new VariableInformation(1,	"Offset S3",							NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47010, new VariableInformation(1,	"Offset S2",							NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47011, new VariableInformation(1,	"Offset S1",							NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47012, new VariableInformation(10,	"Min supply system 4",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47013, new VariableInformation(10,	"Min supply system 3",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47014, new VariableInformation(10,	"Min supply system 2",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47015, new VariableInformation(10,	"Min supply system 1",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47016, new VariableInformation(10,	"Max supply system 4",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47017, new VariableInformation(10,	"Max supply system 3",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47018, new VariableInformation(10,	"Max supply system 2",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47019, new VariableInformation(10,	"Max supply system 1",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47020, new VariableInformation(1,	"Own curve P7",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47021, new VariableInformation(1,	"Own curve P6",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47022, new VariableInformation(1,	"Own curve P5",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47023, new VariableInformation(1,	"Own curve P4",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47024, new VariableInformation(1,	"Own curve P3",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47025, new VariableInformation(1,	"Own curve P2",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47026, new VariableInformation(1,	"Own curve P1",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47027, new VariableInformation(1,	"Point offset outdoor temp",			NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47028, new VariableInformation(1,	"Point offset",							NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47029, new VariableInformation(1,	"External adjustment S4",				NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47030, new VariableInformation(1,	"External adjustment S3",				NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47031, new VariableInformation(1,	"External adjustment S2",				NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47032, new VariableInformation(1,	"External adjustment S1",				NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47033, new VariableInformation(10,	"External adjust with room sensor S4",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47034, new VariableInformation(10,	"External adjust with room sensor S3",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47035, new VariableInformation(10,	"External adjust with room sensor S2",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47036, new VariableInformation(10,	"External adjust with room sensor S1",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47041, new VariableInformation(1,	"Hot water mode",						NibeDataType.S8,	Type.Settings)); // Unit: none, 0=economy, 1=normal, 2=luxury
		put(47043, new VariableInformation(10,	"Start temperature hot water luxury",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47044, new VariableInformation(10,	"Start temperature hot water normal",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47045, new VariableInformation(10,	"Start temperature hot water economy",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47046, new VariableInformation(10,	"Stop temperature periodic hot water",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47047, new VariableInformation(10,	"Stop temperature hot water luxury",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47048, new VariableInformation(10,	"Stop temperature hot water normal",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47049, new VariableInformation(10,	"Stop temperature hot water economy",	NibeDataType.S16,	Type.Settings)); // Unit: C
		put(47050, new VariableInformation(1,	"Periodic hot water",					NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47051, new VariableInformation(1,	"Periodic hot water interval",			NibeDataType.S8,	Type.Settings)); // Unit: days
		put(47131, new VariableInformation(1,	"Language",								NibeDataType.S8,	Type.Settings)); // Unit: none, 0=English, 1=Svenska, 5=Suomi
		put(47133, new VariableInformation(1,	"Period pool 2",						NibeDataType.U8,	Type.Settings)); // Unit: min
		put(47134, new VariableInformation(1,	"Period hot water",						NibeDataType.U8,	Type.Settings)); // Unit: min
		put(47135, new VariableInformation(1,	"Period heat",							NibeDataType.U8,	Type.Settings)); // Unit: min
		put(47136, new VariableInformation(1,	"Period pool",							NibeDataType.U8,	Type.Settings)); // Unit: min
		put(47138, new VariableInformation(1,	"Operational mode heat medium pump",	NibeDataType.U8,	Type.Settings)); // Unit: none, 10=intermittent, 20=continuous, 30=economy, 40=auto
		put(47139, new VariableInformation(1,	"Operational mode brine medium pump",	NibeDataType.U8,	Type.Settings)); // Unit: none, 10=intermittent, 20=continuous, 30=economy, 40=auto
		put(47206, new VariableInformation(1,	"DM start heating",						NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47207, new VariableInformation(1,	"DM start cooling",						NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47208, new VariableInformation(1,	"DM start addition",					NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47209, new VariableInformation(1,	"DM between addition steps",			NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47210, new VariableInformation(1,	"DM start addition with shunt",			NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47212, new VariableInformation(100,	"Max int addition power",				NibeDataType.S16,	Type.Settings)); // Unit: kW
		put(47214, new VariableInformation(1,	"Fuse",									NibeDataType.U8,	Type.Settings)); // Unit: A
		put(47261, new VariableInformation(1,	"Exhaust fan speed 4",					NibeDataType.U8,	Type.Settings)); // Unit: %
		put(47262, new VariableInformation(1,	"Exhaust fan speed 3",					NibeDataType.U8,	Type.Settings)); // Unit: %
		put(47263, new VariableInformation(1,	"Exhaust fan speed 2",					NibeDataType.U8,	Type.Settings)); // Unit: %
		put(47264, new VariableInformation(1,	"Exhaust fan speed 1",					NibeDataType.U8,	Type.Settings)); // Unit: %
		put(47265, new VariableInformation(1,	"Exhaust fan speed normal",				NibeDataType.U8,	Type.Settings)); // Unit: %
		put(47271, new VariableInformation(1,	"Fan return time 4",					NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47272, new VariableInformation(1,	"Fan return time 3",					NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47273, new VariableInformation(1,	"Fan return time 2",					NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47274, new VariableInformation(1,	"Fan return time 1",					NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47275, new VariableInformation(1,	"Filter reminder period",				NibeDataType.U8,	Type.Settings)); // Unit: months
		put(47276, new VariableInformation(1,	"Floor drying",							NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47277, new VariableInformation(1,	"Floor drying period 7",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47278, new VariableInformation(1,	"Floor drying period 6",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47279, new VariableInformation(1,	"Floor drying period 5",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47280, new VariableInformation(1,	"Floor drying period 4",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47281, new VariableInformation(1,	"Floor drying period 3",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47282, new VariableInformation(1,	"Floor drying period 2",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47283, new VariableInformation(1,	"Floor drying period 1",				NibeDataType.U8,	Type.Settings)); // Unit: days
		put(47284, new VariableInformation(1,	"Floor drying temp 7",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47285, new VariableInformation(1,	"Floor drying temp 6",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47286, new VariableInformation(1,	"Floor drying temp 5",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47287, new VariableInformation(1,	"Floor drying temp 4",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47288, new VariableInformation(1,	"Floor drying temp 3",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47289, new VariableInformation(1,	"Floor drying temp 2",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47290, new VariableInformation(1,	"Floor drying temp 1",					NibeDataType.U8,	Type.Settings)); // Unit: C
		put(47291, new VariableInformation(1,	"Floor drying timer",					NibeDataType.U16,	Type.Settings)); // Unit: hours
		put(47302, new VariableInformation(1,	"Climate system 2 accessory",			NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47303, new VariableInformation(1,	"Climate system 3 accessory",			NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47304, new VariableInformation(1,	"Climate system 4 accessory",			NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47305, new VariableInformation(10,	"Climate system 4 mixing valve amp",	NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47306, new VariableInformation(10,	"Climate system 3 mixing valve amp",	NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47307, new VariableInformation(10,	"Climate system 2 mixing valve amp",	NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47308, new VariableInformation(10,	"Climate system 4 shunt wait",			NibeDataType.S16,	Type.Settings)); // Unit: secs
		put(47309, new VariableInformation(10,	"Climate system 3 shunt wait",			NibeDataType.S16,	Type.Settings)); // Unit: secs
		put(47310, new VariableInformation(10,	"Climate system 2 shunt wait",			NibeDataType.S16,	Type.Settings)); // Unit: secs
		put(47312, new VariableInformation(1,	"FLM pump",								NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47313, new VariableInformation(1,	"FLM defrost",							NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47317, new VariableInformation(1,	"Shunt controlled addition accessory",	NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47318, new VariableInformation(1,	"Shunt controlled add min temp",		NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47319, new VariableInformation(1,	"Shunt controlled add min runtime",		NibeDataType.U8,	Type.Settings)); // Unit: hours
		put(47320, new VariableInformation(1,	"Shunt controlled add mix valve amp",	NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47321, new VariableInformation(1,	"Shunt controlled add mix valve wait",	NibeDataType.S16,	Type.Settings)); // Unit: secs
		put(47322, new VariableInformation(1,	"Step controlled add accessory",		NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47323, new VariableInformation(1,	"Step controlled add start DM",			NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47324, new VariableInformation(1,	"Step controlled add diff DM",			NibeDataType.S16,	Type.Settings)); // Unit: DM
		put(47326, new VariableInformation(1,	"Step controlled add mode",				NibeDataType.U8,	Type.Settings)); // Unit: none, 0=linear, 1=binary
		put(47327, new VariableInformation(1,	"Ground water pump accessory",			NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47329, new VariableInformation(1,	"Cooling 2-pipe accessory",				NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47330, new VariableInformation(1,	"Cooling 4-pipe accessory",				NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47331, new VariableInformation(1,	"Min cooling supply temp",				NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47332, new VariableInformation(1,	"Cooling supply temp at 20C",			NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47333, new VariableInformation(1,	"Cooling supply temp at 40C",			NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47334, new VariableInformation(1,	"Cooling close mixing valves",			NibeDataType.U8,	Type.Settings)); // Unit: none
		put(47335, new VariableInformation(1,	"Time between switch heat/cooling",		NibeDataType.S8,	Type.Settings)); // Unit: hours
		put(47336, new VariableInformation(1,	"Heat at room under temp",				NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47337, new VariableInformation(1,	"Cool at room over temp",				NibeDataType.S8,	Type.Settings)); // Unit: C
		put(47338, new VariableInformation(1,	"Cooling mixing valve amp",				NibeDataType.S8,	Type.Settings)); // Unit: none
		put(47339, new VariableInformation(1,	"Cooling mixing valve step delay",		NibeDataType.S16,	Type.Settings)); // Unit: none
		put(47340, new VariableInformation(1,	"Cooling with room sensor",				NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47378, new VariableInformation(10,	"Max diff comp",						NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47379, new VariableInformation(10,	"Max diff add",							NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47380, new VariableInformation(1,	"Low brine out autoreset",				NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON 
		put(47381, new VariableInformation(10,	"Low brine out temp",					NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47382, new VariableInformation(1,	"High brine in",						NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON 
		put(47383, new VariableInformation(10,	"High brine in temp",					NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47384, new VariableInformation(1,	"Date format",							NibeDataType.U8,	Type.Settings)); // Unit: none, 1=DD-MM-YY, 2=YY-MM-DD 
		put(47385, new VariableInformation(1,	"Time format",							NibeDataType.U8,	Type.Settings)); // Unit: none, 12=12 hours, 24=24 hours
		put(47387, new VariableInformation(1,	"Hot water production",					NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47388, new VariableInformation(1,	"Alarm lower room temp",				NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47389, new VariableInformation(1,	"Alarm lower HW temp",					NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47391, new VariableInformation(1,	"Use room sensor S4",					NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47392, new VariableInformation(1,	"Use room sensor S3",					NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47393, new VariableInformation(1,	"Use room sensor S2",					NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47394, new VariableInformation(1,	"Use room sensor S1",					NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(47395, new VariableInformation(10,	"Room sensor setpoint S4",				NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47396, new VariableInformation(10,	"Room sensor setpoint S3",				NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47397, new VariableInformation(10,	"Room sensor setpoint S2",				NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47398, new VariableInformation(10,	"Room sensor setpoint S1",				NibeDataType.S16,	Type.Settings)); // Unit: C 
		put(47399, new VariableInformation(10,	"Room sensor factor S4",				NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47400, new VariableInformation(10,	"Room sensor factor S3",				NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47401, new VariableInformation(10,	"Room sensor factor S2",				NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47402, new VariableInformation(10,	"Room sensor factor S1",				NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(47413, new VariableInformation(1,	"Speed circ pump hot water",			NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47414, new VariableInformation(1,	"Speed circ pump heat",					NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47415, new VariableInformation(1,	"Speed circ pump pool",					NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47416, new VariableInformation(1,	"Speed circ pump economy",				NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47417, new VariableInformation(1,	"Speed circ pump cooling",				NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47418, new VariableInformation(1,	"Speed brine pump",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(47538, new VariableInformation(1,	"Start room temp nigh cooling",			NibeDataType.U8,	Type.Settings)); // Unit: C 
		put(47570, new VariableInformation(1,	"Operational mode",						NibeDataType.U8,	Type.Settings)); // Unit: none, 0=auto, 1=manual, 2=add heat only 
		put(48074, new VariableInformation(10,	"Set point for BT74",					NibeDataType.S16,	Type.Settings)); // Unit: none
		put(48088, new VariableInformation(1,	"Pool 1 accesory",						NibeDataType.U8,	Type.Settings)); // Unit: none
		put(48090, new VariableInformation(10,	"Pool 1 start temp",					NibeDataType.S16,	Type.Settings)); // Unit: C
		put(48092, new VariableInformation(10,	"Pool 1 stop temp",						NibeDataType.S16,	Type.Settings)); // Unit: C
		put(48093, new VariableInformation(1,	"Pool 2 activated",						NibeDataType.U8,	Type.Settings)); // Unit: none
		put(48094, new VariableInformation(1,	"Pool 1 activated",						NibeDataType.U8,	Type.Settings)); // Unit: none
		put(48537, new VariableInformation(1,	"Night cooling",						NibeDataType.U8,	Type.Settings)); // Unit: none, 0=OFF, 1=ON
		put(48539, new VariableInformation(1,	"Night cooling min diff",				NibeDataType.U8,	Type.Settings)); // Unit: C
		put(48053, new VariableInformation(1,	"FLM 2 speed 4",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48054, new VariableInformation(1,	"FLM 2 speed 3",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48055, new VariableInformation(1,	"FLM 2 speed 2",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48056, new VariableInformation(1,	"FLM 2 speed 1",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48057, new VariableInformation(1,	"FLM 2 speed normal",					NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48058, new VariableInformation(1,	"FLM 3 speed 4",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48059, new VariableInformation(1,	"FLM 3 speed 3",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48060, new VariableInformation(1,	"FLM 3 speed 2",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48061, new VariableInformation(1,	"FLM 3 speed 1",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48062, new VariableInformation(1,	"FLM 3 speed normal",					NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48063, new VariableInformation(1,	"FLM 4 speed 4",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48064, new VariableInformation(1,	"FLM 4 speed 3",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48065, new VariableInformation(1,	"FLM 4 speed 2",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48066, new VariableInformation(1,	"FLM 4 speed 1",						NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48067, new VariableInformation(1,	"FLM 4 speed normal",					NibeDataType.U8,	Type.Settings)); // Unit: % 
		put(48068, new VariableInformation(1,	"FLM 4 accessory",						NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(48069, new VariableInformation(1,	"FLM 3 accessory",						NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(48070, new VariableInformation(1,	"FLM 2 accessory",						NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(48071, new VariableInformation(1,	"FLM 1 accessory",						NibeDataType.U8,	Type.Settings)); // Unit: none 
		put(48073, new VariableInformation(1,	"FLM cooling",							NibeDataType.U8,	Type.Settings)); // Unit: none 
	}});

	public static Hashtable<Integer, Short> ParseData(byte[] data)
			throws NibeHeatPumpException {

		if (data[0] == (byte) 0x5C && data[1] == (byte) 0x00
				&& data[2] == (byte) 0x20 && data[3] == (byte) 0x68
				&& data[4] >= (byte) 0x50) {

			int datalen = data[4];
			int msglen = 5 + datalen; 
			
			byte checksum = 0;

			// calculate XOR checksum
			for (int i = 2; i < msglen; i++)
				checksum ^= data[i];

			byte msgChecksum = data[msglen];
			
            // if checksum is 0x5C (start character), heat pump seems to send 0xC5 checksum

			if (checksum == msgChecksum || (checksum == (byte) 0x5C && msgChecksum == (byte) 0xC5)) {

				if ( datalen > 0x50) {
					// if data contains 0x5C (start character), 
					// data seems to contains double 0x5C characters
					
					// let's remove doubles
					for( int i=1; i<msglen; i++) {
						if (data[i] == (byte) 0x5C) {
							data = ArrayUtils.remove(data, i);
							msglen--;
						}
					}
				}
				
				// parse data to hash table

				Hashtable<Integer, Short> values = new Hashtable<Integer, Short>();
				
				try {
					for (int i = 5; i < (msglen - 1); i += 4) {
	
						int id = ((data[i + 1] & 0xFF) << 8 | (data[i + 0] & 0xFF));
						short value = (short) ((data[i + 3] & 0xFF) << 8 | (data[i + 2] & 0xFF));
	
						if (id != 0xFFFF)
							values.put(id, value);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new NibeHeatPumpException("Error occured during data parsing", e);
				}
				
				return values;

			} else {
				throw new NibeHeatPumpException("Checksum does not match");
			}

		} else {
			return null;
		}
	}

}
