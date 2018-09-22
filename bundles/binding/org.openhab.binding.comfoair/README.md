# ComfoAir Binding

This binding should be compatible with the Zehnder ComfoAir 350 ventilation system. ComfoAir 550 is untested but should supposedly use the same protocol. The same is true for the device WHR930 of StorkAir, G90-380 by Wernig and Santos 370 DC to Paul.

## Binding Configuration

You can configure this binding in the file `services/comfoair.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| port     |         |   Yes    | Serial port which is connected to the Zehnder ComfoAir system, for example `/dev/ttyS0` on Linux or `COM1` on Windows |
| refresh  | 60000   |   No     | refresh inverval in milliseconds


### Example

```
port=/dev/ttyS0
```

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
comfoair="<device-command>"
```

where `<device-command>` is replaced with the ComfoAir command from the list below:

| `<device-command>`              | Read / Write | Notes |
|---------------------------------|:------------:|-------|
| activate                        | Write        | - 0 means CCEase Comfocontrol is active and the binding is in sleep state<br/>- 1 means Binding is active and CCEase Comfocontrol is in sleep state | 
| fan_level                       | Write        | - 1 is Level A<br/>- 2 is Level 1<br/>- 3 is Level 2<br/>- 4 is Level 3 | 
| target_temperature              | Write        | value between 15.0 and 25.0 in 0.5 steps |
| outdoor_incomming_temperatur    | Read         |  |
| outdoor_outgoing_temperatur     | Read         |  |
| indoor_incomming_temperatur     | Read         |  |
| indoor_outgoing_temperatur      | Read         |  |
| incomming_fan                   | Read         |  |
| outgoing_fan                    | Read         |  |
| filter_running                  | Read         |  |
| filter_error                    | Read         |  |
| filter_error_intern             | Read         |  |
| filter_error_extern             | Read         |  |
| filter_reset                    | Write        | filterhours will be reset to 0 |
| error_message                   | Read         |  |
| ewt_temperatur                  | Read         |  |
| ewt_temperatur_low              | Read         |  |
| ewt_temperatur_high             | Read         |  |
| ewt_speed                       | Read         |  |
| ewt_mode                        | Read         |  |
| bypass_mode                     | Read         |  |
| error_reset                     | Write        | error messages will be reset |

## Limitations

- Either the ComfoAir binding or the CCEase Comfocontrol can be active
- You must implement auto mode by yourself with rules. But it is more powerful.

## Rights to access the serial port

- Take care that the user that runs openHAB has rights to access the serial port
- On Ubuntu systems that usually means adding the user to the group "dialout", i.e. 

```shell
sudo usermod -a -G dialout openhab
```

if `openhab` is your user.

## Examples

### Basic Examples

```
Number airflowControl "Activated" {comfoair="activate"}
Number airflowFanLevel "Level [%d]" {comfoair="fan_level"}
Number airflowTargetTemperature "Target temperature [%.1f °C]" {comfoair="target_temperature"}

Number airflowOutdoorIncomingTemperature "Outdoor incoming [%.1f °C]" {comfoair="outdoor_incomming_temperatur"}
Number airflowOutdoorOutgoingTemperature "Outdoor outgoing [%.1f °C]" {comfoair="outdoor_outgoing_temperatur"}
Number airflowIndoorIncommingTemperature "Indoor incomming [%.1f °C]" {comfoair="indoor_incomming_temperatur"}
Number airflowIndoorOutgoingTemperature "Indoor outgoing [%.1f °C]" {comfoair="indoor_outgoing_temperatur"}
Number airflowIncommingFan      "Incomming fan [%d %%]" {comfoair="incomming_fan"}
Number airflowOutgoingFan       "Outgoing fan [%d %%]" {comfoair="outgoing_fan"}
Number airflowFilterRuntime         "Filter runtime [%d h]" {comfoair="filter_running"}
Number airflowFilterErrorI          "Filter (intern) [%s]" {comfoair="filter_error_intern"}
Number airflowFilterErrorE          "Filter (extern) [%s]" {comfoair="filter_error_extern"}
String airflowError                 "Errorcode [%s]" {comfoair="error_message"}
```

### Extended Examples

items/comfoair.items

