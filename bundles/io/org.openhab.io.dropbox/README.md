# Dropbox Synchronization Service

This service will synchronize files on your openHAB server, such as configuration and log files, to and/or from your Dropbox account.

The intended main use cases are backing up openHAB configuration and log files to a version-able cloud space and transporting changed files back to openHAB after editing them with the openHAB Designer on the administrator's desktop PC.

> NOTE: This service is currently disabled, due to [#4588](https://github.com/openhab/openhab1-addons/issues/4588).

## Service Configuration

This service can be configured in the file `services/dropbox.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| appkey   | `gbrwwfzvrw6a9uv` |  Yes due to [#4588](https://github.com/openhab/openhab1-addons/issues/4588) | the default app key is defunct and code changes are necessary. |
| appsecret | `gu5v7lp1f5bbs07` | Yes due to [#4588](https://github.com/openhab/openhab1-addons/issues/4588) | the default app secret is defunct and code changes are necessary. |
| fakemode |  `false` |   No    | operates the synchronizer in fake mode which avoids uploading files to or downloading files from Dropbox. Set to `true` as a test mode for the filter settings. |
| contentdir | openHAB configuration directory | No | the base directory to synchronize with openHAB, configure `uploadFilter` and `downloadFilter` to select files |
| uploadInterval | `0 0 2 * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for uploading changes to Dropbox.  The default schedule uploads changes every day at 2am. |
| downloadInterval | `0 0/5 * * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for downloading changes from Dropbox.  The default schedule downloads any changes every five minutes. |
| syncmode | `LOCAL_TO_DROPBOX` | No | There are three different synchronization modes available:<br/><br/>`DROPBOX_TO_LOCAL` - changed files will be downloaded from your Dropbox to openHAB only<br/>`LOCAL_TO_DROPBOX` - locally changed files will be uploaded to your Dropbox only<br/>`BIDIRECTIONAL` - files will be synchronized from Dropbox to your local openHAB and vice versa. All changes will be downloaded from Dropbox to your local system first. After that any local changes will be uploaded to Dropbox. Hence a concurrent change to one file will be overruled by Dropbox in any case.<br/><br/>In case your Dropbox returns the `reset` flag, all local files will be uploaded to your Dropbox once, even if your synchronization mode is set to `DROPBOX_TO_LOCAL`. The `reset` flag might be returned either if the synchronization has been the first call ever (so no delta cursor is available) or there might be technical issues at Dropbox which causes the connected clients to resynchronize their states with Dropbox again. |
| uploadfilter | `^([^/]*/){1}[^/]*$,/configurations.*,/logs/.*,/etc/.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |
| downloadfilter | `^([^/]*/){1}[^/]*$,/configurations.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |

## Authorize openHAB

You'll have to authorize openHAB to connect to your Dropbox. This is done in a three-step process. openHAB requests a token which is used as a one-time-password to obtain an access token (second step) which will be used for all future requests against Dropbox.

### Step 1: Monitor Log for Authorization Message

This service issues a message to the log automatically on first startup. You will find some log entries (also in the console) containing entries like these:

```text
 #########################################################################################
 # Dropbox-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!
 # 1. Open URL 'https://www.dropbox.com/1/oauth2/_type=code'
 # 2. Allow openHAB to access Dropbox
 # 3. Paste the authorisation code here using the command 'dropbox:finishAuthentication "<token>"'
 #########################################################################################
```

### Step 2: Obtain Authorization Token

Copy the given URL to your browser and authorize openHAB to use Dropbox in the future. Be aware that the request token is only valid for the next five minutes, so don't be to placid.

After successful authorization, a token is shown on the Dropbox web page:

![](https://github.com/openhab/openhab1-addons/wiki/images/screenshots/dropbox-authorization.png)

### Step 3: Save Token in openHAB Console

Access the openHAB console to reach the `openhab>` prompt.  One way you can access the openHAB 2 console from the server with:

```shell
ssh openhab@localhost -p 8101
```

The default password is `habopen`.  If this is the first time accessing your console, it may take some time to generate cryptographic keys.

Copy the token shown on the Dropbox Web page and issue the following command 

```text
dropbox:finishAuthentication "replace with the token"
```
