
## _Important: Binding configuration has changed in version 1.8_

## Introduction
The eBUS binding allows you to control your heating system. The eBUS protocol is used by heating system vendors like Wolf, Vaillant, Kromschröder etc. You can read temperatures, pump performance, gas consumption etc.

     ┌────────┐                       serial/usb (rs232) ┌──────┐
     │        │  serial (eBUS)  ┌───┐ or ethernet        │ ──── │
     │        │<--------------->│ X │<------------------>│ :    │
     │  ◊◊◊◊  │                 └───┘                    └──────┘
     └────────┘
    Heating Unit             eBUS Adapter              openHAB Server

To access your heating system you either need an eBUS interface. You can buy a ready interface or solder your own circuit (examples: [eBUS Wiki](http://ebus-wiki.org/doku.php)). A simple read-only interface can be build with an Arduino device.

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration in openhab.cfg

You can find the configuration section for the eBUS binding in file configurations/openhab.cfg, section "eBUS Binding".

For your convenience you can see the relevant section as follows:

    # Serial port of eBUS interface
    # Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
    ebus:serialPort=COM2

    # TCP Hostname and Port
    # Warning: Only use ebus.hostname or ebus.serialPort
    #ebus:hostname=myhostname
    #ebus:port=5000

    # Custom parser configuration file
    # This example tries to load a configuration ${openhab_home}/configurations/ebus-config.json
    #ebus:parserUrl=platform:/base/../configurations/ebus-config.json

    # Define one or more parsers for you eBUS device. You can find the latest devices below or on the wiki page.
    # https://github.com/openhab/openhab/wiki/eBUS-IDs
    #
    # >> Deprecated
    #
    # >> _vaillant - All older vaillant telegrams (will be merged in other files)
    # >> _wolf-35 - All older Wolf eBUS dst address 0x35 telegrams (will be merged in other files)
    #
    # >> Productive
    #
    # >> common -  Commands from eBUS standard
    # >> vaillant-bai00 - All Vaillant BAI000 telegrams
    # >> vaillant-vrc430 - All Vaillant VRC430 telegrams
    # >> vaillant-vrc470 - All Vaillant VRC470 telegrams
    # >> vaillant-vrc630 - All Vaillant VRC630 telegrams
    # >> vaillant-vr90 - All Vaillant VR90 telegrams
    # >> wolf-cgb2_hc - All boiler Wolf CGB2 heating curve  telegrams
    # >> wolf-cgb2 - All boiler Wolf CGB2 telegrams
    # >> wolf-sm1 - All Wolf Solar Module SM1 telegrams
    # >> custom - Use configuration defined by ebus:parserUrl
    #
    # default uses common and all vendor specified telegrams
    #ebus:parsers=common,wolf,testing,custom

    # Write all/debug/unknown telegrams to a CSV file in folder {openhab}/etc/ebus
    #ebus:record=all,debug,unknown

    # Set the sender id of this binding, default is "FF"
    #ebus:senderId=FF

A sample configuration could look like:

    ebus:serialPort=COM2
    ebus:parsers=common,wolf

## Bind Items to eBUS

### Description
In order to bind an item to an eBUS value you need to provide configuration settings. The easiest way to do so is to add binding information in your 'item file' (in the folder configurations/items). The syntax for the eBUS binding configuration string is explained here:

### ID's

The binding uses unique ID's to identifiy eBUS telegrams from the internal database. The ID has a hierarchical structure and is in each case separated by a dot.


    <command-class>.<command-id>.<value>


ID Part           | Description
---               | ---
`<command-class>` | Groups multiple commands to logical class like _heating_, _boiler_, _solar_ etc.
`<command-id>`    | This is the identifier for the command
`<value>`         | The name of a result value, only required if the telegram contains more than one value

#### List of ID's

ID's                                                     | Description
---                                                      | ---
[Common](./docs/json-files/common.md)                    | Standard eBUS commands, Vendor independent
[Vaillant BAI00](./docs/json-files/vaillant-bai00.md)    | Vaillant BAI00
[Vaillant VRC430](./docs/json-files/vaillant-vrc430.md)  | Vaillant VRC430 controller
[Vaillant VRC470](./docs/json-files/vaillant-vrc470.md)  | Vaillant VRC470 controller
[Vaillant VRC630](./docs/json-files/vaillant-vrc630.md)  | Vaillant VRC630 controller
[Vaillant VR90](./docs/json-files/vaillant-vr90.md)      | Vaillant VR90 controller
[Wolf CGB2](./docs/json-files/wolf-cgb2.md)              | Wolf CGB2 Boiler, used in Heating system Wolf CSZ-2
[Wolf CGB2 HC](./docs/json-files/wolf-cgb2_hc.md)        | Wolf CGB2 Boiler (Heating Curve), used in Heating system Wolf CSZ-2
[Wolf SM1](./docs/json-files/wolf-sm1.md)                | Wolf Solar Module, used in Heating system Wolf CSZ-2

You can find a complete list of all supported devices [here](./docs/json-configs.md).

### Parameters

This binding allows you to combine a set of parameters to get/set or request values on your heating system.

If you only want to read a value, you just need to set the parameters `id`. You can find a list above.

Sometimes more than one bus participant will send telegrams with given `id`. To filter the telegrams you can use the properties `dst` (destination address) and `src` (source address).

#### Reading parameters

    ebus="id:<id>"
    ebus="id:<id>, src:<src>"

Parameter      | Description
---            | ---
id             | Defined to read a value from eBUS. Sometimes you can access the same value with different ids with different precisions.
dst            | Filter telegrams for a specified destination eBUS address (hex string).
src            | Filter telegrams for a specified source eBUS address (hex string).

#### Writing parameter

    ebus="id:<id>, dst:<dst>, refresh:<refresh>"
    ebus="id:<id>, dst:<dst>, refresh:<refresh>, set:<set>"
    
    ebus="id:<id>, cmd:<cmd>, dst:<dst>, refresh:<refresh>"
    ebus="id:<id>, cmd:<cmd>, dst:<dst>, refresh:<refresh>, set:<set>"
    ebus="id:<id>, dst:<dst>, refresh:<refresh>, set:<set>"
    

Parameter      | Description
---            | ---
id             | Defined to read a value from eBUS. This normally also identifies a request-command for polling. 
cmd            | Defines the request-command that should be send on `refresh` interval. Normally this parameter is not needed, because parameter `id` also identifies the configuration.
refresh        | Sends the request-command every n seconds (polling), requires parameter `id` or if not similar `cmd`.
set            | Defines a value that should be set. This requires special set commands.
dst            | Filter telegrams for a specified destination eBUS address (hex string).
src            | Filter telegrams for a specified source eBUS address (hex string).

Normally you send a request and expects an answer from a bus participant. But this binding works asynchronous, hence you can get return values only indirectly by using the read parameters. By this way you can update one or more items with only one command because a telegram can contain multiple values.

You should look at the examples below to understand the different cases.

The binding tries to send a commando up to five times before it cancels a request. You only get a short warning in the log. So this is fire-and-forget.

##### Set value
To set a value you have to use the parameter `set`. But you need special commands that accepts setting values. You can only set one value per item (binding/openhab limitation).

##### Polling
On some systems it is required to request answers from bus participants regularly. In this case you can use the parameter `refresh` to send the request-command or RAW data automatically every n seconds. You must add parameter `cmd` if it is not similar to parameter `id`.

##### RAW data
It is also possible to send a raw eBUS telegram directly without any programmatic help. You have to write the telegram as hex string like `FF 08 50 22 03 CC 9A 01 00`. The last byte will be automatic replaced by a valid checksum if it not the sync byte `FF`.

You should use this parameter only in special cases and prefer `cmd` instead.

Parameter      | Description
---            | ---
cmd            | This allows you to send a command to the eBUS.
set            | Set a value id from a setter command
dst            | Define the destination eBUS address as hex value for the telegram.
src            | Defines the source eBUS address as hex value for the telegram. Or default value `FF` would be used. You can overwrite this inf `openhab.cfg`.
refresh        | If you set a refresh interval (sec.) the command will be automatic send via eBUS. This is usually used to poll data from your system.
data           | Instead of the `cmd` parameter you can send a raw telegram as hex string. The last byte will be replaced by the calculated crc value if is not `FF`.
data-{STATE}   | Same as parameter `data`, but is only send on the specified state of the item. As example you can define a switch item with `data-ON` and `data-OFF` to send two different telegrams per state.

## Examples

### Receive values from eBUS

    ebus="id:burner.starts, dst:08"

This item configuration receives the number of firings from the eBUS. This is defined by parameter `id`. But in this case we only want telegrams with the destination address 0x08. But keep in mind that this a read-only configuration. So we can only receive values that are submitted as broadcast or requested by a device.
Optionally you can also filter the source address with the parameter `src`.

### Reading values from eBUS regularly (Polling)

    ebus="id:burner.starts, dst:08, src:FF, refresh:60"
    ebus="id:burner.starts, cmd:burner.starts, dst:08, src:FF, refresh:60"

This examples (both are do the same) are similar to the previous example, but here we active request this value every 60 seconds from the bus (Polling). If you can read multiple values with one command it's enough to send the command once. In this example the source address is explicit set to a hex string. If parameter `cmd` and `id` are equal you can omit parameter `cmd`.

### Set a value

    ebus="id:heating.program_heating_circuit, set:heating.set_program_heating_circuit.program, dst:08"

This example uses the _set_program_heating_circuit_ command to set the value _program_ to a new value.

### Send raw telegrams, receive values

    ebus="id:burner.starts, data:FF 08 50 22 03 CC 1A 27 00"

In this example we send a raw telegram as hex string instead a already defined command. The last byte is replaced by the calculated crc value if it's not 0xFF (eBUS sync byte). But you should prefer the `cmd` parameter.

### Send raw telegram by item status

    ebus="id:heating.no_of_firing, data-ON:FF 08 50 22 03 CC 1A 27 00, data-OFF:FF 08 50 22 03 CC 0E 00 00"

Similar to the last example, but we use different telegrams per item state. If you use a switch item you can send different telegram for the states `ON` or `OFF`.

### Example for item configuration
Here is a longer example.

    /** Heating **/
    Group HeatingUnit                   "Heating"                                             (All)
    Group Solar                         "Solar"                                               (All)

    Group SOL_Chart1                                                                          (HeatingUnit)
    Group SOL_Chart2                                                                          (HeatingUnit)
    Group HU_Chart1                                                                           (HeatingUnit)
    Group HU_Chart2                                                                           (HeatingUnit)
    Number HU_Temp_Warm_Wather          "Hotwater temp.[%.1f °C]"           <temperature>     (HeatingUnit,SOL_Chart1)    { ebus="id:dhw.temp_dhw"}
    Number HU_Temp_M_Warm_Wather        "Hotwater min. temp.[%.1f °C]"      <temperature>     (HeatingUnit)               { ebus="id:controller.temp_d_dhw"}
    Number HU_Temp_T_Warm_Wather        "Hotwater temp(target)[%.1f °C]"    <temperature>     (HeatingUnit)               { ebus="id:controller2.temp_d_dhw, src:F1"}
    Number HU_Temp_Heat_Vessel          "Boiler temp. [%.1f °C]"            <temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:auto_stroker.temp_boiler, src:03"}
    Number HU_Temp_T_Heat_Vessel        "Boiler temp. (target) [%.1f °C]"   <temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:controller.temp_d_boiler"}
    Number HU_Temp_Heat_Return          "HU. Return [%.1f °C]"              <temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:boiler.temp_return"}
    Number HU_Temp_Heat_Exhaust         "Exhaust temp. [%.1f °C]"           <temperature>     (HeatingUnit)               { ebus="id:boiler.temp_exhaust"}
    Number HU_Temp_AvgOutdoor           "Avg. temp. outdoor [%.1f °C]"      <temperature>     (HeatingUnit)               { ebus="id:controller2.temp_outside, src:F1"}
    Number HU_Temp_Outdoor              "Temp. outdoor [%.1f °C]"           <temperature>     (HeatingUnit)               { ebus="id:controller2.temp_outside, src:03"}
    Switch SOL_Status_Pump              "Solar pump"                        <switch>          (Solar,SOL_Chart2)          { ebus="id:solar.solar_data.state_pump"}
    Number SOL_Temp_Collector           "Sol. collektor temp. [%.1f °C]"    <temperature>     (Solar,SOL_Chart1)          { ebus="id:solar.solar_data.temp_collector"}
    Number SOL_Temp_Return              "Sol. return temp. [%.1f °C]"       <temperature>     (Solar,SOL_Chart1)          { ebus="id:solar.temp_flow"}
    Number SOL_Temp_Reservoir           "Sol. store temp. [%.1f °C]"        <temperature>     (Solar,SOL_Chart1)          { ebus="id:solar.solar_data.temp_cylinder"}
    Number SOL_Yield_Sum                "Sol. sum yield [%.1f kW/h]"        <bar_chart>       (Solar)                     { ebus="id:solar.solar_yield.yield_total"}
    Number SOL_Yield_Day                "Sol. day yield [%.2f kW/h]"        <bar_chart>       (Solar)                     { ebus="id:solar.solar_yield.yield_today"}
    Number SOL_Yield_Current            "Current output[%.2f kW]"           <bar_chart>       (Solar,SOL_Chart2)          { ebus="id:solar.solar_yield.output_current"}
    Number HU_Performance_Burner        "Unit output [%s %%]"               <bar_chart>       (HeatingUnit)               { ebus="id:boiler.level_modulation"}
    Number HU_Performance_Pump          "Pump output[%s %%]"                <bar_chart>       (HeatingUnit)               { ebus="id:heating.modulation_pump"}
    Number HU_No_Of_Firing              "No. of Firing[%s]"                 <bar_chart>       (HeatingUnit)               { ebus="id:burner.starts"}
    Number HU_Op_Hrs                    "Op. hours unit[%s h]"              <bar_chart>       (HeatingUnit)               { ebus="id:boiler.runtime"}
    Switch HU_Status_Alarm              "Alarm [MAP(yesno_de.map):%s]"      <siren>           (HeatingUnit)               { ebus="id:auto_stroker.state_alarm, src:03"}
    Switch HU_Status_Fire               "HU. flame [MAP(de.map):%s]"        <gas2>            (HeatingUnit,HU_Chart1)     { ebus="id:auto_stroker.state_flame, src:03"}
    Number HU_Status                    "HU. status [%s]"                   <settings>        (HeatingUnit)               { ebus="id:auto_stroker.status_auto_stroker, src:03"}
    Number HU_Pressure_System           "System pressue [%.2f bar]"         <temperature>     (HeatingUnit)               { ebus="id:heating.pressure"}
    Number HU_FW_Version                "Version[%.2f]"                     <bar_chart>       (HeatingUnit)               { ebus="id:boiler.fw_version"}
    Number HU_StatusReq1                "Status Request 1 [MAP(hu_status_request1_de.map):%s]" <temperature> (HeatingUnit) { ebus="id:controller.status_heat_req1"}
    Number HU_StatusReq2                "Status Request 2 [MAP(hu_status_request2_de.map):%s]" <temperature> (HeatingUnit) { ebus="id:controller.status_heat_req2"}
    Number HU_Program_HC                "Program Heating Circuit [MAP(hu_heat_prog_de.map):%s]" <siren> (HeatingUnit)     { ebus="id:heating.program_heating_circuit, refresh:10, set:heating.set_program_heating_circuit.program"}
    Switch HU_Status_Pump               "Pump [%s]"                         <__idea>            (HeatingUnit,HU_Chart1)   { ebus="id:auto_stroker.state_pump, src:03"}

## Logging
Normally the binding is quiet and you can only see errors or important messages in your log files. But if you need more information then you can modify the logging file `configuration/logback.xml`. Open the file and search for the position below. Then you can add the example lines.

	...
	<logger name="org.openhab" level="INFO"/>
	
	<!-- this class gives an irrelevant warning at startup -->
	<logger name="org.atmosphere.cpr.AtmosphereFramework" level="ERROR" />
	<!-- Add eBus logger here -->
	<logger name="org.openhab.binding.ebus" level="DEBUG" />
	...

### Special loggers for developers
These loggers normally only interesting for developers. This loggers can cause huge log files.

`org.openhab.binding.ebus.parser.EBusTelegramParser`

This is the standard logger for this binding

`org.openhab.binding.ebus.internal.parser.Analyses`

This is a special logger to show/analyse the received telegrams

`org.openhab.binding.ebus.internal.parser.BruteForce`

This is a special logger to show raw telegram data

## FAQ

...

## Custom Parser
You can add your own parser configuration by setting the property `ebus:parserUrl` in your `openhab.cfg` file. This is useful if you get new configuratiosn files that are not already included in this binding.

More information [here](https://github.com/csowada/openhab-bindings/wiki/eBUS-Parser-Config) (german language, use translator)
