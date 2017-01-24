Documentation of the Heatmiser binding Bundle

## Introduction

The Heatmiser binding allows you to control Heatmiser RS-422 network thermostats (also sold under other names). The binding communicates via TCP/IP to an RS-422 adaptor that links to the thermostats. 

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration
The binding polls each thermostat that is configured in your items, at the _refresh_ rate (as set in the cfg file - see below). It automatically detects the type of thermostats - this is not set in the items.

Refer to the example below to see what you need to include in your openhab.cfg file:

    ################################ Heatmiser Binding #####################################
    #
    # refresh interval in milliseconds (optional, defaults to 2000ms)
    heatmiser:refresh
    
    # Set the IP address for the Heatmiser interface
    heatmiser:address=192.168.2.45
    
    # Set the port number for the Heatmiser interface
    heatmiser:port=1024
    

## Item Binding Configuration
Item strings simply consist of two components - the thermostat address (as set in the thermostat), and the parameter within the thermostat. Some examples are below.

    Switch	HallThermostat_Enable	"Hall Heating Enable  [%s]"	{ heatmiser="1:ONOFF" }
    Number	HallThermostat_RoomTemp	"Hall Heating Temperature [%.1f °C]"	{ heatmiser="1:ROOMTEMP" }
    Number	HallThermostat_SetTemp	"Hall Heating Setpoint    [%.1f °C]"	{ heatmiser="1:SETTEMP" }
    String	HallThermostat_HeatState	"Hall Heating State [%s]"	{ heatmiser="1:HEATSTATE" }
    Switch	HallThermostat_HWEnable	"Hot Water Heating Enable  [%s]"	{ heatmiser="1:WATERSTATE" }

The following parameters, and their associated item types are shown below. The R and RW in the description column indicate which parameters are read only (RO) or read/write (RW).

| Parameter     | Type Supported   | Description     |
| ------------- | ---------------- | --------------- |
| SETTEMP       | Number, String   | RW Temperature control setting |
| RUNMODE       | Number, String   | RW  - |
| FROSTTEMP     | Number, String   | RW Frost temperature |
| ROOMTEMP      | Number, String   | RO Current room temperature |
| FLOORTEMP     | Number, String   | RO Floor temperature |
| HOLDTIME      | Switch, DateTime | RO Current hold time (in minutes) |
| HOLIDAYTIME   | Switch, DateTime | RO Holiday time remaining (in days) |
| HOLIDAYSET    | Number, Switch   | RW Sets the holiday time (in days) |
| HOLDMODE      | Switch           | RO Returns ON or OFF if the thermostat is in hold mode |
| HOLIDAYMODE   | Switch           | RO Returns ON or OFF if the thermostat is currently in holiday mode |
| WATERSTATE    | Switch, String   | RW Returns ON or OFF if the hot water is currently heating | 
| HEATSTATE     | Switch, String   | RO Returns ON or OFF if the thermostat is currently heating |
| STATE         | Switch, String   | RO This is a consolidated thermostat state and maybe one of the following - OFF, ON, HOLD, HOLIDAY |
| ONOFF         | Switch, String   | RW Returns ON or OFF if the thermostat is enabled or not |


## Example Sitemap
Below is an example sitemap for a Heatmiser thermostat. This includes conditional visibility of items dependant on the thermostat state.

    Frame label="Lounge Thermostat" {
        Switch item=LoungeThermostat_Enable label="Lounge Thermostat Enable" 			
        Text item=LoungeThermostat_HeatState visibility=[LoungeThermostat_State==ON,LoungeThermostat_State==HOLD]
        Text item=LoungeThermostat_HolidayTime visibility=[LoungeThermostat_State==HOLIDAY]
        Setpoint item=LoungeThermostat_FrostTemp label="Lounge Frost Temperature [%d °C]" minValue=10 maxValue=18 step=1 visibility=[LoungeThermostat_State==HOLIDAY]
        Setpoint item=LoungeThermostat_SetTemp label="Lounge Setpoint [%d °C]" minValue=14 maxValue=25 step=1 visibility=[LoungeThermostat_State==ON,LoungeThermostat_State==HOLD]
        Text item=LoungeThermostat_FloorTemp valuecolor=[>28="red",>26.5="orange",>18="green", >16="orange", <=16="red"] 
        Text item=LoungeThermostat_RoomTemp  valuecolor=[>28="red",>26.5="orange",>18="green", >16="orange", <=16="red"]
        Setpoint item=LoungeThermostat_HolidaySet label="Lounge heating holiday days [%d]" minValue=0 maxValue=30 step=1
    }```
WIP to be completed - WIP to be completed - WIP to be completed
```

