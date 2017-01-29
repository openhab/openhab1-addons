# Primare Binding

[Primare AB](http://www.primare.se) is a Swedish manufacturer of hi-fi and audio and video products such as amplifiers, disc players and tuners. Older Primare devices are equipped with a RS-232 serial control interface. Communication is asynchronous, an external control device connected to the serial interface has no way of differentiating between status messages triggered by external RS-232 commands or, say, manual operation using the front panel controls.

Currently this binding supports Primare models SP31, SP31.7, SPA20 and SPA21, which, according to Primare documentation, share the same serial control interface. It has been tested with a SPA20 with software v.1.50 Nov 2 2003.

This binding can be used either using a serial port on the openHAB controller or a simple IP-to-serial gateway such as an RPi running socat on Raspbian (null modem cable, 4800 bit/s, bits:8, stopbits:1, parity:N).

## Binding Configuration

This binding can be configured in the file `services/primare.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<device-id>`.model |   |    Yes   | model of the Primare device to control.  Can be one of `SP31`, `SP31.7`, `SPA20`, `SPA21` |
| `<device-id>`.serial |  | if connecting via serial port | serial port (or pseudo-tty) of the Primare device to control (such as `/dev/ttyS0`) |
| `<device-id>`.host |    | if connecting to TCP-serial converter (such as a linux box running socat) | hostname or IP address of the Primare device to control |
| `<device-id>`.port |    | if connecting to TCP-serial converter (such as a linux box running socat) | TCP port number of the Primare device to control |

where `<device-id>` is a name you choose for a specific connected Primare device.  You can repeat the configuration properties with different `<device-id>`s supplied to control multiple Primare devices.

### Examples

#### Basic

```
myspa20.model=SPA20
myspa20.serial=/dev/ttyAMA0
```

#### Livingroom SPA20 connected to pty symlink /dev/primare

```
livingroom-spa20.model=SPA20
livingroom-spa20.serial=/dev/primare
```

#### Livingroom SPA20 connected to serial gw at 10.21.0.10

```
livingroom-spa20.model=SPA20
livingroom-spa20.host=10.21.0.10
livingroom-spa20.port=8100
```

## Item Configuration

The syntax is:

```
primare="<openHAB-command>:<device-id>:<device-command>[,<openHAB-command>:<device-id>:<device-command>][,...]"
```

where:

* parts in brackets `[ ]` signify optional information
* `<openHAB-command>` is a OpenHAB command (`INIT`, `ON`, `OFF`, `*`, etc.)
* `<device-id>` is the device which is introduced in the binding configuration (`livingroom-spa20` in above example)
* `<device-command>` corresponds to the Primare device message (`POWER_QUERY`, `VOLUME_DOWN`, etc). See complete list below.

### List of predefined Primare SP31/SP31.7/SPA20/SPA21 commands

- _Operate/Standby_
  - POWER_QUERY
  - POWER_TOGGLE
  - POWER_ON
  - POWER_OFF
- _Main input_
  - MAIN_INPUT_QUERY
  - MAIN_INPUT_UP
  - MAIN_INPUT_DOWN
  - MAIN_INPUT_SET
- _Volume_
  - VOLUME_QUERY
  - VOLUME_UP
  - VOLUME_DOWN
  - VOLUME_SET
- _Balance_
  - BALANCE_QUERY
  - BALANCE_UP
  - BALANCE_DOWN
  - BALANCE_SET
- _Center_
  - CENTER_QUERY
  - CENTER_UP
  - CENTER_DOWN
  - CENTER_SET
- _Surround_
  - SURROUND_QUERY
  - SURROUND_UP
  - SURROUND_DOWN
  - SURROUND_SET
- _Back_
  - BACK_QUERY
  - BACK_UP
  - BACK_DOWN
  - BACK_SET
- _Sub_
  - SUB_QUERY
  - SUB_UP
  - SUB_DOWN
  - SUB_SET
- _Mute_
  - MUTE_QUERY
  - MUTE_TOGGLE
  - MUTE_ON
  - MUTE_OFF
- _Dim_
  - DIM_QUERY
  - DIM_TOGGLE
  - DIM_ON
  - DIM_OFF
- _Record input_
  - RECORD_INPUT_QUERY
  - RECORD_INPUT_UP
  - RECORD_INPUT_DOWN
  - RECORD_INPUT_SET
- _Surround mode_
  - SURROUND_MODE_QUERY
  - SURROUND_MODE_UP
  - SURROUND_MODE_SET
- _Verbose_
  - VERBOSE_QUERY
  - VERBOSE_TOGGLE
  - VERBOSE_ON
  - VERBOSE_OFF
- _Menu_
  - MENU_QUERY
  - MENU_TOGGLE
  - MENU_SET
- _Extra surround mode_
  - EXTRA_SURROUND_MODE_QUERY
  - EXTRA_SURROUND_MODE_TOGGLE
  - EXTRA_SURROUND_MODE_ON
  - EXTRA_SURROUND_MODE_OFF
- _Front panel lock_
  - FRONT_PANEL_LOCK_QUERY
  - FRONT_PANEL_LOCK_TOGGLE
  - FRONT_PANEL_LOCK_ON
  - FRONT_PANEL_LOCK_OFF
- _IR input select_
  - IR_INPUT_QUERY
  - IR_INPUT_TOGGLE
  - IR_INPUT_FRONT
  - IR_INPUT_BACK
- _Recall mem_
  - RECALL_MEMORY
  - RECALL_MEMORY_DIRECT_USER_SETTINGS
  - RECALL_MEMORY_DIRECT_FACTORY_SETTINGS
  - RECALL_MEMORY_DIRECT_INSTALLER_SETTINGS
- _Input name_
  - CURRENT_INPUT_NAME_QUERY
- _Name of productline_
  - PRODUCTLINE_QUERY
- _Model_
  - MODEL_QUERY
- _Version_
  - SW_VERSION_QUERY
- _Late night mode_
  - LATE_NIGHT_MODE_QUERY
  - LATE_NIGHT_MODE_TOGGLE
  - LATE_NIGHT_MODE_ON
  - LATE_NIGHT_MODE_OFF
- _Query all variables_
  - ALL_QUERY;

## Examples

items/spa20.items

```
// A dummy entry for querying all status variables of device
// Useful in rules when a full status sync is required
String livingroomSpa20 {primare="INIT:livingroom-spa20:ALL_QUERY, SYNCINPUT:livingroom-spa20:CURRENT_INPUT_NAME_QUERY, SYNCSURR:livingroom-spa20:SURROUND_MODE_QUERY"}

Switch livingroomSpa20Power             "01 Power [%s]"               {primare="INIT:livingroom-spa20:POWER_QUERY, ON:livingroom-spa20:POWER_ON, OFF:livingroom-spa20:POWER_OFF"}
Dimmer livingroomSpa20Input             "02 Input [%.0f]"             {primare="INIT:livingroom-spa20:MAIN_INPUT_QUERY, INCREASE:livingroom-spa20:MAIN_INPUT_UP, DECREASE:livingroom-spa20:MAIN_INPUT_DOWN"}
Dimmer livingroomSpa20Volume            "03 Volume [%.0f/100]"        {primare="INIT:livingroom-spa20:VOLUME_QUERY, INCREASE:livingroom-spa20:VOLUME_UP, DECREASE:livingroom-spa20:VOLUME_DOWN, *:livingroom-spa20:VOLUME_SET"}
Number livingroomSpa20Balance           "04 Balance [%.0f]"           {primare="INIT:livingroom-spa20:BALANCE_QUERY, *:livingroom-spa20:BALANCE_SET"}
Number livingroomSpa20Center            "05 Center [%.0f/100]"        {primare="INIT:livingroom-spa20:CENTER_QUERY, *:livingroom-spa20:CENTER_SET"}
Number livingroomSpa20Surround          "06 Surround [%.0f/100]"      {primare="INIT:livingroom-spa20:SURROUND_QUERY, *:livingroom-spa20:SURROUND_SET"}
Number livingroomSpa20Back              "07 Back [%.0f/100]"          {primare="INIT:livingroom-spa20:BACK_QUERY, *:livingroom-spa20:BACK_SET"}
Number livingroomSpa20Sub               "08 Sub [%.0f/100]"           {primare="INIT:livingroom-spa20:SUB_QUERY, *:livingroom-spa20:SUB_SET"}
Switch livingroomSpa20Mute              "09 Mute [%s]"                {primare="INIT:livingroom-spa20:MUTE_QUERY, ON:livingroom-spa20:MUTE_ON, OFF:livingroom-spa20:MUTE_OFF"}
Switch livingroomSpa20Dim               "10 Dim [%s]"                 {primare="INIT:livingroom-spa20:DIM_QUERY, ON:livingroom-spa20:DIM_ON, OFF:livingroom-spa20:DIM_OFF"}
Number livingroomSpa20RecordInput       "11 Record input [%.0f]"      {primare="INIT:livingroom-spa20:RECORD_INPUT_QUERY"}
Number livingroomSpa20SurroundMode      "12 Surround mode [%.0f]"     {primare="INIT:livingroom-spa20:SURROUND_MODE_QUERY, *:livingroom-spa20:SURROUND_MODE_SET"}
Switch livingroomSpa20Verbose           "13 Verbose mode [%s]"        {primare="INIT:livingroom-spa20:VERBOSE_QUERY, ON:livingroom-spa20:VERBOSE_ON, OFF:livingroom-spa20:VERBOSE_OFF"}
Switch livingroomSpa20ExtraSurroundMode "16 Extra surround mode [%s]" {primare="INIT:livingroom-spa20:EXTRA_SURROUND_MODE_QUERY, ON:livingroom-spa20:EXTRA_SURROUND_MODE_ON, OFF:livingroom-spa20:EXTRA_SURROUND_MODE_OFF"}
Switch livingroomSpa20FrontPanelLock    "17 Front Panel lock [%s]"    {primare="INIT:livingroom-spa20:FRONT_PANEL_LOCK_QUERY, ON:livingroom-spa20:FRONT_PANEL_LOCK_ON, OFF:livingroom-spa20:FRONT_PANEL_LOCK_OFF"}
Number livingroomSpa20IrInput           "18 IR input [%.0f]"          {primare="INIT:livingroom-spa20:IR_INPUT_QUERY"}
String livingroomSpa20CurrentInput      "20 Current input name [%s]"  {primare="INIT:livingroom-spa20:CURRENT_INPUT_NAME_QUERY"}
String livingroomSpa20Productline       "21 Productline [%s]"         {primare="INIT:livingroom-spa20:PRODUCTLINE_QUERY"}
String livingroomSpa20Model             "22 Model [%s]"               {primare="INIT:livingroom-spa20:MODEL_QUERY"}
String livingroomSpa20SwVersion         "23 SW version [%s]"          {primare="INIT:livingroom-spa20:SW_VERSION_QUERY"}
Switch livingroomSpa20LateNightMode     "25 Late Night mode [%s]"     {primare="INIT:livingroom-spa20:LATE_NIGHT_MODE_QUERY, ON:livingroom-spa20:LATE_NIGHT_MODE_ON, OFF:livingroom-spa20:LATE_NIGHT_MODE_OFF"}
```

sitemaps/spa20.sitemap

```
sitemap spa20 label="Primare SPA20 binding test"
{
    Frame label="Primare Livingroom SPA20 test"  {
        Switch item=livingroomSpa20Power
        Slider item=livingroomSpa20Input
        Slider item=livingroomSpa20Volume
        Setpoint item=livingroomSpa20Balance step=1 minValue=-20 maxValue=20
        Setpoint item=livingroomSpa20Center   step=1 minValue=-20 maxValue=20
        Setpoint item=livingroomSpa20Surround step=1 minValue=-20 maxValue=20
        Setpoint item=livingroomSpa20Back     step=1 minValue=-20 maxValue=20
        Setpoint item=livingroomSpa20Sub      step=1 minValue=-20 maxValue=20
        Switch item=livingroomSpa20Mute
        Switch item=livingroomSpa20Dim
        Text   item=livingroomSpa20RecordInput
        Selection item=livingroomSpa20SurroundMode mappings=[99=Bypass, 0=Stereo, 1="ProLogicII PL emulation", 2="ProLogicII Cinema", 3="ProLogicII Music", 4="Party", 5="DTS NEO:6 Cinema", 6="DTS NEO:6 Music"]
        Switch item=livingroomSpa20Verbose
        Switch item=livingroomSpa20ExtraSurroundMode
        Switch item=livingroomSpa20FrontPanelLock
        Text   item=livingroomSpa20IrInput
        Text   item=livingroomSpa20CurrentInput
        Text item=livingroomSpa20Productline
        Text item=livingroomSpa20Model
        Text item=livingroomSpa20SwVersion
        Switch item=livingroomSpa20LateNightMode
     }
}
```

rules/spa20.rules

```
/*
  Query full status after power on (from standby)
  Reason: after power on (standby off), some device variables are reset,
          and OpenHAB item values are out of sync.
 */
rule "Query full status after power on (from standby)"
when
    Item livingroomSpa20Power changed from OFF to ON
then
    sendCommand(livingroomSpa20, "INIT")  // Query all variables
end

/*
  Query current input name after current input update

  Reason: device does not automatically send updates for affected variables
  when current input (number) is changed, so an explicit rule is required
 */
rule "Query current input name after current input update"
when
    Item livingroomSpa20Input changed
then
    // Do not use item livingroomSpa20CurrentInput directly since it
    // displays command name (SYNCINPUT) briefly as the value of item
    sendCommand(livingroomSpa20, "SYNCINPUT")
    sendCommand(livingroomSpa20, "SYNCSURR")
end
```

## Known Issues

* In some cases, such as Power on or input status change, the Primare device does not automatically send status messages for all affected system variables, but they need to be queried separately. Currently this is must be handled by rules (see example above).

* The serial port might not be visible to the binding. A workaround is to add `-Dgnu.io.rxtx.SerialPorts=/dev/YOURPORTDEVICE` java option to openHAB startup script.
