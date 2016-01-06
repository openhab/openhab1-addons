# 1.7.0
## New Devices
 * Joystick Bricklet
 * Linear Poti Bricklet
 * Dual Button Bricklet

## New Features
 * Tinkerforge Action Addon
 * Brick DC fully supported
 * Brick Servo fully supported

## Other changes
 * updated Tinkerforge API to 2.1.4

## Brick DC
### Incompatible changes
* DriveMode now is one of "brake" or "coast" instead of "0" or "1"
```
tinkerforge:dc_garage.driveMode=brake
```

* switchOnVelocity in openhab.cfg is no longer needed and has gone.
It is replaced by per item configuration:
With the benefit that you can have serveral switch items with different speeds.
~~tinkerforge:dc_garage.switchOnVelocity=10000~~
```
Switch DCSWITCH "DC Switch" {tinkerforge="uid=62Zduj, speed=14000"}
```


### Whats new?
Support for Dimmer, Rollershuter and Number items. Besides that the speed
can be set using a percent value.

The number items show the current velocity. The values are reported using the VelocityListener. 
"callbackPeriod" and "threshold" for the listener can be configured in openhab.cfg. There is more
documentation about callback listeners at the official openHAB TinkerForgeBindig wiki page.

* callbackPeriod: milliseconds
* threshold: numeric value

### New item configuration options
* speed: the target speed (Switch)
* max: the maximum speed (Dimmer, Rollershutter)
* min: the minimum speed (Dimmer, Rollershutter)
* step: the step value for increasing decreasing speed (Dimmer)
* leftspeed: the speed when the left rollershutter controller is pressed or command "DOWN" was send
* rightspeed: the speed when the right rollershutter controller is pressed or command "UP" was send
* acceleration: acceleration overrides value from openhab.cfg
* drivemode: drivemode  overrides value from openhab.cfg

## Brick Servo
### Whats new?
Support for Dimmer, Rollershuter and Number items. Besides that the speed
can be set using a percent value.

Number items will show the current position. 

# New item configuration options
* velocity: the velocity used to reach the new position
* max: the maximum position (Dimmer, Rollershutter)
* min: the minimum position (Dimmer, Rollershutter)
* step: the step value for increasing decreasing position (Dimmer)
* leftposition: the target position to reach when the left rollershutter controller is pressed or command "DOWN" was send
* rightposition: the target position to reach when the right rollershutter controller is pressed or command "UP" was send
* acceleration: the acceleration

### TinkerForge Action
The new openHAB action TinkerForgeAction comes up with the action tfServoSetposition.
tfServoSetposition(uid, num, position, velocity, acceleration) can be used to control the servo.
#### Example
```
tfServoSetposition("6Crt5W", "servo0", "-9000", "65535", "65535")
```

## Tinkerforge Action Addon
* tfServoSetposition as explained above
* tfClearLCD(uid) uid is the uid of the LCD display. A call of tfClearLCD will clear the LCD display.


# 1.5.0
## Bugfixes
  * Reconnect support for IO16 Bricklet
  * polled values now are only send once to the eventbus

## New Devices
 * Remote Switch Bricklet
 * Motion Detection Bricklet
 * Bricklet MultiTouch
 * Bricklet TemperatureIR
 * Bricklet SoundIntensity
 * Bricklet Moisture
 * Bricklet DistanceUS
 * Bricklet VoltageCurrent
 * Bricklet Tilt

## Other changes
 * updated Tinkerforge API to 2.1.0
 * Threshold values now have the unit as the sensor value (incompatible change, you have to update your openhab.cfg)
 * polling is only done for devices which don't support CallbackListeners / InterruptListeners

# 1.4.0
## Bugfixes
  * Missing updates of Items if a Tinkerforge Device is referenced in several Items

## Incompatible Changes
  * LCDBacklight is a sub device of LCD20x4 Bricklet (items file must be changed)
  * LCD20x4Button posts an update not a command anymore (rules must be changed)
  * IndustrialQuadRelay sub id numbering now starts from zero (items file must be changed)

## New Devices
 * Bricklet Industrial Quad Relay
 * Bricklet Industrial Digital In 4
 * Bricklet IO-16

## Other changes
 * updated Tinkerforge API to 2.0.12
 * support for serveral Item types
   * NumberItem
   * SwitchItem
   * ContactItem
 * handle disconnected brickds
   * on binding startup make retries every second
   * when binding is running use the Tinkerforge autoreconnect feature
 