This provides Binduiings for Philips hue.

Integrating tap switches:

NOTE: the information below is taken from http://www.developers.meethue.com/documentation/groups-api


The switch sends an event to the bridge which updates the last Event & Timestamp (with 1 second resolution). 
Currently (2015-01-02) there is no way to be notified by the gateway.
Tap swtches are "Sensors": there can be many senors; by Default, sensor #1 is a built in daylight sensor depending on location

The last button pressed is found in "buttonevent", which is 34,16,17,18 for buttons 1,2,3,4
The effects of pressing a particular switch are determined by "actions" (see: http://www.developers.meethue.com/documentation/rules-api).

inside an action, a set of conditions has to be true to trigger the action.

e.g.:

{
	"name": "Tap 2.4 Standardeinstellung",
	"owner": "<myOwnerKey>",
	"created": "2015-01-02T11:52:26",
	"lasttriggered": "2015-01-02T18:54:46",
	"timestriggered": 41,
	"status": "enabled",
	"conditions": [
		{
			"address": "/sensors/2/state/buttonevent",
			"operator": "eq",
			"value": "18"
		},
		{
			"address": "/sensors/2/state/lastupdated",
			"operator": "dx"
		}
	],
	"actions": [
		{
			"address": "/groups/0/action",
			"method": "PUT",
			"body": {
				"scene": "bba7dcfc2-on-0"
			}
		}
	]
}

which means: if the last pressed button on senser #2 was Button4 and lastupdated changed, then set the scene "bba7dcfc2-on-0" for group 0

There are 2 possibilites:

1. The simpler one: poll the state of the sensors often, an handle it like a switch
Pros: 
	- relatively simple to implement
Cons: 
	- I don't know which side effects polling has; in teh worst case, the ZigBee network is queried each time I poll the sensors; no docs there.
	- Some delays in the reactions 
	- rules on the bridge can lead to interferences with openhab logic.
	
2. provide rules as well

Pros: 
	- determined behaviour
	- no delays
	
Cons:
	- more complicated to implement
	- status updates for the key still produce side effects, but intervals can be bigger than in variant 1
	
Conclusion:

Var 1 is needed anyway, so best build it; V2 can be added later.

Prototype Configs:
Tap:
	contains last button , change date "1;date","2;date"
	
	(from samples: Switch Garage_Gate { binding="xxx", autoupdate="false"})
	
	// possibly cleanest
	Switch  Tap1_1   {hue="tap;1;1", autoupdate="false"}
	Switch  Tap1_2   {hue="tap;1;2", autoupdate="false"}
	
	will react to events only..
	oder 
	
	Number Tap1  {hue="tap;1"}	// will be 1..4; receives events; maybe "0" is acc??

Rule:
	String rule1 {hue="rule;myname}
	The string value contains the rule (as json string??)
	alternative: actions? ist sauberer: hueSetRule("myName",rule...); better: hueSetAction(Tap1_1,"whatever to do..."
	
	correlation between items in switches, action unclear.
	
	
