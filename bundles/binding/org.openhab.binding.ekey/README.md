# ekey Binding

[ekey](http://ekey.net/) is an Austrian company that provides biometric
access-control solutions, more precisely fingerprint readers and
corresponding controllers.  This binding extends the functionality of the
products [ekey home](http://ekey.net/ekey-home-en) and
[ekey multi](http://ekey.net/ekey-multi-en).

## Setup

To use this binding, one needs to have either the _home_ or the _multi_
solution of ekey.  Note that the _multi_ version provides much more
functionality than the _home_ version.

Additionally the _ekey UDP converter_ is needed. This module provides an
interface by converting the internal RS485 signals to Ethernet. Connecting
this to the local network enables ekey to communicate to the ekey binding.

![example](http://ekey.net/media/W/720/bilder/automatisierung/example_E.jpg)

The ekey binding translates information that comes from the ekey controller and
makes it usable to openHAB.  Usually ekey sends packets with information on
each user input. This might be every time a person pulls their finger over the
terminal or if a digital input occurs. 

The information consists at least of these types:

* **userID** - the index of the detected user that is stored on the controller
* **fingerID** - the finger that the person used 
* **terminalID** - the serial number of the fingerprint reader that was used
* **action** - this tells whether the user was recognized successfully and
 access was granted or access was denied

The amount of information depends on whether _ekey home_ or _ekey multi_ is
used and which protocol is used by the converter. But the previously mentioned
4 are supported in any case.

The converter knows up to three different protocols. The _RARE_ protocol that
is enabled by default, the _HOME_ protocol which is very similar to the _RARE_,
and finally the _MULTI_ protocol which is fairly powerful compared to the other
ones. The binding uses the term "mode" instead of "protocol".

The type of protocol that is used by the converter can be changed with a small
tool which is only available for Windows systems. This tool is called
_ConfigConverterUDP_ and can be downloaded from the [ekey website](https://www.ekey.net/en/media_center/).


## Binding Configuration

This binding can be configured in the file `services/ekey.cfg`.

| Property  | Default | Required | Description |
|-----------|---------|:--------:|-------------|
| ip        |         |    No    | IPv4 address of the eKey udp converter.  A static IP address is recommended. |
| port      |         |    Yes   | The port as configured during the UDP Converter configuration.  e.g. 51000 |
| mode      |  RARE   |    No    | Can be RARE, MULTI or HOME depending on what the system supports |
| delimiter | ` ` (space) | No   | The delimiter is also defined on the ekey UDP converter - use the ekey configuration software to determine which delimiter is used or to change it.  Another option is `_` (underscore) |


## Item Configuration

The syntax is:

```
ekey="<interestname>"
```

where `<interestname>` is one of the following:

* action
* fingerid
* inputid
* keyid
* mode
* relayid
* terminalid
* terminalname
* userid
* username
* userstatus


### interestname Definitions

* **Action** 
  This indicates whether access was granted (value=0) or denied (value=-1).
  According to the ekey documentation there are six more values possible as you
  can see in the .map file below. (Item Type: Number, Modes: R/H/M)
* **Finger ID**
  This indicates the finger that was used by a person. The value consists of 2
  digits. The first one specifies the hand (left hand=1, right hand=2) and
  the second digit specifies the finger from left to right. (see the .map file
  below). (Item Type: Number, Modes: R/H/M)
* **Input ID**
  This indicates which of the four digital inputs was triggered. Value is
  number of Input. "-1" indicates that no input was triggered. (Item Type:
  Number, Modes: M)
* **Key ID**
  This indicates which of the four keys was used. See ekey documentation on
  "keys". (Item Type: Number, Modes: M)
* **Mode**
  This simply returns the mode that was used (1=RARE, 2=MULTI, 3=HOME) (Item
  Type: Number, Modes: R/H/M)
* **Relay ID**
  This indicates which relay has been switched. (Item Type: Number, Modes: R/H)
* **Terminal ID**
  This provides the serial number of the packet source. The source can be a
  fingerprint terminal or the controller (in case of digital inputs). The
  Serial number has a length of 13. When using RARE mode, only the trailing 8
  digits can be returned. (Item Type: Number, Modes: R/H/M)
* **Terminal name**
  This returns the 4-character-long name that was specified on the controller
  for the specific terminals. (Item Type: String, Modes: M)
* **User ID**
  This indicates which user has been detected on the terminal. The value is the
  numerical order of the user as it was specified on the controller. (Item
  Type: Number, Modes: R/H/M)
* **Username**
  This returns the ten-character-long name of the person that has been
  recognized on the terminal. The name that is returned must have been
  previously specified on the controller. (Item Type String, Modes: M)
* **User status**
  This indicates the status of the user (-1=undefined, 1=enabled, 0= disabled)
  (Item Type: Number, Modes: M)

In many cases it makes sense to map the number values to more meaningful
strings. See the mapping examples below.


## Examples

### Full Example

Here is an example that demonstrates a simple rule that feeds the openHAB
TTS-engine and welcomes the user when he or she enters the house.

items/ekey.items

```
Number UserID "Last user that accessed the house was [MAP(ekey_names.map):%d]" { ekey="userid" }
Number FingerID "User used finger [MAP(ekey_finger.map):%d]"                   { ekey="fingerid" }
Number TerminalID "Last used terminal [MAP(ekey_terminal.map):%d]"             { ekey="terminalid" }
Number Action "Last action [MAP(ekey_action.map):%d]"                          { ekey="action" }

```

rules/ekey.rules

```java
rule Welcome
when
    Item Action received update       
then
    if (Action.state==-1)
        say("unauthorized access!")
        else {
            var String name = transform("MAP","ekey_names.map",UserID.state.toString())
            var String finger = transform("MAP","ekey_finger.map",FingerID.state.toString())
            var String terminal =   transform("MAP","ekey_terminal.map",TerminalID.state.toString())                     
            var String text = name + " put his " + finger + " finger over the " + terminal + " terminal. Hello "+name
            say(text)  
        }
end
```

transform/ekey_finger.map

```javascript
11=leftlittle
12=leftring
13=leftmiddle
14=leftindex
15=leftthumb
21=rightthumb
22=rightindex
23=rightmiddle
24=rightring
25=rightlittle
30=unspecified
```

transform/ekey_names.map


```javascript
-1=Unspecified
1=John Doe
2=Jane Doe
```

transform/ekey_terminal.map

```javascript
80156839130911=frontdoor
80156839130914=backdoor
```

transform/ekey_action.map

```javascript
0=granted
-1=rejected
1=timeoutA
2=timeoutB
3=inactive
4=alwaysuser
5=notcoupled
6=digitalinput
```

### Further Examples

* [ekey binding demo config](http://pastebin.com/fjXkFbiq) (may be specific to openHAB 1.x)
