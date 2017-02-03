# Picnet Sapp Binding

The openHAB Sapp binding connects to [Picnet](http://www.sinthesi.com) Home Automation installations. The binding supports multi master configuration as well.

The Sapp binding polls the bus in an configurable interval and support sapp over ethernet only,  so if you use an old master series without ethernet connection you need to use a Moxa 5110 in order to use it.

## Binding Configuration

This binding can be configured with the file `services/sapp.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 100     |    No    | refresh interval to Picnet polling service. Value is in milliseconds. |
| pnmas.ids=home,office | | Yes | Examples: `home,office` |
| pnmas.`<id>`.ip |  |    Yes   | Only the pnmas listed in `pnmas.ids` are used |
| pnmas.`<id>`.port | |   Yes   | Only the pnmas listed in `pnmas.ids` are used. Example: `7001` |

where:

* `<id>` is a name you choose for an instance to control. You can specify multiple values for `<id>`.ip and `<id>`.port.


### Example sapp.cfg

```
refresh=100
pnmas.ids=home,work
pnmas.home.ip=192.168.2.97
pnmas.home.port=7001
pnmas.work.ip=192.168.2.98
pnmas.work.port=4001
```

## Item Configuration

The Picnet Master supports Input Output and Virtual address types.

In the binding we use:

***pnmasid status***

It is the `pnmas.ids` name defined in binding configuration.

***Status address type***

These values are accepted in order to get the status

* **`I`**: Input (valid numbers are from 1 to 250). This is the module address
* **`O`**: Output (valid numbers are from 1 to 250). This is the module address
* **`V`**: Virtual (valid numbers are from 1 to 2500). This is the virtual number

***Status address***

It is a number and its value depends from the Status address type as described.

***Status subaddress***

These values are accepted in order to get the status

* **`*`**: The word value 
* **`H`**: The high byte value
* **`L`**: The low byte value
* **`1-16`**: The single bit value
* **`+`**: Signed word, -32768 to 32767 
* **`H+`**: Signed high byte, -128 to 127
* **`L+`**: Signed low byte, -128 to 127

***Control address type***

This values are accepted in order to control the status. Only Virtual is accepted.

* **`V`**: Virtual valid numbers are from 1 to 2500. This is the virtual number

***Control address***

It is a number and the value depends from the Status address type as described.

***Control subaddress***

These values are accepted in order to control the status

* **`*`**: The word value
* **`H`**: The high byte value
* **`L`**: The low byte value
* **`1-16`**: The single bit value
* **`+`**: Signed word, -32768 to 32767 
* **`H+`**: Signed high byte, -128 to 127
* **`L+`**: Signed low byte, -128 to 127

The sapp binding support the following items and their use is listed here:

### Switch

Switch item syntax:

```
<pnmasid status>:<status address type, I/O/V>:<status address,1-250/1-250/1-2500>:<status subaddress, */H/L/1-16>:<on value>/<pnmasid control>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<on value>:<off value>
```

#### Switch Example

In this example we control the status of module 60 Output 1 and we control it using Virtual 2001 bit 1 and we send 1 on ON and 1 on OFF command.
The Virtual 2001 is autoreset type on master program.

```
Switch LightDinner  "Dinner Light" (gLight) { sapp="home:O:60:1/home:V:2001:1:1:1" }
```

***How to start and stop polling using a switch item***

There is a special switch that can stop and start polling in order to write the Master without stopping Openhab sw.

`Switch PollerSwitch "PollerSwitch" { sapp="P" }`

### Contact

Contact item syntax:

```
// <pnmasid status>:<status address type, I/O/V>:<status address, 1-250/1-250/1-2500>:<status subaddress, */H/L/1-16>:<open value>
```

#### Contact Example

In this example we control the status of module 12 Input 7.

```
Contact ContactWindowsBath "Bath Window" { sapp="home:I:12:7" }
```

The contact status is by default inverted, so NC contact for is OPEN and NO is CLOSED. To invert the status just add :0 at the end of the string just like the example:

```
Contact ContactWindowsBath "Bath Window" { sapp="home:I:12:7:0" }
```
  
### Number

Number item syntax:

```
<pnmasid status>:<status address type, I/O/V>:<status address, 1-250/1-250/1-2500>:<status subaddress, */H/L/1-16>
```

#### Number Examples

Number item come with scale system included. For example in order to scale a word value /10 you can use :0:6553 like example. Default scale depends on address type (0-65535 for word, 0-255 for L/H, 0-1 for bit).

```
Number SappNumber1		"Sapp Number * : Value [%.1f]" { sapp="home:V:200:*:1:1000" }
Number SappNumber2		"Sapp Number H : Value [%.1f]" { sapp="home:V:200:H:0:2500" }
Number SappNumber3		"Sapp Number L : Value [%.1f]" { sapp="home:V:200:L" }
Number SappNumber4		"Sapp Number 1 : Value [%.1f]" { sapp="home:V:200:1" }
Number SappNumber5		"Sapp Number 2 : Value [%.1f]" { sapp="home:V:200:2" }
Number SappNumberX1		"Sapp Number Setpoint : Value [%.1f]" { sapp="home:V:230:*:0:6553" }
Number SappNumberX2		"Sapp Number Setpoint : Value [%.1f]" { sapp="home:V:230:*" }
```

### Rollershutter

Rollershutter item syntax:

```
<pnmasid status>:<status address type, only V>:<status address, 1-2500>:<status subaddress, */H/L/1-16>:<up value>:<down value>/<pnmasid up command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<up value>/<pnmasid down command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<down value>/<pnmasid stop command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<stop value>
```

#### Rollershutter Example

In this example we control the status of Virtual 154 from 0 to 100 for percent status. Virtual 155 bit 1 is used for up command, Virtual 155 bit 2 is used for stop command, Virtual 155 bit 3 is used for down command.

```
Rollershutter BlindBath	 	"Bath Blind" { sapp="home:V:154:*:0:100/home:V:155:1:1/home:V:155:2:1/home:V:155:3:1" }
```
### Dimmer

Dimmer item syntax:

```
<pnmasid status>:<status address type, only V>:<status address, 1-2500>:<status subaddress, */H/L/1-16>:<increment>
```

#### Dimmer Examples

Dimmer item come with scale system included; use L or H in order to scale from 0 to 255 and * in order to scale from 0 to 65535. Here Virtual 25 has value from 0 to 255 with step to 10 

```
Dimmer dimmer1 "dimmer [%d %%]" (gSapp1)  { sapp="home:V:25:L:10" }
```

If you want to use dimmer in sitemap you can use this definitions:

```
Switch item=dimmer1 mappings=[INCREASE="+",DECREASE="-", ON="ON", OFF="OFF"]
Switch item=dimmer1 mappings=[0="OFF", 25="25", 50="50", 75="75", 100="100"]
Slider item=dimmer1 sendFrequency=100 switchSupport
```

## Implementation Notes

The default openHAB code formatter has been modified in this binding just in the "maximum line width" parameter, allowing longer lines then the standard openHAB.
This is because the code would otherwise be quite unreadable being split over too many lines.
