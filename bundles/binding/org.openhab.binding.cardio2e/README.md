# Cardio2e Binding
The openHAB Cardio2e binding allows connecting to Secant Cardio IIé home automation system installations. Dimming or switching lights on and off, switching devices on and off, activating roller shutters, executing scenarios, managing HVACs or activating system security are only some examples.  

To access your Cardio system you need an RS-232 interface (e.g. a Prolific PL-2303 based USB to RS-232 converter interface) and a DB-9 to RJ-11 cable suitable for either Cardio PC or Cardio ACC port. You can use this [schema](https://github.com/openhab/openhab1-addons/files/999699/PcCardio_Cable.pdf&sa=D&ust=1528711214015000) to build your Cardio PC port cable.  

> Note: You can also use the Cardio accessory port (ACC) instead of PC port, so you must crimp the RJ-11 in reverse order, exchange RD and TD signals from the DB-9 connector with respect to the previous PC schematic (pins 2 and 3) and do not connect pin 7.  

## Binding Configuration  
This binding can be configured in the file `configurations/openhab.cfg` (openHAB 1) or `conf/services/cardio2e.cfg` (openHAB 2).

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| port | | Yes | Serial RS-232 port. Examples: '/dev/ttyUSB0' for Linux, 'COM1' for Windows. |
| programcode | 00000 |No | Installer program code for login. |
| securitycode | | No | Security code for arm / disarm alarm. |
| zones | false | No | Alarm zones state detection. Enables alarm zones state detection (by default 'false' for minimum use of resources). |
| zoneUnchangedMinRefreshDelay | 600000 | No | Minimum delay in milliseconds for zone detection refresh when no state changes succeed (by default '600000' milliseconds = 10 minutes). |
| datetimeMaxOffset | 15 | No | Date and time maximum offset allowed (in minutes) for progressive (step by step, minute by minute) date and time state update. Special values: '0' will remove offset limit, '-1' will disable progressive update and will remove offset limit, '-2' will do unconditional update without any filter even if current date and time of Cardio IIé matches the update. |
| firstUpdateWillSetDatetime | false | No | Always will set Cardio IIé clock on first date and time update from last binding start, even if allowedDatetimeUpdateHour was set. |
| allowedDatetimeUpdateHour | -1 | No | Allows date and time updates on specified hour only (for example, a safe hour when no events will be triggered by Cardio's schedules). Valid values are from '0' to '23' ('-1' disables hour restriction).|
| testmode | false | No | Enables fake port console test mode, for developer debug purposes only. Warning: Real communication with Cardio IIé will not work if enabled! |
| minDelayBetweenReceivingAndSending | 200 | No | Minimum delay in milliseconds between receiving and sending, for expert tuning only. By default '200' milliseconds (tested safe value).|
| minDelayBetweenSendings | 300 | No | Minimum delay in milliseconds between sendings, for expert tuning only. By default '300' milliseconds (tested safe value). |
| filterUnnecessaryCommand | false | No | Avoid sending commands when the last value of the object reported by Cardio IIé is the same value as the command value. |
| filterUnnecessaryReverseModeUpdate | true | No | Avoid sending updates (reverse mode) when the last value of the object reported by Cardio IIé is the same value as the update value.|
| smartSendingEnabledObjectTypes | | No | Comma separated list of smart sending enabled object types. Smart sending enabled means that no contradictory commands will be stored in the sending buffer for that object type, so that if a command directed to a specific object exists in sending buffer and a new command is received for the same object, the command stored will be replaced by the new one. Valid values are 'LIGHTING', 'RELAY', 'HVAC_CONTROL', 'DATE_AND_TIME', 'SCENARIO', 'SECURITY', 'ZONES_BYPASS' and 'CURTAIN'. |

### Example  
```
cardio2e:port=/dev/ttyUSB0
cardio2e:programcode=00000
cardio2e:securitycode=12345
cardio2e:zones=true
cardio2e:filterUnnecessaryCommand=true
cardio2e:smartSendingEnabledObjectTypes=LIGHTING,RELAY,HVAC_CONTROL,DATE_AND_TIME,SCENARIO,SECURITY,ZONES_BYPASS,CURTAIN
```

## Items Configuration  
### Description  
In order to bind an item to a Cardio IIé system you need to provide configuration settings. The easiest way to do so is to add binding information in your items file. The syntax for the Cardio2e binding configuration string is explained here:  
* **LIGHTING**:  
`c2e="LIGHTING,object_number"`  
where 'object_number' is a number between 1 and 160 that represent the light number you want to control. You can bind both 'Switch' and 'Dimmer' items types.  
*Reverse mode*: Can be enabled by adding '!' symbol before 'LIGHTING' (example: `c2e="!LIGHTING,20"`), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused lighting Cardio control in order to send commands to openHAB item, and to receive item updates (you can enable a unused Cardio lighting control by assigning it a fake X10 address).  
*Dimmer correction*: Can be enabled by adding '%' symbol before 'object_number' (example: `c2e="LIGHTING,%1"`), in order to consider Cardio lighting 1% values as 0% (powered off). This correction is necessary when Cardio is programmed to turn on a light by presence, since when power on time expires, Cardio sends a 1% value to the DM1 instead of 0% power off value (in fact, in practice, any value less than 10% in a DM1 will turn off the light).  
*Autoupdate*: Cardio always reports the status of its 'LIGHTING' objects after executing a command, so we recommend that you add 'autoupdate=false' in the item settings to make sure that the item's value always matches Cardio's value (example: `Dimmer My_Light {c2e="LIGHTING,2",autoupdate=false}`). Not applicable when "reverse mode" is used.  
* **RELAY**:  
*Option #1*: `c2e="RELAY,object_number"`  
where 'object_number' is a number between 1 and 40 that represents the relay number you want to control. You can only bind 'Switch' items type.  
*Option #2*: `c2e="RELAY,shutter_up_object_number,shutter_down_object_number"`  
where 'shutter_up_object_number' and 'shutter_down_object_number' are numbers between 1 and 40 that represents the relay numbers of a pair of timed relays used to move shutter up and down. You can only bind 'Rollershutter' items type.  
*Reverse mode*: Can be enabled by adding '!' symbol before 'RELAY' (example: `c2e="!RELAY,4"`), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused relay Cardio control in order to send commands to openHAB item, and to receive item updates.  
*Autoupdate*: Cardio always reports the status of its 'RELAY' objects after executing a command, so we recommend that you add 'autoupdate=false' in the item settings to make sure that the item's value always matches Cardio's value (example: `RollerShutter My_Shutter {c2e="RELAY,5,6",autoupdate=false}`). Not applicable when "reverse mode" is used.  
* **HVAC_TEMPERATURE**:  
`c2e="HVAC_TEMPERATURE,hvac_zone"`  
can be bound to a 'Number' item type, where 'hvac_zone' is a value between 1 and 5 that represents the Cardio IIé HVAC zone number you want to receive temperature value states from (the unit of measurement, ºC or ºF, will be the same that was set in Cardio system config).  
Example: `Number My_Temperature {c2e="HVAC_TEMPERATURE,1"}`).  
* **HVAC_CONTROL**:  
*One parameter options*: `c2e="HVAC_CONTROL,hvac_zone"`  
where 'hvac_zone' is a value between 1 and 5 that represents the Cardio IIé HVAC zone number you want to control. You can bind to a 'Switch' item in order to simply switch on / off Cardio IIé HVAC zone (example: `Switch My_HVAC_Switch {c2e="HVAC_CONTROL,1"}`). You also can bind to a 'Number' item in order to control HVAC zone with KNX standard internetworking DPT 20.105 HVAC control values (auto=0, heating=1, cooling= 3, off=6), and no KNX compliant values for Cardio IIé additional submodes (normal=254, economy=255).  
*Two parameters options*: `c2e="HVAC_CONTROL,hvac_zone,function"`  
where 'hvac_zone' is a value between 1 and 5 that also represents the Cardio IIé HVAC zone number, and 'function' is the HVAC function you want to control: 'FAN' for fan control, 'AUTO', 'COOLING' and 'HEATING' for HVAC mode switch, and 'ECONOMY' and 'NORMAL' for additional submodes. You can bind to a 'Switch' item in order to switch on / off Cardio IIé HVAC functions, or you can bind to a 'Number' item in order to adjust cooling and heating setpoints (Cardio IIé has two different setpoints, one for cooling and one for heating, and the unit of measurement, ºC or ºF, will be the same that was set in Cardio system config).  
*Autoupdate*: Cardio always reports the status of its 'HVAC_CONTROL' objects after executing a command, so we recommend that you add 'autoupdate=false' in the item settings to make sure that the item's value always matches Cardio's value.  
*Examples*:  
    ```
    Switch My_HVAC_Switch {c2e="HVAC_CONTROL,1",autoupdate=false}
    Number My_HVAC_KNX_Mode {c2e="HVAC_CONTROL,1"[,optional_KNX_binding_config],autoupdate=false}
    Switch My_HVAC_Fan "Fan" {c2e="HVAC_CONTROL,1,FAN",autoupdate=false}
    Switch My_HVAC_Auto "Auto mode" {c2e="HVAC_CONTROL,1,AUTO",autoupdate=false}
    Switch My_HVAC_Heating "Heating mode" {c2e="HVAC_CONTROL,1,HEATING",autoupdate=false}
    Switch My_HVAC_Cooling "Cooling mode" {c2e="HVAC_CONTROL,1,COOLING",autoupdate=false}
    Switch My_HVAC_Eco "Economy submode" {c2e="HVAC_CONTROL,1,ECONOMY",autoupdate=false}
    Switch My_HVAC_Normal "Normal submode" {c2e="HVAC_CONTROL,1,NORMAL",autoupdate=false}
    Number My_HVAC_Cooling_Setpoint "Cooling setpoint [%.1f °C]" {c2e="HVAC_CONTROL,1,COOLING",autoupdate=false}
    Number My_HVAC_Heating_Setpoint "Heating setpoint [%.1f °C]" {c2e="HVAC_CONTROL,1,HEATING",autoupdate=false}
    ```
* **ZONES**:  
`c2e="ZONES,zone_number,zone_type"`  
can be bound to a 'Contact' or 'Switch' item type, where 'zone_number' is a value between 1 and 16 that represents the Cardio IIé alarm zone number you want to monitor, and 'zone_type' is a value that specifies zone type defined in Cardio system config: 'OPEN' for NO (normally open), 'CLOSED' for NC (normally closed) and 'NORMAL' for EOL (end of line resistor). WARNING: Alarm zones state detection is disabled by default for minimum use of resources. In order to monitor alarm zones you must set parameter 'zones=true' in binding configuration.  
*Invert value*: Can be enabled by adding '!' symbol before 'ZONES', so the reported status of the Cardio alarm zone will be inverted.  
Examples:  
    ```
    Contact My_Contact {c2e="ZONES,1,NORMAL"}
    Switch My_Contact {c2e="ZONES,1,CLOSED"}
    Contact My_Contact {c2e="!ZONES,2,OPEN"}
    Switch My_Contact {c2e="!ZONES,2,NORMAL"}
    ```   
* **ZONES_BYPASS**:  
`c2e="ZONES_BYPASS,zone_number"`  
can be bound to a 'Switch' item type, where 'zone_number' is a value between 1 and 16 that represents the Cardio IIé alarm zone number you want to bypass (example: `Switch My_Zone_Bypass {c2e="ZONES_BYPASS,1"}`).  
*Invert value*: Can be enabled by adding '!' symbol before 'ZONES_BYPASS', so the operating mode of the Cardio IIé alarm zone bypass will be inverted, so that the item will indicate whether the zone will be armed for Cardio IIé security system or not (example: `Switch My_Zone_Armed {c2e="!ZONES_BYPASS,1"}`).  
*Note about autoupdate*: Cardio WILL NOT report the status of its 'ZONES_BYPASS' objects after executing a command, but it will do it after arming security system. So we recommend that you DO NOT add 'autoupdate=false' in the item settings.  
* **DATE_AND_TIME**:  
`c2e="DATE_AND_TIME"`  
can be bound to a 'DateTime' item type. When a date and time value command is received Cardio's clock will be unconditionally set to item date and time value. However, upon receiving an update, the clock will be updated based on the criteria defined in the binding configuration (see 'datetimeMaxOffset', 'firstUpdateWillSetDatetime' and 'allowedDatetimeUpdateHour' config parameters).  
Because Cardio's clock usually lags behind, you can use NTP binding in order to keep the clock on time, for example, sending updates every 15 minutes.  
    Time sync example with OH1 NTP binding: `DateTime My_date_and_time {ntp="Europe/Madrid:es_ES",  c2e="DATE_AND_TIME")`  
    Time sync example with OH2 NTP binding: `DateTime My_date_and_time {channel="ntp:ntp:cardiosync:dateTime",  c2e="DATE_AND_TIME")`  
* **SCENARIO**:  
`c2e="SCENARIO"`  
can be bound to 'Number' items type. When a number value command between 0 and 41 is received, the corresponding Cardio scene number between 1 and 42 will be activated (command value + 1).  
Note that numbers between 0 and 41 are used to provide direct compatibility with the scene numbers range used in protocols such as KNX, where the first scene is encoded with the number 0.  
* **SECURITY**:  
`c2e="SECURITY"`  
can be bound to 'Switch' and 'Number' items type.  
Using a 'Switch' item we can arm / disarm Cardio security system by ON / OFF commands as well as receive state updates (securitycode must be previously set in config file).  
*Autoupdate*: Cardio always reports the status of its 'SECURITY' objects after executing a command, so we recommend that you add 'autoupdate=false' in the item settings to make sure that the item's value always matches Cardio's value (example: `Switch Security_Arm {c2e="SECURITY",autoupdate=false}`).  
*Error code*: If security system could not be armed, we can show the reason as numeric error code value update received in a linked 'Number' item (example: `Number Security_Error {c2e="SECURITY", autoupdate=false}`). Likewise, we can configure an entry in the sitemap that translates those values by its corresponding description.  
*Sitemap example*:  
    ```
    Text item=Security_Error label="ERROR # [%d]" visibility=[Security_Error>0] labelcolor=[Security_Error>0="red"] valuecolor=[Security_Error>0="red"]
    Text item=Security_Error label="(security code is not valid)" icon="none" visibility=[Security_Error==4] labelcolor=[Security_Error>0="red"]
    Text item=Security_Error label="(there are open zones)" icon="none" visibility=[Security_Error==16] labelcolor=[Security_Error>0="red"]
    Text item=Security_Error label="(there is a power problem)" icon="none" visibility=[Security_Error==17] labelcolor=[Security_Error>0="red"]
    Text item=Security_Error label="(unknown reason)" icon="none" visibility=[Security_Error==18] labelcolor=[Security_Error>0="red"]
    ```
* **CURTAIN**:  
`c2e="CURTAIN,object_number"`  
where 'object_number' is a number between 1 and 80 that represent the shutter number you want to control. You can bind both 'RollerShutter' and 'Dimmer' items types (no STOP or MOVE commands are supported, and 100% value means shutter down). Note that 'CURTAIN' objects are only available in lastest Cardio IIé firmware versions.  
*Reverse mode*: Can be enabled by adding '!' symbol before 'CURTAIN'' (example: `c2e="!CURTAIN,13"`), so the Cardio object will be considered as a control, not an actuator. You can bind  in reverse mode an unused curtain Cardio control in order to send commands to openHAB item, and to receive item updates.  
*Autoupdate*: Cardio always reports the status of its 'CURTAIN' objects after executing a command, so we recommend that you add 'autoupdate=false' in the item settings to make sure that the item's value always matches Cardio's value (example: `Dimmer My_Curtain {c2e="CURTAIN,3",autoupdate=false}`). Not applicable when "reverse mode" is used.  
