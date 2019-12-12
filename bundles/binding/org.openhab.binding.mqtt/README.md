# MQTT Binding

This binding allows openHAB to act as an MQTT client, so that openHAB items
can send and receive [MQTT](http://mqtt.org/) messages to/from an MQTT broker.
It does not provide MQTT broker functionality, for this you may want to have a
look at [Mosquitto](http://mosquitto.org/) or others. There are test servers
available at m2m.eclipse.org and test.mosquitto.org. 

openHAB provides MQTT support on different levels. The table below gives a quick overview:

| Level | Description | Usage | Configuration |
|-------|-------------|-------|--------|
| **Transport** | Shared transport functions for setting up MQTT broker connections. | Ideal if you want to roll your own binding using MQTT as the transport. | `services/mqtt.cfg` |
| **Item binding** | Allows MQTT publish/subscribe configuration on item level | Ideal for highly customized in and outbound message scenarios. | `items/*.items` |
| **Event bus binding** | Publish/receive all states/commmands directly on the openHAB eventbus. | Perfect for integrating multiple openHAB instances or broadcasting all events. | `services/mqtt-eventbus.cfg` |
| **Persistence** | Uses persistent strategies to push messages on change or a regular interval. | Perfect for persisting time series to a public service like Xively. (See MQTT persistence service.) | `persist/mqtt.persist` |

The OwnTracks (formerly Mqttitude) binding is also available, which is an extension of this binding.

## Transport Configuration

In order to consume or publish messages to an MQTT broker, you need to define all the brokers which you want to connect to, in your `services/mqtt.cfg` file.

| Property             | Default | Required | Description                                                 |
|----------------------|---------|:--------:|-------------------------------------------------------------|
| `<broker>`.url       |         | Yes      | URL to the MQTT broker, e.g. tcp://localhost:1883 or ssl://localhost:8883 |
| `<broker>`.clientId  | random  | No*      | Client ID to use when connecting to the broker. If not provided a random default is generated. Usually restricted to 23 characters in length. (see the `allowLongerClientIds` setting) |
| `<broker>`.user      |         | Yes      | User ID to authenticate with the broker.                    |
| `<broker>`.pwd       |         | Yes      | Password to authenticate with the broker.                   |
| `<broker>`.qos       | 0       | No       | Set the quality of service level for sending messages to this broker. Possible values are 0 (Deliver at most once), 1 (Deliver at least once) or 2 (Deliver exactly once). |
| `<broker>`.retain    | false   | No       | True or false. Defines if the broker should retain the messages sent to it. |
| `<broker>`.async     | true    | No       | True or false. Defines if messages are published asynchronously or synchronously. |
| `<broker>`.keepAlive | 60      | No       | Integer. Defines the keep alive interval in seconds.        |
| `<broker>`.allowLongerClientIds | false | No | True or false. If set to true, allows the use of clientId values up to 65535 characters long. NOTE: clientId values longer than 23 characters may not be supported by all MQTT servers. Check the server documentation. |

`<broker>` is an alias name for the MQTT broker.  This is the name you can use in the item binding configurations afterwards.<br>
\* `<broker>`.clientId is not required to be provided because a random value will be generated if a value is not provided.

### Example Configurations

Example configuration of a simple broker connection:

```
m2m-eclipse.url=tcp://m2m.eclipse.org:1883
```

Example configuration of an encrypted broker connection with authentication:

```
mosquitto.url=ssl://test.mosquitto.org:8883
mosquitto.user=administrator
mosquitto.pwd=mysecret
mosquitto.qos=1
mosquitto.retain=true
mosquitto.async=false
```

## Item Configuration for Inbound Messages

Below you can see the structure of the inbound MQTT configuration string.
Inbound configurations allow you to receive MQTT messages into an openHAB
item. Every item is allowed to have multiple inbound (or outbound)
configurations.

```
Item myItem {mqtt="<direction>[<broker>:<topic>:<type>:<transformer>], <direction>[<broker>:<topic>:<type>:<transformation>], ..."} 
```

Since 1.6 it is possible to add an optional fifth configuration like:

```
Item myItem {mqtt="<direction>[<broker>:<topic>:<type>:<transformer>:<regex_filter>], <direction>[<broker>:<topic>:<type>:<transformation>], ..."} 
```

| Property | Description |
|----------|-------------|
| direction | This is always "&lt;" for inbound messages. |
| broker | The broker alias as it is defined in the openHAB configuration. |
| topic | The MQTT Topic to subscribe to. If a colon is part of the topic, use \\: to escape the colon.|
| type | Describes what the message content contains: a status update or command. Allowed values are 'state' or 'command'. |
| transformation | Rule defining how to transform the received message content into something openHAB recognizes. Transformations are defined in the format of TRANSFORMATION_NAME(transformation_function).  Allowed values are 'default' or any of the transformers provided in the org.openhab.core.transform bundle. Custom transformations can be contributed directly to the transform bundle by making the Transformation available through Declarative Services. Any value other than the above types will be interpreted as static text, in which case the actual content of the message is ignored. |
| regex_filter (optional, since 1.6) | A string representing a regular expression. Only messages that match this expression will be further processed. All other messages will be dropped. Use Case: If multiple different data is sent over one topic (for example multiple sensors of one device), it is possible to distinguish the messages for different items. Example ".*" (excluding the quotes) will match every message, ".*\"type\"=2\n.*" (excluding the quotes) will match every message including type=2. |

### Example Inbound Configurations

```
Number temperature "temp [%.1f]" {mqtt="<[publicweatherservice:london-city/temperature:state:default]"}
Number waterConsumption "consum [%d]" {mqtt="<[mybroker:myHome/watermeter:state:XSLT(parse_water_message.xslt)]"} 
Switch doorbell "bell [%s]" {mqtt="<[mybroker:myHome/doorbell:command:ON]"}
Number mfase1 "mfase1 [%.3f]" {mqtt="<[flukso:sensor/9cf3d75543fa82a4662fe70df5bf4fde/gauge:state:REGEX(.*,(.*),.*)]"}
Number humidity "humidity [%.1f%%]" {mqtt="<[broker:weatherstation/readings:state:JS(convertPercent.js):humidity=.*]"}
```

## Item Configuration for Outbound Messages

Below you can see the structure of the outbound MQTT configuration string.
Outbound configurations allow you to publish (send) an MQTT message to the
MQTT broker when an item receives a command or state update, and other MQTT
clients that are subscribed to the given topic on the same broker, like
Arduino devices for example, will receive those messages. 

```
Item itemName { mqtt="<direction>[<broker>:<topic>:<type>:<trigger>:<transformation>]" }
```

| Property | Description |
|----------|-------------|
| direction | This is always "&gt;" for outbound messages. |
| broker | The broker alias as it was defined in the openHAB configuration. |
| topic | The MQTT Topic to publish messages to. |
| type | 'state' or 'command'. Indicates whether the receiving of a status update or command triggers the sending of an outbound message. |
| trigger | Specifies an openHAB command or state (e.g. ON, OFF, a DecimalType, ..) which triggers the sending of an outbound message. Use `*` to indicate that any command or state should trigger the sending. |
| transformation | Rule defining how to create the message content. Transformations are defined in the format of TRANSFORMATION_NAME(transformation_function).  Allowed values are 'default' or any of the transformers provided in the org.openhab.core.transform bundle. Custom transformations can be contributed directly to the transform bundle by making the Transformation available through Declarative Services. Any value other than the above types will be interpreted as static text, in which case this text is used as the message content. |


When the message content for an outbound message is created, the following variables are replaced with their respective values:

- ${itemName} : name of the item which triggered the sending
- ${state}    : current state of the item (only for type 'state')
- ${command}  : command which triggered the sending of the message (only for type 'command')

### Example Outbound Configurations

```
Switch mySwitch {mqtt=">[mybroker:myhouse/office/light:command:ON:1],>[mybroker:myhouse/office/light:command:OFF:0]"}
Switch mySwitch {mqtt=">[mybroker:myhouse/office/light:command:ON:1],>[mybroker:myhouse/office/light:command:*:Switch ${itemName} was turned ${command}]"}
```

## Event Bus Binding Configuration

In addition to configuring MQTT publish/subscribe options for specific openHAB
items, you can also define a generic configuration in the `services/mqtt-eventbus.cfg`
file which will act on **ALL** status updates or commands on the openHAB event
bus.

The following properties can be used to configure MQTT for the openHAB event
bus in the file `services/mqtt-eventbus.cfg`:

```
broker=<broker>
statePublishTopic=<statePublishTopic>
commandPublishTopic=<commandPublishTopic>
stateSubscribeTopic=<stateSubscribeTopic>
commandSubscribeTopic=<commandSubscribeTopic>
```

The properties indicated by `<...>` need to be replaced with an actual value.
The table below lists the meaning of the different properties.

| Property | Description |
|----------|-------------|
| broker   | Name of the broker as it is defined in the `services/mqtt.cfg`. If this property is not available, no event bus MQTT binding will be created. |
| statePublishTopic | When available, all status updates which occur on the openHAB event bus are published to the provided topic. The message content will be the status. The variable ${item} will be replaced during publishing with the item name for which the state was received. |
| commandPublishTopic | When available, all commands which occur on the openHAB event bus are published to the provided topic. The message content will be the command. The variable ${item} will be replaced during publishing with the item name for which the command was received. |
| stateSubscribeTopic | When available, all status updates received on this topic will be posted to the openHAB event bus. The message content is assumed to be a string representation of the status. The topic should include the variable ${item} to indicate which part of the topic contains the item name which can be used for posting the received value to the event bus. |
| commandSubscribeTopic | When available, all commands received on this topic will be posted to the openHAB event bus. The message content is assumed to be a string representation of the command. The topic should include the variable `${item}` to indicate which part of the topic contains the item name which can be used for posting the received value to the event bus. |

### Example Configurations

Example configuration for an event bus binding, which sends all commands to an
MQTT broker and receives status updates from that broker. This scenario could
be used, for example, to link 2 openHAB instances together where the master
instance sends all commands to the slave instance and the slave instance sends
all status updates back to the master. The example below shows an example
configuration for the master node.

```
broker=m2m-eclipse
commandPublishTopic=/openHAB/out/${item}/command
stateSubscribeTopic=/openHAB/in/${item}/state
```

## Using the transport (org.openhab.io.transport.mqtt) bundle

When the default MQTT binding configuration options are not sufficient for
your needs, you can also use the MQTT transport bundle directly from within
your own binding.

## MqttService

Using the MqttService, your binding can add custom message consumers and
publishers to any of the defined MQTT brokers. You don't have to worry about
(re)connection issues; all of this is done by the transport.mqtt bundle. The
MqttService class is available to your binding through Declarative Services.
A good example on how to use the MqttService can be found in the org.openhab.persistence.mqtt
bundle.

## Eclipse Paho

If the above service doesn't provide all the flexibility you need, you can
also use the Eclipse Paho library directly in your binding.  To make the
library available, it's sufficient to add a dependency to the org.openhab.io.transport.mqtt
bundle and to add org.eclipse.paho.client.mqtttv3 to your list of imported packages.
