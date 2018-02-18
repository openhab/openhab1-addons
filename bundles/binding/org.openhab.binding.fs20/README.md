### FS20 Binding

This binding enables support of sending and receiving FS20 messages via the CUL transport. You will need CULLite or similiar device from busware.de. This device needs to be flashed with the latest culfw firmware from culfw.de.

## Binding Configuration

This binding can be configured in the `services/fs20.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| device   |         |   Yes    | in the form `serial:<device>`, where `<device>` is a local serial port, or<br/> `network:<host>:<port>`, where `<host>` is the host name or IP address and `<port>` is the port number.  The `network` option works with ser2net from a tuxnet device |
| baudrate |         |   No     | one of 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 |
| parity   |         |   No     | one of EVEN, ODD, MARK, NONE, SPACE |

When using a serial port, you may need to add `-Dgnu.io.rxtx.SerialPorts=/dev/ttyACM0` in your server startup.  Please consult the [forum](https://community.openhab.org) for the latest information.

You may also need to add the `openhab` user to the `dialout` group.

To use a networked CUL device for FS20 from ser2net, this line in `/etc/ser2net.conf` on the remote `<host>` will publish the serial interface (replace /dev/ttySP1 with whatever is appropriate):

```
3333:raw:0:/dev/ttySP1:38400 8DATABITS NONE 1STOPBIT
```

From the above, your `device` property would be `network:<host>:3333`.

## Item Configuration

You can use Switch items and Dimmer items with this binding. You need to know the house address and device address of the device you want to receive messages or send messages to. To find these addresses you can start openHAB in debug mode. The CUL transport will print all out all received messages.

A sample switch configuration looks like this

```
Switch  WallSwitch1 "Wandschalter 1"  { fs20="C04B00" }
```

where `C04B` is the house address and `00` the device address. If you want to control switches or dimmers you can simply create you own house and device address. You can set such devices in a pairing mode and they will react to the first message they receive.

### Coming from FHEM?

In the `fhem.cfg` you find such statements:

```
define AmbiLight FS20 c04b 01
```

Just write the last "words" together and you have the full address you need for your item (see above).
