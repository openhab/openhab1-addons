# Expire Binding

The Expire binding will post an update or command that you specify (the "expire" update/command) to items it is bound to after a period of time has passed.  If you don't specify an update or command, the default is to post an Undefined (`UnDefType.UNDEF`) update to the item.

The expiration time will be started or restarted every time an item receives an update or command *other than* the specified "expire" update/command.  

Any future expiring update or command is cancelled if the item receives an update or command that matches the "expire" update/command.

## Binding Configuration

The binding itself has no configuration.

## Item Configuration

The Expire binding accepts a duration of time that can be made up of hours, minutes and seconds in the format

```
expire="1h 30m 45s"
expire="1h05s"
expire="55h 59m        12s"
```

Any part is optional, but any part present must be in the given order (hours, minutes, seconds).  Whitespace is allowed between sections.

This section can optionally be followed by a comma and the state or command to post when the timer expires.  When this optional section is not present, it defaults to posting an Undefined (`UnDefType.UNDEF`) update to the item.

```
expire="1h,command=STOP"  (send STOP command after one hour)
expire="5m,state=0"       (update state to 0 after five minutes)
expire="3m12s,Hello"      (update state to Hello after three minutes and 12 seconds)
expire="2h"               (update state to Undefined two hours after last value)
```
Note that the `state=` part is optional.

Also note that the type of item (`String`, `Number`, `Switch`, `Contact`, etc.) must accept the command or state you specify.  The binding works with all item, state and command types.

## Examples

Set a temperature sensor's state to -999 if five minutes pass since the last numerical update:

```
Number Temperature "Temp [%.1f °C]" { mysensors="24;1;V_TEMP", expire="5m,-999" }
```

Turn off a light (Z-Wave node 3) one and a half hours after it was turned on:

```
Switch HallLight "HallLight [%s]" { zwave="3", expire="1h30m,command=OFF" }
```

Mark a motion sensor as CLOSED 30 seconds after it was opened:

```
Contact MotionSensor "MotionSensor [%s]" { http="<[http://motion/sensor:60000:JSONPATH($.state)]", expire="30s,state=CLOSED" }
```

Boil an egg for seven minutes using a Z-Wave-controlled cooker:

```
Switch EggCooker "Egg Cooker [%s]" { zwave="12", expire="7m,command=OFF" }
```

> ⚠️ If another binding is repeatedly updating the state of the item to be the same state it already was, the expiration timer will continue to be reset into the future.  Dedicating an item to the expiration function (so it doesn't receive repeated updates from another binding) would avoid unwanted behavior, should it apply in your case.
