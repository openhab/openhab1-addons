package org.openhab.binding.nest.internal.api.listeners;

import org.openhab.binding.nest.internal.api.model.SmokeCOAlarm;
  public interface SmokeCOAlarmListener {
        /**
         * Called when updated data is retrieved for a Nest Protect.
         * @param smokeCOAlarm the new data for the Nest Protect (guaranteed
         *                     to be non-null)
         */
        void onSmokeCOAlarmUpdated(SmokeCOAlarm smokeCOAlarm);
    }