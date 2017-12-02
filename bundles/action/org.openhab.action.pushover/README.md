# Pushover Actions

The Pushover action service allows you to notify mobile devices of a message using the Pushover API web service.

## Configuration

You may define default values for parameters to action calls in the file `services/pushover.cfg`.  None of the configuration parameters are required as you can specify required configuration items in the action call, but you must at least provide an _API Token_, _User/Group Key_ and a _Message_ in some manner before a message can be pushed.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| defaultTimeout | 10000 | No   | Timeout in milliseconds for the connection to pushover.net |
| defaultToken | | if using action call without `apiToken` parameter | Pushover [API token](https://pushover.net/api) to send to devices |
| defaultUser | | if using action call without `userKey` parameter | Pushover User or Group key (not e-mail address) of your user (or you) to send to devices. |
| defaultTitle | openHAB | No | Application title for the notification |
| defaultPriority | 0 | No | Priority of the notification, from -2 (low priority) to 2 (high priority) |
| defaultUrl | | No | URL to attach to the message if not specified in the command. This can be used to trigger actions on the device. |
| defaultUrlTitle | | No | URL title to attach to the message if not specified in the command. This can be used to trigger actions on the device. |
| defaultRetry | 300 | No | When priority is 2 (high priority), how often (in seconds) should messages be resent |
| defaultExpire | 3600 | No | When priority is 2 (high priority), how long (in seconds) to continue resending messages until acknowledged |

## Actions

The following are valid action calls that can be made when the plugin is loaded. For specific information on each item, see the [Pushover API](https://pushover.net/api).

* `pushover(String message)`
* `pushover(String message, String device)`
* `pushover(String message, int priority)`
* `pushover(String message, int priority, String url)` 
* `pushover(String message, int priority, String url, String urlTitle)` 
* `pushover(String message, int priority, String url, String urlTitle, String soundFile)` 
* `pushover(String message, String device, int priority)`
* `pushover(String message, String device, int priority, String url)` 
* `pushover(String message, String device, int priority, String url, String urlTitle)` 
* `pushover(String message, String device, int priority, String url, String urlTitle, String soundFile)` 
* `pushover(String apiToken, String userKey, String message)`
* `pushover(String apiToken, String userKey, String message, String device)`
* `pushover(String apiToken, String userKey, String message, int priority)`
* `pushover(String apiToken, String userKey, String message, String device, int priority)`
* `pushover(String apiToken, String userKey, String message, String device, String title, String url, String urlTitle, int priority, String soundFile)`

The action calls have to be configured in the above sequence, if you need to omit one of the call parameters you may use a null value or two double quotes. In this case any default values from `services/pushover.cfg` will be used. 
Note that you cannot use a null value for int priority.

### Example

Send a message without a sound, omit String url and String urlTitle.

* `pushover("test message", 1, null, null, "none")` or
* `pushover("test message", 1, "", "", "none")`
