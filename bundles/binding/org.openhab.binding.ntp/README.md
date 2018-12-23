# Network Time Protocol (NTP) Binding

The [Network Time Protocol](https://en.wikipedia.org/wiki/Network_Time_Protocol) (NTP) Binding is used to query an NTP server you configure for the current time, within tens of milliseconds of accurary.  Items in openHAB will receive these updates.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/ntp/).

## Binding Configuration

This binding can be configured in the file `services/ntp.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 15      |    No    | Frequency that queries will be made to the NTP server defined in `hostname` |
| hostname | ptbtime1.ptb.de | No | The NTP server host to query |    

## Item Configuration

The syntax for the NTP binding configuration string is explained here:

```
ntp="[<timeZone>][:<locale>]"
```

where the parts in `[]` are optional. If no or an incorrect `<timeZone>` is configured it defaults to `TimeZone.getDefault()`. If no or an incorrect `<locale>` is configured it defaults to `Locale.getDefault()`.

Here are some examples of valid binding configuration strings:

```
ntp="America/Detroit:en_US"
ntp="Europe/Berlin:de_DE"
ntp="Europe/Berlin"
ntp=""
```

## Examples

### Simple

```
DateTime    Date    "Date and Time: [%1$tA, %1$td.%1$tm.%1$tY %1$tT]"  (Status)   { ntp="Europe/Berlin:de_DE" } 
``

### Update to KNX Bus

If you would like to post the queried time to the knx-bus your line might look like:

```
DateTime    Date    "Date and Time: [%1$tA, %1$td.%1$tm.%1$tY %1$tT]"  (Status)    { ntp="Europe/Berlin:de_DE", knx="11.001:15/7/2, 10.001:15/7/1" } 
```

where `11.001` is the KNX date type and `10.001` is the KNX time type

### Full Example

This will use your system information to determine timezone and locale.

services/ntp.cfg

```
hostname=0.us.pool.ntp.org
```

items/example.items

```
DateTime    Date    "Date [%1$tA, %1$td.%1$tm.%1$tY]"   (Status)    { ntp="" }
```

sitemap/example.sitemap

```
    sitemap example
    {
        Text item=Date
    }
```


## Formatting

[Here](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) is an overview of how you can format the output of date and time.
