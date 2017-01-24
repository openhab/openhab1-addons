Documentation of the OneWire binding Bundle
# Introduction 

The OneWire bus system is a lightweight and cheap bus system mostly used for sensors (eg. temperature, humidity, counters and presence). But there are also switches available. The binding is designed to work as a client of the [ow-server](http://owfs.org/index.php?page=owserver_protocol) which implements the [owserver-protocol](http://owfs.org/index.php?page=owserver-protocol). The OneWire devices could be connected to the machine running ow-server by a USB adapter such as ds9490r or a serial adapter. For detailed information on OneWire please refer to http://en.wikipedia.org/wiki/One_wire or http://owfs.org.

For installation of the binding, please see the Wiki page [[Bindings]].

# Configuration

If your 1-Wire Bus System is physically connected to your server and working properly please follow the steps:

1. Install and configure the ow-server and ow-shell packages on your 1-Wire server
1. Copy the binding (e.g. openhab.binding.onewire-<version>.jar into the openhab/addons folder
1. Edit the relevant section in the openhab configuration file (openhab/configurations/openhab.cfg). If you are running the 1-Wire server on the same machine, please insert the local IP address of the server (127.0.0.1) and not localhost in the line onewire:ip. In this case on every onewire update you will have a file system access to the /etc/hosts file.

See the "Configuration settings" section below for more information on the settings.

# Generic Item Binding Configuration

In order to bind an item to a OneWire device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). 

**Basic Configuration for an OneWire Item Binding**

	onewire="deviceId=<deviceId>;propertyName<propertyName>"

The sensorId can be either the hex address or an alias if one is configured ( http://owfs.org/index.php?page=aliases )

**Optional parameter refreshinterval**

	refreshinterval=<value in seconds>

If the refreshinterval is not set, the interval defaults to 60 seconds.

If the parameter is set to 0, it only reads the value on system start.
If the parameter is set to -1, the property is not read at any time.

**Optional parameter ignoreReadErrors (OneWire binding openhab Version >= 1.8.0)**

         ignoreReadErrors

With this Parameter it is possible to support iButtons (https://en.m.wikipedia.org/wiki/IButton). In the normal Modus the binding gives an error on the event Bus if an One Wire item is not reachable. With this parameter set, the binding ignores read errors if an item is not present.

**Optional parameter ignore85CPowerOnResetValues**

	ignore85CPowerOnResetValues

Ignores the power-on reset value (+85°C) of DS18B20 devices.

**Examples**

	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10"
	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;ignore85CPowerOnResetValues"


# Type-modifiers

Type-Modifiers can be optionally configured for the items. They will be applied in the same order as the one in the description of the item. The binding provides the following modifiers. For example: by using modifiers you can calibrate onewire temperature devices or invert the value of contacts and switches.

### Number Items
* "add=-value-" - the AddModifier adds a given value to a read-value on read. On write, the given value is subtracted of the value to write.
* "multiply=-value-" - the MultiplyModifier multiplies a given value with the read-value on read. On write, value to write is divided by given value.
* "tukeyfilter" -  Modifier to filter sensor data. Restricts the data point value to be between lowerbound = qbottom - alpha * r and upperbound = qtop + alpha * r where qtop = top n-tile, qbottom = bottom ntile, and the range r = qtop - qbottom. The original Tukey filter drops points if they are outside of 1.5 * range, i.e. alpha = 1.5, and takes quartiles. Another implementation wrinkle: for slow changing data such as temperature, the binding may pick up the same data point over and over again. This compresses the range artificially, and will lead to spurious filtering. For that reason a point is added to the sample set only if it is not present there.

### Switch Items
* "invert" - the InvertModifier inverts the given Value to the opposite
* "pushbutton=-millis-" - this option makes it possible to use a relay like a pushbutton. It is possible to do that with a rule and timer, but then you have no control of the real time a button is pushed on slow systems.  

### Contact Items
* "invert" - the InvertModifier inverts the given Value to the opposite

## Configuration of modifiers:
The modifier name must be set in binding configuration:

	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;<modifier-name>"

### Example

	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;add=0.8;multiply=1.2;tukeyfilter"

# AlarmSwitch

A special Binding is the binding of numeric one wire device properties to openhab switch items. With this binding you can let openhab monitor your temperature or humidity with simple rules. A switch turns on when the read value from a device property is greater than maxWarning value or less than minWarning value.

**Configuration**

	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=<value>;minWarning=<value>"

# Examples

### Number Item

	Number OneWireTempSensor "Temp [%.1f °C]" {onewire="deviceId=28.67C6697351FF;propertyName=temperature;add=0.8;multiply=1.1;refreshinterval=10"}
	
This example uses the add and multiply modifier ("add" then "multiply").

	Number OneWireTempSensor "Temp [%.1f °C]" {onewire="deviceId=28.67C6697351FF;propertyName=temperature;add=0.8;tukeyfilter;refreshinterval=10"}
	
This example uses the add modifier and the tukey filter ("add" then "filter").

### Switch Item
	
	Switch OneWireSwitch "OneWireSwitch 6 [%s]"	{onewire="deviceId=29.F2FBE3467CC2;propertyName=PIO.6;invert;refreshinterval=10"}
	
This example uses an inverted PIO of an DS2408 as Switch, which can be turned on and off. The logic of the OneWire PIO is inverted in OpenHab (On=OFF and OFF=ON) by the InvertModifier.

	Switch OneWireSwitch "OneWireSwitch 6 [%s]" {onewire="deviceId=29.F2FBE3467CC2;propertyName=PIO.6;refreshinterval=10"}
	
Same example as before, but the logic is not inverted.

### Contact Item

	Contact OneWireSensorA "Sensor A [%s]" {onewire="deviceId=12.4AEC29CDBAAB;propertyName=sensed.A;invert;refreshinterval=15"}
	
This example uses a sensed property of an DS2406 as Contact, which is inverted by the InvertModifier.

### Alarm Switch
	
	Switch OneWireTempWarnMax "TempWarnMax [%s]" {onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=5;maxWarning=30"}

Switch turns on when value of device property is greater then maxWarning (30).

	Switch OneWireTempWarnMin "TempWarnMin [%s]" onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;minWarning=5"}

Switch turns on when value of device property is less then minWarning (5).

	Switch OneWireTempWarnMin "TempWarnMin [%s]" onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=30;minWarning=5"}

Switch turns on when value of device property is greater then maxWarning (30) or is less then minWarning (5).

### PushButton

	Switch OneWirePushButton onewire="deviceId=29.66C30E000000;propertyName=sensed.0;refreshinterval=10";pushbutton=500;invert",autoupdate="false"

### iButton (OneWire binding openhab Version >= 1.8.0)

	String OneWireKeyBlack "Key black [%s]" <key> {onewire="deviceId=uncached/01.234567790000;propertyName=r_id;refreshinterval=2"}

The parameter r_id reads the unique r_id of the iButton.
If you use an iButton please edit your owfs.conf and add or configure the entry `timeout_presence = 2` (seconds standard=120s). If you remove the iButton, the owserver will hold the item for the configured time in the one wire file system.

### LCD - Display

Example for writing messages to an HD44780 Display, controlled by a DS2408: http://owfs.org/index.php?page=lcd

I use a 4 bit wiring, so i have some free PIOs for push buttons: http://owfs.org/uploads/LCD%20Driver%20v2.0%20Schematic.pdf 

Items

	String 	OneWireLcdText		"LCD Text [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=LCD_H/message;refreshinterval=-1"}
	Switch 	OneWireLcdStrobe	"LCD Strobe [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=strobe;refreshinterval=-1"}
	Number 	OneWireLcdByte		"LCD Byte [%d]"		{onewire="deviceId=29.44C80E000000;propertyName=PIO.BYTE;refreshinterval=-1"}
	Switch 	OneWireLcdClear		"LCD Clear [%s]"	{onewire="deviceId=29.44C80E000000;propertyName=LCD_H/clear;refreshinterval=-1"}

Rule

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

# Cache

## Config
The cache is active by default, so only changed values will be written to the Event Bus.
If you want to disable the cache, you have to set 

	onewire:post_only_changed_values=false 

in the obenhab.cfg file.

## Problems
Because of unpredictable startup behavior of OpenHab, I sometimes see on my system, that OneWire-Binding starts to read and cache items from onewire-bus, before items become available in openhab. So these items stay Uninitialized until the onewire device state gets changed. 

Therefore I have built in 2 possible ways to reset the internal onewire-binding cache.

### All Items

	Switch OneWireClearCache "OneWireClearCache" {onewire="control=CLEAR_CACHE"}

When the Switch receives command ON, then the whole cache gets cleared.

### One Item

	String OneWireClearCacheOneItem "OneWireClearCacheOneItem" {onewire="control=CLEAR_CACHE"}

You have to send the name of one item to the String Item and the cached value for this item will be removed.

I use this with a simple rule. Every item (with binding to a onewire-device), which should be checked must be part of group grpOneWireDevices2Check.

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


# Generic Item Binding Configuration (OneWire binding openhab Version <= 1.6.2)

In order to bind an item to a OneWire device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). The syntax for the OneWire binding configuration string is explained here:

    onewire="<sensorId>#<unitId>"

Here are some examples of valid binding configuration strings:

    onewire="26.AF9C32000000#temperature"
    onewire="26.AF9C32000000#humidity"

The sensorId can be either the hex address or an alias if one is configured ( http://owfs.org/index.php?page=aliases )

    onewire="bedroom#temperature"

As a result, your lines in the items file might look like the following:

    Number Temperature_FF_Office 	"Temperature [%.1f ¬∞C]"	<temperature>	(FF_Office)		{ onewire="26.AF9C32000000#temperature" }

# Configuration settings
This section describes the configuration settings for the OneWire binding found in openhab.cfg.

## List of configuration settings
    onewire:ip
    onewire:port
    onewire:retry
    onewire:server_retries (in version 1.9.0 or later)
    onewire:server_retryInterval (in version 1.9.0 or later)
    onewire:tempscale
    onewire:post_only_changed_values

## Configuration setting descriptions

    onewire:ip - The IP address of the owserver (required)
    onewire:port - The port to connect to on the owserver (optional; defaults to 4304)
    onewire:retry - The number of times a failed read will be retried (optional; defaults to 3)
    onewire:tempscale - defines which temperature scale owserver should return temperatures in. Valid values are CELSIUS, FAHRENHEIT, KELVIN, and RANKINE (optional; defaults to CELSIUS).
    onewire:post_only_changed_values - only changed values are posted to the event-bus (optional; defaults to true)

The following settings are planned to be added in the 1.9.0 version of the binding.

    onewire:server_retries - The number of attempts that will be made to connect to the owserver after a failed connection attempt (optional; defaults to 3). If set to 0, no retry attempts will be made.
    onewire:server_retryInterval - The amount of time, in seconds, that will elapse between reconnection attempts (optional; defaults to 60).  May not be set to less than 5.
