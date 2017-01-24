# Introduction

Visonic produces the Powermax alarm panel series (PowerMax, PowerMax+, PowerMaxExpress, PowerMaxPro and PowerMaxComplete) and the Powermaster alarm series (PowerMaster 10 and PowerMaster 30). This binding allows you to control the alarm panel (arm/disarm) and allows you to use the Visonic sensors (movement, door contact, ...) within openHAB.

The PowerMax provides support for a serial interface that can be connected to the machine running openHAB. The serial interface is not installed by default but can be ordered from any PowerMax vendor (called the Visonic RS-232 Adaptor Kit).

Visonic does not provide a specification of the RS232 protocol and, thus, the binding uses the available protocol specification given at the [​domoticaforum](http://www.domoticaforum.eu/viewtopic.php?f=68&t=6581).
The binding implemntation of this protocol is largely inspired by the [Vera plugin](http://code.mios.com/trac/mios_visonic-powermax).

For installation of the binding, please see Wiki page [[Bindings]].

# Binding configuration

Configuration is done in the openhab.cfg file (in the folder `${openhab_home}/configurations`):

    ################################### PowerMax Binding ##################################

    # the serial port to use for connecting to the PowerMax alarm system
    # e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
    # Required setting if using a serial connection
    #powermax:serialPort=COM1

    # the IP address and port to use for connecting to the PowerMax alarm system
    # Required settings if using a TCP connection
    #powermax:ip=
    #powermax:tcpPort=

    # The delay in minutes to reset a motion detection (optional, defaults to 3)
    #powermax:motionOffDelay=3

    # Enable or disable arming the PowerMax alarm system from openHAB
    # For security reason, this feature is disabled by default
    # (optional, defaults to false)
    #powermax:allowArming=false

    # Enable or disable disarming the PowerMax alarm system from openHAB
    # For security reason, this feature is disabled by default
    # (optional, defaults to false)
    #powermax:allowDisarming=false

    # The PIN code to use for arming/disarming the PowerMax alarm system from openHAB
    # Not required except when Powerlink mode cannot be used
    #powermax:pinCode=

    # Force the standard mode rather than trying using the Powerlink mode
    # (optional, defaults to false)
    #powermax:forceStandardMode=false

    # Define the panel type
    # Value must be one of these values: PowerMax, PowerMax+, PowerMaxPro,
    # PowerMaxComplete, PowerMaxProPart, PowerMaxCompletePart, PowerMaxExpress,
    # PowerMaster10, PowerMaster30
    # Only required when forcing the standard mode
    # (optional, defaults to PowerMaxPro)
    #powermax:panelType=PowerMaxPro

    # Automatic sync time at openHAB startup (optional, defaults to false)
    #powermax:autoSyncTime=false

Some notes:
* at least you need to specify either a serial connection through the setting `powermax:serialPort` or a TCP connection through the settings `powermax:ip` and `powermax:tcpPort`.
* On Linux, you may get an error stating the serial port cannot be opened when the Powermax plugin tries to load.  You can get around this by adding the `openhab` user to the `dialout` group like this: `usermod -a -G dialout openhab`.
* Also on Linux you may have issues with the USB if using two serial USB devices e.g. Powermax and RFXcom . See the wiki page for more on symlinking the USB ports [[symlinks]]
* For Powerlink mode to work the enrollment procedure has to be followed. If you don't enroll the Powerlink on the PowerMax the binding will operate in Standard mode, and if enrolled in Powerlink mode. On the newer software versions of the PowerMax the Powerlink enrollment is automatic, and the binding should only operate in 'Powerlink' mode (if enrollment is successful).
* You can force the binding to use the Standard mode. In this mode, the binding will not download the alarm panel setup and so the binding will not know what zones you have setup or what is your PIN code for example.

# Item Binding Configuration

In order to bind an item to the alarm system, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`).

General format is:

    powermax="<selector>[:<parameter>]"

A `<selector>` is required; the `<parmater>` is only required for few selectors.

Full list of binding items:

| Selector | Parameter | item type | purpose | changeable |
| --- | --- | --- | --- | --- |
| `panel_mode` | - | String | Either Standard, Powerlink or Download | no
| `panel_type` | - | String | Type of the panel | no
| `panel_serial` | - | String | Serial number | no
| `panel_eprom` | - | String | EPROM version | no
| `panel_software` | - | String | Software version | no
| `panel_trouble` | - | Switch | Whether a trouble is detected by the panel or not | no
| `panel_alert_in_memory` | - | Switch | Whether an alert is saved in panel memory or not | no
| `partition_status` | - | String | A short status summary | no
| `partition_ready` | - | Switch | Whether the panel is ready or not | no
| `partition_bypass` | - | Switch | Whether at least one zone is bypassed or not | no
| `partition_alarm` | - | Switch | Whether an alarm is active or not | no
| `partition_armed` | - | Switch | Whether the system is armed or not | yes (ON or OFF)
| `partition_arm_mode` | - | String | Partition arm mode | yes (possible values are Disarmed, Stay, Armed, StayInstant, ArmedInstant, Night and NightInstant)
| `zone_status` | zone number (first zone is 1) | Switch or Contact | Whether the zone is tripped or not | no
| `zone_last_trip` | zone number (first zone is 1) | DateTime | Timestamp when the zone was last tripped | no
| `zone_bypassed` | zone number (first zone is 1) | Switch | Whether the zone is bypassed or not | yes (ON or OFF)
| `zone_armed` | zone number (first zone is 1) | String or Switch | Whether the zone is armed or not | no
| `zone_low_battery` | zone number (first zone is 1) | Switch | Whether the sensor battery is low or not | no
| `command` | - | String | To trigger a binding action | yes (possible values are get_event_log, download_setup, log_setup and help_items)
| `event_log` | entry number (1 for the most recent) | String | Event log entry | no
| `PGM_status` | - | Switch | PGM switch ON or OFF | yes (ON or OFF)
| `X10_status` | device number (first is 1) | String or Switch | X10 device ON or OFF | yes (possible values are ON, OFF, DIM and BRIGHT)

# Binding actions

Defining such an item

    String Powermax_command "Command [%s]" {powermax="command", autoupdate="false"}

You can trigger an action through a call in a rule:

    Powermax_command.sendCommand("<command>")

Here are the available actions:

| Command | Action |
| --- | --- |
| `get_event_log` | Retrieve the event logs
| `download_setup` | Read the panel setup (and sync time)
| `log_setup` | Log information about the current panel setup
| `help_items` | Log information about how to create items and sitemap

# Items example

Here is an example of what items you can define (only zone 1 and X10 device 1 are considered in this example):

```
Group GPowerMax "Alarm"

String Powermax_partition_status "Partition status [%s]" (GPowerMax) {powermax="partition_status"}
Switch Powermax_partition_ready "Partition ready" (GPowerMax) {powermax="partition_ready", autoupdate="false"}
Switch Powermax_partition_bypass "Partition bypass" (GPowerMax) {powermax="partition_bypass", autoupdate="false"}
Switch Powermax_partition_alarm "Partition alarm" (GPowerMax) {powermax="partition_alarm", autoupdate="false"}
Switch Powermax_panel_trouble "Panel trouble" (GPowerMax) {powermax="panel_trouble", autoupdate="false"}
Switch Powermax_panel_alert_in_mem "Panel alert in memory" (GPowerMax) {powermax="panel_alert_in_memory", autoupdate="false"}
Switch Powermax_partition_armed "Partition armed" (GPowerMax) {powermax="partition_armed", autoupdate="false"}
String Powermax_partition_arm_mode "Partition arm mode [%s]" (GPowerMax) {powermax="partition_arm_mode", autoupdate="false"}

Switch Powermax_zone1_status "Zone 1 status" (GPowerMax) {powermax="zone_status:1", autoupdate="false"}
Contact Powermax_zone1_status2 "Zone 1 status [%s]" (GPowerMax) {powermax="zone_status:1"}
DateTime Powermax_zone1_last_trip "Zone 1 last trip [%1$tH:%1$tM]" (GPowerMax) {powermax="zone_last_trip:1"}
Switch Powermax_zone1_bypassed "Zone 1 bypassed" (GPowerMax) {powermax="zone_bypassed:1", autoupdate="false"}
Switch Powermax_zone1_armed "Zone 1 armed" (GPowerMax) {powermax="zone_armed:1", autoupdate="false"}
Switch Powermax_zone1_low_battery "Zone 1 low battery" (GPowerMax) {powermax="zone_low_battery:1", autoupdate="false"}

String Powermax_command "Command [%s]" (GPowerMax) {powermax="command", autoupdate="false"}

String Powermax_event_log_1 "Event log 1 [%s]" (GPowerMax) {powermax="event_log:1"}
String Powermax_event_log_2 "Event log 2 [%s]" (GPowerMax) {powermax="event_log:2"}
String Powermax_event_log_3 "Event log 3 [%s]" (GPowerMax) {powermax="event_log:3"}
String Powermax_event_log_4 "Event log 4 [%s]" (GPowerMax) {powermax="event_log:4"}
String Powermax_event_log_5 "Event log 5 [%s]" (GPowerMax) {powermax="event_log:5"}

String Powermax_panel_mode "Panel mode [%s]" (GPowerMax) {powermax="panel_mode"}
String Powermax_panel_type "Panel type [%s]" (GPowerMax) {powermax="panel_type"}
String Powermax_panel_eeprom "EPROM [%s]" (GPowerMax) {powermax="panel_eprom"}
String Powermax_panel_software "Software version [%s]" (GPowerMax) {powermax="panel_software"}
String Powermax_panel_serial "Serial [%s]" (GPowerMax) {powermax="panel_serial"}

Switch Powermax_PGM_status "PGM status" (GPowerMax) {powermax="PGM_status", autoupdate="false"}
Switch Powermax_X10_1_status "X10 1 status" (GPowerMax) {powermax="X10_status:1", autoupdate="false"}
String Powermax_X10_1_status2 "X10 1 status [%s]" (GPowerMax) {powermax="X10_status:1", autoupdate="false"}
```

And here is an example of what you could add in your sitemap:

```
Text label="Security" icon="lock" {
    Switch item=Powermax_partition_armed mappings=[OFF="Disarmed", ON="Armed"]
    Switch item=Powermax_partition_arm_mode mappings=[Disarmed="Disarmed", Stay="Armed home", Armed="Armed away"] valuecolor=[=="Armed"="green",=="Stay"="orange"]
    Switch item=Powermax_command mappings=[get_event_log="Event log", download_setup="Get setup", log_setup="Log setup", help_items="Help items"]
    Switch item=Powermax_X10_1_status2 mappings=[OFF="Off", ON="On", DIM="Dim", BRIGHT="Bright"]
    Group item=GPowerMax label="Alarm"
}
```

# Limitations
- Visonic does not provide a specification of the RS232 protocol and, thus, use this binding at your own risk.
- The binding is not able to arm/disarm a particular partition.
- The compatibility of the binding with the Powermaster alarm panel series is probably only partial.
- The TCP connection is implemented but was not tested.