This Section describes how to use AT Heatmiser Wifi Project together with JSONPath and execute commands and actions to completely control an Heatmiser Thermostat.

This is not a binding but is a a set of items/sitemap/rules file that use per Perl development by A. Thoukydides that can be found at https://github.com/thoukydides/heatmiser-wifi

As a first prerequisite please follow the instructions found at https://github.com/thoukydides/heatmiser-wifi/wiki
As far as Perl is concerned I installed the full CPan not CPanminus (It might work also with cpanminus).

You just need the perl script interacting with Json for the sake of OpenHab Integration, so Installation of the WebServer and MySql is optional, but once you are there it is a nice addon for your olf Heatmiser...

Be advised that installation of the prerequisite will take a rather long time. 
Leave all parameters default. 
My installation runs with PI user modify that with your user if needed

Finally:
integrate the following to your sitemap/items/rules.

Please adapt the icon names to the ones you want to use.

Please bear in mind that this is still largely WIP but is published with the intent to finish it with help of the community as I have not much  time to dedicate to it. So... feel free to complete/make it better.

Copy the perl script created by the above setup from cgi bin directory to /home/pi/heatmiser-wifi-read-only/bin/

```
#!/usr/bin/perl
# This script provides a JSON interface to access the iPhone interface of
# Heatmiser's range of Wi-Fi enabled thermostats from languages other than
# Perl.

# Copyright © 2013 Alexander Thoukydides
#
# This file is part of the Heatmiser Wi-Fi project.
# <http://code.google.com/p/heatmiser-wifi/>
#
# Heatmiser Wi-Fi is free software: you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation, either version 3 of the License, or (at your option)
# any later version.
#
# Heatmiser Wi-Fi is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
# more details.
#
# You should have received a copy of the GNU General Public License
# along with Heatmiser Wi-Fi. If not, see <http://www.gnu.org/licenses/>.


# Catch errors quickly
use strict;
use warnings;

# Allow use of modules in the same directory
use Cwd 'abs_path';
use File::Basename;
use lib dirname(abs_path $0);

# Useful libraries
use Getopt::Std;
use JSON;
use heatmiser_config;
use heatmiser_wifi;

# Command line options
my ($prog) = $0 =~ /([^\\\/]+$)/;
sub VERSION_MESSAGE { print "Heatmiser Wi-Fi Thermostat JSON Interface v1\n"; }
sub HELP_MESSAGE { print "Usage: $prog [-h <host>] [-p <pin>] [JSON DCB]\n"; }
$Getopt::Std::STANDARD_HELP_VERSION = 1;
our ($opt_h, $opt_p);
getopts('h:p:');
heatmiser_config::set(host => [h => $opt_h], pin => [p => $opt_p]);
my $items;
$items = decode_json(join(' ', @ARGV)) if @ARGV;

# Loop through all configured hosts
my (%status);
foreach my $host (@{heatmiser_config::get_item('host')})
{
    # Read the current status of the thermostat
    my $heatmiser = new heatmiser_wifi(host => $host,
                                       heatmiser_config::get(qw(pin)));
    my @pre_dcb = $heatmiser->read_dcb();
    my $status = $heatmiser->dcb_to_status(@pre_dcb);

    # Write any specified items to the thermostat
    if ($items)
    {
        my @items = $heatmiser->status_to_dcb($status, %$items);
        my @post_dcb = $heatmiser->write_dcb(@items);
        $status = $heatmiser->dcb_to_status(@post_dcb);
    }

    # Store the decoded status
    $status{$host} = $status;
}

# Output the status in JSON format
print qq(Content-type: text/plain\n\n);
print JSON->new->utf8->pretty->canonical->encode(\%status);

# That's all folks
exit;
```
I reported this file since I am not sure if I did some very minor modification to the script created by the above installation: check and compare.

