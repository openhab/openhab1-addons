# MAX!CUL Binding

The aim of this binding is to allow the connection from openHAB to MAX! devices (wall thermostat/radiator valves) using the [CUL USB dongle](http://busware.de/tiki-index.php?page=CUL) rather than the MAX!Cube. This should allow greater control over the devices than the cube offers as all interaction is handled manually.

A lot of credit must go to the [FHEM project](http://fhem.de/fhem.html): without their implementation of the MAX interface with CUL this would be taking a lot longer to implement!

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/max/).

## Status

The binding is currently in beta and it is recommended that you only use it expecting there to be bugs and issues. It is has enough features to be useful as a heating system, though lacks some of the finer features. This page will be updated as things progress.

## Tutorial

There is a tutorial using a Raspberry Pi available at [technpol](https://technpol.wordpress.com/2016/04/09/configuration-of-maxcul-and-cul-dongle/), which addresses some of the configuration issues and how to pair.  It's not clear that I'm doing pairing right (it seems very manual) but it does work.

## Features

The binding currently offers the following features:

* Listen mode - this allows you to listen in on MAX! network activity from a MAX!Cube for example. A trace will be output in debug mode that decodes implemented messages
* Pairing - can pair devices with OpenHAB by triggering Pair Mode using a Switch item
* Wall Thermostat
 * Can send set point temperature
 * Can receive set point temperature
 * Can receive measured temperature 
 * Can receive battery status
 * Can receive operating mode
 * Can factory reset device
 * Can be configured to display current temperature or current setpoint (_likely 1.8.0+_)
* Radiator Thermostat Valve
 * Can send set point temperature
 * Can receive set point temperature
 * Can receive measured temperature
 * Can receive valve position
 * Can receive battery status
 * Can receive operating mode
 * Can factory reset device
* Push Button
 * Can receive either AUTO or ECO depending on button press (translated to ON/OFF)
 * Can factory reset device
* Association
 * It is possible to link devices together so that they communicate directly with each other, for example a wall thermostat and a radiator valve.
* TX Credit Monitoring
 * It is possible to report TX credits from the CUL device via an item binding.

## Limitations

Aside from understanding what the binding does do which is documented here there are some key things to be aware of that may limit what you hope to achieve.

1. Radiator thermostat data is updated quite sporadically. Items such as set point temperature, measured temperature, valve position, battery status and operating mode are only sent when the state of the valve changes - i.e. valve moves or the dial used to manually set a temperature. If you want measured temperature it is much better to use a wall thermostat.
1. The binding has no concept of 'auto' mode: It currently has no ability to retrieve from any source and subsequently send a schedule to devices. This may change in the future, which would allow basic operation should OpenHAB fail for some reason.
1. If a wall thermostat is set to 'OFF' (mapped to 4.5deg) it won't update the measured temperature.


## Binding Configuration

This binding can be configured in the `services/maxcul.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| device   |         |   Yes    | in the form `serial:<device>`, where `<device>` is a local serial port, or<br/> `network:<host>:<port>`, where `<host>` is the host name or IP address and `<port>` is the port number.  The `network` option works with ser2net from a tuxnet device |
| baudrate |         |   No     | one of 75, 110, 300, 1200, 2400, 4800, 9600, 19200, **38400**, 57600, 115200 |
| parity   |         |   No     | one of EVEN, ODD, MARK, NONE, SPACE or **0** |
| refreshInterval | |    No     | refresh interval in milliseconds |
| timezone | Europe/London | No | set timezone you want the units to be set to |

When using a serial port, you may need to add `-Dgnu.io.rxtx.SerialPorts=/dev/ttyACM0` in your server startup.  Please consult the [forum](https://community.openhab.org) for the latest information.


Create a directory `maxcul` in the `etc` directory of your openHAB installation (e.g. `/usr/share/openhab2/etc/`) and make it writeable for the user account under which the openHAB is running.

## Item Configuration

Some quick examples:

```
Number RadTherm1 { maxcul="RadiatorThermostat:JEQ1234565" }
```

will return/set the thermostat temperature of radiator thermostat with the serial number JEQ1234565

```
Number RadThermBatt { maxcul="RadiatorThermostat:JEQ1234565:feature=battery" }
```

will return the battery level of radiator thermostat with the serial number JEQ0304492, if supported by the thermostat

```
Number wallThermTemp { maxcul="WallThermostat:JEQ1234566:feature=temperature" }
```

will return the measured temperature of a wall mounted thermostat with serial number JEQ1234566

```
Number wallThermSet { maxcul="WallThermostat:JEQ1234566:feature=thermostat" }
```

will set/return the desired temperature of a wall mounted thermostat with serial number JEQ1234566

```
Switch pushBtn { maxcul="PushButton:JEQ1234567" }
```

ON maps to Auto, OFF maps to Eco of the push button with serial number JEQ1234567

```
Switch pair { maxcul="PairMode" }
```

Switch only, ON enables pair mode for 60s. Will automatically switch off after this time.

```
Switch listen { maxcul="ListenMode" }
```

Switch only, puts binding into mode where it doesn't process messages - just listens to traffic, parses and outputs it in the log. Mainly used for debugging and checking behaviours. Can monitor devices associated with another controller, e.g a Max! Cube.

```
Number txCredit { maxcul="CreditMonitor" }
```

Will be updated with the latest value for the TX credit whenever it receives an update or command to a maxcul binding item. This number is used to adhere to the 1% transmission time rule. The 1% rule is enforced by the CUL USB device firmware.

```
Switch heating_display { maxcul="WallThermostat:JEQ1234567:feature=displaySetting" }
```

This allows the wall thermostat display to be switched between actual temperature ("ON") and setpoint temperature ("OFF").

## Additional options

### feature

The following devices have the following valid features:

* RadiatorThermostat - `thermostat` (default),`temperature`,`battery`,`valvepos`,`reset`
* WallThermostat - `thermostat` (default),`temperature`,`battery`,`reset`,`displaySetting`
* PushButton - `switch`,`reset`

Example:

```
Number wallThermTemp { maxcul="WallThermostat:JEQ1234566:feature=temperature" }
```

### configTemp

There is the option of the addition of `configTemp=20.0/15.0/30.5/4.5/4.5/0.0/0.0` at the end of a thermostat device binding (wall or radiator) will allow the setting of comfort/eco/max/min/windowOpenDetectTemp/windowOpenDetectTime/measurementOffset respectively. It's best to set this on only one binding of each device - if you set this on more than one binding for the same device then it will take the first one in the parsing order (whatever that is - hence generating some uncertainty!). These correspond to the following:

* comfort - the defined 'comfort' temperature (default 21.0)
* eco - the defined eco setback temperature (default 17.0)
* max - maximum temperature that can be set on the thermostat (default 30.5, which is the maximum value and corresponds to "open valve")
* min - minimum temperature that can be set on the thermostat (default 4.5, which is the minimum value and corresponds to "closed valve")
* windowOpenDetectTemp - set point in the event that a window open event is triggered by a shutter, if set to 4.5, this function is deactivated.
* windowOpenDetectTime - Rounded down to the nearest 5 minutes. (default is 0)
* measurement offset - offset applied to measure temperature (range is -3.5 to +3.5) - default is 0.0

Example:

```
Number wallThermDesired { maxcul="WallThermostat:KEQ0946847:feature=thermostat:configTemp=20.0/15.0/30.5/4.5/4.5/0.0/0.0" }
```

### associate

Association allows you to link two items together. For example you might want to link a Wall Thermostat and a Radiator Thermostat together. This would have the effect that you don't need rules to keep the set point temperature synchronised as it is communicated directly by the devices. It also means that the radiator thermostat will use the measured temperature from the wall thermostat.

The devices must be associated both ways. The binding doesn't do this automatically (though it could in the future).

Example:

``
Number heating_radvalve  "Valve Setpoint [%.1f °C]" { maxcul="RadiatorThermostat:KEQ1234561:associate=KEQ1234560" }
Number heating_wallThermMeasured "Wall Meas [%.1f °C]" { maxcul="WallThermostat:KEQ1234560:feature=temperature:associate=KEQ1234561" }
``

The binding allows more than one association per device. They just need to be comma separated. Example:

```
Number heating_wallThermMeasured "Wall Meas [%.1f °C]" { maxcul="WallThermostat:KEQ1234560:feature=temperature:associate=KEQ1234561,KEQ1234562" }
```

## Pairing

A device needs to be associated with the Max!CUL binding to work correctly. This is a simple process:

1. Ensure you have an item that has the correct device serial and settings you want configured in openhab
1. If you haven't already then create a separate item and sitemap entry that is a switch that allows you to turn on pairing mode (NB. it will turn off automatically after 30s)
1. Switch on pairing mode
1. Once pairing mode is activated then you need to pair the device by pressing and holding the pairing button the device (see your device manual). You should see it start to count down a timer from 30. Once the pairing process has begun then you will see AC displayed (on Wall and Radiator thermostats at least) or for devices without a display the LED will flash as described in the manual.

**Please note:** If the device has been paired before you will need to factory reset it before use. Please see the device user manual for details on how to do this.

## Technical Information

### Implemented Messages
The table below shows what messages are implemented and to what extent. Transmit means we can build and transmit a packet of that type with relevant data. Decode means we can extract data into some meaningful form. All message types can be received, identified and the raw payloads displayed. Messages not identified in this table cannot be transmitted by the binding and can only be decoded as a raw payload.

| Message               | Transmit | Decode           |Comments                                    |
|-----------------------|:--------:|:----------------:|--------------------------------------------|
|ACK                      | Y        | Y                |                                            |
|PAIR PING                | N        | Y                |                                            |
|PAIR PONG                | Y        | Y                |                                            |
|SET GROUP ID             | Y        | Y                |                                            |
|SET TEMPERATURE          | Y        | Y                | Allows setting of temperature of (wall)therm |
|TIME INFO                | Y        | Y                |                                            |
|WAKEUP                   | Y        | N                |                                            |
|WALL THERMOSTAT CONTROL  | N        | Y                | Provides measured temp and set point       |
|THERMOSTAT STATE         | N        | Y                | Provides battery/valvepos/temperature/thermostat set point |
| WALL THERMOSTAT STATE   | N        | Y                | Provides battery/valvepos/temperature/thermostat set point |
| PUSH BUTTON STATE       | N        | Y                | Auto maps to ON, Eco maps to OFF           |
| ADD LINK PARTNER        | Y        | N                | Links a device with another                |
| SET DISPLAY ACTUAL TEMP | Y        | Y                | Set a wall thermostat to show current measured or current setpoint temperature |

## Message Sequences

For situations such as the pairing where a whole sequences of messages is required the binding has implemented a message sequencing system. This allows the implementation of a state machine for the system to pass through as messages are passed back and forth.

This will be documented in more detail in due course.

## Planned Future Features

These are in no particular priority and are simply ideas. They may not get implemented at all.

1. If there is a pending SET_TEMPERATURE message in the queue and we receive a SET_TEMPERATURE from the thermostat we are waiting to send to then we should clear the message from the queue as it will be outdated.
1. Add the ability to interface with the window contact devices
1. Add the ability pretend to be a wall thermostat. This would allow us to associate with a radiator thermostat and send measured temperatures to it. These could be then sent from another binding for example.
1. Add the ability to simulate a window contact. This would allow us to associate with a radiator thermostat and send window events to it.
1. Explore how to avoid the queue getting too long due to lack of credits with many devices.
1. Add ability to set up device groups which should help reduce lack of credit issue
