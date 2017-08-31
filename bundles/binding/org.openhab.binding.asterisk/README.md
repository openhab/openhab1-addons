# Asterisk Binding

The Asterisk binding is used to enable communication between openhab and the free and open source PBX solution [Asterisk](http://www.asterisk.org). This binding detects incoming phone calls or if someone makes a phone call. In combination with other bindings (e.g., the Samsung TV Binding) you can display caller IDs on your TV.

## Binding Configuration

The binding can be configured in the file `services/asterisk.cfg`.

| Key | Default | Required | Description |
|-----|---------|:--------:|-------------|
| host |        |   Yes    | hostname of the AMI |
| username |    |          | username for login to AMI |
| password |    |          | password for login to AMI |


## Item Configuration

In order to bind an item to the Asterisk PBX you need to provide configuration settings for each item to which you want to bind in your `.items` file (in the `items` folder). 

The format of the binding configuration is simple and looks like this:

```
asterisk=<eventType>
```

where `<eventType>` is of the value `active` for currently active calls. Currently there are no other valid values.

Asterisk binding configurations are valid for `Call`, `Switch` and `String` items.

* `Call` items receive the `to` and `from` phone numbers at the start of the event, and empty numbers at the end of the event.

* `Switch` items with this binding will receive an `ON` update event at the start and an `OFF` update event at the end.

* `String` items will receive the external phone number in the form of a string value as a status update. At the end of an event, an empty string is sent as a status update.

As a result, your lines in your items file might look like follows:

```
Switch Incoming_Call    "Ringing"                          { asterisk=active }
Call Active_Call        "Connected [to %1$s from %2$s]"    { asterisk=active }
Call Active_Call        "Connected to [%s]"                { asterisk=active }
```

## Examples

### Active call examples

* Switch on a light when there is at least an active call (when there are no active calls the light will turn off)

```
Switch light (lights) { asterisk="active" }
```

or

```
Switch light (lights) { asterisk="active:*:*" }
```

* Switch on a light when '215' calls '101' and turn it off when the call ends

```
Switch light (lights) { asterisk="active:215:101" }
```

* Switch on a light on every call to '101' and turn it off when the call ends

```
Switch light (lights) { asterisk="active:*:101" }
```

* Switch on a light on every call originated from '215' and turn it off when the call ends

```
Switch light (lights) { asterisk="active:215:*" }
```

### DTMF Digit examples

* Switch on a light when '1' digit is sent from '215' to '101' during an active call

```
Switch light (lights) { asterisk="digit:215:101:1" }
```

* Switch on a light whenever a '1' digit is sent to '101' during an active call

```
Switch light (lights) { asterisk="digit:*:101:1" }
```

* Switch on a light whenever a '1' digit is sent from '215' during an active call

```
Switch light (lights) { asterisk="digit:215:*:1" }
```

* Switch on a light whenever a '1' digit is sent during an active call

```
Switch light (lights) { asterisk="digit:*:*:1" }
```

### Asterisk

In some cases it is very useful to make call routing decisions in Asterisk based on openHAB Items states. As an example, if nobody is home (away mode is on) route my doorphone calls to mobile, in other case route them to local phones inside the house. To do that AGI (Asterisk application gateway interface) can be used to obtain Item state value into an Asterisk variable and then a routing decision can be performed based on this variable value. Here is a small python script which, when called from Asterisk AGI makes an http request to openHAB REST API, gets specific item state and puts it into specified Asterisk variable:

```python
#!/usr/bin/python
import sys,os,datetime
import httplib
import base64

def send(data):
        sys.stdout.write("%s \n"%data)
        sys.stdout.flush()

AGIENV={}
env = ""
while(env != "\n"):
        env = sys.stdin.readline()
        envdata =  env.split(":")
        if len(envdata)==2:
                AGIENV[envdata[0].strip()]=envdata[1].strip()

username = AGIENV['agi_arg_1']
password = AGIENV['agi_arg_2']
item = AGIENV['agi_arg_3']
varname = AGIENV['agi_arg_4']

auth = base64.encodestring('%s:%s' % (username, password)).replace('\n', '')
headers = {"Authorization" : "Basic %s" % auth}
conn = httplib.HTTPConnection("localhost", 8080)
conn.request('GET', "/rest/items/%s/state"%item, "", headers)
response = conn.getresponse()
item_state = response.read()

send("SET VARIABLE %s %s"%(varname, item_state))
sys.stdin.readline()
```

In Asterisk dialplan (extensions.conf) this AGI script is used in the following way:

```
exten => 1000,1,Answer()
exten => 1000,n,AGI(openhabitem.agi, "asterisk", "password", "Presence", "atHome")
exten => 1000,n,GotoIf($["${atHome}" == "ON"]?athome:away)
exten => 1000,n(athome),Playback(hello-world) ; do whatever you need if Presence is ON
exten => 1000,n,Hangup()
exten => 1000,n(away),Playback(beep) ; do whatever you need if Presence is OFF
exten => 1000,n,Hangup()
```

In AGI, call arguments are:

* the script name itself
* openHAB username
* openHAB password
* openHAB Item name
* Asterisk variable to put state to

