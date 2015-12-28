package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MojioType extends GenericMojioResponse {
  @JsonProperty("Name")
  public String name;
  @JsonProperty("Imei")
  public String IMEI;
  @JsonProperty("VehicleId")
  public String vehicleId;
  @JsonProperty("OwnerId")
  public String ownerId;
}
