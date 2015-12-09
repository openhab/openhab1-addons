/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The Camera Java Bean represents a Nest Cam device. All objects relate in one way or another to a real Nest Cam. The
 * Camera class defines the real camera device, from the API perspective.
 * 
 * @see <a href="https://developer.nest.com/documentation/api-reference">API Reference</a>
 * @author John Cocula
 * @since 1.8.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Camera extends AbstractDevice {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event extends AbstractMessagePart {
		private Boolean has_sound;
		private Boolean has_motion;
		private Date start_time;
		private Date end_time;
		private Date urls_expire_time;
		private String web_url;
		private String app_url;
		private String image_url;
		private String animated_image_url;

		/**
		 * @return true if sound event - sound was detected.
		 */
		@JsonProperty("has_sound")
		public Boolean getHas_sound() {
			return this.has_sound;
		}

		/**
		 * @return true if motion event - motion was detected.
		 */
		@JsonProperty("has_motion")
		public Boolean getHas_motion() {
			return this.has_motion;
		}

		/**
		 * @return event start time.
		 */
		@JsonProperty("start_time")
		public Date getStart_time() {
			return this.start_time;
		}

		/**
		 * @return event end time.
		 */
		@JsonProperty("end_time")
		public Date getEnd_time() {
			return this.end_time;
		}

		/**
		 * @return timestamp that identifies when the last event URLs expire.
		 */
		@JsonProperty("urls_expire_time")
		public Date getUrls_expire_time() {
			return this.urls_expire_time;
		}

		/**
		 * @return Web URL (deep link) to the last sound or motion event at home.nest.com. Used to display the recorded
		 *         event from the camera at that physical location (where). NOTE: If the event URL has expired or the
		 *         device does not have an active subscription, then this value is not included in the payload.
		 */
		@JsonProperty("web_url")
		public String getWeb_url() {
			return this.web_url;
		}

		/**
		 * @return Nest App URL (deep link) to the last sound or motion event. Used to display the recorded event from
		 *         the camera at that physical location (where). NOTE: If the event URL has expired or the device does
		 *         not have an active subscription, then this value is not included in the payload.
		 */
		@JsonProperty("app_url")
		public String getApp_url() {
			return this.app_url;
		}

		/**
		 * @return URL (link) to the image file captured at the start time of a sound or motion event.
		 */
		@JsonProperty("image_url")
		public String getImage_url() {
			return this.image_url;
		}

		/**
		 * @return URL (link) to the gif file captured at the start time of a sound or motion event.
		 */
		@JsonProperty("animated_image_url")
		public String getAnimated_image_url() {
			return this.animated_image_url;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("has_sound", this.has_sound);
			builder.append("has_motion", this.has_motion);
			builder.append("start_time", this.start_time);
			builder.append("end_time", this.end_time);
			builder.append("urls_expire_time", this.urls_expire_time);
			builder.append("web_url", this.web_url);
			builder.append("app_url", this.app_url);
			builder.append("image_url", this.image_url);
			builder.append("animated_image_url", this.animated_image_url);
			return builder.toString();
		}
	}

	private Boolean is_streaming;
	private Boolean is_audio_input_enabled;
	private Date last_is_online_change;
	private Boolean is_video_history_enabled;
	private String web_url;
	private String app_url;
	private Event last_event;

	public Camera(@JsonProperty("device_id") String device_id) {
		super(device_id);
	}

	/**
	 * @return true Camera status, either on and actively streaming video, or off.
	 */
	@JsonProperty("is_streaming")
	public Boolean getIs_streaming() {
		return this.is_streaming;
	}

	/**
	 * Turn the camera off or on
	 */
	@JsonProperty("is_streaming")
	public void setIs_streaming(Boolean streaming) {
		this.is_streaming = streaming;
	}

	/**
	 * return true if camera microphone is on and listening, else return false.
	 */
	@JsonProperty("is_audio_input_enabled")
	public Boolean getIs_audio_input_enabled() {
		return this.is_audio_input_enabled;
	}

	/**
	 * @return the last change to the online status.
	 */
	@JsonProperty("last_is_online_change")
	public Date getLast_is_online_change() {
		return this.last_is_online_change;
	}

	/**
	 * @return Nest Aware subscription status.
	 */
	@JsonProperty("is_video_history_enabled")
	public Boolean getIs_video_history_enabled() {
		return this.is_video_history_enabled;
	}

	/**
	 * @return Web URL (deep link) to the device page at home.nest.com. Used to display the camera live feed at that
	 *         physical location (where).
	 */
	@JsonProperty("web_url")
	public String getWeb_url() {
		return this.web_url;
	}

	/**
	 * @return App URL (deep link) to the device screen in the Nest App. Used to display the camera live feed at that
	 *         physical location (where).
	 */
	@JsonProperty("app_url")
	public String getApp_url() {
		return this.app_url;
	}
	
	/**
	 * @return the last event
	 */
	@JsonProperty("last_event")
	public Event getLast_event() {
		return this.last_event;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("is_streaming", this.is_streaming);
		builder.append("is_audio_input_enabled", this.is_audio_input_enabled);
		builder.append("last_is_online_change", this.last_is_online_change);
		builder.append("is_video_history_enabled", this.is_video_history_enabled);
		builder.append("web_url", this.web_url);
		builder.append("app_url", this.app_url);
		builder.append("last_event", this.last_event);
		return builder.toString();
	}
}
