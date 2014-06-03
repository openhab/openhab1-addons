## Introduction

This is a OpenHab binding for HAI/Leviton Omni and Lumina home automation controllers

The HAI/Leviton Omni is a popular home automation system in the US.  At Its core the Omni is a hardware board that provides security and access features.  It connects to many other devices through serial ports or relays and exposes them through a single TCP based API.

The binding is fairly complete and supports the following functionality.

* Auto item discovery and site map generation
  * if enabled, upon connect the binding will discover all devices on a system and print out to the log a complete items list as well as a crude site map file.
* Items
  * Security
    * Alarm status monitoring (burglary, fire, police, etc..)
    * Alarm mode activation (Off/Day/Night/Away/Vacation)
    * Door, Window, Fire and CO2 contacts
  * Lights
    * On, Off and Dimming commands
    * UPB and HLC rooms
    * UPB Scenes
  * Thermostats
    * Cool and heat set points
    * System modes (off, cool, heat, auto)
    * Fan modes
    * Celsius and Fahrenheit support 
    * Auxiliary temperature and humidity sensors.
  * Audio Controls
    * Track and Artist info
    * Key commands (play, pause, etc..)
    * source inputs 
  * Buttons/Macros
  * Supported Hardware (connected directly through a Omni or Lumina Panel)
    * Z-Wave (lighting and thermostats)
    * X10
    * Lightolier Compose PLC
    * UPB (lighting)
    * NuVo (AudioVideo)
    * Russound (AudioVideo)
    * HAI HiFI (AudioVideo)
    * Xantech (AudioVideo)
    * Speakercraft (AudioVideo)
    * Proficient(AudioVideo)
    * HAI Sensors and Thermostats

