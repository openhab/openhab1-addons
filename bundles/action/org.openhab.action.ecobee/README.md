# Ecobee Actions

The Ecobee Action bundle provides actions such as setting and clearing program holds, sending a text message to the thermostat's display, renaming a remote wireless sensor, and other functions that cannot be performed by setting object properties.  

## Prerequisites

The Ecboee Action bundle requires that the Ecobee Binding (1.x) is also installed.
Read the [Ecobee function API documentation](https://www.ecobee.com/home/developer/api/documentation/v1/functions/using-functions.shtml) to be sure you know the rules for calling these actions.

## Actions

- ecobeeAcknowledge(String selection, String thermostatIdentifier, String ackRef, String ackType, *Boolean remindMeLater*) - Acknowledge an alert.
- ecobeeControlPlug(String selection, String plugName, String plugState, *Date startDateTime*, *Date endDateTime*, *String holdType*, *Number holdHours*) - Control the on/off state of a plug by setting a hold on the plug.
- ecobeeCreateVacation(String selection, String name, Number coolHoldTemp, Number heatHoldTemp, *Date startDateTime*, *Date endDateTime*, *String fan*, *Number fanMinOnTime*) - Create a vacation event on the thermostat.
- ecobeeDeleteVacation(String selection, String name) - Delete a vacation event from a thermostat.
- ecobeeResetPreferences(String selection) - Set all user configurable settings back to the factory default values.
- ecobeeResumeProgram(String selection, *Boolean resumeAll*) - Remove the currently running event providing the event is not a mandatory demand response event.
- ecobeeSendMessage(String selection, String text) - Send an alert message to the thermostat.
- ecobeeSetHold(String selection, *Number coolHoldTemp*, *Number heatHoldTemp*, *String holdClimateRef*, *Date startDateTime*, *Date endDateTime*, *String holdType*, *Number holdHours*) - Set the thermostat into a hold with the specified temperature.
- ecobeeSetHold(String selection, `Map<String, Object>` params, *Date startDateTime*, *Date endDateTime*, *String holdType*, *Number holdHours*) - Set the thermostat into a hold with the specified event params.
- ecobeeSetOccupied(String selection, Boolean occupied, *Date startDateTime*, *Date endDateTime*, *String holdType*, *Number holdHours*) - Switches a (EMS model only) thermostat from occupied mode to unoccupied, or vice versa.
- ecobeeUpdateSensor(String selection, String name, String deviceId, String sensorId) - Update the name of an ecobee3 remote sensor.

Parameters in *italics* are optional, in which case each unused parameter must be replaced with `null`.  

The `selection` parameter is a string that identifies the thermostat(s) against which the action is performed, identical in format to `<thermostat>` used in the Ecobee binding (1.x).
