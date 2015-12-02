package org.openhab.binding.ipx800.internal.itemslot;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Power average/consumption item
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Consumption extends Ipx800Item {
	private static final Logger logger = LoggerFactory.getLogger(Ipx800Consumption.class);
	/** Virtual  */
	private static int VIRTUAL_UPDATE_MULTIPLIER_DELAY = 2;
	/** Minimum delay before starting virtual update in millisecond */
	private static long MIN_VIRTUAL_UPDATE_DELAY = 1000;
	/** The timer for update operations */
	private Timer timer = new Timer();
	/** Current value of ipx800 counter */
	private int currentValue = 0;
	/** Last pulse timestamp */
	private long lastTimestamp = 0;
	/** current pulse timestamp */
	private long currentTimestamp = 0;
	/** When virtual update is enabled, average is computed between now and current timestamp instead of current and last */
	private boolean isVirtualUpdate = false;
	
	
	private Ipx800ConsumptionPeriod period = Ipx800ConsumptionPeriod.MINUTE;
	private float unit = 1;
	
	public Ipx800Consumption(float unit, Ipx800ConsumptionPeriod period) {
		this.unit = unit;
		if (period != null) {
			this.period = period;
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + " with unit=" + this.unit + "and period=" + this.period.shortName;
	}

	@Override
	public State getState() {
		long newest;
		long oldest;
		if (isVirtualUpdate) {
			newest = new Date().getTime();
			oldest = currentTimestamp;
		} else {
			newest = currentTimestamp;
			oldest = lastTimestamp;
		}
		
		int value = (int) (unit * (this.period.time * 1000) / (newest - oldest));
		return new DecimalType(value);
	}
	
	public void destroy() {
		timer.cancel();
	}
	
	public void restartTimer() {
		timer.cancel();
		if (lastTimestamp == 0) {
			return;
		}
		long delay = Math.max(MIN_VIRTUAL_UPDATE_DELAY, (currentTimestamp - lastTimestamp) * VIRTUAL_UPDATE_MULTIPLIER_DELAY);
		logger.debug("Restarting timer with delay={}", delay);
		logger.debug("lastTimestamp={}, currentTimestamp={}, ", lastTimestamp, currentTimestamp);
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				isVirtualUpdate = true;
				postState();
			}}, delay, delay);
	}

	@Override
	protected Type toState(String state) {
		return new DecimalType(Integer.parseInt(state));
	}

	@Override
	protected boolean updateStateInternal(Type state) {
		boolean changed = false;
		if (state instanceof DecimalType) {
			int value = ((DecimalType)state).intValue();
			if (currentValue != 0 && value != currentValue) {
				lastTimestamp = currentTimestamp;
				currentTimestamp = new Date().getTime();
				isVirtualUpdate = false;
				restartTimer();
				if (lastTimestamp != 0) { // Inhibit first update
					changed = true;
				}
			}
			currentValue = value;
		}
		return changed;
	}

	
}
