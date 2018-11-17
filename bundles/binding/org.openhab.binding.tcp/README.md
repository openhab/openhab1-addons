# TCP & UDP Binding

The TCP and UDP bindings provide basic support for simple ASCII-based protocols. They send and receive data as ASCII strings. Data sent out is by default padded with a CR/LF. This should be sufficient for many home automation devices that take simple ASCII-based control commands, or that send back text-based status messages.

The TCP part of the binding has a built-in mechanism to keep connections to remote hosts alive, and will reset connections at regular intervals to overcome the limitation of "stalled" connections or remote hosts. (see broken comment at the bottom)

The TCP & UDP Bindings act as a network client (outgoing direction ">[]" config) or as a network server (incoming direction "<[]").

The TCP & UDP Binding has a single config file for tcp.cfg and another for udp.cfg, and cannot have different parameters for different hosts.

## Binding Configuration

The TCP and UDP bindings can be configured in the files `services/tcp.cfg` and `services/udp.cfg`, respectively.

> Note: This is optional for the configuration and not necessary for receiving data. Item-defintions are enough for receiving data. (Developer confirm? 20150128). There's a bug in the binding that requires at least one udp configuration to be defined or the binding will not send UDP messages.

The indicated default values apply to both bindings unless otherwise noted.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refreshinterval |  | only for receiving data | Refresh interval in milliseconds for polling data.  Example: `250` |
| port     |         | only for receiving data | Port to listen on for incoming connections |
| addressmask | false |    No   | Set to `true` to allow masks in ip:port addressing, e.g. 192.168.0.1:`**` etc. |
| reconnectcron |    |          | Cron-like string to reconnect remote ends, e.g for unstable connection or remote ends. Example: `0 0 0 ** * ?` |
| retryinterval |    |    No    | Interval between reconnection attempts when recovering from a communication error, in seconds.  Example: `5` |
| queue    | false   |    No    | Queue data whilst recovering from a connection problem (TCP only) |
| buffersize |1024   |    No    | Maximum buffer size whilst reading incoming data |
| preamble |         |    No    | Pre-amble string that will be put in front of data being sent |
| postamble | `\r\n` |    No    | Post-amble that will be appended to data being sent |
| blocking | false`  |    No    | Perform all write/read (send/receive) operations in a blocking mode, e.g. the binding will wait for a reply from the remote end after data has been sent |
| timeout | 3000     |    No    | Timeout, in milliseconds, to wait for a reply when initiating a blocking write/read operation |
| updatewithresponse | false | No |Update the status of items using the response received from the remote end (if the remote end sends replies to commands) |
| itemsharedconnections | false | No | Set to `true` to share connections within the item binding configurations |
| bindingsharedconnections | false | No | Set to `true` to share connections between item binding configurations: If true will imply itemsharedconnections=true (and give warning if it was false) |
| directionssharedconnections | true | No | Set to `false` to not share connections between inbound and outbound connections: If true will imply bindingsharedconnections=true (and give warning if it was false) |

## Item Configuration

The syntax for the TCP & UDP binding configuration string is explained here:

**Note:** The examples here below are for the TCP protocol. UDP binding configuration are exactly the same, one only has to substitute `"tcp="` with `"udp="`

The format of the binding configuration is simple and looks like this:

```
tcp="<direction>[<command>:<ip address>:<port>:<transformationrule>], <direction>[<command>:<ip address>:<port>:<transformationrule>], ..."
tcp="<direction>[<ip address>:<port>:<transformationrule>]"
```

where `<direction>` is one of the following values:

- `<` for inbound-triggered communication, whereby the openHAB runtime will act as a server and listen for incoming connections from the specified `<ip address>:<port>`
- `>` for outbound-triggered communication, whereby the openHAB runtime will act as a client and establish an outbound connection to the specified `<ip address>:<port>`

`<command>` is the openHAB command. For String items, `<command>:` can be omitted or be '*'. Omit the command if using generic mapping via transformations

`<ip address>` is the hostname or IP address in dotted notation of the remote host.

`<transformationrule>` can be one of:

* a string in the form of `TEXT1(TEXT2)`, and then it goes through the transformation `TEXT1` with the argument `TEXT2`;
* empty or the string `default`, and then it returns the State or the Command as a String;
* anything else and it sends back the `<transformationrule>` string itself.

> :warning: The `<transformationrule>` field will be stripped of its single quotes if they are present; this means that in any case, `'TEXT'` is treated the same way as `TEXT`.

## Examples

Here are some examples of valid binding configuration strings:

```
tcp=">[ON:192.168.0.1:3000:'MAP(my.device.map)'], >[OFF:192.168.0.1:3000:'MAP(my.device.map)']" // for a Switch Item where values are converted using the my.device.map
tcp="<[192.168.0.2:3000:'REGEX((.*))']" // for a String Item that captures some state of a remote device that connects to openHAB
tcp=">[192.168.0.2:3000:]" // same as REGEX above but outgoing, it connects to remote port, and sends any commands received
```
Here's a full item configuration
```
String TCP_Lyngdorf	"Lyngdorf TCP"      	(gHifi)	[ "Hifi" ]	  { tcp=">[*:10.0.0.4:7000:]" }
Switch	MyVirtualLyngdorfPowerSwitch	"Lyngdorf TDAI-2170"	(gHifi)	[ "Hifi" ]
```
The TCP Binding will open a port to remote host 10.0.0.4:7000. Any commands received on item will be sent to remote with pre and postfix specified by preamble and postamble. Item will get postUpdate on any data received from remote via the tcp connection. The data can be mapped via rules to virtual items as MyVirtualLyngdorfPowerSwitch

## Currently broken 

Please note that it appears the TCP binding is currently broken and has been so for years. See this github issue https://github.com/openhab/openhab1-addons/issues/2706.

It appears some people have a semi-stable tcp-binding setup but many others do not, see https://community.openhab.org/tags/c/add-ons/bindings/tcp

Also for an alternative solution using MTQQ and a small python server delivering data to MTQQ message broker, see JGluch example here: https://community.openhab.org/t/solved-optoma-beamer-via-rs232-over-tcp-ip-connector/38719/10

## Unclear stuff

Its unclear what many of the parameters does. For instance 'directionssharedconnections'. How can a out-going connection (openhab connects to another server) be shared with an incoming connection (remote server connects to openhab)? Ask on our [community forum][https://community.openhab.org/tags/c/add-ons/bindings/tcp] or read the [source code][https://github.com/openhab/openhab1-addons/tree/master/bundles/binding/org.openhab.binding.tcp].

If binding isn't shared, whill it then try to open two connections to the same port on the same server?
