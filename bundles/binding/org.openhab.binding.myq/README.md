# Chamberlain MyQ Binding

[Chamberlain MyQ](http://www.chamberlain.com/smartphone-control-products/myq-smartphone-control) system allows you to connect your garage door to the internet to be controlled from anywhere using your smartphone. Using this API, The Chamberlain MyQ Binding can get the status of your garage door opener and send commands to open or close it.

## Binding Configuration

This binding can be configured in the file `services/myq.cfg`.

The Chamberlain MyQ Binding only requires your Chamberlain MyQ Username and Password.

|   Property   | Default | Required | Description |
|--------------|---------|:--------:|-------------|
| username     |         |   Yes    | Chamberlain MyQ Username |
| password     |         |   Yes    | Chamberlain MyQ Password |
| refresh      | 60000   |   No     | Data refresh interval in milliseconds (60000 is one minute) |
| quickrefresh | 2000    |   No     | Data refresh interval after Event is trigger in milliseconds |
| timeout      | 5000    |   No     | Timeout for HTTP requests in milliseconds |
| appId        | see description | No | Application ID for the MyQ API (only recommended if existing id ceases to work). It is recommended you do not supply your own appId unless you understand how the binding internal logic works and how/what could stop working. | 

## Item configuration

Both Garage Door Openers and Light Switch Module are supported. For Garage Door Openers multiple Item Types are supported.

Contact type will display if you garage door is open or closed. String type will display the state from the MYQ API "Open,Closed,Opening,Closing,Partially Open/Closed". Switch type will display "ON" state for open or partially open/closed and "OFF" state for closed. Turning the switch ON will open the door and turning the switch OFF will close the door.

Light Switch Module only support Switch Item types.

The device index is needed to config the items. For people with only one garage door opener the value needs to be set to "0". If you have multiple garage door openers and/or lights, the device index is based on the order the garage door opener/light's were added to the MyQ App. Starting openHAB in debug mode will display your MyQ Devices and the order they are listed in the API. This order is how you determine the device indexes.

```
[DEBUG] [o.b.myq.internal.MyqDeviceData:44   ] - Chamberlain MyQ Devices:
[DEBUG] [binding.myq.internal.MyqDevice:40   ] - Lamp DeviceID: 1234567 DeviceType: LampModule Lightstate : 0
[DEBUG] [binding.myq.internal.MyqDevice:38   ] - GarageDoorOpener DeviceID: 1234568 DeviceType: GarageDoorOpener Doorstate : 2
```

Both Devices also support parameter values. The parameters values are other information supplied by the API that cannot be changed by the binding.

## Examples

### Items

```
Switch LampModule               "Lamp Module On"                {myq="0" autoupdate="false"}
String LampModuleCustomerName   "Lamp Module Name [%s]"         {myq="0#customerName"}
String LampModuleDesc           "Lamp Module Desc [%s]"         {myq="0#desc"}
String LampModuleOnline         "Lamp Module Online [%s]"       {myq="0#online"}
String LampModuleDeviceId       "Lamp Module Device Id [%s]"    {myq="0#MyQDeviceId"}
String LampModuleDeviceType     "Lamp Module DeviceType [%s]"   {myq="0#MyQDeviceTypeName"}
String LampModuleSerialNumber   "Lamp Module SerialNumber [%s]" {myq="0#SerialNumber"}

Switch GarageDoorSwitch         "Garage Door Open"              {myq="1"}
Contact GarageDoorContact       "Garage Door [%s]" <contact>    {myq="1"}
String GarageDoorString         "Garage Door [%s]"              {myq="1"}
Rollershutter GarageDoorShutter "Garage Door Open"              {myq="1"}
String GarageDoorCustomerName   "Garage Door Name [%s]"         {myq="1#customerName"}
String GarageDoorDesc           "Garage Door Desc [%s]"         {myq="1#desc"}
String GarageDoorOnline         "Garage Door Online [%s]"       {myq="1#online"}
String GarageDoorDeviceId       "Garage Door Device Id [%s]"    {myq="1#MyQDeviceId"}
String GarageDoorDeviceType     "Garage Door DeviceType [%s]"   {myq="1#MyQDeviceTypeName"}
String GarageDoorSerialNumber   "Garage Door SerialNumber [%s]" {myq="1#SerialNumber"}
```

### Sitemap

```
Switch item=LampModule
Text item=LampModuleCustomerName
Text item=LampModuleDesc
Text item=LampModuleOnline
Text item=LampModuleDeviceId
Text item=LampModuleDeviceType
Text item=LampModuleSerialNumber

Switch item=GarageDoorSwitch
Text item=GarageDoorString
Text item=GarageDoorContact
Switch item=GarageDoorShutter
Text item=GarageDoorCustomerName
Text item=GarageDoorDesc
Text item=GarageDoorOnline
Text item=GarageDoorDeviceId
Text item=GarageDoorDeviceType
Text item=GarageDoorSerialNumber
```

## Known Working Hardware

| Model     | Name |
|-----------|------|
| HD950WF   | Chamberlain 1.25 hps Wi-Fi Garage Door Opener |
| WD850KEVG | Chamberlain 1 hps Garage Door Opener with MyQ Internet Gateway |
| WD832KEV  | Chamberlain 1/2 hps MyQ Garage Door Opener |
| 825LM     | Liftmaster 825LM Remote Light Module |
| 823LM     | Liftmaster 823LM Remote Light Switch |
