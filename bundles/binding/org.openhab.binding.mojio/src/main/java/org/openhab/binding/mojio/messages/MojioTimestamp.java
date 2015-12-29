/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

/**
 * MojioStatusResponse represents Mojio timestamp type parser/converter.
 *
 * @author Vladimir Pavluk
 * @since 1.0
 */
public class MojioTimestamp {
  private String timestamp;

  public MojioTimestamp(final String ts) {
    timestamp = ts;
  }

  public Date toDate() {
    return Timestamp.valueOf(this.timestamp.replaceAll("T", " ").replaceAll("Z.*", ""));
  }

  public String toString() {
    return timestamp;
  }
}
