# Smarthomatic Binding #

Available from Version 1.9

The Smarthomatic binding will connect a network of Smarthomatic devices to the openhab server. Information's about Smarthomatic devices can be found here: [Smarthomatic Project](https://www.smarthomatic.org)

Currently only incoming messages of the Smarthomatic env sensor are tested.

Configuration of openhab.cfg

    smarthomatic:serialPort=/dev/ttyUSB0
    smarthomatic:baud=19200

In the config file openhab.cfg there need to be line on which COM port the Smarthomatic Basestation is sending it's messages.

Item Configuration:

Item configuration basically looks like that:
    
    itemtype itemname ["labeltext"] [<iconname>] [(group1, group2, ...)] [{bindingconfig}]
The format of the binding configuration looks like this:
    
    smarthomatic="<direction>[<deviceId>, <messageGroupId>, <messageId>, <messagePart>:<transformationrule>]"

where `<direction>` is one of the following values:
- < - for inbound communication, whereby the openHAB runtime will listen for incoming messaged from the smarthomatic basestation 
- > - for outbound communication, whereby the openHAB runtime will send messages to the smarthomatic basestation
- = - for two way communication where the openhab will send and receive messages. 

where `<deviceId>` is the senderId or the receiverId depending on the direction of the message.

where `<messageGroupId>` is the messageGroupId of the message 

where `<messageId>` is the messageId of the message

where `<messagePart>` is the optional part that determines the part of a massageData part. For messages that contain multiple parts for example HumidityTemperature or messageData that contains arrays. 

where `<transformationrule>` is the optional part if the message needs to be transformed. For example if we need a divion by 100. It uses the inbuild transformation services.
 
Example item definition:

    Number luftfeuchtigkeit {smarthomatic="<[deviceId=10, messageGroupId=10, messageId=2, messagePart=0:JS(getDiv10.js)]"}
    Number temperatur {smarthomatic="<[deviceId=10, messageGroupId=10, messageId=2, messagePart=1:JS(getDiv100.js)]"}
    Number helligkeit {smarthomatic="<[deviceId=10, messageGroupId=11, messageId=1]"}`
    Color dimmer {smarthomatic=">[deviceId=120, messageGroupId=60, messageId=10]"}
    Number dimmer {smarthomatic="=[deviceId=140, messageGroupId=60, messageId=1]"}
    Switch digitalSwitch {smarthomatic="<[deviceId=150, messageGroupId=1, messageId=1, messagePart=1]"}


Example transformation function:

    (function(i) {
        return Number(i) / 10;
    })(input)

## Supported Messages of the different devices ##

##### Generic #####
* BatteryStatus

##### GPIO #####
* DigitalPort

##### Environment Sensor#####
* Temperature tested
* HumidityTemperature tested
* Humidity tested
* Brightness tested

##### Dimmer #####
* Brightness

##### RGB Dimmer#####
* Color tested