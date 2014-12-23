/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This state machine is used to weed out duplicate Insteon broadcast messages
 * @author Bernd Pfrommer
 * @since 1.7.0
 */
public class GroupMessageStateMachine {
	private static final Logger logger = LoggerFactory.getLogger(GroupMessageStateMachine.class);
	enum GroupMessage {
		BCAST, CLEAN, SUCCESS;
	};
	enum State {
		EXPECT_BCAST,
		EXPECT_CLEAN,
		EXPECT_SUCCESS
	};
	State	m_state		= State.EXPECT_BCAST;
	int		m_lastHops	= 0;
	public boolean action(GroupMessage a, int hops) {
		boolean publish = false;
		switch (m_state) {
		case EXPECT_BCAST:
			switch (a) {
			case BCAST:		publish = true;		break;	// missed() move state machine and pub!
			case CLEAN:		publish = true;		break;	// missed(BCAST)
			case SUCCESS:	publish = false;	break;}	// missed(BCAST,CLEAN) or dup SUCCESS
			break;
		case EXPECT_CLEAN:
			switch (a) {
			case BCAST:		publish = false;	break;  // missed(CLEAN, SUCCESS) or dup BCAST
			case CLEAN:		publish = false;	break;	// missed() move state machine, no pub
			case SUCCESS:	publish = false;	break; } // missed(CLEAN)
			break;
		case EXPECT_SUCCESS:
			switch (a) {
			case BCAST:		publish = true;		break;	// missed(SUCCESS)  
			case CLEAN:		publish = false;	break;  // missed(SUCCESS,BCAST) or dup CLEAN
			case SUCCESS:	publish = false;	break; } // missed(), move state machine, no pub
			break;
		}
		State oldState = m_state;
		switch (a) {
		case BCAST: 	m_state = State.EXPECT_CLEAN;	break;
		case CLEAN:		m_state = State.EXPECT_SUCCESS;	break;
		case SUCCESS:	m_state = State.EXPECT_BCAST;	break;
		}
		logger.trace("group state: {} --{}--> {}, publish: {}", oldState, a, m_state, publish);
		return (publish);
	}
}
