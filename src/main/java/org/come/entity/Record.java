package org.come.entity;

import java.math.BigDecimal;

public class Record {
	private BigDecimal recordId;
	private int recordType;
	/**交易id*/
	private BigDecimal transId;
	private String text;
	private String recordTime;
	private String atId;

	public Record() {
	}

	public Record(int recordType, String text) {
		super();
		this.recordType = recordType;
		this.text = text;
	}

	public Record(Integer recordType, BigDecimal transId,String text) {
		this.recordType = recordType;
		this.transId = transId;
		this.text = text;
	}

	public BigDecimal getRecordId() {
		return this.recordId;
	}

	public void setRecordId(BigDecimal recordId) {
		this.recordId = recordId;
	}

	public int getRecordType() {
		return this.recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}
	public BigDecimal getTransId() {
		return transId;
	}
	public void setTransId(BigDecimal transId) {
		this.transId = transId;
	}
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getAtId() {
		return atId;
	}

	public void setAtId(String atId) {
		this.atId = atId;
	}
}