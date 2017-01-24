Documentation of the NeoHub binding Bundle

## Introduction

The NeoHub binding allows you to connect openhab via TCP/IP to Heatmiser's NeoHub and integrate your NeoStat thermostats onto the bus. 

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration
The binding polls each thermostat that is configured in your items, at the _refresh_ rate (as set in the cfg file - see below). 

Refer to the example below to see what you need to include in your openhab.cfg file:

    ################################ NeoHub Binding #####################################
    #
    # Refresh interval in milliseconds (optional, defaults to 60000ms)
    #neohub:refresh=60000
    
    # Set the NeoHub network address
    #neohub:hostname=
 
    # Set the port number for the NeoHub interface (optional, defaults to 4242)
    #neohub:port=4242
    

## Item Binding Configuration
Item strings simply consist of two components - the NeoStat device name (as set in the thermostat and in the mobile app, e.g. Office), and the NeoStat property (e.g. CurrentTemperature). Both parameters are only separated by a colon. Some examples are below.

    Switch Heating_Office       "Heating Office"         {neohub="Office:Heating"}
    Switch Away_Office          "Away Office"            {neohub="Office:Away"}
    Switch Standby_Office       "Standby Office"         {neohub="Office:Standby"}
    Number Temperature_Office   "Temperature [%.1f °C]"  {neohub="Office:CurrentTemperature"}

The following properties, and their associated item types are shown below. The R and RW in the description column indicate which properties are read only (RO) or read/write (RW).

| Property     | Type Supported   | Description     |
| ------------- | ---------------- | --------------- |
| CurrentTemperature       | Number | RO Current room temperature |
| CurrentSetTemperature       | Number | RO Current set temperature |
| CurrentFloorTemperature     | Number | RO Current floor temperature |
| DeviceName      | String   | RO  Returns the device name |
| Heating     | Switch, String   | RO Returns ON or OFF if heating is currently enabled or not |
| Away      | Switch, String | RW Returns ON or OFF if thermostat is in away mode or not |
| Standby   | Switch, String | RW Returns ON or OFF if thermostat is in standby mode or not |