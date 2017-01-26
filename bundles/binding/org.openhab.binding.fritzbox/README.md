# Fritz!Box Binding

## Binding Configuration

This binding can be configured in the `services/fritzbox.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | IP address of your Fritz!Box |
| password |         |   Yes    | Password to your Fritz!Box |
| user     |         | when user management is enabled | User of your Fritz!Box. It is a good practice to create an additional user for openHAB. |

## Prepare your Fritzbox

* Enable Telnet ([Link](http://www.wehavemorefun.de/fritzbox/Starten_von_telnetd))<BR>
* Activate User Login: FRITZ!Box-Benutzer -> Anmeldung im Heimnetz -> Anmeldung mit dem FRITZ!Box-Kennwort

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
