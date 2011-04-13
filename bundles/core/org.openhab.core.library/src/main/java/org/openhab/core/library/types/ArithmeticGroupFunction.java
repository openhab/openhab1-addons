package org.openhab.core.library.types;

import java.math.BigDecimal;
import java.util.List;

import org.openhab.core.items.GroupFunction;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;

public interface ArithmeticGroupFunction extends GroupFunction {

	static class Count implements GroupFunction {

		private final State state;
		
		public Count(State state) {
			this.state = state;
		}

		@Override
		public State calculate(List<Item> items) {
			int count = 0;
			for(Item item : items) {
				if(state.equals(item.getState())) {
					count++;
				}
			}
			return new DecimalType(count);
		}
	}
	
	static class Avg implements GroupFunction {
		
		public Avg() {}

		@Override
		public State calculate(List<Item> items) {
			BigDecimal sum = BigDecimal.ZERO;
			for(Item item : items) {
				if(item.getState() instanceof DecimalType) {
					DecimalType itemState = (DecimalType) item.getState();
					sum.add(itemState.toBigDecimal());
				}
			}
			return new DecimalType(sum.divide(new BigDecimal(items.size())));
		}
	}

}
