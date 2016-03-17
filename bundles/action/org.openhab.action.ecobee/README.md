# Documentation for the Ecobee Action Bundle

## Introduction
This bundle exposes openHAB Rule extensions to be used with the [Ecobee Binding](https://github.com/openhab/openhab/wiki/Ecobee-Binding).

It exposes the ability to call [Ecobee API functions](https://www.ecobee.com/home/developer/api/documentation/v1/functions/using-functions.shtml) from within [openHAB Rules](https://github.com/openhab/openhab/wiki/Rules).

The Ecobee Action bundle depends upon the installation of the Ecobee Binding, and need only be installed if the target deployment uses Ecobee Action extensions in its Rules.

## Configuration
The Ecobee Action bundle relies on the Ecobee Binding being installed and configured, and the installation of the Ecobee Action Bundle (JAR) file.  Once these are done, you're ready to use the Rule extensions this bundle provides.

## Extensions
Parameters in _italics_ are optional, in which case each unused parameter must be replaced with `null`.  Read the [API documentation](https://www.ecobee.com/home/developer/api/documentation/v1/functions/using-functions.shtml) to be sure you know the rules for calling these functions.

Each action's first parameter is an `Item` that is bound to the Ecobee binding, and references the thermostat(s) your action is to target.  The item's Ecobee binding string could be setting or retrieving any thermostat property; what's important is the reference to the thermostat(s) you want to affect.

* ecobeeAcknowledge(Item item, String thermostatIdentifier, String ackRef, String ackType, _Boolean remindMeLater_) - Acknowledge an alert.
* ecobeeControlPlug(Item item, String plugName, String plugState, _Date startDateTime_, _Date endDateTime_, _String holdType_, _Integer holdHours_) - Control the on/off state of a plug by setting a hold on the plug.
* ecobeeCreateVacation(Item item, String name, DecimalType coolHoldTemp, DecimalType heatHoldTemp, _Date startDateTime_, _Date endDateTime_, _String fan_, _Integer fanMinOnTime_) - Create a vacation event on the thermostat.
* ecobeeDeleteVacation(Item item, String name) - Delete a vacation event from a thermostat.
* ecobeeResetPreferences(Item item) - Set all user configurable settings back to the factory default values.
* ecobeeResumeProgram(Item item, _Boolean resumeAll_) - Remove the currently running event providing the event is not a mandatory demand response event.
* ecobeeSendMessage(Item item, String text) - Send an alert message to the thermostat.
* ecobeeSetHold(Item item, _DecimalType coolHoldTemp_, _DecimalType heatHoldTemp_, _String holdClimateRef_, _Date startDateTime_, _Date endDateTime_, _String holdType_, _Integer holdHours_) - Set the thermostat into a hold with the specified temperature.
* ecobeeSetOccupied(Item item, Boolean occupied, _Date startDateTime_, _Date endDateTime_, _String holdType_, _Integer holdHours_) - Switches a (EMS model only) thermostat from occupied mode to unoccupied, or vice versa.
* ecobeeUpdateSensor(Item item, String name, String deviceId, String sensorId) - Update the name of an ecobee3 remote sensor.

## Examples
A wiki page showing more examples for how to use the Ecobee binding and action bundles is forthcoming.

* Switch to `away` comfort setting when a door or window is opened and put a message on the thermostat to close the windows and doors.

```
rule "Heat and cool indoors only"
when 
    Item Bedroom2ZoneTripped changed from CLOSED to OPEN or
    Item Bedroom3ZoneTripped changed from CLOSED to OPEN
then
    if (GWindowsDoors.members.filter(s|s.state==OPEN).size == 1) {
        ecobeeSetHold(SomeEcobeeItem, null, null, "away", null, null, null, null)
        ecobeeSendMessage(SomeEcobeeItem, "Close the windows and doors!")
    }
end
```

## Change Log

* 1.8.0 - First release