More information about the Leviton line of controllers can be found at their [site](http://www.leviton.com/OA_HTML/SectionDisplay.jsp?section=60577&minisite=10251)

## Installation 

Copy the binding jar (org.openhab.omnilink*.jar) to the addons directory

add the following to your openhab.cfg
```
#################################Omnilink##################################################
#
#Enter the port (4369) host ip or name and the two crypto keys for your omni panel.  The
#two keys may be found in the installer menu on a HAI keypad or touchscreen. Each key is
#16 hex characters in pairs separated by colons (aa:bb:cc)
#
#if generateItems is set to true then the binding will print all known items and a sample
#sitemap to the log file (INFO).  Useful when setting up for the first time. 
#
omnilink:port=4369
omnilink:host=panel.yourdomain.com
omnilink:key1=00:AA:BB:CC:DD:EE:FF:11
omnilink:key2=00:AA:BB:CC:DD:EE:FF:11
omnilink:generateItems=true
```

The two keys are hex characters separated  by colons, they can be found in the installer menu on your panel.

if generateItems is true then a items configuration and simple sitemap will be printed to the log.  This is useful for an initial setup, but adds a little time to the binding startup. 


## Items

if you want to manually add a item, the following types are supported:

format is {omnilink:"type:number"}

some types can be read (get) or read and set (get/set)

* unit (get/set)
  * Dimmer
  * Switch
  * String (will display on,off,level or scene, can be sent a scene command like 'scene a')
* thermo_heat_point (get/set)
  * Number
* thermo_cool_point (get/set)
  * Number
* thermo_system_mode (get/set)
  * Number ([0"=Off, "1"=Heat, "2"=Cool,"3"=Auto,"4"=Emergency)
* thermo_fan_mode (get/set)
  * Number ("0"=Auto, "1"=On, "2"=Cycle)
  * Switch
* thermo_hold_mode (get/set)
  * Number ("0"=Off, "1"=On)
  * Switch
* thermo_temp (get)
  * Number
* zone_status_current (get)
  * Contact
* zone_status_latched (get)
  * String
* zone_status_arming (get)
  * String
* zone_status_all (get)
  * String
* area_status_mode (get/set)
  * String ( "Day"="Day","Night"="Night", "Away"="Away","Vacation"="Vacation")
* area_status_alarm (get)
  * String
* area_status_exit_delay (get)
  * Number
* area_status_entry_delay (get)
  * Number
* area_status_exit_timer (get)
  * Number
* area_status_entry_timer (get)
  * Number
* aux_status (get)
  * Number
* aux_current (get)
  * Number
* aux_low (get)
  * Number
* aux_hi (get)
  * Number
* audiozone_power (get/set)
  * Switch
  * Number
* audiozone_source (get/set)
  * Number
* audiozone_volume (get/set)
  * Number
* audiozone_mute (get/set)
  * Switch
  * Number
* audiozone_key (get/set)
  * Number
* audiozone_text (get)
  * String
* audiozone_field1 (get)
  * String
* audiozone_field2 (get)
  * String
* audiozone_field3 (get)
  * String
* audiosource_text (get)
  * String
* audiosource_field1 (get)
  * String
* audiosource_field2 (get)
  * String
* audiosource_field3 (get)
  * String
* button (set)
  * String (send any non empty string to push)
  
## Item Examples

Dimmer for unit 2:

`Dimmer  Lights_Kitchen_Lights_Switch  "Lights [%d%%]" (Lights_Kitchen)  {omnilink="unit:2"}`

Thermostat 1:

```
Number  Thermostats_MasterBed_Temp  "Temperature [%d °F]" (Thermostats_MasterBed) {omnilink="thermo_temp:1"}
Number  Thermostats_MasterBed_CoolPoint "Cool Point [%d°F]" (Thermostats_MasterBed) {omnilink="thermo_cool_point:1"}
Number  Thermostats_MasterBed_HeatPoint "Heat Point [%d°F]" (Thermostats_MasterBed) {omnilink="thermo_heat_point:1"}
Number  Thermostats_MasterBed_System  "System Mode [%d]"  (Thermostats_MasterBed) {omnilink="thermo_system_mode:1"}
Number  Thermostats_MasterBed_Fan "System Fan [%d]" (Thermostats_MasterBed) {omnilink="thermo_fan_mode:1"}
Number  Thermostats_MasterBed_Hold  "System Hold [%d]"  (Thermostats_MasterBed) {omnilink="thermo_hold_mode:1"}
```

Audio Zone 1:

```
Switch  AudioZones_Basement_Power "Power" (AudioZones_Basement) {omnilink="audiozone_power:1"}
Switch  AudioZones_Basement_Mute  "Mute"  (AudioZones_Basement) {omnilink="audiozone_mute:1"}
Number  AudioZones_Basement_Source  "Source: [%d]"  (AudioZones_Basement) {omnilink="audiozone_source:1"}
Dimmer  AudioZones_Basement_Volume  "Voulme: [%d %%]" (AudioZones_Basement) {omnilink="audiozone_volume:1"}
String  AudioZones_Basement_Text  "Now Playing: [%s]" (AudioZones_Basement) {omnilink="audiozone_text:1"}
String  AudioZones_Basement_Field1  "Field 1 [%s]"  (AudioZones_Basement) {omnilink="audiozone_field1:1"}
String  AudioZones_Basement_Field2  "Field 2 [%s]"  (AudioZones_Basement) {omnilink="audiozone_field2:1"}
String  AudioZones_Basement_Field3  "Field 3 [%s]"  (AudioZones_Basement) {omnilink="audiozone_field3:1"}
Number  AudioZones_Basement_Key "Key [%d]"  (AudioZones_Basement) {omnilink="audiozone_key:1",autoupdate="false"}
```

Audio Source 1:

```
String  AudioSources_XM_Text  "Now Playeing: [%s]"  (AudioSources_XM) {omnilink="audiosource_text:1"}
String  AudioSources_XM_Field1  "Field 1 [%s]"  (AudioSources_XM) {omnilink="audiosource_field1:1"}
String  AudioSources_XM_Field2  "Field 2 [%s]"  (AudioSources_XM) {omnilink="audiosource_field2:1"}
String  AudioSources_XM_Field3  "Field 3 [%s]"  (AudioSources_XM) {omnilink="audiosource_field3:1"}
```

Area (security) 1

```
Number  Areas_Main_ExitDelay  "Exit Delay: [%d]"  (Areas_Main)  {omnilink="area_status_exit_delay:1"}
Number  Areas_Main_EntryDelay "Exit Delay: [%d]"  (Areas_Main)  {omnilink="area_status_entry_delay:1"}
Number  Areas_Main_ExitTimer  "Exit Delay: [%d]"  (Areas_Main)  {omnilink="area_status_exit_timer:1"}
Number  Areas_Main_EntryTimer "Exit Delay: [%d]"  (Areas_Main)  {omnilink="area_status_entry_timer:1"}
String  Areas_Main_Mode "Mode: [%s]"  (Areas_Main)  {omnilink="area_status_mode:1"}
String  Areas_Main_Alarm  "Alarm: [%s]" (Areas_Main)  {omnilink="area_status_alarm:1"}
```
Contact zone number 17

```
Contact Zones_FrontDoor_Current "Current: [%s]" (Zones_FrontDoor) {omnilink="zone_status_current:17"}
String  Zones_FrontDoor_Latched "Latched [%s]"  (Zones_FrontDoor) {omnilink="zone_status_latched:17"}
String  Zones_FrontDoor_Arming  "Arming [%s]" (Zones_FrontDoor) {omnilink="zone_status_arming:17"}
String  Zones_FrontDoor_All "Status  [%s]"  (Zones_FrontDoor) {omnilink="zone_status_all:17"}
```

Button 1

`String Buttons_MusicOn "Music On"  (Buttons) {omnilink="button:1",autoupdate="false"}`

