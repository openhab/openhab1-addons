/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.media.items;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.library.media.types.PlayerCommandType;
import org.openhab.library.media.types.PlayerStateType;
import org.openhab.library.media.types.PlayerType;


/**
 * This item identifies a media player 
 * 
 * @author Karel Goderis
 * @since 1.5.0
 */
public class PlayerItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

	static {
		acceptedDataTypes.add(StringType.class);
		acceptedDataTypes.add(PercentType.class);
		acceptedDataTypes.add(OnOffType.class);
		acceptedDataTypes.add(PlayerStateType.class);
		acceptedDataTypes.add(PlayerType.class);
		acceptedDataTypes.add(UnDefType.class);
		
		acceptedCommandTypes.add(OnOffType.class);
		acceptedCommandTypes.add(IncreaseDecreaseType.class);
		acceptedCommandTypes.add(StringType.class);
		acceptedCommandTypes.add(PlayerCommandType.class);

	}
	
	public PlayerItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(State state) {
		State currentState = this.state;
		
		if(currentState instanceof PlayerType) {
			PlayerStateType currentstate = ((PlayerType) currentState).getState();
			StringType source = ((PlayerType) currentState).getSource();
			PercentType volume = ((PlayerType) currentState).getVolume();
			
			if(state==OnOffType.OFF || state==PlayerStateType.OFF) {
				super.setState(new PlayerType(PlayerStateType.OFF, volume, source));
			} else if(state==OnOffType.ON || state==PlayerStateType.ON) {
				super.setState(new PlayerType(PlayerStateType.ON, volume, source));
			} else if(state==PlayerStateType.PLAYING) {
				super.setState(new PlayerType(PlayerStateType.PLAYING, volume, source));
			} else if(state==PlayerStateType.STOPPED) {
				super.setState(new PlayerType(PlayerStateType.STOPPED, volume, source));
			} else if(state==PlayerStateType.PAUSED) {
				super.setState(new PlayerType(PlayerStateType.PAUSED, volume, source));
			} else if(state instanceof PercentType && !(state instanceof PlayerType)) {
				super.setState(new PlayerType(currentstate, (PercentType) state, source));
			} else if(state instanceof StringType && !(state instanceof PlayerType)) {
				super.setState(new PlayerType(currentstate, volume, (StringType) state));
			} else {
				super.setState(state);
			}
		} else {
			if(state==OnOffType.OFF || state==PlayerStateType.OFF) {
				super.setState(PlayerType.ALLOFF);
			} else if(state==OnOffType.ON || state==PlayerStateType.ON) {
				super.setState(PlayerType.POWERON);
			} else if(state instanceof PercentType && !(state instanceof PlayerType)) {
				//switch on, set volume but sit idle
				super.setState(new PlayerType(PlayerStateType.ON, (PercentType) state,StringType.EMPTY));
			} else if(state instanceof StringType && !(state instanceof PlayerType)) {
				//switch on, set source but mute
				super.setState(new PlayerType(PlayerStateType.ON, PercentType.ZERO, (StringType) state));
			}else {
				super.setState(state);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public State getStateAs(Class<? extends State> typeClass) {
		if(typeClass==PlayerType.class) {
			return this.state;
		} else if(typeClass==OnOffType.class) {
			if(state instanceof PlayerType) {
				PlayerType playerState = (PlayerType) state;
				// if OnOff we return the power state of the player
				return playerState.getState().equals(PlayerStateType.OFF) ? OnOffType.OFF : OnOffType.ON;
			}
		} else if(typeClass==DecimalType.class) {
			if(state instanceof PlayerType) {
				PlayerType playerState = (PlayerType) state;
				// if Decimal we return the volume of the player
				return new DecimalType(playerState.getVolume().toBigDecimal().divide(new BigDecimal(100), 8, RoundingMode.UP));
			}
		} else if(typeClass==StringType.class) {
			if(state instanceof PlayerType) {
				PlayerType playerState = (PlayerType) state;
				return playerState.getSource();
			}
		} else if(typeClass==PlayerStateType.class) {
			if(state instanceof PlayerType) {
				PlayerType playerState = (PlayerType) state;
				return playerState.getState();
			}
		}	
		return super.getStateAs(typeClass);
	}
}