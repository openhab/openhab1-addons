# Autelis Binding

Autelis manufactures a network enabled pool interface for many popular pool systems.  See the [Autelis website](http://www.autelis.com) and the [Autelis Command Protocol](http://www.autelis.com/wiki/index.php?title=Pool_Control_(PI)_HTTP_Command_Reference) for more information.

The binding is fairly complete and supports the following functionality.

* Read circuit, auxiliary, temperature, pump, chemistry and system values  
* Control circuit, auxiliary lighting scenes, and temperature set points

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/autelis/).

## Binding Configuration

The binding can be configured in the file `services/autelis.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 5000    |   No     | Refresh rate in milliseconds |
| host     |         |   Yes    | Host (name or ip) to connect to |
| port     | 80      |   No     | Port on which to connect to the host |
| username |         |   if configured | User name for example `admin` |
| password |         |   if configured | Password, for example `admin` |


## Item Configuration

The format is

```
{ autelis="parentNode.childElement" }
```

or

```
{ autelis="lightscmd" }
```

An example to get the system version would be:

```
{ autelis="system.version" }
```

* Only `equipment.*`, `temp.*` and `lightscmd` items can be updated from openHAB, everything else is read only.
* For lighting commands, the following strings can be sent to an item configured with `{ autels="lightscmd"}` (through a mapped push button for example):
  * 'alloff, allon, csync, cset, cswim, party, romance, caribbean, american, sunset, royalty, blue, green, red, white, magenta, hold, recall`

The following are a listing of possible values for the configuration string using a sample xml output from a Autelis controller:

```xml
    <system>
		<runstate>50</runstate>
		<model>13</model>
		<haddr>20</haddr>
		<opmode>0</opmode>
		<freeze>0</freeze>
		<sensor1>0</sensor1>
		<sensor2>0</sensor2>
		<sensor3>0</sensor3>
		<sensor4>0</sensor4>
		<sensor5>0</sensor5>
		<version>1.4.4</version>
		<time>1425269492</time>
	</system>
	<equipment>
		<circuit1>0</circuit1>
		<circuit2>0</circuit2>
		<circuit3>0</circuit3>
		<circuit4>0</circuit4>
		<circuit5>0</circuit5>
		<circuit6>0</circuit6>
		<circuit7>0</circuit7>
		<circuit8>0</circuit8>
		<circuit9>0</circuit9>
		<circuit10></circuit10>
		<circuit11></circuit11>
		<circuit12></circuit12>
		<circuit13></circuit13>
		<circuit14></circuit14>
		<circuit15></circuit15>
		<circuit16></circuit16>
		<circuit17></circuit17>
		<circuit18></circuit18>
		<circuit19></circuit19>
		<circuit20>0</circuit20>
		<feature1>0</feature1>
		<feature2>0</feature2>
		<feature3>0</feature3>
		<feature4>0</feature4>
		<feature5>0</feature5>
		<feature6>0</feature6>
		<feature7>0</feature7>
		<feature8>0</feature8>
		<feature9></feature9>
		<feature10></feature10>
	</equipment>
	<temp>
		<poolht>1</poolht>
		<spaht>1</spaht>
		<htstatus>0</htstatus>
		<poolsp>71</poolsp>
		<spasp>101</spasp>
		<pooltemp>60</pooltemp>
		<spatemp>60</spatemp>
		<airtemp>55</airtemp>
		<tempunits>F</tempunits>
		<htpump>0</htpump>
	</temp>
	<pumps>
		<pump1>0,0,0</pump1>
		<pump2></pump2>
		<pump3></pump3>
		<pump4></pump4>
		<pump5></pump5>
		<pump6></pump6>
		<pump7></pump7>
		<pump8></pump8>
	</pumps>
	<chlor>
		<chloren>1</chloren>
		<poolsp>50</poolsp>
		<spasp>0</spasp>
		<salt>58</salt>
		<super>0</super>
		<chlorerr>0</chlorerr>
		<chlorname>Intellichlor--40</chlorname>
	</chlor>
```

## Item Examples

```
String PoolVersion	"Version [%s]" {autelis="system.version"}
Number PoolSetPoint	"Pool SetPoint [%d]" {autelis="temp.poolsp"}
Number PoolTemp	"Pool Temp [%d]" {autelis="temp.pooltemp"}
Number PoolSpaTemp	"Spa Temp [%d]" {autelis="temp.spatemp"}
Number PoolAirTemp	"Air Temp [%d]" {autelis="temp.airtemp"}
Number PoolSpaSetPoint	"Spa SetPoint [%d]" {autelis="temp.spasp"}
Number PoolChemSalt	"Salt Levels [%d]" {autelis="chlor.salt"}
Switch PoolSpaMode  "Spa Mode [%s]" {autelis="equipment.circuit1"}
Switch PoolMode  "Pool Mode [%s]" {autelis="equipment.circuit6"}
Switch PoolWaterfall  "Waterfall [%s]" {autelis="equipment.circuit2"}
Switch PoolSpaLight  "Spa Light [%s]" {autelis="equipment.circuit3"}
Switch PoolLight  "Pool Light [%s]" {autelis="equipment.circuit4"}
String PoolLightCmd	 {autelis="lightscmd"}
```
