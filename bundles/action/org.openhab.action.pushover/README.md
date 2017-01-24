# Pushover Actions

The Pushover action service allows you to notify mobile devices of a message using the Pushover API web service.

## Configuration

You may define default values for parameters to action calls in the file `services/pushover.cfg`.  None of the configuration parameters are required as you can specify required configuration items in the action call, but you must at least provide an _API Token_, _User/Group Key_ and a _Message_ in some manner before a message can be pushed.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| defaultTimeout | | | The timeout for the communication with the Pushover service. |
| defaultToken | | | Pushover API token to send to devices. |
| defaultUser | | | Pushover User or Group key to send to devices. |
| defaultTitle | | | Application title for the notification. |
| defaultPriority | | | Priority of the notification. Default is 0. |
| defaultUrl | | | A URL to send with the notification. |
| defaultUrlTitle | | | Title of the URL to send with the notification. |
| defaultRetry | | | When priority is 2, how often in seconds should messages be resent. |
| defaultExpire | | | When priority is 2, how long to continue resending messages until acknowledged. |

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
