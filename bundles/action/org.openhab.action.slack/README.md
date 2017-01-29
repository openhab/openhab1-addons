# OpenHab Slack Action
Send messages to a Slack channel or user from an OpenHab rule or script.

## Install
One way of installing would be to simply copy the org.openhab.action.slack*.jar to the OpenHab addons folder
(e.g. for OpenHab2 this would be /usr/share/openhab2/addons)

## Config
In openhab.cfg or services/slack.cfg 

    authToken = your-bot-token-here

This is the token you get when you create a new bot in Slack. 
See https://my.slack.com/services/new/bot

## Usage
In your script / rules:
    
    sendToSlackChannel("Hello from OpenHab", "general")
    
    sendToSlackUser ("Hello from OpenHab", "userx")

## Designer
TBD: make the action available in the Designer


###### Contact
Author: R. van Wijngaarden (Rino.van.Wijngaarden@gmail.com)
