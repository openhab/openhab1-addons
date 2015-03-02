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

public class SML_GetProfilePackReq extends Sequence {

	protected OctetString serverId; // OPTIONAL,
	protected OctetString username; // OPTIONAL,
	protected OctetString password; // OPTIONAL,
	protected SML_Boolean withRawdata; // OPTIONAL,
	protected SML_Time beginTime; // OPTIONAL,
	protected SML_Time endTime; // OPTIONAL,
	protected SML_TreePath parameterTreePath;
	protected List_of_SML_ObjReqEntry object_List; // OPTIONAL,
	protected SML_Tree dasDetails; // OPTIONAL

	public OctetString getServerId() {
		return serverId;
	}

	public OctetString getUsername() {
		return username;
	}

	public OctetString getPassword() {
		return password;
	}

	public SML_Boolean getWithRawdata() {
		return withRawdata;
	}

	public SML_Time getBeginTime() {
		return beginTime;
	}

	public SML_Time getEndTime() {
		return endTime;
	}

	public SML_TreePath getParameterTreePath() {
		return parameterTreePath;
	}

	public List_of_SML_ObjReqEntry getObject_List() {
		return object_List;
	}

	public SML_Tree getDasDetails() {
		return dasDetails;
	}

	/**
	 * 
	 * @param serverId
	 *            OPTIONAL
	 * @param username
	 *            OPTIONAL
	 * @param password
	 *            OPTIONAL
	 * @param withRawdata
	 *            OPTIONAL
	 * @param beginTime
	 *            OPTIONAL
	 * @param endTime
	 *            OPTIONAL
	 * @param parameterTreePath
	 * @param objectList
	 *            OPTIONAL
	 * @param dasDetails
	 *            OPTIONAL
	 */
	public SML_GetProfilePackReq(OctetString serverId, OctetString username, OctetString password,
			SML_Boolean withRawdata, SML_Time beginTime, SML_Time endTime, SML_TreePath parameterTreePath,
			List_of_SML_ObjReqEntry objectList, SML_Tree dasDetails) {

		if (parameterTreePath == null) {
			throw new IllegalArgumentException(
					"SML_GetProfilePackReq: parameterTreePath is not optional and must not be null!");
		}

		this.serverId = serverId;
		this.username = username;
		this.password = password;
		this.withRawdata = withRawdata;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.parameterTreePath = parameterTreePath;
		object_List = objectList;
		this.dasDetails = dasDetails;

		if (this.serverId == null) {
			this.serverId = new OctetString();
		}
		if (this.username == null) {
			this.username = new OctetString();
		}
		if (this.password == null) {
			this.password = new OctetString();
		}
		if (this.withRawdata == null) {
			this.withRawdata = new SML_Boolean();
		}
		if (this.beginTime == null) {
			this.beginTime = new SML_Time();
		}
		if (this.endTime == null) {
			this.endTime = new SML_Time();
		}
		if (object_List == null) {
			object_List = new List_of_SML_ObjReqEntry();
		}
		if (this.dasDetails == null) {
			this.dasDetails = new SML_Tree();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetProfilePackReq() {
	}

	public void setOptionalAndSeq() {

		serverId.setOptional();
		username.setOptional();
		password.setOptional();
		withRawdata.setOptional();
		beginTime.setOptional();
		endTime.setOptional();
		object_List.setOptional();
		dasDetails.setOptional();

		seqArray = new ASNObject[] { serverId, username, password, withRawdata, beginTime, endTime, parameterTreePath,
				object_List, dasDetails };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		username = new OctetString();
		password = new OctetString();
		withRawdata = new SML_Boolean();
		beginTime = new SML_Time();
		endTime = new SML_Time();
		parameterTreePath = new SML_TreePath();
		object_List = new List_of_SML_ObjReqEntry();
		dasDetails = new SML_Tree();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetProfileListReq: ");
			super.print();
		}
	}
}
