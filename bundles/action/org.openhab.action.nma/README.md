# NotifyMyAndroid Actions

Sends push messages to your Android devices.

## Actions

- `notifyMyAndroid(String event, String description)`: Send a message to a the pre configured api key (account) and use the configured or default values for the other parameters
- `notifyMyAndroid(String apiKey, String event, String description)`: Send a message to another api key than the configured or use this method if you have not configured a default api key
- `notifyMyAndroid(String apiKey, String event, String description, int priority, String url, boolean html)`: Send a message overwriting all configured parameters and using the specified values.

## Configuration

This action service can be configured via the `services/nma.cfg` file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| timeout | 10000 | | Timeout for the communication with the NMA service |
| developerKey | | | An optional developer key from NMA |
| apiKey | | | Default API key to send messages to. API keys can be created in your account's dashboard. |
| appName | openHAB | | Application name which NMA will show |
| defaultPriority | | | Priority to use for messages if not specified otherwise. Can range from -2 (lowest) to 2 (highest) |
| defaultUrl | | | URL to attach to NMA messages by default if not specified otherwise. Can be left empty. |