```
Group Lueftung  "Lüftungsanlage"    <pie>

Number Lueftung_Auto_Mode   "Modus"                 <selfAutoMode> (Lueftung)

String Lueftung_Aussentemperatur_Message "Aussen [%s]"          <temperature> (Lueftung)
String Lueftung_Innentemperatur_Message "Innen [%s]"            <temperature> (Lueftung)
String Lueftung_Ventilator_Message "Ventilator [%s]"            <selfAiring> (Lueftung)
String Lueftung_Filterlaufzeit_Message  "Filterlaufzeit [%s]"       <selfRuntime> (Lueftung)
String Lueftung_Status_Message  "Status [%s]"               <selfError> (Lueftung)

Number Lueftung_Control     "Steuerung"                 <settings> (Lueftung) {comfoair="activate"}
Number Lueftung_Fan_Level   "Stufe [%d]"                <selfAiring> (Lueftung) {comfoair="fan_level"}
Number Lueftung_Komfortemperatur "Zieltemperatur [%.1f °C]"         <temperature> (Lueftung) {comfoair="target_temperatur"}
Number Lueftung_Bypass      "Bypass [MAP(bypass_de.map):%s]"    <selfBypass> (Lueftung) {comfoair="bypass_mode"}

Number Lueftung_Aussenlufttemperatur "Aussenzuluft [%.1f °C]"       <temperature> (Lueftung) {comfoair="outdoor_incomming_temperatur"}
Number Lueftung_Fortlufttemperatur "Aussenabluft [%.1f °C]"         <temperature> (Lueftung) {comfoair="outdoor_outgoing_temperatur"}
Number Lueftung_Zulufttemperatur "Raumzuluft [%.1f °C]"             <temperature> (Lueftung) {comfoair="indoor_incomming_temperatur"}
Number Lueftung_Ablufttemperatur "Raumabluft [%.1f °C]"             <temperature> (Lueftung) {comfoair="indoor_outgoing_temperatur"}
Number Lueftung_Zuluft      "Ventilator Zuluft [%d %%]"         <selfAiring> (Lueftung) {comfoair="incomming_fan"}
Number Lueftung_Abluft      "Ventilator Abluft [%d %%]"         <selfAiring> (Lueftung) {comfoair="outgoing_fan"}
Number Lueftung_Filterlaufzeit  "Filterlaufzeit [%d h]"         <selfClock> (Lueftung) {comfoair="filter_running"}
Number Lueftung_FilterfehlerI   "Filter (intern) [MAP(filter_de.map):%s]"   <selfFilterintern> (Lueftung) {comfoair="filter_error_intern"}
Number Lueftung_FilterfehlerE   "Filter (extern) [MAP(filter_de.map):%s]"   <selfFilterextern> (Lueftung) {comfoair="filter_error_extern"}
String Lueftung_Fehlermeldung   "Fehlercode [%s]"               <selfError> (Lueftung) {comfoair="error_message"}
```

transform/bypass_de.map

```
1=Offen
0=Geschlossen
undefined=undefiniert
-=undefiniert
```

transform/filter_de.map

```
1=Wechseln
0=Ok
undefined=undefiniert
-=undefiniert
```

persistence/db4o.persist

```
 Lueftung_Auto_Mode : strategy = everyChange, restoreOnStartup
 Lueftung_Aussentemperatur_Message : strategy = everyChange, restoreOnStartup
 Lueftung_Innentemperatur_Message : strategy = everyChange, restoreOnStartup
 Lueftung_Ventilator_Message : strategy = everyChange, restoreOnStartup
 Lueftung_Filterlaufzeit_Message : strategy = everyChange, restoreOnStartup
 Lueftung_Status_Message : strategy = everyChange, restoreOnStartup
 Lueftung_Fan_Level : strategy = everyChange, restoreOnStartup
```

sitemaps/comfoair.sitemap (fragment)

