# MQTT Persistence

This service allows you to feed item states to an MQTT broker using the openHAB persistence strategies.  The destination broker, topic and messages are configurable.

This persistence service supports only writing information, and so features such as `restoreOnStartup` and sitemap Chart widgets cannot be used with this service.

## Configuration

This service can be configured in the file `services/mqtt-persistence.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| broker   |         |   Yes    | name of the broker as defined in the `<broker>`.url in `services/mqtt.cfg`.  See the MQTT Binding for more information on how to configure MQTT broker connections. |
| topic    |         |   Yes    | MQTT topic to which the persistence messages should be sent. This string may include parameters, see table below. |
| message  |         |   Yes    | string representing the persistence message content. This string may include parameters, see table below. |

Both the topic and message values are reformatted using String.format at the time of publishing.  During this reformat, the following parameters are provided to the format function:

| Parameter | Description |
|:---------:|-------------|
|     1$    | item name   |
|     2$    | item alias as defined in the `persistence/mqtt.persist` file. |
|     3$    | item state: a string representation of the item state. ON/OFF, OPEN/CLOSED and UP/DOWN states are transformed to 1/0 values, respectively. |
|     4$    | current DateTime in long format. |

All item- and event-related configuration is done in the file `persistence/mqtt.persist`.

## Examples

### Configuration for Xively

The following is an example configuration that can be used to persist item states with Xively using the csv format. 
The messages sent to Xively will have the format 

```
<openhab_item>, <item state>
```

For example:

```
light_office, 1
```

First, we need to define the MQTT broker connection in the `services/mqtt.cfg` file:

```
xively.url=tcp://api.xively.com:1883
xively.user=<device key>
xively.pwd=<device key>
```

As `<device key>`, use the device/API key you get from Xively.

Next, we need to configure the MQTT persistence service. To do this, add the following entries to the `services/mqtt-persistence.cfg` file:

```
broker=xively
topic=/v2/feeds/<feed id>.csv
message=%1$s, %3$s
```

where `<feed id>` is your Xively feed id.

And finally, add a persistence strategy in the `persistence/mqtt.persist` file:

```
Strategies {
        everyHour : "0 0 * * * ?"
        default = everyChange
}
Items {
        // persist all items every hour and on every change
        * : strategy = everyChange, everyHour
}
```

With this configuration, all items will persisted whenever their state changes and once per hour.
