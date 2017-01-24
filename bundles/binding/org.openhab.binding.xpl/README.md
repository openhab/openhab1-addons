**Note:** This Binding is available beginning in the 1.6 release.

## Introduction

The xPL Bundle provides two packages :
- Action to send messages over the xPL network
- Bindings to grab xPL message values in items and send xPL commands

## Pre-requisites

The xPL Bundle relies on Gerry Duprey's [xPL4Java](http://www.xpl4java.org/) Library. The library is packaged in the bundle, then you won't need to install it - but in order to make it work, you'll need a working xPL Hub on the openHAB server (you will find all needed information on the [xPL Project](http://xplproject.org.uk/) website.)

## Configuration in openhab.cfg
```
############################## xPL Binding ##############################
#
# The instance name of the xPL server
xpl:instance=somemachinename
```

## xPL Action

This add-on provides xPL message sending on the network:
- `sendxPLMessage(String target, String msgType, String msgClass, String bodyLine1, String bodyLine2 ...)` : Sends a message over the xPL network. 

Parameters shall follow xPL message elements syntax.
* target : string spelled vendor-device.instance
* msgType : being one of : command, trigger, status
* msgClass : string spelled class.type
* bodyLineX : string containing key and value in the form of "bodykey1=bodyvalue1"
There may be as many bodyLine parameters as needed

## xPL Bindings

Bindings to items can be SwitchItem,NumberItem,StringItem. 
They allow to receive/send commands (#COMMAND parameter) and to grab current values in xPL messages (#CURRENT parameter).

```
Switch testswitch "testswitch"  {xpl="*,command,ac.basic,device=0x0a2b,unit=0,command=#COMMAND"}
//Will broadcast (* address| vendor-device.instance) an xPL message in response of sendCommand(testxplswitch, [ON | OFF]) 

Number testxpltemp "testxpltemp" {xpl="*,status,sensor.basic,device=0x0a2x,type=temp,current=#CURRENT"}
//Will get "current" value in the body for the message matching xpl-stat/sensor.basic/device=0x0a2x,type=temp
    
String testxplstr "testxplstr" {xpl="*,status,sensor.basic,device=outside,type=temp,current=#CURRENT"}
//Will get "current" value in the body for the message matching xpl-stat/sensor.basic/device=0x0a2x,type=temp
```

xPL messages can be catched multiple times in multiple items (grabbing different parts of the message).
```
String LivingRoom_Player_Kind "Xmbc [%s]" (LivingRoom)   {xpl="*,status,media.mptrnspt,mp=xbmc,kind=#COMMAND"}
String LivingRoom_Player_Status "Xmbc [%s]" (LivingRoom) {xpl="*,status,media.mptrnspt,mp=xbmc,command=#COMMAND"}
```
