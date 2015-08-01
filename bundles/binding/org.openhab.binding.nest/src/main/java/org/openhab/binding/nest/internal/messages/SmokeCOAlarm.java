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
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * The SmokeCOAlarm Java Bean represents a Nest smoke+CO alarm. All objects relate in one way or another to a real
 * smoke+CO alarm. The SmokeCOAlarm class defines the real smoke+CO device.
 * 
 * @see <a href="https://developer.nest.com/documentation/api-reference">API Reference</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmokeCOAlarm extends AbstractDevice {

	/**
	 * Battery life/health; estimate of time to end of life.
	 * 
	 * Possible values for battery_health:
	 * 
	 * <dl>
	 * <dt>ok</dt>
	 * <dd>Battery OK</dd>
	 * <dt>replace</dt>
	 * <dd>Battery low - replace soon</dd>
	 * </dl>
	 */
	public static enum BatteryHealth {
		OK("ok"), REPLACE("replace");

		private final String state;

		private BatteryHealth(String state) {
			this.state = state;
		}

		@JsonValue
		public String value() {
			return state;
		}

		@JsonCreator
		public static BatteryHealth forValue(String v) {
			for (BatteryHealth bs : BatteryHealth.values()) {
				if (bs.state.equals(v)) {
					return bs;
				}
			}
			throw new IllegalArgumentException("Invalid battery_state: " + v);
		}

		@Override
		public String toString() {
			return this.state;
		}
	}

	/**
	 * Possible values for *_alarm_state:
	 * 
	 * <dl>
	 * <dt>ok</dt>
	 * <dd>OK</dd>
	 * <dt>warning</dt>
	 * <dd>Warning - CO Detected</dd>
	 * <dt>emergency</dt>
	 * <dd>Emergency - * Detected - move to fresh air</dd>
	 * </dl>
	 */
	public static enum AlarmState {
		OK("ok"), WARNING("warning"), EMERGENCY("emergency");

		private final String state;

		private AlarmState(String state) {
			this.state = state;
		}

		@JsonValue
		public String value() {
			return state;
		}

		@JsonCreator
		public static AlarmState forValue(String v) {
			for (AlarmState as : AlarmState.values()) {
				if (as.state.equals(v)) {
					return as;
				}
			}
			throw new IllegalArgumentException("Invalid alarm_state: " + v);
		}

		@Override
		public String toString() {
			return this.state;
		}
	}

	/**
	 * Indicates device status by color in the Nest app UI; it is an aggregate condition for battery+smoke+co states,
	 * and reflects the actual color indicators displayed in the Nest app
	 * 
	 * Possible values for ui_color_state
	 * <dl>
	 * <dt>gray</dt>
	 * <dd>Offline</dd>
	 * <dt>green</dt>
	 * <dd>OK</dd>
	 * <dt>yellow</dt>
	 * <dd>Warning</dd>
	 * <dt>red</dt>
	 * <dd>Emergency</dd>
	 * </dl>
	 */
	public static enum ColorState {
		OFFLINE("gray"), OK("green"), WARNING("yellow"), EMERGENCY("red");

		private final String state;

		private ColorState(String state) {
			this.state = state;
		}

		@JsonValue
		public String value() {
			return state;
		}

		@JsonCreator
		public static ColorState forValue(String v) {
			for (ColorState cs : ColorState.values()) {
				if (cs.state.equals(v)) {
					return cs;
				}
			}
			throw new IllegalArgumentException("Invalid color_state: " + v);
		}

		@Override
		public String toString() {
			return this.state;
		}
	}

	private BatteryHealth battery_health;
	private AlarmState co_alarm_state;
	private AlarmState smoke_alarm_state;
	private Boolean is_manual_test_active;
	private Date last_manual_test_time;
	private ColorState ui_color_state;

	public SmokeCOAlarm(@JsonProperty("device_id") String device_id) {
		super(device_id);
	}

	/**
	 * @return Battery life/health; estimate of time to end of life
	 */
	@JsonProperty("battery_health")
	public BatteryHealth getBattery_health() {
		return this.battery_health;
	}

	/**
	 * @return CO alarm status
	 */
	@JsonProperty("co_alarm_state")
	public AlarmState getCo_alarm_state() {
		return this.co_alarm_state;
	}

	/**
	 * @return Smoke alarm status
	 */
	@JsonProperty("smoke_alarm_state")
	public AlarmState getSmoke_alarm_state() {
		return this.smoke_alarm_state;
	}

	/**
	 * @return State of the manual smoke and CO alarm test.
	 */
	@JsonProperty("is_manual_test_active")
	public Boolean getIs_manual_test_active() {
		return this.is_manual_test_active;
	}

	/**
	 * @return Timestamp of the last successful manual test.
	 */
	@JsonProperty("last_manual_test_time")
	public Date getLast_manual_test_time() {
		return this.last_manual_test_time;
	}

	/**
	 * @return Indicates device status by color in the Nest app UI; it is an aggregate condition for battery+smoke+co
	 *         states, and reflects the actual color indicators displayed in the Nest app.
	 */
	@JsonProperty("ui_color_state")
	public ColorState getUi_color_state() {
		return this.ui_color_state;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("battery_health", this.battery_health);
		builder.append("co_alarm_state", this.co_alarm_state);
		builder.append("smoke_alarm_state", this.smoke_alarm_state);
		builder.append("is_manual_test_active", this.is_manual_test_active);
		builder.append("last_manual_test_time", this.last_manual_test_time);
		builder.append("ui_color_state", this.ui_color_state);

		return builder.toString();
	}
}
