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

public class SML_PublicCloseReq extends Sequence {
	protected OctetString globalSignature;

	public OctetString getGlobalSignature() {
		return globalSignature;
	}

	public SML_PublicCloseReq(OctetString globalSignature) {
		if (globalSignature != null) {
			this.globalSignature = globalSignature;
		}
		else {
			this.globalSignature = new OctetString();
		}

		setOptionalAndSeq();

		isSelected = true;
	}

	public SML_PublicCloseReq() {
	}

	public void setOptionalAndSeq() {
		globalSignature.setOptional();

		seqArray = new ASNObject[] { globalSignature };

	}

	@Override
	protected void createElements() {
		globalSignature = new OctetString();

		setOptionalAndSeq();
	}

}
