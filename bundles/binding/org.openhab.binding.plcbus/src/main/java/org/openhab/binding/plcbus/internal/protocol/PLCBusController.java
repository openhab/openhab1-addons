/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import org.openhab.binding.plcbus.internal.protocol.commands.*;

/**
 * Controller for the PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusController implements IPLCBusController {

	private ISerialPortGateway serialPortGateway;

	private PLCBusController(ISerialPortGateway serialPortGateway) {
		this.serialPortGateway = serialPortGateway;
	}

	public static IPLCBusController create(ISerialPortGateway serialPortGateway) {
		return new PLCBusController(serialPortGateway);
	}

	private boolean sendWithoutAnswer(String usercode, String address,
			Command command) {
		IReceiveFrameContainer container = getDefaultReceiveFrameContainer();

		send(usercode, address, command, container);

		ReceiveFrame answer = container.getAnswerFrame();

		if (answer == null) {
			return false;
		}

		return answer.isAcknowledgement();
	}

	private IReceiveFrameContainer getDefaultReceiveFrameContainer() {
		return new DefaultOnePhaseReceiveFrameContainer();
	}

	private void send(String usercode, String address, Command command,
			IReceiveFrameContainer container) {
		TransmitFrame frame = createTransmitFrame(usercode, address, command);
		serialPortGateway.send(frame, container);
	}

	private TransmitFrame createTransmitFrame(String usercode, String address, Command command) {
		CommandFrame commandFrame = new CommandFrame(command);
		commandFrame.setDemandAckTo(true);

		DataFrame data = new DataFrame(commandFrame);
		data.setUserCode(usercode);
		data.SetAddress(address);

		TransmitFrame frame = new TransmitFrame(data);

		return frame;
	}

	@Override
	public boolean bright(PLCUnit unit, int seconds) {
		Bright command = new Bright();
		command.setSeconds(seconds);
		return sendWithoutAnswer(unit.getUsercode(), unit.getAddress(), command);
	}

	@Override
	public boolean dim(PLCUnit unit, int seconds) {
		Dim command = new Dim();
		command.setSeconds(seconds);
		return sendWithoutAnswer(unit.getUsercode(), unit.getAddress(), command);
	}

	@Override
	public boolean switchOff(PLCUnit unit) {
		return sendWithoutAnswer(unit.getUsercode(), unit.getAddress(), new UnitOff());
	}

	@Override
	public boolean switchOn(PLCUnit unit) {
		return sendWithoutAnswer(unit.getUsercode(), unit.getAddress(), new UnitOn());
	}

	@Override
	public boolean fadeStop(PLCUnit unit) {
		return sendWithoutAnswer(unit.getUsercode(), unit.getAddress(), new FadeStop());
	}

	@Override
	public StatusResponse requestStatusFor(PLCUnit unit) {
		IReceiveFrameContainer container = new StatusRequestReceiveFrameContainer();

		send(unit.getUsercode(), unit.getAddress(), new StatusRequest(), container);

		ReceiveFrame answer = container.getAnswerFrame();

		if (answer == null) {
			return null;
		}

		return new StatusResponse(answer.isAcknowledgement(),
				answer.getCommand(), answer.getFirstParameter(),
				answer.getSecondParameter());
	}

}
