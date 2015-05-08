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

public class SML_AttentionRes extends Sequence {

	public static final OctetString OK = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81, (byte) 0xC7,
			(byte) 0xC7, (byte) 0xFD, (byte) 0x00 });
	public static final OctetString UNKNOWN_SML_DESIGNATOR = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x01 });
	public static final OctetString INADEQUATE_AUTHENTICATION = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x02 });
	public static final OctetString SERVERID_NOT_AVAILABLE = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x03 });
	public static final OctetString REQFILEID_NOT_AVAILABLE = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x04 });
	public static final OctetString ONE_OR_MORE_DESTITNATION_ATTRIBUTES_CANNOT_BE_WRITTEN = new OctetString(new byte[] {
			(byte) 0x81, (byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x05 });
	public static final OctetString ONE_OR_MORE_DESTITNATION_ATTRIBUTES_CANNOT_BE_READ = new OctetString(new byte[] {
			(byte) 0x81, (byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x06 });
	public static final OctetString COMMUNICATION_WITH_MEASURING_POINT_DISTURBED = new OctetString(new byte[] {
			(byte) 0x81, (byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x07 });
	public static final OctetString RAW_DATA_CANNOT_BE_INTERPRETED = new OctetString(new byte[] { (byte) 0x81,
			(byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x08 });
	public static final OctetString VALUE_OUT_OF_BOUNDS = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x09 });
	public static final OctetString ORDER_NOT_EXECUTED = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0A });
	public static final OctetString CHECKSUM_FAULTY = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0B });
	public static final OctetString BROADCAST_NOT_SUPPORTED = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0C });
	public static final OctetString UNEXPECTED_SMLMESSAGE = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0D });
	public static final OctetString UNKNOWN_OBJECT_IN_THE_LOAD_PROFILE = new OctetString(new byte[] { (byte) 0x81,
			(byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0E });
	public static final OctetString DATA_TYPE_NOT_SUPPORTED = new OctetString(new byte[] { (byte) 0x81, (byte) 0x81,
			(byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x0F });
	public static final OctetString OPTIONAL_ELEMENT_NOT_SUPPORTED = new OctetString(new byte[] { (byte) 0x81,
			(byte) 0x81, (byte) 0xC7, (byte) 0xC7, (byte) 0xFE, (byte) 0x10 });

	OctetString serverId;
	OctetString attentionNo;
	OctetString attentionMsg; // OPTIONAL
	SML_Tree attentionDetails; // OPTIONAL

	public OctetString getServerId() {
		return serverId;
	}

	public OctetString getAttentionNo() {
		return attentionNo;
	}

	public OctetString getAttentionMsg() {
		return attentionMsg;
	}

	public SML_Tree getAttentionDetails() {
		return attentionDetails;
	}

	public SML_AttentionRes(OctetString serverId, OctetString attentionNo, OctetString attentionMsg,
			SML_Tree attentionDetails) {

		if (serverId == null) {
			throw new IllegalArgumentException("SML_AttentionRes: serverId is not optional and must not be null!");
		}
		if (attentionNo == null) {
			throw new IllegalArgumentException("SML_AttentionRes: attentionNo is not optional and must not be null!");
		}

		this.serverId = serverId;
		this.attentionNo = attentionNo;
		this.attentionMsg = attentionMsg;
		this.attentionDetails = attentionDetails;

		if (this.attentionMsg == null) {
			this.attentionMsg = new OctetString();
		}
		if (this.attentionDetails == null) {
			this.attentionDetails = new SML_Tree();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_AttentionRes() {
	}

	public void setOptionalAndSeq() {
		attentionMsg.setOptional();
		attentionDetails.setOptional();

		seqArray = new ASNObject[] { serverId, attentionNo, attentionMsg, attentionDetails };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		attentionNo = new OctetString();
		attentionMsg = new OctetString();
		attentionDetails = new SML_Tree();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_AttentionRes: ");
			super.print();
		}
	}
}
