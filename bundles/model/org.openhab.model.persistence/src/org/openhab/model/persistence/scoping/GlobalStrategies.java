package org.openhab.model.persistence.scoping;

import org.openhab.model.persistence.persistence.Strategy;
import org.openhab.model.persistence.persistence.impl.StrategyImpl;

/**
 * This class defines a few persistence strategies that are globally available to
 * all persistence models.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class GlobalStrategies {

	static final public Strategy UPDATE = new StrategyImpl() {
		public String getName() {
			return "everyUpdate";
		};
	};

	static final public Strategy CHANGE = new StrategyImpl() {
		public String getName() {
			return "everyChange";
		};
	};

	static final public Strategy RESTORE = new StrategyImpl() {
		public String getName() {
			return "restoreOnStartup";
		};
	};
}
