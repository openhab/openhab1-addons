# Cisco Spark Actions

[Cisco Spark](https://www.ciscospark.com) can be used to send messages to groups or individuals 
when certain events take place in openHAB. 

Messages in the Cisco Spark plugin support Markdown text.


## Actions

* `sparkMessage('message', 'roomId')`: Send a message to a specific room.
* `sparkMessage('message')`: Send a message to the default room (from config)
* `sparkPerson('message', 'personEmail)`: Send a direct message to a person

## Configuration

The Cisco Spark action service is one of the pre-defined set of actions that is automatically 
available, and can be used within rules and scripts. Before you can use Cisco Spark you need 
to register your application with the Cisco Spark Developers service.

### Register your application

Login and navigate to Cisco [Spark for Developers](https://developer.ciscospark.com/apps.html).  
You are presented with two options: create an integration or a Bot.  Select 'Create a Bot'.

Follow the screens to create your bot.  Once done you'll be presented with an access token.

### Configure openhab

Configure openhab with the access token by adding the following to the file `services/ciscospark.cfg`:

```
    accessToken=<< token from Spark for Developers >>
```

This is enough to use the `sparkMessage('message', 'roomId')` action.  There are some additional
configurations that are provided for your convenience, which unlock additional actions.

```
defaultRoomId=<< UUID of room to use with sparkMessage('message') >>
```


