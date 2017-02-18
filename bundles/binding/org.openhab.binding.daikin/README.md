# Daikin Binding

The Daikin binding allows monitoring and control of a Daikin air conditioner or heat pump, through either the KKRP01A wired module or the BRP072A42 wireless module.

## Prerequisites

A [KKRP01A](http://www.onlinecontroller.eu/) Online Controller must be
installed in the Daikin unit and connected to the LAN. 

The user authentication feature is not supported by the Daikin binding.
Therefore the Daikin unit must be configured with no security (ie. empty
password for all logins).

There is a list of units that are compatible with the KKRP01A
[here](http://www.onlinecontroller.eu/media/downloads/List-of-compatible-INDOOR-and-OUTDOOR-units-4.pdf)
and instructions on how to install and configure the controller [here](http://www.onlinecontroller.eu/en/download).


## Binding Configuration

The binding can be configured in the file `services/daikin.cfg`.

openHAB needs to know where to find the KKRP01A web server and how often the
binding should refresh any status items.

Multiple KKRP01A units can be configured by giving each a unique name.

| Property           | Default | Required | Description                                  |
|--------------------|---------|:--------:|----------------------------------------------|
| `<name>`.host      |         | Yes      | `<name>` is a unique name for the Daikin unit, also used in item bindings.<br/> The value of this setting must be formatted as `<which>`@`<address>`.<br/> `<which>` refers to the Daikin Remote Control Device, and must be either WIRELESS or WIRED.<br/> `<address>` is the IP address or hostname of the Daikin unit. |
| refresh            | 60000   | No       | The refresh interval (in milliseconds)       |


## Item Configuration

Item bindings can be either inbound or outbound. 


## Examples

### Inbound (readonly) item bindings

    // the temperature/humidity at the indoor unit
    Number  DaikinTempIn      "Temp Inside [%.1f °C]"      { daikin="<name>:tempin" }
    Number  DaikinHumidityIn  "Humidity Inside [%.1f %%]"  { daikin="<name>:humidityin" }

    // the temperature at the outdoor unit
    Number  DaikinTempOut     "Temp Outside [%.1f °C]"     { daikin="<name>:tempout" }
 
### Outbound (command) item bindings

    // power
    Switch  DaikinPower  "Power"           { daikin="<name>:power" }
 
    // mode of operation - one of Auto/Dry/Cool/Heat/Fan/Night
    Number DaikinMode   "Mode [%.0f]"       { daikin="<name>:mode" }
 
    // temperature set point
    Number  DaikinTemp   "Temp [%.0f °C]"  { daikin="<name>:temp" }
 
    // fan mode - one of Auto/F1/F2/F3/F4/F5
    Number DaikinFan    "Fan [%.0f]"        { daikin="<name>:fan" }
 
    // swing mode - one of Off/UpDown
    Number DaikinSwing  "Swing [%.0f]"      { daikin="<name>:swing" }
 
    // timer mode - one of Off-Off/Off-On/On-Off/On-On (start/end timers)
    Number DaikinTimer  "Timer [%.0f]"      { daikin="<name>:timer" }

### Sitemap Examples

    Text item=DaikinTempOut
    Switch item=DaikinPower
    Text item=DaikinTemp
    Text item=DaikinHumidityIn
    Switch item=DaikinMode mappings=[0="Auto", 2="Dry", 3="Cool", 4="Heat", 6="Fan"]
    Setpoint item=DaikinTemp minValue="16" maxValue="30" step="1"
    Switch item=DaikinFan mappings=[0="Auto", 1="F1", 2="F2", 3="F3", 4="F4", 5="F5"]
    Switch item=DaikinSwing mappings=[0="Off", 1="Vertical", 2="Horizontal", 3="Hor/Vert"]
