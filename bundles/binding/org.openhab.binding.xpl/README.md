# xPL Binding

The xPL binding grabs xPL message values in items and sends xPL commands.

## Prerequisites

The xPL Bundle relies on Gerry Duprey's [xPL4Java](http://www.xpl4java.org/) Library. The library is packaged in the bundle, then you won't need to install it - but in order to make it work, you'll need a working xPL Hub on the openHAB server (you will find all needed information on the [xPL Project](http://xplproject.org.uk/) website.)

## Binding Configuration

This binding can be configured in the file `services/xpl.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| instance |         |   Yes    | some machine name |


## Item Configuration

Item types can be:

* Switch
* Number
* String

They allow to receive/send commands (#COMMAND parameter) and to grab current values in xPL messages (#CURRENT parameter).

```
Switch testswitch "testswitch"  {xpl="*,command,ac.basic,device=0x0a2b,unit=0,command=#COMMAND"}
//Will broadcast (* address| vendor-device.instance) an xPL message in response of sendCommand(testxplswitch, [ON | OFF]) 

Number testxpltemp "testxpltemp" {xpl="*,status,sensor.basic,device=0x0a2x,type=temp,current=#CURRENT"}
//Will get "current" value in the body for the message matching xpl-stat/sensor.basic/device=0x0a2x,type=temp
    
String testxplstr "testxplstr" {xpl="*,status,sensor.basic,device=outside,type=temp,current=#CURRENT"}
//Will get "current" value in the body for the message matching xpl-stat/sensor.basic/device=0x0a2x,type=temp
```

xPL messages can be caught multiple times in multiple items (grabbing different parts of the message).

```
String LivingRoom_Player_Kind "Xmbc [%s]" (LivingRoom)   {xpl="*,status,media.mptrnspt,mp=xbmc,kind=#COMMAND"}
String LivingRoom_Player_Status "Xmbc [%s]" (LivingRoom) {xpl="*,status,media.mptrnspt,mp=xbmc,command=#COMMAND"}
```
