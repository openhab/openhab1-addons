# OneWire Binding

The OneWire bus system is a lightweight and inexpensive bus system mostly used for sensors (eg. temperature, humidity, counters and presence). There are also switches available. The binding is designed to work as a client of the [ow-server](http://owfs.org/index.php?page=owserver_protocol) which implements the [owserver-protocol](http://owfs.org/index.php?page=owserver-protocol). The OneWire devices could be connected to the machine running ow-server by a USB adapter such as ds9490r or a serial adapter. For detailed information on OneWire please refer to http://en.wikipedia.org/wiki/One_wire or http://owfs.org.

## Prerequisites

1. Your 1-Wire Bus System is physically connected to your server and working properly.
1. Install and configure the ow-server and ow-shell packages on your 1-Wire server

## Binding Configuration

This binding can be configured in the file `services/onewire.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | IP address of the owserver.  Avoid using hostnames (including `localhost`) but instead prefer IP addresses (like `127.0.0.1`) to improve performance (otherwise every onewire update may result in a file system access to the /etc/hosts file) |
| port     | 4304    |   No     | port to connect to on the owserver |
| retry    | 3       |   No     | number of times a failed read will be retried |
| server_retries | 3 |   No     | number of attempts that will be made to connect to the owserver after a failed connection attempt. If set to 0, no retry attempts will be made. |
| server_retryInterval | 60 | No | amount of time, in seconds, that will elapse between reconnection attempts.  May not be set to less than 5. |
| tempscale | CELSIUS |   No    | defines which temperature scale owserver should return temperatures in. Valid values are CELSIUS, FAHRENHEIT, KELVIN, and RANKINE |
| post_only_changed_values | true | No | only changed values are posted to the event-bus.  Set to `false` to post all updates regardless. |


## Item Configuration

### Basic Configuration for an OneWire Item Binding

```
onewire="deviceId=<deviceId>;propertyName=<propertyName>[;refreshinterval=<refreshSeconds>][;ignoreReadErrors][;ignore85CPowerOnResetValues][;<typeModifiers>...]"
```

where:

* sections in `[brackets]` are optional
* `<deviceId>` can be either the hex address or an [alias](http://owfs.org/index.php?page=aliases) if one is configured
* `<propertyName>` is .
* `<refreshSeconds>` to refresh interval in seconds.  If not specified, the default is 60 seconds.  If set to 0, the value is only read when the binding is started.  If set to -1, the property is not read at any time.
* if `;ignoreReadErrors` is included, it becomes possible to support [iButtons](https://en.m.wikipedia.org/wiki/IButton). In the normal Modus the binding gives an error on the event Bus if an One Wire item is not reachable. With this parameter set, the binding ignores read errors if an item is not present.
* if `;ignore85CPowerOnResetValues` is included, the power-on reset value (+85°C) of DS18B20 devices is ignored.
* `<typeModifiers>` is explained in the section below

### Examples

```
onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10"
onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;ignore85CPowerOnResetValues"
```

## Type Modifiers

Type modifiers can be optionally configured for the items. They will be applied in the same order as the one in the description of the item. The binding provides the following modifiers. For example: by using modifiers you can calibrate onewire temperature devices or invert the value of contacts and switches.

### Number Items

* "add=-value-" - the AddModifier adds a given value to a read-value on read. On write, the given value is subtracted of the value to write.
* "multiply=-value-" - the MultiplyModifier multiplies a given value with the read-value on read. On write, value to write is divided by given value.
* "tukeyfilter" -  Modifier to filter sensor data. Restricts the data point value to be between lowerbound = qbottom - alpha * r and upperbound = qtop + alpha * r where qtop = top n-tile, qbottom = bottom ntile, and the range r = qtop - qbottom. The original Tukey filter drops points if they are outside of 1.5 * range, i.e. alpha = 1.5, and takes quartiles. Another implementation wrinkle: for slow changing data such as temperature, the binding may pick up the same data point over and over again. This compresses the range artificially, and will lead to spurious filtering. For that reason a point is added to the sample set only if it is not present there.

### Switch Items

* "invert" - the InvertModifier inverts the given Value to the opposite
* "pushbutton=-millis-" - this option makes it possible to use a relay like a pushbutton. It is possible to do that with a rule and timer, but then you have no control of the real time a button is pushed on slow systems.  

### Contact Items

* "invert" - the InvertModifier inverts the given Value to the opposite

### AlarmSwitch

A special binding is the binding of numeric one wire device properties to openHAB switch items. With this binding you can let openHAB monitor your temperature or humidity with simple rules. A switch turns on when the read value from a device property is greater than `maxWarning` value or less than `minWarning` value.

```
onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=<value>;minWarning=<value>"
```

## Examples

### Number Item

```
Number OneWireTempSensor "Temp [%.1f °C]" { onewire="deviceId=28.67C6697351FF;propertyName=temperature;add=0.8;multiply=1.1;refreshinterval=10" }
```

This example uses the add and multiply modifier ("add" then "multiply").

```
Number OneWireTempSensor "Temp [%.1f °C]" { onewire="deviceId=28.67C6697351FF;propertyName=temperature;add=0.8;tukeyfilter;refreshinterval=10" }
```

This example uses the add modifier and the tukey filter ("add" then "filter").

### Switch Item

```	
Switch OneWireSwitch "OneWireSwitch 6 [%s]"	{ onewire="deviceId=29.F2FBE3467CC2;propertyName=PIO.6;invert;refreshinterval=10" }
```

This example uses an inverted PIO of an DS2408 as Switch, which can be turned on and off. The logic of the OneWire PIO is inverted in OpenHab (On=OFF and OFF=ON) by the InvertModifier.

```
Switch OneWireSwitch "OneWireSwitch 6 [%s]" { onewire="deviceId=29.F2FBE3467CC2;propertyName=PIO.6;refreshinterval=10" }
```

Same example as before, but the logic is not inverted.

### Contact Item

```
Contact OneWireSensorA "Sensor A [%s]" { onewire="deviceId=12.4AEC29CDBAAB;propertyName=sensed.A;invert;refreshinterval=15" }
```

This example uses a sensed property of an DS2406 as Contact, which is inverted by the InvertModifier.

### Alarm Switch

```
Switch OneWireTempWarnMax "TempWarnMax [%s]" {onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=5;maxWarning=30" }
```

Switch turns on when value of device property is greater then maxWarning (30).

```
Switch OneWireTempWarnMin "TempWarnMin [%s]" { onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;minWarning=5" }
```

Switch turns on when value of device property is less then minWarning (5).

```
Switch OneWireTempWarnMin "TempWarnMin [%s]" { onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=30;minWarning=5" }
```

Switch turns on when value of device property is greater then maxWarning (30) or is less then minWarning (5).

### PushButton

```
Switch OneWirePushButton { onewire="deviceId=29.66C30E000000;propertyName=sensed.0;refreshinterval=10";pushbutton=500;invert",autoupdate="false" }
```

### iButton (OneWire binding openhab Version >= 1.8.0)

```
String OneWireKeyBlack "Key black [%s]" <key> { onewire="deviceId=uncached/01.234567790000;propertyName=r_id;refreshinterval=2" }
```

The parameter r_id reads the unique r_id of the iButton.  If you use an iButton please edit your owfs.conf and add or configure the entry 

```
timeout_presence = 2
``

(seconds standard=120s). If you remove the iButton, the owserver will hold the item for the configured time in the one wire file system.

### LCD - Display

Example for writing messages to an HD44780 Display, controlled by a DS2408: http://owfs.org/index.php?page=lcd

I use a 4 bit wiring, so i have some free PIOs for push buttons: http://owfs.org/uploads/LCD%20Driver%20v2.0%20Schematic.pdf 

items/lcd.items

```
String 	OneWireLcdText		"LCD Text [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=LCD_H/message;refreshinterval=-1"}
Switch 	OneWireLcdStrobe	"LCD Strobe [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=strobe;refreshinterval=-1"}
Number 	OneWireLcdByte		"LCD Byte [%d]"		{onewire="deviceId=29.44C80E000000;propertyName=PIO.BYTE;refreshinterval=-1"}
Switch 	OneWireLcdClear		"LCD Clear [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=LCD_H/clear;refreshinterval=-1"}
```

rules/lcd.rule

```
rule "write2LCD"
when
	Time cron "0/10 * * * * ?"	
then		
	var String lvText4LCD = "OpenHab";
			
	//Init and clear display
	OneWireLcdStrobe.sendCommand(ON)
	OneWireLcdByte.sendCommand(255)
	OneWireLcdClear.sendCommand(ON)
	
	//Write 2 LCD
	OneWireLcdText.sendCommand(lvText4LCD)
	
	//Set PIOs to GND
	OneWireLcdByte.sendCommand(255)
end
```

### Possibly Obsolete Workaround

Because of unpredictable startup behavior of openHAB (1.x I assume), I sometimes see on my system, that OneWire-Binding starts to read and cache items from onewire-bus, before items become available in openhab. So these items stay Uninitialized until the onewire device state gets changed. 

Therefore I have built in 2 possible ways to reset the internal onewire-binding cache.

### All Items

```
Switch OneWireClearCache "OneWireClearCache" {onewire="control=CLEAR_CACHE"}
```

When the Switch receives command ON, then the whole cache gets cleared.

### One Item

```
String OneWireClearCacheOneItem "OneWireClearCacheOneItem" {onewire="control=CLEAR_CACHE"}
```

You have to send the name of one item to the String Item and the cached value for this item will be removed.

I use this with a simple rule. Every item (with binding to a onewire-device), which should be checked must be part of group grpOneWireDevices2Check.

```
rule "checkOneWireDevices"
when
   Time cron "0 /5 * * * ?"
then
    grpOneWireDevices2Check?.members.forEach[element1,index1|
      if (element1.state==Undefined || element1.state==Uninitialized) {
              logError("OneWire","State of OneWireDevice: "+element1.name+" is +element1.state.toString)
             OneWireClearCacheOneItem.sendCommand(element1.name)
     }  
   ]
end
```
