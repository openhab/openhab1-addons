# Fritz!Box Binding

> There are 3 openHAB FRITZ!Box bindings available right now.
> * [Fritz!Box Binding](https://www.openhab.org/v2.4/addons/bindings/fritzbox1/) (openHAB 1.x)
> * [Fritzbox Binding (using TR064 protocol)](https://www.openhab.org/v2.4/addons/bindings/fritzboxtr0641/) (openHAB 1.x)
> * [AVM FRITZ! Binding](https://www.openhab.org/v2.4/addons/bindings/avmfritz/) (openHAB 2.x)
>
> This is due to the fact that transitioning from OH1 to OH2 is still ongoing and the functionality of the openHAB 1.x bindings is being consolidated into a new openHAB 2.x binding.
> The openHAB 2.x binding is not yet feature completed, but will supersede the openHAB 1.x bindings in the future.
>
> Both openHAB 1.x bindings offer a similar feature set, but use a different access mechanism to the FRITZ!Box. 
> The "Fritz!Box" binding uses a telnet connection to access certain features of the box and "Fritzbox (using TR064 protocol)" uses the TR064 protocol via HTTP SOAP requests on ports 49000/49443.
> Both features need to be activated on the FRITZ!Box first, before the access works and actions can be triggered.
> 
> NOTE: There is no restriction in using OpenHAB 1.x bindings on an openHAB 2.x server. 
> Just follow the instructions on the binding pages to configure the 1.x binding on a 2.x server.

## Prerequisites

* Enable telnet: from a phone connected to the FRITZ!Box, dial `#96*7*` to enable telnet, dial `#96*8*` to disable
    Note: support for enabling telnet on the FRITZ!Box has been dropped due to security reasons in firmware version 06.25 and later.
The [Fritzbox binding (using TR06 protocol)](https://www.openhab.org/addons/bindings/fritzboxtr0641/) may be a suitable replacement.

* Activate user login: FRITZ!Box-Benutzer -> Anmeldung im Heimnetz -> Anmeldung mit dem FRITZ!Box-Kennwort


## Binding Configuration

This binding can be configured in the `services/fritzbox.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | IP address of your Fritz!Box |
| password |         |   Yes    | Password to your Fritz!Box |
| user     |         | when user management is enabled | User of your Fritz!Box. It is a good practice to create an additional user for openHAB. |


## Item Configuration

The format of the binding configuration is simple and looks like this:

```
fritzbox="<eventType>"
```

where `<eventType>` is one of the following values:

- inbound - for incoming calls
- outbound - for placed calls
- active - for currently active calls

Fritz!Box item configurations are valid on Switch and Call items.

Switch items with this binding will receive an ON update event at the start and an OFF update event at the end (a connection marks the end for inbound and outbound types, only active type will be ON for connected calls).

Call items will receive the external and the internal phone number in form of a string value as a status update. At the end of the event, an empty Call item which contains empty strings is sent as a status update.

As a result, your lines in the items file might look like the following:

```
Switch    Incoming_Call     "Ringing"                        (Phone)    { fritzbox="inbound" }
Call      Active_Call       "Connected to [%1$s from %2$s]"  (Phone)    { fritzbox="active"  }
Call      Incoming_Call_No  "Caller No. [%2$s]"              (Phone)    { fritzbox="inbound" } 
```

## Switching WIFI and DECT

The following items switch DECT, WIFI, GUEST_WIFI and the answering machine 0 (default TAM):

```
Switch DECT {fritzbox="dect"}
Switch WIFI {fritzbox="wlan"}
Switch GWIFI {fritzbox="guestwlan"} (Version 1.7.0)
Switch TAM0 {fritzbox="tam0"}
```
