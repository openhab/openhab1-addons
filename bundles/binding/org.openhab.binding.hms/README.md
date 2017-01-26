# HMS Binding

This binding enables support of receiving HMS messages via the CUL transport.

## Binding Configuration

This binding can be configured in the `services/hms.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| device   |         |   Yes    | in the form `serial:<device>`, where `<device>` is a local serial port, or<br/> `network:<host>:<port>`, where `<host>` is the host name or IP address and `<port>` is the port number.  The `network` option works with ser2net from a tuxnet device |
| baudrate |         |   No     | one of 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 |
| parity   |         |   No     | one of EVEN, ODD, MARK, NONE, SPACE |

When using a serial port, you may need to add `-Dgnu.io.rxtx.SerialPorts=/dev/ttyACM0` in your server startup.  Please consult the [forum](https://community.openhab.org) for the latest information.

To use a networked CUL device for HMS from ser2net, this line in `/etc/ser2net.conf` on the remote `<host>` will publish the serial interface (replace /dev/ttySP1 with whatever is appropriate):

```
3333:raw:0:/dev/ttySP1:38400 8DATABITS NONE 1STOPBIT
```

From the above, your `device` property would be `network:<host>:3333`.

## Item configuration

This binding can only handle HMS components returning temperature or humidity values. Since these values are of type number, it only makes sence to use items of type Number or Text. In your items configuration file this could look like

```
Number Temperature_Outdoor "Temp [%.1f °C]"     <temperature>   (Weather)   { hms="address=A1DB;datapoint=TEMPERATURE" }
Number Humidity_Outdoor    "Humidity [%.1f %%]" <humidity>      (Weather)   { hms="address=A1DB;datapoint=HUMIDITY" }
```

In the example, above the address of the HMS device is `A1DB`. By use of the attribute `datapoint` one can either assign the `HUMIDITY` or the `TEMPERATURE` value to a device.

If you don't know the address of your HMS device, simply search for lines like the following in your `openhab.log`, where the device address is printed out every time a new value is received. Keep in mind that new values are only reported every 5 minutes.

```
HMSBinding[:124]- device: A1DB, T:  14.5,    H: 77.5, Bat.: ok
```
