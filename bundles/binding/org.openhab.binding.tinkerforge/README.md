# TinkerForge Binding

[TinkerForge](http://www.tinkerforge.com) is a system of open source hardware building blocks that allows you to combine sensor and actuator blocks by plug and play. You can create your individual hardware system by choosing the necessary building blocks for your project and combine it with other home automation products. 

There are many blocks available e.g for temperature, humidity or air pressure
measurement as well as for I/O, LCDs and motor control. You will find a complete List of available
blocks [here](http://www.tinkerforge.com/en/doc/Product_Overview.html).

This binding connects the [TinkerForge](http://tinkerforge.com) devices to the openHAB event bus.
Sensor values from devices are made available to openHAB and actions on devices can be triggered by
openHAB.

The binding supports the connection to several brickd instances.
The TinkerForge auto reconnect feature is supported. Furthermore even if the initial connect failed the binding will make retries to get connected to the brickd.

## Table of Contents

- [Generic Item Binding Configuration](#generic-item-binding-configuration)
    - [Basic Configuration](#basic-configuration)
    - [Advanced Configuration](#advanced-configuration)
- [Supported Devices](#supported-devices)
  - Bricks
    - [DC Brick](#dc-brick)
    - [Servo Brick](#servo-brick)
  - Bricklets
    - [Accelerometer Bricklet](#accelerometer-bricklet)
    - [Ambient Light Bricklet](#ambient-light-bricklet)
    - [Ambient Light Bricklet V2](#ambient-light-bricklet-v2)
    - [Analog In Bricklet](#analog-in-bricklet)
    - [Analog In Bricklet 2.0](#analog-in-bricklet-20)
    - [Analog Out Bricklet 2.0](#analog-out-bricklet-20)
    - [Barometer Bricklet](#barometer-bricklet)
    - [CO2 Bricklet](#co2-bricklet)
    - [Color Bricklet](#color-bricklet)
    - [Distance IR Bricklet](#distance-ir-bricklet)
    - [Distance US Bricklet](#distance-us-bricklet)
    - [Dual Button Bricklet](#dual-button-bricklet)
    - [Dual Relay Bricklet](#dual-relay-bricklet)
    - [Dust Detector Bricklet](#dust-detector-bricklet)
    - [Hall Effect Bricklet](#hall-effect-bricklet)
    - [Humidity Bricklet](#humidity-bricklet)
    - [Industrial Digital IN 4 Bricklet](#industrial-digital-in-4-bricklet)
    - [Industrial Digital Out 4 Bricklet](#industrial-digital-out-4-bricklet)
    - [Industrial Dual 0-20mA Bricklet](#industrial-dual-0-20ma-bricklet)
    - [Industrial Dual Analog In Bricklet](#industrial-dual-analog-in-bricklet)
    - [Industrial Quad Relay Bricklet](#industrial-quad-relay-bricklet)
    - [IO4 Bricklet](#io4-bricklet)
    - [IO 16 Bricklet](#io-16-bricklet)
    - [Joystick Bricklet](#joystick-bricklet)
    - [Laser Range Finder Bricklet](#laser-range-finder-bricklet)
    - [LCD 20x4 Display Bricklet](#lcd-20x4-display-bricklet)
    - [LED Strip Bricklet](#led-strip-bricklet)
    - [Linear Poti Bricklet](#linear-poti-bricklet)
    - [Load Cell Bricklet](#load-cell-bricklet)
    - [Motion Detector Bricklet](#motion-detector-bricklet)
    - [Multi Touch Bricklet](#multi-touch-bricklet)
    - [Moisture Bricklet](#moisture-bricklet)
    - [Piezo Speaker Bricklet](#piezo-speaker-bricklet)
    - [PTC Bricklet](#ptc-bricklet)
    - [Remote Switch Bricklet](#remote-switch-bricklet)
    - [Rotary Encoder Bricklet](#rotary-encoder-bricklet)
    - [Segment Display 4x7 Bricklet](#segment-display-4x7-bricklet)
    - [Solid State Relay Bricklet](#solid-state-relay-bricklet)
    - [Sound Intensity Bricklet](#sound-intensity-bricklet)
    - [Temperature Bricklet](#temperature-bricklet)
    - [Temperature IR Bricklet](#temperature-ir-bricklet)
    - [Tilt Bricklet](#tilt-bricklet)
    - [Thermocouple Bricklet](#thermocouple-bricklet)
    - [UV Light Bricklet](#uv-light-bricklet)
    - [Voltage/Current Bricklet](#voltagecurrent-bricklet)
  - Kits
    - [Weatherstation Kit](#weatherstation-kit)
  - Deamon
    - [Brick Daemon](#brick-daemon)
- [Red Brick](#red-brick)
- [Developer Notes](#developer-notes)

---

## Generic Item Binding Configuration

### Basic Configuration

In order to connect openHAB to TinkerForge devices you need to define all the brickd hosts and ports in the services/tinkerforge.cfg file.
The following properties must be configured to define a brickd connection:

```
hosts=<IP address>[:port][:<secret>] ...
```

The properties indicated by '<...>' need to be replaced with an actual value. Properties surrounded
by square brackets are optional. Several brickd configurations are delimited by a space.

*port* and secret are optional values. You must specify the port if your brickd doesn't use the default port 4223.
*secret* must be specified if you use a brickd connection **with authentication**:  (see also [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Software/Brickd.html#brickd-authentication))

| <b>Property</b> | <b>Description</b> |
| --------------- | ------------------ |
| IP address | IP address of the brickd |
| port | The listening port of of the brickd (optional, default 4223) |
| secret | password used for authentication |

#### Example configuration

Authenticate with password 1234 and default port

```
hosts=127.0.0.1::1234
```

Authenticate with password 1234 and individual port 4224

```
hosts=127.0.0.1:4224:1234
```

You may also configure several different hosts.
For connecting several brickds, use multiple &lt;IP address&gt; statements delimited by a space.

```
hosts=127.0.0.1:4224:1234 192.168.1.100::secret
```


#### Item Binding Configuration

In order to bind an item to a device, you need to provide configuration settings. The easiest way
to do so is to add binding information in your item file.
For location of the item file on a Linux based system see [File locations](http://docs.openhab.org/installation/linux.html#file-locations)


The configuration of the TinkerForge binding item looks like this:

```
tinkerforge="(uid=<your_id> [, subid=<your_subid>] | name=<your_name>)"
```

The configuration is quite simple. You either have to set a value for the uid and optionally for the
subid of the device, or - if the device is configured in services/tinkerforge.cfg - the "symbolic name" of the device.

| Property | Description |
| -------- | ----------- |
| uid      | TinkerForge uid of the device (Use the Brick Viewer to get this value) |
| subid    | optional subid of the device|
| name     | _symbolic name_ of the device. The name is only available if there is some configuration for the device in services/tinkerforge.cfg. |

For additional configuration options see the appropriate device section.

---
[Table of Contents](#table-of-contents)

### Advanced Configuration

There are several configuration parameters to control the behavior of the devices. The available
parameters depend on the device type.

#### Overview

For most of the devices **no configuration** is needed in services/tinkerforge.cfg, they can be used with reasonable defaults.

<a id="sym_name"></a>
If you want to get rid of _uid_ and _subid_ statements in the items or rule file, you can use tinkerforge.cfg to get a _symbolic name_.

A configuration line for a TinkerForge Device looks like this in services/tinkerforge.cfg:

```
<symbolic name>.<property>=<value>
```

The *symbolic name* string can be used in the items configuration as an alternative for the uid and subid values.
If you have more than one device of the same type you have to choose a unique *symbolic name* for every device.

The following table lists the general available properties.

|<b>Property</b>|<b>Description</b>|<b>Device</b>|
|---------------|------------------|-------------|
|uid|TinkerForge uid of the device (use the Brick Viewer to get this value)|mandatory for all devices|
|type|the device type|mandatory for all devices|
|subid|  subid of the device, subid's are used if a brick/bricklet houses more then one device (e.g. the Dual Relay Bricklet)|mandatory for sub devices|

The following table shows the TinkerForge device, its device type, its subid and if callback is supported.


|<b>device</b>|<b>type name</b>|<b>subid(s)</b>|<b>Callback</b>|
|-------------|----------------|---------------|---------------|
|DC Brick|brick_dc|||
|Servo Brick sub devices|servo|servo[0-6]||
|Accelerometer Bricklet|bricklet_accelerometer|||
|Accelerometer Bricklet subdevice|accelerometer_direction|x|x|
|Accelerometer Bricklet subdevice|accelerometer_direction|y|x|
|Accelerometer Bricklet subdevice|accelerometer_direction|z|x|
|Accelerometer Bricklet subdevice|accelerometer_temperature|temperature||
|Accelerometer Bricklet subdevice|accelerometer_led|led||
|Ambient Light Bricklet|bricklet_ambient_light||x|
|Ambient Light Bricklet 2.0|bricklet_ambient_lightv2||x|
|Analog In Bricklet|bricklet_analogin||x|
|Analog In Bricklet 2.0|bricklet_analoginv2||x|
|Analog Out Bricklet 2.0|bricklet_analog_out_v2||x|
|Barometer Bricklet|bricklet_barometer||x|
|Barometer Bricklet temperature sensor sub device|barometer_temperature|temperature||
|CO2 Bricklet|bricklet_co2||x|
|Color Bricklet|bricklet_color|||
|Color Bricklet subdevice|color|color-color|x|
|Color Bricklet subdevice|temperature|color_temperature|x|
|Color Bricklet subdevice|illuminance|color_illuminance|x|
|Distance IR Bricklet|bricklet_distance_ir||x|
|Dual Button Bricklet||||
|Dual Button Bricklet subdevice||dualbutton_leftled|x|
|Dual Button Bricklet subdevice||dualbutton_rightled|x|
|Dual Button Bricklet subdevice||dualbutton_leftbutton|x|
|Dual Button Bricklet subdevice||dualbutton_rightbutton|x|
|Dual Relay Bricklet sub devices|dual_relay|relay[1-2]||
|Dust Detector Bricklet|bricklet_dustdetector||x|
|Hall Effect Bricklet|bricklet_halleffect||x|
|Humidity Bricklet|bricklet_humidity||x|
|Industrial Digital In 4 Bricklet|bricklet_industrial_digital_4in|in[0-3]|x|
|Industrial Digital Out 4 Bricklet sub devices|bricklet_industrial_digital_4out|out[0-3]||
|Industrial Dual 0-20mA Bricklet|bricklet_industrialdual020ma|||
|Industrial Dual 0-20mA Bricklet subdevice|industrial020ma_sensor|sensor[0-1]|x|
|Industrial Dual Analog In Bricklet|bricklet_industrial_dual_analogin|||
|Industrial Dual Analog In Bricklet subdevice|industrial_dual_analogin_channel|channel[0-1]|x|
|Industrial Quad Relay Bricklet|industrial_quad_relay|relay[0-3]||
|IO-4 Bricklet|bricklet_io4|||
|IO-4 Bricklet sub devices, which should be used as input ports|io4sensor|in[0-3]|x|
|IO-4 Bricklet sub devices, which should be used as output ports|io4_actuator|out[0-3]||
|IO-16 Bricklet|bricklet_io16|||
|IO-16 Bricklet sub devices, which should be used as input ports|iosensor|in[ab][0-7]|x|
|IO-16 Bricklet sub devices, which should be used as output ports|io_actuator|out[ab][0-7]||
|Joystick Bricklet|bricklet_joystick|||
|Joystick Bricklet subdevice||joystick_xposition|x|
|Joystick Bricklet subdevice||joystick_yposition|x|
|Joystick Bricklet subdevice||joystick_button|x|
|Laser Range Finder Bricklet||bricklet_laser_range_finder||
|Laser Range Finder Bricklet subdevice|laser_range_finder_distance|distance|x|
|Laser Range Finder Bricklet subdevice|laser_range_finder_velocity|velocity|x|
|Laser Range Finder Bricklet subdevice|laser_range_finder_laser|laser||
|LCD20x4 Bricklet|bricklet_LCD20x4|||
|LCD20x4 backlight|backlight|backlight||
|LCD20x4 Bricklet button sub devices|lcd_button|button[0-3]|interrupt|
|LED Strip Bricklet|bricklet_ledstrip|||
|LED Strip Bricklet subdevice|ledgroup|ledgroup[1-x]|x|
|Linear Poti Bricklet|bricklet_linear_poti||x|
|Load Cell Bricklet|bricklet_loadcell|||
|Load Cell Bricklet subdevice|weight|loadcell_weight|x|
|Load Cell Bricklet subdevice|led|loadcell_led|x|
|Motion Detector Bricklet|motion_detector||x|
|Multi Touch Bricklet|bricklet_multitouch|||
|Multi Touch Bricklet electrodes|electrode|electrode[0-11]|x|
|Moisture Bricklet|bricklet_moisture||x|
|Pieco Speaker Bricklet||||
|PTC Bricklet|bricklet_ptc|||
|PTC Bricklet subdevice||ptc_temperature|x|
|PTC Bricklet subdevice||ptc_connected|x|
|PTC Bricklet subdevice||ptc_resistance|x|
|Remote Switch Bricklet|bricklet_remote_switch|configurable||
|Remote Switch Bricklet sub devices|remote_switch_a or remote_switch_b or remote_switch_c|from configuration||
|Rotary Encoder Bricklet||||
|Rotary Encoder Bricklet subdevice|encoder|rotary_encoder|x|
|Rotary Encoder Bricklet subdevice|button|rotary_encoder_button|x|
|Segment Display 4x7 Bricklet||||
|Solid State Relay Bricklet||||
|Sound Intensity Bricklet|bricklet_soundintensity|||
|Temperature Bricklet|bricklet_temperature||x|
|TemperatureIR Bricklet|bricklet_temperatureIR|||
|TemperatureIR Bricklet sub device object temperature|object_temperature||x|
|TemperatureIR Bricklet sub device ambient temperature|ambient_temperature||x|
|Tilt Bricklet|bricklet_tilt|||
|Thermocouple Bricklet|thermocouple||x|
|UV Light Bricklet|bricklet_uv_light||x|
|Voltage Current Bricklet|bricklet_voltageCurrent|||

---
[Table of Contents](#table-of-contents)

#### Callback and Threshold

The TinkerForge CallbackListeners - if available - are used to observe the sensor values of the
devices. These listeners are configured to update sensor values at a given time period
(callbackPeriod). The default configuration sets the **callbackPeriod** to 1 second. This value can
be changed in services/tinkerforge.cfg. The values must be given in milliseconds.

The callbackPeriod controls the amount of traffic from the TF hardware to the binding.

In addition to the Callback a **threshold value** can be configured. This threshold means that even
if the listener reports a changed value, the value is only send to the openHAB eventbus if: the
difference between the last value and the current value is bigger than the threshold value. You can
think of it as a kind of hysteresis, it dampens the oscillation of openHAB item values.

The threshold controls the amount of  traffic from the binding to the openHAB eventbus.
Threshold values have the same unit as sensor values, no conversion is needed.

---

#### Refresh of Sensor Values

Devices which do not support callbacks will be polled with a configurable interval, the default
is 60000 milliseconds. This value can be changed in services/tinkerforge.cfg:

```
refresh=<value in milliseconds>
```

---
[Table of Contents](#table-of-contents)

## Supported Devices

### DC Brick

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricks/DC_Brick.html#dc-brick)

#### Binding properties:

The device supports Dimmer, Rollershutter and Number items.  Besides that the speed
can be set using a percent value.
The number items show the current velocity. The values are reported using the VelocityListener.
"[callbackPeriod](#callback-and-threshold)" and "[threshold](#callback-and-threshold)" for the listener can be configured in services/tinkerforge.cfg.

* callbackPeriod: milliseconds
* threshold: numeric value

The item configuration options are:
* speed: the target speed (Switch)
* max: the maximum speed (Dimmer, Rollershutter)
* min: the minimum speed (Dimmer, Rollershutter)
* step: the step value for increasing decreasing speed (Dimmer)
* leftspeed: the speed when the left rollershutter controller is pressed or command "DOWN" was send
* rightspeed: the speed when the right rollershutter controller is pressed or command "UP" was send
* acceleration: acceleration overrides value from tinkerforge.cfg
* drivemode: drivemode overrides value from tinkerforge.cfg

##### tinkerforge.cfg:

Values for acceleration and drivmode are default values and may be overridden by item definition.

```
dc_garage.uid=<your_uid>
dc_garage.type=brick_dc
dc_garage.pwmFrequency=15000
dc_garage.driveMode=break
dc_garage.acceleration=10000
dc_garage.callbackPeriod=100
```

##### Items file entry (e.g. tinkerforge.items):

```
Dimmer  DCDIMMER  "Dimmer" {tinkerforge="uid=<your_uid>, max=20000, min=-15000, acceleration=10000, drivemode=brake, step=2500"}
Dimmer  DIMMERPERCENT  "Dimmerpercent" {tinkerforge="uid=<your_uid>, max=20000, min=0, acceleration=10000, drivemode=brake, step=2500"}
Rollershutter TF_ROLLER "Roller" {tinkerforge="uid=<your_uid>, leftspeed=10000, rightspeed=-10000, acceleration=10000, drivemode=brake"}
Switch DCSWITCH "DC Switch" {tinkerforge="uid=<your_uid>, speed=14000"}
Number DCSPEED "DC Speed [%.0f]"  {tinkerforge="uid=<your_uid>, max=20000, min=-15000, step=1000, leftspeed=10000, rightspeed=-10000, acceleration=10000, drivemode=brake"}
Dimmer  RULEDIMMER  "RuleDimmer"
Switch  DCMOVE  "Action Move"
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf_weather label="Brick DC"
{
  Frame {
        Switch item=TF_ROLLER
        Switch item=DCSWITCH
        Slider item=DCDIMMER
        Text item=DCSPEED
        Setpoint item=DCSPEED minValue=-1500 maxValue=25000 step=1000
        Slider item=RULEDIMMER
        }
}
```

##### Rules file (e.g. tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "percentdimmer"
    when
        Item RULEDIMMER received command INCREASE
    then
       sendCommand(DIMMERPERCENT, new PercentType(100))
end

rule "percentdimmerdecrease"
    when
        Item RULEDIMMER received command DECREASE
    then
       sendCommand(DIMMERPERCENT, new PercentType(25))
end


rule "move motor"
    when
        Item DCMOVE received command ON
    then
       var Integer acceleration = 10000
       var Short speed = 15000
       tfDCMotorSetspeed("<your_uid>", speed, acceleration, "break")
       Thread::sleep(1000)
       tfDCMotorSetspeed("<your_uid>", speed, acceleration, "break")
       Thread::sleep(1000)
       tfDCMotorSetspeed("<your_uid>", speed, acceleration, "break")
       Thread::sleep(1000)
       tfDCMotorSetspeed("<your_uid>", speed, acceleration, "break")
end

```

---
[Table of Contents](#table-of-contents)

### Servo Brick

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricks/Servo_Brick.html#servo-brick)

#### Binding properties:

The device supports Dimmer, Rollershuter and Number items. Besides that the speed
can be set using a percent value.

Number items will show the current position.

The item configuration options are:
* velocity: the velocity used to reach the new position
* max: the maximum position (Dimmer, Rollershutter)
* min: the minimum position (Dimmer, Rollershutter)
* step: the step value for increasing decreasing position (Dimmer)
* leftposition: the target position to reach when the left rollershutter controller is pressed or command "DOWN" was send
* rightposition: the target position to reach when the right rollershutter controller is pressed or command "UP" was send
* acceleration: the acceleration

##### TinkerForge Action

The openHAB action [TinkerforgeAction](#tinkerforge-actions) comes up with the action tfServoSetposition.
tfServoSetposition(uid, num, position, velocity, acceleration) can be used to control the servo.

###### Example

```
tfServoSetposition("<your_uid>", "servo0", "-9000", "65535", "65535")
```

##### Brick:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | brick_servo |

##### Servo sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as brick |
| subid | openHAB subid of the device | servo0, servo1, servo2, servo3, servo4, servo5, servo6 |
| type | openHAB type name | servo |
| velocity | | default=30000 |
| acceleration | | default=30000 |
| servoVoltage | | |
| pulseWidthMin | | default=1000 |
| pulseWidthMax | | default=2000 |
| period | | default=19500 |
| outputVoltage | output voltage can only be set once (will be used for all servos) | default=5000 |

##### tinkerforge.cfg:

```
servo0.uid=<your_uid>
servo0.type=servo
servo0.subid=servo0
servo0.velocity=65530
servo0.acceleration=65530

servo0.uid=<your_uid>
servo1.type=servo
servo1.subid=servo1
servo1.velocity=65530
servo1.acceleration=65530

servo0.uid=<your_uid>
servo2.type=servo
servo2.subid=servo2
servo2.velocity=65530
servo2.acceleration=65530

servo0.uid=<your_uid>
servo3.type=servo
servo3.subid=servo3
servo3.velocity=65530
servo3.acceleration=65530

servo0.uid=<your_uid>
servo4.type=servo
servo4.subid=servo4
servo4.velocity=65530
servo4.acceleration=65530

servo0.uid=<your_uid>
servo5.type=servo
servo5.subid=servo5
servo5.velocity=65530
servo5.acceleration=65530

servo0.uid=<your_uid>
servo6.type=servo
servo6.subid=servo6
servo6.velocity=65530
servo6.acceleration=65530

```

##### Items file entry (e.g. tinkerforge.items):

```
Dimmer Servo0              "Servo0" { tinkerforge="uid=<your_uid>, subid=servo0, max=9000, min=-9000, step=500, acceleration=3000, velocity=3000" }
Dimmer Servo0Percent              "Servo0Percent" { tinkerforge="uid=<your_uid>, subid=servo0, max=9000, min=-9000, step=500, acceleration=3000, velocity=3000" }
Rollershutter Servo0Rollershutter              "Servo0Rollershutter" { tinkerforge="uid=<your_uid>, subid=servo0, max=9000, min=-9000, leftposition=3000, rightposition=-3000, acceleration=3000, velocity=3000" }
Switch Servo0Switch              "Servo0Switch" { tinkerforge="uid=<your_uid>, subid=servo0, max=9000, min=-9000, position=3000, acceleration=3000, velocity=3000" }
Number Servo0Postion              "Servo0Postion [%.0f]" { tinkerforge="uid=<your_uid>, subid=servo0, max=9000, min=-9000, step=500, acceleration=3000, velocity=3000" }
Dimmer Servo0Rule "Servo0Rule"

Switch Servo1              "Servo1" { tinkerforge="uid=<your_uid>, subid=servo1" }
Switch Servo2              "Servo2" { tinkerforge="uid=<your_uid>, subid=servo2" }
Switch Servo3              "Servo3" { tinkerforge="uid=<your_uid>, subid=servo3" }
Switch Servo4              "Servo4" { tinkerforge="uid=<your_uid>, subid=servo4" }
Switch Servo5              "Servo5" { tinkerforge="uid=<your_uid>, subid=servo5" }
Switch Servo6              "Servo6" { tinkerforge="uid=<your_uid>, subid=servo6" }

Switch ClearLCD            "ClearLCD"

Switch MoveServo "MoveServo" {autoupdate="false"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf_weather label="Brick Servo"
{
  Frame {
    Slider item=Servo0
    Switch item=Servo0Rollershutter
    Switch item=Servo0Switch
    Text item=Servo0Postion
    Setpoint item=Servo0Postion minValue=-9000 maxValue=6000 step=2000
    Slider item=Servo0Rule

    Switch item=ClearLCD
    Switch item=MoveServo

    Switch item=Servo1
    Switch item=Servo2
    Switch item=Servo3
    Switch item=Servo4
    Switch item=Servo5
    Switch item=Servo6
    }
}

```

---
[Table of Contents](#table-of-contents)

### Accelerometer Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Accelerometer.html)

#### Binding properties:

The bricklet returns the acceleration in x, y and z direction. The values are given in g/1000 (1g = 9.80665m/s²).  
Bricklet temperature can be measured in degrees Celsius, LED can be turned on to indicate a specific acceleration was reached.  
Decreasing data rate or full scale range will also decrease the noise on the data.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_accelerometer |
| dataRate | sets the data rate, default=6 (100Hz) |0: off, 1: 3hz, 2: 6hz, 3: 12hz, 4: 25hz, 5: 50hz, 6: 100hz, 7: 400hz, 8: 800hz, 9: 1600hz|
| fullScale | sets the scale rate, default=1 (-4g to +4g) |0: 2g, 1: 4g, 2: 6g, 3: 8g, 4: 16g|
| filterBandwidth | sets the filter bandwidth, default=2 (200Hz) | 0: 800hz, 1: 400hz, 2: 200hz, 3: 50hz|

##### Accelerometer Bricklet sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | x, y, z, temperature, led |
| type | openHAB type name | accelerometer_direction, accelerometer_temperature, accelerometer_led|
| callbackPeriod | | see "Callback and Threshold" |

Note: Subdevices accelerometer_temperature and accelerometer_led don't support callbackPeriod!


##### tinkerforge.cfg:

```
accelerometer.uid=<your_uid>
accelerometer.type=bricklet_accelerometer
accelerometer.dataRate=6
accelerometer.fullScale=1
accelerometer.filterBandwidth=2

ax.uid=<your_uid>
ax.subid=x
ax.type=accelerometer_direction
ax.callbackPeriod=10
ax.threshold=0

ay.uid=<your_uid>
ay.subid=y
ay.type=accelerometer_direction
ay.callbackPeriod=10
ay.threshold=0

az.uid=<your_uid>
az.subid=z
az.type=accelerometer_direction
az.callbackPeriod=10
az.threshold=0

a_temperature.uid=<your_uid>
a_temperature.subid=temperature
a_temperature.type=accelerometer_temperature

a_led.uid=<your_uid>
a_led.subid=led
a_led.type=accelerometer_led
```

##### Items file entry (e.g. tinkerforge.items):

```
Number X "X [%.3f]" {tinkerforge="uid=<your_uid>, subid=x"}
Number Y "Y [%.3f]" {tinkerforge="uid=<your_uid>, subid=y"}
Number Z "Z [%.3f]" {tinkerforge="uid=<your_uid>, subid=z"}
Number temperature "Temperature [%.2f °C]" {tinkerforge="uid=<your_uid>, subid=temperature"}
Switch led "Led"  {tinkerforge="uid=<your_uid>, subid=led"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Accelerometer"
{
    Frame label="Accelerometer" {
        Text item=X
        Text item=Y
        Text item=Z
        Text item=temperature
        Switch item=led
    }
}

```

---
[Table of Contents](#table-of-contents)

### Ambient Light Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Ambient_Light.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold).

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_ambient_light |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
ambientlight.uid=<your_uid>
ambientlight.type=bricklet_ambient_light
ambientlight.callbackPeriod=10
ambientlight.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number TF_AmbientLight "Luminance [%.1f Lux]" { tinkerforge="uid=<your_uid>" }

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge AmbientLight"
{
    Frame label="AmbientLight" {
        Text item=TF_AmbientLight
    }
}

```

---

### Ambient Light Bricklet V2

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Ambient_Light_V2.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_ambient_lightv2 |
| illuminanceRange | sets the illuminance range, default=8000lux | 0=64000lux, 1=32000lux, 2=16000lux, 3=8000lux, 4=1300lux, 5=600lux, 6=unlimited range |
| integrationTime | sets the integration time, default=200ms | 0=50ms, 1=100ms, 2=150ms, 3=200ms, 4=250ms, 5=300ms, 6=350ms, 7=400ms|
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
ambientlightv2.uid=<your_uid>
ambientlightv2.type=bricklet_ambient_lightv2
ambientlightv2.illuminanceRange=3
ambientlightv2.integrationTime=3
ambientlightv2.callbackPeriod=10
ambientlightv2.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number illuminance "Illuminance [%.2f]" {tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge AmbientLightV2"
{
    Frame label="AmbientLightV2" {
        Text item=illuminance
    }
}

```

---
[Table of Contents](#table-of-contents)

### Analog In Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Analog_In.html)

#### Binding properties:

If property $range is set to 0, the device switches between the measurement ranges automatically. Set $range to 1-5 to manually switch between measurement ranges.

#### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_analogin |
| range | sets the measurement range, default=0 (automatically switched) | 1: 0V - 6.05V, 2: 0V - 10.32V, 3: 0V - 36.30V, 4: 0V - 45.00V, 5: 0V - 3.3V with ~0.81mV resolution |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |


##### tinkerforge.cfg:

```
ain.uid=<your_uid>
ain.type=bricklet_analogin
ain.range=0
ain.callbackPeriod=1000
ain.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number voltage "Voltage [%.0f mV]" {tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Analog In"
{
    Frame label="Analog In" {
        Text item=voltage
    }
}
```

---
[Table of Contents](#table-of-contents)

### Analog In Bricklet 2.0

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Analog_In_V2.html)

#### Binding properties:

Moving average is a calculation to analyze data points by creating series of averages of different subsets of the full data set.
Property $movingAverage sets the length of a moving averaging for the measured voltage, default is 50, $movingAverage=1 turns averaging off. With less averaging, there is more noise on the data.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_analoginv2 |
| movingAverage | sets the length of a moving averaging, default=50 | 1: averaging off, 2-50: averaging is 2-50 |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
ainv2.uid=<your_uid>
ainv2.type=bricklet_analoginv2
ainv2.movingAverage=50
ainv2.callbackPeriod=1000
ainv2.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number voltage "Voltage [%.0f mV]" {tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Analog In V2"
{
    Frame label="Analog In V2" {
        Text item=voltage
    }
}
```

---
[Table of Contents](#table-of-contents)

### Analog Out Bricklet 2.0

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Analog_Out_V2.html)

#### Binding properties:

The device supports Dimmer and Setpoint Items. For Dimmer you have to add a "step=" in your items definition.

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_analog_out_v2 |
| minValue | 0-12000 | 0V - 12V in 1mV steps, 12bit resolution |
| maxValue | 0-12000 | 0V - 12V in 1mV steps, 12bit resolution |

##### tinkerforge.cfg:

```
aout.uid=<your_uid>
aout.type=bricklet_analog_out_v2
aout.minValue=0
aout.maxValue=12000
```

##### Items file entry (e.g. tinkerforge.items):

```
Dimmer  ADIMMER  "Dimmer" {tinkerforge="uid=<your_uid>, step=1000"}
Dimmer ADIMMERPERCENT "Dimmerpercent" {tinkerforge="uid=<your_uid"}

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap aout label="AnalogOutV2"
{
    Frame {
    Slider item=ADIMMER
    Setpoint item=ADIMMERPERCENT step=10
    }
}

```

---
[Table of Contents](#table-of-contents)

### Barometer Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Barometer.html)

#### Binding properties:

Bricklet measures air pressure in range of 10 to 1200mbar with a resolution of 0.012mbar.  
An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_barometer |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### Temperature sub device:

Subdevice property $barometer_temperature returns the temperature of the air pressure sensor which is internally used for temperature compensation of the air pressure measurement.
The temperature sub device does not support callbackPeriod, it will be polled. The polling interval can be configured using refresh property.

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | temperature |
| type | openHAB type name | barometer_temperature |

##### tinkerforge.cfg:

```
barometer_balcony.uid=<your_uid>
barometer_balcony.type=bricklet_barometer
barometer_balcony.callbackPeriod=10000
barometer_balcony.threshold=1000
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Barometer "Air Pressure [%.1f hPa]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=Barometer
```

---
[Table of Contents](#table-of-contents)

### CO2 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/CO2.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold).

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_co2 |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
co2.uid=<your_uid>
co2.type=bricklet_co2
co2.callbackPeriod=10
co2.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number C02 "C02 Concentration [%.1f]"  { tinkerforge="uid=<your_uid>" }

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap co2 label="C02"
{
    Frame {
        Text item=C02
    }
}

```

---
[Table of Contents](#table-of-contents)

### Color Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Color.html)

#### Binding properties:

Increasing the gain enables the sensor to detect a color from a higher distance.
The integration time provides a trade-off between conversion time and accuracy. With a longer integration time the values read will be more accurate but it will take longer time to get the conversion results.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_color |
| gain | sets the gain, derault=60x | 0: 1x gain, 1: 4x gain, 2: 16x gain, 3: 60x gain  |
| integrationTime | sets the integration time, default=154ms | 0: 2.4ms, 1: 24ms, 2: 101ms, 3: 154ms, 4: 700ms |

##### Color Bricklet sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | color, temperature, illuminance |
| type | openHAB type name | color_color, color_temperature, color_illuminance |
| callbackPeriod | |see "Callback and Threshold" |

Note: It is not possible to set the property $threshold for the Color Bricklet subdevices! 

##### tinkerforge.cfg:

```
brickletcolor.uid=<your_uid>
brickletcolor.type=bricklet_color
brickletcolor.gain=3
brickletcolor.integrationTime=3

color_color.uid=<your_uid>
color_color.subid=color
color_color.type=color_color
color_color.callbackPeriod=1000

color_temperature.uid=<your_uid>
color_temperature.subid=temperature
color_temperature.type=color_temperature
color_temperature.callbackPeriod=1000

color_illuminance.uid=<your_uid>
color_illuminance.subid=illuminance
color_illuminance.type=color_illuminance
color_illuminance.callbackPeriod=1000
```

##### Items file entry (e.g. tinkerforge.items):

```
Color color "Color" {tinkerforge="uid=<your_uid>, subid=color"}
Number temperature "ColorTemperature [%.2f]" {tinkerforge="uid=<your_uid>, subid=temperature"}
Number illuminance "ColorIlluminance [%.0f]" {tinkerforge="uid=<your_uid>, subid=illuminance"}
Switch led "Color Led"  {tinkerforge="uid=<your_uid>, subid=led"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Color"
{
    Frame label="Color" {
        Colorpicker item=color
        Text item=temperature
        Text item=illuminance
        Switch item=led
    }
}
```

---
[Table of Contents](#table-of-contents)

### Distance IR Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Distance_US.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_distance_ir |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
distance_door.uid=<your_uid>
distance_door.type=bricklet_distance_ir
distance_door.threshold=1
distance_door.callbackPeriod=10
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Distance                 "Distance [%.1f mm]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=Distance
```

---
[Table of Contents](#table-of-contents)

### Distance US Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Quad_Relay.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

Distance is reported as unitless value, not in mm.

Moving average is a calculation to analyze data points by creating series of averages of different subsets of the full data set.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_distanceUS |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |
| movingAverage | sets the moving average, default=20 | 0-100 |

##### tinkerforge.cfg:

```
distanceUS.uid=<your_uid>
distanceUS.type=bricklet_distanceUS
distanceUS.threshold=0
distanceUS.callbackPeriod=100
distanceUS.movingAverage=20
```

##### Items file entry (e.g. tinkerforge.items):

```
Number DistanceUS                 "DistanceUS [%.1f]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=DistanceUS
```

---
[Table of Contents](#table-of-contents)

### Dual Button Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Dual_Button.html)

#### Binding properties:

The Dual Button Bricklet has four sub devices: two leds and two buttons.
The subids are:

* dualbutton_leftled
* dualbutton_rightled
* dualbutton_leftbutton
* dualbutton_rightbutton

##### Leds

There are two operating modes for the leds: with the autotoggle=True the leds are
automatically toggled whenever the corresponding button is pressed. With the autotoggle=False
mode the leds are fully controlled with openHAB UIs or rules. The default autotoggle mode is
autotoggle=False. The autotoggle mode can be configured using tinkerforge.cfg.

##### Buttons

There are also two operating modes for the buttons. The buttons can behave like a switch or
like a tactile switch.  

* Switch mode

The switch mode operates like this: pressing the button toggles the
switch state, if state was ON it goes to OFF and vice versa. Releasing the button doesn't
change anything, only the next button press will change the state.

* Tactile switch mode

Pressing the button changes the switch state to ON and releasing the button changes the
state back to OFF again.

##### Dual Button Bricklet sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | dualbutton_leftled, dualbutton_rightled, dualbutton_leftbutton, dualbutton_rightbutton |
| type | openHAB type name | dualbutton_led, dualbutton_button |
| autotoggle | sets button autotoggle mode | True, False |
| tactile | sets switch mode | True, False |

##### tinkerforge.cfg:

```
led1.uid=<your_uid>
led1.subid=dualbutton_leftled
led1.type=dualbutton_led
led1.autotoggle=True

led2.uid=<your_uid>
led2.subid=dualbutton_rightled
led2.type=dualbutton_led
led2.autotoggle=False

button1.uid=<your_uid>
button1.subid=dualbutton_leftbutton
button1.type=dualbutton_button
button1.tactile=False

button2.uid=<your_uid>
button2.subid=dualbutton_rightbutton
button2.type=dualbutton_button
button2.tactile=True
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact LeftButton              "LeftButton" { tinkerforge="uid=<your_uid>, subid=dualbutton_leftbutton"}
Contact RightButton             "RightButton" { tinkerforge="uid=<your_uid>, subid=dualbutton_rightbutton"}
Switch LeftLed                  "LeftLed" { tinkerforge="uid=<your_uid>, subid=dualbutton_leftled"}
Switch RightLed                 "RightLed" { tinkerforge="uid=<your_uid>, subid=dualbutton_rightled"}
```

##### Rules (e.g. tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "toggleright"
    when
        Item RightButton changed
    then
        logDebug("dualbutton", "{}", RightButton.state)
        if (RightButton.state == OPEN)
            sendCommand(RightLed, ON)
        else
            sendCommand(RightLed, OFF)
end
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="DualButton"
{
  Frame {
    Text item=LeftButton
    Text item=RightButton
    Switch item=LeftLed
    Switch item=RightLed
    }
}
```

---
[Table of Contents](#table-of-contents)

### Dual Relay Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Dual_Relay.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

The Dual Relay Bricklet has two sub devices.
The subids are:
* relay1
* relay2

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_dual_relay |

##### Relay sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | relay1, relay2 |
| type | openHAB type name | dual_relay |

##### tinkerforge.cfg:

```
relay_coffee_machine.uid=<your_uid>
relay_coffee_machine.type=dual_relay
relay_coffee_machine.subid=relay1

relay_garage_door.uid=<your_uid>
relay_garage_door.type=dual_relay
relay_garage_door.subid=relay2
```

##### Items file entry (e.g. tinkerforge.items):

```
Switch DualRelay1   "DualRelay1" { tinkerforge="name=relay_coffee_machine" }
Switch DualRelay2   "DualRelay2" { tinkerforge="uid=<your_uid>, subid=relay2" }
Switch Garage       "Garage"    <garagedoor>    // creates a virtual switch with no bonded hardware
```

##### Rules (e.g. tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "open or close garage door"
    when
        Item Garage received command ON     // rule is triggered when virtual switch Garage receives ON command
    then
        DualRelay2.sendCommand(ON)          // contacts of subdevice relay2 are closed
        Thread::sleep(500)                  // delays execution of next command for 500 ms
        DualRelay2.sendCommand(OFF)         // contacts of subdevice relay2 are opened
end
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="DualRelay"
{
  Frame {
        Switch item=DualRelay1
        Switch item=Garage mappings=[ON="Garage open/close"]    // displays the virtual switch with the given text
    }
}

```

---
[Table of Contents](#table-of-contents)

### Dust Detector Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Dust_Detector.html)

#### Binding properties:

The measured dust density can be read out in µg/m³

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_dustdetector |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
dust.uid=<your_uid>
dust.type=bricklet_dustdetector
dust.callbackPeriod=10
dust.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Dust              "Dust [%.0f]" { tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Dust Detector"
{
    Frame {
        Text item=Dust
    }
}

```

---
[Table of Contents](#table-of-contents)

### Hall Effect Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Hall_Effect.html)

#### Binding properties:

Bricklet can detect the presence of magnetic fields. It counts the (dis-)appearances of magnetic fields.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_halleffect |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
# really optional settings
hall.uid=<your_uid>
hall.type=bricklet_halleffect
hall.callbackPeriod=1000

```

##### Items file entry (e.g. tinkerforge.items):

```
Contact HallEffect       "Hall" { tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge Halleffect"
{
    Frame label="HallEffect" {
        Text item=HallEffect
    }
}

```

---
[Table of Contents](#table-of-contents)

### Humidity Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Humidity.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

The measured humidity can be read out directly in percent, no conversions are necessary.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_humidity |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
humidity_balcony.uid=<your_uid>
humidity_balcony.type=bricklet_humidity
humidity_balcony.threshold=1
humidity_balcony.callbackPeriod=10
```

##### Items file entry (e.g. tinkerforge.items):

```

Number Humidity "Humidity [%.1f %%]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=Humidity
```

---
[Table of Contents](#table-of-contents)

### Industrial Digital IN 4 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Digital_In_4.html)

#### Binding properties:

 If you set the property $debouncePeriod to 100, you will get the interrupt maximal every 100ms. This is necessary if something that bounces is connected to the Digital In 4 Bricklet, such as a button.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | in0, in2, in3, in4 |
| type | openHAB type name | bricklet_industrial_digital_4in |
| debouncePeriod | debounce time in ms | default=100 |

##### Input port sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | in0, in2, in3, in4 |
| type | openHAB type name |  |

##### tinkerforge.cfg:

```
inddi4.uid=<your_uid>
inddi4.type=bricklet_industrial_digital_4in
inddi4.debouncePeriod=100
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact ID1 "ID1 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=in0"}
Contact ID2 "ID2 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=in1"}
Contact ID3 "ID3 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=in2"}
Contact ID4 "ID4 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=in3"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=ID1
Text item=ID2
Text item=ID3
Text item=ID4
```

---
[Table of Contents](#table-of-contents)

### Industrial Digital Out 4 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Digital_Out_4.html)

#### Binding properties:

##### Output port sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | out0, out2, out3, out4 |
| type | openHAB type name |  |

##### tinkerforge.cfg:

no configuration needed

##### Items file entry (e.g. tinkerforge.items):

```
Switch di4out0      {tinkerforge="uid=<your_uid>, subid=out0"}
Switch di4out1      {tinkerforge="uid=<your_uid>, subid=out1"}
Switch di4out2      {tinkerforge="uid=<your_uid>, subid=out2"}
Switch di4out3      {tinkerforge="uid=<your_uid>, subid=out3"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Switch item=di4out0
Switch item=di4out1
Switch item=di4out2
Switch item=di4out3
```

---

### Industrial Dual 0-20mA Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Dual_020mA.html)

#### Binding properties:

* two sub devices: sensor0 and sensor1
* device type bricklet: bricklet_industrialdual020ma
* device type sensors: industrial020ma_sensor
* sensor values are reported as milli ampere

##### tinkerforge.cfg configuration options for the Bricklet

sampleRate: possible values 0, 1, 2, 3. Setting the sample rate is optional
it defaults to 3 (4 samples per second).

* 0 means: 240 samples per second
* 1 means: 60 samples per second
* 2 means: 15 samples per second
* 3 means: 4 samples per second

##### tinkerforge.cfg configuration options for the sensors

callbackPeriod: Setting the callback period is optional, the default is 1000 milli seconds.

##### tinkerforge.cfg

```
brickletid020ma.uid=<your_uid>
brickletid020ma.type=bricklet_industrialdual020ma

temperature0.uid=<your_uid>
temperature0.subid=sensor0
temperature0.type=industrial020ma_sensor
temperature0.callbackPeriod=100

temperature1.uid=<your_uid>
temperature1.subid=sensor1
temperature1.type=industrial020ma_sensor
temperature1.callbackPeriod=1000
```

##### Items file entry (e.g. tinkerforge.items):

```
Number temperature0 "Temperature0 [%.2f]" {tinkerforge="uid=<your_uid>, subid=sensor0"}
Number temperature1 "Temperature1 [%.2f]" {tinkerforge="uid=<your_uid>, subid=sensor1"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge 020"
{
    Frame label="PTC" {
        Text item=temperature0
        Text item=temperature1
    }
}
```

---
[Table of Contents](#table-of-contents)

### Industrial Dual Analog In Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Dual_Analog_In.html)

#### Binding properties:

The property $sampleRate can be between 1 sample per second (SPS) and 976 samples per second. Decreasing the sample rate will also decrease the noise on the data.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_industrial_dual_analogin |
| sampleRate | sets the sample rate, default=6 (2 samples per second) | 0=976 SPS, 1=488 SPS, 2=244 SPS, 3=122 SPS, 4=61 SPS, 5=4 SPS, 6=2 SPS, 7=1 SPS |

##### Dual Analog In sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | channel0, channel1 |
| type | openHAB type name | industrial_dual_analogin_channel |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
diai.uid=<your_uid>
diai.type=bricklet_industrial_dual_analogin
diai.sampleRate=6

channel0.uid=<your_uid>
channel0.subid=channel0
channel0.type=industrial_dual_analogin_channel
channel0.callbackPeriod=1000
channel0.threshold=0

channel1.uid=<your_uid>
channel1.subid=channel1
channel1.type=industrial_dual_analogin_channel
channel1.callbackPeriod=1000
channel1.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number channel0 "Channel0 [%.0f mV]" {tinkerforge="uid=<your_uid>, subid=channel0"}
Number channel1 "Channel1 [%.0f mV]" {tinkerforge="uid=<your_uid>, subid=channel1"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Industrial Dual Analog In"
{
    Frame label="Industrial Dual Analog In" {
        Text item=channel0
        Text item=channel1
    }
}
```

---
[Table of Contents](#table-of-contents)

### Industrial Quad Relay Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Quad_Relay.html)

#### Binding properties:

The Quad Relay Bricklet has four sub devices.
The subids are:
* relay0
* relay1
* relay2
* relay3

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_industrial_quad_relay|

##### Relay sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | relay0, relay2, relay3, relay4 |
| type | openHAB type name | industrial_quad_relay |

##### tinkerforge.cfg:

```
relay0.uid=<your_uid>
relay0.type=quad_relay
relay0.subid=relay0

relay1.uid=<your_uid>
relay1.type=quad_relay
relay1.subid=relay1

relay2.uid=<your_uid>
relay2.type=quad_relay
relay2.subid=relay2

relay3.uid=<your_uid>
relay3.type=quad_relay
relay3.subid=relay3
```

##### Items file entry (e.g. tinkerforge.items):

```
Switch QR1 "QR1" {tinkerforge="uid=<your_uid>, subid=relay0"}
Switch QR2 "QR2" {tinkerforge="uid=<your_uid>, subid=relay1"}
Switch QR3 "QR3" {tinkerforge="uid=<your_uid>, subid=relay2"}
Switch QR4 "QR4" {tinkerforge="uid=<your_uid>, subid=relay3"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="QuadRelay"
{
  Frame {
            Switch item=QR1
            Switch item=QR2
            Switch item=QR3
            Switch item=QR4    
        }
}

```

---
[Table of Contents](#table-of-contents)

### IO4 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO4.html)

#### Binding properties:

If you set the property $debouncePeriod to 100, you will get the interrupt maximal every 100ms. This is necessary if something that bounces is connected to the IO-4 Bricklet, such as a button.

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_io4 |
| debouncePeriod | debounce time in ms | default=100 |

##### IO-Sensor sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | in1 |
| type | openHAB type name | io4sensor |
| pullUpResistorEnabled | enable the pull-up resistor |  true, false |

##### IO-Actor sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | out0 |
| type | openHAB type name | io4_actuator |
| defaultState | default state of the port, true = HIGH, false=LOW | true, false |

##### tinkerforge.cfg:

```
io4.uid=<your_uid>
io4.type=bricklet_io4
io4.debouncePeriod=200

io0.uid=<your_uid>
io0.type=io4_actuator
io0.subid=out0
io0.defaultState=true

io1.uid=<your_uid>
io1.type=io4sensor
io1.subid=in1
io1.pullUpResistorEnabled=true
```

##### Items file entry (e.g. tinkerforge.items):

```
Switch out0 "out0 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=out0"}
Contact in1 "in1 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=in1"}

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap io4 label="IO4"
{
    Frame {
        Switch item=out0
        Text item=in1
    }
}

```

---
[Table of Contents](#table-of-contents)

### IO 16 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/IO16.html)

#### Binding properties:

If you set the property $debouncePeriod to 100, you will get the interrupt maximal every 100ms. This is necessary if something that bounces is connected to the IO-16 Bricklet, such as a button.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_io16 |
| debouncePeriod | debounce time in ms | default=100 |

##### IO-Sensor sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | ina0, ina1, ina2, ina3, ina4, ina5, ina6, ina7, inb0, inb1, inb2, inb3, inb4, inb5, inb6, inb7 |
| type | openHAB type name | iosensor |
| pullUpResistorEnabled | enable the pull-up resistor |  true, false |

##### IO-Actor sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | outa0, outa1, outa2, outa3, outa4, outa5, outa6, outa7, outb1, outb2, outb3, outb4, outb5, outb6, outb7 |
| type | openHAB type name | io_actuator |
| defaultState | default state of the port, true = HIGH, false=LOW | true, false |

##### tinkerforge.cfg:

```
io16.uid=<your_uid>
io16.type=bricklet_io16
io16.debouncePeriod=100

io16ina0.uid=<your_uid>
io16ina0.subid=ina0
io16ina0.type=iosensor
io16ina0.pullUpResistorEnabled=true

io16ina1.uid=<your_uid>
io16ina1.subid=ina1
io16ina1.type=iosensor
io16ina1.pullUpResistorEnabled=true

io16outa2.uid=<your_uid>
io16outa2.subid=outa2
io16outa2.type=io_actuator

io16outa3.uid=<your_uid>
io16outa3.subid=outa3
io16outa3.type=io_actuator

io16ina4.uid=<your_uid>
io16ina4.subid=ina4
io16ina4.type=iosensor
io16ina4.pullUpResistorEnabled=true

io16ina5.uid=<your_uid>
io16ina5.subid=ina5
io16ina5.type=iosensor
io16ina5.pullUpResistorEnabled=true

io16ina6.uid=<your_uid>
io16ina6.subid=ina6
io16ina6.type=iosensor
io16ina6.pullUpResistorEnabled=true

io16ina7.uid=<your_uid>
io16ina7.subid=ina7
io16ina7.type=iosensor
io16ina7.pullUpResistorEnabled=true

#### port b

io16outb0.uid=<your_uid>
io16outb0.subid=outb0
io16outb0.type=io_actuator

io16inb1.uid=<your_uid>
io16inb1.subid=inb1
io16inb1.type=iosensor
io16inb1.pullUpResistorEnabled=true

io16inb2.uid=<your_uid>
io16inb2.subid=inb2
io16inb2.type=iosensor
io16inb2.pullUpResistorEnabled=true

io16inb3.uid=<your_uid>
io16inb3.subid=inb3
io16inb3.type=iosensor
io16inb3.pullUpResistorEnabled=true

io16inb4.uid=<your_uid>
io16inb4.subid=inb4
io16inb4.type=iosensor
io16inb4.pullUpResistorEnabled=true

io16inb5.uid=<your_uid>
io16inb5.subid=inb5
io16inb5.type=iosensor
io16inb5.pullUpResistorEnabled=true

io16inb6.uid=<your_uid>
io16inb6.subid=inb6
io16inb6.type=iosensor
io16inb6.pullUpResistorEnabled=true

io16inb7.uid=<your_uid>
io16inb7.subid=inb7
io16inb7.type=iosensor
io16inb7.pullUpResistorEnabled=true
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact ina0        "ina0 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina0"}
Contact ina1        "ina1 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina1"}
Switch outa2        "outa2" {tinkerforge="uid=<your_uid>, subid=outa2"}
Switch outa3        "outa3" {tinkerforge="uid=<your_uid>, subid=outa3"}
Contact ina4        "ina4 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina4"}
Contact ina5        "ina5 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina5"}
Contact ina6        "ina6 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina6"}
Contact ina7        "ina7 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=ina7"}

Switch outb0        "outb0" {tinkerforge="uid=<your_uid>, subid=outb0"}
Contact inb1        "inb1 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb1"}
Contact inb2        "inb2 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb2"}
Contact inb3        "inb3 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb3"}
Contact inb4        "inb4 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb4"}
Contact inb5        "inb5 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb5"}
Contact inb6        "inb6 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb6"}
Contact inb7        "inb7 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=inb7"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap io16 label="Main Menu"
{
    Frame {
        Text item=ina0
        Text item=ina1
        Switch item=outa2
        Switch item=outa3
        Text item=ina4
        Text item=ina5
        Text item=ina6
        Text item=ina7

        Switch item=outb0
        Text item=inb1
        Text item=inb2
        Text item=inb3
        Text item=inb4
        Text item=inb5
        Text item=inb6
        Text item=inb7
      }
}
```

---
[Table of Contents](#table-of-contents)

### Joystick Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Joystick.html)

#### Binding properties:

There are three sub devices: joystick_xposition, joystick_yposition and joystick_button.
Callback period for xy position defaults to 10 milli seconds. CallbackPeriod must be configured
on bricklet_joystick device, because x and y position can not have
different callback periods.

##### Button

Two operating modes for the button. The button can behave like a switch or
like a tactile switch.

* Switch mode

The switch mode operates like this: pressing the button toggles the
switch state, if state was ON it goes to OFF and vice versa. Releasing the button doesn't
change anything, only the next button press will change the state.

* Tactile switch mode

Pressing the button changes the switch state to ON and releasing the button changes the
state back to OFF again.

Switch Mode is the default mode you can change the mode to tactile by adding a line like this
to your tinkerforge.cfg:

```
joystickbutton.tactile=True
```

##### tinkerforge.cfg:

```
joystick.uid=<your_uid>
joystick.type=bricklet_joystick
joystick.callbackPeriod=1000

joystickbutton.uid=<your_uid>
joystickbutton.subid=joystick_button
joystickbutton.type=joystick_button
joystickbutton.tactile=True

yposition.uid=<your_uid>
yposition.subid=joystick_yposition
yposition.type=joystick_yposition
```

##### Items file entry (e.g. tinkerforge.items):

```
Number XPostion              "XPostion [%.0f]" { tinkerforge="uid=<your_uid>, subid=joystick_xposition"}
Number YPostion              "YPostion [%.0f]" { tinkerforge="name=yposition"}
Contact JoystickButton       "Button" { tinkerforge="uid=<your_uid>, subid=joystick_button"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf_weather label="Joystick"
{
  Frame {
    Text item=XPostion
    Text item=YPostion
    Text item=JoystickButton
    }
}
```

---
[Table of Contents](#table-of-contents)

### Laser Range Finder Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Laser_Range_Finder.html)

#### Binding properties:

The laser will be enabled by default on system start. This can be changed by 
setting enableLaserOnStartup to false on the bricklet_laser_range_finder type in services/tinkerforge.cfg.
If the laser is already enabled it will never be disabled on openHAB startup.

Moving average is a calculation to analyze data points by creating series of averages of different subsets of the full data set.

```
lrf.uid=<your_uid>
lrf.type=bricklet_laser_range_finder
lrf.enableLaserOnStartup=false
```

Properties $distanceAverageLength and $velocityAverageLength set the length of a moving averaging for the distance and velocity. Setting the property to 0 will turn the averaging completely off. With less averaging, there is more noise on the data. The range for the averaging is 0-30, default value is 10.

Property $mode sets the mode for measurements, five modes are available, one mode for distance measurements and four modes for velocity measurements with different ranges and resolutions. 

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_laser_range_finder |
| distanceAverageLength | sets the length of a moving averaging for the distance, default=10 | 0-30 |
| velocityAverageLength | sets the length of a moving averaging for the velocity, default=10 | 0-30 |
| mode | sets the mode for measurements, default=0 | 0: Distance is measured with resolution 1.0 cm and range 0-400 cm |
| mode | sets the mode for measurements, default=0 | 1: Velocity is measured with resolution 0.1 m/s and range is 0-12.7 m/s |
| mode | sets the mode for measurements, default=0 | 2: Velocity is measured with resolution 0.25 m/s and range is 0-31.75 m/s |
| mode | sets the mode for measurements, default=0 | 3: Velocity is measured with resolution 0.5 m/s and range is 0-63.5 m/s |
| mode | sets the mode for measurements, default=0 | 4: Velocity is measured with resolution 1.0 m/s and range is 0-127 m/s |
| enableLaserOnStartup | enables or disables laser on startup, default=true | true, false |

##### Laser Range Finder sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | distance, velocity, laser |
| type | openHAB type name | laser_range_finder_distance, laser_range_finder_velocity, laser_range_finder_laser |
| callbackPeriod | | see "Callback and Threshold" |
| threshold | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
lrf.uid=<your_uid>
lrf.type=bricklet_laser_range_finder
lrf.distanceAverageLength=10
lrf.velocityAverageLength=10
lrf.mode=1
lrf.enableLaserOnStartup=True

distance.uid=<your_uid>
distance.subid=distance
distance.type=laser_range_finder_distance
distance.callbackPeriod=10
distance.threshold=0

velocity.uid=<your_uid>
velocity.subid=velocity
velocity.type=laser_range_finder_velocity
velocity.callbackPeriod=10
velocity.threshold=0

laser.uid=<your_uid>
laser.subid=laser
laser.type=laser_range_finder_laser

```

##### Items file entry (e.g. tinkerforge.items):

```
Number distance "Distance [%.0f]" {tinkerforge="uid=<your_uid>, subid=distance"}
Number velocity "Velocity [%.4f]" {tinkerforge="uid=<your_uid>, subid=velocity"}
Switch laser "Enable Laser" {tinkerforge="uid=<your_uid>, subid=laser"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Laser Range Finder"
{
    Frame label="Laser Range Finder" {
        Text item=distance
        Text item=velocity
        Switch item=laser
    }
}
```

---
[Table of Contents](#table-of-contents)

### LCD 20x4 Display Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/LCD_20x4.html)

#### Binding properties:

The LCD20x4 is a bit special as it acts as actuator which can receive text messages. To
achieve this, you have to configure the device as String item.

What’s the meaning of this magic string?

```
sendCommand(TF_LCD, String::format("TFNUM<213>%4s"Barometer.state.format("%d")))
```

TFNUM is just a flag to signal the binding that some position information is passed. The first
number is the line number, starting from 0. The second and third number are interpreted as the
position in the line, starting from 0.

The above example would write the current value of the barometer bricklet to line 3 starting from
position 14, with a fixed width of 4 (this is because of %4s).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_LCD20x4 |

##### Backlight sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | backlight |
| type | openHAB type name | backlight |

##### Button sub devices:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | button0, button1, button2, button3 |
| type | openHAB type name | lcd_button |


##### Items file entry (e.g. tinkerforge.items):

```
String LCD         "LCD" { tinkerforge="uid=<your_uid>"}
Switch LCDBacklight        "LCDBacklight" { tinkerforge="uid=<your_uid>, subid=backlight"}
Switch Button0         "Button0" { tinkerforge="uid=<your_uid>, subid=button0"}
Switch Button1         "Button1" { tinkerforge="uid=<your_uid>, subid=button1"}
Switch Button2         "Button2" { tinkerforge="uid=<your_uid>, subid=button2"}
Switch Button3         "Button3" { tinkerforge="uid=<your_uid>, subid=button3"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Switch item=LCDBacklight
```

##### Rules file (e.g. tinkerforge.rules):

```
import org.openhab.core.library.types.*

var Number initialSleepTime = 10

rule "Weatherstation LCD init from Backlight"
when
    Item TF_LCDBacklight changed from UNDEF or
        System started
then
    createTimer(now.plusSeconds(initialSleepTime)) [|
        sendCommand(TF_LCD, "TFNUM<00>Temperature:       C")
        sendCommand(TF_LCD, "TFNUM<10>Humidity   :       %")
        sendCommand(TF_LCD, "TFNUM<20>Pressure   :     hPa")
        sendCommand(TF_LCD, "TFNUM<30>Luminance  :     Lux")
        sendCommand(TF_LCDBacklight, ON)
        sendCommand(TF_LCD, String::format("TFNUM<013>%4s",
                                TF_Barometer_Temperature.state.format("%.1f")))
        sendCommand(TF_LCD, String::format("TFNUM<113>%4s",
                                TF_Humdity.state.format("%.1f")))
        sendCommand(TF_LCD, String::format("TFNUM<213>%4s",
                                  TF_Barometer.state.format("%.0f")))
        sendCommand(TF_LCD, String::format("TFNUM<313>%4s",
                                TF_AmbientLight.state.format("%.0f")))
    ]

end

rule Goodbye
when
        System shuts down
then
        sendCommand(TF_LCDBacklight, OFF)
end

rule "Weatherstation LCD Backlight"
        when
                Item TF_Button0 received update
        then
        if (TF_Button0.state == ON)
            sendCommand(TF_LCDBacklight, ON)
        else
            sendCommand(TF_LCDBacklight, OFF)

end

rule "Weatherstation LCD update temperature"
        when
                Item TF_Barometer_Temperature received update
        then
                sendCommand(TF_LCD, String::format("TFNUM<013>%4s",
                                TF_Barometer_Temperature.state.format("%.1f")
                        ))
end

rule "Weatherstation LCD update humidity"
        when
                Item TF_Humdity received update
        then
                sendCommand(TF_LCD, String::format("TFNUM<113>%4s",
                                TF_Humdity.state.format("%.1f")
                            ))
end
rule "Weatherstation LCD update airpressure"
        when
                Item TF_Barometer received update
        then
                sendCommand(TF_LCD, String::format("TFNUM<213>%4s",
                                  TF_Barometer.state.format("%.0f")
                                  ))
end
rule "Weatherstation LCD update ambientLight"
        when
                Item TF_AmbientLight received update
        then
                sendCommand(TF_LCD, String::format("TFNUM<313>%4s",
                                TF_AmbientLight.state.format("%.0f")
                                ))
end
```

---
[Table of Contents](#table-of-contents)

### LED Strip Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/LED_Strip.html)

#### Binding properties:

Supported chip types are WS2801, WS2811, WS2812/SK6812 (NeoPixel RGB), SK6812RGBW (NeoPixel RGBW), LPD8806 or APA102 (DotStar) LED driver.
An entry in services/tinkerforge.cfg is *mandatory*. $type, $frameduration, $chiptype, $clockfrequency and $colorMapping have to be set. The available configuration variables depend on the chip type of the LED strip.  
All LEDs can be switched independently. A subdevice $ledgroup can be set to group LED's together.  
The colormapping of the LED chip types are not standardized, therefore the sequence of the letters "rgb" can be changed individually to match the the color of your LED Strip. 

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_ledstrip |
| framduration | sets the frame duration in ms, default: 100ms (10 fps) |  |
| chiptype | sets the strip chip type |  ws2801, ws2811, ws2812 |
| clockfrequency | sets the frequency of the clock in Hz | 10000Hz (10kHz) up to 2000000Hz (2MHz) |
| colorMapping | sets the colormapping | rbg or any other letter sequence of "r", "g" and "b"|
| subDevices | configures a group of led | f.e. ledgroup1 ledgroup2 |

##### LED Strip sub device (optional):

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | ledgroup1 |
| leds | configures a group of led's | depends on the number of led's used |

##### tinkerforge.cfg:

```
ledstrip.uid=<your_uid>
ledstrip.type=bricklet_ledstrip
ledstrip.frameduration=100
ledstrip.chiptype=ws2801
ledstrip.clockfrequency=1000000
#ledstrip.colorMapping=rbg
ledstrip.colorMapping=brg
ledstrip.subDevices=ledgroup1 ledgroup2

ledgroup1.uid=<your_uid>
ledgroup1.subid=ledgroup1
ledgroup1.type=ledgroup
ledgroup1.leds=0|1-6

ledgroup2.uid=<your_uid>
ledgroup2.subid=ledgroup2
ledgroup2.type=ledgroup
ledgroup2.leds=0|7-14

```

##### Items file entry (e.g. tinkerforge.items):

```
Color  tfled1   <slider> {tinkerforge="uid=<your_uid>, subid=ledgroup1"}
Color  tfled2   <slider> {tinkerforge="uid=<your_uid>, subid=ledgroup2"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge LED"
{
    Frame label="LED Strip" {
        Colorpicker item=tfled1 icon="slider"
        Colorpicker item=tfled2 icon="slider"
    }
}

```

---
[Table of Contents](#table-of-contents)

### Linear Poti Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Linear_Poti.html)

#### Binding properties:

You can expect values from 0 - 100 %.
The default callback period is 10 millis, you can change this within services/tinkerforge.cfg.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_linear_poti |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
linearpoti.uid=<your_uid>
linearpoti.type=bricklet_linear_poti
linearpoti.callbackPeriod=1000
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Poti   "Poti [%.0f]" { tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tflabel="Linear Poti"
{
    Frame {
        Text item=Poti
    }
}
```

---
[Table of Contents](#table-of-contents)

### Load Cell Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Load_Cell.html)

#### Binding properties:

Returns the currently measured weight in grams. An LED can be turned on to inidicate that a weight measurement is in range.

Moving average is a calculation to analyze data points by creating series of averages of different subsets of the full data set.

##### TinkerForge Action

The openHAB action [TinkerforgeAction](#tinkerforge-actions) comes up with the action tfLoadCellTare.
tfLoadCellTare(String uid) sets tare on the load cell bricklet with the given uid.

Example:

```
rule "Tare"
   when
           Item Tare changed to ON
   then
           postUpdate(TareValue, Weight.state)
           tfLoadCellTare("<your_uid>")
end
```    

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_loadcell |


##### Load Cell sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | weight, led |
| type | openHAB type name | loadcell_weight, loadcell_led |
| movingAverage | sets the value for moving average, default=4 | 1-40 |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
loadcell.uid=<your_uid>
loadcell.type=bricklet_loadcell

weight.uid=<your_uid>
weight.subid=weight
weight.type=loadcell_weight
weight.callbackPeriod=100
weight.movingAverage=4

led.uid=<your_uid>
led.subid=led
led.type=loadcell_led
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Weight "Weight [%.0f]" { tinkerforge="uid=<your_uid>, subid=weight"}
Switch Led "Led"  {tinkerforge="uid=<your_uid>, subid=led"}
Switch Tare "Tare"
Number TareValue "Tare Value [%.0f]"
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="Load Cell"
{
    Frame {
        Text item=Weight
        Switch item=Led
        Switch item=Tare
        Text item=TareValue
    }
}
```

##### Rules (e.g tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "Tare"
        when
                Item Tare changed to ON
        then
                postUpdate(TareValue, Weight.state)
                tfLoadCellTare("<your_uid>")
end
```

---
[Table of Contents](#table-of-contents)

### Motion Detector Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Motion_Detector.html)

#### Binding properties:

Senses movement of people and animals with a detection range of 3m to 7m and a sensing angle of 100°.

An entry in services/tinkerforge.cfg is only needed if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | motion_detector |

##### tinkerforge.cfg:

```
motion.uid=<your_uid>
motion.type=motion_detector
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact motion      "motion [MAP(en.map):MOTION%s]" {tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=motion
```

##### en.map file:

```
MOTIONCLOSED=no motion
MOTIONOPEN=montion detected
```

---
[Table of Contents](#table-of-contents)

### Multi Touch Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Multi_Touch.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust sensitivity, recalibrate, disable
electrodes or use a [_symbolic name_](#sym_name).

##### Bricklet:

##### Device configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_multitouch |
| sensitivity | sets the electrodes sensitivity, default=181 | 5-201 |
| recalibrate | recalibrate the sensor | true or false |

```
touch.uid=<your_uid>
touch.type=bricklet_multitouch
touch.sensitivity=181
touch.recalibrate=true
```

##### Electrode sub device configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device|electrode0 electrode1 electrode2 electrode3 electrode4 electrode5 electrode6 electrode7 electrode8 electrode9 electrode10 electrode11|
| disableElectrode | disables the electrode | true or false|

##### tinkerforge.cfg for electrodes:

```
e1.uid=<your_uid>
e1.type=electrode
e1.subid=electrode1
e1.disableElectrode=true
```

##### Proximity sub device configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device|proximity|
| disableElectrode | disables the proximity detection | true or false|

##### tinkerforge.cfg for proximity:

```
prox.uid=<your_uid>
prox.type=proximity
prox.subid=proximity
prox.disableElectrode=true
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact electrode0      "electrode0 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode0"}
Contact electrode1      "electrode1 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode1"}
Contact electrode2      "electrode2 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode2"}
Contact electrode3      "electrode3 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode3"}
Contact electrode4      "electrode4 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode4"}
Contact electrode5      "electrode5 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode5"}
Contact electrode6      "electrode6 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode6"}
Contact electrode7      "electrode7 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode7"}
Contact electrode8      "electrode8 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode8"}
Contact electrode9      "electrode9 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode9"}
Contact electrode10     "electrode10 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode10"}
Contact electrode11     "electrode11 [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=electrode11"}
Contact proximity       "proximity [MAP(en.map):%s]" {tinkerforge="uid=<your_uid>, subid=proximity"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=electrode0
Text item=electrode1
Text item=electrode2
Text item=electrode3
Text item=electrode4
Text item=electrode5
Text item=electrode6
Text item=electrode7
Text item=electrode8
Text item=electrode9
Text item=electrode10
Text item=electrode11
Text item=proximity
```

---
[Table of Contents](#table-of-contents)

### Moisture Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Moisture.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

Moving average is a calculation to analyze data points by creating series of averages of different subsets of the full data set.

##### tinkerforge.cfg:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_moisture |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |
| movingAverage | sets the value for moving average, default=100 | 0-100 |

```
moisture.uid=<your_uid>
moisture.type=bricklet_moisture
moisture.threshold=0
moisture.callbackPeriod=5000
moisture.movingAverage=90
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Moisture                 "Moisture [%.1f]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=Moisture
```

---
[Table of Contents](#table-of-contents)

### Piezo Speaker Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Piezo_Speaker.html)

#### Binding properties:

The Piezo Speaker Bricklet supports two modes:

* sending morse codes with configurable frequency
* sending tones with configurable duration and frequency

Configuration is done through item definition. In order to use different tone sequences you need to use one item per tone sequence.

Example:

```
Switch Beep      "Beep" { autoupdate="false", tinkerforge="uid=<your_uid>, mode=beep, durations=500|100, frequencies=10|10000, repeat=2" }
Switch Morse      "Morse" { autoupdate="false", tinkerforge="uid=<your_uid>, mode=morse, morsecodes=...---...|---, frequencies=10|10000, repeat=2" }
```

With the *repeat* statement the tone sequence is repeated with the given number.

---
[Table of Contents](#table-of-contents)

### PTC Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/PTC.html)

#### Binding properties:

Wire mode of the sensor has to be set. Possible values are 2, 3 and 4 which correspond to 2-, 3- and 4-wire sensors. The value has to match the jumper configuration on the Bricklet.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_ptc |
| wiremode | sets the wire mode of the sensor, default=2 | 2, 3, 4 |

##### PTC sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | same as bricklet |
| subid | openHAB subid of the device | ptc_temperature |
| type | openHAB type name | ptc_temperature |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
brickletptc.uid=<your_uid>
brickletptc.type=bricklet_ptc
brickletptc.wiremode=2

ptctemperature.uid=<your_uid>
ptctemperature.subid=ptc_temperature
ptctemperature.type=ptc_temperature
ptctemperature.callbackPeriod=1000
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact connected {tinkerforge="uid=<your_uid>, subid=ptc_connected"}
Number temperature "Temperature [%.2f]" {tinkerforge="uid=<your_uid>, subid=ptc_temperature"}
Number resistance "Resistance [%.0f]" {tinkerforge="uid=<your_uid>, subid=ptc_resistance"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge PTC"
{
    Frame label="PTC" {
        Text item=temperature
        Text item=resistance
        Text item=connected
    }
}
```

---
[Table of Contents](#table-of-contents)

### Remote Switch Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Remote_Switch.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is *mandatory*. You have to set sub device names $subdevice for your devices in the appropriate
type$typeDevices variable as space separated list. $type depends on the device hardware type of your switching device.
You must also add configuration for the $subdevice device. The available configuration variables depend
on the device type.

##### tinkerforge.cfg:

* Device configuration

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_remote_switch |
| typeADevices | sub device names of type A devices | choose a reasonable string, e.g. "kitchen" or "floor" |
| typeBDevices | sub device names of type B devices  | choose a reasonable string |
| typeCDevices | sub device names of type C devices  |choose a reasonable string |

```
rs1.uid=<your_uid>
rs1.type=bricklet_remote_switch
rs1.typeADevices=rslr1 rslr2
rs1.typeBDevices=kitchen
rs1.typeCDevices=floor
```

* Sub device type A configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | must correspond to the device(A,B,C)Devices setting| e.b. kitchen|
| houseCode | the house code of the switching device | e.g. 31 |
| receiverCode | the receiver code of the switching device | e.g. 8 |
| repeats | the number of times the code is send | e.g. 5 |

```
rs_living_room.uid=<your_uid>
rs_living_room.subid=rslr1
rs_living_room.type=remote_switch_a
rs_living_room.houseCode=31
rs_living_room.receiverCode=8

rs_living_room2.uid=<your_uid>
rs_living_room2.subid=rslr2
rs_living_room2.type=remote_switch_a
rs_living_room2.houseCode=31
rs_living_room2.receiverCode=9
```

* Sub device type B configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | must correspond to the device(A,B,C)Devices setting| e.b. kitchen|
| address | the address of the switching device | e.g. 344 |
| unit | the unit value of the switching device | e.g. 9 |
| repeats | the number of times the code is send | e.g. 5 |

```
rs_kitchen.uid=<your_uid>
rs_kitchen.subid=kitchen
rs_kitchen.type=remote_switch_b
rs_kitchen.address=344
rs_kitchen.unit=9
```

* Sub device type C configuration:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | must correspond to the device(A,B,C)Devices setting| e.b. kitchen|
| systemCode | the system code of the switching device | e.g. A |
| deviceCode | the device code of the switching device | e.g. 8 |
| repeats | the number of times the code is send | e.g. 5 |

```
rs_floor.uid=<your_uid>
rs_floor.subid=floor
rs_floor.type=remote_switch_c
rs_floor.systemCode=A
rs_floor.deviceCode=8
```

##### Items file entry (e.g. tinkerforge.items):

```
Dimmer dimmb     "dimmb [%d %%]"      <slider>     {tinkerforge="uid=<your_uid>, subid=kitchen"}
Switch r0    "r0" <socket> (Lights)      {tinkerforge="uid=<your_uid>, subid=rslr1"}
Switch r1    "r1" <socket> (Lights)      {tinkerforge="uid=<your_uid>, subid=rslr2"}
Switch rb    "rb" <socket> (Lights)      {tinkerforge="uid=<your_uid>, subid=kitchen"}
Switch rc    "rc" <socket> (Lights)      {tinkerforge="uid=<your_uid>, subid=floor"}

Group:Switch:OR(ON,OFF)    Lights    "All Lights [(%d)]"
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="RemoteSwitch"
{
    Frame label="Group" {
        Text label="Group Demo" icon="1stfloor" {
        Switch item=Lights mappings=[OFF="All Off",ON="All On"]
        }
    }
  Frame label="Remote" {
            Switch item=r0
            Switch item=r1
            Switch item=rb
            Slider item=dimmb switchSupport
            Switch item=rc
    }
}
```

---
[Table of Contents](#table-of-contents)

### Rotary Encoder Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Rotary_Encoder.html)

#### Binding properties:

There are two sub devices: encoder and button.
Callback period for encoder defaults to 10 milli seconds. CallbackPeriod can be configured
for the encoder sub device.

##### TinkerForge Action

The openHAB action [TinkerforgeAction](#tinkerforge-actions) comes up with the action tfRotaryEncoderClear.
tfRotaryEncoderClear(String uid) clears the rotary encoder counter with the given uid.

Example:

```
rule "Clear"
   when Item Clear changed
then 
   tfRotaryEncoderClear("<your_uid>")
end
```

##### Button

Two operating modes for the button. The button can behave like a switch or
like a tactile switch.

* Switch mode

The switch mode operates like this: pressing the button toggles the
switch state, if state was ON it goes to OFF and vice versa. Releasing the button doesn't
change anything, only the next button press will change the state.

* Tactile switch mode

Pressing the button changes the switch state to ON and releasing the button changes the
state back to OFF again.

Switch Mode is the default mode you can change the mode to tactile by adding a line like this to your tinkerforge.cfg:

```
button.tactile=True
```

##### tinkerforge.cfg:

```
encoder.uid=<your_uid>
encoder.subid=encoder
encoder.type=rotary_encoder
encoder.callbackPeriod=1

button.uid=<your_uid>
button.subid=button
button.type=rotary_encoder_button
button.tactile=False
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Counter "Counter [%d]" { tinkerforge="uid=<your_uid>, subid=encoder"}
Contact Button "Button" { tinkerforge="uid=<your_uid>, subid=button"}
Switch Clear "Clear"
```

##### Rules (e.g tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "Clear"
when Item Clear changed
then
    tfRotaryEncoderClear("<your_id>")
end
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="RotaryEncoder"
{
  Frame {
    Text item=Counter
    Text item=Button
    Switch item=Clear
    }
}
```

---
[Table of Contents](#table-of-contents)

### Segment Display 4x7 Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Segment_Display_4x7.html)

#### Binding properties:

The Segment Display 4x7 is a bit special as it acts as actuator which can receive number messages. To
achieve this, you have to configure the device as Number item.

An entry in services/tinkerforge.cfg is only needed if you want to use a [_symbolic name_](#sym_name).

##### Items file entry (e.g. tinkerforge.items):

```
Number Segment7         "Segment7" { tinkerforge="uid=<your_uid>"}
```

##### Rules file (e.g. tinkerforge.rules):

If you want to display the object temperature from a temperatureIr Item called ObjectTemperature this
rule would do the trick for you.

```
rule "Weatherstation Segment update ObjectTemperature"
        when
                Item ObjectTemperature received update
        then
                sendCommand(Segment7, ObjectTemperature.state))
end
```

---
[Table of Contents](#table-of-contents)

### Solid State Relay Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Solid_State_Relay.html)

#### Binding properties:

* no sub devices
* no configuration needed for tinkerforge.cfg

##### Items file entry (e.g. tinkerforge.items):

```
Switch relay "Relay" {tinkerforge="uid=<your_uid>"}
Contact relaystatus "Relay Status"
```

##### Rules file (e.g. tinkerforge.rules):

```
import org.openhab.core.library.types.*

rule "change every 5 seconds"
when
    Time cron "0/1 * * * * ?"
then
    if(relay.state == ON)
        sendCommand(relay, OFF)
    else
        sendCommand(relay, ON)
end

rule "Relay Status"
when
    Item relay changed
then
    if (relay.state == ON)
    postUpdate(relaystatus, CLOSED)
    else
    postUpdate(relaystatus, OPEN)
end
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge SolidStateRelay"
{
    Frame label="Relay" {
        Switch item=relay
        Text item=relaystatus
    }
}
```

---
[Table of Contents](#table-of-contents)

### Sound Intensity Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Sound_Intensity.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_soundintensity |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
sound.uid=<your_uid>
sound.type=bricklet_soundintensity
sound.threshold=1
sound.callbackPeriod=5000
```

##### Items file entry (e.g. tinkerforge.items):

```
Number SoundIntensity                 "Sound [%.1f]"  { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=SoundIntensity
```

---
[Table of Contents](#table-of-contents)

### Temperature Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Temperature.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold) or if you want to use a [_symbolic name_](#sym_name).

Since OH 1.8 there is a new option slowI2C which could be set to "True" or "False",
the default value is "False". More information on this setting can be found [here](http://www.tinkerforge.com/en/doc/Software/Bricklets/Temperature_Bricklet_Java.html#BrickletTemperature::setI2CMode__short-).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_temperature |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
temperature.uid=<your_uid>
temperature.type=bricklet_temperature
temperature.slowI2C=False
```

##### Items file entry (e.g. tinkerforge.items):

```
Number temperature "Temperature [%.2f]" {tinkerforge="uid=<your_uid>"}
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf label="TinkerForge Temperature"
{
    Frame label="Temperature" {
        Text item=temperature
    }
}
```

---
[Table of Contents](#table-of-contents)

### Temperature IR Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Temperature_IR.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold),
if you want to use a [_symbolic name_](#sym_name) or adjust the emissivity of the object temperature device.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_temperatureIR |

##### Object temperature sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | object_temperature |
| type | openHAB type name | object_temperature |
| emissivity |  emissivity that is used to calculate the surface temperature | a factor of 65535 e.g. 6553|
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
objIR.uid=<your_uid>
objIR.subid=object_temperature
objIR.type=object_temperature
objIR.emissivity=65535
objIR.threshold=0
```

##### Ambient temperature sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | ambient_temperature |
| type | openHAB type name | ambient_temperature |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
ambIR.uid=<your_uid>
ambIR.subid=ambient_temperature
ambIR.type=ambient_temperature
ambIR.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number AmbientTemperature "AmbientTemperature [%.1f C]"  { tinkerforge="uid=<your_uid>, subid=ambient_temperature" }
Number ObjectTemperature "ObjectTemperature [%.1f C]"  { tinkerforge="uid=<your_uid>, subid=object_temperature" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=AmbientTemperature
Text item=ObjectTemperature
```

---
[Table of Contents](#table-of-contents)

### Tilt Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Tilt.html)

#### Binding properties:

You can use a contact, number or switch item.

An entry in services/tinkerforge.cfg is only needed if you want to use a [_symbolic name_](#sym_name).

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_tilt |

##### tinkerforge.cfg:

```
tilt.uid=<your_uid>
tilt.type=bricklet_tilt
```

##### Items file entry (e.g. tinkerforge.items):

```
Contact tiltContact  "tilt [MAP(en.map):%s]" { tinkerforge="uid=<your_uid>" }
Number tiltSensor    "tilt [MAP(en.map):%s]"  { tinkerforge="uid=<your_uid>" }
Switch tiltSwitch    "tilt" { tinkerforge="uid=<your_uid>" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=tiltContact
Text item=tiltSensor
Switch item=tiltSwitch
```

##### en.map file entry (optional):

```
0=closed
1=open
2=vibrating
```

---
[Table of Contents](#table-of-contents)

### Thermocouple Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Thermocouple.html)

#### Binding properties:

The supported thermocouple types are B, E, J, K, N, R, S and T.

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_thermocouple |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |
| averaging | averaging sizes, default=16 | 1, 2, 4, 8 and 16 samples |
| thermocoupleType | type of sensor used, default=K | B, E, J, K, N, R, S and T |
| filter | frequency filter, 50Hz or 60Hz, default=50Hz| 0 (=50Hz), 1 (=60Hz) |

##### tinkerforge.cfg:

```
thermocouple.uid=<your_uid>
thermocouple.type=bricklet_thermocouple
thermocouple.callbackPeriod=10
thermocouple.threshold=0

thermocouple:averaging=16
thermocouple:thermocoupleType=K
thermocouple:filter=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Temp "Temperature (Thermocouple) [%.1f]" { tinkerforge="uid=<your_uid>" }

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap thermocouple label="Thermocouple"
{
    Frame {
        Text item=Temp
    }
}

```

---
[Table of Contents](#table-of-contents)

### UV Light Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/UV_Light.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold).

##### Bricklet:

| property | descripition | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_uv_light |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
uv.uid=<your_uid>
uv.type=bricklet_uv_light
uv.callbackPeriod=10
uv.threshold=0
```

##### Items file entry (e.g. tinkerforge.items):

```
Number UV "UV Light [%.1f]" { tinkerforge="uid=<your_uid>" }

```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap uv label="UV Light"
{
    Frame {
        Text item=UV
    }
}

```

---
[Table of Contents](#table-of-contents)

### Voltage/Current Bricklet

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Voltage_Current.html)

#### Binding properties:

An entry in services/tinkerforge.cfg is only needed if you want to adjust [threshold and / or callbackPeriod](#callback-and-threshold),
if you want to use a [_symbolic name_](#sym_name) or adjust the averaging, voltage conversion time,
current conversion time of the device.

##### Bricklet:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| type | openHAB type name | bricklet_voltageCurrent |
|averaging|number of averages|0-7|
|voltageConversionTime|voltage conversion time|0-7|
|currentConversionTime|current conversion time|0-7|

```
voltageCurrent.uid=<your_uid>
voltageCurrent.type=bricklet_voltageCurrent
voltageCurrent.averaging=3
voltageCurrent.voltageConversionTime=4
voltageCurrent.currentConversionTime=4
```

##### Voltage sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | voltageCurrent_voltage |
| type | openHAB type name | voltageCurrent_voltage |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
vc_voltage.uid=<your_uid>
vc_voltage.subid=voltageCurrent_voltage
vc_voltage.type=voltageCurrent_voltage
vc_voltage.threshold=20
vc_voltage.callbackPeriod=100
```

##### Current sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | voltageCurrent_current |
| type | openHAB type name | voltageCurrent_current |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
vc_current.uid=<your_uid>
vc_current.subid=voltageCurrent_current
vc_current.type=voltageCurrent_current
vc_current.threshold=10
vc_current.callbackPeriod=100
```

##### Power sub device:

| property | description | values |
|----------|--------------|--------|
| uid | tinkerforge uid | get value from brickv |
| subid | openHAB subid of the device | voltageCurrent_power |
| type | openHAB type name | voltageCurrent_power |
| threshold | | see "Callback and Threshold" |
| callbackPeriod | | see "Callback and Threshold" |

##### tinkerforge.cfg:

```
vc_power.uid=<your_uid>
vc_power.subid=voltageCurrent_power
vc_power.type=voltageCurrent_power
vc_power.threshold=10
vc_power.callbackPeriod=100
```

##### Items file entry (e.g. tinkerforge.items):

```
Number Voltage       "Voltage [%d mV]"  { tinkerforge="uid=<your_uid>, subid=voltageCurrent_voltage" }
Number Current       "Current [%d mA]"  { tinkerforge="uid=<your_uid>, subid=voltageCurrent_current" }
Number Power         "Power [%d mW]"  { tinkerforge="uid=<your_uid>, subid=voltageCurrent_power" }
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
Text item=Voltage
Text item=Current
Text item=Power
```

---
[Table of Contents](#table-of-contents)

### Weatherstation Kit

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Kits/WeatherStation/WeatherStation.html#starter-kit-weather-station)

This hardware and config example makes use of Humidity, Pressure, Temperature, Ambient Light and LCD 20x4 Display Bricklets.

#### Configuration examples:

##### tinkerforge.cfg:

```
lcdbutton2.uid=<your_uid>
lcdbutton2.subid=button2
lcdbutton2.type=lcd_button
lcdbutton2.tactile=True

lcdbutton3.uid=<your_uid>
lcdbutton3.subid=button3
lcdbutton3.type=lcd_button
lcdbutton3.tactile=True

```

##### Items file entry (e.g. tinkerforge.items):

```
Number TF_Humdity                 "Humidity [%.1f %%]"  { tinkerforge="uid=<your_uid>" }
Number TF_Barometer               "Pressure [%.1f mBar]" { tinkerforge="uid=<your_uid>" }
Number TF_Barometer_Temperature   "Temperature [%.1f °C]" { tinkerforge="uid=<your_uid>, subid=temperature" }
Number TF_AmbientLight            "Luminance [%.1f Lux]" { tinkerforge="uid=<your_uid>" }
 
String TF_LCD             "LCD" { tinkerforge="uid=<your_uid>"}
Switch TF_LCDBacklight    "LCDBacklight" { tinkerforge="uid=<your_uid>, subid=backlight"}
Switch TF_Button0         "Button0" { tinkerforge="uid=<your_uid>, subid=button0"}
Switch TF_Button1         "Button1" { tinkerforge="uid=<your_uid>, subid=button1"}
Switch TF_Button2         "Button2" { tinkerforge="uid=<your_uid>, subid=button2"}
Switch TF_Button3         "Button3" { tinkerforge="uid=<your_uid>, subid=button3"}

Contact ConnectionStatus "ConnectionStatus [%]" {tinkerforge="uid=<your_server_ip>:<your_port>, subid=isconnected"}
Number Reconnects "Reconnects [%]" {tinkerforge="uid=<your_server_ip>:<your_port>, subid=connected_counter"}
```

##### Rules (e.g tinkerforge.rules):

```
import org.openhab.core.library.types.*

var Integer initialSleepTime = 10

rule "Weatherstation LCD init from Backlight"
when
    Item TF_LCDBacklight changed or System started
then
    createTimer(now.plusSeconds(initialSleepTime)) [|
        sendCommand(TF_LCD, "TFNUM<00>Temperature:")
        sendCommand(TF_LCD, "TFNUM<019>C")
        sendCommand(TF_LCD, "TFNUM<10>Humidity   :")
        sendCommand(TF_LCD, "TFNUM<119>%")
        sendCommand(TF_LCD, "TFNUM<20>Pressure   :")
        sendCommand(TF_LCD, "TFNUM<217>hPa")
        sendCommand(TF_LCD, "TFNUM<30>Luminance  :")
        sendCommand(TF_LCD, "TFNUM<317>Lux")
        sendCommand(TF_LCD, String::format("TFNUM<013>%4s", 
                                TF_Barometer_Temperature.state.format("%.1f")))
        sendCommand(TF_LCD, String::format("TFNUM<113>%4s", 
                                TF_Humdity.state.format("%.1f")))
        sendCommand(TF_LCD, String::format("TFNUM<213>%4s",
                                  TF_Barometer.state.format("%.0f")))
        sendCommand(TF_LCD, String::format("TFNUM<313>%4s", 
                                TF_AmbientLight.state.format("%.0f")))
    ]

end

rule "Goodbye"
when
    System shuts down
then
    sendCommand(TF_LCDBacklight, OFF)
end

rule "Weatherstation LCD Backlight"
    when
        Item TF_Button0 changed
    then
        if (TF_LCDBacklight.state == ON)
            sendCommand(TF_LCDBacklight, OFF)
        else
            sendCommand(TF_LCDBacklight, ON)
end

rule "Weatherstation LCD update temperature"
    when
        Item TF_Barometer_Temperature received update
    then
        sendCommand(TF_LCD, String::format("TFNUM<013>%4s",
            TF_Barometer_Temperature.state.format("%.1f")))
end

rule "Weatherstation LCD update humidity"
    when
        Item TF_Humdity received update
    then
        sendCommand(TF_LCD, String::format("TFNUM<113>%4s",
            TF_Humdity.state.format("%.1f")))
end
rule "Weatherstation LCD update airpressure"
    when
        Item TF_Barometer received update
    then
        sendCommand(TF_LCD, String::format("TFNUM<213>%4s",
            TF_Barometer.state.format("%.0f")))
end
rule "Weatherstation LCD update ambientLight"
    when
        Item TF_AmbientLight received update
    then
        sendCommand(TF_LCD, String::format("TFNUM<313>%4s",
            TF_AmbientLight.state.format("%.0f")))
end
```

##### Sitemap file entry (e.g tinkerforge.sitemap):

```
sitemap tf_weather label="Tinkerforge Weather Station"
{
    Frame {
        Text item=TF_Humdity 
        Text item=TF_Barometer
        Text item=TF_Barometer_Temperature
        Text item=TF_AmbientLight
        Switch item=TF_LCDBacklight
        Switch item=TF_Button0
        Switch item=TF_Button1
        Switch item=TF_Button2
        Switch item=TF_Button3
        Text item=ConnectionStatus
        Text item=Reconnects
    }
}
```

---
[Table of Contents](#table-of-contents)

### Brick Daemon

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricklets/Industrial_Quad_Relay.html)

#### Binding properties:

##### Items file entry (e.g. tinkerforge.items):

```
Contact brickd1connected {tinkerforge="<uid=<your_ip>:<your_port>, subid=connected"}
Number brickd1counter "Brickd1 [%d]" {tinkerforge="uid=<your_ip>:<your_port>, subid=connected_counter"}
```

##### Sitemap file entry (e.g. tinkerforge.sitemap):

```
sitemap tf label="TinkerForge Brickd"
{
    Frame label="Brickd" {
        Text item=brickd1connected
        Text item=brickd1counter
    }
}
```

##### tinkerforge.cfg:

```
hosts=<your_ip>:<your_port>
refresh=1000
```

---

## Red Brick

Technical description see [Tinkerforge Website](http://www.tinkerforge.com/en/doc/Hardware/Bricks/RED_Brick.html#red-brick)

openHAB is preinstalled on the RED Brick image and can be configured with the TinkerForge BrickViewer.

---

[Table of Contents](#table-of-contents)

## Developer Notes

### 1.7.1

#### New Devices

* [Accelerometer Bricklet](#accelerometer-bricklet)
* [Ambient Light Bricklet 2.0](#ambient-light-bricklet-v2)
* [Analog In Bricklet](#analog-in-bricklet)
* [Analog In Bricklet 2.0](#analog-in-bricklet-20)
* [Color Bricklet](#color-bricklet)
* [Dust Detector Bricklet](#dust-detector-bricklet)
* [Hall Effect Bricklet](#hall-effect-bricklet)
* [Industrial Dual Analog In Bricklet](#industrial-dual-analog-in-bricklet)
* [Laser Range Finder Bricklet](#laser-range-finder-bricklet)
* [Load Cell Bricklet](#load-cell-bricklet)
* [Piezo Speaker Bricklet](#piezo-speaker-bricklet)
* [Rotary Encoder Bricklet](#rotary-encoder-bricklet)

### 1.7.0

#### New Devices

* [Joystick Bricklet](#joystick-bricklet)
* [Linear Poti Bricklet](#linear-poti-bricklet)
* [Dual Button Bricklet](#dual-button-bricklet)
* [PTC Bricklet](#ptc-bricklet)
* [Industrial Dual 0-20mA Bricklet](#industrial-dual-0-20ma-bricklet)
* [Solid State Relay Bricklet](#solid-state-relay-bricklet)
* [Remote Switch dimmer](#remote-switch-bricklet)

#### New Features

* Tinkerforge Action Addon
* [Brick DC](#dc-brick) fully supported
* [Brick Servo](#servo-brick) fully supported
* Authentication support for brickd
* Tactile feature for [Dualbutton](#dual-button-bricklet), [LCD Buttons](#lcd-20x4-display-bricklet), [Joystick Button](#joystick-bricklet)
* [LED Strip](#led-strip-bricklet): sub devices and switching capabilities, configurable Frameduration, ChipType and Clockfrequency

#### Other changes

* updated Tinkerforge API to 2.1.4

#### Bugfixes

* Fix for configuration handling of device aliases

#### Brick DC

### Incompatible changes

* DriveMode now is one of "brake" or "coast" instead of "0" or "1"

```
dc_garage.driveMode=brake
```

* switchOnVelocity in services/tinkerforge.cfg is no longer needed and has gone.
It is replaced by per item configuration:
With the benefit that you can have serveral switch items with different speeds.
~~dc_garage.switchOnVelocity=10000~~

```
Switch DCSWITCH "DC Switch" {tinkerforge="uid=<your_uid>, speed=14000"}
```


##### Whats new?

Support for Dimmer, Rollershuter and Number items. Besides that the speed
can be set using a percent value.

The number items show the current velocity. The values are reported using the VelocityListener. 
"callbackPeriod" and "threshold" for the listener can be configured in services/tinkerforge.cfg. There is more
documentation about callback listeners at the official openHAB TinkerForgeBindig wiki page.

* callbackPeriod: milliseconds
* threshold: numeric value

##### New item configuration options

* speed: the target speed (Switch)
* max: the maximum speed (Dimmer, Rollershutter)
* min: the minimum speed (Dimmer, Rollershutter)
* step: the step value for increasing decreasing speed (Dimmer)
* leftspeed: the speed when the left rollershutter controller is pressed or command "DOWN" was send
* rightspeed: the speed when the right rollershutter controller is pressed or command "UP" was send
* acceleration: acceleration overrides value from services/tinkerforge.cfg
* drivemode: drivemode  overrides value from services/tinkerforge.cfg

#### Brick Servo

##### Whats new?

Support for Dimmer, Rollershuter and Number items. Besides that the speed
can be set using a percent value.

Number items will show the current position. 

##### New item configuration options

* velocity: the velocity used to reach the new position
* max: the maximum position (Dimmer, Rollershutter)
* min: the minimum position (Dimmer, Rollershutter)
* step: the step value for increasing decreasing position (Dimmer)
* leftposition: the target position to reach when the left rollershutter controller is pressed or command "DOWN" was send
* rightposition: the target position to reach when the right rollershutter controller is pressed or command "UP" was send
* acceleration: the acceleration

##### TinkerForge Action

The new openHAB action TinkerForgeAction comes up with the action tfServoSetposition.
tfServoSetposition(uid, num, position, velocity, acceleration) can be used to control the servo.
#### Example

```
tfServoSetposition("6Crt5W", "servo0", "-9000", "65535", "65535")
```

#### Tinkerforge Action Addon

* tfServoSetposition as explained above
* tfClearLCD(uid) uid is the uid of the LCD display. A call of tfClearLCD will clear the LCD display.


### 1.5.0

#### Bugfixes

* Reconnect support for IO16 Bricklet
* polled values now are only send once to the eventbus

#### New Devices

* [Remote Switch Bricklet](#remote-switch-bricklet)
* [Motion Detector Bricklet](#motion-detector-bricklet)
* [Multi Touch Bricklet](#multi-touch-bricklet)
* [Temperature IR Bricklet](#temperature-ir-bricklet)
* [Sound Intensity Bricklet](#sound-intensity-bricklet)
* [Moisture Bricklet](#moisture-bricklet)
* [Distance US Bricklet](#distance-us-bricklet)
* [Voltage/Current Bricklet](#voltagecurrent-bricklet)
* [Tilt Bricklet](#tilt-bricklet)

#### Other changes

* updated Tinkerforge API to 2.1.0
* Threshold values now have the unit as the sensor value (incompatible change, you have to update your services/tinkerforge.cfg)
* polling is only done for devices which don't support CallbackListeners / InterruptListeners

### 1.4.0

#### Bugfixes

* Missing updates of Items if a Tinkerforge Device is referenced in several Items

#### Incompatible Changes

* LCDBacklight is a sub device of LCD20x4 Bricklet (items file must be changed)
* LCD20x4Button posts an update not a command anymore (rules must be changed)
* IndustrialQuadRelay sub id numbering now starts from zero (items file must be changed)

#### New Devices

* [Industrial Quad Relay Bricklet](#industrial-quad-relay-bricklet)
* [Industrial Digital In 4 Bricklet](#industrial-digital-in-4-bricklet)
* [IO 16 Bricklet](#io-16-bricklet)

#### Other changes

* updated Tinkerforge API to 2.0.12
* support for serveral Item types
    * NumberItem
    * SwitchItem
    * ContactItem
* handle disconnected brickds
    * on binding startup make retries every second
    * when binding is running use the Tinkerforge autoreconnect feature

---
[Table of Contents](#table-of-contents)
