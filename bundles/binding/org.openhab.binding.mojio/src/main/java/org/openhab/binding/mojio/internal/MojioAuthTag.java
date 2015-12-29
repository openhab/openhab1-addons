/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.internal;

import org.openhab.binding.mojio.messages.AuthorizeRequest;
import org.openhab.binding.mojio.messages.AuthorizeResponse;
import org.openhab.binding.mojio.messages.MojioTimestamp;
import java.util.Date;

/**
 * MojioAuthTag class stores MojIO credentials and the AuthTag along with the expiration date.
 * When the tag expires, it updates the tag.
 *
 * @author Vladimir Pavluk
 * @since 1.0
 */
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
