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
import java.util.Collection;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Vladimir Pavluk
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleStatusResponse extends AbstractMessage implements Response {
  @JsonProperty("PageSize")
  public int pageSize;
  @JsonProperty("Offset")
  public int offset;
  @JsonProperty("TotalRows")
  public int totalRows;
  @JsonProperty("Data")
  public Collection<GenericMojioResponse> vehicles;
}
