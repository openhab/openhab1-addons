Documentation for the MiOS Bridge Binding.

# Introduction
This binding exposes read, and read-command, access to Devices controlled by a MiOS Home Automation controller, such as those seen at http://getvera.com.

It exposes the ability to do the following things in the MiOS HA Controller

* `Devices` - Read State Variables & Device Attributes, and invoke (single parameter) UPnP Commands to control the Device.
* `Scenes` - Read the current execution state of a Scene, and invoke those Scenes within the remote HA Controller
* `System` - Read System-level Attributes.
It uses the remote control interfaces (aka "UI Simple" JSON Calls, and HTTP Long-polling) of the MiOS HA Controller to keep the _bound_ openHAB Items in sync with their counterparts in the MiOS HA Controller.

The binding uses the openHAB _Transformation Service_ extensively to "map" the Data & Commands between the two systems. A set of example MAP transform files is provided in the `examples/transform` directory of the Binding, but these can readily be augmented without needing to tweak the code.

Original code was used from the XBMC Binding, and then heavily modified. Snippets included from the HTTP Binding for the various datatype mapping functions.

# Releases

* 1.6 - First release
* 1.6.2 - #1889 Only change Item state during incremental updates from MiOS Unit, use UTF-8 for JSON responses for i18n.
* 1.6.2 - #1824 datetime handling made more automatic for MiOS style "epoch" dates.
* 1.6.2 - #1909 Add missing aliases for common UPnP ServiceId's.
* 1.7.0 - #???? Add MiOS Action Binding support for calling Device Actions and invoking Scenes.

