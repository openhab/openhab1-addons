# Pushover Actions

The Pushover action service allows you to notify mobile devices of a message using the Pushover API web service.

## Configuration

You may define default values for parameters to action calls in the file `services/pushover.cfg`.
None of the configuration parameters are required as you can specify required configuration items in the action call, but you must at least provide an *API Token*, *User/Group Key* and a *Message* in some manner before a message can be pushed.

| Property        | Default | Required                                                      | method to set                       | Description                                                                                                            |
|-----------------|---------|:-------------------------------------------------------------:|-------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| defaultTimeout  | 10000   | No                                                            | -                                   | Timeout in milliseconds for the connection to pushover.net                                                             |
| defaultToken    |         | Yes, if using action call without calling withApiKey() method | withApiKey(String apiKey)           | Pushover [API token](https://pushover.net/api) to send to devices                                                      |
| defaultUser     |         | Yes, if using action call without calling withUser() method   | withUser(String user)               | Pushover User or Group key (not e-mail address) of your user (or you) to send to devices.                              |
| defaultTitle    | openHAB | No                                                            | withTitle(String title)             | Application title for the message                                                                                      |
| defaultPriority | 0       | No                                                            | withPriority(int priority)          | Priority of the notification, from -2 (low priority) to 2 (high priority)                                              |
| defaultUrl      |         | No                                                            | withUrl(String url)                 | URL to attach to the message if not specified in the command. This can be used to trigger actions on the device.       |
| defaultUrlTitle |         | No                                                            | withUrlTitle(String urlTitle)       | URL title to attach to the message if not specified in the command. This can be used to trigger actions on the device. |
| attachment      |         | No                                                            | withAttachment(String attachment)   | The full path of a JPEG image attachment to be pushed with the message.                                                |
| contentType     |         | No                                                            | withContentType(String contentType) | Content type, ie  "image/png"                                                                                          |
| defaultRetry    | 300     | No                                                            | -                                   | When priority is 2 (high priority), how often (in seconds) should messages be resent                                   |
| defaultExpire   | 3600    | No                                                            | -                                   | When priority is 2 (high priority), how long (in seconds) to continue resending messages until acknowledged            |

## Actions

The following are valid action calls that can be made when the plugin is loaded.
For specific information on each item, see the [Pushover API](https://pushover.net/api).

- `sendPushoverMessage(Pushover pushover)`

Most parameters are optional and may have a default value. In this case any default values from `services/pushover.cfg` will be used.
Note that you cannot use a null value for int priority.
See the examples section for details on how use the pushover action.

- `cancelPushoverEmergency(String receipt)`
- `cancelPushoverEmergency(String apiToken, String userKey, String receipt)`

The actions which are send using `withEmergencyPriority()` will send a message with [Emergency Priority](https://pushover.net/api#priority).
The actions return a receipt identifier (String).
The identifier has to be passed on to the `cancelPushoverEmergency` actions to cancel the notification prior to reaching the `defaultExpire` value of one hour.

###### Deprecated Actions
- ~~`pushover(String message)`~~
- ~~`pushover(String message, int priority)`~~
- ~~`pushover(String message, int priority, String url)`~~
- ~~`pushover(String message, int priority, String url, String urlTitle)`~~
- ~~`pushover(String message, int priority, String url, String urlTitle, String soundFile)`~~
- ~~`pushover(String message, String device)`~~
- ~~`pushover(String message, String device, int priority)`~~
- ~~`pushover(String message, String device, int priority, String url)`~~
- ~~`pushover(String message, String device, int priority, String url, String urlTitle)`~~
- ~~`pushover(String message, String device, int priority, String url, String urlTitle, String soundFile)`~~
- ~~`pushover(String apiToken, String userKey, String message)`~~
- ~~`pushover(String apiToken, String userKey, String message, int priority)`~~
- ~~`pushover(String apiToken, String userKey, String message, String device)`~~
- ~~`pushover(String apiToken, String userKey, String message, String device, int priority)`~~
- ~~`pushover(String apiToken, String userKey, String message, String device, String title, String url, String urlTitle, int priority, String soundFile)`~~
- ~~`pushover(String apiToken, String userKey, String message, String device, String title, String url, String urlTitle, int priority, String soundFile, String attachment)`~~


### Basic example

Different ways to send a message. You can use multiple withX() methods to suit your needs.

- `sendPushoverMessage(pushoverBuilder("test message"))` 
- `sendPushoverMessage(pushoverBuilder("test message").withEmergencyPriority())` 
- `sendPushoverMessage(pushoverBuilder("test message").withAttachment("/path/to/file.jpeg"))` 
- `sendPushoverMessage(pushoverBuilder("test message").withAttachment("/path/to/file.png").withContentType("image/png"))` 
- `sendPushoverMessage(pushoverBuilder("test message").withApiKey("KEY").withUser("USER").withDevice("DEVICE").withTitle("TITLE").withUrl("URL").withUrlTitle("URL_TITLE").withPriority(0).withSound("SOUND").withAttachment("/path/to/file.png").withContentType("image/png"))`

### Emergency example

```
var String receipt = sendPushoverMessage(pushoverBuilder("Attention, front door opened!").withEmergencyPriority())

// wait for your cancel condition

if( receipt !== null ) {
    cancelPushoverEmergency(receipt)
    receipt = null
}
```
