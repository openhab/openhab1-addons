/**
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.leapmotion.internal;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.leapmotion.LeapMotionBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.4.0
 */
public class LeapMotionBinding extends AbstractBinding<LeapMotionBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(LeapMotionBinding.class);
	
	private LeapMotionListener listener;

	private Controller leapController;
	
	private Map<String, OnOffType> lastOnOffCommandCache;
	private Map<String, HSBType> lastHsbCommandCache;
	
	private static final int HUE_DEGREE_STEP = 10;
	private int hueDegree = 0;
	

	public void activate() {
		this.lastOnOffCommandCache = new HashMap<String, OnOffType>();
		this.lastHsbCommandCache = new HashMap<String, HSBType>();
		
		this.listener = new LeapMotionListener();
		
		leapController = new Controller();
		leapController.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
		leapController.addListener(this.listener);
	}

	public void deactivate() {
		leapController.removeListener(this.listener);
	}
	
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		super.internalReceiveCommand(itemName, command);
		
		if (!(command instanceof OnOffType || command instanceof HSBType)) {
			return;
		}
		
		for (LeapMotionBindingProvider provider : providers) {
			for (String configuredItemName: provider.getItemNames()) {
				if (configuredItemName.equals(itemName)) {
					if (command instanceof OnOffType) {
						lastOnOffCommandCache.put(itemName, (OnOffType) command);
					} else if (command instanceof HSBType) {
						lastHsbCommandCache.put(itemName, (HSBType) command);
					}
				}
			}
		}
	}
	
	
	protected void onKeyTap() {
		logger.debug("on key tap");
		for (LeapMotionBindingProvider provider : providers) {
			for (String itemName: provider.getItemNames()) {
				Command command = lastOnOffCommandCache.get(itemName);
				if (command == null) {
					command = OnOffType.OFF;
				}
				
				command = OnOffType.ON.equals(command) ? OnOffType.OFF : OnOffType.ON;
				lastOnOffCommandCache.put(itemName, (OnOffType) command);
				eventPublisher.sendCommand(itemName, command);
			}
		}
	}
	
	protected void onCircle(boolean clockwise) {
		logger.debug("onCircle (clockwise=" + clockwise + ")");
		
		for (LeapMotionBindingProvider provider : providers) {
			for (String itemName: provider.getItemNames()) {
				if (clockwise) {
					hueDegree = Math.min(360, hueDegree + HUE_DEGREE_STEP);
				} else {
					hueDegree = Math.max(0, hueDegree - HUE_DEGREE_STEP);
				}
				
				// reset to 0 after we've walked around the color circle
				if (clockwise && hueDegree == 360) hueDegree = 0;
				if (!clockwise && hueDegree == 0) hueDegree = 360;
				
				if (OnOffType.ON.equals(lastOnOffCommandCache.get(itemName))) {
					eventPublisher.sendCommand(itemName, 
						new HSBType(new DecimalType(hueDegree), PercentType.HUNDRED, PercentType.HUNDRED));
				}
			}
		}
	}
	
	protected void onFingersShown(final int fingers) {
		logger.debug("on fingers shown '(" + fingers + ")'");
		
		for (LeapMotionBindingProvider provider : providers) {
			for (String itemName: provider.getItemNames()) {
				if (OnOffType.ON.equals(lastOnOffCommandCache.get(itemName))) {
					int percentValue = Math.min(fingers * 20, 100);
					eventPublisher.sendCommand(itemName, new PercentType(percentValue));
				}
			}
		}
	}
	
	protected void onPalmShown(int fingers, float y, float x, float z) {
		logger.debug("palm position (fingers={}, heigth={}, width={}, depth={})", new Object[]{ fingers, y, x, z });
		
		if (fingers == 3) {
			double r = Math.max(0, Math.min(255, y - 60));
			double g = Math.max(0, Math.min(255, 127 + (x * 1.4)));
			double b = Math.max(0, Math.min(255, 127 - (z * 1.2)));
			
			HSBType hsb = new HSBType(new Color((int) r, (int) g, (int) b));
			for (LeapMotionBindingProvider provider : providers) {
				for (String itemName: provider.getItemNames()) {
					if (OnOffType.ON.equals(lastOnOffCommandCache.get(itemName))) {
						eventPublisher.sendCommand(itemName, hsb);
					}
				}
			}
//			System.err.println("r: " + r + ", g=" + g + ", b=" + b);
//			System.err.println(hsb);
		} else if (fingers == 5) {
			int percentValue = (int) Math.min(100, (y - 60) / 3.4);
			for (LeapMotionBindingProvider provider : providers) {
				for (String itemName: provider.getItemNames()) {
					if (OnOffType.ON.equals(lastOnOffCommandCache.get(itemName))) {
						eventPublisher.sendCommand(itemName, new PercentType(percentValue));
					}
				}
			}
//			System.err.println("percent: " + percentValue);
		}
	}

	
	private class LeapMotionListener extends Listener {

		private final static int CIRCLE_COUNTER_THRESHOLD = 15;
		private int circleEventCounter;

		private int lastFingerCount;
		private final static int FINGER_COUNTER_THRESHOLD = 25;
		private int fingerCounter;
		
		private final static int PALM_COUNTER_THRESHOLD = 10;
		private int palmCounter;

		private boolean noHand;

		@Override
		public void onConnect(Controller controller) {
			controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
			controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		}

		@Override
		public void onFrame(Controller controller) {
			Frame frame = controller.frame();
			
			GestureList gestures = frame.gestures();
			for (int i = 0; i < gestures.count(); i++) {
				Gesture gesture = gestures.get(i);
				switch (gesture.type()) {
				case TYPE_KEY_TAP:
					KeyTapGesture keyTap = new KeyTapGesture(gesture);
					System.err.println("Key Tap id: " + keyTap.id() + ", "
							+ keyTap.state() + ", position: "
							+ keyTap.position() + ", direction: "
							+ keyTap.direction());
					onKeyTap();
					break;
				case TYPE_CIRCLE:
					CircleGesture circle = new CircleGesture(gesture);
					// Calculate clock direction using the angle between circle
					// normal and pointable
					boolean clockwiseness;
					if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 4) {
						// Clockwise if angle is less than 90 degrees
						clockwiseness = true;
					} else {
						clockwiseness = false;
					}
					
					// Calculate angle swept since last frame
					double sweptAngle = 0;
					if (circle.state() == com.leapmotion.leap.Gesture.State.STATE_START) {
						// nothing to do
					}
					else if (circle.state() == com.leapmotion.leap.Gesture.State.STATE_UPDATE) {
						CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
						sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
						
						if (circleEventCounter <= CIRCLE_COUNTER_THRESHOLD) {
							circleEventCounter++;
							return;
						} else {
							circleEventCounter = 0;
							onCircle(clockwiseness);
							
							System.err.println("Circle id: " + circle.id() + ", "
									+ circle.state() + ", progress: "
									+ circle.progress() + ", radius: "
									+ circle.radius() + ", angle: "
									+ Math.toDegrees(sweptAngle) + ", "
									+ clockwiseness);
						}
					}
					else if (circle.state() == com.leapmotion.leap.Gesture.State.STATE_STOP) {
						// nothing to do
					}
					
					break;
				default:
					System.err.println("Unknown gesture type.");
					break;
				}
			}

			if (!frame.hands().isEmpty()) {
				noHand = false;
				// Get the first hand
				Hand hand = frame.hands().get(0);
				// Check if the hand has any fingers
				FingerList fingers = hand.fingers();
				if (!fingers.isEmpty()) {
					
					if (fingers.count() > 2) {
						if (palmCounter <= PALM_COUNTER_THRESHOLD) {
							palmCounter++;
						} else {
							palmCounter = 0;
							onPalmShown(fingers.count(), hand.palmPosition().getY(), hand.palmPosition().getX(), hand.palmPosition().getZ());
						}
					}
					
//					if (fingerCounter <= FINGER_COUNTER_THRESHOLD) {
//						fingerCounter++;
//					} else { 
//						fingerCounter = 0;
//						if (lastFingerCount != fingers.count()) {
//							lastFingerCount = fingers.count();
//							// Calculate the hand's average finger tip position
//							Vector avgPos = Vector.zero();
//							for (Finger finger : fingers) {
//								avgPos = avgPos.plus(finger.tipPosition());
//							}
//							avgPos = avgPos.divide(fingers.count());
//							System.err.println("Hand has " + fingers.count()
//									+ " fingers, average finger tip position: "
//									+ avgPos);
//							
//							onFingersShown(fingers.count());
//						}
//					}
				}
			} else {
				if (!noHand) {
					noHand = true;
					System.err.println("no hand");
				}
			}
		}
	}

}
