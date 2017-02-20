# MailControl Binding

This binding provides possibility to receive commands sent via email in JSON format.  

> NOTE: This binding needs close review; please use caution.

Commands of the following types can be sent:

* decimal
* HSB
* increase - decrease
* on - off
* open - closed
* percent
* stop - move
* string
* up - down

Commands are parse and published by event publisher.

MailControl binding is getting use of the following libraries:

1. [access-email-1.0.2.jar](https://github.com/apereverzin/access-email) which needs the following jars: mail-1.4.jar, activation-1.1.jar
2. [openhab-mailcontrol-model-1.0.0.jar](https://github.com/apereverzin/openhab-mailcontrol-model) which needs json-simple-1.1.jar

MailControl binding allows to send commands to the OpenHAB home server remotely without any additional server running somewhere else (for example, in a cloud).

## Binding Configuration

This binding can be configured in `services/mailcontrol.cfg`.

Example of configuration properties for the Message Control binding:

```
username=email.address@some.com
password=XXXXXXXXXX
smtphost=smtp.mail.some.com
smtpport=587
smtpauth=true
smtpstarttls=true
smtpsocketfactoryport=995
pop3host=pop.mail.some.com
pop3port=995
pop3socketfactoryport=995
pop3socketfactoryclass=javax.net.ssl.SSLSocketFactory
```

## Examples of Messages

The subject of an email message must be OpenHAB. (using the incorrect case of letters)

Examples of messages for different types of commands:

```
decimal: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"commandType":"DECIMAL","value":"1.2"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

HSB: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"brightness":30,"saturation":50,"commandType":"HSB","hue":150},"item_id":"Item"},"senderEmail":"email.address@some.com"}

increase: decrease: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"commandType":"INCREASE_DECREASE","value":"INCREASE"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

on - off: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"commandType":"ON_OFF","value":"ON"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

open - closed: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"value":"OPEN","commandType":"OPEN_CLOSED"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

percent: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"value":"12","commandType":"PERCENT"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

stop - move: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"commandType":"STOP_MOVE","value":"STOP"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

string: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"value":"someValue","commandType":"STRING"},"item_id":"Item"},"senderEmail":"email.address@some.com"}

up - down: {"messageType":"110","productVersion":"1.0","itemCommand":{"timeSent":"0","command":{"value":"UP","commandType":"UP_DOWN"},"item_id":"Item"},"senderEmail":"email.address@some.com"}
```