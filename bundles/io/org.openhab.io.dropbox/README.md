# Dropbox Synchronization Service

This service will synchronize files on the openHAB server, such as configuration and log files, to and/or from a Dropbox account.

The main use case is backing up openHAB configuration and log files
to a version-able cloud space and transporting changed files back to openHAB
after editing them with the openHAB Designer on the administrator's desktop PC.


## Service Configuration

This service can be configured in the file `services/dropbox.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| personalAccessToken | | Yes   | This is the generated access token; see instructions below |
| fakemode |  `false` |   No    | operates the synchronizer in fake mode which avoids uploading files to or downloading files from Dropbox. Set to `true` as a test mode for the filter settings. |
| contentdir | openHAB configuration directory | No | the base directory to synchronize with openHAB, configure `uploadFilter` and `downloadFilter` to select files |
| uploadInterval | `0 0 2 * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for uploading changes to Dropbox.  The default schedule uploads changes every day at 2am. |
| downloadInterval | `0 0/5 * * * ?` | No | a [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06) to set the schedule for downloading changes from Dropbox.  The default schedule downloads any changes every five minutes. |
| syncmode | `LOCAL_TO_DROPBOX` | No | There are three different synchronization modes available:<br/><br/>`DROPBOX_TO_LOCAL` - changed files will be downloaded from your Dropbox to openHAB only<br/>`LOCAL_TO_DROPBOX` - locally changed files will be uploaded to your Dropbox only<br/>`BIDIRECTIONAL` - files will be synchronized from Dropbox to your local openHAB and vice versa. All changes will be downloaded from Dropbox to your local system first. After that any local changes will be uploaded to Dropbox. Hence a concurrent change to one file will be overruled by Dropbox in any case.<br/><br/>In case your Dropbox returns the `reset` flag, all local files will be uploaded to your Dropbox once, even if your synchronization mode is set to `DROPBOX_TO_LOCAL`. The `reset` flag might be returned either if the synchronization has been the first call ever (so no delta cursor is available) or there might be technical issues at Dropbox which causes the connected clients to resynchronize their states with Dropbox again. |
| uploadfilter | `^([^/]*/){1}[^/]*$,/configurations.*,/logs/.*,/etc/.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |
| downloadfilter | `^([^/]*/){1}[^/]*$,/configurations.*` | No | The defaults are specific to openHAB 1.x running on Unix-like systems |


## Dropbox App

An app must be created on Dropbox in order to get a generated access token.

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


## Prior versions

Installations that used the Dropbox Synchronization Service in earlier versions
of openHAB may encounter this error:

>[WARN ] [d.internal.DropboxSynchronizer] - Synchronizing data with Dropbox throws an exception: {"error": "Invalid \"cursor\" parameter: this cursor is for a different app."}

To eliminate this error, delete the `deltacursor.dbx` file. It should be in the same folder as the openHAB startup scripts.
