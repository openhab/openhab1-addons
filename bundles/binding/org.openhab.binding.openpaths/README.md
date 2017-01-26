# OpenPaths Binding

[OpenPaths](https://openpaths.cc/%E2%80%8E) is an app you can install on your Android or iOS device which will periodically upload your location to the OpenPaths servers. Using the OpenPaths binding, openHAB will periodically request your latest location data from the OpenPaths servers and calculate your presence relative to a specified location (usually your home).

## Prerequisites

First you will need to install the OpenPaths app on your mobile device. You will then need to sign up to the OpenPaths service. At this point you should be issued with an ACCESS_KEY and SECRET_KEY which you will need to configure the OpenPaths binding. You will also need to link your app to your new OpenPaths account.

## Binding Configuration

You need to let the binding know exactly where 'home' is and what size the [geofence](http://en.wikipedia.org/wiki/Geo-fence) is. You can also optionally specify the refresh interval which determines how often openHAB will poll the OpenPaths servers.  This information can be entered in the file `services/openpaths.cfg`.

Beside a location named 'home' (mandatory) you may specify any number of additional locations, each of it optionally having its own geofence.

Finally, you need to add entries for each OpenPaths account you wish to track.  You can track as many different OpenPaths accounts as you like, you will just need a set of entries in your openhab.cfg for each account, containing the ACCESS_KEY and SECRET_KEY from each account.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| home.lat |         |   Yes    | Latitude of home in decimal degrees |
| home.long |        |   Yes    | Longitude of home in decimal degrees |
| home.geofence | Value of `geofence` | No | Size of the geofence around home |
| `<location>`.lat | | | Latitude of `<location>` in decimal degrees. You may define any number of additional locations. |
| `<location>`.long | | | Longitude of `<location>` in decimal degrees |
| `<location>`.geofence | | | Geofence around `<location>` in decimal degrees |
| geofence | 100 | No | Distance in metres a user must be from 'home' to be considered inside the geofence |
| refresh | 300000 | No | Interval in milliseconds to poll for user location (defaults to 5mins) |
| `<user>`.accesskey | | Yes | OpenPaths access key for `<user>` |
| `<user>`.secretkey | | Yes | OpenPaths secret key for `<user>` |

#### Item Configuration

To track the location/presence of a mobile device, all you need to do is add a Switch item and specify the name you entered in your binding configuration file (for `<user>`) for the OpenPaths account you wish to track.

You can also bind the distance (direct view line) of a tracked phone to a Number item. The coordinates as delivered from OpenPaths API are available as String 'latitude, longitude' and in addition as separate Numbers.

```
Switch  User1PresenceHome     "User 1 @ Home"                    { openpaths="user1:home" }
Number  User1HomeDistance     "User 1 away from home: [%.2f km]" { openpaths="user1:home:distance" }

Switch  User1PresenceWork     "User 1 @ work"                    { openpaths="user1:work" }
Number  User1WorkDistance     "User 1 away from work: [%.2f km]" { openpaths="user1:work:distance" }

String  User1Phone_currentLoc "User1 current location: [%s]"     { openpaths="user1:currentLocation" }
Number  User1Phone_currentLat "User1 latitude: [%.4f °]"         { openpaths="user1:currentLatitude" }
Number  User1Phone_currentLon "User1 longitude: [%.4f °]"        { openpaths="user1:currentLongitude" }
```
