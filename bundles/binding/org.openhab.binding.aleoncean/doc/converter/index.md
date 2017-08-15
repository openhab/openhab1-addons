The binding is using converter to translate between openHAB item / types and device parameters / types used by the aleoncean library.

# parameter class - item class

The conversion is done by the class supported by the deivce parameter and the class of the openHAB item.

## RockerSwitchAction - Rollershutter item

Maps up pressed / up released / down pressed / down released to up / down / stop and vice versa.

If you press up / down, the roller shutter will move up / down.

If the time between press and released event is shorter then one second, the roller shutter moving should be stopped. If the time is longer, the roller shutter should move until a end position is reached or you stop it by yourself (e.g. press again and release faster).

# parameter class - type class

The conversion is done by the class supported by the deivce parameter and one of the supported classes of the openHAB item.

## Boolean - OnOffType

Maps a boolean value to a on off type and vice versa.

* true: on
* false: off

## Boolean - UpDownType

Maps a boolean value to a up down type and vice versa.

* true: up
* false: down

## Double - DecimalType

Maps a double value to a decimal type and vice versa.

## Integer - DecimalType

Maps a integer value to a decimal type and vice versa.

## Long - DecimalType

Maps a long value to a decimal type and vice versa.

## RockerSwitchAction - DecimalType

Maps a rocker switch action to a decimal type and vice versa.

* 1: dim up pressed
* 2: dim up released
* 3: dim down pressed
* 4: dim down released

## RockerSwitchAction - OnOffType

You should use the converter parameter to setup the converter you would like to use.

### PressedUpDown (default)

Maps a rocker switch action to an on off type (e.g. switch) and vice versa.

* dim up pressed: on
* dim down pressed: off

### ReleasedUpDown

Maps a rocker switch action to an on off type (e.g. switch) and vice versa.

* dim up released: on
* dim down released: off

### UpPressedReleased

Maps a rocker switch action to an on off type (e.g. switch) and vice versa.

* dim up pressed: on
* dim up released: off

### DownPressedReleased

Maps a rocker switch action to an on off type (e.g. switch) and vice versa.

* dim down pressed: on
* dim down released: off

## RockerSwitchAction - UpDownType

* dim up pressed: up
* dim down pressed: down

## WindowHandlePosition - DecimalType

Maps a window handle position to a decimal type and vice versa.

Currently supported values:

* 4: up
* 8: down
* 48: left or right
