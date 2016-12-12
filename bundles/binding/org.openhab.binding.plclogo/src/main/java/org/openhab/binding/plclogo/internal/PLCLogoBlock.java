/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

public class PLCLogoBlock {

	public enum Kind { I, Q, M, AI, AQ, AM, NI, NAI, NQ, NAQ, VB, VW };

	private final Kind kind;
	private final int base;

	PLCLogoBlock(Kind kind, int base)
	{
		this.kind = kind;
		this.base = base;
	};

	public Kind getKind()
	{
		return kind;
	}

	public boolean isBitwise()
	{
		switch (kind)
		{
		case I:
		case Q:
		case M:
		case NI:
		case NQ:
			return true;
		}
		return false;
	}

	public boolean isAnalog()
	{
		switch (kind)
		{
		case AI:
		case AQ:
		case AM:
		case NAI:
		case NAQ:
			return true;
		}
		return false;
	}

	public boolean isGeneral()
	{
		switch (kind)
		{
		case VB:
		case VW:
			return true;
		}
		return false;
	}

	public boolean isInput()
	{
		switch (kind)
		{
		case I:
		case AI:
		case NI:
		case NAI:
			return true;
		}
		return false;
	}

	public int getAddress(int idx)
	{
		if (isBitwise())
			return base + idx / 8;
		else
		if (isAnalog())
			return base + idx * 2;
		else
			return idx;
	}

	public int getBit(int idx, int bit)
	{
		if (isBitwise())
			return idx % 8;
		else
		if (isAnalog())
			return -1; // Should not happen
		else
			return bit;
	}

};
