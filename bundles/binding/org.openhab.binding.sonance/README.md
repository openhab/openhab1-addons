## Table of Contents

* [Introduction](#introduction)
* [Binding Configuration](#binding-configuration)
* [Item configuration](#item-configuration)
* [Sitemap Example](#sitemap-example)
* [Logging](#logging)
* [Known Issues](#known-issues)
* [Change Log](#change-log)

## Introduction

This binding integrates with [Sonance DSP Amplifiers](http://www.sonance.com/electronics/amplifiers/dsp). It supports all three models (DSP 2-150, DSP 8-130 and DSP 2-750) but for now it's only tested with the DSP 8-130.
For each group you can enable or disable sound (toggle mute) or set the volume.

>For installation of the binding JAR on your system, please see the Wiki page [Bindings](Bindings).

## Binding Configuration

Edit the file `openhab.cfg` located in `${openhab_home}/configurations/`:

    ############################## Sonance binding ########################################
    #
    # Sonance refresh rate in ms 
    sonance:refresh=60000

When a command for a new volume or mute toggle is pressed, the value is updated immediately. So their is no need to lower te refresh rate to get a faster response.

## Item configuration

```
/* Sonance items*/
Switch 	 Sonance "Amplifier" {sonance="10.0.0.8:52000:power"}

Switch 	 Sonance_bedroom_mute "Bedroom" {sonance="10.0.0.8:52000:mute:00"}
Number 	 Sonance_bedroom_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:00"}

Switch 	 Sonance_bathroom_mute "Bathroom" {sonance="10.0.0.8:52000:mute:01"}
Number 	 Sonance_bathroom_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:01"}

Switch 	 Sonance_spare_room_mute "Spare room" {sonance="10.0.0.8:52000:mute:02"}
Number 	 Sonance_spare_room_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:02"}

Switch 	 Sonance_office_mute "Office" {sonance="10.0.0.8:52000:mute:03"}
Number 	 Sonance_office_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:03"}
```

## Sitemap Example
```
Frame label="Amplifier" {
	Switch item=Sonance

	Switch item=Sonance_bedroom_mute
	Setpoint item=Sonance_bedroom_volume minValue=-70.0 maxValue=12
	
	Switch item=Sonance_office_mute
	Setpoint item=Sonance_office_volume minValue=-70.0 maxValue=12
	
	Switch item=Sonance_bathroom_mute
	Setpoint item=Sonance_bathroom_volume minValue=-70.0 maxValue=12

	Switch item=Sonance_spare_room_mute
	Setpoint item=Sonance_spare_room_volume minValue=-70.0 maxValue=12				
}			
```
## Logging

In order to configure logging for this binding to be generated in a separate file add the following to your /configuration/logback.xml file;
```xml
<appender name="SONANCEFILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
   <file>logs/nest.log</file>
   <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
      <!-- weekly rollover and archiving -->
      <fileNamePattern>logs/sonance-%d{yyyy-ww}.log.zip</fileNamePattern>
      <!-- keep 30 days' worth of history -->
      <maxHistory>30</maxHistory>
   </rollingPolicy>
   <encoder>
     <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{30}[:%line]- %msg%n%ex{5}</pattern>
   </encoder>
</appender>
    
<!-- Choose level ERROR, WARN, INFO, DEBUG or TRACE for detailed logging -->
<logger name="org.openhab.binding.sonance" level="TRACE" additivity="false">
   <appender-ref ref="SONANCEFILE" />
</logger>
```

## Known Issues

1. Getting current power status from the amplifier fails because of a bug in the Sonance software version 2.31. This is fixed in the Sonance firmware version 2.39.
2. The auto on method "music" doesn't work when using an digital input module. Sonance reports this is a hardware limitation which will never be fixed.
3. Absolute commands to directly set a volume (so not relative like UP and DOWN) need firmware version v2.51 or higher. Most GUI's make the change directly (like the Android app and the web GUI).

## Change Log
### openHAB 1.8

* Initial version

### openHAB 1.8.1

* Bug fixes

### openHAB 1.8.4

* Absolute command now use direct IP codes, were they with some unstable algoritme combining UP and DOWN before.