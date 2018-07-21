# Exec Binding

Execute commands as you would enter on the command line, returning the output (possibly transformed) as the bound item's state.  Also, execute command lines in response to commands sent to bound items.

See [examples](#examples) below.

## Considerations

* The user under which you are running openHAB should have the necessary permissions in order to execute your command lines.
* When using the `ssh` command, you should use private key authorization, since the password cannot be read from the command line.
* There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/exec/).

## Binding Configuration

This binding does not have a configuration.

## Item Configuration

### Update item states

When updating the states of items based on executing a command line (an "in" binding):

```
exec="<[<commandLine to execute>:<refreshintervalinmilliseconds>:(<transformationrule>)]"
```

where:

* `<commandLine to execute>` is the command line to execute.  See [Formatting](#formatting) and [Splitting](#splitting) sections below.
* `<refreshintervalinmilliseconds>` is the frequency at which to repeatedly execute the command line.
* `<transformationrule>` is optional, and can be used to transform the string returned from the command before updating the state of the item.

Example item:

```
Number Temperature "Ext. Temp. [%.1f°C]" { exec="<[curl -s http://weather.yahooapis.com/forecastrss?w=566473&u=c:60000:XSLT(demo_yahoo_weather.xsl)]" }
```

### Sending commands

When executing a command line in response to the item receiving a command (an "out" binding):

```
exec=">[<openHAB-command>:<commandLine to execute>] (>[<openHAB-command>:<commandLine to execute>]) (>[...])"
```

where:

* `<openHAB-command>` is the openHAB command that will trigger the execution of the command line.  Can be `ON`, `OFF`, `INCREASE`, etc., or the special wildcard command '`*`' which is called in cases where no direct match could be found
* `<commandLine to execute>` is the command to execute.  See [Formatting](#formatting) and [Splitting](#splitting) sections below.

Example item:

```
Number SoundEffect "Play Sound [%d]" { exec=">[1:open /mp3/gong.mp3] >[2:open /mp3/greeting.mp3] >[*:open /mp3/generic.mp3]" }

```

### Old Format

Deprecated format (do not use; retained for backward compatibility only):

```
exec="<openHAB-command>:<commandLine to execute>[,<openHAB-command>:<commandLine to execute>][,...]"
```

### Formatting

You can substitute formatted values into the command using the syntax described [here](https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html).

1. the current [date](https://docs.oracle.com/javase/7/docs/api/java/util/Date.html), like `%1$tY-%1$tm-%1$td`.
1. the current command or state, like `%2$s` (out bindings only)
1. the current item name (like `%3$s`).

### Splitting

Sometimes the `<commandLine to execute>` isn't executed properly. In that case, another exec-method can be used. To accomplish this please use the special delimiter `@@` to split command line parameters.

## Examples

### Turn a Computer ON and OFF

On possible useage is to turn a computer on or off.  The Wake-on-LAN binding could be bound to a Switch item, so that when the switch receives an ON command, A Wake-on-LAN message is sent to wake up the computer.  When the switch item receives an OFF command, it will call the Exec binding to connect via ssh and issue the shutdown command.  Here is the example item:

```
Switch MyLinuxPC "My Linux PC" { wol="192.168.1.255#00-1f-d0-93-f8-b7", exec=">[OFF:ssh user@host shutdown -p now]" }
```

### KNX Bus to Exec Command

The example below combines three bindings to incorporate the following behavior: query the current state of the NAS with the given IP address. When receiving an OFF command over KNX or the user switches to OFF manually then send the given command line via the exec binding.

```
Switch Network_NAS	"NAS"	(Network, Status)	{ nh="192.168.1.100", knx="<2/0/0", exec=">[OFF:ssh user@host shutdown -p now]" }
```

### More Examples

```
exec=">[ON:/bin/sh@@-c@@echo on >> /tmp/sw_log] >[OFF:/bin/sh@@-c@@echo off >> /tmp/sw_log]"
exec=">[1:open /mp3/gong.mp3] >[2:open /mp3/greeting.mp3] >[*:open /mp3/generic.mp3]"
exec="<[curl -s http://weather.yahooapis.com/forecastrss?w=566473&u=c:60000:XSLT(demo_yahoo_weather.xsl)]"
exec="<[/bin/sh@@-c@@uptime | awk '{ print $10 }':60000:REGEX((.*?))]"
exec="<[execute.bat %1$tY-%1$tm-%1$td %2$s %3$s:60000:REGEX((.*?))]"
exec="<[php ./configurations/scripts/script.php:60000:REGEX((.*?))]"
exec="<[/bin/sh@@-c@@uptime | awk '{ print $10 }':]"

// deprecated format
exec="OFF:ssh user@host shutdown -p now"
exec="OFF:some command, ON:'some other\, more \'complex\' \\command\\ to execute', *:fallback command"
exec="1:open /path/to/my/mp3/gong.mp3, 2:open /path/to/my/mp3/greeting.mp3, *:open /path/to/my/mp3/generic.mp3"
```

