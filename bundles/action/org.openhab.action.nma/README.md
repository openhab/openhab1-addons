# NotifyMyAndroid Actions

Sends push messages to your Android devices.

## Actions

-   `notifyMyAndroid(String event, String description)`: Send a message to the pre-configured api key (account) and use the configured or default values for the other parameters. Event is the notification title, and description the details.
-   `notifyMyAndroid(String event, String description, int priority)`: Send a message to a the pre-configured api key (account) with priority level (-2 to 2, from very low to emergency) and use the configured or default values for the other parameters.
-   `notifyMyAndroid(String apiKey, String event, String description)`: Send a message to another api key than the configured or use this method if you have not configured a default api key
-   `notifyMyAndroid(String apiKey, String event, String description, int priority)`: Send a message to another api key than the configured or use this method if you have not configured a default api key
-   `notifyMyAndroid(String apiKey, String event, String description, int priority, String url)`: Send a message to another api key than the configured or use this method if you have not configured a default api key
-   `notifyMyAndroid(String apiKey, String event, String description, int priority, String url, boolean html)`: Send a message overwriting all configured parameters and using the specified values.

## Configuration

This action service can be configured via the `services/nma.cfg` file.

| Property        | Default                                          | Required                                         | Description                                                                                        |
|-----------------|--------------------------------------------------|:------------------------------------------------:|----------------------------------------------------------------------------------------------------|
| timeout         | 10000                                            | No                                               | Timeout for the communication with the NMA service                                                 |
| developerKey    |                                                  | No                                               | An optional developer key from NMA                                                                 |
| apiKey          |                                                  | if using action calls without `apiKey` parameter | Default API key to send messages to. API keys can be created in your account's dashboard.          |
| appName         | openHAB                                          | No                                               | Application name which NMA will show                                                               |
| defaultPriority | 0                                                | No                                               | Priority to use for messages if not specified otherwise. Can range from -2 (lowest) to 2 (highest) |
| defaultUrl      | https://www.notifymyandroid.com/publicapi/notify | No                                               | URL to attach to NMA messages by default if not specified otherwise. Can be left empty.            |
