package org.openhab.binding.ipx800.internal.handler;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.ipx800.internal.itemslot.Ipx800AstableSwitch;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Dimmer;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800DoubleClic;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;
import org.openhab.binding.ipx800.internal.itemslot.Ipx800SimpleClic;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler can handle simple clic/double clic/dimmer items on the same port
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800HandlerMulti implements Ipx800Handler {
	private static final Logger logger = 
			LoggerFactory.getLogger(Ipx800HandlerMulti.class);
	private final static int MAX_DB_CLIC_DURATION = 2000;
	private final static int DIMMER_REPEAT_PERIOD = 500;
	
	private enum ClicState { IDLE, FIRST_PRESSED, DIMMER, WAITING, SECOND_PRESSED };
	private ClicState clicState = ClicState.IDLE;
	
	private long lastClicTimestamp = 0;
	
	private Timer timer;
	
	private boolean switchStateOf(Class<? extends Ipx800Item> class1, Map<String, Ipx800Item> items) {
		boolean continu = false;
		for (Ipx800Item item : items.values()) {
			if (class1.isInstance(item)) {
				if (item instanceof Ipx800AstableSwitch) {
					Ipx800AstableSwitch i = (Ipx800AstableSwitch) item;
					i.switchState(true);
				} else if (item instanceof Ipx800Dimmer) {
					Ipx800Dimmer i = (Ipx800Dimmer) item;
					i.increment(true);
					if (i.getState() != PercentType.HUNDRED) {
						continu = true;
					}
					// FIXME synchronize all dimers ?
				}
			}
		}
		return continu;
	}
	
	private synchronized void programTimer(TimerTask task, int delay, int period) {
		cancelTimer();
		timer = new Timer();
		if (period == 0) {
			timer.schedule(task, delay);
		} else {
			timer.schedule(task, delay, period);
		}
	}
	
	private synchronized void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public boolean updateState(final Map<String, Ipx800Item> items, String state) {
		boolean enabled = state.charAt(0) == '1' ? true : false;
		if (enabled) {
			if (clicState == ClicState.IDLE) {
				clicState = ClicState.FIRST_PRESSED;
				programTimer(new TimerTask() {
					@Override
					public void run() {
						try {
							clicState = ClicState.DIMMER;
							if (!switchStateOf(Ipx800Dimmer.class, items)) {
								cancelTimer();
							}
						} catch (Exception e) {
							logger.error("### Exception in timer ", e);
						}
					}
				}, MAX_DB_CLIC_DURATION + 100, DIMMER_REPEAT_PERIOD);
			} else if (clicState == ClicState.WAITING) {
				clicState = ClicState.SECOND_PRESSED;
			}
		} else {
			if (clicState == ClicState.FIRST_PRESSED) {
				clicState = ClicState.WAITING;
				lastClicTimestamp = new Date().getTime();
				// See if synchronisation is mandatory
				programTimer(new TimerTask() {
					@Override
					public void run() {
						try {
							clicState = ClicState.IDLE;
							switchStateOf(Ipx800SimpleClic.class, items);
						} catch (Exception e) {
							logger.error("### Exception in timer ", e);
						}
					}
				}, MAX_DB_CLIC_DURATION, 0);
			} else if (clicState == ClicState.SECOND_PRESSED) {
				clicState = ClicState.IDLE;
				if (lastClicTimestamp + MAX_DB_CLIC_DURATION >= new Date().getTime()) {
					switchStateOf(Ipx800DoubleClic.class, items);
				}
				cancelTimer();
			} else {
				clicState = ClicState.IDLE;
				cancelTimer();
			}
		}
		return false;
	}

}
