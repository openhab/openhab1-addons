Documentation of the SNMP binding Bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

**NOTE:** This page is updated for the SNMP binding in V1.3. The binding strings are not compatible with the older binding since there is a lot more functionality.

**NOTE:** The ability to specify the remote port has been added in V1.9.

The SNMP binding allows SNMP GET (polling) and SNMP SET (commanding), and the reception of SNMP TRAPs (asynchronous events). SNMP is often found in network equipment, and the binding can be used to ensure your network is operating correctly. The out binding can be used to configure network settings.

## Generic Item Binding Configuration

In order to bind an item to a SNMP OID (to be precise an OID prefix), you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the SNMP binding configuration string is explained here:

    in:   snmp="<[address:community:oid:update]"
    out:  snmp=">[cmd:address:community:oid:value]"
    trap: snmp="<[address:community:oid:0]"

- Address is the IP address[/Port] of the SNMP device. The Port is optional, the default value is 161
- Community is the SNMP community string
- OID is the object id to GET or SET
- Value is the number to SET. This can only be an integer value.

Here are some examples of valid binding configuration strings:

    in:   snmp="<[192.168.2.111:public:.1.3.6.1.2.1.2.2.1.10.10:10000]"
    out:  snmp=">[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2]"
    trap: snmp="<[192.168.2.111:public:.1.3.6.1.2.1.2.2.1.10.10:0]"

**Note:** Config strings are only valid for Number and Switch items.

As a result, your lines in the items file might look like the following:
    Number Switch_POEState2  "PoE WiFi State  [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.6.1.2:10000]" }
    
    Switch Switch_POEEnable2 "PoE WiFi Enable [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:10000] >[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2] >[ON:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:1]" }

The above configuration allows reading the state of the Power over Ethernet on a Netgear switch, and to change the state of the power. In this configuration, it allows reading back the status, and turning on and off the power of a powered WiFi Access Point.

In case your Switch item stay "uninitialized" you may need to add a mapping to translate the value (like 0 or 1) to ON and OFF.

    Switch Switch_POEEnable2 "PoE WiFi Enable [%s]"  { snmp="<[192.168.2.111:public:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:10000:MAP(SwitchState.map)] >[OFF:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2] >[ON:192.168.2.111:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:1]" }

SwitchState.map

    0=ON
    1=OFF
    -=undefiniert 

## openhab.cfg configuration

The following configuration items can be set in the openhab file -:

- community: This is the default community for listening for traps (defaults to public).
- port: The listening port. Defaults to port 162 (see below).
- timeout: Sets the timeout period (in milliseconds) when polling SNMP GET and SET requests. Defaults to 1500ms.
- retries: Sets the number of retries before giving up. The retries will be sent every *timeout* milliseconds. Defaults to 0 (no retries).


## Binding Port

By default the SNMP binding binds to localhost on Port _162_ which is the SNMP default port. However on `*`nix Systems this port can only be bind by privileged users (root, sudo). Since openHAB won't be run under a privileged user a !BindException will be thrown.

As a workaround one could forward all traps to a port of your choice using _snmptrapd_. You should install _snmptrapd_ (on Ubuntu as part of the _snmpd_-package) somewhere on your network and run it under a privileged user. _snmptrapd_ forwards all traps to the machine openHAB is listening.

The _/etc/snmp/snmptrapd.conf_ should be enhanced like this

    disableAuthorization yes
    forward default udp:<ip of openhab machine>:<port which is configured in openhab.cfg>

Don't forget to restart _snmpd_ after reconfiguring by issuing _/etc/init.d/snmpd restart_