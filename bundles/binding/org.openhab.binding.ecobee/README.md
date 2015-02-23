org.openhab.binding.ecobee
==========================

openHAB Ecobee Binding

Installation
------------

* Add an appkey and scope to openhab.cfg

```
ecobee:refresh=60000
ecobee:appkey=ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHI
ecobee:scope=smartWrite
```

* Login to your Ecobee portal and navigate to My Apps

* Install the binding bundle by copying the appropriate JAR file to your addons directory.

* Monitor the openhab.log for INFO messages from this binding that instruct you to enter 
  the supplied four-letter PIN into the My Apps, and follow those instructions.

* Configure items and rules.  Examples:

```
Number Ecobee_Name "Room [%s]" {ecobee="<[123456789#name]"
```
Return the name of the thermostat whose ID is 123456789 using the default Ecobee app instance (configured in openhab.cfg).

```
Number Dining_Humidity "Humidity [%d %%]" {ecobee="<[123456789#runtime.actualHumidity]"}
```
Example item for retrieving the relative humidity.

```
Number Condo_Temperature "Condo Temperature [%d ºF]" {ecobee="<[condo.987654321#runtime.actualTemperature]"}
```
Return the current temperature read by the thermostat using the condo account at ecobee.com.  If you want to instead have Celsius temperatures reported, add the setting `ecobee:tempscale=C` to your openhab.cfg.

```
Number Dining_FanMinOnTime "Fan Min On Time [%d min/hour]" {ecobee="=[543212345#settings.fanMinOnTime]"}
```
Set the minimum number of minutes per hour the fan will run on thermostat ID 543212345.

```
String All_HVAC_Modes "[%s]" {ecobee=">[*#settings.hvacMode]"}
```
Change the HVAC mode to one of `"auto"`, `"auxHeatOnly"`, `"cool"`, `"heat"`, or
`"off"` on all thermostats registered in the default app instance.

```
Number Lakehouse_Backlights "[%d]" {ecobee=">[lakehouse.*#settings.backlightSleepIntensity]"}
```
Changes the backlight sleep intensity on all thermostats at the lake house (meaning, all thermostats registered to the lakehouse Ecobee account).

Supported are a long list of runtime and configuration values (see below).

Example rule to send a mail if humidity reaches a certain threshold:
```
var boolean humHighWarning = false
var boolean humVeryHighWarning = false

rule "Monitor humidity level"
	when
		Item Dining_Humidity changed
	then
		if(Dining_Humidity.state > 60) {
			if(humHighWarning == false) {
				sendMail("example@example.com",
				         "High humidity level!",
				         "Humidity level is " + Dining_Humidity.state + " %%.")
				humHighWarning = true
			}
		} else if(Dining_Humidity.state > 75) {
			if(humVeryHighWarning == false) {
				sendMail("example@example.com",
				         "Very high humidity level!",
				         "Humidity level is " + Dining_Humidity.state + " %%.")
				humVeryHighWarning = true
			}
		} else {
			humHighWarning = false
			humVeryHighWarning = false
		}
end
```

TBD
