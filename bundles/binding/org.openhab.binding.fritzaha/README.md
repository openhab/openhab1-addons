# Fritz AHA Binding

This binding provides access to AVM Home Automation devices, such as the Fritz!DECT 200 connected to a Fritz!Box or the Fritz!Powerline 546E. It is designed to allow for multiple hosts, for instance using both a Fritz!Box and a Fritz!Powerline.

[![Fritz AHA](http://img.youtube.com/vi/qYrpPrLY868/0.jpg)](http://www.youtube.com/watch?v=qYrpPrLY868)

The binding interfaces with hosts using a choice of two different interfaces, the query script used in the Fritz!OS UI and a webservice designed for interfacing with external applications.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/avmfritz/).

## Binding Configuration

The Fritz AHA Binding supports multiple hosts. Each host must be assigned a Host ID in `services/fritzaha.cfg`. Furthermore, connection data must be provided. For a standard Fritz!Box home gateway setup, the default settings are sufficient and only a password needs to be supplied.

To take advantage of SSL encryption, you need to add the SSL certificate to your Java keystore.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 10000   |    No    | refresh interval in milliseconds |
| `<hostID>`.host | fritz.box | No | host name of the FritzBox |
| `<hostID>`.port | 80        | No | port number on which to reach the FritzBox |
| `<hostID>`.protocol | http  | No | protocol with which to communicate |
| `<hostID>`.password| |  Yes   | your FritzBox password |

Where `<hostID>` is up to user choice.

## Item Configuration

### Webservice Binding Configuration

The preferred way for external applications to interface with AVM home automation devices is a webservice available since Fritz!OS 5.53. It provides an interface with comparatively low latency for switching and power/energy metering. Voltage and current measurements are not available and the refresh interval is slower than that of the Fritz!OS web interface (unless the web interface is accessed simultaneously, e.g. using the query script support of the binding or a browser).

Using the webservice, items are accessed using their AIN/MAC address, which is written on the back of the device. The space in Fritz!DECT AINs is not necessary, the colons in Fritz!Powerline MAC addresses are. If a MAC addressed is used as an AIN, commas must be used as seperators to prevent ambiguity from the use of colons in the MAC address.

### Switches

Switches can be addressed by simply specifying the host and the device:

```
fritzaha="<hostID>,<ain>"
```

where `<hostID>` is the ID assigned in the `services/fritzaha.cfg` file and `<ain>` is the actor ID number as listed on the device.

For example:

```
fritzaha="fritzbox,012345678910"
fritzaha="fritzpowerline,01:23:45:67:89:AB"
```

describe the switch on a Fritz!DECT device with AIN 01234 5678910 connected to the host "fritzbox" and the switch on the Fritz!Powerline device with MAC address 01:23:45:67:89:AB and host ID "fritzpowerline".

### Power/Energy Meters

Meters for current voltage, current and power are available as number items. The syntax is

```
fritzaha="<hostID>,<ain>,<meterType>"
```

where `<hostID>` is the ID assigned in the `services/fritzaha.cfg` file, `<ain>` is the actor ID number as listed on the device and `<meterType>` is the type of meter (either `power` or `energy`).

Example:

```
fritzaha="fritzbox,012345678910,power"
fritzaha="fritzpowerline,01:23:45:67:89:AB,energy"
```

### Temperature sensor

Temperature is available as a new meter type to number items.

Example:

```
fritzaha="fritzbox,012345678910,temperature"
```

## Query Script Binding Configuration

The Query script is part of the Fritz!OS UI and provides lots of information at fast refresh intervals. However, it is not intended for use with external applications and occupies the host for a long time (often more than a second per request), making it ill-suited for large amounts parallel requests. Furthermore, energy consumption is only available pre-formatted for Fritz!OS use and therefore not supported.

Using the query script, items are accessed using their internal ID, which can be obtained from the URLs used in the Fritz!OS UI. They are much shorter than AINs, allowing the binding to automatically identify them as such.

### Switches

Switches can be addressed by simply specifying the host and the device:

```
fritzaha="<hostID>,<deviceID>"
```

where `<hostID>` is the ID assigned in the `services/fritzaha.cfg` file and `<deviceID>` is the internal ID number of the device as assigned by the host.

For example:

```
fritzaha="fritzbox,16"
fritzaha="fritzpowerline,1000"
```

describes the switch on a Fritz!DECT device with internal ID 16 connected to the host "fritzbox" and the switch on a Fritz!Powerline device with host ID fritzpowerline and internal ID 1000.

### Voltage/Current/Power Meters

Meters for current voltage, current and power are available as number items. The syntax is

```
fritzaha="<hostID>,<deviceID>,<meterType>"
```

where `<hostID>` is the ID assigned in the openhab.cfg file, `<deviceID>` is the internal ID number of the device as assigned by the host and `<meterType>` is the type of meter (either voltage, current or power).

Example:
```
fritzaha="fritzpowerline,1000,voltage"
fritzaha="fritzbox,16,power"
```
