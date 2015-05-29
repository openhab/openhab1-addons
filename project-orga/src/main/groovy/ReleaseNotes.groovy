//#!/usr/bin/env groovy

import groovy.json.JsonSlurper
import java.text.SimpleDateFormat


final def MILESTONE = '7' // v.1.7.0
final def BASE_URL = 'https://api.github.com'
final def LABELS_ENHANCEMENT = 'enhancement'
final def LABELS_NEW_BINDING = 'new-binding-action-service'
final def LABELS_BUG = 'bug'

// fetches a Github API URL and parses the result as JSON
def fetch(addr, params = [:]) {
  def auth = "b0c3670c66ff7a5aed52604a3b2df545f46c6e64"
  def json = new JsonSlurper()
  return json.parse(addr.toURL().newReader(requestProperties: ["Authorization": "token ${auth}".toString(), "Accept": "application/json"]))
}

// start building the email body
def body = new StringBuilder()

// fetch the list of Enhancements
body.append("####Major Features:\n")
fetch("${BASE_URL}/repos/openhab/openhab/issues?milestone=${MILESTONE}&state=closed&labels=${LABELS_NEW_BINDING}").each { issue ->
  body.append("* [#${issue.number}](${issue.html_url}) - ${issue.title} (@${issue.user.login})\n")
}
body.append("\n")

// fetch the list of Enhancements
body.append("####Enhancements:\n")
fetch("${BASE_URL}/repos/openhab/openhab/issues?milestone=${MILESTONE}&state=closed&labels=${LABELS_ENHANCEMENT}").each { issue ->
  body.append("* [#${issue.number}](${issue.html_url}) - ${issue.title} (@${issue.user.login})\n")
}
body.append("\n")

// fetch the list of Bugfixes
body.append("####Bugfixes:\n")
fetch("${BASE_URL}/repos/openhab/openhab/issues?milestone=${MILESTONE}&state=closed&labels=${LABELS_BUG}").each { issue ->
  body.append("* [#${issue.number}](${issue.html_url}) - ${issue.title} (@${issue.user.login})\n")
}
body.append("\n")

print body