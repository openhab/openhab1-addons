# Mail Actions

This add-on provides SMTP services. The `to` paremeter can contain a semicolon-separated list of email addresses.

## Actions

- `sendMail(String to, String subject, String message)`: Sends an email via SMTP.
- `sendMail(String to, String subject, String message, String attachmentUrl)`: Sends an email with attachment via SMTP.
- `sendMail(String to, String subject, String message, List<String> attachmentUrlList)`: Sends an email with one or more attachments via SMTP.  

## Configuration

This action service can be configured via the `services/mail.cfg` file.

| Key | Default | Required | Description |
|-----|---------|----------|-------------|
| hostname |    | Yes      | SMTP server hostname, e.g. "smtp.gmail.com" |
| port | 25 (resp. 587 for TLS/SSL) | | SMTP port to use |
| username | | | user name if the SMTP server requires authentication |
| password | | | password if the SMTP server requires authentication |
| from | | Yes | Email address to use for sending mails |
| tls | false | | set to `true` if STARTTLS is enabled (not required) for the connection |
| ssl | false | | set to `true` if SSL negotiation should occur on connection do not set both `tls` and `ssl` to `true` |
| popbeforesmtp | false | | set to `true` if POP before SMTP (another authentication mechanism) should be enabled. Username and password are taken from the above configuration |
| charset | platform default | | Character set used to encode the message body |

## Examples

```
import java.util.List
...
val List<String> attachmentUrlList = newArrayList(
  "http://some.web/site/snap.jpg&param=value",
  "http://192.168.1.100/data.txt",
  "file:///tmp/201601011031.jpg")
sendMail("you@email.net", "Test", "This is the message.", attachmentUrlList)
```
