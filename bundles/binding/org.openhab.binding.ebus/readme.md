Documentation of the eBus Binding Bundle

## Introduction
The eBus binding allows you to control your heating system. The eBus protocol is used by heating system vendors like Wolf, Vaillant, Kromschröder etc. You can read temperatures, pump performance, gas consumption etc.

To access your heating system you either need a eBus interface. You can buy a ready interface or solder your own circuit (examples: [eBus Wiki](http://ebus.webhop.org/twiki/bin/view.pl/EBus/EBusKonverter)). A simple read-only interface can be build with an Arduino device.

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration in openhab.cfg

You can find the configuration section for the eBus binding in file configurations/openhab.cfg, section "eBus Binding". 

For your convenience you can see the relevant section as follows:

	# Serial port of eBus interface
	# Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
	ebus:serialPort=COM2
	
	# TCP Hostname and Port
	# Warning: Only use ebus.hostname or ebus.serialPort
	#ebus:hostname=myhostname
	#ebus:port=5000
	
	# Custom parser configuration file
	# This example tries to load a configuration ${openhab_home}/configurations/ebus-config.json
	#ebus:parserUrl=platform:/base/../configurations/ebus-config.json
	
	# Load different parser, currently supported
	# >> common - All telegrams defined by eBus interest group
	# >> wolf - All telegrams specified by Wolf/Kromschröder
	# >> vaillant - All telegrams specified by Vaillant
	# >> testing -  All unknown or test telegrams
	# >> custom - Use configuration defined by ebus:parserUrl
	# default uses common and all vendor specified telegrams
	#ebus:parser=common,wolf,testing,custom
	
	# Set the sender id of this binding, default is "FF"
	#ebus:senderId=FF

A sample configuration could look like:

	ebus:serialPort=COM2

## Bind Items to eBus

### Description
In order to bind an item to a eBus value you need to provide configuration settings. The easiest way to do so is to add binding information in your 'item file' (in the folder configurations/items`). The syntax for the eBus binding configuration string is explained here:

### Parameters

Parameter      | Description
---            | ---
id             | Defined together with ``class`` to read a value from eBus. The ``class`` is required to distinguish modules in your system. Sometimes you can access the same value with different id/class combinations with different precisions.
class          | Together with ``id`` it addresses a unique value from your system.
cmd            | This allows you to send a command to the eBus. This parameter also needs the ``class`` parameter to addresses a unique command.
dst            | Define the destination eBus address as hex value for the telegram. 
src            | Defines the source eBus address as hex value for the telegram. Or default value ``FF`` would be used. You can overwrite this inf ``openhab.cfg``.
refresh        | If you set a refresh interval (sec.) the command will be automatic send via eBus. This is usually used to poll data from your system.
data           | Instead of the ``cmd`` parameter you can send a raw telegram as hex string. The last byte will be replaced by the calculated crc value if is not ``FF``.
data-{STATE}   | Same as parameter ``data``, but is only send on the specified state of the item. As example you can define a switch item with ``data-ON`` and ``data-OFF`` to send two different telegrams per state.

## Examples

### Receive values from eBus

	id:no_of_firing, class:heating_kw, dst:08

This item configuration receives the number of firings from the eBus. This is defined by parameter ``id`` and ``class``. But in this case we only want telegrams with the destination address 0x08. But keep in mind that this a read-only configuration. So we can only receive values that are submitted as broadcast or requested by a device.
Optionally you can also filter the source address with the parameter ``src``.

### Reading values from eBus regularly (Polling)

	id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, src:FF, refresh:60

This example is similar to the previous example, but here we active request this value every 60 seconds from the bus (Polling). If you can read multiple values with one command it's enough to send the command once. In this example the source address is explicit set to a hex string.

### Send raw telegrams, receive values

	id:no_of_firing, class:heating_kw, data:FF 08 50 22 03 CC 1A 27 00

In this example we send a raw telegram as hex string instead a already defined command. The last byte is replaced by the calculated crc value if it's not 0xFF (eBus sync byte). But you should prefer the ``cmd`` parameter. 

### Send raw telegram by item satus

	id:no_of_firing, class:heating_kw, data-ON:FF 08 50 22 03 CC 1A 27 00, data-OFF:FF 08 50 22 03 CC 0E 00 00

Similar to the last example, but we use different telegrams per item state. If you use a switch item you can send different telegram for the states ``ON`` or ``OFF``.

## List of ID's and Commands

### Wolf / Kromschröder

This is a list for a ``Wolf CSZ-2`` systems, hopefully it is compatible to other Wolf systems.

ID                     | Class             | Command                | Vendor          | Description
---                    | ---               | ---                    | ---             | ---
temp_exhaust           | heating_kw        | temp_exhaust           | Kromsch./Wolf   | Exhaust temperature
temp_vessel            | heating_kw        | temp_vessel            | Kromsch./Wolf   | Boiler temperature
temp_boiler            | heating_kw        | temp_boiler            | Kromsch./Wolf   | Hotwater temperature
temp_return            | heating_kw        | temp_return            | Kromsch./Wolf   | Return temperature
performance_burner     | heating_kw        | performance            | Kromsch./Wolf   | Current  device output %
performance_pump       | heating_kw        | performance_pump       | Kromsch./Wolf   | Current pump output %
system_pressure        | heating_kw        | system_pressure        | Kromsch./Wolf   | System Pressure
fw_version             | heating_kw        | fw_version             | Kromsch./Wolf   | Firmware Version Burner
no_of_firing           | heating_kw        | no_of_firing           | Kromsch./Wolf   | No. of Firing
ionisation             | heating_kw        | ionisation             | Kromsch./Wolf   | Ionisation
no_of_power_on         | heating_kw        | no_of_power_on         | Kromsch./Wolf   | No. of Power-On
op_hrs_heating_unit    | heating_kw        | op_hrs_heating_unit    | Kromsch./Wolf   | Operating hours burner
op_hrs_supply          | heating_kw        | op_hrs_supply          | Kromsch./Wolf   | Operating hours online
op_hrs_pump1           | heating_kw        | op_hrs_pump1           | Kromsch./Wolf   | Operating hours pump 1
e1                     | solar_kw          | e1                     | Kromsch./Wolf   | Solar return temperature
solar_current          | solar_kw          | -BROADCAST-            | Kromsch./Wolf   | Current solar output
yield_day              |                   | -BROADCAST-            | Kromsch./Wolf   | Solar day yield
yield_sum              |                   | -BROADCAST-            | Kromsch./Wolf   | Solar sum yield
solar_status           | solar_kw          | -BROADCAST-            | Kromsch./Wolf   | Solar status (bitmask)
solar_pump             |                   | -BROADCAST-            | Kromsch./Wolf   | Solar pump on
temp_collector         |                   | -BROADCAST-            | Kromsch./Wolf   | Temperature collector
temp_reservoir_1       |                   | -BROADCAST-            | Kromsch./Wolf   | Temperature storage 1

### Vaillant

Here a unsupported list for Vaillant devices. Here we need your support.

ID                     | Class             | Command                | Vendor          | Description
---                    | ---               | ---                    | ---             | ---
temp_ntc1              | water_v           | temp_ntc1              | Vaillant        | NTC sensor 1
temp_ntc2              | water_v           | temp_ntc2              | Vaillant        | NTC sensor 2
temp_ntc3              | water_v           | temp_ntc3              | Vaillant        | NTC sensor 3
temp_vessel            | heating_v         | temp_vessel            | Vaillant        | Boiler temperature
temp_outdoor           | control_v         | temp_vessel            | Vaillant        | Temperature outdoor
temp_return            | solar_v           | temp_return            | Vaillant        | Solar return temperature



### Example for item configuration
Hier ein Beispiel um wichtige Heizungsdaten anzuzeigen.

	/** Heizung **/
	Group HeatingUnit					"Heizung"												(All)
	Group Solar							"Solar"													(All)
	
	Group SOL_Chart1																			(HeatingUnit)
	Group SOL_Chart2																			(HeatingUnit)
	Group HU_Chart1																				(HeatingUnit)
	Group HU_Chart2																				(HeatingUnit)
	Number HU_Temp_Warm_Wather			"Hotwater temp.[%.1f °C]"			<__temperature>		(HeatingUnit,SOL_Chart1)	{ ebus="id:temp_boiler, class:heating_kw"}
	Number HU_Temp_M_Warm_Wather		"Hotwater min. temp.[%.1f °C]"		<__temperature>		(HeatingUnit)				{ ebus="id:temp_t_boiler, class:controller"}
	Number HU_Temp_T_Warm_Wather		"Hotwater temp(target)[%.1f °C]"	<__temperature>		(HeatingUnit)				{ ebus="id:temp_t_boiler, class:controller2, src:F1"}
	
	Number HU_Temp_Heat_Vessel			"Boiler temp. [%.1f °C]"		<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_vessel, class:auto_stroker, src:03"}
	Number HU_Temp_T_Heat_Vessel		"Boiler temp. (target) [%.1f °C]"	<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_t_vessel, class:controller"}
	Number HU_Temp_Heat_Return			"HU. Return [%.1f °C]"			<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_return, class:heating_kw"}
	Number HU_Temp_Heat_Exhaust			"Exhaust temp. [%.1f °C]"		<__temperature>		(HeatingUnit)				{ ebus="id:temp_exhaust, class:heating_kw"}
	Number HU_Temp_AvgOutdoor			"Avg. temp. outdoor [%.1f °C]"		<__temperature>		(HeatingUnit)				{ ebus="id:temp_outdoor, class:controller2, src:F1"}
	Number HU_Temp_Outdoor				"Temp. outdoor [%.1f °C]"				<__temperature>		(HeatingUnit)				{ ebus="id:temp_outdoor, class:controller2, src:03"}
	
	/** Heizung - Solar **/
	Switch SOL_Status_Pump				"Solar pump"						<switch>			(Solar,SOL_Chart2)			{ ebus="id:solar_pump, class:solar_kw"}
	Number SOL_Temp_Collector			"Sol. collektor temp. [%.1f °C]"	<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:temp_collector, class:solar_kw"}
	Number SOL_Temp_Return				"Sol. return temp. [%.1f °C]"		<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:e1, class:solar_kw"}
	Number SOL_Temp_Reservoir			"Sol. store temp. [%.1f °C]"		<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:temp_reservoir_1, class:solar_kw"}
	Number SOL_Yield_Sum				"Sol. sum yield [%.1f kW/h]"		<__bar_chart>		(Solar)						{ ebus="id:yield_sum, class:solar_kw"}
	Number SOL_Yield_Day				"Sol. day yield [%.2f kW/h]"		<__bar_chart>		(Solar)						{ ebus="id:yield_day, class:solar_kw"}
	Number SOL_Yield_Current			"Current output[%.2f kW]"			<__bar_chart>		(Solar,SOL_Chart2)			{ ebus="id:solar_current, class:solar_kw"}
	
	/** Heizung - Allg. Daten **/
	Number HU_Performance_Burner		"Unit output [%s %%]"			<__bar_chart>		(HeatingUnit)				{ ebus="id:performance_burner, class:heating_kw"}
	Number HU_Performance_Pump			"Pump output[%s %%]"				<__bar_chart>		(HeatingUnit)				{ ebus="id:performance_pump, class:heating_kw"}
	Number HU_No_Of_Firing				"No. of Firing[%s]"					<__bar_chart>		(HeatingUnit)				{ ebus="id:no_of_firing, class:heating_kw"}
	Number HU_Op_Hrs					"Op. hours unit[%s h]"				<__bar_chart>		(HeatingUnit)				{ ebus="id:op_hrs_heating_unit, class:heating_kw"}
	Switch HU_Status_Alarm				"Alarm [MAP(yesno_de.map):%s]"		<siren>				(HeatingUnit)				{ ebus="id:state_alarm, class:auto_stroker, src:03"}
	Switch HU_Status_Fire				"HU. flame [MAP(de.map):%s]"		<__gas2>			(HeatingUnit,HU_Chart1)		{ ebus="id:state_flame, class:auto_stroker, src:03"}
	Number HU_Status					"HU. status [%s]"					<settings>			(HeatingUnit)				{ ebus="id:status_auto_stroker, class:auto_stroker, src:03"}
	Number HU_Pressure_System			"System pressue [%.2f bar]"			<__temperature>		(HeatingUnit)				{ ebus="id:system_pressure, class:heating_kw"}
	Number HU_FW_Version				"Version[%.2f]"						<__bar_chart>		(HeatingUnit)				{ ebus="id:fw_version, class:heating_kw"}
	
	Number HU_Status_Burner		"Status burner [MAP(hu_CC5427_de.map):%s]"	<__temperature>		(HeatingUnit)				{ ebus="id:_w_unknown2, class:burnerw"}
	Number HU_Status_OpMode		"Op. mode [MAP(hu_CC7301_de.map):%s]"	<__temperature>		(HeatingUnit)				{ ebus="id:_w_unknown4, class:burnerw"}
	
	Number HU_PollingTest1				"Pollingtest1x [%s]"									(HeatingUnit)				{ ebus="id:fw_version2, data:FF 08 50 22 03 CC 9A 01 00, refresh:60"}
	Number HU_PollingTest2				"Pollingtest2 [%s]"										(HeatingUnit)				{ ebus="id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, refresh:10"}
