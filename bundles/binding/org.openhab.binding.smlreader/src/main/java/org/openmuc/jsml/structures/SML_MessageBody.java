/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SML_MessageBody extends ASNObject {

	public static final int OpenRequest = 0x00000100, OpenResponse = 0x00000101;
	public static final int CloseRequest = 0x0000200, CloseResponse = 0x00000201;
	public static final int GetProfilePackRequest = 0x00000300, GetProfilePackResponse = 0x00000301;
	public static final int GetProfileListRequest = 0x00000400, GetProfileListResponse = 0x00000401;
	public static final int GetProcParameterRequest = 0x00000500, GetProcParameterResponse = 0x00000501;
	public static final int SetProcParameterRequest = 0x00000600, SetProcParameterResponse = 0x00000601;
	public static final int GetListRequest = 0x00000700, GetListResponse = 0x00000701;
	public static final int AttentionResponse = 0x0000FF01;

	protected Unsigned32 tag;
	protected ASNObject choice;

	public SML_MessageBody(int tag, ASNObject choice) {

		if (!(tag == OpenRequest || tag == OpenResponse || tag == CloseRequest || tag == CloseResponse
				|| tag == AttentionResponse || tag == GetProfilePackRequest || tag == GetProfilePackResponse
				|| tag == GetProfileListRequest || tag == GetProfileListResponse || tag == GetProcParameterRequest
				|| tag == GetProcParameterResponse || tag == SetProcParameterRequest || tag == SetProcParameterResponse
				|| tag == GetListRequest || tag == GetListResponse)) {
			throw new IllegalArgumentException("SML_MessagBody: Wrong value for tag! " + tag + " is not allowed.");
		}

		this.choice = choice;
		this.tag = new Unsigned32(tag);
		isSelected = true;
	}

	public SML_MessageBody() {
		tag = new Unsigned32();
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		os.writeByte(0x72);
		tag.code(os);
		choice.code(os);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		byte tlField = is.readByte();
		if (isOptional && (tlField == 0x01)) {
			isSelected = false;
			return true;
		}
		if ((tlField & 0xff) != 0x72 || !tag.decode(is)) {
			return false;
		}

		switch (tag.val) {
		case OpenRequest:
			choice = new SML_PublicOpenReq();
			break;
		case OpenResponse:
			choice = new SML_PublicOpenRes();
			break;
		case CloseRequest:
			choice = new SML_PublicCloseReq();
			break;
		case CloseResponse:
			choice = new SML_PublicCloseRes();
			break;
		case GetProfileListRequest:
			choice = new SML_GetProfileListReq();
			break;
		case GetProfileListResponse:
			choice = new SML_GetProfileListRes();
			break;
		case GetProfilePackRequest:
			choice = new SML_GetProfilePackReq();
			break;
		case GetProfilePackResponse:
			choice = new SML_GetProfilePackRes();
			break;
		case GetProcParameterRequest:
			choice = new SML_GetProcParameterReq();
			break;
		case GetProcParameterResponse:
			choice = new SML_GetProcParameterRes();
			break;
		case SetProcParameterRequest:
			choice = new SML_SetProcParameterReq();
			break;
		case GetListRequest:
			choice = new SML_GetListReq();
			break;
		case GetListResponse:
			choice = new SML_GetListRes();
			break;
		case AttentionResponse:
			choice = new SML_AttentionRes();
			break;
		default:
			return false;
		}

		if (!choice.decode(is)) {
			return false;
		}

		isSelected = true;

		return true;
	}

	public Unsigned32 getTag() {
		return tag;
	}

	public ASNObject getChoice() {
		return choice;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("-- SML_MessageBody begin: --");
			choice.print();
			System.out.println("-- SML_MessageBody end: --");
		}
	}
}
