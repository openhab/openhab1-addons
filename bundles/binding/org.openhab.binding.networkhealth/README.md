Documentation of the NetworkHealth binding Bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

The NetworkHealth binding has several (optional) configuration settings in openhab.cfg:

    # Default timeout in milliseconds if none is specified in binding configuration
    # (optional, default to 5000)
    #networkhealth:timeout=

    # refresh interval in milliseconds (optional, default to 60000)
    #networkhealth:refresh=

    # Cache the state for n minutes so only changes are posted (optional, defaults to 0 = disabled)
    # Example: if period is 60, once per hour the online states are posted to the event bus;
    #          changes are always and immediately (refresh interval) posted to the event bus.
    # The recommended value is 60 minutes.
    #networkhealth:cachePeriod=60

**Timeout** is the default timeout that may be overwritten in the item bindings (see below). The **refresh interval** is the interval the binding checks for host reachability. This means that state updates for all items are sent to the event bus once per minute, even though the state does not change.
Assuming that the NH binding is the only one changing the items, these state updates are actually not needed. This is where the new configuration of a **cache period** comes into play. If set to a number x larger than 0, the binding caches the states and does not send item updates for x minutes if the states do not change. Setting the cache period does not introduce a permanent cache, so all item states are still updated after the configured cache period.

Example: with a default refresh interval of 60 sec and 20 items, there are 20 * 60 * 24 = 28800 events per day, no matter whether the states change or not.
Setting a cache period of 60min, there are just 20 * 24 = 480 events per day, plus the actual changes.
Worst case that could happen is that an item is changed by someone else (e.g. via a client or after item configuration reload) and might be out of sync with the NH cache until the next cache period is over. So the cache period should only be used if the NH binding is the only one changing these particular item states.

## Generic Item Binding Configuration

In order to bind an item to a NetworkHealth check, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). The syntax for the NetworkHealth binding configuration string is explained here:

    nh="<hostname>[:port][:timeout]"

where the parts in `[]` are optional. If no port is configured a simple ping is issued. If no timeout is configured the query defaults to '5000' milliseconds.

Here are some examples of valid binding configuration strings:

    nh="www.google.com:80"
    nh="openhab.org"
    nh="openhab.org:443"
    nh="openhab.org:443:2000"


As a result, your lines in the items file might look like the following:

    Switch Network_OpenhabWebsite   "openHAB Web"   (Status, Network)   { nh="openhab.org:80" }

## Quirks

If the openHAB server user does not have permission to use ICMP for the ping, then Java will attempt to use the Echo server (port 7) on the remote device. If that service is not available, then the ping attempt fails. For discussion of this issue, see the following thread in the community forum:

https://community.openhab.org/t/networkhealth-unable-to-detect-some-devices/2902