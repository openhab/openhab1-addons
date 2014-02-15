/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.openhab.core.items.GroupFunction;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * This interface is only a container for functions that require the core type library
 * for its calculations.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
public interface ArithmeticGroupFunction extends GroupFunction {

	/**
	 * This does a logical 'and' operation. Only if all items are of 'activeState' this
	 * is returned, otherwise the 'passiveState' is returned.
	 * 
	 * Through the getStateAs() method, it can be determined, how many
	 * items actually are not in the 'activeState'.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class And implements GroupFunction {
		
		protected final State activeState;
		protected final State passiveState;
		
		public And(State activeValue, State passiveValue) {
			if(activeValue==null || passiveValue==null) {
				throw new IllegalArgumentException("Parameters must not be null!");
			}
			this.activeState = activeValue;
			this.passiveState = passiveValue;
		}
		
		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			if(items!=null && items.size()>0) {
				for(Item item : items) {
					if(!activeState.equals(item.getState())) {
						return passiveState;
					}
				}
				return activeState;
			} else {
				// if we do not have any items, we return the passive state
				return passiveState;
			}
		}

		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				if(stateClass == DecimalType.class) {
					if(items!=null) {
						return new DecimalType(items.size() - count(items, activeState));
					} else {
						return DecimalType.ZERO;
					}
				} else {
					return null;
				}
			}
		}
		
		private int count(List<Item> items, State state) {
			int count = 0;
			if(items!=null && state!=null) {
				for(Item item : items) {
					if(state.equals(item.getStateAs(state.getClass()))) {
						count++;
					}
				}
			}
			return count;
			
		}
	}

	/**
	 * This does a logical 'or' operation. If at least one item is of 'activeState' this
	 * is returned, otherwise the 'passiveState' is returned.
	 * 
	 * Through the getStateAs() method, it can be determined, how many
	 * items actually are in the 'activeState'.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class Or implements GroupFunction {

		protected final State activeState;
		protected final State passiveState;
		
		public Or(State activeValue, State passiveValue) {
			if(activeValue==null || passiveValue==null) {
				throw new IllegalArgumentException("Parameters must not be null!");
			}
			this.activeState = activeValue;
			this.passiveState = passiveValue;
		}

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {	
			if(items!=null) {
				for(Item item : items) {
					if(activeState.equals(item.getState())) {
						return activeState;
					}
				}
			}
			return passiveState;
		}
		
		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				if(stateClass == DecimalType.class) {
					return new DecimalType(count(items, activeState));
				} else {
					return null;
				}
			}
		}
		
		private int count(List<Item> items, State state) {
			int count = 0;
			if(items!=null && state!=null) {
				for(Item item : items) {
					if(state.equals(item.getStateAs(state.getClass()))) {
						count++;
					}
				}
			}
			return count;
		}
	}
	
	/**
	 * This does a logical 'nand' operation. The state is 'calculated' by 
	 * the normal 'and' operation and than negated by returning the opposite
	 * value. E.g. when the 'and' operation calculates the activeValue the
	 * passiveValue will be returned and vice versa. 
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	static class NAnd extends And {
		
		public NAnd(State activeValue, State passiveValue) {
			super(activeValue, passiveValue);
		}

		public State calculate(List<Item> items) {
			State result = super.calculate(items);
			State notResult = 
				result.equals(activeState) ? passiveState : activeState;
			return notResult;
		}
		
	}

	/**
	 * This does a logical 'nor' operation. The state is 'calculated' by 
	 * the normal 'or' operation and than negated by returning the opposite
	 * value. E.g. when the 'or' operation calculates the activeValue the
	 * passiveValue will be returned and vice versa. 
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	static class NOr extends Or {
		
		public NOr(State activeValue, State passiveValue) {
			super(activeValue, passiveValue);
		}

		public State calculate(List<Item> items) {
			State result = super.calculate(items);
			State notResult = 
				result.equals(activeState) ? passiveState : activeState;
			return notResult;
		}
		
	}
	
	/**
	 * This calculates the numeric average over all item states of decimal type.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class Avg implements GroupFunction {
		
		public Avg() {}

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			BigDecimal sum = BigDecimal.ZERO;
			int count = 0;
			if(items!=null) {
				for(Item item : items) {
					DecimalType itemState = (DecimalType) item.getStateAs(DecimalType.class);
					if(itemState!=null) {
						sum = sum.add(itemState.toBigDecimal());
						count++;
					}
				}
			}
			if(count>0) {
				return new DecimalType(sum.divide(new BigDecimal(count), RoundingMode.HALF_UP));
			} else {
				return UnDefType.UNDEF;
			}
		}
		
		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				return null;
			}
		}
	}

	/**
	 * This calculates the numeric sum over all item states of decimal type.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.1.0
	 *
	 */
	static class Sum implements GroupFunction {
		
		public Sum() {}

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			BigDecimal sum = BigDecimal.ZERO;
			if(items!=null) {
				for(Item item : items) {
					DecimalType itemState = (DecimalType) item.getStateAs(DecimalType.class);
					if(itemState!=null) {
						sum = sum.add(itemState.toBigDecimal());
					}
				}
			}
			return new DecimalType(sum);
		}
		
		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * This calculates the minimum value of all item states of decimal type.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class Min implements GroupFunction {
		
		public Min() {}

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			if(items!=null && items.size()>0) {
				BigDecimal min = null;
				for(Item item : items) {
					DecimalType itemState = (DecimalType) item.getStateAs(DecimalType.class);
					if(itemState!=null) {
						if(min==null || min.compareTo(itemState.toBigDecimal()) > 0) {
							min = itemState.toBigDecimal();
						}
					}
				}
				if(min!=null) {
					return new DecimalType(min);
				}
			}
			return UnDefType.UNDEF;
		}

		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				return null;
			}
		}
}

	/**
	 * This calculates the maximum value of all item states of decimal type.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.7.0
	 *
	 */
	static class Max implements GroupFunction {
		
		public Max() {}

		/**
		 * @{inheritDoc
		 */
		public State calculate(List<Item> items) {
			if(items!=null && items.size()>0) {
				BigDecimal max = null;
				for(Item item : items) {
					DecimalType itemState = (DecimalType) item.getStateAs(DecimalType.class);
					if(itemState!=null) {
						if(max==null || max.compareTo(itemState.toBigDecimal()) < 0) {
							max = itemState.toBigDecimal();
						}
					}
				}
				if(max!=null) {
					return new DecimalType(max);
				}
			}
			return UnDefType.UNDEF;
		}

		/**
		 * @{inheritDoc
		 */
		public State getStateAs(List<Item> items, Class<? extends State> stateClass) {
			State state = calculate(items);
			if(stateClass.isInstance(state)) {
				return state;
			} else {
				return null;
			}
		}
	}
	
	
}
