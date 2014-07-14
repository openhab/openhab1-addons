# Releases

This code is not currently in release-ready form.  Changes are expected, especially as feedback is received.

## 1.6.0
  * TBD

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



## MiOS Item configuration

The MiOS Binding provides a few sources of data from the target MiOS Unit.  These can be categorized into the following data values:

* MiOS Device UPnP State Variables
* MiOS Device Attributes
* MiOS Scenes
* MiOS System Attributes

The examples below illustrates the form of each.


The general form of these bindings is:

    mios="unit:<unitName>,<miosThing>{,in:<inTransform>}{,out:<outTransform>}"

The sections below describe the types of things that can be bound, in addition to the transformations that are permitted.


### Item : MiOS Device Binding
Device Bindings are currently read-only, with data flowing from the MiOS Unit _into_ openHAB.  Device Bindings have the form:

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


The _serviceAliases_ are currently hard-coded.  The following list of aliases is built into the MiOS Binding.  Each is case-sensitive:

| _Core_ UPnP Service Id | Alias |
|----------------------|-------|
|`urn:upnp-org:serviceId:SwitchPower1`|`SwitchPower1`|
|`urn:upnp-org:serviceId:Dimming1`|`Dimming1`|
|`urn:upnp-org:serviceId:TemperatureSensor1`|`TemperatureSensor1`|
|`urn:upnp-org:serviceId:HVAC_FanOperatingMode1`|`HVAC_FanOperatingMode1`|
|`urn:upnp-org:serviceId:HVAC_UserOperatingMode1`|`HVAC_UserOperatingMode1`|
|`urn:upnp-org:serviceId:TemperatureSetpoint1_Heat`|`TemperatureSetpoint1_Heat`|
|`urn:upnp-org:serviceId:TemperatureSetpoint1_Cool`|`TemperatureSetpoint1_Cool`|
|`urn:upnp-org:serviceId:AVTransport`|`AVTransport`|
|`urn:upnp-org:serviceId:RenderingControl`|`RenderingControl`|
|`urn:upnp-org:serviceId:DeviceProperties`|`DeviceProperties`|
|`urn:upnp-org:serviceId:HouseStatus1`|`HouseStatus1`|
|`urn:upnp-org:serviceId:ContentDirectory`|`ContentDirectory`|
|`urn:upnp-org:serviceId:AudioIn`|`AudioIn`|



| _MiCasaVerde_ UPnP Service Id | Alias |
|-----------------------------|-------|
|`urn:micasaverde-com:serviceId:ZWaveDevice1`|`ZWaveDevice1`|
|`urn:micasaverde-com:serviceId:ZWaveNetwork1`|`ZWaveNetwork1`|
|`urn:micasaverde-com:serviceId:HaDevice1`|`HaDevice1`|
|`urn:micasaverde-com:serviceId:SceneControllerLED1`|`SceneControllerLED1`|
|`urn:micasaverde-com:serviceId:SecuritySensor1`|`SecuritySensor1`|
|`urn:micasaverde-com:serviceId:HumiditySensor1`|`HumiditySensor1`|
|`urn:micasaverde-com:serviceId:EnergyMetering1`|`EnergyMetering1`|
|`urn:micasaverde-com:serviceId:SceneController1`|`SceneController1`|
|`urn:micasaverde-com:serviceId:HVAC_OperatingState1`|`HVAC_OperatingState1`|
|`urn:micasaverde-org:serviceId:SerialPort1`|`SerialPort1`|
|`urn:micasaverde-com:serviceId:DoorLock1`|`DoorLock1`|
|`urn:micasaverde-com:serviceId:AlarmPartition2`|`AlarmPartition2`|
|`urn:micasaverde-com:serviceId:Camera1`|`Camera1`|



