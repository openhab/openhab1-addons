# SiteWhere Persistence

The [SiteWhere](http://www.sitewhere.org/) persistence service allows openHAB item states to be forwarded to a SiteWhere server instance running locally or in the cloud. Selected events from an openHAB instance can be stored in SiteWhere under a virtual device with hardware ID specified in the persistence provider implementation. 

Events are delivered via the SiteWhere agent which uses the MQTT protocol. The SiteWhere [administrative application](http://documentation.sitewhere.org/userguide/adminui/adminui.html) may be used to view data for the virtual device. It can also be used to issue commands to items in openHAB, based on the SiteWhere command framework. See [this tutorial](http://documentation.sitewhere.org/integration/openhab.html) for a step-by-step walkthrough.

## Prerequisites

Both SiteWhere and openHAB use port 8080 by default, so the port will need to be changed if both are running on the same machine. To change the port for SiteWhere, open the file `conf/server.xml` file and look for the following:

```xml
<Connector port="8080" protocol="HTTP/1.1"
    connectionTimeout="20000"
    redirectPort="8443" />
```

Change the port to another value, such as 9090.

## Configuration

No configuration is necessary for SiteWhere to listen for events on the MQTT transport if using the default tenant configuration. The SiteWhere agent used by the persistence service is configured to send MQTT messages on the topic where SiteWhere is listening.  Otherwise, this service can be configured in the file `services/sitewhere.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| defaultHardwareId | `123-OPENHAB-777908324` | No | hardware ID of device that will receive events. It provides an association between the openHAB instance and a SiteWhere device. Once connected, if no device exists in SiteWhere with the given hardware ID, a new openHAB virtual device will be registered under that ID. All data sent from the openHAB instance will be recorded under the virtual device. If more than one openHAB instance is connecting to SiteWhere, different hardware IDs should be used for each instance. SiteWhere can scale to support thousands or even millions of openHAB instances running concurrently. |
| specificationToken | `5a95f3f2-96f0-47f9-b98d-f5c081d01948` | No | device specification token used if device is not already registered. It indicates the device specification to be used if a new device needs to be registered with SiteWhere (if the hardware id does not exist). The default value corresponds to the *openHAB Virtual Device* specification included with the SiteWhere sample data. This specification includes the device commands used to trigger events on the openHAB bus from SiteWhere. |
| mqttHost | localhost |  No    | host name for the MQTT broker that SiteWhere is listening to |
| mqttPort | 1883    |    No    | port number for the MQTT broker that SiteWhere is listening to |

All item- and event-related configuration is done in the file `persistence/sitewhere.persist`.
