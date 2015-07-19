
## Changelog

Date        | Description
---         | ---
14.07.2015  | Enhanced send timings, add setter support, new wolf and vaillant commands, restructured json configuration files
16.05.2015  | Corrected logging id's
30.03.2015  | Change internal event handling from Thread to ThreadPool, enhance java doc
21.01.2015  | Add new vaillant configuration ``vaillant-bai00``

## Introduction
The eBUS binding allows you to control your heating system. The eBus protocol is used by heating system vendors like Wolf, Vaillant, Kromschröder etc. You can read temperatures, pump performance, gas consumption etc.

    ┌────────┐                       serial/usb (rs232) ┌──────┐
    │        │  serial (eBUS)  ┌───┐ or ethernet        │ ──── │
    │        │<--------------->│ X │<------------------>│ :    │
    │  ◊◊◊◊  │                 └───┘                    └──────┘
    └────────┘
    Heating Unit             eBUS Adapter              openHAB Server

To access your heating system you either need an eBus interface. You can buy a ready interface or solder your own circuit (examples: [eBUS Wiki](http://ebus.webhop.org/twiki/bin/view.pl/EBus/EBusKonverter)). A simple read-only interface can be build with an Arduino device.

For installation of the binding, please see Wiki page [[Bindings]]. You can download the latest binding from here: 
[eBus binding 1.8.0 SNAPSHOT](https://openhab.ci.cloudbees.com/job/openHAB/)

## Binding Configuration in openhab.cfg

You can find the configuration section for the eBus binding in file configurations/openhab.cfg, section "eBUS Binding". 

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
    # https://github.com/csowada/openhab/blob/ebus-update/bundles/binding/org.openhab.binding.ebus/docs/json-configs.md
    #
    # >> testing - All unknown or test telegrams (for developers only)
    # >> vaillant-bai00 - All Vaillant BAI000 telegrams
    # >> vaillant - All older vaillant telegrams (will be merged in other files)
    # >> vaillant-vrc470 - All Vaillant VRC470 telegrams
    # >> wolf-35 - All older Wolf eBUS dst address 0x35 telegrams (will be merged in other files)
    # >> wolf-cgb2 - All boiler Wolf CGB2 telegrams
    # >> wolf-cgb2_hc - All boiler Wolf CGB2 heating curve  telegrams
    # >> wolf-sm1 - All Wolf Solar Module SM1 telegrams
    # >> wolf-test - All Wolf unknown or test telegrams (for developers only)
    # >> custom - Use configuration defined by ebus:parserUrl
    # default uses common and all vendor specified telegrams
    #ebus:parsers=common,wolf,testing,custom
    
    # Advanced settings
    # Enable slow, but acurate write mode (normally not needed)
    #ebus:writeMode=slow
    
    # Write all/debug/unknown telegrams to a CSV file in folder {openhab}/etc/ebus
    #ebus:record=all,debug,unknown
    
    # Set the sender id of this binding, default is "FF"
    #ebus:senderId=FF

A sample configuration could look like:

    ebus:serialPort=COM2
    ebus:parsers=common,wolf
    
## Bind Items to eBUS

### Description
In order to bind an item to a eBus value you need to provide configuration settings. The easiest way to do so is to add binding information in your 'item file' (in the folder configurations/items`). The syntax for the eBUS binding configuration string is explained here:

### Parameters

This binding allows you to combine a set of parameters to get/set or request values on your heating system.

If you only want to read a value, you just need to set the parameters ``id`` and ``class``. You can find a list below.

Sometimes more than one bus participant will send telegrams with given ``id`` and ``class``. To filter the telegrams you can use the properties ``dst`` (destination address) and ``src`` (source address).

#### Reading parameters

    ebus="id:<id>, class:<class>"
    ebus="id:<id>, class:<class>, src:<src>"

Parameter      | Description
---            | ---
id             | Defined together with ``class`` to read a value from eBUS. The ``class`` is required to distinguish modules in your system. Sometimes you can access the same value with different id/class combinations with different precisions.
class          | Together with ``id`` it addresses a unique value from your system.
dst            | Filter telegrams for a specified destination eBUS address (hex string).
src            | Filter telegrams for a specified source eBUS address (hex string).

#### Writing parameter

There are two common types of writing to the bus. Broadcasts and Request/Answer commands.

To set or request values from your system, you have to set a ``cmd`` (command) parameter to your item.

Normally you send a request and expects an answer from a bus participant. But this binding works asynchronous, hence you can get return values only indirectly by using the read parameters. By this way you can update one or more items with only one command because a telegram can contain multiple values.

You should look at the examples below to understand the different cases.

The binding tries to send a commando up to five times before it cancels a request. You only get a short warning in the log. So this is fire-and-forget.

    ebus="id:<id>, class:<class>, cmd:<cmd>, dst:<dst>"
    ebus="id:<id>, class:<class>, cmd:<cmd>, dst:<dst>, refresh:<refresh>"

##### Request values
To request a value from an eBUS device use the request sytax with the parameter ``cmd`` and ``class``. This sends a request telegram to a specified eBUS address and should response with a valid answer. To set a value look at parameter ``set``.

##### Set value
To set a value you have to use the parameter ``cmd``, ``class`` and ``set``. But you need special commands that accepts setting values. You can only set one value per item (binding/openhab limitation).

##### Polling
On some systems it is required to request answers from bus participants regularly. In this case you can use the parameter ``refresh`` to send the request-command or RAW data automatically every n seconds.

##### RAW data
It is also possible to send a raw eBUS telegram directly without any programmatic help. You have to write the telegram as hex string like ``FF 08 50 22 03 CC 9A 01 00``. The last byte will be automatic replaced by a valid checksum if it not the sync byte ``FF``.

You should use this parameter only in special cases and prefere ``cmd`` instead.

Parameter      | Description
---            | ---
cmd            | This allows you to send a command to the eBUS. This parameter also needs the ``class`` parameter to addresses a unique command.
set            | Set a value id from a setter command
class          | Together with ``cmd`` it addresses a unique value from your system.
dst            | Define the destination eBUS address as hex value for the telegram. 
src            | Defines the source eBUS address as hex value for the telegram. Or default value ``FF`` would be used. You can overwrite this inf ``openhab.cfg``.
refresh        | If you set a refresh interval (sec.) the command will be automatic send via eBUS. This is usually used to poll data from your system.
data           | Instead of the ``cmd`` parameter you can send a raw telegram as hex string. The last byte will be replaced by the calculated crc value if is not ``FF``.
data-{STATE}   | Same as parameter ``data``, but is only send on the specified state of the item. As example you can define a switch item with ``data-ON`` and ``data-OFF`` to send two different telegrams per state.

## Examples

### Receive values from eBUS

    ebus="id:starts, class:burner, dst:08"

This item configuration receives the number of firings from the eBUS. This is defined by parameter ``id`` and ``class``. But in this case we only want telegrams with the destination address 0x08. But keep in mind that this a read-only configuration. So we can only receive values that are submitted as broadcast or requested by a device.
Optionally you can also filter the source address with the parameter ``src``.

### Reading values from eBUS regularly (Polling)

    ebus="id:starts, class:burner, cmd:starts, dst:08, src:FF, refresh:60"

This example is similar to the previous example, but here we active request this value every 60 seconds from the bus (Polling). If you can read multiple values with one command it's enough to send the command once. In this example the source address is explicit set to a hex string.

### Set a value

    ebus="id:program_heating, class:heating, cmd:set_heating_circuit_program, set:program_heating, dst:08"

This example uses the _set_heating_program_ command to set the value _program_heating_ to a new value.

### Send raw telegrams, receive values

    ebus="id:starts, class:burner, data:FF 08 50 22 03 CC 1A 27 00"

In this example we send a raw telegram as hex string instead a already defined command. The last byte is replaced by the calculated crc value if it's not 0xFF (eBUS sync byte). But you should prefer the ``cmd`` parameter. 

### Send raw telegram by item satus

    ebus="id:no_of_firing, class:heating_kw, data-ON:FF 08 50 22 03 CC 1A 27 00, data-OFF:FF 08 50 22 03 CC 0E 00 00"

Similar to the last example, but we use different telegrams per item state. If you use a switch item you can send different telegram for the states ``ON`` or ``OFF``.

## List of ID's and Commands

You can find a complete list of all supported devices [here](./docs/json-configs.md).

### Example for item configuration
Here is a longer example.

    /** Heating **/
    Group HeatingUnit                   "Heating"                                               (All)
    Group Solar                         "Solar"                                                 (All)
    
    Group SOL_Chart1                                                                            (HeatingUnit)
    Group SOL_Chart2                                                                            (HeatingUnit)
    Group HU_Chart1                                                                             (HeatingUnit)
    Group HU_Chart2                                                                             (HeatingUnit)
    Number HU_Temp_Warm_Wather          "Hotwater temp.[%.1f °C]"           <__temperature>     (HeatingUnit,SOL_Chart1)    { ebus="id:temp_boiler, class:heating_kw"}
    Number HU_Temp_M_Warm_Wather        "Hotwater min. temp.[%.1f °C]"      <__temperature>     (HeatingUnit)               { ebus="id:temp_t_boiler, class:controller"}
    Number HU_Temp_T_Warm_Wather        "Hotwater temp(target)[%.1f °C]"    <__temperature>     (HeatingUnit)               { ebus="id:temp_t_boiler, class:controller2, src:F1"}
    
    Number HU_Temp_Heat_Vessel          "Boiler temp. [%.1f °C]"            <__temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:temp_vessel, class:auto_stroker, src:03"}
    Number HU_Temp_T_Heat_Vessel        "Boiler temp. (target) [%.1f °C]"   <__temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:temp_t_vessel, class:controller"}
    Number HU_Temp_Heat_Return          "HU. Return [%.1f °C]"              <__temperature>     (HeatingUnit,HU_Chart1)     { ebus="id:temp_return, class:heating_kw"}
    Number HU_Temp_Heat_Exhaust         "Exhaust temp. [%.1f °C]"           <__temperature>     (HeatingUnit)               { ebus="id:temp_exhaust, class:heating_kw"}
    Number HU_Temp_AvgOutdoor           "Avg. temp. outdoor [%.1f °C]"      <__temperature>     (HeatingUnit)               { ebus="id:temp_outdoor, class:controller2, src:F1"}
    Number HU_Temp_Outdoor              "Temp. outdoor [%.1f °C]"           <__temperature>     (HeatingUnit)               { ebus="id:temp_outdoor, class:controller2, src:03"}
    
    /** Heating - Solar **/
    Switch SOL_Status_Pump              "Solar pump"                        <switch>            (Solar,SOL_Chart2)          { ebus="id:solar_pump, class:solar_kw"}
    Number SOL_Temp_Collector           "Sol. collektor temp. [%.1f °C]"    <__temperature>     (Solar,SOL_Chart1)          { ebus="id:temp_collector, class:solar_kw"}
    Number SOL_Temp_Return              "Sol. return temp. [%.1f °C]"       <__temperature>     (Solar,SOL_Chart1)          { ebus="id:e1, class:solar_kw"}
    Number SOL_Temp_Reservoir           "Sol. store temp. [%.1f °C]"        <__temperature>     (Solar,SOL_Chart1)          { ebus="id:temp_reservoir_1, class:solar_kw"}
    Number SOL_Yield_Sum                "Sol. sum yield [%.1f kW/h]"        <__bar_chart>       (Solar)                     { ebus="id:yield_sum, class:solar_kw"}
    Number SOL_Yield_Day                "Sol. day yield [%.2f kW/h]"        <__bar_chart>       (Solar)                     { ebus="id:yield_day, class:solar_kw"}
    Number SOL_Yield_Current            "Current output[%.2f kW]"           <__bar_chart>       (Solar,SOL_Chart2)          { ebus="id:solar_current, class:solar_kw"}
    
    /** Heating - Common Data **/
    Number HU_Performance_Burner        "Unit output [%s %%]"               <__bar_chart>       (HeatingUnit)               { ebus="id:performance_burner, class:heating_kw"}
    Number HU_Performance_Pump          "Pump output[%s %%]"                <__bar_chart>       (HeatingUnit)               { ebus="id:performance_pump, class:heating_kw"}
    Number HU_No_Of_Firing              "No. of Firing[%s]"                 <__bar_chart>       (HeatingUnit)               { ebus="id:no_of_firing, class:heating_kw"}
    Number HU_Op_Hrs                    "Op. hours unit[%s h]"              <__bar_chart>       (HeatingUnit)               { ebus="id:op_hrs_heating_unit, class:heating_kw"}
    Switch HU_Status_Alarm              "Alarm [MAP(yesno_de.map):%s]"      <siren>             (HeatingUnit)               { ebus="id:state_alarm, class:auto_stroker, src:03"}
    Switch HU_Status_Fire               "HU. flame [MAP(de.map):%s]"        <__gas2>            (HeatingUnit,HU_Chart1)     { ebus="id:state_flame, class:auto_stroker, src:03"}
    Number HU_Status                    "HU. status [%s]"                   <settings>          (HeatingUnit)               { ebus="id:status_auto_stroker, class:auto_stroker, src:03"}
    Number HU_Pressure_System           "System pressue [%.2f bar]"         <__temperature>     (HeatingUnit)               { ebus="id:system_pressure, class:heating_kw"}
    Number HU_FW_Version                "Version[%.2f]"                     <__bar_chart>       (HeatingUnit)               { ebus="id:fw_version, class:heating_kw"}
    
    Number HU_Status_Burner             "Status burner [MAP(hu_CC5427_de.map):%s]"  <__temperature>     (HeatingUnit)               { ebus="id:_w_unknown2, class:burnerw"}
    Number HU_Status_OpMode             "Op. mode [MAP(hu_CC7301_de.map):%s]"   <__temperature>     (HeatingUnit)               { ebus="id:_w_unknown4, class:burnerw"}
    
    Number HU_PollingExample1           "Polling Example 1 [%s]"                                    (HeatingUnit)               { ebus="id:fw_version2, data:FF 08 50 22 03 CC 9A 01 00, refresh:60"}
    Number HU_PollingExample2           "Polling Example 2 [%s]"                                        (HeatingUnit)               { ebus="id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, refresh:10"}

## Logging
Normally the binding is quiet and you can only see errors or important messages in your log files. But if you need more information then you can modify the logging file ``configuration/logback.xml``.

``org.openhab.binding.ebus.parser.EBusTelegramParser``

This is the standard logger for this binding

``org.openhab.binding.ebus.internal.parser.Analyses``

This is a special logger to show/analyse the received telegrams

``org.openhab.binding.ebus.internal.parser.BruteForce``

This is a special logger to show raw telegram data

## Custom Parser
You can add your own parser configuration by setting the property ``ebus:parserUrl`` in your ``openhab.cfg`` file.

More information [here](https://github.com/csowada/openhab-bindings/wiki/eBus-Parser-Config) (german language, use translator)
