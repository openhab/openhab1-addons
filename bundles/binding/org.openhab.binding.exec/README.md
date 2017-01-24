Documentation of the Exec binding Bundle

## Introduction

The Exec binding bundle is available as a separate (optional) download.
If you want to enhance openHAB with a "swiss-army-knife-binding" which executes given commands on the commandline, please place this bundle in the folder `${openhab_home}/addons` and add binding information to your configuration. See the following sections on how to do this. 

The Exec binding could act as the opposite of WoL and sends a shutdown command to the servers. Or switches off WLAN connectivity if a scene "sleeping" is activated.

Note: when using 'ssh' you should use private key authorization since the password cannot be read from commandline. The given user should have the necessary permissions.

## Generic Item Binding Configuration

In order to bind an item to a Exec check, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the Exec binding configuration string is explained here:

### Old format:

    exec="<openHAB-command>:<commandLine to execute>[,<openHAB-command>:<commandLine to execute>][,...]"

where the parts in `[]` are optional.

Note: Besides configuring each single openHAB command one could configure the special wildcard command '`*`' which is called in these cases where no direct match could be found.

### New format:

    in:  exec="<[<commandLine to execute>:<refreshintervalinmilliseconds>:(<transformationrule>)]"
    out: exec=">[<openHAB-command>:<commandLine to execute>] (>[<openHAB-command>:<commandLine to execute>]) (>[...])"

where the parts in `()` are optional.

Notes:
- Besides configuring each single openHAB command one could configure the special wildcard command '`*`' which is called in these cases where no direct match could be found.
- A description to the transformations can be found [here](https://github.com/openhab/openhab/wiki/Transformations). Transforming the command's output is optional (just skip the transformation rule after the ":").

### General

The given commandLine can be enhanced using the well known Syntax of the java.util.Formatter.

The binding currently adds to parameters to the String.format() automatically:

1. the current date (as java.util.Date, example: `%1$tY-%1$tm-%1$td`)
1. the current Command or State (out binding only, example: `%2$s`)
1. the current item name (example: `%3$s`)

Sometimes the commandLine isn't executed properly. In that cases another exec-method can be used. To accomplish this please use the special delimiter @@ to split command line parameters.

Here are some examples of valid binding configuration strings:

    exec="1:open /path/to/my/mp3/gong.mp3, 2:open /path/to/my/mp3/greeting.mp3, *:open /path/to/my/mp3/generic.mp3"
    exec="OFF:ssh user@host shutdown -p now"
    exec="OFF:some command, ON:'some other\, more \'complex\' \\command\\ to execute', *:fallback command"
    exec=">[ON:/bin/sh@@-c@@echo on >> /tmp/sw_log] >[OFF:/bin/sh@@-c@@echo off >> /tmp/sw_log]"
    exec=">[1:open /mp3/gong.mp3] >[2:open /mp3/greeting.mp3] >[*:open /mp3/generic.mp3]"
    exec="<[curl -s http://weather.yahooapis.com/forecastrss?w=566473&u=c:60000:XSLT(demo_yahoo_weather.xsl)]"
    exec="<[/bin/sh@@-c@@uptime | awk '{ print $10 }':60000:REGEX((.*?))]"
    exec="<[execute.bat %1$tY-%1$tm-%1$td %2$s %3$s:60000:REGEX((.*?))]"
    exec="<[php ./configurations/scripts/script.php:60000:REGEX((.*?))]"
    exec="<[/bin/sh@@-c@@uptime | awk '{ print $10 }':]"

As a result, your lines in the items file might look like the following:

    Switch Network_NAS	"NAS"	(Network, Status)	{ nh="192.168.1.100", exec="OFF:ssh user@host shutdown -p now"}

or like this:

    Switch Network_NAS	"NAS"	(Network, Status)	{ nh="192.168.1.100", knx="<2/0/0", exec="OFF:ssh user@host shutdown -p now"}

The example above combines three bindings to incorporate the following behavior: query the current state of the NAS with the given IP address. When receiving an OFF command over KNX or the user switches to OFF manually then send the given command line via the exec binding.