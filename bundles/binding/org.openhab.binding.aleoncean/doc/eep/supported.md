# remote device

## A5-02-05

### Parameter

* TEMPERATURE_CELSIUS (r)

## A5-04-01

### Parameter

* HUMIDITY_PERCENT (r)
* TEMPERATURE_CELSIUS (r)

## A5-08-02

### Parameter

* SUPPLY_VOLTAGE_V (r)
* ILLUMINATION_LUX (r)
* TEMPERATURE_CELSIUS (r)
* MOTION (r)
* OCCUPANCY_BUTTON (r)

## A5-12-01

### Parameter

* ENERGY_WS (r)
* POWER_W (r)

## A5-20-01

### Parameter

* POSITION_PERCENT (r): The position that was last received from the actuator.
* TEMPERATURE_CELSIUS (r): The temperature that was last received from the actuator.
* SETPOINT_POSITION_PERCENT (w): The position that should be set (if TEMPERATURE_CONTROL_ENABLE is false).
* SETPOINT_TEMPERATURE_CELSIUS (rw): The temperature that should be set (if TEMPERATURE_CONTROL_ENABLE is true).
* TEMPERATURE_CONTROL_ENABLE (rw): Flag if the set point is controlled by temperature (or position).
* TEMPERATURE_CONTROL_CUR_TEMP (rw): The current temperature used for proportional–integral controller (used if TEMPERATURE_CONTROL_ENABLE is true).

__Pre-requirement__: [teach-in](#remote device, send data)

## A5-38-08 - CMD 0x02

### Parameter

* POSITION_PERCENT (rw): The dimming value
* SWITCH (rw): On / off

## D2-01-08

### Parameter

* ENERGY_WS (r)
* POWER_W (r)
* SWITCH (rw)

This was tested with ["Funktionsstecker FS1" of "Telefunken Smart Building"](http://www.telefunken-sb.de/produkte/aufputz/funktionsstecker.html).

__Pre-requirement__: [teach-in](#remote device, send data)

## F6-02-01

### Parameter

* BUTTON_DIM_A (r)
* BUTTON_DIM_B (r)

## F6-02-02

### Parameter

* BUTTON_DIM_A (r)
* BUTTON_DIM_B (r)

## F6-10-00

### Parameter

* WINDOW_HANDLE_POSITION (r)

## F6-10-01

### Parameter

* WINDOW_HANDLE_POSITION (r)

# local device

## F6-02-01

### Parameter

* BUTTON_DIM_A (w)
* BUTTON_DIM_B (w)

## F6-02-02

### Parameter

* BUTTON_DIM_A (w)
* BUTTON_DIM_B (w)

# Teach-in

## remote device, send data

To communicate with that remote device the local EnOcean device must be teached-in to the remote device.

You have to clear the old communication partner on the remote device and teach-in your system (see manual of the remote device, how to teach-in new devices).

The most remote devices will be reply a teach-in request if a remote device sends a teach-in request.

For the teach-in reply the local ID of the item is used.
So you MUST use only one local ID per remote ID for that device type.
The binding is using addressed telegrams to communicate to the remote devices,
so you could use the same local ID for multiple remote IDs (if the remote devices evaluates the destination address, too).
