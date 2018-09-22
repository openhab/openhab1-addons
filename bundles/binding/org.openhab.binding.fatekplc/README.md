# Fatek PLC Binding

[Fatek PLC](http://fatek.com/en/prod.php?catId=1) is one kind of programmable logic controller.  This binding support native Fatek protocol to communicate with PLC.  For communication to PLC is used [JFatek library](http://www.simplify4u.org/jfatek/).

## Binding Configuration

The binding can be configured in the file `services/fatekplc.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 60000   |    No    | refresh interval in milliseconds which is used to poll values from the Fatek PLC server |
| `<plcName>`.connectionUri | | Yes    | connection URI for the `<plcName>` you provide, so multiple Fatek PLCs can be addressed. Supports either `tcp://` or `udp://` protocol.  For example, `udp://192.168.1.100?plcId=1` |

## Item Configuration

openHAB items can be bound to Fatek PLC by example definition

```
Item ... { fatekplc="fatek_binding" }
```

`fatek binding` is described below for specific item type.

For all items we can add options `refreshStep`, eg.

```
plc1:R100:refreshStep=2
```

This means that item will be updated every second poll cycle.

Only the item types documented below are supported.

### Color Items

```
plcName:REG1:REG2:REG3[:REG4][:RGB|HSB]
```

Last element indicates color mode used to write data to PLC, default value is HSB.

* REG1, REG2 and RE3 must be data type register.
* for HSB mode REG1 = H, REG2 = S, REG3 = B
* for RGB mode REG1 = R, REG2 = G, REG3 = B

REG4 is optional if you specified it, on/off command will use that register for corresponding on/off state.
If you not specified REG4 on/off command will write white or black value to color registers.


### Contact Items

```
plcName:[!]REG
```

We specified only one register.  If register name starts with "!" (exclamation mark) value read will be inverted.


### Switch Items

```
plcName:[!]REG1[:[!]REG2]
```

* REG1 is used for read state of switch
* REG2 is used for change state of switch, if is not specified - REG1 is used

If registers name starts with "!" (exclamation mark) value read and written will be inverted.


### DateTime Item


#### Version 1

```
plcName:Y:m:d:H:M:S
```

Where:

 * Y - year reg, if value in range 0 - 49 we add 2000, in range 50 - 99 we add 1900
 * m - month reg - range 1 - 12, (1 means January)
 * d - day of the month reg - The first day of the month has value 1
 * H - hour of the day reg - 24-hour clock
 * M - minute reg
 * S - second reg

All fields must be set, if you want to skip some of the item you can put number instead of register name.

Item accept only 16 bit data register.

You can end register name with `L` - lowest 8 bits or `H` - highest 8 bits.

Eg. read current PLC time

```
plcName:R4133:R4132:R4131:R4135H:R4135L:R4128
```

#### Version 2

```
plcName:REG[R][:factor=factorValue]
```

In REG we store seconds from begining of the UNIX epoch.

If we add R to the end of register name DateTime item will recognize as relative seconds to now.
We can read some counter which count seconds and we can display the time when counting was started.

We can also specific factor value, read seconds will be multiplied by this value.


### Dimmer Items

```
plcName:REG:[step=value][:factor=factorValue]
```

We use one data type register for value.

Step options can specified step value for INCREASE/DECREASE command.

We can also specific factor value, all read data will be multiplied by this value,
before write value from OpenHab will be divided by factor.


### Number Items

```
plcName:[+]REG[F][:transformationType(params)][:factor=factorValue]
```

If we start register name with + (plus sign) values read from plc will be recognized as unsigned number.

If we end register name with "F" values read from plc will be recognized as float number.
For float number 32 bits data register must be used.

We can also specific factor value, all read data will be multiplied by this value,
before write value from OpenHab will be divided by factor.

For Number item we can provide transformation function, eg: JS(file.js)


### Rollershutter Items

```
plcName:REG1:REG2:REG3[:factor=factorValue]
```

* REG1 is used for reading current positions in percent.
* REG2 is set to true/1 for UP command
* REG3 is set to true/1 for DOWN command
* for STOP command REG2 and REG3 is set to false/0.

We can also specific factor value, all read data will be multiplied by this value.
