package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleLocation {
  @JsonProperty("Lat")
  public double latitude;
  @JsonProperty("Lng")
  public double longitude;
  @JsonProperty("FromLockedGPS")
  public boolean lockedGPS;
  @JsonProperty("Dilution")
  public double dilution;
  @JsonProperty("IsValid")
  public boolean isValid;
}
