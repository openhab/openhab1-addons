# Network Health Binding

This binding allows openHAB to check whether a device is currently available on the network.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/network/).

## Binding Configuration

This binding can be configured in the file `services/networkhealth.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| timeout  | 5000    |    No    | the default timeout (in milliseconds) if none is specified in the item configuration (see below) |
| refresh  | 60000   |    No    | the interval (in milliseconds) that the binding checks for host reachability. This means that state updates for all items are sent to the event bus once per minute, even though the state does not change.  Assuming that the Network Health binding is the only one changing the items, these state updates are actually not needed.  This is where the new configuration of `cachePeriod` comes into play. |
| cachePeriod | 0    |    No    | if set to a number larger than 0, the binding caches the states and does not send item updates for `cachePeriod` minutes if the states do not change.  Setting the cache period does not introduce a permanent cache, so all item states are still updated after the configured cache period. Example: if `cachePeriod` is 60, the online states are posted once per hour to the event bus; changes are always and immediately (refresh interval) posted to the event bus.  The recommended value is 60 minutes. |

Example: with a default refresh interval of 60 sec and 20 items, there are 20 * 60 * 24 = 28800 events per day, no matter whether the states change or not.

Setting a cache period of 60 minutes, there are just 20 * 24 = 480 events per day, plus the actual changes.

Worst case that could happen is that an item is changed by someone else (e.g. via a client or after item configuration reload) and might be out of sync with the NH cache until the next cache period is over. So the cache period should only be used if the Network Health binding is the only one changing these particular item states.

## Item Configuration

The syntax for the NetworkHealth binding configuration string is explained here:

```
nh="<hostname>[:port][:timeout]"
```

where `<hostname>` is the name of the host to check, and the parts in `[]` are optional. If no port is configured, a simple ping is issued. If no timeout is configured, the query defaults to `timeout` milliseconds as set in the binding configuration.

Here are some examples of valid binding configuration strings:

```
nh="www.google.com:80"
nh="openhab.org"
nh="openhab.org:443"
nh="openhab.org:443:2000"
```

## Example

```
Switch Network_OpenhabWebsite   "openHAB Web"   (Status, Network)   { nh="openhab.org:80" }
```

## Quirks

If the openHAB server user does not have permission to use ICMP for the ping, then Java will attempt to use the Echo server (port 7) on the remote device. If that service is not available, then the ping attempt fails.
