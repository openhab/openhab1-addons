# Serial Binding

The Serial binding allows openHAB to communicate in ASCII over serial ports attached to the openHAB server.

| Item Type | Function |
|-----------|----------|
| Switch    | the item will update its state to `ON` or `OFF` whenever data becomes available on the serial interface (or simply by short-cutting pins 2 and 7 on the RS-232 interface) |
| String    | the item will update its state to a string that is the data received from the serial interface.  Sending a command to the String item will be sent out as data through the serial interface. |
| Number    | the item will receive the RegEx result and attempt to convert the string to a number. |

## Port Configuration Notes

In most cases you will not need to perform special steps to access your serial ports, but these notes might be helpful.

### Linux Users

* If you are using **non standard serial ports** you have to adapt start.sh to have the serial port included. The `java` command line should then include the following parameters:

```
-Dgnu.io.rxtx.SerialPorts=/dev/ttyAMA0
```

where `/dev/ttyAMA0` is the path to your serial port. Please be aware to change all scripts you might use for startup (debug, automatic start in Linux, etc.).

* Your Linux distro might require that you add the `openhab` user to the `dialout` group to grant permission to read/write to the serial port.

```
sudo usermod -a -G dialout openhab
```

The user will need to logout from all login instances and log back in to see their new group added.  If you add your user to this group and still cannot get permission, rebooting the box to ensure the new group permission is attached to your user is suggested.

* If you use more than one USB serial converter like FTDI or CP2102, it may happen that your /dev/ttyUSB0 device is named /dev/ttyUSB1 after a reboot. To prevent this problem you can assign alias names for your serial devices by adding them to `/etc/udev/rules.d/99-com.rules`.

example:

```
SUBSYSTEM=="tty", ATTRS{idVendor}=="0403", ATTRS{idProduct}=="6001", ATTRS{serial}=="AE01F0PD", SYMLINK+="ttyMySensors"
SUBSYSTEM=="tty", ATTRS{idVendor}=="10c4", ATTRS{idProduct}=="ea60", ATTRS{serial}=="0001", SYMLINK+="ttyCulStick"
```

### Mac Users

If you are working with a Mac, you might need to install a driver for your USB-RS232 converter (e.g. [osx-pl2303](http://osx-pl2303.sourceforge.net/) or [pl2303](http://mac.softpedia.com/get/Drivers/PL2303-OS-X-driver.shtml)) and create the /var/lock folder, see the [rxtx troubleshooting guide](http://rxtx.qbang.org/wiki/index.php/Trouble_shooting#Mac_OS_X_users).

## Binding Configuration

This binding does not have a configuration.

## Item Configuration

The format has three variations:

```
serial="<port>@<baudrate>" 
serial="<port>@<baudrate>,REGEX(<regular expression>)" 
serial="<port>@<baudrate>,BASE64 
```

where:

* `<port>` is the identification of the serial port on the host system, e.g. `COM1` on Windows, `/dev/ttyS0` on Linux or `/dev/tty.PL2303-0000103D` on Mac.  The same `<port>` can be bound to multiple items.
* `<baudrate>` is the baud rate of the port. Backward compatibility is given; if no baud rate is specified  the serial binding defaults to 9600 baud.
* `REGEX(<regular expression>)` allows parsing for special strings or numbers in the serial stream. This is based on the [RegEx Service](https://github.com/openhab/openhab1-addons/wiki/Transformations#regex-transformation-service). This is optional. 
* `BASE64` enables the Base64 mode. With this mode all data received on the serial port is saved in Base64 format. In this mode also all data that is sent to the serial port has to be Base64 encoded. (This was implemented because some serial devices are using bytes that are not supported by the REST interface). 

Base64 can be decoded in the rules by importing `javax.xml.bind.DatatypeConverter` and then decoding the value like this:

```
DatatypeConverter::parseBase64Binary(ITEM.state.toString)
```

For encoding use the `printBase64Binary` method of the `DatatypeConverter`. This is optional. 

As a result, your lines in the items file might look like these:

```
Switch HardwareButton     "Bell"	           (Entrance)      { serial="/dev/ttyS0" }
String AVR                "Surround System"    (Multimedia)    { serial="/dev/ttyS1@115200" } 
Number Temperature        "My Temp. Sensor"    (Weather)       { serial="/dev/ttyS1@115200,REGEX(ID:2.*,T:([0-9.]*))" } 
```
