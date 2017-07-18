# SNMP Binding

The SNMP binding allows SNMP GET (polling) and SNMP SET (commanding), and the reception of SNMP TRAPs (asynchronous events). SNMP is often found in network equipment, and the binding can be used to ensure your network is operating correctly. The out binding can be used to configure network settings.

## Binding Configuration

This binding can be configured in the file `services/snmp.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| community | public |    No    | default community for listening for traps (defaults to public). |
| port     | 162     |    No    | listening port.  See [Binding Port](#binding-port) below. |
| timeout  | 1500    |    No    | timeout period (in milliseconds) when polling SNMP GET and SET requests. |
| retries  | 0       |    No    | number of retries before giving up. The retries will be sent every `timeout` milliseconds. 0 means no retries. |

### Binding Port Workaround

By default, the SNMP binding binds to localhost on port 162, which is the SNMP default port. However on `*`nix Systems this port can only be bound by privileged users (root, sudo). Since it's recommended to run openHAB as a non-privileged user, a `BindException` will be thrown.

As a workaround, one could forward all traps to a port of your choice using `snmptrapd`. You should install `snmptrapd` somewhere on your network and run it under a privileged user.  (On Ubuntu, for example, it's part of the `snmpd` package.)  `snmptrapd` forwards all traps to the machine on which openHAB is listening.

The `/etc/snmp/snmptrapd.conf` should be enhanced like this

```
disableAuthorization yes
forward default udp:<ip of openhab machine>:<port which is configured in openhab.cfg>
```

Don't forget to restart `snmpd` after reconfiguring by issuing `/etc/init.d/snmpd restart` (or equivalent, depending on your Linux distro).

## Item Configuration

The binding accepts Number, String and Switch items. Setting values is supported via Switch items and you can only set Integer values.

The syntax for the SNMP binding configuration string depending on whether you are using SNMP GET, SET or TRAP:

GET

```
snmp="<[<address>:<community>:<oid>:<update>]"
```

SET

```
snmp=">[<cmd>:<address>:<community>:<oid>:<value>]"
```

TRAP

```
snmp="<[<address>:<community>:<oid>:0]"
```

where:

* `<address>` is the IP address[/Port] of the SNMP device. The Port is optional, the default value is 161
* `<community>` is the SNMP community string
* `<oid>` is the object ID to GET or SET
* `<value>` is the number to SET. This can only be an integer value.

Here are some examples of valid binding configuration strings:

```
snmp="<[192.168.2.111:public:.1.3.6.1.2.1.2.2.1.10.10:10000]"
snmp=">[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2]"
snmp="<[192.168.2.111:public:.1.3.6.1.2.1.2.2.1.10.10:0]"
```

## Examples

items/snmpdemo.items

```
Number Switch_POEState2  "PoE WiFi State  [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.6.1.2:10000]" }
Switch Switch_POEEnable2 "PoE WiFi Enable [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:10000] >[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2] >[ON:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:1]" }
String switch1p01desc "switch1 port 01 description [%s]" { snmp="<[192.168.3.222:public:.1.3.6.1.4.1.11863.1.1.3.2.1.1.1.1.2.1:10000]" }
```

The above configuration reads the state of the Power-over-Ethernet on a Netgear switch, and allow changing the state of the power. In this configuration, it allows reading back the status, and turning on and off the power of a powered WiFi Access Point. The String item shows the port description of a TP-Link Switch.

In case your Switch item stays uninitialized, you may need to add a mapping to translate the value (like 0 or 1) to ON and OFF.

```
Switch Switch_POEEnable2 "PoE WiFi Enable [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:10000:MAP(SwitchState.map)] >[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2] >[ON:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:1]" }
```

transform/SwitchState.map

```
0=ON
1=OFF
-=undefined
```
