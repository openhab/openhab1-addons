# OpenWebIf Action

The OpenWebIf action allows you to send a message to enigma2-based Linux satellite receivers (Dreambox, VU+, Clarke-Tech, ...) with the installed OpenWebIf plugin.

## Configuration

Configure your satellite receivers in `services/openwebif.cfg`.

| Property                   | Default | Required | Description            |
|----------------------------|---------|:--------:|------------------------|
| `receiver.<name>.host`     |         |          | For example, `vusolo2` |
| `receiver.<name>.port`     |         |          | For example, `81`      |
| `receiver.<name>.user`     |         |          | For example, `root`    |
| `receiver.<name>.password` |         |          |                        |
| `receiver.<name>.https`    |         |          | For example, `false`   |

### Configuration Example

```
receiver.main.host=vusolo2
receiver.main.port=81
receiver.main.user=root
receiver.main.password=xxxxx
receiver.main.https=false
```

## Action

Now you can send a message to the configured receiver:

*   `sendOpenWebIfNotification(NAME, MESSAGE, TYPE, TIMEOUT)`

| Parameter | Meaning                                              |
|-----------|------------------------------------------------------|
| NAME      | The configured name of the satellite receiver        |
| MESSAGE   | The message to send to the receiver                  |
| TYPE      | The message type (INFO, WARNING, ERROR)              |
| TIMEOUT   | How long the text will stay on the screen in seconds |

## Examples

```
sendOpenWebIfNotification("main", "Hello World!\n\nThis is a message sent from openHab!", "WARNING", 10)
```

![](https://farm4.staticflickr.com/3882/15284270826_8cf0e637d8_z.jpg)
