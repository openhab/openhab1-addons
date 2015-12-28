package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

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
