# OwnTracks (formerly MQTTitude) Binding

[OwnTracks](http://owntracks.org), formerly known as _MQTTitude_, was developed as a replacement for the old Google Latitude service. However it is slightly different (better) in that all your location data is private - i.e. there is no 3rd party server somewhere collecting and storing your data. Instead each time your device publishes its location, it is sent to an [MQTT](http://mqtt.org/) broker of your choice. 

This is where openHAB steps in, with the Mqttitude binding. The idea is that the binding will track your location and when you are 'near' to a specified location (usually your home) it will update a Switch item in openHAB, enabling presence detection.

First you need to set up an MQTT broker (e.g. [Mosquitto](http://mosquitto.org/)) and install the [OwnTracks](http://owntracks.org) app on your mobile devices. At this point you should start seeing location updates appearing in your broker. 

NOTE: from openHAB v1.7 the Mqttitude binding will support both the old and new versions of the [OwnTracks](http://owntracks.org) apps. Please see the OwnTracks documentation for more details about the different versions. As of now (18 April 2015) the new versions of the app have not been released.

| Platform | Old       | New        |
|----------|-----------|------------|
| iOS      | &lt; v8.0 | &gt;= v8.0 |
| Android  | &lt; v5.0 | &gt;= v5.0 |

Where the 'new' versions will publish lat/lon values as floats (rather than strings) and region enter/leave events are published to a `/event` sub topic (see below for details).  As of 1 June 2016, the current version of the OwnTracks mobile app is 9.1.5 (iOS) and 0.6.14 (Android).

Now it is time to configure the MQTT and the Mqttitude bindings...

## Broker Configuration

First you will need to install and configure the MQTT binding. This will define the connection properties for your MQTT broker and specify the broker ID which we will need when configuring the Mqttitude item bindings.

## Binding Configuration

There are two modes of operation for the Mqttitude binding. Note: you can have item bindings which are mixture of these two modes.

#### Manual Mode 

The first is a manual calculation of your position relative to a single fixed 'home' geofence. In this mode you specify the 'home' geofence in your `services/mqttitude.cfg` file and then configure your item bindings to watch for location publishes from the OwnTracks app. As each location update is received the binding will calculate the distance from 'home' and update the item (ON/OFF) accordingly.

#### Region Mode 

The second mode leaves the geofence definition and relative location calculations to the OwnTracks app itself. You can setup any number of 'regions' or 'waypoints' in your app and give them unique descriptions. Then in openHAB you simply add the region/waypoint description (optional third parameter of the item binding) and the binding will look for 'enter' or 'leave' events which are published by the app and switch the openHAB item accordingly. This allows you to define as many 'regions' or 'waypoints' as you like, and track a phones location relative to many points of interest - e.g. home, work, holiday house. 

NOTE: if you are using the latest versions of the OwnTracks apps you will need to append `/event` to your MQTT topic as the apps now publish the 'enter'/'leave' events to this sub-topic, rather than the base location topic.
 
### services/mqttitude.cfg Config

You only need to define these properties in your `services/mqttitude.cfg` configuration file if you are using one or more 'Manual Mode' item bindings. In this mode you need to let the binding know exactly where 'home' is and the size of your [geofence](http://en.wikipedia.org/wiki/Geo-fence).

Here is an example;

```
# Optional. The lat/lon coordinates of 'home'
home.lat=xxx.xxxxx
home.lon=xxx.xxxxx

# Optional. Distance in metres from 'home' to be considered 'present'
geofence=100`
```

### Item Configuration

To track the location/presence of a mobile device all you need to do is add a Switch item and specify the MQTT topic that device publishes its location to. 

The binding definition for the two modes of operation are;

Manual Mode:

```
{ mqttitude="<broker_id>:<mqtt_topic>" }
```

Region Mode (old):

```
{ mqttitude="<broker_id>:<mqtt_topic>:<region_description>" }
```

Region Mode (new):

```
{ mqttitude="<broker_id>:<mqtt_topic>/event:<region_description>" }
```

#### Manual Mode

Here is an example of some 'Manual Mode' item bindings;

```
Switch  PresenceBen_PhoneMqtt   "Ben @ Home"   { mqttitude="mosquitto:owntracks/ben" }
Switch  PresenceSam_PhoneMqtt   "Sam @ Home"   { mqttitude="mosquitto:owntracks/sam" }
```

You can track as many different mobile devices as you like, on the one MQTT broker, just by using a different MQTT topic for each. This is configured in the OwnTracks apps on your mobile devices.

When a device publishes a location the binding will receive it instantly, calculate the distance from your 'home' location, and if inside the 'geofence' radius set the Switch item to ON.

#### Region Mode

Here is an example of some 'Region Mode' item bindings;

```
Switch  PresenceBen_PhoneMqttHome   "Ben @ Home"   { mqttitude="mosquitto:owntracks/ben:home" }
Switch  PresenceBen_PhoneMqttWork   "Ben @ Work"   { mqttitude="mosquitto:owntracks/ben:work" }
```

And for the new versions of the OwnTracks apps;

```
Switch  PresenceBen_PhoneMqttHome   "Ben @ Home"   { mqttitude="mosquitto:owntracks/ben/event:home" }
```

Here you can setup as many 'regions' or 'waypoints' as you like in your OwnTracks app and track each one in openHAB by creating a different item for each, with the region description as the optional third parameter in the item binding.

This is a far more powerful mode and gives greater flexibility. It also stops the issue of location publishes happening just before you get close enough to 'home' and thus being considered outside the geofence, and then no further updates being sent because you don't move far enough to trigger one.

In 'Region Mode' the OwnTracks apps detects when you cross a geofence boundary and ALWAYS sends a location update (either enter or leave), meaning openHAB should never lose track of your position. 

All regions/waypoints configured in the OwnTracks apps must to be set as 'Shared' as otherwise the description field is not sent, which the binding needs to match to an item.

#### iPhone considerations

Due to power management policies, OwnTracks on iPhone running in the background is only updating the location roughly every 10 minutes and sometimes even longer. This is blocking for many use cases that need timing wise short response.
