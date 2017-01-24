# Compatibility

There are currently two protocols for communicating with Tivos. v1.0 works with older Tivos, and [v1.1](http://www.tivo.com/assets/images/abouttivo/resources/downloads/brochures/TiVo_TCP_Network_Remote_Control_Protocol.pdf) works with newer Tivos. The current openHAB Tivo binding implements v1.0. While there is some overlap, owners of newer Tivos will encounter some incompatibility. If you find that button commands (`pause, play, record`) work, but `nowshowing` does not work, you likely have a Tivo that implements the v1.1 protocol.

# Configuration

Enable [Network Remote Control](http://support.tivo.com/app/answers/detail/a_id/391) on your Tivo.

Add a line to your `openhab.cfg` file to point to your Tivo's hostname or IP address:

`tivo:host=<hostname>`

Create a `Switch` in your `.items` file for any command you wish to send your Tivo when the `Switch` is turned ON. For example:


    Switch Tivo_Button "Tivo Button" { tivo="tivo" }
    Switch Tivo_Recordings "Tivo Recordings" { tivo="nowshowing" }
    Switch Tivo_Standby "Tivo Standby" { tivo="standby" }

The full list of supported commands is [here](https://bitbucket.org/JonathanGiles/jtivo/src/9bb8a78424a7c8a461b0082c3d2dd6db31bf2454/src/net/jonathangiles/tivo/TivoCommand.java?at=default).

Turn any Tivo `Switch` ON in a [rule](https://github.com/openhab/openhab/wiki/Rules) or [script](https://github.com/openhab/openhab/wiki/Scripts) to send the configured command to your Tivo. For example, the following code in a rule simulates a press of the "tivo" button on the remote control, gives the Tivo 3 seconds to wake up, then jumps to the recording list:

    sendCommand(Tivo_Button, ON)
    createTimer(now.plusSeconds(3)) [|sendCommand(Tivo_Recordings, ON)]

The following code in a rule will put the Tivo in standby:

    sendCommand(Tivo_Standby, ON)