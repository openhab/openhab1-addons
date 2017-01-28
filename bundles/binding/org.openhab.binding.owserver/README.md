# OWServer Binding

This binding reads values from 1-wire devices connected to an [OW-SERVER](http://www.embeddeddatasystems.com/OW-SERVER-1-Wire-to-Ethernet-Server-Revision-2_p_152.html) (both Rev. 1 and 2).

It does not write values to these devices.  See [this wiki page](https://github.com/openhab/openhab1-addons/wiki/Samples-Binding-Config#how-to-turn-onoff-a-switch-from-ow-server-via-http-binding) for how to send commands to a 1-wire device via OW-SERVER.

## Binding Configuration

This binding can be configured in the file `services/owserver.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<serverId1>`.host | |   Yes    | the IP address of the first OW-SERVER to control |
| `<serverId1>`.user | | if you enabled "Require name / password for all pages" in the web interface under `System Configuration \| Network` | the user name to access the web interface of the first OW-SERVER to control |
| `<serverId1>`.password | | if you enabled "Require name / password for all pages" in the web interface under `System Configuration \| Network` | the password to access the web interface of the first OW-SERVER to control |

where `<serverId1>` is a name you choose for this specific OW-SERVER, and must consist only of letters and numbers.  You can repeat the set of properties for different servers.

### Example

```
ow1.host=192.168.1.23
ow1.user=admin
ow1.password=eds
```

## Item Configuration

The syntax accepted is:

```
owserver="<<serverId1>:<ROMId>:<value-name>:<refreshInterval>"
```

where:

* the `<` in front of `<serverId1>` tells the binding to _read_ the following value.
* `<serverId1>` corresponds to the device which is introduced in the binding configuration. This value must match the value in the binding configuration.
* `<ROMId>` corresponds to the ROM-ID of the OneWire-device you want to query.
* `<value-name>` corresponds to the value you want to query.
* `<refreshInterval>` is the interval in milliseconds to refresh the data.

You can find the `<ROMid>` and the `<value-name>` in the `details.xml` from the web interface.

Like: http://192.168.1.23/details.xml

```xml
[..]
<owd_EDS0068 Description="Temperature, Humidity, Barometric Pressure and Light Sensor">
  <Name>EDS0068</Name>
  <Family>7E</Family>
  <ROMId>C200100000XXXXXX</ROMId>
  [..]
  <Temperature Units="Centigrade">31.8750</Temperature>
  <Humidity Units="PercentRelativeHumidity">37.6875</Humidity>
[..]
```

## Examples

items

```
Number bath_temp "Temperature [%.1f °C]"  { owserver="<ow1:C200100000XXXXXX:Temperature:60000" }
Number bath_humidity "Humidity [%.1f %%]" { owserver="<ow1:C200100000XXXXXX:Humidity:60000" }
```

## Limitations

- It is only possible to read values.
- The binding does only read values from the `details.xml`.  That means it is limited to 23 1-wire devices per OW-SERVER.
