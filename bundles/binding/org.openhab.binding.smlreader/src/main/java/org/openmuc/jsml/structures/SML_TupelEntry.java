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

public class SML_TupelEntry extends Sequence {

	protected OctetString serverId;
	protected SML_Time secIndex;
	protected Unsigned64 status;
	protected SML_Unit unit_pA;
	protected Integer8 scaler_pA;
	protected Integer64 value_pA;
	protected SML_Unit unit_R1;
	protected Integer8 scaler_R1;
	protected Integer64 value_R1;
	protected SML_Unit unit_R4;
	protected Integer8 scaler_R4;
	protected Integer64 value_R4;
	protected OctetString signature_pA_R1_R4;
	protected SML_Unit unit_mA;
	protected Integer8 scaler_mA;
	protected Integer64 value_mA;
	protected SML_Unit unit_R2;
	protected Integer8 scaler_R2;
	protected Integer64 value_R2;
	protected SML_Unit unit_R3;
	protected Integer8 scaler_R3;
	protected Integer64 value_R3;
	protected OctetString signature_mA_R2_R3;

	public OctetString getServerId() {
		return serverId;
	}

	public SML_Time getSecIndex() {
		return secIndex;
	}

	public Unsigned64 getStatus() {
		return status;
	}

	public SML_Unit getUnit_pA() {
		return unit_pA;
	}

	public Integer8 getScaler_pA() {
		return scaler_pA;
	}

	public Integer64 getValue_pA() {
		return value_pA;
	}

	public SML_Unit getUnit_R1() {
		return unit_R1;
	}

	public Integer8 getScaler_R1() {
		return scaler_R1;
	}

	public Integer64 getValue_R1() {
		return value_R1;
	}

	public SML_Unit getUnit_R4() {
		return unit_R4;
	}

	public Integer8 getScaler_R4() {
		return scaler_R4;
	}

	public Integer64 getValue_R4() {
		return value_R4;
	}

	public OctetString getSignature_pA_R1_R4() {
		return signature_pA_R1_R4;
	}

	public SML_Unit getUnit_mA() {
		return unit_mA;
	}

	public Integer8 getScaler_mA() {
		return scaler_mA;
	}

	public Integer64 getValue_mA() {
		return value_mA;
	}

	public SML_Unit getUnit_R2() {
		return unit_R2;
	}

	public Integer8 getScaler_R2() {
		return scaler_R2;
	}

	public Integer64 getValue_R2() {
		return value_R2;
	}

	public SML_Unit getUnit_R3() {
		return unit_R3;
	}

	public Integer8 getScaler_R3() {
		return scaler_R3;
	}

	public Integer64 getValue_R3() {
		return value_R3;
	}

	public OctetString getSignature_mA_R2_R3() {
		return signature_mA_R2_R3;
	}

	/**
	 * 
	 * @param serverId
	 * @param secIndex
	 * @param status
	 * @param unitPA
	 * @param scalerPA
	 * @param valuePA
	 * @param unitR1
	 * @param scalerR1
	 * @param valueR1
	 * @param unitR4
	 * @param scalerR4
	 * @param valueR4
	 * @param signaturePAR1R4
	 * @param unitMA
	 * @param scalerMA
	 * @param valueMA
	 * @param unitR2
	 * @param scalerR2
	 * @param valueR2
	 * @param unitR3
	 * @param scalerR3
	 * @param valueR3
	 * @param signatureMAR2R3
	 */
	public SML_TupelEntry(OctetString serverId, SML_Time secIndex, Unsigned64 status, SML_Unit unitPA,
			Integer8 scalerPA, Integer64 valuePA, SML_Unit unitR1, Integer8 scalerR1, Integer64 valueR1,
			SML_Unit unitR4, Integer8 scalerR4, Integer64 valueR4, OctetString signaturePAR1R4, SML_Unit unitMA,
			Integer8 scalerMA, Integer64 valueMA, SML_Unit unitR2, Integer8 scalerR2, Integer64 valueR2,
			SML_Unit unitR3, Integer8 scalerR3, Integer64 valueR3, OctetString signatureMAR2R3) {

		this.serverId = serverId;
		this.secIndex = secIndex;
		this.status = status;
		unit_pA = unitPA;
		scaler_pA = scalerPA;
		value_pA = valuePA;
		unit_R1 = unitR1;
		scaler_R1 = scalerR1;
		value_R1 = valueR1;
		unit_R4 = unitR4;
		scaler_R4 = scalerR4;
		value_R4 = valueR4;
		signature_pA_R1_R4 = signaturePAR1R4;
		unit_mA = unitMA;
		scaler_mA = scalerMA;
		value_mA = valueMA;
		unit_R2 = unitR2;
		scaler_R2 = scalerR2;
		value_R2 = valueR2;
		unit_R3 = unitR3;
		scaler_R3 = scalerR3;
		value_R3 = valueR3;
		signature_mA_R2_R3 = signatureMAR2R3;

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_TupelEntry() {
	}

	public void setOptionalAndSeq() {
		seqArray = new ASNObject[] { serverId, secIndex, status, unit_pA, scaler_pA, value_pA, unit_R1, scaler_R1,
				value_R1, unit_R4, scaler_R4, value_R4, signature_pA_R1_R4, unit_mA, scaler_mA, value_mA, unit_R2,
				scaler_R2, value_R2, unit_R3, scaler_R3, value_R3, signature_mA_R2_R3 };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		secIndex = new SML_Time();
		status = new Unsigned64();
		unit_pA = new SML_Unit();
		scaler_pA = new Integer8();
		value_pA = new Integer64();
		unit_R1 = new SML_Unit();
		scaler_R1 = new Integer8();
		value_R1 = new Integer64();
		unit_R4 = new SML_Unit();
		scaler_R4 = new Integer8();
		value_R4 = new Integer64();
		signature_pA_R1_R4 = new OctetString();
		unit_mA = new SML_Unit();
		scaler_mA = new Integer8();
		value_mA = new Integer64();
		unit_R2 = new SML_Unit();
		scaler_R2 = new Integer8();
		value_R2 = new Integer64();
		unit_R3 = new SML_Unit();
		scaler_R3 = new Integer8();
		value_R3 = new Integer64();
		signature_mA_R2_R3 = new OctetString();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_TupelEntry: ");
			super.print();
		}
	}
}
