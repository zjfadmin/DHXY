package org.come.model;

import java.math.BigDecimal;

import org.come.entity.RoleSummoning;
import org.come.tool.SplitStringTool;

//TODO 兑换召唤兽
public class PetExchange {

	private int eId;//兑换id
	private String type;//分类
	private String consume;//消耗
	private BigDecimal pid;//召唤兽id
	private String value;//随机召唤兽ID 193-194|17181006-81007$90&547-555$10
	private String skin;//皮肤
	
	public void initPet(RoleSummoning pet){
		skin=pet.getSummoningskin();
	}

	public BigDecimal getPetId() {

		return SplitStringTool.GoodRandomId(value);
	}

	public int geteId() {
		return eId;
	}

	public void seteId(int eId) {
		this.eId = eId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConsume() {
		return consume;
	}

	public void setConsume(String consume) {
		this.consume = consume;
	}

	public BigDecimal getPid() {
		return pid;
	}

	public void setPid(BigDecimal pid) {
		this.pid = pid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
}
