# Dropbox Synchronization Service

This service will synchronize files on the openHAB server, such as configuration and log files, to and/or from a Dropbox account.

The main use case is backing up openHAB configuration and log files
to a version-able cloud space and transporting changed files back to openHAB
after editing them with the openHAB Designer on the administrator's desktop PC.


## Service Configuration

This service can be configured in the file `services/dropbox.cfg`.

### Authentication settings

| Property            | Default | Required | Description |
|---------------------|---------|:--------:|-------------|
| personalAccessToken |         | Yes*     | The generated access token |
| appkey              |         | Yes*     | A valid appkey             |
| appsecret           |         | Yes*     | A valid appsecret          |

\* In order to authenticate with Dropbox, the configuration must include
**either** the `personalAccessToken` or **BOTH** the `appkey` AND the
`appsecret`.

The `personalAccessToken` is the newer method of authentication, and is
a simpler process, but the `appkey`/`appsecret` method would theoretically
work if configured.

If all three properties are defined in the configuration, the
`personalAccessToken` method will be used and the others ignored.


### General settings

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| fakemode | `false` |   No     | operates the synchronizer in fake mode which avoids sending files to or from Dropbox. Set to `true` as a test mode for the filter settings. |
| contentdir | openHAB configuration directory | No | the base directory to synchronize with openHAB |
| uploadInterval | `0 0 2 * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for uploading changes to Dropbox.  The default schedule uploads changes every day at 2am. |
| downloadInterval | `0 0/5 * * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for downloading changes from Dropbox.  The default schedule downloads any changes every five minutes. |
| syncmode | `LOCAL_TO_DROPBOX` | No | There are three different synchronization modes available:<br/><br/>`DROPBOX_TO_LOCAL` - changed files will be downloaded from your Dropbox to openHAB only<br/>`LOCAL_TO_DROPBOX` - locally changed files will be uploaded to your Dropbox only<br/>`BIDIRECTIONAL` - files will be synchronized from Dropbox to your local openHAB and vice versa. All changes will be downloaded from Dropbox to your local system first. After that any local changes will be uploaded to Dropbox. Hence a concurrent change to one file will be overruled by Dropbox in any case.<br/><br/>In case your Dropbox returns the `reset` flag, all local files will be uploaded to your Dropbox once, even if your synchronization mode is set to `DROPBOX_TO_LOCAL`. The `reset` flag might be returned either if the synchronization has been the first call ever (so no delta cursor is available) or there might be technical issues at Dropbox which causes the connected clients to resynchronize their states with Dropbox again. |
| uploadfilter | `^([^/]*/){1}[^/]*$,/configurations.*,/logs/.*,/etc/.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |
| downloadfilter | `^([^/]*/){1}[^/]*$,/configurations.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |


## Dropbox App

An app must be created on Dropbox.

### Creating an app

- Create an account in [Dropbox](http://www.dropbox.com)
- Navigate to the [developer section of the site](http://www.dropbox.com/developers)
- Click the "My apps" link in the left navigation menu, then click the "Create app" button
- In the next screen, select "Dropbox API" in step 1
- Select "Full Dropbox" in step 2 (This is probably not critical, as "App folder" may also work, but this has not been tested)
- Choose a name for your app in step 3 and click "Create app"
- The following screen will summarize your app; verify the "Allow implicit grant" setting is set to "Allow"
- Under the heading that says "Generated access token", click the "Generate" button to generate an access token that openHAB can use to connect with your app
- Copy the long code string that is generated and paste it into the openHAB configuration file as the value for the `personalAccessToken` setting shown above

[This tutorial](http://www.iperiusbackup.net/en/create-dropbox-app-get-authentication-token/)
also contains a video of the process.


## Troubleshooting

Installations that used the Dropbox Synchronization Service in earlier versions
of openHAB may encounter this error:

>[WARN ] [d.internal.DropboxSynchronizer] - Synchronizing data with Dropbox throws an exception: {"error": "Invalid \"cursor\" parameter: this cursor is for a different app."}

To eliminate this error, delete the `deltacursor.dbx` file. It should be in the same folder as the openHAB startup scripts.


## Authentication via appkey/appsecret

This is a three-step process. openHAB requests a token which is used as a
one-time-password to obtain an access token which will be used for all future
requests to Dropbox.

### Step 1: Monitor Log for Authorization Message

The following message will be sent to the log on first startup:

```
 #########################################################################################
 # Dropbox-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!
 # 1. Open URL 'https://www.dropbox.com/1/oauth2/_type=code'
 # 2. Allow openHAB to access Dropbox
 # 3. Paste the authorisation code here using the command 'dropbox:finishAuthentication "<token>"'
 #########################################################################################
```

### Step 2: Obtain Authorization Token

Copy the given URL to your browser and authorize openHAB to use Dropbox in
the future. Be aware that the request token is only valid for the next five
minutes.

After successful authorization, a token is shown on the Dropbox web page:

![](https://github.com/openhab/openhab1-addons/wiki/images/screenshots/dropbox-authorization.png)

### Step 3: Save Token in openHAB Console

Access the openHAB console to reach the `openhab>` prompt.  One way you can
access the openHAB 2 console:

```
ssh openhab@localhost -p 8101
```

The default password is `habopen`.  If this is the first time accessing your
console, it may take some time to generate cryptographic keys.

Copy the token shown on the Dropbox web page and issue the following command:

```
dropbox:finishAuthentication "replace with the token"
```
