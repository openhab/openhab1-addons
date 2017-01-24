_**Note:** This Binding is available in 1.8 and later releases._

## Table of Contents

* [Introduction](#introduction)
* [Binding Configuration](#binding-configuration)
* [Item configuration](#item-configuration)
* [Known Working Hardware](#known-working-hardware)

## Introduction

[Chamberlain MyQ](http://www.chamberlain.com/smartphone-control-products/myq-smartphone-control) system allows you to connect your garage door to the internet to be controlled from anywhere using your smartphone. Using this API, The Chamberlain MyQ Binding can get the status of your garage door opener and send commands to open or close it.


## Binding Configuration

The Chamberlain MyQ Binding only requires your Chamberlain MyQ Username and Password

Edit the file `openhab.cfg` located in `${openhab_home}/configurations/`.  Paste your username and password into your `openhab.cfg` file like so:

    ############################## Chamberlain MyQ Binding ##############################
    #
    # Data refresh interval in ms (optional, defaults to 60000)
    # myq:refresh=60000

    # Timeout for HTTP requests (optional, defaults to 5000)
    # myq:timeout=5000

    # Chamberlain MyQ Username and Password
    myq:username=email@website.com
    myq:password=password123

    # Application ID for the MyQ API (optional, only recommended if existing id ceases to work)
    # myq:appId=JVM/G9Nwih5BwKgNCjLxiFUQxQijAebyyg8QUHr7JOrP+tuPb8iHfRHKwTmDzHOu

Optional settings refresh interval, HTTP request timeout, and Application ID may also be specified. Refresh interval can be changed via the `refresh` parameter, and defaults to a polling rate of one call per every 60000ms (one minute). HTTP request timeout ('timeout') defaults to 5000ms or 5 seconds. Application ID ('appId') is used to access the the MyQ API. This should not every be changed unless the current id stops working. Changing the id overrides the included default id and could prevent new versions of the binding from working in the future. It is recommended you do not change the id unless you understand how the binding internal logic works and how/what could stop working. 


## Item configuration

Three Item Types are supported with this binding. Contact type will display if you garage door is open or closed. String type will display the state from the MYQ API "Open|Closed|Opening|Closing|Partially Open/Closed". Switch type will will  "ON" state for open or partially open/closed  and "OFF" state for closed. Turning the switch ON will open the door and turning the switch OFF will close the door.

items example:
```
Switch GarageDoorSwitch         "Garage Door Open"           {myq="0"}
Contact GarageDoorContact       "Garage Door [%s]" <contact> {myq="0"}
String GarageDoorString         "Garage Door [%s]"           {myq="0"}
Rollershutter GarageDoorShutter "Garage Door Open"           {myq="0"}
```

sitemap example:
```
Switch item=GarageDoorSwitch
Text item=GarageDoorString
Text item=GarageDoorContact
Switch item=GarageDoorShutter
```

The device index is needed to config the items. For people with only one garage door opener the value needs to be set to "0". If you have multiple garage door openers, the device index is based on the order the garage door opener's were added to the MyQ App. 

```
2015-11-08 00:09:04.667 [INFO ] [.b.myq.internal.GarageDoorData] - Chamberlain MyQ Devices:
2015-11-08 00:09:04.669 [INFO ] [.b.myq.internal.GarageDoorData] - DeviceID: 1810905 DeviceName: CG0810243E7F DeviceType: GarageDoorOpener Doorstate : 2
```

## Known Working Hardware

<table>
  <tr><td>Model: HD950WF</td><td>Chamberlain 1.25 hps Wi-Fi Garage Door Opener</td></tr>
  <tr><td>Model: WD850KEVG</td><td>Chamberlain 1 hps Garage Door Opener with MyQ Internet Gateway</td></tr>
  <tr><td>Model: WD832KEV</td><td>Chamberlain 1/2 hps MyQ Garage Door Opener</td></tr>
</table>