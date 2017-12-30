# Pushbullet Actions

The Pushbullet action allows you to notify iOS, Android & Windows 10 Phone & Desktop devices of a message using the Pushbullet API web service.

## Actions

The following is a valid action call that can be made when the plugin is loaded.
For specific information on each item, see the [Pushbullet API](https://docs.pushbullet.com/).

*   `sendPushbulletNote(String receiver, String title, String message)`
*   `sendPushbulletNote(String botname, String receiver, String title, String message)`

## Examples

```
sendPushbulletNote("mybot", "someone@somewhere.com", "this is the title", "And this is the body of the message")
```

## Creating an account for your bot(s)

The pushbullet accounts are bound to either Google or Facebook accounts.
*   Go to "<https://www.pushbullet.com/>"
*   Chose to either "Sign up with Google" or "Sign up with Facebook".
*   Complete the signup process as guided by the pushbullet web site.
*   Continue with "Obtaining an API key".

## Obtaining an API key

The API keys are bound to the pushbullet account.
*   Go to the pushbullet site.
*   Log in with either your personal account or the one you created for your bot.
*   Go to "<https://www.pushbullet.com/#settings/account>"
*   Click on "Create Access Token".
*   Copy the token created on the site.

You must at least provide an API token (Private or Alias Key from Pushbullet.com) and a message in some manner before a message can be pushed.
All other parameters are optional.
If you use an alias key, the parameters (device, icon, sound, vibration) are overwritten by the alias setting on pushbullet.

## Rate limits

As of June 2017, free accounts have a limit of 500 pushes per month.
This action does not evaluate the rate limiting headers though.

## Libraries

This action has been written without using libraries as jpushbullet or jpushbullet2.
Both of those libraries use various libraries themselves which makes integrating them into openHAB a challenge.

## pushbullet API

*   <https://docs.pushbullet.com/>
*   <https://docs.pushbullet.com/#push-limit>
