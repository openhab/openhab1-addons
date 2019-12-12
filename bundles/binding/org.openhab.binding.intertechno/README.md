# Intertechno Binding

You can send intertechno in every mode. I.e., if you are using the CUL in slow RF mode or BidCos mode you can still send intetechno commands. The firmware will take care of switching to intertechno mode, sending the command and switching back.

## Binding Configuration

This binding can be configured in the `services/culintertechno.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| device   |         |   Yes    | in the form `serial:<device>`, where `<device>` is a local serial port, or<br/> `network:<host>:<port>`, where `<host>` is the host name or IP address and `<port>` is the port number.  The `network` option works with ser2net from a tuxnet device |
| baudrate |         |   No     | one of 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 |
| parity   |         |   No     | one of EVEN, ODD, MARK, NONE, SPACE |

When using a serial port, you may need to add `-Dgnu.io.rxtx.SerialPorts=/dev/ttyACM0` in your server startup.  Please consult the [forum](https://community.openhab.org) for the latest information.

To use a networked CUL device for Intertechno from ser2net, this line in `/etc/ser2net.conf` on the remote `<host>` will publish the serial interface (replace /dev/ttySP1 with whatever is appropriate):

```
3333:raw:0:/dev/ttySP1:38400 8DATABITS NONE 1STOPBIT
```

From the above, your `device` property would be `network:<host>:3333`.


## Item Configuration

The item configuration depends on your specific intertechno device. Unfortunately there is no single manufacturer for these devices but many of them using intertechno a little differently.

openHAB currently has support for different intertechno device types, but probably not all. For more detailed information please have a look [here](http://www.fhemwiki.de/wiki/Intertechno_Code_Berechnung).

Currently openHab can handle FLS, Rev, Classic and "raw" devices.

For all supported, you simply have to read the position of the switches and use the read values as group and address.

## Examples

### FLS

```
{ culintertechno="type=fls;group=I;address=1" }
```

### REV

```
{ culintertechno="type=rev;group=I;address=1" }
```

### Classic

```
{ culintertechno="type=classic;group=I;address=1" }
```

### Intertechno V3

```
{ culintertechno="type=v3;id=01101011000011110000000000;channel=2" }
```

You have to provide the 26-digit id and the channel (0-15). 
Optional, you can provide the group parameter (with value "1") , which results in switching all items with the given id.

```
{ culintertechno="type=v3;id=01101011000011110000000000;group=1" }
```
 

### Raw mode

If you have an unsupported intertechno device you can fallback to the raw mode

```
{ culintertechno="type=raw;commandOn=FF00FF00FF;commandOff=FF00FF00F0" }
```

This configuration allows you to manually specify the complete commands to send in either ON or OFF state. The given commands will be sent directly to the CUL (prefixed with "is").
