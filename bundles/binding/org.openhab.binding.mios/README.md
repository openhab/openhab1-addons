## MiOS Bridge Binding

This binding exposes read, and read-command, access to Devices controlled by a MiOS Home Automation controller, such as those seen at http://getvera.com.

It exposes the ability to do the following things in the MiOS HA Controller

* `Devices` - Read State Variables & Device Attributes, and invoke (single parameter) UPnP Commands to control the Device.
* `Scenes` - Read the current execution state of a Scene, and invoke those Scenes within the remote HA Controller
* `System` - Read System-level Attributes.

It uses the remote control interfaces (aka "UI Simple" JSON Calls, and HTTP Long-polling) of the MiOS HA Controller to keep the _bound_ openHAB Items in sync with their counterparts in the MiOS HA Controller.

The binding uses the openHAB _Transformation Service_ extensively to "map" the Data & Commands between the two systems. A set of example MAP transform files is provided and these can readily be augmented without needing to tweak the code.

Original code was used from the XBMC Binding, and then heavily modified. Snippets included from the HTTP Binding for the various datatype mapping functions.

<!-- MarkdownTOC -->

- [Configuration](#configuration)
    - [MiOS Unit Configuration](#mios-unit-configuration)
    - [Transformations](#transformations)
    - [Item Configuration](#item-configuration)
    - [Item : MiOS System Binding](#item--mios-system-binding)
- [Item Commands \(Reacting\)](#item-commands-reacting)
    - [Item : MiOS Device Binding - Commands \(Reacting\)](#item--mios-device-binding---commands-reacting)
    - [Item : MiOS Scene Binding - Commands \(Reacting\)](#item--mios-scene-binding---commands-reacting)
- [MiOS Binding and MiOS Action Examples](#mios-binding-and-mios-action-examples)
    - [Examples for Augmenting](#examples-for-augmenting)
    - [Examples for Co-existing](#examples-for-co-existing)
    - [Examples for Replacing](#examples-for-replacing)

<!-- /MarkdownTOC -->


## Configuration

### MiOS Unit Configuration

In order for the MiOS openHAB Binding to talk to your MiOS Unit, it needs configuration indicating _where_ it lives.  This information is specified within the `services/mios.cfg` file.

Each MiOS Unit is identified by a _Unit name_, which is user-supplied.  This name will be used throughout the subsequent setup steps, and permits you to connect to more than one MiOS Unit that you might have within your environment.

The binding will only talk you MiOS Units living on the same LAN as your MiOS Unit and/or are directly reachable from your LAN where openHAB is running.

The MiOS gateway services, such as `http://cp.mios.com` and `http://home.getvera.com`, are not supported.

🚨🔧 The simplest configuration entry for `openhab.cfg` contains a Unit name, `house`, and a hostname, `192.168.1.22`, to use for the MiOS Unit connection:

```
house.host=192.168.1.22
```

If you have local DNS setup correctly, then use this form instead:

```
house.host=ha.myhouse.example.com
```

Optionally, you can specify the `port` and `timeout` to use.  These default to `3480` and `60000` (ms) respectively.  These have reasonable defaults, so you shouldn't need to make adjustments.

```
house.host=ha.myhouse.example.com
house.port=3480
house.timeout=30000
```

You can also declare multiple MiOS Units, as illustrated in this example.

```
houseUpstairs.host=ha-upstairs.myhouse.example.com
houseDownstairs.host=ha-downstairs.myhouse.example.com
```

🔦 The MiOS Unit name is case-sensitive, and may only contain AlphaNumeric characters.  The leading character must be an [ASCII] alpha.

[Back to Table of Contents](#configuration)

### Transformations

Internally, the MiOS Binding uses the openHAB _Transformation Service_.  The MiOS Binding supplies a number of pre-configured MAP Transformation for the common use-cases.

🚨🔧 These transformations must be copied from the source-code repository:

```
features/openhab-addons-external/src/main/resources/transform/mios*.map
```

and placed into your openHAB installation under the `transform` directory.

If you have a Unix machine, the MAP files can also be downloaded using:

```
sudo apt-get install subversion
svn checkout https://github.com/openhab/openhab/trunk/features/openhab-addons-external/src/main/resources/transform/
```

🔦  These transformations can be readily extended by the user, for any use-cases that aren't covered by those pre-configured & shipped with the Binding.

[Back to Table of Contents](#configuration)

### Item Configuration

The MiOS Binding provides a few sources of data from the target MiOS Unit.  These can be categorized into the following data values:

* MiOS Device UPnP State Variables
* MiOS Device Attributes
* MiOS Scene Attributes
* MiOS System Attributes

The examples below illustrates the form of each.

The general form of these bindings is:

```
mios="unit:<unitName>,<miosThing>{,command:<commandTransform>}{,in:<inTransform>}{,out:<outTransform>}"
```

In many cases, only a subset of these parameters need to be specified/used, with *internal defaults* applied for the common use-cases.

The sections below describe the types of things that can be bound, in addition to the transformations that are permitted, and any default transformations that may be applied for you.

#### Item Generation : MiOS Item Generator

🚨🔧The [MiOS Item Generator](https://github.com/openhab/openhab1-addons/tree/master/bundles/binding/org.openhab.binding.mios/examples/scripts) is a free-standing tool that generates an initial openHAB Items file for a MiOS Unit.

After the initial generation the openHAB Items file can be customized, or can be regenerated, as Devices are added/removed from the MiOS Unit.

🔦 The Item Generator examples use a MiOS Unit name of "`house`".  This name must match the MiOS Unit name declared in the [MiOS Unit configuration](#mios-unit-configuration).
  Any name can be used, as long as they're in sync across the configuration files.

[Back to Table of Contents](#configuration)

#### Item : MiOS Device Binding - Values (Reading)

Device Bindings can be read-only, with data flowing from the MiOS Unit _into_ openHAB.  Device Bindings have the form:

```
mios="unit:<unitName>,device:<deviceId>/service/<serviceURN>/<serviceVariable>
```

or

```
mios="unit:<unitName>,device:<deviceId>/service/<serviceAlias>/<serviceVariable>
```

With examples like:

```
Number MiOSMemoryUsed         "Used [%.0f KB]"       (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryUsed"}
Number MiOSMemoryAvailable    "Available [%.0f KB]"  (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryAvailable"}
Number MiOSMemoryCached       "Cached [%.0f KB]"     (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryCached"}
Number MiOSMemoryBuffers      "Buffers [%.0f KB]"    (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryBuffers"}
String MiOSCMHLastRebootLinux "Reboot [%s]"          (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/cmhLastRebootTime"}
String MiOSMemoryUsedString   "Memory Used [%s KB]"  (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryUsed"}
```

or, since we've internally Alias'd the UPnP _Service Id_ that Chris used, you can also use:

```
Number MiOSMemoryUsed         "Used [%.0f KB]"       (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryUsed"}
Number MiOSMemoryAvailable    "Available [%.0f KB]"  (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryAvailable"}
Number MiOSMemoryCached       "Cached [%.0f KB]"     (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryCached"}
Number MiOSMemoryBuffers      "Buffers [%.0f KB]"    (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryBuffers"}
String MiOSCMHLastRebootLinux "Reboot [%s]"          (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/cmhLastRebootTime"}
String MiOSMemoryUsedString   "Memory Used [%s KB]"  (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryUsed"}
```

Or you can replace the Weather information, from the openHAB `demo.items` file, with contents from the [Weather Underground](http://wunderground.com) (WUI) [Plugin](http://forum.micasaverde.com/index.php/board,42.0.html) from MiOS:

```
Number Weather_Temperature        "Outside Temperature [%.1f °F]" <temperature> (Weather_Chart) {mios="unit:house,device:318/service/TemperatureSensor1/CurrentTemperature"}
```

or, you can track the status of a Light Switch or perhaps a Dimmer:

```
Number HallLightAsSwitch      "On/Off [%1d]"         (BindingDemo) {mios="unit:house,device:11/service/SwitchPower1/Status"}
Number HallLightAsDimmer      "Level [%3d]"          (BindingDemo) {mios="unit:house,device:11/service/Dimming1/LoadLevelStatus"}  
```

The _serviceAliases_ are built into the MiOS Binding and may be expanded over time, as feedback is received.  Each Alias is case-sensitive, and there can be multiple Aliases for a single UPnP ServiceId:

| _Core_ UPnP Service Id | Aliases |
|----------------------|-------|
|`urn:upnp-org:serviceId:SwitchPower1`|`SwitchPower1`, `Switch`|
|`urn:upnp-org:serviceId:Dimming1`|`Dimming1`,`Dimming`,`Dimmer`|
|`urn:upnp-org:serviceId:TemperatureSensor1`|`TemperatureSensor1`,`Temperature`|
|`urn:upnp-org:serviceId:HVAC_FanOperatingMode1`|`HVAC_FanOperatingMode1`,`HVACFan`|
|`urn:upnp-org:serviceId:HVAC_UserOperatingMode1`|`HVAC_UserOperatingMode1`,`HVACUser`|
|`urn:upnp-org:serviceId:TemperatureSetpoint1_Heat`|`TemperatureSetpoint1_Heat`,`Heat`|
|`urn:upnp-org:serviceId:TemperatureSetpoint1_Cool`|`TemperatureSetpoint1_Cool`,`Cool`|
|`urn:upnp-org:serviceId:AVTransport`|`AVTransport`|
|`urn:upnp-org:serviceId:RenderingControl`|`RenderingControl`|
|`urn:upnp-org:serviceId:DeviceProperties`|`DeviceProperties`|
|`urn:upnp-org:serviceId:HouseStatus1`|`HouseStatus1`,`HouseStatus`|
|`urn:upnp-org:serviceId:ContentDirectory`|`ContentDirectory`|
|`urn:upnp-org:serviceId:AudioIn`|`AudioIn`|
|`urn:upnp-org:serviceId:ZoneGroupTopology`|`ZoneGroupTopology`|


| _MiCasaVerde_ UPnP Service Id | Aliases |
|-----------------------------|-------|
|`urn:micasaverde-com:serviceId:ZWaveDevice1`|`ZWaveDevice1`,`ZWaveDevice`|
|`urn:micasaverde-com:serviceId:ZWaveNetwork1`|`ZWaveNetwork1`,`ZWaveNetwork`|
|`urn:micasaverde-com:serviceId:HaDevice1`|`HaDevice1`,`HaDevice`|
|`urn:micasaverde-com:serviceId:SceneControllerLED1`|`SceneControllerLED1`,`SceneControllerLED`|
|`urn:micasaverde-com:serviceId:SecuritySensor1`|`SecuritySensor1`,`Security`|
|`urn:micasaverde-com:serviceId:HumiditySensor1`|`HumiditySensor1`,`Humidity`|
|`urn:micasaverde-com:serviceId:EnergyMetering1`|`EnergyMetering1`,`EnergyMeter`|
|`urn:micasaverde-com:serviceId:SceneController1`|`SceneController1`,`SceneController`|
|`urn:micasaverde-com:serviceId:HVAC_OperatingState1`|`HVAC_OperatingState1`,`HVACState`|
|`urn:micasaverde-org:serviceId:SerialPort1`|`SerialPort1`,`Serial`|
|`urn:micasaverde-com:serviceId:DoorLock1`|`DoorLock1`,`DoorLock`|
|`urn:micasaverde-com:serviceId:AlarmPartition2`|`AlarmPartition2`,`Alarm`|
|`urn:micasaverde-com:serviceId:Camera1`|`Camera1`,`Camera`|
|`urn:micasaverde-com:serviceId:MiosUpdater1`|`MiosUpdater1`,`MiosUpdater`|
|`urn:micasaverde-com:serviceId:HouseModes1`|`HouseModes1`,`HouseModes`|


| _Plugin-specific_ UPnP Service Id | Aliases |
|---------------------------------|-------|
|`urn:cd-jackson-com:serviceId:SystemMonitor`|`SystemMonitor`|
|`urn:cd-jackson-com:serviceId:Config`|`CDJConfig`|
|`urn:garrettwp-com:serviceId:WPSwitch1`|`WPSwitch1`,`WPSwitch`|
|`urn:watou-com:serviceId:Nest1`|`Nest1`,`Nest`|
|`urn:watou-com:serviceId:NestStructure1`|`NestStructure1`,`NestStructure`|
|`urn:upnp-micasaverde-com:serviceId:Weather1`|`Weather1`,`Weather`|
|`urn:demo-ted-striker:serviceId:PingSensor1`|`PingSensor1`,`PingSensor`|
|`urn:micasaverde-com:serviceId:Sonos1`|`Sonos1`,`Sonos`|
|`urn:demo-paradox-com:serviceId:ParadoxSecurityEVO1`|`ParadoxSecurityEVO1`,`Paradox`|
|`urn:macrho-com:serviceId:LiftMasterOpener1`|`LiftMasterOpener1`,`LiftMaster`|
|`urn:directv-com:serviceId:DVR1`|`DirecTVDVR1`,`DirecTV`|
|`urn:futzle-com:serviceId:UPnPProxy1`|`UPnPProxy1`,`UPnPProxy`|
|`urn:rfxcom-com:serviceId:rfxtrx1`|`RfxtrxNew`|
|`upnp-rfxcom-com:serviceId:rfxtrx1`|`Rfxtrx`|
|`urn:upnp-arduino-cc:serviceId:arduinonode1`|`ArduinoNode1`,`ArduinoNode`|
|`urn:upnp-arduino-cc:serviceId:arduino1`|`Arduino1`,`Arduino`|
|`urn:akbooer-com:serviceId:DataYours1`|`DataYours1`,`DataYours`|
|`urn:richardgreen:serviceId:VeraAlert1`|`VeraAlert1`,`VeraAlert`|
|`urn:upnp-org:serviceId:VSwitch1`|`VSwitch1`,`VSwitch`|

[Back to Table of Contents](#configuration)

#### Item : MiOS Scene Binding - Values (Reading)

Scene Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  Scene Bindings have the form:

```
mios="unit:<unitName>,scene:<sceneId>/<attributeName>
```

With examples like:

```
Number   SceneGarageOpenId         (GScene) {mios="unit:house,scene:109/id"}
Number   SceneGarageOpenStatus     (GScene) {mios="unit:house,scene:109/status"}
String   SceneGarageOpenActive     (GScene) {mios="unit:house,scene:109/active"}
```

[Back to Table of Contents](#configuration)
      
### Item : MiOS System Binding

System Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  System Bindings have the form:

```
mios="unit:<unitName>,system:/<attributeName>
```

With examples like:

```
Number   SystemZWaveStatus         "[%d]"  (GSystem) {mios="unit:house,system:/ZWaveStatus"}
String   SystemLocalTime           "[%s]"  (GSystem) {mios="unit:house,system:/LocalTime"}
String   SystemTimeStamp           "[%s]"  (GSystem) {mios="unit:house,system:/TimeStamp"}
String   SystemUserDataDataVersion "[%s]"  (GSystem) {mios="unit:house,system:/UserData_DataVersion"}
Number   SystemDataVersion         "[%d]"  (GSystem) {mios="unit:house,system:/DataVersion"} 
String   SystemLoadTime            "[%s]"  (GSystem) {mios="unit:house,system:/LoadTime"} 
```

[Back to Table of Contents](#configuration)

#### Transformations

Sometimes the value presented by the binding isn't in the format that you require for your Item.  For these cases, the binding provides access to the standard openHAB _Transformation Service_.

To utilize the _Transformation Service_, you need to declare additional settings on your bindings.

These take the form of the `in:` and `out:` declarations at the end of the binding.  The `in:` declaration is used when values are received from the MiOS Unit, but before it places the value into openHAB.  The `out:` declaration is used when values are taken from the openHAB system for delivered to the MiOS Unit (in _Command_ execution, for example).

```
mios="unit:<unitName>,<miosThing>{,in:<inTransform>}{,out:<outTransform>}"
```

As you can see by the above declaration, the input and output transformations are optional.  If they aren't declared, then an internal, automated, transformation will be attempted based upon the Type of the Item being bound and, in some cases, the type of MiOS Attribute and/or State Variable involved in the binding.

With examples like:

```
String   SystemZWaveStatusString "ZWave Status String [%d]" (GSystem) {mios="unit:house,system:/ZWaveStatus,in:MAP(miosZWaveStatusIn.map)"}
Contact  LivingRoomZoneTripped "Living Room (Zone 2) [%s]" <contact> (GContact,GWindow,GPersist) {mios="unit:house,device:117/service/SecuritySensor1/Tripped,in:MAP(miosContactIn.map)"}
```

and a map transform file like `transform/miosZWaveStatusIn.map`:

```
1=Cool Bananas
0=In the Dog house
-=Your guess is as good as mine!
```

and a map transform file like `transform/miosSwitchIn.map`:

```
1=OPEN
0=CLOSED
```

Then as data flows from the MiOS system, data for these items will be _transformed_ into the new String format for display and/or rule purposes.

To ease the setup process, the common MiOS entities have internal defaults for these parameters.  This aids in keeping the binding string simple for typical use-case scenarios.  The defaults are as follows:

For Devices, the defaults are as follows:

State Variable / Attribute  | Default Parameters
--------------------------- | -------------------
`service/urn:micasaverde-com:serviceId:DoorLock1/Status` | `command:MAP(miosLockCommand.map)`<br>`in:MAP(miosSwitchIn.map)`<br>`out:MAP(miosSwitchOut.map)`
`service/urn:watou-com:serviceId:Nest1/status` | `in:MAP(miosContactIn.map)`<br>`out:MAP(miosContactOut.map)`
`service/urn:upnp-org:serviceId:RenderingControl/Mute`  | ` command:MAP(miosUPnPRenderingControlMuteCommand.map)`<br>`in:MAP(miosSwitchIn.map)`<br>`out:MAP(miosSwitchOut.map)`
`service/urn:micasaverde-com:serviceId:SecuritySensor1/Armed` | `command:MAP(miosArmedCommand.map)`<br>`in:MAP(miosSwitchIn.map)`<br>`out:MAP(miosSwitchOut.map)`
`service/urn:micasaverde-com:serviceId:SecuritySensor1/Tripped` | `in:MAP(miosContactIn.map)`<br>`out:MAP(miosContactOut.map)`
`service/urn:upnp-org:serviceId:SwitchPower1/Status` | `command:ON/OFF`<br>`in:MAP(miosSwitchIn.map)`<br>`out:MAP(miosSwitchOut.map)`
`service/urn:upnp-org:serviceId:Dimming1/LoadLevelStatus` | `command:MAP(miosDimmerCommand.map)`
`service/urn:upnp-org:serviceId:TemperatureSetpoint1_Heat/CurrentSetpoint` | `command:MAP(miosTStatSetpointHeatCommand.map)`
`service/urn:upnp-org:serviceId:TemperatureSetpoint1_Cool/CurrentSetpoint` | `command:MAP(miosTStatSetpointCoolCommand.map)`
`service/urn:upnp-org:serviceId:HVAC_UserOperatingMode1/ModeStatus` | `command:MAP(miosTStatModeStatusCommand.map)`
`service/urn:upnp-org:serviceId:HVAC_FanOperatingMode1/Mode` | `command:MAP(miosTStatFanOperatingModeCommand.map)`
`service/urn:upnp-org:serviceId:RenderingControl/Volume` | `command:MAP(miosUPnPRenderingControlVolumeCommand.map)`
`service/urn:upnp-org:serviceId:AVTransport/TransportState` | `command:MAP(miosUPnPTransportStatePlayModeCommand.map)` 
`status` | `in:MAP(miosStatusIn.map)` | 


For Scenes, they look like:

State Variable / Attribute  | Default Parameters
--------------------------- | -------------------
`active` | `in:MAP(miosSceneActiveIn.map)`
`status` | `command:`<br>`in:MAP(miosStatusIn.map)`


For users wanting more advanced configurations, the openHAB _Transformation Service_ provides a number of other transforms that may be of interest:

* `JS(example.js)` - run the Javascript to perform the conversion.
* `MAP (example.map)` - Transform using the static, file-based, conversion.
* `XSLT(example.xslt)` - Transform using an XSLT transformation.
* `EXEC(...)` - Transform using the OS-level script.
* `REGEX(...)` - Transform using the supplied Regular Expression and use Capture markers `(` and `)` around the value to be extracted.
* `XPATH(...)` - Transform using the supplied XPath Expression.

More reading on these is available in the openHAB Wiki.

[Back to Table of Contents](#configuration)

## Item Commands (Reacting)

By default, openHAB will send Commands to the Controls that have been outlined in the associated `sitemaps/*.sitemap` file.  The Commands sent depend upon the type of Control that's been bound to the Item.

Through observation, the following commands are commonly sent:

* Switch - `ON`, `OFF` (When Bound to a _Switch_ Item)
* Switch - `TOGGLE` (When Bound to a _Contact_ Item)
* Switch - `ON` (When `autoupdate="false"` is also present in the binding list)
* Slider - `INCREASE`, `DECREASE`, _&lt;PCTNumber>_

MiOS Units don't natively handle these Commands so a mapping step must occur before openHAB Commands can be executed by a MiOS Unit.  Additionally, since MiOS Bindings are read-only by default, we must add a parameter to indicate we want data to flow back to the MiOS Unit.

The `command:` Binding parameter is used to specify that we want data to flow back to the MiOS unit as well as how to perform the required mapping.  For most Items bound using the MiOS Binding, internal defaults will take care of the correct `command:`, `in:` and `out:` parameters.  These need only be specified if you have something not handled by the internal defaults, or wish to override them with custom behavior.

[Back to Table of Contents](#configuration)

### Item : MiOS Device Binding - Commands (Reacting)

For MiOS Devices, this parameter can take one of several forms:

```
mios="unit:<unitName>,device:<deviceId>/service/<UPnPVariable>,command:{<CommandMap>}"
```

With definitions as:

_&lt;CommandMap>_ is _&lt;blank>_ OR;<br>
_&lt;CommandMap>_ is _&lt;InlineCommandMap>_ OR;<br>
_&lt;CommandMap>_ is _&lt;openHABTransform>_ `(` _&lt;TransformParams>_ `)`<br>

_&lt;InlineCommandMap>_ is _&lt;openHABCommandMap>_ { `|` _&lt;openHABCommandMap>_ }*<br>
_&lt;openHABCommandMap>_ is _&lt;openHABCommand>_ { `=` _&lt;UPnPAction>_ }<br>
_&lt;openHABCommand>_ is `ON`, `OFF`, `INCREASE`, `DECREASE`, etc or the special value `_defaultCommand`<br>

_&lt;UPnPAction>_ is _&lt;ServiceName>_ `/` _&lt;ServiceAction>_ `(` { _&lt;ServiceParam>_ { `=` _&lt;ServiceValue>_ | `=` _&lt;BoundValue>_} } `)` OR;<br>
_&lt;UPnPAction>_ is _&lt;ServiceAlias>_ `/` _&lt;ServiceAction>_ `(` { _&lt;ServiceParam>_ { `=` _&lt;ServiceValue>_ | `=` _&lt;BoundValue>_} } `)`<br>

_&lt;UPnPVariable>_ is _&lt;ServiceName>_ `/` _&lt;ServiceVariable>_ OR;<br>
_&lt;UPnPVariable>_ is _&lt;ServiceAlias>_ `/` _&lt;ServiceVariable>_<br>

_&lt;openHABTransform>_ is `MAP`, `XSLT`, `EXEC`, `XPATH`, etc<br>

_&lt;BoundValue>_ is `?`, `??`, `?++`, `?--`

[Back to Table of Contents](#configuration)

#### Device Command Binding Examples (Parameterless)

In practice, when discrete commands are being sent by openHAB, the map is fairly simple.  In the examples listed below, the `*.map` files are provided and can be downloaded per the [Transformations](#transformations) setup descriptions.

[Back to Table of Contents](#configuration)

##### A Switch...

You might start off with an inline definition of the mapping:

```
Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status,command:ON=SwitchPower1/SetTarget(newTargetValue=1)|OFF=SwitchPower1/SetTarget(newTargetValue=0),in:MAP(miosSwitchIn.map)"}
```

And then reduce it to the internal default map, but specify that you only want to handle `ON` and `OFF` Commands:

```
Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status,command:ON|OFF,in:MAP(miosSwitchIn.map)"}
```

or, *more simply*, use the internal defaults altogether:

```
Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status"}
```

[Back to Table of Contents](#configuration)

##### An Armed Sensor...

The simple version, using internal defaults for the `SecuritySensor1/Armed` service state of the Device:

```
Switch   LivingRoomZoneArmed "Zone Armed [%s]" {mios="unit:house,device:117/service/SecuritySensor1/Armed"}
```

or the fully spelled out version:

```
Switch   LivingRoomZoneArmed "Zone Armed [%s]" {mios="unit:house,device:117/service/SecuritySensor1/Armed,command:MAP(miosArmedCommand.map),in:MAP(miosSwitchIn.map)"}
```

[Back to Table of Contents](#configuration)

##### A Lock...

The simple version, using internal defaults for the `DoorLock1/Status` service state of the Device:

```
Switch   GarageDeadboltDStatus "Garage Deadbolt" (GLock,GSwitch) {mios="unit:house,device:189/service/DoorLock1/Status"}
```

or the full version:

```
Switch   GarageDeadboltDStatus "Garage Deadbolt" (GLock,GSwitch) {mios="unit:house,device:189/service/DoorLock1/Status,command:MAP(miosLockCommand.map),in:MAP(miosSwitchIn.map)"}
```

[Back to Table of Contents](#configuration)

#### Device Command Binding Examples (Parameterized)

For some Commands, in order to pass this information to the remote MiOS Unit, we need to know either the current value of the _Item_, or we need to know the current value of the _Command_.

To do this, we introduce the _&lt;BoundValue>_ parameter that, when present in the mapped-command, will be expanded prior to being sent to the MiOS Unit:

* `?++` - Item Value + 10
* `?--` - Item Value - 10
* `?` - Item Value
* `??` - Command Value

Additionally, since _&lt;PCTNumber>_ is just a value, it won't match any of the entries in our Mapping file, so we introduce a magic key `_defaultCommand`.  We first attempt to do a literal mapping and, if that doesn't find a match, we go look for this magic key and use it's entry.

[Back to Table of Contents](#configuration)

##### A Dimmer, Volume Control, Speed controlled Fan...

The simple version, using internal defaults for the `Dimming1/LoadLevelStatus` service state of the Device:

```
Dimmer   MasterCeilingFanLoadLevelStatus "Master Ceiling Fan [%d]%" <slider> (GDimmer) {mios="unit:house,device:101/service/Dimming1/LoadLevelStatus"}
```

or the full version:

```
Dimmer   MasterCeilingFanLoadLevelStatus "Master Ceiling Fan [%d]%" <slider> (GDimmer) {mios="unit:house,device:101/service/Dimming1/LoadLevelStatus,command:MAP(miosDimmerCommand.map)"}
```

Since Dimmer Items in openHAB can be sent `INCREASE`, `DECREASE` or _&lt;PCTNumber>_ as the command, the mapping file must account for both the static commands (`INCREASE`, `DECREASE`) as well as the possibility of a _Command Value_ being sent.

The `miosDimmerCommand.map` file has a definition that handles this situation:

```
INCREASE=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=?++)
DECREASE=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=?--)
    _defaultCommand=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=??)
```

[Back to Table of Contents](#configuration)

##### A Thermostat...

A Thermostat is composed of a number of pieces.  Each piece must be first bound to openHAB Items, and then a number of mappings must be put in place.

Since all the components of a Thermostat have reasonable internal defaults, we'll use the simpler form for our Item definitions in openHAB:

```
/* Thermostat Upstairs */
Number   ThermostatUpstairsId "ID [%d]" {mios="unit:house,device:335/id"}
String   ThermostatUpstairsDeviceStatus "Device Status [%s]" (GThermostatUpstairs) {mios="unit:house,device:335/status"}
Number   ThermostatUpstairsCurrentTemperature "Upstairs Temperature [%.1f °F]" <temperature> (GThermostatUpstairs, GTemperature) {mios="unit:house,device:335/service/TemperatureSensor1/CurrentTemperature"}
Number   ThermostatUpstairsHeatCurrentSetpoint "Heat Setpoint [%.1f °F]" <temperature> (GThermostatUpstairs) {mios="unit:house,device:335/service/TemperatureSetpoint1_Heat/CurrentSetpoint"}
Number   ThermostatUpstairsCoolCurrentSetpoint "Cool Setpoint [%.1f °F]" <temperature> (GThermostatUpstairs) {mios="unit:house,device:335/service/TemperatureSetpoint1_Cool/CurrentSetpoint"}
String   ThermostatUpstairsFanMode "Fan Mode" (GThermostatUpstairs) {mios="unit:house,device:335/service/HVAC_FanOperatingMode1/Mode"}
String   ThermostatUpstairsFanStatus "Fan Status [%s]" (GThermostatUpstairs) {mios="unit:house,device:335/service/HVAC_FanOperatingMode1/FanStatus"}
String   ThermostatUpstairsModeStatus "Mode Status" (GThermostatUpstairs) {mios="unit:house,device:335/service/HVAC_UserOperatingMode1/ModeStatus"}
String   ThermostatUpstairsModeState "Mode State [%s]" (GThermostatUpstairs) {mios="unit:house,device:335/service/HVAC_OperatingState1/ModeState"}
Number   ThermostatUpstairsBatteryLevel "Battery Level [%d] %" (GThermostatUpstairs) {mios="unit:house,device:335/service/HaDevice1/BatteryLevel"}
DateTime ThermostatUpstairsBatteryDate "Battery Date [%1$ta, %1$tm/%1$te %1$tR]" <calendar> (GThermostatUpstairs) {mios="unit:house,device:335/service/HaDevice1/BatteryDate"}
DateTime ThermostatUpstairsLastUpdate "Last Update [%1$ta, %1$tm/%1$te %1$tR]" <calendar> (GThermostatUpstairs) {mios="unit:house,device:335/service/HaDevice1/LastUpdate"}
```

and these need to be paired with similar items in the `*.sitemap` file:

```
Text     item=ThermostatUpstairsCurrentTemperature {
    Text     item=ThermostatHumidityUpstairsCurrentLevel
    Setpoint item=ThermostatUpstairsHeatCurrentSetpoint minValue=40 maxValue=80
    Setpoint item=ThermostatUpstairsCoolCurrentSetpoint minValue=40 maxValue=80
    Switch   item=ThermostatUpstairsFanMode mappings=[ContinuousOn="On", Auto="Auto"]
    Text     item=ThermostatUpstairsFanStatus
    Switch   item=ThermostatUpstairsModeStatus mappings=[HeatOn="Heat", CoolOn="Cool", AutoChangeOver="Auto", Off="Off"]
    Text     item=ThermostatUpstairsModeState
    Text     item=ThermostatUpstairsBatteryLevel
    Text     item=ThermostatUpstairsBatteryDate
}
```

[Back to Table of Contents](#configuration)

### Item : MiOS Scene Binding - Commands (Reacting)

MiOS Scenes are parameterless.  They can only be requested to execute, and they provide status updates as attribute values during their execution (`status`) or if they're currently active (`active`).

For MiOS Scenes, the `command:` parameter has a simpler form:

```
mios="unit:<unitName>,scene:<sceneId>{/<SceneAttribute>},command:{<CommandMap>}{,in:<inTransform>}"
```

With definitions as:

_&lt;CommandList>_ is _&lt;blank>_ OR;<br>
_&lt;CommandList>_ is _&lt;openHABCommand>_ { `|` _&lt;openHABCommand>_ }*

_&lt;openHABCommand>_ is `ON`, `OFF`, `INCREASE`, `DECREASE`, `TOGGLE` etc

_&lt;SceneAttribute>_ is `status` | `active`

[Back to Table of Contents](#configuration)

#### Scene Command Binding Examples

In general Scenes tend to look like:

```
String   SceneMasterClosetLights "Master Closet Lights Scene" <sofa> (GScene) {mios="unit:house,scene:109/status, autoupdate="false"}
```

Or if you want the Scene executed upon receipt of `ON` or `TOGGLE` Commands:

```
String   SceneMasterClosetLights "Master Closet Lights Scene" <sofa> (GScene) {mios="unit:house,scene:109/status,command:ON|TOGGLE", autoupdate="false"}
```

🔦 Here we've added an additional configuration to the binding declaration, `autoupdate="false"`, to ensure the Switch no longer has the `ON` and `OFF` States automatically managed.  In openHAB, this declaration ensures that the UI rendition appears like a Button.

[Back to Table of Contents](#configuration)


## MiOS Binding and MiOS Action Examples

Users typically have configurations falling into one or more of the following categories, which will be used to outline any subsequent examples:

* Augmenting - openHAB Rules that "add" to existing MiOS Scenes.
* Co-existing - Replacing MiOS Scenes with openHAB Rules, but keeping the Devices.
* Replacing - wholesale replacement of MiOS functionality (Devices|Scenes) with openHAB equivalent functionality.

### Examples for Augmenting

#### Adding Notifications and Text-to-Speech (TTS) when a House Alarm is triggered

MiOS has a standardized definition that most Alarm Panel plugins adhere to (DSC, Ademco, GE Caddx, Paradox, etc).  This exposes a standardized UPnP-style attribute, `AlarmPartition2/Alarm`, for the Alarm System being in active _Alarm_ mode.  It has the value `None` or `Alarm`.

Here we check the specific transition between those two states as we want to avoid being re-notified, when the `Uninitialized` » `Alarm` state transition occurs, should openHAB restart.

Item declaration (`house.items`):

```xtend
String   AlarmArea1Alarm "Alarm Area 1 Alarm [%s]" (GAlarmArea1) {mios="unit:house,device:228/service/AlarmPartition2/Alarm"}
String   AlarmArea1ArmMode "Alarm Area 1 Arm Mode [%s]" (GAlarmArea1) {mios="unit:house,device:228/service/AlarmPartition2/ArmMode"}
String   AlarmArea1LastUser "Alarm Area 1 Last User [%s]" (GAlarmArea1) {mios="unit:house,device:228/service/AlarmPartition2/LastUser"}
```

Rule declaration (`house-alarm.rules`):

```xtend
rule "Alarm Panel Breach"
when
	Item AlarmArea1Alarm changed to Active
then
	pushNotification("House-Alarm", "House in ALARM!! Notification")
	say("Alert: House in Alarm Notification")
end

rule "Alarm Panel Armed (Any)"
when
	Item AlarmArea1ArmMode changed from Disarmed to Armed
then
	say("Warning! House Armed Notification")

	// Perform deferred notifications, as the User.state may not have been processed yet.
	createTimer(now.plusSeconds(1)) [
		logDebug("house-alarm", "Alarm-Panel-Armed-Any Deferred notification")
		var user = AlarmArea1LastUser.state as StringType

		if (user == null) user = "user unknown"
		pushNotification("House-Armed", "House Armed Notification (" + user + ")")
	]
end

rule "Alarm Panel Disarmed (Fully)"
when
	Item AlarmArea1ArmMode changed from Armed to Disarmed
then
	say("Warning! House Disarmed Notification")

	// Perform deferred notifications, as the User.state may not have been processed yet. 
	createTimer(now.plusSeconds(1)) [
		logDebug("house-alarm", "Alarm-Panel-Disarmed-Fully Deferred notification")
		var user = AlarmArea1LastUser.state as StringType

		if (user == null) user = "user unknown"
		pushNotification("House-Disarmed", "House Disarmed Notification (" + user + ")")
	]
end
```

#### Adding Notifications when Battery Powered devices are running low

MiOS Systems standardize Battery Level indications (0-100%) for all battery-power devices (Alarm sensors, Z-Wave Door Locks, etc) and Nest uses a simple "ok" String to represent the Battery Status.

Item declaration (`house.items`):

```xtend
Number   GarageDeadboltBatteryLevel "Garage Deadbolt Battery Level [%d %%]" <energy> (GBattery,GPersist) {mios="unit:house,device:189/service/HaDevice1/BatteryLevel"}
Number   HallCupboardZoneBatteryLevel "Hall Cupboard Battery Level [%d %%]" <energy> (GBattery,GPersist) {mios="unit:house,device:301/service/HaDevice1/BatteryLevel"}
Number   EXTFrontMotionZoneBatteryLevel "EXT Front Motion Zone Battery Level [%d %%]" <energy> (GBattery,GPersist) {mios="unit:house,device:302/service/HaDevice1/BatteryLevel"}
Number   EXTRearMotionZoneBatteryLevel "EXT Rear Motion Battery Level [%d %%]" <energy> (GBattery,GPersist) {mios="unit:house,device:396/service/HaDevice1/BatteryLevel"}
String   NestSmokeGuestBedroom_battery_health "Guest Bedroom Smoke Battery Health [%s]" <energy>       (GSmoke,GBattery,GPersist) {nest="<[smoke_co_alarms(Guest Bedroom).battery_health]"}
```

Rule declaration (`house-battery.rules`):

```xtend
val Number LOW_BATTERY_THRESHOLD = 60 // for Z-Wave Battery devices
val String OK_BATTERY_STATE = 'ok'    // for Nest Thermostat and Protect devices

rule "Low Battery Alert"
when
        Time cron "0 0 8,12,20 * * ?"
then
        GBattery?.members.filter(s|s.state instanceof DecimalType).forEach[item |
                var Number level = item.state as Number
                var String name = item.name

                if (level < LOW_BATTERY_THRESHOLD) {
                        logInfo('Low-Battery-Alert', 'Bad: ' + name)
                        pushNotification("Low-Battery-Alert", "House Low Battery Notification (" + name + ")")
                } else {
                        logDebug('Low-Battery-Alert', 'Good: ' + name)
                }
        ]

        GBattery?.members.filter(s|s.state instanceof StringType).forEach[item |
                var String level = (item.state as StringType).toString
                var String name = item.name

                if (level != OK_BATTERY_STATE) {
                        logInfo('Low-Battery-Alert', 'Bad: ' + name)
                        pushNotification("Low-Battery-Alert", "House Low Battery Notification (" + name + ")")
                } else {
                        logDebug('Low-Battery-Alert', 'Good: ' + name)
                }
        ] 
end
```

### Examples for Co-existing

#### When Motion detected turn Lights ON (OFF after 5 minutes)

This is typical of a declarative Scene in MiOS.  In this case, the lights are left on for 5 minutes, and if new motion is detected in that time, another 5 minute clock is started.

The logging can be removed as needed.

Item declaration (`house.items`):

```xtend
Group GSwitch All

Switch   MasterClosetLightsStatus "Master Closet Lights" (GSwitch) {mios="unit:house,device:391/service/SwitchPower1/Status"}
Switch   MasterClosetFibaroLightStatus "Master Closet Fibaro Light" (GSwitch) {mios="unit:house,device:431/service/SwitchPower1/Status"}
```

Rule declaration (`house-master.rules`):

```xtend
import org.openhab.model.script.actions.Timer
import java.util.concurrent.locks.ReentrantLock

val int MCL_DELAY_SECONDS = 300
var Timer mclTimer = null
var ReentrantLock mclLock = new ReentrantLock(false)

rule "Master Closet Motion"
when
	Item MasterClosetZoneTripped changed from CLOSED to OPEN
then
	logInfo("house-master", "Master-Closet-Motion Timer lights ON")
	sendCommand(MasterClosetLightsStatus, ON)
	sendCommand(MasterClosetFibaroLightStatus, ON)

	mclLock.lock
	if (mclTimer != null) {
		mclTimer.cancel
		logInfo("house-master", "Master-Closet-Motion Timer Cancel")
	}

	mclTimer = createTimer(now.plusSeconds(MCL_DELAY_SECONDS)) [
		logInfo("house-master", "Master-Closet-Motion Timer lights OFF")
		sendCommand(MasterClosetLightsStatus, OFF)
		sendCommand(MasterClosetFibaroLightStatus, OFF)
	]
	mclLock.unlock
end
```

#### When Motion detected turn Lights ON (if nighttime) and OFF after 5 minutes

A variant of the above, this Rule has parts that only run at Nighttime.  Here we use the [[Astro Binding|Astro-Binding]] to compute daylight hours.  See the Astro Binding configuration for details on how to setup that Binding's `configuration/openhab.cfg` entry.

Item declaration (`sunrise.items`):

```xtend
DateTime ClockDaylightStart "Daylight Start [%1$tH:%1$tM]" <calendar> {astro="planet=sun, type=daylight, property=start, offset=-30"}
DateTime ClockDaylightEnd   "Daylight End [%1$tH:%1$tM]" <calendar>   {astro="planet=sun, type=daylight, property=end, offset=+30"}
```

Item declaration (`house.items`):

```xtend
Switch   KitchenSinkLightStatus "Kitchen Sink Light" (GSwitch) {mios="unit:house,device:99/service/SwitchPower1/Status"}
Switch   KitchenPantryLightStatus "Kitchen Pantry Light" (GSwitch) {mios="unit:house,device:425/service/SwitchPower1/Status"}
Switch   PowerHotWaterPumpStatus "Power Hot Water Pump" (GSwitch) {mios="unit:house,device:303/service/SwitchPower1/Status"}
Switch   KitchenPantryZoneArmed "Zone Armed [%s]" {mios="unit:house,device:426/service/SecuritySensor1/Armed"}
```

Rule declaration (`house-kitchen.rules`):

```xtend
import org.openhab.model.script.actions.Timer
import java.util.concurrent.locks.ReentrantLock

val int K_DELAY_SECONDS = 240
var Timer kTimer = null
var ReentrantLock kLock = new ReentrantLock(false)

rule "Kitchen Motion"
when
	Item KitchenMotionZoneTripped changed from CLOSED to OPEN
then
	logInfo("house-kitchen", "Kitchen-Motion Timer ON")

	// Ignore this Rule if the Motion sensor is bypassed.
	if (KitchenMotionZoneArmed.state != ON) {
		logInfo("house-kitchen", "Kitchen-Motion Not Armed, skipping")
		return void
	}

	val DateTime daylightStart = new DateTime((ClockDaylightStart.state as DateTimeType).getCalendar)
	val DateTime daylightEnd = new DateTime((ClockDaylightEnd.state as DateTimeType).getCalendar)

	var boolean night = daylightStart.isAfterNow || daylightEnd.isBeforeNow

	if (night) {
		logInfo("house-kitchen", "Kitchen-Motion Night Time")
		sendCommand(KitchenSinkLightStatus, ON)
		sendCommand(KitchenPantryLightStatus, ON)
	}

	logInfo("house-kitchen", "Kitchen-Motion Any Time")
	sendCommand(PowerHotWaterPumpStatus, ON)

	kLock.lock
	if (kTimer != null) {
		kTimer.cancel
		logInfo("house-kitchen", "Kitchen-Motion Timer Cancel")
	}

	kTimer = createTimer(now.plusSeconds(K_DELAY_SECONDS)) [
		logInfo("house-kitchen", "Kitchen-Motion Timer OFF")
		sendCommand(KitchenSinkLightStatus, OFF)
		sendCommand(KitchenPantryLightStatus, OFF)
		sendCommand(PowerHotWaterPumpStatus, OFF)
	]
	kLock.unlock
end
```

#### When opening/closing Windows keep Nest _Away_ state in sync to save energy.

This originally ran as a Scene on the MiOS Unit, but was replaced with an openHAB Rule.  The Items are a mix of Items, from an Alarm system running on MiOS, and the [[Nest Binding|Nest-Binding]], running locally.

Explicitly check for the `OPEN` » `CLOSED` state transition, to avoid issues when openHAB restarts (`Uninitialized` » `OPEN`) or when _duplicate_ values come in from the MiOS System (`OPEN` » `OPEN`).

Item declaration (`house.items`):

```xtend
Group GPersist (All)
Group GWindow "All Windows [%d]" <contact> (GContact)

Contact  LivingRoomZoneTripped "Living Room (Zone 2) [MAP(en.map):%s]" <contact>        (GWindow,GPersist) {mios="unit:house,device:117/service/SecuritySensor1/Tripped"}
Contact  KitchenZoneTripped "Kitchen (Zone 3) [MAP(en.map):%s]" <contact>               (GWindow,GPersist) {mios="unit:house,device:118/service/SecuritySensor1/Tripped"}
Contact  FamilyRoomZoneTripped "Family Room (Zone 5) [MAP(en.map):%s]" <contact>        (GWindow,GPersist) {mios="unit:house,device:120/service/SecuritySensor1/Tripped"}
Contact  MasterBedroomZoneTripped "Master Bedroom (Zone 8) [MAP(en.map):%s]" <contact>  (GWindow,GPersist) {mios="unit:house,device:122/service/SecuritySensor1/Tripped"}
Contact  Bedroom3ZoneTripped "Bedroom #3 (Zone 9) [MAP(en.map):%s]" <contact>           (GWindow,GPersist) {mios="unit:house,device:123/service/SecuritySensor1/Tripped"}
Contact  Bedroom2ZoneTripped "Bedroom #2 (Zone 10) [MAP(en.map):%s]" <contact>          (GWindow,GPersist) {mios="unit:house,device:124/service/SecuritySensor1/Tripped"}
Contact  GuestBathZoneTripped "Guest Bathroom (Zone 11) [MAP(en.map):%s]" <contact>     (GWindow,GPersist) {mios="unit:house,device:125/service/SecuritySensor1/Tripped"}
Contact  StairsWindowsZoneTripped "Stairs Windows (Zone 12) [MAP(en.map):%s]" <contact> (GWindow,GPersist) {mios="unit:house,device:126/service/SecuritySensor1/Tripped"}
Contact  MasterBath1ZoneTripped "Master Bath (Zone 19) [MAP(en.map):%s]" <contact>      (GWindow,GPersist) {mios="unit:house,device:133/service/SecuritySensor1/Tripped"}
Contact  MasterBath2ZoneTripped "Master Bath (Zone 20) [MAP(en.map):%s]" <contact>      (GWindow,GPersist) {mios="unit:house,device:134/service/SecuritySensor1/Tripped"}
Contact  MasterBath3ZoneTripped "Master Bath (Zone 21) [MAP(en.map):%s]" <contact>      (GWindow,GPersist) {mios="unit:house,device:135/service/SecuritySensor1/Tripped"}
```

Rule declaration (house.rules):

```xtend
rule "Windows Closed (all)
when
	Item Bedroom2ZoneTripped changed from OPEN to CLOSED or
	Item Bedroom3ZoneTripped changed from OPEN to CLOSED or
	Item FamilyRoomZoneTripped changed from OPEN to CLOSED or
	Item GuestBathZoneTripped changed from OPEN to CLOSED or
	Item KitchenZoneTripped changed from OPEN to CLOSED or
	Item LivingRoomZoneTripped changed from OPEN to CLOSED or
	Item MasterBath1ZoneTripped changed from OPEN to CLOSED or
	Item MasterBath2ZoneTripped changed from OPEN to CLOSED or
	Item MasterBath3ZoneTripped changed from OPEN to CLOSED or
	Item MasterBedroomZoneTripped changed from OPEN to CLOSED or
	Item StairsWindowsZoneTripped changed from OPEN to CLOSED
then
	if (GWindow.members.filter(s|s.state==OPEN).size == 0) {
		say("Attention: All Windows closed.")
		sendCommand(Nest_away, "home")
	}
end

rule "Windows Opened (any)"
when
	Item Bedroom2ZoneTripped changed from CLOSED to OPEN or
	Item Bedroom3ZoneTripped changed from CLOSED to OPEN or
	Item FamilyRoomZoneTripped changed from CLOSED to OPEN or
	Item GuestBathZoneTripped changed from CLOSED to OPEN or
	Item KitchenZoneTripped changed from CLOSED to OPEN or
	Item LivingRoomZoneTripped changed from CLOSED to OPEN or
	Item MasterBath1ZoneTripped changed from CLOSED to OPEN or
	Item MasterBath2ZoneTripped changed from CLOSED to OPEN or
	Item MasterBath3ZoneTripped changed from CLOSED to OPEN or
	Item MasterBedroomZoneTripped changed from CLOSED to OPEN or
	Item StairsWindowsZoneTripped changed from CLOSED to OPEN
then
	if (GWindow.members.filter(s|s.state==OPEN).size == 1) {
		say("Attention: First Window opened.")
		sendCommand(Nest_away, "away")
	}
end
```

### Examples for Replacing

#### Publishing data to SmartEnergyGroups.com (SEG)

I wrote [[this script|http://forum.micasaverde.com/index.php/topic,31212.0.html]] to publish data from MiOS to [[SmartEnergyGroups (SEG)|http://smartenergygroups.com]] for analysis.

Here's what you do to replace it with openHAB functionality:

Item declaration (`house.items`):

```xtend
Group GPersist (All)

Group GMonitor (All)
Group GMonitorTemperature (GMonitor)
Group GMonitorHumidity (GMonitor)
Group GMonitorPower (GMonitor)
Group GMonitorEnergy (GMonitor)

Number   WeatherTemperatureCurrentTemperature "Outside [%.1f °F]" <temperature>          (GPersist,GMonitorTemperature) {mios="unit:house,device:318/service/TemperatureSensor1/CurrentTemperature"}
Number   WeatherLowTemperatureCurrentTemperature "Outside Low [%.1f °F]" <temperature>   (GPersist,GMonitorTemperature) {mios="unit:house,device:319/service/TemperatureSensor1/CurrentTemperature"}
Number   WeatherHighTemperatureCurrentTemperature "Outside High [%.1f °F]" <temperature> (GPersist,GMonitorTemperature) {mios="unit:house,device:320/service/TemperatureSensor1/CurrentTemperature"}
Number   WeatherHumidityCurrentLevel "Outside Humidity [%d %%]"                          (GPersist,GMonitorHumidity)    {mios="unit:house,device:321/service/HumiditySensor1/CurrentLevel"}
Number   NestTStatUpstairs_humidity "Humidity [%d %%]"                                   (GPersist,GMonitorHumidity)    {nest="<[thermostats(Upstairs).humidity]"}
Number   NestTStatUpstairs_ambient_temperature_f "Upstairs [%.1f °F]" <temperature>      (GPersist,GMonitorTemperature) {nest="<[thermostats(Upstairs).ambient_temperature_f]"}
Number   NestTStatDownstairs_humidity "Humidity [%d %%]"                                 (GPersist,GMonitorHumidity)    {nest="<[thermostats(Downstairs).humidity]"}
Number   NestTStatDownstairs_ambient_temperature_f "Downstairs [%.1f °F]" <temperature>  (GPersist,GMonitorTemperature) {nest="<[thermostats(Downstairs).ambient_temperature_f]"}
```

Persistence declaration (`rrd4j.persist`):

```xtend
Strategies {
	// for rrd charts, we need a cron strategy
	everyMinute : "0 * * * * ?"
	everyDay : "0 0 23 * * ?"
}

Items {
	SystemDataVersion, SystemUserDataDataVersion, SystemTimeStamp, SystemLocalTime, SystemLoadTime : strategy = everyDay
	GPersist* : strategy = everyChange, everyMinute, restoreOnStartup
	GTemperature* : strategy = everyMinute, restoreOnStartup
}
```

Rule declaration (`seg.rules`):

```xtend
import java.util.Locale

rule "Log Data to SmartEnergyGroups (SEG)"
when
	Time cron "0 0/2 * * * ?" or
	Item NestTStatUpstairs_ambient_temperature_f changed or
	Item NestTStatDownstairs_ambient_temperature_f changed or
	Item WeatherTemperatureCurrentTemperature changed or
	Item WeatherLowTemperatureCurrentTemperature changed or
	Item WeatherHighTemperatureCurrentTemperature changed or
	Item NestTStatUpstairs_humidity changed or
	Item NestTStatDownstairs_humidity changed or
	Item WeatherHumidityCurrentLevel changed
then
	val String SEG_SITE = "<yourSiteKeyHere>"
	val String SEG_URL = "http://api.smartenergygroups.com/api_sites/stream"
	val String NODE_NAME = "openHAB"
	val Locale LOCALE = Locale::getDefault

	var String segData = ""

	GMonitorTemperature?.members.forEach(item|
		segData = segData + String::format(LOCALE, "(t_%s %s)", item.name, (item.state as Number).toString)
	)

	GMonitorHumidity?.members.forEach(item |
		segData = segData + String::format(LOCALE, "(h_%s %s)", item.name, (item.state as Number).toString)
	)

	GMonitorPower?.members.forEach(item |
		segData = segData + String::format(LOCALE, "(p_%s %s)", item.name, (item.state as Number).toString)
	)

	GMonitorEnergy?.members.forEach(item |
		segData = segData + String::format(LOCALE, "(e_%s %s)", item.name, (item.state as Number).toString)
	)

	segData = String::format("(site %s (node %s ? %s))", SEG_SITE, NODE_NAME, segData)
	sendHttpPostRequest(SEG_URL, "application/x-www-form-urlencoded", segData)
end
```