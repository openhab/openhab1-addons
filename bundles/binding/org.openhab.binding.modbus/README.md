# Modbus Binding

The binding supports both TCP and Serial slaves. RTU, ASCII and BIN variants of Serial Modbus are supported.

The binding can act as 

* Modbus TCP Client (that is, as modbus master), querying data from Modbus TCP servers (that is, modbus slaves).
* Modbus serial master, querying data from modbus serial slaves

The Modbus binding polls the slaves with an configurable poll period. openHAB commands are translated to write requests.

## Table of Contents

<!-- You can generate TOC with https://github.com/ekalinin/github-markdown-toc and running the following command (and stripping out some of the too detailed ones)-->
<!-- cat bundles/binding/org.openhab.binding.modbus/README.md | ./gh-md-toc -  -->
   * [Modbus Binding](#modbus-binding)
      * [Table of Contents](#table-of-contents)
      * [Binding Configuration](#binding-configuration)
         * [Global configuration](#global-configuration)
         * [Configuration parameters specific to each slave](#configuration-parameters-specific-to-each-slave)
         * [Advanced connection parameters](#advanced-connection-parameters)
      * [Item Configuration](#item-configuration)
         * [Simple format](#simple-format)
         * [Extended format](#extended-format)
         * [Item configuration examples](#item-configuration-examples)
      * [Details](#details)
         * [Modbus functions supported](#modbus-functions-supported)
         * [Comment on addressing](#comment-on-addressing)
         * [Many modbus binding slaves for single physical slave](#many-modbus-binding-slaves-for-single-physical-slave)
         * [Read and write functions (modbus slave type)](#read-and-write-functions-modbus-slave-type)
         * [Register interpretation (valuetype) on read &amp; write](#register-interpretation-valuetype-on-read--write)
      * [Config Examples](#config-examples)
      * [Troubleshooting](#troubleshooting)
         * [Enable verbose logging](#enable-verbose-logging)
      * [For developers](#for-developers)
         * [Testing serial implementation](#testing-serial-implementation)
         * [Testing TCP implementation](#testing-tcp-implementation)
         * [Writing data](#writing-data)
         * [Troubleshooting](#troubleshooting-1)



## Binding Configuration

This binding can be configured in the file `services/modbus.cfg`.

### Global configuration

Most of config parameters are related to specific slaves, but some are global and thus affect all slaves.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| poll     | 200     |   No     | **Poll period (optional)**<br/> Frequency of polling Modbus slaves. Note that the value is in milliseconds! For example, `poll=1000` makes the binding poll Modbus slaves once per second. |
| writemultipleregisters | false | No | **Function code to use when writing holding registers (optional)**<br/>Binding can be configured to use FC 16 (*Write Multiple Holding Registers*) over FC 6 (*Write Single Holding Register*) when writing holding register items (see above).  This is optional and default is `false`. For example, `writemultipleregisters=true` makes the binding to use FC16 when writing holding registers. |

### Configuration parameters specific to each slave

The slaves are configured using key value pairs in openHAB config file. Each slave (identified by the slave name) in the config corresponds to a single modbus read request. Note that it might be necessary to define many "slaves" in openHAB configuration to read all data from a single physical modbus slave.

The configuration parameters have the following pattern:

```
<slave-type>.<slave-name>.<slave-parameter-name>=<slave-parameter-value>
```

where:

- `<slave-type>` can be either "tcp" or "serial" depending on the type of this Modbus slave
- `<slave-name>` is unique name per slave you are connecting to. Used in openHAB configuration to refer to the slave.
- `<slave-parameter-name>` identifies the parameter to configure
- `<slave-parameter-value>` is the value of the parameter

Valid slave parameters are

| Property | Required | Description |
|----------|----------|-------------|
| connection | mandatory | Connection string for the slave.<br/><br/>**TCP slaves** use the form `host_ip[:port]` e.g. `192.168.1.55` or `192.168.1.55:511`. If you omit port, default `502` will be used.<br/><br/>For **serial connections** use the form `port:[:baud:[dataBits:[parity:[stopBits:[encoding]]]]]`, e.g. `/dev/ttyS0:38400:8:none:1:rtu` (read from `/dev/ttyS0` using baud rate of 38400, 8 databits, no parity, 1 stopbits, and rtu encoding. Another minimal example is `/dev/ttyS0:38400` where all optional parameters except baud rate will get their default values.<br/><br/>`port` refers to COM port name on Windows and serial device path in *nix. Optionally one can configure one or more of the serial parameters: baud (default `9600`), dataBits (default `8`), parity (default `none`), stopBits (default `1`), encoding (default `ascii`). <br/>Options for the optional serial parameters are as follows: parity={`even`, `odd`}; encoding={`ascii`, `rtu`, `bin`}. |
| id       | optional | slave id, default `1`. Also known as _Address_, _Station address_, or _Unit identifier_, see [Wikipedia](https://en.wikipedia.org/wiki/Modbus) and [simplymodbus](http://www.simplymodbus.ca/index.html) articles for more information |
| type     | mandatory | object type. Dictates the function codes used for read and write. See below for more information. Can be either `coil`, `discrete`, `holding`, or `input`. |
| start    | optional  | address of first coil/discrete input/register to read. Default is 0. The address is directly passed to the corresponding Modbus request, and thus is zero-based. See below for more information on the addressing. |
| length   | mandatory | number of _data items_ to read. _Data items_ here refers to registers, coils or discrete inputs depending on the slave type. For example, if the goal is to read one item with `valuetype=int32`, one needs to read two registers (2 * 16bit = 32bit), thus `length = 2`. If three coils are of interest, one should specify `length = 3` |
| valuetype | optional | tells how interpret the register data. For details, consult [Register interpretation (valuetype) on read & write](#register-interpretation-valuetype-on-read--write). 
| updateunchangeditems | optional | **Since 1.9.0*. `true` or `false`. Controls whether the binding sends an update event on every successful poll (`true`) or only if the state of the item actually changes (`false`).  Default is `false`. When polling many items with high poll frequency, setting this parameter to `true` may cause significant CPU usage. |
| postundefinedonreaderror | optional | **Since 1.9.0**. `true` or `false`. Controls whether the binding sends `Undefined` (`UnDefType.UNDEF`) to the items associated with this slave when a read error occurs. Here read error refers to connection issues (cannot establish connection), I/O error (e.g. uninterrupted connection, unexpected EOF), [modbus protocol exceptions](http://www.simplymodbus.ca/exceptions.htm) (e.g. "Illegal data address"), or response transaction id not matching the request. Note that when `updateunchangeditems` is enabled, the `Undefined` is sent only once on errors, unless the slave recovers from the error. |

### Advanced connection parameters

Since 1.9.0, `connection` parameter can take additional colon (`:`) separated parameters:

- For TCP slave the new format for connection parameter is: `host_ip[:port[:interTransactionDelayMillis[:reconnectAfterMillis[:interConnectDelayMillis[:connectMaxTries[:connectTimeout]]]]]]`. 
- For the serial slaves the new format is: `port[:baud[:dataBits[:parity[:stopBits[:encoding[:interTransactionDelayMillis[:receiveTimeoutMillis[:flowControlIn[:flowControlOut]]]]]]]]`.

Explanation of these new parameters

- `interTransactionDelayMillis`: Time to wait between consecutive modbus transactions (to the same host or serial device), in milliseconds. Each modbus transaction corresponds to read or write operation. Default 35 for serial slaves and 60 for tcp slaves.
- `reconnectAfterMillis`: Time after which connection is disconnected and re-established, in milliseconds. Default 0 (closes connection after every transaction).
- `interConnectDelayMillis`: Time to wait between consecutive connection attempts (to the same host or ip), in milliseconds. Default 0.
- `connectMaxTries`: Maximum tries when establishing connection. Default 3.
- `connectTimeout`: Maximum time to wait for connection establishment, in milliseconds. Default is zero which corresponds to infinite/OS default.
- `receiveTimeoutMillis`: Maximum time to wait for a single "read operation" (sequence of bytes) before giving up, in milliseconds. Default 1500. 
- `flowControlIn`: Flow control for the input data. Default `none`. Valid values: `none`, `xon/xoff in`, `rts/cts in`.
- `flowControlOut`: Flow control for the output data. Default `none`. Valid values: `none`, `xon/xoff out`, `rts/cts out`.

Most important of these is the `interTransactionDelayMillis` which ensures that Modbus RTU (serial) has enough "silent time" between the transactions, as required by the Modbus/RTU protocol. Furthermore it ensures that modbus slave (applies to both tcp and serial slaves) is not spammed with too many transactions, for example some PLC devices might not be able to handle many requests coming in a short time window.

These new parameters have conservative defaults, meaning that they should work for most users. In some cases when extreme performance is required (e.g. poll period below 10ms), one might want to decrease the delay parameters, especially `interTransactionDelayMillis`. With some slower devices on might need to increase the values.

With low baud rates and/or long read requests (that is, large `length` parameter is modbus slave definition), [there might be need to increase `receiveTimeoutMillis` to something larger](https://community.openhab.org/t/connection-pooling-in-modbus-binding/5246/205), e.g. `5000`.

Examples:

- `serial.serialslave1.connection=/dev/ttyS0:38400:8:none:1:rtu:150` connect to serial slave using with `interTransactionDelayMillis` of 150ms
- `tcp.tcpslave1.connection=192.168.1.100:502:0` connect to tcp slave with no `interTransactionDelayMillis`

## Item Configuration

ModbusBindingProvider provides binding for openHAB Items.

The item configuration has two formats: simple and extended.

### Simple format

Simple item configuration format looks like the below:

```
Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:5"}
```

where `slave1` is the name of the slave in openhab configuration, and `5` is the reference to the coil, discrete input, input register or holding register. 

The index `5` is expressed relative to the slave `start` parameter and `valueType` parameter to determine which bits are used when interpreting the value. See also [Comment on addressing](#Comment-on-addressing) and [Register interpretation (valuetype) on read & write](#Register-interpretation-(valuetype)-on-read-&-write) for more details on this.

```
Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:<6:>7"}
```

In this case data is read from "index 6", while data is written to "index 7".

### Extended format

In addition to simple format, a new configuration syntax was introduced in version 1.10.0.

This gives more freedom to the user. Among other things it enables
- specify transformation to use on read/write
- mark item as read-only (received openHAB commands are not processed)
- mark item as write-only (polled data do not change the item state)
- overriding `valueType` per-item basis (allows to read many registers at once and then interpret them using different value types)
- processing only certain commands (on write)
- processing only certain state updates (on read) based on value

The extended format looks like:

(for read)

```ini
<[slaveName:readIndex:trigger=TRIGGER, transformation=TRANSFORMATION, valueType=VALUETYPE]
 ```

(for write)

```ini
>[slaveName:writeIndex:trigger=TRIGGER, transformation=TRANSFORMATION, valueType=VALUETYPE]
```

Read and write entries can be combined, and there can be zero or more read/write entries. All the keyword arguments after index are optional. Defaults are such that they correspond to binding behaviour currently. Multiple read/write definitions can be specified with commas (whitespace allowed as well).

Example of having multiple entries:

```ini
<[slaveName:0], >[slaveName:0], >[slaveName:1]
```

The above item config would read from "index 0", and write to both "index 0" and "index 1".


Similar to simple configuration syntax, place the configuration string in `modbus` parameter of the item. For example,

```ini
Switch MySwitch "My Modbus Switch" (ALL) {modbus="<[slaveName:0], >[slaveName:0], >[slaveName:1]"}
```


Explanation of different keyword arguments
- `trigger`: a string matching command (write definitions) or state (read definitions). If the command/state does not match the trigger value, the command/state is not processed by this write/read definition. 
 - use `*` to process all (overrides slave's `updateunchangeditems`)
 - use `CHANGED` (case-insensitive) with read entries to handle only changed values (similar to slave setting `updateunchangeditems=false`). 
 - use `default` (case-insensitive) to handle all or just the changed values, depending on slave `updateunchangeditems` parameter
 - if omitted, uses `default`.
- `transformation`: transformation to use interpreting the result. 
 - use `default` (case-insensitive) to communicate that no transformation is done and value should be passed as is
 - use `SERVICE(ARG)` to refer to transformation service. 
 - any other value than the above types will be interpreted as static text, in which case the actual content of the command/polled value is ignored. 
 - if omitted, uses `default`.
- `valueType`: valueType to use when interpreting the register
 - use `default` (case-insensitive) to refer to slave definition's valueType
 - if omitted, uses `default`. Allows to poll the data only once with a single request, and then interpret pieces of data with the correct value type.

The transformations can convert to string representing command and state types of the item. You can also always use numbers (that is, `DecimalType`), `ON`/`OFF` and `OPEN`/`CLOSED`. The transformation can be quoted in case of special characters (e.g. `=` or `,`). One can use java-like escaping inside the quotes for complex transformations such as regular expressions (e.g. `\"`).


### Item configuration examples
 

#### Single coil/register per item

```ini
Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:5"}
```

- This binds MySwitch to modbus slave defined as "slave1" in `modbus.cfg` reading/writing to the coil (5 + slave's `start` index). The `5` is called item read index.
- If the slave is read-only, that is the `type` is `input` or `discrete`, the binding ignores any write commands. 
- if the slave1 refers to registers, and after parsing using the registers as rules defined by the `valuetype`, zero value is considered as `OFF`, everything else as `ON`.

#### Separate index for reading and writing

```ini
Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:<6:>7"}
``` 

- In this case index 6 is used for reading while and commands are put to index 7.

#### Index and value types

```
Number MyCounterH "My Counter high [%d]" (All) {modbus="slave3:0"}
```

this reads counter 1 high word when valuetype=`int8` or `uint8`

```
Number MyCounterL "My Counter low [%d]" (All) {modbus="slave3:1"}
```

this reads counter 1 low word when valuetype=`int8` or `uint8`

#### 32bit floating point value numbers

When using a float32 value you must use [%f] in item description.

```
Number MyCounter "My Counter [%f]" (All) {modbus="slave5:0"}`
```

The above reads two registers, starting from index 0 (plus slave offset `start`).

#### Read-only items

```
Number NumberItem "Number [%.1f]" <temperature> {modbus="<[slave1:0]"}
```


#### Write-only items

```
Number NumberItem "Number [%.1f]" <temperature> {modbus=">[slave1:0]"}
```

#### Set coil to 1 on any command

(inspired by [[Modbus] Coil reset only after item update to ON (OFF excepted) #4745](https://github.com/openhab/openhab1-addons/issues/4745))

Reads from index 0, writes to index 1. All writes (no matter what command) are converted to 1.

```
Number NumberItem "Number [%.1f]" <temperature> {modbus="<[slave1:0], >[slave1:1:transformation=ON]"}
```

#### JS transformation (scaling)

This example multiplies the DecimalType commands by 10 when writing to modbus, and divides the values by 10 when reading from modbus.

default.items:

```
Number NumberItem "Number [%.1f]" <temperature> {modbus=">[slave1:0:transformation=JS(multiply10.js)], <[slave1:0:transformation=JS(divide10.js)]"}
```

transform/multiply10.js:

```
// Wrap everything in a function
(function(i) {
    return Math.round(parseFloat(i, 10) * 10);
})(input)
// input variable contains data passed by openhab
```

transform/divide10.js:

```
// Wrap everything in a function
(function(i) {
    return parseFloat(i) / 10;
})(input)
// input variable contains data passed by openhab
```



#### Rollershutter

(inspired by [[modbus] enhance modbus binding for rollershutter items #4654](https://github.com/openhab/openhab1-addons/pull/4654))

This is an example how different Rollershutter commands can be written to MODBUS.

Roller shutter position is read from register 0, UP(=1)/DOWN(=-1) commands are written to register 1, and MOVE(=1)/STOP(=0) commands are written to register 2.

default.items:

```
Rollershutter RollershutterItem "Roller shutter position [%.1f]" <temperature> {modbus="<[slave1:0], >[slave1:1:trigger=UP, transformation=1], >[slave1:1:trigger=DOWN, transformation=-1], >[slave1:2:trigger=MOVE, transformation=1], >[slave1:2:trigger=STOP, transformation=0]"}
```

default.sitemap:

```
sitemap demo label="Main Menu" {
    Switch item=RollershutterItem label="Roller shutter [(%d)]" mappings=[UP="up", STOP="X", DOWN="down"]
}
```



## Details

### Modbus functions supported

#### Supported Modbus object types

Modbus binding allows to connect to multiple Modbus slaves. The binding supports following Modbus *object types*

- coils, also known as *digital out (DO)* (read & write)
- discrete inputs, also known as *digital in (DI)* (read)
- input registers (read)
- holding registers (read & write)

Binding can be configured to interpret values stored in the 16bit registers in different ways, e.g. as signed or unsigned integer.

For more information on these object types, please consult [Modbus wikipedia article](https://en.wikipedia.org/wiki/Modbus).

#### Read and write functions

Modbus specification has different operations for reading and writing different object types. These types of operations are identified by *function code*. Some devices support only certain function codes.

For more background information, please consult [Modbus wikipedia article](https://en.wikipedia.org/wiki/Modbus).

The binding uses following function codes when communicating with the slaves:

- read coils: function code (FC) 1 (*Read Coils*)
- write coil: FC 5  (*Write Single Coil*)
- read discrete inputs: FC 2 (*Read Discrete Inputs*)
- read input registers: FC 4 (*Read Input Registers*)
- read holding registers: FC 3 (*Read Multiple Holding Registers*)
- write holding register: FC 6 (*Write Single Holding Register*), OR  FC 16 (*Write Multiple Holding Registers*) (see note on `writemultipleregisters` configuration parameter below)

### Comment on addressing

[Modbus Wikipedia article](https://en.wikipedia.org/wiki/Modbus#Coil.2C_discrete_input.2C_input_register.2C_holding_register_numbers_and_addresses) summarizes this excellently:

> In the traditional standard, [entity] numbers for those entities start with a digit, followed by a number of four digits in range 1–9,999:

> - coils numbers start with a zero and then span from 00001 to 09999
> - discrete input numbers start with a one and then span from 10001 to 19999
> - input register numbers start with a three and then span from 30001 to 39999
> - holding register numbers start with a four and then span from 40001 to 49999

> This translates into [entity] addresses between 0 and 9,998 in data frames.

The openHAB modbus binding uses data frame entity addresses when referring to modbus entities. That is, the entity address configured in modbus binding is passed to modbus protocol frame as-is. For example, modbus slave definition with `start=3`, `length=2` and `type=holding` will read modbus entities with the following numbers 40004 and 40005.

### Many modbus binding slaves for single physical slave

One needs to configure as many modbus slaves to openHAB as there are corresponding modbus requests. For example, in order to poll status of `coil` and `holding` items from a single [physical] modbus slave, two separate modbus slave definitions need to be configured in the `modbus.cfg`. For example:

```
serial.slave1.connection=/dev/pts/8:38400:8:none:1:rtu
serial.slave1.type=coil
serial.slave1.length=3

serial.slave2.connection=/dev/pts/8:38400:8:none:1:rtu
serial.slave2.type=holding
serial.slave2.length=5
```

Please note that the binding requires that all slaves connecting to the same serial port share the same connection parameters (e.g. baud-rate, parity, ..). In particular are different parameter settings considered bad practice, because they can confuse the instances (slaves) on the modbus.  For additional information, see [this discussion](https://community.openHAB.org/t/connection-pooling-in-modbus-binding/5246/161?u=ssalonen) in the community forums.

Similarly, one must have identical connection parameters for all tcp slaves connecting to same host+port.

### Read and write functions (modbus slave type)

Modbus read functions 

- `type=coil` uses function 1 "Read Coil Status" 
- `type=discrete` uses function 2 "Read Input Status" (readonly inputs)
- `type=holding` uses function 3, "Read Holding Registers"
- `type=input` uses function 4 "Read Input Register" (readonly-registers eG analogue inputs)

Modbus write functions 

- `type=coil` uses function 5 "Write Single Coil"
- `type=holding` uses function 6 "Write Single Register", or function 16 "Write Multiple registers" when `writemultipleregisters` is `true`

See also [simplymodbus.ca](http://www.simplymodbus.ca) and [wikipedia article](https://en.wikipedia.org/wiki/Modbus#Supported_function_codes).


### Register interpretation (valuetype) on read & write

Note that this section applies to register elements only (`holding` or `input` type)

#### Read

When the binding interprets and converts polled input registers (`input`) or holding registers (`holding`) to openHAB items, the process goes like this:

- 1. register(s) are first parsed to a number (see below for the details, exact logic depends on `valuetype`)
- 2a. if the item is Switch or Contact: zero is converted CLOSED / OFF. Other numbers are converted to OPEN / ON.
- 2b. if the item is Number: the value is used as is
- 3. transformation is done to the value, if configured. The transformation output (string) is parsed to state using item's accepted state types (e.g. number, or CLOSED/OPEN).

Polled registers from the Modbus slave are converted to openHAB state. The exact conversion logic depends on `valuetype` as described below.

Note that _first register_ refers to register with address `start` (as defined in the slave definition), _second register_ refers to register with address `start + 1` etc. The _index_ refers to item read index, e.g. item `Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:5"}` has 5 as read index.

`valuetype=bit`:

- a single bit is read from the registers
- indices between 0...15 (inclusive) represent bits of the first register
- indices between 16...31 (inclusive) represent bits of the second register, etc.
- index 0 refers to the least significant bit of the first register
- index 1 refers to the second least significant bit of the first register, etc.

(Note that updating a bit in a holding type register will NOT work as expected across Modbus, the whole register gets rewritten. Best to use a read-only mode, such as Contact item.  Input type registers are by definition read-only.)

`valuetype=int8`:

- a byte (8 bits) from the registers is interpreted as signed integer
- index 0 refers to low byte of the first register, 1 high byte of first register
- index 2 refers to low byte of the second register, 3 high byte of second register, etc.
- it is assumed that each high and low byte is encoded in most significant bit first order

`valuetype=uint8`:

- same as `int8` except values are interpreted as unsigned integers

`valuetype=int16`:

- register with index (counting from zero) is interpreted as 16 bit signed integer.
- it is assumed that each register is encoded in most significant bit first order

`valuetype=uint16`:

- same as `int16` except values are interpreted as unsigned integers

`valuetype=int32`:

- registers (2 index) and ( 2 *index + 1) are interpreted as signed 32bit integer.
- it assumed that the first register contains the most significant 16 bits
- it is assumed that each register is encoded in most significant bit first order

`valuetype=uint32`:

- same as `int32` except values are interpreted as unsigned integers

`valuetype=float32`:

- registers (2 index) and ( 2 *index + 1) are interpreted as signed 32bit floating point number.
- it assumed that the first register contains the most significant 16 bits
- it is assumed that each register is encoded in most significant bit first order

##### Word Swapped valuetypes (New since 1.9.0)

The MODBUS specification defines each 16bit word to be encoded as Big Endian,
but there is no specification on the order of those words within 32bit or larger data types.
The net result is that when you have a master and slave that operate with the same
Endian mode things work fine, but add a device with a different Endian mode and it is
very hard to correct. To resolve this the binding supports a second set of valuetypes
that have the words swapped.

If you get strange values using the `int32`, `uint32` or `float32` valuetypes then just try the `int32_swap`, `uint32_swap` or `float32_swap` valuetype, depending upon what your data type is.

`valuetype=int32_swap`:

- registers (2 index) and ( 2 *index + 1) are interpreted as signed 32bit integer.
- it assumed that the first register contains the least significant 16 bits
- it is assumed that each register is encoded in most significant bit first order (Big Endian)

`valuetype=uint32_swap`:

- same as `int32_swap` except values are interpreted as unsigned integers

`valuetype=float32_swap`:

- registers (2 index) and ( 2 *index + 1) are interpreted as signed 32bit floating point number.
- it assumed that the first register contains the least significant 16 bits
- it is assumed that each register is encoded in most significant bit first order (Big Endian)


##### Extra notes

- `valuetypes` smaller than one register (less than 16 bits) actually read the whole register, and finally extract single bit from the result.

#### Write

When the binding processes openHAB command (e.g. sent by `sendCommand` as explained [here](https://github.com/openhab/openhab1-addons/wiki/Actions)), the process goes as follows

1. it is checked whether the associated item is bound to holding register. If not, command is ignored.
2. command goes through transformation, if configured. No matter what commands the associated item accepts, the transformation can always output number (DecimalType), OPEN/CLOSED (OpenClosedType) and ON/OFF (OnOffType).
3. command is converted to 16bit integer (in [two's complement format](https://www.cs.cornell.edu/~tomf/notes/cps104/twoscomp.html)). See below for details.
4. the 16bits are written to the register with address `start` (as defined in the slave definition)

Conversion rules for converting command to 16bit integer

- UP, ON, OPEN commands that are converter to number 1
- DOWN, OFF, CLOSED commands are converted to number 0 
- Decimal commands are truncated as 32 bit integer (in 2's complement representation), and then the least significant 16 bits of this integer are extracted.
- INCREASE, DECREASE: see below

Other commands are not supported.

**Note: The way Decimal commands are handled currently means that it is probably not useful to try to use Decimal commands with non-16bit `valuetype`s.**

Converting INCREASE and DECREASE commands to numbers is more complicated

(After 1.10.0)

1. Most recently polled state (as it has gone through the read transformations etc.) of this item is acquired
2. add/subtract `1` from the state. If the state is not a number, the whole command is ignored.

(Before 1.10.0)

1. Register matching (`start` + read index) is interpreted as unsigned 16bit integer. Previous polled register value is used
2. add/subtract `1` from the integer

**Note (before 1.10.0): note that INCREASE and DECREASE ignore valuetype when using the previously polled value. Thus, it is not recommended to use INCREASE and DECREASE commands with other than `valuetype=uint16`**

#### Modbus RTU over TCP 

Some devices uses modbus RTU over TCP. This is usually Modbus RTU encapsulation in an ethernet packet. So, those devices does not work with Modbus TCP binding since it is Modbus with a special header. Also Modbus RTU over TCP is not supported by Openhab Modbus Binding. But there is a workaround: you can use a Virtual Serial Port Server, to emulate a COM Port and Bind it with OpenHab unsing Modbus Serial.



## Config Examples

Please take a look at [Samples-Binding-Config page](https://github.com/openhab/openhab1-addons/wiki/Samples-Binding-Config) or examine to the following examples.

- Minimal construction in modbus.cfg for TCP connections will look like:

```
# read 10 coils starting from address 0
tcp.slave1.connection=192.168.1.50
tcp.slave1.length=10
tcp.slave1.type=coil
```
 
- Minimal construction in modbus.cfg for serial connections will look like:

```
# read 10 coils starting from address 0
serial.slave1.connection=/dev/ttyUSB0
tcp.slave1.length=10
tcp.slave1.type=coil
```

- More complex setup could look like

```
# Poll values very 300ms = 0.3 seconds
poll=300

# Connect to modbus slave at 192.168.1.50, port 502
tcp.slave1.connection=192.168.1.50:502
# use slave id 41 in requests
tcp.slave1.id=41
# read 32 coils (digital outputs) starting from address 0
tcp.slave1.start=0
tcp.slave1.length=32
tcp.slave1.type=coil
```

- Another example where coils, discrete inputs (`discrete`) and input registers (`input`) are polled from modbus tcp slave at `192.168.6.180`.

> (original example description:)
> example for an moxa e1214 module in simple io mode
> 6 output switches starting from modbus address 0 and
> 6 inputs from modbus address 10000 (the function 2 implizits the modbus 10000 address range)
> you only read 6 input bits and say start from 0
> the moxa manual ist not right clear in this case 

```ini
poll=300

# Query coils from 192.168.6.180
tcp.slave1.connection=192.168.6.180:502
tcp.slave1.id=1
tcp.slave1.start=0
tcp.slave1.length=6
tcp.slave1.type=coil

# Query discrete inputs from 192.168.6.180
tcp.slave2.connection=192.168.6.180:502
tcp.slave2.id=1
tcp.slave2.start=0
tcp.slave2.length=6
tcp.slave2.type=discrete

# Query input registers from 192.168.6.180
tcp.slave3.connection=192.168.6.180:502
tcp.slave3.id=1
tcp.slave3.start=17
tcp.slave3.length=2
tcp.slave3.type=input

# Query holding registers from 192.168.6.181
# Holding registers matching addresses 33 and 34 are read
tcp.slave4.connection=192.168.6.181:502
tcp.slave4.id=1
tcp.slave4.start=33
tcp.slave4.length=2
tcp.slave4.type=holding

# Query 2 input registers from 192.168.6.181. 
# Interpret the two registers as single 32bit floating point number
tcp.slave5.connection=192.168.6.181:502
tcp.slave5.id=1
tcp.slave5.start=10
tcp.slave5.length=2
tcp.slave5.type=input
tcp.slave5.valuetype=float32
```

Above we used the same modbus gateway with ip 192.168.6.180 multiple times 
on different modbus address ranges and modbus functions.

## Troubleshooting

### Enable verbose logging 

Enable `DEBUG` or `TRACE` (even more verbose) logging for the loggers named:

* `net.wimpi.modbus`
* `org.openhab.binding.modbus`

## For developers

### Testing serial implementation

You can use test serial slaves without any hardware on linux using these steps:

1. Set-up virtual null modem emulator using [tty0tty](https://github.com/freemed/tty0tty)
2. Download [diagslave](http://www.modbusdriver.com/diagslave.html) and start modbus serial slave up using this command: 

```
./diagslave -m rtu -a 1 -b 38400 -d 8 -s 1 -p none -4 10 /dev/pts/7
```

3. Configure openHAB's modbus slave to connect to `/dev/pts/8`: 

```
xxx.connection=/dev/pts/8:38400:8:none:1:rtu
```

4. Modify `start.sh` or `start_debug.sh` to include the unconventional port name by adding the following argument to `java`: 

```
-Dgnu.io.rxtx.SerialPorts=/dev/pts/8
```

Naturally this is not the same thing as the real thing but helps to identify simple issues.

### Testing TCP implementation

1. Download [diagslave](http://www.modbusdriver.com/diagslave.html) and start modbus tcp server (slave) using this command: 

```
./diagslave -m tcp -a 1 -p 55502
```

2. Configure openHAB's modbus slave to connect to `127.0.0.1:55502`: 

```
tcp.slave1.connection=127.0.0.1:55502
```

### Writing data

See this [community post](https://community.openhab.org/t/something-is-rounding-my-float-values-in-sitemap/13704/32?u=ssalonen) explaining how `pollmb` and `diagslave` can be used to debug modbus communication.

### Troubleshooting

To troubleshoot, you might be asked to update to latest development version. You can find the "snapshot" or development version from [Cloudbees CI](https://openhab.ci.cloudbees.com/job/openHAB1-Addons/lastSuccessfulBuild/artifact/bundles/binding/org.openhab.binding.modbus/target/).

With modbus binding before 1.9.0, it strongly recommended to try out with the latest development version since many bugs were fixed in 1.9.0. Furthermore, error logging is enhanced in this new version.

If the problem persists in the new version, it is recommended to try to isolate to issue using minimal configuration. Easiest would be to have a fresh openHAB installation, and configure it minimally (if possible, single modbus slave in `modbus.cfg`, single item, no rules etc.). This helps the developers and community to debug the issue.

Problems can be communicated via [community.openhab.org](https://community.openhab.org). Please use the search function to find out existing reports of the same issue. 

It helps greatly to document the issue in detail (especially how to reproduce the issue), and attach the related [verbose logs](#enable-verbose-debug-logging). Try to keep interaction minimal during this test; for example, if the problem occurs with modbus read alone, do not touch the the switch items in openHAB sitemap (would trigger write). 

For attaching the logs to a community post, the [pastebin.com](http://pastebin.com/) service is strongly recommended to keep the thread readable. It is useful to store the logs from openHAB startup, and let it run for a while.

Remember to attach configuration lines from modbus.cfg, item definitions related to modbus binding, and any relevant rules (if any). You can use [pastebin.com](http://pastebin.com/) for this purpose as well.

To summarize, here are the recommended steps in case of errors

1. Update to latest development version; especially if you are using modbus binding version before 1.9.0
2. isolate the issue using minimal configuration, and enable verbose logging (see above)
3. record logs and configuration to [pastebin.com](http://pastebin.com/).  