Unix Bash Oneliners Scripts:

Create the folder /home/pi/heatmiser-wifi-read-only/openhabscripts/
and put this scripts in the folder. Make sure theyare executable by the openhab user.

hw_keylock.sh

```/home/pi/heatmiser-wifi-read-only/bin/heatmiser_json.pl {\"keylock\":$1}```

hw_setback.sh
```
/home/pi/heatmiser-wifi-read-only/bin/heatmiser_json.pl  {\"enabled\": $1 }
```
hw_settemp.sh
```
a='{"heating":{"target":'
b='}}'
echo $a$1$b
/home/pi/heatmiser-wifi-read-only/bin/heatmiser_json.pl $a$1$b
```

hw_status.sh
```
/home/pi/heatmiser-wifi-read-only/bin/heatmiser_json.pl 
```

Sitemap:
```
sitemap heatmiserwifi label="Heatmiser Wifi"
{
    Frame label="Heatmiser Wifi"  
    {
        Group label="Heating" icon="heatinghm" 
        {
            Text item=HW_TemperatureInternal label="Room Temperature [%s C]" icon="thermometer"
            Setpoint item=HW_HeatingTarget label="Set Temperature [%d C]" icon="thermometer" step=1                   
            Switch item=HW_Keylock label="Lock" icon="lockclosed"
            Switch item=HW_HeatingEnabled label="Disable Heating" icon="snowflake"
            Group label="Set Heating Times" icon="year" 
            {
                Group  label="Weekdays" icon="weekdays" 
                {
                    Frame label="Weekdays Schedules"
                    {
                        Frame label="Wake" 
                        {
                            Setpoint icon="thermometer" label="Temperature [%d]" item=HW_WWDTemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%d]" item=HW_WWDHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%d]" item=HW_WWDMinute minValue=0 maxValue=60 step=30
	                }
                        Frame label="Leave"
                        {
                            Setpoint icon="thermometer" label="Temperature [%d]" item=HW_LWDTemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%d]" item=HW_LWDHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%d]" item=HW_LWDMinute minValue=0 maxValue=60 step=30
                        }
                        Frame label="Return"
                        {
                            Setpoint icon="thermometer" label="Temperature [%s]" item=HW_RWDTemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%s]" item=HW_RWDHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%s]" item=HW_RWDMinute minValue=0 maxValue=60 step=30
                        }
                        Frame label="Sleep"
                        {
                            Setpoint icon="thermometer" label="Temperature [%s]" item=HW_SWDTemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%s]" item=HW_SWDHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%s]" item=HW_SWDMinute minValue=0 maxValue=60 step=30
                        }
	            }
                }
	        Group label="Weekend" icon="weekend" 
                {
                    Frame label="Weekend Schedules"
                    {
                        Frame label="Wake"
                        {
                            Setpoint icon="thermometer" label="Temperature [%s]" item=HW_WWETemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%s]" item=HW_WWEHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%s]" item=HW_WWEMinute minValue=0 maxValue=60 step=30
                        }
                        Frame label="Sleep"
                        {
                            Setpoint icon="thermometer" label="Temperature [%s]" item=HW_SWETemp minValue=0 maxValue=50 step=1
                            Setpoint icon="clock" label="Hour [%s]" item=HW_SWEHour minValue=0 maxValue=24 step=1
                            Setpoint icon="clock" label="Minute [%s]" item=HW_SWEMinute minValue=0 maxValue=60 step=30
                        }
	            }
                }
            }
	    Group label="Temperature Hold" icon="weekdays" 
            {
                Setpoint item=HW_TempHoldDuration minValue=0 maxValue=55 step=5
                Setpoint item=HW_TempHoldTemperature minValue=0 maxValue=55 step=5
            }
            Group label="Holiday" icon="airplane" 
            {
                Frame label="SetHolidays"
                {
                    Switch item=HW_SetHoliday icon="lockopened"
                    Frame label="Date"
                    {
                        Setpoint item=HW_HolidayDay icon="day" minValue=0 maxValue=55 step=5
                        Setpoint item=HW_HolidayMonth icon="month" minValue=0 maxValue=55 step=5
                        Setpoint item=HW_HolidayYear icon="year" minValue=0 maxValue=55 step=5
                    }
                }
            }
            Group label="Additional Information" icon="airplane" 
            {
                Frame label="Additional Information"
                {
                        
                        Text item=HW_ProductModel label="Model : [%s]" 
                        Text item=HW_ProductVendor label="Vendor [%s]"
                        Text item=HW_ProductVersion label="Version [%s]" 
                        Text item=HW_FrostprotectEnabled label="Frost protection enabled: [%s]" 
                        Text item=HW_FrostprotectTarget label="Frost Protection Target Degrees [%s]"
                }
            }
}
```


