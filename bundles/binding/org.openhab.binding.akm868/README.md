# AKM868 Binding

This binding is for users coming from the proprietary homeautomation-system "IP-Symcon". If you have bought some hardware from them, you can use this binding to enable your AKM868 presence detection system.

The following hardware was used to do presence detection with the AKM-868 controller and a OVO868-tracker for your key-ring:

![AKM-Module](https://raw.githubusercontent.com/openhab/openhab1-addons/master/bundles/binding/org.openhab.binding.akm868/src/main/resources/AKM868.png) 
![LAN-T Module](https://raw.githubusercontent.com/openhab/openhab1-addons/master/bundles/binding/org.openhab.binding.akm868/src/main/resources/LAN-T868.png)
![868xOVO Tracker](https://raw.githubusercontent.com/openhab/openhab1-addons/master/bundles/binding/org.openhab.binding.akm868/src/main/resources/Tracker-868xOVO.png)

## Binding Configuration

The binding can be configured in the file `services/akm868.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| host     |         |   Yes    | IP address of the adapter LAN-T 868 |
| port     | 10001   |   No     | Port of the adapter LAN-T 868 |
| timeout  |         |          | Timeout in milliseconds. If the AKM-Controller does not send an update within that timeframe, the switch item is changing to OFF |

## Item  Configuration

The syntax of the binding configuration strings accepted is the following:

```
{ akm868="id=x,channel=y" }
```

The `id` represents the ID of your OVO-Tracker.

The `channel` can be one of the following values:

| channel | meaning |
|---------|---------|
| 0 | notify on pings from the tracker |
| 1 | when the button of the tracker was pressed |
| 5 | when the button of the tracker was pressed longer |


## Examples

### Items

```
Switch PresenceMichael "Key Michael" <present> {akm868="id=9999,channel=0"}
```

This item would turn the Switch to `ON` as soon as openHAB receives a PING from the tracker. It will turn the Switch to `OFF` if the tracker doesn't send another PING within the given timeframe (`timeout` value from the configuration).

```
Switch KeyPressedShortMichael "Key Michael" <present> {akm868="id=9999,channel=1"}
```

This item would turn the Switch to `ON` every time the key was pressed.

```
Switch KeyPressedLongeMichael "Key Michael" <present> {akm868="id=9999,channel=5"}
```

This item would turn the Switch to **ON** every time the key was pressed for a longer time.


### Rules


```
rule "Turn off WIFI if not at Home"
when 
  Item PresenceMichael changed to OFF	  
then 
  logInfo("Wifi","Wifi OFF") 
  sendCommand(Power_Upstairs_Wifi, OFF)
end
```

## Limitations

You must use a LANT-868 Adapter. The USB868-Adapter doesn't work.  However, if you own a USB adapter, there is a workaround utilizing the Serial binding (1.x).

Once the Serial binding is installed, following items are needed for the workaround:

```
String	 Presence_AKM_ComPort		"Last String from AKM [%s]" 	{ serial="/dev/ttyUSB1" } //check USB port is matching to your system configuration
DateTime Presence_AKM1_LastUpdate	"Key 1: Last Update:  [%1$td.%1$tm.%1$tY %1$tT]"
String   Presence_AKM1_Action 		"Key 1: Last Action:  [%s]"
Switch	 Presence_AKM				"Presence AKM"	<contact>
```

This rule handles an incoming event form the keyfob:

```
rule "Presence AKM Direct"
  when Item Presence_AKM_ComPort received update 
then
  var String[] buffer= Presence_AKM_ComPort.state.toString.split(",")
  // var String id = buffer.get(2) // this is the ID of the keyfob. rule could be enhanced to distinguish several keyfobs
  var String action = buffer.get(3)
  var String packetValid = buffer.get(4)	

  if (packetValid.contains("OK")) {
    postUpdate(Presence_AKM1_LastUpdate, new DateTimeType())
    Presence_AKM.sendCommand("ON") 
    switch (action) {
      case '0' : Presence_AKM1_Action.sendCommand("Ping")
      case '1' : Presence_AKM1_Action.sendCommand("PressedShort")
      case '5' : Presence_AKM1_Action.sendCommand("PressedLong")
    }
  }
end
```

A second rule is needed to periodically check if the there was an update from the keyfob. If there was no update in the last 100 seconds, the assumption is that the fob is out of range.

```
rule "Presence AKM reset"
when 
  Time cron "0 0/1 * * * ?"
then
  if (Presence_AKM.state == ON) {
    var DateTimeType l_akm1 = Presence_AKM1_LastUpdate.state as DateTimeType			
    if ((new DateTimeType().calendar.timeInMillis - l_akm1.calendar.timeInMillis) > 100000){  // 100 sec timeout
      Presence_AKM1_Action.sendCommand("Away")
      Presence_AKM.sendCommand("OFF")
    }
  }
end
```
