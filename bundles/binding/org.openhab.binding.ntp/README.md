Documentation of the NTP binding Bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a NTP query, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the NTP binding configuration string is explained here:

    ntp="[timeZone][:locale]"

where the parts in `[]` are optional. If no or an incorrect time zone is configured it defaults to `TimeZone.getDefault()`. If no or an incorrect Locale is configured it defaults to `Locale.getDefault()`.

Here are some examples of valid binding configuration strings:

    ntp="America/Detroit:en_US"
    ntp="Europe/Berlin:de_DE"
    ntp="Europe/Berlin"
    ntp=""

As a result, your lines in the items file might look like the following:

    DateTime    Date    "Date and Time: [%1$tA, %1$td.%1$tm.%1$tY %1$tT]"  (Status)   { ntp="Europe/Berlin:de_DE" } 

If you like to post the queried time to the knx-bus your line might look like:

    DateTime    Date    "Date and Time: [%1$tA, %1$td.%1$tm.%1$tY %1$tT]"  (Status)    { ntp="Europe/Berlin:de_DE", knx="11.001:15/7/2, 10.001:15/7/1" } 
where 11.001 is the KNX date type and 10.001 is the KNX time type

## Formatting

[Here](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) is an overview of how you can format the output of date and time.

## Settings

Via ```openhab.cfg``` following NTP Binding settings can be set:

 - ```ntp:refresh``` default: 15 minutes
 - ```ntp:hostname``` default: ptbtime1.ptb.de

Note: Via openHAB console a time update can be forced using ```update org.openhab.binding.ntp```.

## Example configurations

### Simple Example 1
This will use your system information to determine timezone and locale.

openhab.cfg

    ntp:hostname=0.us.pool.ntp.org

Items:

    DateTime	Date	"Date [%1$tA, %1$td.%1$tm.%1$tY]"	(Status)	{ ntp="" }

Sitemap:

    sitemap example
    {
        Text item=Date
    }


### Other Examples
Example configurations can be found [here](https://github.com/openhab/openhab/wiki/Samples-Binding-Config).