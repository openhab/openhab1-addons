# FHT Binding

FHT is a range of devices produced by busware.de which allows to interact with various protocols and devices over radio frequencies. It's also possible to build one on your own.


- Original Manufacturer: http://busware.de
- Build your own (German) http://www.fhemwiki.de/wiki/Selbstbau_CUL
- Original firmware: http://culfw.de
- Alternative firmware: https://github.com/heliflieger/a-culfw

## Binding Configuration

This binding can be configured in the `services/fht.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| device   |         |   Yes    | in the form `serial:<device>`, where `<device>` is a local serial port, or<br/> `network:<host>:<port>`, where `<host>` is the host name or IP address and `<port>` is the port number.  The `network` option works with ser2net from a tuxnet device |
| baudrate |         |   No     | one of 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 |
| parity   |         |   No     | one of EVEN, ODD, MARK, NONE, SPACE |

When using a serial port, you may need to add `-Dgnu.io.rxtx.SerialPorts=/dev/ttyACM0` in your server startup.  Please consult the [forum](https://community.openhab.org) for the latest information.

## Item Configuration

Receive the valve position in percent. It can be bound to a Number item.  Read-only.

```
{ fht="TR3D4900" }
```

Receive the window state of a FHT window contact.  Read-only.

```
{ fht="TR952E90" }
```

This would be a binding to receive reports of a FHT80b. Currently only the measured temperature is received.

```
{ fht="TR3D49" }
```

This binding enables you to send commands to a FHT80b. Currently this will be mostly the desired temperature. But you need also a writeable binding to update the time on your FHT80b.

```
{ fht="TW3D49" }
```
