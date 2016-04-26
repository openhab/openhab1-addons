package org.openhab.core.persistence;

public class HistoricItemRange {
	private HistoricItem begin;
	private HistoricItem end;
	
	public HistoricItemRange() {
		super();
	}

	public HistoricItemRange(HistoricItem begin, HistoricItem end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public HistoricItem getBegin() {
		return begin;
	}
	
	public void setBegin(HistoricItem begin) {
		this.begin = begin;
	}
	
	public HistoricItem getEnd() {
		return end;
	}
	
	public void setEnd(HistoricItem end) {
		this.end = end;
	}
	
	public boolean foundBegin() {
		return this.begin != null;
	}
	
	public boolean foundEnd() {
		return this.end != null;
	}
}
