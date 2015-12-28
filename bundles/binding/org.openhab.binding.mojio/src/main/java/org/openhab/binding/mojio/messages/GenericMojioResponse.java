package org.openhab.binding.mojio.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "Type")
@JsonSubTypes({  
    @Type(value = AuthorizeResponse.class, name = "Token"),  
    @Type(value = MojioType.class, name = "Mojio"),  
    @Type(value = VehicleType.class, name = "Vehicle") })
@JsonIgnoreProperties(ignoreUnknown = false)
public abstract class GenericMojioResponse extends AbstractMessage implements Response {

  @JsonProperty("_id")
	private String id;

  @JsonProperty("_deleted")
	private boolean deleted;

  @JsonProperty("_id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("_deleted")
  public boolean getDeleted() {
    return this.deleted;
  }

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("Id", this.id);
		builder.append("Deleted", this.deleted);
		return builder.toString();
  }
}
