/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
