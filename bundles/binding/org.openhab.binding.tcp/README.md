Documentation of the TCP & UDP binding Bundle

## Introduction

The TCPBinding provides basic support for TCP based ASCII protocols. It sends and receives 
data as ASCII strings. Data sent out is by default padded with a CR/LF (This behavior can be changed by the
`tcp:postamble=` configuration option). This should be sufficient for many
home automation devices that take simple ASCII based control commands, or that send back
text based status messages.

The TCP part of the binding has a built-in mechanism to keep connections to remote hosts alive, and will reset connections at regular intervals to overcome the limitation of "stalled" connections or remote hosts.

The TCP & UDP binding bundle acts as a network client or as a network server.

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a remote host:port, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the TCP & UDP binding configuration string is explained here:

**Note:** The examples here below are for the TCP protocol. UDP binding configuration are exactly the same, one only has to substitute `"tcp="` with `"udp="`

The format of the binding configuration is simple and looks like this:

    tcp="<direction>[<command>:<ip address>:<port>:<transformationrule>], <direction>[<command>:<ip address>:<port>:<transformationrule>], ..."

where `<direction>` is one of the following values:
- < - for inbound-triggered communication, whereby the openHAB runtime will act as a server and listen for incoming connections from the specified `<ip address>:<port>`
- > - for outbound-triggered communication, whereby the openHAB runtime will act as a client and establish an outbound connection to the specified `<ip address>:<port>`

where `<command>` is the openHAB command. For String Items `<command>`: can be omitted  

where `<ip address>` is the hostname or ip address in dotted notation of the remote host

and where `<transformationrule>` can be one of:

* a string in the form of `TEXT1(TEXT2)`, and then it goes through the transformation `TEXT1` with the argument `TEXT2`;
* **(1.9)** empty or the string `default`, and then it returns the State or the Command as a String;
* **(1.9)** anything else and it sends back the `<transformationrule>` string itself.

> :warning: The `<transformationrule>` field will be stripped of its single quotes if they are present; this means that in any case, `'TEXT'` is treated the same way as `TEXT`.

##Configuration Parameters

Note: This is optional for the configuration and not necessary for receiving data. Item-defintions are enough for receiving data. (Developer confirm? 20150128). There's a bug in the binding that requires at least one udp configuration to be defined or the binding will not send UDP messages.

The TCP and UDP bindings are highly configurable through the openhab.cfg . 
The following parameters can be set (substitute `"tcp:"` with `"udp:"` for the UDP binding):
The indicated values are the default values used by either binding

`tcp:refreshinterval=250` - This is a mandatory field in order to start up the binding - Refresh interval for the polling thread, can be used to manage oh Host CPU Load

`tcp:port=25001` - This is a mandatory field when inbound communication (`<direction>` equals <) are used - Port to listen on for incoming connections. 

`tcp:addressmask=true` - Allow masks in ip:port addressing, e.g. 192.168.0.1:`**` etc

`tcp:reconnectcron=0 0 0 ** * ?` - Cron-like string to reconnect remote ends, e.g for unstable connection or remote ends

`tcp:retryinterval=5` - Interval between reconnection attempts when recovering from a communication error, in seconds

`tcp:queue=true` - Queue data whilst recovering from a connection problem (TCP only)

`tcp:buffersize=1024` - Maximum buffer size whilst reading incoming data

`tcp:preamble=` - Pre-amble that will be put in front of data being sent

`tcp:postamble=\r\n` - Post-amble that will be appended to data being sent

`tcp:blocking=false` - Perform all write/read (send/receive) operations in a blocking mode, e.g. the binding will wait for a reply from the remote end after data has been sent

`tcp:timeout=3000` - Timeout, in milliseconds, to wait for a reply when initiating a blocking write/read operation

`tcp:updatewithresponse=true` - Update the status of Items using the response received from the remote end (if the remote end sends replies to commands)

The parameters here below influence the way the binding will multiplex data over TCP and UDP connections

`tcp:itemsharedconnections=true` - Share connections within the Item binding configurations

`tcp:bindingsharedconnections=true` - Share connections between Item binding configurations

`tcp:directionssharedconnections=false` - Share connections between inbound and outbound connections

##Examples

Here are some examples of valid binding configuration strings:

    tcp=">[ON:192.168.0.1:3000:'MAP(my.device.map)')], >[OFF:192.168.0.1:3000:'MAP(my.device.map)']" // for a Switch Item where values are converted using the my.device.map
    tcp="<[192.168.0.2:3000:'REGEX((.*))']" // for a String Item that captures some state of a remote device that connects to openHAB

##Working item and .sitemap

Here should come working item and a working sitemap definition,
and also some netcat commands to check and prove function.
And how to receive and map via TCP/UDP-port and store to another Binding (i.e. KNX GAs).
(by the dev?). (20150128)