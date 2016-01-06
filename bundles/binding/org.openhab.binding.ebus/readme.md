_**Note:** This Binding will be available in the upcoming 1.7 Release. For preliminary builds please see the [CI server at Cloudbees](https://openhab.ci.cloudbees.com/job/openHAB/)._

## Change log

Date        | Description
---         | ---
30.03.2015  | Change internal event handling from Thread to ThreadPool, enhance java doc
21.01.2015  | Add new vaillant configuration ``vaillant-bai00``

## Introduction
The eBus binding allows you to control your heating system. The eBus protocol is used by heating system vendors like Wolf, Vaillant, Kromschröder etc. You can read temperatures, pump performance, gas consumption etc.

	┌────────┐                                          ┌──────┐
	│        │  serial (eBus)  ┌───┐ serial/usb (rs232) │ ──── │
	│        │<--------------->│ X │<------------------>│ :    │
	│  ◊◊◊◊  │                 └───┘                    └──────┘
	└────────┘
	Heating Unit             eBus Adapter              openHAB Server

To access your heating system you either need an eBus interface. You can buy a ready interface or solder your own circuit (examples: [eBus Wiki](http://ebus.webhop.org/twiki/bin/view.pl/EBus/EBusKonverter)). A simple read-only interface can be build with an Arduino device.

For installation of the binding, please see Wiki page [[Bindings]]. You can download the latest binding from here: 
[eBus binding 1.7.0 SNAPSHOT](https://buildhive.cloudbees.com/job/openhab/job/openhab/2067/org.openhab.binding$org.openhab.binding.ebus/artifact/org.openhab.binding/org.openhab.binding.ebus/1.7.0-SNAPSHOT/org.openhab.binding.ebus-1.7.0-SNAPSHOT.jar)

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
	# >> vaillant-bai00 - Telegrams for Vaillant Controller (used by VSC 196 etc. ?) 
	# >> testing -  All unknown or test telegrams
	# >> custom - Use configuration defined by ebus:parserUrl
	# default uses common and all vendor specified telegrams
	#ebus:parsers=common,wolf,testing,custom
	
	# Set the sender id of this binding, default is "FF"
	#ebus:senderId=FF

A sample configuration could look like:

	ebus:serialPort=COM2
	ebus:parsers=common,wolf
	
## Bind Items to eBus

### Description
In order to bind an item to a eBus value you need to provide configuration settings. The easiest way to do so is to add binding information in your 'item file' (in the folder configurations/items`). The syntax for the eBus binding configuration string is explained here:

### Parameters

This binding allows you to combine a set of parameters to get/set or request values on your heating system.

If you only want to read a value, you just need to set the parameters ``id`` and ``class``. You can find a list below.

Sometimes more than one bus participant will send telegrams with given ``id`` and ``class``. To filter the telegrams you can use the properties ``dst`` (destination address) and ``src`` (source address).

#### Reading parameters

	ebus="id:<id>, class:<class>"
	ebus="id:<id>, class:<class>, src:<src>"

Parameter      | Description
---            | ---
id             | Defined together with ``class`` to read a value from eBus. The ``class`` is required to distinguish modules in your system. Sometimes you can access the same value with different id/class combinations with different precisions.
class          | Together with ``id`` it addresses a unique value from your system.
dst            | Filter telegrams for a specified destination eBus address (hex string).
src            | Filter telegrams for a specified source eBus address (hex string).

#### Writing parameter

There are two common types of writing to the bus. Broadcasts and Request/Answer commands.

To set or request values from your system, you have to set a ``cmd`` (command) parameter to your item.

Normally you send a request and expects an answer from a bus participant. But this binding works asynchronous, hence you can get return values only indirectly by using the read parameters. By this way you can update one or more items with only one command because a telegram can contain multiple values.

You should look at the examples below to understand the different cases.

The binding tries to send a commando up to five times before it cancels a request. You only get a short warning in the log. So this is fire-and-forget.

	ebus="id:<id>, class:<class>, cmd:<cmd>, dst:<dst>"
	ebus="id:<id>, class:<class>, cmd:<cmd>, dst:<dst>, refresh:<refresh>"

##### Polling
On some systems it is required to request answers from bus participants regularly. In this case you can use the parameter ``refresh`` to send the command or RAW data automatically every n seconds.

##### RAW data
It is also possible to send a raw eBus telegram directly without any programmatic help. You have to write the telegram as hex string like ``FF 08 50 22 03 CC 9A 01 00``. The last byte will be automatic replaced by a valid checksum if it not the sync byte ``FF``.

You should use this parameter only in special cases and prefere ``cmd`` instead.

Parameter      | Description
---            | ---
cmd            | This allows you to send a command to the eBus. This parameter also needs the ``class`` parameter to addresses a unique command.
class          | Together with ``cmd`` it addresses a unique value from your system.
dst            | Define the destination eBus address as hex value for the telegram. 
src            | Defines the source eBus address as hex value for the telegram. Or default value ``FF`` would be used. You can overwrite this inf ``openhab.cfg``.
refresh        | If you set a refresh interval (sec.) the command will be automatic send via eBus. This is usually used to poll data from your system.
data           | Instead of the ``cmd`` parameter you can send a raw telegram as hex string. The last byte will be replaced by the calculated crc value if is not ``FF``.
data-{STATE}   | Same as parameter ``data``, but is only send on the specified state of the item. As example you can define a switch item with ``data-ON`` and ``data-OFF`` to send two different telegrams per state.

## Examples

### Receive values from eBus

	ebus="id:no_of_firing, class:heating_kw, dst:08"

This item configuration receives the number of firings from the eBus. This is defined by parameter ``id`` and ``class``. But in this case we only want telegrams with the destination address 0x08. But keep in mind that this a read-only configuration. So we can only receive values that are submitted as broadcast or requested by a device.
Optionally you can also filter the source address with the parameter ``src``.

### Reading values from eBus regularly (Polling)

	ebus="id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, src:FF, refresh:60"

This example is similar to the previous example, but here we active request this value every 60 seconds from the bus (Polling). If you can read multiple values with one command it's enough to send the command once. In this example the source address is explicit set to a hex string.

### Send raw telegrams, receive values

	ebus="id:no_of_firing, class:heating_kw, data:FF 08 50 22 03 CC 1A 27 00"

In this example we send a raw telegram as hex string instead a already defined command. The last byte is replaced by the calculated crc value if it's not 0xFF (eBus sync byte). But you should prefer the ``cmd`` parameter. 

### Send raw telegram by item satus

	ebus="id:no_of_firing, class:heating_kw, data-ON:FF 08 50 22 03 CC 1A 27 00, data-OFF:FF 08 50 22 03 CC 0E 00 00"

Similar to the last example, but we use different telegrams per item state. If you use a switch item you can send different telegram for the states ``ON`` or ``OFF``.

## List of ID's and Commands

### Common

This is a list based on the eBus specification - Application Layer - OSI 7 V1.6.3 from 03/2007

ID                     | Class             | Command                | Item type | Description
---                    | ---               | ---                    | ---       | ---
vendor                 | common            | -                      | Number    | Vendor ID
device                 | common            | -                      | String    | Device String
software_version       | common            | -                      | Number    | Software Version
hardware_version       | common            | -                      | Number    | Hardware Version
status_auto_stroker    | auto_stroker      | -                      | Number    | Status auto stroker
state_ldw              | auto_stroker      | -                      | Switch    | Status ldw?
state_gdw              | auto_stroker      | -                      | Switch    | Status gdw?
state_ws               | auto_stroker      | -                      | Switch    | Status ws?
state_flame            | auto_stroker      | -                      | Switch    | Status flame
state_valve1           | auto_stroker      | -                      | Switch    | Status valve 1
state_valve2           | auto_stroker      | -                      | Switch    | Status valve 2
state_uwp              | auto_stroker      | -                      | Switch    | Status circulation pump
state_alarm            | auto_stroker      | -                      | Switch    | Status alarm
performance_burner     | auto_stroker      | -                      | Number    | Performance burner
temp_vessel            | auto_stroker      | -                      | Number    | Vessel temperature 
temp_return            | auto_stroker      | -                      | Number    | Return temperature
temp_boiler            | auto_stroker      | -                      | Number    | Boiler temperature
temp_outdoor           | auto_stroker      | -                      | Number    | Outdoor temperature
status_warm_req1       | controller        | -                      | Switch    | Status warm request 1
status_warm_req2       | controller        | -                      | Switch    | Status warm request 1
temp_t_vessel          | controller        | -                      | Number    | Target vessel temperature
pressure_t_vessel      | controller        | -                      | Number    | Target vessel pressure
performance_burner     | controller        | -                      | Number    | Performance burner
temp_t_boiler          | controller        | -                      | Number    | Target boiler temperature
value_fuel             | controller        | -                      | Number    | Fuel value
date                   | common            | -                      | String    | Date value
time                   | common            | -                      | String    | Time value
temp_t_vessel          | controller2       | -                      | Number    | Target vessel temperature
temp_outdoor           | controller2       | -                      | Number    | Outdoor temperature
power_enforcement      | controller2       | -                      | Number    | Power entforcement
status_bwr             | controller2       | -                      | Switch    | Status bwr?
status_heat_circuit    | controller2       | -                      | Switch    | Status heat circuit
temp_t_boiler          | controller2       | -                      | Number    | Target boiler temperature

### Wolf / Kromschröder

This is a list for a ``Wolf CSZ-2`` systems, hopefully it is compatible to other Wolf systems.
Compatible with ``COB-20`` for some values.

ID                     | Class             | Command                | Item type | Description
---                    | ---               | ---                    | ---       | ---
temp_exhaust           | heating_kw        | temp_exhaust           | Number    | Exhaust temperature
temp_vessel            | heating_kw        | temp_vessel            | Number    | Boiler temperature
temp_boiler            | heating_kw        | temp_boiler            | Number    | Hotwater temperature
temp_return            | heating_kw        | temp_return            | Number    | Return temperature
performance_burner     | heating_kw        | performance            | Number    | Current  device output %
performance_pump       | heating_kw        | performance_pump       | Number    | Current pump output %
system_pressure        | heating_kw        | system_pressure        | Number    | System Pressure
fw_version             | heating_kw        | fw_version             | Number    | Firmware Version Burner
no_of_firing           | heating_kw        | no_of_firing           | Number    | No. of Firing
ionisation             | heating_kw        | ionisation             | Number    | Ionisation
no_of_power_on         | heating_kw        | no_of_power_on         | Number    | No. of Power-On
op_hrs_heating_unit    | heating_kw        | op_hrs_heating_unit    | Number    | Operating hours burner
op_hrs_supply          | heating_kw        | op_hrs_supply          | Number    | Operating hours online
op_hrs_pump1           | heating_kw        | op_hrs_pump1           | Number    | Operating hours pump 1
e1                     | solar_kw          | e1                     | Number    | Solar return temperature
solar_current          | solar_kw          | -BROADCAST-            | Number    | Current solar output
yield_day              | solar_kw          | -BROADCAST-            | Number    | Solar day yield
yield_sum              | solar_kw          | -BROADCAST-            | Number    | Solar sum yield
solar_status           | solar_kw          | -BROADCAST-            | Number    | Solar status (bitmask)
solar_pump             | solar_kw          | -BROADCAST-            | Switch    | Solar pump on
temp_collector         | solar_kw          | -BROADCAST-            | Number    | Temperature collector
temp_reservoir_1       | solar_kw          | -BROADCAST-            | Number    | Temperature storage 1

### Vaillant

Here a unsupported list for Vaillant devices. Here we need your support.

ID                     | Class             | Command                | Item type | Description
---                    | ---               | ---                    | ---       | ---
temp_ntc1              | water_v           | temp_ntc1              | Number    | NTC sensor 1
temp_ntc2              | water_v           | temp_ntc2              | Number    | NTC sensor 2
temp_ntc3              | water_v           | temp_ntc3              | Number    | NTC sensor 3
temp_vessel            | heating_v         | temp_vessel            | Number    | Boiler temperature
temp_outdoor           | control_v         | temp_vessel            | Number    | Temperature outdoor
temp_return            | solar_v           | temp_return            | Number    | Solar return temperature

### Vaillant Testing ( BAI 00 )

Here a unsupported list for Vaillant BIA 00 device. ID's, class and command names can be changed in further releases.

ID                     | Class             | Command                | Item type | Description
---                    | ---               | ---                    | ---       | ---
performance_burner-kw  | bai00             | performance_burner-kw  | Number    | Burner performance kW
performance_burner     | bai00             | performance_burner     | Number    | Burner performance %
temp_flow              | bai00             | temp_flow              | Number    | Flow temperature °C
status_flow            | bai00             |                        | Switch    | Flow temperature status
temp_return            | bai00             | temp_return            | Number    | Return temperature °C
status_return          | bai00             |                        | Switch    | Return temperature status


### Example for item configuration
Here is a longer example.

	/** Heating **/
	Group HeatingUnit					"Heating"												(All)
	Group Solar							"Solar"													(All)
	
	Group SOL_Chart1																			(HeatingUnit)
	Group SOL_Chart2																			(HeatingUnit)
	Group HU_Chart1																				(HeatingUnit)
	Group HU_Chart2																				(HeatingUnit)
	Number HU_Temp_Warm_Wather			"Hotwater temp.[%.1f °C]"			<__temperature>		(HeatingUnit,SOL_Chart1)	{ ebus="id:temp_boiler, class:heating_kw"}
	Number HU_Temp_M_Warm_Wather		"Hotwater min. temp.[%.1f °C]"		<__temperature>		(HeatingUnit)				{ ebus="id:temp_t_boiler, class:controller"}
	Number HU_Temp_T_Warm_Wather		"Hotwater temp(target)[%.1f °C]"	<__temperature>		(HeatingUnit)				{ ebus="id:temp_t_boiler, class:controller2, src:F1"}
	
	Number HU_Temp_Heat_Vessel			"Boiler temp. [%.1f °C]"			<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_vessel, class:auto_stroker, src:03"}
	Number HU_Temp_T_Heat_Vessel		"Boiler temp. (target) [%.1f °C]"	<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_t_vessel, class:controller"}
	Number HU_Temp_Heat_Return			"HU. Return [%.1f °C]"				<__temperature>		(HeatingUnit,HU_Chart1)		{ ebus="id:temp_return, class:heating_kw"}
	Number HU_Temp_Heat_Exhaust			"Exhaust temp. [%.1f °C]"			<__temperature>		(HeatingUnit)				{ ebus="id:temp_exhaust, class:heating_kw"}
	Number HU_Temp_AvgOutdoor			"Avg. temp. outdoor [%.1f °C]"		<__temperature>		(HeatingUnit)				{ ebus="id:temp_outdoor, class:controller2, src:F1"}
	Number HU_Temp_Outdoor				"Temp. outdoor [%.1f °C]"			<__temperature>		(HeatingUnit)				{ ebus="id:temp_outdoor, class:controller2, src:03"}
	
	/** Heating - Solar **/
	Switch SOL_Status_Pump				"Solar pump"						<switch>			(Solar,SOL_Chart2)			{ ebus="id:solar_pump, class:solar_kw"}
	Number SOL_Temp_Collector			"Sol. collektor temp. [%.1f °C]"	<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:temp_collector, class:solar_kw"}
	Number SOL_Temp_Return				"Sol. return temp. [%.1f °C]"		<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:e1, class:solar_kw"}
	Number SOL_Temp_Reservoir			"Sol. store temp. [%.1f °C]"		<__temperature>		(Solar,SOL_Chart1)			{ ebus="id:temp_reservoir_1, class:solar_kw"}
	Number SOL_Yield_Sum				"Sol. sum yield [%.1f kW/h]"		<__bar_chart>		(Solar)						{ ebus="id:yield_sum, class:solar_kw"}
	Number SOL_Yield_Day				"Sol. day yield [%.2f kW/h]"		<__bar_chart>		(Solar)						{ ebus="id:yield_day, class:solar_kw"}
	Number SOL_Yield_Current			"Current output[%.2f kW]"			<__bar_chart>		(Solar,SOL_Chart2)			{ ebus="id:solar_current, class:solar_kw"}
	
	/** Heating - Common Data **/
	Number HU_Performance_Burner		"Unit output [%s %%]"				<__bar_chart>		(HeatingUnit)				{ ebus="id:performance_burner, class:heating_kw"}
	Number HU_Performance_Pump			"Pump output[%s %%]"				<__bar_chart>		(HeatingUnit)				{ ebus="id:performance_pump, class:heating_kw"}
	Number HU_No_Of_Firing				"No. of Firing[%s]"					<__bar_chart>		(HeatingUnit)				{ ebus="id:no_of_firing, class:heating_kw"}
	Number HU_Op_Hrs					"Op. hours unit[%s h]"				<__bar_chart>		(HeatingUnit)				{ ebus="id:op_hrs_heating_unit, class:heating_kw"}
	Switch HU_Status_Alarm				"Alarm [MAP(yesno_de.map):%s]"		<siren>				(HeatingUnit)				{ ebus="id:state_alarm, class:auto_stroker, src:03"}
	Switch HU_Status_Fire				"HU. flame [MAP(de.map):%s]"		<__gas2>			(HeatingUnit,HU_Chart1)		{ ebus="id:state_flame, class:auto_stroker, src:03"}
	Number HU_Status					"HU. status [%s]"					<settings>			(HeatingUnit)				{ ebus="id:status_auto_stroker, class:auto_stroker, src:03"}
	Number HU_Pressure_System			"System pressue [%.2f bar]"			<__temperature>		(HeatingUnit)				{ ebus="id:system_pressure, class:heating_kw"}
	Number HU_FW_Version				"Version[%.2f]"						<__bar_chart>		(HeatingUnit)				{ ebus="id:fw_version, class:heating_kw"}
	
	Number HU_Status_Burner				"Status burner [MAP(hu_CC5427_de.map):%s]"	<__temperature>		(HeatingUnit)				{ ebus="id:_w_unknown2, class:burnerw"}
	Number HU_Status_OpMode				"Op. mode [MAP(hu_CC7301_de.map):%s]"	<__temperature>		(HeatingUnit)				{ ebus="id:_w_unknown4, class:burnerw"}
	
	Number HU_PollingExample1			"Polling Example 1 [%s]"									(HeatingUnit)				{ ebus="id:fw_version2, data:FF 08 50 22 03 CC 9A 01 00, refresh:60"}
	Number HU_PollingExample2			"Polling Example 2 [%s]"										(HeatingUnit)				{ ebus="id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, refresh:10"}

## Customize this binding

?

### How the binding works

?

### Logging

?

### Modify/create own Parser configuration

?
