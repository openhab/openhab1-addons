## UCProjects.eu Relay Board Binding

This binding provides support for relay board available from [ucprojects.eu](http://ucprojects.eu) (site in Polish)

## Binding Configuration

This binding must be configured in the file `services/ucprelayboard.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| board.`<name>`.port | | Yes   | Name of the serial device to which the board is connected |
| board.`<name>`.baud | 57600 | No | Baud rate for the serial device |
| refresh  | 60000    |   No    | Refresh of relay board state interval in miliseconds (60000 is one minute) |

where:

* `<name>` is a name you choose, and you can specify more than one board.

## Item Configuration

Only Switch items are supported.

```
ucprelayboard="board=<name>;relay=<number>[;inverted=true]"
```

where:

* `<name>` is a name you configured in the binding configuration
* `<number>` is the index of the relay on the board you wish to switch
* `[;inverted=true]` an optional section that inverts the meaning of ON and OFF
