# Chamberlain MyQ Binding

[Chamberlain MyQ](http://www.chamberlain.com/smartphone-control-products/myq-smartphone-control) system allows you to connect your garage door to the internet to be controlled from anywhere using your smartphone. Using this API, The Chamberlain MyQ Binding can get the status of your garage door opener and send commands to open or close it.

## Binding Configuration

This binding can be configured in the file `services/myq.cfg`.

The Chamberlain MyQ Binding only requires your Chamberlain MyQ Username and Password.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| username |         |   Yes    | Chamberlain MyQ Username |
| password |         |   Yes    | Chamberlain MyQ Password |
| refresh  | 60000   |   No     | Data refresh interval in milliseconds (60000 is one minute) |
| timeout  | 5000    |   No     | Timeout for HTTP requests in milliseconds |
| appId    | see description | No | Application ID for the MyQ API (only recommended if existing id ceases to work). It is recommended you do not supply your own appId unless you understand how the binding internal logic works and how/what could stop working. | 

## Item configuration

Three Item Types are supported with this binding. Contact type will display if you garage door is open or closed. String type will display the state from the MYQ API "Open|Closed|Opening|Closing|Partially Open/Closed". Switch type will will  "ON" state for open or partially open/closed  and "OFF" state for closed. Turning the switch ON will open the door and turning the switch OFF will close the door.

## Examples

### Items

```
Switch GarageDoorSwitch         "Garage Door Open"           {myq="0"}
Contact GarageDoorContact       "Garage Door [%s]" <contact> {myq="0"}
String GarageDoorString         "Garage Door [%s]"           {myq="0"}
Rollershutter GarageDoorShutter "Garage Door Open"           {myq="0"}
```

### Sitemap

```
Switch item=GarageDoorSwitch
Text item=GarageDoorString
Text item=GarageDoorContact
Switch item=GarageDoorShutter
```

The device index is needed to config the items. For people with only one garage door opener the value needs to be set to "0". If you have multiple garage door openers, the device index is based on the order the garage door opener's were added to the MyQ App. 

```
[INFO ] [.b.myq.internal.GarageDoorData] - Chamberlain MyQ Devices:
[INFO ] [.b.myq.internal.GarageDoorData] - DeviceID: 1810905 DeviceName: CG0810243E7F DeviceType: GarageDoorOpener 
```

## Known Working Hardware

| Model     | Name |
|-----------|------|
| HD950WF   | Chamberlain 1.25 hps Wi-Fi Garage Door Opener |
| WD850KEVG | Chamberlain 1 hps Garage Door Opener with MyQ Internet Gateway |
| WD832KEV  | Chamberlain 1/2 hps MyQ Garage Door Opener |
