package org.openhab.core.items;

import java.util.List;

import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

abstract public interface GroupFunction {

	public State calculate(List<Item> items);

	static class UnDef implements GroupFunction {
		@Override
		public State calculate(List<Item> items) {
			return UnDefType.UNDEF;
		}}

	static class And implements GroupFunction {
		
		private final State activeState;
		private final State passiveState;
		
		public And(State activeValue, State passiveValue) {
			this.activeState = activeValue;
			this.passiveState = passiveValue;
		}
		
		public State calculate(List<Item> items) {
			for(Item item : items) {
				if(!activeState.equals(item.getState())) {
					return passiveState;
				}
			}
			return activeState;
		}
	}

	static class Or implements GroupFunction {

		private final State activeState;
		private final State passiveState;
		
		public Or(State activeValue, State passiveValue) {
			this.activeState = activeValue;
			this.passiveState = passiveValue;
		}

		@Override
		public State calculate(List<Item> items) {			
			for(Item item : items) {
				if(activeState.equals(item.getState())) {
					return activeState;
				}
			}
			return passiveState;
		}
	}
	
}
