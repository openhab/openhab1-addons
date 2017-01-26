# IPX800 Binding

IPX800 is a 8 relay webserver from gce-electronics with a lot of possibilities:

* 8 Digital Input
* 8 Relay (250v/ 10A / channel)
* 4 Analog Input
* 8 Counters
* Ability to cascade up to 3 extensions for a total of 32 inputs / 32 relay

Each IPX800 connected to openHAB must be configured with the setting 'Send data on status changed' on the website in M2M > TCP client.

To make it simple, IPX800 is a simple device that drive output and retrieve input. On input we generally connect push button (for instance house switchs), on ouputs we can connect light bulbs for instance.

Features of the binding:

 * Multi ipx support
 * Direct TCP connection
 * Extensions support with alias name
 * Auto reconnect
 * Input to output redirection (even to different ipx)
 * Counter with custom average mode
 * Simple/double clic
 * Virtual dimmer
 * Pulse mode support

## Binding Configuration

The binding can be configured in the file `services/ipx800.cfg`.

| Property  | Default | Required | Description |
|-----------|---------|:--------:|-------------|
| `<name>`.host | | Yes | IP address or hostname |
| `<name>`.port | 9870 | No | TCP client connection port |
| `<name>.<extension>.<address>` | | No | Needed to declare extensions and give them aliases.  `<extension>` can be `x880` or `x400`.  `<address>` is a sequential number starting at `1`.  Assign this property to alias you want to use. |

## Item Configuration

### Syntax

ipx800 items are described as below (italic items are optionnal)

```
ipx800="name:port:*options>to\_name:to\_port*"
```

| name | ipx name or extension alias as defined in the configuration |
| port | ipx port name as Tnn, with T port type (O : ouput, I : input, C : Counter, A : Analog) and nn port number |
| options | depending on items |

* `>*to\_name:*to\_port` : redirection option, is used to drive directly one output using an input. to\_name is the optional name of the ipx800 to send to command. to\_port is the port to send the command to (if no to_name, the command will be send to the same ipx)

### Item Types

#### Output

```
Switch Output { ipx800="myipx:O01" }
Switch Output { ipx800="myipx:O01:p" }
```

Drive output directly from a openhab item. Option p put this ouput in pulse mode (sending SetNxxp at ipx800)

#### Mirror

```
Switch InputMirror { ipx800="myipx:I08:m" }
```

State of this item will follow the input state.

#### Normal astable switch

```
Switch InputNormal { ipx800="myipx:I08" }
```

On each rising edge of the input, item state will change.

#### Simple Clic

```
Switch InputSimpleClic { ipx800="myipx:I08:d", milight="m1;3" }
```

When coupled with a double click, after a single rising/falling edge, will wait the double clic timeout before changing item state.

#### Double click

```
Switch InputDoubleClic2 { ipx800="myipx:I08:D>myipx2:O03" }
```

Change item state after a double clic on the input.

#### Virtual dimmer

```
Switch InputDimmer { ipx800="ipx1:I02\:v\:\<step\>" }
```

A long press will raise the value of this item each 500ms by <step>.

Once item value reaches 100, it will stick to this value. A new long press will restart the dimmer to 0.

#### Simple Counter

```
Number SimpleCounter {ipx800="myipx:C01"}
```

Will reflect ipx800 counter value.

#### Average counter

```
Number AverageCounter {ipx800="myipx\:C01\: a\:1\:m"}
```

Will compute the average based on the counter. This is very useful to use in conjunction to pulse based counter (water/gaz/electrical counter). 

8 different counter could be connected direclty to ipx800.

With this kind of counter, all the power will be monitored easily (liters per minute, Kw...)

This item will publish its state at least each period.

Options : \<step\>\:\<period\>

| Step | unit of each counter increment (as defined by hardware counter) |
| Period | Base period to compute the average |


#### To be done

* Long press


### Example

```
Switch Output { ipx800="myipx:O01" }
Switch InputNormal { ipx800="myipx:I08" }
Switch InputSimpleClic { ipx800="myipx:I08:d>O02" }
Switch InputSimpleClic { ipx800="myipx:I08:d", milight="m1;3" }
Switch InputDoubleClic { ipx800="myipx:I08:D", milight="m1;9" }
Switch InputDoubleClic2 { ipx800="myipx:I08:D>myipx2:O03" }
Switch InputDoubleClic3 { ipx800="myipx:I08:v>O04" }
Switch Output2 { ipx800="myipx:O02" }
Switch Output3 { ipx800="myipx:O03" }
Switch Output9 { ipx800="myipx2:O01" }

Switch InputToOuput {ipx800="myipx:I08>O01"}

Switch Mirror {ipx800="myipx:I06:m>O06" }

Number PowerSimple {ipx800="myipx:C01"}
Number PowerAverage {ipx800="myipx:C01:a:1:m"}
```

## Architecture & developpment choices

The goal of this binding is to connect openHAB to ipx800 while provinding extra functionnality to like double click (double change of input state will change the state of an openhab item), long press (long change of input), average power counter (count the average of pulse on a specified input)...

To do this two things are needed :

* Keep a cache of ipx800 device state -> Because the change of an ipx800 input doesn't directly change the state of an openhab item state.

* Keep the bindingProvider configured with openhab item configuration -> When you change an ipx800 input, the provider need to know what kind of item is configured on openhab

Ex:

A double click item is configured on an input.

A change occur on this input, the provider will just change its internal state to wait for the next clic
On the second change, the provider will change the state of the openhab item

In the current architecture, for each openhab item setup in conf file (Ex : Switch InputDoubleClic { ipx800="ipx1:I02:D"}), an ipx800Item is configured in the binding (in this case : Ipx800DoubleClic).
To be able to handle multiple items on the same ipx800 input port, i add the handler layer, so each item is linked to these handlers. This layer can for instance handle SimpleClic, DoubleClic and virtual dimmer connected on the same ipx800 input port (Ipx800HandlerMulti).

These features (double item state change, virtual dimmer,...) could have been implemented as rules, but it's easier to only configure an item.
