package org.openhab.binding.mojio.internal;

import org.openhab.binding.mojio.messages.AuthorizeRequest;
import org.openhab.binding.mojio.messages.AuthorizeResponse;
import org.openhab.binding.mojio.messages.MojioTimestamp;
import java.util.Date;

public class MojioAuthTag {
  String appId;
  String appKey;
  String username;
  String password;
  String authToken = "";
  MojioTimestamp validUntil;

  MojioAuthTag(String appId, String appKey, String username, String password) {
    this.appId = appId;
    this.appKey = appKey;
    this.username = username;
    this.password = password;
    updateToken();
  }

  String getAuthToken() {
    Date now = new Date();
    if(now.compareTo(validUntil.toDate()) > 0) {
      updateToken();
    }
    return authToken;
  }

  void updateToken() {
    final AuthorizeRequest request = new AuthorizeRequest(appId, appKey, username, password);
    final AuthorizeResponse response = request.execute();
    authToken = response.getAuthToken();
    validUntil = response.getValidUntil();
  }
}
