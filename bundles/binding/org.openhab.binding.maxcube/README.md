# MAX!Cube Binding

The MAX!Cube binding allows openHAB to connect to [(ELV) MAX!Cube Lan Gateway](http://www.elv.de/max-cube-lan-gateway.html) installations. The binding allows openHAB to communicate with the MAX! devices through the MAX!Cube Lan Gateway.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/max/).

## Prerequisites

To communicate with MAX! devices, an already setup MAX! environment including a MAX!Cube Lan Gateway is required. In addition, the binding expects an already set up MAX environment.

## Binding Configuration

The MAX!Cube is automatically discovered from the network, so no configuration is required in basic setting. Otherwise, the binding can be configured in the file `services/maxcube.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       | auto-discovered via DHCP, or 192.168.0.222 | | MAX!Cube LAN gateway IP address |
| port     | 62910   |   No     | MAX!Cube port<br/>should not need to be changed |
| refreshInterval | 10000 | No  | MAX!Cube refresh interval in milliseconds 10000 = 10 seconds |
| exclusive | false  |   No     | Max!Cube exclusive mode.<br/>When `true`, the binding keeps the connection to the Cube open and polls more efficiently. No other application can use the Cube while the binding is running in exclusive mode, including Android and Desktop Max! Software. With this mode, the refreshInterval can easily set to 500 or 1000ms if you want the window contacts or eco button to be more responsive. If you are not using MAX! smartphone or desktop software, this is recommended. Please note that the MAX!Cube desktop software also keeps the connection open and openhab will not be able to connect if the MAX!Cube software is running. Close if from the windows taskbar. |
| maxRequestsPerConnection | 1000 | No |  Max!Cube maximum requests per connection.<br/>In exclusive mode, the binding will open the connection to the Cube and polls with `refreshInterval` `maxRequestsPerConnection` times. When `maxRequestsPerConnection` is reached, it will disconnect and reconnect. This may work around issues with long going connections like slow reaction on events.  When set to 0, the binding will keep the connection open as long as possible. |

## Item Configuration

Items bound to MAX!Cube must at lease contain the serial number of the device you wish to control (not the serial of the cube).

> **How to get the serial number of a device**: you can use the original MAX! software: edit the room, click on structure,  rename devices.  There you'll see the names and serials.

The syntax of the binding configuration strings accepted is the following: 

```
maxcube="<serialNumber>"
```

### Shutter Contact

The state of a shutter contact can be retrieved via the generic item binding. To display the shutter state, you need to use a Contact item.

```
Contact Office_Window "Office Window [MAP(en.map):%s]" (MyGroup) { maxcube="JEQ0650337" }
```

### Heating Thermostat or Wallmounted Thermostat

#### Display the Target Temperature

For a heating thermostat, an identical configuration will provide the setpoint temperature of the heating thermostat (4.5° corresponds to OFF shown on the thermostat display). To show the temperature setpoint you need to use a number item.

```
    Number Heating_Max "Heating Thermostat [%.1f °C]" (MyGroup) { maxcube="JEQ0336148" }
```

The above examples would be shown as 

![MAX! Binding](https://dl.dropboxusercontent.com/u/7347332/web/maxcube.png)

MAX heating thermostat devices show OFF when turned to the minimum or On when turned to the maximum. The openHAB MAX!Cube binding would show the values 4.5 for OFF and 30.5 for On instead. 

If you would like to display OFF and on instead, you can apply a mapping and change the binding using this mapping to 

```
Number Heating_Max "Heating Thermostat [MAP(maxcube.map):%s]" (MyGroup) { maxcube="JEQ0336148" }
```

Instead of values 4.5 and 30.5 the results would look like

![MAX! Binding](https://dl.dropboxusercontent.com/u/7347332/web/max_on_off_small.png)

To apply this mapping you need to copy the [maxcube.map](https://dl.dropboxusercontent.com/u/7347332/web/maxcube.map) mapping file into the configuration/transformation folder within the openHAB directory. (Alternatively you can use this [maxcube.map](https://gist.githubusercontent.com/joek/0df1727580df7f98f1a4/raw/a3c7b1d629bbc29fa87ef5ca5808f4fa1a34a3bc/maxcube.map) file when the mappings of round temperature settings don't show.)

Depending on the correpsonding device the MAX!Cube binding can be used to provide specific information about a device instead of the default information.

#### Setting the Target Temperature

In order to be able to set a thermostat (and thus sending a temperature setting to an individual thermostat) use the Setpoint item in your sitemap configuration:

```
Setpoint item=Heating_Max_Valve step=0.5 minValue=18 maxValue=30
```

This SetPoint item will allow a user to set the thermostat with 0.5 degrees intervals. If you would like to set the thermostat yourself, for instance in a rule, use the sendCommand option in your rules file, like in the following example:

```
rule "Bedtime"
when
   Time cron "0 0 23 * * ?"
then
   sendCommand (Heating_Max_Valve, 15 )
end
```

To receive the valve position of a heating thermostat, the type for the desired information needs to be specified in the bonding configuration.

#### Actual Temperature

Per release 1.6 you can request the actual temperature for the WallThermostat. The actual temperature can also be requested from the heating Thermostats, however is usually outdated for the radiator thermostats, since they only send it over when their valve position changes. For the Wall thermostats, the value is accurate, since those send updates every couple of minutes.

```
Number Heating_Max_Temp "Thermostat Temperature  [%.1f °C]" (MyGroup) { maxcube="JEQ0336148:type=actual" }
```

#### Valve Position

To receive the valve position of a heating thermostat, the type for the desired information needs to be specified in the bonding configuration

```
Number Heating_Max_Valve "Thermostat Valve Position [%.1f %%]" (MyGroup) { maxcube="JEQ0336148:type=valve" }
```

![MAX! Binding Valve Position](https://dl.dropboxusercontent.com/u/7347332/web/max_valve.png)

The value position is transmitted as rarely as the actual temperature. You may have to wait very long until something is displayed.

#### Mode

The operating mode can be requested using the _mode_ type in the corresponding binding configuration.

```
String Heating_Max_Valve_Mode "Thermostat Mode [%s]" (MyGroup) { maxcube="JEQ0336148:type=mode" }
```

The mode is displayed as "AUTOMATIC" for example.

### All devices

The following configuration is available for all MAX! devices.

#### Battery State

The battery state of a device can be requested using the _battery_ type in the corresponding binding configuration. 

```
String Heating_Max_Valve "Thermostat Battery [%s]" (MyGroup) { maxcube="JEQ0336148:type=battery" }
```

![MAX! Binding Battery State](https://dl.dropboxusercontent.com/u/7347332/web/max_battery.png)

String values returned by the binding are either _ok_ or _low_.

#### Connection Error (Since 1.8.0

The connection state between a device and the MAX!Cube can be requested using the _connectionError_ type in the corresponding binding configuration. 

```
Switch Heating_Connection_Error "Thermostat Conn. Error" (MyGroup) { maxcube="JEQ0336148:type=connectionError" }
```

The switch will change to ON if the MAX!Cube reports that it cannot connect (anymore) to the configured device.

## Logging

If you want to have some insights into what actually happens it may be useful to print some log messages.

The logger instance you would want to set to DEBUG or TRACE is `org.openhab.binding.maxcube`.

If you have a lot of devices the log is an alternative way to get the serial numbers of the devices. They are logged when the binding is started.

To log even more information, set the level of `org.openhab.binding.maxcube` from DEBUG to TRACE.

