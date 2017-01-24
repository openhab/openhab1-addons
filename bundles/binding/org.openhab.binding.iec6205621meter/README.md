Documentation of the IEC 62056-21 meter binding bundle

## Introduction

This binding is used to communicate to metering devices supporting serial communication according IEC 62056-21 mode C master. It can be used to read metering data from slaves such as gas, water, heat, or electricity meters. 
For further information read Wiki page of [IEC 62056-21](http://en.wikipedia.org/wiki/IEC_62056#IEC_62056-21).

Information receive from the meter device are structured according IEC 62056-6-1:2013, Object identification system (OBIS). For further information read Wiki page of [OBIS ("Object Identification System")](http://de.wikipedia.org/wiki/OBIS-Kennzahlen).

For installation of the binding, please see Wiki page [Bindings](https://github.com/openhab/openhab/wiki/Bindings).

## IEC 62056 - 21 Meter Binding Configuration
### openhab.cfg
The openhab.cfg file (in the folder '${openhab_home}/configurations') contain following config parameters  to configure the binding.
The configuration allows the definition of multiple meter devices.

    #########################  IEC 620562-21 Meter Binding ####################
    # the serial port to use for connecting to the metering device e.g. COM1 for Windows and /dev/ttyS0 or
    # /dev/ttyUSB0 for Linux
    iec6205621meter:<meter-id1>.serialPort=/dev/ttyS0
    
    # Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms
    # default is 0ms
    iec6205621meter:<meter-id1>.baudRateChangeDelay=0
    
    # Enable handling of echos caused by some optical tranceivers
    # default is true
    iec6205621meter:<meter-id1>.echoHandling=true
    
    # Perform a module status query every x seconds (optional, defaults to 600 (10 minutes)).
    iec6205621meter:refresh=60

NOTE: the `<meter-id1>` will be used in both the binding item configs and the action calls to defined which of your meter devices is connected to the item.

## Generic Item Binding Configuration

### Item Binding Configuration

In order to bind an item to the data received from the meter device , you need to provide item configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:
The syntax of an item configuration is shown in the following line in general:

    iec6205621meter="<meter-id>;<OBIS>"
Where meter-id matches one of the ids defined in your openhab.cfg file.

If you do not know the available OBIS on your meter device you propably find them on the local HMI of you meter device. Please review you manual of the meter device or read the instruction of your utility.
You can also start openhab in debug mode, the binding will then dump all available OBIS it receives from the meter device in the osgi console. 

### Examples

Below is an example of represent the current energy counter tarif values as numeric items.

    /* IEC 62056-21 Meter data */
    Number Tarif_Period	"Chart Period"
    Number Tarif1 "High price tarif [%d kwh]" (gEnergy) { iec6205621meter="meter1;1.8.1" }
    Number Tarif2 "Low price tarif [%d kwh]" (gEnergy) { iec6205621meter="meter1;1.8.2" }
    Number ActualEnergyConsumption "Actual energy consumption [%.2f KW]" <gEnergy> () { iec6205621meter="meter1;16.7" }

### Rules

In order to calculate the daily accumulated energy consumption below item and script will allow you to present a chart in your sitemap.
First create the necessary items: 

    Number Tarif1Today	"Daily Consumption High Price" (gEnergieConsumption)
    Number Tarif2Today	"Daily Consumption Low Price" (gEnergieConsumption)
 
Second add below script to your rules:

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

Third add below Frame to your sitemap:

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

### Tested Hardware

The binding has been successfully tested with below hardware configuration:
* Landis & Gyr meter [ZMD120AR](http://www.landisgyr.ch/product/landisgyr-zmd120ar/)  connected with [IR-Reader RS232](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf) from open hardware project in [volkszaehler](http://volkszaehler.org/)
* Landis & Gyr meter [E350](http://www.landisgyr.ch/product/landisgyr-e350-electricity-meter-new-generation/)  connected with [IR-Reader RS232](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf) from open hardware project in [volkszaehler](http://volkszaehler.org/)
* Landis & Gyr meter connected the [IR-Reader USB](http://wiki.volkszaehler.org/hardware/controllers/ir-schreib-lesekopf-usb-ausgang) from open hardware project in [volkszaehler](http://volkszaehler.org/)