Items:
```
Number  HW_TemperatureInternal
Number HW_HeatingTarget 
Switch HW_HeatingEnabled  { exec=">[ON:/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_setback.sh@@0] >[OFF:/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_setback.sh@@1]" }
Switch HW_Keylock   { exec=">[ON:/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_keylock.sh@@1] >[OFF:/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_keylock.sh@@0]" }
Number HW_WWDTemp 
Number HW_WWDHour 
Number HW_WWDMinute 
Number HW_LWDTemp
Number HW_LWDHour
Number HW_LWDMinute
Number HW_RWDTemp
Number HW_RWDHour
Number HW_RWDMinute
Number HW_SWDTemp
Number HW_SWDHour
Number HW_SWDMinute
Number HW_WWETemp
Number HW_WWEHour
Number HW_WWEMinute
Number HW_SWETemp
Number HW_SWEHour
Number HW_SWEMinute
Number HW_TempHoldDuration
Number HW_TempHoldTemperature
Number HW_HolidayEnabled
Number HW_HolidayDay
Number HW_HolidayMonth
Number HW_HolidayYear
String HW_FrostprotectEnabled
String HW_FrostprotectTarget
String HW_ProductModel
String HW_ProductVendor
String HW_ProductVersion

```

Rules:

```
import org.openhab.core.library.types.* 
import org.openhab.core.persistence.*
import org.openhab.model.script.actions.*
import java.util.concurrent.locks.ReentrantLock
import java.lang.Integer
import java.lang.Double
import java.lang.String


var Timer timer1 = null
var java.util.concurrent.locks.ReentrantLock lock1 = new java.util.concurrent.locks.ReentrantLock()
var Number tempint
var String temps
// eofvar String HeatmiserIP="'192.168.2.251'"

rule hm_statusinit

rule "Initialization"
 when 
   System started or
   Time cron "0 5 0 * * ?"
 then
{
    // Get Full State of thermostat periodically, every 5 minutes, and initially at system start

    var String HW_Status = executeCommandLine("/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_status.sh",1000)
    logInfo("Heatmiser", "Retrieved JSON Status " + HW_Status)
   
    // decode and update items for read only or not DCB writable info 

    postUpdate(HW_ProductModel , transform("JSONPATH", "$.['192.168.2.251'].product.model", HW_Status))
    logInfo("Heatmiser", "Product Model :" + HW_ProductModel)

    postUpdate(HW_ProductVersion , transform("JSONPATH", "$.['192.168.2.251'].product.version", HW_Status))
    logInfo("Heatmiser", "Product Version :" + HW_ProductVersion)

    postUpdate(HW_ProductVendor , transform("JSONPATH", "$.['192.168.2.251'].product.vendor", HW_Status))
    logInfo("Heatmiser", "Product Vendor :" + HW_ProductVendor)

    postUpdate(HW_FrostprotectEnabled , transform("JSONPATH", "$.['192.168.2.251'].frostprotect.enabled", HW_Status))
    logInfo("Heatmiser", "Frost protect " + HW_FrostprotectEnabled)
    
    postUpdate(HW_FrostprotectTarget , transform("JSONPATH", "$.['192.168.2.251'].frostprotect.target", HW_Status))
    logInfo("Heatmiser", "Frostprotect Target Temp" + HW_FrostprotectTarget)

    // Decode and update items for read write items

    postUpdate(HW_HeatingTarget ,  transform("JSONPATH", "$.['192.168.2.251'].heating.target", HW_Status))
    logInfo("Heatmiser", "Target Temperature " + HW_HeatingTarget)

    postUpdate(HW_TemperatureInternal , transform("JSONPATH", "$.['192.168.2.251'].temperature.internal", HW_Status))
    logInfo("Heatmiser", "Room Temperature " + HW_TemperatureInternal)

    if( transform("JSONPATH", "$.['192.168.2.251'].enabled", HW_Status) == "1" ){
          postUpdate(HW_HeatingEnabled , OFF)
            }
       else {
          postUpdate(HW_HeatingEnabled , ON)
    }
    logInfo("Heatmiser", "Heating Enabled " + HW_HeatingEnabled)

    if( transform("JSONPATH", "$.['192.168.2.251'].enabled", HW_Status) == "1" ){
          postUpdate(HW_Keylock , OFF)
            }
       else {
          postUpdate(HW_Keylock , ON)
    }
   logInfo("Heatmiser", "Keylock " + HW_Keylock)

   // Now use lambda functions for schedules details 
   
   //First for Weekdays schedules and temperatures

    postUpdate(HW_WWDTemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].target[0]", HW_Status))
    logInfo("Heatmiser", "Wake WeekDays Temperature " + HW_WWDTemp) 
    postUpdate(HW_WWDHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].time[0]", HW_Status).substring(0,2)) 
    logInfo("Heatmiser", "Wake WeekDays Hour " +  HW_WWDHour)
    postUpdate(HW_WWDMinute , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].time[0]", HW_Status).substring(3,5)) 
    logInfo("Heatmiser", "Wake WeekDays Minute " +  HW_WWDMinute)

    postUpdate(HW_LWDTemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].target[1]", HW_Status))
    logInfo("Heatmiser", "Leave WeekDays Temperature " + HW_LWDTemp)
    postUpdate(HW_LWDHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].time[1]", HW_Status))
    logInfo("Heatmiser", "Leave WeekDays Hour " +  HW_LWDHour)

    postUpdate(HW_RWDTemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].target[2]", HW_Status))
    logInfo("Heatmiser", "Return WeekDays Temperature " + HW_RWDTemp)
    postUpdate(HWeRWDHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].time[2]", HW_Status))
    logInfo("Heatmiser", "Return WeekDays Hour " +  HW_RWDHour)

    postUpdate(HW_SWDTemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].target[3]", HW_Status))
    logInfo("Heatmiser", "Sleep WeekDays Temperature " + HW_SWDTemp)
    postUpdate(HW_SWDHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[0].time[3]", HW_Status))
    logInfo("Heatmiser", "Sleep WeekDays Hour " +  HW_SWDHour)

    postUpdate(HW_WWETemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[1].target[0]", HW_Status))
    logInfo("Heatmiser", "Wake WeekEnds Temperature " + HW_WWETemp)
    postUpdate(HW_WWEHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[1].time[0]", HW_Status))
    logInfo("Heatmiser", "Wake WeekEnds Hour " +  HW_WWEHour)

    postUpdate(HW_SWETemp , transform("JSONPATH", "$.['192.168.2.251'].comfort[1].target[1]", HW_Status))
    logInfo("Heatmiser", "Sleep WeekEnds Temperature " + HW_SWETemp)
    postUpdate(HW_SWEHour , transform("JSONPATH", "$.['192.168.2.251'].comfort[1].time[1]", HW_Status))
    logInfo("Heatmiser", "Sleep WeekEnds Hour " +  HW_SWEHour)

 }
end

rule "ChangeSet"
when
    Item HW_SetTemperature  changed 
then
  lock1.lock()
  try {
    logInfo("Heatmiser", "Sending new set temperature : " + HW_SetTemperature)
    var s = "/home/pi/heatmiser-wifi-read-only/openhabscripts/hw_settemp.sh@@"+HW_SetTemperature.state
    logInfo("Heatmiser", "executing : " + s)
    executeCommandLine(s)
  } finally  {
     // release the lock - we are ready to process the next event
     lock1.unlock()
  }
end
```

Please note that here my heatmiser ip (192.168.2.251) is hardcoded but it shall be substituted with a variable.







