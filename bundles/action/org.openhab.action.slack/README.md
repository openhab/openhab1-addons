# Slack Action

The Slack action sends messages to a [Slack](https://slack.com/) channel or Slack user from a rule or script.


## Prerequisites

As described on the [Slack page](https://my.slack.com/services/new/bot) you first need to create a bot.
After that you can use the authToken in the openHAB configuration (see below)


## Install

One way of installing would be to simply copy the org.openhab.action.slack*.jar to the OpenHAB addons folder
(e.g. for OpenHAB2 this would be /usr/share/openhab2/addons)


## Configuration
 
The action can be configured in `services/slack.cfg`.


### Configuration example

```
authToken = your-bot-token-here
```


## Examples


### Send a text message to a Slack channel

slack.rules

```
rule "Send text message to Slack channel 'general'"
when
    Item Foo changed
then
    sendToSlackChannel("Item Foo changed", "general")
end    
```


### Send a text message to a Slack user

slack.rules

```
rule "Send text message to Slack user 'userxyz'"
when
    Item Foo changed
then
     sendToSlackUser ("Item Foo changed", "userxyz")
end    
```