| _Plugin-specific_ UPnP Service Id | Alias |
|---------------------------------|-------|
|`urn:cd-jackson-com:serviceId:SystemMonitor`|`SystemMonitor`|
|`urn:garrettwp-com:serviceId:WPSwitch1`|`WPSwitch1`|
|`urn:watou-com:serviceId:Nest1`|`Nest1`|
|`urn:watou-com:serviceId:NestStructure1`|`NestStructure1`|
|`urn:upnp-micasaverde-com:serviceId:Weather1`|`Weather1`|
|`urn:demo-ted-striker:serviceId:PingSensor1`|`PingSensor1`|
|`urn:micasaverde-com:serviceId:Sonos1`|`Sonos1`|
|`urn:demo-paradox-com:serviceId:ParadoxSecurityEVO1`|`ParadoxSecurityEVO1`|
|`urn:macrho-com:serviceId:LiftMasterOpener1`|`LiftMasterOpener1`|
|`urn:directv-com:serviceId:DVR1`|`DirecTVDVR1`|


### Item : MiOS Scene Binding

Scene Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  Scene Bindings have the form:

    mios="unit:<unitName>,scene:<deviceId>/<attributeName>

With examples like:

    Number   SceneGarageOpenId         (BindingDemo) {mios="unit:house,scene:109/id"}
    Number   SceneGarageOpenStatus     (BindingDemo) {mios="unit:house,scene:109/status"}
    String   SceneGarageOpenActive     (BindingDemo) {mios="unit:house,scene:109/active"}
       
### Item : MiOS System Binding

System Bindings are read-only, with data flowing from the MiOS Unit _into_ openHAB.  System Bindings have the form:

    mios="unit:<unitName>,system:/<attributeName>

With examples like:

    Number   SystemZWaveStatus         "[%d]"  (BindingDemo) {mios="unit:house,system:/ZWaveStatus"}
    String   SystemLocalTime           "[%s]"  (BindingDemo) {mios="unit:house,system:/LocalTime"}
    String   SystemTimeStamp           "[%s]"  (BindingDemo) {mios="unit:house,system:/TimeStamp"}
    String   SystemUserDataDataVersion "[%s]"  (BindingDemo) {mios="unit:house,system:/UserData_DataVersion"}
    Number   SystemDataVersion         "[%d]"  (BindingDemo) {mios="unit:house,system:/DataVersion"} 
    String   SystemLoadTime            "[%s]"  (BindingDemo) {mios="unit:house,system:/LoadTime"} 
      
### Transformations

NOTE: Transformations are not yet enabled, they're only supported in the binding declaration (but then otherwise ignored).

Sometimes the value presented by the binding isn't in the format that you require for your Item.  For these cases, the binding provides access to the standard openHAB Transformation service.

To utilize the Transformation service, you need to declare additional settings on your bindings.

These take the form of the `in:` and `out:` declarations at the end of the binding.  The `in:` declaration is used when values are received from the MiOS Unit, but before it places the value into openHAB.  The `out:` declaration is used when values are taken from the openHAB system for delivered to the MiOS Unit (in _Command_ execution, for example).

    mios="unit:<unitName>,<miosThing>{,in:<inTransform>}{,out:<outTransform>}"

As you can see by the above declaration, the input and output transformations are optional.  If they aren't declared, then an internal, automated, transformation will be attempted based upon the Type of the Item being bound.

With examples like:

    String SystemZWaveStatusString "[%s]"        (BindingDemo) {mios="unit:house,system:/ZWaveStatus,in:MAP(miosZWaveStatus.map)"}
    Contact HallLightAsSwitch2     "On/Off [%s]" (BindingDemo) {mios="unit:house,device:11/service/SwitchPower1/Status,in:MAP(miosSwitch.map)"}

and a map transform file like `configurations/transform/miosZWaveStatus.map`:

    1=Cool Bananas
    0=In the Dog house
    -=Your guess is as good as mine!

and a map transform file like `configurations/transform/miosSwitch.map`:

    1=OPEN
    0=CLOSED
    
Then as data flows from the MiOS system, data for these items will be _transformed_ into the new [String] format for for display and/or rule purposes.  

The openHAB _Transformation Service_ provides a number of other transforms that may be of interest:

* `JS(example.js)` - run the Javascript to perform the conversion.
* `MAP (example.map)` - Transform using the static, file-based, conversion.
* `XSLT(example.xslt)` - Transform using an XSLT transformation.
* `EXEC(...)` - Transform using the OS-level script.
* `REGEX(<regularExpression>)` - Transform using the supplied Regular Expression and use Capture markers `(` and `)` around the value to be extracted.
* `XPATH(...)` - Transform using the supplied XPath Expression.