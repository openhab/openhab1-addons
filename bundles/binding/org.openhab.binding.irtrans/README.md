# IRTrans Binding

This binding communicates with the [IRTrans](http://www.irtrans.de) infrared emitter/transceiver.

The IRTrans binding currently supports the Ethernet version of the IRtrans transceiver and communicates with the transceiver over a TCP connection.

## Prerequisites

**Note:** the IRtrans binding makes use of the TCP/UDP binding, and therefore also the TCP binding has to be installed and configured.


## Binding Configuration

This binding does not need its own configuration.

## Item Configuration

The format of the binding configuration is simple and looks like this:

```
irtrans="[<command>:<ip address>:<port>:<led>:<device>:<ircommand>], [<command>:<ip address>:<port>:<led>:<device>:<ircommand>], ..."
```

where 

* `<ip address>` is the hostname or IP address in dotted notation of the remote host
* `<led>` is the transceiver led to be used to send or receive the infrared command, and is one of the following values: `DEFAULT`, `INTERNAL`, `EXTERNAL`, `ALL`, `ONE`, `TWO`, `THREE`, `FOUR`, `FIVE`, `SIX`, `SEVEN`, `EIGHT`. 

> **Note:** It is up to the user to check that the led value is valid for the device installed, e.g. not all IRtrans transceivers have the same number of led transmitters or received built-in. See the documentation of your IRtrans device.

* `<device>` is the name of the device (category), as defined using the IRtrans LAN Server software

* `<ircommand>` is the name of the infrared command, as defined using the IRtrans LAN Server software, to be sent to the transceiver when `<command>` is received. When an infrared command is received by the transceiver that matches the `<device>`:`<ircommand>`, then the Item will be updated with the `<command>`.

It is possible to define wildcards for `<device>` and `<ircommand>`: if either of them are equal to `**` then respectively, any device that defines the given `<ircommand>`, or any `<ircommand>` for the given `<device>`, will match and trigger the status update of the item. If you would put `**` for both `<device>` and `<ircommand>` then any infrared command received by the transceiver will cause the item to be updated.

> **Note:** the IRtrans device does not communicate which `<led>` a command is received by, so that information cannot be used to wildcard infrared commands received

> **Note:** If the item is of the type String, then any infrared command received by the transceiver will cause the Item to be updated with "`<device>`,`<ircommand>`". Likewise, any string passed on by the OpenHAB runtime will simply be parsed using the "`<device>`,`<ircommand>`" pattern. For such Items the `<command>` element has to be omitted in the configuration string

Here are some examples of valid binding configuration strings:

```
irtrans="[ON:192.168.0.1:3000:1:pioneer:voldown]" // send the 'voldown' infrared command of remote type 'pioneer' when ON is received
irtrans="[ON:192.168.0.1:3000:1:pioneer:*]" // accept any command from remote of type Pioneer, e.g. any key press on the remote
irtrans="[ON:192.168.0.1:3000:*:pioneer:voldown]" // send command on all leds
irtrans="[ON:192.168.0.1:3000:1:*:*]" // accept all infrared commands from any type of remote
irtrans="[192.168.0.1:3000:1:*:*]" // for String Items, take or update 'remote,command' strings that match the pattern
```

As a result, your lines in the items file might look like the following:

```
Switch  PioneerReceiver_Vol     "Pioneer"       (AV)    { irtrans="*[ON:192.168.0.1:3000:1:pioneer:volup], *[OFF:192.168.0.1:3000:1:pioneer:voldown]" }
```