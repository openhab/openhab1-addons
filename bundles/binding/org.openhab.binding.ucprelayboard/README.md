## Introduction

This binding provides support for relay board available from [ucprojects.eu](http://ucprojects.eu) (site in polish)

For installation of the binding, please see Wiki page [[Bindings]].

## Configuration in openhab.cfg

    ################################ UCProjects.eu Relay Board Binding ###################################
    #
    # Port and baud rate (optional, defaults to 57600) for every device
    # ucprelayboard:board.<name>.port=/dev/tyUSB0
    # ucprelayboard:board.<name>.baud=
    #
    # Refresh of relay board state interval in miliseconds (optional, defaults to 60000)
    # ucprelayboard:refresh=

You can define more than one board.

## Items

Only Switch item is supported. In order to bind relay board to the switch, you need to provide configuration settings in your item file (in the folder configurations/items).


```
{ucprelayboard="board=<name>;relay=<number>[;inverted=true]"}
```

| Property | Description | Mandatory |
| :------------- | :-----| :--------- |
| board | name of the board, as defined in openhab.cfg | Yes |
| relay | index of the relay on the board | Yes |
| inverted | if set to true, inverts value of the relay | No |

## Notes

see [[Serial Binding]] notes for serial port configuration and troubleshooting.