## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a Serial device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items). 

The format of the binding configuration is simple and looks like this:

    serial="<port>@<baudrate>" 
    serial="<port>@<baudrate>,REGEX(<regular expression>)" 
    serial="<port>@<baudrate>,BASE64 

* where `<port>` is the identification of the serial port on the host system, e.g. "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac.
* where `<baudrate>` is the baud rate of the port. Backward compatibility is given; if no baud rate is specified  the serial binding defaults to 9600 baud.
* where `REGEX(<regular expression>)` allows parsing for special strings or numbers in the serial stream. This is based on the [RegEx Service](https://github.com/openhab/openhab/wiki/Transformations#regex-transformation-service). This is optional and new in version 1.8. 
* where `BASE64` enables the Base64 mode. With this mode all data received on the serial port is saved in Base64 format. In this mode also all data that is sent to the serial port has to be Base64 encoded. (This was implemented because some serial devices are using bytes that are not supported by the rest interface) Base64 can be decoded in the rules by importing `javax.xml.bind.DatatypeConverter` and then decoding the value like this: `DatatypeConverter::parseBase64Binary(ITEM.state.toString)` For encoding use the `printBase64Binary` method of the DatatypeConverter. This is optional and new in version 1.8. 

Switch items with this binding will receive an ON-OFF update on the bus, whenever data becomes available on the serial interface (or simply by short-cutting pins 2 and 7 on the RS-232 interface)

String items will receive the submitted data in the form of a string value as a status update, while openHAB commands to a String item are sent out as data through the serial interface.

Number items will receive the RegEx result and attempt to convert the string to a number.

As a result, your lines in the items file might look like the following:

    Switch HardwareButton     "Bell"	           (Entrance)      { serial="/dev/ttyS0" }
    String AVR                "Surround System"    (Multimedia)    { serial="/dev/ttyS1@115200" } 
    Number Temperature        "My Temp. Sensor"    (Weather)       { serial="/dev/ttyS1@115200,REGEX(ID:2.*,T:([0-9.]*))" } 

###### Note
 If you are working with a Mac, you might need to install a driver for your USB-RS232 converter (e.g. [osx-pl2303](http://osx-pl2303.sourceforge.net/) or [pl2303](http://mac.softpedia.com/get/Drivers/PL2303-OS-X-driver.shtml)) and create the /var/lock folder, see the [rxtx troubleshooting guide](http://rxtx.qbang.org/wiki/index.php/Trouble_shooting#Mac_OS_X_users).

###### Note2
 If you are using **non standard serial ports** you have to adapt start.sh to have the serial port included. the java command line should then include the following parameters:

```
-Dgnu.io.rxtx.SerialPorts=/dev/ttyAMA0
```

where `ttyAMA0` is the path to your serial port. Please be aware to change all scripts you might use for startup (debug, automatic start in linux,...)

###### Note3
 With version 1.8 it is allowed to use a serial connection for multiple items. This has changed for the RegEx extension.

###### Note4
 If you are running on linux then you have to remember to add openhab as user to the dialout group to allow openhab permission to read/write to the serial port:

assuming you are running openhab as user "openhab" with the serial port access rights tied to the group "dialout" (i.e. ubuntu, check the docs for your distribution)

`sudo usermod -a -G dialout openhab`

The user will need to logout from all login instances and log back in to see their new group added.  If you add your user to this group and still cannot get permission, rebooting the box to ensure the new group permission is attached to your user is suggested.

###### Note5 
 If you use more than one USB serial converter like FTDI or CP2102, it may happen that your /dev/ttyUSB0 device is named /dev/ttyUSB1 after a reboot. To prevent this problem you can assign alias names for your serial devices by adding them to `/etc/udev/rules.d/99-com.rules`.

example:
```
SUBSYSTEM=="tty", ATTRS{idVendor}=="0403", ATTRS{idProduct}=="6001", ATTRS{serial}=="AE01F0PD", SYMLINK+="ttyMySensors"
SUBSYSTEM=="tty", ATTRS{idVendor}=="10c4", ATTRS{idProduct}=="ea60", ATTRS{serial}=="0001", SYMLINK+="ttyCulStick"
```

