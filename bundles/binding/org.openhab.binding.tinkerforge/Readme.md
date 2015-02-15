# 1.7.0
## New Devices
 * Joystick Bricklet
 * Linear Poti Bricklet
 * Dual Button Bricklet
## New Features
 * Tinkerforge Action Addon
 * Brick DC
 * Brick Servo
## Other changes
 * updated Tinkerforge API to 2.1.4

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
 