```
Text label="Informationen" icon="house" {
    Text item=Lueftung_Fan_Level label="Lüftung [MAP(level_de.map):%d]" icon="selfAiring" {
        Frame {
            Switch item=Lueftung_Auto_Mode label="Modus" mappings=[1="Auto", 0="Manuell"]
            Switch item=Lueftung_Fan_Level label="Stufe" mappings=[1="A", 2="1", 3="2", 4="3"]
        }
        Frame {
            Setpoint item=Lueftung_Komfortemperatur step=0.5 minValue=15 maxValue=25
            Text item=Lueftung_Aussentemperatur_Message
            Text item=Lueftung_Innentemperatur_Message
            Text item=Lueftung_Ventilator_Message
            Text item=Lueftung_Bypass
        }
        Frame {
            Text item=Lueftung_Filterlaufzeit_Message
            Text item=Lueftung_Status_Message
            Switch item=Lueftung_Control mappings=[1="App", 0="CCEase"]
        }
    }
    Text item=Gas_Aktueller_Tagesverbrauch label="Gas [%.2f m³]" {
        Frame {
            Text item=Gas_Aktueller_Tagesverbrauch
            Text item=Gas_Aktueller_Verbrauch
            Text item=Gas_Min_Verbrauch
            Text item=Gas_Max_Verbrauch
        }
        Frame {
            Chart item=Gas_Aktueller_Verbrauch period=D refresh=10000
            Chart item=Gas_Letzter_Tagesverbrauch period=M refresh=86400
            Chart item=Gas_Letzter_Tagesverbrauch period=Y refresh=86400
        }
    }
    Text item=Strom_Aktueller_Tagesverbrauch label="Strom [%.2f KWh]"{
        Frame {
            Text item=Strom_Aktueller_Tagesverbrauch
            Text item=Strom_Aktueller_Verbrauch
            Text item=Strom_Min_Verbrauch
            Text item=Strom_Max_Verbrauch
            Text item=Strom_Zaehler
        }
        Frame {
            Chart item=Strom_Aktueller_Verbrauch period=D refresh=10000
            Chart item=Strom_Letzter_Tagesverbrauch period=M refresh=86400
            Chart item=Strom_Letzter_Tagesverbrauch period=Y refresh=86400
        }
    }
    Text item=Wetter_Aussentemperatur label="Temperatur [%.1f °C]"{
        Frame {
            Text item=Wetter_Innentemperatur
            Text item=Wetter_Aussentemperatur
            Text item=Wetter_Temp_Max
            Text item=Wetter_Temp_Min
        }
        Frame {
            Chart item=Wetter_Diagramm period=D refresh=10000
            Chart item=Wetter_Aussentemperatur period=W refresh=10000
            Chart item=Wetter_Aussentemperatur period=M refresh=86400
            Chart item=Wetter_Aussentemperatur period=Y refresh=86400
        }
    }
    Text item=Datum
}
```

rules/airflow.rules

