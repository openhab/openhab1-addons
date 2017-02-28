# IEC 62056-21 Meter Binding

This binding is used to communicate to metering devices supporting serial communication according IEC 62056-21 mode C master. It can be used to read metering data from slaves such as gas, water, heat, or electricity meters. 

For further information read Wiki page of [IEC 62056-21](http://en.wikipedia.org/wiki/IEC_62056#IEC_62056-21).

Information received from the meter device are structured according IEC 62056-6-1:2013, Object identification system (OBIS). For further information read Wiki page of [OBIS ("Object Identification System")](http://de.wikipedia.org/wiki/OBIS-Kennzahlen).

## Binding Configuration

This binding can be configured in the file `services/iec6205621meter.cfg`.  The configuration allows the definition of multiple meter devices.


| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<meter-id1>`.serialPort | | Yes | the serial port to use for connecting to the metering device e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux |
| `<meter-id1>`.baudRateChangeDelay | 0 | No | Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms |
| `<meter-id1>`.echoHandling | true | No | Enable handling of echos caused by some optical tranceivers |
| refresh | 600 | No | Perform a module status query every `refresh` seconds |

NOTE: the `<meter-id1>` will be used in both the binding item configs and the action calls to defined which of your meter devices is connected to the item.

## Item Configuration

The syntax of an item configuration is shown in the following line in general:

```
iec6205621meter="<meter-id>;<OBIS>"
```

Where `<meter-id>` matches one of the IDs defined in your `services/iec6205621meter.cfg` file.

If you do not know the available OBIS on your meter device, you can probably find them on the local HMI of you meter device. Please review you manual of the meter device or read the instruction of your utility.

You can also start openHAB in debug mode, the binding will then dump all available OBIS it receives from the meter device in the osgi console. 

### Examples

#### Items

Below is an example representing the current energy counter tarif values as numeric items.

```
/* IEC 62056-21 Meter data */
Number Tarif_Period "Chart Period"
Number Tarif1 "High price tarif [%d kwh]" (gEnergy) { iec6205621meter="meter1;1.8.1" }
Number Tarif2 "Low price tarif [%d kwh]" (gEnergy) { iec6205621meter="meter1;1.8.2" }
Number ActualEnergyConsumption "Actual energy consumption [%.2f KW]" (gEnergy) { iec6205621meter="meter1;16.7" }
```

#### Rules

In order to calculate the daily accumulated energy consumption, below item and script will allow you to present a chart in your sitemap.

First create the necessary items: 

```
Number Tarif1Today  "Daily Consumption High Price" (gEnergieConsumption)
Number Tarif2Today  "Daily Consumption Low Price" (gEnergieConsumption)
```

Add the following to your rules:

```
rule "Energy chart"
when
    Item Tarif1 changed or
    Item Tarif2 changed
then
    var Number tmp
    var Number stopTarif1
    var Number stopTarif2
    var Number startTarif1
    var Number startTarif2
        
    if (Tarif1.state instanceof DecimalType && Tarif2.state instanceof DecimalType) {   
        stopTarif1  = Tarif1.maximumSince(now.toDateMidnight).state as DecimalType
        stopTarif2  = Tarif2.maximumSince(now.toDateMidnight).state as DecimalType
        
            //Today
                startTarif1 = Tarif1.minimumSince(now.toDateMidnight).state as DecimalType
                startTarif2 = Tarif2.minimumSince(now.toDateMidnight).state as DecimalType
                
                tmp = stopTarif1 - startTarif1
                postUpdate(Tarif1Today, tmp)
        
                tmp = stopTarif2 - startTarif2
                postUpdate(Tarif2Today, tmp)
    }
end
```

Next, add below Frame to your sitemap:

```
Frame label="Energie" {
    Text item=Tarif1 icon="energy" {
        Frame {
            Text item=Tarif1 
            Text item=Tarif2
            Chart item=gEnergieConsumption period=D refresh=600 
            Chart item=gEnergieConsumption period=W refresh=3600    
        }
    Text item=ActualEnergyConsumption icon="energy" {
        Frame {
                          Text item=ActualEnergyConsumption
                          Switch item=ActualEnergyConsumption_Chart_Perdiod label="Period" mappings=[0="Hour", 1="Day", 2="Week"]
                          Chart item=ActualEnergyConsumption  period=h visibility=[ActualEnergyConsumption_Period==0, ActualUsage_Chart_Period==Uninitialized ]
                          Chart item=ActualEnergyConsumption  period=D visibility=[ActualEnergyConsumption_Chart_Period==1]
                          Chart item=ActualEnergyConsumption  period=W visibility=[ActualEnergyConsumption_Chart_Period==2]
        }
    }   
}
```

## Tested Hardware

The binding has been successfully tested with below hardware configuration:

* Landis & Gyr meter [ZMD120AR](http://www.landisgyr.ch/product/landisgyr-zmd120ar/)  connected with [IR-Reader RS232](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf) from open hardware project in [volkszaehler](http://volkszaehler.org/)
* Landis & Gyr meter [E350](http://www.landisgyr.ch/product/landisgyr-e350-electricity-meter-new-generation/)  connected with [IR-Reader RS232](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf) from open hardware project in [volkszaehler](http://volkszaehler.org/)
* Landis & Gyr meter connected the [IR-Reader USB](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf-usb-ausgang) from open hardware project in [volkszaehler](http://volkszaehler.org/)
