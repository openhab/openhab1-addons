# Z-Wave Binding

The openHAB Z-Wave binding allows you to connect to your Z-Wave wireless mesh network.  A Z-Wave network typically consists of one primary controller “stick”, zero or more additional controllers and zero or more Z-Wave enabled devices, e.g. dimmers, switches, sensors etc.

<!-- MarkdownTOC depth=1 -->

- [Prerequisites](#prerequisites)
- [Binding Configuration](#binding-configuration)
- [Item Configuration](#item-configuration)
- [Battery Devices and Wakeup](#battery-devices-and-wakeup)
- [Database](#database)
- [Logging](#logging)
- [Examples](#examples)
- [Z-Wave Security Testing](#z-wave-security-testing)

<!-- /MarkdownTOC -->

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/zwave/).

## Prerequisites

Connection to the Z-Wave controller is done through the serial port of your host system. USB controllers typically create a virtual COM port to connect to the stick. Please write down the port name before configuring this binding. In case your port name changes dynamically and you want to use a symlink, see [Tricks](https://github.com/openhab/openhab1-addons/wiki/Samples-Tricks#how-to-configure-openhab-to-connect-to-device-symlinks-on-linux). 

Initialization of the binding typically takes several seconds to minutes depending on the number of devices in the network. When battery operated devices are used the binding tries to reach the device first. After one minute the node is marked sleeping. On wake up of the device initialization will continue.

[HABmin](https://github.com/cdjackson/HABmin) can be used to configure devices ([setting configuration parameters and association groups](https://github.com/cdjackson/HABmin/wiki/Z-Wave-Configuration)) directly within openHAB. Alternatively the [open-zwave control panel](https://code.google.com/p/openzwave-control-panel/) is a good choice to set up your network. Commercial software like Homeseer can be used as well.

**For technical reference and implementation:** Chris Jackson maintains an excellent technical resource for the ZWave binding on the [HABmin Wiki](https://github.com/cdjackson/HABmin/wiki/Z-Wave-Configuration)

### Supported controllers

The binding supports all controllers that implement the Z-Wave Serial API. A list of confirmed supported controllers is

| Controller | Remarks |
|------------|---------|
| Aeon Labs USB Z-Stick | No remarks |
| Aeon Labs USB Z-Stick Gen5 | Do NOT enable Soft Reset |
| The Razberry-Zwave-Daughterboard | See known issues |
| Vision USB stick Z-wave | Do NOT enable Soft Reset |
| Z-Wave.me Z-StickC | No remarks |
| Z-Wave.me ZME-UZB1 | Do NOT enable Soft Reset |
| Sigma UZB ZWave-Plus | No Windows drivers ? |

**NOTE:** Sigma UZB ZWave-Plus driver can be get there https://github.com/benoit934/drivers/blob/master/ZW050x_USB_VCP_PC_Driver.zip 
This is an official driver for the RS232 chip, you just have to install it manually using the inf file, actually working on Windows 7, probably any Windows version.

## Binding Configuration

First of all you need to introduce the port settings of your Z-Wave controller in the file `services/zwave.cfg`.

| Property |  Description |
|----------|--------------|
| port     |  value indicates the serial port on the host system to which the Z-Wave controller is connected, e.g. "COM1" on Windows, "/dev/ttyS0" or "/dev/ttyUSB0" on Linux or "/dev/tty.PL2303-0000103D" on Mac.<br/>Note that some controllers register themselves as a modem (/dev/ttyACM) on Linux. In this case it is necessary to add user "openhab" to the group "dialout". Else openHAB won't be able to access the controller. |
| healtime |  Sets the hour of the day when a network heal will be performed. This will try to update the neighbor node list, associations and routes. The actual routing is performed by the controller. |
| pollingQueue | Sets the maximum number of frames in the polling queue at once. This avoids swamping the network with poll messages. |
| aliveCheckPeriod | Sets the time (in milliseconds) between each node health check message. This is used to periodically check if a node is alive. |
| softReset | Set to `true` to perform a soft reset on the controller during the heal, and when the binding starts. This can help solve some problems with the stick, but it can also cause some new controllers to lock up (eg the ZWave Plus controllers) |
| masterController | This option tells the binding that it is the main controller in the network.  This isn't necessarily the same as a primary controller - it simply means that your openhab binding is being used as the main network interface. If set to `true`, the binding will configure devices automatically to send some communications to the binding. This would include setting the wakeup class to send notifications to openhab, and set some associations so that the binding recieves notifications of configuration change or alarms. |
| setSUC | Set to `true` to set the controller to the special role of Static Update Controller, whereby it will be the keeper of the routing table from the primary controller and offer it to all other controllers in the network. |
| networkKey | See [Z-Wave Security Testing](#z-wave-security-testing) |

## Item Configuration

The format of the binding configuration is simple and looks like this:

```
zwave="<nodeId>[:<endpointId>][:command=<command>[,parameter=<value>][,parameter=<value>]...]"
```

where parts in `[square brackets]` indicate an optional component. Usually, only one item is bound to a device, but more items can be bound to a device as well, either for reporting variables, or in case the device consists of multiple endpoints / instances.

The `<nodeId>` indicates the number (in decimal notation) of the node, to which this item is bound. To find out your devices' `<nodeIds>`, either look at the startup log of openHAB, or use other Z-Wave configuration programs like openzwave control panel to detect and configure your setup.

The `<endpointId>` is only required/allowed when using the `multi_instance` command class. In case a node consists of multiple instances or endpoints, the instance number can be specified using this value. The default value is not to use the `multi_instance` command class - the number must be positive and must not be 0. An example of a multi-endpoint device is the Fibaro FGS 221 double relay.

If you're not sure about endpoint numbering, look in the logs to see if sensor data is being correlated. You may see the following warning -:

```
[WARN ]  o.o.b.z.i.ZWaveActiveBinding[:459]- NODE 8: No item bound for event, endpoint = 0, command class = SENSOR_MULTILEVEL, value = 51, ignoring.
```

This indicates that the binding can’t find an item linked to this sensor - often this is because the endpoint numbering is incorrect. If the warning above says endpoint = 0, then the binding string shouldn't include an endpoint number.

The `<command>` is optional, but recommended if you have multiple items bound to the same device, or the device reports multiple bits of information. Without the command class, the binding cannot unambiguously differentiate different data, so it is recommended to provide a command class. Z-Wave nodes support functionality through command classes. A specific command class can be specified to use that specific functionality of the node. A node can contain multiple supported command classes. If the command is omitted, the best suitable command class for the item / node combination is automatically chosen.

Command classes may support parameters. A parameter is a name=value pair that configures some aspect of the command class on the node or in the binding.

### Supported Command Classes

Each node in the network provides functionality in the form of Command Classes. The OpenHAB Z-Wave binding implements the same Command Classes to be able to use the nodes in the network. Not all Z-Wave Command classes are currently supported by the binding. The supported command classes are listed in the table below.

| Command Class | Remarks | Supported parameters |
|---------------|---------|----------------------|
| NO_OPERATION | Used by the binding during initialization |  |
| BASIC | Provides basic SET and GET of the default node value |  |
| HAIL | Used by nodes to indicate that they want to be polled. The binding handles this automatically |  |
| METER | Used to get measurements from a node | **meter_scale=value** :  optional parameter to select the meter scale in case the meter supports multiple scales (and types). Value is one of the following **textual** values:<br/>E_KWh (0, MeterType.ELECTRIC, "kWh", "Energy") <br/>E_KVAh (1, MeterType.ELECTRIC, "kVAh", "Energy")<br/>E_W(2, MeterType.ELECTRIC, "W", "Power")<br/>E_Pulses (3, MeterType.ELECTRIC, "Pulses", "Count")<br/>E_V (4, MeterType.ELECTRIC, "V", "Voltage")<br/>E_A (5, MeterType.ELECTRIC, "A", "Current")<br/>E_Power_Factor (6, MeterType.ELECTRIC, "Power Factor", "Power Factor")<br/>G_Cubic_Meters (0, MeterType.GAS, "Cubic Meters", "Volume")<br/>G_Cubic_Feet (1, MeterType.GAS, "Cubic Feet", "Volume")<br/> G_Pulses(3, MeterType.GAS, "Pulses", "Count")<br/>W_Cubic_Meters (0, MeterType.WATER, "Cubic Meters", "Volume")<br/>W_Cubic_Feet (1, MeterType.WATER, "Cubic Feet", "Volume")<br/>W_Gallons (2, MeterType.WATER, "US gallons", "Volume")<br/>W_Pulses (3, MeterType.WATER, "Pulses", "Count") | 
| METER_RESET | Used to reset a meter back to 0 | To reset a meter use the "meter_reset=true" attribute on the meter.    When moving the switch from off to on it will reset the meter.<br/>e.g.:<br/>`Switch sReset { zwave="8:command=meter, meter_reset=true"}` |
| SWITCH_BINARY | Used to bind directly to a SWITCH |  |
| SWITCH_MULTILEVEL | Used to bind directly to a DIMMER | restore_last_value=true : restores the dimmer to its last value if an ON command is sent to the dimmer (as opposed to setting its value to 100%) |
| SENSOR_BINARY | Used to bind to a sensor. | **sensor_type=value** : optional parameter to select a sensor in case the node supports multiple sensors. Value is one of the following **numerical** values:<br/>  1 = General Purpose<br/>  2 = Smoke<br/>  3 = Carbon Monoxide<br/>  4 = Carbon Dioxide<br/> 5 = Heat<br/> 6 = Water<br/>  7 = Freeze<br/> 8 = Tamper<br/> 9 = Aux<br/>  10 = Door/Window<br/> 11 = Tilt<br/>  12 = Motion<br/>  13 = Glass Break |
| SENSOR_MULTILEVEL | Used to bind to e.g. a temperature sensor. Currently only single sensors are supported. | **sensor_type=value** : optional parameter to select a sensor in case the node supports multiple sensors. Value is one of the following **numerical** values:<br/>1 = Temperature<br/>2 = General<br/>3 = Luminance<br/>4 = Power<br/>5 = RelativeHumidity<br/>6 = Velocity<br/>7 = Direction<br/>8 = AtmosphericPressure<br/>9 = BarometricPressure<br/>10 = SolarRadiation<br/>11 = DewPoint<br/>12 = RainRate<br/>13 = TideLevel<br/>14 = Weight<br/>15 = Voltage<br/>16 = Current<br/>17 = CO2<br/>18 = AirFlow<br/>19 = TankCapacity<br/>20 = Distance<br/>21 = AnglePosition<br/>22 =Rotation<br/>23 = WaterTemperature<br/>24 = SoilTemperature<br/>25 = SeismicIntensity<br/>26 = SeismicMagnitude<br/>27 = Ultraviolet<br/>28 = ElectricalResistivity<br/>29 = ElectricalConductivity<br/>30 = Loudness<br/>31 = Moisture<br/>32 = MaxType |
| MULTI_INSTANCE | Used to channel commands to the right endpoint on multi-channel devices. See item configuration. |  |
| MANUFACTURER_SPECIFIC | Used to get manufacturer info from the device |  |
| BATTERY | Used to get the battery level from battery operated devices. See item configuration. |  |
| WAKE_UP | Used to respond to wake-up signals of battery operated devices. |  |
| VERSION | Used to get version info from a node. |  |
| SENSOR_ALARM | Used to get alarm info from sensors. | *alarm_type=value* : optional parameter to select an alarm type in case the node supports multiple alarms. Value is one of the following **numerical** values: <br/>GENERAL(0, "General")<br/>SMOKE(1, "Smoke")<br/>CARBON_MONOXIDE(2, "Carbon Monoxide")<br/>CARBON_DIOXIDE(3, "Carbon Dioxide")<br/>HEAT(4, "Heat")<br/>FLOOD(5, "Flood") |
| SCENE_ACTIVATION | Used to respond to Scene events. | scene=xx to identify the scene to trigger on <br/> state=xx to set the item to the specified state (xx is an integer) |
| ALARM |  |  |
| MULTI_CMD | Used to send multiple command classes in a single packet. |  |
| THERMOSTAT_MODE | Used to get and set the mode (of Number type) of the thermostat |  numeric values translate to the following types <br/>0 = "Off"<br/>    1 = "Heat"<br/>   2 = "Cool"<br/>   3 = "Auto"<br/>   4 = "Aux Heat"<br/>   5 = "Resume"<br/>   6 = "Fan Only"<br/>   7 = "Furnace"<br/>    8 = "Dry Air"<br/>    9 = "Moist Air"<br/>    10 = "Auto Changeover"<br/>   11 = "Heat Econ"<br/>   12 = "Cool Econ"<br/>   13 = "Away"   |
| THERMOSTAT_OPERATING_STATE | Used to get the operating state (of Number type) of the thermostat |  numeric values translate to the following types <br/>    0 = "Idle"<br/>    1 = "Heating"<br/>    2 = "Cooling"<br/>    3 = "Fan Only"<br/>   4 = "Pending Heat"<br/>   5 = "Pending Cool"<br/>   6 = "Vent / Economizer"<br/>   |
| THERMOSTAT_SETPOINT | Used to get and set the setpoint of the thermostat | **setpoint_type=value** : parameter to select setpoint type, value is one of the following numerical values:<br/>               1 = Heat <br/>               2 = Cool <br/> *setpoint_scale=value** : parameter to select setpoint scale, value is one of the following numerical values:<br/>               0 = Celsius <br/>              1 = Fahrenheit <br/> |
| THERMOSTAT_FAN_MODE | (since 1.6.0) Used to get the fan mode (of Number type) of the thermostat |  numeric values translate to the following types <br/>                0 = "Auto Low"<br/>   1 = "On Low"<br/>   2 = "Auto High"<br/>    3 = "On High"<br/>    4 = "Unknown"<br/>    5 = "Unknown"<br/>    6 = "Circulate"<br/>   |
| THERMOSTAT_FAN_STATE | (since 1.6.0) Used to get the fan state (of Number type) of the thermostat |  numeric values translate to the following types <br/>                0 = "Idle"<br/>   1 = "Running"<br/>    2 = "Running High"<br/>  |
| CONFIGURATION | Used to set configuration parameters. Normally, this is done through HABmin as most configuration is static, but some devices have parameters that need to be changed via a rule or sitemap.  | Use the "parameter=" option in the binding string to set the parameter number linked to this item. |
| INFO | This is not a 'real' zwave command class, but can be used to get information from the binding about a node and its state. | Controller only:<br/>HOME_ID<br/>SOF<br/>CAN<br/>NAK<br/>OOF<br/>ACK<br/>TIME_OUT<br/>TX_QUEUE<br/><br/>All Nodes:<br/>NODE_ID<br/>LISTENING<br/>DEAD<br/>ROUTING<br/>VERSION<br/>BASIC<br/>BASIC_LABEL<br/>GENERIC<br/>GENERIC_LABEL<br/>SPECIFIC<br/>SPECIFIC_LABEL<br/>MANUFACTURER<br/>DEVICE_ID<br/>DEVICE_TYPE<br/>LAST_UPDATE<br/> |
| INDICATOR | Show the state, or level of a device, usually through a button LED, or display on the actual device | If you use the bit=n (n between 0 and 8) parameter then you can bind Switch Items to individual bits in the indicator value which can be used to turn on and off status LEDs on device buttons for instance. |

### Parameters that can be added to any item

There are some general parameters that can be added to any command class in an item string. These are `refresh_interval=value` and `respond_to_basic=true`

`refresh_interval=<value>` sets the refresh interval to `<value>` seconds. `0` indicates that no polling is performed and the node should inform the binding itself on value changes. This is the default value.

`respond_to_basic=true` indicates that the item will respond to basic reports. Some Fibaro contacts and universal sensors report their values as BASIC reports instead of a specific command class. You can add this parameter to an item to indicate that this item should respond to those reports.

`meter_zero=xx.x` can be set when using the Meter command class. If set, anything below the value specified will be considered as 0. This allows the user to account for small power consumption readings even when a device is off.

`sensor_scale=X` can be set for the multilevel sensor class to force the sensor to be converted to a specific scale. This uses the scale information provided by the device to decide how, or if, the conversion needs to be applied. Currently this is only available for temperature sensors. eg. `sensor_scale=0` will ensure that a temperature sensor is always shown in celsius, while `sensor_scale=1` will ensure fahrenheit.

`invert_state=true` can be used to invert the state of a multilevel switch command class. eg this can be used to reverse the direction of a rollershutter.

### Basic command class

The basic Command Class is a special command class that almost every node implements. It provides functionality to set a value on the device and/or to read back values. It can be used to address devices that are currently not supported by their native command class like thermostats.

When the basic command class is used, devices support setting values and reporting values when polling. Direct updates from the device on changes will fail however.

You can force a device to work with the basic command set (or any specific command set for that matter using a syntax like:

```
Switch    ZwaveDevice        { zwave="3:1:command=BASIC" }
```

To find out which command classes are supported by your Z-Wave device, you can look in the manual or use the list at http://www.pepper1.net/zwavedb/ or http://products.z-wavealliance.org/. In case your command class is supported by the device and binding, but you have a problem, you can create an issue at: https://github.com/openhab/openhab/issues. In case you want a command class implemented by the binding, please create an issue.

## Battery Devices and Wakeup

One of the questions that gets asked a lot is _"my battery device is sending information, so it's clearly awake, but I can't change parameters"_. ZWave battery devices only 'wake up' periodically however they will send their data whenever THEY want. So if the door opens, then a sensor will send its data immediately (otherwise it would be of no use!) and for temperature sensors, or multi sensors, there's normally a configuration that allows you to configure how often the sensor will send the data, or how much change there needs to be before it will send an update (or a combination of the two).

However, wakeup is different. In the above scenarios, the device is not 'awake' - it's just sending notifications. When a device wakes up, it sends a special message to the binding to say "I'm awake, do you have anything to send me". We can then send data to the device (configuration setting, parameter requests etc), and the device can respond. When we have no more messages to send to the device, we tell it to go back to sleep (this currently happens 1 second after we send the last message - just to give the system time to respond to any rules etc before the device goes to sleep again).

A device will wake up based on the information in the wakeup command class, which is available in HABmin. If wakeup isn't set correctly, then we can't command the device, even though we may receive sensor data etc. The binding will attempt to configure this automatically when the device/binding initialises - it will make sure that the node is set to reference the binding, but in general it won't touch the time. The exception to this is where the time is set to 0, it will set it to 1 hour.

If wakeup hasn't been set, then we will never be able to send commands to the device to initialise it, or change parameters. In this case, you need to wake the device up manually. Normally, this is achieved by pressing a button on the device (maybe 3 times). This causes the device to send what's called a NIF (Node Information Frame) - this is sent as a broadcast though so it's not routed. This means that the device MUST be able to communicate directly to the controller, so it needs to be close by.

The last wakeup time is shown in HABmin in the wakeup area for a battery device.

One last point on the wakeup configuration node. There is (currently) no way to set the target node - the binding will automatically set it to its own ID when the interval is changed. Some people have used a value of 255 for the target node, or it may come as the default value - this means that the wakeup is broadcast to anyone who wants to listen. This might seem a good idea as multiple controllers can receive the message however, broadcast messages do not get routed, so this only works if the controller is in direct contact with the device which is often not the case.


## Database

**Note**: A new online database editor is being produced. This should make it easier for most people to add and update devices within the database, and will improve support for both the OH1 and OH2 bindings. Please support this [here](http://www.cd-jackson.com/index.php/zwave/zwave-device-database/zwave-device-database-guide).

The binding uses a database of devices so that it can work around any quirks, or present information about association groups and configuration data. The format for the database is [here](https://github.com/cdjackson/HABmin2/wiki/Z-Wave-Product-Database).

If you are not able to produce the XML file yourself, then please open an issue. The following information is required -:
* Type and ID for the device - HABmin will print this information when a device is not in the database
* Manufacturer
* Reference to the device manual (ie link to PDF)
* Link to device in pepper1 database (if it exists). [http://www.pepper1.net/zwavedb/](http://www.pepper1.net/zwavedb/)

## Logging

The loggers used by the binding, which you can set to `DEBUG` or `TRACE` (more verbose than `DEBUG`) are:

* `org.openhab.binding.zwave`

It is highly recommended to turn on at least DEBUG logging whilst setting up and configuring your ZWave network for the first time. 

## Examples

Here are some examples of valid Z-Wave binding configuration strings, as defined in your items file:

### Generic

#### Z-Wave statistics
    
    Number ZwaveStatsSOF "Number Start of Frames[%s]" (gZwaveStats) {zwave="1:command=info,item=sof"}
    Number ZwaveStatsACK "Number of Acknowledgments [%s]" (gZwaveStats) {zwave="1:command=info,item=ack"}
    Number ZwaveStatsCAN "Number of CAN [%s]" (gZwaveStats) {zwave="1:command=info,item=can"}
    Number ZwaveStatsNAK "Number of NAK [%s]" (gZwaveStats) {zwave="1:command=info,item=nak"}
    Number ZwaveStatsOOF "Number of OOF [%s]" (gZwaveStats) {zwave="1:command=info,item=oof"}
    Number ZwaveStatsTimeout "Number of Time-outs [%s]" (gZwaveStats) {zwave="1:command=info,item=time_out"}
    String ZwaveNode01HomeID  "Home ID [%s]" (gZwaveNode01) {zwave="1:command=info,item=home_id"}
    Number ZwaveNode01NetworkID "Node ID [%s]" (gZwaveNode01) {zwave="1:command=info,item=node_id"}
    
#### Dimmer and a Contact
    
    Dimmer Light_Corridor_Dimmer "Hallway Dimmer [%d %%]" (GF_Corridor) {zwave="6"}
    Contact Door_Corridor_Switch "Front door sensor [MAP(nl.map):%s]" (GF_Corridor) {zwave="21:command=sensor_binary,respond_to_basic=true"} 
    Number Door_Corridor_Battery "Front door sensor battery level [%d %%]" (GF_Corridor) { zwave="21:command=battery" }
    
#### Node with multiple endpoints
    
    Switch Mech_Vent      "Mechanical ventilation middle."  (GF_Kitchen) {zwave="11:1"}
    Switch Mech_Vent_High   "Mechanical ventilation high."  (GF_Kitchen) {zwave="11:2"}

### Lighting and Bulbs

#### Aeotec Micro Smart Switch (2nd Gen) (MSS2E)

    Switch     Bathroom_Switch       "Bathroom Switch"     <switch>        { zwave="4:command=switch_binary,respond_to_basic=true" }
    Number     Bathroom_Switch_Power "Bathroom Switch Power [%.1f W]"               { zwave="4:command=meter,meter_scale=E_W" }


#### Domitech ZBULB  

    Switch  Light_Landing   "Landing Light" <whites>    (FF_Hall,Lights)   {zwave="37:respond_to_basic=true"}
    Dimmer  Light_LandingBrightness   "Landing Brightness" <whites>    (FF_Hall,Lights)   {zwave="37:command=switch_multilevel"}

#### Everspring Wireless Dimmer ADA1311  

    Dimmer Light { zwave="6:command=SWITCH_MULTILEVEL,respond_to_basic=true" }

#### Fibaro Relay 1x2.5kW

    Switch      Fibaro_Relay    "Fibaro Relay"          <switch>                { zwave="2" }

#### Fibaro RGBW Controller  

    Group gWohnzimmer   "Wohnzimmer"      <sofa>  (gAlles)
    Group gwzRGBW     "TV Rücklicht Erweitert"  <sofa>  (gWohnzimmer)

    Color   wzRGBW    "TV Rücklicht"    <slider> (gwzRGBW)
    Dimmer  wzRGBW_All  "Helligkeit [%d %%]"  <switch> (gwzRGBW)  {zwave="2"}
    Dimmer  wzRGBW_R  "Rot [%d %%]"     <switch> (gwzRGBW)  {zwave="2:2:command=switch_multilevel"}
    Dimmer  wzRGBW_G  "Grün [%d %%]"    <switch> (gwzRGBW)  {zwave="2:3:command=switch_multilevel"}
    Dimmer  wzRGBW_B  "Blau [%d %%]"    <switch> (gwzRGBW)  {zwave="2:4:command=switch_multilevel"}
    Dimmer  wzRGBW_W  "Weiß [%d %%]"    <switch> (gwzRGBW)  {zwave="2:5:command=switch_multilevel"}
    
    Switch  wzRGBW_Switch "Schalter Alle"   <switch>  (gwzRGBW) { zwave="2:1"}
    Switch  wzRGBW_R_Switch "Schalter Rot"    <switch>  (gwzRGBW) { zwave="2:2"}
    Switch  wzRGBW_G_Switch "Schalter Grün"   <switch>  (gwzRGBW) { zwave="2:3"}
    Switch  wzRGBW_B_Switch "Schalter Blau"   <switch>  (gwzRGBW) { zwave="2:4"}
    Switch  wzRGBW_W_Switch "Schalter Weiß"         <switch>  (gwzRGBW) { zwave="2:5"}

    Number wzRGBW_Power     "Stromverbrauch [%.1f W]"     <energy>  (gwzRGBW) { zwave="2:command=sensor_multilevel"}
    Number wzRGBW_Energy    "Gesamtverbrauch [%.2f KWh]"    <energy>  (gwzRGBW) { zwave="2:command=meter", refresh_interval=60}

#### Fibaro Universal Dimmer (FGD-211)

    Switch  swLight_HallCeiling      "Hall: Ceiling"          { zwave="9:command=SWITCH_MULTILEVEL" }
    Dimmer  diLight_HallCeiling      "Hall: Ceiling [%d %%]"  { zwave="9:command=SWITCH_MULTILEVEL" }
    Switch  swScene1_HallCeiling     "Hall-Simpleclick"       { zwave="9:command=SCENE_ACTIVATION,scene=26,state=0" }
    Switch  swScene2_HallCeiling     "Hall-Doubleclick"       { zwave="9:command=SCENE_ACTIVATION,scene=24,state=0" }
    Switch  swScene3_HallCeiling     "Hall-Tripleclick"       { zwave="9:command=SCENE_ACTIVATION,scene=25,state=0" }

#### Fibaro Universal Dimmer 2 (FGD-212)

    Switch      foo             "Foo"                                           { zwave="42:command=SWITCH_MULTILEVEL" }
    Dimmer      foo_dim         "Foo [%d %%]"                                   { zwave="42:command=SWITCH_MULTILEVEL" }
    Number      foo_power       "Foo - current power consumption [%.2f W]"      { zwave="42:command=METER,meter_scale=E_W,refresh_interval=300" }
    Number      foo_energy      "Foo - total energy use [%.2f KWh]"             { zwave="42:command=METER,meter_scale=E_KWh,refresh_interval=300" }

#### Lineartec LB60Z-1

    Dimmer myLight "Light" { zwave="2:command=SWITCH_MULTILEVEL" }

#### Qubino Flush Dimmer Plus (ZMNHDD)

    Dimmer     Qubino_Dimmer     "Qubino Dimmer [%d %%]"                        { zwave="3:command=switch_multilevel" }
    Number     Qubino_Power      "Qubino Power [%.2f W]"                        { zwave="3:command=meter,meter_scale=E_W" }
    Number     Qubino_Energy     "Qubino Energy [%.2f kWh]"                     { zwave="3:command=meter,meter_scale=E_KWh,refresh_interval=900" }   

#### Qubino Flush 1 Relay (ZMNHAA)

    Switch     Qubino_Switch     "Qubino Switch"     <switch>                   { zwave="2:command=switch_binary" }
    Number     Qubino_Power      "Qubino Power [%.2f W]"                        { zwave="2:command=meter,meter_scale=E_W" }
    Number     Qubino_Energy     "Qubino Energy [%.2f kWh]"                     { zwave="2:command=meter,meter_scale=E_KWh" }


### Remote Controls

#### Nodon CRC 3100 (Octan Remote) and CRC3605 (SoftRemote)

1. Change the configuration parameter 3 to 1.
1. Create the items as shown below:

```
    Switch  Nodon_Button1_Single  "Nodon_Button1_Single"  <switch>  { zwave="8:command=SCENE_ACTIVATION,scene=10,state=1" }
    Switch  Nodon_Button2_Single  "Nodon_Button2_Single"  <switch>  { zwave="8:command=SCENE_ACTIVATION,scene=20,state=1" }
    Switch  Nodon_Button3_Single  "Nodon_Button3_Single"  <switch>  { zwave="8:command=SCENE_ACTIVATION,scene=30,state=1" }
    Switch  Nodon_Button4_Single  "Nodon_Button4_Single"  <switch>  { zwave="8:command=SCENE_ACTIVATION,scene=40,state=1" }

    Switch  Nodon_Button1_Long      "Nodon_Button1_Long"  <switch>          { zwave="8:command=SCENE_ACTIVATION,scene=12,state=1" }
    Switch  Nodon_Button2_Long      "Nodon_Button2_Long"  <switch>          { zwave="8:command=SCENE_ACTIVATION,scene=22,state=1" }
    Switch  Nodon_Button3_Long      "Nodon_Button3_Long"  <switch>          { zwave="8:command=SCENE_ACTIVATION,scene=32,state=1" }
    Switch  Nodon_Button4_Long      "Nodon_Button4_Long"  <switch>          { zwave="8:command=SCENE_ACTIVATION,scene=42,state=1" }

    Switch  Nodon_Button1_Release   "Nodon_Button1_Release" <switch>        { zwave="8:command=SCENE_ACTIVATION,scene=11,state=1" }
    Switch  Nodon_Button2_Release   "Nodon_Button2_Release" <switch>        { zwave="8:command=SCENE_ACTIVATION,scene=21,state=1" }
    Switch  Nodon_Button3_Release   "Nodon_Button3_Release" <switch>        { zwave="8:command=SCENE_ACTIVATION,scene=31,state=1" }
    Switch  Nodon_Button4_Release   "Nodon_Button4_Release" <switch>        { zwave="8:command=SCENE_ACTIVATION,scene=41,state=1" }

    Switch  Nodon_Button1_Double    "Nodon_Button1_Double" <switch>         { zwave="8:command=SCENE_ACTIVATION,scene=13,state=1" }
    Switch  Nodon_Button2_Double    "Nodon_Button2_Double" <switch>         { zwave="8:command=SCENE_ACTIVATION,scene=23,state=1" }
    Switch  Nodon_Button3_Double    "Nodon_Button3_Double" <switch>         { zwave="8:command=SCENE_ACTIVATION,scene=33,state=1" }
    Switch  Nodon_Button4_Double    "Nodon_Button4_Double" <switch>         { zwave="8:command=SCENE_ACTIVATION,scene=43,state=1" }
```

### Sensors

#### Aeotec Door/Window Sensor (2nd Edition) Model: DSB29-ZWUS  

    Contact     zwave_contact_16_sensor "office - test door"                                             (doors,monitor)  {zwave="16:command=basic,respond_to_basic=true"}
    Contact     zwave_contact_16_tamper "office - test door tamper"                                      (doors,tamper)   {zwave="16:command=ALARM"}
    Number      FrontDoorBattery        "office - test door battery [%d %%]"              <battery>      (doors,battery)  {zwave="16:command=BATTERY"}

#### Aeotec hidden door sensor (gen 5, DSB54)
  
    Contact garagewalkin_1_sensor    "garage walk-in [%s]"                                         (ALL,ff,sensor)              {zwave="3:command=BASIC"}
    Number  garagewalkin_1_battery   "garage walk-in battery [%d %%]"              <battery>       (ALL,battery)                {zwave="3:command=BATTERY"}

#### Aeotec Multi Sensor 4  

    Number      Multi1_temp                "office - Multi Temperature [%.1f °F]"                  (multi,multiGraph) {zwave="10:command=SENSOR_MULTILEVEL,sensor_type=1"}
    Number      Multi1_humidity            "office - Multi Humidity    [%.0f %%]"                  (multi,multiGraph) {zwave="10:command=SENSOR_MULTILEVEL,sensor_type=5"}
    Number      Multi1_luminance           "office - Multi Luminance    [%.0f Lux]"                (multi)            {zwave="10:command=SENSOR_MULTILEVEL,sensor_type=3"}
    Contact     Multi1_motion              "office - Multi motion [MAP(motion.map):%s]" <motion>   (multi,motion)     {zwave="10:command=SENSOR_BINARY,respond_to_basic=true"}
    Number      Multi1_battery             "office - Multi battery [%d %%]"             <battery>  (multi,battery)    {zwave="10:command=BATTERY"}

#### Aeotec Multi Sensor 6  

    Contact Motion_GFToilet "Motion [MAP(motion.map):%s]" (GF_Toilet) { zwave="8:command=sensor_binary,respond_to_basic=true" }
    Number Alarm_GFToilet "Alarm: [%s]" (GF_Toilet) { zwave="8:command=alarm" }
    Number Temp_GFToilet "Temperature: [%.1f °C]" (GF_Toilet) { zwave="8:command=sensor_multilevel,sensor_type=1,sensor_scale=0" }
    Number Humid_GFToilet "Humidity: [%.0f %%]" (GF_Toilet) { zwave="8:command=sensor_multilevel,sensor_type=5" }
    Number Lumin_GFToilet "Luminance: [%.0f Lux]" (GF_Toilet) { zwave="8:command=sensor_multilevel,sensor_type=3" }
    Number UV_GFToilet "Luminance: [UV index %d]" (GF_Toilet) { zwave="8:command=sensor_multilevel,sensor_type=27" }
    Number Battery_GFToilet "Battery: [%d %%]" (GF_Toilet) { zwave="8:command=battery" }
    DateTime LastUpdated_GFToilet "Last update [%1$tH:%1$tM]" (GF_Toilet) { zwave="8:command=info,item=LAST_UPDATE" }

#### D-Link DCH-Z110  

    Contact C "Movement [%s]" (gf)  { zwave="10:command=SENSOR_BINARY,respond_to_basic=TRUE" }
    Number  T "Temperature [%.1f °C]" (gf)  { zwave="10:command=sensor_multilevel,sensor_type=1" }
    Number  H "Luminance [%.0f Lux]"  (gf)  { zwave="10:command=sensor_multilevel,sensor_type=3" }
    DateTime    L "Last update [%1$tH:%1$tM]" (gf)  { zwave="10:command=info,item=LAST_UPDATE" }

#### Ecolink tiltzwave1 garage door sensor:  

    Group DoorsWindows "Doors and Windows"
    Contact GarageDoor "Garage Door is [MAP(en.map):%s]" (DoorsWindows){zwave="3:command=SENSOR_BINARY"}

#### Ecolink PIR motion detector  

    Contact EntryMotion              "entry motion [MAP(motion.map):%s]"                           (ALL,motion,ff)              {zwave="4:command=SENSOR_BINARY"}
    Contact EntryMotionTamper        "entry motion tamper [MAP(tamper.map):%s]"    <battery>       (ALL,tamper)                 {zwave="4:command=ALARM"}
    Number  EntryMotionBattery       "entry motion battery [%d %%]"                <battery>       (ALL,battery)                {zwave="4:command=BATTERY"}

#### Everspring Flood Detector model:ST812-2  

    Contact     zwave_water_9_sensor       "office - water sensor 2"                    <water>    (water,monitor)    {zwave="9:command=SENSOR_ALARM"}
    Number      Water_sensor_battery       "office - water sensor 2 battery [%d %%]"    <battery>  (water,battery)    {zwave="9:command=BATTERY"}

#### Everspring ST814 temperature and humidity sensor  
   
    Number T_AZH  "Temperature [%.1f °C]" (AZH) {zwave="32:command=sensor_multilevel,sensor_type=1" }
    Number RH_AZH "Humidity    [%.0f %%]" (AZH) { zwave="32:command=sensor_multilevel,sensor_type=5" }
    Number BAT_AZH "Battery    [%.0f %%]" (AZH) { zwave="32:command=battery" }

#### Fortrez water/temperature sensor  

    Number WaterTemp3 "Water Temp 3" (water,temp) {zwave="18:command=SENSOR_MULTILEVEL,sensor_type=1"}
    Contact Water3 "water sensor 3" (water) {zwave="18"}
    Number Water3_battery "Water 3 battery [%s %%]" (water) {zwave="18:command=BATTERY"}

#### Fibaro Door-Contact (FGK-101)  

    Contact   coStatus_DoorBell     "Doorbell: [MAP(bell.map):%s]"   { zwave="10:command=SENSOR_BINARY,respond_to_basic=TRUE" }
    Number    nuBattery_DoorBell   "Doorbell: Battery [%s %%]"      { zwave="10:command=BATTERY" }

#### Fibaro FGK-101 door sensor (requires DS18B20 to be added):  

    Number  Temp_UtilityRoom "Utility room temperature [%.1f °C]" { zwave="7:2:command=SENSOR_MULTILEVEL" }

#### Fibaro Flood-Sensor (FGFS-101)

    Contact coFibFlood_Alarm    "Water-Sensor: [MAP(water.map):%s]"   { zwave="11:command=SENSOR_ALARM, alarm_type=5,respond_to_basic=TRUE" }
    Number nuFibFlood_Battery   "Water-Sensor: Batterie [%s %%]"      { zwave="11:command=BATTERY" }
    Number nuFibFlood_Temp      "Water-Sensor: Temperatur [%.1f Â°C]" { zwave="11:2:command=sensor_multilevel" }
    Switch swFibFlood_Tamper    "Water-Sensor: Tamper"                { zwave="11:command=sensor_alarm, alarm_type=0,respond_to_basic=true" }

#### Fibaro Multisensor (FIB_FGMS-001)

    Number  Movement         "Movement: [%s]"          <present>      { zwave="4:command=sensor_binary" }
    Number  Alarm            "Alarm: [%s]"             <fire>         { zwave="4:command=sensor_alarm" }
    Number  Lux              "Lux: [%.2f Lux]"         <sun>          { zwave="4:command=sensor_multilevel,sensor_type=3" }
    Number  Bat              "Battery: [%d %%]"        <energy>       { zwave="4:command=battery" }
    Number  Temp             "Temperature: [%.1f °C]"  <temperature>  { zwave="4:command=sensor_multilevel,sensor_type=1" }


#### Fibaro Universal Sensor  

    Contact Z_deurbel0  "Z_deurbel_sensor"  { zwave="8:0:command=SENSOR_BINARY,respond_to_basic=TRUE" }
    Contact Z_deurbel1  "Z_deurbel_input1"  { zwave="8:1:command=SENSOR_BINARY,respond_to_basic=TRUE" }
    Contact Z_deurbel2  "deurbel" { zwave="8:2:command=SENSOR_BINARY,respond_to_basic=TRUE" }

#### Fibaro Universal Sensor with attached DS18B20 temperature sensors  

    Number Sensor_Temp_1 "Temp1 [%.2f °C]" { zwave="17:3:command=sensor_multilevel" }
    Number Sensor_Temp_2 "Temp2 [%.2f °C]" { zwave="17:4:command=sensor_multilevel" }
    Number Sensor_Temp_3 "Temp3 [%.2f °C]" { zwave="17:5:command=sensor_multilevel" }

#### Monoprice motion detector (ZP3102)  

    Contact OfficeMotion            "office motion [MAP(motion.map):%s]"                               (ALL,motion,ff)              {zwave="2:command=BASIC"}
    Number  OfficeMotionBattery     "office motion battery [%d %%]"                                    (ALL,battery)                {zwave="2:command=BATTERY"}
    Contact OfficeMotionTamper      "office motion tamper [MAP(tamper.map):%s]"                        (ALL,tamper)                 {zwave="2:command=ALARM"}
    Number  OfficeMotionTemp        "office temp [%.1f °F]"                            <temperature>   (ALL,temperature,ff)         {zwave="2:command=sensor_multilevel,sensor_type=1,sensor_scale=1" }

#### Philio 4in1 Multisensor (PST-02)

    Contact    KitchenMotion     "Kitchen Motion [%s]"     (motion)     { zwave="2:command=sensor_binary,sensor_type=12" }
    Contact    KitchenDoor       "Kitchen Door [%s]"       (door)       { zwave="2:command=sensor_binary,sensor_type=10" }
    Number     KitchenTemp       "Kitchen Temp [%.1f]"     (temp)       { zwave="2:command=sensor_multilevel,sensor_type=1,sensor_scale=0" }
    Number     KitchenBrightness "Kitchen Brightness [%d]" (brightness) { zwave="2:command=sensor_multilevel,sensor_type=3" }
    Contact    KitchenTamper     "Kitchen Tamper [%s]"     (alarm)      { zwave="2:command=alarm" }
    Number     KitchenBattery    "Kitchen Battery [%d]"    (battery)    { zwave="2:command=battery" }

#### Vision Security Door/Window Sensor

    Contact Contact_BackDoor  "Back Door [MAP(motion.map):%s]"  <frontdoor> (GF_Kitchen)  {zwave="5:command=basic,respond_to_basic=true"}
    Number  Battery_BackDoor  "Back Door Battery: [%d %%]"  <battery> (GF_Kitchen,Battery)  {zwave="5:command=battery"}

#### Zooz 4-in-1 Multisensor (ZSE40)

```
Contact UPSTAIRS_HALLWAY_PIR_TAMPER "Upstairs Hallway Tamper [MAP(zwave_motion.map):%s]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=ALARM"}
Number UPSTAIRS_HALLWAY_PIR_BATTERY "Upstairs Hallway Battery [%d %%]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=BATTERY"}
Number UPSTAIRS_HALLWAY_PIR_TEMPERATURE "Upstairs Hallway Temperature [%.2f F]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=sensor_multilevel,sensor_type=1,sensor_scale=1"}
Number UPSTAIRS_HALLWAY_PIR_LIGHT "Upstairs Hallway Light [%.1f %%]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=sensor_multilevel,sensor_type=3"}
Number UPSTAIRS_HALLWAY_PIR_REL_HUMID "Upstairs Hallway RH [%.0f %%]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=sensor_multilevel,sensor_type=5"}
Contact UPSTAIRS_HALLWAY_PIR_MOTION "Upstairs Hallway Motion [MAP(zwave_motion.map):%s]" (Group_Motion, Group_Persistence, Group_Upstairs) {zwave="58:command=BASIC"}
```

zwave_motion.map

```
CLOSED=No Motion
OPEN=MOTION
-=(No value yet)
```

zwave_tamper.map

```
CLOSED=No Tamper
OPEN=TAMPER
-=(No value yet)
```


###  Shutters

#### Fibaro Rollershutter FGRM-222 (v1 and v2 HW revisions)

    Rollershutter kdSHUTTER   "Roller shutter [%d %%]"        (gkdSHUTTER)  {zwave="3:command=switch_multilevel,invert_state=false,invert_percent=true"}
    Rollershutter VenetianSHUTTERv1 "Venetian blind (blind position) [%d %%]"     (gkdSHUTTER)  {zwave="4:command=FIBARO_FGRM_222,type=shutter"}
    Rollershutter VenetianLAMELLAv1 "Venetian blind (lamella tilt) [%d %%]"     (gkdSHUTTER)  {zwave="4:command=FIBARO_FGRM_222,type=lamella"}
    Rollershutter VenetianSHUTTERv2 "Venetian blind (blind position) [%d %%]"     (gkdSHUTTER)  {zwave="4:command=MANUFACTURER_PROPRIETARY,type=shutter"}
    Rollershutter VenetianLAMELLAv2 "Venetian blind (lamella tilt) [%d %%]"     (gkdSHUTTER)  {zwave="4:command=MANUFACTURER_PROPRIETARY,type=lamella"}

    Number kdSHUTTER_Power    "current power usage [%.1f W]"  <energy>  (gkdSHUTTER)  { zwave="3:command=sensor_multilevel"}
    Number kdSHUTTER_Energy   "power consumption [%.2f KWh]"  <energy>  (gkdSHUTTER)  { zwave="3:command=meter" }

### Smoke Detectors

#### Monoprice Smoke detector (Vision Security ZS6101)  

    Contact iSmokeSensorMasterAlarm     "Master Status [%s]"    <fire>    (gSmokeSensorMaster)    { zwave="8:command=SENSOR_BINARY,respond_to_basic=true" }
    Number  iSmokeSensorMasterBattery   "Master Battery [%s]"   <battery> (gSmokeSensorMaster)    { zwave="8:command=BATTERY" }

#### Fibaro Smoke detector (FGSS101, FGSD002)  

    Contact Z_Kitchen_Smoke "Smoke detector is [%s]"  (Smoke_Alarm) {zwave="6:command=sensor_alarm,alarm_type=1" }
    Contact Z_Kitchen_Heat  "Heat detector is [%s]" (Smoke_Alarm) {zwave="6:command=sensor_alarm,alarm_type=4" }
    Contact Z_Kitchen_Tamper  "Smoke_sensor_K Tamper is[MAP(en.map):%s]"  (Tamper_Alarm)  { zwave="6:command=sensor_alarm,alarm_type=0" }
    Number  Z_Kitchen_Battery "Smoke_sensor_batt [%d %%]" (Battery_Levels)  {zwave="6:command=battery" }
    Number  Z_Kitchen_Temp  "Kitchen_temperature [%.1f°C]"  (Temperatures) {zwave="6:command=sensor_multilevel,sensor_type=1" }

### Switches

#### Fibaro Wall Plug (FGWPF-101 & FGWPF-102 & FGWPE)  

    Switch Wall_Plug           "Wall Plug"                             { zwave="3:command=switch_binary"} 
    Number Wall_Plug_Power     "Wall Plug - current energy [%.1f W]"   { zwave="3:command=sensor_multilevel"}
    Number Wall_Plug_Energy    "Wall Plug - total energy [%.2f KWh]"   { zwave="3:command=meter" }
 
#### General Electric GE12722

    Switch  bedroom_light "Bedroom Light" { zwave="8:command=SWITCH_BINARY" }

#### GreenWave PowerNode 6-port power strip
    
    Switch Switch_Powerbar_Subwoofer "Subwoofer" (GF_Living) {zwave="26:1:command=switch_binary"} 
    Switch Switch_Powerbar_Reiceiver "Receiver" (GF_Living) {zwave="26:2:command=switch_binary"} 
    Switch Switch_Powerbar_DVD "DVD" (GF_Living) {zwave="26:3:command=switch_binary"} 
    Switch Switch_Powerbar_TV "TV" (GF_Living) {zwave="26:4:command=switch_binary"} 
    Switch Switch_Powerbar_Xbox "XBOX-360" (GF_Living) {zwave="26:5:command=switch_binary"} 
    Switch Switch_Powerbar_PC "Mediacenter" (GF_Living) {zwave="26:6:command=switch_binary"} 
    
    Number Power_Powerbar_Subwoofer "Subwoofer power consumption  [%d W]" (GF_Living,GF_Energy) {zwave="26:1:command=meter,meter_scale=E_W,refresh_interval=60"} 
    Number Power_Powerbar_Reiceiver "Receiver power consumption  [%d W]" (GF_Living,GF_Energy) {zwave="26:2:command=meter,meter_scale=E_W,refresh_interval=70"} 
    Number Power_Powerbar_DVD "DVD power consumption [%d W]" (GF_Living,GF_Energy) {zwave="26:3:command=meter,meter_scale=E_W,refresh_interval=60"} 
    Number Power_Powerbar_TV "TV power consumption [%d W]" (GF_Living,GF_Energy) {zwave="26:4:command=meter,meter_scale=E_W,refresh_interval=70"} 
    Number Power_Powerbar_Xbox "XBOX-360 power consumption [%d W]" (GF_Living,GF_Energy) {zwave="26:5:command=meter,meter_scale=E_W,refresh_interval=80"} 
    Number Power_Powerbar_PC "Mediacenter power consumption [%d W]" (GF_Living,GF_Energy) {zwave="26:6:command=meter,meter_scale=E_W,refresh_interval=80"} 
    
    Number Energy_Powerbar_Subwoofer "Subwoofer total energy usage  [%.4f KWh]" (GF_Living) {zwave="26:1:command=meter,meter_scale=E_KWh,refresh_interval=300"} 
    Number Energy_Powerbar_Reiceiver "Receiver total energy usage  [%.4f KWh]" (GF_Living) {zwave="26:2:command=meter,meter_scale=E_KWh,refresh_interval=310"} 
    Number Energy_Powerbar_DVD "DVD totaal total energy usage  [%.4f KWh]" (GF_Living) {zwave="26:3:command=meter,meter_scale=E_KWh,refresh_interval=320"} 
    Number Energy_Powerbar_TV "TV total energy usage [%.4f KWh]" (GF_Living) {zwave="26:4:command=meter,meter_scale=E_KWh,refresh_interval=330"} 
    Number Energy_Powerbar_Xbox "XBOX-360 total energy usage  [%.4f KWh]" (GF_Living) {zwave="26:5:command=meter,meter_scale=E_KWh,refresh_interval=340"} 
    Number Energy_Powerbar_PC "Mediacenter total energy usage  [%.4f KWh]" (GF_Living) {zwave="26:6:command=meter,meter_scale=E_KWh,refresh_interval=350"} 

#### Aeotec Smart Switch 6 (ZW096)  

Note: you may need to add ",refresh_interval=*&lt;secs&gt;*" at the end to get more than a single reading at startup.

    Switch Smart6         "Smart6 outlet"                   (ff,ALL,outlet)  {zwave="6:command=switch_binary" }
    Number Smart6_Power   "Smart6 power  [%.2f W]"          (ALL,power)     {zwave="6:command=meter,meter_scale=E_W" }
    Number Smart6_Energy  "Smart6 consumption  [%.2f KWh]"  (ALL,power)     {zwave="6:command=meter,meter_scale=E_KWh" }
    Number Smart6_Volts   "Smart6 voltage [%.2f V]"         (ALL,power)      {zwave="6:command=meter,meter_scale=E_V"}
    Number Smart6_Amps    "Smart6 amperage [%.2f A]"        (ALL,power)      {zwave="6:command=meter,meter_scale=E_A"}

#### TKB Home TZ68E Wall switches

    Switch  WallSwitch_Hall "Hallway Wall switch" (GF_Hall,MyOpenHAB) {zwave="3"}

#### Z-wave.me double paddle wall switch  

    Switch WCD1_1_BUT1 "Test BUT 1" { zwave="7:command=SCENE_ACTIVATION,scene=11,state=1" }
    Switch WCD1_1_BUT3 "Test BUT 3" { zwave="7:command=SCENE_ACTIVATION,scene=12,state=0" }
    Switch WCD1_1_BUT2 "Test BUT 2" { zwave="7:command=SCENE_ACTIVATION,scene=21,state=1" }
    Switch WCD1_1_BUT4 "Test BUT 4" { zwave="7:command=SCENE_ACTIVATION,scene=22,state=0" }
    Switch WCD1_1_SW1 "Test WCD SW 1" (gTest) 
    Switch WCD1_1_SW2 "Test WCD SW 2" (gTest)

#### AspireRF RF9517

    Switch Remote_Button "button" { zwave="23:command=BASIC,respond_to_basic=true,refresh_interval=2" }

### Thermostats

#### Danfoss LC13 radiator thermostat:
    Number          bedroom_thermostat_setpoint     "Bedroom Thermostat Setpoint [%.2f C]"  { zwave="3:command=THERMOSTAT_SETPOINT" }
    Number          bedroom_thermostat_battery      "Bedroom Thermostat battery [%d %%]"    { zwave="3:command=BATTERY" }

#### Danfoss RS Room Sensor (014G0160 DRS21) &  Devolo MT:2649:

Items:

```
Number  EG_WoZi_Raumthermostat_Batteriestatus "EG WoZi Raumthermostat [%d %%]"  <battery> (Batt,gGraph)   { zwave="81:command=battery" }
Number  EG_WoZi_Raumthermostat_Temp "EG WoZi Raumthermostat Temperatur [%.1f Â°C]"  <temperature> (GaeZi,gGraph)    { zwave="81:command=SENSOR_MULTILEVEL" }
Number  EG_WoZi_Raumthermostat_Temp_Vorwahl "EG WoZi Raumthermostat Temperatur Vorwahl [%.1f Â°C]"  <temperature> { zwave="81:command=THERMOSTAT_SETPOINT" }
Number  EG_WoZi_Raumthermostat_Button "EG WoZi Raumthermostat Button" { zwave="81:command=central_scene" }
```

Rule:

```
rule "OG Gaestezimmer Thermostat Button"
when
  /** Item OG_GaesteZimmer_Raumthermostat_Button changed or **/
  Item OG_GaesteZimmer_Raumthermostat_Button received update
then
       /**the .state is every time "1" but the rule is triggered when button is pressed **/
       /** in the log you will get an error: "Protocoll Error (CAN), resending" **/
end
```

#### Eurotronic Stella Z thermostat:  

    Number Temp_Sensor_StellaZ_Bad "Badezimmer Temperatur: [%.1f C]" <temperature> (Heizung,Bad,Temperaturen) { zwave="28:command=sensor_multilevel,sensor_type=1" }
    Number Battery_Sensor_StellaZ_Bad "Badezimmer Batterie: [%d %%]" <energy> (Heizung,Batterien) { zwave="28:command=battery" }
    Number Temp_Setpoint_StellaZ_Bad " [%d]" <temperature> (Heizung,Heizung_Soll) { zwave="28:command=thermostat_setpoint, setpoint_type=1" }

#### Honeywell Thermostat with both heating and cooling and in Fahrenheit

    Number Down_HVAC_HeatSetPoint "Heat Set [%.0f F]" <thermostat>  (Group_HVAC_Downstairs) { zwave="7:command=thermostat_setpoint,setpoint_type=1,setpoint_scale=1" }
    Number Down_HVAC_CoolSetPoint "Cool Set [%.0f F]" <thermostat>  (Group_HVAC_Downstairs) { zwave="7:command=thermostat_setpoint,setpoint_type=2,setpoint_scale=1" }
    Number Down_HVAC_Temperature  "Temperature [%.1f °F]" <thermostat> (Group_HVAC_Downstairs) { zwave="7:command=sensor_multilevel,sensor_type=1" }
    Number Down_HVAC_Mode "Mode [%d]" (Group_HVAC_Downstairs) { zwave="7:command=thermostat_mode" }
    Number Down_HVAC_Fan_Mode "Fan Mode [%d]" (Group_HVAC_Downstairs) { zwave="7:command=thermostat_fan_mode" }
    Number Down_HVAC_Operating_State "Opp State [MAP(thermostatOpState.map):%d]" (Group_HVAC_Downstairs) { zwave="7:command=thermostat_operating_state" }
    Number Down_HVAC_Fan_State "Fan State [MAP(thermostatFanState.map):%d]" (Group_HVAC_Downstairs) { zwave="7:command=thermostat_fan_state" }

#### Heat-it thermostat  

    Number Temperature { zwave="4:0:command=SENSOR_MULTILEVEL,sensor_type=1" }
    Number Set_Temp { zwave="4:command=THERMOSTAT_SETPOINT,setpoint_type=1,setpoint_scale=0" }
    Number Mode {zwave="4:0:command=THERMOSTAT_MODE" }
    DateTime LastUpdated { zwave="4:command=info,item=LAST_UPDATE"}

#### Secure SRT321 thermostat
    Number Temperature_FF_Living "SRT Temperature [%.1f °C]" (ALL,FF_Living) <temperature> {zwave="4:command=SENSOR_MULTILEVEL,sensor_type=1"}
    Number SRT_Temperature_Setpoint "SRT Temperature set point" (ALL,FF_Living {zwave="4:command=THERMOSTAT_SETPOINT,setpoint_type=1,setpoint_scale=0"}
    Number SRT_Mode {zwave="4:0:command=THERMOSTAT_MODE"}
    Number SRT_Battery "Thermostat battery [%d %%]" (ALL,power,batteries,FF_Living) <battery> {zwave="4:0:command=BATTERY"}
    DateTime SRT_LastUpdated "SRT last updated" (ALL,temperatures,FF_Living) {zwave="4:command=INFO,item=LAST_UPDATE"}

#### CT100 Thermostat  

    Number HVAC_HeatSetPoint        "Heat Set [%.1f F]"                            <thermostat>    (ALL,HVAC)                   {zwave="5:command=thermostat_setpoint,setpoint_type=1,setpoint_scale=1" }
    Number HVAC_CoolSetPoint        "Cool Set [%.1f F]"                            <thermostat>    (ALL,HVAC)                   {zwave="5:command=thermostat_setpoint,setpoint_type=2,setpoint_scale=1" }
    Number HVAC_Temperature         "Thermostat temperature [%.1f °F]"             <temperature>   (ALL,HVAC,ff)                {zwave="5:1:command=sensor_multilevel,sensor_type=1,refresh_interval=60"}
    Number HVAC_Humidity            "Thermostat humidity [%.1f %%]"                <humidity>      (ALL,HVAC,ff)                {zwave="5:2:command=sensor_multilevel,sensor_type=5,refresh_interval=60"}
    Number HVAC_Mode                "Mode [MAP(thermostatMode.map):%s]"            <climate>       (ALL,HVAC)                   {zwave="5:command=thermostat_mode"}
    Number HVAC_Fan_Mode            "Fan Mode [MAP(thermostatFanMode.map):%s]"                     (ALL,HVAC)                   {zwave="5:command=thermostat_fan_mode"}
    Number HVAC_Operating_State     "Operation State [MAP(thermostatOpState.map):%s]"              (ALL,HVAC)                   {zwave="5:command=thermostat_operating_state,refresh_interval=60"}
    Number HVAC_Fan_State           "Fan State [MAP(thermostatFanState.map):%s]"                   (ALL,HVAC)                   {zwave="5:command=thermostat_fan_state"}
    Number HVAC_Battery             "Thermostat battery [%d %%]"                   <battery>       (ALL,HVAC,battery)           {zwave="5:command=BATTERY"}

#### CT100 with refreshing setpoints (so manual changes are captured) items: 
  
    Number HVAC_HeatSetPoint "Heat Set [%.0f F]" <temperature> (gHVAC) {zwave="7:command=thermostat_setpoint,setpoint_type=1,setpoint_scale=1,refresh_interval=20" }
    Number HVAC_CoolSetPoint "Cool Set [%.0f F]" <temperature> (gHVAC) {zwave="7:command=thermostat_setpoint,setpoint_type=2,setpoint_scale=1,refresh_interval=20" }
    Number HVAC_Temperature "Temperature [%.1f F]" <temperature> (gHVAC) {zwave="7:1:command=sensor_multilevel,sensor_type=1,refresh_interval=60" }
    Number HVAC_Humidity "Rel Humidity [%.1f %%]" <temperature> (gHVAC) {zwave="7:2:command=sensor_multilevel,sensor_type=5,refresh_interval=60" }
    Number HVAC_Mode "Mode [MAP(thermostatMode.map):%d]" (gHVAC) {zwave="7:command=thermostat_mode" }
    Number HVAC_Fan_Mode "Fan Mode [MAP(thermostatFanMode.map):%d]" (gHVAC) {zwave="7:command=thermostat_fan_mode" }
    Number HVAC_Operating_State "Op State [MAP(thermostatOpState.map):%d]" (gHVAC) {zwave="7:command=thermostat_operating_state,refresh_interval=60" }
    Number HVAC_Fan_State "Fan State [MAP(thermostatFanState.map):%d]" (gHVAC) {zwave="7:command=thermostat_fan_state,refresh_interval=60" }
    Number HVAC_Battery "Battery State [%d %%]" (gHVAC) {zwave="7:command=BATTERY"}

#### TBZ48 thermostat (does not have humidity sensor)  

    Number      HVAC_HeatSetPoint       "Heat Set [%.1f F]"                               <thermostat>   (HVAC)           {zwave="15:command=THERMOSTAT_SETPOINT,setpoint_type=1,setpoint_scale=1" }
    Number      HVAC_CoolSetPoint       "Cool Set [%.1f F]"                               <thermostat>   (HVAC)           {zwave="15:command=thermostat_setpoint,setpoint_type=2,setpoint_scale=1" }
    Number      HVAC_Temperature        "Thermostat temperature [%.1f °F]"                <temperature>  (HVAC)           {zwave="15:command=sensor_multilevel,sensor_type=1,refresh_interval=60"}
    Number      HVAC_Mode               "Mode [MAP(thermostatMode.map):%s]"               <climate>      (HVAC)           {zwave="15:command=thermostat_mode"}
    Number      HVAC_Fan_Mode           "Fan Mode [MAP(thermostatFanMode.map):%s]"        <wind>         (HVAC)           {zwave="15:command=thermostat_fan_mode"}
    Number      HVAC_Operating_State    "Operation State [MAP(thermostatOpState.map):%s]" <climate>      (HVAC)           {zwave="15:command=thermostat_operating_state,refresh_interval=60"}
    Number      HVAC_Fan_State          "Fan State [MAP(thermostatFanState.map):%s]"      <wind>         (HVAC)           {zwave="15:command=thermostat_fan_state"}
    Number      HVAC_Battery            "HVAC battery state [%d %%]"                      <battery>      (HVAC,battery)   {zwave="15:command=BATTERY"}

#### Horstmann HRT4-ZW Thermostat

    Number  Battery_Sensor_Thermostat "Thermostat Battery: [%d %%]" <battery> (GF_Lounge,Battery) {zwave="31:command=battery"}
    Number  Temp_Desired_Thermostat "Thermostat Desired Temp: [%.1f C]" <temperature> (GF_Lounge,MyOpenHAB) {zwave="31:command=thermostat_setpoint, setpoint_type=1"}
    Number  Temp_Sensor_Thermostat  "Thermostat Measured Temp: [%.1f C]" <temperature>  (GF_Lounge,MyOpenHAB) {zwave="31:command=sensor_multilevel, sensor_type=1"}
    Number  HeatCall_Thermostat "Thermostat calling for heat [MAP(heat.map):%d]"  <fire>  (GF_Lounge) {zwave="31:command=switch_binary"}

#### Horstmann ASR-ZW Boiler Switch

    Switch  Boiler_Sensor "Boiler Switch" <fire>  (GF_Hall) {zwave="33:command=switch_binary"}
    Number  Boiler_Thermo "Boiler Status [MAP(thermostatmode.map):%d]"  <fire>  (GF_Hall) {zwave="33:command=thermostat_mode,refresh_interval=600"}

### Weather Stations

#### Z-Weather Weather Station  

    Number  Windspeed       "Wind [%.2f m/s]"       <wind>  (weather_station)               {     zwave="5:command=sensor_multilevel,sensor_type=6,refresh_interval=300" }
    Number  Luminance       "Luminance [%.1f %%]"   (weather_station)               { zwave="5:command=sensor_multilevel,sensor_type=3,refresh_interval=300" }
    Number  RelativeHumidity        "Humidity [%.1f %%]"    (weather_station)               { zwave="5:command=sensor_multilevel,sensor_type=5,refresh_interval=300" }
    Number  DewPoint        "Dew Point [%.1f °C]"   (weather_station)               { zwave="5:command=sensor_multilevel,sensor_type=11,refresh_interval=300" }
    Number  BarometricPressure      "Barometric Pressure [%.1f kPa]"        (weather_station)               { zwave="5:command=sensor_multilevel,sensor_type=9,refresh_interval=300" }
    Number  TempWeatherStation      "Temp Weatherstation [%.1f °C]" <temperature>   (weather_station)               { zwave="5:command=sensor_multilevel,sensor_type=1,refresh_interval=300" }
    Number  BatteryWeatherStation   "Battery Weatherstation [%.2f %%]"      { zwave="5:command=battery,refresh_interval=600" }This page is an attempt to creating a complete working example of CT100 Thermostat support.

I found the parent page only had items so was missing the needed transforms and rules.
I also did not want to accidentally break formatting for other entries.

### Rules

So far I only have a rule that turns on the Fan if the system has been idle for an hour. A simple effort to keep the air an even temperature/humidity in the house. Here in Florida this will typically only happen late at night.

    import org.openhab.model.script.actions.Timer
    
    val String PATTERN = "HH:mm "
    var Timer timerFanIdle = null
    var Timer timerFanCirculate = null
    var Integer ruleRunning = 0
    
    rule "Idle Fan"
    when
        Item HVAC_Fan_State changed to 0
    then
        var String ts = now.toString(PATTERN)
        if (timerFanIdle != null) {
          timerFanIdle.cancel
          timerFanIdle = null
        }
        if (timerFanCirculate != null) {
          timerFanCirculate.cancel
          timerFanCirculate = null
        }
        timerFanIdle = createTimer(now.plusMinutes(60)) [|
            ts = now.toString(PATTERN)
            ruleRunning = 1
            sendCommand(HVAC_Fan_Mode, 1)
            sendBroadcastNotification(ts + "Run Fan to circulate")
            timerFanCirculate = createTimer(now.plusMinutes(15)) [|
                    ts = now.toString(PATTERN)
              sendCommand(HVAC_Fan_Mode, 0)
              ruleRunning = 0 ]
            ]
         }
    end

    rule "Running Fan"
    when
        Item HVAC_Fan_State changed to 1
    then
        var String ts = now.toString(PATTERN)
        if (ruleRunning == 0) {
          if (timerFanIdle != null) {
            timerFanIdle.cancel
            timerFanIdle = null
          }
          if (timerFanCirculate != null) {
            timerFanCirculate.cancel
            timerFanCirculate = null
          }
        }

A second set of rules which simply turns on the heat in the morning (to 63F), lowers it to 60F for mid day, and sets it to 59F at night.

    rule "climate_morning"                                                 
    when                                                                   
      Time cron "0 40 6 1/1 * ? *"                                         
    then                                                                   
      logInfo("climate", "Temp: " + HVAC_Temperature.state + "; Target: 63")  
      sendCommand(HVAC_HeatSetPoint, 63)                                   
    end                                                                    
                                                                           
    rule "climate_day"                                                     
    when                                                                   
      Time cron "0 0 11 1/1 * ? *"                                         
    then                                                                   
      logInfo("climate", "Temp: " + HVAC_Temperature.state + "; Target: 60")  
      sendCommand(HVAC_HeatSetPoint, 60)                                   
    end                                                                    
                                                                           
    rule "climate_night"                                                   
    when                                                                   
      Time cron "0 30 22 1/1 * ? *"                                        
    then                                                                   
      logInfo("climate", "Temp: " + HVAC_Temperature.state + "; Target: 59")  
      sendCommand(HVAC_HeatSetPoint, 59)                                   
    end

## Z-Wave Security Testing

Many Z-Wave devices communicate with a basic radio protocol which can be intercepted or spoofed.  But Z-Wave also supports encrypted communications via the Security Command Class which is used for high value use cases such as door locks.  The Security Class provides extra protection to help prevent messages from being intercepted and/or spoofed.

- Lock and unlock a door lock
- Reports battery percentage
- Update Lock status
- Set user codes

### General notes

- If you are using this code with a door lock, note that the toggle switch on the GUI may not represent the true state of the lock.  Please manually check that your doors are locked at night.
- The Security command classes in openhab are beta, use at your own risk!
- As with all modern crypto, the encryption is only as strong as the key.  Please take some time to generate a random key and do not use all zeros, etc.
- Door locks have very limited range.  This is not specific to openHAB and appears to be the case in general.  If the door lock is more than a few feet away from the controller, you will likely need a repeater (appliance module) for reliable communication.
- The secure protocol has more overhead than normal communications.  For example, turning on a light is near instantaneous - you hit the switch in the UI and light comes on very quickly.  Secure messages take much longer as there are messages to exchange prior to sending the actual message.  When locking/unlocking a door, it's typical to take 5-10 seconds for the lock to respond.  It will then take additional time for the status to update on the UI.

### Limitations/Known Issues

Not a wishlist, just things that are necessary for the bare minimum functionality that aren't working yet

- Do not do a habmin reinitialize on a door lock.  It has been known to cause issues and require a new secure repairing to restore functionality

### Configuration

* Edit `services/zwave.cfg` and add a new entry. You must replace each number with random hex digits ([GRCs password generator](https://www.grc.com/passwords.htm) is a good source of random numbers):  

```
networkKey=0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##, 0x##
```

* If you have already paired the device with openHAB you must unpair it using the exclusion function.  After it has been excluded, Stop openHAB and delete the etc/zwave/node#.xml that corresponded to the device

* Hard reset the device.  NOTE: if this is a door lock, this will likely erase all door codes you have programmed!

### Steps to test

1. Note that pairing process is different than with other Z-Wave devices. The Z-Wave controller needs to stay connected to the machine running openHAB and it needs to be very close to the secure device (a foot or less) during pairing.
1. Put the Z-Wave controller into inclusion mode using HABmin ![habmin](http://community-openhab-org.s3-eu-central-1.amazonaws.com/original/1X/63ec67c2a6dff6a42c8ef18e46333b0404953cb7.png)
- trigger secure pair from the device per the instructions.  You should see lots of activity in the log file at this time.  The device make show a confirmation or make noise that it is complete, but that is often premature. After a minute or 2, check your logs for "Secure Inclusion complete" or "Secure Inclusion FAILED".  If it was complete, WAIT until you see "Node advancer: loop - DONE" before continuing to the next step.  This is the critical indication that openhab is done interrogating the device to see which commands it supports.  This literally took 4 minutes with my Yale lock, so be patient.  
1. If secure inclusion failed, you can stop the server right away.  Post your results to the thread with the device you are using and the full openhab.log file.  Before trying to secure pair again, you must exclude the device using habmin
1. Stop the openHAB server.  You will now add the device to the items config file.  For example, door lock would require 3 new entries: 1) control the lock, 2) show the current state (requesting a lock/unlock doesn't mean it worked, and someone can manually lock/unlock at any time, so it's critical to NOT rely on the state of the switch), and 3) show battery status. For example: 

```
Switch Door_Lock "Front door lock control" <none> (GF_Office) {zwave="#:command=door_lock"}
Contact Door_Basic "Front door lock status [%s]" <lock> (GF_Office) {zwave="#:command=door_lock,refresh_interval=120"}
Number Door_Corridor_Battery "Front door lock battery level [%d %%]" (GF_Office) { zwave="#:command=battery,refresh_interval=43200" }
```

Be sure to replace # above with the id of your door lock from the secure pairing session

1. start openHAB, wait for everything to initialize and check the logs for errors

1. Try the switch and wait 10 seconds.  If you hear the lock turn, great!  If not, the switch may have been in the wrong position to begin with, so try it again and wait 10 seconds

### User Codes

The user code command class allows the door entry codes to be set on the door lock by OH.  Currently this is a manual process which requires editing the node xml file.  Hopefully this will change in the future with OpenHab2.

The friendlyName value below is just a way for humans to track the codes.  The friendlyName is never sent to the door lock.  OH and the door lock track codes by the position in the userCodeList list.

### Adding Codes

1. Make a backup of all files in openhabhome\etc\zwave.  Move the backup outside of the zwave directory.
1. Open the node xml file for the door lock with a text editor.  Search for "<commandClass>USER_CODE</commandClass>" and you should see something like the example below.
**NOTE:** If you don't see the USER_CODE section, you probably paired the lock with OH before user code support was added to the security branch.  Do a git pull, then exclude and re-perform the secure inclusion process as described above

```xml
 <entry>
   <commandClass>USER_CODE</commandClass>
   <userCodeCommandClass>
     <version>1</version>
     <instances>1</instances>
     <numberOfUsersSupported>30</numberOfUsersSupported>
     <userCodeList/>
   </userCodeCommandClass>
 </entry>
```

1. Note the value of numberOfUsersSupported.  This is the maximum number of user codes you can store in the lock.  If this value is zero, than no codes can be stored
1. You will now edit the "<userCodeList/>" line.  Each code is given a friendly name to go along with the code.  For example to add the user "Dave" with the code "1234" it would look like this:

```xml
<entry>
  <commandClass>USER_CODE</commandClass>
  <userCodeCommandClass>
    <version>1</version>
    <instances>1</instances>
    <numberOfUsersSupported>30</numberOfUsersSupported>
    <userCodeList>
      <userCode>
        <friendlyName>Dave</friendlyName>
        <code>1234</code>
      </userCode>
    </userCodeList>
  </userCodeCommandClass>
</entry>
```

5. To add multiple codes, simply repeat the userCode block, like so:

```xml
<entry>
  <commandClass>USER_CODE</commandClass>
  <userCodeCommandClass>
    <version>1</version>
    <instances>1</instances>
    <numberOfUsersSupported>30</numberOfUsersSupported>
    <userCodeList>
      <userCode>
        <friendlyName>Dave</friendlyName>
        <code>1234</code>
      </userCode>
      <userCode>
        <friendlyName>Bob</friendlyName>
        <code>5678</code>
      </userCode>
    </userCodeList>
  </userCodeCommandClass>
</entry>
```

1. Once your edits are complete, restart OH and your changes should take effect shortly.

**Editing codes**

1. You can add or change codes at any time, just edit the node xml and restart OH
1. If you are removing an item from the list, set the code to 0 (zero) and recycle.  This will trigger OH to tell the door lock to remove the code in that position from the door lock.  Test it out to make sure the code no longer works, then you can remove that userCode block
