/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
 * Ideally, Insteon ALL LINK messages are received in this order, and
 * only a single one of each:
 * 
 * BCAST (a broadcast message from the device to all group members)
 * CLEAN (a cleanup point-to-point message to ensure more reliable transmission)
 * SUCCESS (a broadcast report of success or failure of cleanup, with cmd1=0x06)
 * 
 * But often, the BCAST, CLEAN and SUCCESS messages are retransmitted multiple times,
 * or (less frequently) messages are lost. The present state machine was developed
 * to remove duplicates, yet make sure that a single lost message does not cause
 * the binding to miss an update.
 * 
 * 
 *                          "SUCCESS"
 *                         EXPECT_BCAST
 *                    ^ /                ^ \
 *           SUCCESS / /                  \ \ [BCAST]
 *                  / /['CLEAN']  'SUCCESS'\ \
 *                 / /                      \ \
 *                / V         CLEAN          \ V
 * "CLEAN" EXPECT_SUCCESS <-------------- EXPECT_CLEAN "BCAST"
 *                         -------------->
 *                            ['BCAST']
 * 
 * How to read this diagram:
 * 
 * Regular, expected, non-duplicate messages do not have any quotes around them,
 * and lead to the obvious state transitions.
 * 
 * The actions in [square brackets] are transitions that cause a state
 * update to be published when they occur.
 * 
 * The presence of double quotes indicates a duplicate that does not lead
 * to any state transitions, i.e. it is simply ignored.
 *
 * Single quotes indicate a message that is the result of a single dropped
 * message, and leads to a state transition, in some cases even to a state
 * update to be published. 
 * 
 * For instance at the top of the diagram, if a "SUCCESS" message is received
 * when in state EXPECT_BCAST, it is considered a duplicate (it has "").
 * 
 * When in state EXPECT_SUCCESS though, receiving a ['BCAST'] is most likely because
 * the SUCCESS message was missed, and therefore it is considered the result
 * of a single lost message (has '' around it). The state changes to EXPECT_CLEAN,
 * and the message should lead to publishing of a state update (it has [] around it).
 * 
 * @author Bernd Pfrommer
 * @since 1.7.0
 */
public class GroupMessageStateMachine {
	private static final Logger logger = LoggerFactory.getLogger(GroupMessageStateMachine.class);
	/**
	 * The different kinds of Insteon ALL Link (Group) messages that can be received.
	 * Here is a typical sequence:
	 * BCAST:   IN:Cmd:0x50|fromAddress:20.AC.99|toAddress:00.00.01|messageFlags:0xCB=ALL_LINK_BROADCAST:3:2|command1:0x13|command2:0x00|
	 * CLEAN:   IN:Cmd:0x50|fromAddress:20.AC.99|toAddress:23.9B.65|messageFlags:0x41=ALL_LINK_CLEANUP:1:0|command1:0x13|command2:0x01|
	 * SUCCESS: IN:Cmd:0x50|fromAddress:20.AC.99|toAddress:13.03.01|messageFlags:0xCB=ALL_LINK_BROADCAST:3:2|command1:0x06|command2:0x00|
	 */
	enum GroupMessage {
		BCAST, CLEAN, SUCCESS;
	};
	/**
	 * The state of the machine (i.e. what message we are expecting next).
	 * The usual state should be EXPECT_BCAST
	 */
	enum State {
		EXPECT_BCAST,
		EXPECT_CLEAN,
		EXPECT_SUCCESS
	};
	State	m_state		= State.EXPECT_BCAST;
	int		m_lastHops	= 0;
	/**
	 * Advance the state machine and determine if update is genuine (no duplicate)
	 * @param a the group message (action) that was received
	 * @param hops number of hops that was given on the message. Currently not used.
	 * @return true if the group message is not a duplicate
	 */
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
