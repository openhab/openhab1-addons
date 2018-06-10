# XMPP Actions

This set of actions allows you to send a message to an XMPP user or multi-user chat, or send a message with an attachment to an XMPP user.

## Actions

- `sendXMPP(String to, String message)`: Sends a message to an XMPP user
- `sendXMPP(String to, String message, String attachmentUrl)`: Sends a message with an attachment to an XMPP user
- `chatXMPP(String message)`: Sends a message to a multi user chat

## Configuration

The XMPP Action service can be configured in `services/xmpp.cfg`.

| Property     | Default       | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|--------------|---------------|:--------:|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| username     |               |          | The username of the XMPP account used by openHAB. Most services will require that you use only the localpart of the account's JID. For example if your account's JID is `myuser@example.org`, then only configure `myuser`.                                                                                                                                                                                                                                                                                                                                                                    |
| password     |               |          | The password of the XMPP account used by openHAB                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| servername   |               |          | The XMPP service to use, e.g. `jabber.de`. A list of public XMPP services can be found at https://xmpp.net/directory.php                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| securitymode | `disabled`    |       No | The Security mode used for the XMPP connection. Can be either `required` or `disabled`. Defaults to `disabled`, which means that TLS will not be used.  Warning: If you change this to non-disabled, then you must make sure that your TLS server certificate can be validated, otherwhise the connection will fail.                                                                                                                                                                                                                                                                          |
| tlspin       |               |          | The TLS Pin used to verify the XMPP service's certificate. Set this in case openHAB's default `SSLContext` is unable to verify it (e.g. because the XMPP service uses a self-signed certificate). The PIN value is basically the hash of the certificate in hexadecimal notation. Set `securitymode` to `required` to enable TLS for XMPP connections. PIN generation is [discussed in detail here](https://github.com/Flowdalic/java-pinning) or see [this example](#Example_Generate_PIN). Example: `tlspin=CERTSHA256:83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3` |
| proxy        |               |          | The XMPP Proxyserver to use, e.g. `gmail.com`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| port         | `5222`        |          | The server port to use                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| consoleusers |               |          | A comma-separated list of users that are allowed to use the XMPP console                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| chatroom     |               |          | The multi user chat to join, e.g. `openhab@chat.example.com`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| chatnickname | `openhab-bot` |       No | The nickname used in the multi-user chat                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| chatpassword |               |          | The password required to join the multi user chat                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |

Note: openHAB does not resolve SRV entries like other XMPP clients do, you have to setup the server details manually.
Generally, if `joe@example.org` is your XMPP user ID and `xmpp.example.net` points to the server running the service, set `servername` to the actual server `xmpp.example.net`, the user name `username` to `joe` and `proxy` to the domain name part of your user ID `example.org`.

## Example: Google

```java
servername=talk.google.com
securitymode=required
# You need this "tlspin" if openhab cannot verify the certificate from the google server
tlspin=CERTSHA256:9e670d6624fc0c451d8d8e3efa81d4d8246ff9354800de09b549700e8d2a730a
proxy=gmail.com
username=my.openhab@gmail.com
password=mysecret
# you may need to add the cryptic talk.google.com address of your private google account to the allowed users
# check you openhab.log to found the address after you send something via Hangouts to your openHAB account
consoleusers=**cryptic**@public.talk.google.com,myname@gmail.com
```

## Example: Generate PIN

Assuming the PEM-encoded certificate is in a file called `jabber.crt` (it is PEM encoded if it contains the lines `BEGIN CERTIFICATE` and `END CERTIFICATE` with lots of dashes in them)  this shell invocation prints the required value for the `tlspin=` configuration setting:

```bash
$ echo "CERTSHA256:$(openssl x509 -in "jabber.crt" -noout -fingerprint -sha256 | sed 's/.*=//')"
CERTSHA256:D8:8E:B1:....:7D:84
```

You then copy the whole last line into `tlspin`.
