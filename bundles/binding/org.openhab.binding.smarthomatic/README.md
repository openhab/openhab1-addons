# Smarthomatic Binding

The [Smarthomatic](https://www.smarthomatic.org) binding will connect a network of Smarthomatic devices to your openHAB server.

Currently only incoming messages of the Smarthomatic env sensor are tested.

## Binding Configuration

This binding can be configured in the file `services/smarthomatic.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| serialPort |       |    Yes   | For example, `/dev/ttyUSB0`. |
| baud     |         |    Yes   | For example, `19200`. |

## Item Configuration

The format looks like this:

```    
smarthomatic="<direction>[<deviceId>, <messageGroupId>, <messageId>, <messagePart>:<transformationrule>]"
```

where:

* `<direction>` is one of the following values:

 * `<` - for inbound communication, where the binding will listen for incoming messages from the Smarthomatic base station 
 * `>` - for outbound communication, where the binding will send messages to the Smarthomatic base station
 * `=` - for two-way communication, where the binding both will send and receive messages. 

* `<deviceId>` is the senderId or the receiverId, depending on the direction of the message.

* `<messageGroupId>` is the messageGroupId of the message 

* `<messageId>` is the messageId of the message

* `<messagePart>` is the optional part that determines the part of a massageData part. For messages that contain multiple parts, for example HumidityTemperature or messageData that contains arrays. 

* `<transformationrule>` is the optional part, given if the message needs to be transformed. For example, if we need to divide by 100. It uses the transformation services that you have installed in openHAB 2.
 
## Examples

items/smarthomaticdemo.items

```
Number luftfeuchtigkeit {smarthomatic="<[deviceId=10, messageGroupId=10, messageId=2, messagePart=0:JS(getDiv10.js)]"}
Number temperatur {smarthomatic="<[deviceId=10, messageGroupId=10, messageId=2, messagePart=1:JS(getDiv100.js)]"}
Number helligkeit {smarthomatic="<[deviceId=10, messageGroupId=11, messageId=1]"}`
Color dimmer {smarthomatic=">[deviceId=120, messageGroupId=60, messageId=10]"}
Number dimmer {smarthomatic="=[deviceId=140, messageGroupId=60, messageId=1]"}
Switch digitalSwitch {smarthomatic="<[deviceId=150, messageGroupId=1, messageId=1, messagePart=1]"}
```

transform/getDiv10.js

```javascript
(function(i) {
    return Number(i) / 10;
})(input)
```

## Supported Messages

### Generic

* BatteryStatus

### GPIO

* DigitalPort

### Environment Sensor

* Temperature tested
* HumidityTemperature tested
* Humidity tested
* Brightness tested

### Dimmer

* Brightness

### RGB Dimmer#

* Color tested
