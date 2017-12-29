# Cisco Spark Action


Cisco Spark can be used to message individuals are *rooms* when certain events take place in openHAB.


## Configuration

Configuration is very easy and only required an access token.
To obtain this token, first log in to [Spark for Developers](https://developer.ciscospark.com/add-app.html) and add a new App.
Make sure to select 'Create a Bot' and fill in the required fields.
Once completed, you will be presented with an access token.

Next, configure openHAB with the access token by adding following line to the file `services/ciscospark.cfg`:

    accessToken= << access token from Spark for Developers >>

There's an optional convenience configuration option that allows you to set a default room to which you want Spark to send messages so you don't have to include it in each action request.

    defaultRoomId= << UUID of the default room >>

Note: you can find the uuids for rooms when using the [web client for Cisco Spark](https://web.ciscospark.com).
When you navigate to a room, the uuid can be copied from the browsers location bar.
The uuid looks like this:
`24c617f0-fbe4-11e5-be0f-2fe93bbeddd9`

## Actions

Send a message to a specific room

    sparkMessage('message', 'roomId')

Send a message to the default room (from config)

    sparkMessage('message')

Send a direct message to a person

    sparkPerson('message', 'personEmail')
