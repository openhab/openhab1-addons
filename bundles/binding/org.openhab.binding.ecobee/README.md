# Ecobee Binding

Ecobee Inc. of Toronto, Canada, sells a range of Wi-Fi enabled thermostats, principally in the Americas.  The EMS, EMS Si, Smart, Smart Si and ecobee3 models are supported by this binding, which communicates with the [Ecobee API](https://www.ecobee.com/home/developer/api/documentation/v1/index.shtml) over a secure, RESTful API to Ecobee's servers. Monitoring ambient temperature and humidity, changing HVAC mode, changing heat or cool setpoints, changing the backlight intensity, and even sending textual messages to one or a group of thermostats, can be accomplished through this binding.


## Table of Contents

<!-- MarkdownTOC depth=2 -->

- [Prerequisites](#prerequisites)
- [Binding Configuration](#binding-configuration)
	- [Example ecobee.cfg](#example-ecobeecfg)
	- [Multiple Connections](#multiple-connections)
- [Item Configuration](#item-configuration)
	- [ecobee3 Remote Sensors](#ecobee3-remote-sensors)
- [Authorization](#authorization)
	- [Troubleshooting Authorization](#troubleshooting-authorization)
- [Example Item Configurations](#example-item-configurations)
- [Examples](#examples)
	- [Item Examples](#item-examples)
	- [Basic configuration](#basic-configuration)
	- [Tracking last occupancy](#tracking-last-occupancy)
	- [MAP Transformations](#map-transformations)
- [Notes](#notes)

<!-- /MarkdownTOC -->

## Prerequisites

In order to use this binding, you must have already registered your thermostat(s) with Ecobee, registered a new app as a [developer](https://www.ecobee.com/developers/), and then login to your [web portal](https://www.ecobee.com/).

![app](https://watou.github.io/images/ecobee-pin.jpg)

## Binding Configuration

This binding can be configured in the file `services/ecobee.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| appkey   |         |   Yes    | You must generate your own `appkey` on the Ecobee developer dashboard (see [Prerequisites](#prerequesities)) |
| scope    |         |   Yes    | Typically, `scope` will be set to `smartWrite`, but if you have an EMS thermostat, set `scope` to `ems`. |
| refresh  | 180000  |   No     | Data refresh interval in milliseconds |
| quickpoll | 6000   |   No     | Time in milliseconds to wait after successful update, command or action before refresh |
| timeout  | 20000   |   No     | Time in milliseconds to allow an API request to complete |
| tempscale | F      |   No     | temperature scale to use when sending or receiving temperatures.  Can be `C` or `F` |

### Example ecobee.cfg

```
# the private API key issued be Ecobee to use the API (required, replace with your own)
#appkey=9T4huoUXlT5b5qNpEJvM5sqTMgaNCFoV

# the application scope used when authorizing the binding
# choices are smartWrite,smartRead, or ems, or multiple (required, comma-separated, no spaces)
#scope=smartWrite

# Rate at which to check if poll is to run, in ms (as of 1.8, optional, defaults to 5000)
#granularity=5000

# Data refresh interval in ms (optional, defaults to 180000)
#refresh=180000

# Time in ms to wait after successful update, command or action before refresh (as of 1.8, optional, defaults to 6000)
#quickpoll=6000

# Time in ms to allow an API request to complete (as of 1.8, optional, defaults to 20000)
#timeout=20000

# the temperature scale to use when sending or receiving temperatures
# optional, defaults to Fahrenheit (F)
#tempscale=C
```

### Multiple Connections

You can set up multiple, distinct API connections by repeating the `appkey` and `scope` settings with a prepended "user ID" that indicates a separate ecobee.com account will be used to complete authorization.

```
condo.appkey=T5vMsUXlpEsqT4huoJb5qN9TMgaNCFoV
condo.scope=smartRead
```

You would then include `condo.` in item references (see below) for those thermostats available from the "condo" account.

## Item Configuration

The syntax for the Ecobee binding configuration string is explained below.

Ecobee bindings start with a `<`, `>` or `=`, to indicate if the item receives values from the API (in binding), sends values to the API (out binding), or both (bidirectional binding), respectively.

The first character is then followed by a section between square brackets (`[` and `]` characters):

```
[<thermostat>#<property>]
```

Where `<thermostat>` is a decimal thermostat identifier for in (`<`), out (`>`) and bidirectional (`=`) bindings.

> *Where can I find my thermostat identifier?*
> A thermostat identifier is a long, decimal number.  For ecobee3 users, one way to find the number is to login to your Ecobee portal and read the URL you were taken to in the browser's address bar: `https://www.ecobee.com/consumerportal/index.html#/thermostats/318973256526`
> The final component of the URL is your thermostat identifier.
> For non-ecobee3 users who are using the older web portal, you can go to the Home IQ&trade; tab and choose Download Data from the Report chart.  The thermostat identifier can be found in the report.

For out (`>`) bindings only, `<thermostat>` can instead be selection criteria that specify which thermostats to change. You can use either a comma-separated list of thermostat identifiers (no spaces), or, for non-EMS thermostats only, a wildcard (the `*` character).

In the case of out bindings for EMS or Utility accounts, the `<thermostat>` criteria can be a path to a management set (for example, `/Toronto/Campus/BuildingA`).  Please note that management set path elements that contain the `.` or `#` characters cannot be specified.

The `<thermostat>` specification can be optionally prepended with a specific "user ID" as specified in the binding configuration, as in `condo.123456789` when you have specified `condo.scope` and `condo.appkey` binding configuration properties.

`<property>` is one of a long list of thermostat properties than you can read and optionally change. See the list below, and peruse this binding's JavaDoc for all specifics as to their meanings.

Property | In | Out | Type
---------|----|-----|-----
name     | X  | X   | StringType
runtime.actualTemperature | X | | DecimalType
runtime.actualHumidity | X | | DecimalType
settings.hvacMode | X | X | StringType

Please see a complete list [below](#examples).

### ecobee3 Remote Sensors

If you have an ecobee3 thermostat with remote, wireless temperature/occupancy sensors, the binding can read their values.  Format:

```
ecobee="<[<thermostat_id>#remoteSensors(<sensor_name>).capability(<capability>).value]"
```

Supply the long, decimal thermostat ID as in other in-binding configurations.  The binding configuration string uses the simple name you gave the remote sensor for `<sensor_name>`.  The characters `(`,`)`,`[`,`]`, and `.` are not permitted in sensor names when used with the binding.

The ecobee3 sensors are known to report temperature or occupancy, and so you would specify either `temperature` or `occupancy` as the `<capability>` you wish to retrieve.  `temperature` returns a `DecimalType` temperature in the binding's chosen temperature scale, and `occupancy` returns an `OnOffType` value as you would use in a switch or rule.  If either value is unavailable for some reason, such as loss of connectivity, the binding returns an `UnDefType.NULL` value.

> Note that `occupancy` is computed (by Ecobee, not the binding) to mean presence within the last 30 minutes, not current occupancy.

```
Number KitchenTemp "Kitchen temperature [%.1f °F]"   { ecobee="<[123456789#remoteSensors(Kitchen).capability(temperature).value]" }
Number BasementTemp "Basement temperature [%.1f °F]" { ecobee="<[123456789#remoteSensors(Basement).capability(temperature).value]" }
Switch BedroomOccu "Bedroom occupancy [%s]"          { ecobee="<[123456789#remoteSensors(Bedroom).capability(occupancy).value]" }
```

See the Example Binding Strings section below for more examples.

## Authorization

After you have installed and configured the binding, added items to your `.items file` and started openHAB (if not previously started), when the binding performs its first poll, it will discover that is has not yet been authorized by the Ecobee servers, and will retrieve a four-character PIN from the Ecobee server.  This PIN will appear prominently in your `openhab.log` file:

	#########################################################################################
	# Ecobee-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!
	# 1. Login to www.ecobee.com using your 'DEFAULT_USER' account
	# 2. Enter the PIN 'gxvg' in My Apps within the next 9 minutes.
	# NOTE: Any API attempts will fail in the meantime.
	#########################################################################################

When it does, enter it into your Apps settings in your account at ecobee.com.  This will authorize your instance of the binding to work with your Ecobee account.  On the next poll of the API, it will retrieve access and refresh tokens and continue.

### Troubleshooting Authorization

Setting the binding's logger `org.openhab.binding.ecobee` to DEBUG or TRACE level will help you diagnose any issues.

If you happen to miss the time window to register your PIN, the ecobee.com website will still accept the PIN but openHAB will be unable to get authorization. If this happens the following may help:

* Stop the openHAB server
* Delete the Java Preferences storage where the tokens are kept.
    * On Linux, this is done by deleting the specific obfuscated directory name under ~/.java/.userPrefs. If you only have one garbled directory name there, you can just rm -rf ~/.java/.userPrefs (so as to not delete something else's storage).
    * On Windows, the Java Preferences are kept in the Windows Registry.
    * On macOS, the Java preferences are stored in the file ~/Library/Preferences/com.apple.java.util.prefs.plist *of the user running OpenHAB.* You may need to reboot your Mac in order to recreate the preferences file.
* Restart the openHAB server
* Register a new PIN like above

## Example Item Configurations

Return or set the name of the thermostat whose ID is 123456789 using the default
Ecobee app instance (configured in [binding configuration](#binding-configuration)):

```
ecobee="=[123456789#name]"
```

Return the current temperature read by the thermostat using the condo account
at ecobee.com:

```
ecobee="<[condo.987654321#runtime.actualTemperature]"
```

Return or set the minimum number of minutes per hour the fan will run on thermostat ID
543212345:

```
ecobee="=[543212345#settings.fanMinOnTime]"
```

Change the HVAC mode to one of `auto`, `auxHeatOnly`, `cool`, `heat`, or
`off` on all thermostats registered in the default app instance:

```
ecobee=">[*#settings.hvacMode]"
```

Changes the backlight sleep intensity on all thermostats at the lake house
(meaning, all thermostats registered to the lakehouse Ecobee account):

```
ecobee=">[lakehouse.*#settings.backlightSleepIntensity]"
```

Determine if there was any occupancy in the condo's kitchen within the last 30 minutes:

```
ecobee="<[condo.987654321#remoteSensors(Kitchen).capability(occupancy).value]"
```

## Examples

### Item Examples

Here are some examples of valid item binding strings, as you would define in your `.items` file.  Each item binding indicates if it is an in-only binding or bidirectional binding string in the examples below.

```
/* Ecobee binding items (replace 123456789012 with your thermostat ID) */

String identifier       "identifier [%s]"                                      { ecobee="<[123456789012#identifier]" }
String name             "name [%s]"                                            { ecobee="=[123456789012#name]" }
String thermostatRev    "thermostatRev [%s]"                                   { ecobee="<[123456789012#thermostatRev]" }
String isRegistered     "isRegistered [%s]"                                    { ecobee="<[123456789012#isRegistered]" }
String modelNumber      "modelNumber [%s]"                                     { ecobee="<[123456789012#modelNumber]" }
String brand            "brand [%s]"                                           { ecobee="<[123456789012#brand]" } // as of openHAB 1.8
String features         "features [%s]"                                        { ecobee="<[123456789012#features]" } // as of openHAB 1.8
DateTime lastModified   "lastModified [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]"   { ecobee="<[123456789012#lastModified]" }
DateTime thermostatTime "thermostatTime [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" { ecobee="<[123456789012#thermostatTime]" }
DateTime utcTime        "utcTime [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]"        { ecobee="<[123456789012#utcTime]" }
String equipmentStatus  "equipmentStatus [%s]"                                 { ecobee="<[123456789012#equipmentStatus]" }
String version_thermostatFirmwareVersion "thermostatFirmwareVersion [%s]"      { ecobee="<[123456789012#version.thermostatFirmwareVersion]" }
String program_currentClimateRef "currentClimateRef [%s]"                      { ecobee="<[123456789012#program.currentClimateRef]" }

Group All
Group gSettings (All)

String settings_hvacMode "hvacMode [%s]"                                       (gSettings) { ecobee="=[123456789012#settings.hvacMode]" }
String settings_lastServiceDate "lastServiceDate [%s]"                         (gSettings) { ecobee="=[123456789012#settings.lastServiceDate]" }
Switch settings_serviceRemindMe "serviceRemindMe [%s]"                         (gSettings) { ecobee="=[123456789012#settings.serviceRemindMe]" }
Number settings_monthsBetweenService "monthsBetweenService [%d]"               (gSettings) { ecobee="=[123456789012#settings.monthsBetweenService]" }
String settings_remindMeDate "remindMeDate [%s]"                               (gSettings) { ecobee="=[123456789012#settings.remindMeDate]" }
String settings_vent "vent [%s]"                                               (gSettings) { ecobee="=[123456789012#settings.vent]" }
Number settings_ventilatorMinOnTime "ventilatorMinOnTime [%d]"                 (gSettings) { ecobee="=[123456789012#settings.ventilatorMinOnTime]" }
Switch settings_serviceRemindTechnician "serviceRemindTechnician [%s]"         (gSettings) { ecobee="=[123456789012#settings.serviceRemindTechnician]" }
String settings_eiLocation "eiLocation [%s]"                                   (gSettings) { ecobee="=[123456789012#settings.eiLocation]" }
Number settings_coldTempAlert "coldTempAlert [%.1f °F]"                        (gSettings) { ecobee="=[123456789012#settings.coldTempAlert]" }
Switch settings_coldTempAlertEnabled "coldTempAlertEnabled [%s]"               (gSettings) { ecobee="=[123456789012#settings.coldTempAlertEnabled]" }
Number settings_hotTempAlert "hotTempAlert [%.1f °F]"                          (gSettings) { ecobee="=[123456789012#settings.hotTempAlert]" }
Switch settings_hotTempAlertEnabled "hotTempAlertEnabled [%s]"                 (gSettings) { ecobee="=[123456789012#settings.hotTempAlertEnabled]" }
Number settings_coolStages "coolStages [%d]"                                   (gSettings) { ecobee="<[123456789012#settings.coolStages]" }
Number settings_heatStages "heatStages [%d]"                                   (gSettings) { ecobee="<[123456789012#settings.heatStages]" }
Number settings_maxSetBack "maxSetBack [%.1f °F]"                              (gSettings) { ecobee="=[123456789012#settings.maxSetBack]" }
Number settings_maxSetForward "maxSetForward [%.1f °F]"                        (gSettings) { ecobee="=[123456789012#settings.maxSetForward]" }
Number settings_quickSaveSetBack "quickSaveSetBack [%.1f °F]"                  (gSettings) { ecobee="=[123456789012#settings.quickSaveSetBack]" }
Number settings_quickSaveSetForward "quickSaveSetForward [%.1f °F]"            (gSettings) { ecobee="=[123456789012#settings.quickSaveSetForward]" }
Switch settings_hasHeatPump "hasHeatPump [%s]"                                 (gSettings) { ecobee="<[123456789012#settings.hasHeatPump]" }
Switch settings_hasForcedAir "hasForcedAir [%s]"                               (gSettings) { ecobee="<[123456789012#settings.hasForcedAir]" }
Switch settings_hasBoiler "hasBoiler [%s]"                                     (gSettings) { ecobee="<[123456789012#settings.hasBoiler]" }
Switch settings_hasHumidifier "hasHumidifier [%s]"                             (gSettings) { ecobee="<[123456789012#settings.hasHumidifier]" }
Switch settings_hasErv "hasErv [%s]"                                           (gSettings) { ecobee="<[123456789012#settings.hasErv]" }
Switch settings_hasHrv "hasHrv [%s]"                                           (gSettings) { ecobee="<[123456789012#settings.hasHrv]" }
Switch settings_condensationAvoid "condensationAvoid [%s]"                     (gSettings) { ecobee="=[123456789012#settings.condensationAvoid]" }
Switch settings_useCelsius "useCelsius [%s]"                                   (gSettings) { ecobee="=[123456789012#settings.useCelsius]" }
Switch settings_useTimeFormat12 "useTimeFormat12 [%s]"                         (gSettings) { ecobee="=[123456789012#settings.useTimeFormat12]" }
String settings_locale "locale [%s]"                                           (gSettings) { ecobee="=[123456789012#settings.locale]" }
String settings_humidity "humidity [%s]"                                       (gSettings) { ecobee="=[123456789012#settings.humidity]" }
String settings_humidifierMode "humidifierMode [%s]"                           (gSettings) { ecobee="=[123456789012#settings.humidifierMode]" }
Number settings_backlightOnIntensity "backlightOnIntensity [%d]"               (gSettings) { ecobee="=[123456789012#settings.backlightOnIntensity]" }
Number settings_backlightSleepIntensity "backlightSleepIntensity [%d]"         (gSettings) { ecobee="=[123456789012#settings.backlightSleepIntensity]" }
Number settings_backlightOffTime "backlightOffTime [%d]"                       (gSettings) { ecobee="=[123456789012#settings.backlightOffTime]" }
Number settings_soundTickVolume "soundTickVolume [%d]"                         (gSettings) { ecobee="=[123456789012#settings.soundTickVolume]" }
Number settings_soundAlertVolume "soundAlertVolume [%d]"                       (gSettings) { ecobee="=[123456789012#settings.soundAlertVolume]" }
Number settings_compressorProtectionMinTime "compressorProtectionMinTime [%d]" (gSettings) { ecobee="=[123456789012#settings.compressorProtectionMinTime]" }
Number settings_compressorProtectionMinTemp "compressorProtectionMinTemp [%.1f °F]" (gSettings) { ecobee="=[123456789012#settings.compressorProtectionMinTemp]" }
Number settings_stage1HeatingDifferentialTemp "stage1HeatingDifferentialTemp [%.1f °F]" (gSettings) { ecobee="=[123456789012#settings.stage1HeatingDifferentialTemp]" }
Number settings_stage1CoolingDifferentialTemp "stage1CoolingDifferentialTemp [%.1f °F]" (gSettings) { ecobee="=[123456789012#settings.stage1CoolingDifferentialTemp]" }
Number settings_stage1HeatingDissipationTime "stage1HeatingDissipationTime [%d]" (gSettings) { ecobee="=[123456789012#settings.stage1HeatingDissipationTime]" }
Number settings_stage1CoolingDissipationTime "stage1CoolingDissipationTime [%d]" (gSettings) { ecobee="=[123456789012#settings.stage1CoolingDissipationTime]" }
Switch settings_heatPumpReversalOnCool "heatPumpReversalOnCool [%s]" (gSettings) { ecobee="=[123456789012#settings.heatPumpReversalOnCool]" }
Switch settings_fanControlRequired "fanControlRequired [%s]"                   (gSettings) { ecobee="=[123456789012#settings.fanControlRequired]" }
Number settings_fanMinOnTime "fanMinOnTime [%d]"                               (gSettings) { ecobee="=[123456789012#settings.fanMinOnTime]" }
Number settings_heatCoolMinDelta "heatCoolMinDelta [%.1f °F]"                  (gSettings) { ecobee="=[123456789012#settings.heatCoolMinDelta]" }
Number settings_tempCorrection "tempCorrection [%.1f °F]"                      (gSettings) { ecobee="=[123456789012#settings.tempCorrection]" }
String settings_holdAction "holdAction [%s]"                                   (gSettings) { ecobee="=[123456789012#settings.holdAction]" }
Switch settings_heatPumpGroundWater "heatPumpGroundWater [%s]"                 (gSettings) { ecobee="<[123456789012#settings.heatPumpGroundWater]" }
Switch settings_hasElectric "hasElectric [%s]"                                 (gSettings) { ecobee="<[123456789012#settings.hasElectric]" }
Switch settings_hasDehumidifier "hasDehumidifier [%s]"                         (gSettings) { ecobee="<[123456789012#settings.hasDehumidifier]" }
String settings_dehumidifierMode "dehumidifierMode [%s]"                       (gSettings) { ecobee="=[123456789012#settings.dehumidifierMode]" }
Number settings_dehumidifierLevel "dehumidifierLevel [%d]"                     (gSettings) { ecobee="=[123456789012#settings.dehumidifierLevel]" }
Switch settings_dehumidifyWithAC "dehumidifyWithAC [%s]"                       (gSettings) { ecobee="=[123456789012#settings.dehumidifyWithAC]" }
Number settings_dehumidifyOvercoolOffset "dehumidifyOvercoolOffset [%d]"       (gSettings) { ecobee="=[123456789012#settings.dehumidifyOvercoolOffset]" }
Switch settings_autoHeatCoolFeatureEnabled "autoHeatCoolFeatureEnabled [%s]"   (gSettings) { ecobee="=[123456789012#settings.autoHeatCoolFeatureEnabled]" }
Switch settings_wifiOfflineAlert "wifiOfflineAlert [%s]"                       (gSettings) { ecobee="=[123456789012#settings.wifiOfflineAlert]" }
Number settings_heatMinTemp "heatMinTemp [%.1f °F]"                            (gSettings) { ecobee="<[123456789012#settings.heatMinTemp]" }
Number settings_heatMaxTemp "heatMaxTemp [%.1f °F]"                            (gSettings) { ecobee="<[123456789012#settings.heatMaxTemp]" }
Number settings_coolMinTemp "coolMinTemp [%.1f °F]"                            (gSettings) { ecobee="<[123456789012#settings.coolMinTemp]" }
Number settings_coolMaxTemp "coolMaxTemp [%.1f °F]"                            (gSettings) { ecobee="<[123456789012#settings.coolMaxTemp]" }
Number settings_heatRangeHigh "heatRangeHigh [%.1f °F]"                        (gSettings) { ecobee="=[123456789012#settings.heatRangeHigh]" }
Number settings_heatRangeLow "heatRangeLow [%.1f °F]"                          (gSettings) { ecobee="=[123456789012#settings.heatRangeLow]" }
Number settings_coolRangeHigh "coolRangeHigh [%.1f °F]"                        (gSettings) { ecobee="=[123456789012#settings.coolRangeHigh]" }
Number settings_coolRangeLow "coolRangeLow [%.1f °F]"                          (gSettings) { ecobee="=[123456789012#settings.coolRangeLow]" }
String settings_userAccessCode "userAccessCode [%s]"                           (gSettings) { ecobee="=[123456789012#settings.userAccessCode]" }
Number settings_userAccessSetting "userAccessSetting [%d]"                     (gSettings) { ecobee="=[123456789012#settings.userAccessSetting]" }
Number settings_auxRuntimeAlert "auxRuntimeAlert [%.1f °F]"                    (gSettings) { ecobee="=[123456789012#settings.auxRuntimeAlert]" }
Number settings_auxOutdoorTempAlert "auxOutdoorTempAlert [%.1f °F]"            (gSettings) { ecobee="=[123456789012#settings.auxOutdoorTempAlert]" }
Number settings_auxMaxOutdoorTemp "auxMaxOutdoorTemp [%.1f °F]"                (gSettings) { ecobee="=[123456789012#settings.auxMaxOutdoorTemp]" }
Switch settings_auxRuntimeAlertNotify "auxRuntimeAlertNotify [%s]"             (gSettings) { ecobee="=[123456789012#settings.auxRuntimeAlertNotify]" }
Switch settings_auxOutdoorTempAlertNotify "auxOutdoorTempAlertNotify [%s]"     (gSettings) { ecobee="=[123456789012#settings.auxOutdoorTempAlertNotify]" }
Switch settings_auxRuntimeAlertNotifyTechnician "auxRuntimeAlertNotifyTechnician [%s]" (gSettings) { ecobee="=[123456789012#settings.auxRuntimeAlertNotifyTechnician]" }
Switch settings_auxOutdoorTempAlertNotifyTechnician "auxOutdoorTempAlertNotifyTechnician [%s]" (gSettings) { ecobee="=[123456789012#settings.auxOutdoorTempAlertNotifyTechnician]" }
Switch settings_disablePreHeating "disablePreHeating [%s]"                     (gSettings) { ecobee="=[123456789012#settings.disablePreHeating]" }
Switch settings_disablePreCooling "disablePreCooling [%s]"                     (gSettings) { ecobee="=[123456789012#settings.disablePreCooling]" }
Switch settings_installerCodeRequired "installerCodeRequired [%s]"             (gSettings) { ecobee="=[123456789012#settings.installerCodeRequired]" }
String settings_drAccept "drAccept [%s]"                                       (gSettings) { ecobee="=[123456789012#settings.drAccept]" }
Switch settings_isRentalProperty "isRentalProperty [%s]"                       (gSettings) { ecobee="=[123456789012#settings.isRentalProperty]" }
Switch settings_useZoneController "useZoneController [%s]"                     (gSettings) { ecobee="=[123456789012#settings.useZoneController]" }
Number settings_randomStartDelayCool "randomStartDelayCool [%d]"               (gSettings) { ecobee="=[123456789012#settings.randomStartDelayCool]" }
Number settings_randomStartDelayHeat "randomStartDelayHeat [%d]"               (gSettings) { ecobee="=[123456789012#settings.randomStartDelayHeat]" }
Number settings_humidityHighAlert "humidityHighAlert [%d]"                     (gSettings) { ecobee="=[123456789012#settings.humidityHighAlert]" }
Number settings_humidityLowAlert "humidityLowAlert [%d]"                       (gSettings) { ecobee="=[123456789012#settings.humidityLowAlert]" }
Switch settings_disableHeatPumpAlerts "disableHeatPumpAlerts [%s]"             (gSettings) { ecobee="=[123456789012#settings.disableHeatPumpAlerts]" }
Switch settings_disableAlertsOnIdt "disableAlertsOnIdt [%s]"                   (gSettings) { ecobee="=[123456789012#settings.disableAlertsOnIdt]" }
Switch settings_humidityAlertNotify "humidityAlertNotify [%s]"                 (gSettings) { ecobee="=[123456789012#settings.humidityAlertNotify]" }
Switch settings_humidityAlertNotifyTechnician "humidityAlertNotifyTechnician [%s]" (gSettings) { ecobee="=[123456789012#settings.humidityAlertNotifyTechnician]" }
Switch settings_tempAlertNotify "tempAlertNotify [%s]"                         (gSettings) { ecobee="=[123456789012#settings.tempAlertNotify]" }
Switch settings_tempAlertNotifyTechnician "tempAlertNotifyTechnician [%s]"     (gSettings) { ecobee="=[123456789012#settings.tempAlertNotifyTechnician]" }
Number settings_monthlyElectricityBillLimit "monthlyElectricityBillLimit [%d]" (gSettings) { ecobee="=[123456789012#settings.monthlyElectricityBillLimit]" }
Switch settings_enableElectricityBillAlert "enableElectricityBillAlert [%s]"   (gSettings) { ecobee="=[123456789012#settings.enableElectricityBillAlert]" }
Switch settings_enableProjectedElectricityBillAlert "enableProjectedElectricityBillAlert [%s]" (gSettings) { ecobee="=[123456789012#settings.enableProjectedElectricityBillAlert]" }
Number settings_electricityBillingDayOfMonth "electricityBillingDayOfMonth [%d]" (gSettings) { ecobee="=[123456789012#settings.electricityBillingDayOfMonth]" }
Number settings_electricityBillCycleMonths "electricityBillCycleMonths [%d]"   (gSettings) { ecobee="=[123456789012#settings.electricityBillCycleMonths]" }
Number settings_electricityBillStartMonth "electricityBillStartMonth [%d]"     (gSettings) { ecobee="=[123456789012#settings.electricityBillStartMonth]" }
Number settings_ventilatorMinOnTimeHome "ventilatorMinOnTimeHome [%d]"         (gSettings) { ecobee="=[123456789012#settings.ventilatorMinOnTimeHome]" }
Number settings_ventilatorMinOnTimeAway "ventilatorMinOnTimeAway [%d]"         (gSettings) { ecobee="=[123456789012#settings.ventilatorMinOnTimeAway]" }
Switch settings_backlightOffDuringSleep "backlightOffDuringSleep [%s]"         (gSettings) { ecobee="=[123456789012#settings.backlightOffDuringSleep]" }
Switch settings_autoAway "autoAway [%s]"                                       (gSettings) { ecobee="<[123456789012#settings.autoAway]" }
Switch settings_smartCirculation "smartCirculation [%s]"                       (gSettings) { ecobee="=[123456789012#settings.smartCirculation]" }
Switch settings_followMeComfort "followMeComfort [%s]"                         (gSettings) { ecobee="=[123456789012#settings.followMeComfort]" }
String settings_ventilatorType "ventilatorType [%s]"                           (gSettings) { ecobee="<[123456789012#settings.ventilatorType]" }
Switch settings_isVentilatorTimerOn "isVentilatorTimerOn [%s]"                 (gSettings) { ecobee="=[123456789012#settings.isVentilatorTimerOn]" }
DateTime settings_ventilatorOffDateTime "ventilatorOffDateTime [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gSettings) { ecobee="<[123456789012#settings.ventilatorOffDateTime]" }
Switch settings_hasUVFilter "hasUVFilter [%s]"                                 (gSettings) { ecobee="=[123456789012#settings.hasUVFilter]" }
Switch settings_coolingLockout "coolingLockout [%s]"                           (gSettings) { ecobee="=[123456789012#settings.coolingLockout]" }
Switch settings_ventilatorFreeCooling "ventilatorFreeCooling [%s]"             (gSettings) { ecobee="=[123456789012#settings.ventilatorFreeCooling]" }
Switch settings_dehumidifyWhenHeating "dehumidifyWhenHeating [%s]"             (gSettings) { ecobee="=[123456789012#settings.dehumidifyWhenHeating]" }
String settings_groupRef "groupRef [%s]"                                       (gSettings) { ecobee="=[123456789012#settings.groupRef]" }
String settings_groupName "groupName [%s]"                                     (gSettings) { ecobee="=[123456789012#settings.groupName]" }
Number settings_groupSetting "groupSetting [%d]"                               (gSettings) { ecobee="=[123456789012#settings.groupSetting]" }

Group gRuntime (All)

String runtime_runtimeRev "runtimeRev [%s]"                                    (gRuntime) { ecobee="<[123456789012#runtime.runtimeRev]" }
Switch runtime_connected "connected [%s]"                                      (gRuntime) { ecobee="<[123456789012#runtime.connected]" }
DateTime runtime_firstConnected "firstConnected [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gRuntime) { ecobee="<[123456789012#runtime.firstConnected]" }
DateTime runtime_connectDateTime "connectDateTime [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gRuntime) { ecobee="<[123456789012#runtime.connectDateTime]" }
String runtime_disconnectDateTime "disconnectDateTime [%s]"                    (gRuntime) { ecobee="<[123456789012#runtime.disconnectDateTime]" }
DateTime runtime_lastModified "lastModified [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gRuntime) { ecobee="<[123456789012#runtime.lastModified]" }
DateTime runtime_lastStatusModified "lastStatusModified [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gRuntime) { ecobee="<[123456789012#runtime.lastStatusModified]" }
String runtime_runtimeDate "runtimeDate [%s]"                                  (gRuntime) { ecobee="<[123456789012#runtime.runtimeDate]" }
Number runtime_runtimeInterval "runtimeInterval [%d]"                          (gRuntime) { ecobee="<[123456789012#runtime.runtimeInterval]" }
Number runtime_actualTemperature "actualTemperature [%.1f °F]"                 (gRuntime) { ecobee="<[123456789012#runtime.actualTemperature]" }
Number runtime_actualHumidity "actualHumidity [%d %%]"                         (gRuntime) { ecobee="<[123456789012#runtime.actualHumidity]" }
Number runtime_desiredHeat "desiredHeat [%.1f °F]"                             (gRuntime) { ecobee="<[123456789012#runtime.desiredHeat]" }
Number runtime_desiredCool "desiredCool [%.1f °F]"                             (gRuntime) { ecobee="<[123456789012#runtime.desiredCool]" }
Number runtime_desiredHumidity "desiredHumidity [%d %%]"                       (gRuntime) { ecobee="<[123456789012#runtime.desiredHumidity]" }
Number runtime_desiredDehumidity "desiredDehumidity [%d %%]"                   (gRuntime) { ecobee="<[123456789012#runtime.desiredDehumidity]" }
String runtime_desiredFanMode "desiredFanMode [%s]"                            (gRuntime) { ecobee="<[123456789012#runtime.desiredFanMode]" }

Group gLocation (All)

Number location_timeZoneOffsetMinutes "timeZoneOffsetMinutes [%d]"             (gLocation) { ecobee="<[123456789012#location.timeZoneOffsetMinutes]" }
String location_timeZone "timeZone [%s]"                                       (gLocation) { ecobee="=[123456789012#location.timeZone]" }
Switch location_isDaylightSaving "isDaylightSaving [%s]"                       (gLocation) { ecobee="=[123456789012#location.isDaylightSaving]" }
String location_streetAddress "streetAddress [%s]"                             (gLocation) { ecobee="=[123456789012#location.streetAddress]" }
String location_city "city [%s]"                                               (gLocation) { ecobee="=[123456789012#location.city]" }
String location_provinceState "provinceState [%s]"                             (gLocation) { ecobee="=[123456789012#location.provinceState]" }
String location_country "country [%s]"                                         (gLocation) { ecobee="=[123456789012#location.country]" }
String location_postalCode "postalCode [%s]"                                   (gLocation) { ecobee="=[123456789012#location.postalCode]" }
String location_phoneNumber "phoneNumber [%s]"                                 (gLocation) { ecobee="=[123456789012#location.phoneNumber]" }
String location_mapCoordinates "mapCoordinates [%s]"                           (gLocation) { ecobee="=[123456789012#location.mapCoordinates]" }

Group gWeather (All)

DateTime weather_timestamp "timestamp [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]"   (gWeather) { ecobee="<[123456789012#weather.timestamp]" }
String weather_weatherStation "weatherStation [%s]"                            (gWeather) { ecobee="<[123456789012#weather.weatherStation]" }

Number weather_forecasts0_weatherSymbol "weatherSymbol [MAP(ecobeeWeatherSymbol.map):%d]" (gWeather) { ecobee="<[123456789012#weather.forecasts[0].weatherSymbol]" }
DateTime weather_forecasts0_dateTime "dateTime [%1$tm/%1$td/%1$tY %1$tH:%1$tM:%1$tS]" (gWeather) { ecobee="<[123456789012#weather.forecasts[0].dateTime]" }
String weather_forecasts0_condition "condition [%s]"                           (gWeather) { ecobee="<[123456789012#weather.forecasts[0].condition]" }
Number weather_forecasts0_temperature "temperature [%.1f °F]"                  (gWeather) { ecobee="<[123456789012#weather.forecasts[0].temperature]" }
Number weather_forecasts0_pressure "pressure [%d]"                             (gWeather) { ecobee="<[123456789012#weather.forecasts[0].pressure]" }
Number weather_forecasts0_relativeHumidity "relativeHumidity [%d %%]"          (gWeather) { ecobee="<[123456789012#weather.forecasts[0].relativeHumidity]" }
Number weather_forecasts0_dewpoint "dewpoint [%d]"                             (gWeather) { ecobee="<[123456789012#weather.forecasts[0].dewpoint]" }
Number weather_forecasts0_visibility "visibility [%d meters]"                  (gWeather) { ecobee="<[123456789012#weather.forecasts[0].visibility]" }
Number weather_forecasts0_windSpeed "windSpeed [%d]"                           (gWeather) { ecobee="<[123456789012#weather.forecasts[0].windSpeed]" }
Number weather_forecasts0_windGust "windGust [%d]"                             (gWeather) { ecobee="<[123456789012#weather.forecasts[0].windGust]" }
String weather_forecasts0_windDirection "windDirection [%s]"                   (gWeather) { ecobee="<[123456789012#weather.forecasts[0].windDirection]" }
Number weather_forecasts0_windBearing "windBearing [%d]"                       (gWeather) { ecobee="<[123456789012#weather.forecasts[0].windBearing]" }
Number weather_forecasts0_pop "pop [%d %%]"                                    (gWeather) { ecobee="<[123456789012#weather.forecasts[0].pop]" }
Number weather_forecasts0_tempHigh "tempHigh [%.1f °F]"                        (gWeather) { ecobee="<[123456789012#weather.forecasts[0].tempHigh]" } 
Number weather_forecasts0_tempLow "tempLow [%.1f °F]"                          (gWeather) { ecobee="<[123456789012#weather.forecasts[0].tempLow]" } 
Number weather_forecasts0_sky "sky [MAP(ecobeeSky.map):%d]"                    (gWeather) { ecobee="<[123456789012#weather.forecasts[0].sky]" } 

Group gHouseDetails (All)

String houseDetails_style "style [%s]"                                         (gHouseDetails) { ecobee="=[123456789012#houseDetails.style]" }
Number houseDetails_size "size [%d]"                                           (gHouseDetails) { ecobee="=[123456789012#houseDetails.size]" }
Number houseDetails_numberOfFloors "numberOfFloors [%d]"                       (gHouseDetails) { ecobee="=[123456789012#houseDetails.numberOfFloors]" }
Number houseDetails_numberOfRooms "numberOfRooms [%d]"                         (gHouseDetails) { ecobee="=[123456789012#houseDetails.numberOfRooms]" }
Number houseDetails_numberOfOccupants "numberOfOccupants [%d]"                 (gHouseDetails) { ecobee="=[123456789012#houseDetails.numberOfOccupants]" }
Number houseDetails_age "age [%d]"                                             (gHouseDetails) { ecobee="=[123456789012#houseDetails.age]" }

/* If you have remote sensors named Kitchen and Bedroom connected to an ecobee3 with ID 123456789012 */

Group gRemoteSensors (All)

Number remoteSensors_Kitchen_capability_temperature "Kitchen temp. [%.1f °F]" (gRemoteSensors) { ecobee="<[123456789012#remoteSensors(Kitchen).capability(temperature).value]" }
Switch remoteSensors_Bedroom_capability_occupancy "Bedroom occu. [%s]"        (gRemoteSensors) { ecobee="<[123456789012#remoteSensors(Bedroom).capability(occupancy).value]" }

Group gEvents (All)

String firstEvent_type "First event type [%s]"                                (gEvents)       { ecobee="<[123456789012#events[0].type]" }
String firstEvent_name "First event name [%s]"                                (gEvents)       { ecobee="<[123456789012#events[0].name]" }
String firstEvent_climate "First event climate [%s]"                          (gEvents)       { ecobee="<[123456789012#events[0].holdClimateRef]" }

/* runningEvent.* is first event marked running, available as of openHAB 1.8 */
String runningEvent_type "Running event type [%s]"                            (gEvents)       { ecobee="<[123456789012#runningEvent.type]" }
String runningEvent_name "Running event name [%s]"                            (gEvents)       { ecobee="<[123456789012#runningEvent.name]" }
String runningEvent_climate "Running event climate [%s]"                      (gEvents)       { ecobee="<[123456789012#runningEvent.holdClimateRef]" }
```

### Basic configuration

The following items, sitemap and rules files show the actual temperature and humidity, the thermostat's current mode and scheduled comfort setting reference.  You can set a hold for a defined comfort setting (like sleep, away, home, custom ones as well), or resume the scheduled program from all holds.  If the thermostat is in "heat" or "cool" modes, a single temperature setpoint is given to adjust the desired temperature.  If the thermostat is in "auto" mode, two temperature setpoints are given to adjust both the heat and cool setpoints.

The sitemap will also show sensor values for an ecobee3's remote sensors in the dining room, bedroom and living room.

#### ecobee.items

```
DateTime lastModified "last mod [%1$tH:%1$tM:%1$tS]" { ecobee="<[123456789012#lastModified]" }
Number actualTemperature "actual temp [%.1f °F]" { ecobee="<[123456789012#runtime.actualTemperature]" }
Number actualHumidity "actual hum [%d %%]" { ecobee="<[123456789012#runtime.actualHumidity]" }
String hvacMode "hvac mode [%s]"            { ecobee="=[123456789012#settings.hvacMode]" }
String currentClimateRef "sched comf [%s]"  { ecobee="<[123456789012#program.currentClimateRef]" }
String desiredComf "desired comf"           { ecobee="<[123456789012#runningEvent.holdClimateRef]", autoupdate="false" }
Number desiredTemp "desired temp [%.1f °F]"
Number desiredHeat "desired heat [%.1f °F]" { ecobee="<[123456789012#runtime.desiredHeat]" }
Number desiredCool "desired cool [%.1f °F]" { ecobee="<[123456789012#runtime.desiredCool]" }
String desiredFan  "desired fan mode [%s]"  { ecobee="<[123456789012#runtime.desiredFanMode]" }

// Address the ecobee3 unit itself as a sensor, unlike remote sensors it also supports humidity:
Number diningRoomTemperature "dining rm temp [%.1f °F]" { ecobee="<[123456789012#remoteSensors(Dining Room).capability(temperature).value]" }
Number diningRoomHumidity "dining rm hum [%d %%]" { ecobee="<[123456789012#remoteSensors(Dining Room).capability(humidity).value]" }
Switch diningRoomOccupancy "dining rm occ [%s]" { ecobee="<[123456789012#remoteSensors(Dining Room).capability(occupancy).value]" }

Number bedroomTemperature "bedroom temp [%.1f °F]" { ecobee="<[123456789012#remoteSensors(Bedroom).capability(temperature).value]" }
Switch bedroomOccupancy "bedroom occ [%s]" { ecobee="<[123456789012#remoteSensors(Bedroom).capability(occupancy).value]" }

Number livingRoomTemperature "living rm temp [%.1f °F]" { ecobee="<[123456789012#remoteSensors(Living Room).capability(temperature).value]" }
Switch livingRoomOccupancy "living rm occ [%s]" { ecobee="<[123456789012#remoteSensors(Living Room).capability(occupancy).value]" }
```

#### ecobee.sitemap

```
sitemap ecobee label="Ecobee"
{
  Frame label="Thermostat" {
    Text item=lastModified
    Text item=actualTemperature
    Text item=actualHumidity
    Switch item=hvacMode label="HVAC Mode" mappings=[heat=Heat,cool=Cool,auto=Auto,off=Off]
    Text item=currentClimateRef
    Switch item=desiredComf mappings=[sleep=Sleep,wakeup=Wake,home=Home,away=Away,smart6=Gym,resume=Resume]
    Setpoint item=desiredTemp label="Temp [%.1f °F]" minValue=50 maxValue=80 step=1 visibility=[hvacMode==heat,hvacMode==cool]
    Setpoint item=desiredHeat label="Heat [%.1f °F]" minValue=50 maxValue=80 step=1 visibility=[hvacMode==auto]
    Setpoint item=desiredCool label="Cool [%.1f °F]" minValue=50 maxValue=80 step=1 visibility=[hvacMode==auto]
    Switch item=desiredFan mappings=[on=On,auto=Auto] // for rule supported in 1.9
  }

  Frame label="Dining Room Sensors" {
    Text item=diningRoomTemperature
    Text item=diningRoomHumidity
    Text item=diningRoomOccupancy
  }

  Frame label="Bedroom Sensors" {
   Text item=bedroomTemperature
   Text item=bedroomOccupancy
  }

  Frame label="Living Room Sensors" {
   Text item=livingRoomTemperature
   Text item=livingRoomOccupancy
  }
}
```

#### ecobee.rules

```
rule "Populate desiredTemp from desiredHeat"
when
  Item desiredHeat received update
then
  if (hvacMode.state.toString == "heat" && desiredHeat.state instanceof DecimalType) {
    desiredTemp.postUpdate(desiredHeat.state)
  }
end

rule "Populate desiredTemp from desiredCool"
when
  Item desiredCool received update
then
  if (hvacMode.state.toString == "cool" && desiredCool.state instanceof DecimalType) {
    desiredTemp.postUpdate(desiredCool.state)
  }
end

rule TempHold
when
  Item desiredTemp received command
then
  switch hvacMode.state.toString {
    case "heat" : desiredHeat.sendCommand(receivedCommand)
    case "cool" : desiredCool.sendCommand(receivedCommand)
    case "auto" : logWarn("TempHold", "in auto mode, single setpoint ignored")
    case "off"  : logWarn("TempHold", "in off mode, single setpoint ignored")
  }
end

rule HeatHold
when
  Item desiredHeat received command
then
  logInfo("HeatHold", "Setting heat setpoint to " + receivedCommand.toString)
  val DecimalType desiredHeatTemp = receivedCommand as DecimalType
  var DecimalType desiredCoolTemp
  if (desiredCool.state instanceof DecimalType) {
    desiredCoolTemp = desiredCool.state as DecimalType
  } else {
    desiredCoolTemp = new DecimalType(90)
  }

  ecobeeSetHold("123456789012", desiredCoolTemp, desiredHeatTemp, null, null, null, null, null)
end

rule CoolHold
when
  Item desiredCool received command
then
  logInfo("CoolHold", "Setting cool setpoint to " + receivedCommand.toString)
  val DecimalType desiredCoolTemp = receivedCommand as DecimalType
  var DecimalType desiredHeatTemp
  if (desiredHeat.state instanceof DecimalType) {
    desiredHeatTemp = desiredHeat.state as DecimalType
  } else {
    desiredHeatTemp = new DecimalType(50)
  }

  ecobeeSetHold("123456789012", desiredCoolTemp, desiredHeatTemp, null, null, null, null, null)
end

rule FanHold
when
  Item desiredFan received command
then
  logInfo("FanHold", "Setting fan hold to " + receivedCommand.toString)
  val params = newLinkedHashMap(
    'isTemperatureAbsolute'-> false,
    'isTemperatureRelative' -> false,
    'isCoolOff' -> true,
    'isHeatOff' -> true,
    'coolHoldTemp' -> 90,
    'heatHoldTemp' -> 50,
    'fan' -> receivedCommand.toString)
  ecobeeSetHold("123456789012", params, null, null, null, null)
end

rule ComfortHold
when
  Item desiredComf received command
then
  if (receivedCommand.toString.equals("resume")) {
    ecobeeResumeProgram("123456789012", true)
  } else {
    ecobeeSetHold("123456789012", null, null, receivedCommand.toString, null, null, null, null)
  }
end
```

### Tracking last occupancy

The ecobee3 thermostat can connect to a number of wireless remote sensors that measure occupancy and temperature.  The thermostat normally uses these to implement its "follow-me comfort" feature, where the thermostat is constantly adjusting its idea of the current ambient temperature based on an average of the temperatures of rooms that are currently occupied.

The binding can individually address each remote sensor's temperature and occupancy state.  The occupancy state is ON when there has been motion within its range in the last 30 minutes.  The rules below could be used to update a `DateTime` item to show the last time movement was sensed around any of the sensors in your home.

Items:

```
Switch EcobeeMBROccu "Ecobee MBR Occu [%s]" { ecobee="<[123456789012#remoteSensors(Bedroom).capability(occupancy).value]" }
Switch EcobeeKitchenOccu "Ecobee Kitchen Occu [%s]" { ecobee="<[123456789012#remoteSensors(Kitchen).capability(occupancy).value]" }
Switch EcobeeDROccu "Ecobee DR Occu [%s]" { ecobee="<[123456789012#remoteSensors(Dining Room).capability(occupancy).value]" }
DateTime LastOccuTime "Last Occu [%1$tm/%1$td %1$tH:%1$tM]"
```

Rules:

```
rule LastMotionON
when
    Item EcobeeMBROccu changed to ON or
    Item EcobeeKitchenOccu changed to ON or
    Item EcobeeDROccu changed to ON
then
    postUpdate(LastOccuTime, new DateTimeType())
end

rule LastMotionOFF
when
    Item EcobeeMBROccu changed to OFF or
    Item EcobeeKitchenOccu changed to OFF or
    Item EcobeeDROccu changed to OFF
then
    switch LastOccuTime.state {
      DateTimeType: {
        var DateTime halfHourAgo = now.minusMinutes(30)
        var DateTime lastKnownMotion = new DateTime((LastOccuTime.state as DateTimeType).calendar.timeInMillis)
        if (halfHourAgo.isAfter(lastKnownMotion)) {
          LastOccuTime.postUpdate(new DateTimeType(halfHourAgo.toString))
        }
      }
    }
end
```

### MAP Transformations

The mapping of [weather symbol numbers](https://www.ecobee.com/home/developer/api/documentation/v1/objects/WeatherForecast.shtml) to their meanings can be specified if you place the following in the file `transform/ecobeeWeatherSymbol.map`:

```
-2=no_symbol
0=sunny
1=few_clouds
2=partly_cloudy
3=mostly_cloudy
4=overcast
5=drizzle
6=rain
7=freezing_rain
8=showers
9=hail
10=snow
11=flurries
12=freezing_snow
13=blizzard
14=pellets
15=thunderstorm
16=windy
17=tornado
18=fog
19=haze
20=smoke
21=dust
-=unknown
```

The mapping of the [sky numbers](https://www.ecobee.com/home/developer/api/documentation/v1/objects/WeatherForecast.shtml) to their meanings can be specified if you place the following in the file `transform/ecobeeSky.map`:

```
1=SUNNY 
2=CLEAR 
3=MOSTLY SUNNY 
4=MOSTLY CLEAR 
5=HAZY SUNSHINE 
6=HAZE 
7=PASSING CLOUDS 
8=MORE SUN THAN CLOUDS 
9=SCATTERED CLOUDS 
10=PARTLY CLOUDY 
11=A MIXTURE OF SUN AND CLOUDS 
12=HIGH LEVEL CLOUDS 
13=MORE CLOUDS THAN SUN 
14=PARTLY SUNNY 
15=BROKEN CLOUDS 
16=MOSTLY CLOUDY 
17=CLOUDY 
18=OVERCAST 
19=LOW CLOUDS 
20=LIGHT FOG 
21=FOG 
22=DENSE FOG 
23=ICE FOG 
24=SANDSTORM 
25=DUSTSTORM 
26=INCREASING CLOUDINESS 
27=DECREASING CLOUDINESS 
28=CLEARING SKIES 
29=BREAKS OF SUN LATE 
30=EARLY FOG FOLLOWED BY SUNNY SKIES 
31=AFTERNOON CLOUDS 
32=MORNING CLOUDS 
33=SMOKE 
34=LOW LEVEL HAZE
-=UNKNOWN
```


## Notes

1. Ecobee thermostats normally run based on a weekly schedule, but you can override the current program by setting a hold that controls the cool setpoint, the heat setpoint, and other options.  You can set a hold from a rule by calling the action `ecobeeSetHold`.  One of the parameters is a reference to a "climate" (also known as a comfort setting).  The default references for climates are `sleep`, `home`, and `away` (some models also have `wakeup`).
