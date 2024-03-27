package org.come.model;

import org.come.entity.Goodstable;
import org.come.entity.RoleSummoning;

import java.math.BigDecimal;

/**
 * 物品兑换 翼杰
 */
public class ItemExchange {

	private int eId;//兑换id
	private String type;//分类
	private String consume;//消耗
	private BigDecimal itemId;//物品id
	private String itemName;//物品名称
	private String instruction;//说明
	private String value;//属性值
	private String skin;//皮肤

	public void initPet(Goodstable goodstable){
		itemName=goodstable.getGoodsname();
		skin=goodstable.getSkin();
		value=goodstable.getValue();
		instruction=goodstable.getInstruction();
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

	public BigDecimal getItemId() {
		return itemId;
	}

	public void setItemId(BigDecimal itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
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
