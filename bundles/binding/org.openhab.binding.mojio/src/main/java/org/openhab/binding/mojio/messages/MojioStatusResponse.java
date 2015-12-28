package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MojioStatusResponse extends AbstractMessage implements Response {
  @JsonProperty("PageSize")
  public int pageSize;
  @JsonProperty("Offset")
  public int offset;
  @JsonProperty("TotalRows")
  public int totalRows;
  @JsonProperty("Data")
  public Collection<GenericMojioResponse> mojios;
}
