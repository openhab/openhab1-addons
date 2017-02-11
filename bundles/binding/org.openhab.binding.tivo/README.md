# TiVo Binding

This binding works with older TiVo units which support the v1.0 protocol.  TiVo units that implement the [newer v1.1 API](http://www.tivo.com/assets/images/abouttivo/resources/downloads/brochures/TiVo_TCP_Network_Remote_Control_Protocol.pdf) will encounter some incompatibility.  If you find that button commands (`pause, play, record`) work, but `nowshowing` does not work, you likely have a Tivo that implements the v1.1 protocol.

## Prerequisites

Enable [Network Remote Control](http://support.tivo.com/app/answers/detail/a_id/391) on your TiVo.

## Binding Configuration

This binding can be configured with the file `services/tivo.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| host     |         |   Yes    | Your Tivo's hostname or IP address |

## Item Configuration

The format of item binding configuration strings is

```
tivo="<command>"
```

where `<command>` is one of:

* tivo
* info
* guide
* window
* record
* clear
* enter
* play
* reverse
* pause
* forward
* replay
* slow
* advance
* cc_on
* cc_off
* standby
* nowshowing
* num0
* num1
* num2
* num3
* num4
* num5
* num6
* num7
* num8
* num9
* live_tv     
* thumbs_up   
* thumbs_down 
* channel_up  
* channel_down


## Examples

```
Switch Tivo_Button "Tivo Button" { tivo="tivo" }
Switch Tivo_Recordings "Tivo Recordings" { tivo="nowshowing" }
Switch Tivo_Standby "Tivo Standby" { tivo="standby" }
```

Turn any TiVo `Switch` ON in a rule or script to send the configured command to your TiVo. 

For example, the following code in a rule simulates a press of the "tivo" button on the remote control, gives the Tivo 3 seconds to wake up, then jumps to the recording list:

```
Tivo_Button.sendCommand(ON)
createTimer(now.plusSeconds(3)) [ | Tivo_Recordings.sendCommand(ON) ]
```

The following code in a rule will put the Tivo in standby:

```
Tivo_Standby.sendCommand(ON)
```