# Configuration
 * [MiOS Unit configuration](MiOS-Binding#mios-unit-configuration)
 * [Transformations](MiOS-Binding#mios-transformations)
 * [Actions](MiOS-Binding#mios-action-addon)
 * [Logger](MiOS-Binding#mios-logger)
 * [Item configuration (Reading)](MiOS-Binding#mios-item-configuration)
    * [MiOS - Device Binding](MiOS-Binding#item--mios-device-binding---values-reading)
    * [MiOS - Scene Binding](MiOS-Binding#item--mios-scene-binding---values-reading)
    * [MiOS - System Binding](MiOS-Binding#item--mios-system-binding)
    * [Transformations (Use)](MiOS-Binding#transformations)
 * [Item Commands (Reacting)](MiOS-Binding#item-commands-reacting)
     * [MiOS - Device Binding - Commands (Reacting)] (MiOS-Binding#item--mios-device-binding---commands-reacting)
        * [Device Command Binding Examples (Parameterless)] (MiOS-Binding#device-command-binding-examples-parameterless)
            * [A Switch ...](MiOS-Binding#a-switch)
            * [An Armed Sensor ...](MiOS-Binding#an-armed-sensor)
            * [A Lock ...] (MiOS-Binding#a-lock)
        * [Device Command Binding Examples (Parameterized)](MiOS-Binding#device-command-binding-examples-parameterized)
            * [A Dimmer, Volume Control, Speed controlled Fan ...](MiOS-Binding#a-dimmer-volume-control-speed-controlled-fan)
            * [A Thermostat ...] (MiOS-Binding#a-thermostat)
     * [MiOS Scene Binding - Commands (Reacting)] (MiOS-Binding#item--mios-scene-binding---commands-reacting)
        * [Scene Command Binding Examples] (MiOS-Binding#scene-command-binding-examples)

***

## MiOS Unit configuration

In order for the MiOS openHAB Binding to talk to your MiOS Unit, it needs configuration indicating _where_ it lives.  This information is specified within the `openhab.cfg` file.

Each MiOS Unit is identified by a _Unit name_, which is user-supplied.  This name will be used throughout the subsequent setup steps, and permits you to connect to more than one MiOS Unit that you might have within your environment.


The binding will only talk you MiOS Units living on the same LAN as your MiOS Unit and/or are directly reachable from your LAN where openHAB is running.

The MiOS gateway services, such as `http://cp.mios.com` and `http://home.getvera.com`, are not supported.

Here is the simplest configuration.  It contains a Unit name, `house`, and a hostname, `192.168.1.22`, to use for the MiOS Unit connection.

    mios:house.host=192.168.1.22

Or if you have local DNS setup correctly, then the following works also:

    mios:house.host=ha.myhouse.example.com

Optionally, you can specify the `port` and `timeout` to use.  These default to `3480` and `60000` (ms) respectively.  These have reasonable defaults, so you shouldn't need to make adjustments.

    mios:house.host=ha.myhouse.example.com
    mios:house.port=3480
    mios:house.timeout=30000

You can also declare multiple MiOS Units, as illustrated in this example.

    mios:houseUpstairs.host=ha-upstairs.myhouse.example.com
    mios:houseDownstairs.host=ha-downstairs.myhouse.example.com

**NOTE**: The MiOS Unit name is case-sensitive, and may only contain AlphaNumeric characters.  The leading character must be an [ASCII] alpha.

[Back to Table of Contents](MiOS-Binding#configuration)

## MiOS Transformations

Internally, the MiOS Binding uses the openHAB _Transformation Service_.  The MiOS Binding supplies a number of pre-configured MAP Transformation for the common use-cases.

From a configuration standpoint, these transformations need to be copied from the source-code repository:

    bundles/binding/org.openhab.binding.mios/examples/transform/mios*.map
    
and placed into your openHAB installation under the directory:

    {openHAB Home}/configurations/transform/

**NOTE**: These transformations can be readily extended by the user, for any use-cases that aren't covered by those pre-configured & shipped with the Binding.

[Back to Table of Contents](MiOS-Binding#configuration)

## MiOS Action Addon

The MiOS Binding automatically synchronizes data between openHAB Items, and their bound Device Variables on the MiOS Unit.
For more advanced integration, the MiOS Action bundle can be installed.  This bundle extends openHAB's Rule language to include support for calling MiOS Device Actions, as well as invoking MiOS Scenes.
Installation of the MiOS Action bundle is optional and only required if your deployment needs the MiOS Rule language extensions.

[Back to Table of Contents](MiOS-Binding#configuration)

## MiOS Logger
Especially during setup of the binding the log information can provide you valuable information. Therefore it is recommended to configure logging to use a dedicated file for MiOS logging.

There are two configuration files to configure the log subsystem of openHAB:
 * {openHAB Home}/configurations/logback.xml
 * {openHAB Home}/configurations/logback_debug.xml (if you start in debug mode)

To simplify analysis and to keep things structured we'll use a dedicated logfile for this demo configuration.

```xml
    <!-- log appender to be used for MIOS binding -->
    <appender 
       name  = "MIOSFILE" 
       class = "ch.qos.logback.core.rolling.RollingFileAppender">
       <!-- target file -->
        <file>logs/mios.log</file>
        <!-- settings how to archive old logs and how long to retain them [days] -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/mios-%d{yyyy-ww}.log.zip</fileNamePattern>
            <maxHistory>7</maxHistory>
        </rollingPolicy>
        <!-- encoder rule (how to format the messages in the log file ) -->
        <encoder>
           <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%-30.30logger{36}] - %msg%n</pattern>
        </encoder>
    </appender>
```
Next we configure the actual logger:
```xml
    <!-- valid log levels: OFF | ERROR | INFO | DEBUG | TRACE -->
    <logger 
      name       = "org.openhab.binding.mios"
      level      = "TRACE" 
      additivity = "FALSE">
      <appender-ref ref="MIOSFILE"  />
    </logger>
```

Below how it should look if the configuration is correct (TRACE):
```
...
2015-01-18 16:37:18.971 [DEBUG] [.o.b.mios.internal.MiosBinding] - internalPropertyUpdate: BOUND {mios="unit:vera,device:1/service/urn:micasaverde-com:serviceId:ZWaveNetwork1/Role"}, value=Master SIS:NO PRI:YES, bound 1 time(s)
2015-01-18 16:37:18.971 [TRACE] [.o.b.mios.internal.MiosBinding] - internalPropertyUpdate: NOT BOUND {mios="unit:vera,device:1/service/urn:micasaverde-com:serviceId:ZWaveNetwork1/LastDongleBackup"}, value=2015-01-14T23:30:57
2015-01-18 16:37:18.971 [TRACE] [.o.b.mios.internal.MiosBinding] - internalPropertyUpdate: NOT BOUND {mios="unit:vera,device:1/service/urn:micasaverde-com:serviceId:ZWaveNetwork1/LastError"}, value=Poll failed
2015-01-18 16:37:18.971 [TRACE] [.o.b.mios.internal.MiosBinding] - internalPropertyUpdate: NOT BOUND {mios="unit:vera,device:1/service/urn:micasaverde-com:serviceId:ZWaveNetwork1/LastHeal"}, value=2015-01-14T05:16:26
2015-01-18 16:37:18.971 [TRACE] [.o.b.mios.internal.MiosBinding] - internalPropertyUpdate: NOT BOUND {mios="unit:vera,device:1/service/urn:micasaverde-com:serviceId:ZWaveNetwork1/LastRouteFailure"}, value=2015-01-14T04:11:33
...
```

[Back to Table of Contents](MiOS-Binding#configuration)

## MiOS Item configuration

The MiOS Binding provides a few sources of data from the target MiOS Unit.  These can be categorized into the following data values:

* MiOS Device UPnP State Variables
* MiOS Device Attributes
* MiOS Scene Attributes
* MiOS System Attributes

The examples below illustrates the form of each.


The general form of these bindings is:

    mios="unit:<unitName>,<miosThing>{,command:<commandTransform>}{,in:<inTransform>}{,out:<outTransform>}"

In many cases, only a subset of these parameters need to be specified/used, with *internal defaults* applied for the common use-cases.

The sections below describe the types of things that can be bound, in addition to the transformations that are permitted, and any default transformations that may be applied for you.

[Back to Table of Contents](MiOS-Binding#configuration)

### Item : MiOS Device Binding - Values (Reading)

Device Bindings can be read-only, with data flowing from the MiOS Unit _into_ openHAB.  Device Bindings have the form:

    mios="unit:<unitName>,device:<deviceId>/service/<serviceURN>/<serviceVariable>

or

    mios="unit:<unitName>,device:<deviceId>/service/<serviceAlias>/<serviceVariable>

With examples like:

    Number MiOSMemoryUsed         "Used [%.0f KB]"       (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryUsed"}
    Number MiOSMemoryAvailable    "Available [%.0f KB]"  (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryAvailable"}
    Number MiOSMemoryCached       "Cached [%.0f KB]"     (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryCached"}
    Number MiOSMemoryBuffers      "Buffers [%.0f KB]"    (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryBuffers"}
    String MiOSCMHLastRebootLinux "Reboot [%s]"          (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/cmhLastRebootTime"}
    String MiOSMemoryUsedString   "Memory Used [%s KB]"  (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryUsed"}
    
or, since we've internally Alias'd the UPnP _Service Id_ that Chris used, you can also use:

    Number MiOSMemoryUsed         "Used [%.0f KB]"       (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryUsed"}
    Number MiOSMemoryAvailable    "Available [%.0f KB]"  (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryAvailable"}
    Number MiOSMemoryCached       "Cached [%.0f KB]"     (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryCached"}
    Number MiOSMemoryBuffers      "Buffers [%.0f KB]"    (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryBuffers"}
    String MiOSCMHLastRebootLinux "Reboot [%s]"          (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/cmhLastRebootTime"}
    String MiOSMemoryUsedString   "Memory Used [%s KB]"  (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryUsed"}

Or you can replace the Weather information, from the openHAB `demo.items` file, with contents from the [Weather Underground](http://wunderground.com) (WUI) [Plugin](http://forum.micasaverde.com/index.php/board,42.0.html) from MiOS:

    Number Weather_Temperature        "Outside Temperature [%.1f °F]" <temperature> (Weather_Chart) {mios="unit:house,device:318/service/TemperatureSensor1/CurrentTemperature"}

or, you can track the status of a Light Switch or perhaps a Dimmer:

    Number HallLightAsSwitch      "On/Off [%1d]"         (BindingDemo) {mios="unit:house,device:11/service/SwitchPower1/Status"}
    Number HallLightAsDimmer      "Level [%3d]"          (BindingDemo) {mios="unit:house,device:11/service/Dimming1/LoadLevelStatus"}  


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
|`urn:upnp-org:serviceId:DigitalSecurityCameraSettings1`|`DigitalSecurityCameraSettings1`|
|`urn:upnp-org:serviceId:DigitalSecurityCameraStillImage1`|`DigitalSecurityCameraStillImage1`|
|`urn:upnp-org:serviceId:EnergyCalculator1`|`EnergyCalculator1`|
|`urn:upnp-org:serviceId:FanSpeed1`|`FanSpeed1`|



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
|`urn:micasaverde-com:serviceId:LightSensor1`|`LightSensor1`,`LightSensor`|
|`urn:micasaverde-com:serviceId:HomeAutomationGateway1`|`HomeAutomationGateway1`,`HAGateway`|
|`urn:micasaverde-com:serviceId:InsteonNetwork1`|`InsteonNetwork1`,`InsteonNetwork`|
|`urn:micasaverde-com:serviceId:InsteonDevice1`|`InsteonDevice1`,`InsteonDevice`|
|`urn:micasaverde-com:serviceId:USBUIRT1`|`USBUIRT1`,`USBUIRT`|
|`urn:micasaverde-com:serviceId:PanTiltZoom1`|`PanTiltZoom1`,`PTZ`|
|`urn:micasaverde-com:serviceId:WindowCovering1`|`WindowCovering1`,`WindowCovering`|
|`urn:micasaverde-com:serviceId:IrTransmitter1`|`IrTransmitter1`,`IrTransmitter`|
|`urn:micasaverde-com:serviceId:IrDevice1`|`IrDevice1`,`IrDevice`|
|`urn:micasaverde-com:serviceId:GenericIO`|`GenericIO`|
|`urn:micasaverde-com:serviceId:CameraMotionDetection1` |`CameraMotionDetection1`
|`urn:micasaverde-com:serviceId:DiscretePower1` |`DiscretePower1`
|`urn:micasaverde-com:serviceId:InputSelection1` |`InputSelection1`
|`urn:micasaverde-com:serviceId:Keypad1` |`Keypad1`
|`urn:micasaverde-com:serviceId:MediaNavigation1` |`MediaNavigation1`
|`urn:micasaverde-com:serviceId:MenuNavigation1` |`MenuNavigation1`
|`urn:micasaverde-com:serviceId:Misc1` |`Misc1`
|`urn:micasaverde-com:serviceId:NumericEntry1` |`NumericEntry1`
|`urn:micasaverde-com:serviceId:PIP1` |`PIP1`
|`urn:micasaverde-com:serviceId:Scene1` |`Scene1`
|`urn:micasaverde-com:serviceId:TV1` |`TV1`
|`urn:micasaverde-com:serviceId:TogglePower1` |`TogglePower1`
|`urn:micasaverde-com:serviceId:Tuning1` |`Tuning1`
|`urn:micasaverde-com:serviceId:VideoAdjustment1` |`VideoAdjustment1`
|`urn:micasaverde-com:serviceId:Volume1` |`Volume1`
|`urn:micasaverde-com:serviceId:WMC1` |`WMC1`



| _Plugin-specific_ UPnP Service Id | Aliases |
|---------------------------------|-------|
|`urn:cd-jackson-com:serviceId:SystemMonitor`|`SystemMonitor`|
|`urn:garrettwp-com:serviceId:WPSwitch1`|`WPSwitch1`,`WPSwitch`|
|`urn:watou-com:serviceId:Nest1`|`Nest1`,`Nest`|
|`urn:watou-com:serviceId:NestStructure1`|`NestStructure1`,`NestStructure`|
|`urn:upnp-micasaverde-com:serviceId:Weather1`|`Weather1`,`Weather`|
|`urn:demo-ted-striker:serviceId:PingSensor1`|`PingSensor1`,`PingSensor`|
|`urn:micasaverde-com:serviceId:Sonos1`|`Sonos1`,`Sonos`|
|`urn:demo-paradox-com:serviceId:ParadoxSecurityEVO1`|`ParadoxSecurityEVO1`,`Paradox`|
|`urn:macrho-com:serviceId:LiftMasterOpener1`|`LiftMasterOpener1`,`LiftMaster`|
|`urn:directv-com:serviceId:DVR1`|`DirecTVDVR1`,`DirecTV`|

[Back to Table of Contents](MiOS-Binding#configuration)

### Item : MiOS Scene Binding - Values (Reading)

Scene Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  Scene Bindings have the form:

    mios="unit:<unitName>,scene:<sceneId>/<attributeName>

With examples like:

    Number   SceneGarageOpenId         (GScene) {mios="unit:house,scene:109/id"}
    Number   SceneGarageOpenStatus     (GScene) {mios="unit:house,scene:109/status"}
    String   SceneGarageOpenActive     (GScene) {mios="unit:house,scene:109/active"}
 
[Back to Table of Contents](MiOS-Binding#configuration)
      
### Item : MiOS System Binding

System Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  System Bindings have the form:

    mios="unit:<unitName>,system:/<attributeName>

With examples like:

    Number   SystemZWaveStatus         "[%d]"  (GSystem) {mios="unit:house,system:/ZWaveStatus"}
    String   SystemLocalTime           "[%s]"  (GSystem) {mios="unit:house,system:/LocalTime"}
    String   SystemTimeStamp           "[%s]"  (GSystem) {mios="unit:house,system:/TimeStamp"}
    String   SystemUserDataDataVersion "[%s]"  (GSystem) {mios="unit:house,system:/UserData_DataVersion"}
    Number   SystemDataVersion         "[%d]"  (GSystem) {mios="unit:house,system:/DataVersion"} 
    String   SystemLoadTime            "[%s]"  (GSystem) {mios="unit:house,system:/LoadTime"} 
      
[Back to Table of Contents](MiOS-Binding#configuration)

### Transformations

Sometimes the value presented by the binding isn't in the format that you require for your Item.  For these cases, the binding provides access to the standard openHAB _Transformation Service_.

To utilize the _Transformation Service_, you need to declare additional settings on your bindings.

These take the form of the `in:` and `out:` declarations at the end of the binding.  The `in:` declaration is used when values are received from the MiOS Unit, but before it places the value into openHAB.  The `out:` declaration is used when values are taken from the openHAB system for delivered to the MiOS Unit (in _Command_ execution, for example).

    mios="unit:<unitName>,<miosThing>{,in:<inTransform>}{,out:<outTransform>}"

As you can see by the above declaration, the input and output transformations are optional.  If they aren't declared, then an internal, automated, transformation will be attempted based upon the Type of the Item being bound and, in some cases, the type of MiOS Attribute and/or State Variable involved in the binding.

With examples like:

    String   SystemZWaveStatusString "ZWave Status String [%d]" (GSystem) {mios="unit:house,system:/ZWaveStatus,in:MAP(miosZWaveStatusIn.map)"}
    Contact  LivingRoomZoneTripped "Living Room (Zone 2) [%s]" <contact> (GContact,GWindow,GPersist) {mios="unit:house,device:117/service/SecuritySensor1/Tripped,in:MAP(miosContactIn.map)"}

and a map transform file like `configurations/transform/miosZWaveStatusIn.map`:

    1=Cool Bananas
    0=In the Dog house
    -=Your guess is as good as mine!

and a map transform file like `configurations/transform/miosSwitchIn.map`:

    1=OPEN
    0=CLOSED
    
Then as data flows from the MiOS system, data for these items will be _transformed_ into the new [String] format for display and/or rule purposes.  

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

[Back to Table of Contents](MiOS-Binding#configuration)

## Item Commands (Reacting)

By default, openHAB will send Commands to the Controls that have been outlined in the associated `configurations/sitemaps/*.sitemap` file.  The Commands sent depend upon the type of Control that's been bound to the Item.

Through observation, the following commands are commonly sent:

* Switch - `ON`, `OFF` (When Bound to a _Switch_ Item)
* Switch - `TOGGLE` (When Bound to a _Contact_ Item)
* Switch - `ON` (When `autoupdate="false"` is also present in the binding list)
* Slider - `INCREASE`, `DECREASE`, _&lt;PCTNumber>_

MiOS Units don't natively handle these Commands so a mapping step must occur before openHAB Commands can be executed by a MiOS Unit.  Additionally, since MiOS Bindings are read-only by default, we must add a parameter to indicate we want data to flow back to the MiOS Unit.

The `command:` Binding parameter is used to specify that we want data to flow back to the MiOS unit as well as how to perform the required mapping.  For most Items bound using the MiOS Binding, internal defaults will take care of the correct `command:`, `in:` and `out:` parameters.  These need only be specified if you have something not handled by the internal defaults, or wish to override them with custom behavior.

[Back to Table of Contents](MiOS-Binding#configuration)

### Item : MiOS Device Binding - Commands (Reacting)

For MiOS Devices, this parameter can take one of several forms:

    mios="unit:<unitName>,device:<deviceId>/service/<UPnPVariable>,command:{<CommandMap>}"

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

[Back to Table of Contents](MiOS-Binding#configuration)

#### Device Command Binding Examples (Parameterless)

In practice, when discrete commands are being sent by openHAB, the map is fairly simple.  In the examples listed below, the `*.map` files are provided in the `examples/transform` directory of the MiOS binding.

[Back to Table of Contents](MiOS-Binding#configuration)

##### A Switch...

You might start off with an inline definition of the mapping:

    Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status,command:ON=SwitchPower1/SetTarget(newTargetValue=1)|OFF=SwitchPower1/SetTarget(newTargetValue=0),in:MAP(miosSwitchIn.map)"}

And then reduce it to the internal default map, but specify that you only want to handle `ON` and `OFF` Commands:

    Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status,command:ON|OFF,in:MAP(miosSwitchIn.map)"}

or, *more simply*, use the internal defaults altogether:

    Switch   FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status"}

[Back to Table of Contents](MiOS-Binding#configuration)

##### An Armed Sensor...

The simple version, using internal defaults for the `SecuritySensor1/Armed` service state of the Device:

    Switch   LivingRoomZoneArmed "Zone Armed [%s]" {mios="unit:house,device:117/service/SecuritySensor1/Armed"}

or the fully spelled out version:

    Switch   LivingRoomZoneArmed "Zone Armed [%s]" {mios="unit:house,device:117/service/SecuritySensor1/Armed,command:MAP(miosArmedCommand.map),in:MAP(miosSwitchIn.map)"}

[Back to Table of Contents](MiOS-Binding#configuration)

##### A Lock...

The simple version, using internal defaults for the `DoorLock1/Status` service state of the Device:

    Switch   GarageDeadboltDStatus "Garage Deadbolt" (GLock,GSwitch) {mios="unit:house,device:189/service/DoorLock1/Status"}

or the full version:

    Switch   GarageDeadboltDStatus "Garage Deadbolt" (GLock,GSwitch) {mios="unit:house,device:189/service/DoorLock1/Status,command:MAP(miosLockCommand.map),in:MAP(miosSwitchIn.map)"}

[Back to Table of Contents](MiOS-Binding#configuration)

#### Device Command Binding Examples (Parameterized)

For some Commands, in order to pass this information to the remote MiOS Unit, we need to know either the current value of the _Item_, or we need to know the current value of the _Command_.

To do this, we introduce the _&lt;BoundValue>_ parameter that, when present in the mapped-command, will be expanded prior to being sent to the MiOS Unit:

* `?++` - Item Value + 10
* `?--` - Item Value - 10
* `?` - Item Value
* `??` - Command Value

Additionally, since _&lt;PCTNumber>_ is just a value, it won't match any of the entries in our Mapping file, so we introduce a magic key `_defaultCommand`.  We first attempt to do a literal mapping and, if that doesn't find a match, we go look for this magic key and use it's entry.

[Back to Table of Contents](MiOS-Binding#configuration)

##### A Dimmer, Volume Control, Speed controlled Fan...

The simple version, using internal defaults for the `Dimming1/LoadLevelStatus` service state of the Device:

    Dimmer   MasterCeilingFanLoadLevelStatus "Master Ceiling Fan [%d]%" <slider> (GDimmer) {mios="unit:house,device:101/service/Dimming1/LoadLevelStatus"}

or the full version:

    Dimmer   MasterCeilingFanLoadLevelStatus "Master Ceiling Fan [%d]%" <slider> (GDimmer) {mios="unit:house,device:101/service/Dimming1/LoadLevelStatus,command:MAP(miosDimmerCommand.map)"}


Since Dimmer Items in openHAB can be sent `INCREASE`, `DECREASE` or _&lt;PCTNumber>_ as the command, the mapping file must account for both the static commands (`INCREASE`, `DECREASE`) as well as the possibility of a _Command Value_ being sent.

The `examples/transform/miosDimmerCommand.map` file has a definition that handles this situation:

    INCREASE=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=?++)
    DECREASE=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=?--)
    _defaultCommand=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=??)

[Back to Table of Contents](MiOS-Binding#configuration)


##### A Roller shutter...

The simple version, using internal defaults for the `WindowCovering1` service of the Device:

    Rollershutter Kitchen "Kitchen"	(GKitchen)  {mios="unit:micasa,device:13/service/WindowCovering1"}

or the full version:

    Rollershutter Kitchen "Kitchen"	(GKitchen)  {mios="unit:micasa,device:13/service/WindowCovering1,command:MAP(miosShutterCommand.map)"}

Since Rollershutter Items in openHAB can be sent `UP`, `DOWN`, `STOP` or _&lt;PCTNumber>_ as the command, the mapping file must account for both the static commands (`UP`, `DOWN`, `STOP`) as well as the possibility of a _Command Value_ being sent.

The `examples/transform/miosShutterCommand.map` file has a definition that handles this situation:

    DOWN=urn:upnp-org:serviceId:WindowCovering1/Down()
    UP=urn:upnp-org:serviceId:WindowCovering1/Up()
    STOP=urn:upnp-org:serviceId:WindowCovering1/Stop()
    _defaultCommand=urn:upnp-org:serviceId:Dimming1/SetLoadLevelTarget(newLoadlevelTarget=??)


##### A Thermostat...

A Thermostat is composed of a number of pieces.  Each piece must be first bound to openHAB Items, and then a number of mappings must be put in place.

Since all the components of a Thermostat have reasonable internal defaults, we'll use the simpler form for our Item definitions in openHAB:

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

and these need to be paired with similar items in the `*.sitemap` file:

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

[Back to Table of Contents](MiOS-Binding#configuration)

### Item : MiOS Scene Binding - Commands (Reacting)

MiOS Scenes are parameterless.  They can only be requested to execute, and they provide status updates as attribute values during their execution (`status`) or if they're currently active (`active`).

For MiOS Scenes, the `command:` parameter has a simpler form:

    mios="unit:<unitName>,scene:<sceneId>{/<SceneAttribute>},command:{<CommandMap>}{,in:<inTransform>}"

With definitions as:

_&lt;CommandList>_ is _&lt;blank>_ OR;<br>
_&lt;CommandList>_ is _&lt;openHABCommand>_ { `|` _&lt;openHABCommand>_ }*

_&lt;openHABCommand>_ is `ON`, `OFF`, `INCREASE`, `DECREASE`, `TOGGLE` etc

_&lt;SceneAttribute>_ is `status` | `active`

[Back to Table of Contents](MiOS-Binding#configuration)

#### Scene Command Binding Examples

In general Scenes tend to look like:

    String   SceneMasterClosetLights "Master Closet Lights Scene" <sofa> (GScene) {mios="unit:house,scene:109/status, autoupdate="false"}

Or if you want the Scene executed upon receipt of `ON` or `TOGGLE` Commands:

    String   SceneMasterClosetLights "Master Closet Lights Scene" <sofa> (GScene) {mios="unit:house,scene:109/status,command:ON|TOGGLE", autoupdate="false"}


**NOTE**: Here we've added an additional configuration to the binding declaration, `autoupdate="false"`, to ensure the Switch no longer has the `ON` and `OFF` States automatically managed.  In openHAB, this declaration ensures that the UI rendition appears like a Button.

[Back to Table of Contents](MiOS-Binding#configuration)
