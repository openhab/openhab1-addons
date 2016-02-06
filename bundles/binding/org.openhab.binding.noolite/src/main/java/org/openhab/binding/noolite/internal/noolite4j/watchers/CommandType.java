/*
 * Copyright 2014 Nikolay A. Viguro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhab.binding.noolite.internal.noolite4j.watchers;

/**
 * @author Nikolay Viguro
 * @since 1.0.0
 */

public enum CommandType {
    TURN_OFF(0), SLOW_TURN_OFF(1), TURN_ON(2), SLOW_TURN_ON(3), SWITCH(4), REVERT_SLOW_TURN(5), SET_LEVEL(6), RUN_SCENE(
	    7), RECORD_SCENE(8), UNBIND(9), STOP_DIM_BRIGHT(10), BIND(15), SLOW_RGB_CHANGE(16), SWITCH_COLOR(
		    17), SWITCH_MODE(18), SWITCH_SPEED_MODE(19), BATTERY_LOW(20), TEMP_HUMI(21);

    private final int code;

    CommandType(int i) {
	this.code = i;
    }

    public int getCode() {
	return code;
    }

    public static CommandType getValue(byte value) {
	for (CommandType e : CommandType.values()) {
	    if (e.getCode() == value) {
		return e;
	    }
	}
	return null;
    }
}
