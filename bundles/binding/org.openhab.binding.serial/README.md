# Serial Binding

The Serial binding allows openHAB to communicate over serial ports attached to the openHAB server.

| Item Type | Function |
|-----------|----------|
| Switch    | the item will update its state to `ON` or `OFF` whenever data becomes available on the serial interface (or simply by short-cutting pins 2 and 7 on the RS-232 interface) |
| String    | the item will update its state to a string that is the data received from the serial interface.  Sending a command to the String item will be sent out as data through the serial interface. |
| Number    | the item will receive the RegEx result and attempt to convert the string to a number. |

## Port Configuration Notes

In most cases it will not be needed to perform special steps to access serial ports, but these notes might be helpful.

### Linux Users

* When using **non standard serial ports**, adapt start.sh to have the serial port included. The `java` command line should then include the following parameters:

```
-Dgnu.io.rxtx.SerialPorts=/dev/ttyAMA0
```

where `/dev/ttyAMA0` is the path to the serial port. Remember to change all scripts used for startup (debug, automatic start in Linux, etc.).

* A Linux distro might require adding the `openhab` user to the `dialout` group to grant permission to read/write to the serial port.

```
sudo usermod -a -G dialout openhab
```

The user will need to logout from all login instances and log back in to see their new group added.  If the user added to this group still cannot get permission, rebooting the box to ensure the new group permission is attached to the user is suggested.

* When using more than one USB serial converter like FTDI or CP2102, it may happen that the /dev/ttyUSB0 device is named /dev/ttyUSB1 after a reboot. To prevent this problem, alias names can be assigned to serial devices by adding them to `/etc/udev/rules.d/99-com.rules`.

example:

```
SUBSYSTEM=="tty", ATTRS{idVendor}=="0403", ATTRS{idProduct}=="6001", ATTRS{serial}=="AE01F0PD", SYMLINK+="ttyMySensors"
SUBSYSTEM=="tty", ATTRS{idVendor}=="10c4", ATTRS{idProduct}=="ea60", ATTRS{serial}=="0001", SYMLINK+="ttyCulStick"
```

### Mac Users

When working with a Mac, it may be necessary to install a driver for the USB-RS232 converter (e.g. [osx-pl2303](http://osx-pl2303.sourceforge.net/) or [pl2303](http://mac.softpedia.com/get/Drivers/PL2303-OS-X-driver.shtml)) and create the /var/lock folder; see the [rxtx troubleshooting guide](http://rxtx.qbang.org/wiki/index.php/Trouble_shooting#Mac_OS_X_users).

## Binding Configuration

This binding does not have a configuration.

## Item Configuration

The format has the following variations:

```
serial="<port>@<baudrate>" 
serial="<port>@<baudrate>,REGEX(<regular expression>)" 
serial="<port>@<baudrate>,BASE64"
serial="<port>@<baudrate>,ON(<On string>),OFF(<Off string>)" 
serial="<port>@<baudrate>,REGEX(<regular expression>), UP(<Up string>),DOWN(<Down string>), STOP(<Stop string>)" 
```

where:

* `<port>` is the identification of the serial port on the host system, e.g. `COM1` on Windows, `/dev/ttyS0` on Linux or `/dev/tty.PL2303-0000103D` on Mac.  The same `<port>` can be bound to multiple items.
* `<baudrate>` is the baud rate of the port. If no baud rate is specified, the binding defaults to 9600 baud.
* `REGEX(<regular expression>)` allows parsing for special strings or numbers in the serial stream. A capture group (e.g. REGEX(Position:([0-9.]*)) can be used to capture "12" in `Position:12` or substitution (e.g. REGEX(s/Position:100/ON/) or REGEX(s/Position:100/ON/g)) to replace (FIRST or ALL) "Position:100" strings in response with "ON". This is based on the [RegEx Service](https://github.com/openhab/openhab1-addons/wiki/Transformations#regex-transformation-service) and [ESH RegExTransformationService](https://github.com/eclipse/smarthome/tree/master/extensions/transform/org.eclipse.smarthome.transform.regex). This is optional.
* `BASE64()` enables the Base64 mode. With this mode all data received on the serial port is saved in Base64 format. All data that is sent to the serial port also has to be Base64 encoded. (This was implemented because some serial devices are using bytes that are not supported by the REST interface).
* `ON(<On string>),OFF(<Off string>)` used in conjunction with a Switch, this mapping will send specific commands to serial port and also match a serial command to specific ON/OFF state. This makes it unnecessary to use a rule to send a command to serial.
* `UP(<Up string>),DOWN(<Down string>),STOP(<Stop string>)` used in conjunction with a Rollershutter, this mapping will send specific commands to serial port. Use REGEX to parse Rollershutter postion (0-100%) coming as feedback over serial link.
* `CHARSET(<charset>)` set's the charset to be used for converting to a String and back to bytes when writing. (e.g. UTF-8, ISO-8859-1, etc.)

Base64 can be decoded in the rules by importing `javax.xml.bind.DatatypeConverter` and then decoding the value like this:

```
DatatypeConverter::parseBase64Binary(ITEM.state.toString)
```

For encoding, use the `printBase64Binary` method of the `DatatypeConverter`. This is optional. 

As a result, lines in the items file might look like these:

```
Switch         HardwareButton     "Bell"              (Entrance)      { serial="/dev/ttyS0" }
String         AVR                "Surround System"   (Multimedia)    { serial="/dev/ttyS1@115200" } 
Number         Temperature        "My Temp. Sensor"   (Weather)       { serial="/dev/ttyS1@115200,REGEX(ID:2.*,T:([0-9.]*))" } 
Switch         SerialRelay        "Relay Q1"          (Entrance)      { serial="/dev/ttyS0,ON(Q1_ON\n),OFF(Q1_OFF\n)" }
Rollershutter  SerialRollo        "Entrance Rollo"    (Entrance)      { serial="/dev/ttyS0,REGEX(Position:([0-9.]*)),UP(Rollo_UP\n),DOWN(Rollo_Down\n),STOP(Rollo_Stop\n)" }
Switch         RoloAt100          "Rolo at 100"       (Entrance)      { serial="/dev/ttyS0,REGEX(s/Position:100/ON/)" }
```