```Xtend
import java.lang.Math

var boolean autoChangeInProgress = false

rule "Filterlaufzeit"
when
    System started
    or
    Item Lueftung_Filterlaufzeit changed
then
    if( Lueftung_Filterlaufzeit.state instanceof DecimalType ){

        var Number laufzeit = Lueftung_Filterlaufzeit.state as DecimalType

        var Number weeks = Math::floor( (laufzeit/168).doubleValue )
        var Number days = Math::floor( ((laufzeit-(weeks*168))/24).doubleValue)

        var String msg = ""

        if( weeks > 0 ){

            if( weeks == 1 ) msg = weeks.intValue + " Woche"
            else msg = weeks.intValue + " Wochen"
        }

        if( days > 0 ){

            if( msg.length > 0 ) msg = msg + ", "

            if( days == 1 ) msg = msg + days.intValue + " Tag"
            else msg = msg + days.intValue + " Tage"
        }

        postUpdate(Lueftung_Filterlaufzeit_Message,msg)
    }
end

rule "Status Meldung"
when
    System started
    or
    Item Lueftung_Fehlermeldung changed
    or
    Item Lueftung_FilterfehlerI changed
    or
    Item Lueftung_FilterfehlerE changed
then
    var String msg = ""

    if( (Lueftung_FilterfehlerI.state instanceof DecimalType) && (Lueftung_FilterfehlerE.state instanceof DecimalType) && (Lueftung_Fehlermeldung.state instanceof StringType) ){

        if( (Lueftung_FilterfehlerI.state as DecimalType) == 1 || (Lueftung_FilterfehlerE.state as DecimalType) == 1 ){
            if( msg.length > 0 ) msg = msg + ", "
            msg = msg + "Filter: "
            if( (Lueftung_FilterfehlerI.state as DecimalType) == 1 ) msg = msg + "I"
            if( (Lueftung_FilterfehlerI.state as DecimalType) == 1 && (Lueftung_FilterfehlerE.state as DecimalType) == 1) msg = msg + " & "
            if( (Lueftung_FilterfehlerE.state as DecimalType) == 1 ) msg = msg + "E"
        }
        if( (Lueftung_Fehlermeldung.state as StringType) != "Ok" ){
            if( msg.length > 0 ) msg = msg + ", "
            msg = msg + "Error: " + (Lueftung_Fehlermeldung.state as StringType)
        }
    }

    if( msg.length == 0 ){

        msg = "Alles in Ordnung"
    }

    postUpdate(Lueftung_Status_Message,msg)
end

rule "Aussentemperatur Meldung"
when
    System started
    or
    Item Lueftung_Aussenlufttemperatur changed
    or
    Item Lueftung_Fortlufttemperatur changed
then
    if( (Lueftung_Aussenlufttemperatur.state instanceof DecimalType) && (Lueftung_Fortlufttemperatur.state instanceof DecimalType) ){

        postUpdate(Lueftung_Aussentemperatur_Message,"→ " + Lueftung_Aussenlufttemperatur.state.format("%.1f") +"°C, ← " + Lueftung_Fortlufttemperatur.state.format("%.1f") + "°C")
    }
end

rule "Innentemperatur Meldung"
when
    System started
    or
    Item Lueftung_Zulufttemperatur changed
    or
    Item Lueftung_Ablufttemperatur changed
then
    if( (Lueftung_Zulufttemperatur.state instanceof DecimalType) && (Lueftung_Ablufttemperatur.state instanceof DecimalType) ){

        postUpdate(Lueftung_Innentemperatur_Message,"→ " + Lueftung_Zulufttemperatur.state.format("%.1f") +"°C, ← " + Lueftung_Ablufttemperatur.state.format("%.1f") + "°C")
    }
end

rule "Ventilator Meldung"
when
    System started
    or
    Item Lueftung_Zuluft changed
    or
    Item Lueftung_Abluft changed
then
    if( (Lueftung_Zuluft.state instanceof DecimalType) && (Lueftung_Abluft.state instanceof DecimalType) ){

        postUpdate(Lueftung_Ventilator_Message,"→ " + (Lueftung_Zuluft.state as DecimalType) +"%, ← " + (Lueftung_Abluft.state as DecimalType) + "%")
    }
end

rule "Filter Fehler"
when
    Item Lueftung_Fehlermeldung changed
then
    if( (Lueftung_Fehlermeldung.state as StringType) != "Ok" ){

        send("test@gmail.com", "Lüftung hat einen Fehler gemeldet")
    }
end

rule "Filter wechseln"
when
    Item Lueftung_FilterfehlerI changed
    or
    Item Lueftung_FilterfehlerE changed
then
    if( (Lueftung_FilterfehlerI.state as DecimalType) == 1 || (Lueftung_FilterfehlerE.state as DecimalType) == 1 ){

        send("test@gmail.com", "Lüftungsfilter muss gewechselt werden")
        }
end

rule "Manueller Eingriff"
when
    Item Lueftung_Fan_Level changed
then

    if( autoChangeInProgress ){

        autoChangeInProgress = false
    }
    else{

        postUpdate(Lueftung_Auto_Mode,0)
    }
end

rule "Lüfterstufe"
when
    Item Lueftung_Auto_Mode changed
    or
    Time cron "0 0/1 * * * ?"
then
    if( (Lueftung_Auto_Mode.state as DecimalType) == 1 ){

        var Number day    = now.getDayOfWeek
        var Number hour   = now.getHourOfDay
        var Number minute = now.getMinuteOfHour

        var boolean isNight = false

        // Freitag
        if( day == 5 ){
            if( hour < 7 || (hour >= 23 && minute >=30) ) isNight = true
        }
        // Samstag
        else if( day == 6 ){
            if( hour < 9 || (hour >= 23 && minute >=30) ) isNight = true
        }
        // Sonntag
        else if( day == 7 ){
            if( hour < 9 || (hour >= 22 && minute >=30) || hour >= 23 ) isNight = true
        }
        else{

            if( hour < 7 || (hour >= 22 && minute >=30) || hour >= 23 ) isNight = true
        }

        var Number currentLevel = (Lueftung_Fan_Level.state as DecimalType)
        var Number newLevel = 3

        if( !isNight ){

            var Number raumTemperatur = (Lueftung_Ablufttemperatur.state as DecimalType)
            var Number aussenTemperatur = (Lueftung_Aussenlufttemperatur.state as DecimalType)
            var Number zielTemperatur = (Lueftung_Komfortemperatur.state as DecimalType)

            if(
                raumTemperatur >= zielTemperatur
                &&
                aussenTemperatur >= raumTemperatur
            ){

                newLevel = 1
            }
            else if(
                raumTemperatur >= zielTemperatur - 1
                &&
                aussenTemperatur >= raumTemperatur - 1
                &&
                currentLevel == 1
            ){

                newLevel = 1
            }
        }
        else if( Fenster_OG_Schlafzimmer.state == CLOSED || Fenster_OG_Ankleide.state == OPEN ) {

            newLevel = 2
        }

        if( newLevel != currentLevel ){

            if( newLevel == 1 ){

                logInfo("airflow.rules", "auto slowdown start")
                    send("test@gmail.com", "Lüftung verlangsamt")
            }
            else if( currentLevel == 1 ){

                    logInfo("airflow.rules", "auto slowdown end")
                send("test@gmail.com", "Lüftung wieder normal")
            }

            autoChangeInProgress=true
            sendCommand(Lueftung_Fan_Level,newLevel)
        }
    }
end
```
