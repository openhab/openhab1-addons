# Twitter Actions

Twitter can be used to "tweet" notifications when certain events take place in openHAB.


## Actions

*   `sendTweet(message)`: Send a Tweet
*   `sendDirectMessage('recipient', 'direct message')`: Send a direct message simply use the following syntax inside a rule or script;


## Configuration

The Twitter action service is one of the pre-defined set of actions that is automatically available, and can be used within rules and scripts.
You will need to enable "tweeting" by adding the following to the file `services/twitter.cfg`:

```
enabled=true
```

After enabling, you will then need to authenticate openHAB with Twitter.
Typically a new Twitter account would be set up for openHAB, rather than using your personal account, but that is entirely up to you.

## Prerequisites

You'll have to authorise openHAB to use Twitter.
This is done using a two step process, similar to Dropbox authentication.
openHAB requests a token which is used as a one-time-password to get hold of an authentication token (second step) which will be used for all future requests to Twitter.

### Step 1

This is performed by openHAB automatically after configuration above.
You will find some entries like this in your `openhab.log` file:

```text
08:46:10.013 INFO  o.openhab.io.net.actions.Tweet[:136] - ################################################################################################
08:46:10.014 INFO  o.openhab.io.net.actions.Tweet[:137] - # Twitter-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!
08:46:10.014 INFO  o.openhab.io.net.actions.Tweet[:138] - # 1. Open URL 'http://api.twitter.com/oauth/authorize?oauth_token=hP9gKIQ4wfMrzpmzqp4NcTJjAxXdwLo9fFEcndkks'
08:46:10.014 INFO  o.openhab.io.net.actions.Tweet[:139] - # 2. Grant openHAB access to your Twitter account
08:46:10.014 INFO  o.openhab.io.net.actions.Tweet[:140] - # 3. Create an empty file 'twitter.pin' in your openHAB install path
08:46:10.015 INFO  o.openhab.io.net.actions.Tweet[:141] - # 4. Add the line 'pin=<authpin>' to the twitter.pin file
08:46:10.015 INFO  o.openhab.io.net.actions.Tweet[:142] - # 5. openHAB will automatically detect the file and complete the authentication process
08:46:10.015 INFO  o.openhab.io.net.actions.Tweet[:143] - # NOTE: You will only have 5 mins before openHAB gives up waiting for the pin!!!
08:46:10.015 INFO  o.openhab.io.net.actions.Tweet[:144] - ################################################################################################
```

### Step 2

This steps needs your interaction.
Copy the given URL to your browser and authorize openHAB to use Twitter.
Be aware that the request token is only valid for the next five minutes, so don't wait too long.
After successful authorization, you will be given an authentication PIN which you will need to copy to a new file you create in the `<openhabhome>` directory called `twitter.pin`.
Please note that the PIN must be given in this format: `pin=<authpin>`.

### Step 3

This step is handled automatically by openHAB.
It will detect the PIN file you just created and use it to generate an authentication token which it will store away for future use.
openHAB will give up waiting after 5 minutes, so if you are not quick enough you will have to restart openHAB and begin the authentication process again.

Once authenticated, you should see an entry in the log file to indicate authentication was successful.

```text
08:53:53.486 INFO  o.openhab.io.net.actions.Twitter[:94] - TwitterAction has been successfully authenticated > awaiting your Tweets!
```

And you will notice a new file is created in `etc` called `twitter.token`.
This is where the authentication token is stored for future use.
You can now safely delete the `twitter.pin` file if you wish as it is no longer required.

## Rate Limits

Please beware there are technical limits that apply to your account.
The current limits are:

*   Direct messages: 250 per day.
*   Tweets: 1,000 per day. The daily update limit is further broken down into smaller limits for semi-hourly intervals. Retweets are counted as Tweets.
*   Following (daily): The technical follow limit is 1,000 per day. Please note that this is a technical account limit only, and there are additional rules prohibiting aggressive following behavior. Details about following limits and prohibited behavior are on the Follow Limits and Best Practices page.
*   Following (account-based): Once an account is following 2,000 other users, additional follow attempts are limited by account-specific ratios. The Follow Limits and Best Practices page has more information.
