# Pushsafer Actions

The Pushsafer action allows you to notify iOS, Android & Windows 10 Phone & Desktop devices of a message using the Pushsafer API web service.

## Actions

The following is a valid action call that can be made when the plugin is loaded.
For specific information on each item, see the [Pushsafer API](https://www.pushsafer.com/en/pushapi).

```
pushsafer(String apiToken, String message, String title, String device, String icon, String vibration, String sound)
```

You must at least provide an API token (Private or Alias Key from Pushsafer.com) and a message in some manner before a message can be pushed.
All other parameters are optional.
If you use an alias key, the parameters (device, icon, sound, vibration) are overwritten by the alias setting on pushsafer.
